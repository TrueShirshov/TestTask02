package org.shirshov.testtask.network.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.math.BigInteger
import java.security.MessageDigest

val defaultObjectMapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(ThreeTenModule())

fun md5(string: String) = String.format("%032x", BigInteger(1, MessageDigest.getInstance("MD5").digest(string.toByteArray(Charsets.UTF_8))))
