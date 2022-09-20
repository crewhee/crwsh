package ru.crwsh.mse.utils

class EnvLoader {
    fun load() : MutableMap<String, String> {
        return mutableMapOf(
            "SHELL" to "crwsh",
            "PWD" to System.getProperty("user.dir"),
            "OLDPWD" to System.getProperty("user.dir"),
            "PATH" to "/usr/bin/",
            "?" to "0",
            "HOME_DIR" to System.getProperty("user.home")
        )
    }
}
