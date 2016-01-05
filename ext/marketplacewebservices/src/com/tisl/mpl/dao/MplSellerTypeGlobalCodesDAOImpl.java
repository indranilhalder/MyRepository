/**
 *
 */
package com.tisl.mpl.dao;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.model.SellerTypeGlobalCodesModel;


/**
 * @author TCS
 *
 */
public class MplSellerTypeGlobalCodesDAOImpl implements MplSellerTypeGlobalCodesDAO
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


	/*
	 * Returns a list of SellerTypeGlobalCodesModel objects (non-Javadoc)
	 *
	 * @see com.tisl.mpl.dao.MplSellerTypeGlobalCodesDAO#findSellerTypeDescriptionByCode(java.lang.String)
	 *
	 * @code
	 *
	 * @return
	 */
	@Override
	public List<SellerTypeGlobalCodesModel> findSellerTypeDescriptionByCode(final String code) throws EtailBusinessExceptions,
			EtailNonBusinessExceptions
	{
		// Build a query for the flexible search.
		final String queryString = "SELECT {p:" + SellerTypeGlobalCodesModel.PK + "}" //
				+ "FROM {" + SellerTypeGlobalCodesModel._TYPECODE + " AS p} "//
				+ "WHERE " + "{p:" + SellerTypeGlobalCodesModel.GLOBALCODE + "}=?code ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("code", code);

		// Return the list of SellerTypeGlobalCodesModel.
		return flexibleSearchService.<SellerTypeGlobalCodesModel> search(query).getResult();

	}

}
