import KBD.kPins
import java.io.File

fun readFile(path: String): List<String>{
    return File(path).readLines()
}

fun getInputPins(line: String): Int {
    var result = 0
    val badPins = line.split("->")[1]
        .trim()
        .removePrefix("UsbPort.I")
        .removeSurrounding("[", "]")
    try {
        result += pow(2, badPins.toInt())
    } catch (e: NumberFormatException) {
        val badPins = badPins.split("-")
        for (i in badPins[0].toInt()..badPins[1].toInt()) {
            result += pow(2, i)
        }
    }
    return result
}

fun getOutputPins(line: String): Int {
    var result = 0
    val badPins = line.split("->")[0]
        .trim()
        .removePrefix("UsbPort.O")
        .removeSurrounding("[", "]")
    try {
        result += pow(2, badPins.toInt())
    } catch (e: NumberFormatException) {
        val badPins = badPins.split("-")
        for (i in badPins[0].toInt()..badPins[1].toInt()) {
            result += pow(2, i)
        }
    }
    return result
}