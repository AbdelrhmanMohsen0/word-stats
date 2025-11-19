package com.future.wordstats.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class DirectoryExplorer {

    private final ConcurrentLinkedQueue<FileResult> fileResultsQueue = new ConcurrentLinkedQueue<>();
    private final List<FileResult> fileResults = new ArrayList<>();
    private final AtomicReference<String> longestWord = new AtomicReference<>("");
    private final AtomicReference<String> shortestWord = new AtomicReference<>("");
    private final AtomicBoolean stopped = new AtomicBoolean(true);
    private static final int NUM_OF_PROCESSOR_CORES = Runtime.getRuntime().availableProcessors();

    public List<FileResult> explore(String directoryPath, boolean recursive) {
        stopped.set(false);
        exploreDirectoryRecursively(directoryPath, recursive);
        exploreFiles();
        return fileResults;
    }

    private void exploreDirectoryRecursively(String directoryPath, boolean recursive) {
        File[] contents = new File(directoryPath).listFiles();

        if (contents == null) {
            return;
        }

        for (File file : contents) {
            if (recursive && file.isDirectory()) {
                exploreDirectoryRecursively(file.getAbsolutePath(), true);
            }
            else if (file.isFile() && file.getName().endsWith(".txt")) {
                FileResult fileResult = new FileResult(file.getAbsolutePath());
                fileResultsQueue.add(fileResult);
                fileResults.add(fileResult);
            }
        }
    }

    private void exploreFiles() {
        Thread[] threads = new Thread[NUM_OF_PROCESSOR_CORES];
        FileExplorerRunnable[] fileExplorers = new FileExplorerRunnable[NUM_OF_PROCESSOR_CORES];

        for (int i = 0; i < NUM_OF_PROCESSOR_CORES; i++) {
            fileExplorers[i] = new FileExplorerRunnable(fileResultsQueue, longestWord, shortestWord, stopped);
            threads[i] = new Thread(fileExplorers[i]);
            threads[i].start();
        }
    }

    public void stop() {
        stopped.set(true);
        fileResultsQueue.clear();
        fileResults.clear();
        longestWord.set("");
        shortestWord.set("");
    }

    public AtomicReference<String> getLongestWord() {
        return longestWord;
    }

    public AtomicReference<String> getShortestWord() {
        return shortestWord;
    }

    public AtomicBoolean getStopped() {
        return stopped;
    }

}
