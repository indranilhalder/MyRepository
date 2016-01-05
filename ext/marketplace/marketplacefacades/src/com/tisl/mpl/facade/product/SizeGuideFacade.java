/**
 *
 */
package com.tisl.mpl.facade.product;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.facades.product.data.SizeGuideData;
import com.tisl.mpl.wsdto.SizeGuideWsDTO;


/**
 * @author TCS
 *
 */
public interface SizeGuideFacade
{
	public Map<String, List<SizeGuideData>> getProductSizeguide(final String productCode) throws CMSItemNotFoundException;

	public SizeGuideWsDTO getWSProductSizeguide(final String productCode) throws CMSItemNotFoundException;
}
