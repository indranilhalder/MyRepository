/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplJewelleryDao;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;

import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.JewellerySellerDetailsModel;
import de.hybris.platform.core.model.JwlryRevSealInfoModel;


/**
 * @author TCS
 *
 */
public class MplJewelleryServiceImpl implements MplJewelleryService
{

	@Resource(name = "mplJewelleryDao")
	private MplJewelleryDao mplJewelleryDao;

	@Resource(name = "buyBoxService")
	private BuyBoxService buyBoxService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService#getJewelleryUssid(java.lang.String)
	 */
	@Override
	public List<JewelleryInformationModel> getJewelleryUssid(final String productCode)
	{

		return mplJewelleryDao.getJewelleryUssid(productCode);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService#getJewelleryInfoByUssid(java.lang.String)
	 */
	@Override
	public List<JewelleryInformationModel> getJewelleryInfoByUssid(final String ussid)
	{
		// YTODO Auto-generated method stub
		return mplJewelleryDao.getJewelleryInfoByUssid(ussid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService#getWeightVarientUssid(java.lang.String)
	 */
	@Override
	public List<String> getWeightVarientUssid(final String ussid)
	{
		// YTODO Auto-generated method stub
		return mplJewelleryDao.getWeightVarientUssid(ussid);
	}

	@Override
	public List<BuyBoxModel> getAllWeightVariant(final String ussid)
	{
		// YTODO Auto-generated method stub
		return mplJewelleryDao.getAllWeightVariant(ussid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService#getPanCardStatus(java.lang.String)
	 */
	//CKD:TPR-3809
	@Override
	public String getPanCardStatus(final String orderLineId)
	{
		return mplJewelleryDao.getPanCardStatus(orderLineId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService#getSellerMsgForRetRefTab(java.lang.String)
	 */
	@Override
	public List<JewellerySellerDetailsModel> getSellerMsgForRetRefTab(final String sellerId)
	{
		// YTODO Auto-generated method stub
		return mplJewelleryDao.getSellerMsgForRetRefTab(sellerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService#getSealInfo(java.lang.String)
	 */
	@Override
	public JwlryRevSealInfoModel getSealInfo(final String sellerId)
	{
		final List<JwlryRevSealInfoModel> sealList = mplJewelleryDao.getSealInfo(sellerId);
		if (CollectionUtils.isNotEmpty(sealList))
		{
			return sealList.get(0);
		}
		else
		{
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService#getAllWeightVariantByPussid(java.lang.String)
	 */
	@Override
	public List<BuyBoxModel> getAllWeightVariantByPussid(final String pUssid)
	{
		// YTODO Auto-generated method stub
		return mplJewelleryDao.getAllWeightVariantByPussid(pUssid);
	}


	@Override
	public String checkIfWeightVariantJewl(final String productCode)
	{
		String jewelleryVariant = MarketplacecommerceservicesConstants.Y;

		final List<BuyBoxModel> buyboxModelList = new ArrayList<BuyBoxModel>(buyBoxService.buyboxPrice(productCode));

		if (CollectionUtils.isNotEmpty(buyboxModelList))
		{
			final List<BuyBoxModel> bList = new ArrayList<BuyBoxModel>();
			if (buyboxModelList.size() == 1)
			{
				jewelleryVariant = MarketplacecommerceservicesConstants.N;
			}
			else
			{
				if (StringUtils.isNotEmpty(buyboxModelList.get(0).getSellerId()))
				{
					final String winningSeller = buyboxModelList.get(0).getSellerId();
					for (final BuyBoxModel bModel : buyboxModelList)
					{
						if (winningSeller.equals(bModel.getSellerId()))
						{
							bList.add(bModel);
						}
					}
					if (bList.size() == 1)
					{
						jewelleryVariant = MarketplacecommerceservicesConstants.N;
					}
				}
			}
		}
		return jewelleryVariant;
	}
}
