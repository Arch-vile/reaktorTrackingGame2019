import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.lang.RuntimeException
import kotlin.math.max

//DEPS com.fasterxml.jackson.core:jackson-core:2.10.1, com.fasterxml.jackson.core:jackson-databind:2.10.1, com.fasterxml.jackson.module:jackson-module-kotlin:2.10.1

fun main(args: Array<String>) {
	val testData = listOf(2,1,0,-2,0,-1,0,1,3)

	var poolVolume: Int = calculatePools(testData)
	var expected = 15
	if (poolVolume != expected)
		throw RuntimeException("Wrong volume, got $poolVolume expected $expected")

	println("All good!!!")

//	var data =
//			ObjectMapper()
//					.registerModule(KotlinModule())
//					.readValue(File(args[0]), FloodData::class.java)


}


// TODO: remember to skip the start and end slopes
// TODO: always towards highest point (from left / right)
fun calculatePools(data: List<Int>): Int {
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