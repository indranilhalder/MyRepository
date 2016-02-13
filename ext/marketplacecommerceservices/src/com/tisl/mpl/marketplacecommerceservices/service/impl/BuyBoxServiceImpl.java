/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.BuyBoxDao;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;



public class BuyBoxServiceImpl implements BuyBoxService
{


	private BuyBoxDao buyBoxDao;

	/*
	 * This service method will return buybox prices for product code
	 *
	 * @param - productCode
	 *
	 * @return- buyBoxList
	 */
	@Override
	//	public List<BuyBoxModel> buyboxPrice(final String productCode) throws EtailNonBusinessExceptions
	public List<BuyBoxModel> buyboxPrice(final String productCode) throws EtailNonBusinessExceptions, EtailBusinessExceptions
	{

		final List<BuyBoxModel> buyBoxList = buyBoxDao.buyBoxPrice(productCode);



		return buyBoxList;
	}

	/*
	 * This service method will return buybox prices for product code
	 *
	 * @param - productCode
	 *
	 * @return- List<BuyBoxModel>
	 */

	@Override
	public List<BuyBoxModel> getBuyboxPricesForSearch(final String productCode) throws EtailNonBusinessExceptions
	{

		return buyBoxDao.getBuyboxPricesForSearch(productCode);
	}

	/*
	 * This service method will return buybox inventory for product code
	 *
	 * @param - productCode
	 */
	@Override
	public Integer getBuyboxInventoryForSearch(final String productCode, final String productType)
	{

		return buyBoxDao.getBuyboxAvailableInventoryForSearch(productCode, productType);
	}

	/*
	 * This service method will inavalidating pks of the buyboxbox sellers
	 *
	 * @param - productCode buyBoxDao.invalidatePkofBuybox(currenttime)
	 */
	@Override
	public List<BuyBoxModel> invalidatePkofBuybox(final Date currenttime) throws EtailNonBusinessExceptions
	{
		return buyBoxDao.invalidatePkofBuybox(currenttime);
	}



	@Required
	public void setBuyBoxDao(final BuyBoxDao buyBoxDao)
	{
		this.buyBoxDao = buyBoxDao;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService#buyBoxPriceNoStock(java.lang.String)
	 */
	@Override
	public List<BuyBoxModel> buyBoxPriceNoStock(final String productCode) throws EtailNonBusinessExceptions
	{
		final List<BuyBoxModel> buyBoxList = buyBoxDao.buyBoxPriceNoStock(productCode);
		return buyBoxList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService#getRichAttributeData(java.lang.String)
	 */
	@Override
	public RichAttributeModel getRichAttributeData(final String ussid) throws EtailNonBusinessExceptions, EtailBusinessExceptions
	{
		return buyBoxDao.getRichAttributeData(ussid);
	}

	@Override
	public Set<Map<BuyBoxModel, RichAttributeModel>> getsellersDetails(final String productCode)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions
	{
		final Set<Map<BuyBoxModel, RichAttributeModel>> resultMap = buyBoxDao.getsellersDetails(productCode);
		return resultMap;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService#getpriceForUssid(java.lang.String)
	 */
	@Override
	public BuyBoxModel getpriceForUssid(final String ussid)
	{
		final BuyBoxModel buyBox = buyBoxDao.priceForUssid(ussid);



		return buyBox;
	}

	/*
	 * This service method will return buybox prices for product code
	 *
	 * @param - productCode
	 *
	 * @return- buyBoxList
	 */
	@Override
	//	public List<BuyBoxModel> buyboxPrice(final String productCode) throws EtailNonBusinessExceptions
	public List<BuyBoxModel> buyboxForSizeGuide(final String productCode, final String sellerId)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions
	{

		final List<BuyBoxModel> buyBoxList = buyBoxDao.buyBoxForSizeGuide(productCode, sellerId);



		return buyBoxList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService#buyBoxStockForSeller(java.lang.String)
	 */
	@Override
	public List<BuyBoxModel> buyBoxStockForSeller(final String sellerID)
	{
		// YTODO Auto-generated method stub
		return buyBoxDao.buyBoxStockForSeller(sellerID);
	}

}