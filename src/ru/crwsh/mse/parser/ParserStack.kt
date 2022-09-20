package ru.crwsh.mse.parser

import ru.crwsh.mse.parser.tokens.CrwshToken
import ru.crwsh.mse.parser.tokens.WordToken
import java.util.*

class ParserStack : Stack<CrwshToken>() {

    fun getStackString(): String {
        val tempStringBuilder = StringBuilder()
        for (state in this) {
            if (state is WordToken) {
                if (state.children.isNotEmpty()) {
                    tempStringBuilder.append(state.children[0].name)
                }
            }
            else
                tempStringBuilder.append(state.name)
            tempStringBuilder.append(" ")
        }
        return tempStringBuilder.toString()
    }
}