package com.example.help.boards

import com.example.help.*
import javafx.animation.Animation
import javafx.animation.Interpolator
import javafx.animation.Transition
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.effect.InnerShadow
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.util.Duration
import java.util.*

abstract class GUIBoard(width: Int, height: Int, controller: MainController) : GridPane(), UserInterface {
    private var controller: MainController
    private var width: Int
    private var height: Int
    protected var pieceImages = HashMap<String, String>() //<symbol, style class> (style class has imaged url)
    private var positions: Queue<Position> = ArrayDeque()

    //CONSTRUCTOR
    init {
        styleClass.setAll("board")
        this.width = width
        this.height = height
        this.controller = controller
        for (y in 0 until height) {
            for (x in 0 until width) {
                add(createSquare(x, y), x, height - y)
            }
        }
    }

    //METHODS
    private fun createSquare(x: Int, y: Int): Button {
        val square = Button()
        square.isPickOnBounds = false //The pick bounds 'wander' upwards is this isn't set. Don't know why though?
        square.styleClass.setAll("square", "greenFocusHighlight")
        square.styleClass.add(if ((x + y) % 2 == 1) "lightSquare" else "darkSquare")
        square.onAction = EventHandler {
            if (!isSelectionValid(x, y)) requestFocus() //remove green highlight
            else {
                positions.add(Position(x, y))
                if (positions.size == 2) {
                    requestFocus() //remove green highlight
                    val move = Move(positions.remove(), positions.remove(), this)
                    controller.getGame().makeMove(move)
                    repaint()
                    Platform.runLater {
                        //if next player turn is the computer, call the AI.
                        if (controller.currentPlayerIsAI())
                            controller.makeAIMove()
                    }
                }
            }
        }
        return square
    }

    fun repaint() {
        Platform.runLater {
            for (x in 0 until width) {
                for (y in 0 until height) {
                    val piece: Piece? = controller.getGame().getCurrentGameState()?.getPiece(Position(x, y))
                    val square = getSquareAt(x, y)
                    if (pieceImages.containsKey(piece.toString())) {
                        square.styleClass.removeAll(pieceImages.values)
                        square.styleClass.add(pieceImages[piece.toString()])
                    } else { //else just use the internal symbol, ie. ♝, ◔, etc
                        square.text = piece.toString()
                    }
                }
            }
            highlightLastMove()
        }
    }

    private fun promptPlayer(message: String) {
        Platform.runLater { PopUp.presentMessage(message) }
    }

    override fun notifyGameOver(winner: Colour) {
        promptPlayer("Game Over: " + winner.toString().lowercase(Locale.getDefault()) + " wins.")
    }

    private fun highlightLastMove() {
        val move: Move? = controller.getGame().getCurrentGameState()?.getLastMove()
        if (move != null) {
            flashColour(getSquareAt(move.getX1(), move.getY1()))
        }
        if (move != null) {
            flashColour(getSquareAt(move.getX2(), move.getY2()))
        }
    }

    private fun flashColour(button: Button) {
        //Thanks to Stack Overflow user negate, for help with this code: https://stackoverflow.com/a/41672541
        val s = InnerShadow()
        s.width = 250.0
        button.effect = s
        val animation: Animation = object : Transition() {
            init {
                cycleDuration = Duration.millis(1000.0)
                interpolator = Interpolator.EASE_OUT
            }

            override fun interpolate(frag: Double) {
                s.color = Color.GREEN.deriveColor(0.0, 255.0, 255.0, 1 - frag)
            }
        }
        animation.play()
        animation.onFinished = EventHandler {
            button.effect = null
        }
    }

    private fun isSelectionValid(x: Int, y: Int): Boolean {
        //Simple validation check.
        val gameState: GameState = controller.getGame().getCurrentGameState()!!
        val chosen: Piece? = gameState.getPiece(Position(x, y))
        //The first selection, position.size = 0, can't be an empty square nor enemy piece.
        //The second selection, position.size = 1, can be whatever.
        return (chosen?.getColour() != gameState.getPlayerTurn().opponent()
                    || positions.size == 1)
    }

    //GETTERS
    fun getSquareAt(x: Int, y: Int): Button {
        return children[y * 8 + x] as Button
    }
}
