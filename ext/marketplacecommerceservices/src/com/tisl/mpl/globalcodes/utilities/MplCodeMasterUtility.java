/**
 *
 */
package com.tisl.mpl.globalcodes.utilities;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import org.apache.log4j.Logger;

import com.tisl.mpl.core.model.GlobalCodeMasterModel;


/**
 * @author TCS
 * @Desciption Ultil class for fetching global codes from database
 *
 */
public class MplCodeMasterUtility
{
	private static final Logger LOG = Logger.getLogger(MplCodeMasterUtility.class);

	private static final String QC_FAILED_CODE_PREFIX = "QC_FAILED_";
	private static FlexibleSearchService flexibleSearchService = Registry.getApplicationContext()
			.getBean(FlexibleSearchService.class);

	public static String getglobalCode(final String enumCode)
	{

		GlobalCodeMasterModel globalCodeMasterModel = new GlobalCodeMasterModel();
		globalCodeMasterModel.setEnumCode(enumCode);

		LOG.debug("MplCodeMasterUtility :: finding global code using enumCode :: " + enumCode);
		globalCodeMasterModel = getGlobalCodeMasterModel(globalCodeMasterModel);
		return globalCodeMasterModel == null ? null : globalCodeMasterModel.getGlobalCode();

	}


	public static GlobalCodeMasterModel getGlobalCodeMasterModel(final GlobalCodeMasterModel globalCodeMasterModel)
	{

		try
		{
			final GlobalCodeMasterModel newGlobalCodeMasterModel = flexibleSearchService.getModelByExample(globalCodeMasterModel);
			LOG.debug("MplCodeMasterUtility :: getGlobalCodeMasterModel() :: " + newGlobalCodeMasterModel);
			return newGlobalCodeMasterModel;
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
		return null;
	}


	public static String getEnumCode(final String globalCode)
	{

		GlobalCodeMasterModel globalCodeMasterModel = new GlobalCodeMasterModel();
		globalCodeMasterModel.setGlobalCode(globalCode);

		LOG.debug("MplCodeMasterUtility :: finding enum code using globalCode :: " + globalCode);
		globalCodeMasterModel = getGlobalCodeMasterModel(globalCodeMasterModel);
		return globalCodeMasterModel == null ? null : globalCodeMasterModel.getGlobalCode();
	}

	public static String getQCFailedDesc(final String qCFailedCode)
	{

		LOG.debug(
				"MplCodeMasterUtility :: finding qCFailed Desc code using qCFailedCode :: " + QC_FAILED_CODE_PREFIX + qCFailedCode);

		GlobalCodeMasterModel globalCodeMasterModel = new GlobalCodeMasterModel();
		globalCodeMasterModel.setGlobalCode(QC_FAILED_CODE_PREFIX + qCFailedCode);

		LOG.debug("MplCodeMasterUtility :: globalCode :: " + QC_FAILED_CODE_PREFIX + qCFailedCode);
		globalCodeMasterModel = getGlobalCodeMasterModel(globalCodeMasterModel);
		return globalCodeMasterModel == null ? null : globalCodeMasterModel.getDescription();

	}
}
