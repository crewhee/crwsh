package test

import org.junit.jupiter.api.Test
import ru.crwsh.mse.commands.Cat

class CatTest {
    @Test
    fun simpleTest() {
        val a = Cat(listOf("-s", "/Users/crewhee/Desktop/учеба/итмо/2/temp/test.txt"))
        println(a.execute(hashMapOf()))
    }
}