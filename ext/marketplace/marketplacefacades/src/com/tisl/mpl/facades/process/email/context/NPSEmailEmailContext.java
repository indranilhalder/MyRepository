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

import com.tisl.mpl.core.model.OrderUpdateProcessModel;
import com.tisl.mpl.core.model.PcmProductVariantModel;


/**
 * @author TCS
 *
 */
public class NPSEmailEmailContext extends AbstractEmailContext<OrderUpdateProcessModel>
{

	private Converter<OrderModel, OrderData> orderConverter;
	private OrderData orderData;

	private static final String ORDER_CODE = "orderCode";
	private static final String PRODUCT_NAME = "productName";
	private static final String PRODUCT_IMG = "productImg";
	private static final String PRODUCT_SIZE = "productSize";
	private static final String QUANTITY = "quantity";
	private static final String SELLER_NAME = "sellerName";
	private static final String TOTALPRICE = "totalPrice";
	private static final String SHIPPINGCHARGE = "shippingCharge";
	private static final String COUPON_DISCOUNT = "couponDiscount";
	private static final String CUSTOMER_NAME = "customerName";


	@Override
	public void init(final OrderUpdateProcessModel orderUpdateProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(orderUpdateProcessModel, emailPageModel);
		final OrderModel order = orderUpdateProcessModel.getOrder();
		final String orderCode = (null != orderUpdateProcessModel.getOrder().getParentReference().getCode()) ? orderUpdateProcessModel
				.getOrder().getParentReference().getCode()
				: "";
		final AbstractOrderEntryModel orderEntry = (AbstractOrderEntryModel) orderUpdateProcessModel.getOrder().getEntries();
		if (orderEntry != null)
		{

			final AbstractOrderEntryModel orderEntryNumber = orderUpdateProcessModel.getOrder().getEntries().get(0);
			final ProductModel productInfo = orderEntryNumber.getProduct();
			final String productName = productInfo.getDescription();
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
			}

			if (productInfo.getPicture() != null)
			{

				final String productImg = productInfo.getPicture().getURL();
				put(PRODUCT_IMG, productImg);
			}

		}


		final Double totalPrice = order.getTotalPrice();
		final Double shippingCharge = order.getDeliveryCost();
		final AddressModel deliveryAddress = order.getDeliveryAddress();

		final Long quantity = orderEntry.getQuantity();
		final String sellerName = orderEntry.getSellerInfo();


		put(ORDER_CODE, orderCode);
		put(TOTALPRICE, totalPrice);
		put(SHIPPINGCHARGE, shippingCharge);
		put(CUSTOMER_NAME, deliveryAddress.getFirstname());

		put(QUANTITY, quantity);
		put(SELLER_NAME, sellerName);

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
