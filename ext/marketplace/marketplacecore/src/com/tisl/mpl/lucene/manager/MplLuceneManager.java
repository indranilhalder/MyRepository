/**
 *
 */
package com.tisl.mpl.lucene.manager;

import de.hybris.platform.lucenesearch.jalo.LucenesearchManager;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.JspContext;

import java.util.Map;

import org.apache.log4j.Logger;


/**
 * @author 768233
 *
 */
public class MplLuceneManager extends LucenesearchManager
{
	private static final Logger LOG = Logger.getLogger(MplLuceneManager.class);

	//    @Override
	//    public String getCreatorParameterDefault(final String param)
	//    {
	//        return Config. getParameter("hybris.jalo.lucenesearch.createessentialdata.enabled");
	//    }

	/**
	 * here return nothing. check ${hybris}/data/luceneindex directory
	 */
	@Override
	public void createEssentialData(final Map values, final JspContext jspc) //  meta code
	{
		if (Config.getBoolean("lucenesearch.createessentialdata.enabled", false))
		{
			LOG.warn("--------- creating essential data as  lucenesearch.createessentialdata.enabled is true-----");
			try
			{
				super.createEssentialData(values, jspc);
			}
			catch (final Exception e)
			{
				// Auto-generated catch block
				LOG.error("message : ", e);
			}
		}
		else
		{
			LOG.warn("--------- Disabling createEssentialData lucenesearch as it slows down the update system process -----");
			LOG.warn("--------- lucene search only needed for quick search in hmc. Can be disabled -----");
		}
	}
}
