import isel.leic.utils.Time
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger


/**
 * Envia tramas para os diferentes módulos Serial Receiver.
 */
object SerialEmitter {
    /**
     * Contem os modulos que podem receber uma trama em serie
     */
    enum class Destination {
        LCD, ROULETTE
    }

    /**
     * Define o índice do sinal SS
     */
    var SS_LCD_ID = 0

    /**
     * Define o índice do sinal SDX
     */
    var SDX_LCD_ID = 3

    /**
     * Define o índice do sinal SCLK
     */
    var SCLK_LCD_ID = 4

    /**
     * Inicia a class
     */
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
        } catch(_:Exception){
            Logger.getLogger("SerialEmitter").log(Level.WARNING, "No config file found, using default values")
        }
    }

    /**
     * Faz o set up necessário para o envio de uma trama em série para a tela LCD
     * @param data
     * @param size
     */
    private fun sendLCD(data: Int, size: Int) {
        HAL.writeBits(0b1111_1111, 0b0000_0011)
        HAL.clrBits(pow(2, 0))
        // println("Sending to LCD: $data")
        parseAndSend(data, size, 5)

        HAL.setBits(pow(2, 0))

    }

    /**
     * Faz o set up necessário para o envio de uma trama em série para o Roulette
     * @param data
     * @param size
     */
    private fun sendRoulette(data: Int, size: Int) {
        HAL.writeBits(0b1111_1111, 0b0000_0011)
        HAL.clrBits(pow(2, 1))

        parseAndSend(data, size)

        HAL.setBits(pow(2, 1))
    }

    /**
     * Chama a função responsável por enviar cada tipo de trama
     * @param addr define o destinatário da trama
     * @param data
     * @param size
     */
    fun send(addr: Destination, data: Int, size: Int) {
        if (addr == Destination.LCD) {
            sendLCD(data, size)
        } else if (addr == Destination.ROULETTE) {
            sendRoulette(data, size)
        }
    }

    /**
     *Efetua o envio final dos dados em serie e do SDLK, e o calculo do bit de paridade
     * @param data
     * @param size
     * @param time
     */
    private fun parseAndSend(data: Int, size: Int, time: Long = 0) {

        Time.sleep(time)
        val p = if (data.countOneBits() % 2 == 0) 1 else 0

        for (i in 0..size){
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