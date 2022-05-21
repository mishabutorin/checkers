package com.example.help.boards

import com.example.help.MainController

class GUICheckers(controller: MainController) : GUIBoard(8, 8, controller) {
    init {
        stylesheets.add("/checkers.css") //добавление изображений
        pieceImages["○"] = "lightMan"
        pieceImages["●"] = "darkMan"
        pieceImages["◔"] = "lightKing"
        pieceImages["◕"] = "darkKing"


        //откллючение тёмных квадратов поля
        for (x in 0 until width) {
            for (y in 0 until height) {
                if ((x + y) % 2 == 0) {
                    getSquareAt(x, y).isDisable = true
                }
            }
        }
    }
}