package ru.crwsh.mse.expansions

import ru.crwsh.mse.parser.tokens.CrwshToken

interface Expansion {
    fun expand(tokens: MutableList<CrwshToken>)
}