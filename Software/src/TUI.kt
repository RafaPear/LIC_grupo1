import TUI.writeCenterLine
import TUI.writeRightLine
import isel.leic.utils.Time

object TUI {
    private var canWrite: Boolean = true

    const val NONE = KBD.NONE
    const val COLS = LCD.COLS
    const val LINES = LCD.LINES

    fun init() {
        LCD.init()
        KBD.init()
        RouletteDisplay.init()
        RouletteDisplay.clrAll()
        Time.sleep(100)
    }

    /**
     * Pula para a para a segunda linha da tela LCD
     */
    fun nextLine() {
        LCD.cursor(1, 0)
    }

    /**
     * Escreve um [Char] no LCD
     * @param ch
     * @param wrap quando 'false' não utiliza o salto automático de linha
     */
    fun write(ch: Char,wrap: Boolean = true, line: Int = LCD.cursorPos.first,
                column: Int = LCD.cursorPos.second
    ){
        if (line !in 0..1 || column > LCD.COLS) error("invalid line or column")
        LCD.cursor(line,column)
        LCD.write(ch,wrap)
    }

    /**
     * Escreve uma [String] no LCD
     * @param line1
     * @param wrap quando 'false' não utiliza o salto automático de linha
     */
    fun write(line1: String, wrap: Boolean = true, line: Int = LCD.cursorPos.first,
                column: Int = LCD.cursorPos.second) =
        if (line1.length > LCD.COLS * 2)
            error("String very Big")
        else {
            LCD.cursor(line,column)
            LCD.write(line1, wrap)
        }

    /**
     * È um whilhe true que captura as teclas e as impremie na tela LCD
     */
    fun captureAndPrint() {
        while (true) {
            val key = KBD.getKey()
            if (key == '*' && canWrite) {
                LCD.clear()
                canWrite = false
            } else if (canWrite && key != NONE) {
                println("Key: $key")
                LCD.write(key)
                key == NONE
                canWrite = false
            } else if (key == NONE)
                canWrite = true
        }
    }

    /**
     * captura a tecla e retorna esta mesma
     * @return [Char]
     */
    fun capture(): Char {
        val key = KBD.getKey()
        return if (canWrite && key != NONE) {
            println("Key: $key")
            canWrite = false
            key
        } else if (key == NONE) {
            canWrite = true
            key
        }
        else NONE
    }

    /**
     * captura a tecla e retorna esta mesma
     * @return [Char]
     */
    fun waitForCapture(timeout: Long): Char {
        val key = KBD.waitKey(timeout)
        return key
    }

    /**
     * Quebra a [String] e escreve-a nas duas linhas da tela LCD
     * @param text
     */
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

    /**
     * Quebra se necessário a [String] e chama [writeRightLine]
     * @param line1
     */
    fun writeRight(line1: String) {
        if (line1.length > LCD.COLS * 2) error("String very Big")
        if (line1.length > LCD.COLS) {
            // se a String for maior que a primeira linha ele quebra e escreve nas duas linhas
            writeRightLine(line1.substring(0, LCD.COLS),0)
            writeRightLine(line1.substring(LCD.COLS, line1.length),1)
        } else writeRightLine(line1)
    }

    /**
     * Escreve o [text] o mais deslocado para a direita possível
     * @param text
     */
    fun writeRightLine(text: String,line: Int = 0) {
        if (line !in 0..1 || text.length >= COLS) error("invalid line or text")
        var column = COLS - text.length
        refreshPixels(text,line,column)
    }

    /**
     * Se necessário quebra a [String] em dois para poder escrever centrado, chamando o método [writeCenterLine]
     * @param line1
     */
    fun writeCenter(line1: String) {
        if (line1.length > LCD.COLS * 2) error("String verry Big")
        if (line1.length > LCD.COLS) {
            // se a String for maior que a primeira linha ele quebra e escreve nas duas linhas
            writeCenterLine(line1.substring(0, LCD.COLS), 0)
            writeCenterLine(line1.substring(LCD.COLS, line1.length), 1)
        } else writeCenterLine(line1)
    }

    /**
     * Escreve a [String] centrada na linha definida em [line]
     * @param line1
     * @param line
     */
    fun writeCenterLine(line1: String, line: Int = 0) {
        if (line !in 0..1) error("invalid line")

        val cols = LCD.COLS
        val listChar = CharArray(cols) // cada elemento do array corresponde a uma coluna do LCD

        //serve para ajustar quando a frase for ímpar e não ficar exatamente no centro
        val parity = if (line1.length % 2 == 0) 0 else 1

        for (i in line1.indices) {//adiciona a frase a lista
            listChar[i] = line1[i]
        }

        var leftSize = 0
        var rightSize = listChar.lastIndex - line1.lastIndex

        while (true) {//move para a direita a frase até estar centrada
            if (leftSize == rightSize - parity) break
            moveStrInArray(listChar)
            leftSize++
            rightSize--
        }

        LCD.cursor(line, leftSize)
        LCD.write(line1, false)
    }

