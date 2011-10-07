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
package fr.insalyon.creatis.vip.core.client.view.system;

import fr.insalyon.creatis.vip.core.client.CoreModule;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.system.group.ManageGroupsTab;
import fr.insalyon.creatis.vip.core.client.view.system.news.ManageNewsTab;
import fr.insalyon.creatis.vip.core.client.view.system.user.ManageUsersTab;

/**
 *
 * @author Rafael Silva
 */
public class SystemParser extends ApplicationParser {

    @Override
    public void loadApplications() {

        if (CoreModule.user.isSystemAdministrator()) {
            addApplication(CoreConstants.APP_USER, CoreConstants.APP_IMG_USER);
            addApplication(CoreConstants.APP_GROUP, CoreConstants.APP_IMG_GROUP);
            addApplication(CoreConstants.APP_NEWS, CoreConstants.APP_IMG_NEWS);
        }
    }

    @Override
    public boolean parse(String applicationName) {

        if (applicationName.equals(CoreConstants.APP_USER)) {
            Layout.getInstance().addTab(new ManageUsersTab());
            return true;

        } else if (applicationName.equals(CoreConstants.APP_GROUP)) {
            Layout.getInstance().addTab(new ManageGroupsTab());
            return true;

        } else if (applicationName.equals(CoreConstants.APP_NEWS)) {
            Layout.getInstance().addTab(new ManageNewsTab());
            return true;

        }
        return false;
    }
}