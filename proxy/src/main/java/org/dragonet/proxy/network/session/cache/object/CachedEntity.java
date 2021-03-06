/*
 * DragonProxy
 * Copyright (C) 2016-2019 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can view the LICENSE file for more details.
 *
 * @author Dragonet Foundation
 * @link https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.network.session.cache.object;

import com.flowpowered.math.vector.Vector3f;
import com.nukkitx.protocol.PlayerSession;
import com.nukkitx.protocol.bedrock.data.EntityData;
import com.nukkitx.protocol.bedrock.data.EntityDataDictionary;
import com.nukkitx.protocol.bedrock.data.EntityFlag;
import com.nukkitx.protocol.bedrock.packet.AddEntityPacket;
import com.nukkitx.protocol.bedrock.packet.RemoveEntityPacket;
import lombok.Data;
import org.dragonet.proxy.data.entity.EntityType;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.types.EntityEffectTranslator;

import java.util.HashSet;
import java.util.Set;

@Data
public class CachedEntity {
    protected EntityType type;
    protected long entityId;

    protected EntityDataDictionary metadata = new EntityDataDictionary();

    protected boolean spawned = false;

    protected Vector3f position;

    protected Set<EntityEffectTranslator.BedrockEffect> effects = new HashSet<>();

    public CachedEntity(EntityType type, long entityId) {
        this.type = type;
        this.entityId = entityId;

        addDefaultMetadata();
    }

    public void spawn(ProxySession session) {
        if(spawned) {
            throw new IllegalStateException("Cannot spawn entity that is already spawned");
        }

        AddEntityPacket addEntityPacket = new AddEntityPacket();
        addEntityPacket.setIdentifier("minecraft:" + type.name().toLowerCase()); // TODO: this may need mapping
        addEntityPacket.setEntityType(type.getType());
        addEntityPacket.setRotation(Vector3f.ZERO);
        addEntityPacket.setMotion(Vector3f.ZERO);
        addEntityPacket.setPosition(Vector3f.ZERO);
        addEntityPacket.setRuntimeEntityId(entityId);
        addEntityPacket.setUniqueEntityId(entityId);
        addEntityPacket.getMetadata().putAll(getMetadata());

        session.getBedrockSession().sendPacket(addEntityPacket);
        spawned = true;
    }

    public void despawn(ProxySession session) {
        if(spawned) {
            RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
            removeEntityPacket.setUniqueEntityId(entityId);

            session.getBedrockSession().sendPacket(removeEntityPacket);
            spawned = false;
        }
    }

    private void addDefaultMetadata() {
        metadata.put(EntityData.NAMETAG, "");
        metadata.put(EntityData.SCALE, 1f);
        metadata.put(EntityData.AIR, 400);
        metadata.put(EntityData.MAX_AIR, 400);
        metadata.put(EntityData.ENTITY_AGE, 0);
        metadata.put(EntityData.BOUNDING_BOX_HEIGHT, (float) type.getHeight());
        metadata.put(EntityData.BOUNDING_BOX_WIDTH, (float) type.getWidth());
    }
}
