package ru.crwsh.mse.commands

import java.io.InputStream
import java.io.OutputStream

interface Command {
    val name: String
    var args: List<String>

    fun execute(
        env: MutableMap<String, String>,
        istream: InputStream = System.`in`,
        ostream: OutputStream = System.out
    ): Int
}