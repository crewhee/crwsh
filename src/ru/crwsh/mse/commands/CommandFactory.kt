package ru.crwsh.mse.commands

enum class CommandType {
    PWD,
    CAT,
    ECHO,
    WC,
    EXIT,
}

class CommandFactory {
    fun makeCommand(type: CommandType) : Command {
        return when(type) {
            CommandType.PWD -> Pwd()
            CommandType.CAT -> Cat()
            CommandType.ECHO -> Echo()
            CommandType.WC -> Wc()
            CommandType.EXIT -> Exit()
        }
    }

    fun getName(type: CommandType) : String {
        return when(type) {
            CommandType.PWD -> "pwd"
            CommandType.CAT -> "cat"
            CommandType.ECHO -> "echo"
            CommandType.WC -> "wc"
            CommandType.EXIT -> "exit"
        }
    }
}