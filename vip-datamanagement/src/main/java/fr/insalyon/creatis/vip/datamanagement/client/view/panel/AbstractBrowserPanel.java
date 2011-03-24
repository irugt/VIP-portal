/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
package fr.insalyon.creatis.vip.datamanagement.client.view.panel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.CheckboxColumnConfig;
import com.gwtext.client.widgets.grid.CheckboxSelectionModel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtextux.client.data.PagingMemoryProxy;
import fr.insalyon.creatis.vip.common.client.bean.Authentication;
import fr.insalyon.creatis.vip.common.client.view.Context;
import fr.insalyon.creatis.vip.common.client.view.FieldUtil;
import fr.insalyon.creatis.vip.datamanagement.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanagement.client.bean.Data;
import fr.insalyon.creatis.vip.datamanagement.client.rpc.FileCatalogService;
import fr.insalyon.creatis.vip.datamanagement.client.rpc.FileCatalogServiceAsync;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public abstract class AbstractBrowserPanel extends Panel {

    private String pId;
    protected CheckboxSelectionModel cbSelectionModel;
    protected GridPanel grid;
    protected Store store;
    protected Toolbar topToolbar;
    protected Store simulationsStore;
    protected ComboBox pathCB;

    public AbstractBrowserPanel(String pId) {
        this.pId = pId;
        this.setId(pId + "-panel");
        this.setLayout(new FitLayout());
        this.setMargins(0, 0, 0, 0);
        this.setBorder(false);

        this.add(getRemoteGrid());
        this.setTopToolbar(getToolbar());
        this.setWidth(420);
    }

    private GridPanel getRemoteGrid() {

        grid = new GridPanel();
        grid.setFrame(true);
        grid.setStripeRows(true);
        grid.setMargins(0, 0, 0, 0);

        cbSelectionModel = new CheckboxSelectionModel();

        RecordDef recordDef = new RecordDef(
                new FieldDef[]{
                    new StringFieldDef("typeico"),
                    new StringFieldDef("fileName")
                });

        ArrayReader reader = new ArrayReader(recordDef);
        PagingMemoryProxy proxy = new PagingMemoryProxy(getRootData());
        store = new Store(proxy, reader);
        store.setSortInfo(new SortState("typeico", SortDir.DESC));
        store.load();
        grid.setStore(store);

        BaseColumnConfig[] columns = {
            new CheckboxColumnConfig(cbSelectionModel),
            getIcoTypeColumnConfig(),
            new ColumnConfig("File Name", "fileName", 455),};
        ColumnModel columnModel = new ColumnModel(columns);
        grid.setColumnModel(columnModel);
        grid.setSelectionModel(cbSelectionModel);

        return grid;
    }

    private ColumnConfig getIcoTypeColumnConfig() {
        ColumnConfig icoColumn = new ColumnConfig("", "typeico", 25);
        icoColumn.setRenderer(new Renderer() {

            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex,
                    int colNum, Store store) {
                String status = ((String) value);
                String image = "icon-file.png";
                if (status.equals("Folder")) {
                    image = "icon-folder.gif";
                }
                return "<img src=\"./images/" + image + "\" style=\"border: 0\"/>";
            }
        });
        return icoColumn;
    }

    private Toolbar getToolbar() {

        topToolbar = new Toolbar();

        // Path ComboBox
        simulationsStore = FieldUtil.getComboBoxStore(pId + "-path-name");
        Object[][] data = new Object[][]{{DataManagerConstants.ROOT}};
        MemoryProxy usersProxy = new MemoryProxy(data);
        simulationsStore.setDataProxy(usersProxy);
        simulationsStore.load();

        pathCB = FieldUtil.getComboBox(pId + "-path-cb", "", 350,
                "", simulationsStore, pId + "-path-name");
        pathCB.addListener(new ComboBoxListenerAdapter() {

            @Override
            public void onSelect(ComboBox comboBox, Record record, int index) {
                String path = pathCB.getValue();

                if (path != null && !path.isEmpty()) {
                    loadData(pathCB.getValue(), false);
                }
            }
        });
        pathCB.setValue(DataManagerConstants.ROOT);

        // Folder up Button
        ToolbarButton folderupButton = new ToolbarButton("", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                String selectedPath = pathCB.getValue();
                if (!selectedPath.equals(DataManagerConstants.ROOT)) {
                    String newPath = selectedPath.substring(0, selectedPath.lastIndexOf("/"));
                    if (newPath.isEmpty()) {
                        newPath = DataManagerConstants.ROOT;
                    }
                    loadData(newPath, false);
                }
            }
        });
        folderupButton.setIcon("images/icon-folderup.gif");
        folderupButton.setCls("x-btn-icon");

        // Refresh Button
        ToolbarButton refreshButton = new ToolbarButton("", new ButtonListenerAdapter() {

            @Override
            public void onClick(Button button, EventObject e) {
                String selectedPath = pathCB.getValue();
                if (!selectedPath.equals(DataManagerConstants.ROOT)) {
                    loadData(selectedPath, false);
                }
            }
        });
        refreshButton.setIcon("images/icon-refresh.gif");
        refreshButton.setCls("x-btn-icon");

        topToolbar.addField(pathCB);
        topToolbar.addButton(folderupButton);
        topToolbar.addButton(refreshButton);

        return topToolbar;
    }

    /**
     * 
     * @param baseDir
     * @param newPath
     */
    public void loadData(String baseDir, boolean newPath) {

        Record[] records = pathCB.getStore().getRecords();
        Object[][] data;
        if (newPath) {
            data = new Object[records.length + 1][1];
            for (int i = 0; i < records.length; i++) {
                data[i][0] = records[i].getAsString(pId + "-path-name");
            }
            data[records.length][0] = baseDir;

        } else {
            data = new Object[records.length][1];
            for (int i = 0; i < records.length; i++) {
                data[i][0] = records[i].getAsString(pId + "-path-name");
            }
        }

        MemoryProxy usersProxy = new MemoryProxy(data);
        simulationsStore.setDataProxy(usersProxy);
        simulationsStore.load();
        simulationsStore.commitChanges();
        pathCB.setValue(baseDir);

        if (!baseDir.equals(DataManagerConstants.ROOT)) {
            Ext.get(pId + "-panel").mask("Loading...");
            FileCatalogServiceAsync service = FileCatalogService.Util.getInstance();
            AsyncCallback<List<Data>> callback = new AsyncCallback<List<Data>>() {

                public void onFailure(Throwable caught) {
                    MessageBox.alert("Error", "Error executing get files list: " + caught.getMessage());
                    Ext.get(pId + "-panel").unmask();
                }

                public void onSuccess(List<Data> result) {
                    if (result != null) {
                        Object[][] data = new Object[result.size()][2];
                        int i = 0;
                        for (Data d : result) {
                            data[i][0] = d.getType();
                            data[i][1] = d.getName();
                            i++;
                        }
                        PagingMemoryProxy proxy = new PagingMemoryProxy(data);
                        store.setDataProxy(proxy);
                        store.load();
                        store.commitChanges();
                    } else {
                        MessageBox.alert("Error", "Unable to get list of files.");
                    }
                    Ext.get(pId + "-panel").unmask();
                }
            };
            Authentication auth = Context.getInstance().getAuthentication();
            service.listDir(auth.getUser(), auth.getProxyFileName(), baseDir, callback);

        } else {
            PagingMemoryProxy proxy = new PagingMemoryProxy(getRootData());
            store.setDataProxy(proxy);
            store.load();
            store.commitChanges();
        }
    }

    private Object[][] getRootData() {
        return new Object[][]{
                    {"Folder", DataManagerConstants.USERS_HOME},
                    {"Folder", DataManagerConstants.PUBLIC_HOME},
                    {"Folder", DataManagerConstants.GROUPS_HOME},
                    {"Folder", DataManagerConstants.ACTIVITIES_HOME},
                    {"Folder", DataManagerConstants.WORKFLOWS_HOME},
                    {"Folder", DataManagerConstants.CREATIS_HOME}
                };
    }

    public String getPathCBValue() {
        return this.pathCB.getValueAsString();
    }
}
