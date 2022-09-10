package ru.crwsh.mse.expansions

import ru.crwsh.mse.parser.ParserStack
import ru.crwsh.mse.parser.tokens.CrwshToken

class BraceExpansion(val stack: ParserStack) : Expansion {
    override fun expand(tokens: MutableList<CrwshToken>) {
        TODO()
        var flag = true
        while (flag) {
            flag = false
            tokens.forEach {

            }
        }
    }
}