package ru.crwsh.mse.commands

import ru.crwsh.mse.parser.tokens.WordToken
import java.util.stream.Collectors

class CommandFactory {
    fun getByName(args: List<WordToken>): Command {
        if (args.isEmpty()) {
            throw RuntimeException("Empty command, this must never happen")
        }
        val strargs: List<String> = args.stream()
            .map { it.content }
            .collect(Collectors.toList())
        return when (strargs[0]) {
            "pwd" -> Pwd(strargs)
            "cat" -> Cat(strargs)
            "echo" -> Echo(strargs)
            "wc" -> Wc(strargs)
            "exit" -> Exit(strargs)
            "env" -> Env(strargs)
            "cd" -> Cd(strargs)
            "ls" -> Ls(strargs)
            "grep" -> Grep(strargs)
            else -> Executable(strargs)
        }
    }
}