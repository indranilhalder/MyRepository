/**
 *
 */
package com.tisl.mpl.facade.search.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.model.config.SolrEndpointSlaveUrlModel;

import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.facades.search.data.EndpointSlaveURL;


/**
 * @author TCS
 *
 */
public class DefaultEndpointSlaveUrlPopulator implements Populator<SolrEndpointSlaveUrlModel, EndpointSlaveURL>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final SolrEndpointSlaveUrlModel source, final EndpointSlaveURL target) throws ConversionException
	{
		// YTODO Auto-generated method stub
		if (StringUtils.isNotEmpty(source.getUrl()))
		{
			target.setUrl(source.getUrl());
		}

		target.setSlave(source.isSlave());
	}

}