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

package io.kamax.vbox5_0.storage;

import io.kamax.hboxd.hypervisor.storage._RawStorageControllerSubType;

public enum VBoxStorageControllerSubType implements _RawStorageControllerSubType {

    LsiLogic(VBoxStorageControllerType.SCSI),
    BusLogic(VBoxStorageControllerType.SCSI),
    IntelAhci(VBoxStorageControllerType.SATA),
    PIIX3(VBoxStorageControllerType.IDE),
    PIIX4(VBoxStorageControllerType.IDE),
    ICH6(VBoxStorageControllerType.IDE),
    I82078(VBoxStorageControllerType.Floppy),
    LsiLogicSas(VBoxStorageControllerType.SAS);

    private VBoxStorageControllerType type;

    private VBoxStorageControllerSubType(VBoxStorageControllerType type) {
        this.type = type;
    }

    @Override
    public String getParentType() {
        return type.getId();
    }

    @Override
    public String getId() {
        return toString();
    }

}
