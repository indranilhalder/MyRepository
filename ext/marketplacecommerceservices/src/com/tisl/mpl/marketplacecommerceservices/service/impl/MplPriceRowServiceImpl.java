/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPriceRowDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplPriceRowService;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 */
public class MplPriceRowServiceImpl implements MplPriceRowService
{
	@Autowired
	private MplPriceRowDao mplPriceRowDao;
	@Autowired
	private ModelService modelService;
	@Autowired
	private CatalogVersionService catalogVersionService;
	private static final Logger LOG = Logger.getLogger(MplPriceRowServiceImpl.class);


	/**
	 * @return the mplPriceRowDao
	 */
	public MplPriceRowDao getMplPriceRowDao()
	{
		return mplPriceRowDao;
	}

	/**
	 * @param mplPriceRowDao
	 *           the mplPriceRowDao to set
	 */
	public void setMplPriceRowDao(final MplPriceRowDao mplPriceRowDao)
	{
		this.mplPriceRowDao = mplPriceRowDao;
	}



	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}



	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/*
	 * 
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplPriceRowService#getPriceRowDetail(de.hybris.platform.europe1
	 * .model.PriceRowModel)
	 * 
	 * @Javadoc Method to Retrieve Pricerow based on articleSKUID
	 * 
	 * @param articleSKUID
	 * 
	 * @return listOfPrice
	 */
	@Override
	public List<PriceRowModel> getPriceRowDetail(final String aticleSKUID)
	{
		final CatalogVersionModel catalogVersionModel = getCatalogVersion();
		final List<PriceRowModel> priceDetails = getMplPriceRowDao().getPriceRowDetail(catalogVersionModel, aticleSKUID);
		return priceDetails;
	}

	/*
	 * @Javadoc Method to Retrieve Pricerow based on articleSKUIDs
	 * 
	 * @param aticleSKUIDs
	 * 
	 * @return mopMap
	 */


	@Override
	public Map<String, PriceRowModel> getAllPriceRowDetail(final String aticleSKUIDs)
	{
		final CatalogVersionModel catalogVersionModel = getCatalogVersion();
		final List<PriceRowModel> priceDetails = getMplPriceRowDao().getAllPriceRowDetail(catalogVersionModel, aticleSKUIDs);
		final Map<String, PriceRowModel> mopMap = new HashMap<String, PriceRowModel>();
		for (final PriceRowModel priceRowModel : priceDetails)
		{
			mopMap.put(priceRowModel.getSellerArticleSKU(), priceRowModel);
		}

		return mopMap;
	}

	@Override
	public Map<String, PriceRowModel> getAllPriceRowDetail(final List<String> aticleSKUIDs)
	{
		final CatalogVersionModel catalogVersionModel = getCatalogVersion();
		final List<PriceRowModel> priceDetails = getMplPriceRowDao().getAllPriceRowDetail(catalogVersionModel, aticleSKUIDs);
		final Map<String, PriceRowModel> mopMap = new HashMap<String, PriceRowModel>();
		for (final PriceRowModel priceRowModel : priceDetails)
		{
			mopMap.put(priceRowModel.getSellerArticleSKU(), priceRowModel);
		}

		return mopMap;
	}


	/*
	 * 
	 * @Javadoc Method to Retrieve Pricerow with lowest price for a specific product based on articleSKUIDs with checking
	 * greater than 0 Stock,if Stock is available for any price row then it will return minimum price
	 * 
	 * @param ProductModel
	 * 
	 * @return PriceRow
	 */

	@Override
	public PriceRowModel getLeastPriceForProduct(final ProductModel product)
	{

		final List<PriceRowModel> priceList = new ArrayList<>();
		final List<String> uusids = new ArrayList<>();

		final CatalogVersionModel catalogVersionModel = getCatalogVersion();
		try
		{
			if (product != null)
			{
				final List<SellerInformationModel> productSellers = (List<SellerInformationModel>) product
						.getSellerInformationRelator();

				for (final SellerInformationModel seller : productSellers)
				{
					if (seller != null && seller.getSellerArticleSKU() != null)
					{
						uusids.add(seller.getSellerArticleSKU());
					}
				}

				for (final String ussid : uusids)
				{
					final PriceRowModel priceDetails = getMplPriceRowDao().getPriceRowDetailForSKUWithStockCheck(catalogVersionModel,
							ussid);
					if (priceDetails != null)
					{
						priceList.add(priceDetails);
					}
				}

				if (!priceList.isEmpty())
				{
					final BeanComparator fieldComparator = new BeanComparator(
							MarketplacecommerceservicesConstants.PRODUCT_PRICE_COLUMN);
					Collections.sort(priceList, fieldComparator);

					return priceList.get(0);
				}
				else
				{
					final PriceRowModel leastPrice = getMplPriceRowDao().getMinimumPriceForProduct(catalogVersionModel, product);
					if (leastPrice != null)
					{
						return leastPrice;
					}

				}

			}
			return null;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("Exception is : " + e);
			throw new EtailNonBusinessExceptions(e);
		}
		catch (final Exception e)
		{

			LOG.error("Exception is : " + e);
			throw new EtailNonBusinessExceptions(e);
		}
	}

	/**
	 * @return
	 *
	 */
	private CatalogVersionModel getCatalogVersion()
	{
		final CatalogVersionModel catalogVersionModel = getCatalogVersionService().getCatalogVersion(
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_ID,
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_VERSION);
		return catalogVersionModel;
	}

	/**
	 * @return the catalogVersionService
	 */
	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	/**
	 * @param catalogVersionService
	 *           the catalogVersionService to set
	 */
	public void seogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	/**
	 * @return the log
	 */
	public static Logger getLog()
	{
		return LOG;
	}

}
