package ru.crwsh.mse.commands

import kotlin.system.exitProcess

class Exit : Command {
    override fun Execute(args: List<String>, env: Map<String, String>): String? {
        exitProcess(0);
    }
}