/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.brand.impl;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.tisl.mpl.marketplacecommerceservices.daos.brand.BrandDao;
import com.tisl.mpl.marketplacecommerceservices.service.brand.BrandService;


/**
 * @author TCS
 *
 */
public class DefaultBrandService implements BrandService
{

	@Resource(name = "categoryService")
	private CategoryService categoryService;

	@Resource(name = "brandDao")
	BrandDao brandDao;

	@Override
	public Map<Character, List<CategoryModel>> getAllBrandsInAplhabeticalOrder(final String rootBrandCode)
	{


		Collection<CategoryModel> subBrandList = new ArrayList<CategoryModel>();
		final CategoryModel categoryModel = categoryService.getCategoryForCode(rootBrandCode);

		List<CategoryModel> subcategoryList = new ArrayList<CategoryModel>();

		subcategoryList = categoryModel.getCategories();



		final ConcurrentMap<Character, List<CategoryModel>> alphabeticalSubBrands = new ConcurrentHashMap();


		for (final CategoryModel category : subcategoryList)
		{
			subBrandList = category.getAllSubcategories();
			//if (subBrandList.size() > 0)
			if (CollectionUtils.isNotEmpty(subBrandList))
			{
				for (final CategoryModel subBrand : subBrandList)
				{ //gets the first character from a sub brand name

					final char firstCharacter = subBrand.getName().charAt(0);
					final Character firstChar = new Character(firstCharacter);
					if (alphabeticalSubBrands.isEmpty())
					{
						//Creating the first entry for an empty map
						final List<CategoryModel> subBrandModelList = new ArrayList();
						subBrandModelList.add(subBrand);
						alphabeticalSubBrands.put(firstChar, subBrandModelList);
					}
					else
					{
						boolean exists = false;

						for (final Entry<Character, List<CategoryModel>> entry : alphabeticalSubBrands.entrySet())
						{
							if (entry.getKey().equals(firstChar))
							{

								final List<CategoryModel> value = entry.getValue();
								value.add(subBrand);
								entry.setValue(value);

								exists = true;
							}

						}
						if (!exists)
						{
							//if the entry doesn't contain any existing entry
							final List<CategoryModel> subBrandModelList = new ArrayList();
							subBrandModelList.add(subBrand);
							alphabeticalSubBrands.put(firstChar, subBrandModelList);
						}

					}
				}
			}



		}




		final Map<Character, List<CategoryModel>> sortedMap = new TreeMap<Character, List<CategoryModel>>(alphabeticalSubBrands);

		return sortedMap;
	}

	/**
	 * this method checks whether the email id exists or not
	 *
	 * @param emailId
	 * @return boolean value
	 */
	@Override
	public boolean checkEmailId(final String emailId)
	{
		// YTODO Auto-generated method stub
		return brandDao.checkEmailId(emailId);
	}

}
