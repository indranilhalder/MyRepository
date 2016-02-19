/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPincodeRestrictionDao;
import com.tisl.mpl.model.RestrictionsetupModel;


/**
 * @author TCS
 *
 */
public class MplPincodeRestrictionDaoImpl implements MplPincodeRestrictionDao
{

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	private static final String SELECT_CLASS = "SELECT {c:";
	private static final String FROM_CLASS = "FROM {";
	private static final String OR_CLASS = ") OR {c:";
	private static final String IN_CLASS = "} IN (";
	private static final Logger LOG = Logger.getLogger(MplPincodeRestrictionDaoImpl.class);

	/**
	 * @description this method queries the restrictionmodel to fetch the restricted pincode
	 * @param pincode
	 * @throws EtailNonBusinessExceptions
	 */


	@Override
	public List<RestrictionsetupModel> getRestrictedPincode(final String pincode, final List<String> articleSKUID,
			final List<String> sellerId, final String listingID, final List<String> categoryCode) throws EtailNonBusinessExceptions
	{

		String skuIds = getIds(articleSKUID);
		String sellerIds = getIds(sellerId);
		String categoryIds = getIds(categoryCode);
		if (null != skuIds && skuIds.length() > 0)
		{
			skuIds = skuIds.substring(0, skuIds.length() - 1);

		}
		if (null != sellerIds && sellerIds.length() > 0)
		{
			sellerIds = sellerIds.substring(0, sellerIds.length() - 1);
		}
		if (null != categoryIds && categoryIds.length() > 0)
		{
			categoryIds = categoryIds.substring(0, categoryIds.length() - 1);
		}
		final String queryString = //
		SELECT_CLASS + RestrictionsetupModel.PK + "} "
				+ FROM_CLASS
				+ RestrictionsetupModel._TYPECODE
				+ " AS c} "//
				+ "WHERE {c:" + RestrictionsetupModel.PINCODE + "} = " + pincode + " and ( {c:" + RestrictionsetupModel.SELLERID
				+ IN_CLASS + sellerIds + OR_CLASS + RestrictionsetupModel.USSID + IN_CLASS + skuIds + OR_CLASS
				+ RestrictionsetupModel.LISTINGID + "} = ?listingId OR {c:" + RestrictionsetupModel.PRIMARYCATID + IN_CLASS
				+ categoryIds + ") )";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("listingId", listingID);
		//query.addQueryParameter("categoryID", categoryCode);
		LOG.info("Query:::::::::::" + queryString);
		final List<RestrictionsetupModel> restrictionsList = flexibleSearchService.<RestrictionsetupModel> search(query)
				.getResult();


		return restrictionsList;

	}

	/**
	 * @description this method is being used for the cart datas to check the pincode restriction from pin code
	 *              restrictiion model
	 * @param pincode
	 * @param articleSKUID
	 * @param sellerId
	 * @param productCode
	 * @param categoryCode
	 * @return restrictionsList
	 * @throws EtailNonBusinessExceptions
	 **
	 **/

	@Override
	public List<RestrictionsetupModel> getRestrictedPincodeCart(final String pincode, final List<String> articleSKUID,
			final List<String> sellerId, final List<String> productCode, final List<String> categoryCode)
			throws EtailNonBusinessExceptions
	{

		String skuIds = getIds(articleSKUID);
		String sellerIds = getIds(sellerId);
		String categoryIds = getIds(categoryCode);
		String productIds = getIds(productCode);
		if (null != skuIds && skuIds.length() > 0)
		{
			skuIds = skuIds.substring(0, skuIds.length() - 1);

		}
		if (null != sellerIds && sellerIds.length() > 0)
		{
			sellerIds = sellerIds.substring(0, sellerIds.length() - 1);
		}
		if (null != categoryIds && categoryIds.length() > 0)
		{
			categoryIds = categoryIds.substring(0, categoryIds.length() - 1);
		}
		if (null != productIds && productIds.length() > 0)
		{
			productIds = productIds.substring(0, productIds.length() - 1);
		}
		final String queryString = //
		SELECT_CLASS + RestrictionsetupModel.PK + "} "
				+ FROM_CLASS
				+ RestrictionsetupModel._TYPECODE
				+ " AS c} "//
				+ "WHERE {c:" + RestrictionsetupModel.PINCODE + "} = " + pincode + " and ( {c:" + RestrictionsetupModel.SELLERID
				+ IN_CLASS + sellerIds + OR_CLASS + RestrictionsetupModel.USSID + IN_CLASS + skuIds + OR_CLASS
				+ RestrictionsetupModel.LISTINGID + "} IN ( " + productIds + OR_CLASS + RestrictionsetupModel.PRIMARYCATID + IN_CLASS
				+ categoryIds + ") )";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		//query.addQueryParameter("listingId", listingID);
		//query.addQueryParameter("categoryID", categoryCode);
		LOG.info("Query:::::::::::" + queryString);
		final List<RestrictionsetupModel> restrictionsList = flexibleSearchService.<RestrictionsetupModel> search(query)
				.getResult();


		return restrictionsList;

	}

	/**
	 *
	 * @param sellerDataList
	 * @return sellerArticleSKUs
	 */
	@SuppressWarnings("javadoc")
	private String getIds(final List<String> dataList)
	{
		String ids = null;
		if (null != dataList && !dataList.isEmpty())
		{
			final StringBuilder stringBuilder = new StringBuilder();
			for (final String id : dataList)
			{
				//stringBuilder.append("'").append(id).append("',"); Avoid appending characters as strings in StringBuffer.append
				stringBuilder.append('\'').append(escapeString(id)).append('\'').append(',');
			}

			ids = stringBuilder.toString();
		}

		return ids;
	}

	private String escapeString(final String id)
	{
		String escapedId = id;
		if (null != id)
		{
			escapedId = id.replaceAll("'", "''");
		}

		return escapedId;
	}


}
