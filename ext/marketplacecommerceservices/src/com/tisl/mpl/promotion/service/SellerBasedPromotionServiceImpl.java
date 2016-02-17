/**
 *
 */
package com.tisl.mpl.promotion.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;

import java.util.List;

import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.promotion.dao.SellerBasedPromotionDao;


/**
 * @author TCS
 *
 */
public class SellerBasedPromotionServiceImpl implements SellerBasedPromotionService
{

	private SellerBasedPromotionDao sellerBasedPromotionDao;

	/**
	 * @return the sellerBasedPromotionDao
	 */
	public SellerBasedPromotionDao getSellerBasedPromotionDao()
	{
		return sellerBasedPromotionDao;
	}

	/**
	 * @param sellerBasedPromotionDao
	 *           the sellerBasedPromotionDao to set
	 */
	public void setSellerBasedPromotionDao(final SellerBasedPromotionDao sellerBasedPromotionDao)
	{
		this.sellerBasedPromotionDao = sellerBasedPromotionDao;
	}

	/**
	 * @Description : For Fetching the Seller Information Data Corresponding to Seller USSID
	 * @param : code,oModel
	 */
	@Override
	public List<SellerInformationModel> fetchSellerInformation(final String code, final CatalogVersionModel oModel)
	{
		return getSellerBasedPromotionDao().fetchSellerInformation(code, oModel);
	}

	/**
	 * @Description : For Fetching the Price Information Data Corresponding to Seller USSID
	 * @param : code,oModel
	 */
	@Override
	public List<PriceRowModel> fetchPriceInformation(final String code, final CatalogVersionModel oModel)
	{
		return getSellerBasedPromotionDao().fetchPriceInformation(code, oModel);
	}

	/**
	 * @Description : Fetch Promotion Details Corresponding to a code
	 * @param : code
	 */
	@Override
	public List<AbstractPromotionModel> fetchPromotionDetails(final String code)
	{
		return getSellerBasedPromotionDao().fetchPromotionDetails(code);
	}

}
