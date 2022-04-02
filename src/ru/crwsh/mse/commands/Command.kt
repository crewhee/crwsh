package ru.crwsh.mse.commands

import java.io.InputStreamReader
import java.io.OutputStreamWriter

interface Command {
    val name: String
    var args: List<String>

    fun execute(
        env: Map<String, String>,
        istream: InputStreamReader = InputStreamReader(System.`in`),
        ostream: OutputStreamWriter = OutputStreamWriter(System.out)
    ): Int
}