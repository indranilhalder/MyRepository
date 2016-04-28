/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSContentSlotService;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tisl.mpl.core.model.DepartmentCollectionComponentModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author 847370
 *
 */
@Controller
public class HTMLSitemapController extends AbstractPageController
{
	private final String SITEMAP_CMS_PAGE = "sitemap";
	@Resource(name = "categoryService")
	private CategoryService categoryService;
	@Autowired
	private DefaultCMSContentSlotService contentSlotService;

	@RequestMapping(value = RequestMappingUrlConstants.SITEMAP, method = RequestMethod.GET)
	@RequireHardLogIn
	public String sitemap(
			@RequestParam(value = ModelAttributetConstants.PAGE, defaultValue = ModelAttributetConstants.ONE_VAL) final int page,
			final Model modelSpring) throws Exception
	{


		final Map<CategoryModel, Map<CategoryModel, Collection<CategoryModel>>> megaMap = new LinkedHashMap<CategoryModel, Map<CategoryModel, Collection<CategoryModel>>>();

		final ContentSlotModel contentSlotModel = contentSlotService.getContentSlotForId("NavigationBarSlot");
		final List<AbstractCMSComponentModel> componentLists = contentSlotModel.getCmsComponents();
		for (final AbstractCMSComponentModel model : componentLists)
		{
			if (model instanceof DepartmentCollectionComponentModel)
			{
				final DepartmentCollectionComponentModel deptModel = (DepartmentCollectionComponentModel) model;
				final Collection<CategoryModel> mainDepts = deptModel.getDepartmentCollection();
				for (final CategoryModel categoryModel : mainDepts)
				{
					try
					{
						final Map<CategoryModel, Collection<CategoryModel>> innerLevelMap = new HashMap<CategoryModel, Collection<CategoryModel>>();

						final CategoryModel department = categoryService.getCategoryForCode(categoryModel.getCode());
						final Collection<CategoryModel> secondLevelCategories = department.getCategories();

						// Iterating through the second level categories
						for (final CategoryModel secondLevelCategory : secondLevelCategories)
						{
							// Fetching the third level category against a second
							// level category
							final Collection<CategoryModel> thirdLevelCategory = secondLevelCategory.getCategories();
							// Storing the third level categories in a map
							//thirdLevelCategoryMap.put(secondLevelCategory.getCode(), thirdLevelCategory);
							innerLevelMap.put(secondLevelCategory, thirdLevelCategory);
						}
						// Storing the second level categories in a map
						//secondLevelCategoryMap.put(department.getCode(), secondLevelCategories);
						megaMap.put(categoryModel, innerLevelMap);
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
				for (final Map.Entry<CategoryModel, Map<CategoryModel, Collection<CategoryModel>>> entry : megaMap.entrySet())
				{
					System.out.println("Parent --" + entry.getKey().getName());

					for (final Map.Entry<CategoryModel, Collection<CategoryModel>> entrySecondLevel : entry.getValue().entrySet())
					{
						System.out.println("L2 -------" + entrySecondLevel.getKey().getName());
						final Collection<CategoryModel> thirdLevelList = entrySecondLevel.getValue();
						for (final CategoryModel thirdLevelCategory : thirdLevelList)
						{
							System.out.println("L3-------------" + thirdLevelCategory.getName());
						}
					}
				}
			}
		}

		modelSpring.addAttribute("megaMap", megaMap);

		storeCmsPageInModel(modelSpring, getContentPageForLabelOrId(SITEMAP_CMS_PAGE));
		setUpMetaDataForContentPage(modelSpring, getContentPageForLabelOrId(SITEMAP_CMS_PAGE));
		return getViewForPage(modelSpring);
	}

	@Override
	protected void storeCmsPageInModel(final Model model, final AbstractPageModel cmsPage)
	{
		if (model != null && cmsPage != null)
		{
			model.addAttribute(CMS_PAGE_MODEL, cmsPage);
			if (cmsPage instanceof ContentPageModel)
			{
				storeContentPageTitleInModel(model, getPageTitleResolver().resolveContentPageTitle(cmsPage.getTitle()));
			}
		}
	}


}
