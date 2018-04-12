/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import java.util.List;

import com.tisl.mpl.category.data.LandingDetailsforCategoryData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.wsdto.AboutUsResultWsData;
import com.tisl.mpl.wsdto.CategoryListHierarchyData;
import com.tisl.mpl.wsdto.DepartmentListHierarchyData;
import com.tisl.mpl.wsdto.DepartmentListHierarchyDataAmp;
import com.tisl.mpl.wsdto.HelpAndServicestWsData;
import com.tisl.mpl.wsdto.HeroBannerCompWsDTO;
import com.tisl.mpl.wsdto.ProductForCategoryData;


/**
 * @author TCS
 *
 */
public interface MplCustomCategoryService
{

	public ProductForCategoryData getCategoryforCategoryid(final String CategoryId);

	public DepartmentListHierarchyData getAllCategories();

	public AboutUsResultWsData getAboutus();

	public HelpAndServicestWsData getHelpnServices();

	public LandingDetailsforCategoryData getCategoryforCategoryNameUsingId(String CategoryId);

	public List<AbstractCMSComponentModel> getCMSComponentsforCategory(CategoryModel selectedCategory);

	public String getHTMLParsedTextContent(String description);

	public DepartmentListHierarchyDataAmp getAllCategoriesAmp();

	/**
	 * @return
	 * @throws EtailNonBusinessExceptions
	 */
	CategoryListHierarchyData getAllCategorieshierarchy() throws EtailNonBusinessExceptions;

	/**
	 * @param categoryId
	 * @return
	 */
	public HeroBannerCompWsDTO getBannerDataForCategory(String categoryId);

}
