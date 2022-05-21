package com.example.help


abstract class Piece protected constructor(colour: Colour, lightSymbol: String, darkSymbol: String, value: Int) {
    private val colour: Colour
    private var lightSymbol: String
    private var darkSymbol: String
    private var value: Int

    //CONSTRUCTOR
    init {
        this.colour = colour
        this.lightSymbol = lightSymbol
        this.darkSymbol = darkSymbol
        this.value = value
    }

    abstract fun isMoveLegal(gameState: GameState, move: Move): Boolean
    abstract fun makeMove(gameState: GameState, move: Move)
    override fun toString(): String {
        return if (getColour() == Colour.LIGHT) lightSymbol else darkSymbol
    }

    open fun getSuccessors(gameState: GameState, myPosition: Position): ArrayList<GameState> {
        val successors = ArrayList<GameState>()
        for (position in gameState.getAllPossiblePositions()) {
            val move = Move(myPosition, position)
            if (gameState.isMoveLegal(move)) {
                val copy = gameState.copy()
                copy.setPrev(gameState) //подключение копии шашки к дугих копиям на поле
                copy.makeMove(move)
                successors.add(copy)
            }
        }
        return successors
    }

    open fun getValue(): Int {
        return value
    }

    open fun getColour(): Colour {
        return colour
    }
}


