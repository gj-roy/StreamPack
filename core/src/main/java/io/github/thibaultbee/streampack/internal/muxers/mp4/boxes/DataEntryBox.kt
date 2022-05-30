/*
 * Copyright (C) 2022 Thibault B.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.thibaultbee.streampack.internal.muxers.mp4.boxes

import io.github.thibaultbee.streampack.internal.utils.putString
import java.nio.ByteBuffer

sealed class DataEntryBox(type: String, flags: Int) : FullBox(type, 0, flags)

class DataEntryUrlBox(private val location: String? = null) :
    DataEntryBox(
        "url ", if (location != null) {
            0
        } else {
            1
        }
    ) {
    override val size: Int = super.size + (location?.let { it.length + 1 } ?: 0)

    override fun write(buffer: ByteBuffer) {
        super.write(buffer)
        location?.let {
            buffer.putString(it)
            buffer.put(0.toByte())
        }
    }
}

class DataEntryUrnBox(private val name: String, private val location: String? = null, flags: Int) :
    DataEntryBox("urn ", flags) {
    override val size: Int = super.size + name.length + 1 + (location?.let { it.length + 1 } ?: 0)

    override fun write(buffer: ByteBuffer) {
        super.write(buffer)
        buffer.putString(name)
        buffer.put(0.toByte())
        location?.let {
            buffer.putString(it)
            buffer.put(0.toByte())
        }
    }
}