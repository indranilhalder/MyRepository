/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.core.setup;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;


/**
 * This class provides hooks into the system's initialization and update processes.
 *
 * @see "https://wiki.hybris.com/display/release4/Hooks+for+Initialization+and+Update+Process"
 */
@SystemSetup(extension = MarketplaceCoreConstants.EXTENSIONNAME)
public class CoreSystemSetup extends AbstractSystemSetup
{
	//Added for adhoc impex
	@Autowired
	private ConfigurationService configurationService;

	public static final String IMPORT_ACCESS_RIGHTS = "accessRights";
	ArrayList<String> releaseList = new ArrayList<String>();

	/**
	 * This method will be called by system creator during initialization and system update. Be sure that this method can
	 * be called repeatedly.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.ESSENTIAL, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{

		importImpexFile(context, "/marketplacecore/import/common/essential-data.impex");
		importImpexFile(context, "/marketplacecore/import/common/countries.impex");
		importImpexFile(context, "/marketplacecore/import/common/delivery-modes.impex");

		importImpexFile(context, "/marketplacecore/import/common/themes.impex");
		importImpexFile(context, "/marketplacecore/import/common/user-groups.impex");
		importImpexFile(context, "/marketplacecore/import/common/payment.impex");
		importImpexFile(context, "/marketplacecore/import/common/cronjobs.impex");

	}

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		//Added for adhoc.impex
		final String releases = configurationService.getConfiguration().getString(MarketplaceCoreConstants.releaseImpex);
		if (!StringUtils.isEmpty(releases))
		{

			for (final String release : releases.split(MarketplacecommerceservicesConstants.COMMA))
			{
				releaseList.add(release);
			}

		}




		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		params.add(createBooleanSystemSetupParameter(IMPORT_ACCESS_RIGHTS, "Import Users & Groups", true));
		//Added for adhoc.impex
		if (CollectionUtils.isNotEmpty(releaseList))
		{
			for (final String rel : releaseList)
			{
				params.add(createBooleanSystemSetupParameter(rel, rel, false));
			}
		}

		return params;
	}

	/**
	 * This method will be called during the system initialization.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{



		final boolean importAccessRights = getBooleanSystemSetupParameter(context, IMPORT_ACCESS_RIGHTS);

		final List<String> extensionNames = getExtensionNames();

		if (importAccessRights && extensionNames.contains("cmscockpit"))
		{
			importImpexFile(context, "/marketplacecore/import/cockpits/cmscockpit/cmscockpit-users.impex");
			importImpexFile(context, "/marketplacecore/import/cockpits/cmscockpit/cmscockpit-access-rights.impex");
		}

		if (importAccessRights && extensionNames.contains("btgcockpit"))
		{
			importImpexFile(context, "/marketplacecore/import/cockpits/cmscockpit/btgcockpit-users.impex");
			importImpexFile(context, "/marketplacecore/import/cockpits/cmscockpit/btgcockpit-access-rights.impex");
		}

		if (importAccessRights && extensionNames.contains("productcockpit"))
		{
			importImpexFile(context, "/marketplacecore/import/cockpits/productcockpit/productcockpit-users.impex");
			importImpexFile(context, "/marketplacecore/import/cockpits/productcockpit/productcockpit-access-rights.impex");
			importImpexFile(context, "/marketplacecore/import/cockpits/productcockpit/productcockpit-constraints.impex");
		}

		if (importAccessRights && extensionNames.contains("cscockpit"))
		{
			importImpexFile(context, "/marketplacecore/import/cockpits/cscockpit/cscockpit-users.impex");
			importImpexFile(context, "/marketplacecore/import/cockpits/cscockpit/cscockpit-access-rights.impex");
		}

		if (importAccessRights && extensionNames.contains("reportcockpit"))
		{
			importImpexFile(context, "/marketplacecore/import/cockpits/reportcockpit/reportcockpit-users.impex");
			importImpexFile(context, "/marketplacecore/import/cockpits/reportcockpit/reportcockpit-access-rights.impex");
		}

		if (extensionNames.contains("mcc"))
		{
			importImpexFile(context, "/marketplacecore/import/common/mcc-sites-links.impex");
		}

		//Added for adhoc.impex
		if (CollectionUtils.isNotEmpty(releaseList))
		{
			for (final String rel : releaseList)
			{
				if (getBooleanSystemSetupParameter(context, rel))
				{
					//		System.out.println("Executing" + rel + "impexex");
					importImpexFile(context, "/marketplacecore/import/release/" + rel + ".impex");
				}
			}
		}
	}


	protected List<String> getExtensionNames()
	{
		return Registry.getCurrentTenant().getTenantSpecificExtensionNames();
	}

	protected <T> T getBeanForName(final String name)
	{
		return (T) Registry.getApplicationContext().getBean(name);
	}
}