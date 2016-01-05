/**
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

import java.util.List;


/**
 * @author TCS
 *
 */
public class OrderConfirmationRiskEmailContext extends AbstractEmailContext<OrderProcessModel>
{
	private OrderData orderData;


	private List<AbstractOrderEntryModel> childEntries;

	private static final String ORDER_CODE = "orderReferenceNumber";
	private static final String CHILDORDERS = "childOrders";
	private static final String CHILDENTRIES = "childEntries";
	private static final String TOTALPRICE = "totalPrice";
	private static final String SHIPPINGCHARGE = "shippingCharge";
	private static final String DELIVERYADDRESS = "deliveryAddress";
	private static final String CUSTOMER_NAME = "customerName";

	//	private static final String TRANSACTION_ID = "transactionId";
	//private static final String PRICE = "price";
	private static final String COD_CHARGES = "codCharge";
	//private static final String PRODUCT_NAME = "nameOfProduct";
	//private static final String QUANTITY = "quantity";

	private static final String MOBILENUMBER = "mobilenumber";
	private static final String NAMEOFPERSON = "nameofperson";
	private static final String SPACE = " ";
	private static final String CUSTOMER = "Customer";
	private static final String COMMA = ",";




	@Override
	public void init(final OrderProcessModel orderProcessModel, final EmailPageModel emailPageModel)
	{

		String orderCode;
		List<OrderModel> childOrders;
		Double totalPrice;
		Double shippingCharge;
		AddressModel deliveryAddress;
		super.init(orderProcessModel, emailPageModel);
		//orderData = getOrderConverter().convert(orderProcessModel.getOrder());
		totalPrice = orderProcessModel.getOrder().getTotalPrice();
		shippingCharge = orderProcessModel.getOrder().getDeliveryCost();
		deliveryAddress = orderProcessModel.getOrder().getDeliveryAddress();
		orderCode = orderProcessModel.getOrder().getCode();

		childOrders = orderProcessModel.getOrder().getChildOrders();

		put(ORDER_CODE, orderCode);
		put(CHILDORDERS, childOrders);
		put(CHILDENTRIES, childEntries);
		put(TOTALPRICE, totalPrice);
		put(SHIPPINGCHARGE, shippingCharge);
		put(NAMEOFPERSON, deliveryAddress.getFirstname());
		final StringBuilder deliveryAddr = new StringBuilder(150);



		deliveryAddr.append(deliveryAddress.getStreetname()).append(COMMA).append(deliveryAddress.getStreetnumber()).append(COMMA)
				.append(deliveryAddress.getAddressLine3()).append(COMMA).append(deliveryAddress.getTown()).append(COMMA)
				.append(deliveryAddress.getDistrict()).append(COMMA).append(deliveryAddress.getPostalcode());
		put(DELIVERYADDRESS, deliveryAddr);
		put(MOBILENUMBER, (null != deliveryAddress.getPhone1() ? deliveryAddress.getPhone1() : deliveryAddress.getCellphone()));
		put(COD_CHARGES, orderProcessModel.getOrder().getConvenienceCharges());

		final CustomerModel customer = (CustomerModel) orderProcessModel.getOrder().getUser();
		put(EMAIL, customer.getOriginalUid());
		put(DISPLAY_NAME, (null != deliveryAddress.getFirstname() ? deliveryAddress.getFirstname() : CUSTOMER_NAME));
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
		put(CUSTOMER_NAME, (null != deliveryAddress.getFirstname() ? deliveryAddress.getFirstname() : CUSTOMER_NAME));
		/*
		 * if (null != customer.getDisplayName()) { if (!customer.getDisplayName().equals(" ")) { put(CUSTOMER_NAME,
		 * customer.getDisplayName()); } else { put(CUSTOMER_NAME, "Customer"); } } else { put(CUSTOMER_NAME, "Customer");
		 * }
		 */


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
