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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */
public class MplSellerIdValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;
	private BuyBoxService buyBoxService;


	/**
	 * @return Collection<fieldValues>
	 * @param indexConfig
	 *           ,indexedProperty,model
	 * @description: It returns the seller Id of a specific product
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

		final Set<String> uniqueSellerIdList = new HashSet<String>();
		final List<SellerInformationModel> sellers = (List<SellerInformationModel>) productModel.getSellerInformationRelator();
		Set<Map<BuyBoxModel, RichAttributeModel>> sellerDetailsSet = null;
		try
		{
			sellerDetailsSet = buyBoxService.getsellersDetails(productModel.getCode());
			if (sellerDetailsSet != null)
			{
				for (final Map<BuyBoxModel, RichAttributeModel> resultMap : sellerDetailsSet)
				{
					for (final Map.Entry<BuyBoxModel, RichAttributeModel> entry : resultMap.entrySet())
					{
						final BuyBoxModel buyBox = entry.getKey();
						final boolean sellerStatus = checkSellerAvailability(buyBox.getSellerId(), sellers);
						if (sellerStatus)
						{
							uniqueSellerIdList.add(buyBox.getSellerId());
						}
					}
				}
			}

			if (!uniqueSellerIdList.isEmpty())
			{

				final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
				for (final String seller : uniqueSellerIdList)
				{
					fieldValues.addAll(createFieldValue(seller, indexedProperty));
				}

				return fieldValues;
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			return Collections.emptyList();
		}
		catch (final EtailBusinessExceptions e)
		{
			return Collections.emptyList();
		}

		return Collections.emptyList();
	}


	public boolean checkSellerAvailability(final String buyboxSeller, final List<SellerInformationModel> sellers)
	{
		for (final SellerInformationModel sellerInformationModel : sellers)
		{
			if (buyboxSeller.equalsIgnoreCase(sellerInformationModel.getSellerID()))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @return fieldValues
	 * @param seller
	 *           ,indexedProperty
	 * @description: It create seller field with adding index property
	 *
	 */
	protected List<FieldValue> createFieldValue(final String seller, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Object value = seller;
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

	/**
	 * @return the buyBoxService
	 */
	public BuyBoxService getBuyBoxService()
	{
		return buyBoxService;
	}

	/**
	 * @param buyBoxService
	 *           the buyBoxService to set
	 */
	public void setBuyBoxService(final BuyBoxService buyBoxService)
	{
		this.buyBoxService = buyBoxService;
	}
}
