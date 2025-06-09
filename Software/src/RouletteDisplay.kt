import isel.leic.utils.Time
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Responsável pelos displays de 7 segmentos
 */
object RouletteDisplay {
    /**
     * Total de display na placa
     */
    private const val TOTAL_DISPLAYS = 6

    /**
     * Representação máxima do hexadecimal 15 ou F
     */
    private const val MAX_VALUE = 15

    /**
     * Número de bits para o comando
     */
    private const val CMD_SIZE = 3

    /**
     * Total de bits a serem enviados
     */
    const val TOTAL_SIZE = 8

    /**
     * Comando para atualizar os displays
     */
    private const val CMD_UPDATE = 0b0000_0110

    /**
     * Posição atual do cursor
     */
    var cursor = 0
    /**
     * Indice de cada display
     */
    val POS = listOf(
        0b0000_0000,
        0b0000_0001,
        0b0000_0010,
        0b0000_0011,
        0b0000_0100,
        0b0000_0101
    )

    /**
     * Sequencia de código para efetuar um ciclo da animação de girar
     */
    val ANIM = listOf(
        0x14,
        0x15,
        0x16,
        0x11,
        0x12,
        0x13,
    )
    const val TIME_FRAME_ANIME = 80
    val timeANIM = TIME_FRAME_ANIME * ANIM.size
    /**
     * Inicia a classe, estabelecendo os valores iniciais.
     */
    fun init() {
        SerialEmitter.init()
        off(false)
    }

    /**
     * Realiza a animação do sorteio
     * Tem uma doração de [timeANIM], padrão é 480 ms
     */
    fun animation() {
        for (i in ANIM) {
            for (j in 0 until TOTAL_DISPLAYS) {
                SerialEmitter.send(
                    SerialEmitter.Destination.ROULETTE,
                    i.shl(CMD_SIZE) or POS[j],
                    TOTAL_SIZE
                )
                Time.sleep(10)
            }
            Time.sleep(TIME_FRAME_ANIME.toLong())
            update()
        }
    }

    /**
     * Escreve no display em que se situa o [cursor] o valor de [value]
     * @param value
     */
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

    /**
     * Envia comando para desativar/ativar a visualização do mostrador da roleta
     * @param value false os displays estão ligados
     */
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

    /**
     * Realiza a atualização dos displays
     */
    fun update() {
        SerialEmitter.send(
            SerialEmitter.Destination.ROULETTE,
            CMD_UPDATE,
            TOTAL_SIZE
        )
    }

    /**
     * Deixa todos os displays com o valor 0
     */
    fun clrAll() {
        for (i in POS) {
            cursor = i
            setValue(0x1F)
        }
        update()
    }

    /**
     * Escreve no display correspondente ao [indice] o valor de [value]
     * @param value
     * @param indice
     */
    fun set(value: Int, indice: Int = 0) {
        cursor = indice
        setValue(value)
        update()
    }

}