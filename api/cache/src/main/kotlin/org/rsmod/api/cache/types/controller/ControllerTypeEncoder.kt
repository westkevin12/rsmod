package org.rsmod.api.cache.types.controller

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.openrs2.cache.Cache
import org.rsmod.api.cache.util.EncoderContext
import org.rsmod.game.type.controller.ControllerType

public object ControllerTypeEncoder {

    public fun encodeAll(dest: Cache, types: Iterable<ControllerType>, ctx: EncoderContext) {
        val packed = mutableListOf<ControllerType>()
        for (type in types) {
            val data = Unpooled.buffer()
            encodeJs5(type, data)
            encodeGame(type, data)
            dest.write(0, 0, type.id, data)
            packed.add(type)
        }
        // TODO: Encode packed types to a separate index for game client.
    }

    public fun encodeJs5(type: ControllerType, data: ByteBuf) {
        // ControllerType has no JS5-specific encoding.
    }

    public fun encodeGame(type: ControllerType, data: ByteBuf) {
        // ControllerType has no game-specific encoding.
    }
}
