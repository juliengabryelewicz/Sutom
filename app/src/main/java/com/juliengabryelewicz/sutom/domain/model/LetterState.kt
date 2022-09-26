package com.juliengabryelewicz.sutom.domain.model

class LetterState(
    var char: String,
    var used: Boolean = false,
    var isInWord: Boolean = false,
    var isInProperPlace: Boolean = false
)

val keyboardInit = listOf(
    listOf(
        LetterState("A"),
        LetterState("B"),
        LetterState("C"),
        LetterState("D"),
        LetterState("E"),
        LetterState("F"),
        LetterState("G"),
        LetterState("H"),
        LetterState("I"),
        LetterState("J")
    ),
    listOf(
        LetterState("K"),
        LetterState("L"),
        LetterState("M"),
        LetterState("N"),
        LetterState("O"),
        LetterState("P"),
        LetterState("Q"),
        LetterState("R"),
        LetterState("S")
    ),
    listOf(
        LetterState("T"),
        LetterState("U"),
        LetterState("V"),
        LetterState("W"),
        LetterState("X"),
        LetterState("Y"),
        LetterState("Z"),
        LetterState("DEL")
    )
)