/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

//import de.hybris.platform.core.model.product.ProductModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OrderShortUrlInfoModel;
import com.tisl.mpl.facades.account.address.MplAccountAddressFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.shorturl.service.ShortUrlService;


/**
 * Velocity context for a order notification email.
 */
public class OrderNotificationEmailContext extends AbstractEmailContext<OrderProcessModel>
{
	final List<AbstractOrderEntryModel> childEntries = new ArrayList<AbstractOrderEntryModel>();
	private OrderData orderData;
	private Converter<OrderModel, OrderData> orderConverter;
	private static final String ORDER_CODE = "orderReferenceNumber";
	private static final String CHILDORDERS = "childOrders";
	private static final String CHILDENTRIES = "childEntries";
	private static final String SUBTOTAL = "subTotal";
	private static final String TOTALPRICE = "totalPrice";
	private static final String SHIPPINGCHARGE = "shippingCharge";
	private static final String CONVENIENCECHARGE = "convenienceChargesVal";
	private static final String DELIVERYADDRESS = "deliveryAddress";
	private static final String CNCSTOREADDRESS = "storeAddress";
	private static final String CUSTOMER_NAME = "customerName";
	private static final String COD_CHARGES = "codCharge";
	private static final String SUBTOTALFORJEWELLERY = "subTotalForJewellery";
	private static final String ISPANCARDREQUIRED = "isPancardRequired";



	private static final String MOBILENUMBER = "mobilenumber";
	private static final String NAMEOFPERSON = "nameofperson";
	public static final String TRACK_ORDER_URL = "trackOrderUrl";
	private static final String COMMA = ",";
	private static final String CUSTOMER = "Customer";
	private static final String SPACE = " ";
	private static final String NUMBERTOOL = "numberTool";
	private static final String WEBSITE_URL = "websiteUrl";
	//private static final String PRODUCT_IMAGE_URL = "productImageUrl";//commented as part of TISSPTEN-7
	private static final String ORDERPLACEDATE = "orderPlaceDate";
	@Autowired
	private MplAccountAddressFacade accountAddressFacade;
	private static final Logger LOG = Logger.getLogger(OrderNotificationEmailContext.class);
	@Autowired
	private ShortUrlService shortUrlService;


	@Override
	public void init(final OrderProcessModel orderProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(orderProcessModel, emailPageModel);
		//final OrderData orderData = getOrderConverter().convert(orderProcessModel.getOrder());
		//		final double orderSubTotalPrice = orderProcessModel.getOrder().getSubtotal() == null ? 0D : orderProcessModel.getOrder()
		//				.getSubtotal().doubleValue();

		final double orderTotalPrice = orderProcessModel.getOrder().getTotalPrice() == null ? 0D : orderProcessModel.getOrder()
				.getTotalPrice().doubleValue();
		final double convenienceCharges = orderProcessModel.getOrder().getConvenienceCharges() == null ? 0D : orderProcessModel
				.getOrder().getConvenienceCharges().doubleValue();
		//final List<AbstractOrderEntryModel> childEntries = orderProcessModel.getOrder().getEntries();
		final Double totalPrice = Double.valueOf(orderTotalPrice + convenienceCharges);
		final Double convenienceChargesVal = Double.valueOf(convenienceCharges);
		double subTotal = 0.0d;
		double shippingCharge = 0.0d;
		double totalFineJewelleryPrice = 0.0d;
		double isPancardRequired = 0.0d;
		//final String amount = getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.ORDER_AMOUNT_FOR_PANCARD_UPLOAD);

		final double totalAmount = Integer.parseInt(getConfigurationService().getConfiguration().getString(
				"order.amount.for.pancard.upload"));

		//final double totalAmount1 = amount;
		//Changes for discount
		//final Double subTotal = Double.valueOf(orderSubTotalPrice);
		final List<OrderModel> childOrders = orderProcessModel.getOrder().getChildOrders();
		for (final OrderModel childOrder : childOrders)
		{
			for (final AbstractOrderEntryModel childOrderEntries : childOrder.getEntries())
			{
				subTotal += childOrderEntries.getNetAmountAfterAllDisc().doubleValue();
				shippingCharge += childOrderEntries.getCurrDelCharge().doubleValue();
				final ProductModel prod = childOrderEntries.getProduct();
				//added for jewellery
				if (prod != null && prod.getProductCategoryType().equalsIgnoreCase("FineJewellery"))
				{
					totalFineJewelleryPrice += childOrderEntries.getNetAmountAfterAllDisc().doubleValue();
					isPancardRequired = (totalFineJewelleryPrice >= totalAmount) ? 1 : 0;

				}
			}

		}



		//final DecimalFormat myFormatter = new DecimalFormat("#,###");
		//final String subTotalNew = myFormatter.format(subTotal);
		//final String totalPriceNew = myFormatter.format(totalPrice);
		//final String subTotalNew = NumberFormat.getIntegerInstance().format(subTotal);

		LOG.info(" *********************- totalPrice:" + totalPrice + " orderTotalPrice:" + orderTotalPrice
				+ " convenienceCharges:" + convenienceCharges);


		//	final Double shippingCharge = orderProcessModel.getOrder().getDeliveryCost();


		final String orderCode = orderProcessModel.getOrder().getCode();

		//final List<OrderModel> childOrders = orderProcessModel.getOrder().getChildOrders();

		final String trackOrderUrl = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.MPL_TRACK_ORDER_LONG_URL_FORMAT)
				+ orderCode;
		/* Added in R2.3 for shortUrl START */
		//final String shortUrl = shortUrlService.genearateShortURL(orderCode);
		//final String shortUrl = orderProcessModel.getOrderTrackUrl();

