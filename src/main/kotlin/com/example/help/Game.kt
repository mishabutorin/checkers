package com.example.help

class Game {

    private var currentGameState: GameState? = null

    //METHODS
    fun makeMove(move: Move) {
        if (currentGameState?.isMoveLegal(move) == true) {
            val copy = currentGameState?.copy()
            copy?.makeMove(move)
            if (copy != null) {
                makeMove(copy)
            }
        }
    }

    fun makeMove(gameState: GameState) {
        gameState.setFuture(null) //overwrite future board as it's no longer valid
        currentGameState?.setFuture(gameState)
        gameState.setPrev(currentGameState)
        currentGameState = gameState
        gameState.postMoveUINotifications(gameState.getLastMove()?.getSender()!!)
    }

    fun newGame() {
        while (currentGameState?.getPrev() != null)
            currentGameState = currentGameState!!.getPrev()
    }

    fun setCurrentGameState(gameState: GameState) {
        currentGameState = gameState
    }

    fun getCurrentGameState(): GameState? {
        return currentGameState
    }
}