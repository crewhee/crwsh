package ru.crwsh.mse.pipeline

import ru.crwsh.mse.commands.Command

class Pipeline {
    val type: String
        get() = "pipeline"

    fun execute(commands: List<Command>, env: Map<String, String>): String? {
        TODO("Not implemented")
    }
}