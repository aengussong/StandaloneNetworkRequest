package com.aengussong.standalonenetworkrequest.parser

import android.util.Log
import com.aengussong.standalonenetworkrequest.network.RequestController
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

/**
 * Basic deserialization implementation that used as default with [RequestController]
 * */
class ResponseParser : Parser {

    /**Does not deserialize [JSONArray]. Can deserialize only simple object without nested objects.
     * Throws exception if field, marked as not nullable, cannot be found in json. If field cannot be
     * found in json for nullable parameter - sets it to null.
     *
     * @param data - JSON string to deserialize
     * @param klass - KClass of object, to which [data] JSON will be deserialized
     **/
    override fun <T : Any> parse(data: String, klass: KClass<T>): T? {
        return when (val json = JSONTokener(data).nextValue()) {
            is JSONObject -> parseObject(json, klass)
            is JSONArray -> throw UnsupportedOperationException("JSON array is not supported")
            else -> throw java.lang.UnsupportedOperationException("Only JSON object to simple object parsing available ")
        }
    }

    private fun <T : Any> parseObject(json: JSONObject, klass: KClass<T>): T {
        checkAvailability(klass)

        val params = mutableListOf<Any?>()
        klass.primaryConstructor?.parameters?.forEach { param: KParameter ->
            val isNullable = param.type.isMarkedNullable
            try {
                val parsedParam = json.get(param.name!!)
                params.add(parsedParam)
            } catch (ex: JSONException) {
                if (isNullable) {
                    params.add(null)
                    Log.e("JSON parser", "cannot find parameter ${param.name} in json")
                } else {
                    throw MissingFormatArgumentException("Missing object field in JSON: ${param.name}")
                }
            }
        }

        return klass.primaryConstructor?.call(*params.toTypedArray())
            ?: throw NoPrimaryConstructorException()
    }

    /**
     * Does not deserialize [JSONArray]
     * */
    private fun checkAvailability(klass: KClass<out Any>) {
        if (klass is Array<*>) {
            throw UnsupportedOperationException("JSON parsing to Array is not supported")
        }
    }
}

class NoPrimaryConstructorException(private val msg: String = "No primary constructor in cast object") :
    Exception(msg)