package test.ru.crwsh.mse.commands

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.crwsh.mse.commands.Grep
import java.io.File
import java.io.PipedInputStream
import java.io.PipedOutputStream
import kotlin.streams.toList


class GrepTest {

    private var path = File("test/resources/grep").absolutePath
//    private var absolutePath = file.absolutePath

    @Test
    fun simpleTest() {
        // file: abc \n bcd \n ebf \n a42
        val grep = Grep(listOf("a", path.plus("/grepSimpleFile.txt")))
        val ostream = PipedOutputStream()
        val istream = PipedInputStream()
        istream.connect(ostream)
        grep.execute(mutableMapOf(), System.`in`, ostream)
        ostream.close()
        val res = istream.bufferedReader().lines().toList()
        assertEquals("abc", res[0])
        assertEquals("a42", res[1])
    }

    @Test
    fun harderTest() {
        val grep = Grep(listOf("grep", "-A", "1", "a", path.plus("/grepSimpleFile.txt")))
        val ostream = PipedOutputStream()
        val istream = PipedInputStream()
        istream.connect(ostream)
        grep.execute(mutableMapOf(), System.`in`, ostream)
        ostream.close()
        val res = istream.bufferedReader().lines().toList()
        assertEquals("abc", res[0])
        assertEquals("bcd", res[1])
        assertEquals("a42", res[2])
    }
}