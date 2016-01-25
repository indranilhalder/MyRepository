/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketplaceCallContextController;

import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cscockpit.session.impl.OrderedConfigurableBrowserArea;
import de.hybris.platform.cscockpit.utils.CssUtils;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;
import de.hybris.platform.cscockpit.widgets.models.impl.CallContextWidgetModel;
import de.hybris.platform.cscockpit.widgets.popup.PopupWindowCreator;
import de.hybris.platform.cscockpit.widgets.renderers.impl.CallContextWidgetRenderer;

/**
 * @author Atmaram
 *
 */
public class MarketplaceCallContextWidgetRenderer extends
		CallContextWidgetRenderer {

	/**
	 * @param
	 */
	private static final String SUCCESS = "Success";
	
	private static final String ERROR = "FaildtoValidate";
	
	private static final String NEW_PASSWORD = "newPassword";

	private static final String CONFIRM_PASSWORD = "confirmPassword";
	
	private static final String CHANGE_PASSWORD = "changePassword";
	
	private static final String CHANGE_PASSWORD_BTN = "changePasswordBtn";

	private static final String PASSWORD = "password";

	private static final String INVALID_FORMAT = "invalidFormat";

	private static final String SAMEASPREVIOUS = "sameasprevious";

	private static final String DIFFER = "differ";

	private static final String EXCEEDED = "exceeded";


	private Toolbarbutton changeAgentPwdButton;
	
	private PopupWindowCreator popupWindowCreator;

	
	public PopupWindowCreator getPopupWindowCreator() {
		return popupWindowCreator;
	}



	public void setPopupWindowCreator(PopupWindowCreator popupWindowCreator) {
		this.popupWindowCreator = popupWindowCreator;
	}



	protected HtmlBasedComponent createContentInternal(
			Widget<CallContextWidgetModel, CallContextController> widget,
			HtmlBasedComponent rootContainer) {
		CockpitEventAcceptor addressNotificationEventAcceptor = createAddressNotificationEventAcceptor(widget);
		OrderedConfigurableBrowserArea browserArea = (OrderedConfigurableBrowserArea) UISessionUtils
				.getCurrentSession().getCurrentPerspective().getBrowserArea();
		browserArea.addNotificationListener(widget.getWidgetCode(),
				addressNotificationEventAcceptor);

		Div content = new Div();

		content.appendChild(createSiteContent(widget));
		content.appendChild(createAgentContent(widget));//Agent password block
		content.appendChild(createCustomerContent(widget));
		content.appendChild(createOrderContent(widget));
		content.appendChild(createTicketContent(widget));
		content.appendChild(createCurrencyContent(widget));
		content.appendChild(createEndCallContent(widget));

		return content;
	}
	
	@Override
	  protected void handleOpenOrderSearchEvent(Widget<CallContextWidgetModel, CallContextController> widget, Event event, Div container)
	  {
	    getPopupWidgetHelper().createPopupWidget(container, "csOrderSearchWidgetConfig", "csOrderSearchWidget-Popup", 
	      "csOrderSearchPopup", LabelUtils.getLabel(widget, "popup.orderSearchTitle", new Object[0]),850);
	  }
	@Override
	   protected void handleOpenCustomerSearchEvent(Widget<CallContextWidgetModel, CallContextController> widget, Event event, Div container)
	   {
	     getPopupWidgetHelper().createPopupWidget(container, "csCustomerSearchWidgetConfig", "csCustomerSearchWidget-Popup", 
	       "csCustomerSearchPopup", LabelUtils.getLabel(widget, "popup.customerSearchTitle", new Object[0]),700);
	   }
	   
protected HtmlBasedComponent createAgentContent(
		final Widget<CallContextWidgetModel, CallContextController> widget) {
	 
	UserModel loggedInAgent = ((MarketplaceCallContextController)widget.getWidgetController()).getCurrentUser();
	
	Div container = new Div();
	container.setSclass(CssUtils.combine(new String[] {
			"csCallContextContainer", "csCallContextCustomer" }));

	Div customerInfoContainer = new Div();
	customerInfoContainer.setSclass(CssUtils.combine(new String[] {
			"csCustomerInfoContainer", "csInfoContainer" }));
	customerInfoContainer.setParent(container);

	Label customerLabel = new Label(LabelUtils.getLabel(widget,
			"hasAgent", new Object[0]));
	customerLabel.setSclass(CssUtils.combine(new String[] {
			"customerLabel", "infoLabel" }));
	customerLabel.setParent(customerInfoContainer);

		if (UISessionUtils.getCurrentSession().isUsingTestIDs()) {
			UITools.applyTestID(customerInfoContainer,
					"CallContext_Agent_link");
		}
		String agentName =null;
		if(StringUtils.isNotEmpty(loggedInAgent.getDisplayName())) {
			agentName =loggedInAgent.getDisplayName();
		}else{
			agentName =loggedInAgent.getUid();
		}
		Label containerValue =  new Label(agentName);

			/*containerValue.setSclass(CssUtils.combine(new String[] {
					"csCallContextContainerData",
					"csCallContentCurrentCustomer", "blueLink" }));*/
		
		containerValue.setParent(customerInfoContainer);

/*		containerValue.addEventListener("onClick",
				new EventListener() {
					public void onEvent(Event event) throws Exception {
						
								handleSwitchToCustomerClickEvent(
										widget, event);
					}
				});*/
		createChangeAgentPwdButton(widget, container);
	return container;
}

protected void createChangeAgentPwdButton(
		final Widget<CallContextWidgetModel, CallContextController> widget,
		final Div container) {
	this.changeAgentPwdButton = new Toolbarbutton(LabelUtils.getLabel(widget,
			"changeAgentPwd", new Object[0]));
	this.changeAgentPwdButton.setParent(container);
	this.changeAgentPwdButton.setSclass(CssUtils.combine(new String[] {
			"csCallContextContainerPopupBtn", "blueLink" }));
	if (UISessionUtils.getCurrentSession().isUsingTestIDs()) {
		UITools.applyTestID(this.changeAgentPwdButton,
				"Call_Context_agent_pwd_link");
	}
	this.changeAgentPwdButton.addEventListener("onClick", new EventListener()
			  {
		public void onEvent(Event event) throws Exception {
			handleOpenChangeAgentPwdEvent(
					widget, event);
		}
	});
}


private void handleOpenChangeAgentPwdEvent(
		final Widget<CallContextWidgetModel, CallContextController> widget,
		Event event) {
	Div container = new Div();

	Window popupWindow = getPopupWindowCreator().createModalPopupWindow(
			widget,
			LabelUtils.getLabel(widget, CHANGE_PASSWORD, new Object[0]),
			CHANGE_PASSWORD, 510, container);

	populateChangeAgentPwdForm(widget, container, popupWindow);
}



private void populateChangeAgentPwdForm(
		Widget<CallContextWidgetModel, CallContextController> widget,
		Div container, Window popupWindow) {
	
	container.appendChild(new Label(LabelUtils.getLabel(widget, NEW_PASSWORD,
			new Object[0])));
	
	Textbox passwordTextBox = new Textbox();
	passwordTextBox.setType(PASSWORD);
	if (UISessionUtils.getCurrentSession().isUsingTestIDs()) {
		UITools.applyTestID(passwordTextBox,
				CHANGE_PASSWORD);
	}
	container.appendChild(passwordTextBox);
	
	container.appendChild(new Label(LabelUtils.getLabel(widget,
			CONFIRM_PASSWORD, new Object[0])));
	
	Textbox confirmPasswordTextBox = new Textbox();
	confirmPasswordTextBox.setType(PASSWORD);
	if (UISessionUtils.getCurrentSession().isUsingTestIDs()) {
		UITools.applyTestID(confirmPasswordTextBox,
				CONFIRM_PASSWORD);
	}
	container.appendChild(confirmPasswordTextBox);
	Button resetPassword = new Button();
	resetPassword.setLabel(LabelUtils.getLabel(widget, "OK",
			new Object[0]));
	container.appendChild(resetPassword);
	if (UISessionUtils.getCurrentSession().isUsingTestIDs()) {
		UITools.applyTestID(resetPassword,
				CHANGE_PASSWORD_BTN);
	}
	
	EventListener eventListener = createChangePasswordEventListener(widget,
			passwordTextBox, confirmPasswordTextBox, popupWindow);
	passwordTextBox.addEventListener(Events.ON_OK, eventListener);
	resetPassword.addEventListener(Events.ON_CLICK, eventListener);
	
}


protected EventListener createChangePasswordEventListener(
		final Widget<CallContextWidgetModel, CallContextController> widget,
		Textbox codeTextBox, Textbox codeTextBox1, Window popupWindow) {
	return new ResetPasswordEventListener(widget, codeTextBox,
			codeTextBox1, popupWindow);
}

protected class ResetPasswordEventListener implements EventListener {
	private final Widget<CallContextWidgetModel, CallContextController> widget;
	private final Textbox passwordTextBox;
	private final Textbox confirmPasswordTextBox;
	private final Window popupWindow;

	public ResetPasswordEventListener(
			final Widget<CallContextWidgetModel, CallContextController> widget,
			Textbox codeTextBox, Textbox codeTextBox1, Window popupWindow) {
		this.widget = widget;
		this.passwordTextBox = codeTextBox;
		this.confirmPasswordTextBox = codeTextBox1;
		this.popupWindow = popupWindow;
	}

	public void onEvent(Event event) throws Exception {
		
			handlePasswordResetEvent(this.widget, event,
					this.passwordTextBox, this.confirmPasswordTextBox,
					this.popupWindow);
		
	}
}

protected void handlePasswordResetEvent(
		final Widget<CallContextWidgetModel, CallContextController> widget,
		Event event, Textbox passwordTextBox,
		Textbox confirmPasswordTextBox, Window popupWindow)
		throws InterruptedException {

	String result = ((MarketplaceCallContextController)widget.getWidgetController())
			.changeAgentPassword(passwordTextBox.getValue(), confirmPasswordTextBox.getValue());
	switch (result) {
	
	case "SUCCESS":
		popupWindow.detach();
		Map data = Collections.singletonMap("refresh", Boolean.TRUE);
		widget.getWidgetController().dispatchEvent(null, null, data);
		Messagebox.show(LabelUtils.getLabel(widget, SUCCESS,
				new Object[0]), 	SUCCESS, Messagebox.OK,
				Messagebox.INFORMATION);
		
		break;

	case "FAILED":
		Messagebox.show(LabelUtils.getLabel(widget, INVALID_FORMAT,
				new Object[0]), 	INVALID_FORMAT, Messagebox.OK,
				Messagebox.ERROR);
		break;
	case "SAMEASPREVIOUS":
		Messagebox.show(LabelUtils.getLabel(widget, SAMEASPREVIOUS,
				new Object[0]), 	SAMEASPREVIOUS,Messagebox.OK,
				Messagebox.ERROR);
		break;
		
	case "DIFFER":
		Messagebox.show(LabelUtils.getLabel(widget, DIFFER,
				new Object[0]), 	DIFFER, Messagebox.OK,
				Messagebox.ERROR);
	break;
	
	case "EXCEEDED":
		Messagebox.show(
				LabelUtils.getLabel(widget, EXCEEDED, new Object[0]),
				EXCEEDED, Messagebox.OK, Messagebox.ERROR);
		Executions.sendRedirect("/login.zul");
		break;
		
	default:
		Messagebox.show(LabelUtils.getLabel(widget, ERROR,
				new Object[0]), 	ERROR, Messagebox.OK,
				Messagebox.ERROR);
		
	}		
	
}

@Override
/* No ticket functionality would be used in cs cockpit so hiding it
 * (non-Javadoc)
 * @see de.hybris.platform.cscockpit.widgets.renderers.impl.CallContextWidgetRenderer#createTicketContent(de.hybris.platform.cockpit.widgets.Widget)
 */
protected HtmlBasedComponent createTicketContent(Widget<CallContextWidgetModel, CallContextController> widget)

{
	//super.createTicketContent(widget);
	Div container = new Div();
	return container;
}

}
