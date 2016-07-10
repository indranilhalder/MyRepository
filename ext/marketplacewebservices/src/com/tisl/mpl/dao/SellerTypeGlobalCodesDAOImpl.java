/**
 *
 */
package com.tisl.mpl.dao;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.model.SellerTypeGlobalCodesModel;


/**
 * @author TCS
 *
 */
public class SellerTypeGlobalCodesDAOImpl implements SellerTypeGlobalCodesDAO
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.dao.SellerTypeGlobalCodesDao#findSellerTypeDescriptionByCode(java.lang.String)
	 */
	@Autowired
	private FlexibleSearchService flexibleSearchService;



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



	@Override
	public List<SellerTypeGlobalCodesModel> findSellerTypeDescriptionByCode(final String code)
	{
		// YTODO Auto-generated method stub


		/**
		 * Finds all Stadiums by performing a FlexibleSearch using the {@link FlexibleSearchService}.
		 */

		// Build a query for the flexible search.
		final String queryString = "SELECT {p:" + SellerTypeGlobalCodesModel.PK + "}" //
				+ "FROM {" + SellerTypeGlobalCodesModel._TYPECODE + " AS p} "//
				+ "WHERE " + "{p:" + SellerTypeGlobalCodesModel.GLOBALCODE + "}=?code ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("code", code);
		// Note that we could specify paginating logic by providing a start and count variable (commented out below)
		// This can provide a safeguard against returning very large amounts of data, or hogging the database when there are
		// for example millions of items being returned.
		// As we know that there are only a few persisted stadiums in this use case we do not need to provide this.

		//query.setStart(start);
		//query.setCount(count);

		// Return the list of StadiumModels.
		return flexibleSearchService.<SellerTypeGlobalCodesModel> search(query).getResult();

	}

}
