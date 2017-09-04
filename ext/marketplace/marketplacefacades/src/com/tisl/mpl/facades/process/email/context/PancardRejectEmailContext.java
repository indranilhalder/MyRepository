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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.facades.account.address.MplAccountAddressFacade;
import com.tisl.mpl.shorturl.service.ShortUrlService;


/**
 * Velocity context for a pan card reject email.
 */
public class PancardRejectEmailContext extends AbstractEmailContext<OrderProcessModel>
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
	//private static final String CNCSTOREADDRESS = "storeAddress";
	private static final String CUSTOMER_NAME = "customerName";
	private static final String COD_CHARGES = "codCharge";
	private static final String SUBTOTALFORJEWELLERY = "subTotalForJewellery";
	public static final String TRACK_ORDER_URL = "trackOrderUrl";
	private static final String CUSTOMER = "Customer";
	private static final String NUMBERTOOL = "numberTool";
	private static final String WEBSITE_URL = "websiteUrl";
	private static final String PRODUCT_IMAGE_URL = "productImageUrl";
	private static final String ORDERPLACEDATE = "orderPlaceDate";
	@Autowired
	private MplAccountAddressFacade accountAddressFacade;
	private static final Logger LOG = Logger.getLogger(PancardRejectEmailContext.class);
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
				}
			}

		}

		LOG.info(" *********************- totalPrice:" + totalPrice + " orderTotalPrice:" + orderTotalPrice
				+ " convenienceCharges:" + convenienceCharges);


		final String orderCode = orderProcessModel.getOrder().getCode();

		final String trackOrderUrl = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.MPL_TRACK_ORDER_LONG_URL_FORMAT)
				+ orderCode;
		/* Added in R2.3 for shortUrl START */
		final String shortUrl = shortUrlService.genearateShortURL(orderCode);
		put(TRACK_ORDER_URL, null != shortUrl ? shortUrl : trackOrderUrl);
		put(ORDER_CODE, orderCode);
		put(CHILDORDERS, childOrders);
		put(SUBTOTAL, Double.valueOf(subTotal));
		//put(SUBTOTAL, subTotalNew);
		put(TOTALPRICE, totalPrice);
		put(SUBTOTALFORJEWELLERY, Double.valueOf(totalFineJewelleryPrice));
		//put(TOTALPRICE, totalPriceNew);
		put(SHIPPINGCHARGE, Double.valueOf(shippingCharge));
		put(CONVENIENCECHARGE, convenienceChargesVal);

		//final Set<PointOfServiceModel> storeAddrList = new HashSet<PointOfServiceModel>();
		//final StringBuilder deliveryAddr = new StringBuilder(150);
		//final List<StateData> stateDataList = getAccountAddressFacade().getStates();
		for (final AbstractOrderEntryModel entryModel : orderProcessModel.getOrder().getEntries())

		{
			LOG.debug("convienence apportion " + entryModel.getConvenienceChargeApportion());
			LOG.debug("total mrp" + entryModel.getTotalMrp());
			LOG.debug("total price" + entryModel.getTotalPrice());
			LOG.debug("total sale price" + entryModel.getTotalSalePrice());
			childEntries.add(entryModel);

			final String productImageUrl;

			final ProductModel productModel = entryModel.getProduct();
			if (null != productModel.getPicture())
			{
				productImageUrl = productModel.getPicture().getURL();
			}
			else
			{
				productImageUrl = "";
			}
			put(PRODUCT_IMAGE_URL, productImageUrl);


			final String orderPlaceDate;

			SimpleDateFormat formatter;
			formatter = new SimpleDateFormat("MMM d, yyyy");
			orderPlaceDate = formatter.format(entryModel.getCreationtime());

			put(ORDERPLACEDATE, orderPlaceDate);


		}
		put(CHILDENTRIES, childEntries);

		//TISJEW-4487
		final CustomerModel customer = (CustomerModel) orderProcessModel.getOrder().getUser();
		final AddressModel deliveryAddress = orderProcessModel.getOrder().getDeliveryAddress();
		String displayName = "";
		if (deliveryAddress != null)
		{
			displayName = deliveryAddress.getFirstname();
		}

		if (StringUtils.isEmpty(displayName))
		{
			put(CUSTOMER_NAME, CUSTOMER);
		}
		else
		{
			put(CUSTOMER_NAME, displayName);
		}


		String websiteUrl = null;
		websiteUrl = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.SMS_SERVICE_WEBSITE_URL);
		if (null != websiteUrl)
		{
			put(WEBSITE_URL, websiteUrl);
		}

		put(COD_CHARGES, orderProcessModel.getOrder().getConvenienceCharges());

		//final CustomerModel customer = (CustomerModel) orderProcessModel.getOrder().getUser();
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