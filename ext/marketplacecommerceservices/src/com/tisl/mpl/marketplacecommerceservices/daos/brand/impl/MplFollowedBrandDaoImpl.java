/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.brand.impl;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BrandMasterModel;
import com.tisl.mpl.core.model.FollowedBrandGenderModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.brand.MplFollowedBrandDao;


/**
 * @author TCS
 *
 */
public class MplFollowedBrandDaoImpl implements MplFollowedBrandDao
{

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.brand.MplFollowedBrandDao#getFollowedBrands(java.lang.String)
	 */

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<BrandMasterModel> getFollowedBrands(final String gender)
	{
		// YTODO Auto-generated method stub
		try
		{


			final String queryString = "SELECT {bm:" + BrandMasterModel.PK + "}" + "FROM {" + FollowedBrandGenderModel._TYPECODE
					+ " AS bg} , {" + BrandMasterModel._TYPECODE + " AS bm } " + "WHERE {bm:" + BrandMasterModel.BRANDCODE
					+ " } = {bg:" + FollowedBrandGenderModel.BRANDCODE + " } AND {bg:" + FollowedBrandGenderModel.GENDER
					+ "}=?gender ";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("gender", gender);

			final List<BrandMasterModel> listOfData = flexibleSearchService.<BrandMasterModel> search(query).getResult();
			return listOfData;
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final IllegalArgumentException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final NullPointerException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final UnknownIdentifierException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}

	}

}
