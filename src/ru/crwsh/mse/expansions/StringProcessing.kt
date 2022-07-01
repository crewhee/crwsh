package ru.crwsh.mse.expansions

import ru.crwsh.mse.parser.tokens.CrwshToken
import ru.crwsh.mse.parser.tokens.DQuoteToken
import ru.crwsh.mse.parser.tokens.SQuoteToken
import ru.crwsh.mse.parser.tokens.WordToken

class StringProcessing : Expansion {
    override fun expand(tokens: MutableList<CrwshToken>) {
        for (i in tokens.indices) {
            if (tokens[i] is SQuoteToken || tokens[i] is DQuoteToken) {
                val sb = StringBuilder()
                for (w in tokens[i].children) {
                    sb.append((w as WordToken).content)
                }
                tokens[i] = WordToken(sb.toString())
            }
        }
    }
}