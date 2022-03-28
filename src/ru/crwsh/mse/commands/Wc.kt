package ru.crwsh.mse.commands

class Wc : Command {
    override val type: String
        get() = "command"

    override fun Execute(args: List<String>, env: Map<String, String>): String {
        TODO("Not yet implemented")
    }

}
