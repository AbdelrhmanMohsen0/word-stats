package com.future.wordstats.core;

import java.io.File;

public class FileResult {

    private final String fileName;
    private final String filePath;

    private long numOfWords = 0;
    private long numOfIsWords = 0;
    private long numOfAreWords = 0;
    private long numOfYouWords = 0;
    private String longestWord = "";
    private String shortestWord = "";

    public FileResult(String filePath) {
        this.filePath = filePath;
        this.fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

    public synchronized void incrementNumOfWords() {
        numOfWords++;
    }

    public synchronized void incrementNumOfIsWords() {
        numOfIsWords++;
    }

    public synchronized void incrementNumOfAreWords() {
        numOfAreWords++;
    }

    public synchronized void incrementNumOfYouWords() {
        numOfYouWords++;
    }

    public synchronized void compareAndSetIfLonger(String word) {
        if (word.length() >= longestWord.length()) {
            longestWord = word;
        }
    }

    public synchronized void compareAndSetIfShorter(String word) {
        if (shortestWord.isEmpty()) {
            shortestWord = word;
            return;
        }
        if (word.length() <= shortestWord.length()) {
            shortestWord = word;
        }
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public synchronized long getNumOfWords() {
        return numOfWords;
    }

    public synchronized long getNumOfIsWords() {
        return numOfIsWords;
    }

    public synchronized long getNumOfAreWords() {
        return numOfAreWords;
    }

    public synchronized long getNumOfYouWords() {
        return numOfYouWords;
    }

    public synchronized String getLongestWord() {
        return longestWord;
    }

    public synchronized String getShortestWord() {
        return shortestWord;
    }
}
