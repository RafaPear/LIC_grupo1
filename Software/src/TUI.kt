import isel.leic.utils.Time

object TUI {
    private var canWrite: Boolean = true

    fun init() {
        LCD.init()
        KBD.init()
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
    fun write(ch: Char,wrap: Boolean = true){
        LCD.write(ch,wrap)
    }

    /**
     * Escreve uma [String] no LCD
     * @param line1
     * @param wrap quando 'false' não utiliza o salto automático de linha
     */
    fun write(line1: String,wrap: Boolean = true) =
        if (line1.length > LCD.COLS * 2)
            error("String very Big")
        else LCD.write(line1,wrap)

    /**
     * È um whilhe true que captura as teclas e as impremie na tela LCD
     */
    fun captureAndPrint() {
        while (true) {
            val key = KBD.getKey()
            if (key == '*' && canWrite) {
                LCD.clear()
                canWrite = false
            } else if (canWrite && key != KBD.NONE) {
                println("Key: $key")
                LCD.write(key)
                key == KBD.NONE
                canWrite = false
            } else if (key == KBD.NONE)
                canWrite = true
        }
    }

    /**
     * captura a tecla e retorna esta mesma
     * @return [Char]
     */
    fun capture(): Char {
        val key = KBD.getKey()
        return if (canWrite && key != KBD.NONE) {
            println("Key: $key")
            canWrite = false
            key
        } else if (key == KBD.NONE) {
            canWrite = true
            key
        }
        else KBD.NONE
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
            writeRightLine(line1.substring(0, LCD.COLS))
            writeRightLine(line1.substring(LCD.COLS, line1.length))
        } else writeRightLine(line1)
    }

    /**
     * Escreve o [text] o mais deslocado para a direita possível
     * @param text
     */
    private fun writeRightLine(text: String) {
        val newText = " ".repeat(LCD.COLS - text.length) + text
        for (c in newText) {
            LCD.write(c)
        }
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
    fun isValid(key: Char): Boolean = key != KBD.NONE
}