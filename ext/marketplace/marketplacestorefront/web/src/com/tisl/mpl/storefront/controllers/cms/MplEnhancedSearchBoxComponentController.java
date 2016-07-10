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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplEnhancedSearchBoxComponentModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.data.EnhancedSearchBrandData;
import com.tisl.mpl.facades.data.EnhancedSearchCategoryData;
import com.tisl.mpl.facades.data.EnhancedSearchData;
import com.tisl.mpl.facades.data.EnhancedSearchSellerData;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 * @author TCS
 *
 */
@Controller("MplEnhancedSearchBoxComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.MplEnhancedSearchBoxComponent)
public class MplEnhancedSearchBoxComponentController extends AbstractCMSComponentController<MplEnhancedSearchBoxComponentModel>
{
	//@Resource(name = "modelService")
	//private ModelService modelService;
	//	@Resource(name = "enumerationService")
	//	private EnumerationService enumerationService;

	// Setter required for UnitTests
	//public void setModelService(final ModelService modelService)
	//{
	//	this.modelService = modelService;
	//}


	/*
	 * Method to load the component with configurations
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final MplEnhancedSearchBoxComponentModel component) throws EtailNonBusinessExceptions
	{
		// this will return all front end
		// properties which we just inject into the model.
		/*
		 * for (final String property : getCmsComponentService().getEditorProperties(component)) { try { final Object
		 * value = modelService.getAttributeValue(component, property); model.addAttribute(property, value); } catch
		 * (final AttributeNotSupportedException e) { throw new EtailNonBusinessExceptions(e,
		 * MarketplacecommerceservicesConstants.E0005); } }
		 */

		//load the category list box
		try
		{




			/*
			 * final List<SearchDropDownCatagory> categoryList =
			 * enumerationService.getEnumerationValues(SearchDropDownCatagory.class);
			 * 
			 * final List<SearchDropDownBrand> brands = enumerationService.getEnumerationValues(SearchDropDownBrand.class);
			 * final List<SearchDropDownSeller> seller =
			 * enumerationService.getEnumerationValues(SearchDropDownSeller.class);
			 */


			model.addAttribute("categoryList", component.getSearchBoxCategories());
			model.addAttribute("brands", component.getSearchBoxBrands());
			model.addAttribute("sellers", component.getSearchBoxSellerMaster());
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
			final MplEnhancedSearchBoxComponentModel component = getCmsComponentService().getSimpleCMSComponent(
					"MplEnhancedSearchBox");

			final List<CategoryModel> categoryCollection = new ArrayList<CategoryModel>();
			final List<CategoryModel> brandCollection = new ArrayList<CategoryModel>();
			final List<SellerMasterModel> sellerCollection = new ArrayList<SellerMasterModel>();
			final List<EnhancedSearchCategoryData> categoryDataList = new ArrayList<EnhancedSearchCategoryData>();
			final List<EnhancedSearchBrandData> brandDataList = new ArrayList<EnhancedSearchBrandData>();
			final List<EnhancedSearchSellerData> sellerDataList = new ArrayList<EnhancedSearchSellerData>();

			if (CollectionUtils.isNotEmpty(component.getSearchBoxCategories()))
			{
				categoryCollection.addAll(component.getSearchBoxCategories());
			}
			if (CollectionUtils.isNotEmpty(component.getSearchBoxBrands()))
			{
				brandCollection.addAll(component.getSearchBoxBrands());
			}
			if (CollectionUtils.isNotEmpty(component.getSearchBoxSellerMaster()))
			{
				sellerCollection.addAll(component.getSearchBoxSellerMaster());
			}
			for (final CategoryModel categoryModel : categoryCollection)
			{
				final EnhancedSearchCategoryData categoryData = new EnhancedSearchCategoryData();
				categoryData.setCode(categoryModel.getCode());
				categoryData.setName(categoryModel.getName());
				categoryDataList.add(categoryData);
			}

			for (final CategoryModel brandModel : brandCollection)
			{
				final EnhancedSearchBrandData brandData = new EnhancedSearchBrandData();
				brandData.setCode(brandModel.getCode());
				brandData.setName(brandModel.getName());
				brandDataList.add(brandData);
			}

			for (final SellerMasterModel sellerModel : sellerCollection)
			{
				final EnhancedSearchSellerData sellerData = new EnhancedSearchSellerData();
				sellerData.setId(sellerModel.getId());
				//sellerData.setName(sellerModel.getLegalName());
				//TISPRD-1792
				final String sellerName = sellerModel.getFirstname() == null ? sellerModel.getLegalName() : sellerModel
						.getFirstname();
				sellerData.setName(sellerName);
				sellerDataList.add(sellerData);
			}


			enhancedSearchData.setCategoryData(categoryDataList);
			enhancedSearchData.setBrandData(brandDataList);
			enhancedSearchData.setSellerData(sellerDataList);
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
