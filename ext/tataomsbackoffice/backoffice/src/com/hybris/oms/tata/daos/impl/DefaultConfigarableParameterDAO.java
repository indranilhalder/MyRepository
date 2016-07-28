/**
 *
 */
package com.hybris.oms.tata.daos.impl;

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.tata.constants.TataomsbackofficeConstants;
import com.hybris.oms.tata.daos.ConfigarableParameterDAO;
import com.hybris.oms.tata.model.MplBUCConfigurationsModel;
import com.hybris.oms.tata.model.MplTimeSlotsModel;


/**
 * Configarable search parameter dao class used for CURD operation
 *
 * @author prabhakar
 */

public class DefaultConfigarableParameterDAO implements ConfigarableParameterDAO
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultConfigarableParameterDAO.class);


	/**
	 * We use hybris' FlexibleSearchService for running queries against the database
	 *
	 * @see "https://wiki.hybris.com/display/release5/FlexibleSearch"
	 */
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@Autowired
	private ModelService modelService;

	/*
	 * (non-Javadoc)Finds all MplTimeSlots by performing a FlexibleSearch using the {@link FlexibleSearchService}.
	 *
	 * @see com.hybris.oms.tata.daos.ConfigarableParameterDAO#onLoadMplTimeSlots()
	 */
	@Override
	public List<MplTimeSlotsModel> onLoadMplTimeSlots()
	{

		// Build a query for the flexible search.
		final String queryString = //
		"SELECT {p:" + MplTimeSlotsModel.PK + "} "//
				+ "FROM {" + MplTimeSlotsModel._TYPECODE + " AS p} ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		// Return the list of MplTimeSlotsModel.
		return flexibleSearchService.<MplTimeSlotsModel> search(query).getResult();

	}

	/*
	 * (non-Javadoc) this method is used to save mplTimeSlots by removing existing time slots
	 *
	 * @see
	 * com.hybris.oms.tata.daos.ConfigarableParameterDAO#saveMplTimeSlots(com.hybris.oms.tata.model.MplTimeSlotsModel)
	 */
	@Override
	public void saveMplTimeSlots(final List<MplTimeSlotsModel> mplTimeSlots, final String timeSlotType)
	{

		LOG.info("save MplTimeSlots dao");

		final StringBuilder queryString = new StringBuilder("SELECT {p:" + MplTimeSlotsModel.PK + "} "//
				+ "FROM {" + MplTimeSlotsModel._TYPECODE + " AS p} WHERE {p:" + MplTimeSlotsModel.TIMESLOTTYPE + "} " + "LIKE ");

		LOG.info(queryString.toString());

		if (timeSlotType.equalsIgnoreCase(TataomsbackofficeConstants.SCHEDULEDDELIVERY))
		{
			queryString.append("'" + TataomsbackofficeConstants.SCHEDULEDDELIVERY + "'");
			LOG.info(queryString.toString());
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());
			final List<MplTimeSlotsModel> sdTimeSlotsList = flexibleSearchService.<MplTimeSlotsModel> search(query).getResult();
			modelService.removeAll(sdTimeSlotsList);
			modelService.saveAll(mplTimeSlots);
		}
		else if (timeSlotType.equalsIgnoreCase(TataomsbackofficeConstants.EXPRESSDELIVERY))
		{
			queryString.append("'" + TataomsbackofficeConstants.EXPRESSDELIVERY + "'");
			LOG.info(queryString.toString());
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());
			final List<MplTimeSlotsModel> edTimeSlotsList = flexibleSearchService.<MplTimeSlotsModel> search(query).getResult();
			modelService.removeAll(edTimeSlotsList);
			modelService.saveAll(mplTimeSlots);
		}
		else if (timeSlotType.equalsIgnoreCase(TataomsbackofficeConstants.RETURNDELIVERY))
		{
			queryString.append("'" + TataomsbackofficeConstants.RETURNDELIVERY + "'");
			LOG.info(queryString.toString());
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());
			final List<MplTimeSlotsModel> rdTimeSlotsList = flexibleSearchService.<MplTimeSlotsModel> search(query).getResult();
			modelService.removeAll(rdTimeSlotsList);
			modelService.saveAll(mplTimeSlots);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.oms.tata.daos.ConfigarableParameterDAO#saveMplBUCConfigurations(com.hybris.oms.tata.model.
	 * MplBUCConfigurationsModel)
	 */
	@Override
	public void saveMplBUCConfigurations(final MplBUCConfigurationsModel mplBucConfigurations)
	{

		if (mplBucConfigurations != null)
		{
			modelService.save(mplBucConfigurations);
		}
		else
		{
			LOG.warn("Empty mplBucConfigurations ");
		}

	}
}