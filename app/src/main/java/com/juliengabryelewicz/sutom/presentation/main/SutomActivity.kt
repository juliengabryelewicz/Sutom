package com.juliengabryelewicz.sutom.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juliengabryelewicz.sutom.domain.model.LetterGuessed
import com.juliengabryelewicz.sutom.ui.theme.SutomTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.ui.window.Dialog

@AndroidEntryPoint
class SutomActivity : ComponentActivity() {
    private val viewModel: SutomViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SutomTheme {
                Scaffold(
                    topBar = {
                        SmallTopAppBar(
                            title = {
                                Text(
                                    text = "Sutom",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            },
                            colors = TopAppBarDefaults.smallTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = Color.White,
                            ),
                        )
                    }
                ) {
                    val guessWordCount by viewModel.currentWordGuess.observeAsState(initial = 0)
                    val guessLetterCount by viewModel.currentCharGuess.observeAsState(initial = 1)
                    val sutomWord by viewModel.sutomWord.observeAsState()
                    val sutomGuesses by viewModel.sutomGuesses.observeAsState(initial = emptyList())
                    val keyboard by viewModel.keyboard.observeAsState(initial = emptyList())
                    val wordFound by viewModel.wordFound.observeAsState(false)
                    val validationError by viewModel.wordError.observeAsState(false)

                    sutomWord?.let { guessWord ->
                        Column {
                            BoxWithConstraints(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = MaterialTheme.colorScheme.surface)
                            ) {
                                val maxHeight = this.maxHeight
                                val gridHeight = maxHeight.times(.67f)
                                val keyboardHeight = maxHeight.times(.33f)

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(gridHeight)
                                        .align(Alignment.TopCenter)
                                        .padding(top = 4.dp)
                                ) {
                                    sutomGuesses.forEach { guess ->
                                        BoxWithConstraints(
                                            modifier = Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            val spacing = this.maxWidth.times(.08f) / 7
                                            val width = this.maxWidth.times(.90f) / 7
                                            val height = gridHeight.times(.80f) / 7

                                            Row(
                                                horizontalArrangement = Arrangement.Center,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                guess.forEachIndexed { index, guessLetter ->
                                                    Surface(
                                                        modifier = Modifier
                                                            .size(if (height < width) height else width)
                                                            .padding(
                                                                horizontal = spacing,
                                                                vertical = 4.dp
                                                            ),
                                                        color =
                                                        if (guessLetter.isInWord && guessLetter.isInProperPlace)
                                                            Color(93, 134, 81)
                                                        else if (guessLetter.isInWord)
                                                            Color(178, 160, 76)
                                                        else
                                                            MaterialTheme.colorScheme.surface,
                                                        border = BorderStroke(
                                                            1.dp,
                                                            MaterialTheme.colorScheme.onSurface
                                                        )
                                                    ) {
                                                        Box(contentAlignment = Alignment.Center) {
                                                            BasicTextField(
                                                                value = guessLetter.value,
                                                                onValueChange = {
                                                                    guessLetter.value = it
                                                                },
                                                                readOnly = true,
                                                                textStyle = TextStyle(
                                                                    color = MaterialTheme.colorScheme.onSurface,
                                                                    fontSize = 26.sp,
                                                                    textAlign = TextAlign.Center
                                                                )
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(keyboardHeight)
                                        .align(Alignment.BottomCenter)
                                ) {
                                    keyboard.forEach { letters ->
                                        BoxWithConstraints(
                                            Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            val spacing = this.maxWidth.times(.05f) / 10
                                            val width = this.maxWidth.times(.95f) / 10

                                            Row(
                                                horizontalArrangement = Arrangement.Center,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                letters.forEach { letter ->
                                                    val haptic = LocalHapticFeedback.current

                                                    Surface(
                                                        modifier = Modifier
                                                            .width(
                                                                if (letter.char == "DEL") width.times(
                                                                    1.5f
                                                                ) else width
                                                            )
                                                            .height(70.dp)
                                                            .padding(
                                                                horizontal = spacing,
                                                                vertical = 4.dp
                                                            )
                                                            .clickable {
                                                                haptic.performHapticFeedback(
                                                                    HapticFeedbackType.LongPress
                                                                )
                                                                when (letter.char) {
                                                                    "DEL" -> {
                                                                        removePreviousInput(
                                                                            guessWordCount,
                                                                            guessLetterCount,
                                                                            viewModel,
                                                                            sutomGuesses
                                                                        )
                                                                    }
                                                                    else -> {
                                                                        inputChar(
                                                                            guessWordCount,
                                                                            guessLetterCount,
                                                                            letter.char,
                                                                            viewModel,
                                                                            sutomGuesses
                                                                        )
                                                                    }
                                                                }
                                                            },
                                                        shape = RoundedCornerShape(8.dp),
                                                        color = if (letter.isInProperPlace && letter.isInProperPlace) {
                                                            Color(93, 134, 81)
                                                        } else if (letter.isInWord) {
                                                            Color(178, 160, 76)
                                                        } else if (letter.used) {
                                                            Color.DarkGray
                                                        } else {
                                                            MaterialTheme.colorScheme.primary
                                                        }
                                                    ) {
                                                        Box(contentAlignment = Alignment.Center) {
                                                            Text(
                                                                text = letter.char,
                                                                textAlign = TextAlign.Center,
                                                                style = MaterialTheme.typography.bodyLarge,
                                                                color =
                                                                if (letter.isInProperPlace
                                                                    || letter.isInProperPlace
                                                                    || letter.used
                                                                ) Color.White
                                                                else MaterialTheme.colorScheme.onPrimary,
                                                                fontWeight = FontWeight.Bold
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (wordFound) {
                                Dialog(onDismissRequest = { viewModel.reset() }) {
                                    Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp)) {
                                        Column(
                                            modifier = Modifier
                                                .background(MaterialTheme.colorScheme.surface)
                                                .padding(16.dp),
                                            verticalArrangement = Arrangement.spacedBy(8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                                Text("Bravo, vous avez réussi")
                                        }
                                    }
                                }
                            }

                            if (validationError) {
                                AlertDialog(
                                    onDismissRequest = { },
                                    title = {
                                        Text("Perdu")
                                    },
                                    text = { Text("Voulez-vous réessayer?") },
                                    confirmButton = {
                                        TextButton(onClick = { viewModel.reset() }) {
                                            Text(text = "Oui")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}

private fun removePreviousInput(
    guessWordCount: Int,
    guessLetterCount: Int,
    viewModel: SutomViewModel,
    sutomGuesses: List<List<LetterGuessed>>,
) {
    if (guessLetterCount > 0) {
        val guess = LetterGuessed(TextFieldValue(""))
        val sutomGridCopy = sutomGuesses.toMutableList()
        val guesses = sutomGridCopy[guessWordCount].toMutableList()
        guesses.removeAt(guessLetterCount - 1)
        guesses.add(guessLetterCount - 1, guess)
        sutomGridCopy[guessWordCount] = guesses.toList()
        viewModel.sendCharDelete(sutomGridCopy.toList())
    }
}

fun inputChar(
    guessWordCount: Int,
    guessLetterCount: Int,
    guessLetter: String,
    viewModel: SutomViewModel,
    sutomGuesses: List<List<LetterGuessed>>,
) {
    val guess = LetterGuessed(TextFieldValue(guessLetter))
    val sutomGridCopy = sutomGuesses.toMutableList()
    val guesses = sutomGridCopy[guessWordCount].toMutableList()
    guesses.removeAt(guessLetterCount)
    guesses.add(guessLetterCount, guess)
    sutomGridCopy[guessWordCount] = guesses.toList()
    viewModel.updateSutomGuesses(sutomGridCopy.toList())
}