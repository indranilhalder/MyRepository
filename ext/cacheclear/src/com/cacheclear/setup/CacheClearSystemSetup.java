/**
 *
 */
package com.cacheclear.setup;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;

import java.util.List;

import com.cacheclear.constants.CacheclearConstants;


/**
 * <h1>Cache Clear System Setup</h1>
 * <p>
 * This setup loads the project data required for extension from /cacheclear/import/common/project-data.impex
 * </p>
 *
 * @author Krishnakumar Raju
 *
 */
@SystemSetup(extension = CacheclearConstants.EXTENSIONNAME)
public class CacheClearSystemSetup extends AbstractSystemSetup
{
	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		importImpexFile(context, "/cacheclear/import/common/project-data.impex");
	}

	@Override
	public List<SystemSetupParameter> getInitializationOptions()
	{
		return null;
	}

}
