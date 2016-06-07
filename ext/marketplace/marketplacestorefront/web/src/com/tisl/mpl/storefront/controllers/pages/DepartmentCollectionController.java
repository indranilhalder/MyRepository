/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;

import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.HomepageComponentService;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 *
 */
@Controller
@Scope("tenant")
@RequestMapping(value = RequestMappingUrlConstants.DEPARTMENT_COLLECTION)
public class DepartmentCollectionController
{

	@Resource(name = "categoryService")
	private CategoryService categoryService;

	@Autowired
	private HomepageComponentService homepageComponentService;


	/**
	 * This method fetches all the departments in the component and finds out the second level and third level
	 * hierarchies
	 *
	 * @param request
	 * @param model
	 *
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String get(@RequestParam("department") final String departmentCode, final Model model, final HttpServletRequest request)
	{

		final Map<String, Collection<CategoryModel>> secondLevelCategoryMap = new HashMap();
		final Map<String, Collection<CategoryModel>> thirdLevelCategoryMap = new HashMap();

		try
		{
			final CategoryModel department = categoryService.getCategoryForCode(departmentCode);

			final Collection<CategoryModel> secondLevelCategories = department.getCategories();

			for (final CategoryModel secondLevelCategory : secondLevelCategories)
			{
				final Collection<CategoryModel> thirdLevelCategory = secondLevelCategory.getCategories();

				for (final CategoryModel thirdLevelCategories : thirdLevelCategory)
				{

					String categoryPathThird = GenericUtilityMethods.buildPathString(homepageComponentService
							.getCategoryPath(thirdLevelCategories));
					if (StringUtils.isNotEmpty(categoryPathThird))
					{
						categoryPathThird = URLDecoder.decode(categoryPathThird, "UTF-8");
						categoryPathThird = categoryPathThird.toLowerCase();
						categoryPathThird = GenericUtilityMethods.changeUrl(categoryPathThird);
					}

					thirdLevelCategories.setName(thirdLevelCategories.getName() + "||" + categoryPathThird);
				}

				// Storing the third level categories in a map
				thirdLevelCategoryMap.put(secondLevelCategory.getCode(), thirdLevelCategory);
				String categoryPath = GenericUtilityMethods.buildPathString(homepageComponentService
						.getCategoryPath(secondLevelCategory));
				if (StringUtils.isNotEmpty(categoryPath))
				{
					categoryPath = URLDecoder.decode(categoryPath, "UTF-8");
					categoryPath = categoryPath.toLowerCase();
					categoryPath = GenericUtilityMethods.changeUrl(categoryPath);
				}

				secondLevelCategory.setName(secondLevelCategory.getName() + "||" + categoryPath);


			}

			// Storing the second level categories in a map
			secondLevelCategoryMap.put(department.getCode(), secondLevelCategories);

			model.addAttribute(ModelAttributetConstants.FIRST_LEVEL_CATEGORY, department);
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

		model.addAttribute(ModelAttributetConstants.SECOND_LEVEL_CATEGORY, secondLevelCategoryMap);
		model.addAttribute(ModelAttributetConstants.THIRD_LEVEL_CATEGORY, thirdLevelCategoryMap);

		//}
		return ControllerConstants.Views.Fragments.Home.DepartmentCollection;
	}

}
