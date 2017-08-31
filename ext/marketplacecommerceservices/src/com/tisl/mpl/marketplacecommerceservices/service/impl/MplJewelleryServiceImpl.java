/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.JewellerySellerDetailsModel;

import java.util.List;

import javax.annotation.Resource;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplJewelleryDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;


/**
 * @author TCS
 *
 */
public class MplJewelleryServiceImpl implements MplJewelleryService
{

	@Resource(name = "mplJewelleryDao")
	private MplJewelleryDao mplJewelleryDao;

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

}
