import isel.leic.UsbPort

object HAL {
    // Guarda o valor do array de LEDs
    var light = 0b0000_0000

    // NOTE: Inicia o objeto
    fun init(){
        writeBits(0b1111_1111,light)
    }

    // NOTE: Retorna 'true' se o bit definido pela mask está com o valor lógico '1' no UsbPort
    fun isBit(mask: Int): Boolean{
        if (mask.countOneBits() == 1){
            if (UsbPort.read().and(mask).countOneBits() == 1)
                return true
        }
        return false
    }

    // NOTE: Retorna os valores dos bits representados por mask presentes no UsbPort
    fun readBits(mask: Int): Int{
        return UsbPort.read().and(mask)
    }

    // NOTE: Escreve nos bits representados por mask os valores dos bits correspondentes em value
    fun writeBits(mask: Int, value: Int){
        light = mask.and(value)
        UsbPort.write(light)
    }

    // NOTE: Coloca os bits representados por mask no valor lógico '1'
    fun setBits(mask: Int){
        light = mask.or(light)
        UsbPort.write(light)
    }

    // NOTE: Coloca os bits representados por mask no valor lógico '0'
    fun clrBits(mask: Int){
        light = mask.inv().and(light)
        UsbPort.write(light)
    }
}