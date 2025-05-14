import isel.leic.utils.Time

object TUI {
    private var canWrite: Boolean = true

    fun init() {
        LCD.init()
        KBD.init()
    }

    fun write(str: String) =
        if (str.length > LCD.COLS * 2)
            error("String very Big")
        else LCD.write(str)

    fun capture() {
        while (true) {
            val key = KBD.getKey()

            if (key == '*' && canWrite) {
                LCD.clear()
                canWrite = false
            } else if (canWrite && key != KBD.NONE) {
                LCD.write(key)
                key == KBD.NONE
                canWrite = false
            } else if (key == KBD.NONE)
                canWrite = true
        }
    }

    fun writeSplited(text: String) {
        if (text.length > LCD.COLS * 2) error("String verry Big")
        var count = 0
        val words = text.split(Regex("(?<=\\s)|(?=\\s)"))
        for (word in words) {
            if (count + word.length > LCD.COLS) {
                LCD.cursor(1, 0)
                count = 0
            }
            for (i in word)
                LCD.write(i)
            count += word.length
        }
    }

    fun writeRight(str: String) {
        if (str.length > LCD.COLS * 2) error("String very Big")
        if (str.length > LCD.COLS) {
            // se a String for maior que a primeira linha ele quebra e escreve nas duas linhas
            writeRightLine(str.substring(0, LCD.COLS))
            writeRightLine(str.substring(LCD.COLS, str.length))
        } else writeRightLine(str)
    }
    private fun writeRightLine(text: String) {
        val newText = " ".repeat(LCD.COLS - text.length) + text
        for (c in newText) {
            LCD.write(c)
        }
    }

    fun writeCenter(str: String) {
        if (str.length > LCD.COLS * 2) error("String verry Big")
        if (str.length > LCD.COLS) {
            // se a String for maior que a primeira linha ele quebra e escreve nas duas linhas
            writeCenterLine(str.substring(0, LCD.COLS), 0)
            writeCenterLine(str.substring(LCD.COLS, str.length), 1)
        } else writeCenterLine(str)
    }

    private fun writeCenterLine(str: String, line: Int = 0) {
        if (line !in 0..1) error("invalid line")

        val cols = LCD.COLS
        val listChar = CharArray(cols) // cada elemento do array corresponde a uma coluna do LCD

        //serve para ajustar quando a frase for ímpar e não ficar exatamente no centro
        val parity = if (str.length % 2 == 0) 0 else 1

        for (i in str.indices) {//adiciona a frase a lista
            listChar[i] = str[i]
        }

        var leftSize = 0
        var rightSize = listChar.lastIndex - str.lastIndex

        while (true) {//move para a direita a frase até estar centrada
            if (leftSize == rightSize - parity) break
            moveStrInArray(listChar)
            leftSize++
            rightSize--
        }

        LCD.cursor(line, leftSize)
        LCD.write(str, false)
    }

    private fun moveStrInArray(str: CharArray, dir: Int = 1) {
        if(dir != -1 && dir != 1) error("invalid direction")
        val lastCH = str.indexOfLast { it != '\u0000' }
        if (lastCH != str.lastIndex) {
            for (i in lastCH downTo 0) {
                str[i + dir] = str[i]
                str[i] = '\u0000'
            }
        }
    }

    fun loadingScreen(time: Long = 200, condition: () -> Boolean) {
        LCD.clear()
        writeCenter("Loading")

        var i = 0
        while (!condition()) {
            LCD.write('.', false)

            if (i == 3) {
                i = -1
                LCD.clear()
                writeCenter("Loading")
            }
            i++
            Time.sleep(time)
        }
        LCD.clear()
    }

    fun writeWalkText(str: String,time: Long = 200) {
        var window = ""

        for (i in 0 until LCD.COLS - 1) {
            window += " "
        }
        window += str

        for (i in 0 until LCD.COLS - 1) {
            window += " "
        }
        var l = 0
        var r = LCD.COLS - 1

        while (true) {
            if (r == window.length) {
                r = LCD.COLS - 1
                l = 0
            }
            for (i in l..r) {
                LCD.write(window[i], false)
            }
            l++
            r++
            Time.sleep(time)
            LCD.clear()
        }
    }
}