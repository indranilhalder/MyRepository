/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.services.config.impl;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.core.model.PcmProductVariantModel;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cscockpit.services.config.impl.ProductDescriptionColumn;

/**
 * @author 1006687
 *
 */
public class MarketplaceProductDescriptionColumn extends
		ProductDescriptionColumn {

	@Override
	protected String getProductValue(ProductModel product, Locale locale) {
		if (null != product && product instanceof PcmProductVariantModel) {
			PcmProductVariantModel pcmProductVariantModel=(PcmProductVariantModel)product;
			StringBuilder description=new StringBuilder();
			if(StringUtils.isNotBlank(pcmProductVariantModel.getColour())){
				description.append("Colour: ");
				description.append(pcmProductVariantModel.getColour());
				description.append(",");
			}
			if(StringUtils.isNotBlank(pcmProductVariantModel.getSize())){
				description.append("Size: ");
				description.append(pcmProductVariantModel.getSize());
				description.append(",");
			}
			if(StringUtils.isNotBlank(pcmProductVariantModel.getCapacity())){
				description.append("Capacity: ");
				description.append(pcmProductVariantModel.getCapacity());
				description.append(",");
			}
			
			return description.toString();
		}

		return StringUtils.EMPTY;
	}

}
