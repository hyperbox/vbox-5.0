/*
 * Hyperbox - Virtual Infrastructure Manager
 * Copyright (C) 2015 Maxime Dor
 * 
 * http://kamax.io/hbox/
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package io.kamax.vbox5_0.setting.network;

import io.kamax.hbox.constant.NetworkInterfaceAttribute;
import io.kamax.hbox.exception.ConfigurationException;
import io.kamax.setting._Setting;
import io.kamax.vbox.settings.network.NicAttachModeSetting;
import io.kamax.vbox5_0.setting._NetworkInterfaceSettingAction;
import org.virtualbox_5_0.INetworkAdapter;
import org.virtualbox_5_0.LockType;
import org.virtualbox_5_0.NetworkAttachmentType;
import org.virtualbox_5_0.VBoxException;

public class NicAttachModeSettingAction implements _NetworkInterfaceSettingAction {

    @Override
    public LockType getLockType() {
        return LockType.Shared;
    }

    @Override
    public String getSettingName() {
        return NetworkInterfaceAttribute.AttachMode.getId();
    }

    @Override
    public void set(INetworkAdapter nic, _Setting setting) {
        try {
            nic.setAttachmentType(NetworkAttachmentType.valueOf(setting.getValue().toString()));
        } catch (VBoxException e) {
            throw new ConfigurationException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ConfigurationException("Unkown attach mode [" + setting.getString() + "]");
        }
    }

    @Override
    public _Setting get(INetworkAdapter nic) {
        return new NicAttachModeSetting(nic.getAttachmentType().toString());
    }

}
