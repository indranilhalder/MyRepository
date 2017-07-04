/**
 *
 */
package com.tisl.mpl.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.SendInvoiceProcessModel;


/**
 * @author TCS
 *
 */
public class SendInvoiceEmailContext extends CustomerEmailContext
{
	List<AbstractOrderEntryModel> childEntries = new ArrayList<AbstractOrderEntryModel>();
	List<OrderModel> childOrders = new ArrayList<OrderModel>();

	private static final String CUSTOMER_EMAIL = "customerEmail";
	private static final String ORDER_CODE = "orderCode";
	private static final String TRANSACTION_ID = "transactionID";
	private static final String INVOICE_URL = "invoiceUrl";
	private static final String WEBSITE_URL = "websiteUrl";
	private static final String TOTALPRICE = "totalPrice";

	//	private CustomerData customerData;

	private static final String CUSTOMER_CARE_NUMBER = "customerCareNumber";
	private static final String CUSTOMER_CARE_EMAIL = "customerCareEmail";
	private static final String CHILDENTRIES = "childEntries";
	private static final String CHILDORDERS = "childOrders";
	private static final String ORDERPLACEDATE = "orderPlaceDate";
	private static final String DELIVERYADDRESS = "deliveryAddress";
	private static final String COMMA = ",";
	private static final String SPACE = " ";

	private static final String SHIPPINGCHARGE = "shippingCharge";
	private static final String SUBTOTAL = "subTotal";
	private static final String CONVENIENCECHARGE = "convenienceChargesVal";


	@Autowired
	private ConfigurationService configurationService;
	private AddressModel deliveryAddress;


	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		{
			super.init(storeFrontCustomerProcessModel, emailPageModel);

			//			final CustomerData customerData = getCustomerConverter().convert(getCustomer(storeFrontCustomerProcessModel));
			final CustomerModel customer = getCustomer(storeFrontCustomerProcessModel);
			final String displayName = customer.getDisplayName();
			if (StringUtils.isEmpty(displayName))
			{
				put(DISPLAY_NAME, displayName);
			}

			if (storeFrontCustomerProcessModel instanceof SendInvoiceProcessModel)
			{
				put(EMAIL, ((SendInvoiceProcessModel) storeFrontCustomerProcessModel).getCustomerEmail());
				//put(DISPLAY_NAME, ((SendInvoiceProcessModel) storeFrontCustomerProcessModel).getCustomerEmail());
				put(CUSTOMER_EMAIL, ((SendInvoiceProcessModel) storeFrontCustomerProcessModel).getCustomerEmail());
				String ordercode = null;
				ordercode = ((SendInvoiceProcessModel) storeFrontCustomerProcessModel).getOrderCode();
				if (null != ordercode)
				{
					put(ORDER_CODE, ordercode);

				}

				String transactionID = null;
				transactionID = ((SendInvoiceProcessModel) storeFrontCustomerProcessModel).getTransactionId();
				if (null != transactionID)
				{
					put(TRANSACTION_ID, transactionID);

				}

				String invoiceUrl = null;
				invoiceUrl = ((SendInvoiceProcessModel) storeFrontCustomerProcessModel).getInvoiceUrl();
				if (null != invoiceUrl)
				{
					put(INVOICE_URL, invoiceUrl);

				}
				String websiteUrl = null;
				websiteUrl = getConfigurationService().getConfiguration().getString(
						MarketplacecommerceservicesConstants.SMS_SERVICE_WEBSITE_URL);
				if (null != websiteUrl)
				{
					put(WEBSITE_URL, websiteUrl);
				}



				deliveryAddress = ((SendInvoiceProcessModel) storeFrontCustomerProcessModel).getDeliveryAddress();

				final StringBuilder deliveryAddr = new StringBuilder(150);



				deliveryAddr.append(deliveryAddress.getStreetname());
				if (!StringUtils.isEmpty(deliveryAddress.getStreetnumber()))
				{

					deliveryAddr.append(COMMA).append(SPACE).append(deliveryAddress.getStreetnumber());
				}
				if (!StringUtils.isEmpty(deliveryAddress.getAddressLine3()))
				{
					deliveryAddr.append(COMMA).append(SPACE).append(deliveryAddress.getAddressLine3());
				}

				deliveryAddr.append('\n');

				final String city = deliveryAddress.getTown();
				deliveryAddr.append(city.substring(0, 1).toUpperCase() + city.substring(1)).append(COMMA).append(SPACE)
						.append(deliveryAddress.getDistrict()).append(COMMA).append(SPACE).append(deliveryAddress.getPostalcode());

				put(DELIVERYADDRESS, deliveryAddr);

				final String orderPlaceDate;
				SimpleDateFormat formatter;
				formatter = new SimpleDateFormat("MMM d, yyyy");
				orderPlaceDate = formatter.format(((SendInvoiceProcessModel) storeFrontCustomerProcessModel).getOrderPlacedDate());

				put(ORDERPLACEDATE, orderPlaceDate);

				childEntries = ((SendInvoiceProcessModel) storeFrontCustomerProcessModel).getAbstractOrderEntryList();


				childOrders = ((SendInvoiceProcessModel) storeFrontCustomerProcessModel).getChildOrderList();

				double convenienceChargesVal = 0;
				double shippingCharge = 0;
				double subTotal = 0;
				double totalPrice = 0;


				for (final AbstractOrderEntryModel childOrderEntry : childEntries)
				{

					childEntries.add(childOrderEntry);
					convenienceChargesVal += childOrderEntry.getConvenienceChargeApportion().doubleValue();
					shippingCharge += childOrderEntry.getCurrDelCharge().doubleValue();
					subTotal += childOrderEntry.getNetAmountAfterAllDisc().doubleValue();


				}

				totalPrice = subTotal + shippingCharge + convenienceChargesVal;

				put(TOTALPRICE, Double.valueOf(totalPrice));
				put(CHILDENTRIES, childEntries);
				put(SHIPPINGCHARGE, Double.valueOf(shippingCharge));
				put(SUBTOTAL, Double.valueOf(subTotal));
				put(CONVENIENCECHARGE, Double.valueOf(convenienceChargesVal));



			}

			final String customerCareNumber = configurationService.getConfiguration().getString("marketplace.sms.service.contactno",
					"1800-208-8282");
			put(CUSTOMER_CARE_NUMBER, customerCareNumber);


			final String customerCareEmail = configurationService.getConfiguration().getString("cliq.care.mail",
					"hello@tatacliq.com");
			put(CUSTOMER_CARE_EMAIL, customerCareEmail);

		}
	}



}
