/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.core.model.MplNowTrendingProductCarouselComponentModel;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


@Controller("MplNowTrendingProductCarouselComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.MplNowTrendingProductCarouselComponent)
public class MplNowTrendingProductCarouselComponentController extends
		AbstractCMSComponentController<MplNowTrendingProductCarouselComponentModel>
{
	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	@Resource(name = "categoryService")
	private CategoryService categoryService;

	@Resource(name = "cmsSiteService")
	private CMSSiteService cmsSiteService;



	private static final String MISSING_IMAGE_URL = "/store/_ui/desktop/theme-blue/images/missing-product-96x96.jpg";

	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE,
			ProductOption.VARIANT_FULL);

	/**
	 * This method fetches all the products for the default category and displays them initially
	 *
	 * @param request
	 * @param model
	 * @param component
	 * @return
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final MplNowTrendingProductCarouselComponentModel component)
	{

		//default selected category
		//Making the root category as the default category. This default category will come from personalisation engine..
		final CatalogVersionModel catalogVersionModel = cmsSiteService.getCurrentCatalogVersion();

		final List<CategoryModel> rootCategories = (List<CategoryModel>) categoryService
				.getRootCategoriesForCatalogVersion(catalogVersionModel);

		if (rootCategories != null && rootCategories.size() != 0)
		{

			final List<ProductData> products = getProducts(rootCategories.get(0));
			model.addAttribute(ModelAttributetConstants.PRODUCT_DATA, products);
		}







	}

	/**
	 * Takes the selected category and fetches the now trending products for the selected category
	 *
	 * @param categoryCode
	 * @return Map<Integer, Map<String, String>>
	 */
	@ResponseBody
	@RequestMapping(value = "/nowtrending", method = RequestMethod.GET, produces = "application/json")
	public Map<Integer, Map<String, String>> getProductsForSelectedCategory(@RequestParam("categoryCode") final String categoryCode)
	{



		final CategoryModel selectedCategory = categoryService.getCategoryForCode(categoryCode);
		//
		final Map<Integer, Map<String, String>> allProductsMap = new HashMap();

		final List<ProductData> products = getProducts(selectedCategory);


		for (int index = 0; index < products.size(); index++)
		{
			final Map<String, String> productMap = new HashMap();
			if (products.get(index).getName() != null)
			{
				productMap.put(ModelAttributetConstants.PRODUCT_NAME, products.get(index).getName());
			}
			if (products.get(index).getUrl() != null)
			{
				productMap.put(ModelAttributetConstants.PRODUCT_URL, products.get(index).getUrl());
			}
			if (products.get(index).getImages() != null)
			{
				final List<ImageData> images = (List<ImageData>) products.get(index).getImages();

				if (images.get(0).getUrl() != null)
				{
					productMap.put(ModelAttributetConstants.PRODUCT_IMAGE_URL, images.get(0).getUrl());
				}
				else
				{
					productMap.put(ModelAttributetConstants.PRODUCT_IMAGE_URL, MISSING_IMAGE_URL);
				}
			}
			else
			{
				productMap.put(ModelAttributetConstants.PRODUCT_IMAGE_URL, MISSING_IMAGE_URL);
			}

			if (products.get(index).getProductMRP() != null)
			{
				productMap.put(ModelAttributetConstants.PRODUCT_PRICE, products.get(index).getProductMRP().getFormattedValue());
			}
			allProductsMap.put(index, productMap);


		}


		return allProductsMap;
	}


	/**
	 * This method travels through the entire hierarchy of subcategories and fetches all the products present in any of
	 * the subcategory
	 *
	 * @param category
	 * @return List<ProductData>
	 */
	private List<ProductData> getProducts(final CategoryModel category)
	{
		final List<ProductData> products = new ArrayList<>();

		final Collection<CategoryModel> subcategories = categoryService.getAllSubcategoriesForCategory(category);

		if (subcategories != null && subcategories.size() != 0)
		{

			for (final CategoryModel subcategory : subcategories)
			{
				if (subcategory.getProducts() != null)
				{
					for (final ProductModel productModel : subcategory.getProducts())
					{
						if (productModel instanceof PcmProductVariantModel)
						{
							try
							{
								if (productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS) != null)
								{
									products.add(productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS));
								}
							}
							catch (final Exception exception)
							{
								LOG.error("Exception is : " + exception);
								throw new EtailNonBusinessExceptions(exception);
							}

						}
					}
				}
			}
		}
		else
		{
			if (category.getProducts() != null)
			{
				for (final ProductModel productModel : category.getProducts())
				{

					if (productModel instanceof PcmProductVariantModel)
					{
						try
						{
							if (productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS) != null)
							{
								products.add(productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS));
							}
						}
						catch (final Exception exception)
						{
							LOG.error("Exception is : " + exception);
							throw new EtailNonBusinessExceptions(exception);
						}

					}
				}
			}
		}
		return products;
	}
}
