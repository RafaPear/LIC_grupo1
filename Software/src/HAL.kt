import isel.leic.UsbPort

object HAL {
    // TODO: init()
    // NOTE: Inicia o objeto
    fun init(){

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
        UsbPort.write(mask.and(value))
    }

    // TODO: setBits()
    // NOTE: Coloca os bits representados por mask no valor lógico '1'
    fun setBits(mask: Int){
    }

    // TODO: clrBits()
    // NOTE: Coloca os bits representados por mask no valor lógico '0'
    fun clrBits(mask: Int){
    }
}