    private fun moveStrInArray(line1: CharArray, dir: Int = 1) {
        if (dir != -1 && dir != 1) error("invalid direction")
        val lastCH = line1.indexOfLast { it != '\u0000' }
        if (lastCH != line1.lastIndex) {
            for (i in lastCH downTo 0) {
                line1[i + dir] = line1[i]
                line1[i] = '\u0000'
            }
        }
    }

    /**
     * Quria uma animação de screen de load. Para quando a condição for verdadeira
     * @param time
     * @param condition
     */
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

    /**
     * Cria uma animação de um texto que se desloca da direita para a esquerda.
     * @param line1
     * @param time velocidade a qual o texto irá andar
     * @param breakKey tecla a qual irá quebra o loop
     */
    fun writeWalkText(line1: String, time: Long = 200, breakKey: Char) {
        var window = ""
        line1.padEnd(LCD.COLS, ' ')

        for (i in 0 until LCD.COLS - 1) {
            window += " "
        }
        window += line1

        for (i in 0 until LCD.COLS - 1) {
            window += " "
        }
        var l = 0
        var r = LCD.COLS - 1

        while (true) {
            if (KBD.getKey() == breakKey) return
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

    fun confirmMenu(
        question: String = "Continue?",
        yes: String = "Yes",
        no: String = "No",
        yesKey: Char = 'A',
        noKey: Char = 'B'
    ): Boolean {
        clear()
        writeCenterLine(question, 0)
        writeCenterLine("$yes($yesKey)  $no($noKey)", 1)

        while (true) {
            val key = capture()
            if (key == yesKey) return true
            else if (key == noKey) return false
        }
    }

    fun switchScreen(list: Array<String>, sleep: Long, wrt: (l: String) -> Unit) {
        for (line in list) {
            wrt(line)
            Time.sleep(sleep)
            clear()
        }
    }

    fun clearWrite(line: String, wrap: Boolean = true) {
        clear()
        writeSplited(line)
    }

    /**
     * limpa a tela LCD
     */
    fun clear(){
        LCD.clear()
    }

    /**
     * valida a tecla
     * @param key
     */
    fun isValid(key: Char): Boolean = key != NONE

    /**
     * Limpa a tela LCD, e em seguinda executa o lambda [write1] para escrever na tela, mesma coisa para o [write2]
     * ambos em linhas diferentes
     * @param write1 uma função de extensão do TUI
     * @param write2 uma função de extensão do TUIa
     */
    fun refresh(write1: TUI.()-> Unit = {},write2: TUI.()-> Unit = {}) {
        clear()
        write1()
        nextLine()
        write2()
    }

    fun refreshPixel(ch: Char, line: Int = LCD.cursorPos.first, column: Int = LCD.cursorPos.second
    ){
        if (line !in 0..< LCD.LINES || column !in 0..< LCD.COLS) error("invalid line or column")
        val temp_cursor = LCD.cursorPos
        LCD.cursor(line,column)
        write(ch)
        LCD.cursor(temp_cursor.first,temp_cursor.second)
    }

    fun refreshPixels(ch: String, line: Int = LCD.cursorPos.first, column: Int = LCD.cursorPos.second) {
        if (line !in 0..< LCD.LINES || column !in 0..< LCD.COLS) error("invalid line or column")
        if (!hasSpace(ch,column)) error("Very Big")
        val temp_cursor = LCD.cursorPos
        LCD.cursor(line,column)
        write(ch)
        LCD.cursor(temp_cursor.first,temp_cursor.second)
    }

    fun clearChar() {
        refreshPixel(' ',LCD.cursorPos.first, LCD.cursorPos.second-1)
        LCD.cursor(LCD.cursorPos.first,LCD.cursorPos.second-1)
    }

    fun showCursor(on: Boolean) {
        LCD.showCursor(on)
    }

    fun hasSpace(ch: String,column: Int = LCD.cursorPos.second): Boolean = ch.length + column <= COLS

    fun clearLine(line: Int) {
        if (line !in 0..1) error("invalid line")
        val clean = ' '
        LCD.cursor(line,0)
        for (i in 0..< COLS){
            LCD.write(clean)
        }
    }
}