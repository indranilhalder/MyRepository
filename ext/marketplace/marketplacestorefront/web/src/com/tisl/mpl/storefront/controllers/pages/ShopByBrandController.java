/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
//import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tisl.mpl.core.model.BrandComponentModel;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 * @author TCS
 *
 */
@Controller
@Scope("tenant")
@RequestMapping(value = RequestMappingUrlConstants.SHOP_BY_BRAND_AJAX)
public class ShopByBrandController
{
	@Resource(name = "cmsComponentService")
	private CMSComponentService cmsComponentService;

	//	@Autowired
	//	private UrlResolver<CategoryModel> categoryModelUrlResolver;
	//
	//	@Autowired
	//	private HomepageComponentService homepageComponentService;

	private static final Logger LOG = Logger.getLogger(ShopByBrandController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String getShopByBrandContent(@RequestParam("compId") final String componentUid, final Model model)
			throws CMSItemNotFoundException
	{
		BrandComponentModel brandComponent = null;

		if (StringUtils.isNotEmpty(componentUid))
		{
			brandComponent = (BrandComponentModel) cmsComponentService.getSimpleCMSComponent(componentUid);
		}

		if (null != brandComponent.getLayout())
		{
			model.addAttribute("masterBrandUrl", brandComponent.getMasterBrandURL());
			model.addAttribute("masterBrandName", brandComponent.getMasterBrandName());
			model.addAttribute("subBrandList", brandComponent.getSubBrandList());
			model.addAttribute("subBrands", brandComponent.getSubBrands());
			model.addAttribute("layout", brandComponent.getLayout());
		}

		for (final CategoryModel category : brandComponent.getSubBrands())
		{
			String categoryPath = GenericUtilityMethods.urlSafe(category.getName());
			if (StringUtils.isNotEmpty(categoryPath))
			{
				try
				{
					categoryPath = URLDecoder.decode(categoryPath, "UTF-8");
				}
				catch (final UnsupportedEncodingException e)
				{
					LOG.error(e.getMessage());
				}
				categoryPath = categoryPath.toLowerCase();
				categoryPath = GenericUtilityMethods.changeUrl(categoryPath);
			}
			category.setName(category.getName() + "||" + categoryPath);
		}

		return ControllerConstants.Views.Fragments.Home.ShopByBrandImagesPanel;

	}
}
