package com.example.help.bots

import com.example.help.GameState

interface MoveBot {
    fun getMove(gameState: GameState, maxDepth: Int): GameState
}
