/**
 *
 */
package com.tisl.mpl.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.core.model.QCInitResponseDetailModel;
import com.tisl.mpl.daos.MplQCInitDetailData;
import com.tisl.mpl.review.daos.impl.MplGigyaReviewCommentDaoImpl;


/**
 * @author TUL
 *
 */
public class MplQCInitDataImpl implements MplQCInitDetailData
{

	private final static Logger LOG = Logger.getLogger(MplGigyaReviewCommentDaoImpl.class.getName());

	@Resource
	private FlexibleSearchService flexibleSearchService;



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


	@Override
	public QCInitResponseDetailModel getQCInitDetailDataImpl()
	{
		try
		{

			final String searchQuery = "SELECT {" + QCInitResponseDetailModel.PK + "}  FROM {QCInitResponseDetail}";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(searchQuery);

			final SearchResult<QCInitResponseDetailModel> searchResult = getFlexibleSearchService().search(query);
			final List<QCInitResponseDetailModel> results = searchResult.getResult();
			return results.get(0);

		}
		catch (final Exception e)
		{
			LOG.error(e);
		}
		return null;
	}


}
