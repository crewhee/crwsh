package test.ru.crwsh.mse.expansions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.crwsh.mse.expansions.VariableExpansion
import ru.crwsh.mse.parser.Parser
import ru.crwsh.mse.parser.tokens.WordToken

class ExpansionTest {
    val parser = Parser()
    val varexp = VariableExpansion(mutableMapOf("x" to "ex", "y" to "it"))

    @Test
    fun simpleTest() {
        parser.appendLine("echo \$x")
        val tokens = parser.getTokens()
        varexp.expand(tokens)
        assertEquals(2, tokens.size)
        parser.reset()
    }

    @Test
    fun exitTest() {
        parser.appendLine("\$x\$y")
        val tokens = parser.getTokens()
        varexp.expand(tokens)
        assertEquals(1, tokens.size)
        assertEquals("exit", (tokens[0] as WordToken).content)
        parser.reset()
    }

    @Test
    fun sqouteTest() {
        parser.appendLine("echo 'text \$x'")
        val tokens = parser.getTokens()
        varexp.expand(tokens)
        parser.reset()
    }

    @Test
    fun dqouteTest() {
        parser.appendLine("echo \"text \$x\"")
        val tokens = parser.getTokens()
        varexp.expand(tokens)
        parser.reset()
    }



}