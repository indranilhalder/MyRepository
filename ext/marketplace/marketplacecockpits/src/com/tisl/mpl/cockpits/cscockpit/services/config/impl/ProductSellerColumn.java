package com.tisl.mpl.cockpits.cscockpit.services.config.impl;

import java.util.Collection;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;

import com.tisl.mpl.model.SellerInformationModel;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractProductCustomColumn;

public class ProductSellerColumn extends AbstractProductCustomColumn {

	@Override
	protected String getProductValue(ProductModel productModel, Locale arg1) {

		if(null!=productModel) {
			Collection<SellerInformationModel> sellerInfoList = productModel.getSellerInformationRelator();
			StringBuilder sellerNames=new StringBuilder();
			if(CollectionUtils.isNotEmpty(sellerInfoList)) {
				for(SellerInformationModel sellerInfo:sellerInfoList) {
					sellerNames.append(sellerInfo.getSellerName()).append(",");
				}
			}
			
			return sellerNames.substring(0, sellerNames.length()-1);

		}
		return null;
	}
}
