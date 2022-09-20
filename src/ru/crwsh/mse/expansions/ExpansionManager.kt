package ru.crwsh.mse.expansions

import ru.crwsh.mse.parser.tokens.CrwshToken

class ExpansionManager(val env: MutableMap<String, String>) {
    val varExp = VariableExpansion(env)
    val strPrcs = StringProcessing()
    val commandSubst = CommandSubst(env)
    fun performExpansions(list: MutableList<CrwshToken>): MutableList<CrwshToken> {
        varExp.expand(list)
        strPrcs.expand(list)
        commandSubst.expand(list)
        return list
    }
}