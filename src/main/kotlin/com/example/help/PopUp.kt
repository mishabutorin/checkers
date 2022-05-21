package com.example.help

import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage

object PopUp {
    private var chosen: Any? = null

    //METHODS
    fun <T> presentOptions(options: Array<T>, horizontal: Boolean): T {
        chosen = null
        val stage = Stage()
        stage.initModality(Modality.APPLICATION_MODAL)
        val layout = if (horizontal) HBox() else VBox()
        for (option in options) {
            val button = Button(option.toString())
            button.styleClass.add("popUpChoice")
            button.onAction = EventHandler {
                chosen = option
                stage.close()
            }
            layout.children.add(button)
        }
        val scene = Scene(layout)
        scene.stylesheets.add("myStyle.css")
        stage.scene = scene
        stage.showAndWait()
        return if (chosen == null)
            options[0]
        else chosen as T
    }

    fun presentMessage(message: String) {
        val box = HBox(Label(message))
        box.styleClass.add("popUpMessage")
        val scene = Scene(box)
        scene.stylesheets.add("myStyle.css")
        val stage = Stage()
        stage.scene = scene
        //окно закрывается при нажатии на любое место
        stage.focusedProperty()
            .addListener { _: ObservableValue<out Boolean>, _: Boolean, newIsFocusedValue: Boolean
                -> if (!newIsFocusedValue) stage.close() }
        stage.show()
    }
}
