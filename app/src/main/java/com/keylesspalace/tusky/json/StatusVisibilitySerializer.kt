package com.keylesspalace.tusky.json

import com.keylesspalace.tusky.entity.Status
import kotlinx.serialization.encoding.Encoder

object StatusVisibilitySerializer : kotlinx.serialization.KSerializer<Status.Visibility> {
    override val descriptor: kotlinx.serialization.descriptors.SerialDescriptor =
        kotlinx.serialization.descriptors.PrimitiveSerialDescriptor(
            "StatusVisibility",
            kotlinx.serialization.descriptors.PrimitiveKind.STRING
        )

    override fun deserialize(decoder: kotlinx.serialization.encoding.Decoder): Status.Visibility {
        val value = decoder.decodeInt()
        return Status.Visibility.fromInt(value)
    }

    override fun serialize(encoder: Encoder, value: Status.Visibility) {
        encoder.encodeInt(value.int)
    }
}
