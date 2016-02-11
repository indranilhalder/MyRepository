package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;



import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultItemWidgetModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;
import de.hybris.platform.cscockpit.widgets.controllers.OrderManagementActionsWidgetController;
import de.hybris.platform.cscockpit.widgets.renderers.impl.OrderDetailsOrderItemsWidgetRenderer;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.text.ParseException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.tisl.mpl.cockpits.cscockpit.services.ItemModificationHistoryService;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;

public class MarketPlaceAlternateContactDetailsWidgetRenderer
extends OrderDetailsOrderItemsWidgetRenderer
{

	
	private static final Logger LOG = Logger
			.getLogger(MarketPlaceAlternateContactDetailsWidgetRenderer.class);
private static final String CUSTOMER_DETAILS_UPDATED = "customerdetailsupdated";
private static final String INFO = "info";
private static final String FAILED_TO_VALIDATE_PICKUP_DETAILS_FORM = "failedToValidatepickupdetailsForm";
private Boolean valid = Boolean.TRUE;


@Autowired
private FlexibleSearchService flexibleSearhService;


@Autowired
private ModelService modelService;

@Autowired
private ItemModificationHistoryService itemModificationHistoryService;

@Autowired
private ConfigurationService configurationService;

//added by sana
private CallContextController callContextController;
		
protected CallContextController getCallContextController()
			 {
			     return callContextController;
			 }
  
@Required
public void setCallContextController(CallContextController callContextController)
			  {
			     this.callContextController = callContextController;
			  }
			
public TypedObject getOrder()
			  {
			     return getCallContextController().getCurrentOrder();
			  }



 
private Hbox createHbox(final Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget, final String attributeLabel,
final boolean hidden, final boolean overwriteWidth)
		{
					LOG.info("^^^^^^^^Hbox^^^^^^^^^^");
					final Hbox hbox = new Hbox();
					hbox.setWidth("36%");
					if (overwriteWidth)
					{
						hbox.setWidths("9em, none");
					}
					hbox.setAlign("center");
					
					if (hidden)
					{
						hbox.setVisible(false);
					}
					
					hbox.setClass("editorWidgetEditor");
					final Label label = new Label(LabelUtils.getLabel(widget, attributeLabel));
					label.setParent(hbox);
					return hbox;

		}//createHbox






private Textbox createTextbox(final Hbox parent)
		{
					final Textbox textBox = new Textbox();
					textBox.setWidth("40%");
					textBox.setParent(parent);
					return textBox;
		}//Textbox







protected HtmlBasedComponent createContentInternal(
		Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget,
					HtmlBasedComponent rootContainer){
					
					Div content = new Div();
					TypedObject order = getOrder();
					content.setClass("customerDetails");
				try{
					if (valid)
							{	
								final OrderModel ordermodel = (OrderModel) order.getObject();
		
								final Hbox pickupNameHbox = createHbox(widget, "PickupName", false, true);
								final Textbox pickupNameFieldTextBox = createTextbox(pickupNameHbox);
									try{
										if (ordermodel.getPickupPersonName() != null)
											{	
											pickupNameFieldTextBox.setValue(ordermodel.getPickupPersonName());
											}//if
										}//try
									catch (final Exception e) {
											LOG.error("Pickup Name is null", e);
											}//catch
											content.appendChild(pickupNameHbox);	
								
								final Hbox pickupPhoneHbox = createHbox(widget, "PickupPhone", false, true);
								final Textbox pickupPhoneFieldTextBox = createTextbox(pickupPhoneHbox);
									try{
										if (ordermodel.getPickupPersonMobile() != null)
											{
											pickupPhoneFieldTextBox.setValue(ordermodel.getPickupPersonMobile());
											pickupPhoneFieldTextBox.setSclass("pickupNameFieldTextBox");
											pickupPhoneFieldTextBox.setMaxlength(10);
											String errorMsgMobile = LabelUtils.getLabel(widget, "error.msg.mobile",
													new Object[0]);
											pickupPhoneFieldTextBox.setConstraint("/[0-9]*$/:" + errorMsgMobile);
											}//if
										}//try
									catch (final Exception e) {
											LOG.error("Pick Phone Name is null", e);
											}//catch
											content.appendChild(pickupPhoneHbox);
							
					
						final Div actionContainer = new Div();
						actionContainer.setSclass("updateBtn");
						actionContainer.setParent(content);
						// Update button
						final Button update = new Button(LabelUtils.getLabel(widget, "Update",new Object[0]));
						update.setParent(content);
						actionContainer.setAlign("center");
						update.addEventListener(
								Events.ON_CLICK,
								createUpdateDetailsEventListener(widget,ordermodel, pickupNameFieldTextBox, pickupPhoneFieldTextBox));
					   }//if
				
				}//try
				catch (final Exception e) {
					LOG.error("unable to render alternate contacts widget", e);
				}//catch
				
				return content;
				
		}//createContentInternal
	


	
/**
 * 		        
 * @param widget
 * @param orderModel
 * @param pickupNameFieldTextBox
 * @param pickupPhoneFieldTextBox
 * @return
 */
private EventListener createUpdateDetailsEventListener(Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget,
	final OrderModel orderModel, final Textbox pickupNameFieldTextBox, final Textbox pickupPhoneFieldTextBox)
	{
		return new UpdateDetailsEventListener(widget, orderModel, pickupNameFieldTextBox, pickupPhoneFieldTextBox);
	}//createUpdateDetailsEventListener

	protected class UpdateDetailsEventListener implements EventListener{	
						final Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget;	
						private static final String PICKUP_PHONE_REGEX = "^[0-9]{10}";
						private final OrderModel orderModel;
						private final Textbox pickupNameFieldTextBox;
						private final Textbox pickupPhoneFieldTextBox;

				public UpdateDetailsEventListener(
						
						final Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget,
						final OrderModel orderModel, final Textbox pickupNameFieldTextBox,
						final Textbox pickupPhoneFieldTextBox) {
								this.widget = widget;
								this.orderModel = orderModel;
								this.pickupNameFieldTextBox = pickupNameFieldTextBox;
								this.pickupPhoneFieldTextBox = pickupPhoneFieldTextBox;
								}//UpdateDetailsEventListener()
						
			@Override
			public void onEvent(final Event event) throws InterruptedException, ParseException
				{
					try{
						handleUpdateDetails(widget, orderModel, pickupNameFieldTextBox, pickupPhoneFieldTextBox);	
						}
					catch(final Exception e){
							LOG.error("unable to render listner", e);
						}
				}//onEvent
			
			
			
			
			
			private void handleUpdateDetails(final Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget, final OrderModel order, 
					final Textbox pickupNameFieldTextBox,final Textbox pickupPhoneFieldTextBox) throws InterruptedException,
					ParseException
					{
					
						
						if (StringUtils.isBlank(pickupNameFieldTextBox.getValue()) || StringUtils.isBlank(pickupNameFieldTextBox.getValue().trim()))
						{
							Messagebox.show(LabelUtils.getLabel(widget, "pickupNameFieldvalue"), LabelUtils.getLabel(widget, FAILED_TO_VALIDATE_PICKUP_DETAILS_FORM),
									Messagebox.OK, Messagebox.ERROR);
							return;
						}
						else if (pickupNameFieldTextBox.getValue().length() > 255)
						{
							Messagebox.show(LabelUtils.getLabel(widget, "invalidpickupNameLength"),LabelUtils.getLabel(widget, FAILED_TO_VALIDATE_PICKUP_DETAILS_FORM),
									Messagebox.OK, Messagebox.ERROR);
							return;
						}
						
					
						else if (StringUtils.isBlank(pickupPhoneFieldTextBox.getValue()) || StringUtils.isBlank(pickupPhoneFieldTextBox.getValue().trim()))
						{
							Messagebox.show(LabelUtils.getLabel(widget, "pickupPhoneValueField"), LabelUtils.getLabel(widget, FAILED_TO_VALIDATE_PICKUP_DETAILS_FORM),
									Messagebox.OK, Messagebox.ERROR);
							
							return;
						}
						else if (pickupPhoneFieldTextBox.getValue().length() > 10)
						{
							Messagebox.show(LabelUtils.getLabel(widget, "invalidPhoneLength"),LabelUtils.getLabel(widget, FAILED_TO_VALIDATE_PICKUP_DETAILS_FORM),
									Messagebox.OK, Messagebox.ERROR);
							return;
						}
						else if (!(pickupPhoneFieldTextBox.getValue().matches(PICKUP_PHONE_REGEX)))
						{
							
							Messagebox.show(LabelUtils.getLabel(widget, "mobileNumberValueIncorrect"),
									LabelUtils.getLabel(widget, FAILED_TO_VALIDATE_PICKUP_DETAILS_FORM), Messagebox.OK, Messagebox.ERROR);
							return;
						}
						
						else
						{
							valid = true;
						}
						
						if (valid)
						{
					
						final String changedPickupName = pickupNameFieldTextBox.getValue();
						final String changedPickupPhone = pickupPhoneFieldTextBox.getValue();
						
						boolean error = false;
					
						if (!error)
						{
					
			
							if (changedPickupName != null && !changedPickupName.isEmpty())
							{
								order.setPickupPersonName(changedPickupName);
					
							}
							else
							{
								orderModel.setPickupPersonName(MarketplacecommerceservicesConstants.EMPTY);
							}
					
							if (changedPickupPhone != null && !changedPickupPhone.isEmpty())
							{
								order.setPickupPersonMobile(changedPickupPhone);
							}
							else
							{
								orderModel.setPickupPersonMobile(MarketplacecommerceservicesConstants.EMPTY);
							}
						
							itemModificationHistoryService.logItemModification(itemModificationHistoryService.createModificationInfo(order));
							modelService.save(order);
					
							Messagebox.show(LabelUtils.getLabel(widget, CUSTOMER_DETAILS_UPDATED, new Object[0]), INFO, Messagebox.OK,
									Messagebox.INFORMATION);
					
						}//if(!error)
					}//if(valid)

				}//handleUpdateDetails

		}//class UpdateDetailsEventListener()

}//class MarketPlaceAlternateContactDetailsWidgetRenderer





