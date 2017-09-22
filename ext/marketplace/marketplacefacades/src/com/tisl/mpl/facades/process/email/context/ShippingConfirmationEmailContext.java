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
import de.hybris.platform.core.model.JwlryRevSealInfoModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OrderUpdateProcessModel;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;




/**
 * Velocity context for a order notification email.
 */
public class ShippingConfirmationEmailContext extends AbstractEmailContext<OrderUpdateProcessModel>
{
	private Converter<OrderModel, OrderData> orderConverter;
	private OrderData orderData;


	final List<AbstractOrderEntryModel> childEntries = new ArrayList<AbstractOrderEntryModel>();
	private static final String ORDER_CODE = "orderReferenceNumber";
	private static final String P_ORDER_CODE = "pOrderCode";
	private static final String CHILDORDERS = "childOrders";
	private static final String CHILDENTRIES = "childEntries";
	private static final String TOTALPRICE = "totalPrice";
	private static final String SHIPPINGCHARGE = "shippingCharge";
	private static final String DELIVERYADDRESS = "deliveryAddress";
	private static final String ORDERDATE = "orderDate";
	private static final String CUSTOMER_NAME = "customerName";
	private static final String COD_CHARGES = "codCharge";
	private static final String MOBILENUMBER = "mobilenumber";
	private static final String NAMEOFPERSON = "nameofperson";
	private static final String AWBNUMBER = "trackingId";
	private static final String CARRIER = "carrier";
	private static final String CUSTOMER = "Customer";
	public static final String TRACK_ORDER_URL = "trackOrderUrl";
	private static final String NUMBERTOOL = "numberTool";
	private static final String COMMA = ",";
	private static final String SPACE = " "; //TISUATSE-80
	private static final String CUSTOMER_CARE_NUMBER = "customerCareNumber";
	private static final String CUSTOMER_CARE_EMAIL = "customerCareEmail";

	private static final String COUNT = "count"; //added for jewellery
	private static final String JWLRY_FRWRD_SEAL_DESCRIPTION = "jwlryFrwrdSealDesc";
	private static final String JWLRY_FRWRD_SEAL_IMAGE = "jwlryFrwrdSealImg";
	private static final String JWLRY_REV_SEAL_DESCRIPTION = "jwlryRevSealDesc";
	private static final String JWLRY_REV_SEAL_IMAGE = "jwlryRevSealImg";


	//TPR-5329
	//Sonar Issue Fixed For Kidswear
	//private static final String PRODUCT_IMAGE_URL = "productImageUrl";
	private static final String ORDERPLACEDATE = "orderPlaceDate";
	private static final String SUBTOTAL = "subTotal";
	private static final String CONVENIENCECHARGE = "convenienceChargesVal";

	@Autowired
	private ConfigurationService configurationService;
	
	@Resource(name = "mplJewelleryService")
	private MplJewelleryService mplJewelleryService;
	
//	@Autowired
//	private ShortUrlService shortUrlService;//Sonar Fix

	@Override
	public void init(final OrderUpdateProcessModel orderUpdateProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(orderUpdateProcessModel, emailPageModel);

		final OrderModel order = orderUpdateProcessModel.getOrder();

		//final double orderSubTotalPrice = order.getSubtotal() == null ? 0D : order.getSubtotal().doubleValue();

		//final double orderTotalPrice = order.getTotalPrice() == null ? 0D : order.getTotalPrice().doubleValue();
		//final double convenienceCharges = order.getConvenienceCharges() == null ? 0D : order.getConvenienceCharges().doubleValue();
		//final List<AbstractOrderEntryModel> childEntries = orderProcessModel.getOrder().getEntries();
		//final Double totalPrice = Double.valueOf(orderTotalPrice + convenienceCharges);
		//final Double convenienceChargesVal = Double.valueOf(convenienceCharges);
		//final Double subTotal = Double.valueOf(orderSubTotalPrice);



		final Set<ConsignmentModel> consignmentEntries = orderUpdateProcessModel.getOrder().getConsignments();
		for (final ConsignmentModel consignment : consignmentEntries)
		{
			final String carrier = consignment.getCarrier();
			put(CARRIER, carrier);
		}

		final String pOrderCode = (null != orderUpdateProcessModel.getOrder().getParentReference().getCode()) ? orderUpdateProcessModel
				.getOrder().getParentReference().getCode()
				: "";

		//final Double totalPrice = order.getTotalPrice();
		//final Double shippingCharge = order.getDeliveryCost();
		final String orderDate = (null != order.getCreationtime()) ? order.getCreationtime().toString() : "";

		final AddressModel deliveryAddress = order.getDeliveryAddress();
		final String orderReferenceNumber = order.getCode();

		final List<AbstractOrderEntryModel> childOrders = order.getEntries();
		final List<String> entryNumbers = orderUpdateProcessModel.getEntryNumber();

		double convenienceChargesVal = 0;
		double shippingCharge = 0;
		double subTotal = 0;
		double totalPrice = 0;
		double count = 0; //added for jewellery

		for (final String entryNumber : entryNumbers)
		{
			for (final AbstractOrderEntryModel childOrder : childOrders)
			{
				if (childOrder.getEntryNumber() == Integer.valueOf(entryNumber))
				{
					childEntries.add(childOrder);
					convenienceChargesVal += childOrder.getConvenienceChargeApportion().doubleValue();
					shippingCharge += childOrder.getCurrDelCharge().doubleValue();
					subTotal += childOrder.getNetAmountAfterAllDisc().doubleValue();


					//added for jewellery
					final ProductModel prod = childOrder.getProduct();
					if (prod != null && prod.getProductCategoryType().equalsIgnoreCase("FineJewellery"))
					{
						count += 1;
						final String sellerId = childOrder.getSelectedUSSID().substring(0, 6);
						final JwlryRevSealInfoModel sealInfo = mplJewelleryService.getSealInfo(sellerId);
						if (null != sealInfo)
						{
							put(JWLRY_FRWRD_SEAL_DESCRIPTION, sealInfo.getFrwrdSealDescription());
							put(JWLRY_FRWRD_SEAL_IMAGE, sealInfo.getFrwrdSealImageUrl());
							put(JWLRY_REV_SEAL_DESCRIPTION, sealInfo.getRevSealDescription());
							put(JWLRY_REV_SEAL_IMAGE, sealInfo.getRevSealImageUrl());
						}

					}

					//creation Time
					SimpleDateFormat formatter;
					formatter = new SimpleDateFormat("MMM d, yyyy");
					final String orderPlaceDate = formatter.format(childOrder.getCreationtime());
					put(ORDERPLACEDATE, orderPlaceDate);

					//					final ProductModel productModel = childOrder.getProduct();
					//					final String productImageUrl = productModel.getPicture().getURL();
					//					put(PRODUCT_IMAGE_URL, productImageUrl);

				}
			}
		}
		totalPrice = subTotal + shippingCharge + convenienceChargesVal;





		final String trackOrderUrl = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.MPL_TRACK_ORDER_LONG_URL_FORMAT)
				+ pOrderCode;
		/* Added in R2.3 for shortUrl START */
		// final String shortUrl = shortUrlService.genearateShortURL(pOrderCode);
		final String shortUrl = orderUpdateProcessModel.getOrderTrackUrl();
		put(TRACK_ORDER_URL, null != shortUrl ? shortUrl : trackOrderUrl);


