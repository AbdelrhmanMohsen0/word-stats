package com.future.wordstats.controller;

import com.future.wordstats.core.DirectoryExplorer;
import com.future.wordstats.core.FileResult;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class MainController implements Initializable {
    public TextField dirPathTextField;
    public Button browseButton;
    public CheckBox subDirectoriesCheckBox;
    public VBox fileResultsContainer;
    public StackPane noFilesPane;
    public Label errorLabel;
    public Label longestWordLabel;
    public Label shortestWordLabel;
    public Button startButton;
    public Button resetButton;

    private final DirectoryExplorer directoryExplorer;
    private final AtomicReference<String> longestWord;
    private final AtomicReference<String> shortestWord;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        browseButton.setOnAction(event -> onBrowse());
        startButton.setOnAction(event -> onStart());
        resetButton.setOnAction(event -> onReset());
        dirPathTextField.setOnKeyPressed(event -> errorLabel.setVisible(false));
    }

    public MainController() {
        this.directoryExplorer = new DirectoryExplorer();
        this.longestWord = this.directoryExplorer.getLongestWord();
        this.shortestWord = this.directoryExplorer.getShortestWord();
    }

    private void onBrowse() {
        errorLabel.setVisible(false);
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");

        File selectedDirectory = directoryChooser.showDialog(dirPathTextField.getScene().getWindow());

        if (selectedDirectory != null) {
            dirPathTextField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private void onStart() {
        if (!isDirectoryExists(dirPathTextField.getText())) {
            errorLabel.setVisible(true);
            return;
        }
        String dirPath = dirPathTextField.getText();
        boolean recursive = subDirectoriesCheckBox.isSelected();
        List<FileResult> fileResults = directoryExplorer.explore(dirPath, recursive);
        renderFileResults(fileResults);
        renderShortestLongest();
    }

    private void onReset() {
        directoryExplorer.stop();
        startButton.setDisable(false);
        browseButton.setDisable(false);
        resetButton.setDisable(true);
        fileResultsContainer.getChildren().clear();
        noFilesPane.setVisible(true);
        noFilesPane.setManaged(true);

    }

    private boolean isDirectoryExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    private void renderFileResults(List<FileResult> fileResults) {
        fileResultsContainer.getChildren().clear();
        if (fileResults.isEmpty()) {
            noFilesPane.setVisible(true);
            noFilesPane.setManaged(true);
            return;
        }
        browseButton.setDisable(true);
        noFilesPane.setVisible(false);
        noFilesPane.setManaged(false);
        startButton.setDisable(true);
        resetButton.setDisable(false);
        for (FileResult fileResult : fileResults) {
            fileResultsContainer.getChildren().add(createFileResultCell(fileResult));
        }
    }

    private Node createFileResultCell(FileResult fileResult) {
        AnchorPane cell;
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/view/file-result-cell-view.fxml"));
            cell = loader.load();

            FileResultCellController controller = loader.getController();
            controller.setFileResultCell(fileResult, directoryExplorer.getStopped());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return cell;
    }

    private void renderShortestLongest() {
        AnimationTimer shortestLongestUpdater = new AnimationTimer() {
            @Override
            public void handle(long l) {
                longestWordLabel.setText(longestWord.get());
                shortestWordLabel.setText(shortestWord.get());
            }
        };
        shortestLongestUpdater.start();
    }



}
