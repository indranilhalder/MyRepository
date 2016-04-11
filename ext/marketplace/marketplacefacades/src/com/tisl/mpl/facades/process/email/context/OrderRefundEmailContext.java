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
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.OrderRefundProcessModel;


/**
 * Velocity context for a order refund email.
 */
public class OrderRefundEmailContext extends AbstractEmailContext<OrderRefundProcessModel>
{
	private Converter<OrderModel, OrderData> orderConverter;
	private OrderData orderData;
	private String orderCode;
	private String orderGuid;
	private boolean guest;
	private String storeName;
	private PriceData refundAmount;
	private static final String ORDER_REFERENCE_NUMBER = "orderReferenceNumber";
	private static final String CUSTOMER_NAME = "customerName";
	private static final String TRANSACTION_ID = "transactionId";
	private static final String NAME_OF_PRODUCT = "nameOfProduct";
	private static final String AMOUNT_REFUNDED = "amountrefunded";
	private static final String DELIVERY_CHARGE = "deliveryCharge";
	private static final String CUSTOMER = "Customer";
	private static final String CONTACT_US_LINK = "contactUsLink";
	private static final String NUMBERTOOL = "numberTool";

	private static final String CUSTOMER_CARE_NUMBER = "customerCareNumber";
	private static final String CUSTOMER_CARE_EMAIL = "customerCareEmail";


	@Autowired
	private ConfigurationService configurationService;

	private static final Logger LOG = Logger.getLogger(OrderRefundEmailContext.class);

	@Override
	public void init(final OrderRefundProcessModel orderRefundProcessModel, final EmailPageModel emailPageModel)
	{
		LOG.info("In return refund email context");
		super.init(orderRefundProcessModel, emailPageModel);
		orderData = getOrderConverter().convert(orderRefundProcessModel.getOrder());
		final OrderModel orderModel = orderRefundProcessModel.getOrder();
		orderCode = orderRefundProcessModel.getOrder().getCode();
		orderGuid = orderRefundProcessModel.getOrder().getGuid();
		guest = CustomerType.GUEST.equals(getCustomer(orderRefundProcessModel).getType());
		storeName = orderRefundProcessModel.getOrder().getStore().getName();
		orderData = getOrderConverter().convert(orderRefundProcessModel.getOrder());
		refundAmount = orderData.getTotalPrice();
		final String contactUsLink = configurationService.getConfiguration().getString("marketplace.contactus.link");
		put(CONTACT_US_LINK, contactUsLink);
		put(ORDER_REFERENCE_NUMBER, orderCode);
		final CustomerModel customer = (CustomerModel) orderRefundProcessModel.getOrder().getUser();
		put(EMAIL, customer.getOriginalUid());
		final ReturnEntryModel returnEntry = orderRefundProcessModel.getReturnEntry();

		BigDecimal refundAmount = BigDecimal.ZERO;
		AbstractOrderEntryModel orderEntry = null;
		double deliveryCharge = 0.0;
		if (returnEntry instanceof RefundEntryModel)
		{
			final RefundEntryModel refundEntry = (RefundEntryModel) returnEntry;
			orderEntry = refundEntry.getOrderEntry();
			deliveryCharge = orderEntry.getCurrDelCharge() == null ? 0D : orderEntry.getCurrDelCharge().doubleValue();
			if (null != orderEntry.getNetAmountAfterAllDisc())
			{
				refundAmount = BigDecimal.valueOf(orderEntry.getNetAmountAfterAllDisc().doubleValue());
			}
		}

		LOG.info("--------------------------- For order :" + orderCode + ", Refund Amount is >>>>>>" + refundAmount
				+ " where deliverycharge is :" + deliveryCharge + " product name:" + orderEntry.getProduct().getName());
		final AddressModel deliveryAddress = orderModel.getDeliveryAddress();
		put(TRANSACTION_ID, orderEntry.getTransactionID());
		put(NAME_OF_PRODUCT, orderEntry.getProduct().getName());
		put(AMOUNT_REFUNDED, refundAmount);
		put(DELIVERY_CHARGE, Double.valueOf(deliveryCharge));
		put(CUSTOMER_NAME, (null != deliveryAddress.getFirstname() ? deliveryAddress.getFirstname() : CUSTOMER));
		put(DISPLAY_NAME, (null != deliveryAddress.getFirstname() ? deliveryAddress.getFirstname() : CUSTOMER));
		put(NUMBERTOOL, new NumberTool());


		final String customerCareNumber = configurationService.getConfiguration().getString("marketplace.sms.service.contactno",
				"1800-208-8282");
		put(CUSTOMER_CARE_NUMBER, customerCareNumber);


		final String customerCareEmail = configurationService.getConfiguration().getString("cliq.care.mail", "hello@tatacliq.com");
		put(CUSTOMER_CARE_EMAIL, customerCareEmail);
	}


	@Override
	protected BaseSiteModel getSite(final OrderRefundProcessModel orderRefundProcessModel)
	{
		return orderRefundProcessModel.getOrder().getSite();
	}

	@Override
	protected CustomerModel getCustomer(final OrderRefundProcessModel orderRefundProcessModel)
	{
		return (CustomerModel) orderRefundProcessModel.getOrder().getUser();
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
	protected LanguageModel getEmailLanguage(final OrderRefundProcessModel orderRefundProcessModel)
	{
		return orderRefundProcessModel.getOrder().getLanguage();
	}

	public OrderData getOrderData()
	{
		return orderData;
	}

	public void setOrderData(final OrderData orderData)
	{
		this.orderData = orderData;
	}

	public String getOrderCode()
	{
		return orderCode;
	}

	public void setOrderCode(final String orderCode)
	{
		this.orderCode = orderCode;
	}

	public String getOrderGuid()
	{
		return orderGuid;
	}

	public void setOrderGuid(final String orderGuid)
	{
		this.orderGuid = orderGuid;
	}

	public boolean isGuest()
	{
		return guest;
	}

	public void setGuest(final boolean guest)
	{
		this.guest = guest;
	}

	public String getStoreName()
	{
		return storeName;
	}

	public void setStoreName(final String storeName)
	{
		this.storeName = storeName;
	}

	public PriceData getRefundAmount()
	{
		return refundAmount;
	}
}
