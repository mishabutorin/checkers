package com.example.help

class Game {
    private lateinit var currentGameState: GameState

    //METHODS
    fun makeMove(move: Move) {
        if (currentGameState.isMoveLegal(move)) {
            val copy: GameState = currentGameState.copy()
            copy.makeMove(move)
            makeMove(copy)
        }
    }

    fun makeMove(gameState: GameState) {
        gameState.setFuture(null) //обновить будущее поле, поскольку оно больше не действительно
        currentGameState.setFuture(gameState)
        gameState.setPrev(currentGameState)
        currentGameState = gameState
        gameState.postMoveUINotifications(gameState.getLastMove()!!.getSender()!!)
    }

    private fun undoAll() {
        while (currentGameState.getPrev() != null)
            currentGameState = currentGameState.getPrev()!!
    }


    fun newGame() {
        undoAll()
        currentGameState.setFuture(null)
    }

    //GETTERS
    fun setCurrentGameState(gameState: GameState) {
        currentGameState = gameState
    }

    fun getCurrentGameState(): GameState {
        return currentGameState
    }
}