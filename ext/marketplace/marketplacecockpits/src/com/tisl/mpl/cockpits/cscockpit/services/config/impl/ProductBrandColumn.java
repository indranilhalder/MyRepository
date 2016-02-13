package com.tisl.mpl.cockpits.cscockpit.services.config.impl;

import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractProductCustomColumn;

public class ProductBrandColumn extends AbstractProductCustomColumn {

	protected String getBrandValue(ProductModel product, Locale locale) {
		if (product != null && CollectionUtils.isNotEmpty(product.getBrands())) {
			
		  return product.getBrands().iterator().next().getName();
			
			
		}		
		return "";
	}

	@Override
	protected String getProductValue(ProductModel product, Locale locale) {
		// TODO Auto-generated method stub
		return getBrandValue(product, locale);
	}
}
