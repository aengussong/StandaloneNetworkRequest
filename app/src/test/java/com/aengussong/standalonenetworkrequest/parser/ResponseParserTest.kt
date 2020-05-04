package com.aengussong.standalonenetworkrequest.parser

import com.aengussong.standalonenetworkrequest.testUtils.mockLog
import org.junit.Assert
import org.junit.Test

class ResponseParserTest {

    private val parser: Parser = ResponseParser()

    @Test
    fun `parse object json - should parse successfully`() {
        val name = "test"
        val size = 10
        val json = "{\"name\":\"$name\", \"size\":$size}"


        val parsed = parser.parse(json, JsonDummy::class)

        Assert.assertEquals(name, parsed!!.name)
        Assert.assertEquals(size, parsed.size)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun `parse array json - should throw exception`() {
        val json = "[]"

        parser.parse(json, JsonDummy::class)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun `parse json to array - should throw exception`() {
        val json = "{}"

        parser.parse(json, Array<JsonDummy>::class)
    }

    @Test
    fun `parse json with missing parameter marked as nullable in object - should parse object with parameter set to null`() {
        val name = "mock"
        val json = "{\"name\":\"$name\"}"
        mockLog()

        val parsed = parser.parse(json, JsonDummyNullable::class)

        Assert.assertNotNull(parsed)
        Assert.assertEquals(name, parsed!!.name)
        Assert.assertNull(parsed.size)
    }

    class JsonDummy(val name: String, val size: Int)
    class JsonDummyNullable(val name: String, val size: Int?)
}