package ru.crwsh.mse.shell

import java.io.InputStream
import java.io.OutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream

class ShellFactory {
    companion object {
        fun getPipedShells(
            count: Int,
            sh: Shell,
            inputStream: InputStream,
            outputStream: OutputStream
        ): List<Shell> {
            val istreams = List(count - 1) { PipedInputStream() }
            val ostreams = List(count - 1) { PipedOutputStream() }
            val result = mutableListOf<Shell>()
            for (i in 0 until count - 1) {
                ostreams[i].connect(istreams[i])
            }
            result.add(Shell(sh.publicEnv.toMutableMap(), inputStream, ostreams[0]))
            for (i in 0 until count - 2) {
                result.add(Shell(sh.publicEnv.toMutableMap(), istreams[i], ostreams[i + 1]))
            }
            sh.inputReader = istreams.last().reader()
            result.add(sh)
            return result
        }

        fun getSingleShell(env: MutableMap<String, String>, outputStream: OutputStream) : Shell {
            return Shell(env.toMutableMap(), outputStream = outputStream)
        }
    }
}