import isel.leic.utils.Time
import kotlin.text.iterator

// Escreve no LCD usando a interface a 4 bits.
object LCD {
	//para ter uma melhor noção por onde anda o cursor
	var cursorPos: Pair<Int,Int> = Pair(0,0) // (linha, coluna)

	// Dimensão do display.
	const val LINES = 2
	const val COLS = 16
	private const val NIBBLE = 3

	// Define se a interface é Série ou Paralela.
	private const val SERIAL_INTERFACE = false

	var E_MASK = if (SERIAL_INTERFACE) 1 else 0b0010_0000
	var RS_MASK = if (SERIAL_INTERFACE) 1 else 0b0001_0000
	var NIBBLE_MASK = if (SERIAL_INTERFACE) 1 else 0b0000_1111

	// Escreve um nibble de comando/dados no LCD em paralelo.
	private fun writeNibbleParallel(rs: Boolean, data: Int) {
		// Envia rs
        rs.toBit(RS_MASK)
    
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
		// Envia rs
		HAL.writeBits(RS_MASK, rs.toInt())
		// Envia E On
		HAL.setBits(E_MASK)
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
		val time_list = longArrayOf(15, 5, 1)
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
			0b0000_0110,

			0b0000_0001, // Clear display
			0b0000_0010, // Return home
			0b0000_1100 /// Cursor On / Blinking On
		)

		for (time in time_list) {
			Time.sleep(time)
			writeNibble(false, init_Code[0])
		}

		writeNibble(false, init_Code[1])

		for (i in 2 until init_Code.size) {
			writeByte(false, init_Code[i])
		}
	}

	// Escreve um caractere na posição corrente.
	fun write(c: Char, wrap: Boolean = true) {
		autoCursor(wrap)
		writeDATA(c.code)
	}

	// Escreve uma string na posição corrente.
	fun write(text: String, wrap: Boolean = true) {
		for (c in text) {
			write(c,wrap)
		}
	}



	// Envia comando para posicionar o cursor.
	// (line: 0..LINES-1, column: 0..COLS-1)
	fun cursor(line: Int, column: Int) {
		if (line in 0 until LINES && column in 0 until COLS) {
			val address = when (line) {
				0 -> (column + 0b1000_0000)
				1 -> (column + 0b1100_0000)
				else -> 0
			}
			cursorPos = Pair(line,column)
			writeCMD(address)
		} else {
			throw IllegalArgumentException("Posição Inválida.")
		}
	}

	// Envia comando para limpar o ecrã e posicionar o cursor em (0,0).
	fun clear() { /* Implementação */
		writeCMD(1)
		cursorPos = Pair(0,0)
	}

	fun autoCursor(wrap: Boolean) {
		if (wrap && cursorPos == Pair(0,16)) cursor(1,0)
		else cursorPos = cursorPos.copy(second = cursorPos.second + 1)
	}
}
