/**
 *
 */
package com.tisl.mpl.solrfacet.search.service.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.ValueRange;
import de.hybris.platform.solrfacetsearch.config.ValueRangeSet;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.IndexedTypeFieldsValuesProvider;
import de.hybris.platform.solrfacetsearch.search.Facet;
import de.hybris.platform.solrfacetsearch.search.FacetValue;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.DefaultFacetSearchService;
import de.hybris.platform.solrfacetsearch.search.impl.SearchResultConverters;
import de.hybris.platform.solrfacetsearch.search.impl.SolrSearchResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.BooleanUtils;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;


public class MplDefaultFacetSearchService extends DefaultFacetSearchService
{
	//TPR-4947 performance fix start
	private SearchResultConverters searchResultConverters;

	/**
	 * @param searchResultConverters
	 *           the searchResultConverters to set
	 */
	@Override
	@Required
	public void setSearchResultConverters(final SearchResultConverters searchResultConverters)
	{
		this.searchResultConverters = searchResultConverters;
	}

	/**
	 * @param fieldNameProvider
	 *           the fieldNameProvider to set
	 */
	@Override
	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	private FieldNameProvider fieldNameProvider;
	@Autowired
	private ConfigurationService configurationService;

	@Override
	protected SolrSearchResult convertResponse(final QueryResponse queryResponse, final SearchQuery query,
			final IndexedType indexedType)
	{

		final String configurationFA = configurationService.getConfiguration().getString("search.facet.name");
		final Map facets = new TreeMap();
		final List<FacetField> facetFields = queryResponse.getFacetFields();
		IndexedProperty indexedProperty;/*     */
		List facetValues;
		if (facetFields != null)
		{
			for (final FacetField facetField : facetFields)
			{
				final String facetFieldName = getFacetFieldName(indexedType, facetField);
				indexedProperty = indexedType.getIndexedProperties().get(facetFieldName);
				if (!(indexedProperty.isVisible()) && configurationFA.contains(indexedProperty.getName()))
				{
					continue;
				}
				final List<Count> values = facetField.getValues();
				if (values == null || values.isEmpty())
				{
					continue;
				}
				facetValues = new ArrayList();

				for (final FacetField.Count count : values)
				{
					final boolean selected = isFacetValueSelected(query.getAllFields(), facetFieldName, count.getName());
					final String displayName = getFacetValueDisplayName(query, indexedProperty, count.getName());
					if (displayName == null)
					{
						facetValues.add(new FacetValue(count.getName(), count.getCount(), selected));
					}
					else
					{
						facetValues.add(new FacetValue(count.getName(), displayName, count.getCount(), selected));
					}
				}

				if (indexedProperty != null)
				{
					facetValues = sortFacetValues(indexedType, indexedProperty, facetValues, query);
				}
				final Facet facet = new Facet(facetFieldName, facetValues);
				facets.put(facetFieldName, facet);

			}

		}
		final int pageSize = query.getPageSize();
		final int offset = query.getOffset();
		final ArrayList breadcrumbs = new ArrayList(query.getBreadcrumbs());

		final SimpleOrderedMap params = (SimpleOrderedMap) queryResponse.getResponseHeader().get("params");
		final String group = (String) params.get("group");
		final boolean isGrouped = BooleanUtils.toBoolean(group);

		final long numberOfResults = (isGrouped) ? queryResponse.getGroupResponse().getValues().get(0).getNGroups().longValue()
				: queryResponse.getResults().getNumFound();
		final List identifiers = new ArrayList();
		//GroupCommand command;
		if (isGrouped)
		{
			final GroupResponse groups = queryResponse.getGroupResponse();
			//	GroupCommand command;
			//	for (command = groups.getValues().iterator(); indexedProperty.hasNext();)
			for (final GroupCommand command : groups.getValues())
			{
				//command = (GroupCommand) indexedProperty.next();

				for (final Group g : command.getValues())
				{
					for (final SolrDocument doc : g.getResult())
					{
						identifiers.add(doc.getFieldValue("id"));
					}
				}
			}
		}
		else
		{
			for (final SolrDocument solrDocument : queryResponse.getResults())
			{
				identifiers.add(solrDocument.getFieldValue("id"));
			}
		}

		if (this.searchResultConverters == null)
		{
			return new SolrSearchResult(numberOfResults, facets, indexedType, identifiers, queryResponse, pageSize, offset,
					breadcrumbs, query, null);
		}
		return new SolrSearchResult(numberOfResults, facets, indexedType, identifiers, queryResponse, pageSize, offset,
				breadcrumbs, query, null, this.searchResultConverters.getConverterMapping(indexedType.getCode()));
	}

	private String getFacetFieldName(final IndexedType indexedType, final FacetField facetField)
	{
		final IndexedTypeFieldsValuesProvider fieldsValuesProvider = getFieldsValuesProvider(indexedType);
		if (fieldsValuesProvider != null)
		{
			final Map fieldNamesMapping = fieldsValuesProvider.getFieldNamesMapping();
			if (fieldNamesMapping.containsKey(facetField.getName()))
			{
				return ((String) fieldNamesMapping.get(facetField.getName()));
			}
		}
		return this.fieldNameProvider.getPropertyName(facetField.getName());
	}

	//TPR-4947 performance fix end

	@Override
	protected List<ValueRange> getValueRanges(final IndexedProperty property, final String qualifier)
	{
		List<ValueRange> valueRangesList = new ArrayList<ValueRange>();
		ValueRangeSet valueRangeSet;
		// Customized mpl price range will be taken
		if (property.getName().equalsIgnoreCase("price"))
		{
			//			if (MapUtils.isNotEmpty(property.getValueRangeSets()))
			//			{
			//				for (final Map.Entry<String, ValueRangeSet> entry : property.getValueRangeSets().entrySet())
			//				{
			//					valueRangesList.addAll(entry.getValue().getValueRanges());
			//				}
			//			}
			//commented for SDI-575/576
			valueRangeSet = property.getValueRangeSets().get("INR-APPAREL");
			if (valueRangeSet != null)
			{
				valueRangesList = valueRangeSet.getValueRanges();
			}

			valueRangeSet = property.getValueRangeSets().get("INR-ELECTRONICS");
			if (valueRangeSet != null)
			{
				valueRangesList.addAll(valueRangeSet.getValueRanges());
			}

			// JEWELLERY CHANGES START
			valueRangeSet = property.getValueRangeSets().get("INR-FASHIONJEWELLERY");
			if (valueRangeSet != null)
			{
				valueRangesList.addAll(valueRangeSet.getValueRanges());
			}
			// JEWELLERY CHANGES END

			valueRangeSet = property.getValueRangeSets().get("INR-LUXURY");
			if (valueRangeSet != null)
			{
				valueRangesList.addAll(valueRangeSet.getValueRanges());
			}

			return valueRangesList;
		}
		else if (property.getName().equalsIgnoreCase("discountFlag"))
		{
			valueRangeSet = property.getValueRangeSets().get("DISCOUNT");
			if (valueRangeSet != null)
			{
				valueRangesList = valueRangeSet.getValueRanges();
			}
			return valueRangesList;
		}
		else
		{

			if (qualifier == null)
			{
				valueRangeSet = property.getValueRangeSets().get("default");
			}
			else
			{
				valueRangeSet = property.getValueRangeSets().get(qualifier);
				if (valueRangeSet == null)
				{
					valueRangeSet = property.getValueRangeSets().get("default");
				}
			}

			if (valueRangeSet != null)
			{
				valueRangesList = valueRangeSet.getValueRanges();
				return valueRangesList;
			}

		}


		return Collections.emptyList();
	}
}
