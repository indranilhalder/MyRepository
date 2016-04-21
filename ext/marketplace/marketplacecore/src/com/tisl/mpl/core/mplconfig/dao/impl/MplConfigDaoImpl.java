/**
 *
 */
package com.tisl.mpl.core.mplconfig.dao.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.MplConfigModel;
import com.tisl.mpl.core.mplconfig.dao.MplConfigDao;



/**
 * The MplConfigDaoImpl class interacts with the hybris data and retrieves the result set on the basis of requested id.
 *
 * @author SAP
 * @version 1.0
 *
 */
public class MplConfigDaoImpl implements MplConfigDao
{
	private static final Logger LOGGER = Logger.getLogger(MplConfigDaoImpl.class);
	public static final String CONFIG_QUERY = "select {pk} from {MplConfig} where {ID}=?id";
	public static final String ID_STRING = "id";

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MplConfigModel getConfigValueById(final String id)
	{

		//To validate the incoming parameter
		ServicesUtil.validateParameterNotNull(id, "Id must not be null");
		final Map queryParams = new HashMap();
		final String query = CONFIG_QUERY;
		queryParams.put(ID_STRING, id);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigValueById() - Config Model Query " + fQuery);
		}

		final List<MplConfigModel> mplConfigModelList = flexibleSearchService.<MplConfigModel> search(fQuery).getResult();

		if (!mplConfigModelList.isEmpty())
		{
			return mplConfigModelList.get(0);
		}
		else
		{
			return null;
		}
	}


}
