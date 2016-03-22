/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.exception.PointOfServiceDaoException;
import de.hybris.platform.storelocator.impl.GeometryUtils;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.PincodeDao;


/**
 * @author Techouts
 *
 */
@Component(value = "pincodeDao")
public class PincodeDaoImpl implements PincodeDao
{
	

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;




	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PincodeDaoImpl.class);
	
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
		PincodeModel pincodeModel = null;
		try
		{
			final StringBuilder query = new StringBuilder();
			query.append("SELECT {PK} FROM {").append(PincodeModel._TYPECODE).append("} WHERE {").append("pincode")
					.append("} = ?pincode");
			LOG.debug(" Query for Pincode Service:" + query.toString());
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
			fQuery.addQueryParameter("pincode", pincode);
			final SearchResult result = this.flexibleSearchService.search(fQuery);
			LOG.debug("Result for Pincode  Service:" + result.getResult());
			if (null != result.getResult() && result.getResult().size() > 0)
			{
				pincodeModel = (PincodeModel) result.getResult().get(0);
				return pincodeModel;
			}
			return pincodeModel;
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
			final StringBuilder query = new StringBuilder();
			query.append("SELECT {PK} FROM {").append(GeneratedBasecommerceConstants.TC.POINTOFSERVICE).append("} WHERE {")
					.append("latitude").append("} is not null AND {").append("longitude").append("} is not null AND {")
					.append("latitude").append("} >= ?latMin AND {").append("latitude").append("} <= ?latMax AND {")
					.append("longitude").append("} >= ?lonMin AND {").append("longitude").append("} <= ?lonMax ")
					.append(" AND {sellerid").append("} = ?sellerId AND {").append("clicknCollect")
					.append("} = ?clicknCollect AND  { active ").append("} = ?active");
			//
			LOG.debug("Query for get SlaveIds from PointofService :" + query.toString());
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
			fQuery.addQueryParameter("latMax", latMax);
			fQuery.addQueryParameter("latMin", latMin);
			fQuery.addQueryParameter("lonMax", lonMax);
			fQuery.addQueryParameter("lonMin", lonMin);
			fQuery.addQueryParameter("sellerId", sellerId);
			fQuery.addQueryParameter("clicknCollect", MarketplacecommerceservicesConstants.CLICK_N_COLLECT);
			fQuery.addQueryParameter("active", MarketplacecommerceservicesConstants.ACTIVE);
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
	protected FlexibleSearchQuery buildQuery(final GPS center, final double radius)
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
			final StringBuilder query = new StringBuilder();
			query.append("SELECT {PK} FROM {").append(GeneratedBasecommerceConstants.TC.POINTOFSERVICE).append("} WHERE {")
					.append("latitude").append("} is not null AND {").append("longitude").append("} is not null AND {")
					.append("latitude").append("} >= ?latMin AND {").append("latitude").append("} <= ?latMax AND {")
					.append("longitude").append("} >= ?lonMin AND {").append("longitude").append("} <= ?lonMax ");
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
