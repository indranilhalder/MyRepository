/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.brand.impl;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BrandMasterModel;
import com.tisl.mpl.core.model.FollowedBrandGenderModel;
import com.tisl.mpl.core.model.FollowedBrandMcvidModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.brand.MplFollowedBrandDao;


/**
 * @author TCS
 *
 */
public class MplFollowedBrandDaoImpl implements MplFollowedBrandDao
{
	private static final Logger LOG = Logger.getLogger(MplFollowedBrandDaoImpl.class);
	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.brand.MplFollowedBrandDao#getFollowedBrands(java.lang.String)
	 */
	private static final String PRODUCT_PARAM = "{bm.brandCode}=?productParam";
	private static final String OR = " OR ";
	private static final String PRODUCTPARAM = "productParam";

	private static final String BB_PRODUCTPARAM1 = " {bm.brandCode}=?productParam1";

	private static final String PRODUCTPARAM1 = "productParam1";

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

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.brand.MplFollowedBrandDao#getBrands(java.lang.String)
	 */
	@Override
	public List<BrandMasterModel> getBrands(final String brands)
	{
		// YTODO Auto-generated method stub
		final StringBuilder brandCodes = new StringBuilder(100);
		final Map<String, String> queryParamMap = new HashMap<String, String>();
		try
		{
			if (brands.indexOf(MarketplacecommerceservicesConstants.COMMA) != -1)//if multiple brands
			{

				final String[] codes = brands.split(MarketplacecommerceservicesConstants.COMMA);
				int cnt = 0;
				brandCodes.append("( ");
				for (final String code : codes)
				{
					cnt = cnt + 1;
					if (cnt == 1)
					{
						brandCodes.append(PRODUCT_PARAM + (cnt));
					}
					else
					{
						brandCodes.append(OR + PRODUCT_PARAM + (cnt));
					}
					queryParamMap.put(PRODUCTPARAM + (cnt), code);
				}
				brandCodes.append(" )");
			}
			else
			//if one brand
			{
				brandCodes.append("( ");
				brandCodes.append(BB_PRODUCTPARAM1);
				queryParamMap.put(PRODUCTPARAM1, brands);
				brandCodes.append(" )");
			}

			final String queryStringForPrice = "SELECT {bm:" + BrandMasterModel.PK + "}" + "FROM {" + BrandMasterModel._TYPECODE
					+ " AS bm } " + "WHERE " + brandCodes.toString();



			LOG.debug("" + queryStringForPrice);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryStringForPrice);

			for (final Map.Entry<String, String> entry : queryParamMap.entrySet())
			{
				query.addQueryParameter(entry.getKey(), entry.getValue());
			}

			final List<BrandMasterModel> retList = flexibleSearchService.<BrandMasterModel> search(query).getResult();
			return retList;
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
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.brand.MplFollowedBrandDao#getMcvIdBrands(java.lang.String)
	 */
	@Override
	public List<FollowedBrandMcvidModel> getMcvIdBrands(final String mcvId)
	{
		// YTODO Auto-generated method stub

		try
		{
			final String queryString = "SELECT {fb:" + FollowedBrandMcvidModel.PK + "}" + " FROM { "
					+ FollowedBrandMcvidModel._TYPECODE + " AS fb }  WHERE { fb:" + FollowedBrandMcvidModel.MCVID + "}=?mcvId ";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("mcvId", mcvId);

			final List<FollowedBrandMcvidModel> listOfData = flexibleSearchService.<FollowedBrandMcvidModel> search(query)
					.getResult();
			return listOfData;
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
