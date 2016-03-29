/**
 * @author TCS
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.converters.populator.ProductCategoriesPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import java.util.Collection;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;



/**
 *
 * @author TCS
 *
 * @param <SOURCE>
 * @param <TARGET>
 */
public class CustomProductCategoriesPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		ProductCategoriesPopulator<SOURCE, TARGET>
{

	@Resource
	private ConfigurationService configurationService;

	protected static final Logger LOG = Logger.getLogger(CustomProductBasicPopulator.class);


	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * Finding category name from category
	 *
	 * @param productModel
	 * @param productData
	 * @throws ConversionException
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException,
			EtailNonBusinessExceptions
	{

		//product super category like electronics,clothing are being populated by interceptor.
		productData.setRootCategory(productModel.getProductCategoryType());
		
		  final Collection<CategoryModel> categories = getCommerceProductService()
		  .getSuperCategoriesExceptClassificationClassesForProduct(productModel);
		  productData.setCategories(Converters.convertAll(categories, getCategoryConverter()));
		 
	}


}
