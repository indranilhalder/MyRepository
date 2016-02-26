/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.VoucherStatusNotificationModel;
import com.tisl.mpl.data.NotificationData;


/**
 * @author TCS
 *
 */
public class CustomNotificationCouponPopulator<SOURCE extends VoucherStatusNotificationModel, TARGET extends NotificationData>
		implements Populator<SOURCE, TARGET>
{


	private UrlResolver<ProductModel> productModelUrlResolver;

	protected UrlResolver<ProductModel> getProductModelUrlResolver()
	{
		return productModelUrlResolver;
	}

	@Required
	public void setProductModelUrlResolver(final UrlResolver<ProductModel> productModelUrlResolver)
	{
		this.productModelUrlResolver = productModelUrlResolver;
	}

	private UrlResolver<CategoryModel> categoryModelUrlResolver;

	protected UrlResolver<CategoryModel> getCategoryModelUrlResolver()
	{
		return categoryModelUrlResolver;
	}

	@Required
	public void setCategoryModelUrlResolver(final UrlResolver<CategoryModel> categoryModelUrlResolver)
	{
		this.categoryModelUrlResolver = categoryModelUrlResolver;
	}


	protected static final Logger LOG = Logger.getLogger(CustomNotificationCouponPopulator.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final SOURCE VoucherStatusNotificationModel, final TARGET notificationData) throws ConversionException
	{
		if (null != VoucherStatusNotificationModel)
		{
			notificationData.setCouponCode(VoucherStatusNotificationModel.getVoucherCode());
			notificationData.setNotificationRead(VoucherStatusNotificationModel.getIsRead());
			notificationData.setNotificationCreationDate(VoucherStatusNotificationModel.getVoucherStartDate());
			notificationData.setNotificationCustomerStatus(VoucherStatusNotificationModel.getCustomerStatus());
		}

	}
}
