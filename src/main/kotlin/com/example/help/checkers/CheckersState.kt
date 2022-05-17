package com.example.help.checkers

import com.example.help.*
import com.example.help.checkers.pieces.King
import com.example.help.checkers.pieces.Man
import java.util.*

class CheckersState constructor(
    arrangement: String? = """
    a-b-c-d-e-f-g-h
    8|○| |○| |○| |○| |8
    7| |○| |○| |○| |○|7
    6|○| |○| |○| |○| |6
    5| | | | | | | | |5
    4| | | | | | | | |4
    3| |●| |●| |●| |●|3
    2|●| |●| |●| |●| |2
    1| |●| |●| |●| |●|1
    a-b-c-d-e-f-g-h
    """.trimIndent()
) : GameState(
    8, 8, arrangement.toString(), arrayOf(
        Man(Colour.LIGHT),
        Man(Colour.DARK),
        King(Colour.LIGHT),
        King(Colour.DARK)
    )
) {
    private var toPromote: Position? = null
    var capturedPieces: ArrayDeque<Position> = ArrayDeque<Position>()

    //CONSTRUCTORS
    init {
        setPlayerTurn(Colour.DARK)
    }

    //METHODS
    override fun isMoveLegal(move: Move): Boolean {
        return (super.isMoveLegal(move) //must make a jump move if one is available.
                && (move.distance() == 2
                || !playerHasJump(getPlayerTurn())))
    }

    override fun makeMove(move: Move) {
        super.makeMove(move)
        //if last move was not a jump, or it was but there are no more jumps available to the piece that jumped...
        if (move.distance() == 1 || !hasJumps(move.getDestination())) {
            endPlayerTurn()
            while (!capturedPieces.isEmpty()) setPiece(capturedPieces.pop(), null)
            if (toPromote != null) {
                val promoted: Man = getPiece(toPromote!!) as Man
                promoted.promote(this, toPromote!!)
                toPromote = null
            }
        }
    }

    override fun postMoveUINotifications(ui: UserInterface) {
        if (isTerminal()) ui.notifyGameOver(getPlayerTurn().opponent())
    }

    private fun playerHasJump(playerColour: Colour): Boolean {
        for (x in 0 until getBoardWidth()) {
            for (y in 0 until getBoardHeight()) {
                val piecePosition = Position(x, y)
                val piece: Piece? = getPiece(piecePosition)
                if ((piece != null) && (piece.getColour() == playerColour) && hasJumps(piecePosition)) return true
            }
        }
        return false
    }

    private fun hasJumps(piecePosition: Position): Boolean {
        val piece: Piece? = getPiece(piecePosition)
        val Xs = intArrayOf(piecePosition.getX() - 2, piecePosition.getX() + 2)
        val Ys: IntArray = if (piece is King) intArrayOf(
            piecePosition.getY() - 2,
            piecePosition.getY() + 2
        ) else intArrayOf(if (piece?.getColour() == Colour.LIGHT) piecePosition.getY() - 2 else piecePosition.getY() + 2)
        for (x in Xs) {
            for (y in Ys) {
                if (isMoveLegal(Move(piecePosition, Position(x, y)))) return true
            }
        }
        return false
    }

    override fun copy(): CheckersState {
        val copy = super.copy() as CheckersState
        copy.toPromote = toPromote
        copy.capturedPieces = capturedPieces.clone()
        return copy
    }

    override fun isTerminal(): Boolean {
        return (super.isTerminal()
                || getSuccessors().isEmpty())
    }

    override fun evaluate(maximizingPlayer: Colour?): Double {
        return if (isTerminal())
            if (getPlayerTurn() !== maximizingPlayer) 2000.0 else -2000.0 else super.evaluate(
            maximizingPlayer
        ) + if (getPlayerTurn() === maximizingPlayer) capturedPieces.size else -capturedPieces.size //arbitrarily large number
        //super.evaluation performs simple material balance (allies - enemies), but, in checkers,
        //some pieces on the board have been captured - making it even more favourable to the current player.
    }

    //GETTERS & SETTERS
    fun addCaptured(position: Position) {
        capturedPieces.add(position)
    }

    fun setToPromote(position: Position?) {
        toPromote = position
    }
}
