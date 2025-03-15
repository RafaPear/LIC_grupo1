// Ler teclas. Funcoes retornam '0'..'9','A'..'D','#','*' ou NONE.
object KBD {
    const val NONE = 0;
    val CHAR_LIST = charArrayOf(
        '1', '2', '3', 'A',
        '4', '5', '6', 'B',
        '7', '8', '9', 'C',
        '*', '0', '#', 'D'
    )

    // Inicia a classe
    fun init() {

    }

    // Retorna de imediato a tecla premida ou NONE se nao ha tecla premida.
    fun getKey(): Char {
        return if (HAL.isBit(0b0001_0000))
            CHAR_LIST[HAL.readBits(0b0000_1111)]
        else NONE.toChar()
    }

    // Retorna a tecla premida, caso ocorra antes do 'timeout' (em milissegundos),
    // ou NONE caso contrario.
    fun waitKey(timeout: Long): Char {
        var time = timeout
        while (time > 0) {
            val key = getKey()
            if (key != NONE.toChar()) {
                return key
            }
            Thread.sleep(1)
            time--
        }
        return NONE.toChar()
    }
}
