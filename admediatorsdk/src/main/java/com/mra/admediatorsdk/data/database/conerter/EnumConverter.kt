package com.mra.admediatorsdk.data.database.conerter

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T : Enum<T>> T.toInt(): Int = this.ordinal

inline fun <reified T : Enum<T>> Int.toEnum(): T = enumValues<T>()[this]

inline fun <reified T : Enum<T>> String.toEnum(): T = enumValueOf(this)