package ru.crwsh.mse.shell

import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream

class ShellFactory {
    companion object {
        fun getPipedShells(
            count: Int,
            env: MutableMap<String, String>,
            inputStream: InputStream,
            outputStream: OutputStream
        ): List<Shell> {
            val istreams = List(count - 1) { PipedInputStream() }
            val ostreams = List(count - 1) { PipedOutputStream() }
            val result = mutableListOf<Shell>()
            for (i in 0 until count - 1) {
                ostreams[i].connect(istreams[i])
            }
            result.add(Shell(env.toMutableMap(), inputStream, ostreams[0]))
            for (i in 0 until count - 2) {
                result.add(Shell(env.toMutableMap(), istreams[i], ostreams[i + 1]))
            }
            result.add(Shell(env.toMutableMap(), istreams.last(), outputStream))
            return result
        }
    }

    fun getShell() : Shell {
        return Shell()
    }
}