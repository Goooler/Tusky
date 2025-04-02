package com.keylesspalace.tusky.json

import com.keylesspalace.tusky.entity.Notification
import com.keylesspalace.tusky.entity.notificationTypeFromString
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object NotificationTypeSerializer : KSerializer<Notification.Type> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("NotificationType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Notification.Type) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): Notification.Type {
        return notificationTypeFromString(decoder.decodeString())
    }
}
