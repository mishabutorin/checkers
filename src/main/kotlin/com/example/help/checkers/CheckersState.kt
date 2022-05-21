package com.example.help.checkers

import com.example.help.*
import com.example.help.checkers.pieces.*
import java.util.*

class CheckersState constructor(
    arrangement: String = "a-b-c-d-e-f-g-h\n" +
            "8|○| |○| |○| |○| |8\n" +
            "7| |○| |○| |○| |○|7\n" +
            "6|○| |○| |○| |○| |6\n" +
            "5| | | | | | | | |5\n" +
            "4| | | | | | | | |4\n" +
            "3| |●| |●| |●| |●|3\n" +
            "2|●| |●| |●| |●| |2\n" +
            "1| |●| |●| |●| |●|1\n" +
            "a-b-c-d-e-f-g-h"
) : GameState(
    8, 8, arrangement, arrayOf(
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
        return super.isMoveLegal(move) //должен сделать прыжок, если он доступен.
                && (move.distance() == 2
                || !playerHasJump(getPlayerTurn()))
    }

    override fun makeMove(move: Move) {
        super.makeMove(move)
        //если последний ход не был прыжком, или он был, но для прыгнувшей фигуры больше нет доступных прыжков...
        if (move.distance() == 1 || !hasJumps(move.getDestination())) {
            endPlayerTurn()
            while (!capturedPieces.isEmpty())
                setPiece(capturedPieces.pop(), null) //удаление шашки с поля
            if (toPromote != null) {
                val promoted = getPiece(toPromote!!) as Man
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
                if ((piece != null) && (piece.getColour() == playerColour) && hasJumps(piecePosition))
                    return true
            }
        }
        return false
    }

    private fun hasJumps(piecePosition: Position): Boolean {
        val piece: Piece? = getPiece(piecePosition)
        val xS = intArrayOf(piecePosition.getX() - 2, piecePosition.getX() + 2)
        val yS: IntArray = if (piece is King) intArrayOf(
            piecePosition.getY() - 2,
            piecePosition.getY() + 2
        ) else intArrayOf(if (piece?.getColour() == Colour.LIGHT) piecePosition.getY() - 2 else piecePosition.getY() + 2)
        for (x: Int in xS) {
            for (y: Int in yS) {
                if (isMoveLegal(Move(piecePosition, Position(x, y))))
                    return true
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
        return super.isTerminal() || getSuccessors().isEmpty()
    }

    override fun evaluate(maximizingPlayer: Colour): Double {
        return if (isTerminal())
            if (getPlayerTurn() != maximizingPlayer) 2000.0 else -2000.0
        else
            super.evaluate(maximizingPlayer) + if (getPlayerTurn() == maximizingPlayer) capturedPieces.size
            else -capturedPieces.size //сколь угодно большое число
//super.evaluation обеспечивает простой материальный баланс (союзники - враги), но в шашках
// некоторые фигуры на доске были захвачены, что делает его еще более выгодным для текущего игрока.
    }

    //GETTERS & SETTERS
    fun addCaptured(position: Position) {
        capturedPieces.add(position)
    }

    fun setToPromote(position: Position) {
        toPromote = position
    }
}
