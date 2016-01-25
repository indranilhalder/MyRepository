/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.widgets.impl.DefaultListboxWidget;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.OrderFraudController;
import de.hybris.platform.cscockpit.widgets.models.impl.DefaultMasterDetailListWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.FraudReportWidgetRenderer;
import de.hybris.platform.fraud.model.FraudSymptomScoringModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

/**
 * @author 890223
 *
 */
public class MplFraudReportWidgetRenderer extends FraudReportWidgetRenderer {

	@Autowired
	private ConfigurationService configurationService;
	
	@Override
	protected void renderButtons(
			final DefaultListboxWidget<DefaultMasterDetailListWidgetModel<FraudSymptomScoringModel>, OrderFraudController> widget,
			final HtmlBasedComponent container) {

	     final Div buttonsContainer = new Div();
	     buttonsContainer.setClass("csButtonsContainer");
	     container.appendChild(buttonsContainer);
	     if (isUserInRole(configurationService
					.getConfiguration()
					.getString(
							"cscockpit.user.group.fraudcsagentgroup"))) {
	     Button proceedButton = new Button(LabelUtils.getLabel(widget, "proceed", new Object[0]));
	     proceedButton.setVisible(((OrderFraudController)widget.getWidgetController()).canProceedOrder());
	     buttonsContainer.appendChild(proceedButton);
	 
	     Button rejectButton = new Button(LabelUtils.getLabel(widget, "reject", new Object[0]));
	     rejectButton.setVisible(((OrderFraudController)widget.getWidgetController()).canRejectOrder());
	     buttonsContainer.appendChild(rejectButton);
	 
	     proceedButton.addEventListener("onClick", new EventListener()
	     {
	       public void onEvent(Event event)
	         throws Exception
	       {
	         handleProceedAction((MouseEvent)event,widget,  buttonsContainer);

	       }
	     });
	     rejectButton.addEventListener("onClick", new EventListener()
	     {
	       public void onEvent(Event event)
	         throws Exception
	       {
	        handleRejectAction((MouseEvent)event, widget,  buttonsContainer);
	       }
	     });
	     }
	   
	}
	 private void handleProceedAction(MouseEvent event, DefaultListboxWidget<DefaultMasterDetailListWidgetModel<FraudSymptomScoringModel>, OrderFraudController> widget, HtmlBasedComponent buttonsContainer)
	   {
	     TypedObject orderObject = ((OrderFraudController)widget.getWidgetController()).getCurrentOrder();
	     ((OrderFraudController)widget.getWidgetController()).proceedWithFraudCheckedOrder(orderObject);
	     buttonsContainer.setVisible(false);
	   }



	   private void handleRejectAction(MouseEvent event, DefaultListboxWidget<DefaultMasterDetailListWidgetModel<FraudSymptomScoringModel>, OrderFraudController> widget, Div buttonsContainer)
	   {
	     TypedObject orderObject = ((OrderFraudController)widget.getWidgetController()).getCurrentOrder();
	     ((OrderFraudController)widget.getWidgetController()).rejectFraudOrder(orderObject);
	     buttonsContainer.setVisible(false);
	   }
	   
		private boolean isUserInRole(String groupName) {
			Set<PrincipalGroupModel> userGroups = UISessionUtils
					.getCurrentSession().getUser().getAllGroups();

			for (PrincipalGroupModel ug : userGroups) {
				if (ug.getUid().equalsIgnoreCase(groupName)) {
					return true;
				}
			}
			return false;
		}
}