		final OrderShortUrlInfoModel orderShortUrlInfoModel = shortUrlService.getShortUrlReportModelByOrderId(orderCode);

		LOG.debug("**OrderCode**" + orderCode + "--**orderShortUrlInfoModel--" + orderShortUrlInfoModel);

		String shortUrl = null;

		if (orderShortUrlInfoModel == null)
		{

			LOG.debug("Inside orderShortUrlInfoModel null for orderCode**" + orderCode);
			shortUrl = shortUrlService.genearateShortURL(orderCode);
		}
		else
		{
			LOG.debug("Inside orderShortUrlInfoModel not null for orderCode**" + orderCode);
			shortUrl = orderShortUrlInfoModel.getShortURL();
		}
		LOG.debug("Generated shortUrl**" + shortUrl);

		put(TRACK_ORDER_URL, null != shortUrl ? shortUrl : trackOrderUrl);


		//final paymentMode = transactionEntry.getEntries().get(0).getPaymentMode().getMode();

		/*
		 * final String trackOrderUrl = getConfigurationService().getConfiguration().getString(
		 * MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL) + orderProcessModel.getOrder().getCode();
		 * put(TRACK_ORDER_URL, trackOrderUrl);
		 */

		put(ORDER_CODE, orderCode);
		put(CHILDORDERS, childOrders);
		put(SUBTOTAL, Double.valueOf(subTotal));
		//put(SUBTOTAL, subTotalNew);
		put(TOTALPRICE, totalPrice);
		put(SUBTOTALFORJEWELLERY, Double.valueOf(totalFineJewelleryPrice));
		put(ISPANCARDREQUIRED, Double.valueOf(isPancardRequired));
		//put(TOTALPRICE, totalPriceNew);
		put(SHIPPINGCHARGE, Double.valueOf(shippingCharge));
		put(CONVENIENCECHARGE, convenienceChargesVal);
		//Setting first name and last name to NAMEOFPERSON
		final StringBuilder name = new StringBuilder(150);
		final Set<PointOfServiceModel> storeAddrList = new HashSet<PointOfServiceModel>();
		final StringBuilder deliveryAddr = new StringBuilder(150);
		final List<StateData> stateDataList = getAccountAddressFacade().getStates();
		for (final AbstractOrderEntryModel entryModel : orderProcessModel.getOrder().getEntries())

		{
			LOG.debug("convienence apportion " + entryModel.getConvenienceChargeApportion());
			LOG.debug("total mrp" + entryModel.getTotalMrp());
			LOG.debug("total price" + entryModel.getTotalPrice());
			LOG.debug("total sale price" + entryModel.getTotalSalePrice());
			childEntries.add(entryModel);

			//Commented as part of TISSPTEN-7
			/*
			 * final String productImageUrl;
			 *
			 * final ProductModel productModel = entryModel.getProduct(); if (null != productModel.getPicture()) {
			 * productImageUrl = productModel.getPicture().getURL(); } else { productImageUrl = ""; }
			 *
			 *
			 * put(PRODUCT_IMAGE_URL, productImageUrl);
			 */


			final String orderPlaceDate;

			SimpleDateFormat formatter;
			formatter = new SimpleDateFormat("MMM d, yyyy");
			orderPlaceDate = formatter.format(entryModel.getCreationtime());

			put(ORDERPLACEDATE, orderPlaceDate);

			if (entryModel.getMplDeliveryMode().getDeliveryMode().getCode().equalsIgnoreCase(MarketplaceFacadesConstants.C_C))
			{
				final PointOfServiceModel model = entryModel.getDeliveryPointOfService();
				final AddressModel address = model.getAddress();
				for (final StateData state : stateDataList)
				{
					if (address.getState().equalsIgnoreCase(state.getCode()))
					{
						address.setState(state.getName());
						model.setAddress(address);
						break;
					}
				}
				storeAddrList.add(model);
				put(CNCSTOREADDRESS, storeAddrList);
				put(CUSTOMER_NAME, CUSTOMER);
			}
		}
		put(CHILDENTRIES, childEntries);

