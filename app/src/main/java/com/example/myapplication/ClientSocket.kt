import java.io.BufferedInputStream
import java.io.PrintWriter
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

class ClientSocket(private val host: String?, private val resourceLoc: String) {
    lateinit var factory: SSLSocketFactory
    lateinit var clientSocket: SSLSocket
    lateinit var cArray: CharArray

    fun makeRequest() {
        factory = SSLSocketFactory.getDefault() as SSLSocketFactory
        clientSocket = factory.createSocket(host, 443) as SSLSocket
        val writer = PrintWriter(clientSocket.getOutputStream())

        val protocol = "GET /$resourceLoc HTTP/1.1"
        val connection = "Connection: close"
        val headerEnd = ""
        val HostHeader = "Host: ${host}"


        writer.println(protocol)
        writer.println(HostHeader)
        writer.println(connection)
        writer.println(headerEnd)
        writer.flush()
    }

    fun getHeaders(): String {
        val reader = BufferedInputStream(clientSocket.inputStream)

        var byteCode = 0
        val builder = StringBuilder()

        while (reader.read().also { byteCode = it } != -1) {
            builder.append(byteCode.toChar())
        }

        val text = builder.toString()
        val sub = text.split("\r\n\r\n".toRegex()).toTypedArray()

        cArray = sub[1].toCharArray()

        return sub[0]
    }

    fun getBody(): ByteArray {
        val bArray = ByteArray(cArray.size)

        cArray.forEachIndexed { index, char ->
            bArray[index] = cArray[index].toByte()
        }

        return bArray
    }

}


