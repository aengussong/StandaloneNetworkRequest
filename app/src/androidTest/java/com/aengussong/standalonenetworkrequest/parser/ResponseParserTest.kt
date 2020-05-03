package com.aengussong.standalonenetworkrequest.parser

import org.junit.Assert
import org.junit.Test

class ResponseParserTest {

    private val parser: Parser = ResponseParser()

    @Test
    fun parseObjectJson_shouldParseSuccessfully() {
        val name = "test"
        val size = 10
        val json = "{\"name\":\"$name\", \"size\":$size}"


        val parsed = parser.parse(json, JsonDummy::class)

        Assert.assertEquals(name, parsed!!.name)
        Assert.assertEquals(size, parsed.size)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun parseArrayJson_shouldThrowException() {
        val json = "[]"

        parser.parse(json, JsonDummy::class)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun parseJsonToArray_shouldThrowException() {
        val json = "{}"

        parser.parse(json, Array<JsonDummy>::class)
    }

    @Test
    fun parseJsonWithMissingParameterMarkedAsNullableInObject_shouldParseObjectWithNullParameter() {
        val name = "mock"
        val json = "{\"name\":\"$name\"}"

        val parsed = parser.parse(json, JsonDummyNullable::class)

        Assert.assertNotNull(parsed)
        Assert.assertEquals(name, parsed!!.name)
        Assert.assertNull(parsed.size)
    }

    class JsonDummy(val name: String, val size: Int)
    class JsonDummyNullable(val name: String, val size: Int?)
}