package org.rsmod.api.death.plugin

import kotlin.reflect.KProperty
import org.rsmod.api.drops.NpcDropTable
import org.rsmod.game.entity.Npc

// Define a simple nullableProperty delegate
public class NullablePropertyDelegate<T> {
    private var value: T? = null

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): T? = value

    public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        this.value = value
    }
}

public fun <T> nullableProperty(): NullablePropertyDelegate<T> = NullablePropertyDelegate()

public var Npc.dropTables: NpcDropTable? by nullableProperty<NpcDropTable?>()
