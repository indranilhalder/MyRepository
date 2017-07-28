package com.tisl.mpl.facade.product.impl;

import de.hybris.platform.core.model.JewelleryInformationModel;

import java.util.List;

import javax.annotation.Resource;

import com.tisl.mpl.facade.product.MplJewelleryFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;


/**
 * @author TCS
 */

public class MplJewelleryFacadeImpl implements MplJewelleryFacade
{
	@Resource(name = "mplJewelleryService")
	private MplJewelleryService mplJewelleryService;

	@Override
	public List<JewelleryInformationModel> getJewelleryInfoByUssid(final String ussid)
	{

		return mplJewelleryService.getJewelleryInfoByUssid(ussid);
	}



}
