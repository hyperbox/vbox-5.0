/*
 * Hyperbox - Virtual Infrastructure Manager
 * Copyright (C) 2015 - Maxime Dor
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

package io.kamax.vbox5_0;

import io.kamax.hbox.exception.HypervisorException;
import io.kamax.hboxd.hypervisor.Hypervisor;
import io.kamax.tool.AxStrings;
import io.kamax.tool.logging.Logger;
import io.kamax.vbox.VBoxWSOptions;
import io.kamax.vbox.VBoxWebSrv;
import io.kamax.vbox.VirtualBox;
import io.kamax.vbox._VBoxWebSrv;
import java.net.URISyntaxException;
import org.virtualbox_5_0.VBoxException;
import org.virtualbox_5_0.VirtualBoxManager;

@Hypervisor(
        id = VirtualBox.ID.WS_5_0,
        vendor = VirtualBox.VENDOR,
        product = VirtualBox.PRODUCT,
        version = VirtualBox.Version.v5_0,
        typeId = VirtualBox.Type.WEB_SERVICES)
public class VBoxWebServicesHypervisor extends VBoxHypervisor {

    private _VBoxWebSrv webSrv;

    @Override
    protected VirtualBoxManager connect(String rawOptions) {
        Logger.debug("Using Web Services");
        try {
            VBoxWSOptions options = new VBoxWSOptions(rawOptions);
            if (!options.hasOptions()) {
                if (webSrv == null) {
                    webSrv = new VBoxWebSrv(options.getHost(), options.getPort(), "null");
                    webSrv.start();
                } else {
                    Logger.warning("Got an already running VirtualBox WebServices instance!");
                }
                options.setPort(webSrv.getPort());
            }

            try {
                VirtualBoxManager mgr = VirtualBoxManager.createInstance(null);
                Logger.debug("Connection info: " + options.extractServer());
                Logger.debug("User: " + options.getUsername());
                Logger.debug("Password given: " + (AxStrings.isEmpty(options.getPasswd().toString())));
                mgr.connect(options.extractServer(), options.getUsername(), options.getPasswd().toString());

                return mgr;
            } catch (VBoxException e) {
                disconnect();
                throw new HypervisorException("Unable to connect to the Virtualbox WebServices : " + e.getMessage(), e);
            }
        } catch (URISyntaxException e) {
            throw new HypervisorException("Invalid options syntax: " + e.getMessage(), e);
        }
    }

    @Override
    protected void disconnect() {
        try {
            try {
                vbMgr.disconnect();
            } catch (Throwable t) {
                Logger.warning("Error when disconnecting : " + t.getMessage());
            }
            if (webSrv != null) {
                webSrv.stop();
            }
        } catch (Throwable t) {
            Logger.warning("Failed to stop WebServices", t);
        } finally {
            webSrv = null;
        }
    }

}
