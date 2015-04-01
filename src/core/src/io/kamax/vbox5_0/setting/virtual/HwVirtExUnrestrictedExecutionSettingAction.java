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

package io.kamax.vbox5_0.setting.virtual;

import org.altherian.hbox.constant.MachineAttribute;
import org.altherian.setting._Setting;
import org.altherian.vbox.settings.virtual.HwVirtExExclSetting;
import io.kamax.vbox5_0.setting._MachineSettingAction;
import org.virtualbox_5_0.HWVirtExPropertyType;
import org.virtualbox_5_0.IMachine;
import org.virtualbox_5_0.LockType;

public class HwVirtExUnrestrictedExecutionSettingAction implements _MachineSettingAction {

   @Override
   public LockType getLockType() {
      return LockType.Write;
   }

   @Override
   public String getSettingName() {
      return MachineAttribute.HwVirtExExcl.toString();
   }

   @Override
   public void set(IMachine machine, _Setting setting) {
      machine.setHWVirtExProperty(HWVirtExPropertyType.UnrestrictedExecution, setting.getBoolean());
   }

   @Override
   public _Setting get(IMachine machine) {
      return new HwVirtExExclSetting(machine.getHWVirtExProperty(HWVirtExPropertyType.UnrestrictedExecution));
   }

}
