package com.tisl.mpl.core.keygenerator;

import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;

/**
 * @author TECHOUTS
 * 
 * Generate key using prefix        
 */
public class MplPrefixablePersistentKeyGenerator extends PersistentKeyGenerator
{

	private String prefix = "";

	@Override
	public Object generate()
	{
		return getPrefix() + super.generate();
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix()
	{
		return prefix;
	}

	/**
	 * @param prefix
	 *           the prefix to set
	 */
	public void setPrefix(final String prefix)
	{
		this.prefix = prefix;
	}

}
