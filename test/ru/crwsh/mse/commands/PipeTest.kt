package test.ru.crwsh.mse.commands

import org.junit.jupiter.api.Test
import ru.crwsh.mse.parser.Parser
import ru.crwsh.mse.shell.Shell

class PipeTest {
    val parser = Parser()
    val sh = Shell()

    @Test
    fun simpleTest() {
        parser.appendLine("echo 3 | echo 3")
        val tok = parser.getTokens()
        sh.execute(tok)
        parser.reset()
    }

    @Test
    fun innerPipeTest() {
        parser.appendLine("echo 3 | \$(echo cat | cat | cat)")
        val tok = parser.getTokens()
        sh.execute(tok)
        parser.reset()
    }
}