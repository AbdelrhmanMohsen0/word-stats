package com.future.wordstats.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class FileExplorerRunnable implements Runnable {

    private final ConcurrentLinkedQueue<FileResult> fileResultsQueue;
    private final AtomicReference<String> longestWord;
    private final AtomicReference<String> shortestWord;
    private final AtomicBoolean stopped;

    public FileExplorerRunnable(ConcurrentLinkedQueue<FileResult> fileResultsQueue,
            AtomicReference<String> longestWord,
            AtomicReference<String> shortestWord,
            AtomicBoolean stopped) {
        this.fileResultsQueue = fileResultsQueue;
        this.longestWord = longestWord;
        this.shortestWord = shortestWord;
        this.stopped = stopped;
    }

    @Override
    public void run() {
        FileResult fileResult;
        while (!stopped.get() && (fileResult = fileResultsQueue.poll()) != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileResult.getFilePath()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    updateFileResult(fileResult, line);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateFileResult(FileResult fileResult, String line) {
        String[] words = line.split(" ");
        for (String word : words) {
            fileResult.incrementNumOfWords();
            String nword = word.replaceAll("[^a-zA-Z-—]+", "");

            switch (nword) {
                case "is" -> fileResult.incrementNumOfIsWords();
                case "are" -> fileResult.incrementNumOfAreWords();
                case "you" -> fileResult.incrementNumOfYouWords();
            }

            if (nword.trim().isEmpty()) {
                return;
            }

            fileResult.compareAndSetIfLonger(nword);
            fileResult.compareAndSetIfShorter(nword);

            longestWord.updateAndGet(current -> nword.length() >= current.length() ? nword : current);
            shortestWord.updateAndGet(current -> {
                if (current.isEmpty())
                    return nword;
                return nword.length() <= current.length() ? nword : current;
            });
        }
    }

}
