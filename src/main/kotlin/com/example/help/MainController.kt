package com.example.help

import com.example.help.boards.GUIBoard
import com.example.help.boards.GUICheckers
import com.example.help.bots.AlphaBetaBot
import com.example.help.bots.MinimaxBot
import com.example.help.bots.MoveBot
import com.example.help.bots.OptimalMiniMax
import com.example.help.checkers.CheckersState
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane

class MainController {
    @FXML
    lateinit var boardHolder: StackPane

    @FXML
    lateinit var lblFooter: Label

    //top level buttons
    @FXML
    lateinit var btnNewGame: Button

    @FXML
    lateinit var btnAIMove: Button

    @FXML
    lateinit var btnLightPlayer: Button

    @FXML
    lateinit var btnDarkPlayer: Button

    private val depthOptions = arrayOf(1, 2, 3, 4, 5, 6, 7)
    private var lightBot: MoveBot = MinimaxBot()
    private var darkBot: MoveBot = MinimaxBot()
    private val humanSymbol = "👨"
    private val robotSymbol = "🤖"
    private val aiOptions: Array<MoveBot> = arrayOf(
        MinimaxBot(),
        AlphaBetaBot(),
        OptimalMiniMax()
    )
    private val game = Game()
    private var aiThread: Thread? = null
    private var lightDepth = 3
    private var darkDepth = 3
    private lateinit var gui: GUIBoard

    fun initialize() {
        btnLightPlayer.text = humanSymbol
        btnDarkPlayer.text = humanSymbol
        menuCheckersClicked()
    }

    //Game Selection
    @FXML
    fun menuCheckersClicked() {
        gui = GUICheckers(this)
        boardHolder.children.setAll(gui)
        game.setCurrentGameState(CheckersState())
        refreshGUI()
    }

    @FXML
    fun btnNewGameClicked() {
        game.newGame()
        refreshGUI()
    }

    fun makeAIMove() {
        //disabledButtonList = board modifying buttons. Mustn't allow changes while AI is doing work.
        val disabledButtonList = arrayOf<Node>(gui, btnNewGame, btnAIMove)
        //if makeAIMove is called while aiThread is already busy, e.g. user spams play-as-computer button, ignore it.
        if (aiThread == null) {
            for (n in disabledButtonList) n.isDisable = true
            aiThread = Thread {

                //get move from move bot - must call the appropriate algorithm for the player
                val aiMove: GameState =
                    if (game.getCurrentGameState()!!.getPlayerTurn() == Colour.LIGHT) lightBot.getMove(
                        game.getCurrentGameState()!!,
                        lightDepth
                    ) else darkBot.getMove(game.getCurrentGameState()!!, darkDepth)
                //make move
                aiMove.getLastMove()!!.setSender(gui)
                game.makeMove(aiMove)
                gui.repaint()
                aiThread = null
                if (currentPlayerIsAI()) Platform.runLater { makeAIMove() }
                for (n in disabledButtonList) n.isDisable = false
            }
            aiThread!!.start()
        }
    }

    fun currentPlayerIsAI(): Boolean {
        return if (game.getCurrentGameState()!!.getPlayerTurn() == Colour.LIGHT
        ) btnLightPlayer.text == robotSymbol else btnDarkPlayer.text == robotSymbol
    }

    @FXML
    fun togglePlayerAI(actionEvent: ActionEvent) {
        val btn = actionEvent.source as Button
        btn.text = if (btn.text == humanSymbol) robotSymbol else humanSymbol
        if (currentPlayerIsAI()) makeAIMove()
    }

    @FXML
    fun chooseAIBot(mouseEvent: MouseEvent) {
        if (mouseEvent.isSecondaryButtonDown) if (mouseEvent.source == btnLightPlayer) {
            lightBot = PopUp.presentOptions(aiOptions, false)
            lightDepth = PopUp.presentOptions(depthOptions, true)
        } else {
            darkBot = PopUp.presentOptions(aiOptions, false)
            darkDepth = PopUp.presentOptions(depthOptions, true)
        }
    }

    private fun refreshGUI() {
        gui.repaint()
        aiThread = null
        btnLightPlayer.text = "👨"
        btnDarkPlayer.text = "👨"
    }

    //GETTERS & SETTERS
    fun getGame(): Game {
        return game
    }
}