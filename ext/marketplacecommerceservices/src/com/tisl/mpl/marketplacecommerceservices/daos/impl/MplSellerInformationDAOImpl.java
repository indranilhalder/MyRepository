/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplSellerInformationDAO;
import com.tisl.mpl.model.RestrictionsetupModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerSalesCategoryModel;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "PMD" })
public class MplSellerInformationDAOImpl implements MplSellerInformationDAO
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	private static final Logger LOG = Logger.getLogger(MplSellerInformationDAOImpl.class);
	private static final String SELECT_CLASS = "SELECT {c:";
	private static final String FROM_CLASS = "FROM {";
	private static final String C_CLASS = "{c:";


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
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.MplSellerInformationDAO#getSellerInforationDetails(java.lang.String)
	 */
	@Override
	public SellerInformationModel getSellerInforationDetails(final String articleSKUID, final CatalogVersionModel catalogVersion)
	{
		try
		{
			//TISEE-5429 , TISEE-5458
			final String queryString = //
			SELECT_CLASS + SellerInformationModel.PK
					+ "}" //
					+ FROM_CLASS + SellerInformationModel._TYPECODE
					+ " AS c} "//
					+ "WHERE " + C_CLASS + SellerInformationModel.SELLERARTICLESKU + "}=?articleSKUID" + " AND " + C_CLASS
					+ SellerInformationModel.CATALOGVERSION + "}=?catalogVersion";

			final Map<String, Object> params = new HashMap<String, Object>(1);
			params.put("articleSKUID", articleSKUID);
			params.put(SellerInformationModel.CATALOGVERSION, catalogVersion);

			final SearchResult<SellerInformationModel> searchRes = flexibleSearchService.search(queryString, params);
			if (searchRes != null && searchRes.getCount() > 0)
			{
				return searchRes.getResult().get(0);
			}

			return null;

		}

		catch (final Exception ex)
		{
			LOG.debug(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.MplSellerInformationDAO#getSellerInforationDetails(java.lang.String)
	 */
	public List<SellerInformationModel> getSellerInformation(final String sellerID)
	{
		try
		{
			final String queryString = //
			SELECT_CLASS + SellerInformationModel.PK + "}" //
					+ FROM_CLASS + SellerInformationModel._TYPECODE + " AS c} "//
					+ "WHERE " + C_CLASS + SellerInformationModel.SELLERID + "}=?sellerID";

			final Map<String, Object> params = new HashMap<String, Object>(1);
			params.put(SellerInformationModel.SELLERID, sellerID);
			final SearchResult<SellerInformationModel> searchRes = flexibleSearchService.search(queryString, params);
			if (searchRes != null && searchRes.getCount() > 0)
			{
				return searchRes.getResult();
			}

			return null;

		}

		catch (final Exception ex)
		{
			LOG.debug(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}



	public RestrictionsetupModel beforesave(final String pinCode, final String listingID, final String primaryCatId,
			final String sellerID, final String ussid, final String shipmentMode, final String deliveryMode)
	{
		try
		{
			final Map<String, Object> params = new HashMap<String, Object>(1);

			String queryString = //
			SELECT_CLASS
					+ RestrictionsetupModel.PK
					+ "}" //
					+ FROM_CLASS + RestrictionsetupModel._TYPECODE + " AS c} WHERE {c:" + RestrictionsetupModel.PINCODE
					+ "}=?pinCode AND {c:" + RestrictionsetupModel.SHIPMENTMODE + "}=?shipmentMode AND {c:"
					+ RestrictionsetupModel.DELIVERYMODE + "}=?deliveryMode AND ";

			//SellerID and USSID
			if ((sellerID != null && ussid != null) && (!sellerID.isEmpty() && !ussid.isEmpty())
					&& (listingID == null || listingID.isEmpty()) && (primaryCatId == null || primaryCatId.isEmpty()))
			{
				queryString = queryString + C_CLASS + RestrictionsetupModel.SELLERID + "}=?sellerID  AND " + C_CLASS
						+ RestrictionsetupModel.USSID + "}=?ussid";
				params.put(RestrictionsetupModel.PINCODE, pinCode);
				params.put(RestrictionsetupModel.SELLERID, sellerID);
				params.put(RestrictionsetupModel.USSID, ussid);
				params.put(RestrictionsetupModel.SHIPMENTMODE, shipmentMode);
				params.put(RestrictionsetupModel.DELIVERYMODE, deliveryMode);

			}
			//PrimaryCategoryID and SellerID
			else if ((primaryCatId != null && sellerID != null) && (!sellerID.isEmpty() && !primaryCatId.isEmpty())
					&& (listingID == null || listingID.isEmpty() && (ussid == null || ussid.isEmpty())))
			{
				queryString = queryString + C_CLASS + RestrictionsetupModel.PRIMARYCATID + "}=?primaryCatId  AND " + C_CLASS
						+ RestrictionsetupModel.SELLERID + "}=?sellerID";
				params.put(RestrictionsetupModel.PINCODE, pinCode);
				params.put(RestrictionsetupModel.PRIMARYCATID, primaryCatId);
				params.put(RestrictionsetupModel.SELLERID, sellerID);
				params.put(RestrictionsetupModel.SHIPMENTMODE, shipmentMode);
				params.put(RestrictionsetupModel.DELIVERYMODE, deliveryMode);
			}
			//PrimaryCategoryID
			else if ((primaryCatId != null)
					&& (!primaryCatId.isEmpty())
					&& (listingID == null || listingID.isEmpty() && (ussid == null || ussid.isEmpty())
							&& (sellerID == null || sellerID.isEmpty())))
			{
				queryString = queryString + C_CLASS + RestrictionsetupModel.PRIMARYCATID + "}=?primaryCatId";
				params.put(RestrictionsetupModel.PINCODE, pinCode);
				params.put(RestrictionsetupModel.PRIMARYCATID, primaryCatId);
				params.put(RestrictionsetupModel.SHIPMENTMODE, shipmentMode);
				params.put(RestrictionsetupModel.DELIVERYMODE, deliveryMode);
			}
			//ListingID
			else if ((listingID != null)
					&& (!listingID.isEmpty())
					&& ((ussid == null || ussid.isEmpty()) && (sellerID == null || sellerID.isEmpty()) && (primaryCatId == null || primaryCatId
							.isEmpty())))
			{
				queryString = queryString + C_CLASS + RestrictionsetupModel.LISTINGID + "}=?listingID";
				params.put(RestrictionsetupModel.PINCODE, pinCode);
				params.put(RestrictionsetupModel.LISTINGID, listingID);
				params.put(RestrictionsetupModel.SHIPMENTMODE, shipmentMode);
				params.put(RestrictionsetupModel.DELIVERYMODE, deliveryMode);
			}
			else
			{
				LOG.error(">>>>>> Restriction data did not match criteria <<<<<<");
				return null;
			}

			LOG.debug("Restriction query string is >>>> " + queryString);

			final SearchResult<RestrictionsetupModel> searchRes = flexibleSearchService.search(queryString, params);// remove queryString.toString()
			if (searchRes != null && searchRes.getCount() > 0)
			{
				return searchRes.getResult().get(0);
			}

			return null;

		}

		catch (final Exception ex)
		{
			LOG.debug(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}



	/**
	 * @param pinCode
	 * @param primaryCatID
	 * @return
	 */
	public RestrictionsetupModel befSavePinCat(final String pinCode, final String primaryCatID)
	{
		// YTODO Auto-generated method stub
		try
		{
			final String queryString = //
			SELECT_CLASS + RestrictionsetupModel.PK
					+ "}" //
					+ FROM_CLASS + RestrictionsetupModel._TYPECODE
					+ " AS c} "//
					+ "WHERE " + C_CLASS + RestrictionsetupModel.PINCODE + "}=?pinCode AND " + C_CLASS
					+ RestrictionsetupModel.PRIMARYCATID + "}=?primaryCatID";

			final Map<String, Object> params = new HashMap<String, Object>(1);
			params.put(RestrictionsetupModel.PINCODE, pinCode);
			params.put(RestrictionsetupModel.PRIMARYCATID, primaryCatID);
			final SearchResult<RestrictionsetupModel> searchRes = flexibleSearchService.search(queryString, params);
			if (searchRes != null && searchRes.getCount() > 0)
			{
				return searchRes.getResult().get(0);
			}

			return null;

		}

		catch (final Exception ex)
		{
			LOG.debug(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
			throw new EtailNonBusinessExceptions(ex);
		}

	}

	@Override
	public SellerInformationModel getSellerInformationBySellerName(final CatalogVersionModel catalogVersion,
			final String sellerName)
	{
		try
		{
			//	final StringBuilder query = new StringBuilder("SELECT {sim." + SellerInformationModel.PK + "} ");
			final StringBuilder query = new StringBuilder();
			query.append("SELECT {sim." + SellerInformationModel.PK + "} ");
			query.append(FROM_CLASS + SellerInformationModel._TYPECODE + " AS sim} ");
			query.append("WHERE UPPER({sim." + SellerInformationModel.SELLERNAME + "}) = (?" + SellerInformationModel.SELLERNAME
					+ ")");
			query.append(" AND {sim." + SellerInformationModel.CATALOGVERSION + "} = (?" + SellerInformationModel.CATALOGVERSION
					+ ")");

			final Map<String, Object> params = new HashMap<String, Object>();
			params.put(SellerInformationModel.CATALOGVERSION, catalogVersion);
			params.put(SellerInformationModel.SELLERNAME, sellerName);

			final SearchResult<SellerInformationModel> searchRes = flexibleSearchService.search(query.toString(), params);
			if (searchRes != null && searchRes.getCount() > 0)
			{
				return searchRes.getResult().get(0);
			}
			else
			{
				return null;
			}
		}
		catch (final Exception ex)
		{
			LOG.debug(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}

	@Override
	public SellerSalesCategoryModel getActiveSellerRootCategoryBySellerId(final String sellerId)
	{
		//final StringBuilder query = new StringBuilder("SELECT {ss." + SellerSalesCategoryModel.PK + "} ");
		try
		{
			final StringBuilder query = new StringBuilder();
			query.append("SELECT {" + SellerSalesCategoryModel.PK + "} FROM {SellerSalesCategory} where upper({sellerId}) = upper('"
					+ sellerId + "') AND upper({active})='TRUE'");

			final SearchResult<SellerSalesCategoryModel> searchRes = flexibleSearchService.search(query.toString());

			if (searchRes != null && searchRes.getCount() > 0)
			{
				return searchRes.getResult().get(0);
			}
			else
			{
				return null;
			}
		}
		catch (final Exception ex)
		{
			LOG.debug(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.MplSellerInformationDAO#getSellerInformationWithSellerMaster(java
	 * .lang.String)
	 */
	@Override
	public SellerInformationModel getSellerInformationWithSellerMaster(final String sellerID)
	{

		try
		{
			final String queryString = //
			SELECT_CLASS + SellerInformationModel.PK
					+ "}" //
					+ FROM_CLASS + SellerInformationModel._TYPECODE
					+ " AS c} "//
					+ "WHERE " + C_CLASS + SellerInformationModel.SELLERID + "}=?sellerID and {" + SellerInformationModel.SELLERMASTER
					+ "} is null";

			final Map<String, Object> params = new HashMap<String, Object>(1);
			params.put(SellerInformationModel.SELLERID, sellerID);
			final SearchResult<SellerInformationModel> searchRes = flexibleSearchService.search(queryString, params);
			if (searchRes != null && searchRes.getCount() > 0)
			{
				return searchRes.getResult().get(0);
			}

			return null;

		}

		catch (final Exception ex)
		{
			LOG.debug(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}

}
