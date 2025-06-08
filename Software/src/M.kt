import SerialEmitter.SCLK_ID
import SerialEmitter.SDX_ID
import SerialEmitter.SS_LCD_ID
import SerialEmitter.SS_RD_ID
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

object M {

    var idx_M = 7

    val password = "123"
    var inM = false
        get() = HAL.isBit(0b1000_0000)

    fun init(){
        HAL.init()

        try {
            val file = File(HAL.configPath)
                idx_M = 0

            file.forEachLine { i ->
                if (i.contains("m.out")) {
                    idx_M += getInputPins(i)
                }
            }
        } catch(_:Exception){
            Logger.getLogger("M").log(Level.WARNING, "No config file found, using default values")
            idx_M = pow(2,7)
        }
    }
}