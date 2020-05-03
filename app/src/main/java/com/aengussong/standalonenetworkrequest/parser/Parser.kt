package com.aengussong.standalonenetworkrequest.parser

import kotlin.reflect.KClass
import com.aengussong.standalonenetworkrequest.network.*

/**
 * Describes main parser function to parse connection response. This interface should be implemented
 * to be user with [RequestController]
 * */
interface Parser{
    fun <T:Any> parse(data:String, klass: KClass<T>):T?
}