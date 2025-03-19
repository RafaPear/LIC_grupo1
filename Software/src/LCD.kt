// Escreve no LCD usando a interface a 4 bits.
object LCD {
    // Dimensão do display.
    private const val LINES = 2
    private const val COLS = 16
    private const val NIBBLE = 3

    // Define se a interface é Série ou Paralela.
    private const val SERIAL_INTERFACE = false

    var E_MASK = if (SERIAL_INTERFACE) 1 else 0b0010_0000
    var RS_MASK = if (SERIAL_INTERFACE) 1 else 0b0001_0000
    var NIBBLE_MASK = if (SERIAL_INTERFACE) 1 else 0b0000_1111

    // Escreve um nibble de comando/dados no LCD em paralelo.
    private fun writeNibbleParallel(rs: Boolean, data: Int) {
        // Envia E On
        HAL.setBits(E_MASK)

        // Envia rs
        HAL.writeBits(RS_MASK, rs.toInt())
        HAL.writeBits(NIBBLE_MASK, data)

        // Envia E Off
        HAL.clrBits(E_MASK)
    }

    // Escreve um nibble de comando/dados no LCD em série.
    private fun writeNibbleSerial(rs: Boolean, data: Int) {
        // Envia E On
        HAL.setBits(E_MASK)
        // Envia rs
        HAL.writeBits(RS_MASK, rs.toInt())
        // Envia data
        for (i in NIBBLE..0) {
            val bit = data.isBit(i)
            HAL.writeBits(NIBBLE_MASK, bit.toInt())
        }
        // Envia E Off
        HAL.clrBits(E_MASK)
    }

    // Escreve um nibble de comando/dados no LCD.
    private fun writeNibble(rs: Boolean, data: Int) {
        if (data.countOneBits() < 5){
            if (SERIAL_INTERFACE)
                writeNibbleSerial(rs, data)
            else
                writeNibbleParallel(rs, data)
        }
    }

    // Escreve um byte de comando/dados no LCD.
    private fun writeByte(rs: Boolean, data: Int) {
        writeNibble(rs, data shr 4)
        writeNibble(rs, data and 0b0000_1111)
    }

    // Escreve um comando no LCD.
    private fun writeCMD(data: Int) {
        writeByte(false, data)
    }

    // Escreve um dado no LCD.
    private fun writeDATA(data: Int) {
        writeByte(true, data)
    }

    // Envia a sequência de iniciação para comunicação a 4 bits.
    fun init() { /* Implementação */ }

    // Escreve um caractere na posição corrente.
    fun write(c: Char) {
        writeDATA(c.code)
    }

    // Escreve uma string na posição corrente.
    fun write(text: String) {
        var count = 0
        for (c in text) {
            if (count == COLS) {
                cursor(1, 0)
                count = 0
            }
            write(c)
            count++
        }
    }

    // Envia comando para posicionar o cursor.
    // (line: 0..LINES-1, column: 0..COLS-1)
    fun cursor(line: Int, column: Int) { /* Implementação */
        if (line in 0 until LINES && column in 0 until COLS) {
            val address = when (line) {
                0 -> column // 1.ª linha
                1 -> (column + 0x40) // 0x40 -> 64, passa para a próx. linha
                else -> throw IllegalArgumentException("Linha Inválida.") // exceção
            }
            writeCMD(0x80 or address)
            println("Posição do Cursor: $line, $column")
        }
        else {
            throw IllegalArgumentException ("Posição Inválida.")
        }
    }

    // Envia comando para limpar o ecrã e posicionar o cursor em (0,0).
    fun clear() { /* Implementação */
        writeCMD(0x01)
        println("LCD limpo e cursor posicionado em (0,0)")
    }
}