		final AddressModel deliveryAddress = orderProcessModel.getOrder().getDeliveryAddress();
		if (deliveryAddress != null)

		{
			if (null != deliveryAddress.getFirstname())
			{
				put(CUSTOMER_NAME, deliveryAddress.getFirstname());
			}
			if (null != deliveryAddress.getFirstname())
			{
				name.append(deliveryAddress.getFirstname());
			}
			if (null != deliveryAddress.getLastname())
			{
				name.append(SPACE).append(deliveryAddress.getLastname());
			}
			put(NAMEOFPERSON, (name.length() > 0 ? name : CUSTOMER));
			put(MOBILENUMBER, (null != deliveryAddress.getPhone1() ? deliveryAddress.getPhone1() : deliveryAddress.getCellphone()));
			put(DISPLAY_NAME, (null != deliveryAddress.getFirstname() ? deliveryAddress.getFirstname() : CUSTOMER));

			deliveryAddr.append(deliveryAddress.getStreetname());
			if (!StringUtils.isEmpty(deliveryAddress.getStreetnumber()))
			{
				deliveryAddr.append(COMMA).append(SPACE).append(deliveryAddress.getStreetnumber());
			}
			if (!StringUtils.isEmpty(deliveryAddress.getAddressLine3()))
			{
				deliveryAddr.append(COMMA).append(SPACE).append(deliveryAddress.getAddressLine3());
			}
			if (!StringUtils.isEmpty(deliveryAddress.getLandmark()))
			{
				deliveryAddr.append(COMMA).append(SPACE).append(deliveryAddress.getLandmark());
			}

			deliveryAddr.append(COMMA).append(SPACE).append(deliveryAddress.getTown()).append(COMMA).append(SPACE)
					.append(deliveryAddress.getDistrict()).append(COMMA).append(SPACE).append(deliveryAddress.getPostalcode());

			put(DELIVERYADDRESS, deliveryAddr);
		}
		String websiteUrl = null;
		websiteUrl = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.SMS_SERVICE_WEBSITE_URL);
		if (null != websiteUrl)
		{
			put(WEBSITE_URL, websiteUrl);
		}

		put(COD_CHARGES, orderProcessModel.getOrder().getConvenienceCharges());

		final CustomerModel customer = (CustomerModel) orderProcessModel.getOrder().getUser();
		put(EMAIL, customer.getOriginalUid());
		put("math", new MathTool());
		put(NUMBERTOOL, new NumberTool());

	}

	@Override
	protected BaseSiteModel getSite(final OrderProcessModel orderProcessModel)
	{
		return orderProcessModel.getOrder().getSite();
	}

	@Override
	protected CustomerModel getCustomer(final OrderProcessModel orderProcessModel)
	{
		return (CustomerModel) orderProcessModel.getOrder().getUser();
	}

	protected Converter<OrderModel, OrderData> getOrderConverter()
	{
		return orderConverter;
	}

	@Required
	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

	public OrderData getOrder()
	{
		return orderData;
	}

	@Override
	protected LanguageModel getEmailLanguage(final OrderProcessModel orderProcessModel)
	{
		return orderProcessModel.getOrder().getLanguage();
	}

	/**
	 * @return the accountAddressFacade
	 */
	public MplAccountAddressFacade getAccountAddressFacade()
	{
		return accountAddressFacade;
	}

	/**
	 * @param accountAddressFacade
	 *           the accountAddressFacade to set
	 */
	public void setAccountAddressFacade(final MplAccountAddressFacade accountAddressFacade)
	{
		this.accountAddressFacade = accountAddressFacade;
	}

}

