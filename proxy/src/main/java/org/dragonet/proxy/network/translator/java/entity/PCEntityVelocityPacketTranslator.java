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
package org.dragonet.proxy.network.translator.java.entity;

import com.flowpowered.math.vector.Vector3f;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;
import com.nukkitx.protocol.bedrock.packet.SetEntityMotionPacket;
import org.dragonet.proxy.network.session.ProxySession;
import org.dragonet.proxy.network.translator.PacketTranslator;

public class PCEntityVelocityPacketTranslator implements PacketTranslator<ServerEntityVelocityPacket> {
    public static final PCEntityVelocityPacketTranslator INSTANCE = new PCEntityVelocityPacketTranslator();

    @Override
    public void translate(ProxySession session, ServerEntityVelocityPacket packet) {
        SetEntityMotionPacket entityMotion = new SetEntityMotionPacket();
        entityMotion.setRuntimeEntityId(packet.getEntityId());
        entityMotion.setMotion(new Vector3f(packet.getMotionX(), packet.getMotionY(), packet.getMotionZ()));

        session.getBedrockSession().sendPacket(entityMotion);

    }
}
