import java.io.File
import kotlin.text.toInt

fun readFile(path: String): List<String>{
    return File(path).readLines()
}

fun parsePins(pins: String): Int{
    var result = 0
    try {
        result += pow(2, pins.toInt())
    } catch (e: NumberFormatException) {
        val badPins = pins.split("-")
        for (i in badPins[0].toInt()..badPins[1].toInt()) {
            result += pow(2, i)
        }
    }
    return result
}

fun getInputPins(line: String): Int {
    val badPins = line.split("->")[1]
        .trim()
        .removePrefix("UsbPort.I")
        .removeSurrounding("[", "]")
    return parsePins(badPins)
}

fun getOutputPins(line: String): Int {
    val badPins = line.split("->")[0]
        .trim()
        .removePrefix("UsbPort.O")
        .removeSurrounding("[", "]")
    return parsePins(badPins)
}