package com.example.help

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

class Checkers : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(Checkers::class.java.getResource("StartWindow.fxml"))
        val scene = Scene(fxmlLoader.load())
        stage.title = "checkers"
        stage.icons.add(Image("C:\\Users\\esyre\\IdeaProjects\\help\\src\\main\\resources\\assets\\gameicon.png"))
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(Checkers::class.java)
}