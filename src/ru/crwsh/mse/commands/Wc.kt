package ru.crwsh.mse.commands

import java.io.InputStream
import java.io.OutputStream

private data class WordCountResult(var line: Int, var word: Int, var byte: Int)

//private class StreamVisiter {
//    fun visit(istream: InputStreamReader, wcResult : WordCountResult) {
//
//    }
//}

class Wc(override var args: List<String>) : Command {
    override val name: String
        get() = "command"

    override fun execute(
        env: MutableMap<String, String>,
        istream: InputStream,
        ostream: OutputStream
    ): Int {
        TODO("Not yet implemented")
    }

}
