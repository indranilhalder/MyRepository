package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Span;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketplaceSearchCommandController;
import com.tisl.mpl.cockpits.cscockpit.widgets.helpers.MarketplaceServiceabilityCheckHelper;
import com.tisl.mpl.cockpits.cscockpit.widgets.models.impl.MarketplaceSearchResultWidgetModel;
import com.tisl.mpl.core.enums.ClickAndCollectEnum;
import com.tisl.mpl.core.enums.ExpressDeliveryEnum;
import com.tisl.mpl.core.enums.HomeDeliveryEnum;
import com.tisl.mpl.core.enums.PaymentModesEnum;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.facades.product.data.ProductDetailPopupDTO;

import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.config.ColumnGroupConfiguration;
import de.hybris.platform.cockpit.services.config.impl.DefaultColumnGroupConfiguration;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.commercefacades.product.data.DeliveryDetailsData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cscockpit.utils.CockpitUiConfigLoader;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.ObjectGetValueUtils;
import de.hybris.platform.cscockpit.widgets.popup.PopupWindowCreator;
import de.hybris.platform.cscockpit.widgets.renderers.details.impl.ProductSearchProductDetailRenderer;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.WeakArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class MarketPlaceProductSearchProductDetailRenderer.
 */
public class MarketPlaceProductSearchProductDetailRenderer extends
		ProductSearchProductDetailRenderer {

	/** The Constant NOT_SPECIFIED. */
	private static final String NOT_SPECIFIED = "notSpecified";

	/** The Constant LABELSTYLE. */
	private static final String LABELSTYLE = "display:block";

	/** The Constant LOG. */
	private static final Logger LOG = Logger
			.getLogger(MarketPlaceProductSearchProductDetailRenderer.class);

	/** The Constant PRODUCT_DETAILS. */
	private static final String PRODUCT_DETAILS = "productDetails";

	/** The Constant CS_PRODUCT_DETAIL_POPUP. */
	private static final String CS_PRODUCT_DETAIL_POPUP = "productDetailPopup";

	private static final String SELLER_DETAILS = "sellerDetails";

	private static final String CS_SELLER_DETAIL_POPUP = "sellerDetailPopup";

	@Autowired
	private MarketplaceSearchCommandController marketplaceSearchCommandController;

	/** The popup window creator. */
	@Autowired
	private PopupWindowCreator popupWindowCreator;

	@Autowired
	private MarketplaceServiceabilityCheckHelper marketplaceServiceabilityCheckHelper;

	@Autowired
	private ConfigurationService configurationService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.cscockpit.widgets.renderers.details.impl.
	 * ProductSearchProductDetailRenderer#createContent(java.lang.Object,
	 * de.hybris.platform.cockpit.model.meta.TypedObject,
	 * de.hybris.platform.cockpit.widgets.Widget)
	 */
	@Override
	public HtmlBasedComponent createContent(Object context, TypedObject item,
			Widget widget) {
		Div container = new Div();

		ColumnGroupConfiguration detailConfiguration = CockpitUiConfigLoader
				.getColumnGroupConfiguration(
						UISessionUtils.getCurrentSession(),
						getConfigurationCode(), item.getType().getCode());

		if (detailConfiguration != null)

		{
			Div groupContainer = new Div();
			groupContainer.setParent(container);

			List<ColumnConfiguration> columns = CockpitUiConfigLoader
					.getDirectVisibleColumnConfigurations(detailConfiguration);
			for (ColumnConfiguration col : columns) {
				CellRenderer renderer = col.getCellRenderer();
				if (renderer != null) {
					renderer.render(
							createSingleValueTableModel(ObjectGetValueUtils
									.getObjectValue(col.getValueHandler(), item)),
							0, 0, groupContainer);
				} else {
					Label dataLabel = new Label(ObjectGetValueUtils.getValue(
							col.getValueHandler(), item));
					dataLabel.setParent(groupContainer);
				}

			}

			for (ColumnGroupConfiguration groupCfg : detailConfiguration
					.getColumnGroupConfigurations()) {
				Div groupContainer1 = new Div();
				groupContainer1.setParent(container);

				String label = (groupCfg instanceof DefaultColumnGroupConfiguration) ? ((DefaultColumnGroupConfiguration) groupCfg)
						.getLabelWithFallback() : groupCfg.getLabel();

				Label groupLabel = new Label(label + ": ");
				groupLabel.setSclass("csGroupLabel");
				groupLabel.setParent(groupContainer);

				List<ColumnConfiguration> columnList = CockpitUiConfigLoader
						.getAllVisibleColumnConfigurations(groupCfg);
				for (ColumnConfiguration col : columnList) {
					CellRenderer renderer = col.getCellRenderer();
					if (renderer != null) {
						renderer.render(
								createSingleValueTableModel(ObjectGetValueUtils
										.getObjectValue(col.getValueHandler(),
												item)), 0, 0, groupContainer);
					} else {
						Label dataLabel = new Label(
								ObjectGetValueUtils.getValue(
										col.getValueHandler(), item));
						dataLabel.setParent(groupContainer);
					}
				}
			}
		}

		if ((getUrlStrategy() != null)
				&& (getUrlStrategy().getUrl(item) != null)) {
			Div div = new Div();
			div.setParent(container);

			Span span = new Span();
			span.setParent(div);
			span.setSclass("csProductLink");

			Toolbarbutton websiteLink = new Toolbarbutton(LabelUtils.getLabel(
					"cscockpit.widget.basket", "viewProductBtn", new Object[0]));
			websiteLink.setParent(span);
			websiteLink.setSclass("blueLink");
			websiteLink.addEventListener("onClick",
					createShowProductEventListener(item));
		}

		/* CHANGES FOR TIS-261: View Product Link START */

		createViewProductDetailsLink(widget, container, item);

		createSellerSpecificPriceLink(widget, container, item);

		/* CHANGES FOR TIS-261: View Product Link END */

		return container;
	}

	/**
	 * Creates the view product details link.
	 *
	 * @param widget
	 *            the widget
	 * @param container
	 *            the container
	 * @param item
	 *            the item
	 */
	private void createViewProductDetailsLink(final Widget widget,
			final Div container, final TypedObject item) {

		Div div = new Div();
		div.setParent(container);
		Span span = new Span();
		span.setParent(div);
		span.setSclass("csProductLink");

		Toolbarbutton viewMoreProductDetails = new Toolbarbutton(
				LabelUtils.getLabel("marketplacecockpit.widget.basket.result",
						"viewProductDetailsBtn", new Object[0]));
		viewMoreProductDetails.setParent(span);
		viewMoreProductDetails.setSclass("blueLink");

		viewMoreProductDetails.addEventListener("onClick",
				createViewProductDetailsListener(widget, item, div));
	}

	/**
	 * Creates the seller specific price link.
	 *
	 * @param widget
	 *            the widget
	 * @param container
	 *            the container
	 * @param item
	 *            the item
	 */
	private void createSellerSpecificPriceLink(final Widget widget,
			final Div container, final TypedObject item) {

		Div div = new Div();
		div.setParent(container);
		Span span = new Span();
		span.setParent(div);
		span.setSclass("csProductLink");

		Toolbarbutton viewSellerDetails = new Toolbarbutton(
				LabelUtils.getLabel(widget, "viewSellerDetailsBtn",
						new Object[0]));
		viewSellerDetails.setParent(span);
		viewSellerDetails.setSclass("blueLink");

		viewSellerDetails.addEventListener("onClick",
				createSellerPriceDetailsListener(widget, item, div));
	}

	/**
	 * Creates the view product details listener.
	 *
	 * @param widget
	 *            the widget
	 * @param item
	 *            the item
	 * @param div
	 *            the div
	 * @return the event listener
	 */
	protected EventListener createSellerPriceDetailsListener(Widget widget,
			TypedObject item, Div div) {
		return new OpenSellerDetailWindowEventListener(widget, item, div);
	}

	/**
	 * Creates the view product details listener.
	 *
	 * @param widget
	 *            the widget
	 * @param item
	 *            the item
	 * @param div
	 *            the div
	 * @return the event listener
	 */
	protected EventListener createViewProductDetailsListener(Widget widget,
			TypedObject item, Div div) {
		return new OpenProductDetailWindowEventListener(widget, item, div);
	}

	/**
	 * The listener interface for receiving openProductDetailWindowEvent events.
	 * The class that is interested in processing a openProductDetailWindowEvent
	 * event implements this interface, and the object created with that class
	 * is registered with a component using the component's
	 * <code>addOpenProductDetailWindowEventListener<code> method. When
	 * the openProductDetailWindowEvent event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OpenProductDetailWindowEventEvent
	 */
	protected class OpenProductDetailWindowEventListener implements
			EventListener {

		/** The widget. */
		private final Widget widget;

		/** The item. */
		private final TypedObject item;

		/** The div. */
		private final Div div;

		/**
		 * Instantiates a new open product detail window event listener.
		 *
		 * @param widget
		 *            the widget
		 * @param item
		 *            the item
		 * @param div
		 *            the div
		 */
		public OpenProductDetailWindowEventListener(Widget widget,
				TypedObject item, Div div) {
			this.widget = widget;
			this.item = item;
			this.div = div;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.zkoss.zk.ui.event.EventListener#onEvent(org.zkoss.zk.ui.event
		 * .Event)
		 */
		public void onEvent(Event event) throws Exception {
			handleOpenProductDetailWindowEvent(this.widget, event, item, div);
		}
	}

	/**
	 * The listener interface for receiving openSellerDetailWindowEvent events.
	 * The class that is interested in processing a openSellerDetailWindowEvent
	 * event implements this interface, and the object created with that class
	 * is registered with a component using the component's
	 * <code>addOpenSellerDetailWindowEventListener<code> method. When
	 * the openSellerDetailWindowEvent event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OpenSellerDetailWindowEventEvent
	 */
	protected class OpenSellerDetailWindowEventListener implements
			EventListener {

		/** The widget. */
		private final Widget widget;

		/** The item. */
		private final TypedObject item;

		/** The div. */
		private final Div div;

		/**
		 * Instantiates a new open product detail window event listener.
		 *
		 * @param widget
		 *            the widget
		 * @param item
		 *            the item
		 * @param div
		 *            the div
		 */
		public OpenSellerDetailWindowEventListener(Widget widget,
				TypedObject item, Div div) {
			this.widget = widget;
			this.item = item;
			this.div = div;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.zkoss.zk.ui.event.EventListener#onEvent(org.zkoss.zk.ui.event
		 * .Event)
		 */
		public void onEvent(Event event) throws Exception {
			handleSellerDetailWindowEvent(this.widget, event, item, div);
		}
	}

	/**
	 * Handle open product detail window event.
	 *
	 * @param widget
	 *            the widget
	 * @param event
	 *            the event
	 * @param item
	 *            the item
	 * @param div
	 *            the div
	 */
	private void handleOpenProductDetailWindowEvent(Widget widget, Event event,
			final TypedObject item, final Div div) {
		Div productDetailDiv = new Div();
		productDetailDiv.setParent(div);

		Long pincode = ((MarketplaceSearchResultWidgetModel) widget
				.getWidgetModel()).getPinCode();
		Window popupWindow = popupWindowCreator.createModalPopupWindow(widget,
				LabelUtils.getLabel(widget, PRODUCT_DETAILS, new Object[0]),
				CS_PRODUCT_DETAIL_POPUP, 700, productDetailDiv);

		if (null == pincode || pincode.longValue() == 0) {
			LOG.info("Pin code value is null, hence the popup would contain non realtime data");
			populateProductDetailsPopup(widget, productDetailDiv, popupWindow,
					item);
		} else {

			ProductModel product = ((ProductModel) (item.getObject()));
			final String isDeliveryDateRequired = LabelUtils.getLabel(widget,
					"isDeliveryDateRequired", new Object[0]);

			processPinCodeServiceability(widget, item, productDetailDiv,
					pincode, popupWindow, product, isDeliveryDateRequired);
		}

	}

	/**
	 * Handle seller detail window event.
	 *
	 * @param widget
	 *            the widget
	 * @param event
	 *            the event
	 * @param item
	 *            the item
	 * @param div
	 *            the div
	 */
	private void handleSellerDetailWindowEvent(final Widget widget,
			final Event event, final TypedObject item, final Div div) {
		final Div productDetailDiv = new Div();
		productDetailDiv.setParent(div);

		Window popupWindow = popupWindowCreator.createModalPopupWindow(widget,
				LabelUtils.getLabel(widget, SELLER_DETAILS, new Object[0]),
				CS_SELLER_DETAIL_POPUP, 700, productDetailDiv);

		LOG.info("Pin code value is null, hence the popup would contain non realtime data");
		populateSellerDetailsPopup(widget, productDetailDiv, popupWindow, item);
	}

	/**
	 * Process pin code serviceability.
	 *
	 * @param widget
	 *            the widget
	 * @param item
	 *            the item
	 * @param productDetailDiv
	 *            the product detail div
	 * @param pincode
	 *            the pincode
	 * @param popupWindow
	 *            the popup window
	 * @param product
	 *            the product
	 * @param isDeliveryDateRequired
	 *            the is delivery date required
	 */
	private void processPinCodeServiceability(Widget widget,
			final TypedObject item, Div productDetailDiv, Long pincode,
			Window popupWindow, ProductModel product,
			final String isDeliveryDateRequired) {
		((MarketplaceSearchCommandController) widget.getWidgetController())
				.setCurrentSite();
		try {

			LOG.info("Bypass proxy status:"
					+ configurationService
							.getConfiguration()
							.getBoolean(
									MarketplaceCockpitsConstants.COCKPIT_SERVICEABILITY_CHECK_BYPASS,
									false));
			if (configurationService
					.getConfiguration()
					.getBoolean(
							MarketplaceCockpitsConstants.COCKPIT_SERVICEABILITY_CHECK_BYPASS,
							false)) {
				populateProductDetailsPopup(widget, productDetailDiv,
						popupWindow, item);
			} else {
				final List<PinCodeResponseData> pinCodeResponse = ((MarketplaceSearchCommandController) widget
						.getWidgetController()).getResponseForPinCode(product,
						String.valueOf(pincode), isDeliveryDateRequired, null);

				if (CollectionUtils.isNotEmpty(pinCodeResponse)) {
					LOG.info("pinCodeResponse:" + pinCodeResponse.size());
					List<ProductDetailPopupDTO> productDetailPopupDTOs = populateProductDetailPopupDTO(pinCodeResponse);
					populateProductDetailsPopup(widget, productDetailDiv,
							popupWindow, item, productDetailPopupDTOs);
					final List<DeliveryDetailsData> deliveryModes = pinCodeResponse
							.get(0).getValidDeliveryModes();
					if (CollectionUtils.isNotEmpty(deliveryModes)) {
						((MarketplaceSearchResultWidgetModel) widget
								.getWidgetModel())
								.setDeliveryModes(deliveryModes);
					} else {
						popupMessage(widget, "nonRealTime");
						LOG.info("There are no delivery modes configured for product:"
								+ product.getCode()
								+ "on OMS server, hence the popup would contain non real time data");
					}
				} else {
					popupMessage(widget, "noResponseForProduct");
					LOG.info("There is no response from the server for product:"
							+ product.getCode()
							+ ", hence the popup would contain non real time data");
				}

			}

		} catch (final EtailNonBusinessExceptions ex) {
			LOG.error(
					"EtailNonBusinessException while fetching pinCodeResponses:",
					ex);
			popupMessage(widget,
					MarketplaceCockpitsConstants.NO_RESPONSE_FROM_SERVER + ":"
							+ ex);
		} catch (final ClientEtailNonBusinessExceptions e) {
			LOG.error(
					"ClientEtailNonBusinessExceptions while fetching pinCodeResponses:",
					e);
			popupMessage(widget,
					MarketplaceCockpitsConstants.NO_RESPONSE_FROM_SERVER + ":"
							+ e);
		}
	}

	/**
	 * Populate product detail popup dto.
	 *
	 * @param pinCodeResponse
	 *            the pin code response
	 * @return the product detail popup dto
	 */
	private List<ProductDetailPopupDTO> populateProductDetailPopupDTO(
			List<PinCodeResponseData> pinCodeResponse) {

		final List<ProductDetailPopupDTO> productDetailDTOs = new WeakArrayList<ProductDetailPopupDTO>();
		for (final PinCodeResponseData pinCodeResponseData : pinCodeResponse) {
			final ProductDetailPopupDTO productDetailDTO = new ProductDetailPopupDTO();
			final List<String> deliveryModes = new ArrayList<String>();
			final List<String> payModes = new ArrayList<String>();



				for (final DeliveryDetailsData deliveryDetail : pinCodeResponseData
						.getValidDeliveryModes()) {
					deliveryModes.add(deliveryDetail.getType());
				}
				
				
			if("N".equalsIgnoreCase(pinCodeResponseData.getIsServicable())){
				productDetailDTO.setPaymentMode(payModes);
			} else{
				
				if("Y".equalsIgnoreCase(pinCodeResponseData.getIsPrepaidEligible())){
					payModes.add("PREPAID");
				}
				if("Y".equalsIgnoreCase(pinCodeResponseData.getCod())){
					payModes.add("COD");
				}
			}
			productDetailDTO.setPaymentMode(payModes);
			productDetailDTO.setUssId(pinCodeResponseData.getUssid());
			productDetailDTO.setValidDeliveryModes(deliveryModes);
			productDetailDTO.setFulfillmentMode(pinCodeResponseData
					.getFulfilmentType());

			productDetailDTOs.add(productDetailDTO);
		}

		return productDetailDTOs;
	}

	/**
	 * Populate product details popup.
	 *
	 * @param widget
	 *            the widget
	 * @param container
	 *            the container
	 * @param popupWindow
	 *            the popup window
	 * @param item
	 *            the item This method would function when the data needs to be
	 *            painted from hybris
	 */
	private void populateProductDetailsPopup(final Widget widget,
			Div container, final Window popupWindow, final TypedObject item) {
		container.setSclass("csProductDetailsWindow");

		Listbox listbox = populateHeaderRow(widget, container);
		populateListData(widget, container, item, listbox);
	}

	/**
	 * Populate product details popup.
	 *
	 * @param widget
	 *            the widget
	 * @param container
	 *            the container
	 * @param popupWindow
	 *            the popup window
	 * @param item
	 *            the item
	 * @param productDetailPopupDTO
	 *            the product detail popup dto This method would function when
	 *            the pincode response is recieved
	 */
	private void populateProductDetailsPopup(final Widget widget,
			Div container, final Window popupWindow, final TypedObject item,
			final List<ProductDetailPopupDTO> productDetailPopupDTO) {

		container.setSclass("csProductDetailsWindow");

		Listbox listbox = populateHeaderRow(widget, container);
		populateListData(container, listbox, productDetailPopupDTO, item);
	}

	/**
	 * Populate list data. This method functions when the data needs to be
	 * painted from pincode response
	 * 
	 * @param widget
	 *            the widget
	 * @param container
	 *            the container
	 * @param listbox
	 *            the listbox
	 * @param productDetailPopupDTOs
	 *            the product detail popup dt os
	 */
	private void populateListData(final Div container, Listbox listbox,
			List<ProductDetailPopupDTO> productDetailPopupDTOs,
			final TypedObject item) {

		ProductModel productModel = (ProductModel) item.getObject();
		List<SellerInformationData> sellers = marketplaceServiceabilityCheckHelper
				.getSellerInformation(productModel);

		Listitem productDetailItem = null;
		;
		Listcell cell = null;
		;

		String sellerName = StringUtils.EMPTY;
		for (final ProductDetailPopupDTO productDetailPopupDTO : productDetailPopupDTOs) {
			for (SellerInformationData sData : sellers) {
				if (productDetailPopupDTO.getUssId().equalsIgnoreCase(
						sData.getUssid())) {
					sellerName = sData.getSellername();
					break;
				}
			}
			productDetailItem = new Listitem();
			productDetailItem.setParent(listbox);

			cell = new Listcell();
			cell.setParent(productDetailItem);
			Label ussidLabel = new Label(productDetailPopupDTO.getUssId());
			ussidLabel.setParent(cell);

			cell = new Listcell();
			cell.setParent(productDetailItem);
			Label seller = new Label(sellerName);
			seller.setParent(cell);

			cell = new Listcell();
			cell.setParent(productDetailItem);
			Label deliverymodeLabel = null;
			for (final String deliveryMode : productDetailPopupDTO
					.getValidDeliveryModes()) {
				deliverymodeLabel = new Label(
						MarketplaceCockpitsConstants.delNameMap
								.get(deliveryMode));
				deliverymodeLabel.setStyle(LABELSTYLE);
				deliverymodeLabel.setParent(cell);
			}

			cell = new Listcell();
			cell.setParent(productDetailItem);
			Label paymentmodeLabel = null;
			for (final String payMode : productDetailPopupDTO
					.getPaymentMode()) {
				paymentmodeLabel = new Label(payMode
						);
				paymentmodeLabel.setStyle(LABELSTYLE);
				paymentmodeLabel.setParent(cell);
			}

			cell = new Listcell();
			cell.setParent(productDetailItem);
			Label fulfilmentLabel = new Label(
					productDetailPopupDTO.getFulfillmentMode());
			fulfilmentLabel.setParent(cell);
			cell.setParent(productDetailItem);
		}
	}

	/**
	 * Populate seller details popup.
	 *
	 * @param widget
	 *            the widget
	 * @param container
	 *            the container
	 * @param popupWindow
	 *            the popup window
	 * @param item
	 *            the item
	 */
	private void populateSellerDetailsPopup(final Widget widget, Div container,
			final Window popupWindow, final TypedObject item) {
		container.setSclass("csProductDetailsWindow");

		boolean showPriceFromBuyBox = true;// ((MarketplaceSearchCommandController)
											// widget.getWidgetController()).getPricingFlag();
		Listbox listbox = populateSellerLinkHeaderRow(widget, container,
				showPriceFromBuyBox);

		if (showPriceFromBuyBox) {
			populateSellerBuyBoxData(widget, container, item, listbox,
					popupWindow);
		} else {
			populateSellerPriceData(widget, container, item, listbox,
					popupWindow);
		}
	}

	/**
	 * Populate seller price data.
	 *
	 * @param widget
	 *            the widget
	 * @param container
	 *            the container
	 * @param item
	 *            the item
	 * @param listbox
	 *            the listbox
	 */
	private void populateSellerPriceData(Widget widget, Div container,
			TypedObject item, Listbox listbox, final Window popupWindow) {
		final ProductModel product = (ProductModel) item.getObject();
		Map<String, PriceRowModel> sellerPriceMap = ((MarketplaceSearchCommandController) widget
				.getWidgetController()).getsellerSpecificPrices(product);
		if (sellerPriceMap.isEmpty()) {
			popupMessage(widget,
					MarketplaceCockpitsConstants.NO_SELLERS_FOR_PRODUCT);
		} else {
			for (Map.Entry<String, PriceRowModel> entry : sellerPriceMap
					.entrySet()) {
				LOG.info("Entry=" + entry.getKey() + ";PriceRow="
						+ entry.getValue());
				populateSellerDetails(widget, container, item, listbox,
						entry.getKey(), entry.getValue(), popupWindow);
			}
		}
	}

	private void populateSellerBuyBoxData(final Widget widget,
			final Div container, final TypedObject item, final Listbox listbox,
			final Window popupWindow) {
		final ProductModel product = (ProductModel) item.getObject();
		List<BuyBoxModel> buyBoxes = ((MarketplaceSearchCommandController) widget
				.getWidgetController()).getsellerSpecificBuyBoxPrices(product);
		if (CollectionUtils.isEmpty(buyBoxes)) {
			popupMessage(widget,
					MarketplaceCockpitsConstants.NO_SELLERS_FOR_PRODUCT);
		} else {
			for (final BuyBoxModel buyBox : buyBoxes) {
				// LOG.info("Entry="+entry.getKey()+";PriceRow="+entry.getValue());
				LOG.info("Product Code:" + buyBox.getProduct()
						+ ";Article SKU:" + buyBox.getSellerArticleSKU()
						+ "Seller Name:" + buyBox.getSellerName() + ";Price:"
						+ buyBox.getPrice() + ";Special Price:"
						+ buyBox.getSpecialPrice() + ";MRP:" + buyBox.getMrp());
				populateSellerDetails(widget, container, item, listbox, buyBox,
						popupWindow);
			}
		}
	}

	/**
	 * Populate list data. This method functions when the data needs to be
	 * painted from hybris
	 * 
	 * @param widget
	 *            the widget
	 * @param container
	 *            the container
	 * @param item
	 *            the item
	 * @param listbox
	 *            the listbox
	 */
	private void populateListData(final Widget widget, final Div container,
			final TypedObject item, final Listbox listbox) {

		ProductModel product = (ProductModel) item.getObject();
		Listitem productDetailItem = null;
		Label label = null;
		Listcell cell = null;
		for (SellerInformationData sData : marketplaceServiceabilityCheckHelper
				.getSellerInformation(product)) {
			productDetailItem = new Listitem();
			productDetailItem.setParent(listbox);
			// sku id
			cell = new Listcell();
			cell.setParent(productDetailItem);
			label = new Label(sData.getUssid());
			label.setParent(cell);
			// seller name
			cell = new Listcell();
			cell.setParent(productDetailItem);
			label = new Label(sData.getSellername());
			label.setParent(cell);

			// delivery mode
			cell = new Listcell();
			cell.setParent(productDetailItem);
			for (MarketplaceDeliveryModeData dData : sData.getDeliveryModes()) {
				label = new Label(dData.getName());
				label.setStyle(LABELSTYLE);
				label.setParent(cell);
			}

			// payment Mode
			cell = new Listcell();
			cell.setParent(productDetailItem);
			if ("Y".equalsIgnoreCase(sData.getIsCod())) {
				label = new Label("COD");
				label.setParent(cell);
				label.setStyle(LABELSTYLE);
				label = new Label("PREPAID");
				label.setStyle(LABELSTYLE);
				label.setParent(cell);
			} else {
				label = new Label("PREPAID");
				label.setStyle(LABELSTYLE);
				label.setParent(cell);
			}

			// shipment Mode
			cell = new Listcell();
			cell.setParent(productDetailItem);
			label = new Label(sData.getFullfillment());
			label.setParent(cell);

		}
		/*
		 * 
		 * 
		 * Listitem productDetailItem = new Listitem();
		 * 
		 * setSellerInfoFromConfiguration(product, productDetailItem);
		 * setDeliveryModesfromConfiguration(widget, product,
		 * productDetailItem); setPaymentModesFromConfiguration(widget, product,
		 * productDetailItem); setShipmentModeFromConfiguration(widget, product,
		 * productDetailItem);
		 */

	}

	/**
	 * Sets the seller info from configuration.
	 *
	 * @param widget
	 *            the widget
	 * @param product
	 *            the product
	 * @param productDetailItem
	 *            the product detail item
	 */
	private void setSellerInfoFromConfiguration(final ProductModel product,
			Listitem productDetailItem) {
		Listcell cell = new Listcell();
		cell.setParent(productDetailItem);

	}

	/**
	 * Populate seller details.
	 *
	 * @param widget
	 *            the widget
	 * @param container
	 *            the container
	 * @param product
	 *            the product
	 * @param listbox
	 *            the listbox
	 * @param sellerId
	 *            the seller id
	 * @param priceRow
	 *            the price row
	 */
	private void populateSellerDetails(final Widget widget,
			final Div container, final TypedObject product,
			final Listbox listbox, final String sellerId,
			final PriceRowModel priceRow, final Window popupWindow) {

		Listitem sellerDetailItem = new Listitem();
		sellerDetailItem.setParent(listbox);

		Listcell sellerCell = new Listcell();
		sellerCell.setParent(sellerDetailItem);
		Label sellerIDLabel = new Label(sellerId);
		sellerIDLabel.setParent(sellerCell);

		Listcell priceCell = new Listcell();
		priceCell.setParent(sellerDetailItem);
		Label priceLabel = new Label(String.valueOf(priceRow.getPrice()));
		priceLabel.setParent(priceCell);

		Listcell qtyCell = new Listcell();
		qtyCell.setParent(sellerDetailItem);
		final Longbox quantityInput = new Longbox(1);
		quantityInput.setWidth("33px");
		quantityInput.setParent(qtyCell);

		Listcell actionCell = new Listcell();
		actionCell.setParent(sellerDetailItem);

		Button actionButton = new Button(LabelUtils.getLabel(widget,
				MarketplaceCockpitsConstants.ADD_TO_BASKET, new Object[0]));
		actionButton.setParent(actionCell);
		actionButton.setSclass("btngreen");

		actionButton.addEventListener(Events.ON_CLICK, new EventListener() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				try {
					if (null == ((MarketplaceSearchResultWidgetModel) widget
							.getWidgetModel()).getPinCode()) {
						popupMessage(widget,
								MarketplaceCockpitsConstants.PIN_CODE_EMPTY);
					} else {
						boolean isCartAdded = ((MarketplaceSearchCommandController) widget
								.getWidgetController()).addToMarketPlaceCart(
								product, quantityInput.getValue(), sellerId);
						if (!isCartAdded) {
							popupMessage(
									widget,
									MarketplaceCockpitsConstants.NO_RESPONSE_FROM_SERVER);
						}
						popupWindow.detach();
						((MarketplaceSearchCommandController) widget
								.getWidgetController()).dispatchEvent();
					}
				} catch (EtailNonBusinessExceptions ex) {
					LOG.error(
							"EtailNonBusinessExceptions in MarketPlaceProductSearchProductDetailRenderer-> populateSellerDetails (pricerow)->addToMarketPlaceCart:",
							ex);
					popupMessage(
							widget,
							MarketplaceCockpitsConstants.NO_RESPONSE_FROM_SERVER);
				}
			}
		});
	}

	/**
	 * Populate seller details.
	 *
	 * @param widget
	 *            the widget
	 * @param container
	 *            the container
	 * @param product
	 *            the product
	 * @param listbox
	 *            the listbox
	 * @param sellerId
	 *            the seller id
	 * @param priceRow
	 *            the price row
	 * @param popupWindow
	 *            the popup window
	 */
	private void populateSellerDetails(final Widget widget,
			final Div container, final TypedObject product,
			final Listbox listbox, final BuyBoxModel buyBox,
			final Window popupWindow) {

		Listitem sellerDetailItem = new Listitem();
		sellerDetailItem.setParent(listbox);

		Listcell sellerCell = new Listcell();
		sellerCell.setParent(sellerDetailItem);
		Label sellerIDLabel = new Label(buyBox.getSellerArticleSKU());
		sellerIDLabel.setParent(sellerCell);

		Listcell sellerNameCell = new Listcell();
		sellerNameCell.setParent(sellerDetailItem);
		Label sellerNameLabel = new Label(buyBox.getSellerName());
		sellerNameLabel.setParent(sellerNameCell);

		// Label priceLabel = null;
		Listcell priceCell = new Listcell();
		priceCell.setParent(sellerDetailItem);
		// priceLabel = new Label(String.valueOf(buyBox.getPrice()));

		/*
		 * Listcell specialPriceCell = new Listcell();
		 * specialPriceCell.setParent(sellerDetailItem); Label specialPriceLabel
		 * = new Label(String.valueOf(buyBox.getSpecialPrice()));
		 * specialPriceLabel.setParent(specialPriceCell);
		 */

		Double discount = null;
		int compareValue = Double.compare(buyBox.getPrice(), buyBox.getMrp());

		// negative if mrp>price

		if (!(null == buyBox.getMrp() || null == buyBox.getPrice() || null == buyBox
				.getSpecialPrice())) {
			String productPrice = ((MarketplaceSearchCommandController) widget
					.getWidgetController()).formatProductPrice(buyBox
					.getSpecialPrice());
			String mrp = ((MarketplaceSearchCommandController) widget
					.getWidgetController()).formatProductPrice(buyBox.getMrp());
			Label priceLabel = new Label(productPrice);
			Label strikeLabel = new Label(String.valueOf("(" + mrp + ")"));
			priceLabel.setParent(priceCell);
			strikeLabel.setSclass("strikethrough");
			strikeLabel.setParent(priceCell);
			discount = ((buyBox.getMrp() - buyBox.getSpecialPrice()) / buyBox
					.getMrp()) * 100;
		} else {
			if (compareValue != 0) {
				final String productPrice = ((MarketplaceSearchCommandController) widget
						.getWidgetController()).formatProductPrice(buyBox
						.getPrice());
				String mrp = ((MarketplaceSearchCommandController) widget
						.getWidgetController()).formatProductPrice(buyBox
						.getMrp());
				Label priceLabel = new Label(productPrice);
				priceLabel.setParent(priceCell);
				Label strikeLabel = new Label(String.valueOf("(" + mrp + ")"));
				strikeLabel.setSclass("strikethrough");
				strikeLabel.setParent(priceCell);
				discount = ((buyBox.getPrice() - buyBox.getMrp()) / buyBox
						.getMrp()) * 100;
			} else if (compareValue == 0) {
				Label priceLabel = new Label(String.valueOf(buyBox.getPrice()));
				priceLabel.setParent(priceCell);
				discount = 0.00;
			}
		}

		Listcell discountCell = new Listcell();
		discountCell.setParent(sellerDetailItem);
		Label discountLabel = new Label(String.valueOf(Math
				.abs(roundOffDiscount(discount))));
		discountLabel.setParent(discountCell);

		Listcell qtyCell = new Listcell();
		qtyCell.setParent(sellerDetailItem);
		final Longbox quantityInput = new Longbox(1);
		quantityInput.setWidth("33px");
		quantityInput.setParent(qtyCell);

		Listcell actionCell = new Listcell();
		actionCell.setParent(sellerDetailItem);

		if (buyBox.getAvailable() == null || buyBox.getAvailable() == 0) {
			actionCell.setValue(LabelUtils.getLabel(
					"cscockpit.widget.basket.result", "outOfStock",
					new Object[0]));
		} else {
			Button actionButton = new Button(LabelUtils.getLabel(widget,
					MarketplaceCockpitsConstants.ADD_TO_BASKET, new Object[0]));
			actionButton.setParent(actionCell);
			actionButton.setSclass("btngreen");

			actionButton.addEventListener(Events.ON_CLICK, new EventListener() {

				@Override
				public void onEvent(Event arg0) throws Exception {
					try {
						if (null == ((MarketplaceSearchResultWidgetModel) widget
								.getWidgetModel()).getPinCode()) {
							popupMessage(widget,
									MarketplaceCockpitsConstants.PIN_CODE_EMPTY);
						} else {
							boolean isCartAdded = ((MarketplaceSearchCommandController) widget
									.getWidgetController())
									.addToMarketPlaceCart(product,
											quantityInput.getValue(),
											buyBox.getSellerArticleSKU());
							if (!isCartAdded) {
								popupMessage(
										widget,
										MarketplaceCockpitsConstants.NO_SELLER_COD);
							}
							popupWindow.detach();
							((MarketplaceSearchCommandController) widget
									.getWidgetController()).dispatchEvent();

							Map data = Collections.singletonMap("refresh",
									Boolean.TRUE);
							marketplaceSearchCommandController.dispatchEvent(
									null, null, data);
						}
					} catch (EtailNonBusinessExceptions ex) {
						LOG.error(
								"EtailNonBusinessExceptions in MarketPlaceProductSearchProductDetailRenderer-> populateSellerDetails->addToMarketPlaceCart:",
								ex);
						popupMessage(
								widget,
								MarketplaceCockpitsConstants.NO_RESPONSE_FROM_SERVER);
					}
				}
			});
		}
	}

	private Double roundOffDiscount(Double discount) {
		DecimalFormat df = new DecimalFormat("0.00");
		String formatDiscount = df.format(discount);
		double roundedOffDiscount = Double.parseDouble(formatDiscount);

		return roundedOffDiscount;
	}

	/**
	 * Sets the shipment mode.
	 *
	 * @param widget
	 *            the widget
	 * @param product
	 *            the product
	 * @param productDetailItem
	 *            the product detail item
	 */
	private void setShipmentModeFromConfiguration(final Widget widget,
			final ProductModel product, final Listitem productDetailItem) {

		Listcell cell = new Listcell();
		cell.setParent(productDetailItem);

		Label shipmentLabel = new Label();
		for (RichAttributeModel richAttribute : product.getRichAttribute()) {
			if (null == richAttribute.getShippingModes()) {
				// Listcell cell = new Listcell("Not Specified");
				/*
				 * shipmentLabel = new Label(LabelUtils.getLabel(widget,
				 * NOT_SPECIFIED, new Object[0]));
				 * 
				 * shipmentLabel.setParent(cell);
				 */
			} else {
				shipmentLabel = new Label(TypeTools.getEnumName(richAttribute
						.getShippingModes()));
				shipmentLabel.setStyle(LABELSTYLE);
				shipmentLabel.setParent(cell);
			}
		}

	}

	/**
	 * Sets the payment modes.
	 *
	 * @param widget
	 *            the widget
	 * @param product
	 *            the product
	 * @param productDetailItem
	 *            the product detail item
	 */
	private void setPaymentModesFromConfiguration(Widget widget,
			ProductModel product, final Listitem productDetailItem) {

		Listcell cell = new Listcell();
		cell.setParent(productDetailItem);

		Label paymentModeLabel = new Label();
		for (RichAttributeModel richAttribute : product.getRichAttribute()) {
			if ((PaymentModesEnum.BOTH).equals(richAttribute.getPaymentModes())) {
				PaymentModesEnum[] paymentModes = PaymentModesEnum.values();
				for (PaymentModesEnum paymentMode : paymentModes) {
					if (!paymentMode.equals(PaymentModesEnum.BOTH)) {
						Label paymentLabel = new Label(
								TypeTools.getEnumName(paymentMode));

						paymentLabel.setStyle(LABELSTYLE);
						paymentLabel.setParent(cell);
					}
				}
			} else {
				if (null == richAttribute.getPaymentModes()) {

				} else {
					paymentModeLabel = new Label(
							TypeTools.getEnumName(richAttribute
									.getPaymentModes()));
					paymentModeLabel.setStyle(LABELSTYLE);
					paymentModeLabel.setParent(cell);

				}
			}
			break;
		}
	}

	/**
	 * Sets the delivery modes.
	 *
	 * @param widget
	 *            the widget
	 * @param product
	 *            the product
	 * @param productDetailItem
	 *            the product detail item
	 */
	private void setDeliveryModesfromConfiguration(final Widget widget,
			final ProductModel product, final Listitem productDetailItem) {

		Listcell cell = new Listcell();
		cell.setParent(productDetailItem);
		List<DeliveryDetailsData> deliveryModes = ((MarketplaceSearchResultWidgetModel) widget
				.getWidgetModel()).getDeliveryModes();

		if (CollectionUtils.isNotEmpty(deliveryModes)) {
			// Case when OMS serviceability is recieved
			for (final DeliveryDetailsData deliveryDetail : deliveryModes) {
				Label expressDeliveryLabel = new Label(LabelUtils.getLabel(
						widget, deliveryDetail.getType().toLowerCase(),
						new Object[0]));
				expressDeliveryLabel.setStyle(LABELSTYLE);
				cell.appendChild(expressDeliveryLabel);
			}
		} else {

			// Case when pop up is processed by Product model
			Map<HybrisEnumValue, String> labelMap = new HashMap<>();

			labelMap.put(ExpressDeliveryEnum.YES, LabelUtils.getLabel(widget,
					"expressDelivery", new Object[0]));
			labelMap.put(ClickAndCollectEnum.YES, LabelUtils.getLabel(widget,
					"clickAndCollect", new Object[0]));
			labelMap.put(HomeDeliveryEnum.YES,
					LabelUtils.getLabel(widget, "homeDelivery", new Object[0]));
			for (RichAttributeModel richAttribute : product.getRichAttribute()) {
				LOG.info("product.getExpressDelivery():"
						+ richAttribute.getExpressDelivery());
				LOG.info("product.getClickAndCollect():"
						+ richAttribute.getClickAndCollect());
				LOG.info("product.getHomeDelivery():"
						+ richAttribute.getHomeDelivery());

				Label expressDeliveryLabel = new Label(
						labelMap.get(richAttribute.getExpressDelivery()));
				expressDeliveryLabel.setStyle(LABELSTYLE);
				cell.appendChild(expressDeliveryLabel);

				expressDeliveryLabel = new Label(labelMap.get(richAttribute
						.getClickAndCollect()));
				expressDeliveryLabel.setStyle(LABELSTYLE);
				cell.appendChild(expressDeliveryLabel);

				expressDeliveryLabel = new Label(labelMap.get(richAttribute
						.getHomeDelivery()));
				cell.appendChild(expressDeliveryLabel);
				break;
			}
		}

	}

	/**
	 * Populate header row.
	 *
	 * @param widget
	 *            the widget
	 * @param container
	 *            the container
	 * @return the listbox
	 */
	/*
	 * protected Listbox populateHeaderRow(Widget widget, Div container) {
	 * 
	 * Listbox listBox = new Listbox(); container.appendChild(listBox);
	 * 
	 * Listhead row = new Listhead(); listBox.appendChild(row); Listheader
	 * listheader = new Listheader(LabelUtils.getLabel(widget,
	 * "deliveryModeAvailable", new Object[0])); listheader.setWidth("80px");
	 * listheader.setSclass("listPriceColHeader"); row.appendChild(listheader);
	 * 
	 * listheader = new Listheader(LabelUtils.getLabel(widget,
	 * "paymentModeAvailable", new Object[0])); listheader.setWidth("45px");
	 * listheader.setSclass("listQtyColHeader"); row.appendChild(listheader);
	 * 
	 * listheader = new Listheader(LabelUtils.getLabel(widget, "shipmentMode",
	 * new Object[0])); listheader.setWidth("110px");
	 * listheader.setSclass("listActionColHeader"); row.appendChild(listheader);
	 * 
	 * return listBox; }
	 */

	private Listbox populateHeaderRow(final Widget widget, final Div container) {

		Listbox listBox = new Listbox();
		container.appendChild(listBox);

		Listhead row = new Listhead();
		listBox.appendChild(row);

		Listheader listheader = new Listheader(LabelUtils.getLabel(widget,
				"ussId", new Object[0]));
		listheader.setWidth("110px");
		listheader.setSclass("listPriceColHeader");
		row.appendChild(listheader);

		listheader = new Listheader(LabelUtils.getLabel(widget, "sellerName",
				new Object[0]));
		listheader.setWidth("110px");
		listheader.setSclass("listPriceColHeader");
		row.appendChild(listheader);

		listheader = new Listheader(LabelUtils.getLabel(widget, "deliveryMode",
				new Object[0]));
		listheader.setWidth("130px");
		listheader.setSclass("listPriceColHeader");
		row.appendChild(listheader);

		listheader = new Listheader(LabelUtils.getLabel(widget, "paymentMode",
				new Object[0]));
		listheader.setWidth("130px");
		listheader.setSclass("listQtyColHeader");
		row.appendChild(listheader);

		listheader = new Listheader(LabelUtils.getLabel(widget, "shipmentMode",
				new Object[0]));
		listheader.setWidth("110px");
		listheader.setSclass("listActionColHeader");
		row.appendChild(listheader);

		return listBox;

	}

	/**
	 * Populate seller link header row.
	 *
	 * @param widget
	 *            the widget
	 * @param container
	 *            the container
	 * @param showPriceFromBuyBox
	 *            the show price from buy box
	 * @return the listbox
	 */
	private Listbox populateSellerLinkHeaderRow(Widget widget, Div container,
			final boolean showPriceFromBuyBox) {

		Listbox listBox = new Listbox();
		container.appendChild(listBox);

		Listhead row = new Listhead();
		listBox.appendChild(row);

		Listheader listheader = new Listheader(LabelUtils.getLabel(widget,
				"sellerId", new Object[0]));
		listheader.setWidth("20px");
		listheader.setSclass("listPriceColHeader");
		row.appendChild(listheader);

		listheader = new Listheader(LabelUtils.getLabel(widget, "sellerName",
				new Object[0]));
		listheader.setWidth("20px");
		listheader.setSclass("listPriceColHeader");
		row.appendChild(listheader);

		if (showPriceFromBuyBox) {
			/*
			 * listheader = new Listheader(LabelUtils.getLabel(widget,
			 * "specialPrice", new Object[0])); listheader.setWidth("20px");
			 * listheader.setSclass("listPriceColHeader");
			 * row.appendChild(listheader);
			 */
			listheader = new Listheader(LabelUtils.getLabel(widget, "price",
					new Object[0]));
			listheader.setWidth("20px");
			listheader.setSclass("listPriceColHeader");
			row.appendChild(listheader);

			/*
			 * listheader = new Listheader(LabelUtils.getLabel(widget, "mrp",
			 * new Object[0])); listheader.setWidth("45px");
			 * listheader.setSclass("listPriceColHeader");
			 * row.appendChild(listheader);
			 */

			listheader = new Listheader(LabelUtils.getLabel(widget, "discount",
					new Object[0]));
			listheader.setWidth("65px");
			listheader.setSclass("listPriceColHeader");
			row.appendChild(listheader);
		}

		listheader = new Listheader(LabelUtils.getLabel(widget, "quantity",
				new Object[0]));
		listheader.setWidth("20px");
		listheader.setSclass("listQtyColHeader");
		row.appendChild(listheader);

		listheader = new Listheader(LabelUtils.getLabel(widget, "action",
				new Object[0]));
		listheader.setWidth("45px");
		listheader.setSclass("listQtyColHeader");
		row.appendChild(listheader);

		return listBox;
	}

	/**
	 * Popup message.
	 *
	 * @param widget
	 *            the widget
	 * @param message
	 *            the message
	 */
	private void popupMessage(Widget widget, final String message) {
		try {
			Messagebox.show(
					LabelUtils.getLabel(widget, message, new Object[0]),
					"Info", Messagebox.OK, Messagebox.ERROR);
		} catch (InterruptedException e) {
			LOG.error(e);
		}
	}

}
