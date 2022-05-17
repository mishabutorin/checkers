package com.example.help.checkers

import com.example.help.*

abstract class CheckerPiece  //CONSTRUCTOR
protected constructor(colour: Colour, whiteSymbol: String, blackSymbol: String, value: Int) :
    Piece(colour, whiteSymbol, blackSymbol, value) {
    //METHODS
    override fun isMoveLegal(gameState: GameState, move: Move): Boolean {
        val between = gameState.getPiece(getBetweenSquare(move))
        return (move.isDiagonal()
                && (gameState.getPiece(move.getDestination()) == null)
                && ((move.distance() == 1)
                || ((move.distance() == 2) && (between != null)
                && (between.getColour() === getColour().opponent())))
                && !(gameState as CheckersState).capturedPieces.contains(
            getBetweenSquare(move)
        ))
    }


    override fun makeMove(gameState: GameState, move: Move) {
        gameState.setPiece(move.getDestination(), gameState.getPiece(move.getStart()))
        gameState.setPiece(move.getStart(), null)
        if (move.distance() == 2) (gameState as CheckersState).addCaptured(getBetweenSquare(move))
    }

    private fun getBetweenSquare(move: Move): Position {
        val x = (move.getX2() + move.getX1()) / 2
        val y = (move.getY2() + move.getY1()) / 2
        return Position(x, y)
    }
}
