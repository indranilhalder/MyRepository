package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import com.tisl.mpl.cockpits.cscockpit.strategies.MplFindDeliveryFulfillModeStrategy;
import com.tisl.mpl.core.enums.DeliveryFulfillModesEnum;
import com.tisl.mpl.core.model.RichAttributeModel;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnGroupConfiguration;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.ObjectGetValueUtils;
import de.hybris.platform.cscockpit.widgets.models.impl.OrderItemWidgetModel;
import de.hybris.platform.cscockpit.widgets.popup.PopupWindowCreator;
import de.hybris.platform.cscockpit.widgets.renderers.impl.OrderDetailsOrderItemsWidgetRenderer;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

public class MarketPlaceOrderDetailsOrderItemsWidgetRenderer extends
		OrderDetailsOrderItemsWidgetRenderer {

	private static final Logger LOG = Logger
			.getLogger(MarketPlaceOrderDetailsOrderItemsWidgetRenderer.class);

	protected static final String CSS_ORDER_DETAILS_LABEL = "csOrderDetailsLabel";
	protected static final String CSS_ORDER_DETAILS_CONSIGNMENTS = "csOrderDetailsConsignments";
	private static final String TOTAL_PRICE_DETAILS = "csTotals";
	private static final String CS_TOTAL_PRICE_DETAIL_POPUP = "csTotals";

	protected static final String CSS_CART_FOOTER = "csCartFooter";
	protected static final String CSS_TOOLBAR = "csToolbar";
	protected static final String CSS_MASTER_CONTENT_CELL = "csMasterContentCell";

	private FormatFactory formatFactory;
	private CommonI18NService commonI18NService;
	private SessionService sessionService;

	@Autowired
	private MplFindDeliveryFulfillModeStrategy mplFindDeliveryFulfillModeStrategy;
	
	protected FormatFactory getFormatFactory() {
		return this.formatFactory;
	}

	@Required
	public void setFormatFactory(FormatFactory formatFactory) {
		this.formatFactory = formatFactory;
	}

	protected CommonI18NService getCommonI18NService() {
		return this.commonI18NService;
	}

	@Required
	public void setCommonI18NService(CommonI18NService commonI18NService) {
		this.commonI18NService = commonI18NService;
	}

	protected SessionService getSessionService() {
		return this.sessionService;
	}

	@Required
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	@Autowired
	private PopupWindowCreator popupWindowCreator;

	protected void populateMasterRow(ListboxWidget widget, Listitem row,
			Object context, TypedObject item) {

		PropertyDescriptor entryNumberPD = getCockpitTypeService()
				.getPropertyDescriptor("AbstractOrderEntry.entryNumber");
		PropertyDescriptor basePricePD = getCockpitTypeService()
				.getPropertyDescriptor("AbstractOrderEntry.basePrice");
		PropertyDescriptor totalPricePD = getCockpitTypeService()
				.getPropertyDescriptor("AbstractOrderEntry.totalPrice");
		PropertyDescriptor qtyPD = getCockpitTypeService()
				.getPropertyDescriptor("AbstractOrderEntry.quantity");

		ObjectValueContainer valueContainer = getValueContainer(
				item,
				Arrays.asList(new PropertyDescriptor[] { entryNumberPD,
						basePricePD, totalPricePD, qtyPD }));

		Integer entryNumber = ObjectGetValueUtils.getIntegerValue(
				valueContainer, entryNumberPD);
		String entryNumberString = (entryNumber != null) ? entryNumber
				.toString() : "";
		row.appendChild(new Listcell(entryNumberString));

		List<ColumnGroupConfiguration> columns = (List<ColumnGroupConfiguration>) getMasterColumns();
		if (CollectionUtils.isNotEmpty(columns)) {
			for (ColumnGroupConfiguration col : columns) {
				Listcell cell = new Listcell();
				cell.setSclass("csMasterContentCell");
				row.appendChild(cell);
				getPropertyRendererHelper().buildMasterValue(item, col, cell);
			}
		}

		final CurrencyModel cartCurrencyModel = (CurrencyModel) ((OrderItemWidgetModel) widget
				.getWidgetModel()).getOrderCurrency().getObject();

		NumberFormat currencyInstance = (NumberFormat) getSessionService()
				.executeInLocalView(new SessionExecutionBody() {
					public Object execute() {
						MarketPlaceOrderDetailsOrderItemsWidgetRenderer.this
								.getCommonI18NService().setCurrentCurrency(
										cartCurrencyModel);
						return MarketPlaceOrderDetailsOrderItemsWidgetRenderer.this
								.getFormatFactory().createCurrencyFormat();
					}
				});

		Double basePriceValue = ObjectGetValueUtils.getDoubleValue(
				valueContainer, basePricePD);
		String basePriceString = (basePriceValue != null) ? currencyInstance
				.format(basePriceValue) : "";
		row.appendChild(new Listcell(basePriceString));

		Double totalPriceValue = ObjectGetValueUtils.getDoubleValue(
				valueContainer, totalPricePD);
		String totalPriceString = (totalPriceValue != null) ? currencyInstance
				.format(totalPriceValue) : "";

		Div tempDiv = new Div();
		Listcell rowLabel = new Listcell();
		createTotalPriceLink(widget, tempDiv, item, totalPriceString);
		rowLabel.appendChild(tempDiv);
		row.appendChild(rowLabel);

		Long qty = ObjectGetValueUtils.getLongValue(valueContainer, qtyPD);
		String qtyString = (qty != null) ? qty.toString() : "";
		row.appendChild(new Listcell(qtyString));
	}

	private void createTotalPriceLink(final Widget widget, final Div container,
			final TypedObject item, String totalPriceString) {
		Toolbarbutton viewTotalPriceDetails = new Toolbarbutton(
				totalPriceString);
		viewTotalPriceDetails.setParent(container);
		viewTotalPriceDetails.setSclass("blueLink");
		viewTotalPriceDetails.addEventListener("onClick",
				createTotalPriceListener(widget, item, container));
	}

	protected EventListener createTotalPriceListener(Widget widget,
			TypedObject item, Div div) {
		return new OpenTotalPriceWindowEventListener(widget, item, div);
	}

	protected class OpenTotalPriceWindowEventListener implements EventListener {
		private final Widget widget;
		private final TypedObject item;
		private final Div div;

		public OpenTotalPriceWindowEventListener(Widget widget,
				TypedObject item, Div div) {
			this.widget = widget;
			this.item = item;
			this.div = div;
		}

		public void onEvent(Event event) throws Exception {
			handleTotalPriceWindowEvent(this.widget, event, item, div);
		}
	}

	private void handleTotalPriceWindowEvent(final Widget widget,
			final Event event, final TypedObject item, final Div div) {
		final Div productDetailDiv = new Div();
		productDetailDiv.setParent(div);
		Window popupWindow = popupWindowCreator
				.createModalPopupWindow(widget, LabelUtils.getLabel(widget,
						TOTAL_PRICE_DETAILS, new Object[0]),
						CS_TOTAL_PRICE_DETAIL_POPUP, 400, productDetailDiv);
		populateTotalPricePopup(widget, productDetailDiv, popupWindow, item);
	}

	protected void populateTotalPricePopup(final Widget widget, Div container,
			final Window popupWindow, final TypedObject item) {
		container.setSclass("csProductDetailsWindow");
		Div div = populateTotalPrice(widget, container, item);
		container.appendChild(div);
	}

	protected Div populateTotalPrice(Widget widget, Div container,
			final TypedObject item) {
		final CurrencyModel cartCurrencyModel = (CurrencyModel) ((OrderItemWidgetModel) widget
				.getWidgetModel()).getOrderCurrency().getObject();
		NumberFormat currencyInstance = (NumberFormat) getSessionService()
				.executeInLocalView(new SessionExecutionBody() {
					public Object execute() {
						MarketPlaceOrderDetailsOrderItemsWidgetRenderer.this
								.getCommonI18NService().setCurrentCurrency(
										cartCurrencyModel);
						return MarketPlaceOrderDetailsOrderItemsWidgetRenderer.this
								.getFormatFactory().createCurrencyFormat();
					}
				});

		final AbstractOrderEntryModel orderEntry = (AbstractOrderEntryModel) item
				.getObject();
		Div div = new Div();
		Vbox table = new Vbox();
		table.setSclass("associatedTable");

		Hbox dataRow1 = new Hbox();
		dataRow1.setSclass("dataRowNew");
		final Label basePricelabel1 = new Label(LabelUtils.getLabel(widget,
				"basePrice", new Object[0]));
		basePricelabel1.setSclass("asslabel");
		dataRow1.appendChild(basePricelabel1);
		String basePriceString = (null != orderEntry.getBasePrice()) ? currencyInstance
				.format(orderEntry.getBasePrice()) : "";
		final Label basePricelabel2 = new Label(basePriceString);
		basePricelabel2.setSclass("asslabel");
		dataRow1.appendChild(basePricelabel2);
		table.appendChild(dataRow1);

		Hbox dataRow2 = new Hbox();
		dataRow2.setSclass("dataRowNew");
		final Label promotionslabel1 = new Label(LabelUtils.getLabel(widget,
				"promotions", new Object[0]));
		promotionslabel1.setSclass("asslabel");
		dataRow2.appendChild(promotionslabel1);
		
		
		String promotionString = (null != orderEntry.getTotalProductLevelDisc()) ? currencyInstance
				.format(orderEntry.getTotalProductLevelDisc()) : "";
		final Label promotionslabel2 = new Label(promotionString);
		promotionslabel2.setSclass("asslabel");
		dataRow2.appendChild(promotionslabel2);
		table.appendChild(dataRow2);

		
		Hbox dataRow3 = new Hbox();
		dataRow3.setSclass("dataRowNew");
		final Label deliveryChargeslabel1 = new Label(LabelUtils.getLabel(
				widget, "deliveryCharges", new Object[0]));
		deliveryChargeslabel1.setSclass("asslabel");
		dataRow3.appendChild(deliveryChargeslabel1);
		
		//String deliveryChargesString = (null != orderEntry.getCurrDelCharge()) ? currencyInstance			.format(orderEntry.getCurrDelCharge() + (orderEntry.getRefundedDeliveryChargeAmt() !=null ? orderEntry.getRefundedDeliveryChargeAmt():0d)) : "";
		//TISEE-5583
		
		//Double deliveryCost = (null != orderEntry.getCurrDelCharge() && null != orderEntry.getPrevDelCharge() )? ((orderEntry.getPrevDelCharge() > orderEntry.getCurrDelCharge()) ? orderEntry.getPrevDelCharge() : orderEntry.getCurrDelCharge()) : 0d;
		//String deliveryChargesString =  currencyInstance.format(deliveryCost + (orderEntry.getRefundedDeliveryChargeAmt() !=null ? orderEntry.getRefundedDeliveryChargeAmt():0d)) ;
		
		Double currDeliveryCost = (null != orderEntry.getCurrDelCharge()) ? ((orderEntry.getRefundedDeliveryChargeAmt()!=null && orderEntry.getRefundedDeliveryChargeAmt()> orderEntry.getCurrDelCharge())? orderEntry.getRefundedDeliveryChargeAmt(): orderEntry.getCurrDelCharge() ) : 0d;  
		Double deliveryCost = (null != orderEntry.getPrevDelCharge())? ((orderEntry.getPrevDelCharge() > currDeliveryCost) ? orderEntry.getPrevDelCharge() : currDeliveryCost) : 0d;
		String deliveryChargesString =  currencyInstance
				.format(deliveryCost) ;

		
		final Label deliveryChargeslabel2 = new Label(deliveryChargesString);
		deliveryChargeslabel2.setSclass("asslabel");
		dataRow3.appendChild(deliveryChargeslabel2);
		table.appendChild(dataRow3);

		Hbox dataRow4 = new Hbox();
		dataRow4.setSclass("dataRowNew");
		final Label deliveryDiscountlabel1 = new Label(LabelUtils.getLabel(
				widget, "deliveryDiscount", new Object[0]));
		deliveryDiscountlabel1.setSclass("asslabel");
		dataRow4.appendChild(deliveryDiscountlabel1);
		
		Double deliveryDiscountString = 0D;
		final Double deliveryCostDisc = Double.valueOf(orderEntry.getPrevDelCharge().doubleValue() - orderEntry.getCurrDelCharge().doubleValue());
		deliveryDiscountString = deliveryDiscountString
						+ (mplFindDeliveryFulfillModeStrategy.isTShip(orderEntry.getSelectedUSSID())?0d:deliveryCostDisc);
		final Label deliveryDiscountlabel2 = new Label(currencyInstance.format(deliveryDiscountString > 0 ? deliveryDiscountString : 0d));		
		deliveryDiscountlabel2.setSclass("asslabel");
		dataRow4.appendChild(deliveryDiscountlabel2);
		table.appendChild(dataRow4);

/*		Hbox dataRow5 = new Hbox();
		dataRow5.setSclass("dataRowNew");
		final Label paymentModeDiscountlabel1 = new Label(LabelUtils.getLabel(
				widget, "paymentModeDiscount", new Object[0]));
		paymentModeDiscountlabel1.setSclass("asslabel");
		dataRow5.appendChild(paymentModeDiscountlabel1);
		
		 * String paymentModeDiscountString =
		 * (CollectionUtils.isNotEmpty(orderEntry .getPaymentModeApportion())) ?
		 * currencyInstance .format(orderEntry.getPaymentModeApportion()) : "";
		 final Label paymentModeDiscountlabel2 = new Label();
		paymentModeDiscountlabel2.setSclass("asslabel");
		dataRow5.appendChild(paymentModeDiscountlabel2);
		table.appendChild(dataRow5);*/

		
		
		Hbox dataRow7 = new Hbox();
		dataRow7.setSclass("dataRowNew");
		final Label totalPricelabel1 = new Label(LabelUtils.getLabel(widget,
				"totalPrice", new Object[0]));
		totalPricelabel1.setSclass("asslabel");
		dataRow7.appendChild(totalPricelabel1);
		String totalPriceString = (null != orderEntry.getTotalPrice()) ? currencyInstance
				.format(orderEntry.getTotalPrice()) : currencyInstance
				.format(orderEntry.getTotalPrice());
		final Label totalPricelabel2 = new Label(totalPriceString);
		totalPricelabel2.setSclass("asslabel");
		dataRow7.appendChild(totalPricelabel2);
		table.appendChild(dataRow7);


		Hbox dataRow6 = new Hbox();
		dataRow6.setSclass("dataRowNew");
		final Label codChargeslabel1 = new Label(LabelUtils.getLabel(widget,
				"codCharges", new Object[0]));
		codChargeslabel1.setSclass("asslabel");
		dataRow6.appendChild(codChargeslabel1);
		
		
		String codChargesString = (null != orderEntry
				.getConvenienceChargeApportion()) ? currencyInstance
				.format(orderEntry.getConvenienceChargeApportion()*orderEntry.getQuantity()) : "";
		final Label codChargeslabel2 = new Label(codChargesString);
		codChargeslabel2.setSclass("asslabel");
		dataRow6.appendChild(codChargeslabel2);
		table.appendChild(dataRow6);

		
		
		Hbox dataRow8 = new Hbox();
		dataRow8.setSclass("dataRowNew");
		final Label totalRefundAmountlabel1 = new Label(LabelUtils.getLabel(
				widget, "totalRefundAmount", new Object[0]));
		totalRefundAmountlabel1.setSclass("asslabel");
		dataRow8.appendChild(totalRefundAmountlabel1);

		double totalRefundAmount = Double.valueOf(0);
		if (CollectionUtils.isEmpty(orderEntry.getConsignmentEntries())) {
			if (orderEntry.QUANTITY.equals(0)) {
				totalRefundAmount = orderEntry.getNetAmountAfterAllDisc();
			}
		} else {
			ConsignmentStatus consignmentStatus = orderEntry
					.getConsignmentEntries().iterator().next().getConsignment()
					.getStatus();
			if (consignmentStatus.equals(ConsignmentStatus.ORDER_CANCELLED)
					|| (consignmentStatus
							.equals(ConsignmentStatus.RETURN_COMPLETED))) {
				totalRefundAmount = orderEntry.getNetAmountAfterAllDisc();
			}
		}
		if (null != orderEntry.getRefundedDeliveryChargeAmt()) {
			totalRefundAmount = totalRefundAmount
					+ orderEntry.getRefundedDeliveryChargeAmt();
		}
		String totalRefundAmountString = currencyInstance
				.format(totalRefundAmount);
		final Label totalRefundAmountlabel2 = new Label(totalRefundAmountString);
		totalRefundAmountlabel2.setSclass("asslabel");
		dataRow8.appendChild(totalRefundAmountlabel2);
		table.appendChild(dataRow8);
		div.appendChild(table);
		return div;
	}
	
	private String getDeliveryCharges(AbstractOrderEntryModel orderEntryModel,NumberFormat currencyInstance)
	{
		String deliveryChargesString="";
		Double deliveryCost = (null != orderEntryModel.getCurrDelCharge() && null != orderEntryModel.getPrevDelCharge() )? ((orderEntryModel.getPrevDelCharge() > orderEntryModel.getCurrDelCharge()) ? orderEntryModel.getPrevDelCharge() : orderEntryModel.getCurrDelCharge()) : 0d;
		deliveryChargesString =  currencyInstance
						.format(deliveryCost + (orderEntryModel.getRefundedDeliveryChargeAmt() !=null ? orderEntryModel.getRefundedDeliveryChargeAmt():0d)) ;
		
		return deliveryChargesString;
	}
	
	
	
}