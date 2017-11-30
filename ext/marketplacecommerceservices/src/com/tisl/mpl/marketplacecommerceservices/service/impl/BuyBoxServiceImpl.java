/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

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


	private ConfigurationService configurationService;

	//	@Resource
	//	private AgentIdForStore agentIdForStore;


	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
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
	public List<BuyBoxModel> buyboxPrice(final String productCode) throws EtailNonBusinessExceptions, EtailBusinessExceptions
	{

		final List<BuyBoxModel> buyBoxList = buyBoxDao.buyBoxPrice(productCode);
		return buyBoxList;
	}

	//CKD:TPR-250: Start
	@Override
	public List<BuyBoxModel> buyboxPriceForMicrosite(final String productCode, final String sellerId)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions
	{

		final List<BuyBoxModel> buyBoxList = buyBoxDao.buyboxPriceForMicrosite(productCode, sellerId);
		return buyBoxList;
	}

	//CKD:TPR-250: End

	/*
	 * This service method will return buybox prices for product code
	 *
	 * @param - productCode
	 *
	 * @return- buyBoxList
	 */
	@Override
	//added for jewellery
	public List<BuyBoxModel> buyboxPriceForJewellery(final String pcmUssid)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions
	{

		final List<BuyBoxModel> buyBoxList = buyBoxDao.buyBoxPriceForJewellery(pcmUssid);

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
	//CKD: TPR-3809
	//public Set<Map<BuyBoxModel, RichAttributeModel>> getsellersDetails(final String productCode)
	public Set<Map<BuyBoxModel, RichAttributeModel>> getsellersDetails(final String productCode, final String prodCatType)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions
	{
		//CKD: TPR-3809
		//final Set<Map<BuyBoxModel, RichAttributeModel>> resultMap = buyBoxDao.getsellersDetails(productCode);
		final Set<Map<BuyBoxModel, RichAttributeModel>> resultMap = buyBoxDao.getsellersDetails(productCode, prodCatType);
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

	/**
	 * This service method will return buybox data for product code and seller id
	 *
	 * @param productCode
	 * @param sellerId
	 * @return
	 * @throws EtailNonBusinessExceptions
	 * @throws EtailBusinessExceptions
	 */
	@Override
	public BuyBoxModel buyboxForSizeGuide(final String productCode, final String sellerId)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions
	{
		//Get Buybox data in respect of product code and seller id
		return buyBoxDao.buyBoxForSizeGuide(productCode, sellerId);
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

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService#getClassAttrAssignmentsForCode(java.lang.String)
	 */
	@Override
	public List<ClassAttributeAssignmentModel> getClassAttrAssignmentsForCode(final String code)
	{
		// YTODO Auto-generated method stub
		return buyBoxDao.getClassAttrAssignmentsForCode(code);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService#getBuyBoxDataForUssids(java.util.List)
	 */
	//TPR-3736
	@Override
	public Map<String, List<Double>> getBuyBoxDataForUssids(final String ussidList) throws EtailNonBusinessExceptions
	{
		// YTODO Auto-generated method stub
		return buyBoxDao.getBuyBoxDataForUssids(ussidList);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService#getBuyboxSellerPricesForSearch(java.util.List)
	 */
	//JEWELLERY
	@Override
	public List<BuyBoxModel> getBuyboxSellerPricesForSearch(final List<String> SellerArticleSKUList)
			throws EtailNonBusinessExceptions
	{
		return buyBoxDao.getBuyboxSellerPricesForSearch(SellerArticleSKUList);
	}

	//INC144315542_INC144314878_INC_11113
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService#getBuyboxPricesForSizeVariant(java.lang.String)
	 */
	@Override
	public List<BuyBoxModel> getBuyboxPricesForSizeVariant(final String productCode) throws EtailNonBusinessExceptions
	{
		// YTODO Auto-generated method stub
		return buyBoxDao.getBuyboxPricesForSizeVariant(productCode);// INC144314878_INC_11113
	}

	// TISPRD-8944
	@Override
	public List<BuyBoxModel> buyboxPriceMobile(final String productCode) throws EtailNonBusinessExceptions, EtailBusinessExceptions
	{
		final List<BuyBoxModel> buyBoxList = buyBoxDao.buyBoxPriceMobile(productCode);

		return buyBoxList;
	}

	/***
	 * TPR-5712 service to get price for all the sellers
	 */
	@Override
	public List<BuyBoxModel> buyboxPriceForAllSeller(final String productCode)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions
	{

		final List<BuyBoxModel> buyBoxList = buyBoxDao.buyBoxPriceForAllSeller(productCode);
		return buyBoxList;
	}

	@Override
	public ProductModel getProductDetailsByProductCode(final String productcode)
	{
		return buyBoxDao.getProductDetailsByProductCode(productcode);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService#buyboxPriceForJewelleryWithVariant(java.lang.String
	 * )
	 */
	@Override
	public List<BuyBoxModel> buyboxPriceForJewelleryWithVariant(final String ussID)
	{
		return buyBoxDao.buyboxPriceForJewelleryWithVariant(ussID);
	}

	@Override
	public String findPussid(final String selectedUSSID)
	{
		return buyBoxDao.findPussid(selectedUSSID);
	}

	/**
	 * This Method is for Seller Monogramming Message Changes
	 *
	 * @param sellerId
	 * @param productCode
	 * @return String
	 */
	@Override
	public String getSellerMonogrammingMsg(final String productCode, final String sellerId)
	{
		return buyBoxDao.getSellerMonogrammingMsg(productCode, sellerId);
	}
}
