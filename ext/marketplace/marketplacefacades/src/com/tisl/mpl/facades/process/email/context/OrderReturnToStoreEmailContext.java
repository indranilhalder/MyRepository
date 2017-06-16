/**
 *
 */
package com.tisl.mpl.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
/*import de.hybris.platform.commercefacades.product.data.PriceData;
*/import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

/*import java.math.BigDecimal;*/
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tis.mpl.facade.data.ReturnToStoreAddressData;
import com.tisl.mpl.core.model.ReturnQuickDropProcessModel;


/**
 * @author TO-OW101
 *
 */
public class OrderReturnToStoreEmailContext extends AbstractEmailContext<ReturnQuickDropProcessModel>
{

	private Converter<OrderModel, OrderData> orderConverter;
	private OrderData orderData;
	private String orderCode;
	private String orderGuid;
	private boolean guest;
	private String storeName;
/*	private PriceData refundAmount;*/
	private static final String ORDER_REFERENCE_NUMBER = "orderReferenceNumber";
	private static final String CUSTOMER_NAME = "customerName";
	private static final String TRANSACTION_ID = "transactionId";
	private static final String NAME_OF_PRODUCT = "nameOfProduct";
	/*private static final String AMOUNT_REFUNDED = "amountrefunded";*/
	private static final String DELIVERY_CHARGE = "deliveryCharge";
	private static final String CUSTOMER = "Customer";
	private static final String CONTACT_US_LINK = "contactUsLink";
	/*private static final String STORE_LIST = "storeList";*/

	private static final String CUSTOMER_CARE_NUMBER = "customerCareNumber";
	private static final String CUSTOMER_CARE_EMAIL = "customerCareEmail";
	//private static final String SHIPPING_CHARGES = "shippingCharges";
	private static final String COD_CHARGES = "codCharges";
	private static final String TOTAl_AMOUNT = "total";
	private static final String PRODUCT_PRICE = "productPrice";
	private static final String SELLER_ORDER_NUMBER = "sellerOrderNumber";

	@Autowired
	private ConfigurationService configurationService;

	private static final Logger LOG = Logger.getLogger(OrderReturnToStoreEmailContext.class);

	@Override
	public void init(final ReturnQuickDropProcessModel returnQuickDropProcessModel, final EmailPageModel emailPageModel)
	{
		LOG.info("In return refund email context");
		super.init(returnQuickDropProcessModel, emailPageModel);
		orderData = getOrderConverter().convert(returnQuickDropProcessModel.getOrder());
		final OrderModel orderModel = returnQuickDropProcessModel.getOrder();
		orderCode = returnQuickDropProcessModel.getOrder().getCode();
		orderGuid = returnQuickDropProcessModel.getOrder().getGuid();
		guest = CustomerType.GUEST.equals(getCustomer(returnQuickDropProcessModel).getType());
		orderData = getOrderConverter().convert(returnQuickDropProcessModel.getOrder());
/*		refundAmount = orderData.getTotalPrice();*/
		final String contactUsLink = configurationService.getConfiguration().getString("marketplace.contactus.link");
		put(CONTACT_US_LINK, contactUsLink);
		put(ORDER_REFERENCE_NUMBER, orderCode);
		final CustomerModel customer = (CustomerModel) returnQuickDropProcessModel.getOrder().getUser();
		String name=customer.getFirstName()!=null?customer.getFirstName():CUSTOMER;
		put(CUSTOMER_NAME, name);
		put(EMAIL, customer.getOriginalUid());
      String subOrderId = null;
      

	/*	final BigDecimal refundAmount = BigDecimal.ZERO;*/
		AbstractOrderEntryModel orderEntry = null;
		 double deliveryCharge = 0.0;
		 double codCharges = 0.0;
		 double productPrice = 0.0;
		 double total = 0.0;
		final boolean isTrue = false;
		for (final OrderModel childOrder : orderModel.getChildOrders())
		{
			for (final AbstractOrderEntryModel entry : childOrder.getEntries())
			{
				if (StringUtils.isNotEmpty(returnQuickDropProcessModel.getTransactionId()) && 
						returnQuickDropProcessModel.getTransactionId().equalsIgnoreCase(entry.getTransactionID()))
				{
					orderEntry = new AbstractOrderEntryModel();
					subOrderId=childOrder.getCode();
					orderEntry = entry;
					break;
				}
				if (isTrue)
				{
					break;
				}

			}

		}
		ArrayList<ReturnToStoreAddressData> storeInfoList =new ArrayList<ReturnToStoreAddressData>();
		int countName=0;
		ReturnToStoreAddressData rtsData=null;
		for(String storeAdd:returnQuickDropProcessModel.getStoreNames()){
			rtsData=new ReturnToStoreAddressData();
			rtsData.setDate(returnQuickDropProcessModel.getDateReturnToStore());
			rtsData.setStoreAddress(storeAdd);
			rtsData.setStoreName(returnQuickDropProcessModel.getStoreIds().get(countName));
			storeInfoList.add(rtsData);
			countName++;
		}
		
		if(null != orderEntry && null != orderEntry.getCurrDelCharge()) {
			deliveryCharge+=orderEntry.getCurrDelCharge().doubleValue();
		}
		
		if(null != orderEntry && null != orderEntry.getScheduledDeliveryCharge()) {
			deliveryCharge+=orderEntry.getScheduledDeliveryCharge().doubleValue();
		}
		
		
		if(null != orderEntry && null != orderEntry.getConvenienceChargeApportion()) {
			codCharges=orderEntry.getConvenienceChargeApportion().doubleValue();
		}
		if(null != orderEntry && null != orderEntry.getNetAmountAfterAllDisc()) {
			productPrice=orderEntry.getNetAmountAfterAllDisc().doubleValue();
			total=orderEntry.getNetAmountAfterAllDisc().doubleValue();
				total += deliveryCharge+codCharges;
		}
		
		
		put("storeInfoList",storeInfoList);
		put(TRANSACTION_ID, orderEntry.getTransactionID());
		put(NAME_OF_PRODUCT, orderEntry.getProduct().getName());
		put(SELLER_ORDER_NUMBER, subOrderId);
		put(DELIVERY_CHARGE, Double.valueOf(deliveryCharge));
		put(COD_CHARGES, Double.valueOf(codCharges));
		put(PRODUCT_PRICE, Double.valueOf(productPrice));
		put(TOTAl_AMOUNT, String.valueOf(total));


		final String customerCareNumber = configurationService.getConfiguration().getString("marketplace.sms.service.contactno",
				"1800-208-8282");
		put(CUSTOMER_CARE_NUMBER, customerCareNumber);


		final String customerCareEmail = configurationService.getConfiguration().getString("cliq.care.mail", "hello@tatacliq.com");
		put(CUSTOMER_CARE_EMAIL, customerCareEmail);
	}


	@Override
	protected BaseSiteModel getSite(final ReturnQuickDropProcessModel returnQuickDropProcessModel)
	{
		return returnQuickDropProcessModel.getOrder().getSite();
	}

	@Override
	protected CustomerModel getCustomer(final ReturnQuickDropProcessModel returnQuickDropProcessModel)
	{
		return (CustomerModel) returnQuickDropProcessModel.getOrder().getUser();
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
	protected LanguageModel getEmailLanguage(final ReturnQuickDropProcessModel returnQuickDropProcessModel)
	{
		return returnQuickDropProcessModel.getOrder().getLanguage();
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

/*	public PriceData getRefundAmount()
	{
		return refundAmount;
	}*/
}