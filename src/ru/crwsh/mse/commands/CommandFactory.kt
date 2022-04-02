package ru.crwsh.mse.commands

class CommandFactory {
    fun getByName(name: String, args: List<String>): Command {
        return when (name) {
            "pwd" -> Pwd(args)
            "cat" -> Cat(args)
            "echo" -> Echo(args)
            "wc" -> Wc(args)
            "exit" -> Exit(args)
            else -> Executable(listOf(name) + args)
        }
    }
}