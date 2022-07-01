package ru.crwsh.mse.parser

import ru.crwsh.mse.parser.tokens.*

/*
    Parser rules:
    () -> subshell
    '' -> ignore $
    "" -> do not ignore $ ` \, ignore other
    `` -> act as command substitution, same as $()
    \  -> protect next character, newline->whitespace
 */

class Parser {
    private val stack = ParserStack()                   // stack consists of currently building tokens
    private val sb = StringBuilder()                    // current word
    private val output: MutableList<CrwshToken> = mutableListOf()

    private var canRecover = true

    private var line: String = ""
    private var pos: Int = 0

    fun canRecover(): Boolean {
        return canRecover
    }

    fun getStackString(): String {
        return stack.getStackString()
    }

    fun getTokens(): MutableList<CrwshToken> {
        return output
    }

    fun reset() {
        output.clear()
        stack.clear()
        pos = 0
        canRecover = true
        sb.clear()
        line = ""
    }

    private fun appendToWord(c: Char) {
        sb.append(c)
    }

    private fun endCurrentWord() {
        if (sb.isEmpty()) return
        if (stack.isNotEmpty()) {
            var pt : CrwshToken? = null
            if (stack.peek() is PipeToken || stack.peek() is RedirToken) {
                pt = stack.pop()
            }
            if (stack.isNotEmpty()) {
                stack.peek().children.add(WordToken(sb.toString()))
                if (stack.peek() is VariableToken
                    // || stack.peek() is CmdSubstToken
                ) {
                    pt?.let { stack.peek().children.add(it) }
                    attachToken()
                }
            }
            else {
                pt?. let { output.add(it) }
                output.add(WordToken(sb.toString()))
            }
        }
        else {
            output.add(WordToken(sb.toString()))
        }
        sb.clear()
    }

//    private fun putCurrentWordToStack() {
//        stack.push(WordToken(""))
//        endCurrentWord()
//    }

    private fun attachToken() {
        if (stack.size > 1) {
            val tok = stack.pop()
            stack.peek().children.add(tok)
        }
        else {
            throw RuntimeException("Must never happen")
        }
    }

    private fun finalizeToken() {
        if (stack.isNotEmpty()) output.add(stack.pop())
    }

    private fun processDollarSign() {

        if (pos + 1 < line.length) {
            if (line[pos + 1].isLetter()) {
                if (stack.isNotEmpty() && stack.peek() is VariableToken)
                    endCurrentWord()
                if (stack.isEmpty() || stack.peek() !is WordToken)
                    stack.push(WordToken(""))
                // do wordtoken with var inside
                endCurrentWord()
                stack.push(VariableToken())
            } else if (line[pos + 1] == '(') {
                if (stack.isEmpty() || stack.peek() !is WordToken)
                    stack.push(WordToken(""))
                endCurrentWord()
                stack.push(CmdSubstToken())
                pos++
            } else {
                // continue to do just a word token
                appendToWord(line[pos])
            }
        } else {
            output.add(WordToken(line[pos].toString()))
        }
    }

