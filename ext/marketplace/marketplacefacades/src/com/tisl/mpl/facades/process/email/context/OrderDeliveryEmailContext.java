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
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.OrderUpdateProcessModel;


/**
 * @author TCS
 *
 */
public class OrderDeliveryEmailContext extends AbstractEmailContext<OrderUpdateProcessModel>
{

	private Converter<OrderModel, OrderData> orderConverter;
	private OrderData orderData;


	final List<AbstractOrderEntryModel> childEntries = new ArrayList<AbstractOrderEntryModel>();
	private static final String ORDER_CODE = "orderCode";
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
	private static final String NUMBERTOOL = "numberTool";
	private static final String COMMA = ",";


	@Override
	public void init(final OrderUpdateProcessModel orderUpdateProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(orderUpdateProcessModel, emailPageModel);
		final OrderModel order = orderUpdateProcessModel.getOrder();

		final Set<ConsignmentModel> consignmentEntries = orderUpdateProcessModel.getOrder().getConsignments();
		for (final ConsignmentModel consignment : consignmentEntries)
		{
			final String carrier = consignment.getCarrier();
			//fix for TISEE-5438
			if (null != orderUpdateProcessModel.getAwbNumber() && null != consignment.getTrackingID()
					&& orderUpdateProcessModel.getAwbNumber().equalsIgnoreCase(consignment.getTrackingID()))
			{
				put(CARRIER, carrier);
				break;
			}

		}

		final String pOrderCode = (null != orderUpdateProcessModel.getOrder().getParentReference().getCode()) ? orderUpdateProcessModel
				.getOrder().getParentReference().getCode()
				: "";

		final Double totalPrice = order.getTotalPrice();
		final Double shippingCharge = order.getDeliveryCost();
		final String orderDate = (null != order.getCreationtime()) ? order.getCreationtime().toString() : "";

		final AddressModel deliveryAddress = order.getDeliveryAddress();
		final String orderCode = order.getCode();

		final List<AbstractOrderEntryModel> childOrders = order.getEntries();
		final List<String> entryNumbers = orderUpdateProcessModel.getEntryNumber();

		for (final String entryNumber : entryNumbers)
		{

			for (final AbstractOrderEntryModel childOrder : childOrders)
			{
				if (childOrder.getEntryNumber() == Integer.valueOf(entryNumber))
				{
					childEntries.add(childOrder);

				}
			}
		}

		put(P_ORDER_CODE, pOrderCode);
		put(ORDER_CODE, orderCode);
		put(CHILDORDERS, childOrders);
		put(CHILDENTRIES, childEntries);
		put(TOTALPRICE, totalPrice);
		put(SHIPPINGCHARGE, shippingCharge);
		put(COD_CHARGES, orderUpdateProcessModel.getOrder().getConvenienceCharges());
		put(ORDERDATE, orderDate);
		put(AWBNUMBER, orderUpdateProcessModel.getAwbNumber());

		put(NAMEOFPERSON, deliveryAddress.getFirstname());
		final StringBuilder deliveryAddr = new StringBuilder(150);



		deliveryAddr.append(deliveryAddress.getStreetname()).append(COMMA).append(deliveryAddress.getStreetnumber()).append(COMMA)
				.append(deliveryAddress.getAddressLine3()).append(COMMA).append(deliveryAddress.getTown()).append(COMMA)
				.append(deliveryAddress.getDistrict()).append(COMMA).append(deliveryAddress.getPostalcode());


		put(DELIVERYADDRESS, deliveryAddr);

		put(NUMBERTOOL, new NumberTool());

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
