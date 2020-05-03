package com.aengussong.standalonenetworkrequest.utils

import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader

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

        val NUMBER_OF_CORES: Int by lazy { Runtime.getRuntime().availableProcessors() }
    }
}