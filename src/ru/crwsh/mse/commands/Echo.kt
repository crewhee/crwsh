package ru.crwsh.mse.commands

class Echo : Command {
    override fun Execute(args: List<String>, env: Map<String, String>): String? {
        val result = StringBuilder()
        for (arg in args)
            result.append("$arg ")
        return result.toString()
    }
}
