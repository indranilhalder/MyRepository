/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplCustomerProfileDao;


/**
 *
 * @author TCS
 *
 */
public class MplCustomerProfileDaoImpl implements MplCustomerProfileDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.MplCustomerProfileDao#getCustomerProfileDetail(de.hybris.platform
	 * .core.model.user.CustomerModel)
	 */
	@Override
	public List<CustomerModel> getCustomerProfileDetail(final CustomerModel oCustomerModel)
	{
		try
		{
			final String originalUid = oCustomerModel.getOriginalUid();

			final String queryString = //
			"SELECT {c:" + CustomerModel.PK + "}" //
					+ "FROM {" + CustomerModel._TYPECODE + " AS c} "//
					+ "WHERE " + "{c:" + CustomerModel.ORIGINALUID + "}=?code ";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("code", originalUid);

			final List<CustomerModel> listOfData = flexibleSearchService.<CustomerModel> search(query).getResult();
			return listOfData;
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
