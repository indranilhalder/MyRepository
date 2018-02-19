package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.FlexibleSearchUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.MplCMSComponentDao;


public class DefaultMplCMSComponentDao extends AbstractItemDao implements MplCMSComponentDao
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DefaultMplCMSComponentDao.class.getName());

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private ModelService modelService;

	private final static String QUERY = "SELECT {acc.pk} FROM { contentslot AS s JOIN ElementsForSlot AS e2s ON {e2s:SOURCE} = {s:pk} JOIN ContentSlotForPage as csfp ON {s.pk} = {csfp.contentslot} JOIN AbstractPage as ap ON {ap.pk}={csfp.page} JOIN AbstractCMSComponent as acc ON {e2s:target} = {acc:pk} } WHERE {acc.uid}= ?componentId and {ap.uid} =?pageId and ";

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.CMSComponentDao#getPagewiseComponent(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public AbstractCMSComponentModel getPagewiseComponent(final String pageId, final String componentId,
			final Collection<CatalogVersionModel> catalogVersions)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("inside getPagewiseComponent()");
		}
		final StringBuilder queryBuilder = new StringBuilder();
		final Map queryParameters = new HashMap();

		queryBuilder.append(QUERY);

		queryBuilder.append(FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{catalogVersion} in (?catalogVersions)",
				"catalogVersions", "OR", catalogVersions, queryParameters));

		queryParameters.put("pageId", pageId);
		queryParameters.put("componentId", componentId);

		final List<AbstractCMSComponentModel> abstractCMSComponentModels = flexibleSearchService
				.<AbstractCMSComponentModel> search(queryBuilder.toString(), queryParameters).getResult();
		//final SearchResult result = search(queryBuilder.toString(), queryParameters);
		if (abstractCMSComponentModels.size() > 0)
		{
			return abstractCMSComponentModels.get(0);
		}
		else
		{
			return null;
		}

	}

}
