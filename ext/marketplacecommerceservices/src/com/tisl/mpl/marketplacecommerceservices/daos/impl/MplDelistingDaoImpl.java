/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.MarketplaceDelistModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplDelistingDao;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */
//TISPRD-207 Changes
public class MplDelistingDaoImpl implements MplDelistingDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	private final static String SELECT = "SELECT {c:";
	private final static String QUERY = "Query:";
	private final static String C = "{c:";
	private static final Logger LOG = Logger.getLogger(MplDelistingDaoImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplDelistingDao#getAllUSSIDforSeller(java.lang.String)
	 */
	@Override
	public List<SellerInformationModel> getAllUSSIDforSeller(final String sellerId)
	{
		try
		{
			final String queryString = //
			SELECT + SellerInformationModel.PK + "} " //
					+ "FROM {" + SellerInformationModel._TYPECODE + " AS c} "//
					+ "WHERE " + C + SellerInformationModel.SELLERID + "}=?sellerId";

			LOG.info(QUERY + queryString);

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("sellerId", sellerId);
			//	query.addQueryParameter(SellerInformationModel.CATALOGVERSION, catalogVersionModel);


			final List<SellerInformationModel> listSellerInformation = flexibleSearchService.<SellerInformationModel> search(query)
					.getResult();
			return listSellerInformation;
		}

		catch (final Exception ex)
		{

			throw new EtailNonBusinessExceptions(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplDelistingDao#delistSeller(java.util.List, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void delistSeller(final List<String> ussid, final String delisting, final String blockOMS)
	{
		// YTODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplDelistingDao#delistUSSID(java.util.List, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void delistUSSID(final List<String> ussid, final String delisting, final String delist)
	{
		// YTODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplDelistingDao#getModelforUSSID(java.lang.String)
	 */
	@Override
	public List<SellerInformationModel> getModelforUSSID(final String ussid)
	{
		try
		{
			final String queryString = //
			SELECT + SellerInformationModel.PK + "} " //
					+ "FROM {" + SellerInformationModel._TYPECODE + " AS c} "//
					+ "WHERE " + C + SellerInformationModel.SELLERARTICLESKU + "}=?ussid";
			//As Sellerarticlesku is same as USSID
			LOG.info(QUERY + queryString);

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("ussid", ussid);
			//	query.addQueryParameter(SellerInformationModel.CATALOGVERSION, catalogVersionModel);


			final List<SellerInformationModel> listSellerInformation = flexibleSearchService.<SellerInformationModel> search(query)
					.getResult();
			return listSellerInformation;
		}

		catch (final Exception ex)
		{

			throw new EtailNonBusinessExceptions(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * TISEE-5143
	 */
	@Override
	public List<SellerInformationModel> getModelforUSSID(final String ussid, final CatalogVersionModel catalogVersion)
	{
		try
		{
			final String queryString = //
			SELECT + SellerInformationModel.PK
					+ "} " //
					+ "FROM {" + SellerInformationModel._TYPECODE
					+ " AS c} "//
					+ "WHERE " + C + SellerInformationModel.SELLERARTICLESKU + "}=?ussid" + " AND {c:"
					+ SellerInformationModel.CATALOGVERSION + "}=?catalogVersion";
			//As Sellerarticlesku is same as USSID
			LOG.info(QUERY + queryString);

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("ussid", ussid);
			query.addQueryParameter(SellerInformationModel.CATALOGVERSION, catalogVersion);


			final List<SellerInformationModel> listSellerInformation = flexibleSearchService.<SellerInformationModel> search(query)
					.getResult();
			return listSellerInformation;
		}

		catch (final Exception ex)
		{

			throw new EtailNonBusinessExceptions(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplDelistingDao#FindUnprocessedRecord()
	 */
	@Override
	public List<MarketplaceDelistModel> findUnprocessedRecord()
	{
		try
		{
			//			final String queryString = //
			//			"SELECT {c:" + MarketplaceDelistModel.PK + "} " //
			//					+ "FROM {" + MarketplaceDelistModel._TYPECODE + " AS c} "//
			//					+ "WHERE " + "{c:" + MarketplaceDelistModel.ISPROCESSED + "}='0'";
			final StringBuilder stbQuery = new StringBuilder(50);
			stbQuery.append(SELECT + MarketplaceDelistModel.PK + "} FROM " + "{" + MarketplaceDelistModel._TYPECODE
					+ " AS c} WHERE " + C + MarketplaceDelistModel.ISPROCESSED + "}='0'");

			LOG.info(QUERY + stbQuery);

			final FlexibleSearchQuery query = new FlexibleSearchQuery(stbQuery.toString());

			final List<MarketplaceDelistModel> listSellerInformation = flexibleSearchService.<MarketplaceDelistModel> search(query)
					.getResult();
			return listSellerInformation;
		}

		catch (final Exception ex)
		{

			throw new EtailNonBusinessExceptions(ex);
		}

	}
}