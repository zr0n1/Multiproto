package com.github.zr0n1.multiproto.event

import com.github.zr0n1.multiproto.protocol.Protocol
import net.mine_diver.unsafeevents.Event

class VersionChangedEvent : Event() {
    val version = Protocol.version
}