package com.aengussong.standalonenetworkrequest.utils

import java.io.*

class Utils {
    companion object {
        fun readStream(stream: InputStream): String? {
            val reader: Reader = InputStreamReader(stream, "UTF-8")
            val rawBuffer = CharArray(500)
            val buffer = StringBuffer()
            var readSize: Int = reader.read(rawBuffer)
            while (readSize != -1) {
                buffer.append(rawBuffer, 0, readSize)
                readSize = reader.read(rawBuffer)
            }
            return buffer.toString()
        }

        fun writeToStream(stream: OutputStream, data: String) {
            val writer: Writer = OutputStreamWriter(stream, "UTF-8")
            writer.use {
                writer.write(data)
            }
        }

        val NUMBER_OF_CORES: Int by lazy { Runtime.getRuntime().availableProcessors() }
    }
}