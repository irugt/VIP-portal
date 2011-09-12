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
package fr.insalyon.creatis.vip.application.client.view.monitor.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowService;
import fr.insalyon.creatis.vip.application.client.rpc.WorkflowServiceAsync;
import fr.insalyon.creatis.vip.application.client.view.monitor.FileViewerWindow;
import fr.insalyon.creatis.vip.application.client.view.monitor.LogsStackSection;

/**
 *
 * @author Rafael Silva
 */
public class LogsContextMenu extends Menu {

    private LogsStackSection section;
    private String baseDir;

    public LogsContextMenu(LogsStackSection section, final String simulationID,
            final String dataName, final String folder, boolean isFile) {

        this.section = section;
        this.baseDir = "/" + simulationID + "/" + folder;
        this.setShowShadow(true);
        this.setShadowDepth(10);
        this.setWidth(90);

        MenuItem viewItem = new MenuItem("View File");
        viewItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                new FileViewerWindow("Viewing File: " + dataName,
                        simulationID, folder, dataName, "").show();
            }
        });

        MenuItem downloadItem = new MenuItem("Download File");
        downloadItem.setIcon("icon-download.png");
        downloadItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                Window.open(
                        GWT.getModuleBaseURL()
                        + "/getfileservice?filepath=" + baseDir
                        + "/" + dataName, "", "");
            }
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setIcon("icon-delete.png");
        deleteItem.addClickHandler(new ClickHandler() {

            public void onClick(MenuItemClickEvent event) {
                SC.confirm("Do you really want to delete '" + baseDir + "/" + dataName + "'?", new BooleanCallback() {

                    public void execute(Boolean value) {
                        if (value != null && value) {
                            delete(baseDir + "/" + dataName);
                        }
                    }
                });
            }
        });

        if (isFile) {
            this.setItems(viewItem, downloadItem, deleteItem);
            
        } else {
            this.setItems(deleteItem);
        }
    }

    private void delete(String path) {
        WorkflowServiceAsync service = WorkflowService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                section.getModal().hide();
                SC.warn("Error executing delete: " + caught.getMessage());
            }

            public void onSuccess(Void result) {
                section.getModal().hide();
                section.loadData(baseDir);
            }
        };
        section.getModal().show("Deleting data...", true);
        service.deleteLogData(path, callback);
    }
}
