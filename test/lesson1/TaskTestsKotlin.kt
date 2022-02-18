package lesson1

import org.junit.jupiter.api.Tag
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskTestsKotlin : AbstractTaskTests() {

    @Test
    @Tag("3")
    fun testSortTimes() {
        sortTimes { inputName, outputName -> sortTimes(inputName, outputName) }
    }

    @Test
    @Tag("4")
    fun testSortAddresses() {
        sortAddresses { inputName, outputName -> sortAddresses(inputName, outputName) }
    }

    @Test
    @Tag("4")
    fun testSortTemperatures() {

        sortTemperatures("./test/lesson1/test.txt", "./test/lesson1/output.txt")
        assertFileContent("./test/lesson1/actual.txt", File("./test/lesson1/output.txt").readText())

        sortTemperatures { inputName, outputName -> sortTemperatures(inputName, outputName) }
    }

    @Test
    @Tag("4")
    fun testSortSequence() {

        lesson1.sortSequence("./test/lesson1/empty.txt", "./test/lesson1/outputEmpty.txt")
        assertFileContent("./test/lesson1/outputEmpty.txt", "")

        sortSequence { inputName, outputName -> sortSequence(inputName, outputName) }
    }

    @Test
    @Tag("2")
    fun testMergeArrays() {

        assert(
            arrayOf<Int>().contentEquals(
                mergeArrays(
                    arrayOf<Int>(),
                    arrayOf<Int?>()
                )
            )
        )

        assert(
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8).contentEquals(
                mergeArrays(
                    arrayOf(5, 6, 7, 8),
                    arrayOf(null, null, null, null, 1, 2, 3, 4)
                )
            )
        )

        mergeArrays { first, second -> mergeArrays(first, second) }
    }
}
