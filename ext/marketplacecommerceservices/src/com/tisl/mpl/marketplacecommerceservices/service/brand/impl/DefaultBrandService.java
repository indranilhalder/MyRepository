/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.brand.impl;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;

import java.net.URLDecoder;
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
import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.core.model.BrandComponentModel;
import com.tisl.mpl.core.model.PriorityBrandsModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.brand.BrandDao;
import com.tisl.mpl.marketplacecommerceservices.service.brand.BrandService;
import com.tisl.mpl.model.cms.components.MplNewsLetterSubscriptionModel;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;


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


	@Resource(name = "cmsComponentService")
	private CMSComponentService cmsComponentService;

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

	@Override
	public List<MplNewsLetterSubscriptionModel> checkEmailIdForluxury(final String emailId)
	{
		// YTODO Auto-generated method stub
		return brandDao.checkEmailIdForluxury(emailId);
	}


	/*
	 * public List<MplNewsLetterSubscriptionModel> checkEmailIdForMarketplace(final String emailId) { return
	 * brandDao.checkEmailIdForMarketplace(emailId); }
	 */

	/**
	 * @description method fetches all the brands from cmscockpit and sorts all the brands as per first alphabet
	 *
	 * @return sortMp
	 */

	@Override
	public Map<Character, List<CategoryModel>> getAllBrandsFromCmsCockpit(final String componentUid)
	{

		Map<Character, List<CategoryModel>> sortMp = null;
		Collection<CategoryModel> subBrandList = new ArrayList<CategoryModel>();
		SimpleCMSComponentModel component = null;
		try
		{
			if (StringUtils.isNotEmpty(componentUid))
			{
				component = cmsComponentService.getSimpleCMSComponent(componentUid);
				final BrandComponentModel brandComponent = (BrandComponentModel) component;

				if (null != brandComponent)
				{
					subBrandList = brandComponent.getSubBrands();
					sortMp = getBrandsInAplhabeticalOrder(subBrandList);

				}
				for (final CategoryModel categoryModel : subBrandList)
				{
					String categoryPath = GenericUtilityMethods.urlSafe(categoryModel.getName());
					if (StringUtils.isNotEmpty(categoryPath))
					{
						categoryPath = URLDecoder.decode(categoryPath, "UTF-8");
						categoryPath = categoryPath.toLowerCase();
						categoryPath = GenericUtilityMethods.changeUrl(categoryPath);
					}
					categoryModel.setName(categoryModel.getName() + "||" + categoryPath);
				}
			}
		}
		catch (final EtailBusinessExceptions businessException)
		{
			ExceptionUtil.etailBusinessExceptionHandler(businessException, null);
		}
		catch (final EtailNonBusinessExceptions nonBusinessException)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(nonBusinessException);
		}
		catch (final Exception exception)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(exception));
		}


		return sortMp;


	}

	/**
	 * @description method takes list of brands as a parameter and sort them as per their first alphabet
	 *
	 * @param subBrandList
	 * @return sortedMap
	 */


	private Map<Character, List<CategoryModel>> getBrandsInAplhabeticalOrder(final Collection<CategoryModel> subBrandList)
	{

		final ConcurrentMap<Character, List<CategoryModel>> alphabeticalSubBrands = new ConcurrentHashMap();

		if (null != subBrandList)
		{
			for (final CategoryModel category : subBrandList)
			{
				final char firstCharacter = category.getName().charAt(0);
				final Character firstChar = new Character(firstCharacter);
				if (alphabeticalSubBrands.isEmpty())
				{
					//Creating the first entry for an empty map
					final List<CategoryModel> subBrandModelList = new ArrayList();
					subBrandModelList.add(category);
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
							value.add(category);
							entry.setValue(value);

							exists = true;
						}

					}
					if (!exists)
					{
						//if the entry doesn't contain any existing entry
						final List<CategoryModel> subBrandModelList = new ArrayList();
						subBrandModelList.add(category);
						alphabeticalSubBrands.put(firstChar, subBrandModelList);
					}
				}
			}
		}
		final Map<Character, List<CategoryModel>> sortedMap = new TreeMap<Character, List<CategoryModel>>(alphabeticalSubBrands);
		return sortedMap;
	}

	/**
	 * @param emailId
	 * @return
	 */
	@Override
	public List<MplNewsLetterSubscriptionModel> checkEmailIdForMarketplace(final String emailId)
	{
		// YTODO Auto-generated method stub
		return brandDao.checkEmailIdForMarketplace(emailId);
	}

	
	/**
	 * @param emailId
	 * @param isLuxury
	 * @return
	 */
	@Override
	public List<MplNewsLetterSubscriptionModel> checkEmailIdForluxury(final String emailId, final String isLuxury)
	{
		// YTODO Auto-generated method stub
		return brandDao.checkEmailIdForluxury(emailId, isLuxury);

	}

	
	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.brand.BrandService#priorityBrands()
	 */
	@Override
	public List<PriorityBrandsModel> priorityBrands(final String categoryCode)
	{
		// YTODO Auto-generated method stub
		return brandDao.priorityBrands(categoryCode);
	}

}
