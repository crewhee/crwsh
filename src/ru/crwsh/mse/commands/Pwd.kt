package ru.crwsh.mse.commands

class Pwd : Command {
    override val type: String
        get() = "command"

    override fun Execute(args: List<String>, env: Map<String, String>): String {
        var flag_p = true
        var flag_l = false
        for (arg in args) {
            if (arg == "-P") {
                flag_p = true
            }
            if (arg == "-L") {
                flag_l = true
            } else
                return "pwd: bad option: $arg"
        }
        return if (flag_l) {
            env["PWD"] ?: throw RuntimeException("Empty env")
        } else
            env["PWD"] ?: throw RuntimeException("Empty env")
    }
}