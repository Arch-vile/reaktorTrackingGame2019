import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.text.SimpleDateFormat
import java.util.Comparator
import kotlin.math.max

//DEPS com.fasterxml.jackson.core:jackson-core:2.10.1, com.fasterxml.jackson.core:jackson-databind:2.10.1, com.fasterxml.jackson.module:jackson-module-kotlin:2.10.1

fun main(args: Array<String>) {

    var data =
            ObjectMapper()
                    .registerModule(KotlinModule())
                    .readValue(File(args[0]), FloodData::class.java)

    data.regions.forEach {
        checkRegion(it)
    }


}

private fun checkRegion(region: Region) {
    var sorted = region.readings
            .sortedWith(Comparator { r1, r2 ->
                toDate(r1).compareTo(toDate(r2))
            })

    val suspicious = sorted.windowed(2, 1)
            .map {
                val firstDay = it[0]
                val secondDay = it[1]
                val delta = calculatePools(secondDay.reading) - calculatePools(firstDay.reading)
                var triple = Triple(delta, firstDay, secondDay)
                println(triple)
                triple
            }
            .filter { it.first > 1000 }

//    val numbers = suspicious
//            .map { it.first }
//    println(numbers[0])

//    numbers
//            .sortedBy { it  }
//            .forEach { println(it) }
//            .sortedBy { triple -> triple.first }
//            .forEach {
//                val delta = it.first
//                val firstDay = it.second
//                val secondDay = it.third
//                println("Region ${region.regionID} delta $delta first ${firstDay.readingID}  ${firstDay.date}\t second ${secondDay.readingID} ${secondDay.date}")
//            }
}

fun toDate(r1: Reading) =
        SimpleDateFormat("dd-MMM-yyyy").parse(r1.date)

private fun testPoolFunction(testData: List<Int>, expected: Int) {
    var poolVolume: Int = calculatePools(testData)
    if (poolVolume != expected)
        throw RuntimeException("Wrong volume, got $poolVolume expected $expected")
}

fun calculatePools(data: List<Int>): Int {
    var maxHeight = data.max()
    var indexOfMax = data.indexOfFirst { it == maxHeight }

    var leftOfMax = data.subList(0, indexOfMax + 1)
    var rightOfMax = data.subList(indexOfMax, data.size).reversed()

    return calculateSection(leftOfMax) + calculateSection(rightOfMax)
}

fun calculateSection(data: List<Int>): Int {
    var sum = 0
    var currentReferenceHeight = data[0]
    for (height in data) {
        currentReferenceHeight = max(currentReferenceHeight, height)
        sum += currentReferenceHeight - height
    }
    return sum
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


fun foo() {

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
}