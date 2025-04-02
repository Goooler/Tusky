package com.keylesspalace.tusky.json

import com.keylesspalace.tusky.entity.Attachment
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object AttachmentTypeSerializer : KSerializer<Attachment.Type> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("AttachmentType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Attachment.Type) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): Attachment.Type {
        val str = decoder.decodeString()
        return Attachment.Type.entries.find { it.name == str } ?: Attachment.Type.UNKNOWN
    }
}
