import java.io.File
import kotlin.random.Random

fun main(args: Array<String>) {

    var humanReadableChars = grepHumanReadable(args[0])

    traverseIn16byteChunks(humanReadableChars)

//findDividers()
//grepAscii(args[0])
//toSymbols(args[0])
// findPattern(args[0])
}

// TODO currently skips numbers and spaces
fun traverseIn16byteChunks(humanReadableChars: List<Char>) {

    var iterationIndex = 0

    while (iterationIndex < humanReadableChars.size) {
        var collectedChars: MutableList<Char> = mutableListOf()
        var nextCharIndex = iterationIndex++

        var asString = ""
        while (asString.toByteArray().size < 16 && nextCharIndex < humanReadableChars.size) {
            val nextCharToAdd = humanReadableChars.get(nextCharIndex++)

            // Do not add if that char is already there
            if (!collectedChars.contains(nextCharToAdd)) {
                collectedChars.add(nextCharToAdd)
                asString = String(collectedChars.toCharArray())
            }
        }

        if (asString.toByteArray().size == 16)
            println(asString)
    }

}

fun grepHumanReadable(file: String): List<Char> {

    var filter: List<Char> = File(file).readText(Charsets.UTF_8)
            .toCharArray()
            .filter { it.isLetter() || it.equals(' ') }

    // print(String(filter.toCharArray()))

    return filter
}

fun findPattern(file: String) {

    val whole = File(file)
            .readBytes()
            .toList()

    val concatString = whole.joinToString(separator = "/") { it.toInt().toString() }

    // Take 10 random samples
    for (i in 1..100000) {
        val sampleLenght = 5
        val startPost = Random.nextInt(whole.size - sampleLenght)
        val part = whole.subList(startPost, startPost + sampleLenght)
        val partConcat = part.joinToString(separator = "/") { it.toInt().toString() }

        var findAll: Sequence<MatchResult> = partConcat.toRegex().findAll(concatString)
        var matches = findAll.toList().size

        if (matches > 1)
            println(
                    String(partConcat.split("/")
                            .toList()
                            .map { it.toInt().toByte() }
                            .toList().toByteArray()))
    }
}

fun toSymbols(file: String) {

    File(file).readBytes().toList()
            .chunked(2)
            .forEach {
                print("-" + String(it.toByteArray()))
            }


}

fun grepAscii(file: String) {
    val l = File(file).readBytes()
//            .map { as255(it) }
//            .forEach {
//                print( "" + it + " " )
//            }
            .toList()

    print(
            String(l.toByteArray(), Charsets.UTF_8))
}


fun findDividers() {
    val l = 9612
    for (i in 1..9612) {
        if (l % i == 0)
            println(i)
    }

}


fun asPositiveNumbers(filename: String) {
    File(filename).readBytes()
            .map {
                if (it >= 0)
                    it
                else
                    256 - it
            }
            .forEach {
                print(" ")
            }

}

// print asciis that represent numbers
fun grepNumbers(filename: String) {
    File(filename).readBytes()
            .filter { it > 48 && it < 58 }
            .forEach {
                print(it.toChar() + " ")
            }

}

