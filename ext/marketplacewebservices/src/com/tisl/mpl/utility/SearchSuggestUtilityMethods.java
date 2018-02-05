/**
 *
 */
package com.tisl.mpl.utility;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.core.enums.LuxIndicatorEnum;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.facades.product.data.ProductTagDto;
import com.tisl.mpl.helper.ProductDetailsHelper;
import com.tisl.mpl.marketplacecommerceservices.daos.BuyBoxDao;
import com.tisl.mpl.service.MplProductWebService;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.MplCompetingProductsUtility;
import com.tisl.mpl.wsdto.AutoCompleteResultWsData;
import com.tisl.mpl.wsdto.CategorySNSWsData;
import com.tisl.mpl.wsdto.DepartmentFilterWsDto;
import com.tisl.mpl.wsdto.DepartmentHierarchyWs;
import com.tisl.mpl.wsdto.FacetDataWsDTO;
import com.tisl.mpl.wsdto.FacetValueDataWsDTO;
import com.tisl.mpl.wsdto.GalleryImageData;
import com.tisl.mpl.wsdto.PriceWsData;
import com.tisl.mpl.wsdto.ProductSNSWsData;
import com.tisl.mpl.wsdto.ProductSearchPageWsDto;
import com.tisl.mpl.wsdto.SellerItemDetailWsDto;
import com.tisl.mpl.wsdto.SellerSNSWsData;
import com.tisl.mpl.wsdto.SellingItemDetailWsDto;
import com.tisl.mpl.wsdto.VariantOptionsWsDto;


/**
 * @author TCS
 *
 */