		/* R2.3 shortUrl END */
		put(P_ORDER_CODE, pOrderCode);
		put(ORDER_CODE, orderReferenceNumber);
		put(CHILDORDERS, childOrders);
		put(CHILDENTRIES, childEntries);
		put(TOTALPRICE, Double.valueOf(totalPrice));
		put(SHIPPINGCHARGE, Double.valueOf(shippingCharge));
		put(COD_CHARGES, orderUpdateProcessModel.getOrder().getConvenienceCharges());
		put(ORDERDATE, orderDate);
		put(AWBNUMBER, orderUpdateProcessModel.getAwbNumber());
		put(SUBTOTAL, Double.valueOf(subTotal));
		put(CONVENIENCECHARGE, Double.valueOf(convenienceChargesVal));

		put(NAMEOFPERSON, deliveryAddress.getFirstname());
		final StringBuilder deliveryAddr = new StringBuilder(150);
		put(COUNT, Double.valueOf(count)); //added for jewellery


		deliveryAddr.append(deliveryAddress.getStreetname());
		if (!StringUtils.isEmpty(deliveryAddress.getStreetnumber()))
		{
			//TISUATSE-80 starts
			deliveryAddr.append(COMMA).append(SPACE).append(deliveryAddress.getStreetnumber());
		}
		if (!StringUtils.isEmpty(deliveryAddress.getAddressLine3()))
		{
			deliveryAddr.append(COMMA).append(SPACE).append(deliveryAddress.getAddressLine3());
		}
		//TISUATSE-80 starts

		deliveryAddr.append('\n');//Sonar fix
		//TISUATSE-81 starts
		final String city = deliveryAddress.getTown();
		deliveryAddr.append(city.substring(0, 1).toUpperCase() + city.substring(1)).append(COMMA).append(SPACE)
				.append(deliveryAddress.getDistrict()).append(COMMA).append(SPACE).append(deliveryAddress.getPostalcode());

		put(DELIVERYADDRESS, deliveryAddr);

		put(MOBILENUMBER, (null != deliveryAddress.getPhone1() ? deliveryAddress.getPhone1() : deliveryAddress.getCellphone()));

		final CustomerModel customer = (CustomerModel) orderUpdateProcessModel.getOrder().getUser();
		put(EMAIL, customer.getOriginalUid());

		if (null != orderUpdateProcessModel.getOrder().getDeliveryAddress().getFirstname())
		{
			if (!orderUpdateProcessModel.getOrder().getDeliveryAddress().getFirstname().equals(" "))
			{
				final String firstName = orderUpdateProcessModel.getOrder().getDeliveryAddress().getFirstname();
				put(CUSTOMER_NAME, firstName);
				put(DISPLAY_NAME, firstName);
			}
			else
			{
				put(CUSTOMER_NAME, CUSTOMER);
				put(DISPLAY_NAME, CUSTOMER);
			}
		}
		else
		{
			put(DISPLAY_NAME, CUSTOMER);
			put(CUSTOMER_NAME, CUSTOMER);
		}
		put("math", new MathTool());
		put(NUMBERTOOL, new NumberTool());


		final String customerCareNumber = configurationService.getConfiguration().getString("marketplace.sms.service.contactno",
				"1800-208-8282");
		put(CUSTOMER_CARE_NUMBER, customerCareNumber);


		final String customerCareEmail = configurationService.getConfiguration().getString("cliq.care.mail", "hello@tatacliq.com");
		put(CUSTOMER_CARE_EMAIL, customerCareEmail);
	}

	@Override
	protected BaseSiteModel getSite(final OrderUpdateProcessModel orderProcessModel)
	{
		return orderProcessModel.getOrder().getSite();
	}

	@Override
	protected CustomerModel getCustomer(final OrderUpdateProcessModel orderProcessModel)
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
	protected LanguageModel getEmailLanguage(final OrderUpdateProcessModel orderProcessModel)
	{
		return orderProcessModel.getOrder().getLanguage();
	}

}
