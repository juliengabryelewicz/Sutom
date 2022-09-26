package com.juliengabryelewicz.sutom.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.juliengabryelewicz.sutom.domain.model.LetterGuessed
import com.juliengabryelewicz.sutom.domain.model.LetterState
import com.juliengabryelewicz.sutom.domain.model.keyboardInit
import com.juliengabryelewicz.sutom.domain.model.sutomGrid
import com.juliengabryelewicz.sutom.domain.repository.SutomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SutomViewModel @Inject constructor(
    private val sutomRepository: SutomRepository
) : ViewModel() {

    private val _sutomGuesses: MutableLiveData<List<List<LetterGuessed>>> = MutableLiveData()
    val sutomGuesses: LiveData<List<List<LetterGuessed>>> = _sutomGuesses
    private val _keyboard: MutableLiveData<List<List<LetterState>>> = MutableLiveData()
    val keyboard: LiveData<List<List<LetterState>>> = _keyboard
    val currentCharGuess: MutableLiveData<Int> = MutableLiveData(0)
    val currentWordGuess: MutableLiveData<Int> = MutableLiveData(0)
    val sutomWord: MutableLiveData<String> = MutableLiveData()
    val wordError: MutableLiveData<Boolean> = MutableLiveData()
    val wordFound: MutableLiveData<Boolean> = MutableLiveData()

    init {
        _sutomGuesses.postValue(sutomGrid)
        _keyboard.postValue(keyboardInit)
        sutomWord.postValue(sutomRepository.getSutomWord())
    }

    fun updateSutomGuesses(guesses: List<List<LetterGuessed>>) {
        _sutomGuesses.postValue(guesses)
        if (currentCharGuess.value!! == 6) {
            if(wrongGuess(guesses)) {
                updateKeyboard(guesses)
                currentWordGuess.postValue(currentWordGuess.value?.inc())
                currentCharGuess.postValue(0)
                if (currentWordGuess.value!! == 5) {
                    wordError.postValue(true)
                }
            }
        } else {
            currentCharGuess.postValue(currentCharGuess.value?.inc())
        }

    }

    private fun updateKeyboard(guesses: List<List<LetterGuessed>>) {
        val guess = guesses[currentWordGuess.value!!]
        keyboard.value?.let { keyRowsList: List<List<LetterState>> ->
            val keyboardCopy = keyRowsList.toMutableList()
            keyboardCopy.forEach { keyRows ->
                keyRows.forEach { key ->
                    guess.forEach { guessLetter ->
                        if (guessLetter.value.text == key.char) {
                            key.used = true
                            key.isInWord = guessLetter.isInWord
                            key.isInProperPlace = guessLetter.isInProperPlace
                        }
                    }
                }
            }
            _keyboard.postValue(keyboardCopy)
        }
    }

    private fun wrongGuess(guesses: List<List<LetterGuessed>>) : Boolean {
        val sutomGridCopy = guesses.toMutableList()
        val guess = sutomGridCopy[currentWordGuess.value!!].toMutableList()

        guess.forEachIndexed { index, guessLetter ->
            guessLetter.isInWord =
                sutomWord.value!!.contains(guessLetter.value.text, ignoreCase = true)
            guessLetter.isInProperPlace =
                sutomWord.value!!.toCharArray()[index].toString()
                    .equals(guessLetter.value.text, ignoreCase = true)
        }
        sutomGridCopy[currentWordGuess.value!!] = guess.toList()
        _sutomGuesses.postValue(sutomGridCopy)

        if (doAllLettersMatchWord(guess)) {
            wordFound.postValue(true)
            return false
        }

        return true
    }

    private fun doAllLettersMatchWord(guess: MutableList<LetterGuessed>): Boolean {
        var guessString = ""
        guess.forEachIndexed { index, guessLetter ->
            guessString += guessLetter.value.text
        }

        return guessString.equals(sutomWord.value, ignoreCase = true)
    }

    fun sendCharDelete(guesses: List<List<LetterGuessed>>) {
        _sutomGuesses.postValue(guesses)

        currentCharGuess.postValue(currentCharGuess.value?.dec())
    }

    fun reset() {
        _sutomGuesses.postValue(sutomGrid)
        wordError.postValue(false)
        wordFound.postValue(false)
        sutomWord.postValue(sutomRepository.getSutomWord())
        keyboard.value?.let { keyRowsList: List<List<LetterState>> ->
            val keyboardCopy = keyRowsList.toMutableList()
            keyboardCopy.forEach { keyRows ->
                keyRows.forEach { key ->
                    key.used = false
                    key.isInWord = false
                    key.isInProperPlace = false
                }
            }
            _keyboard.postValue(keyboardCopy)
        }

        currentCharGuess.postValue(0)
        currentWordGuess.postValue(0)
    }

}