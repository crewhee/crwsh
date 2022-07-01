package ru.crwsh.mse.parser.tokens

sealed class CrwshToken (val name : String, val children: MutableList<CrwshToken> = mutableListOf()) {
}

open class WordToken(var content : String) : CrwshToken("word")

class SQuoteToken : CrwshToken("squote")
class DQuoteToken : CrwshToken("dquote")

class PipeToken : CrwshToken("pipe")
class RedirToken : CrwshToken("redir")

class AssignmentToken : CrwshToken("assign")
class VariableToken : CrwshToken("var")
//class BraceExpansionToken : CrwshToken("braceexp")
class CmdSubstToken : CrwshToken("cmdsubst")
class NLToken : CrwshToken("newline")