    private fun advance() {
        /*
            $
            $(
            <( ->
            | -> pipe
            [a-zA-Z0-9] -> word or assignment
            ' ->
            " ->
         */
        if (stack.isEmpty()) {
            when (line[pos]) {
                ' ' -> endCurrentWord()
                '\'' -> stack.push(SQuoteToken())
                '"' -> stack.push(DQuoteToken())
                '|' -> {
                    endCurrentWord()
                    if ((output.isEmpty() || output.last() is PipeToken) && stack.isEmpty())
                        canRecover = false
                    stack.add(PipeToken())
                }
                '>' -> {
                    endCurrentWord(); stack.add(RedirToken())
                }
                '$' -> processDollarSign()
                '=' -> {
                    val name = sb.toString()
                    sb.clear()
                    while (pos + 1 < line.length) {
                        pos++
                        sb.append(line[pos])
                    }
                    val tok = AssignmentToken()
                    tok.children.add(WordToken(name))
                    tok.children.add(WordToken(sb.toString()))
                    sb.clear()
                    output.add(tok)
                }
                '\\' -> {
                    if (pos + 1 < line.length) {
                        appendToWord(line[pos + 1])
                        pos++
                    } else {
                        stack.push(NLToken())
                    }
                }
                else -> appendToWord(line[pos])
            }
        } else {
            if (stack.peek() is SQuoteToken) {
                if (line[pos] == '\'') {
                    endCurrentWord()
                    finalizeToken()
                } else {
                    appendToWord(line[pos])
                }
            } else {
                if (line[pos] == '\\') {
                    if (pos + 1 < line.length) {
                        appendToWord(line[pos + 1])
                        pos++
                    } else {
                        stack.push(NLToken())
                    }
                } else {
                    when (line[pos]) {
                        ' ' -> {
                            if (stack.peek() is DQuoteToken) {
                                appendToWord(line[pos])
                            } else {
                                endCurrentWord()
                                if (stack.size == 1 && stack.peek() is WordToken) finalizeToken()
                            }
                        }
                        '$' -> processDollarSign()
                        '"' -> {
                            if (stack.peek() is DQuoteToken || stack.peek() is VariableToken) {
                                if (stack.peek() is VariableToken) {
                                    endCurrentWord()
                                    attachToken()
                                }
                                endCurrentWord()
                                finalizeToken()
                            } else {
                                endCurrentWord()
                                stack.push(DQuoteToken())
                            }
                        }
                        '|' -> {
                            if ((output.isEmpty() || output.last() is PipeToken) && stack.all({it !is WordToken}))  canRecover = false
                            stack.add(PipeToken())
                        }
                        '>' -> {
                            output.add(RedirToken())
                        }
                        ')' -> {
                            if (stack.peek() is CmdSubstToken) {
                                endCurrentWord()
                                val tok = stack.pop()
                                if (stack.isNotEmpty() && stack.peek() is WordToken)
                                    stack.peek().children.add(tok)
                                else
                                    finalizeToken()
                            } else appendToWord(line[pos])
                        }
                        else -> appendToWord(line[pos])
                    }
                }
            }
        }
        pos++
    }


    fun appendLine(inLine: String?): Boolean {
        if (stack.isNotEmpty()) {
            if (stack.peek() is NLToken)
                stack.pop()
            else if (stack.peek() is SQuoteToken || stack.peek() is DQuoteToken){
                appendToWord('\n')
            }
        }
        if (inLine != null) {
            line += inLine
        }
        while (pos < line.length && canRecover) {
            advance()
        }
        if (sb.isNotEmpty() || stack.isNotEmpty() && (stack.peek() is VariableToken || stack.peek() is WordToken)) {
            endCurrentWord()
            if (stack.isNotEmpty() && stack.peek() is WordToken)
                finalizeToken()
        }
        if (output.isNotEmpty() && output[0] is AssignmentToken) {
            if (output.size != 1
                    || output[0].children.size != 2
                    || output[0].children[0] !is WordToken
                    || output[0].children[1] !is WordToken) {
                canRecover = false
                return false
            }
        }
        return when (stack.isEmpty()) {
            true -> {
                if (sb.isNotEmpty()) endCurrentWord()
                true
            }
            false -> false
        }
    }

    fun getCommandList(pipeline: List<CrwshToken>): List<List<CrwshToken>> {
        var i = 0
        val cmdList : MutableList<List<CrwshToken>> = mutableListOf()
        while (i < pipeline.size) {
            val tmpCmd : MutableList<CrwshToken> = mutableListOf()
            while (i < pipeline.size) {
                if (pipeline[i] is WordToken) {
                    tmpCmd.add(pipeline[i])
                    i++
                    if (i == pipeline.size) {
                        cmdList.add(tmpCmd)
                        break
                    }
                }
                else if (pipeline[i] is PipeToken) {
                    cmdList.add(tmpCmd)
                    i++
                    break
                }
                else {
                    throw RuntimeException("pipe parse error")
                }
            }
        }
        return cmdList
    }
}