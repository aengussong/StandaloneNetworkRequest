package com.aengussong.standalonenetworkrequest.parser

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ResponseParserTest {

    private val parser: Parser = ResponseParser()

    @Test
    fun parseObjectJson_shouldParseSuccessfully() {
        val name = "test"
        val size = 10
        val json = "{\"$name\":\"test\", \"size\":$size}"


        val parsed = parser.parse(json, JsonDummy::class)

        Assert.assertEquals(JsonDummy(name, size), parsed)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun parseArrayJson_shouldThrowException() {
        val json = "[]"

        parser.parse(json, JsonDummy::class)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun parseJsonToArray_shouldThrowException() {
        val json = ""

        parser.parse(json, Array<JsonDummy>::class)
    }

    @Test
    fun parseJsonWithMissingParameterMarkedAsNullableInObject_shouldParseObjectWithNullParameter() {
        val name = "mock"
        val json = "{\"$name\":\"test\"}"

        val parsed = parser.parse(json, JsonDummyNullable::class)

        Assert.assertNotNull(parsed)
        Assert.assertEquals(name, parsed!!.name)
        Assert.assertNull(parsed.size)
    }

    class JsonDummy(val name: String, val size: Int)
    class JsonDummyNullable(val name:String, val size:Int?)
}