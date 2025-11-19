module com.future.wordstats {
    requires javafx.fxml;
    requires java.compiler;
    requires atlantafx.base;
    requires java.logging;

    opens com.future.wordstats to javafx.fxml;
    exports com.future.wordstats;
    exports com.future.wordstats.core;
    exports com.future.wordstats.controller;
}