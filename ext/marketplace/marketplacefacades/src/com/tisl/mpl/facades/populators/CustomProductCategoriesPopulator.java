/**
 * @author TCS
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.converters.populator.ProductCategoriesPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.product.ExchangeGuideFacade;



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

	protected static final Logger LOG = Logger.getLogger(CustomProductCategoriesPopulator.class);

	//Exchange Changes
	@Resource(name = "exchangeGuideFacade")
	private ExchangeGuideFacade exchangeGuideFacade;


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
		//For TPR:4847: size facet clubbing for kidswear
		if (StringUtils.isNotEmpty(productModel.getProductCategoryTypeL2()))
		{
			productData.setRootCategoryL2((productModel.getProductCategoryTypeL2()));
		}
		//For TPR:4847: size facet clubbing for kidswear end

		//		  final Collection<CategoryModel> categories = getCommerceProductService()
		//		  .getSuperCategoriesExceptClassificationClassesForProduct(productModel);
		final List<CategoryModel> resultList = new ArrayList<>();
		// For TISSQAUAT-665
		for (final CategoryModel categoryModel : productModel.getSupercategories())
		{
			resultList.add(categoryModel);
		}
		//final Collection<CategoryModel> categories = getCommerceProductService()
		//	.getSuperCategoriesExceptClassificationClassesForProduct(productModel);
		productData.setCategories(Converters.convertAll(resultList, getCategoryConverter()));
		//TPR-1083 Changes Exchange Start
		//Check if Exchange is Allowed from properties file


		final Boolean exchangeAllowed = configurationService.getConfiguration().getBoolean("mpl.exchange.enabled", Boolean.FALSE);
		final List<String> exchangeAllowedRootCategory = Arrays.asList(configurationService.getConfiguration()
				.getString("mpl.exchange.enabled.rootCategory", "Electronics").split(","));


		if (exchangeAllowed.booleanValue() && exchangeAllowedRootCategory.contains(productData.getRootCategory()))
		{
			CategoryModel l4category = null;
			for (final CategoryModel cat : productModel.getSupercategories())
			{ //Fetch Hierarchy Based on Local Properties
				if (cat.getCode().startsWith(configurationService.getConfiguration().getString("mpl.exchange.hierarchy", "MPH")))
				{
					l4category = cat;
				}
			}

			populateL3(l4category.getSupercategories(), productData);

		}

		//TPR-1083 Changes Exchange End




		//commented as a fix for TISSQAUAT-665
		/**
		 * final Collection<CategoryModel> categories = getCommerceProductService()
		 * .getSuperCategoriesExceptClassificationClassesForProduct(productModel);
		 * productData.setCategories(Converters.convertAll(categories, getCategoryConverter()));
		 **/



	}

	/**
	 * @param source
	 * @param target
	 *
	 */
	private void populateL3(final Collection<CategoryModel> source, final ProductData target)
	{
		for (final CategoryModel category : source)
		{

			if (category.getCode().startsWith(configurationService.getConfiguration().getString("mpl.exchange.hierarchy", "MPH"))
					&& !(category instanceof ClassificationClassModel) && exchangeGuideFacade.isExchangable(category.getCode()))
			{

				target.setLevel3CategoryCode(category.getCode());
				target.setLevel3CategoryName(category.getName());

				break;
			}

		}
	}
}
