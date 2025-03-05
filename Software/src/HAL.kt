import isel.leic.UsbPort

object HAL {
    // TODO:  init()
    // NOTE: Inicia o objeto
    fun init(){
    }

    // TODO: isBit()
    // NOTE: Retorna 'true' se o bit definido pela mask está com o valor lógico '1' no UsbPort
    fun isBit(mask: Int): Boolean{
        return false
    }

    // TODO: readBits()
    // NOTE: Retorna os valores dos bits representados por mask presentes no UsbPort
    fun readBits(mask: Int): Int{
        return 0
    }

    // TODO: writeBits()
    // NOTE: Escreve nos bits representados por mask os valores dos bits correspondentes em value
    fun writeBits(mask: Int, value: Int){
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