package com.example.help

public abstract class Piece {
    private lateinit var colour: Colour
    private lateinit var lightSymbol: String
    private lateinit var darkSymbol: String
    protected var value = 0

    //CONSTRUCTOR
    protected open fun Piece(colour: Colour, lightSymbol: String, darkSymbol: String, value: Int) {
        this.colour = colour
        this.lightSymbol = lightSymbol
        this.darkSymbol = darkSymbol
        this.value = value
    }


    //METHODS
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
                copy.setPrev(gameState) //connect copy to the chain of boards.
                copy.makeMove(move)
                successors.add(copy)
            }
        }
        return successors
    }


    @JvmName("getValue1")
    open fun getValue(): Int {
        return value
    }

    open fun getColour(): Colour {
        return colour
    }

}