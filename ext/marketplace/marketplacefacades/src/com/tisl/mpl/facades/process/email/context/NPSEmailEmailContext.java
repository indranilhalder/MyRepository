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

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.NpsEmailProcessModel;
import com.tisl.mpl.core.model.PcmProductVariantModel;


/**
 * @author TCS
 *
 */
public class NPSEmailEmailContext extends AbstractEmailContext<NpsEmailProcessModel>
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


	@Override
	public void init(final NpsEmailProcessModel npsUpdateProcessModel, final EmailPageModel emailPageModel)
	{
		System.out.println("Inside NPS Email context");
		super.init(npsUpdateProcessModel, emailPageModel);
		final AbstractOrderEntryModel orderEntry = npsUpdateProcessModel.getAbstractOrderEntry();
		System.out.println("Inside NPS Email context" + orderEntry);
		if (orderEntry != null)
		{

			final ProductModel productInfo = orderEntry.getProduct();
			final String productName = productInfo.getName();
			put(PRODUCT_SIZE, productName);

			if (productInfo instanceof PcmProductVariantModel)
			{
				final PcmProductVariantModel variantProductModel = (PcmProductVariantModel) productInfo;
				final String productSize = variantProductModel.getSize();
				put(PRODUCT_NAME, productSize);
			}

			if (orderEntry.getCouponCode() != null)
			{
				final Double couponDiscount = orderEntry.getCouponValue();
				put(COUPON_DISCOUNT, couponDiscount);

				final Double totalNetAmount = new Double(orderEntry.getNetAmountAfterAllDisc().doubleValue()
						- orderEntry.getCouponValue().doubleValue());
				put(TOTAL_NET_AMOUNT, totalNetAmount);
			}

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

			final AddressModel deliveryAddress = orderEntry.getDeliveryAddress();
			final String customerName = deliveryAddress.getFirstname();
			put(CUSTOMER_NAME, customerName);
			final String orderCode = orderEntry.getOrder().getCode();
			put(ORDER_CODE, orderCode);
		}




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
	protected BaseSiteModel getSite(final NpsEmailProcessModel npsbusinessProcessModel)
	{
		return npsbusinessProcessModel.getOrder().getSite();
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
	protected LanguageModel getEmailLanguage(final NpsEmailProcessModel npsbusinessProcessModel)
	{
		return npsbusinessProcessModel.getOrder().getLanguage();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getCustomer(de.hybris.platform
	 * .processengine.model.BusinessProcessModel)
	 */
	@Override
	protected CustomerModel getCustomer(final NpsEmailProcessModel businessProcessModel)
	{
		return (CustomerModel) businessProcessModel.getOrder().getUser();
	}
}
