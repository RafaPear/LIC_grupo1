
import java.io.File


/**
 * Função que lê um ficheiro e devolve uma lista de strings
 *
 * @param path caminho do ficheiro
 * @return [List] lista de strings
 * */
fun readFile(path: String): List<String>{
    return File(path).readLines()
}

fun parsePins(pins: String): Int{
    var result = 0
    try {
        result += pow(2, pins.toInt())
    } catch (_: NumberFormatException) {
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

/** Devolve a string com número par de caracteres, juntando um espaço se necessário. */
private fun String.even(): String =
    if (length.isEven()) this else "$this "

private fun Int.isEven() = this and 1 == 0

/** Constrói a linha de navegação (setas ou teclas) já centrada. */
fun navLine(
    text: String,
    prefix: String,           // " <" ou "(1)"
    suffix: String,           // "> " ou "(2)"
    isFirst: Boolean,
    isLast: Boolean,
    width: Int = LCD.COLS
): String {
    val txt = text.even()
    val gap = (width - txt.length) / 2

    val left  = prefix.padEnd(gap, ' ')
    val right = suffix.padStart(gap, ' ')

    return when {
        isFirst -> (txt + right).padStart(width, ' ')
        isLast  -> (left + txt).padEnd(width, ' ')
        else    -> left + txt + right
    }
}