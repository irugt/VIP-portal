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
package fr.insalyon.creatis.vip.core.client.view.auth;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.ModalWindow;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Silva
 */
public class RecoveryTab extends Tab {

    private ModalWindow modal;
    private VLayout vLayout;
    private VLayout recoverLayout;
    private TextItem emailField;
    private TextItem codeField;
    private PasswordItem passwordField;
    private PasswordItem confirmPasswordField;
    private IButton continueButton;
    private IButton resetButton;
    private DynamicForm newForm;

    public RecoveryTab() {

        this.setID(CoreConstants.TAB_RECOVERY);
        this.setTitle("Account Recovery");
        this.setCanClose(true);

        vLayout = new VLayout();
        vLayout.setWidth100();
        vLayout.setHeight100();
        vLayout.setMargin(5);
        vLayout.setOverflow(Overflow.AUTO);
        vLayout.setDefaultLayoutAlign(Alignment.CENTER);

        modal = new ModalWindow(vLayout);

        configureRecoveryLayout();
        configureNewForm();
        vLayout.addMember(recoverLayout);
        vLayout.addMember(newForm);

        this.setPane(vLayout);
    }

    private void configureRecoveryLayout() {

        emailField = FieldUtil.getTextItem(300, false, "", "[a-zA-Z0-9_.\\-+@]");
        emailField.addKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getKeyName().equals("Enter")) {
                    sendResetCode();
                }
            }
        });

        continueButton = new IButton("Continue");
        continueButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                sendResetCode();
            }
        });

        recoverLayout = WidgetUtil.getVIPLayout(320, 150);
        recoverLayout.addMember(WidgetUtil.getLabel("To reset your password, "
                + "enter the email address you use to sign in to VIP.", 25));
        WidgetUtil.addFieldToVIPLayout(recoverLayout, "Email address", emailField);
        recoverLayout.addMember(continueButton);
    }

    private void configureNewForm() {

        newForm = FieldUtil.getForm(FieldUtil.getLinkItem("link_code", "Have a reset code already?",
                new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        configureResetLayout(null);
                    }
                }));
        newForm.setWidth(300);
    }

    private void sendResetCode() {

        if (emailField.validate()) {
            final String email = emailField.getValueAsString().trim();

            ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
            final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                @Override
                public void onFailure(Throwable caught) {
                    modal.hide();
                    SC.warn("Unable to send reset code:<br />" + caught.getMessage());
                }

                @Override
                public void onSuccess(Void result) {
                    configureResetLayout(email);
                    modal.hide();
                    SC.say("A reset code was sent to '" + email + "'. "
                            + "Please use this code to reset the password.");
                }
            };
            modal.show("Sending code...", true);
            service.sendResetCode(email, callback);
        }
    }

    private void configureResetLayout(String email) {

        vLayout.removeMembers(vLayout.getMembers());

        emailField = FieldUtil.getTextItem(300, false, "", "[a-zA-Z0-9_.\\-+@]");
        if (email != null) {
            emailField.setValue(email);
            emailField.setDisabled(true);
        }

        codeField = FieldUtil.getTextItem(300, false, "", "[a-zA-Z0-9\\-]");

        passwordField = FieldUtil.getPasswordItem(150, 32);
        confirmPasswordField = FieldUtil.getPasswordItem(150, 32);

        resetButton = new IButton("Reset Password");
        resetButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {

                if (emailField.validate() & codeField.validate()
                        & passwordField.validate() & confirmPasswordField.validate()) {

                    if (!passwordField.getValueAsString().equals(confirmPasswordField.getValueAsString())) {
                        SC.warn("Passwords do not match. Please verify the entered password.");
                        passwordField.focusInItem();
                        return;
                    }

                    final String email = emailField.getValueAsString().trim();

                    ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
                    final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            modal.hide();
                            SC.warn("Unable to reset password:<br />" + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(Void result) {
                            modal.hide();
                            Layout.getInstance().removeTab(CoreConstants.TAB_RECOVERY);
                            SC.say("Your password was successfully reseted.");
                        }
                    };
                    service.resetPassword(email, codeField.getValueAsString().trim(),
                            passwordField.getValueAsString(), callback);
                }
            }
        });

        recoverLayout = WidgetUtil.getVIPLayout(320, 200);
        WidgetUtil.addFieldToVIPLayout(recoverLayout, "Email address", emailField);
        WidgetUtil.addFieldToVIPLayout(recoverLayout, "Reset Code", codeField);
        WidgetUtil.addFieldToVIPLayout(recoverLayout, "New Password", passwordField);
        WidgetUtil.addFieldToVIPLayout(recoverLayout, "Re-enter Password", confirmPasswordField);
        recoverLayout.addMember(resetButton);

        vLayout.addMember(recoverLayout);
    }
}
