/**
 * 
 */
package com.tisl.mpl.facade.config;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.mplconfig.service.MplConfigService;

/**
 * @author Techouts
 *
 */
public class MplConfigFacadeImpl implements MplConfigFacade
{
	@Autowired
   private MplConfigService mplConfigService;
   
	/**
	 * Get configuration value from MplConfig Model by key
	 * 
	 * @param configKey
	 * @return configurationValue
	 */
	@Override
	public String getCongigValue(String configKey)
	{
		return mplConfigService.getConfigValueById(configKey);
	}

}
