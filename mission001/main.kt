import java.io.File
import java.nio.charset.Charset

fun main(args: Array<String>) {

//    grepNumbers(args[0])
//findDividers()
//grepAscii(args[0])
toSymbols(args[0])

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
                print(  " " )
            }

}
// print asciis that represent numbers
fun grepNumbers(filename: String) {
    File(filename).readBytes()
            .filter { it > 48 && it < 58 }
            .forEach {
                print( it.toChar() + " " )
            }

}

