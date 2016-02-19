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
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.MathTool;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


/**
 * Velocity context for a order notification email.
 */
public class OrderNotificationEmailContext extends AbstractEmailContext<OrderProcessModel>
{
	private OrderData orderData;
	private Converter<OrderModel, OrderData> orderConverter;
	private static final String ORDER_CODE = "orderReferenceNumber";
	private static final String CHILDORDERS = "childOrders";
	//private static final String CHILDENTRIES = "childEntries";
	private static final String TOTALPRICE = "totalPrice";
	private static final String SHIPPINGCHARGE = "shippingCharge";
	private static final String DELIVERYADDRESS = "deliveryAddress";
	private static final String CUSTOMER_NAME = "customerName";
	private static final String COD_CHARGES = "codCharge";



	private static final String MOBILENUMBER = "mobilenumber";
	private static final String NAMEOFPERSON = "nameofperson";
	public static final String TRACK_ORDER_URL = "trackOrderUrl";
	private static final String COMMA = ",";
	private static final String CUSTOMER = "Customer";
	private static final String SPACE = " ";
	private static final Logger LOG = Logger.getLogger(OrderNotificationEmailContext.class);


	@Override
	public void init(final OrderProcessModel orderProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(orderProcessModel, emailPageModel);
		//final OrderData orderData = getOrderConverter().convert(orderProcessModel.getOrder());
		final double orderTotalPrice = orderProcessModel.getOrder().getTotalPrice() == null ? 0D : orderProcessModel.getOrder()
				.getTotalPrice().doubleValue();
		final double convenienceCharges = orderProcessModel.getOrder().getConvenienceCharges() == null ? 0D : orderProcessModel
				.getOrder().getConvenienceCharges().doubleValue();

		final Double totalPrice = Double.valueOf(orderTotalPrice + convenienceCharges);

		LOG.info(" *********************- totalPrice:" + totalPrice + " orderTotalPrice:" + orderTotalPrice
				+ " convenienceCharges:" + convenienceCharges);

		final Double shippingCharge = orderProcessModel.getOrder().getDeliveryCost();
		final AddressModel deliveryAddress = orderProcessModel.getOrder().getDeliveryAddress();
		final String orderCode = orderProcessModel.getOrder().getCode();

		final List<OrderModel> childOrders = orderProcessModel.getOrder().getChildOrders();


		final String trackOrderUrl = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
				+ orderProcessModel.getOrder().getCode();
		put(TRACK_ORDER_URL, trackOrderUrl);
		put(ORDER_CODE, orderCode);
		put(CHILDORDERS, childOrders);
		put(TOTALPRICE, totalPrice);
		put(SHIPPINGCHARGE, shippingCharge);
		//Setting first name and last name to NAMEOFPERSON
		final StringBuilder name = new StringBuilder(150);
		if (null != deliveryAddress.getFirstname())
		{
			name.append(deliveryAddress.getFirstname());
		}
		if (null != deliveryAddress.getLastname())
		{
			name.append(SPACE).append(deliveryAddress.getLastname());
		}
		put(NAMEOFPERSON, (name.length() > 0 ? name : CUSTOMER));
		/* put(NAMEOFPERSON, (null != deliveryAddress.getFirstname() ? deliveryAddress.getFirstname() : CUSTOMER)); */
		put(CUSTOMER_NAME, (null != deliveryAddress.getFirstname() ? deliveryAddress.getFirstname() : CUSTOMER));
		final StringBuilder deliveryAddr = new StringBuilder(150);



		deliveryAddr.append(deliveryAddress.getStreetname()).append(COMMA).append(deliveryAddress.getStreetnumber()).append(COMMA)
				.append(deliveryAddress.getAddressLine3()).append(COMMA).append(deliveryAddress.getTown()).append(COMMA)
				.append(deliveryAddress.getDistrict()).append(COMMA).append(deliveryAddress.getPostalcode());






		put(DELIVERYADDRESS, deliveryAddr);

		put(MOBILENUMBER, (null != deliveryAddress.getPhone1() ? deliveryAddress.getPhone1() : deliveryAddress.getCellphone()));
		put(COD_CHARGES, orderProcessModel.getOrder().getConvenienceCharges());

		final CustomerModel customer = (CustomerModel) orderProcessModel.getOrder().getUser();
		put(EMAIL, customer.getOriginalUid());
		put(DISPLAY_NAME, (null != deliveryAddress.getFirstname() ? deliveryAddress.getFirstname() : CUSTOMER));
		//		if (null != customer.getDisplayName())
		//		{
		//			if (!customer.getDisplayName().equals(" "))
		//			{
		//				put(CUSTOMER_NAME, customer.getDisplayName());
		//			}
		//			else
		//			{
		//				put(CUSTOMER_NAME, "Customer");
		//			}
		//		}
		//		else
		//		{
		//			put(CUSTOMER_NAME, "Customer");
		//		}

		put("math", new MathTool());

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

}
