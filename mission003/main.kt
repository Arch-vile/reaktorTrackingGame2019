import kotlin.math.max

//DEPS com.fasterxml.jackson.core:jackson-core:2.10.1, com.fasterxml.jackson.core:jackson-databind:2.10.1, com.fasterxml.jackson.module:jackson-module-kotlin:2.10.1

fun main(args: Array<String>) {
    testPoolFunction(
            listOf(2, 1, 0, -2, 0, -1, 0, 1, 4),
            15)

    testPoolFunction(
            listOf(2, 1, 0, -2, 0, -1, 0, 1, 4, 1, 0, -2, 0, 4),
            32)

    // TODO fix this
    testPoolFunction(
            listOf(2, 1, 0, -2, 0, -1, 0, 1, 4, 1, 0, -2, 0, 2),
            24)


    // Missing test: multiple maximums on the data
    // Missing test: max is first or last value



    println("All good!!!")

//	var data =
//			ObjectMapper()
//					.registerModule(KotlinModule())
//					.readValue(File(args[0]), FloodData::class.java)


}

private fun testPoolFunction(testData: List<Int>, expected: Int) {
    var poolVolume: Int = calculatePools(testData)
    if (poolVolume != expected)
        throw RuntimeException("Wrong volume, got $poolVolume expected $expected")
}

// TODO: remember to skip the start and end slopes
// TODO: always towards highest point (from left / right)
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