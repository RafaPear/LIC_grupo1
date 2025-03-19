// Escreve no LCD usando a interface a 4 bits.
object LCD {
    // Dimensão do display.
    private const val LINES = 2
    private const val COLS = 16

    // Define se a interface é Série ou Paralela.
    private const val SERIAL_INTERFACE = false

    // Escreve um nibble de comando/dados no LCD em paralelo.
    private fun writeNibbleParallel(rs: Boolean, data: Int) { /* Implementação */ }

    // Escreve um nibble de comando/dados no LCD em série.
    private fun writeNibbleSerial(rs: Boolean, data: Int) { /* Implementação */ }

    // Escreve um nibble de comando/dados no LCD.
    private fun writeNibble(rs: Boolean, data: Int) {
        if (SERIAL_INTERFACE)
            writeNibbleSerial(rs, data)
        else
            writeNibbleParallel(rs, data)
    }

    // Escreve um byte de comando/dados no LCD.
    private fun writeByte(rs: Boolean, data: Int) {

    }

    // Escreve um comando no LCD.
    private fun writeCMD(data: Int) { /* Implementação */ }

    // Escreve um dado no LCD.
    private fun writeDATA(data: Int) { /* Implementação */ }

    // Envia a sequência de iniciação para comunicação a 4 bits.
    fun init() { /* Implementação */ }

    // Escreve um caractere na posição corrente.
    fun write(c: Char) { /* Implementação */ }

    // Escreve uma string na posição corrente.
    fun write(text: String) { /* Implementação */ }

    // Envia comando para posicionar o cursor.
    // (line: 0..LINES-1, column: 0..COLS-1)
    fun cursor(line: Int, column: Int) { /* Implementação */ }

    // Envia comando para limpar o ecrã e posicionar o cursor em (0,0).
    fun clear() { /* Implementação */ }
}
