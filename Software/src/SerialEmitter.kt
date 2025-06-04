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
     * Define o índice do sinal SS do LCD
     */
    var SS_LCD_ID = 0

    /**
     * Define o índice do sinal SS do Roulette Display
     */
    var SS_RD_ID = 1

    /**
     * Define o índice do sinal SDX
     */
    var SDX_ID = 3

    /**
     * Define o índice do sinal SCLK
     */
    var SCLK_ID = 4

    /**
     * Define o tempo em ms que o SerialEmitter espera
     * entra cada trama enviada
     * */
    const val LCD_SLEEP = 1L


    /**
     * Inicia a class
     */
    fun init() {
        HAL.init()

        try {
            val file = File(HAL.configPath)
            SS_LCD_ID = 0
            SS_RD_ID = 0
            SDX_ID = 0
            SCLK_ID = 0

            file.forEachLine { i ->
                if (i.contains("srl./SS")) {
                    SS_LCD_ID += getOutputPins(i)
                } else if (i.contains("srr./SS")){
                    SS_RD_ID += getOutputPins(i)
                } else if (i.contains("srl.SDX")) {
                    SDX_ID += getOutputPins(i)
                } else if (i.contains("srl.SCLK")) {
                    SCLK_ID += getOutputPins(i)
                }
            }
        } catch(_:Exception){
            Logger.getLogger("SerialEmitter").log(Level.WARNING, "No config file found, using default values")
            SS_LCD_ID = pow(2,SS_LCD_ID)
            SS_RD_ID = pow(2,SS_RD_ID)
            SDX_ID = pow(2,SDX_ID)
            SCLK_ID = pow(2,SCLK_ID)
        }
    }

    /**
     * Faz o set up necessário para o envio de uma trama em série para a tela LCD
     * @param data
     * @param size
     */
    private fun sendLCD(data: Int, size: Int) {
        rst()
        HAL.clrBits(SS_LCD_ID)
        // println("Sending to LCD: $data")
        parseAndSend(data, size, LCD_SLEEP)

        HAL.setBits(SS_LCD_ID)

    }

    /**
     * Faz o set up necessário para o envio de uma trama em série para o Roulette
     * @param data
     * @param size
     */
    private fun sendRoulette(data: Int, size: Int) {
        rst()
        HAL.clrBits(SS_RD_ID)

        parseAndSend(data, size)

        HAL.setBits(SS_RD_ID)
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

        val p = if (data.countOneBits() % 2 == 0) 1 else 0

        for (i in 0..size){
            if (i == size) {
                if (p.isBit(0))
                    HAL.setBits(SDX_ID)
                else
                    HAL.clrBits(SDX_ID)
            }
            else {
                if (data.isBit(i)) {
                    HAL.setBits(SDX_ID)
                }
                else {
                    HAL.clrBits(SDX_ID)
                }
            }

            HAL.setBits(SCLK_ID)

            HAL.clrBits(SCLK_ID)
        }
        Time.sleep(time)
    }

    fun rst(){
        val addr = SS_LCD_ID + SS_RD_ID
        HAL.writeBits(0b1111_1111, addr)
    }
}