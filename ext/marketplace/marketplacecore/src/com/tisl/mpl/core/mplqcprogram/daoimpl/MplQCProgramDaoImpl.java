/**
 * 
 */
package com.tisl.mpl.core.mplqcprogram.daoimpl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.MplQCProgramConfigModel;
import com.tisl.mpl.core.mplconfig.dao.impl.MplConfigDaoImpl;
import com.tisl.mpl.core.mplqcprogram.dao.MplQCProgramDao;

/**
 * @author Tech
 *
 */
public class MplQCProgramDaoImpl implements MplQCProgramDao
{
	private static final Logger LOGGER = Logger.getLogger(MplConfigDaoImpl.class);
	public static final String QC_PROGRAM_CONFIG_QUERY = "select {pk} from {MplQCProgramConfig} where {programId}=?programId";
	public static final String ID_STRING = "programId";

	@Autowired
	private FlexibleSearchService flexibleSearchService;
	
	@Override
	public MplQCProgramConfigModel getProgramIdConfigValueById(String programId)
	{
		ServicesUtil.validateParameterNotNull(programId, "programId must not be null");
		final Map queryParams = new HashMap();
		final String query = QC_PROGRAM_CONFIG_QUERY;
		queryParams.put(ID_STRING, programId);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigValueById() - Config Model Query " + fQuery);
		}

		final List<MplQCProgramConfigModel> mplQCProgramConfigModelList = flexibleSearchService.<MplQCProgramConfigModel> search(fQuery).getResult();

		if (!mplQCProgramConfigModelList.isEmpty())
		{
			return mplQCProgramConfigModelList.get(0);
		}
		else
		{
			return null;
		}
	}

}
