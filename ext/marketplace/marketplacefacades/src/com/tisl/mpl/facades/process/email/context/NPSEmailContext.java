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
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.NpsEmailProcessModel;
import com.tisl.mpl.core.model.PcmProductVariantModel;


/**
 * @author TCS
 *
 */
public class NPSEmailContext extends AbstractEmailContext<NpsEmailProcessModel>
{

	private Converter<OrderModel, OrderData> orderConverter;
	private OrderData orderData;

	private static final String ORDER_CODE = "orderCode";
	private static final String PRODUCT_NAME = "productName";
	private static final String PRODUCT_IMG = "productImg";
	private static final String PRODUCT_SIZE = "productSize";
	private static final String SELLER_NAME = "sellerName";
	private static final String TOTALPRICE = "totalPrice";
	private static final String SHIPPINGCHARGE = "shippingCharge";
	private static final String COUPON_DISCOUNT = "couponDiscount";
	private static final String CUSTOMER_NAME = "customerName";
	private static final String TOTAL_NET_AMOUNT = "totalNetAmount";
	private static final String ORIGINAL_UID = "originalUid";
	private static final String TRANSACTIONID = "transactionId";
	private static final String WEBSITE_URL = "websiteUrl";
	private static final String CUSTOMER = "Customer";
	private static final String DELIVERY_MODE = "deliveryMode";



	private static final Logger LOG = Logger.getLogger(NPSEmailContext.class);

	@Override
	public void init(final NpsEmailProcessModel npsUpdateProcessModel, final EmailPageModel emailPageModel)
	{
		LOG.info("In NPS email context");
		super.init(npsUpdateProcessModel, emailPageModel);

		final AbstractOrderEntryModel orderEntry = npsUpdateProcessModel.getAbstractOrderEntry();
		LOG.info("In NPS email context orderEntry" + orderEntry);


		if (orderEntry != null)
		{
			LOG.info("In NPSEmaiContext orderEntry is not null>>>>>>>>>>" + orderEntry);
			final ProductModel productInfo = orderEntry.getProduct();
			if (orderEntry.getMplDeliveryMode().getDeliveryMode().getCode() != null)
			{

				final String deliveryMode = orderEntry.getMplDeliveryMode().getDeliveryMode().getCode();
				put(DELIVERY_MODE, deliveryMode);
			}

			final String productName = productInfo.getName();
			put(PRODUCT_NAME, productName);

			if (productInfo instanceof PcmProductVariantModel)
			{
				final PcmProductVariantModel variantProductModel = (PcmProductVariantModel) productInfo;
				final String productSize = variantProductModel.getSize();
				put(PRODUCT_SIZE, productSize);
			}
			//Changed for-6033
			if (orderEntry.getCouponCode() != null)
			{
				final Double couponDiscount = orderEntry.getCouponValue();
				put(COUPON_DISCOUNT, couponDiscount);
			}
			final Double totalNetAmount = new Double(orderEntry.getNetAmountAfterAllDisc().doubleValue());

			put(TOTAL_NET_AMOUNT, totalNetAmount);



			if (productInfo.getPicture() != null)
			{

				final String productImg = productInfo.getPicture().getURL();
				put(PRODUCT_IMG, productImg);
			}

			final Double shippingCharge = orderEntry.getCurrDelCharge();
			put(SHIPPINGCHARGE, shippingCharge);

			final String sellerName = orderEntry.getSellerInfo();
			put(SELLER_NAME, sellerName);


			final Double totalPrice = new Double(orderEntry.getNetAmountAfterAllDisc().doubleValue()
					+ orderEntry.getCurrDelCharge().doubleValue());



			put(TOTALPRICE, totalPrice);

			final String transactionId = orderEntry.getTransactionID();

			put(TRANSACTIONID, transactionId);

		}

		final String orderCode = npsUpdateProcessModel.getOrder().getCode();
		put(ORDER_CODE, orderCode);
		final AddressModel deliveryAddress = npsUpdateProcessModel.getOrder().getDeliveryAddress();
		if (deliveryAddress != null)

		{
			if (null != deliveryAddress.getFirstname())
			{
				put(CUSTOMER_NAME, deliveryAddress.getFirstname());
			}
		}

		final CustomerModel customerModel = (CustomerModel) npsUpdateProcessModel.getOrder().getUser();

		if (customerModel != null)
		{

			put(ORIGINAL_UID, customerModel.getOriginalUid());
			put(EMAIL, customerModel.getOriginalUid());
		}

		String websiteUrl = null;
		websiteUrl = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.SMS_SERVICE_WEBSITE_URL);
		if (null != websiteUrl)
		{
			put(WEBSITE_URL, websiteUrl);
		}

		if (deliveryAddress != null)
		{
			put(CUSTOMER_NAME, (null != deliveryAddress.getFirstname() ? deliveryAddress.getFirstname() : CUSTOMER));
		}
		else
		{
			put(CUSTOMER_NAME, CUSTOMER);
		}


		LOG.info("All the NPSEmailContext data have been set sucessfully in context file>>>>>>>>>>");

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



	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getSite(de.hybris.platform.
	 * processengine.model.BusinessProcessModel)
	 */
	@Override
	protected BaseSiteModel getSite(final NpsEmailProcessModel npsUpdateProcessModel)
	{
		return npsUpdateProcessModel.getOrder().getSite();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getCustomer(de.hybris.platform
	 * .processengine.model.BusinessProcessModel)
	 */


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getEmailLanguage(de.hybris.platform
	 * .processengine.model.BusinessProcessModel)
	 */
	@Override
	protected LanguageModel getEmailLanguage(final NpsEmailProcessModel npsUpdateProcessModel)
	{
		return npsUpdateProcessModel.getOrder().getLanguage();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getCustomer(de.hybris.platform
	 * .processengine.model.BusinessProcessModel)
	 */
	@Override
	protected CustomerModel getCustomer(final NpsEmailProcessModel npsUpdateProcessModel)
	{
		return (CustomerModel) npsUpdateProcessModel.getOrder().getUser();
	}
}
