package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Span;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.services.search.meta.processor.MplMetaProductPriceValue;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceBasketController;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketplaceSearchCommandController;
import com.tisl.mpl.cockpits.cscockpit.widgets.models.impl.MarketplaceSearchResultWidgetModel;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.strategy.MplFindDeliveryCostStrategy;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnGroupConfiguration;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cscockpit.model.data.DataObject;
import de.hybris.platform.cscockpit.services.search.CsFacetSearchCommand;
import de.hybris.platform.cscockpit.utils.CssUtils;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.SafeUnbox;
import de.hybris.platform.cscockpit.widgets.controllers.search.SearchCommandController;
import de.hybris.platform.cscockpit.widgets.models.impl.SearchResultWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.BasketResultWidgetRenderer;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.PriceValue;

// TODO: Auto-generated Javadoc
/**
 * The Class MarketPlaceBasketResultWidgetRenderer.
 *
 * @param <SC>
 *            the generic type
 */
public class MarketPlaceBasketResultWidgetRenderer<SC extends CsFacetSearchCommand>
		extends BasketResultWidgetRenderer<SC> {
	
	@Autowired
	private SessionService sessionService;
	
	
	/** The Constant IS_DELIVERY_DATE_REQUIRED. */
	private static final String IS_DELIVERY_DATE_REQUIRED = "isDeliveryDateRequired";

	private static final String PIN_REGEX = "^[1-9][0-9]{5}";
	
	/** The Constant INFO. */
	private static final String INFO = "Info";

	/** The Constant NO_RESPONSE_FROM_SERVER. */
	private static final String NO_RESPONSE_FROM_SERVER = "noResponseFromServer";

	/** The Constant YES. */
	private static final String YES = "Y";

	/** The Constant NOT_SERVICE_ABLE. */
	private static final String NOT_SERVICE_ABLE = "notServiceAble";

	/** The Constant INVENTORY_INSUFFICIENT. */
	private static final String INVENTORY_INSUFFICIENT = "inventoryInsufficient";
	
	@Autowired
	private ConfigurationService configurationService;
	

	/** The Constant LOG. */
	private static final Logger LOG = Logger
			.getLogger(MarketPlaceBasketResultWidgetRenderer.class);

	/** The basket controller. */
	@Autowired
	private MarketPlaceBasketController basketController;

	@Autowired
	private MplFindDeliveryCostStrategy mplFindDeliveryCostStrategy;
	
	/**
	 * Creates the content internal.
	 *
	 * @param widget
	 *            the widget
	 * @param rootContainer
	 *            the root container
	 * @return the html based component
	 */
	@Override
	protected HtmlBasedComponent createContentInternal(
			ListboxWidget<SearchResultWidgetModel, SearchCommandController<SC>> widget,
			HtmlBasedComponent rootContainer) {
		if ((widget.getWidgetModel() != null)
				&& (((SearchResultWidgetModel) widget.getWidgetModel())
						.getItems() != null)
				&& (!(((SearchResultWidgetModel) widget.getWidgetModel())
						.getItems().isEmpty()))) {
			HtmlBasedComponent container = super.createContentInternal(widget,
					rootContainer);

			Component firstChild = container.getFirstChild();

			Div toolbar = new Div();
			toolbar.setSclass("csToolbar");

			createAndPopulatePinCode(widget, toolbar);
			container.insertBefore(toolbar, firstChild);

			return container;
		}

		Div content = new Div();

		Label dummyLabel = new Label(LabelUtils.getLabel(widget, "noResults",
				new Object[0]));
		dummyLabel.setParent(content);

		return content;
	}

	/**
	 * Creates the and populate pin code.
	 *
	 * @param widget
	 *            the widget
	 * @param toolbar
	 *            the toolbar
	 */
	private void createAndPopulatePinCode(
			ListboxWidget<SearchResultWidgetModel, SearchCommandController<SC>> widget,
			Div toolbar) {
		Div pincodediv = new Div();
		Hbox hbox = new Hbox();
		hbox.setWidths("40%, 40% ");
		pincodediv.appendChild(hbox);
		toolbar.appendChild(pincodediv);
		CartModel cart = (CartModel) (basketController.getCart().getObject());

		Label pincodeLabel = new Label(LabelUtils.getLabel(widget,
				"pincodeLabel", new Object[0]));

		hbox.appendChild(pincodeLabel);

		Longbox pincodeBox = new Longbox();
		pincodeBox.setMaxlength(Integer.valueOf(LabelUtils.getLabel(widget,
				"maxLength", new Object[0])));
		//pincodeBox.setConstraint("/[0-9]/");
		
/*		if(null!=sessionService.getAttribute("isclear")){
		if (null != cart
				&& CollectionUtils.isEmpty(cart.getEntries())){
			((MarketplaceSearchResultWidgetModel)widget.getWidgetModel()).setPinCode(null);			
			pincodeBox.setDisabled(false);
		}
		sessionService.removeAttribute("isclear");
		}
		
		if(null!=((MarketplaceSearchResultWidgetModel)widget.getWidgetModel()).getPinCode()) {
			pincodeBox.setValue(((MarketplaceSearchResultWidgetModel)widget.getWidgetModel()).getPinCode());
			pincodeBox.setDisabled(true);
		}
		hbox.appendChild(pincodeBox);

		if (null != cart
				&& CollectionUtils.isNotEmpty(cart.getEntries())
				&& null != ((MarketplaceSearchResultWidgetModel) widget
						.getWidgetModel()).getPinCode()) {
			pincodeBox.setDisabled(true);
			pincodeBox.setValue(((MarketplaceSearchResultWidgetModel) widget
					.getWidgetModel()).getPinCode());
		} else {
			pincodeBox.addEventListener(Events.ON_CHANGE,
					createAddPinCodeListener(widget, pincodeBox));
		}*/
		
		
		hbox.appendChild(pincodeBox);

		pincodeBox.addEventListener(Events.ON_BLUR,
				createAddPinCodeListener(widget, pincodeBox));
		
		if (null != ((MarketplaceSearchResultWidgetModel) widget
						.getWidgetModel()).getPinCode()) {
			pincodeBox.setDisabled(!cart.getEntries().isEmpty());
			pincodeBox.setValue(((MarketplaceSearchResultWidgetModel) widget
					.getWidgetModel()).getPinCode());
		}
		
		
		if (null != sessionService.getAttribute("isclear")) {
			if (CollectionUtils.isEmpty(cart.getEntries())) {
				pincodeBox.setDisabled(false);
				((MarketplaceSearchResultWidgetModel) widget.getWidgetModel())
						.setPinCode(null);
				pincodeBox.setValue(null);
				sessionService.removeAttribute("pincode");
			}
			sessionService.removeAttribute("isclear");
		}

	}

	/**
	 * Creates the add to basket event listener.
	 *
	 * @param widget
	 *            the widget
	 * @param pincodeValue
	 *            the pincode value
	 * @return the event listener
	 */
	protected EventListener createAddPinCodeListener(
			ListboxWidget<SearchResultWidgetModel, SearchCommandController<SC>> widget,
			final Longbox pincodeValue) {
		return new AddPinCodeEventListener(widget, pincodeValue);
	}

	/**
	 * The listener interface for receiving addPinCodeEvent events. The class
	 * that is interested in processing a addPinCodeEvent event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addAddPinCodeEventListener<code> method. When
	 * the addPinCodeEvent event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see AddPinCodeEventEvent
	 */
	protected class AddPinCodeEventListener implements EventListener {

		/** The widget. */
		private final ListboxWidget<SearchResultWidgetModel, SearchCommandController<SC>> widget;

		/** The pincode value. */
		private final Longbox pincodeValue;

		/**
		 * Instantiates a new adds the pin code event listener.
		 *
		 * @param widget
		 *            the widget
		 * @param pincodeValue
		 *            the pincode value
		 */
		public AddPinCodeEventListener(
				ListboxWidget<SearchResultWidgetModel, SearchCommandController<SC>> widget,
				final Longbox pincodeValue) {
			this.widget = widget;
			this.pincodeValue = pincodeValue;

		}

		/**
		 * On event.
		 *
		 * @param event
		 *            the event
		 */
		public void onEvent(Event event) {
			Long pincode = pincodeValue.getValue();
			
			if(null!=pincode){
			if(String.valueOf(pincode).matches(PIN_REGEX)){				
				((MarketplaceSearchResultWidgetModel) widget.getWidgetModel())
				.setPinCode(pincode);
				sessionService.setAttribute("pincode", pincode.toString());
				LOG.info("Pin code entered:" + pincode);
			} else{
				((MarketplaceSearchResultWidgetModel) widget.getWidgetModel())
				.setPinCode(null);
				pincodeValue.setValue(null);
				pincodeValue.setFocus(true);
				sessionService.removeAttribute("pincode");
				popupMessage(widget, MarketplaceCockpitsConstants.PIN_CODE_INVALID);
			}
			}

			LOG.info("Pin code entered:" + pincode);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.cscockpit.widgets.renderers.impl.
	 * BasketResultWidgetRenderer
	 * #populateMasterRow(de.hybris.platform.cockpit.widgets.ListboxWidget,
	 * org.zkoss.zul.Listitem,
	 * de.hybris.platform.cscockpit.widgets.renderers.impl
	 * .BasketResultWidgetRenderer.RowContext,
	 * de.hybris.platform.cscockpit.model.data.DataObject)
	 */
	@Override
	protected void populateMasterRow(
			final ListboxWidget<SearchResultWidgetModel, SearchCommandController<SC>> widget,
			final Listitem row, final RowContext context,
			DataObject<TypedObject> metaItem) {
		TypedObject item = (TypedObject) metaItem.getItem();

		if (CollectionUtils.isNotEmpty(context.getColumns())) {
			Listcell cell = null;;
			for (ColumnGroupConfiguration col : context.getColumns()) {
				cell = new Listcell();
				cell.setSclass("csMasterContentCell");
				row.appendChild(cell);
 				getPropertyRendererHelper().buildMasterValue(item, col, cell); 
			}
					buildUSSIDColumn(metaItem, cell);
		}

		row.appendChild(new Listcell(
				formatProductPrice(getProductPriceValue(metaItem))));

		List<TypedObject> potentialProductPromotions = getPotentialProductPromotions(metaItem);
		if ((potentialProductPromotions != null)
				&& (!(potentialProductPromotions.isEmpty()))) {
			row.setSclass(CssUtils.combine(new String[] { row.getSclass(),
					"hasPromotions" }));
		}

		if (canProductBeAddedToCart(metaItem))

		{
			Listcell qtyCell = new Listcell();
			Longbox quantityInput = new Longbox(1);
			quantityInput.setWidth("33px");
			quantityInput.setParent(qtyCell);
			row.appendChild(qtyCell);

			Listcell actionCell = new Listcell();
			Button actionButton = new Button(LabelUtils.getLabel(widget,
					"addToBasketBtn", new Object[0]));
			actionButton.setParent(actionCell);
			actionButton.setSclass("btngreen");

			if (UISessionUtils.getCurrentSession().isUsingTestIDs()) {
				ProductModel productModel = (item.getObject() instanceof ProductModel) ? (ProductModel) item
						.getObject() : null;
				String itemCode = (productModel == null) ? "unknown"
						: productModel.getCode();

				UITools.applyTestID(quantityInput,
						"Cart_SearchResult_Product_Quantity_input_" + itemCode);
				UITools.applyTestID(actionButton,
						"Cart_SearchResult_Product_Actions_input_" + itemCode);

			}

			String productMessage = getProductMessage(metaItem);
			if ((productMessage != null) && (productMessage.length() > 0)) {
				Div stockDiv = new Div();
				stockDiv.setSclass("csProductMessage");
				Label stockMessageLabel = new Label(productMessage);
				stockDiv.appendChild(stockMessageLabel);
				actionCell.appendChild(stockDiv);
			}

			row.appendChild(actionCell);
			
			String ussid = null;
			MplMetaProductPriceValue metaProductPriceValue = (MplMetaProductPriceValue) metaItem
					.getMeta(MplMetaProductPriceValue.class);
			if (metaProductPriceValue != null) {
				ussid = metaProductPriceValue.getUssid();
			}

			EventListener addToBasketListener = createAddToBasketEventListener(
					widget, item, quantityInput,ussid);
			context.setAddToBasketListener(addToBasketListener);

			actionButton.addEventListener("onClick", addToBasketListener);
			row.addEventListener("onOK", addToBasketListener);

			row.setCtrlKeys(row.getCtrlKeys() + "@a");

			row.addEventListener("onCtrlKey", new EventListener() {
				public void onEvent(Event event) throws Exception {
					if (!(event instanceof org.zkoss.zk.ui.event.KeyEvent)) {
						return;
					}
					handleRowKeyEvent(widget,
							(org.zkoss.zk.ui.event.KeyEvent) event, row,
							context);
				}

			});
		} else {
			Listcell qtyCell = new Listcell();
			row.appendChild(qtyCell);

			Listcell actionCell = new Listcell();
			row.appendChild(actionCell);

			String productMessage = getProductMessage(metaItem);
			if ((productMessage != null) && (productMessage.length() > 0)) {
				Div stockDiv = new Div();
				stockDiv.setSclass("csProductMessage");
				Label stockMessageLabel = new Label(productMessage);
				stockDiv.appendChild(stockMessageLabel);
				actionCell.appendChild(stockDiv);

			}

			context.setAddToBasketListener(null);
		}
	}

	private void buildUSSIDColumn(DataObject<TypedObject> metaItem,
			Listcell cell) {
		Div lineContainer = new Div();
		lineContainer.setParent(cell);
     lineContainer.setSclass("csWidgetMasterDetailListboxCellLine" + 1);

		  Span fieldContainer = new Span();
		  fieldContainer.setSclass("csFieldContainer");
		  fieldContainer.setParent(lineContainer);

		  Label fieldLabel = new Label("USSID" + ": ");
		  fieldLabel.setSclass("csListLabel" + CssUtils.sanitizeForCss("Ussid"));
		  fieldLabel.setParent(fieldContainer);
		  String value= null;
		  MplMetaProductPriceValue metaProductPriceValue = (MplMetaProductPriceValue)metaItem.getMeta(MplMetaProductPriceValue.class);
		      if (metaProductPriceValue != null)
		      {
		    	  value = metaProductPriceValue.getUssid() +  " " + metaProductPriceValue.getSellerName();
		      }
		  Span fieldValues = new Span();
		  fieldLabel = new Label(value);
		  fieldLabel.setParent(fieldValues);
		  fieldValues.setSclass("csListValue" + CssUtils.sanitizeForCss("Ussid"));
		  fieldValues.setParent(fieldContainer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.cscockpit.widgets.renderers.impl.
	 * BasketResultWidgetRenderer
	 * #createAddToBasketEventListener(de.hybris.platform
	 * .cockpit.widgets.ListboxWidget,
	 * de.hybris.platform.cockpit.model.meta.TypedObject, org.zkoss.zul.Longbox)
	 */
	protected EventListener createAddToBasketEventListener(
			ListboxWidget<SearchResultWidgetModel, SearchCommandController<SC>> widget,
			TypedObject item, Longbox pincodeBox, String ussid) {
		return new AddToBasketEventListener(widget, item, pincodeBox, ussid);
	}

	/**
	 * The listener interface for receiving addToBasketEvent events. The class
	 * that is interested in processing a addToBasketEvent event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addAddToBasketEventListener<code> method. When
	 * the addToBasketEvent event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see AddToBasketEventEvent
	 */
	protected class AddToBasketEventListener implements EventListener {

		/** The widget. */
		private final ListboxWidget<SearchResultWidgetModel, SearchCommandController<SC>> widget;

		/** The item. */
		private final TypedObject item;

		/** The qty input. */
		private final Longbox qtyInput;
		
		private final String ussid;

		/**
		 * Instantiates a new adds the to basket event listener.
		 *
		 * @param widget
		 *            the widget
		 * @param item
		 *            the item
		 * @param qtyInput
		 *            the qty input
		 * @param ussid 
		 */
		public AddToBasketEventListener(
				ListboxWidget<SearchResultWidgetModel, SearchCommandController<SC>> widget,
				TypedObject item, Longbox qtyInput, String ussid) {
			this.widget = widget;
			this.qtyInput = qtyInput;
			this.item = item;
			this.ussid = ussid;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.zkoss.zk.ui.event.EventListener#onEvent(org.zkoss.zk.ui.event
		 * .Event)
		 */
		public void onEvent(Event event) {
			handleAddToBasketEvent(this.widget, event, this.item, this.qtyInput, this.ussid);
		}
	}

	/**
	 * Handle add to basket event1.
	 *
	 * @param widget
	 *            the widget
	 * @param event
	 *            the event
	 * @param item
	 *            the item
	 * @param qtyInput
	 *            the qty input
	 * @param ussid 
	 */
	protected void handleAddToBasketEvent(
			ListboxWidget<SearchResultWidgetModel, SearchCommandController<SC>> widget,
			Event event, TypedObject item, Longbox qtyInput, String ussid) {
		long quantityToAdd = SafeUnbox.toLong(qtyInput.getValue());
			
		if (quantityToAdd <= 0L)
			return;
		Long pincode = ((MarketplaceSearchResultWidgetModel) widget
				.getWidgetModel()).getPinCode();
		// get the pin code, call the serviceability check from OMS, if the
		// serviceability is there, then add the product to cart, otherwise,
		// flag an error
		// pin code serviceability integration is pending. Refer to TIS-262 for
		// details
		
		if(configurationService.getConfiguration().getBoolean(MarketplaceCockpitsConstants.COCKPIT_SERVICEABILITY_CHECK_BYPASS, false)) {
			super.handleAddToBasketEvent(widget, event, item, qtyInput);
		} else{
			
		if (null == pincode  ) {
			popupMessage(widget, MarketplaceCockpitsConstants.PIN_CODE_EMPTY);
		} else {
			ProductModel product = (ProductModel) item.getObject();
			final String isDeliveryDateRequired = LabelUtils.getLabel(widget,
					IS_DELIVERY_DATE_REQUIRED, new Object[0]);
			processPinCodeServiceability(widget, item, quantityToAdd, pincode, product,
					isDeliveryDateRequired, ussid);
		}
		Map data = Collections.singletonMap("refresh", Boolean.TRUE);
		widget.getWidgetController().dispatchEvent(null, null, data);
	}
	}

	/**
	 * Process pin code serviceability.
	 *
	 * @param widget the widget
	 * @param item the item
	 * @param quantityToAdd the quantity to add
	 * @param pincode the pincode
	 * @param product the product
	 * @param isDeliveryDateRequired the is delivery date required
	 * @param ussid 
	 */
	private void processPinCodeServiceability(
			ListboxWidget<SearchResultWidgetModel, SearchCommandController<SC>> widget,
			TypedObject item, long quantityToAdd, Long pincode,
			ProductModel product, final String isDeliveryDateRequired, String ussid) {
		try {
			((MarketplaceSearchCommandController) widget
					.getWidgetController()).setCurrentSite();

			//TISUAT-4526 no sship in cod
			if(!mplFindDeliveryCostStrategy.isTShip(ussid)){
				popupMessage(widget,"noSellerCOD");
				return;
			}
				
			List<PinCodeResponseData> pinCodeResponses = ((MarketplaceSearchCommandController) widget
					.getWidgetController()).getResponseForPinCode(product,
					String.valueOf(pincode), isDeliveryDateRequired, ussid);
			if(CollectionUtils.isNotEmpty(pinCodeResponses)) {
				LOG.info("pinCodeResponse:" + pinCodeResponses.size());

				boolean isAddtoCartRequired = checkServiceAbility(widget, item, quantityToAdd, pincode, product,
						pinCodeResponses);
				if(isAddtoCartRequired){
					((MarketplaceSearchCommandController) widget
							.getWidgetController()).setCurrentSite();
					getItemAppender().add(item, quantityToAdd);
				} else {
					LOG.info("Serviceability check failed for product:"+product.getCode());
				}
			} else {
				popupMessage(widget,NO_RESPONSE_FROM_SERVER);
			}
		}catch(EtailNonBusinessExceptions ex) {
			LOG.error("EtailNonBusinessException while fetching pinCodeResponses:",ex);
			popupMessage(widget,NO_RESPONSE_FROM_SERVER+":"+ex);
		}catch(ClientEtailNonBusinessExceptions e) {
			LOG.error("ClientEtailNonBusinessExceptions while fetching pinCodeResponses:",e);
			popupMessage(widget,NO_RESPONSE_FROM_SERVER+":"+e);
		}
	}

	/**
	 * Check service ability.
	 *
	 * @param widget the widget
	 * @param item the item
	 * @param quantityToAdd the quantity to add
	 * @param pincode the pincode
	 * @param product the product
	 * @param pinCodeResponses the pin code responses
	 * @return true, if successful
	 */
	private boolean checkServiceAbility(
			ListboxWidget<SearchResultWidgetModel, SearchCommandController<SC>> widget,
			TypedObject item, long quantityToAdd, Long pincode,
			ProductModel product, List<PinCodeResponseData> pinCodeResponses) {
		
		boolean isAddtoCartRequired = false;
		for (PinCodeResponseData pinCodeResponse : pinCodeResponses) {
			if (null!= pinCodeResponse && null != pinCodeResponse.getStockCount()
					&& null != pinCodeResponse.getIsServicable()) {
				final Long actualInventory = pinCodeResponse
						.getStockCount() - quantityToAdd;
				if (StringUtils.equals(pinCodeResponse.getIsServicable(),
						YES) && actualInventory >= 0) {
					LOG.info("Adding product with code:"
							+ product.getCode());
					LOG.info("Quantity added:" + quantityToAdd);
					isAddtoCartRequired=true;
				} else if (!StringUtils.equals(
						pinCodeResponse.getIsServicable(), YES)) {
					LOG.info("Not serviceable at " + pincode);
					popupMessage(widget, NOT_SERVICE_ABLE);
				} else if (pinCodeResponse.getStockCount() > 0) {
					LOG.info("There is no sufficient inventory available for this product. Total Inventory available:"
							+ pinCodeResponse.getStockCount());
					popupMessage(widget, INVENTORY_INSUFFICIENT);
				}
			} else {
				popupMessage(widget, NO_RESPONSE_FROM_SERVER);
			}
			break;
		}
		return isAddtoCartRequired;
	}

	/**
	 * Popup message.
	 *
	 * @param widget
	 *            the widget
	 * @param message
	 *            the message
	 */
	private void popupMessage(
			ListboxWidget<SearchResultWidgetModel, SearchCommandController<SC>> widget,
			final String message) {
		try {
			Messagebox.show(
					LabelUtils.getLabel(widget, message, new Object[0]),
					INFO, Messagebox.OK, Messagebox.ERROR);
		} catch (InterruptedException e) {
			LOG.error(e);
		}
	}
	
		@Override
	   protected PriceValue getProductPriceValue(DataObject<TypedObject> metaItem)
	   {
			MplMetaProductPriceValue metaProductPriceValue = (MplMetaProductPriceValue)metaItem.getMeta(MplMetaProductPriceValue.class);
	     if (metaProductPriceValue != null)
	     {
	       return metaProductPriceValue.getProductPriceValue();
	     }
	     return null;
	   }
}
