/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.MplShopByLookModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.ShopByLookDao;


/**
 * @author TCS
 *
 */
public class ShopByLookDaoImpl implements ShopByLookDao
{

	private static final Logger log = Logger.getLogger(BuyBoxDaoImpl.class.getName());


	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/**
	 * @param collectionId
	 *           ID or the name of the page
	 * @Desc This method fetches all data pertaining to the supplied collection id
	 * @return List<MplShopByLookModel>
	 */
	@Override
	public List<MplShopByLookModel> getAllShopByLookProducts(final String collectionId) throws EtailNonBusinessExceptions
	{

		if (null != collectionId && !collectionId.isEmpty())
		{
			final String queryString = "SELECT {bb.PK} FROM {" + MplShopByLookModel._TYPECODE + " AS bb}"

			+ " WHERE {bb:" + MplShopByLookModel.COLLECTIONID + "}=?collectionId";

			log.debug("queryString:" + queryString);

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("collectionId", collectionId.trim());
			return flexibleSearchService.<MplShopByLookModel> search(query).getResult();
		}
		else
		{
			return null;
		}

	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

}
