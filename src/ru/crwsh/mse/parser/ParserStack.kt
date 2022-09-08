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

//    fun updateStack(c : Char) : Int {
//        // returns number of char to skip
//        return 0
//
//        if (c != ' ') {
//            if (!stack.isEmpty() && (stack.peek() == ParserStates.PIPELINE
//                        || stack.peek() == ParserStates.CMD_OR
//                        || stack.peek() == ParserStates.CMD_AND)) {
//                stack.pop()
//            }
//            metSymbolBeforePipe = true
//        }
//        if (stack.isEmpty()) {  // || (stack.peek() != ParserStates.SINGLE_QUOTE || stack.peek() != ParserStates.DOUBLE_QUOTE)
//            if (line[i] == '\\') {
//                i++
//            } else if (line[i] == '\'' && (i == 0 || line[i - 1] != '\\')) {
//                stack.push(ParserStates.SINGLE_QUOTE)
//            } else if (line[i] == '"' && (i == 0 || line[i - 1] != '\\')) {
//                stack.push(ParserStates.DOUBLE_QUOTE)
//            } else if (line[i] == '`' && (i == 0 || line[i - 1] != '\\')) {
//                stack.push(ParserStates.BQUOTE)
//            } else if (line[i] == '$') {
//                if (i + 1 < line.length && line[i + 1] == '(') {
//                    if (i + 2 < line.length && line[i + 2] == '(') {
//                        stack.push(ParserStates.ARITH_SUBST)
//                        i += 2
//                    } else {
//                        stack.push(ParserStates.COMMAND_SUBST)
//                        i += 1
//                    }
//                }
//            } else if (line[i] == '<' && (i + 1 < line.length && line[i + 1] == '(')) {
//                stack.push(ParserStates.COMMAND_SUBST)
//                i += 1
//            } else if (line[i] == '|') {
//                if (i + 1 < line.length && line[i+1] == '|' && (stack.isEmpty() || stack.peek() == ParserStates.ARITH_SUBST)) {
//                    stack.push(ParserStates.CMD_OR)
//                    i += 1
//                }
//                else {
//                    if (metSymbolBeforePipe) {
//                        stack.push(ParserStates.PIPELINE)
//                        metSymbolBeforePipe = false
//                    } else {
//                        canRecover = false
//                        return false
//                    }
//                }
//            } else if (line[i] == '&') {
//                if (i + 1 < line.length && line[i+1] == '&') {
//                    stack.push(ParserStates.CMD_AND)
//                    i += 1
//                }
//            }
//        } else {
//            if (stack.peek() == ParserStates.SINGLE_QUOTE) {
//                if (line[i] == '\'') stack.pop()
//            }
//            else {
//                if (line[i] == '\\') i++
//                else {
//                    if (stack.peek() == ParserStates.DOUBLE_QUOTE) {
//
//                    }
//                    else if (stack.peek() == ParserStates.CMD_AND || stack.peek() == ParserStates.CMD_OR || stack.peek() == ParserStates.PIPELINE){
//                        if (line[i] != ' ') {
//                            stack.pop()
//                        }
//                    }
//                    else if (stack.peek() == ParserStates.BQUOTE) {
//                        if (line[i] == '`') {
//                            stack.pop()
//                        }
//                    }
//                    else if (stack.peek() == ParserStates.ARITH_SUBST) {
//                        if (line[i] == ')' && i + 1 < line.length && line[i+1] == ')') {
//                            i += 1
//                            stack.pop()
//                        }
//                    }
//                    else if (stack.peek() == ParserStates.COMMAND_SUBST) {
//                        if (line[i] == ')') {
//                            stack.pop()
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
//    fun canExpand(): Boolean {
//        return stack.isEmpty() || stack.peek() != ParserState.SINGLE_QUOTE || stack.peek() != ParserState.DOUBLE_QUOTE
//    }
}