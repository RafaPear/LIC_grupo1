// Escreve no LCD usando a interface a 4 bits.
object LCD {
    // Dimensão do display.
    private const val LINES = 2
    private const val COLS = 16
    private const val NIBBLE = 3

    // Define se a interface é Série ou Paralela.
    private const val SERIAL_INTERFACE = false

    // Escreve um nibble de comando/dados no LCD em paralelo.
    private fun writeNibbleParallel(rs: Boolean, data: Int) { /* Implementação */ }

    // Escreve um nibble de comando/dados no LCD em série.
    private fun writeNibbleSerial(rs: Boolean, data: Int) {
        // Envia E On
        HAL.setBits(1)
        // Envia rs
        HAL.setBits(0b0000_0001)
        // Envia data
        for (i in NIBBLE..0) {
            val bit = HAL.isBit(1.shl(i))
            HAL.writeBits(1, if (bit) 0b0000_0001 else 0b0000_0000)
        }
        // Envia E Off
        HAL.clrBits(1)
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

    }

    // Envia comando para posicionar o cursor.
    // (line: 0..LINES-1, column: 0..COLS-1)
    fun cursor(line: Int, column: Int) { /* Implementação */ }

    // Envia comando para limpar o ecrã e posicionar o cursor em (0,0).
    fun clear() { /* Implementação */ }
}
