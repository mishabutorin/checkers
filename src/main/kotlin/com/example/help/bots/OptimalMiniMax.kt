package com.example.help.bots

import com.example.help.Colour
import com.example.help.GameState
import java.util.*

class OptimalMiniMax : MoveBot {
    private var maxDepth: Int = 0
    private lateinit var maximizingPlayer: Colour

    //METHODS
    override fun getMove(gameState: GameState, maxDepth: Int): GameState? {
        this.maxDepth = maxDepth
        maximizingPlayer = gameState.getPlayerTurn()
        var chosen: GameState? = null
        if (!gameState.isTerminal()) {
            val max = max(gameState, 0)
            chosen = max.gameState
            for (i in 1 until max.depth) {
                chosen = chosen?.getPrev()
            }
        }
        return chosen
    }

    private fun max(gameState: GameState, currentDepth: Int): MinimaxNode {
        if (currentDepth == maxDepth || gameState.isTerminal()) return MinimaxNode(
            gameState,
            gameState.evaluate(maximizingPlayer),
            currentDepth
        )
        var v = MinimaxNode(Double.NEGATIVE_INFINITY)
        for (successor in shuffle(gameState.getSuccessors())) {
            v = biggest(v, min(successor, currentDepth + 1))
        }
        return v
    }

    private fun min(gameState: GameState, currentDepth: Int): MinimaxNode {
        if (currentDepth == maxDepth || gameState.isTerminal()) return MinimaxNode(
            gameState,
            gameState.evaluate(maximizingPlayer),
            currentDepth
        )
        var v = MinimaxNode(Double.POSITIVE_INFINITY)
        for (successor in shuffle(gameState.getSuccessors())) {
            v = smallest(v, max(successor, currentDepth + 1))
        }
        return v
    }

    override fun toString(): String {
        return "Optimal"
    }

    private fun shuffle(gameStates: ArrayList<GameState>): ArrayList<GameState> {
        gameStates.shuffle()
        return gameStates
    }

    //INNER CLASS
    private class MinimaxNode {
        var gameState: GameState? = null
        var score: Double = 0.0
        var depth = 0

        constructor(score: Double) {
            this.score = score
        }

        constructor(gameState: GameState?, score: Double, depth: Int) {
            this.gameState = gameState
            this.score = score
            this.depth = depth
        }
    }

    companion object {
        private fun smallest(n1: MinimaxNode, n2: MinimaxNode): MinimaxNode {
            if (n1.score < n2.score) return n1
            if (n1.score > n2.score) return n2
            return if (n1.depth <= n2.depth) n1 else n2
        }

        private fun biggest(n1: MinimaxNode, n2: MinimaxNode): MinimaxNode {
            if (n1.score > n2.score) return n1
            if (n1.score < n2.score) return n2
            return if (n1.depth <= n2.depth) n1 else n2
        }
    }
}
