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
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

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
import com.tisl.mpl.facades.account.address.MplAccountAddressFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.product.data.StateData;


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
	private static final String TOTALPRICE = "totalPrice";
	private static final String SHIPPINGCHARGE = "shippingCharge";
	private static final String DELIVERYADDRESS = "deliveryAddress";
	private static final String CNCSTOREADDRESS = "storeAddress";
	private static final String CUSTOMER_NAME = "customerName";
	private static final String COD_CHARGES = "codCharge";



	private static final String MOBILENUMBER = "mobilenumber";
	private static final String NAMEOFPERSON = "nameofperson";
	public static final String TRACK_ORDER_URL = "trackOrderUrl";
	private static final String COMMA = ",";
	private static final String CUSTOMER = "Customer";
	private static final String SPACE = " ";
	private static final String NUMBERTOOL = "numberTool";
	private static final String WEBSITE_URL = "websiteUrl";
	@Autowired
	private MplAccountAddressFacade accountAddressFacade;
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
		//final List<AbstractOrderEntryModel> childEntries = orderProcessModel.getOrder().getEntries();
		final Double totalPrice = Double.valueOf(orderTotalPrice + convenienceCharges);



		LOG.info(" *********************- totalPrice:" + totalPrice + " orderTotalPrice:" + orderTotalPrice
				+ " convenienceCharges:" + convenienceCharges);


		final Double shippingCharge = orderProcessModel.getOrder().getDeliveryCost();


		final String orderCode = orderProcessModel.getOrder().getCode();

		final List<OrderModel> childOrders = orderProcessModel.getOrder().getChildOrders();

		//final paymentMode = transactionEntry.getEntries().get(0).getPaymentMode().getMode();

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
				//TISUATSE-81 starts
				deliveryAddr.append(deliveryAddress.getStreetnumber());
			}
			if (!StringUtils.isEmpty(deliveryAddress.getAddressLine3()))
			{
				deliveryAddr.append(deliveryAddress.getAddressLine3());
			}
			//TISUATSE-81 ends
			if (!StringUtils.isEmpty(deliveryAddress.getLandmark()))
			{
				deliveryAddr.append(COMMA).append(deliveryAddress.getLandmark());
			}

			//TISUATSE-70 starts

			final String city = deliveryAddress.getTown();
			deliveryAddr.append(city.substring(0, 1).toUpperCase() + city.substring(1)).append(COMMA).append(SPACE)
					.append(deliveryAddress.getDistrict()).append(SPACE).append(deliveryAddress.getPostalcode());
			//TISUATSE-70 ends
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
