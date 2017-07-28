/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.JewelleryPriceRowModel;

import java.util.List;



/**
 * @author TCS
 *
 */
public interface PriceBreakupDao
{
	public List<JewelleryPriceRowModel> getPricebreakup(final String ussid);

	public List<JewelleryInformationModel> getJewelInfo(final String ussid);

}