public class SearchSuggestUtilityMethods
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(SearchSuggestUtilityMethods.class);

	@Autowired
	private ConfigurationService configurationService;

	@Resource(name = "categoryService")
	private CategoryService categoryService;

	@Resource(name = "mplCompetingProductsUtility")
	private MplCompetingProductsUtility mplCompetingProductsUtility;
	@Resource(name = "mplProductWebService")
	private MplProductWebService mplProductWebService;
	//	@Resource(name = "productService")
	//	private ProductService productService;

	//@Resource(name = "productService")
	//private ProductService productService;

	//@Resource(name = "defaultPromotionManager")
	//private DefaultPromotionManager defaultPromotionManager;

	/*
	 * @Resource(name = "accProductFacade") private ProductFacade productFacade;
	 */
	//@Resource(name = "accProductFacade")
	//private ProductFacade productFacade;
	//@Resource(name = "defaultMplProductSearchFacade")
	//private DefaultMplProductSearchFacade searchFacade;
	@Resource(name = "productDetailsHelper")
	private ProductDetailsHelper productDetailsHelper;

	@Resource(name = "buyBoxDao")
	private BuyBoxDao buyBoxDao;

	/**
	 * @Description : Sets Category Data to a DTO
	 * @param wsData
	 * @return categoryWSList
	 */
	public List<CategorySNSWsData> getCategoryDetails(final AutoCompleteResultWsData wsData)
	{
		final List<CategorySNSWsData> categoryWSList = new ArrayList<CategorySNSWsData>();
		if (null != wsData && null != wsData.getCategories() && !wsData.getCategories().isEmpty())
		{
			final List<CategoryData> categoryData = wsData.getCategories();
			if (null != categoryData && !categoryData.isEmpty())
			{
				for (final CategoryData category : categoryData)
				{
					final CategorySNSWsData wDto = new CategorySNSWsData();
					wDto.setCategoryCode(category.getCode());
					wDto.setCategoryName(category.getName());
					categoryWSList.add(wDto);
				}
			}
		}
		return categoryWSList;
	}

	/**
	 * @Description : Sets Seller Data to a DTO
	 * @param wsData
	 * @return sellerWSList
	 */
	public List<SellerSNSWsData> getSellerDetails(final AutoCompleteResultWsData wsData)
	{
		final List<SellerSNSWsData> sellerWSList = new ArrayList<SellerSNSWsData>();
		if (null != wsData && null != wsData.getSellerDetails() && !wsData.getSellerDetails().isEmpty())
		{
			final List<String> sellerData = wsData.getSellerDetails();
			if (null != sellerData && !sellerData.isEmpty())
			{
				for (final String seller : sellerData)
				{
					final SellerSNSWsData wsDTO = new SellerSNSWsData();
					wsDTO.setSellerName(seller);
					sellerWSList.add(wsDTO);
				}
			}
		}

		return sellerWSList;
	}


	/**
	 * @Description : Sets Top Product Data to a DTO
	 * @param wsData
	 * @return productWSList
	 */
	public List<ProductSNSWsData> getTopProductDetails(final AutoCompleteResultWsData wsData)
	{
		final List<ProductSNSWsData> productWSList = new ArrayList<ProductSNSWsData>();
		if (null != wsData && null != wsData.getProducts() && !wsData.getProducts().isEmpty())
		{
			final List<ProductData> productDetails = wsData.getProducts();
			if (null != productDetails && !productDetails.isEmpty())
			{
				for (final ProductData data : productDetails)
				{
					//Deeply nested if..then statements are hard to read
					final ProductSNSWsData productSNSWsData = getTopProductDetailsDto(data);
					productWSList.add(productSNSWsData);
				}
			}
		}
		return productWSList;
	}

	/*
	 * @param productData
	 *
	 * @retrun ProductSNSWsData
	 */
	private ProductSNSWsData getTopProductDetailsDto(final ProductData productData)
	{
		final ProductSNSWsData wsDto = new ProductSNSWsData();
		wsDto.setCode(productData.getCode());
		wsDto.setName(productData.getName());

		final ImageData imgData = getPrimaryImageForProductAndFormat(productData, "searchPage");

		if (imgData != null && imgData.getUrl() != null)
		{
			wsDto.setImageURL(imgData.getUrl());
		}

		//Set MRP and SellingPrice
		if (null != productData.getProductMRP())
		{
			wsDto.setMrpPrice(productData.getProductMRP());
		}
		//Below codes are commented for channel specific promotion

		/*
		 * if (null != productData.getPrice()) { wsDto.setSellingPrice(productData.getPrice()); }
		 */
		if (null != productData.getMobileprice())
		{
			wsDto.setSellingPrice(productData.getMobileprice());
		}

		if (null != productData.getLeastSizeProduct())
		{
			wsDto.setLeastSizeProduct(productData.getLeastSizeProduct());
		}


		final ProductTagDto tag = new ProductTagDto();
		tag.setNewProduct(productData.getIsProductNew());//new Boolean(true)
		tag.setOnlineExclusive(productData.getIsOnlineExclusive());//new Boolean(true)

		wsDto.setProductTag(tag);

		if (null != productData.getMobileBrandName())
		{
			wsDto.setBrandName(productData.getMobileBrandName());
		}

		if (null != productData.getRootCategory())
		{
			wsDto.setRootCategory(productData.getRootCategory());
		}
		if (null != productData.getRatingCount())
		{
			wsDto.setRatingCount(productData.getRatingCount());
		}
		if (null != productData.getProductCategoryType())
		{
			wsDto.setProductCategoryType(productData.getProductCategoryType());
		}
		if (null != productData.getSwatchColor())
		{
			final List<String> colorList = new ArrayList<String>();
			for (final String color : productData.getSwatchColor())
			{
				if (color.contains("_"))
				{
					final String[] colorAry = color.split("_");
					if (colorAry.length > 1)
					{
						if (colorAry[1].equalsIgnoreCase(colorAry[0]))
						{
							colorList.add(colorAry[1]);

						}
						else
						{
							colorList.add("#" + colorAry[1]);
						}
					}
					else
					{
						colorList.add(colorAry[0]);
					}

				}
				else
				{
					colorList.add(color);
				}
			}
			wsDto.setVariantOptions(colorList);
		}
		if (null != productData.getAverageRating())
		{
			wsDto.setRating(productData.getAverageRating());
		}
		if (null != productData.getUrl())
		{
			wsDto.setUrl(productData.getUrl());
		}
		if (null == productData.getIsOfferExisting())
		{
			wsDto.setIsOfferExisting(Boolean.FALSE);
		}
		else
		{
			wsDto.setIsOfferExisting(productData.getIsOfferExisting());
		}


		return wsDto;
	}

	/**
	 * @Description : Sets Popular Product Data to a DTO
	 * @param wsData
	 * @return productWSList
	 */
	public List<ProductSNSWsData> getPopularProductDetails(final AutoCompleteResultWsData wsData)
	{
		final List<ProductSNSWsData> productWSList = new ArrayList<ProductSNSWsData>();
		if (null != wsData && null != wsData.getProductNames() && !wsData.getProductNames().isEmpty())
		{
			final List<ProductData> productDetails = wsData.getProductNames();
			if (null != productDetails && !productDetails.isEmpty())
			{
				for (final ProductData data : productDetails)
				{
					final ProductSNSWsData wsDto = new ProductSNSWsData();
					wsDto.setCode(data.getCode());
					wsDto.setName(data.getName());
					wsDto.setUrl(data.getUrl());
					wsDto.setVariantType(data.getVariantType());
					productWSList.add(wsDto);
				}
			}
		}
		return productWSList;
	}


	public List<CategorySNSWsData> getBrandDetails(final AutoCompleteResultWsData wsData)
	{
		final List<CategorySNSWsData> categoryWSList = new ArrayList<CategorySNSWsData>();
		if (null != wsData && null != wsData.getBrands() && !wsData.getBrands().isEmpty())
		{
			final List<CategoryData> categoryData = wsData.getBrands();
			if (null != categoryData && !categoryData.isEmpty())
			{
				for (final CategoryData category : categoryData)
				{
					final CategorySNSWsData wDto = new CategorySNSWsData();
					if (null != category.getCode())
					{
						wDto.setCategoryCode(category.getCode());
					}
					if (null != category.getName())
					{
						wDto.setCategoryName(category.getName());
					}
					if (null != category.getCode())
					{
						wDto.setType(getCategoryType(category.getCode()));
					}
					categoryWSList.add(wDto);
				}
			}
		}
		return categoryWSList;
	}

	private String getCategoryType(final String catCode)
	{
		CategoryModel selectedCategory = new CategoryModel();
		if (null != categoryService && null != catCode)
		{
			selectedCategory = categoryService.getCategoryForCode(catCode);
		}
		String secondrootname = null;

		String cloth = null;
		String women = null;
		String men = null;
		String electroinics = null;
		if (null != configurationService && null != configurationService.getConfiguration()
				&& null != MarketplacecommerceservicesConstants.SALESCLOTH)
		{
			cloth = configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.SALESCLOTH, "");
		}

		if (null != configurationService && null != configurationService.getConfiguration()
				&& null != MarketplacecommerceservicesConstants.SALESMEN)
		{
			men = configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.SALESMEN, "");
		}
		if (null != configurationService && null != configurationService.getConfiguration()
				&& null != MarketplacecommerceservicesConstants.SALESWOMEN)
		{
			women = configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.SALESWOMEN, "");
		}

		if (null != configurationService && null != configurationService.getConfiguration()
				&& null != MarketplacecommerceservicesConstants.SALESELECTRONICS)
		{
			electroinics = configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.SALESELECTRONICS,
					"");
		}

		final Collection<CategoryModel> super_category_list = categoryService.getAllSupercategoriesForCategory(selectedCategory);
		for (final CategoryModel cattype : super_category_list)
		{
			if (!(cattype instanceof ClassificationClassModel))
			{
				if (null != cattype.getCode()
						&& (cattype.getCode().equalsIgnoreCase(cloth) || cattype.getCode().equalsIgnoreCase(women)
								|| cattype.getCode().equalsIgnoreCase(men) || cattype.getCode().equalsIgnoreCase(electroinics))
						&& null != cattype.getName())
				{
					secondrootname = cattype.getName();
				}
			}
		}
		return secondrootname;
	}

	/**
	 * @desc setting Facets (Filter) in the Search
	 * @param productSearchPage
	 * @param searchPageData
	 * @return
	 */
	public ProductSearchPageWsDto setSearchPageData(final ProductSearchPageWsDto productSearchPage,
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData)
	{
		final List<FacetDataWsDTO> searchfacetDTOList = new ArrayList<>();
		DepartmentHierarchyWs categoryHierarchy = new DepartmentHierarchyWs();
		if (null != searchPageData.getResults())
		{

			// serp product results comes here
			final List<SellingItemDetailWsDto> searchProductDTOList = getProductResults(searchPageData);
			if (searchProductDTOList != null)
			{
				productSearchPage.setSearchresult(searchProductDTOList);
				productSearchPage.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				productSearchPage.setError(MarketplacecommerceservicesConstants.SEARCHNOTFOUND);
			}

			// competing products results comes here
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> competingProductsSearchPageData = mplCompetingProductsUtility
					.getCompetingProducts(searchPageData);

			if (competingProductsSearchPageData != null && competingProductsSearchPageData.getResults() != null)
			{
				final List<SellingItemDetailWsDto> competingProductsDTOList = getProductResults(competingProductsSearchPageData);
				productSearchPage.setCompetingProducts(competingProductsDTOList);
			}
		}
		else
		{
			productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			productSearchPage.setError(MarketplacecommerceservicesConstants.SEARCHNOTFOUND);
		}

		if (null != searchPageData.getFacets())
		{
			for (final FacetData<SearchStateData> facate : searchPageData.getFacets())
			{
				if (facate.isVisible() && !facate.getCode().equalsIgnoreCase("snsCategory")
						&& !facate.getCode().equalsIgnoreCase(MarketplacewebservicesConstants.CATEGORY)
						&& !facate.getCode().equalsIgnoreCase("deptType") && !facate.getCode().equalsIgnoreCase("sellerId")
						&& !facate.getCode().equalsIgnoreCase("micrositeSnsCategory")
						&& !facate.getCode().equalsIgnoreCase("allPromotions")
						&& !facate.getCode().equalsIgnoreCase("categoryNameCodeMapping")) //CAR -245-Luxury
				{
					final FacetDataWsDTO facetWsDTO = new FacetDataWsDTO();

					//	facetWsDTO.setCategory(facate.getCode());
					facetWsDTO.setMultiSelect(Boolean.valueOf((facate.isCategory())));
					if (null != facate.getName())
					{
						facetWsDTO.setName(StringUtils.capitalize(facate.getName()));
					}
					facetWsDTO.setCategory(Boolean.valueOf((facate.isCategory())));
					facetWsDTO.setPriority(Integer.valueOf((facate.getPriority())));
					facetWsDTO.setKey(facate.getCode());

					//Generic filter condition
					if (searchPageData.getDeptType().equalsIgnoreCase(MarketplacewebservicesConstants.GENERIC))
					{

						if (facate.isGenericFilter())
						{
							facetWsDTO.setVisible(Boolean.TRUE);
						}
						else
						{
							facetWsDTO.setVisible(Boolean.FALSE);
						}
					}
					else
					{
						facetWsDTO.setVisible(Boolean.valueOf((facate.isVisible())));
					}

					final List<FacetValueDataWsDTO> facetValueWsDTOList = new ArrayList<>();

					if (null != facate.getValues())
					{
						String currentFacet = "";
						for (final FacetValueData<SearchStateData> values : facate.getValues())
						{
							final FacetValueDataWsDTO facetValueWsDTO = new FacetValueDataWsDTO();
							//facetValueWsDTO.setCount(new Long(values.getCount()));
							if (null != values.getName())
							{
								facetValueWsDTO.setName(values.getName());
							}
							facetValueWsDTO.setSelected(Boolean.valueOf((values.isSelected())));
							if (null != values.getQuery() && null != values.getQuery().getQuery()
									&& null != values.getQuery().getQuery().getValue()
									&& null != values.getQuery().getQuery().getValue().toString())
							{
								currentFacet = values.getQuery().getQuery().getValue().toString();
								facetValueWsDTO.setQuery(currentFacet);
								//facetValueWsDTO.setValue(currentFacet.substring((currentFacet.lastIndexOf(":") + 1)));
								facetValueWsDTO.setValue(values.getCode());
							}
							if (null != values.getQuery() && StringUtils.isNotEmpty(values.getQuery().getUrl())) //CAR -245-Luxury
							{
								facetValueWsDTO.setUrl(values.getQuery().getUrl().toString());
							}
							// added count
							facetValueWsDTO.setCount(Long.valueOf(values.getCount()));
							// To skip Include out of stock
							if (!(null != values.getCode() && values.getCode().equalsIgnoreCase("false")))
							{
								facetValueWsDTOList.add(facetValueWsDTO);
							}
							//facetValueWsDTOList.add(facetValueWsDTO);
						}
					}
					facetWsDTO.setValues(facetValueWsDTOList);
					//Fix to send only facets with visible true
					if (facetWsDTO.getVisible().booleanValue())
					{
						searchfacetDTOList.add(facetWsDTO);
					}
					//searchfacetDTOList.add(facetWsDTO);
				}
				else if (facate.isVisible() && facate.getCode().equalsIgnoreCase(MarketplacewebservicesConstants.CATEGORY))
				{
					if (null != searchPageData.getDepartmentHierarchyData()
							&& CollectionUtils.isNotEmpty(searchPageData.getDepartmentHierarchyData().getHierarchyList()))
					{
						categoryHierarchy = getDepartmentHierarchy(searchPageData.getDepartmentHierarchyData().getHierarchyList(),
								facate.getValues());
					}
					categoryHierarchy.setMultiSelect(Boolean.valueOf((facate.isCategory())));
					if (null != facate.getName())
					{
						categoryHierarchy.setName(StringUtils.capitalize(facate.getName()));
					}
					categoryHierarchy.setCategory(Boolean.valueOf((facate.isCategory())));
					categoryHierarchy.setPriority(Integer.valueOf((facate.getPriority())));
					categoryHierarchy.setKey(facate.getCode());

					//Generic filter condition
					if (searchPageData.getDeptType().equalsIgnoreCase(MarketplacewebservicesConstants.GENERIC))
					{

						if (facate.isGenericFilter())
						{
							categoryHierarchy.setVisible(Boolean.TRUE);
						}
						else
						{
							categoryHierarchy.setVisible(Boolean.FALSE);
						}
					}
					else
					{
						categoryHierarchy.setVisible(Boolean.valueOf((facate.isVisible())));
					}
					productSearchPage.setFacetdatacategory(categoryHierarchy);
				}
			}
			productSearchPage.setFacetdata(searchfacetDTOList);
		}
		else
		{
			productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			productSearchPage.setError(MarketplacecommerceservicesConstants.SEARCHNOTFOUND);
		}

		return productSearchPage;
	}

	/**
	 * @desc setting Products in the Search --TPR-817
	 * @param searchPageData
	 * @return
	 */
	public ProductSearchPageWsDto setPDPSearchPageData(
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData)
	{
		final ProductSearchPageWsDto productSearchPage = new ProductSearchPageWsDto();
		if (null != searchPageData.getResults())
		{
			// serp product results comes here
			final List<SellingItemDetailWsDto> searchProductDTOList = getProductResults(searchPageData);
			if (searchProductDTOList != null)
			{
				productSearchPage.setSearchresult(searchProductDTOList);
				productSearchPage.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				productSearchPage.setError(MarketplacecommerceservicesConstants.SEARCHNOTFOUND);
			}
		}
		else
		{
			productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			productSearchPage.setError(MarketplacecommerceservicesConstants.SEARCHNOTFOUND);
		}

		return productSearchPage;
	}



	// Check if Keyword exists
	/**
	 * @desc Setting keyword redirect in search
	 * @param searchText
	 * @return
	 */
	public Map<String, List<String>> getKeywordSearch(String searchText)
	{
		//TODO parse the URL and remove any extra sort query within it
		String url = null;
		Map<String, List<String>> params = null;
		final List<String> urlList = new ArrayList<String>();
		try
		{
			//searchText = URLParamUtil.getQueryParamParsed(searchText);
			searchText = URLParamUtil.filter(searchText);
			//searchPageData = searchFacade.textSearch(searchText);
			url = mplProductWebService.getKeywordSearch(searchText);
			if (StringUtils.isNotEmpty(url))
			{
				//fetching the Parameters from the redirect URL in Map with Key and values
				params = URLParamUtil.getQueryParams(url);
				urlList.add(url);
				params.put("keywordUrl", urlList);
				//LOG.debug("---search keyword url" + url);
			}
		}
		catch (final Exception e)
		{
			LOG.debug(String.format("searchText-----%s -----url %s", searchText, url));
		}
		return params;
	}

	/**
	 * @desc Setting Products in the search response
	 * @param searchPageData
	 * @return
	 */
	private List<SellingItemDetailWsDto> getProductResults(
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData)
	{
		final List<SellerItemDetailWsDto> sellerItemDetailWsDtoList = new ArrayList<>();
		final List<SellingItemDetailWsDto> searchProductDTOList = new ArrayList<>();
		final String emiCuttOffAmount = configurationService.getConfiguration().getString("marketplace.emiCuttOffAmount");
		List<GalleryImageData> galleryImages = null;

		final boolean specialMobileFlag = configurationService.getConfiguration().getBoolean(
				MarketplacewebservicesConstants.SPECIAL_MOBILE_FLAG, false);
		//ProductData productDataImage = null;
		double savingsAmt = 0.0;
		double calculatedPerSavings = 0.0;
		String floorValue = "";

		for (final ProductData productData : searchPageData.getResults())
		{

			final SellingItemDetailWsDto sellingItemDetail = new SellingItemDetailWsDto();
			final List<VariantOptionsWsDto> variantOptionsWsDtoWsDtoList = new ArrayList<>();

			if (null != productData && null != productData.getCode())
			{
				if (null != productData.getUssID())
				{
					sellingItemDetail.setUssid(productData.getUssID());
				}

				//Revert of TPR-796


				/*
				 * try { productDataImage = productFacade.getProductForCodeAndOptions(productData.getCode(),
				 * Arrays.asList(ProductOption.GALLERY)); galleryImages =
				 * productDetailsHelper.getGalleryImagesMobile(productDataImage); } catch (final Exception e) { LOG.error(
				 * "SERPSEARCH Product Image Error:" + productData.getCode()); continue; }
				 */

				//TPR-796
				/*
				 * try { galleryImages = productDetailsHelper.getPrimaryGalleryImagesMobile(productData); } catch (final
				 * Exception e) { LOG.error("SERPSEARCH ProductError:" + productData.getCode());
				 * ExceptionUtil.getCustomizedExceptionTrace(e); continue; }
				 */

				//TPR-796
				try
				{
					galleryImages = productDetailsHelper.getPrimaryGalleryImagesMobile(productData);
				}
				catch (final Exception e)
				{
					LOG.error("SERPSEARCH ProductError:" + productData.getCode());
					ExceptionUtil.getCustomizedExceptionTrace(e);
					continue;
				}


				if (CollectionUtils.isNotEmpty(galleryImages))
				{
					sellingItemDetail.setGalleryImagesList(galleryImages);
				}
				//SDI-3930 Commented as this has to be calculated in case mobile has a special price
				/*
				 * if (null != productData.getSavingsOnProduct() && null != productData.getSavingsOnProduct().getValue()) {
				 * sellingItemDetail
				 * .setDiscountPercent(String.valueOf(productData.getSavingsOnProduct().getValue().intValue())); }
				 */
				if (null != productData.getName())
				{
					sellingItemDetail.setProductname(productData.getName());
				}
				if (null != productData.getDescription())
				{
					sellingItemDetail.setProductdescription(productData.getDescription());
				}
				if (null != productData.getCode())
				{
					sellingItemDetail.setProductId(productData.getCode());
				}
				if (null != productData.getAvailableForPickup())
				{
					sellingItemDetail.setAvailableForPickup(productData.getAvailableForPickup());
				}

				if (null != productData.getProductCategoryType())
				{
					sellingItemDetail.setProductCategoryType(productData.getProductCategoryType());
				}

				if (null != productData.getImmediateCategory())
				{
					sellingItemDetail.setCompareProductType(productData.getImmediateCategory());
				}
				//	sellingItemDetail.setCompareProductType("Type1");

				if (null != productData.getArticleDescription())
				{
					sellingItemDetail.setStylenote(productData.getArticleDescription());
				}
				sellingItemDetail.setDetails(productData.getDescription());
				if (null != productData.getMobileBrandName())
				{
					sellingItemDetail.setBrandname(productData.getMobileBrandName());
				}
				/*
				 * if (null != productData.getUrl()) { sellingItemDetail.setImageURL(productData.getUrl()); } else
				 */

				if (null != productData.getLeastSizeProduct())
				{
					sellingItemDetail.setLeastSizeProduct(productData.getLeastSizeProduct());
				}

				if (productData.getLuxIndicator() != null
						&& productData.getLuxIndicator().equalsIgnoreCase(LuxIndicatorEnum.LUXURY.getCode()))
				{
					final ImageData imgDataLuxury = getPrimaryImageForProductAndFormat(productData, "luxurySearchPage");
					if (imgDataLuxury != null && imgDataLuxury.getUrl() != null)
					{
						sellingItemDetail.setImageURL(imgDataLuxury.getUrl());
					}
				}
				else
				{
					final ImageData imgData = getPrimaryImageForProductAndFormat(productData, "searchPage");
					if (imgData != null && imgData.getUrl() != null)
					{
						sellingItemDetail.setImageURL(imgData.getUrl());
					}
				}

				if (null != productData.getDescription())
				{
					sellingItemDetail.setDetails(productData.getDescription());
				}
				if (null != productData.getAverageRating())
				{
					sellingItemDetail.setRatingvalue(productData.getAverageRating().toString());
				}
				if (null != productData.getColour())
				{
					if (productData.getColour().contains("_"))
					{
						final String[] colorAry = productData.getColour().split("_");
						if (colorAry.length > 1)
						{
							if (colorAry[1].equalsIgnoreCase(colorAry[0]))
							{
								sellingItemDetail.setColourCode(colorAry[1]);

							}
							else
							{
								sellingItemDetail.setColourCode("#" + colorAry[1]);
							}
						}
						sellingItemDetail.setColour(colorAry[0]);
						//TODO: ColourCode needs to be replaced here once we receive color code from PCM

					}
					else
					{
						sellingItemDetail.setColour(productData.getColour());
						sellingItemDetail.setColourCode("#c6e2ff");
					}
				}
				if (null != productData.getProductMRP())
				{
					sellingItemDetail.setMrpPrice(productData.getProductMRP());
				}
				// Below codes are commented for channel specific promotion
				if (specialMobileFlag && null != productData.getMobileprice())
				{
					sellingItemDetail.setSellingPrice(productData.getMobileprice());
					//SDI-3930
					savingsAmt = productData.getProductMRP().getDoubleValue().doubleValue()
							- productData.getMobileprice().getDoubleValue().doubleValue();
					calculatedPerSavings = (savingsAmt / productData.getProductMRP().getDoubleValue().doubleValue()) * 100;
					floorValue = String.valueOf(Math.floor((calculatedPerSavings * 100.0) / 100.0));
				}
				else if (!specialMobileFlag && null != productData.getPrice()) //backward compatible
				{
					sellingItemDetail.setSellingPrice(productData.getPrice());
					//SDI-3930
					savingsAmt = productData.getProductMRP().getDoubleValue().doubleValue()
							- productData.getPrice().getDoubleValue().doubleValue();
					calculatedPerSavings = (savingsAmt / productData.getProductMRP().getDoubleValue().doubleValue()) * 100;
					floorValue = String.valueOf(Math.floor((calculatedPerSavings * 100.0) / 100.0));
				}

				//SDI-3930
				if (StringUtils.isNotEmpty(floorValue) && calculatedPerSavings >= 1.0)
				{
					sellingItemDetail.setDiscountPercent(floorValue);
				}
				else if (null != productData.getSavingsOnProduct() && null != productData.getSavingsOnProduct().getValue())
				{
					sellingItemDetail.setDiscountPercent(String.valueOf(productData.getSavingsOnProduct().getValue().intValue()));
				}

				//added for jewellery mobile web services:maxSellingPrice & minSellingPrice
				if (null != productData.getProductCategoryType()
						&& MarketplacewebservicesConstants.FINEJEWELLERY.equalsIgnoreCase(productData.getProductCategoryType()))
				{
					PriceData pDataMax = new PriceData();
					PriceData pDataMin = new PriceData();
					final List<BuyBoxModel> buyModList = buyBoxDao.getVariantListForPriceRange(productData.getCode());
					if (CollectionUtils.isNotEmpty(buyModList))
					{
						final List<BuyBoxModel> modifiableBuyBox = new ArrayList<BuyBoxModel>(buyModList);
						modifiableBuyBox.sort(Comparator.comparing(BuyBoxModel::getPrice).reversed());

						if (CollectionUtils.isNotEmpty(modifiableBuyBox))
						{
							pDataMin = productDetailsHelper.formPriceData(modifiableBuyBox.get(modifiableBuyBox.size() - 1).getPrice());
							pDataMax = productDetailsHelper.formPriceData(modifiableBuyBox.get(0).getPrice());
						}
					}
					if (null != pDataMin && null != pDataMax)
					{
						sellingItemDetail.setMaxSellingPrice(pDataMax);
						sellingItemDetail.setMinSellingPrice(pDataMin);
					}
				}

				if (null != productData.getInStockFlag())
				{
					sellingItemDetail.setInStockFlag(productData.getInStockFlag());
				}

				//CumulativeStock
				sellingItemDetail.setCumulativeStockLevel(Boolean.valueOf(productData.isStockValue()));
				if (null == productData.getIsOfferExisting())
				{
					sellingItemDetail.setIsOfferExisting(Boolean.FALSE);
				}
				else
				{
					sellingItemDetail.setIsOfferExisting(productData.getIsOfferExisting());
				}


				final ProductTagDto productTag = new ProductTagDto();
				productTag.setNewProduct(productData.getIsProductNew());

				productTag.setOnlineExclusive(productData.getIsOnlineExclusive());//new Boolean(true)
				sellingItemDetail.setProductTag(productTag);
				VariantOptionsWsDto variantOptionsWsDto = null;
				if (null != productData.getSwatchColor())
				{
					for (final String color : productData.getSwatchColor())
					{
						variantOptionsWsDto = new VariantOptionsWsDto();
						if (color.contains("_"))
						{
							final String[] colorAry = color.split("_");
							if (colorAry.length > 1)
							{
								if (colorAry[1].equalsIgnoreCase(colorAry[0]))
								{
									variantOptionsWsDto.setColorcode(colorAry[1]);

								}
								else
								{
									variantOptionsWsDto.setColorcode("#" + colorAry[1]);
								}

							}
							variantOptionsWsDto.setColorname(colorAry[0]);
						}
						else
						{
							variantOptionsWsDto.setColorcode(color);
							variantOptionsWsDto.setColorname("#c6e2ff");
						}
						variantOptionsWsDtoWsDtoList.add(variantOptionsWsDto);
					}
				}
				SellerItemDetailWsDto sellerItemDetailWsDto = null;
				if (null != productData.getSeller())
				{


					for (final SellerInformationData seller : productData.getSeller())

					{
						sellerItemDetailWsDto = new SellerItemDetailWsDto();
						int price = 0;
						if (null != seller.getSellerID())
						{
							sellerItemDetailWsDto.setSellerId(seller.getSellerID());
						}
						if (null != seller.getSellername())
						{
							sellerItemDetailWsDto.setSellerName(seller.getSellername());
						}
						if (null != seller.getSpPrice() && null != seller.getSpPrice().getValue())
						{

							price = seller.getSpPrice().getValue().intValue();
							sellerItemDetailWsDto.setProductprice(seller.getSpPrice().getValue().toString());
						}
						else
						{
							if (null != seller.getMopPrice() && null != seller.getMopPrice().getValue())
							{
								price = seller.getMopPrice().getValue().intValue();
								sellerItemDetailWsDto.setProductprice(seller.getMopPrice().getValue().toString());
							}
							else
							{
								if (null != seller.getMrpPrice() && null != seller.getMrpPrice().getValue())
								{

									price = seller.getMrpPrice().getValue().intValue();
									sellerItemDetailWsDto.setProductprice(seller.getMrpPrice().getValue().toString());
								}

							}
						}
						if (null != emiCuttOffAmount)
						{
							final int emiCuttOffAmountinvalue = Integer.parseInt(emiCuttOffAmount);
							if (price > emiCuttOffAmountinvalue)
							{
								sellerItemDetailWsDto.setEMItag(MarketplacecommerceservicesConstants.Y);
							}
							else
							{
								sellerItemDetailWsDto.setEMItag(MarketplacecommerceservicesConstants.N);
							}
							if (null != seller.getSpPrice() && null != seller.getSpPrice().getValue())
							{
								sellerItemDetailWsDto.setOfferprice(seller.getSpPrice().getValue().toString());

							}
						}

						sellerItemDetailWsDtoList.add(sellerItemDetailWsDto);
					}

				}
				/*
				 * else { sellerItemDetailWsDto = new SellerItemDetailWsDto(); sellerItemDetailWsDto.setSellerId("767865");
				 * sellerItemDetailWsDto.setSellerName("TATA"); sellerItemDetailWsDto.setEMItag("Y");
				 * sellerItemDetailWsDto.setOfferprice("767"); sellerItemDetailWsDtoList.add(sellerItemDetailWsDto); }
				 */
				sellingItemDetail.setSeller(sellerItemDetailWsDtoList);
				sellingItemDetail.setVariantOptions(variantOptionsWsDtoWsDtoList);
				searchProductDTOList.add(sellingItemDetail);
				//	productSearchPage.setSearchresult(searchProductDTOList);

			}
			else
			{
				return null;
			}
		}
		return searchProductDTOList;
	}

	/**
	 * @desc Setting Images data
	 * @param product
	 * @param format
	 * @return
	 */
	private ImageData getPrimaryImageForProductAndFormat(final ProductData product, final String format)
	{
		if (product != null && format != null)
		{
			final Collection<ImageData> images = product.getImages();
			if (images != null && !images.isEmpty())
			{
				for (final ImageData image : images)
				{
					if (ImageDataType.PRIMARY.equals(image.getImageType()) && format.equals(image.getFormat()))
					{
						return image;
					}
				}
			}
		}
		return null;
	}


	/**
	 * @desc Setting department hierarchy in the filter
	 * @param departmentFilters
	 * @return
	 */
	public DepartmentHierarchyWs getDepartmentHierarchy(final List<String> departmentFilters)
	{
		final Set<String> traversedDepartments = new HashSet<String>();
		final DepartmentHierarchyWs departmentHierarchy = new DepartmentHierarchyWs();
		//	final List<L1DepartmentFilterWsDto> l1DepartmentFilterWsDtos = new ArrayList<L1DepartmentFilterWsDto>();

		if (departmentFilters != null && !departmentFilters.isEmpty())
		{
			for (final String departmentFil : departmentFilters)
			{
				final String traverseString = findTraversedCategories(traversedDepartments, departmentFil);
				if (traverseString.equals(MarketplacecommerceservicesConstants.DEPT_L3))
				{
					final String[] foundDeparts = departmentFil.split(MarketplacecommerceservicesConstants.SPLITSTRING);
					if (foundDeparts.length > 4)
					{
						for (final DepartmentFilterWsDto oldL1Filter : departmentHierarchy.getFilters())
						{
							if (oldL1Filter.getCategoryCode().equals(
									foundDeparts[1].split(MarketplacecommerceservicesConstants.COLON)[0]))
							{
								for (final DepartmentFilterWsDto oldL2DepartFilter : oldL1Filter.getChildFilters())
								{
									if (oldL2DepartFilter.getCategoryCode().equals(
											foundDeparts[2].split(MarketplacecommerceservicesConstants.COLON)[0]))
									{
										for (final DepartmentFilterWsDto oldL3DepartFilter : oldL2DepartFilter.getChildFilters())
										{
											if (oldL3DepartFilter.getCategoryCode().equals(
													foundDeparts[3].split(MarketplacecommerceservicesConstants.COLON)[0]))
											{
												final DepartmentFilterWsDto newDepartmentFilter = getDepartmentFilter(foundDeparts[4]
														.split(":"));
												if (oldL3DepartFilter.getChildFilters() != null
														&& !oldL3DepartFilter.getChildFilters().isEmpty())
												{
													oldL3DepartFilter.getChildFilters().add(newDepartmentFilter);
												}
												else
												{
													final List<DepartmentFilterWsDto> l4List = new ArrayList<DepartmentFilterWsDto>();
													l4List.add(newDepartmentFilter);
													oldL3DepartFilter.setChildFilters(l4List);
												}
											}
										}
									}
								}

							}
						}
					}
				}
				else if (traverseString.equals(MarketplacecommerceservicesConstants.DEPT_L1))
				{
					final String[] foundDeparts = departmentFil.split(MarketplacecommerceservicesConstants.SPLITSTRING);
					for (final DepartmentFilterWsDto oldL1Filter : departmentHierarchy.getFilters())
					{
						if (null != oldL1Filter.getCategoryCode()
								&& oldL1Filter.getCategoryCode().equals(
										foundDeparts[1].split(MarketplacecommerceservicesConstants.COLON)[0]))
						{
							final DepartmentFilterWsDto l2DepartFilter = new DepartmentFilterWsDto();
							final DepartmentFilterWsDto l3DepartFilter = new DepartmentFilterWsDto();
							List<DepartmentFilterWsDto> l4List = new ArrayList<DepartmentFilterWsDto>();
							final List<DepartmentFilterWsDto> l3List = new ArrayList<DepartmentFilterWsDto>();
							for (int i = 2; i < foundDeparts.length; i++)
							{
								final String[] newCategories = foundDeparts[i].split(MarketplacecommerceservicesConstants.COLON);
								if (newCategories[2].equals(MarketplacecommerceservicesConstants.DEPT_L2))
								{
									//final DepartmentFilterWsDto newL2DepartmentFilter = getDepartmentFilter(newCategories);
									l2DepartFilter.setCategoryCode(newCategories[0]);
									l2DepartFilter.setCategoryName(newCategories[1]);
									l2DepartFilter.setLevel(newCategories[2]);
									if (newCategories[3].equals(MarketplacecommerceservicesConstants.TRUE))
									{
										l2DepartFilter.setCategoryType(MarketplacecommerceservicesConstants.DEPARTMENT);
									}
									else
									{
										l2DepartFilter.setCategoryType(MarketplacecommerceservicesConstants.CATEGORY);
									}
									l2DepartFilter.setRanking(newCategories[4]);
									//l2DepartFilter = new L2DepartmentFilterWsDto();
									//l2DepartFilter.setLevel2DepartmentFilter(newL2DepartmentFilter);
								}
								else if (newCategories[2].equals(MarketplacecommerceservicesConstants.DEPT_L3))
								{
									//final DepartmentFilterWsDto newL3DepartmentFilter = getDepartmentFilter(newCategories);
									//l3DepartFilter = new L3DepartmentFilterWsDto();
									l3DepartFilter.setCategoryCode(newCategories[0]);
									l3DepartFilter.setCategoryName(newCategories[1]);
									l3DepartFilter.setLevel(newCategories[2]);
									if (newCategories[3].equals(MarketplacecommerceservicesConstants.TRUE))
									{
										l3DepartFilter.setCategoryType(MarketplacecommerceservicesConstants.DEPARTMENT);
									}
									else
									{
										l3DepartFilter.setCategoryType(MarketplacecommerceservicesConstants.CATEGORY);
									}
									l3DepartFilter.setRanking(newCategories[4]);
									l3List.add(l3DepartFilter);
									//l3DepartFilter.setLevel3DepartmentFilter(newL3DepartmentFilter);
								}
								else if (newCategories[2].equals(MarketplacecommerceservicesConstants.DEPT_L4))
								{
									final DepartmentFilterWsDto newDepartmentFilter = getDepartmentFilter(newCategories);
									l4List = new ArrayList<DepartmentFilterWsDto>();
									l4List.add(newDepartmentFilter);
								}

								if (!l4List.isEmpty())
								{
									l3DepartFilter.setChildFilters(l4List);
								}

							}
							l2DepartFilter.setChildFilters(l3List);

							if (oldL1Filter.getChildFilters() != null && !oldL1Filter.getChildFilters().isEmpty())
							{
								oldL1Filter.getChildFilters().add(l2DepartFilter);
							}
							else
							{
								final List<DepartmentFilterWsDto> l2List = new ArrayList<DepartmentFilterWsDto>();
								l2List.add(l2DepartFilter);
								oldL1Filter.setChildFilters(l2List);
							}
							traversedDepartments.addAll(concateDepartmentString(departmentFil,
									MarketplacecommerceservicesConstants.DEPT_L1));
						}
					}

				}
				else if (traverseString.equals(MarketplacecommerceservicesConstants.DEPT_L2))
				{
					final String[] foundDeparts = departmentFil.split(MarketplacecommerceservicesConstants.SPLITSTRING);
					for (final DepartmentFilterWsDto oldL1Filter : departmentHierarchy.getFilters())
					{
						if (oldL1Filter.getCategoryCode().equals(foundDeparts[1].split(MarketplacecommerceservicesConstants.COLON)[0]))
						{
							for (final DepartmentFilterWsDto oldL2DepartFilter : oldL1Filter.getChildFilters())
							{
								if (oldL2DepartFilter.getCategoryCode().equals(
										foundDeparts[2].split(MarketplacecommerceservicesConstants.COLON)[0]))
								{
									final DepartmentFilterWsDto l3DepartFilter = new DepartmentFilterWsDto();
									List<DepartmentFilterWsDto> l4List = new ArrayList<DepartmentFilterWsDto>();
									for (int i = 3; i < foundDeparts.length; i++)
									{
										final String[] newCategories = foundDeparts[i].split(MarketplacecommerceservicesConstants.COLON);
										if (newCategories[2].equals(MarketplacecommerceservicesConstants.DEPT_L3))
										{

											//final DepartmentFilterWsDto newL3DepartmentFilter = getDepartmentFilter(newCategories);
											//l3DepartFilter = new L3DepartmentFilterWsDto();
											l3DepartFilter.setCategoryCode(newCategories[0]);
											l3DepartFilter.setCategoryName(newCategories[1]);
											l3DepartFilter.setLevel(newCategories[2]);
											if (newCategories[3].equals(MarketplacecommerceservicesConstants.TRUE))
											{
												l3DepartFilter.setCategoryType(MarketplacecommerceservicesConstants.DEPARTMENT);
											}
											else
											{
												l3DepartFilter.setCategoryType(MarketplacecommerceservicesConstants.CATEGORY);
											}
											l3DepartFilter.setRanking(newCategories[4]);
											//l3DepartFilter.setLevel3DepartmentFilter(newL3DepartmentFilter);

										}
										else if (newCategories[2].equals(MarketplacecommerceservicesConstants.DEPT_L4))
										{
											final DepartmentFilterWsDto newDepartmentFilter = getDepartmentFilter(newCategories);
											l4List = new ArrayList<DepartmentFilterWsDto>();
											l4List.add(newDepartmentFilter);
										}
										if (!l4List.isEmpty())
										{
											l3DepartFilter.setChildFilters(l4List);
										}

										if (oldL2DepartFilter.getChildFilters() != null && !oldL2DepartFilter.getChildFilters().isEmpty())
										{
											oldL2DepartFilter.getChildFilters().add(l3DepartFilter);
										}
										else
										{
											final List<DepartmentFilterWsDto> l3List = new ArrayList<DepartmentFilterWsDto>();
											l3List.add(l3DepartFilter);
											oldL2DepartFilter.setChildFilters(l3List);
										}
										traversedDepartments.addAll(concateDepartmentString(departmentFil,
												MarketplacecommerceservicesConstants.DEPT_L2));

									}
								}
							}
						}
					}
				}
				else if (traverseString.equals(MarketplacecommerceservicesConstants.DEPT_L0))
				{
					final String[] newDeparts = departmentFil.split(MarketplacecommerceservicesConstants.SPLITSTRING);
					final DepartmentFilterWsDto l1DepartFilter = new DepartmentFilterWsDto();
					final DepartmentFilterWsDto l2DepartFilter = new DepartmentFilterWsDto();
					final DepartmentFilterWsDto l3DepartFilter = new DepartmentFilterWsDto();
					List<DepartmentFilterWsDto> l4List = new ArrayList<DepartmentFilterWsDto>();
					final List<DepartmentFilterWsDto> l3List = new ArrayList<DepartmentFilterWsDto>();
					final List<DepartmentFilterWsDto> l2List = new ArrayList<DepartmentFilterWsDto>();
					final List<DepartmentFilterWsDto> l1List = new ArrayList<DepartmentFilterWsDto>();
					for (int i = 1; i < newDeparts.length; i++)
					{
						final String[] newCategories = newDeparts[i].split(MarketplacecommerceservicesConstants.COLON);
						if (newCategories[2].equals(MarketplacecommerceservicesConstants.DEPT_L1))
						{
							//final DepartmentFilterWsDto newL1DepartmentFilter = getDepartmentFilter(newCategories);
							//l1DepartFilter = new L1DepartmentFilterWsDto();
							l1DepartFilter.setCategoryCode(newCategories[0]);
							l1DepartFilter.setCategoryName(newCategories[1]);
							l1DepartFilter.setLevel(newCategories[2]);
							if (newCategories[3].equals(MarketplacecommerceservicesConstants.TRUE))
							{
								l1DepartFilter.setCategoryType(MarketplacecommerceservicesConstants.DEPARTMENT);
							}
							else
							{
								l1DepartFilter.setCategoryType(MarketplacecommerceservicesConstants.CATEGORY);
							}
							l1DepartFilter.setRanking(newCategories[4]);
							//l1DepartFilter.setLevel1DepartmentFilter(newL1DepartmentFilter);
						}
						else if (newCategories[2].equals(MarketplacecommerceservicesConstants.DEPT_L2))
						{
							//final DepartmentFilterWsDto newL2DepartmentFilter = getDepartmentFilter(newCategories);
							l2DepartFilter.setCategoryCode(newCategories[0]);
							l2DepartFilter.setCategoryName(newCategories[1]);
							l2DepartFilter.setLevel(newCategories[2]);
							if (newCategories[3].equals(MarketplacecommerceservicesConstants.TRUE))
							{
								l2DepartFilter.setCategoryType(MarketplacecommerceservicesConstants.DEPARTMENT);
							}
							else
							{
								l2DepartFilter.setCategoryType(MarketplacecommerceservicesConstants.CATEGORY);
							}
							l2DepartFilter.setRanking(newCategories[4]);
							//l2DepartFilter = new L2DepartmentFilterWsDto();
							//l2DepartFilter.setLevel2DepartmentFilter(newL2DepartmentFilter);
						}
						else if (newCategories[2].equals(MarketplacecommerceservicesConstants.DEPT_L3))
						{
							//final DepartmentFilterWsDto newL3DepartmentFilter = getDepartmentFilter(newCategories);
							//l3DepartFilter = new L3DepartmentFilterWsDto();
							l3DepartFilter.setCategoryCode(newCategories[0]);
							l3DepartFilter.setCategoryName(newCategories[1]);
							l3DepartFilter.setLevel(newCategories[2]);
							if (newCategories[3].equals(MarketplacecommerceservicesConstants.TRUE))
							{
								l3DepartFilter.setCategoryType(MarketplacecommerceservicesConstants.DEPARTMENT);
							}
							else
							{
								l3DepartFilter.setCategoryType(MarketplacecommerceservicesConstants.CATEGORY);
							}
							l3DepartFilter.setRanking(newCategories[4]);
							//l3DepartFilter.setLevel3DepartmentFilter(newL3DepartmentFilter);
						}
						else if (newCategories[2].equals(MarketplacecommerceservicesConstants.DEPT_L4))
						{
							final DepartmentFilterWsDto newDepartmentFilter = getDepartmentFilter(newCategories);
							l4List = new ArrayList<DepartmentFilterWsDto>();
							l4List.add(newDepartmentFilter);
						}
					}
					if (!l4List.isEmpty())
					{
						l3DepartFilter.setChildFilters(l4List);
					}
					l3List.add(l3DepartFilter);
					l2DepartFilter.setChildFilters(l3List);
					l2List.add(l2DepartFilter);
					l1DepartFilter.setChildFilters(l2List);
					if (departmentHierarchy.getFilters() != null && !departmentHierarchy.getFilters().isEmpty())
					{
						departmentHierarchy.getFilters().add(l1DepartFilter);
					}
					else
					{
						l1List.add(l1DepartFilter);
						departmentHierarchy.setFilters(l1List);
					}
					traversedDepartments.addAll(concateDepartmentString(departmentFil, MarketplacecommerceservicesConstants.DEPT_L0));

				}

			}
		}

		return departmentHierarchy;
	}

	/**
	 * @param traversedDepartments
	 * @param departmentFil
	 */
	private String findTraversedCategories(final Set<String> traversedDepartments, final String departmentFil)
	{
		final String departmentFound = MarketplacecommerceservicesConstants.DEPT_L0;
		final String[] categories = departmentFil.split(MarketplacecommerceservicesConstants.COLON);
		final String concatedCategories = categories[1] + categories[5] + categories[9];

		if (traversedDepartments != null && !traversedDepartments.isEmpty())
		{
			if (traversedDepartments.contains(concatedCategories))
			{
				return MarketplacecommerceservicesConstants.DEPT_L3;
			}
			else if (traversedDepartments.contains(categories[1] + categories[5]))
			{
				return MarketplacecommerceservicesConstants.DEPT_L2;
			}
			else if (traversedDepartments.contains(categories[1]))
			{
				return MarketplacecommerceservicesConstants.DEPT_L1;
			}
		}

		return departmentFound;
	}

	private List<String> concateDepartmentString(final String departmentFil, final String type)
	{
		final String[] categories = departmentFil.split(MarketplacecommerceservicesConstants.COLON);
		final List<String> allCategories = new ArrayList<String>();
		final String concatedCategories = categories[1] + categories[5] + categories[9];
		allCategories.add(concatedCategories);
		allCategories.add(categories[1] + categories[5]);
		allCategories.add(categories[1]);
		return allCategories;
	}

	public DepartmentFilterWsDto getDepartmentFilter(final String[] categories)
	{
		final DepartmentFilterWsDto newDepartment = new DepartmentFilterWsDto();
		newDepartment.setCategoryCode(categories[0]);
		newDepartment.setCategoryName(categories[1]);
		newDepartment.setLevel(categories[2]);
		if (categories[3].equals(MarketplacecommerceservicesConstants.TRUE))
		{
			newDepartment.setCategoryType(MarketplacecommerceservicesConstants.DEPARTMENT);
		}
		else
		{
			newDepartment.setCategoryType(MarketplacecommerceservicesConstants.CATEGORY);
		}
		newDepartment.setRanking(categories[4]);
		return newDepartment;
	}

	/**
	 * @desc Setting Filters in the DTO
	 * @param productSearchPage
	 * @param searchPageData
	 * @return
	 */
	public ProductSearchPageWsDto setFilterData(final ProductSearchPageWsDto productSearchPage,
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData)
	{
		final List<FacetDataWsDTO> searchfacetDTOList = new ArrayList<>();
		DepartmentHierarchyWs categoryHierarchy = new DepartmentHierarchyWs();
		List<FacetValueDataWsDTO> facetValueWsDTOList = null;
		if (CollectionUtils.isNotEmpty(searchPageData.getFacets()))
		{
			for (final FacetData<SearchStateData> facate : searchPageData.getFacets())
			{
				if (facate.isVisible() && StringUtils.isNotEmpty(facate.getCode())
						&& !facate.getCode().equalsIgnoreCase("snsCategory")
						&& !facate.getCode().equalsIgnoreCase(MarketplacewebservicesConstants.CATEGORY)
						&& !facate.getCode().equalsIgnoreCase("deptType") && !facate.getCode().equalsIgnoreCase("sellerId")
						&& !facate.getCode().equalsIgnoreCase("micrositeSnsCategory")
						&& !facate.getCode().equalsIgnoreCase("categoryNameCodeMapping")) //CAR -245-Luxury

				{
					final FacetDataWsDTO facetWsDTO = new FacetDataWsDTO();
					facetValueWsDTOList = new ArrayList<>();


					//	facetWsDTO.setCategory(facate.getCode());
					facetWsDTO.setMultiSelect(Boolean.valueOf((facate.isCategory())));
					if (StringUtils.isNotEmpty(facate.getName()))
					{
						facetWsDTO.setName(StringUtils.capitalize(facate.getName()));
					}
					facetWsDTO.setCategory(Boolean.valueOf((facate.isCategory())));
					facetWsDTO.setPriority(Integer.valueOf((facate.getPriority())));
					facetWsDTO.setKey(facate.getCode());

					//Generic filter condition
					if (searchPageData.getDeptType().equalsIgnoreCase(MarketplacewebservicesConstants.GENERIC))
					{
						if (facate.isGenericFilter())
						{
							facetWsDTO.setVisible(Boolean.TRUE);
						}
						else
						{
							facetWsDTO.setVisible(Boolean.FALSE);
						}
					}
					else
					{
						facetWsDTO.setVisible(Boolean.valueOf((facate.isVisible())));
					}

					if (CollectionUtils.isNotEmpty(facate.getValues()))
					{
						String currentFacet = "";
						for (final FacetValueData<SearchStateData> values : facate.getValues())
						{
							final FacetValueDataWsDTO facetValueWsDTO = new FacetValueDataWsDTO();
							//facetValueWsDTO.setCount(new Long(values.getCount()));
							if (StringUtils.isNotEmpty(values.getName()))
							{
								facetValueWsDTO.setName(values.getName());
							}
							facetValueWsDTO.setSelected(Boolean.valueOf((values.isSelected())));
							if (null != values.getQuery() && null != values.getQuery().getQuery()
									&& null != values.getQuery().getQuery().getValue()
									&& null != values.getQuery().getQuery().getValue().toString())
							{
								currentFacet = values.getQuery().getQuery().getValue().toString();
								facetValueWsDTO.setQuery(currentFacet);
								//facetValueWsDTO.setValue(currentFacet.substring((currentFacet.lastIndexOf(":") + 1)));
								if (StringUtils.isNotEmpty(values.getCode()))
								{
									facetValueWsDTO.setValue(values.getCode());
								}
							}
							if (null != values.getQuery() && StringUtils.isNotEmpty(values.getQuery().getUrl())) //CAR -245-Luxury
							{
								facetValueWsDTO.setUrl(values.getQuery().getUrl().toString());
							}
							// added count
							facetValueWsDTO.setCount(Long.valueOf(values.getCount()));

							//If facet name is "Include out of stock"  value will be false
							if (!(null != values.getCode() && StringUtils.isNotEmpty(values.getCode()) && values.getCode()
									.equalsIgnoreCase("false")))
							{
								facetValueWsDTOList.add(facetValueWsDTO);
							}
							//facetValueWsDTOList.add(facetValueWsDTO);
						}
					}
					facetWsDTO.setValues(facetValueWsDTOList);
					//Fix to send only facets with visible true
					if (facetWsDTO.getVisible().booleanValue())
					{
						searchfacetDTOList.add(facetWsDTO);
					}
					//searchfacetDTOList.add(facetWsDTO);
				}
				else if (facate.isVisible() && StringUtils.isNotEmpty(facate.getCode())
						&& facate.getCode().equalsIgnoreCase(MarketplacewebservicesConstants.CATEGORY))
				{
					if (null != searchPageData.getDepartmentHierarchyData()
							&& CollectionUtils.isNotEmpty(searchPageData.getDepartmentHierarchyData().getHierarchyList()))
					{
						categoryHierarchy = getDepartmentHierarchy(searchPageData.getDepartmentHierarchyData().getHierarchyList(),
								facate.getValues());
					}
					categoryHierarchy.setMultiSelect(Boolean.valueOf((facate.isCategory())));
					if (StringUtils.isNotEmpty(facate.getName()))
					{
						categoryHierarchy.setName(StringUtils.capitalize(facate.getName()));
					}
					categoryHierarchy.setCategory(Boolean.valueOf((facate.isCategory())));
					categoryHierarchy.setPriority(Integer.valueOf((facate.getPriority())));
					categoryHierarchy.setKey(facate.getCode());

					//Generic filter condition
					if (StringUtils.isNotEmpty(searchPageData.getDeptType())
							&& searchPageData.getDeptType().equalsIgnoreCase(MarketplacewebservicesConstants.GENERIC))
					{

						if (facate.isGenericFilter())
						{
							categoryHierarchy.setVisible(Boolean.TRUE);
						}
						else
						{
							categoryHierarchy.setVisible(Boolean.FALSE);
						}
					}
					else
					{
						categoryHierarchy.setVisible(Boolean.valueOf((facate.isVisible())));
					}
					productSearchPage.setFacetdatacategory(categoryHierarchy);
				}
			}
			productSearchPage.setFacetdata(searchfacetDTOList);
			//productSearchPage.setFacetdatacategory();

		}
		else
		{
			productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			productSearchPage.setError(MarketplacecommerceservicesConstants.SEARCHNOTFOUND);
		}
		return productSearchPage;
	}

	/**
	 * @desc Setting department hierarchy with facet count
	 * @param departmentFilters
	 * @param facetValues
	 * @return
	 */
	public DepartmentHierarchyWs getDepartmentHierarchy(final List<String> departmentFilters,
			final List<FacetValueData<SearchStateData>> facetValues)
	{
		final DepartmentHierarchyWs departmentHierarchy = new DepartmentHierarchyWs();
		DepartmentFilterWsDto l1DepartFilter = new DepartmentFilterWsDto();
		DepartmentFilterWsDto l2DepartFilter = new DepartmentFilterWsDto();
		DepartmentFilterWsDto l3DepartFilter = new DepartmentFilterWsDto();
		DepartmentFilterWsDto l4DepartFilter = new DepartmentFilterWsDto();
		Map<String, DepartmentFilterWsDto> l4Map = new TreeMap<String, DepartmentFilterWsDto>();
		Map<String, DepartmentFilterWsDto> l3Map = new TreeMap<String, DepartmentFilterWsDto>();
		Map<String, DepartmentFilterWsDto> l2Map = new TreeMap<String, DepartmentFilterWsDto>();
		Map<String, DepartmentFilterWsDto> l1Map = new TreeMap<String, DepartmentFilterWsDto>();
		List<DepartmentFilterWsDto> lFinalList = new ArrayList<DepartmentFilterWsDto>();
		List<DepartmentFilterWsDto> lSortedFinalList = new ArrayList<DepartmentFilterWsDto>();

		DepartmentFilterWsDto oldl1 = null;
		String[] newCategories = null;
		String[] newDeparts = null;
		boolean flag = false;

		if (CollectionUtils.isNotEmpty(departmentFilters))
		{
			for (final String departmentFil : departmentFilters)
			{
				newDeparts = departmentFil.split(MarketplacecommerceservicesConstants.SPLITSTRING);
				if (newDeparts.length < 4)
				{
					LOG.error("Error in Query");
				}
				for (int i = 1; i < newDeparts.length; i++)
				{
					//For L1
					newCategories = newDeparts[i].split(MarketplacecommerceservicesConstants.COLON);
					if (newCategories[2].equals(MarketplacecommerceservicesConstants.DEPT_L1))
					{
						l1DepartFilter = getDepartmentFilter(newCategories);
						l1Map.put(newCategories[0], l1DepartFilter);
					}

					//For L2
					newCategories = newDeparts[i].split(MarketplacecommerceservicesConstants.COLON);
					if (newCategories[2].equals(MarketplacecommerceservicesConstants.DEPT_L2))
					{
						l2DepartFilter = getDepartmentFilter(newCategories);
						l2Map.put(newCategories[0], l2DepartFilter);
					}

					//For L3
					newCategories = newDeparts[i].split(MarketplacecommerceservicesConstants.COLON);
					if (newCategories[2].equals(MarketplacecommerceservicesConstants.DEPT_L3))
					{
						l3DepartFilter = getDepartmentFilter(newCategories);
						l3Map.put(newCategories[0], l3DepartFilter);
					}

					//For L4
					newCategories = newDeparts[i].split(MarketplacecommerceservicesConstants.COLON);
					if (newCategories[2].equals(MarketplacecommerceservicesConstants.DEPT_L4))
					{
						l4DepartFilter = getDepartmentFilter(newCategories);
						l4Map.put(newCategories[0], l4DepartFilter);
					}

				}

			}
		}

		//re-attaching Facet selection into filter
		if (CollectionUtils.isNotEmpty(facetValues))
		{
			for (final DepartmentFilterWsDto l1Depart : l1Map.values())
			{

				for (final DepartmentFilterWsDto l2Depart : l2Map.values())
				{

					for (final DepartmentFilterWsDto l3Depart : l3Map.values())
					{

						for (final DepartmentFilterWsDto l4Depart : l4Map.values())
						{
							//Setting L4
							//FOr select if facet is selected
							for (final FacetValueData<SearchStateData> value : facetValues)
							{
								if (value != null && StringUtils.isNotEmpty(value.getCode())
										&& value.getCode().equalsIgnoreCase(l4Depart.getCategoryCode()))
								{
									l4Depart.setSelected(Boolean.valueOf(value.isSelected()));
									//TPR-796
									l4Depart.setQuantity((int) (value.getCount()));
									if (value.isSelected())
									{
										flag = true;
									}
									break;
								}
							}
							if (l4Depart.getCategoryCode().contains(l3Depart.getCategoryCode()))
							{
								if (CollectionUtils.isNotEmpty(l3Depart.getChildFilters()))
								{
									l3Depart.getChildFilters().add(l4Depart);
								}
								else
								{
									lFinalList = new ArrayList<DepartmentFilterWsDto>();
									lFinalList.add(l4Depart);
									l3Depart.setChildFilters(lFinalList);

								}

							}

						}
						//setting L3
						//FOr select if facet is selected
						for (final FacetValueData<SearchStateData> value : facetValues)
						{
							if (value != null && StringUtils.isNotEmpty(value.getCode())
									&& value.getCode().equalsIgnoreCase(l3Depart.getCategoryCode()))
							{
								l3Depart.setSelected(Boolean.valueOf(value.isSelected()));
								//TPR-796
								l3Depart.setQuantity((int) (value.getCount()));
								if (value.isSelected())
								{
									flag = true;
								}
								break;
							}
						}
						if (StringUtils.isNotEmpty(l3Depart.getCategoryCode())
								&& l3Depart.getCategoryCode().contains(l2Depart.getCategoryCode()))
						{
							if (CollectionUtils.isNotEmpty(l2Depart.getChildFilters()))
							{
								l2Depart.getChildFilters().add(l3Depart);
								//break;
							}
							else
							{
								lFinalList = new ArrayList<DepartmentFilterWsDto>();
								lFinalList.add(l3Depart);
								l2Depart.setChildFilters(lFinalList);
								//break;
							}

						}
					}
					//Setting L2
					//FOr select if facet is selected
					for (final FacetValueData<SearchStateData> value : facetValues)
					{
						if (value != null && StringUtils.isNotEmpty(value.getCode())
								&& value.getCode().equalsIgnoreCase(l2Depart.getCategoryCode()))
						{
							l2Depart.setSelected(Boolean.valueOf(value.isSelected()));
							//TPR-796
							l2Depart.setQuantity((int) (value.getCount()));
							if (value.isSelected())
							{
								flag = true;
							}
							break;
						}
					}
					if (StringUtils.isNotEmpty(l2Depart.getCategoryCode())
							&& l2Depart.getCategoryCode().contains(l1Depart.getCategoryCode()))
					{
						if (CollectionUtils.isNotEmpty(l1Depart.getChildFilters()))
						{
							l1Depart.getChildFilters().add(l2Depart);
							//break;
						}
						else
						{
							lFinalList = new ArrayList<DepartmentFilterWsDto>();
							lFinalList.add(l2Depart);
							l1Depart.setChildFilters(lFinalList);
						}
					}

				}
				//FOr select if facet is selected
				for (final FacetValueData<SearchStateData> value : facetValues)
				{
					if (value != null && StringUtils.isNotEmpty(value.getCode())
							&& value.getCode().equalsIgnoreCase(l1Depart.getCategoryCode()))
					{
						l1Depart.setSelected(Boolean.valueOf(value.isSelected()));
						//TPR-796
						l1Depart.setQuantity((int) (value.getCount()));
						if (value.isSelected())
						{
							flag = true;
						}
						break;
					}
				}
				//Setting L1
				if (!l1Depart.equals(oldl1))
				{
					if (CollectionUtils.isNotEmpty(departmentHierarchy.getFilters()))
					{
						departmentHierarchy.getFilters().add(l1Depart);
					}
					else
					{
						lFinalList = new ArrayList<DepartmentFilterWsDto>();
						lFinalList.add(l1Depart);
						departmentHierarchy.setFilters(lFinalList);
					}
				}
				oldl1 = l1Depart;
			}
		}
		if (flag)
		{
			departmentHierarchy.setSelected(Boolean.TRUE);
		}
		else
		{
			departmentHierarchy.setSelected(Boolean.FALSE);
		}


		l1Map = new TreeMap<String, DepartmentFilterWsDto>();
		if (CollectionUtils.isNotEmpty(departmentHierarchy.getFilters()))
		{
			for (final DepartmentFilterWsDto l1 : departmentHierarchy.getFilters())
			{
				l2Map = new TreeMap<String, DepartmentFilterWsDto>();
				if (CollectionUtils.isNotEmpty(l1.getChildFilters()))
				{
					for (final DepartmentFilterWsDto l2 : l1.getChildFilters())
					{
						l3Map = new TreeMap<String, DepartmentFilterWsDto>();
						if (CollectionUtils.isNotEmpty(l2.getChildFilters()))
						{
							for (final DepartmentFilterWsDto l3 : l2.getChildFilters())
							{
								if (CollectionUtils.isNotEmpty(l3.getChildFilters()))
								{
									l4Map = new TreeMap<String, DepartmentFilterWsDto>();

									for (final DepartmentFilterWsDto l4 : l3.getChildFilters())
									{
										l4Map.put(l4.getCategoryCode(), l4);
									}
									lSortedFinalList = new ArrayList<DepartmentFilterWsDto>(l4Map.values());
									l3.setChildFilters(lSortedFinalList);
								}
								l3Map.put(l3.getCategoryCode(), l3);

							}
						}
						if (MapUtils.isNotEmpty(l3Map))
						{
							lSortedFinalList = new ArrayList<DepartmentFilterWsDto>(l3Map.values());
							l2.setChildFilters(lSortedFinalList);
						}
						l2Map.put(l2.getCategoryCode(), l2);
					}
				}
				if (MapUtils.isNotEmpty(l2Map))
				{
					lSortedFinalList = new ArrayList<DepartmentFilterWsDto>(l2Map.values());
					l1.setChildFilters(lSortedFinalList);
				}
				l1Map.put(l1.getCategoryCode(), l1);
			}
		}
		if (MapUtils.isNotEmpty(l1Map))
		{
			lSortedFinalList = new ArrayList<DepartmentFilterWsDto>(l1Map.values());
			departmentHierarchy.setFilters(lSortedFinalList);
		}

		return departmentHierarchy;
	}

	//	added for NU-38 start
	public ProductSearchPageWsDto setFilterWsData(final ProductSearchPageWsDto productSearchPage,
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData)
	{
		final List<FacetDataWsDTO> searchfacetDTOList = new ArrayList<>();
		DepartmentHierarchyWs categoryHierarchy = new DepartmentHierarchyWs();
		List<FacetValueDataWsDTO> facetValueWsDTOList = null;
		if (CollectionUtils.isNotEmpty(searchPageData.getFacets()))
		{
			for (final FacetData<SearchStateData> facate : searchPageData.getFacets())
			{
				if (facate.isVisible() && StringUtils.isNotEmpty(facate.getCode())
						&& !facate.getCode().equalsIgnoreCase("snsCategory")
						&& !facate.getCode().equalsIgnoreCase(MarketplacewebservicesConstants.CATEGORY)
						&& !facate.getCode().equalsIgnoreCase("deptType") && !facate.getCode().equalsIgnoreCase("sellerId")
						&& !facate.getCode().equalsIgnoreCase("micrositeSnsCategory")
						&& !facate.getCode().equalsIgnoreCase("categoryNameCodeMapping")) //CAR -245-Luxury

				{
					final FacetDataWsDTO facetWsDTO = new FacetDataWsDTO();
					facetValueWsDTOList = new ArrayList<>();

					facetWsDTO.setMultiSelect(Boolean.valueOf((facate.isMultiSelect())));
					if (StringUtils.isNotEmpty(facate.getName()))
					{
						facetWsDTO.setName(StringUtils.capitalize(facate.getName()));
					}

					//	facetWsDTO.setPriority(Integer.valueOf((facate.getPriority())));
					facetWsDTO.setKey(facate.getCode());
					if (facate.isMultiSelect())
					{
						facetWsDTO.setSelectedFilterCount(facate.getSelectedFilterCount());
					}
					Boolean visible = Boolean.FALSE;
					//Generic filter condition
					if (searchPageData.getDeptType().equalsIgnoreCase(MarketplacewebservicesConstants.GENERIC))
					{
						if (facate.isGenericFilter())
						{
							visible = Boolean.TRUE;
						}
						else
						{
							visible = Boolean.FALSE;
						}
					}
					else
					{
						visible = Boolean.valueOf((facate.isVisible()));
					}

					if (CollectionUtils.isNotEmpty(facate.getValues()))
					{

						for (final FacetValueData<SearchStateData> values : facate.getValues())
						{
							final FacetValueDataWsDTO facetValueWsDTO = new FacetValueDataWsDTO();

							facetValueWsDTO.setCount(Long.valueOf(values.getCount()));

							if (StringUtils.isNotEmpty(values.getName()))
							{
								facetValueWsDTO.setName(values.getName());
							}
							facetValueWsDTO.setSelected(Boolean.valueOf((values.isSelected())));

							if (StringUtils.isNotEmpty(values.getCode()))
							{
								facetValueWsDTO.setValue(values.getCode());
							}

							//If facet name is "Include out of stock"  value will be false
							if (!(null != values.getCode() && StringUtils.isNotEmpty(values.getCode()) && values.getCode()
									.equalsIgnoreCase("false")))
							{
								facetValueWsDTOList.add(facetValueWsDTO);
							}

						}
					}
					facetWsDTO.setValues(facetValueWsDTOList);
					//Fix to send only facets with visible true
					if (visible.booleanValue())
					{
						searchfacetDTOList.add(facetWsDTO);
					}

				}
				else if (facate.isVisible() && StringUtils.isNotEmpty(facate.getCode())
						&& facate.getCode().equalsIgnoreCase(MarketplacewebservicesConstants.CATEGORY))
				{
					if (null != searchPageData.getDepartmentHierarchyData()
							&& CollectionUtils.isNotEmpty(searchPageData.getDepartmentHierarchyData().getHierarchyList()))
					{
						categoryHierarchy = getDepartmentHierarchy(searchPageData.getDepartmentHierarchyData().getHierarchyList(),
								facate.getValues());
					}
					categoryHierarchy.setMultiSelect(Boolean.valueOf((facate.isMultiSelect())));
					if (StringUtils.isNotEmpty(facate.getName()))
					{
						categoryHierarchy.setName(StringUtils.capitalize(facate.getName()));
					}
					categoryHierarchy.setCategory(Boolean.valueOf((facate.isCategory())));
					categoryHierarchy.setPriority(Integer.valueOf((facate.getPriority())));
					categoryHierarchy.setKey(facate.getCode());
					//Generic filter condition
					if (StringUtils.isNotEmpty(searchPageData.getDeptType())
							&& searchPageData.getDeptType().equalsIgnoreCase(MarketplacewebservicesConstants.GENERIC))
					{

						if (facate.isGenericFilter())
						{
							categoryHierarchy.setVisible(Boolean.TRUE);
						}
						else
						{
							categoryHierarchy.setVisible(Boolean.FALSE);
						}
					}
					else
					{
						categoryHierarchy.setVisible(Boolean.valueOf((facate.isVisible())));
					}
					productSearchPage.setFacetdatacategory(categoryHierarchy);
				}
			}
			productSearchPage.setFacetdata(searchfacetDTOList);

		}
		else
		{
			productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			productSearchPage.setError(MarketplacecommerceservicesConstants.SEARCHNOTFOUND);
		}
		return productSearchPage;
	}


	public ProductSearchPageWsDto setSearchPageWsData(final ProductSearchPageWsDto productSearchPage,
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData)
	{
		final List<FacetDataWsDTO> searchfacetDTOList = new ArrayList<>();
		DepartmentHierarchyWs categoryHierarchy = new DepartmentHierarchyWs();
		if (null != searchPageData.getResults())
		{

			// serp product results comes here
			final List<SellingItemDetailWsDto> searchProductDTOList = getProductWsResults(searchPageData);
			if (searchProductDTOList != null)
			{
				productSearchPage.setSearchresult(searchProductDTOList);
				productSearchPage.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				productSearchPage.setError(MarketplacecommerceservicesConstants.SEARCHNOTFOUND);
			}

		}
		else
		{
			productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			productSearchPage.setError(MarketplacecommerceservicesConstants.SEARCHNOTFOUND);
		}

		if (null != searchPageData.getFacets())
		{
			for (final FacetData<SearchStateData> facate : searchPageData.getFacets())
			{
				if (facate.isVisible() && !facate.getCode().equalsIgnoreCase("snsCategory")
						&& !facate.getCode().equalsIgnoreCase(MarketplacewebservicesConstants.CATEGORY)
						&& !facate.getCode().equalsIgnoreCase("deptType") && !facate.getCode().equalsIgnoreCase("sellerId")
						&& !facate.getCode().equalsIgnoreCase("micrositeSnsCategory")
						&& !facate.getCode().equalsIgnoreCase("allPromotions")
						&& !facate.getCode().equalsIgnoreCase("categoryNameCodeMapping")) //CAR -245-Luxury
				{
					final FacetDataWsDTO facetWsDTO = new FacetDataWsDTO();

					facetWsDTO.setMultiSelect(Boolean.valueOf((facate.isMultiSelect())));
					if (null != facate.getName())
					{
						facetWsDTO.setName(StringUtils.capitalize(facate.getName()));
					}

					facetWsDTO.setKey(facate.getCode());
					if (facate.isMultiSelect())
					{
						facetWsDTO.setSelectedFilterCount(facate.getSelectedFilterCount());
					}
					Boolean visible = Boolean.FALSE;
					//Generic filter condition
					if (searchPageData.getDeptType().equalsIgnoreCase(MarketplacewebservicesConstants.GENERIC))
					{
						if (facate.isGenericFilter())
						{
							visible = Boolean.TRUE;
						}
						else
						{
							visible = Boolean.FALSE;
						}
					}
					else
					{
						visible = Boolean.valueOf((facate.isVisible()));
					}

					final List<FacetValueDataWsDTO> facetValueWsDTOList = new ArrayList<>();

					if (null != facate.getValues())
					{
						//final String currentFacet = "";
						for (final FacetValueData<SearchStateData> values : facate.getValues())
						{
							final FacetValueDataWsDTO facetValueWsDTO = new FacetValueDataWsDTO();

							if (null != values.getName())
							{
								facetValueWsDTO.setName(values.getName());
							}
							facetValueWsDTO.setSelected(Boolean.valueOf((values.isSelected())));

							if (StringUtils.isNotEmpty(values.getCode()))
							{
								facetValueWsDTO.setValue(values.getCode());
							}

							facetValueWsDTO.setCount(Long.valueOf(values.getCount()));
							// To skip Include out of stock
							if (!(null != values.getCode() && values.getCode().equalsIgnoreCase("false")))
							{
								facetValueWsDTOList.add(facetValueWsDTO);
							}

						}
					}
					facetWsDTO.setValues(facetValueWsDTOList);
					//Fix to send only facets with visible true
					if (visible.booleanValue())
					{
						searchfacetDTOList.add(facetWsDTO);
					}
					//searchfacetDTOList.add(facetWsDTO);
				}
				else if (facate.isVisible() && facate.getCode().equalsIgnoreCase(MarketplacewebservicesConstants.CATEGORY))
				{
					if (null != searchPageData.getDepartmentHierarchyData()
							&& CollectionUtils.isNotEmpty(searchPageData.getDepartmentHierarchyData().getHierarchyList()))
					{
						categoryHierarchy = getDepartmentHierarchy(searchPageData.getDepartmentHierarchyData().getHierarchyList(),
								facate.getValues());
					}
					categoryHierarchy.setMultiSelect(Boolean.valueOf((facate.isMultiSelect())));
					if (null != facate.getName())
					{
						categoryHierarchy.setName(StringUtils.capitalize(facate.getName()));
					}
					categoryHierarchy.setCategory(Boolean.valueOf((facate.isCategory())));
					categoryHierarchy.setPriority(Integer.valueOf((facate.getPriority())));
					categoryHierarchy.setKey(facate.getCode());

					//Generic filter condition
					if (searchPageData.getDeptType().equalsIgnoreCase(MarketplacewebservicesConstants.GENERIC))
					{

						if (facate.isGenericFilter())
						{
							categoryHierarchy.setVisible(Boolean.TRUE);
						}
						else
						{
							categoryHierarchy.setVisible(Boolean.FALSE);
						}
					}
					else
					{
						categoryHierarchy.setVisible(Boolean.valueOf((facate.isVisible())));
					}
					productSearchPage.setFacetdatacategory(categoryHierarchy);
				}
			}
			productSearchPage.setFacetdata(searchfacetDTOList);
		}
		else
		{
			productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			productSearchPage.setError(MarketplacecommerceservicesConstants.SEARCHNOTFOUND);
		}

		return productSearchPage;
	}

	private List<SellingItemDetailWsDto> getProductWsResults(
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData)
	{

		final List<SellingItemDetailWsDto> searchProductDTOList = new ArrayList<>();

		final boolean specialMobileFlag = configurationService.getConfiguration().getBoolean(
				MarketplacewebservicesConstants.SPECIAL_MOBILE_FLAG, false);

		double savingsAmt = 0.0;
		double calculatedPerSavings = 0.0;
		String floorValue = "";

		for (final ProductData productData : searchPageData.getResults())
		{
			final PriceWsData priceWsData = new PriceWsData();
			Boolean isRange = Boolean.FALSE;
			final SellingItemDetailWsDto sellingItemDetail = new SellingItemDetailWsDto();

			if (null != productData && null != productData.getCode())
			{
				if (null != productData.getUssID())
				{
					sellingItemDetail.setUssid(productData.getUssID());
				}


				final ImageData img = getPrimaryImageForProductAndFormat(productData, "product");
				final Map<String, String> formats = new HashMap<String, String>();
				final GalleryImageData galleryImageDataNew = new GalleryImageData();
				final List<GalleryImageData> galleryImageList = new ArrayList<GalleryImageData>();
				if (null != img)
				{
					if (null != img.getFormat() && !img.getFormat().isEmpty() && null != img.getUrl() && !img.getUrl().isEmpty())
					{
						formats.put(img.getFormat(), img.getUrl());
					}
					galleryImageDataNew.setGalleryImages(formats);

					if (null != img.getMediaType() && StringUtils.isNotEmpty(img.getMediaType().getCode()))
					{
						galleryImageDataNew.setMediaType(img.getMediaType().getCode());
					}
					else
					{
						galleryImageDataNew.setMediaType(MarketplacecommerceservicesConstants.IMAGE_MEDIA_TYPE);
					}
				}
				galleryImageList.add(galleryImageDataNew);
				sellingItemDetail.setGalleryImagesList(galleryImageList);

				if (null != productData.getName())
				{
					sellingItemDetail.setProductname(productData.getName());
				}
				if (null != productData.getDescription())
				{
					sellingItemDetail.setProductdescription(productData.getDescription());
				}
				if (null != productData.getCode())
				{
					sellingItemDetail.setProductId(productData.getCode());
				}
				if (null != productData.getAvailableForPickup())
				{
					sellingItemDetail.setAvailableForPickup(productData.getAvailableForPickup());
				}

				if (null != productData.getProductCategoryType())
				{
					sellingItemDetail.setProductCategoryType(productData.getProductCategoryType());
				}

				if (null != productData.getImmediateCategory())
				{
					sellingItemDetail.setCompareProductType(productData.getImmediateCategory());
				}
				sellingItemDetail.setDetails(productData.getDescription());
				if (null != productData.getMobileBrandName())
				{
					sellingItemDetail.setBrandname(productData.getMobileBrandName());
				}

				final ImageData imgData = getPrimaryImageForProductAndFormat(productData, "searchPage");
				if (imgData != null && imgData.getUrl() != null)
				{
					sellingItemDetail.setImageURL(imgData.getUrl());
				}


				if (null != productData.getDescription())
				{
					sellingItemDetail.setDetails(productData.getDescription());
				}

				if (null != productData.getProductMRP())
				{
					final PriceData priceData = getFormatPrice(productData.getProductMRP());

					priceWsData.setMrpPrice(priceData);
				}
				// Below codes are commented for channel specific promotion
				if (specialMobileFlag && null != productData.getMobileprice())
				{
					final PriceData priceData = getFormatPrice(productData.getMobileprice());
					priceWsData.setSellingPrice(priceData);
					//SDI-3930
					savingsAmt = productData.getProductMRP().getDoubleValue().doubleValue()
							- productData.getMobileprice().getDoubleValue().doubleValue();
					calculatedPerSavings = (savingsAmt / productData.getProductMRP().getDoubleValue().doubleValue()) * 100;
					floorValue = String.valueOf(Math.floor((calculatedPerSavings * 100.0) / 100.0));
				}
				else if (!specialMobileFlag && null != productData.getPrice()) //backward compatible
				{
					final PriceData priceData = getFormatPrice(productData.getPrice());
					priceWsData.setSellingPrice(priceData);
					//SDI-3930
					savingsAmt = productData.getProductMRP().getDoubleValue().doubleValue()
							- productData.getPrice().getDoubleValue().doubleValue();
					calculatedPerSavings = (savingsAmt / productData.getProductMRP().getDoubleValue().doubleValue()) * 100;
					floorValue = String.valueOf(Math.floor((calculatedPerSavings * 100.0) / 100.0));
				}

				//SDI-3930
				if (StringUtils.isNotEmpty(floorValue) && calculatedPerSavings >= 1.0)
				{
					sellingItemDetail.setDiscountPercent(floorValue);
				}
				else if (null != productData.getSavingsOnProduct() && null != productData.getSavingsOnProduct().getValue())
				{
					sellingItemDetail.setDiscountPercent(String.valueOf(productData.getSavingsOnProduct().getValue().intValue()));
				}

				//added for jewellery mobile web services:maxSellingPrice & minSellingPrice
				if (null != productData.getProductCategoryType()
						&& MarketplacewebservicesConstants.FINEJEWELLERY.equalsIgnoreCase(productData.getProductCategoryType()))
				{
					isRange = Boolean.TRUE;
					PriceData pDataMax = new PriceData();
					PriceData pDataMin = new PriceData();
					final List<BuyBoxModel> buyModList = buyBoxDao.getVariantListForPriceRange(productData.getCode());
					if (CollectionUtils.isNotEmpty(buyModList))
					{
						final List<BuyBoxModel> modifiableBuyBox = new ArrayList<BuyBoxModel>(buyModList);
						modifiableBuyBox.sort(Comparator.comparing(BuyBoxModel::getPrice).reversed());

						if (CollectionUtils.isNotEmpty(modifiableBuyBox))
						{
							pDataMin = productDetailsHelper.formPriceData(modifiableBuyBox.get(modifiableBuyBox.size() - 1).getPrice());
							pDataMax = productDetailsHelper.formPriceData(modifiableBuyBox.get(0).getPrice());
						}
					}
					PriceData priceData = null;
					if (null != pDataMin && null != pDataMax)
					{
						priceData = getFormatPrice(pDataMax);
						priceWsData.setMaxPrice(priceData);

						priceData = getFormatPrice(pDataMin);
						priceWsData.setMinPrice(priceData);
					}
				}
				priceWsData.setIsRange(isRange);
				sellingItemDetail.setPrice(priceWsData);
				//				if (null != productData.getInStockFlag())
				//				{
				//					sellingItemDetail.setInStockFlag(productData.getInStockFlag());
				//				}

				//CumulativeStock
				sellingItemDetail.setCumulativeStockLevel(Boolean.valueOf(productData.isStockValue()));
				if (null == productData.getIsOfferExisting())
				{
					sellingItemDetail.setIsOfferExisting(Boolean.FALSE);
				}
				else
				{
					sellingItemDetail.setIsOfferExisting(productData.getIsOfferExisting());
				}

				sellingItemDetail.setNewProduct(productData.getIsProductNew());
				sellingItemDetail.setOnlineExclusive(productData.getIsOnlineExclusive());

				searchProductDTOList.add(sellingItemDetail);
			}
			else
			{
				return null;
			}
		}
		return searchProductDTOList;

	}

	/**
	 * @param productMRP
	 * @return
	 */
	private PriceData getFormatPrice(final PriceData productMRP)
	{
		// YTODO Auto-generated method stub
		final PriceData priceData = new PriceData();
		priceData.setCurrencyIso(productMRP.getCurrencyIso());
		priceData.setCurrencySymbol(productMRP.getFormattedValue().substring(0, 1));
		priceData.setDoubleValue(productMRP.getDoubleValue());
		priceData.setFormattedValue(productMRP.getFormattedValue().substring(1));
		return priceData;
	}

}
