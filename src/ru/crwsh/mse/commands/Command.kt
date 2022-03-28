package ru.crwsh.mse.commands

interface Command {
    val type : String

    fun Execute(args: List<String>, env: Map<String, String>) : String?
}