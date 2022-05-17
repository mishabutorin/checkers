package com.example.help

class Position(private val x: Int, private val y: Int) {
    //METHODS
    override fun equals(other: Any?): Boolean {
        return other is Position
                && other.x == x
                && other.y == y
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
