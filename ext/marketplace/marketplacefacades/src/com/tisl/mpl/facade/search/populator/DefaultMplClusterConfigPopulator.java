/**
 *
 */
package com.tisl.mpl.facade.search.populator;


import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.config.ClusterConfig;
import de.hybris.platform.solrfacetsearch.converters.populator.DefaultClusterConfigPopulator;
import de.hybris.platform.solrfacetsearch.model.config.SolrEndpointSlaveUrlModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.facades.search.data.EndpointSlaveURL;


/**
 * @author TCS
 *
 */
public class DefaultMplClusterConfigPopulator extends DefaultClusterConfigPopulator
{

	private Converter<SolrEndpointSlaveUrlModel, EndpointSlaveURL> endpointSlaveUrlConverter;

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final SolrServerConfigModel source, final ClusterConfig target) throws ConversionException
	{
		// YTODO Auto-generated method stub
		super.populate(source, target);
		if (CollectionUtils.isNotEmpty(source.getSolrEndpointUrlsSlave()))
		{
			target.setEndpointSlaveURLs(populateEndpointSlaveUrls(source));
		}
	}


	protected List<EndpointSlaveURL> populateEndpointSlaveUrls(final SolrServerConfigModel source)
	{
		if (CollectionUtils.isNotEmpty(source.getSolrEndpointUrlsSlave()))
		{
			return Converters.convertAll(source.getSolrEndpointUrlsSlave(), endpointSlaveUrlConverter);
		}
		return Collections.emptyList();
	}

	/**
	 * @param endpointSlaveUrlConverter
	 *           the endpointSlaveUrlConverter to set
	 */
	@Required
	public void setEndpointSlaveUrlConverter(final Converter<SolrEndpointSlaveUrlModel, EndpointSlaveURL> endpointSlaveUrlConverter)
	{
		this.endpointSlaveUrlConverter = endpointSlaveUrlConverter;
	}

}