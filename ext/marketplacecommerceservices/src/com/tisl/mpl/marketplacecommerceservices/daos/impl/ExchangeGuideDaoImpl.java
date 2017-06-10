/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.ExchangeCouponValueModel;
import com.tisl.mpl.core.model.ExchangePincodeModel;
import com.tisl.mpl.core.model.ExchangeTransactionModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.ExchangeGuideDao;


/**
 * @author TCS
 *
 */
public class ExchangeGuideDaoImpl implements ExchangeGuideDao
{



	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/*
	 * @Javadoc
	 *
	 * @returns All L4 for which Exchange is Applicable
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.ExchangeGuideDao#getDistinctL4()
	 */

	@Override
	public boolean isExchangable(final String categoryCode)
	{
		boolean isExchangable = false;
		try
		{
			final StringBuffer exchangeL3Query = new StringBuffer(500);
			exchangeL3Query.append("SELECT count(*)" + "FROM {" + ExchangeCouponValueModel._TYPECODE + " AS exchange" + " JOIN "
					+ CategoryModel._TYPECODE + " AS category " + "on {exchange.thirdLevelCategory}={category.pk} "
					+ "} where {category.code} =?categoryCode");


			final FlexibleSearchQuery flexExchangeL3Query = new FlexibleSearchQuery(exchangeL3Query.toString());
			flexExchangeL3Query.addQueryParameter("categoryCode", categoryCode);
			final List resultClassList = new ArrayList();
			resultClassList.add(Integer.class);
			flexExchangeL3Query.setResultClassList(resultClassList);
			//flexExchangeL3Query.setResultClassList(Arrays.asList(String.class));

			final List<Integer> categoryListcount = flexibleSearchService.<Integer> search(flexExchangeL3Query).getResult();

			if (CollectionUtils.isNotEmpty(categoryListcount))
			{
				if (categoryListcount.get(0).intValue() > 0)
				{
					isExchangable = true;
				}
			}
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return isExchangable;
	}

	/**
	 *
	 * @param categoryCode
	 * @return List<ExchangeCouponValueModel>
	 * @description Gets All Exchange Applicable L4's for L3
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.SizeGuideDao#getsizeGuideByCode(java.lang.String)
	 */

	@Override
	public List<ExchangeCouponValueModel> getExchangeOptionforCategorycode(final String categoryCode)
	{

		try
		{
			final String queryString = "SELECT {exchange." + ExchangeCouponValueModel.PK + "} " + "FROM {"
					+ ExchangeCouponValueModel._TYPECODE + " AS exchange" + " JOIN " + CategoryModel._TYPECODE + " AS category "
					+ "on {exchange.thirdLevelCategory}={category.pk} " + "} where {category.code} =?categoryCode";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("categoryCode", categoryCode);
			return flexibleSearchService.<ExchangeCouponValueModel> search(query).getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

	}

	/**
	 * @param pincode
	 * @return boolean value
	 * @description Checks whether a pincode is Exchange serviceable
	 */
	@Override
	public boolean isBackwardServiceble(final String pincode)
	{
		try
		{
			final String queryString = "SELECT {expin.pk} FROM {ExchangePincode as expin } where {expin.pincode} =?pincode";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("pincode", pincode);

			final List<ExchangePincodeModel> serviceableResultList = flexibleSearchService.<ExchangePincodeModel> search(query)
					.getResult();
			if (CollectionUtils.isNotEmpty(serviceableResultList))
			{
				return serviceableResultList.get(0).isIsServicable();
			}
			else
			{
				return false;
			}
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 *
	 * @param l3code
	 * @param l4
	 * @param isWorking
	 * @return List<ExchangeCouponValueModel>
	 * @description Gets ExchangeCouponValueModel for Linking with Temporary Transaction Table
	 */

	@Override
	public List<ExchangeCouponValueModel> getPriceMatrix(final String l3code, final String l4, final String isWorking)
	{
		try
		{
			final String queryString = "SELECT {exchange." + ExchangeCouponValueModel.PK + "} " + "FROM {"
					+ ExchangeCouponValueModel._TYPECODE + " AS exchange" + " JOIN " + CategoryModel._TYPECODE + " AS category "
					+ "on {exchange.thirdLevelCategory}={category.pk} " + "} where {category.code} =?categoryCode "
					+ "and {exchange.l4categoryName}=?l4name " + "and {exchange.isWorking}=?working";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("categoryCode", l3code);
			query.addQueryParameter("l4name", l4);
			if (isWorking.equalsIgnoreCase("Working"))
			{
				query.addQueryParameter("working", "1");
			}
			else
			{
				query.addQueryParameter("working", "0");
			}
			return flexibleSearchService.<ExchangeCouponValueModel> search(query).getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.ExchangeGuideDao#changePincode(java.lang.String)
	 */

	@Override
	public List<ExchangeTransactionModel> getTeporaryExchangeModelforId(final String exId)
	{
		final StringBuilder queryString = new StringBuilder(500);
		String exIdString = "";
		if (exId.indexOf(',') > 0)
		{
			final String exidArray[] = exId.split(",");

			exIdString = "'" + StringUtils.join(exidArray, "','") + "'";
		}
		else
		{
			exIdString = exId;
		}
		try
		{

			queryString.append("SELECT {exchange." + ExchangeTransactionModel.PK + "} " + "FROM {"
					+ ExchangeTransactionModel._TYPECODE + " AS exchange }" + " where {exchange.exchangeid} in (");
			queryString.append(exIdString);
			queryString.append(")");
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);


			return flexibleSearchService.<ExchangeTransactionModel> search(query).getResult();


		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.ExchangeGuideDao#addToExchangeTable(com.tisl.mpl.core.model.
	 * ExchangeTransactionModel)
	 */
	@Override
	public boolean addToExchangeTable(final ExchangeTransactionModel ex)
	{
		// YTODO Auto-generated method stub
		return false;
	}



}