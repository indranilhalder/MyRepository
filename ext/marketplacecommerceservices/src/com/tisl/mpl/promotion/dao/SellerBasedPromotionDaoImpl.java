/**
 *
 */
package com.tisl.mpl.promotion.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */
@Component(value = "sellerBasedPromotionDao")
public class SellerBasedPromotionDaoImpl implements SellerBasedPromotionDao
{
	private static final Logger LOG = Logger.getLogger(SellerBasedPromotionDaoImpl.class);

	private static final String P = "{p.";
	private static final String PRM = "{prm.";
	private static final String PROMO = "{promo.";
	private static final String APM = "{apm.";
	private static final String QUERY_FROM = "FROM {";


	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/**
	 * @Description: Fetch Seller Information based on Seller USSID
	 * @param : code, oModel
	 * @return List<SellerInformationModel>
	 */
	@Override
	public List<SellerInformationModel> fetchSellerInformation(final String code, final CatalogVersionModel oModel)
	{
		LOG.debug("Fetching Seller Data");
		final String queryString = //
		"SELECT {p:" + SellerInformationModel.PK
				+ "} "//
				+ QUERY_FROM + SellerInformationModel._TYPECODE + " AS p } where" + P + SellerInformationModel.SELLERARTICLESKU
				+ "} = ?code and " + P + SellerInformationModel.CATALOGVERSION + "} = ?oModel";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("code", code);
		query.addQueryParameter("oModel", oModel);
		return flexibleSearchService.<SellerInformationModel> search(query).getResult();
	}

	/**
	 * @Description: Fetch Price Information based on Seller USSID
	 * @param : code, oModel
	 * @return List<SellerInformationModel>
	 */
	@Override
	public List<PriceRowModel> fetchPriceInformation(final String code, final CatalogVersionModel oModel)
	{
		LOG.debug("Fetching Price Information Data Corresponding to a USSID");
		final String queryString = //
		"SELECT {prm:" + PriceRowModel.PK
				+ "} "//
				+ QUERY_FROM + PriceRowModel._TYPECODE + " AS prm } where" + PRM + PriceRowModel.SELLERARTICLESKU + "} = ?code and "
				+ PRM + PriceRowModel.CATALOGVERSION + "} = ?oModel";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("code", code);
		query.addQueryParameter("oModel", oModel);
		return flexibleSearchService.<PriceRowModel> search(query).getResult();
	}

	/**
	 * @Description: Fetch Promotion Details corresponding to a code
	 * @param : code
	 * @return List<AbstractPromotionModel>
	 */
	@Override
	public List<AbstractPromotionModel> fetchPromotionDetails(final String code)
	{
		LOG.debug("Fetching Promotion Details");
		final String queryString = //
		"SELECT {promo:" + AbstractPromotionModel.PK
				+ "} "//
				+ QUERY_FROM + AbstractPromotionModel._TYPECODE + " AS promo } where" + PROMO + AbstractPromotionModel.CODE
				+ "} = ?code";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("code", code);
		return flexibleSearchService.<AbstractPromotionModel> search(query).getResult();
	}

	/**
	 * Fetch Promotion Details
	 */
	@Override
	public List<AbstractPromotionModel> getPromoDetails()
	{
		LOG.debug("Fetching Promotion Details");
		final String queryString = //
		"SELECT {apm:" + AbstractPromotionModel.PK
				+ "} "//
				+ QUERY_FROM + AbstractPromotionModel._TYPECODE + " AS apm } where" + APM + AbstractPromotionModel.ENABLED
				+ "} = ?true";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("true", Boolean.TRUE);
		return flexibleSearchService.<AbstractPromotionModel> search(query).getResult();
	}
}
