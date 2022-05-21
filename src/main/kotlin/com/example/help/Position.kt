package com.example.help

class Position(arrayX: Int, arrayY: Int) {

    private var x: Int = arrayX
    private var y: Int = arrayY

    //METHODS
    override fun equals(other: Any?): Boolean {
        return ((other is Position)
                && (getX() == getX())
                && (getY() == getY()))
    }

    fun getX(): Int {
        return x
    }

    fun getY(): Int {
        return y
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}
