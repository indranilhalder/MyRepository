/**
 *
 */
package com.hybris.oms.tata.daos;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.model.SellerMasterModel;


/**
 * @author techouts
 *
 */
public class DefaultSellerMasterModelDao implements SellerMasterModelDao
{

	private static final Logger LOG = Logger.getLogger(DefaultSellerMasterModelDao.class);
	private static final String FIND_SELLER_IDS = "SELECT {" + SellerMasterModel.PK + "},{" + SellerMasterModel.ID + "} FROM {"
			+ SellerMasterModel._TYPECODE + "}";

	private static final String MPL_SELLER_ISLPAWBEDIT = "SELECT {srm:" + SellerMasterModel.PK + "}" + " FROM {"
			+ SellerMasterModel._TYPECODE + " AS srm} " + "WHERE " + "{srm:" + SellerMasterModel.ID + "}=?id ";

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/**
	 * @return sellers
	 */
	@Override
	public List<String> getAllSellers()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_SELLER_IDS);
		final SearchResult<SellerMasterModel> searchResult = flexibleSearchService.search(query);
		final List<String> sellers = new ArrayList<String>();
		for (final SellerMasterModel model : searchResult.getResult())
		{
			sellers.add(model.getId());
		}
		return sellers;
	}


	/**
	 * @return sellers
	 */
	@Override
	public SellerMasterModel getSeller(final String sellerID)
	{

		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("In getSellerBysellerID - sellerID ***" + sellerID);
			}
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(MPL_SELLER_ISLPAWBEDIT);
			fQuery.addQueryParameter("id", sellerID);

			final List<SellerMasterModel> listOfData = flexibleSearchService.<SellerMasterModel> search(fQuery).getResult();
			return !listOfData.isEmpty() ? listOfData.get(0) : null;
		}
		catch (final Exception e)
		{
			LOG.error("�rror while searching for SellerMasterModel    for seller  id" + sellerID);
		}
		return null;

	}

}
