import java.io.File
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun main(args: Array<String>) {


    var bytes = File(args[0]).readBytes().toList()
    bytes
            .mapIndexed { i, _ -> bytes.subList(i, min(i+16, bytes.size)) }
            .filter { it.toSet().size == 16 }
            .forEach { println(String(it.toByteArray())) }

return

    println("Please give the original base64 coded signal as input")
    // TODO maybe we should allow e.g. multiple space on word
    toCharList(
            filterAgainstRule(
                    chunkCombinations(
//                    replaceChars(
                            readfile(args)
//                    )
                    )
            ))
                    .forEach { println(String(it.toCharArray()) ) }
println("Please base64 decode above")
}


fun toCharList(intList: List<List<Int>>): List<List<Char>> =
    intList.map { it.map { asInt -> asInt.toChar() }}

fun removeChars(chars: List<Int>)
    = chars.filter { it >= 32 }

fun replaceChars(chars: List<Int>) =
        chars.map { if (it < 32) 32 else it }

private fun readfile(args: Array<String>): List<Int> {
    return File(args[0])
            .readBytes()
            .toList()
            .map { if (it.toInt() < 0) it.toInt() + 255 else it.toInt()  }
}

fun asString(byteList: List<Int>) = byteList.joinToString(",")

fun filterAgainstRule(chunks: List<List<Int>>) =
//        chunks.filter { it.toSet().size == 16 - it.filter { char -> char.equals(' ') }.size   }
    chunks.filter { it.toSet().size == 16 }



fun chunkCombinations(text: List<Int>): List<List<Int>> =
    text.mapIndexed { index, _ -> text.subList(index, min(index+16, text.size)) }

fun chunkCombinations2(text: List<Int>): List<List<Int>> {
    var wordList = mutableListOf<List<Int>>()
    for (startIndex in 0..text.size - 16) {
        wordList.add(text.subList(startIndex, startIndex + 16))
    }
    return wordList
}

fun traverseInChunks(grepAsciiPrintable: List<Char>) {

    for (startIndex in 0..grepAsciiPrintable.size - 16) {
        var word = grepAsciiPrintable.subList(startIndex, startIndex + 16)
        if (word.toSet().size == 16)
            println(String(word.toCharArray()))
    }

}

fun chunkTo16BytesThatMatch(chars: List<Char>): List<List<Char>> {
    return chars.chunked(16)
            .map { String(it.toCharArray()).toLowerCase() }
            .map { it.toCharArray().toList() }
            .filter { it.toSet().size == 16 }
}


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

fun grepAsciiPrintable(file: String): List<Char> {
    var filter: List<Char> = File(file).readText(Charsets.US_ASCII)
            .toCharArray()
//            .filter { it.toInt() in 47..57 || it.toInt() in 65..90 || it.toInt() in 97..122 || it.toInt() == 43 }
            .filter { it.toInt() in 66..90 || it.toInt() in 97..122 || it.toInt() == 32 }

    return filter
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
                if (it < 0) it.toInt() + 256
                else it.toInt()
            }
            .forEach {
                println(it)
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

