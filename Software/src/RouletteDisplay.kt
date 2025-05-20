import isel.leic.utils.Time
import java.util.logging.Level
import java.util.logging.Logger
import javax.swing.plaf.basic.BasicOptionPaneUI

// Controla o mostrador de pontuação.
object RouletteDisplay {

    const val TOTAL_DISPLAYS = 6
    const val MAX_VALUE = 15
    const val CMD_SIZE = 3
    const val TOTAL_SIZE = 8
    const val CMD_UPDATE = 0b0000_0110

    val POS = listOf(
        0b0000_0000,
        0b0000_0001,
        0b0000_0010,
        0b0000_0011,
        0b0000_0100,
        0b0000_0101
    )

    val ANIM = listOf(
        0x14,
        0x15,
        0x16,
        0x11,
        0x12,
        0x13,
    )

    var cursor = 0

    // Inicia a classe, estabelecendo os valores iniciais.
    fun init() {
        SerialEmitter.init()
    }

    // Realiza a animação do sorteio
    fun animation() {
        for (i in ANIM) {
            for (j in 0 until TOTAL_DISPLAYS) {
                SerialEmitter.send(
                    SerialEmitter.Destination.ROULETTE,
                    i.shl(CMD_SIZE) + POS[j],
                    TOTAL_SIZE
                )
            }
            update()
            Time.sleep(100)
        }
    }

    // Envia comando para atualizar o valor do mostrador da roleta
    fun setValue(value: Int) {
        if (value > MAX_VALUE || value < 0) {
            Logger.getLogger("RouletteDisplay").log(Level.WARNING, "Value too high. Max: $MAX_VALUE, Value: $value")
            return
        }

        val command = POS[cursor] or (value shl CMD_SIZE)
        SerialEmitter.send(
            SerialEmitter.Destination.ROULETTE,
            command,
            TOTAL_SIZE
        )
    }

    // Envia comando para desativar/ativar a visualização do mostrador da roleta
    fun off(value: Boolean) {
        if (value) {
            SerialEmitter.send(
                SerialEmitter.Destination.ROULETTE,
                0b0000_1111,
                TOTAL_SIZE
            )
        } else {
            SerialEmitter.send(
                SerialEmitter.Destination.ROULETTE,
                0b0000_0111,
                TOTAL_SIZE
            )
        }
    }

    fun update() {
        SerialEmitter.send(
            SerialEmitter.Destination.ROULETTE,
            CMD_UPDATE,
            TOTAL_SIZE
        )
    }
}
