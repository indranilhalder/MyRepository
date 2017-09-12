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
import org.apache.log4j.Logger;
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

	protected static final Logger LOG = Logger.getLogger(ExchangeGuideDaoImpl.class);

	private static final String logQuery = " ***********Query********* ";
	private static final String logQueryParam = " ***********QueryParameters********* ";
	private static final String SELECT_EXCHANGE = "SELECT {exchange.";
	private static final String CATEGORY_CODE = "categoryCode";
	private static final String WORKING = "working";
	private static final String FROM_EX = "FROM {";


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
			final String exchangeL3Query = "SELECT count(*)" + FROM_EX + ExchangeCouponValueModel._TYPECODE + " AS exchange"
					+ " JOIN " + CategoryModel._TYPECODE + " AS category " + "on {exchange.thirdLevelCategory}={category.pk} "
					+ "} where {category.code} =?categoryCode";


			final FlexibleSearchQuery flexExchangeL3Query = new FlexibleSearchQuery(exchangeL3Query);
			flexExchangeL3Query.addQueryParameter(CATEGORY_CODE, categoryCode);
			final List resultClassList = new ArrayList();
			resultClassList.add(Integer.class);
			flexExchangeL3Query.setResultClassList(resultClassList);
			//flexExchangeL3Query.setResultClassList(Arrays.asList(String.class));

			LOG.debug(logQuery + flexExchangeL3Query.getQuery() + logQueryParam + flexExchangeL3Query.getQueryParameters());
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
			final String queryString = SELECT_EXCHANGE + ExchangeCouponValueModel.PK + "} " + FROM_EX
					+ ExchangeCouponValueModel._TYPECODE + " AS exchange" + " JOIN " + CategoryModel._TYPECODE + " AS category "
					+ "on {exchange.thirdLevelCategory}={category.pk} " + "} where {category.code} =?categoryCode";


			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(CATEGORY_CODE, categoryCode);
			LOG.debug(logQuery + query.getQuery() + logQueryParam + query.getQueryParameters());
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

			LOG.debug(logQuery + query.getQuery() + logQueryParam + query.getQueryParameters());
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
			final String queryString = SELECT_EXCHANGE + ExchangeCouponValueModel.PK + "} " + FROM_EX
					+ ExchangeCouponValueModel._TYPECODE + " AS exchange" + " JOIN " + CategoryModel._TYPECODE + " AS category "
					+ "on {exchange.thirdLevelCategory}={category.pk} " + "} where {category.code} =?categoryCode "
					+ "and {exchange.l4categoryName}=?l4name " + "and {exchange.isWorking}=?working";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(CATEGORY_CODE, l3code);
			query.addQueryParameter("l4name", l4);
			if (isWorking.equalsIgnoreCase(WORKING))
			{
				query.addQueryParameter(WORKING, "1");
			}
			else
			{
				query.addQueryParameter(WORKING, "0");
			}
			LOG.debug(logQuery + query.getQuery() + logQueryParam + query.getQueryParameters());
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
		String exIdString = MarketplacecommerceservicesConstants.NON_EMPTY;
		if (exId.indexOf(MarketplacecommerceservicesConstants.COMMACONSTANT) > 0)
		{
			final String exidArray[] = exId.split(MarketplacecommerceservicesConstants.COMMACONSTANT);

			exIdString = MarketplacecommerceservicesConstants.INVERTED_COMMA + StringUtils.join(exidArray, "','")
					+ MarketplacecommerceservicesConstants.INVERTED_COMMA;
		}
		else
		{
			exIdString = MarketplacecommerceservicesConstants.INVERTED_COMMA + exId
					+ MarketplacecommerceservicesConstants.INVERTED_COMMA;
		}
		try
		{

			queryString.append(SELECT_EXCHANGE + ExchangeTransactionModel.PK + "} " + FROM_EX + ExchangeTransactionModel._TYPECODE
					+ " AS exchange }" + " where {exchange.exchangeid} in (");
			queryString.append(exIdString);
			queryString.append(')');//SONAR FIX JEWELLERY
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

			LOG.debug(logQuery + query.getQuery() + logQueryParam + query.getQueryParameters());
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



}