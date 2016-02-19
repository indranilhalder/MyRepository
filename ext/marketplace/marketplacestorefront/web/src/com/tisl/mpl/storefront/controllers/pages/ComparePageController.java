/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ClassificationData;
import de.hybris.platform.commercefacades.product.data.FeatureData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.fest.util.Collections;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.storefront.breadcrumb.impl.MplSearchBreadcrumbBuilder;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */


@Controller
@Scope("tenant")
@RequestMapping(value = "/compare")
public class ComparePageController extends AbstractPageController
{
	private static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE,
			ProductOption.VARIANT_FULL, ProductOption.DELIVERY_MODE_AVAILABILITY, ProductOption.CLASSIFICATION,
			ProductOption.CATEGORIES);
	private static final String COMPARE_PAGE_ID = "compare";
	private static final String HASH = "#";
	private static final String LAST_LINK_CLASS = "active";
	private static final String MISSING_IMAGE_URL = "/store/_ui/desktop/theme-blue/images/missing-product-96x96.jpg";
	private static final String COMPARE_LIST = "compareList";

	private List<CategoryModel> referenceCategories = new ArrayList<CategoryModel>();

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "searchBreadcrumbBuilder")
	private MplSearchBreadcrumbBuilder searchBreadcrumbBuilder;

	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;
	@Resource
	private BuyBoxFacade buyBoxFacade;

	private static final Logger LOG = Logger.getLogger(ComparePageController.class);

	/**
	 * @param productCode
	 * @param session
	 * @return Map<Integer, Map<String, String>> This method returns the products to be shown in the compare section
	 *         below the SERP/PLP page through an ajax response
	 */

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public Map<Integer, Map<String, String>> getComparableProducts(@RequestParam("productCode") final String productCode,
			final HttpSession session)

	{
		List<ProductData> sessionCompareList = new ArrayList();//3
		//get the products from session which are not in the list of selected product codes
		if (session.getAttribute(COMPARE_LIST) != null)
		{
			sessionCompareList = (List<ProductData>) session.getAttribute(COMPARE_LIST);

		}

		final ProductModel productModel = productService.getProductForCode(productCode);
		final ProductData productData = productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS);

		//Check if the compare list size is less than 4 and product is comparable
		if (sessionCompareList.size() < 4 && addToCompareList(productModel, sessionCompareList))
		{
			final BuyBoxData buyBoxData = buyBoxFacade.buyboxPrice(productData.getCode());
			if (buyBoxData != null)
			{
				if (buyBoxData.getMrpPriceValue() != null)
				{
					productData.setProductMRP(buyBoxData.getMrpPriceValue());
				}
				if (buyBoxData.getSpecialPrice() != null)
				{
					LOG.info("**************Product Special Price**************** " + buyBoxData.getSpecialPrice());
					productData.setProductMOP(buyBoxData.getSpecialPrice());
				}
				else if (buyBoxData.getPrice() != null)
				{
					LOG.info("************Product MOP Price**************" + buyBoxData.getPrice());
					productData.setProductMOP(buyBoxData.getPrice());

				}
			}

			sessionCompareList.add(productData);

		}


		session.setAttribute(COMPARE_LIST, sessionCompareList);

		return populateProductMap(sessionCompareList);
	}

	/**
	 * @param productDatas
	 *
	 * @return Map<Integer, Map<String, String>> This method returns a map of products with all relevant attributes
	 *
	 */
	@SuppressWarnings("boxing")
	private Map<Integer, Map<String, String>> populateProductMap(final List<ProductData> productDatas)
	{
		int index = 0;
		final Map<Integer, Map<String, String>> comparableProductMap = new HashMap();
		for (final ProductData productData : productDatas)
		{

			final Map<String, String> productAttributeMap = new HashMap();
			productAttributeMap.put(ModelAttributetConstants.PRODUCT_CODE, productData.getCode());
			productAttributeMap.put(ModelAttributetConstants.PRODUCT_NAME, productData.getProductTitle()); // productData.getProductTitle()
			productAttributeMap.put(ModelAttributetConstants.PRODUCT_BRAND, productData.getBrand().getBrandname());
			productAttributeMap.put(ModelAttributetConstants.PRODUCT_URL, productData.getUrl());

			final BuyBoxData buyBoxData = buyBoxFacade.buyboxPrice(productData.getCode());

			if (buyBoxData != null)
			{
				if (buyBoxData.getMrpPriceValue() != null)
				{
					productAttributeMap.put(ModelAttributetConstants.PRODUCT_PRICE, buyBoxData.getMrpPriceValue().getFormattedValue());
				}
				if (buyBoxData.getSpecialPrice() != null)
				{
					LOG.info("**************Product Special Price**************** " + buyBoxData.getSpecialPrice());
					productAttributeMap.put(ModelAttributetConstants.PRODUCT_PRICE_SPCL, buyBoxData.getSpecialPrice()
							.getFormattedValue());
				}
				else if (buyBoxData.getPrice() != null)
				{
					LOG.info("************Product MOP Price**************" + buyBoxData.getPrice());
					productAttributeMap.put(ModelAttributetConstants.PRODUCT_PRICE_SPCL, buyBoxData.getPrice().getFormattedValue());

				}
			}



			final List<ImageData> images = (List<ImageData>) productData.getImages();
			if (images != null)
			{
				if (images.get(0).getUrl() != null)
				{
					productAttributeMap.put(ModelAttributetConstants.PRODUCT_IMAGE_URL, images.get(0).getUrl());
				}
				else
				{
					productAttributeMap.put(ModelAttributetConstants.PRODUCT_IMAGE_URL, MISSING_IMAGE_URL);
				}
			}
			else
			{
				productAttributeMap.put(ModelAttributetConstants.PRODUCT_IMAGE_URL, MISSING_IMAGE_URL);
			}

			comparableProductMap.put(index, productAttributeMap);
			index++;
		}
		return comparableProductMap;
	}

	/**
	 * @param productModel
	 * @param products
	 * @return boolean This method checks if a product is already in the compare list and adds it if not present after
	 *         checking if it is comparable
	 */
	private boolean addToCompareList(final ProductModel productModel, final List<ProductData> products)
	{

		final List<CategoryModel> superCategories = (List<CategoryModel>) productModel.getSupercategories();

		boolean present = false;
		boolean addable = false;

		if (products.size() == 0 && !Collections.isEmpty(superCategories))
		{
			referenceCategories.clear();

			for (final CategoryModel superCategory : superCategories)
			{
				if (superCategory != null && !StringUtils.isEmpty(superCategory.getCode())
						&& superCategory.getCode().startsWith("MSH"))
				{
					referenceCategories.add(superCategory);
				}
			}

			return true;
		}
		else
		{
			for (final ProductData productInCompareList : products)
			{
				if (productInCompareList.getCode().equals(productModel.getCode()))
				{
					present = true;

				}
			}

		}
		if (!present)
		{

			final List<CategoryModel> updatedReferenceCategories = new ArrayList<CategoryModel>();
			for (final CategoryModel supercategory : superCategories)
			{
				for (final CategoryModel referenceCategory : referenceCategories)
				{
					if (supercategory.getCode().equals(referenceCategory.getCode()))
					{

						updatedReferenceCategories.add(referenceCategory);
						addable = true;
					}
				}
			}
			if (updatedReferenceCategories.size() > 0)
			{
				referenceCategories = updatedReferenceCategories;



			}
		}

		return addable;
	}



	/**
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 *            This method returns the view for the compare page
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String getComparePageView(final Model model, final HttpSession session) throws CMSItemNotFoundException
	{
		try
		{
			List<ProductData> sessionCompareList = new ArrayList();
			if (session.getAttribute(COMPARE_LIST) != null)
			{
				sessionCompareList = (List<ProductData>) session.getAttribute(COMPARE_LIST);
			}
			model.addAttribute(ModelAttributetConstants.PRODUCT_DATAS, sessionCompareList);
			final List<ClassificationData> uniqueClassficationClasses = new ArrayList<ClassificationData>();
			if (getClassifyingCategories(sessionCompareList) != null)
			{
				for (final ClassificationData classification : getClassifyingCategories(sessionCompareList))
				{
					if (isUniqueClassification(classification.getName(), uniqueClassficationClasses))
					{ //if unique then add
						uniqueClassficationClasses.add(classification);
					}
					else
					{
						int classificationIndex = -1;
						for (int index = 0; index < uniqueClassficationClasses.size(); index++)
						{
							if (StringUtils.isNotEmpty(uniqueClassficationClasses.get(index).getName())
									&& uniqueClassficationClasses.get(index).getName().equalsIgnoreCase(classification.getName()))
							{
								classificationIndex = index;
								break;
							}
						}
						LOG.info("index::::" + classificationIndex);
						// if it is not unique then get all features of this classification as well as the older one
						List<FeatureData> appendedFeatureList = new ArrayList<FeatureData>();

						if (!Collections.isEmpty(getTotalFeatureList(uniqueClassficationClasses, classification)))
						{
							appendedFeatureList = getTotalFeatureList(uniqueClassficationClasses, classification);

						}
						final ClassificationData classificationData = new ClassificationData();
						if (!StringUtils.isEmpty(classification.getCode()))
						{
							classificationData.setCode(classification.getCode());
						}
						if (!StringUtils.isEmpty(classification.getName()))
						{
							classificationData.setName(classification.getName());
						}

						classificationData.setFeatures(appendedFeatureList);

						if (classificationIndex != -1)
						{
							uniqueClassficationClasses.set(classificationIndex, classificationData);
						}
					}
				}
			}
			model.addAttribute("ClassifyingCategories", uniqueClassficationClasses);
			final ContentPageModel comparePage = getContentPageForLabelOrId(COMPARE_PAGE_ID);
			if (referenceCategories != null && referenceCategories.size() > 0)
			{
				final CategoryModel referenceCategory = referenceCategories.get(0);
				final List<Breadcrumb> breadCrumbs = searchBreadcrumbBuilder.getBreadcrumbs(referenceCategory.getCode(), null, false);
				breadCrumbs.add(new Breadcrumb(HASH, comparePage.getName(), LAST_LINK_CLASS));
				model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadCrumbs);
			}

			storeCmsPageInModel(model, comparePage);
			return getViewForPage(model);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, e.getErrorMessage());

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.COMPARE_SYSTEM_ERROR);

		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.B2001));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.COMPARE_SYSTEM_ERROR);

		}

	}

	/**
	 * @param uniqueClassficationClasses
	 * @param newClassification
	 */
	private List<FeatureData> getTotalFeatureList(final List<ClassificationData> uniqueClassficationClasses,
			final ClassificationData newClassification)
	{

		final List<FeatureData> featureList = new ArrayList<FeatureData>();
		//Add features from this new classification
		featureList.addAll(newClassification.getFeatures());
		//Also add features from existing classification
		for (final ClassificationData classification : uniqueClassficationClasses)
		{
			if (classification.getName().equalsIgnoreCase(newClassification.getName()))
			{
				featureList.addAll(classification.getFeatures());
			}
		}
		LOG.debug("New feature list size :::" + featureList.size());
		return featureList;

	}

	/**
	 * @param name
	 * @param uniqueClassficationClasses
	 * @return boolean
	 */
	private boolean isUniqueClassification(final String name, final List<ClassificationData> uniqueClassficationClasses)
	{
		if (!Collections.isEmpty(uniqueClassficationClasses))
		{
			for (final ClassificationData classification : uniqueClassficationClasses)
			{
				if (classification.getName().equalsIgnoreCase(name))
				{
					return false;

				}
			}
		}

		return true;

	}

	/**
	 * @param productCode
	 * @param session
	 * @return String This method removes a product from the compare List
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	@ResponseBody
	public String removeFromCompare(@RequestParam("productCode") final String productCode, final HttpSession session)
	{
		List<ProductData> sessionCompareList = new ArrayList();
		if (session.getAttribute(COMPARE_LIST) != null)
		{
			sessionCompareList = (List<ProductData>) session.getAttribute(COMPARE_LIST);
		}
		int removalIndex = 0;
		for (int index = 0; index < sessionCompareList.size(); index++)
		{
			if (sessionCompareList.get(index).getCode().equals(productCode))
			{
				removalIndex = index;
			}
		}

		sessionCompareList.remove(removalIndex);

		if (session.getAttribute(COMPARE_LIST) != null)
		{

			session.setAttribute(COMPARE_LIST, sessionCompareList);
		}
		return "success";
	}

	/**
	 * @param session
	 * @return String This method removes all the products from the compare list
	 */
	@RequestMapping(value = "/removeAll", method = RequestMethod.GET)
	@ResponseBody
	public String removeAllProductsFromCompare(final HttpSession session)
	{
		List<ProductData> sessionCompareList = new ArrayList();
		if (session.getAttribute(COMPARE_LIST) != null)
		{
			sessionCompareList = (List<ProductData>) session.getAttribute(COMPARE_LIST);
		}
		if (sessionCompareList != null)
		{
			sessionCompareList.clear();
			session.setAttribute(COMPARE_LIST, sessionCompareList);
		}

		return "success";
	}

	/**
	 * @param compareList
	 * @return List<ClassificationData> This method gets the classification categories for the comparable products
	 */
	public List<ClassificationData> getClassifyingCategories(final List<ProductData> compareList)
	{
		final List<ClassificationData> classifyingCategories = new ArrayList();
		boolean present = false;
		for (final ProductData product : compareList)
		{
			if (product.getClassifications() != null && product.getClassifications().size() > 0)
			{
				for (final ClassificationData productClassification : product.getClassifications())
				{
					if (!productClassification.getName().equalsIgnoreCase("NA"))
					{
						for (final ClassificationData classificationPresent : classifyingCategories)
						{
							if (classificationPresent.getCode().equals(productClassification.getCode()))
							{
								present = true;
							}
						}
						if (!present)
						{
							classifyingCategories.add(productClassification);
						}
					}
				}

			}
		}
		return classifyingCategories;
	}

}
