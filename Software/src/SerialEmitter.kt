import isel.leic.utils.Time
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

// Envia tramas para os diferentes módulos Serial Receiver.
object SerialEmitter {
    enum class Destination {
        LCD, ROULETTE
    }

    var SS_LCD_ID = 0
    var SDX_LCD_ID = 3
    var SCLK_LCD_ID = 4

    // Inicia a classe
    fun init() {
        HAL.init()

        try {
            val file = File(HAL.configPath)
            SS_LCD_ID = 0
            SDX_LCD_ID = 0
            SCLK_LCD_ID = 0

            file.forEachLine { i ->
                if (i.contains("srl./SS")) {
                    SS_LCD_ID += getOutputPins(i)
                } else if (i.contains("srl.SDX")) {
                    SDX_LCD_ID += getOutputPins(i)
                } else if (i.contains("srl.SCLK")) {
                    SCLK_LCD_ID += getOutputPins(i)
                }
            }
        } catch(e:Exception){
            Logger.getLogger("SerialEmitter").log(Level.WARNING, "No config file found, using default values")
        }
    }

    private fun sendLCD(data: Int, size: Int) {
        HAL.writeBits(0b1111_1111, 0b0000_0011)
        HAL.clrBits(pow(2, 0))

        parseAndSend(data, size, 1)

        HAL.setBits(pow(2, 0))

    }

    private fun sendRoulette(data: Int, size: Int) {
        HAL.writeBits(0b1111_1111, 0b0000_0011)
        HAL.clrBits(pow(2, 1))

        parseAndSend(data, size)

        HAL.setBits(pow(2, 1))
    }

    // Envia uma trama para o Serial Receiver
    // identificado no destino em ‘addr’,
    // os bits de dados em ‘data’
    // e em ‘size’ o número de bits a enviar.
    fun send(addr: Destination, data: Int, size: Int) {
        if (addr == Destination.LCD) {
            sendLCD(data, size)
        } else if (addr == Destination.ROULETTE) {
            sendRoulette(data, size)
        }
    }

    fun parseAndSend(data: Int, size: Int, time: Long = 0) {

        val p = if (data.countOneBits() % 2 == 0) 1 else 0

        for (i in 0..size){
            Time.sleep(time)
            if (i == size) {
                if (p.isBit(0))
                    HAL.setBits(pow(2, 3))
                else
                    HAL.clrBits(pow(2, 3))
            }
            else {
                if (data.isBit(i)) {
                    HAL.setBits(pow(2, 3))
                }
                else {
                    HAL.clrBits(pow(2, 3))
                }
            }
            Time.sleep(time)

            HAL.setBits(pow(2, 4))
            Time.sleep(time)

            HAL.clrBits(pow(2, 4))
        }
    }
}
