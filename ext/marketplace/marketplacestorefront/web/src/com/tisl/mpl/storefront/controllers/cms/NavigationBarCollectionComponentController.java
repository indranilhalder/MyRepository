///*
// * [y] hybris Platform
// *
// * Copyright (c) 2000-2016 hybris AG
// * All rights reserved.
// *
// * This software is the confidential and proprietary information of hybris
// * ("Confidential Information"). You shall not disclose such Confidential
// * Information and shall use it only in accordance with the terms of the
// * license agreement you entered into with hybris.
// *
// *
// */
//package com.tisl.mpl.storefront.controllers.cms;
//
//import de.hybris.platform.acceleratorcms.model.components.NavigationBarCollectionComponentModel;
//import de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel;
//import de.hybris.platform.category.model.CategoryModel;
//
//import java.net.URLDecoder;
//import java.util.ArrayList;
//import java.util.Collection;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.tisl.mpl.exception.EtailBusinessExceptions;
//import com.tisl.mpl.exception.EtailNonBusinessExceptions;
//import com.tisl.mpl.marketplacecommerceservices.service.HomepageComponentService;
//import com.tisl.mpl.storefront.controllers.ControllerConstants;
//import com.tisl.mpl.util.ExceptionUtil;
//import com.tisl.mpl.util.GenericUtilityMethods;
//
//
///**
// * @author TCS
// *
// */
//@Controller("NavigationBarCollectionComponentController")
//@Scope("tenant")
//@RequestMapping(value = ControllerConstants.Actions.Cms.NavigationBarCollectionComponent)
//public class NavigationBarCollectionComponentController extends
//		AbstractCMSComponentController<NavigationBarCollectionComponentModel>
//{
//	@Autowired
//	private HomepageComponentService homepageComponentService;
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see
//	 * com.tisl.mpl.storefront.controllers.cms.AbstractCMSComponentController#fillModel(javax.servlet.http.HttpServletRequest
//	 * , org.springframework.ui.Model, de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)
//	 */
//	@Override
//	protected void fillModel(final HttpServletRequest request, final Model model,
//			final NavigationBarCollectionComponentModel component)
//	{
//		try
//		{
//			final ArrayList<CategoryModel> departments = new ArrayList<CategoryModel>();
//			final Collection<NavigationBarComponentModel> navigationBars = component.getComponents();
//			for (final NavigationBarComponentModel navigationBar : navigationBars)
//			{
//				final CategoryModel department = navigationBar.getLink().getCategory();
//				departments.add(department);
//			}
//
//			String categoryPathFirst = null;
//
//			for (final CategoryModel dept : departments)
//			{
//
//				categoryPathFirst = GenericUtilityMethods.buildPathString(homepageComponentService.getCategoryPath(dept));
//				if (StringUtils.isNotEmpty(categoryPathFirst))
//				{
//					categoryPathFirst = URLDecoder.decode(categoryPathFirst, "UTF-8");
//					categoryPathFirst = categoryPathFirst.toLowerCase();
//					categoryPathFirst = GenericUtilityMethods.changeUrl(categoryPathFirst);
//					dept.setName(dept.getName() + "||" + categoryPathFirst);
//				}
//			}
//			model.addAttribute("departmentList", departments);
//
//
//		}
//		catch (final EtailBusinessExceptions businessException)
//		{
//			ExceptionUtil.etailBusinessExceptionHandler(businessException, null);
//		}
//		catch (final EtailNonBusinessExceptions nonBusinessException)
//		{
//			ExceptionUtil.etailNonBusinessExceptionHandler(nonBusinessException);
//		}
//		catch (final Exception exception)
//		{
//			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(exception));
//		}
//	}
//}
