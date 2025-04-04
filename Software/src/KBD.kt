import isel.leic.utils.Time

// Ler teclas. Funcoes retornam '0'..'9','A'..'D','#','*' ou NONE.
object KBD {
	const val NONE = 0.toChar();
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

		else NONE
	}

	fun getKeyVal(): Int {
		return if (HAL.isBit(0b0001_0000))
			HAL.readBits(0b0000_1111)
		else -1
	}

	// Retorna a tecla premida, caso ocorra antes do 'timeout' (em milissegundos),
	// ou NONE caso contrario.
	fun waitKey(timeout: Long): Char {
		val startTime = Time.getTimeInMillis()
		while (startTime + timeout > Time.getTimeInMillis()) {
			val key = getKey()
			if (key != NONE) {
				return key
			}
		}
		return NONE
	}

	fun waitKeyVal(timeout: Long): Int {
		val startTime = Time.getTimeInMillis()
		while (startTime + timeout > Time.getTimeInMillis()) {
			val key = getKeyVal()
			if (key != -1) {
				return key
			}
		}
		return -1
	}
}
