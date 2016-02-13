package com.tisl.mpl.promotion.service;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.product.Product;

import java.util.Date;
import java.util.List;


/**
 * @author TCS
 *
 */
public interface UpdatePromotionalPriceService
{
	void updatePromotionalPrice(final List<Product> products, final List<Category> categories, final Double value,
			final Date startDate, final Date endtDate, final boolean percent, final Integer priority, final List<String> sellers,
			final List<String> brands);

	void disablePromotionalPrice(final List<Product> products, final List<Category> categories, final boolean isEnabled,
			final Integer priority, final List<String> brands);



}
