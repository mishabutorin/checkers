package com.example.help.bots

import com.example.help.Colour
import com.example.help.GameState

class AlphaBetaBot : MoveBot {
    private var maxDepth: Int = 3
    private lateinit var maximizingPlayer: Colour
    //METHODS
    override fun getMove(gameState: GameState, maxDepth: Int): GameState? {
        var chosen: GameState? = null
        maximizingPlayer = gameState.getPlayerTurn()
        if (!gameState.isTerminal()) {
            this.maxDepth = maxDepth
            var value = Double.NEGATIVE_INFINITY
            var alpha = Double.NEGATIVE_INFINITY
            for (successor in shuffle(gameState.getSuccessors())) {
                println()
                val tmp = value.coerceAtLeast(min(successor, 0, alpha, Double.POSITIVE_INFINITY))
                if (tmp > value) {
                    value = tmp
                    chosen = successor
                }
                alpha = alpha.coerceAtLeast(value)
            }
        }
        return chosen
    }

    private fun min(gameState: GameState, depth: Int, alpha: Double, beta: Double): Double {
        var beta = beta
        if (depth == maxDepth || gameState.isTerminal())
            return gameState.evaluate(maximizingPlayer)
        var value = Double.POSITIVE_INFINITY
        for (successor in shuffle(gameState.getSuccessors())) {
            value = value.coerceAtMost(max(successor, depth + 1, alpha, beta))
            if (value <= alpha) return value
            beta = beta.coerceAtMost(value)
        }
        return value
    }

    private fun max(gameState: GameState, depth: Int, alpha: Double, beta: Double): Double {
        var alpha = alpha
        if (depth == maxDepth || gameState.isTerminal())
            return gameState.evaluate(maximizingPlayer)
        var value = Double.NEGATIVE_INFINITY
        for (successor in shuffle(gameState.getSuccessors())) {
            value = value.coerceAtLeast(min(successor, depth + 1, alpha, beta))
            if (value >= beta) return value
            alpha = alpha.coerceAtLeast(value)
        }
        return value
    }

    override fun toString(): String {
        return "Alpha-Beta"
    }

    private fun shuffle(gameStates: ArrayList<GameState>): ArrayList<GameState> {
        gameStates.shuffle()
        return gameStates
    }
}
