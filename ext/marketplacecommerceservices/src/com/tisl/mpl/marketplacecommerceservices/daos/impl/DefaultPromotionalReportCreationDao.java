/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.hmc.model.SavedValuesModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.PromotionalReportCreationDao;


/**
 * @author TCS
 *
 */
public class DefaultPromotionalReportCreationDao implements PromotionalReportCreationDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DefaultPromotionalReportCreationDao.class.getName());

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

	/**
	 * @Descriptiion: Getting All Promotions
	 * @param startDate
	 * @param endDate
	 * @return : set of all created/ modified promotions with users within startDate and endDate
	 */
	@Override
	public Set<Map<AbstractPromotionModel, SavedValuesModel>> getAllPromotions(final Date startDate, final Date endDate)
	{
		final Set<Map<AbstractPromotionModel, SavedValuesModel>> promotionWithCreatedBymap = new LinkedHashSet<Map<AbstractPromotionModel, SavedValuesModel>>();

		//final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

		final String startDateStr = formatter.format(startDate);
		final String endDateStr = formatter.format(endDate);

		final String queryString = "select {promo.pk},{savedVal.pk} from {" + AbstractPromotionModel._TYPECODE
				+ " as promo JOIN "//
				+ SavedValuesModel._TYPECODE
				+ " as savedVal ON {promo.itemtype}={savedVal.modifiedItemType} }"//
				+ " where {promo.immutableKeyHash} is NULL AND CAST({savedVal.timestamp} AS DATE) BETWEEN to_date('" + startDateStr
				+ "', 'MM/DD/YYYY') AND to_date('" + endDateStr + "', 'MM/DD/YYYY') ";//

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.setResultClassList(Arrays.asList(AbstractPromotionModel.class, SavedValuesModel.class));

		final SearchResult<List<Object>> result = flexibleSearchService.search(query);

		for (final List<Object> row : result.getResult())
		{
			final Map<AbstractPromotionModel, SavedValuesModel> resultMap = new HashMap<AbstractPromotionModel, SavedValuesModel>();
			final AbstractPromotionModel promotion = (AbstractPromotionModel) row.get(0);
			final SavedValuesModel savedValues = (SavedValuesModel) row.get(1);

			//if (savedValues.getCreationtime() != null || savedValues.getModifiedtime() != null)
			if (savedValues.getTimestamp() != null)
			{
				try
				{
					//final Date creationTime = formatter.parse(formatter.format(savedValues.getCreationtime()));
					//final Date modifiedTime = formatter.parse(formatter.format(savedValues.getModifiedtime()));

					//if ((creationTime.compareTo(startDate) >= 0 && endDate.compareTo(creationTime) >= 0)
					//|| (modifiedTime.compareTo(startDate) >= 0 && endDate.compareTo(modifiedTime) >= 0))

					//final Date timestamp = formatter.parse(formatter.format(savedValues.getTimestamp()));

					//					if ((timestamp.compareTo(startDate) >= 0 && endDate.compareTo(timestamp) >= 0)
					//							|| (timestamp.compareTo(startDate) >= 0 && endDate.compareTo(timestamp) >= 0))
					//					{
					resultMap.put(promotion, savedValues);
					promotionWithCreatedBymap.add(resultMap);
					//}
				}
				catch (final Exception e)
				{
					LOG.debug(e.getMessage());
				}
			}
		}
		return promotionWithCreatedBymap;
	}
}
