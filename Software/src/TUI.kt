import isel.leic.utils.Time

object TUI {

    fun writeSplited(text: String) {

        var count = 0
        var words = text.split(Regex("(?<=\\s)|(?=\\s)"))
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

    fun writeRight(text: String) {

        val newText = " ".repeat(LCD.COLS - text.length) + text

        var count = 0
        for (c in newText) {
            if (count != 0 && count % LCD.COLS == 0) {
                LCD.cursor(1, 0)
                count = 0
            }
            LCD.write(c)
            count++
        }
    }


    fun writeCenter(text: String) {

        val words = text.split(Regex(" +"))
        var line = ""

        for (word in words) {
            if (line.length + word.length >= LCD.COLS) {
                val padding = (LCD.COLS - line.length) / 2
                val centeredLine = " ".repeat(padding) + line.trim()
                for (i in centeredLine)
                    LCD.write(i)
                LCD.cursor(1, 0)
                line = ""
            }

            if (line.isNotEmpty()) {
                line += " "
            }
            line += word
        }

        if (line.isNotEmpty()) {
            val padding = (LCD.COLS - line.length) / 2
            val centeredLine = " ".repeat(padding) + line.trim()
            for (i in centeredLine)
                LCD.write(i)
        }
    }

    fun loadingScreen(time: Long, condition: () -> Boolean) {
        LCD.clear()
        writeCenter("Loading")

        var i = 0
        while (!condition()) {
            LCD.write(".")

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

    fun writeWalkText(time: Long, text: String) {
        for (a in 0..text.length / 40) {
            val newText = text.subSequence(0, ((a + 1) * 40).coerceIn(0, text.length)).toString()
            for (i in a * 40 + 1 until (a + 1) * 40 + 1) {
                if (LCD.COLS - i >= 0) {
                    LCD.cursor(0, LCD.COLS - i)
                    write(newText, false)
                    var count = 0
                    for (c in newText) {
                        LCD.write(c)
                        count++
                    }
                } else {
                    write(newText.drop(-(LCD.COLS - i)), false)
                    var count = 0
                    for (c in newText) {
                        LCD.write(c)
                        count++
                    }
                }
                Time.sleep(time)
                LCD.clear()
            }
        }
        Time.sleep(time)
    }

    private fun write(text: String, wrap: Boolean = true) {
        var count = 0
        for (c in text) {
            if (wrap && count != 0 && count % LCD.COLS == 0) {
                LCD.cursor(1, 0)
                count = 0
            }
            LCD.write(c)
            count++
        }
    }
}