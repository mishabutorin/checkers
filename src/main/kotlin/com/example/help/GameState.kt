package com.example.help

import kotlin.system.exitProcess

abstract class GameState constructor(boardWidth: Int = 8, boardHeight: Int = 8) {
    private var prev: GameState? = null
    private var future: GameState? = null
    private var lastMove: Move? = null
    private var playerTurn: Colour = Colour.LIGHT
    private val squares: Array<Array<Piece?>>

    //CONSTRUCTORS
    init {
        squares = Array(boardWidth) { arrayOfNulls(boardHeight) }
    }

    constructor(boardWidth: Int, boardHeight: Int, arrangement: String, pieces: Array<Piece>) : this(
        boardWidth,
        boardHeight
    ) {
        //создание map которая связывает фигуры и их символы
        val symbols = HashMap<String, Piece?>()
        for (piece in pieces) {
            symbols[piece.toString()] = piece
        }
        symbols[" "] = null //добавление пустых мест для пустых квадратов
        var pointer = 0
        //использование шахматной последовательности, чтобы правильно разместить фигуру на поле
        for (y in boardHeight - 1 downTo 0) {
            for (x in 0 until boardWidth) {
                var symbol = arrangement[pointer++].toString() + ""
                while (!symbols.containsKey(symbol)) symbol = arrangement[pointer++].toString() + ""
                setPiece(Position(x, y), symbols[symbol])
            }
        }
    }

    //METHODS
    open fun isMoveLegal(move: Move): Boolean {
        //находится ли ход в пределах доски?
        for (coordinate in intArrayOf(
            move.getX1(),
            move.getY1(),
            move.getX2(),
            move.getY2()
        )) if (coordinate < 0 || coordinate >= getBoardWidth()) return false
        val piece = getPiece(move.getStart())
        //принадлежит ли выбранная фигура текущему игроку и может ли она сделать запрошенный ход?
        return (piece != null)
                && (piece.getColour() == getPlayerTurn())
                && piece.isMoveLegal(this, move)
    }

    open fun makeMove(move: Move) {
        getPiece(move.getStart())?.makeMove(this, move)
        setLastMove(move)
    }

    abstract fun postMoveUINotifications(ui: UserInterface)

    open fun copy(): GameState {
        val copy: GameState
        try {
            copy = this.javaClass.getDeclaredConstructor().newInstance()
            copy.prev = prev
            copy.future = future
            copy.lastMove = lastMove
            copy.playerTurn = playerTurn
            for (x in squares.indices) {
                System.arraycopy(squares[x], 0, copy.squares[x], 0, squares[x].size)
            }
        }catch (  e: java.lang.Exception){
            println("ERROR: " + this.javaClass + " must have a no-arg constructor for copy() to work.")
            exitProcess(1)
        }
        return copy
    }
    open fun isTerminal(): Boolean {
        //Есть ли у обоих игроков материалы на доске?
        var whiteFound = false
        var blackFound = false
        for (position in getAllPossiblePositions()) {
            val piece = getPiece(position)
            if (piece != null) {
                if (piece.getColour() == Colour.LIGHT)
                    whiteFound = true
                else
                    blackFound = true
            }
        }
        return !whiteFound || !blackFound
    }

    open fun evaluate(maximizingPlayer: Colour): Double {
        //Функция оценки материального баланса
        var score = 0
        for (position in getAllPossiblePositions()) {
            val piece = getPiece(position)
            if (piece != null)
                if (piece.getColour() == maximizingPlayer)
                    score += piece.getValue()
                else
                    score -= piece.getValue()
        }
        return score.toDouble()
    }

    open fun getSuccessors(): ArrayList<GameState> {
        val successors = ArrayList<GameState>()
        for (position in getPlayersPositions(getPlayerTurn())) {
            val piece = getPiece(position)
            if (piece != null && piece.getColour() == getPlayerTurn()) {
                successors.addAll(piece.getSuccessors(this, position))
            }
        }
        return successors
    }

    private fun getPlayersPositions(playerColour: Colour): ArrayList<Position> {
        val positions: ArrayList<Position> = ArrayList()
        for (position in getAllPossiblePositions()) {
            val piece = getPiece(position)
            if (piece != null && piece.getColour() == playerColour)
                positions.add(position)
        }
        return positions
    }

    open fun getAllPossiblePositions(): ArrayList<Position> {
        val positions = ArrayList<Position>(64)
        for (x in 0 until getBoardWidth()) {
            for (y in 0 until getBoardHeight()) {
                positions.add(Position(x, y))
            }
        }
        return positions
    }

    override fun toString(): String {
        val files = "  a b c d e f g h" //заголовки столбцов
        val sb = StringBuilder(
            """
              $files
              
              """.trimIndent()
        )
        for (y in 7 downTo 0) {
            sb.append(y + 1).append("|") //добавление рядов слева (номера строк)
            for (x in 0..7) {
                if (squares[x][y] != null) {
                    sb.append(squares[x][y]).append("|")
                } else {
                    sb.append(" |")
                }
            }
            sb.append(y).append(1).append("\n") //добавление рядов справа (номера строк)
        }
        sb.append(files)
        return sb.toString()
    }

    //GETTERS & SETTERS
    fun getPrev(): GameState? {
        return prev
    }

    fun setPrev(prev: GameState) {
        this.prev = prev
    }

    fun setFuture(future: GameState?) {
            this.future = future
    }

    fun getLastMove(): Move? {
        return lastMove
    }

    private fun setLastMove(lastMove: Move) {
        this.lastMove = lastMove
    }

    fun getPlayerTurn(): Colour {
        return playerTurn
    }

    fun setPlayerTurn(playerTurn: Colour) {
        this.playerTurn = playerTurn
    }

    fun endPlayerTurn() {
        playerTurn = playerTurn.opponent()
    }

    fun getPiece(position: Position): Piece? {
        return squares[position.getX()][position.getY()]
    }

    fun setPiece(position: Position, piece: Piece?) {
        squares[position.getX()][position.getY()] = piece
    }

    fun getBoardWidth(): Int {
        return squares.size
    }

    fun getBoardHeight(): Int {
        return squares[0].size
    }

}
