package com.example.help

enum class Colour {
    LIGHT, DARK;

    fun opponent(): Colour {
        return if (this == LIGHT) DARK else LIGHT
    }
}
