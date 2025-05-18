import isel.leic.utils.Time
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

// Ler teclas. Funcoes retornam '0'..'9','A'..'D','#','*' ou NONE.
object KBD {
	const val NONE = 0.toChar();
	val CHAR_LIST = charArrayOf(
		'1', '2', '3', 'A',
		'4', '5', '6', 'B',
		'7', '8', '9', 'C',
		'*', '0', '#', 'D'
	)
	var valPins = 0b0001_0000
	var kPins = 0b0000_1111

	// Inicia a classe
	fun init() {
		HAL.init()
		val file: File
		try {
			val file = File(HAL.configPath)
			valPins = 0
			kPins = 0

			file.forEachLine { i ->
				if (i.contains("ob.Q[0-3]")) {
					kPins += getInputPins(i)
				} else if (i.contains("ob.Dval")) {
					valPins += getInputPins(i)
				}
			}
		} catch(e:Exception){
			Logger.getLogger("KBD").log(Level.WARNING, "No config file found, using default values")
		}


	}

	// Retorna de imediato a tecla premida ou NONE se nao ha tecla premida.
	fun getKey(): Char {
		return if (HAL.isBit(valPins))
			CHAR_LIST[HAL.readBits(kPins)]

		else NONE
	}

	fun getKeyVal(): Int {
		return if (HAL.isBit(valPins))
			HAL.readBits(kPins)
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
