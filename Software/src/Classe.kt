import java.util.logging.Logger

class Classe : Iterable<Int> {
    private var arr : Array<Int?> = Array(RouletteDisplay.TOTAL_DISPLAYS) { null }

    val isFull: Boolean
        get() = index < 0

    var index = arr.size - 1
        get() = if (field < 0) 0 else field

    val charSet = ('A'..'F') + ('0'..'9')

    private enum class CharToInt(val value: Int) {
        A(10),
        B(11),
        C(12),
        D(13),
        E(14),
        F(15)
    }

    constructor(vararg args: Any) {
        if (args.size > arr.size) {
            Logger.getLogger("Classe").warning("Too many arguments")
            return
        }
        for (arg in args) {
            add(arg)
        }
    }

    fun add(value: Any) {
        if (isFull){
            Logger.getLogger("Classe").warning("Array is full")
            return
        }
        set(value = value)
        index--
    }

    fun remove() {
        arr[index++] = null
    }

    fun set(idx: Int = index, value: Any) {
        if (idx < 0 || idx >= arr.size) {
            Logger.getLogger("Classe").warning("Index out of bounds")
            return
        }
        if (validate(value)) {
            if (value is Int) {
                arr[idx] = value
            } else if (value is Char) {
                if (value.isDigit()) arr[idx] = value.digitToInt()
                else if (value.isLetter()) {
                    val charValue = when (value.uppercase()) {
                        "A" -> CharToInt.A.value
                        "B" -> CharToInt.B.value
                        "C" -> CharToInt.C.value
                        "D" -> CharToInt.D.value
                        "E" -> CharToInt.E.value
                        "F" -> CharToInt.F.value
                        else -> return
                    }
                    arr[idx] = charValue
                }
            }
        } else {
            Logger.getLogger("Classe").warning("Invalid value: $value")
        }
    }

    fun clear() {
        arr = Array(RouletteDisplay.TOTAL_DISPLAYS) { null }
        index = arr.size - 1
    }

    fun print() {
        println(arr.joinToString { it.toString() })
    }

    // Return iterator only wit ints and not nulls
    override fun iterator(): Iterator<Int> {
        return object : Iterator<Int> {
            private var currentIndex = 0

            override fun hasNext(): Boolean {
                while (currentIndex < arr.size && (arr[currentIndex] == null || arr[currentIndex] !is Int)) {
                    currentIndex++
                }
                return currentIndex < arr.size
            }

            override fun next(): Int {
                if (!hasNext()) throw NoSuchElementException()
                return arr[currentIndex++]!!
            }
        }
    }

    private fun validate(value: Any): Boolean {
        return when (value) {
            is Int -> {
                !(value < 0 || value > RouletteDisplay.MAX_VALUE)
            }
            is Char -> {
                value in charSet
            }
            else -> {
                false
            }
        }
    }
}

fun main() {
    val classe = Classe(1, 2, 3, 4, 5, 6)
    classe.print()
    classe.remove()
    classe.print()
    classe.add(7)
    classe.print()
}