package com.juliengabryelewicz.sutom.domain.repository

import android.content.Context
import com.juliengabryelewicz.sutom.R
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SutomRepository @Inject constructor(private val context: Context) {
    var sutomWords: List<String>
    var sutomCount = 0

    init {
        sutomWords = loadSutomWords()
    }

    fun getSutomWord(): String {
        sutomCount = (0..sutomWords.count()-1).random()
        return sutomWords[sutomCount]
    }

    private fun loadSutomWords(): List<String> {
        val reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.sutom_words)))
        val listOfWords = mutableListOf<String>()
        var mLine = reader.readLine()
        while (mLine != null) {
            listOfWords.add(mLine) // process line
            mLine = reader.readLine()
        }
        reader.close()
        return listOfWords
    }
}