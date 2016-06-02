/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
	private CommerceCategoryService commerceCategoryService;


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

					String categoryPathThird = buildPathString(getCategoryPath(thirdLevelCategories));
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
				String categoryPath = buildPathString(getCategoryPath(secondLevelCategory));
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

	protected List<CategoryModel> getCategoryPath(final CategoryModel category)
	{
		final Collection<List<CategoryModel>> path = commerceCategoryService.getPathsForCategory(category);
		if (null != path)
		{
			for (final List<CategoryModel> path1 : path)
			{
				if (path1.size() > 1)
				{
					path1.remove(0);
				}
			}
		}

		return (path.iterator().next());
	}

	protected String buildPathString(final List<CategoryModel> path)
	{
		final StringBuilder result = new StringBuilder();

		for (int i = 0; i < path.size(); ++i)
		{
			if (i != 0)
			{
				result.append('-');
			}
			result.append(urlSafe(path.get(i).getName()));
		}

		return result.toString();
	}

	protected String urlSafe(final String text)
	{
		if ((text == null) || (text.isEmpty()))
		{
			return "";
		}
		String encodedText;
		try
		{
			encodedText = URLEncoder.encode(text, "utf-8");
		}
		catch (final UnsupportedEncodingException encodingException)
		{
			encodedText = text;
			//	LOG.debug(encodingException.getMessage());
		}

		String cleanedText = encodedText;
		cleanedText = cleanedText.replaceAll("%2F", "/");
		cleanedText = cleanedText.replaceAll("[^%A-Za-z0-9\\-]+", "-");
		return cleanedText;
	}
}
