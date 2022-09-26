package com.juliengabryelewicz.sutom.domain.model

import androidx.compose.ui.text.input.TextFieldValue

class LetterGuessed(
    var value: TextFieldValue = TextFieldValue(""),
    var isInWord: Boolean = false,
    var isInProperPlace: Boolean = false
)

val sutomGrid = listOf(
    listOf(
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
    ),
    listOf(
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
    ),
    listOf(
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
    ), listOf(
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
    ), listOf(
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
    ), listOf(
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
        LetterGuessed(),
    )
)