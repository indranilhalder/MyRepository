/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.LuxEnhancedSearchBoxComponentModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.data.EnhancedSearchCategoryData;
import com.tisl.mpl.facades.data.EnhancedSearchData;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 * @author BORN
 *
 */
@Controller("LuxEnhancedSearchBoxComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.LuxEnhancedSearchBoxComponent)
public class LuxEnhancedSearchBoxComponentController extends AbstractCMSComponentController<LuxEnhancedSearchBoxComponentModel>
{
	private static final Logger LOG = Logger.getLogger(LuxEnhancedSearchBoxComponentController.class);


	/*
	 * Method to load the component with configurations
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final LuxEnhancedSearchBoxComponentModel component) throws EtailNonBusinessExceptions
	{

		//load the category list box
		try
		{
			LOG.debug("Load all the Categories Associated");
			LOG.warn("Load all the Categories Associated");
			model.addAttribute("categoryList", component.getSearchBoxCategories());
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * Method to load the component with configurations
	 */
	@ResponseBody
	@RequestMapping(value = "/searchdropdown", method = RequestMethod.GET/* , produces = "application/json" */)
	public EnhancedSearchData loadSearchDropdown() throws EtailNonBusinessExceptions
	{
		final EnhancedSearchData enhancedSearchData = new EnhancedSearchData();
		try
		{
			final LuxEnhancedSearchBoxComponentModel component = getCmsComponentService()
					.getSimpleCMSComponent("LuxEnhancedSearchBox");

			final List<CategoryModel> categoryCollection = new ArrayList<CategoryModel>();
			final List<EnhancedSearchCategoryData> categoryDataList = new ArrayList<EnhancedSearchCategoryData>();

			if (CollectionUtils.isNotEmpty(component.getSearchBoxCategories()))
			{
				categoryCollection.addAll(component.getSearchBoxCategories());
			}
			for (final CategoryModel categoryModel : categoryCollection)
			{
				final EnhancedSearchCategoryData categoryData = new EnhancedSearchCategoryData();
				categoryData.setCode(categoryModel.getCode());
				categoryData.setName(categoryModel.getName());
				categoryDataList.add(categoryData);
			}

			enhancedSearchData.setCategoryData(categoryDataList);
		}
		catch (final CMSItemNotFoundException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0005);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return enhancedSearchData;
	}

}
