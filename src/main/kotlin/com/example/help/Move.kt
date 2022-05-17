package com.example.help

import kotlin.math.abs

class Move (start: Position, destination: Position, ui: UserInterface?) {
    private var start: Position
    private var destination: Position

    //If sender is null, that indicates the move was not created by a user - it must have been the computer/AI.
    private var sender: UserInterface?

    //CONSTRUCTORS
    constructor(start: Position, destination: Position) : this(start, destination, null)

    init {
        this.start = start
        this.destination = destination
        sender = ui

    }

    fun isNorth(): Boolean {
        return getY2() - getY1() > 0
    }

    fun isSouth(): Boolean {
        return getY2() - getY1() < 0
    }

    fun isDiagonal(): Boolean {
        return abs(getY2() - getY1()) == abs(getX2() - getX1())
    }

    /**
     * How many squared from start to destination? Only works with linear moves (diagonal or straight).
     */
    fun distance(): Int {
        return abs(getY2() - getY1()).coerceAtLeast(abs(getX2() - getX1()))
    }

    fun getStart(): Position {
        return start
    }

    fun getDestination(): Position {
        return destination
    }

    fun getX1(): Int {
        return getStart().getX()
    }

    fun getY1(): Int {
        return getStart().getY()
    }

    fun getX2(): Int {
        return getDestination().getX()
    }

    fun getY2(): Int {
        return getDestination().getY()
    }

    fun getSender(): UserInterface? {
        return sender
    }

    fun setSender(sender: UserInterface) {
        this.sender = sender
    }
}