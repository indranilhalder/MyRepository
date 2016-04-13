package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.strategies.MplFindDeliveryFulfillModeStrategy;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketplaceCheckoutController;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketplaceSearchCommandController;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.strategy.MplFindDeliveryCostStrategy;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.SafeUnbox;
import de.hybris.platform.cscockpit.widgets.controllers.BasketController;
import de.hybris.platform.cscockpit.widgets.controllers.CheckoutController;
import de.hybris.platform.cscockpit.widgets.models.impl.CheckoutCartWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.CheckoutCartWidgetRenderer;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

// TODO: Auto-generated Javadoc
/**
 * The Class MarketPlaceCheckoutCartWidgetRenderer.
 */
public class MarketPlaceCheckoutCartWidgetRenderer extends
		CheckoutCartWidgetRenderer {

	/** The Constant PIN_CODE_EMPTY. */
	private static final String PIN_CODE_EMPTY = "pinCodeEmpty";

	/** The Constant SELECT. */
	private static final String SELECT = "select";

	/** The Constant IS_DELIVERY_DATE_REQUIRED. */
	private static final String IS_DELIVERY_DATE_REQUIRED = "isDeliveryDateRequired";

	/** The Constant PINCODE. */
	private static final String PINCODE = "pincode";
	
	/** The Constant NO_RESPONSE_FROM_SERVER. */
	private static final String NO_RESPONSE_FROM_SERVER = "noResponseFromServer";
	private static final String CAN_NOT_PLACE_CNC_ORDER = "CncOrderPlacingNotAllowed";
	private static final String INFO = "info";

	/** The Constant LOG. */
	private static final Logger LOG = Logger
			.getLogger(MarketPlaceCheckoutCartWidgetRenderer.class);

	/** The session service. */
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	MarketplaceSearchCommandController marketplaceSearchCommandController;
	
	@Autowired
	private MplFindDeliveryFulfillModeStrategy mplFindDeliveryFulfillModeStrategy;
	
	
	@Autowired
	private MplFindDeliveryCostStrategy mplFindDeliveryCostStrategy;
	
	
	/**
	 * Render listbox1.
	 *
	 * @param listBox
	 *            the list box
	 * @param widget
	 *            the widget
	 * @param rootContainer
	 *            the root container
	 */
	protected void renderListbox(Listbox listBox,
			ListboxWidget<CheckoutCartWidgetModel, CheckoutController> widget,
			HtmlBasedComponent rootContainer) {
		listBox.setSclass("");
		int count = 0;
		
		for (TypedObject cartEntry : widget.getWidgetController().getBasketController().getEntries())
		//for (TypedObject cartEntry : ((CheckoutCartWidgetModel)widget.getWidgetModel()).getItems())

		{
			Listitem row = new Listitem();
			row.setParent(listBox);
			row.setSclass((count % 2 == 0) ? "csListItemEven" : "csListItemOdd");
			Listcell cell = new Listcell();
			cell.setParent(row);
			cell.setSclass("csCartLine");
			cell.appendChild(createCartEntryInformationBlock(widget, cartEntry));

			if (isDisplayActionsPerLineItem()) {
				Div actionsDiv = new Div();
				actionsDiv.setSclass("csCartLineDeliveryActions");
				cell.appendChild(actionsDiv);

				if (isDisplayPerLineDeliveryModeSelector()) {
					/*LOG.info("Bypass serviceability flag:"+configurationService.getConfiguration().getBoolean(MarketplaceCockpitsConstants.COCKPIT_SERVICEABILITY_CHECK_BYPASS));
					if(configurationService.getConfiguration().getBoolean(MarketplaceCockpitsConstants.COCKPIT_SERVICEABILITY_CHECK_BYPASS, false)) {*/
						actionsDiv.appendChild(createCartEntryDeliveryModeBlock(
							widget, cartEntry));
					/*}else {
						actionsDiv.appendChild(createMplCartEntryDeliveryModeBlock(
								widget, cartEntry));
					}*/
				}

				if (isDisplayPerLineDeliveryDateSelector()) {
					actionsDiv
							.appendChild(createCartEntryNamedDeliveryDateBlock(
									widget, cartEntry));
				}

				if (isDisplayPerLineDeliveryAddressSelector()) {
					actionsDiv.appendChild(createCartEntryDeliveryAddressBlock(
							widget, cartEntry));
				}
			}
			++count;
		}
	}

	
	/**
	 * Creates the cart entry delivery mode block1.
	 *
	 * @param widget
	 *            the widget
	 * @param cartEntry
	 *            the cart entry
	 * @return the html based component
	 */
	private HtmlBasedComponent createMplCartEntryDeliveryModeBlock(
			ListboxWidget<CheckoutCartWidgetModel, CheckoutController> widget,
			TypedObject cartEntry) {
		Div container = new Div();

		CartEntryModel cartEntryModel = (CartEntryModel) cartEntry.getObject();
		// DeliveryModeModel deliveryModeModel =
		// cartEntryModel.getDeliveryMode();

		container.setSclass("csDeliveryActionBox");

		Label header = new Label(LabelUtils.getLabel(widget,
				"currentDeliveryMode", new Object[0]));
		header.setSclass("boxHeader");
		container.appendChild(header);

		Listbox deliveryModeDropdown = new Listbox();
		deliveryModeDropdown.setMold(SELECT);
		deliveryModeDropdown.setRows(1);
		deliveryModeDropdown.setParent(container);
		deliveryModeDropdown.addEventListener(
				Events.ON_SELECT,
				createCartEntryDeliveryModeChangeEventListener(widget,
						cartEntry, false));

		deliveryModeDropdown.appendItem("", null);

		//populateDeliveryModes(widget, cartEntryModel, deliveryModeDropdown);

		return container;
	}

	/**
	 * Populate delivery modes.
	 *
	 * @param widget
	 *            the widget
	 * @param cartEntryModel
	 *            the cart entry model
	 * @param deliveryModeDropdown
	 *            the delivery mode dropdown
	 */
	private void populateDeliveryModes(
			ListboxWidget<CheckoutCartWidgetModel, CheckoutController> widget,
			CartEntryModel cartEntryModel, Listbox deliveryModeDropdown) {
		String pinCode = sessionService.getAttribute(MarketplaceCockpitsConstants.PIN_CODE);
		if(cartEntryModel.getOrder().getDeliveryAddress()!=null){
				pinCode = cartEntryModel.getOrder().getDeliveryAddress().getPostalcode();
			
		}
		try {
			((MarketplaceCheckoutController) widget
					.getWidgetController()).setCurrentSite();
			if (StringUtils.isEmpty(pinCode)) {
				((MarketplaceCheckoutController) widget
						.getWidgetController()).popupMessage(widget,PIN_CODE_EMPTY);
				LOG.info("Pin code value is null, hence the operation could not be performed");
			} else {
				//ProductModel product = ((ProductModel) (cartEntryModel.getProduct()));
				final String isDeliveryDateRequired = LabelUtils.getLabel(widget,
						IS_DELIVERY_DATE_REQUIRED, new Object[0]);
				((MarketplaceCheckoutController) widget
						.getWidgetController()).processPinServiceability(widget, deliveryModeDropdown, pinCode, cartEntryModel,
						isDeliveryDateRequired);
			}
		}catch(EtailNonBusinessExceptions ex) {
			LOG.error("EtailNonBusinessException while fetching pinCodeResponses:",ex);
			((MarketplaceCheckoutController) widget
					.getWidgetController())
					.popupMessage(widget, NO_RESPONSE_FROM_SERVER+":"+ex);
		}catch(ClientEtailNonBusinessExceptions e) {
			LOG.error("ClientEtailNonBusinessExceptions while fetching pinCodeResponses:",e);
			((MarketplaceCheckoutController) widget
					.getWidgetController()).popupMessage(widget,NO_RESPONSE_FROM_SERVER+":"+e);
		}
	}
	
	   protected void handleCartEntryDeliveryModeChangeEvent(ListboxWidget<CheckoutCartWidgetModel, CheckoutController> widget, Event event, TypedObject item, boolean initial)
	   {
	     BasketController basketController = ((CheckoutController)widget.getWidgetController()).getBasketController();
	     boolean changed = false;
	    
	   
	     
	       if (initial )
	     {
	    	 
	    	 List<MplZoneDeliveryModeValueModel> deliveryModes = ((MarketplaceCheckoutController) widget
						.getWidgetController()).getDeliveryModesAndCost(MarketplaceCockpitsConstants.INR,
						((AbstractOrderEntryModel) item.getObject())
								.getSelectedUSSID());

				TypedObject deliveryMode = null;

				if (CollectionUtils.isNotEmpty(deliveryModes)) {
					deliveryMode = getCockpitTypeService().wrapItem(
							deliveryModes.iterator().next());
					changed = ((CheckoutController)widget.getWidgetController()).getBasketController()
							.setCartEntryDeliveryMode(item, deliveryMode);
				}
				/* try {
					((MarketplaceCheckoutController)widget.getWidgetController()).validateWithOMS(item, deliveryMode);
				} catch (ValidationException e) {
					 try {
							Messagebox.show(e.getMessage() + ((e.getCause() == null) ? "" : new StringBuilder(" - ").append(e.getCause().getMessage()).toString()), 
							     LabelUtils.getLabel(widget, "failedToValidate", new Object[0]), 1, "z-msgbox z-msgbox-error");
						} catch (InterruptedException e1) {
							//
						}
				}*/
	          
	     }
	     else if (event instanceof SelectEvent)
	     {
	       SelectEvent selectEvent = (SelectEvent)event;
	       Set selectedItems = selectEvent.getSelectedItems();
	       if ((selectedItems != null) && (!(selectedItems.isEmpty())))
	       {
	         Listitem selectedItem = (Listitem)selectedItems.iterator().next();
	         TypedObject selectedDeliveryMode = (TypedObject)selectedItem.getValue();
	         
	         if (selectedDeliveryMode != null)
	         {
	        	 try{
	        	 MplZoneDeliveryModeValueModel selectedDeliveryModeValue=(MplZoneDeliveryModeValueModel)selectedDeliveryMode.getObject();
	        	 if(selectedDeliveryModeValue.getDeliveryMode().getName().equalsIgnoreCase(MarketplaceCockpitsConstants.delNameMap
							.get("CnC"))) {
		        	 changed = ((CheckoutController)widget.getWidgetController()).getBasketController().setCartEntryDeliveryMode(item, null);
		        	
		        	 try {
						Messagebox.show(LabelUtils.getLabel(widget,
									CAN_NOT_PLACE_CNC_ORDER, new Object[0]), INFO,
									Messagebox.OK, Messagebox.INFORMATION);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	 
		         }
	        	 else { 
	        	 try{ 
	        		        		 
	        		 ((MarketplaceCheckoutController)widget.getWidgetController()).validateWithOMS(item, selectedDeliveryMode);
	        		 
	        		 changed = ((CheckoutController)widget.getWidgetController()).getBasketController().setCartEntryDeliveryMode(item, selectedDeliveryMode);
	        		 	
		        	} catch (ValidationException e)
		        		   {
		        		changed = ((CheckoutController)widget.getWidgetController()).getBasketController().setCartEntryDeliveryMode(item, null);
		        		      try {
								Messagebox.show(e.getMessage() + ((e.getCause() == null) ? "" : new StringBuilder(" - ").append(e.getCause().getMessage()).toString()), 
								     LabelUtils.getLabel(widget, "failedToValidate", new Object[0]), 1, "z-msgbox z-msgbox-error");
							} catch (InterruptedException e1) {
								//
							}
		        		    }
	           //changed = ((CheckoutController)widget.getWidgetController()).getBasketController().setCartEntryDeliveryMode(item, selectedDeliveryMode);
	         }
	         }catch(Exception e) {
	        	 LOG.debug("Exception "+e);
	         }
	         }
	         else{
	        	 changed = ((CheckoutController)widget.getWidgetController()).getBasketController().setCartEntryDeliveryMode(item, null);
	         }
	         }
	       }
	       
	     else if (event instanceof MouseEvent)

	     {
	       changed = ((CheckoutController)widget.getWidgetController()).getBasketController().setCartEntryDeliveryMode(item, null);
	     }
	 
	     if (!(changed))
	       return;
	     Map data = Collections.singletonMap("refresh", Boolean.TRUE);
	     basketController.dispatchEvent(null, this, data);
	     widget.getWidgetController().dispatchEvent(null, widget, data);
	      
	   
	   }
	   
	   @Override
	protected HtmlBasedComponent createCartEntryInformationBlock(
			ListboxWidget<CheckoutCartWidgetModel, CheckoutController> widget,
			TypedObject cartEntry) {
		   CartEntryModel cartEntryModel = (CartEntryModel)cartEntry.getObject();
		   if(cartEntryModel ==null) return  new Div();
		   Div container = new Div();

		    Div cartEntryPropertiesContainer = new Div();
		    cartEntryPropertiesContainer.setSclass("csProperties");
		    cartEntryPropertiesContainer.setParent(container);

		    Div cartEntryTotalsContainer = new Div();
		    cartEntryTotalsContainer.setSclass("csEntryTotals");
		    cartEntryTotalsContainer.setParent(cartEntryPropertiesContainer);

		    final CurrencyModel cartCurrencyModel = ((AbstractOrderModel)((CheckoutCartWidgetModel)widget.getWidgetModel()).getCart().getObject()).getCurrency();

		    NumberFormat currencyInstance = (NumberFormat)getSessionService().executeInLocalView(new SessionExecutionBody()

		    {
		      public Object execute()
		      {
		        getCommonI18NService().setCurrentCurrency(cartCurrencyModel);
		        return getFormatFactory().createCurrencyFormat();


		      }
		    });
		    Div priceContainer = new Div();
		    priceContainer.setParent(cartEntryTotalsContainer);
		    Double basePriceValue = cartEntryModel.getBasePrice();
		    String basePriceString = (basePriceValue != null) ? currencyInstance.format(basePriceValue) : "";
		    priceContainer.appendChild(new Label(LabelUtils.getLabel(widget, "basePrice", new Object[0])));
		    priceContainer.appendChild(new Label(basePriceString));


		    Double totalPriceValue = cartEntryModel.getTotalPrice();
		    String totalPriceString = (totalPriceValue != null) ? currencyInstance.format(totalPriceValue) : "";
		    priceContainer.appendChild(new Label(LabelUtils.getLabel(widget, "totalPrice", new Object[0])));
		    priceContainer.appendChild(new Label(totalPriceString));



		    Long qty = cartEntryModel.getQuantity();
		    Longbox quantityInput = new Longbox();
		    if (qty != null)
		    {
		      quantityInput.setValue(qty);
		    }
		    quantityInput.setWidth("33px");
		    priceContainer.appendChild(new Label(LabelUtils.getLabel(widget, "qty", new Object[0])));
		    priceContainer.appendChild(quantityInput);


		    Div actionContainer = new Div();
		    actionContainer.setSclass("csCartLineActions");
		    actionContainer.setParent(cartEntryPropertiesContainer);


		    String rowMessage = getRowActionMessage(cartEntry);
		    if (rowMessage != null)
		    {
		      Label rowMessageLabel = new Label(rowMessage);
		      rowMessageLabel.setSclass("csCartEntryMessage");
		      actionContainer.appendChild(rowMessageLabel);

		    }

		    Button changeCartEntryQtyButton = new Button(LabelUtils.getLabel(widget, "changeCartEntryQtyBtn", new Object[0]));
		    changeCartEntryQtyButton.setParent(actionContainer);
		    changeCartEntryQtyButton.setSclass("btngreen");


		    EventListener basketListener = createCartEntryQtyChangeEventListener(widget, cartEntry, quantityInput);
		    changeCartEntryQtyButton.addEventListener("onClick", basketListener);

		    Button splitCartEntryButton = new Button(LabelUtils.getLabel(widget, "splitCartEntryBtn", new Object[0]));
		    splitCartEntryButton.setParent(actionContainer);
		    splitCartEntryButton.setSclass("btngreen");
		    //splitCartEntryButton.setDisabled(SafeUnbox.toLong(qty, 0L) < 2L);
		    //sub pressing TISEE-5268
		    splitCartEntryButton.setDisabled(true);
		    splitCartEntryButton.addEventListener("onClick", createSplitCartEntryEventListener(widget, cartEntry));


		    List columns = getColumnConfigurations();
		    getPropertyRendererHelper().buildPropertyValuesFromColumnConfigs(cartEntry, columns, cartEntryPropertiesContainer);




		    return container;
		   
	}


	   
	private void setFreeBieDeliveryMode(ListboxWidget widget ,AbstractOrderEntryModel freeEntry){
			for(AbstractOrderEntryModel entryM :  freeEntry.getOrder().getEntries()){
				for(String id : freeEntry.getAssociatedItems() ){
					if(entryM.getSelectedUSSID().equals(id)){
						freeEntry.setMplDeliveryMode(entryM.getMplDeliveryMode());
						 ((MarketplaceCheckoutController) widget
									.getWidgetController()).setFreeBiDeliveryModesAndCost(freeEntry, entryM.getMplDeliveryMode());
						break;
					}
				}
			}
	}
	protected HtmlBasedComponent createCartEntryDeliveryModeBlock(
				ListboxWidget widget, TypedObject cartEntry) {
			Div container = new Div();
			CartEntryModel cartEntryModel = (CartEntryModel) cartEntry.getObject();
			
			boolean isFree = cartEntryModel.getGiveAway()!=null && cartEntryModel.getGiveAway().booleanValue();
			if(isFree){
				setFreeBieDeliveryMode(widget, cartEntryModel);
			}
			MplZoneDeliveryModeValueModel deliveryModeModel = cartEntryModel
					.getMplDeliveryMode();
/*			if (deliveryModeModel == null) {
				container.setSclass("csDeliveryActionLink");
				Toolbarbutton changeDeliveryModeButton = new Toolbarbutton(
						LabelUtils.getLabel(widget, "changeDeliveryModeBtn",
								new Object[0]));
				changeDeliveryModeButton.addEventListener(
						"onClick",
						createCartEntryDeliveryModeChangeEventListener(widget,
								cartEntry, false));
				changeDeliveryModeButton.setSclass("blueLink");
				container.appendChild(changeDeliveryModeButton);
			} else {*/
				container.setSclass("csDeliveryActionBox");
				Label header = new Label(LabelUtils.getLabel(widget,
						"currentDeliveryMode", new Object[0]));
				header.setSclass("boxHeader");
				container.appendChild(header);
				Listbox deliveryModeDropdown = new Listbox();
				deliveryModeDropdown.setDisabled(isFree);
				deliveryModeDropdown.setMold("select");
				deliveryModeDropdown.setRows(1);
				deliveryModeDropdown.setParent(container);
				deliveryModeDropdown.addEventListener(
						"onSelect",
						createCartEntryDeliveryModeChangeEventListener(widget,
								cartEntry, false));
				deliveryModeDropdown.appendItem("", null);

				final CurrencyModel cartCurrencyModel = cartEntryModel.getOrder()
						.getCurrency();

				List<MplZoneDeliveryModeValueModel> deliveryModes = ((MarketplaceCheckoutController) widget
						.getWidgetController()).getDeliveryModesAndCost(
						cartCurrencyModel.getIsocode(),
						cartEntryModel.getSelectedUSSID());

				NumberFormat currencyInstance = (NumberFormat) getSessionService()
						.executeInLocalView(new SessionExecutionBody() {
							public Object execute() {
								MarketPlaceCheckoutCartWidgetRenderer.this
										.getCommonI18NService().setCurrentCurrency(
												cartCurrencyModel);
								return MarketPlaceCheckoutCartWidgetRenderer.this
										.getFormatFactory().createCurrencyFormat();
							}
						});

				if (deliveryModes != null) {
					StringBuilder sb = null;
					for (final MplZoneDeliveryModeValueModel deliveryEntry : deliveryModes) {
						sb = new StringBuilder();
						
						sb.append(deliveryEntry.getDeliveryMode().getName()).
						append( " (").
						append( deliveryEntry.getDeliveryMode().getCode()).
						append( ")").
						append( " - ").
						append( currencyInstance.format(mplFindDeliveryFulfillModeStrategy.isTShip(cartEntryModel.getSelectedUSSID()) ? new Double("0"): deliveryEntry
								.getValue())).
						append( "  ").
						append(mplFindDeliveryCostStrategy.getDeliveryModeDesc(deliveryEntry, cartEntryModel.getSelectedUSSID()) );
						
						Listitem deliveryModeItem = new Listitem(
								sb.toString(),
								getCockpitTypeService().wrapItem(deliveryEntry));
						
						deliveryModeItem.setParent(deliveryModeDropdown);
						
						
						if (deliveryModeModel !=null && ObjectUtils.nullSafeEquals(deliveryModeModel
								.getDeliveryMode().getCode(), deliveryEntry
								.getDeliveryMode().getCode()))
							deliveryModeItem.setSelected(true);
					}
				}
				if(!isFree){
					Toolbarbutton actionButton = new Toolbarbutton(LabelUtils.getLabel(
							widget, "removeDeliveryModeBtn", new Object[0]));
					actionButton.setParent(container);
					actionButton.addEventListener(
							"onClick",
							createCartEntryDeliveryModeChangeEventListener(widget,
									cartEntry, false));
					actionButton.setSclass("csRemoveFacetButton");
				}
			/*}*/
			return container;
		}

	@Override
	   protected void handleCartEntryQtyChangeEvent(ListboxWidget<CheckoutCartWidgetModel, CheckoutController> widget, Event event, TypedObject item, Longbox qtyInput)
	   {
	     Long qty = qtyInput.getValue();
	     if ((qty == null) || 

	       (item.getObject() == null))
	       return;
	     AbstractOrderEntryModel entry = (AbstractOrderEntryModel)item.getObject();
		  String pincode=sessionService.getAttribute(PINCODE);
		  if(entry.getOrder().getDeliveryAddress()!=null){
			  pincode = entry.getOrder().getDeliveryAddress().getPostalcode();
		  }
		  try {
			  boolean byPassOmsChecks=configurationService.getConfiguration().getBoolean(MarketplaceCockpitsConstants.COCKPIT_SERVICEABILITY_CHECK_BYPASS);
				LOG.info("Bypass the pincode serviceability:"
						+ byPassOmsChecks);
				if (byPassOmsChecks) {
					 BasketController basketController = ((CheckoutController)widget.getWidgetController()).getBasketController();
				     basketController.setQuantity(item, SafeUnbox.toLong(qty));
				     basketController.dispatchEvent(null, widget, null);
				} else {
					if(StringUtils.isNotEmpty(pincode)) {
						  final ProductModel product = entry.getProduct();
							final String isDeliveryDateRequired = LabelUtils.getLabel(widget,
									"isDeliveryDateRequired", new Object[0]);
							List<PinCodeResponseData> pinCodeResponses = marketplaceSearchCommandController.getResponseForPinCode(product,
									String.valueOf(pincode), isDeliveryDateRequired,entry.getSelectedUSSID());
							LOG.info("pinCodeResponse:" + pinCodeResponses.size());

							for(PinCodeResponseData response:pinCodeResponses) {
								if(qty<=response.getStockCount()) {
									 BasketController basketController = ((CheckoutController)widget.getWidgetController()).getBasketController();
								     basketController.setQuantity(item, SafeUnbox.toLong(qty));
								     basketController.dispatchEvent(null, widget, null);
								} else {
									LOG.info("The item could not be updated due to insufficient quantity. Code:"+product.getCode());
									throw new ClientEtailNonBusinessExceptions("The item could not be updated due to insufficient quantity. Code:"+product.getCode());
								}
								break;
							}
					  }
				}
		  } catch(ClientEtailNonBusinessExceptions ex) {
			  LOG.error("ClientEtailNonBusinessExceptions in handleCartEntryQtyChangeEvent",ex);
			  throw new ClientEtailNonBusinessExceptions("The item could not be updated due to insufficient quantity.");
		  }
	   }
	
}