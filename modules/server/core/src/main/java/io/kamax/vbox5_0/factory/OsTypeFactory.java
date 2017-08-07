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

package io.kamax.vbox5_0.factory;

import io.kamax.hbox.comm.io.factory.SettingIoFactory;
import io.kamax.hbox.constant.*;
import io.kamax.hbox.data.Device;
import io.kamax.hbox.data.Machine;
import io.kamax.hboxd.hypervisor._RawOsType;
import io.kamax.tools.setting.StringSetting;
import io.kamax.vbox.settings.cpu.PaeSetting;
import io.kamax.vbox.settings.general.KeyboardModeSetting;
import io.kamax.vbox.settings.general.MouseModeSetting;
import io.kamax.vbox.settings.medium.MediumSizeSetting;
import io.kamax.vbox.settings.memory.MemorySetting;
import io.kamax.vbox.settings.motherboard.ChipsetSetting;
import io.kamax.vbox.settings.motherboard.FirmwareSetting;
import io.kamax.vbox.settings.network.NicAdapterTypeSetting;
import io.kamax.vbox.settings.usb.UsbOhciSetting;
import io.kamax.vbox.settings.video.Accelerate2dVideoSetting;
import io.kamax.vbox.settings.video.Accelerate3dSetting;
import io.kamax.vbox.settings.video.VRamSetting;
import io.kamax.vbox.settings.virtual.HpetSetting;
import io.kamax.vbox.settings.virtual.HwVirtExExclSetting;
import io.kamax.vbox5_0.VBoxOsType;
import io.kamax.vbox5_0.data.Mappings;
import org.virtualbox_5_0.IGuestOSType;

public final class OsTypeFactory {

    private OsTypeFactory() {
        // class is static
    }

    public static _RawOsType get(IGuestOSType guestOs) {
        return new VBoxOsType(guestOs);
    }

    public static Machine getSettings(IGuestOSType guestOs) {
        Machine vm = new Machine();

        vm.setSetting(SettingIoFactory.get(new Accelerate2dVideoSetting(guestOs.getRecommended2DVideoAcceleration())));
        vm.setSetting(SettingIoFactory.get(new Accelerate3dSetting(guestOs.getRecommended3DAcceleration())));
        // TODO settings.add(new AudioControllerSetting(guestOs.getRecommendedAudioController()));
        vm.setSetting(SettingIoFactory.get(new ChipsetSetting(guestOs.getRecommendedChipset().toString())));

        vm.setSetting(SettingIoFactory.get(new PaeSetting(guestOs.getRecommendedPAE())));
        vm.setSetting(SettingIoFactory.get(new MemorySetting(guestOs.getRecommendedRAM())));
        // TODO add RTCuseUTC
        vm.setSetting(SettingIoFactory.get(new UsbOhciSetting(guestOs.getRecommendedUSB())));
        if (guestOs.getRecommendedUSBHID()) {
            vm.setSetting(SettingIoFactory.get(new KeyboardModeSetting(KeyboardMode.Usb)));
            vm.setSetting(SettingIoFactory.get(new MouseModeSetting(MouseMode.Usb)));
        }
        if (guestOs.getRecommendedUSBTablet()) {
            vm.setSetting(SettingIoFactory.get(new MouseModeSetting(MouseMode.UsbTablet)));
        }
        vm.setSetting(SettingIoFactory.get(new VRamSetting(guestOs.getRecommendedVRAM())));
        vm.setSetting(SettingIoFactory.get(new FirmwareSetting(Mappings.get(guestOs.getRecommendedFirmware()))));
        vm.setSetting(SettingIoFactory.get(new HpetSetting(guestOs.getRecommendedHPET())));
        vm.setSetting(SettingIoFactory.get(new HwVirtExExclSetting(guestOs.getRecommendedVirtEx())));

        if (guestOs.getRecommendedFloppy()) {
            Device dev = new Device();
            dev.setTypeId(EntityType.StorageController.getId());
            dev.setSetting(SettingIoFactory.get(new StringSetting(StorageControllerAttribute.Type, StorageControllerType.Floppy.getId())));
            vm.addDevice(dev);
        }

        Device dvdStorCtrl = new Device(EntityType.DvdDrive.getId());
        dvdStorCtrl.setTypeId(EntityType.DvdDrive.getId());
        dvdStorCtrl.setSetting(SettingIoFactory.get(new StringSetting(StorageControllerAttribute.Type, guestOs.getRecommendedDVDStorageBus().toString())));
        dvdStorCtrl.setSetting(SettingIoFactory
                .get(new StringSetting(StorageControllerAttribute.SubType, guestOs.getRecommendedDVDStorageController().toString())));
        vm.addDevice(dvdStorCtrl);

        Device hddStorCtrl = new Device(EntityType.DiskDrive.getId());
        hddStorCtrl.setTypeId(EntityType.DiskDrive.getId());
        hddStorCtrl.setSetting(SettingIoFactory.get(new MediumSizeSetting(guestOs.getRecommendedHDD())));
        hddStorCtrl.setSetting(SettingIoFactory.get(new StringSetting(StorageControllerAttribute.Type, guestOs.getRecommendedHDStorageBus().toString())));
        hddStorCtrl.setSetting(SettingIoFactory
                .get(new StringSetting(StorageControllerAttribute.SubType, guestOs.getRecommendedHDStorageController().toString())));
        vm.addDevice(hddStorCtrl);

        Device networkDevice = new Device();
        networkDevice.setTypeId(EntityType.Network.getId());
        networkDevice.setSetting(SettingIoFactory.get(new NicAdapterTypeSetting(guestOs.getAdapterType().toString())));

        return vm;
    }

}
