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
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane

class MainController {

    @FXML
    lateinit var boardHolder: StackPane

    //top level buttons
    @FXML
    lateinit var btnNewGame: Button

    @FXML
    lateinit var btnAIMove: Button

    @FXML
    lateinit var btnLightPlayer: Button

    @FXML
    lateinit var btnDarkPlayer: Button

    private var depthOptions = arrayOf(1, 2, 3, 4, 5, 6, 7)
    private var lightBot: MoveBot = MinimaxBot()
    private var darkBot: MoveBot = MinimaxBot()
    private var humanSymbol = "👨"
    private var robotSymbol = "🤖"
    private var aiOptions: Array<MoveBot> = arrayOf(
        MinimaxBot(),
        AlphaBetaBot(),
        OptimalMiniMax()
    )

    //GETTERS & SETTERS
    private var game = Game()
    private var aiThread: Thread? = null
    private var lightDepth = 3
    private var darkDepth = 3
    private var gui: GUIBoard? = null

    //METHODS
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

    //General game controls
    @FXML
    fun btnNewGameClicked() {
        game.newGame()
        refreshGUI()
    }

    fun makeAIMove() {
        //disabledButtonList = список отключеных кнопок. Нельзя нажимать кнопки, пока работает ИИ.
        val disabledButtonList = arrayOf<Node?>(gui, btnNewGame, btnAIMove)
        if (aiThread == null) {
            for (n in disabledButtonList) n?.isDisable = true
            aiThread = Thread {
                val aiMove = if (game.getCurrentGameState().getPlayerTurn() == Colour.LIGHT) lightBot.getMove(game.getCurrentGameState(), lightDepth)
                else darkBot.getMove(game.getCurrentGameState(), darkDepth)
                //make move
                if (aiMove != null) {
                    aiMove.getLastMove()!!.setSender(gui)
                    game.makeMove(aiMove)
                    gui?.repaint()
                    aiThread = null
                    if (currentPlayerIsAI()) Platform.runLater { makeAIMove() }
                }
                for (n in disabledButtonList) if (n != null) {
                    n.isDisable = false
                }
            }
            aiThread!!.start()
        }
    }

    fun currentPlayerIsAI(): Boolean {
        return if (game.getCurrentGameState().getPlayerTurn() == Colour.LIGHT) btnLightPlayer.text == robotSymbol
        else btnDarkPlayer.text == robotSymbol
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
        gui?.repaint()
        aiThread = null
        btnLightPlayer.text = "👨"
        btnDarkPlayer.text = "👨"
    }

    fun getGame(): Game {
        return game
    }

}