import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.text.SimpleDateFormat
import java.util.Comparator
import kotlin.math.max

//DEPS com.fasterxml.jackson.core:jackson-core:2.10.1, com.fasterxml.jackson.core:jackson-databind:2.10.1, com.fasterxml.jackson.module:jackson-module-kotlin:2.10.1

fun main(args: Array<String>) {

    runTests()
    System.exit(1)


    var data =
            ObjectMapper()
                    .registerModule(KotlinModule())
                    .readValue(File(args[0]), FloodData::class.java)

    data.regions.map {
        checkRegion(it)
    }
            .flatten()
//            .sortedBy { it.second.first }.reversed()
//            .sortedWith(Comparator { r1, r2 ->
//                toDate(r1.second.second).compareTo(toDate(r2.second.third))
//            })
            .forEach {
                val region = it.first
                val delta = it.second.first
                val firstDay = it.second.second
                val secondDay = it.second.third
                println("Region ${region.regionID} delta $delta first ${firstDay.readingID}  ${firstDay.date}\t second ${secondDay.readingID} ${secondDay.date}")
            }
}

private fun checkRegion(region: Region): List<Pair<Region, Triple<Int, Reading, Reading>>> {
    return region.readings
            .sortedWith(Comparator { r1, r2 ->
                toDate(r1).compareTo(toDate(r2))
            })
            .windowed(2, 1)
            .map {
                val firstDay = it[0]
                val secondDay = it[1]
                val delta = calculatePools(secondDay.reading) - calculatePools(firstDay.reading)
                Pair(region, Triple(delta, firstDay, secondDay))
            }
            .filter { it.second.first > 1000 }
}

fun toDate(r1: Reading) =
        SimpleDateFormat("dd-MMM-yyyy").parse(r1.date)

private fun testPoolFunction(testData: List<Int>, expected: Int) {
    var poolVolume: Int = calculatePools(testData)
    println("Data: $testData Result: $poolVolume")
    if (poolVolume != expected)
        throw RuntimeException("Wrong volume, got $poolVolume expected $expected")
}

fun calculatePools(data: List<Int>): Int {
    return data.withIndex()
            .groupBy({ it.index < data.indexOf(data.max()) }, { it.value })
            .map { it.value }.chunked(2)
            .flatMap { listOf(it[0], it[1].reversed()) }
            .map {
                it.fold(
                        Pair(Int.MIN_VALUE, 0),
                        { a, v -> max(a.first, v).let { Pair(it, a.second + it - v) } })
                        .second
            }
            .sum()

}


data class FloodData(
        val regions: List<Region>
)

data class Region(
        val regionID: String,
        val readings: List<Reading>
)

data class Reading(
        val readingID: String,
        val date: String,
        val reading: List<Int>
)


fun runTests() {

    testPoolFunction(
            listOf(2, 1, 0, -2, 0, -1, 0, 1, 4),
            15)

    testPoolFunction(
            listOf(2, 1, 0, -2, 0, -1, 0, 1, 4, 1, 0, -2, 0, 4),
            32)

    testPoolFunction(
            listOf(2, 1, 0, -2, 0, -1, 0, 1, 4, 1, 0, -2, 0, 2),
            24)

    testPoolFunction(
            listOf(2, 1, 0, -2, 0, -1, 0, 1, 4, 1, 0, -2, 0, 4, -1, -2, -1, 2, -1, 0),
            43)

    testPoolFunction(
            listOf(2, 1, 0, -2, 0, -1, 0, 1, 4, 1, 0, -2, 0, 4, -1, -2, -1, 4, -1, 0),
            49)

    testPoolFunction(
            listOf(3, -1, 0, -2, -2, 1, -1, 0),
            10)

    testPoolFunction(
            listOf(3, -1, 0, -2, -2, 1, -1, 0).reversed(),
            10)

    // Missing test: flat
    testPoolFunction(listOf(0, 0, 0), 0)
    testPoolFunction(listOf(0, 1, 0), 0)
    testPoolFunction(listOf(0, 1, 2, 3, 2, 1), 0)
    testPoolFunction(listOf(0, 1, 2, 2, 2, 3, 5), 0)

    // Timon testit
    testPoolFunction(listOf(7, 7, 6, 5, 4, 4, 9, 8, 8, 7, 7, 7), 9)
    testPoolFunction(listOf(2, 1, 2, 0, 1, 0, 3), 6)
    testPoolFunction(listOf(2, 0, 1, 0, 1, 0, 3), 8)
    testPoolFunction(listOf(2, 0, 0, 1), 2)
    testPoolFunction(listOf(2, 0, 0, 4, 0, 0, 4), 12)
    testPoolFunction(listOf(12, 0, 0, 4, 0, 0, 4), 16)
    testPoolFunction(listOf(12, 0, 0, 4, 0, 0, 5), 21)
    testPoolFunction(listOf(0, 12, 0, 3, 0, 0, 5), 17)
    testPoolFunction(listOf(0, 12, 0, 12, 0, 0, 5), 22)

}