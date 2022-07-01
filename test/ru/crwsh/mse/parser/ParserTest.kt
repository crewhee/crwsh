package test.ru.crwsh.mse.parser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.crwsh.mse.parser.Parser
import ru.crwsh.mse.parser.tokens.WordToken


class ParserTest {
    val parser = Parser()

    @Test
    fun simpleTest() {
        parser.appendLine("echo 3")
        val tokens = parser.getTokens()
        assertEquals(2, tokens.size)
        assertTrue(tokens[0] is WordToken)
        assertTrue(tokens[1] is WordToken)
        assertEquals("echo", (tokens[0] as WordToken).content)
        assertEquals("3", (tokens[1] as WordToken).content)
        parser.reset()
    }

    @Test
    fun singleQuoteTest() {
        parser.appendLine("echo 'some text'")
        val tokens = parser.getTokens()
        assertEquals(2, tokens.size)
        parser.reset()
    }

    @Test
    fun doubleQuoteTest() {
        parser.appendLine("echo \"some \$var\"")
        val tokens = parser.getTokens()
        assertEquals(2, tokens.size)
        parser.reset()
    }

    @Test
    fun spaceTest() {
        parser.appendLine("     echo         3 2      ")
        val tokens = parser.getTokens()
        assertEquals(3, tokens.size)
        assertTrue(tokens.all { it is WordToken })
        assertEquals("echo", (tokens[0] as WordToken).content)
        assertEquals("3", (tokens[1] as WordToken).content)
        assertEquals("2", (tokens[2] as WordToken).content)
        parser.reset()
    }

    @Test
    fun pipeTest() {
        parser.appendLine(" a | b | c")
        val tokens = parser.getTokens()
        assertEquals(5, tokens.size)
        parser.reset()
    }

    @Test
    fun redirTest() {
        parser.appendLine("echo 3 > 1.txt")
        var tokens = parser.getTokens()
        parser.reset()
    }

    @Test
    fun wordTest() {
        parser.appendLine("\$x\$y")
        var tokens = parser.getTokens()
        parser.reset()
    }

    @Test
    fun wordTestHard() {
        parser.appendLine("echo abc\$x")
        var tokens = parser.getTokens()
        parser.reset()
    }

    @Test
    fun cmdSubstTest() {
        parser.appendLine("echo \$(echo 3)")
        var tokens = parser.getTokens()
        parser.reset()
    }

    @Test
    fun cmdSubstHardTest() {
        parser.appendLine("echo abc\$(echo 3)")
        var tokens = parser.getTokens()
        parser.reset()
    }

    @Test
    fun backCommandSubstTest() {
        parser.appendLine("echo \$(echo 3)abc")
        var tokens = parser.getTokens()
        parser.reset()
    }

    @Test
    fun varsPipeTest() {
        parser.appendLine("echo \$x | echo \$x")
        var tokens = parser.getTokens()
        parser.reset()
    }
}