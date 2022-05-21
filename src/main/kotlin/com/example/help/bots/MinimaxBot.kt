package com.example.help.bots

import com.example.help.Colour
import com.example.help.GameState
import java.util.*

class MinimaxBot : MoveBot {
    private lateinit var maximizingPlayer: Colour
    //METHODS
    override fun getMove(gameState: GameState, maxDepth: Int): GameState? {
        var chosen: GameState? = null
        maximizingPlayer = gameState.getPlayerTurn()
        if (!gameState.isTerminal()) {
            var maxValue = Double.NEGATIVE_INFINITY
            for (successor in shuffle(gameState.getSuccessors())) {
                val tmp = maxValue.coerceAtLeast(min(successor, maxDepth - 1))
                if (tmp >= maxValue) {
                    maxValue = tmp
                    chosen = successor
                }
            }
        }
        return chosen
    }

    private fun max(gameState: GameState, depth: Int): Double {
        if (depth == 0 || gameState.isTerminal()) return gameState.evaluate(maximizingPlayer)
        var value = Double.NEGATIVE_INFINITY
        for (successor in shuffle(gameState.getSuccessors())) {
            value = value.coerceAtLeast(min(successor, depth - 1))
        }
        return value
    }

    private fun min(gameState: GameState, depth: Int): Double {
        if (depth == 0 || gameState.isTerminal()) return gameState.evaluate(maximizingPlayer)
        var value = Double.POSITIVE_INFINITY
        for (successor in shuffle(gameState.getSuccessors())) {
            value = value.coerceAtMost(max(successor, depth - 1))
        }
        return value
    }

    override fun toString(): String {
        return "Minimax"
    }

    private fun shuffle(boards: ArrayList<GameState>): ArrayList<GameState> {
        boards.shuffle()
        return boards
    }
}
