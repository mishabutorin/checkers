package com.example.help.boards

import com.example.help.MainController

class GUICheckers(controller: MainController) : GUIBoard(8, 8, controller) {
    init {
        stylesheets.add("@checkers.css")
        pieceImages["○"] = "lightMan"
        pieceImages["●"] = "darkMan"
        pieceImages["◔"] = "lightKing"
        pieceImages["◕"] = "darkKing"

        //disable all dark squares
        for (x in 0 until width.toInt()) {
            for (y in 0 until height.toInt()) {
                if ((x + y) % 2 == 0) {
                    getSquareAt(x, y).isDisable = true
                }
            }
        }
    }
}