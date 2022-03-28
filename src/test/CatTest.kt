package test

import org.junit.Test
import ru.crwsh.mse.commands.Cat

class CatTest {
    @Test
    fun simpleTest() {
        val a = Cat()
        println(a.Execute(listOf("-s", "/Users/crewhee/Desktop/учеба/итмо/2/temp/test.txt"), hashMapOf()))
    }
}