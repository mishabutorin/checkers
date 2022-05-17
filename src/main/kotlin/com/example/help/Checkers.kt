package com.example.help

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class Checkers : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(Checkers::class.java.getResource("StartWindow.fxml"))
        val scene = Scene(fxmlLoader.load())
        stage.title = "help"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(Checkers::class.java)
}