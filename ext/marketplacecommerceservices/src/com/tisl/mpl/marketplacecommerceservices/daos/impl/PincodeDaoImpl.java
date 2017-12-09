/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.exception.PointOfServiceDaoException;
import de.hybris.platform.storelocator.impl.GeometryUtils;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.PincodeDao;


/**
 * @author Techouts
 *
 */
@Component(value = "pincodeDao")
public class PincodeDaoImpl implements PincodeDao
{


	/**
	 *
	 */
	private static final String ACTIVE = "active";




	/**
	 *
	 */
	private static final String WHERE = "} WHERE {";




	/**
	 *
	 */
	private static final String SELECT_PK_FROM = "SELECT {PK} FROM {";




	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;




	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PincodeDaoImpl.class);

	private final String LATITUDE = "latitude";
	private final String LONGITUDE = "longitude";
	private final String IS_NOT_NULL = "} is not null AND {";

	private final String SELECT_CLASS = "select {";
	private final String FROM_CLASS = "} from {";
	private final String WHERE_CLASS = "} where {";


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.PincodeDao#getAllGeocodedPOS(de.hybris.platform.storelocator.GPS,
	 * double)
	 */
	@Override
	public Collection<PointOfServiceModel> getAllGeocodedPOS(final GPS center, final double radius)
	{

		LOG.debug("center for given Pincode:" + center.getDecimalLatitude() + "---" + center.getDecimalLongitude());
		final FlexibleSearchQuery fQuery = buildQuery(center, radius);
		LOG.debug("FlexibleSearchQuery :" + fQuery);
		final SearchResult result = this.flexibleSearchService.search(fQuery);
		return result.getResult();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.PincodeDao#getAllGeocodedPOS(de.hybris.platform.storelocator.GPS,
	 * double)
	 */
	@Override
	public Collection<PointOfServiceModel> getAllGeocodedPOS(final GPS center, final double radius, final String sellerId)
	{

		LOG.debug("center for given Pincode:" + center.getDecimalLatitude() + "---" + center.getDecimalLongitude());
		final FlexibleSearchQuery fQuery = buildQuery(center, radius, sellerId);
		LOG.debug("FlexibleSearchQuery :" + fQuery);
		final SearchResult result = this.flexibleSearchService.search(fQuery);
		return result.getResult();
	}

	@Override
	public PincodeModel getLatAndLongForPincode(final String pincode)
	{
		/*
		 * PincodeModel pincodeModel = null;
		 *///Sonar Issue
		try
		{
			final StringBuilder query = new StringBuilder(50);
			query.append(SELECT_PK_FROM.intern()).append(PincodeModel._TYPECODE).append(WHERE.intern()).append("pincode")
					.append("} = ?pincode");
			LOG.debug(" Query for Pincode Service:" + query.toString());
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
			fQuery.addQueryParameter("pincode", pincode);
			final SearchResult result = this.flexibleSearchService.search(fQuery);
			LOG.debug("Result for Pincode  Service:" + result.getResult());
			if (CollectionUtils.isNotEmpty(result.getResult()))
			{
				return (PincodeModel) result.getResult().get(0);
			}
			return null;
		}
		catch (final FlexibleSearchException e)
		{
			throw new FlexibleSearchException("Could not fetch pincode from database, due to :" + e.getMessage());
		}
	}

	/**
	 * @param center
	 * @param radius
	 * @return
	 */
	protected FlexibleSearchQuery buildQuery(final GPS center, final double radius, final String sellerId)
			throws PointOfServiceDaoException
	{
		try
		{
			final List corners = GeometryUtils.getSquareOfTolerance(center, radius);
			if ((corners == null) || (corners.isEmpty()) || (corners.size() != 2))
			{
				throw new PointOfServiceDaoException("Could not fetch locations from database. Unexpected neighborhood");
			}
			final Double latMax = Double.valueOf(((GPS) corners.get(1)).getDecimalLatitude());
			final Double lonMax = Double.valueOf(((GPS) corners.get(1)).getDecimalLongitude());
			final Double latMin = Double.valueOf(((GPS) corners.get(0)).getDecimalLatitude());
			final Double lonMin = Double.valueOf(((GPS) corners.get(0)).getDecimalLongitude());
			final StringBuilder query = new StringBuilder(280);
			query.append(SELECT_PK_FROM.intern()).append(GeneratedBasecommerceConstants.TC.POINTOFSERVICE).append(WHERE.intern())
					.append(LATITUDE).append(IS_NOT_NULL).append(LONGITUDE).append(IS_NOT_NULL).append(LATITUDE)
					.append("} >= ?latMin AND {").append(LATITUDE).append("} <= ?latMax AND {").append(LONGITUDE)
					.append("} >= ?lonMin AND {").append(LONGITUDE).append("} <= ?lonMax ").append(" AND {sellerid")
					.append("} = ?sellerId AND {").append("clicknCollect").append("} = ?clicknCollect AND  { active ")
					.append("} = ?active");
			//
			LOG.debug("Query for get SlaveIds from PointofService :" + query.toString());
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
			fQuery.addQueryParameter("latMax", latMax);
			fQuery.addQueryParameter("latMin", latMin);
			fQuery.addQueryParameter("lonMax", lonMax);
			fQuery.addQueryParameter("lonMin", lonMin);
			fQuery.addQueryParameter("sellerId", sellerId);
			fQuery.addQueryParameter("clicknCollect", MarketplacecommerceservicesConstants.CLICK_N_COLLECT);
			fQuery.addQueryParameter(ACTIVE.intern(), MarketplacecommerceservicesConstants.ACTIVE);
			return fQuery;
		}
		catch (final GeoLocatorException e)
		{
			throw new PointOfServiceDaoException("Could not fetch locations from database, due to :" + e.getMessage(), e);
		}
	}

	/**
	 * @param center
	 * @param radius
	 * @return fetch PointOfService for GPS and radius
	 */
	protected FlexibleSearchQuery buildQuery(final GPS center, final double radius) throws PointOfServiceDaoException
	{
		try
		{
			final List corners = GeometryUtils.getSquareOfTolerance(center, radius);
			if ((corners == null) || (corners.isEmpty()) || (corners.size() != 2))
			{
				throw new PointOfServiceDaoException("Could not fetch locations from database. Unexpected neighborhood");
			}
			final Double latMax = Double.valueOf(((GPS) corners.get(1)).getDecimalLatitude());
			final Double lonMax = Double.valueOf(((GPS) corners.get(1)).getDecimalLongitude());
			final Double latMin = Double.valueOf(((GPS) corners.get(0)).getDecimalLatitude());
			final Double lonMin = Double.valueOf(((GPS) corners.get(0)).getDecimalLongitude());
			final StringBuilder query = new StringBuilder(200);
			query.append(SELECT_PK_FROM.intern()).append(GeneratedBasecommerceConstants.TC.POINTOFSERVICE).append(WHERE.intern())
					.append(LATITUDE).append(IS_NOT_NULL).append(LONGITUDE).append(IS_NOT_NULL).append(LATITUDE)
					.append("} >= ?latMin AND {").append(LATITUDE).append("} <= ?latMax AND {").append(LONGITUDE)
					.append("} >= ?lonMin AND {").append(LONGITUDE).append("} <= ?lonMax ");
			//
			LOG.debug("Query for get SlaveIds from PointofService :" + query.toString());
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
			fQuery.addQueryParameter("latMax", latMax);
			fQuery.addQueryParameter("latMin", latMin);
			fQuery.addQueryParameter("lonMax", lonMax);
			fQuery.addQueryParameter("lonMin", lonMin);
			return fQuery;
		}
		catch (final GeoLocatorException e)
		{
			throw new PointOfServiceDaoException("Could not fetch locations from database, due to :" + e.getMessage(), e);
		}
	}

	/**
	 * Get the Details of the given pincode
	 *
	 * @param pincode
	 * @return PincodeModel
	 */
	@Override
	public List<PincodeModel> getAllDetailsOfPincode(final String pincode)
	{
		try
		{
			final String query = SELECT_CLASS + PincodeModel.PK + FROM_CLASS + PincodeModel._TYPECODE + WHERE_CLASS
					+ PincodeModel.PINCODE + "}=?pincode AND {" + PincodeModel.ACTIVE + "}" + "=?active";
			final FlexibleSearchQuery flexQuery = new FlexibleSearchQuery(query);
			flexQuery.addQueryParameter("pincode", pincode);
			flexQuery.addQueryParameter(ACTIVE.intern(), "Y");
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Pincode Query String ::::::::: " + query);
			}
			final SearchResult<PincodeModel> result = flexibleSearchService.search(flexQuery);
			if (null != result && null != result.getResult())
			{
				return result.getResult();
			}

		}
		catch (final FlexibleSearchException exception)
		{
			LOG.error(exception.getMessage());
			throw new EtailNonBusinessExceptions(exception, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage());
			throw ex;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @param sellerId
	 * @param center
	 * @param radius
	 * @return FlexibleSearchQuery
	 */
	protected FlexibleSearchQuery buildQueryForRetrunableStores(final GPS center, final double radius, final String sellerId)
			throws PointOfServiceDaoException
	{
		try
		{
			final List corners = GeometryUtils.getSquareOfTolerance(center, radius);
			if ((corners == null) || (corners.isEmpty()) || (corners.size() != 2))
			{
				throw new PointOfServiceDaoException("Could not fetch locations from database. Unexpected neighborhood");
			}
			final Double latMax = Double.valueOf(((GPS) corners.get(1)).getDecimalLatitude());
			final Double lonMax = Double.valueOf(((GPS) corners.get(1)).getDecimalLongitude());
			final Double latMin = Double.valueOf(((GPS) corners.get(0)).getDecimalLatitude());
			final Double lonMin = Double.valueOf(((GPS) corners.get(0)).getDecimalLongitude());
			final StringBuilder query = new StringBuilder(280);
			query.append(SELECT_PK_FROM.intern()).append(GeneratedBasecommerceConstants.TC.POINTOFSERVICE).append(WHERE.intern())
					.append(LATITUDE).append(IS_NOT_NULL).append(LONGITUDE).append(IS_NOT_NULL).append(LATITUDE)
					.append("} >= ?latMin AND {").append(LATITUDE).append("} <= ?latMax AND {").append(LONGITUDE)
					.append("} >= ?lonMin AND {").append(LONGITUDE).append("} <= ?lonMax ").append(" AND {sellerid")
					.append("} = ?sellerId AND {").append(ACTIVE.intern())
					.append("} = ?active AND {isReturnable}=?isReturnToStoreAllowed");
			//
			LOG.debug("Query for get SlaveIds from PointofService :" + query.toString());
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
			fQuery.addQueryParameter("latMax", latMax);
			fQuery.addQueryParameter("latMin", latMin);
			fQuery.addQueryParameter("lonMax", lonMax);
			fQuery.addQueryParameter("lonMin", lonMin);
			fQuery.addQueryParameter("sellerId", sellerId);
			fQuery.addQueryParameter("isReturnToStoreAllowed", MarketplacecommerceservicesConstants.RETURNABLE);
			/* fQuery.addQueryParameter("clicknCollect", MarketplacecommerceservicesConstants.CLICK_N_COLLECT); */
			fQuery.addQueryParameter(ACTIVE.intern(), MarketplacecommerceservicesConstants.ACTIVE);
			return fQuery;
		}
		catch (final GeoLocatorException e)
		{
			throw new PointOfServiceDaoException("Could not fetch locations from database, due to :" + e.getMessage(), e);
		}
	}

	/**
	 *
	 * Get All Returnable POS for Seller
	 *
	 * @param center
	 * @param radius
	 * @param sellerId
	 *
	 * @return Collection<PointOfServiceModel>
	 *
	 */
	@Override
	public Collection<PointOfServiceModel> getAllReturnablePOSforSeller(final GPS center, final double radius,
			final String sellerId) throws EtailNonBusinessExceptions
	{
		LOG.debug("center for given Pincode:" + center.getDecimalLatitude() + "---" + center.getDecimalLongitude());
		final FlexibleSearchQuery fQuery = buildQueryForRetrunableStores(center, radius, sellerId);
		LOG.debug("FlexibleSearchQuery :" + fQuery);
		try
		{
			final SearchResult result = this.flexibleSearchService.search(fQuery);
			return result.getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}

	}

	/**
	 * This method wrapped POS data for given selllerId and SlaveId
	 *
	 * @return POS object
	 */
	@Override
	public List<PointOfServiceModel> findPOSForSellerAndSlave(final List<String> slaveIds)
	{
		LOG.debug("in dao findPOSForSellerAndSlave ");
		final StringBuilder query = new StringBuilder();
		try
		{
			LOG.debug("call to commerse db search for stores given sellerId and SlaveId ");
			final String slaveStrings = slaveIds.stream().map((s) -> "'" + s + "'").collect(Collectors.joining(", "));
			//create the flexible search query
			query.append(MarketplacecommerceservicesConstants.POS_QUERY_FOR_SELLER_AND_SLAVE.toString());
			query.append(slaveStrings);
			query.append(MarketplacecommerceservicesConstants.POS_QUERY_FOR_SELLER_AND_SLAVE2.toString());

			final FlexibleSearchQuery posQuery = new FlexibleSearchQuery(query);
			posQuery.addQueryParameter(MarketplacecommerceservicesConstants.POS_ACTIVE, MarketplacecommerceservicesConstants.ACTIVE);
			return flexibleSearchService.<PointOfServiceModel> search(posQuery).getResult();
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
