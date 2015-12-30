package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.util.Feature;
import de.hybris.platform.catalog.jalo.classification.util.FeatureContainer;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.impl.ClassificationPropertyValueProvider;

import java.util.Collection;
import java.util.Collections;

import com.tisl.mpl.core.model.PcmProductVariantModel;


public class ClassificationValueProvider extends ClassificationPropertyValueProvider
{
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof PcmProductVariantModel)
		{
			final ClassAttributeAssignmentModel classAttributeAssignmentModel = indexedProperty.getClassAttributeAssignment();
			final ClassAttributeAssignment classAttributeAssignment = (ClassAttributeAssignment) this.modelService
					.getSource(classAttributeAssignmentModel);

			final Product product = (Product) this.modelService.getSource(model);
			final FeatureContainer cont = FeatureContainer.loadTyped(product, new ClassAttributeAssignment[]
			{ classAttributeAssignment });
			if (cont.hasFeature(classAttributeAssignment))
			{
				final Feature feature = cont.getFeature(classAttributeAssignment);
				if ((feature == null) || (feature.isEmpty()))
				{
					return Collections.emptyList();
				}

				return getFeaturesValues(indexConfig, feature, indexedProperty);
			}

			return Collections.emptyList();
		}

		throw new FieldValueProviderException("Cannot provide classification property of non-product item");
	}
}