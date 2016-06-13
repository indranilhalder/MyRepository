/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import de.hybris.platform.category.model.CategoryModel;

import java.net.URLDecoder;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.core.model.DepartmentCollectionComponentModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.HomepageComponentService;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;



/**
 * @author TCS
 *
 */

@Controller("DepartmentCollectionComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.DepartmentCollectionComponent)
public class DepartmentCollectionComponentController extends AbstractCMSComponentController<DepartmentCollectionComponentModel>
{

	@Autowired
	private HomepageComponentService homepageComponentService;

	/**
	 * This method fetches all the departments in the component and finds out the second level and third level
	 * hierarchies
	 *
	 * @param request
	 * @param model
	 * @param component
	 *
	 */

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final DepartmentCollectionComponentModel component)
	{
		try
		{
			final Collection<CategoryModel> department = component.getDepartmentCollection();
			String categoryPathFirst = null;

			for (final CategoryModel dept : department)
			{

				categoryPathFirst = GenericUtilityMethods.buildPathString(homepageComponentService.getCategoryPath(dept));
				if (StringUtils.isNotEmpty(categoryPathFirst))
				{
					categoryPathFirst = URLDecoder.decode(categoryPathFirst, "UTF-8");
					categoryPathFirst = categoryPathFirst.toLowerCase();
					categoryPathFirst = GenericUtilityMethods.changeUrl(categoryPathFirst);
					dept.setName(dept.getName() + "||" + categoryPathFirst);
				}
			}
			model.addAttribute("departmentList", department);


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
	}

}
