/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.utilities;

import org.apache.log4j.Logger;

import com.tisl.mpl.core.model.GlobalCodeMasterModel;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
/**
 * @author 1006687
 *
 */


public class CodeMasterUtility {

	private static final Logger LOG = Logger.getLogger(CodeMasterUtility.class);

	private static FlexibleSearchService flexibleSearchService = Registry
			.getApplicationContext().getBean(FlexibleSearchService.class);

	public static String getglobalCode(String enumCode) {

		GlobalCodeMasterModel globalCodeMasterModel = new GlobalCodeMasterModel();
		globalCodeMasterModel.setEnumCode(enumCode);
		try {
			globalCodeMasterModel = flexibleSearchService
					.getModelByExample(globalCodeMasterModel);
			return globalCodeMasterModel.getGlobalCode();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

}