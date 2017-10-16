package com.tisl.mpl.facade.product.impl;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.JewellerySellerDetailsModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.facade.product.MplJewelleryFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.BuyBoxServiceImpl;


/**
 * @author TCS
 */

public class MplJewelleryFacadeImpl implements MplJewelleryFacade
{
	@Resource(name = "mplJewelleryService")
	private MplJewelleryService mplJewelleryService;

	@Resource(name = "buyBoxService")
	private BuyBoxServiceImpl buyBoxService;

	@Override
	public List<JewelleryInformationModel> getJewelleryInfoByUssid(final String ussid)
	{

		return mplJewelleryService.getJewelleryInfoByUssid(ussid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.facade.product.MplJewelleryFacade#getSellerMsgForRetRefTab(de.hybris.platform.commercefacades.product
	 * .data.ProductData)
	 */
	@Override
	public List<String> getSellerMsgForRetRefTab(final ProductData productData)
	{
		// YTODO Auto-generated method stub
		final List<String> descList = new ArrayList<String>();
		final List<BuyBoxModel> buyBoxList = buyBoxService.buyboxPrice(productData.getCode());
		if (CollectionUtils.isNotEmpty(buyBoxList))
		{
			final String sellerId = buyBoxList.get(0).getSellerId();
			if (StringUtils.isNotEmpty(sellerId))
			{
				final List<JewellerySellerDetailsModel> jewellerySellerDetails = mplJewelleryService
						.getSellerMsgForRetRefTab(sellerId);
				if (CollectionUtils.isNotEmpty(jewellerySellerDetails))
				{
					for (final JewellerySellerDetailsModel jsInfo : jewellerySellerDetails)
					{
						if (StringUtils.isNotEmpty(jsInfo.getDescription()))
						{
							descList.add(jsInfo.getDescription());
						}
					}
				}
			}
		}
		return descList;
	}

}
