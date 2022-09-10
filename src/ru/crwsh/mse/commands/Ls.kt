package ru.crwsh.mse.commands

import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.attribute.PosixFilePermission
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import kotlin.io.path.fileSize
import kotlin.io.path.getOwner
import kotlin.io.path.getPosixFilePermissions

class Ls(override var args: List<String>) : Command {
    override val name: String
        get() = "command"

    private val flags: Set<String> = flagParser()
    private val dfs = DateFormatSymbols.getInstance()
    private val format: SimpleDateFormat

    init {
        if (Locale.getDefault().country == "RU") {
            val shortMonths = arrayOf(
                "янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек"
            )
            dfs.shortMonths = shortMonths
        }
        format = SimpleDateFormat("dd MMM HH:mm", dfs)
    }

    private fun formatOutputShort(files: List<File>): String {
        val res = StringBuilder()
        files.forEach { res.append(it.name); res.append("\n") }
        return res.toString()
    }

    private fun formatPermissions(p: Set<PosixFilePermission>): String {
        val res = StringBuilder()

        if (p.contains(PosixFilePermission.OWNER_READ)) res.append("r")
        else res.append("-")
        if (p.contains(PosixFilePermission.OWNER_WRITE)) res.append("w")
        else res.append("-")
        if (p.contains(PosixFilePermission.OWNER_EXECUTE)) res.append("x")
        else res.append("-")

        if (p.contains(PosixFilePermission.GROUP_READ)) res.append("r")
        else res.append("-")
        if (p.contains(PosixFilePermission.GROUP_WRITE)) res.append("w")
        else res.append("-")
        if (p.contains(PosixFilePermission.GROUP_EXECUTE)) res.append("x")
        else res.append("-")

        if (p.contains(PosixFilePermission.OTHERS_READ)) res.append("r")
        else res.append("-")
        if (p.contains(PosixFilePermission.OTHERS_WRITE)) res.append("w")
        else res.append("-")
        if (p.contains(PosixFilePermission.OTHERS_EXECUTE)) res.append("x")
        else res.append("-")
        return res.toString()
    }

    private fun formatOutputLong(files: List<File>): String {
        // file mode, number of links, owner name, group name, number of bytes in the file
        // abbreviated month, day-of-month file was last modified, hour file last modified, minute file last modified
        // pathname
        val res = StringBuilder()
        for (it in files) {
            val filePath = it.toPath()
            res.append(
                "%c%s %s %6d %s %s\n".format(
                    if (it.isDirectory) 'd' else '-',
                    formatPermissions(filePath.getPosixFilePermissions()),
                    filePath.getOwner()?.name,
                    filePath.fileSize(),
                    format.format(it.lastModified()),
                    it.name
                )
            )
        }
        return res.toString()
    }

    override fun execute(env: MutableMap<String, String>, istream: InputStream, ostream: OutputStream): Int {
        val currentDir = File(env.getOrDefault("PWD", "/"))
        var result = currentDir.listFiles()?.toList() ?: return 1
        if (!flags.contains("a")) {
            result = result.filter { !it.isHidden }
        }

        val w = ostream.writer()
        if (flags.contains("l")) w.write(formatOutputLong(result))
        else w.write(formatOutputShort(result))
        w.flush()
        return 0
    }
}