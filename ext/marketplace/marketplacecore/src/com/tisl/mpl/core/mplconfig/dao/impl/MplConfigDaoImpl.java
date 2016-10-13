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

import com.hybris.oms.tata.model.MplBUCConfigurationsModel;
import com.hybris.oms.tata.model.MplTimeSlotsModel;
import com.tisl.mpl.core.model.MplConfigModel;
import com.tisl.mpl.core.model.MplLPHolidaysModel;
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
	
	public static final String TIMESLOT_CONFIG_QUERY = "select {pk} from {MplTimeSlots} where {timeslotType}=?configKey order by {fromtime}";
	
	public static final String DELIVERYCHARG_CONFIG_QUERY = "select {pk} from {MplBUCConfigurations} ";
	public static final String TIMESLOT_CONFIG_KEY = "configKey";
	
	public static final String MPL_LPHOLIDAYS_CONFIG_QUERY = "select {pk} from {MplLPHolidays} where {lpname}=?configKey ";

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

	/* (non-Javadoc)
	 * @see com.tisl.mpl.core.mplconfig.dao.MplConfigDao#getDeliveryTimeSlotByKey(java.lang.String)
	 */
	@Override
	public List<MplTimeSlotsModel> getDeliveryTimeSlotByKey(String configKey)
	{

				ServicesUtil.validateParameterNotNull(configKey, "Id must not be null");
				final Map queryParams = new HashMap();
				final String query = TIMESLOT_CONFIG_QUERY;
				queryParams.put(TIMESLOT_CONFIG_KEY, configKey);
				final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
				fQuery.addQueryParameters(queryParams);
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("getDeliveryTimeSlotByKey() - MplTimeSlots Query " + fQuery);
				}

				final List<MplTimeSlotsModel> mplConfigModelList = flexibleSearchService.<MplTimeSlotsModel> search(fQuery).getResult();

				if (!mplConfigModelList.isEmpty())
				{
					return mplConfigModelList;
				}
				else
				{
					return null;
				}
	}

	/* (non-Javadoc)
	 * @see com.tisl.mpl.core.mplconfig.dao.MplConfigDao#getDeliveryCharges()
	 */
	@Override
	public MplBUCConfigurationsModel getDeliveryCharges()
	{
	
		final String query = DELIVERYCHARG_CONFIG_QUERY;
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getDeliveryTimeSlotByKey() - MplTimeSlots Query " + fQuery);
		}

		final MplBUCConfigurationsModel mplConfigModel = flexibleSearchService.<MplBUCConfigurationsModel> search(fQuery).getResult().get(0);

		if (null != mplConfigModel)
		{
			return mplConfigModel;
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.tisl.mpl.core.mplconfig.dao.MplConfigDao#getMplLPHolidays(java.lang.String)
	 */
	@Override
	public MplLPHolidaysModel getMplLPHolidays(String configKey)
	{
		final String query = MPL_LPHOLIDAYS_CONFIG_QUERY;
		final Map queryParams = new HashMap();
		queryParams.put(TIMESLOT_CONFIG_KEY, configKey);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getMplLPHolidays() - MplLPHolidays Query " + fQuery);
		}

		final MplLPHolidaysModel mplLpHolidays = flexibleSearchService.<MplLPHolidaysModel> search(fQuery).getResult().get(0);

		if (null != mplLpHolidays)
		{
			return mplLpHolidays;
		}
		else
		{
			return null;
		}
	}


}
