package com.example.help.checkers.pieces

import com.example.help.Colour
import com.example.help.GameState
import com.example.help.Move
import com.example.help.Position
import com.example.help.checkers.CheckerPiece
import com.example.help.checkers.CheckersState

class Man  //CONSTRUCTOR
    (colour: Colour) : CheckerPiece(colour, "○", "●", 1) {
    //METHODS
    override fun isMoveLegal(gameState: GameState, move: Move): Boolean {
        return super.isMoveLegal(gameState, move)
                && ((getColour() == Colour.LIGHT && move.isSouth())
                || getColour() == Colour.DARK && move.isNorth())
    }

    override fun makeMove(gameState: GameState, move: Move) {
        super.makeMove(gameState, move)
        val promotionY = if (getColour() == Colour.LIGHT) 0 else 7
        if (move.getY2() == promotionY)
                (gameState as CheckersState).setToPromote(move.getDestination())
    }

    fun promote(gameState: GameState, position: Position) {
        gameState.setPiece(position, King(getColour()))
    }
}

