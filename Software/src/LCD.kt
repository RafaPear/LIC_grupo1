import isel.leic.utils.Time

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
        // Envia rs
       if (rs) HAL.setBits(RS_MASK) else HAL.clrBits(RS_MASK)

        Time.sleep(1)

        // Envia E On
        HAL.setBits(E_MASK)

        Time.sleep(1)
        // Envia data
        HAL.writeBits(NIBBLE_MASK, data)

        Time.sleep(1)
        // Envia E Off
        HAL.clrBits(E_MASK)

        Time.sleep(1)
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
        if (SERIAL_INTERFACE)
            writeNibbleSerial(rs, data)
        else
            writeNibbleParallel(rs, data)
    }

    // Escreve um byte de comando/dados no LCD.
    private fun writeByte(rs: Boolean, data: Int) {
        writeNibble(rs, data.rotateRight(4))
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
    fun init() {
        HAL.init()
        val time_list = longArrayOf(15L,5L,1L)
        val init_Code = intArrayOf(
            0b0000_0011,
            0b0000_0010,
            0b0000_0010,
            0b0000_1000,
            0b0000_0000,
            0b0000_1000,
            0b0000_0000,
            0b0000_0001,
            0b0000_0000,
            0b0000_0111,
            0b0000_0001, // Clear display
            0b0000_0010, // Return home
            0b0000_1111 /// Cursor On / Blinking On
        )
        for (time in time_list){
            Time.sleep(time)
            writeCMD(init_Code[0])
            ///Time.sleep(1000)
        }
        for(i in 1..<init_Code.size){
            Time.sleep(1)
            writeCMD(init_Code[i])
            //Time.sleep(1000)
        }
    }

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
    fun cursor(line: Int, column: Int) {
        if (line in 0 until LINES && column in 0 until COLS) {
            val address = when (line) {
                0 -> column // 1.ª linha
                1 -> (column + 0b0100_0000) // 0b0100_0000 -> 64, passa para a próx. linha
                else -> 0
            }
            writeCMD(address)
            println("Posição do Cursor: $line, $column")
        }
        else {
            throw IllegalArgumentException ("Posição Inválida.")
        }
    }

    // Envia comando para limpar o ecrã e posicionar o cursor em (0,0).
    fun clear() { /* Implementação */
        writeCMD(1)
        cursor(0,0)
        println("LCD limpo e cursor posicionado em (0,0)")
    }
}
