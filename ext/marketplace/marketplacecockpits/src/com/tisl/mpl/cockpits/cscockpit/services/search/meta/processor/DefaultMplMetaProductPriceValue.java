/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.services.search.meta.processor;

import de.hybris.platform.cscockpit.services.search.meta.data.impl.DefaultMetaProductPriceValue;
import de.hybris.platform.util.PriceValue;

/**
 * @author 890223
 *
 */
public class DefaultMplMetaProductPriceValue extends
		DefaultMetaProductPriceValue  implements MplMetaProductPriceValue{

	private final String ussid ;
	private final String sellerName ;
	
	public DefaultMplMetaProductPriceValue(PriceValue productPriceValue,String ussid,String sellerName) {
		super(productPriceValue);
		this.ussid = ussid;
		this.sellerName = sellerName;
	}

	@Override
	
	public String getUssid() {
		return ussid;
	}
	@Override
	public String getSellerName() {
		return sellerName;
	}

	
}
