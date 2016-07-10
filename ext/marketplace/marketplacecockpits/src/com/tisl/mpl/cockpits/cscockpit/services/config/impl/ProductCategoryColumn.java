package com.tisl.mpl.cockpits.cscockpit.services.config.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.zkoss.spring.SpringUtil;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cscockpit.services.config.impl.AbstractProductCustomColumn;
import de.hybris.platform.servicelayer.config.ConfigurationService;

public class ProductCategoryColumn extends AbstractProductCustomColumn {

	private ConfigurationService configurationService;
	
	private CategoryService categoryService;
	
	
	public CategoryService getCategoryService() {
		if(categoryService ==null)
			categoryService = ((CategoryService)SpringUtil.getBean("categoryService"));
		return categoryService;
	}



	public ConfigurationService getConfigurationService() {
		if(configurationService ==null)
			configurationService = ((ConfigurationService)SpringUtil.getBean("configurationService"));
		return configurationService;
	}


	protected String getProductValue(ProductModel product, Locale locale) {
		final List<String> breadcrumbs = new ArrayList<>();
		if (product != null) {
			final Collection<CategoryModel> categoryModels = new ArrayList<>();
			final ProductModel baseProductModel = getBaseProduct(product);
			
			categoryModels.addAll(baseProductModel!=null ? baseProductModel.getSupercategories() : product.getSupercategories());
			
			while (!categoryModels.isEmpty())
			{
				CategoryModel toDisplay = null;
				for (final CategoryModel categoryModel : categoryModels)
				{
					//if (!(categoryModel instanceof ClassificationClassModel))
						if (categoryModel.getCode().startsWith(
								getConfigurationService().getConfiguration().getString("marketplace.mplcatalog.salescategory.code")))
					{
						if (toDisplay == null)
						{
							toDisplay = categoryModel;
						}
						
					}
				}
				
				categoryModels.clear();
				if (toDisplay != null)
				{
					if (!getCategoryService().isRoot(toDisplay)){
						breadcrumbs.add(StringUtils.isNotEmpty(toDisplay.getName())? toDisplay.getName():toDisplay.getCode() );
						categoryModels.addAll(toDisplay.getSupercategories());
					}
				}
					
			}
			Collections.reverse(breadcrumbs);
			
			
			StringBuilder categoryList = new StringBuilder("->");
			for (String categoryName : breadcrumbs) {
				categoryList.append(categoryName).append("->");
			}
			try{
			String categories = categoryList.substring(2, categoryList.length() - 2);
			
			if (!(StringUtils.isBlank(categories))) {
				return categories;
			}
			}catch(Exception e){
				//
			}
		}
		return "";
	}
}
