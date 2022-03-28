package ru.crwsh.mse.pipeline

import ru.crwsh.mse.commands.Command

class Pipeline {
    val type: String
        get() = "pipeline"

    fun execute(commands: List<Command>, env: Map<String, String>): String? {
//        var res : List<String> = listOf("")
//        for (cmd in commands)
//            res = cmd.Execute(res, env)
//        return res
        return null
    }
}