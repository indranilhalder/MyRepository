/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.enums.ExpressDeliveryEnum;
import com.tisl.mpl.core.enums.HomeDeliveryEnum;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */
//Index deliveryMode for a PcmProductVariantModel
public class MplDeliveryModeValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;

	//	@Autowired
	//	private ConfigurationService configurationService;

	/**
	 * @return Collection<fieldValues>
	 * @param indexConfig
	 *           ,indexedProperty,model
	 * @description: It returns if a specific variant product is new or not
	 *
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final ProductModel productModel = (ProductModel) model;
		if (productModel == null)
		{
			return Collections.emptyList();
		}

		final Set<String> deliveryModes = new HashSet<String>();

		final Collection<SellerInformationModel> sellerInfoRelColl = productModel.getSellerInformationRelator();
		for (final SellerInformationModel sellerInfo : sellerInfoRelColl)
		{

			for (final RichAttributeModel richAttr : sellerInfo.getRichAttribute())
			{
				if (HomeDeliveryEnum.YES.equals(richAttr.getHomeDelivery()))
				{
					deliveryModes.add("home-delivery");
				}
				if (ExpressDeliveryEnum.YES.equals(richAttr.getExpressDelivery()))
				{
					deliveryModes.add("express-delivery");
				}
				// Click and collect option is commented out for release 1
				//if (ClickAndCollectEnum.YES.equals(richAttr.getClickAndCollect()))
				//{
				//	deliveryModes.add("click-and-collect");
				//}
			}

		}

		if (deliveryModes.isEmpty())
		{
			return Collections.emptyList();
		}
		final Iterator<String> deliveryModesItr = deliveryModes.iterator();
		final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
		while (deliveryModesItr.hasNext())
		{
			fieldValues.addAll(createFieldValue(deliveryModesItr.next(), indexedProperty));
		}

		return fieldValues;

	}

	/**
	 * @return fieldValues
	 * @param deliveryMode
	 *           ,indexedProperty
	 * @description: It create deliveryMode field with adding index property
	 *
	 */
	protected List<FieldValue> createFieldValue(final String deliveryMode, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Object value = deliveryMode;
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
		return fieldValues;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}
}