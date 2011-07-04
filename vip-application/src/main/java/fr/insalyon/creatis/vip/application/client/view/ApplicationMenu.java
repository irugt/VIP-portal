/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.client.view;

import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.insalyon.creatis.vip.application.client.view.launch.LaunchTab;
import fr.insalyon.creatis.vip.application.client.view.monitor.SimulationsTab;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.system.application.application.ManageApplicationsTab;

/**
 *
 * @author Rafael Silva
 */
public class ApplicationMenu extends Menu {

    public ApplicationMenu(final String applicationClass, final boolean isGroupAdmin) {
        
        this.setShowShadow(true);
        this.setShadowDepth(3);

        MenuItem launchItem = new MenuItem("Launch " + applicationClass);
        launchItem.setIcon("icon-launch.png");
        launchItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new LaunchTab(applicationClass));
            }
        });

        MenuItem monitorItem = new MenuItem("Monitor " + applicationClass);
        monitorItem.setIcon("icon-simulation-monitor.png");
        monitorItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                Layout.getInstance().addTab(new SimulationsTab(isGroupAdmin));
            }
        });

        if (isGroupAdmin) {
            MenuItem manageItem = new MenuItem("Manage " + applicationClass);
            manageItem.setIcon("icon-simulation-manage.png");
            manageItem.addClickHandler(new ClickHandler() {

                public void onClick(MenuItemClickEvent event) {
                    Layout.getInstance().addTab(new ManageApplicationsTab(applicationClass));
                }
            });

            MenuItemSeparator separator = new MenuItemSeparator();
            this.setItems(launchItem, monitorItem, separator, manageItem);

        } else {
            this.setItems(launchItem, monitorItem);
        }
    }
}