package com.future.wordstats.controller;

import com.future.wordstats.core.FileResult;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;

import java.util.concurrent.atomic.AtomicBoolean;

public class FileResultCellController {

    public Label fileNameCell;
    public Label numWordsCell;
    public Label numIsCell;
    public Label numAreCell;
    public Label numYouCell;
    public Label longestCell;
    public Label shortestCell;

    private FileResult fileResult;

    public void setFileResultCell(FileResult fileResult, AtomicBoolean stopped) {
        this.fileResult = fileResult;
        updateCell();
        AnimationTimer cellUpdater = new AnimationTimer() {
            @Override
            public void handle(long l) {
                updateCell();
            }
        };
        cellUpdater.start();
    }

    public void updateCell() {
        fileNameCell.setText(fileResult.getFileName());
        numWordsCell.setText(String.valueOf(fileResult.getNumOfWords()));
        numIsCell.setText(String.valueOf(fileResult.getNumOfIsWords()));
        numAreCell.setText(String.valueOf(fileResult.getNumOfAreWords()));
        numYouCell.setText(String.valueOf(fileResult.getNumOfYouWords()));
        longestCell.setText(fileResult.getLongestWord());
        shortestCell.setText(fileResult.getShortestWord());
    }
}
