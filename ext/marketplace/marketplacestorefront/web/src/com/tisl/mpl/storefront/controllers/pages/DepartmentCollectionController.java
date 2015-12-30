/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.util.ExceptionUtil;


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


		//if (!Collections.isEmpty(component.getDepartmentCollection()))
		//{
		//final List<CategoryModel> departmentList = (List<CategoryModel>) component.getDepartmentCollection();
		try
		{
			//for (final CategoryModel category : departmentList)
			//{
			final CategoryModel department = categoryService.getCategoryForCode(departmentCode);

			// Fetch all the second level categories for a particular
			// category
			final Collection<CategoryModel> secondLevelCategories = department.getCategories();

			// Iterating through the second level categories
			for (final CategoryModel secondLevelCategory : secondLevelCategories)
			{
				// Fetching the third level category against a second
				// level category
				final Collection<CategoryModel> thirdLevelCategory = secondLevelCategory.getCategories();
				// Storing the third level categories in a map
				thirdLevelCategoryMap.put(secondLevelCategory.getCode(), thirdLevelCategory);
			}

			// Storing the second level categories in a map
			secondLevelCategoryMap.put(department.getCode(), secondLevelCategories);

			//}
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
