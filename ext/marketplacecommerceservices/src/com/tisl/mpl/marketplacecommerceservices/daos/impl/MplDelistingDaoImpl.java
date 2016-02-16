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

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplDelistingDao;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */
public class MplDelistingDaoImpl implements MplDelistingDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

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
			"SELECT {c:" + SellerInformationModel.PK + "} " //
					+ "FROM {" + SellerInformationModel._TYPECODE + " AS c} "//
					+ "WHERE " + "{c:" + SellerInformationModel.SELLERID + "}=?sellerId";

			LOG.info("Query:" + queryString);

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
			"SELECT {c:" + SellerInformationModel.PK + "} " //
					+ "FROM {" + SellerInformationModel._TYPECODE + " AS c} "//
					+ "WHERE " + "{c:" + SellerInformationModel.SELLERARTICLESKU + "}=?ussid";
			//As Sellerarticlesku is same as USSID
			LOG.info("Query:" + queryString);

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
			"SELECT {c:" + SellerInformationModel.PK + "} " //
					+ "FROM {" + SellerInformationModel._TYPECODE + " AS c} "//
					+ "WHERE " + "{c:" + SellerInformationModel.SELLERARTICLESKU + "}=?ussid" + " AND {c:"
					+ SellerInformationModel.CATALOGVERSION + "}=?catalogVersion";
			//As Sellerarticlesku is same as USSID
			LOG.info("Query:" + queryString);

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
	 * @Javadoc Method to retrieve list of BuyBoxWieghtageModel based on articleSKUID
	 *
	 * @param articleSKUIDList
	 *
	 * @return listOfWieghtage
	 */

	//	@Override
	//	public BuyBoxWieghtageModel getAllBuyBoxDetail(final String articleSKUIDList)
	//	{
	//		try
	//		{
	//			BuyBoxWieghtageModel buyBoxWieghtageModel = null;
	//			final String queryString = //
	//			"SELECT {c:" + BuyBoxWieghtageModel.PK
	//					+ "} " //
	//					+ "FROM {" + BuyBoxWieghtageModel._TYPECODE
	//					+ " AS c} "//
	//					+ "WHERE " + "{c:" + BuyBoxWieghtageModel.SELLERARTICLESKU + "} in (" + articleSKUIDList + ") order by {c."
	//					+ BuyBoxWieghtageModel.WEIGHTAGE + "} desc";
	//
	//			LOG.info("Query:::::::::::" + queryString);
	//
	//			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
	//			final List<BuyBoxWieghtageModel> listOfWieghtage = flexibleSearchService.<BuyBoxWieghtageModel> search(query)
	//					.getResult();
	//			if (null != listOfWieghtage && !listOfWieghtage.isEmpty())
	//			{
	//				buyBoxWieghtageModel = flexibleSearchService.<BuyBoxWieghtageModel> search(query).getResult().get(0);
	//			}
	//
	//			return buyBoxWieghtageModel;
	//		}
	//
	//		catch (final Exception ex)
	//		{
	//			throw new EtailNonBusinessExceptions(ex);
	//		}
	//	}
}
