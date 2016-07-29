/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.GlobalCodeMasterModel;
import com.tisl.mpl.marketplacecommerceservices.service.GlobalCodeService;


/**
 * @author 1006687
 *
 */
public class DefaultGlobalCodeService implements GlobalCodeService
{

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	private static final Logger LOG = Logger.getLogger(DefaultGlobalCodeService.class);


	/**
	 * This function fetches global code master based on the enum code provided in function signature.
	 *
	 * @param String
	 *           code: the code of enum for which global code is required.
	 * @return GlobaslCOdeMasterModel: Te model against the provided enum code.
	 */
	@Override
	public GlobalCodeMasterModel getGlobalMasterCode(final String code)
	{
		GlobalCodeMasterModel globalCode = new GlobalCodeMasterModel();
		globalCode.setEnumCode(code);
		try
		{
			globalCode = flexibleSearchService.getModelByExample(globalCode);
			LOG.info("Global code found . Global code is " + globalCode.getGlobalCode());
			return globalCode;
		}
		catch (final ModelNotFoundException e)
		{
			LOG.error("No Model Found", e);

		}
		catch (final AmbiguousIdentifierException e)
		{
			LOG.error("Ambigious code provided", e);

		}
		return null;
	}


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

}
