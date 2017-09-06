/**
 *
 */
package com.tisl.mpl.util;

import de.hybris.platform.core.model.JewelleryInformationModel;

import java.util.List;

import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;


/**
 * @author TCS
 *
 */
public class MplJewelleryUtility
{

	private MplJewelleryService mplJewelleryService;

	/**
	 * @return the mplJewelleryService
	 */
	public MplJewelleryService getMplJewelleryService()
	{
		return mplJewelleryService;
	}

	/**
	 * @param mplJewelleryService
	 *           the mplJewelleryService to set
	 */
	public void setMplJewelleryService(final MplJewelleryService mplJewelleryService)
	{
		this.mplJewelleryService = mplJewelleryService;
	}

	/**
	 * @param code
	 * @return
	 */
	public List<JewelleryInformationModel> getJewelleryInformationList(final String productCode)
	{
		final List<JewelleryInformationModel> resultList = mplJewelleryService.getJewelleryUssid(productCode);
		return resultList;
	}
}
