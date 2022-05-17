package com.example.help

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.stage.Stage

class StartController {
    @FXML
    private lateinit var exitButton: Button

    @FXML
    private lateinit var startButton: Button

    @FXML
    fun initialize() {
        startButton.setOnAction {
            startButton.scene.window.hide()
            val stage = Stage()
            val fxmlLoader = FXMLLoader(Checkers::class.java.getResource("MainWindow.fxml"))
            val scene = Scene(fxmlLoader.load())
            stage.scene = scene
            stage.showAndWait()
        }
        exitButton.setOnAction { Platform.exit() }
    }
}
