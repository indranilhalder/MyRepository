/**
 *
 */
package com.tisl.mpl.facade.compare;

import com.tisl.mpl.facades.product.data.ProductCompareData;


/**
 * @author 584443
 *
 */
public interface MplProductCompareFacade
{
	ProductCompareData getWebServicesForProductCompare(String firstProductCode, String secondProductCode);
}
