package ru.crwsh.mse.commands

import java.io.File
import java.io.InputStream
import java.io.OutputStream

class Cd(override var args: List<String>) : Command {
    override val name: String
        get() = "cd"

    private fun getOneArgPath(env: MutableMap<String, String>, s: String): String? {
        if (s[0] == '/') {
            val f = File(s)
            return if (f.isDirectory) f.absolutePath else null
        } else if (s == "-") {
            return env.getOrDefault("OLDPWD", "/")
        } else {
            val f = File(env.getOrDefault("PWD", "/")).resolve(s)
            return if (f.isDirectory) f.canonicalPath else null
        }
    }

    private fun getTwoArgPath(args: List<String>, env: MutableMap<String, String>): String? {
        val insideDir = File(env.getOrDefault("PWD", "/"))
        val parentDir = insideDir.parent
        val dirList = File(parentDir).list { dir, name -> File(dir, name).isDirectory && name.contains(args[0]) }
        dirList?.forEach {
            val f = File(parentDir, it.replace(args[0], args[1]))
            if (f.isDirectory)
                return f.canonicalPath
        }
        return null
    }

    private fun updatePWD(s: String, env: MutableMap<String, String>) {
        env["OLDPWD"] = env.getOrDefault("PWD", "/")
        env["PWD"] = s
    }

    override fun execute(env: MutableMap<String, String>, istream: InputStream, ostream: OutputStream): Int {
        val w = ostream.bufferedWriter()
        when (args.size) {
            1 -> {
                updatePWD(env.getOrDefault("HOME_DIR", "/"), env)
            }
            2 -> {
                val res = getOneArgPath(env, args[1])
                if (res == null) {
                    w.write("cd: can't find directory ${args[1]}\n")
                    w.flush()
                    return 1
                } else {
                    updatePWD(res, env)
                }
            }
            3 -> {
                val res = getTwoArgPath(args.drop(1), env)
                if (res == null) {
                    w.write("cd: string ${args[1]} not in PWD or no such directory after replacement\n")
                    w.flush()
                    return 1
                } else {
                    updatePWD(res, env)
                }
            }
            else -> {
                w.write("cd: bad arguments count\n")
                w.flush()
                return 1
            }
        }
        return 0
    }

}
