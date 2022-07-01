package ru.crwsh.mse.parser.tokens

sealed class CrwshToken (public val name : String, public val children: MutableList<CrwshToken> = mutableListOf<CrwshToken>()) {
}

open class WordToken(var content : String) : CrwshToken("word") {
//    private var content = ""
//        get() = field
//        set(value) {
//            field = value
//        }
}

class SQuoteToken : CrwshToken("squote")
class DQuoteToken : CrwshToken("dquote")

class PipeToken : CrwshToken("pipe")
class RedirToken : CrwshToken("redir")

class AssignmentToken : CrwshToken("assign")
class VariableToken : CrwshToken("var")
class BraceExpansionToken : CrwshToken("braceexp")
class CmdSubstToken : CrwshToken("cmdsubst")
class NLToken : CrwshToken("newline")
