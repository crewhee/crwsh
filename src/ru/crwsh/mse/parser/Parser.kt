package ru.crwsh.mse.parser

import ru.crwsh.mse.commands.Command
import ru.crwsh.mse.commands.CommandFactory

class Parser {
    fun Parse(
        env: Map<String, String>,
        commandGetter: CommandFactory
    ): Command? {
        // \'[^\']+\'|(\\\$.*)
        var line = (readLine())?.split(' ')
        if (line == null)
            return null
        line = line.map {
            if (it.matches(Regex("\\\$.*"))) env[it.drop(1)] ?: ""
            else it
        }
        return commandGetter.getByName(line.get(0), line.drop(1))
    }
}