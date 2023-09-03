/*
 * This file is part of ViaMCP - https://github.com/FlorianMichael/ViaMCP
 * Copyright (C) 2020-2023 FlorianMichael/EnZaXD and contributors
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
 */
package de.florianmichael.viamcp.gui;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.StringTextComponent;

public class AsyncVersionSlider extends SliderPercentageOption {
    private int x, y, width, height;

    public AsyncVersionSlider(int x, int y , int widthIn, int heightIn)
    {
        super("", 0, ViaLoadingBase.getProtocols().size() - 1, 1, (sett) -> {
            return (double) (ViaLoadingBase.getProtocols().size() - ViaLoadingBase.getInstance().getTargetVersion().getIndex() - 1);
        }, (sett, d) -> {
            ViaLoadingBase.getInstance().reload(ViaLoadingBase.getProtocols().get((int)(double)d));
        }, (a, b) -> {
            return new StringTextComponent(ViaLoadingBase.getProtocols().get((int)b.get(a)).getName());
        });

        ViaLoadingBase.getInstance().reload(
                ProtocolVersion.getProtocol(ViaLoadingBase.getInstance().getNativeVersion())
        );
        this.maxValue = ViaLoadingBase.getProtocols().size() - 1;
        this.x = x;
        this.y = y;
        this.width = widthIn;
        this.height = heightIn;
    }

    public Widget getButton() {
        return this.createWidget(Minecraft.getInstance().gameSettings, x, y, width);
    }
}
