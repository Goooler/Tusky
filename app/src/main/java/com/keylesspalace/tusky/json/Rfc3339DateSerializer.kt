package com.keylesspalace.tusky.json

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object Rfc3339DateSerializer : KSerializer<Date> {
    private val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US)

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(format.format(value))
    }

    override fun deserialize(decoder: Decoder): Date {
        return format.parse(decoder.decodeString()) ?: Date(0)
    }
}
