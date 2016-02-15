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
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.facades.product.data.ProductTagDto;
import com.tisl.mpl.util.MplCompetingProductsUtility;
import com.tisl.mpl.wsdto.AutoCompleteResultWsData;
import com.tisl.mpl.wsdto.CategorySNSWsData;
import com.tisl.mpl.wsdto.DepartmentFilterWsDto;
import com.tisl.mpl.wsdto.DepartmentHierarchy;
import com.tisl.mpl.wsdto.FacetDataWsDTO;
import com.tisl.mpl.wsdto.FacetValueDataWsDTO;
import com.tisl.mpl.wsdto.GalleryImageData;
import com.tisl.mpl.wsdto.L1DepartmentFilterWsDto;
import com.tisl.mpl.wsdto.L2DepartmentFilterWsDto;
import com.tisl.mpl.wsdto.L3DepartmentFilterWsDto;
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

	//	@Resource(name = "productService")
	//	private ProductService productService;
	//
	//	@Resource(name = "cwsProductFacade")
	//	private ProductFacade productFacade;

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
		if (null != productData.getPrice())
		{
			wsDto.setSellingPrice(productData.getPrice());
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

	public ProductSearchPageWsDto setSearchPageData(final ProductSearchPageWsDto productSearchPage,
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData)
	{
		final List<FacetDataWsDTO> searchfacetDTOList = new ArrayList<>();
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
						&& !facate.getCode().equalsIgnoreCase("deptType") && !facate.getCode().equalsIgnoreCase("sellerId")
						&& !facate.getCode().equalsIgnoreCase("category") && !facate.getCode().equalsIgnoreCase("micrositeSnsCategory")
						&& !facate.getCode().equalsIgnoreCase("allPromotions"))
				{
					final FacetDataWsDTO facetWsDTO = new FacetDataWsDTO();

					//	facetWsDTO.setCategory(facate.getCode());
					facetWsDTO.setMultiSelect(Boolean.valueOf((facate.isCategory())));
					if (null != facate.getName())
					{
						facetWsDTO.setName(facate.getName());
					}
					facetWsDTO.setCategory(Boolean.valueOf((facate.isCategory())));
					facetWsDTO.setPriority(Integer.valueOf((facate.getPriority())));
					facetWsDTO.setKey(facate.getCode());

					//Generic filter condition
					if (searchPageData.getDeptType().equalsIgnoreCase("Generic"))
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
							if (null != values.getQuery().getUrl())
							{
								facetValueWsDTO.setUrl(values.getQuery().getUrl().toString());
							}
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

	private final List<GalleryImageData> getGalleryImagesList(final List<Map<String, String>> galleryImages)
	{
		final List<GalleryImageData> galleryImageList = new ArrayList<GalleryImageData>();
		GalleryImageData galleryImage = null;
		for (final Map<String, String> map : galleryImages)
		{
			galleryImage = new GalleryImageData();
			galleryImage.setGalleryImages(map);
			galleryImageList.add(galleryImage);
		}

		return galleryImageList;
	}

	private List<SellingItemDetailWsDto> getProductResults(
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData)
	{
		final List<SellerItemDetailWsDto> sellerItemDetailWsDtoList = new ArrayList<>();
		final List<SellingItemDetailWsDto> searchProductDTOList = new ArrayList<>();
		final String emiCuttOffAmount = configurationService.getConfiguration().getString("marketplace.emiCuttOffAmount");

		for (final ProductData productData : searchPageData.getResults())
		{
			final SellingItemDetailWsDto sellingItemDetail = new SellingItemDetailWsDto();
			final List<VariantOptionsWsDto> variantOptionsWsDtoWsDtoList = new ArrayList<>();

			if (null != productData)
			{
				final List<Map<String, String>> gallaryImages = getGalleryImages(productData);
				if (null != gallaryImages)
				{

					final List<GalleryImageData> gallaryImagesList = getGalleryImagesList(gallaryImages);

					if (!gallaryImagesList.isEmpty())
					{
						sellingItemDetail.setGalleryImagesList(gallaryImagesList);
					}
				}
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

				final ImageData imgData = getPrimaryImageForProductAndFormat(productData, "searchPage");

				if (imgData != null && imgData.getUrl() != null)
				{

					sellingItemDetail.setImageURL(imgData.getUrl());
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
				if (null != productData.getPrice())
				{
					sellingItemDetail.setSellingPrice(productData.getPrice());
				}
				if (null != productData.getInStockFlag())
				{
					sellingItemDetail.setInStockFlag(productData.getInStockFlag());
				}
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
				else
				{
					sellerItemDetailWsDto = new SellerItemDetailWsDto();
					sellerItemDetailWsDto.setSellerId("767865");
					sellerItemDetailWsDto.setSellerName("TATA");
					sellerItemDetailWsDto.setEMItag("Y");
					sellerItemDetailWsDto.setOfferprice("767");
					sellerItemDetailWsDtoList.add(sellerItemDetailWsDto);
				}
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

	private List<Map<String, String>> getGalleryImages(final ProductData productData)
	{

		final List<Map<String, String>> galleryImages = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(productData.getImages()))
		{
			final List<ImageData> images = new ArrayList<>();
			for (final ImageData image : productData.getImages())
			{
				if (ImageDataType.GALLERY.equals(image.getImageType()))
				{
					images.add(image);
				}
			}
			Collections.sort(images, new Comparator<ImageData>()
			{
				@Override
				public int compare(final ImageData image1, final ImageData image2)
				{
					return image1.getGalleryIndex().compareTo(image2.getGalleryIndex());
				}
			});

			if (CollectionUtils.isNotEmpty(images))
			{
				int currentIndex = images.get(0).getGalleryIndex().intValue();
				Map<String, String> formats = new HashMap<String, String>();
				for (final ImageData image : images)
				{
					if (currentIndex != image.getGalleryIndex().intValue())
					{
						galleryImages.add(formats);
						formats = new HashMap<>();
						currentIndex = image.getGalleryIndex().intValue();
					}
					if (null != image.getFormat() && null != image.getUrl())
					{
						formats.put(image.getFormat(), image.getUrl());
					}
				}
				if (!formats.isEmpty() && formats.equals(MarketplacecommerceservicesConstants.THUMBNAIL))
				{
					galleryImages.add(formats);
				}
			}
		}
		return galleryImages;
	}

	public DepartmentHierarchy getDepartmentHierarchy(final List<String> departmentFilters)
	{
		final Set<String> traversedDepartments = new HashSet<String>();
		final DepartmentHierarchy departmentHierarchy = new DepartmentHierarchy();
		//	final List<L1DepartmentFilterWsDto> l1DepartmentFilterWsDtos = new ArrayList<L1DepartmentFilterWsDto>();

		if (departmentFilters != null && !departmentFilters.isEmpty())
		{
			for (final String departmentFil : departmentFilters)
			{
				final String traverseString = findTraversedCategories(traversedDepartments, departmentFil);
				if (traverseString.equals(MarketplacecommerceservicesConstants.DEPT_L3))
				{
					final String[] foundDeparts = departmentFil.split("/");
					if (foundDeparts.length > 4)
					{
						for (final L1DepartmentFilterWsDto oldL1Filter : departmentHierarchy.getFilters())
						{
							if (oldL1Filter.getCategoryCode().equals(foundDeparts[1].split(":")[0]))
							{
								for (final L2DepartmentFilterWsDto oldL2DepartFilter : oldL1Filter.getChildFilters())
								{
									if (oldL2DepartFilter.getCategoryCode().equals(foundDeparts[2].split(":")[0]))
									{
										for (final L3DepartmentFilterWsDto oldL3DepartFilter : oldL2DepartFilter.getChildFilters())
										{
											if (oldL3DepartFilter.getCategoryCode().equals(foundDeparts[3].split(":")[0]))
											{
												final DepartmentFilterWsDto newDepartmentFilter = getDepartmentFilter(
														foundDeparts[4].split(":"));
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
					final String[] foundDeparts = departmentFil.split("/");
					for (final L1DepartmentFilterWsDto oldL1Filter : departmentHierarchy.getFilters())
					{
						if (oldL1Filter.getCategoryCode().equals(foundDeparts[1].split(":")[0]))
						{
							final L2DepartmentFilterWsDto l2DepartFilter = new L2DepartmentFilterWsDto();
							final L3DepartmentFilterWsDto l3DepartFilter = new L3DepartmentFilterWsDto();
							List<DepartmentFilterWsDto> l4List = new ArrayList<DepartmentFilterWsDto>();
							final List<L3DepartmentFilterWsDto> l3List = new ArrayList<L3DepartmentFilterWsDto>();
							for (int i = 2; i < foundDeparts.length; i++)
							{
								final String[] newCategories = foundDeparts[i].split(":");
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
								final List<L2DepartmentFilterWsDto> l2List = new ArrayList<L2DepartmentFilterWsDto>();
								l2List.add(l2DepartFilter);
								oldL1Filter.setChildFilters(l2List);
							}
							traversedDepartments.addAll(concateDepartmentString(departmentFil));
						}
					}

				}
				else if (traverseString.equals(MarketplacecommerceservicesConstants.DEPT_L2))
				{
					final String[] foundDeparts = departmentFil.split("/");
					for (final L1DepartmentFilterWsDto oldL1Filter : departmentHierarchy.getFilters())
					{
						if (oldL1Filter.getCategoryCode().equals(foundDeparts[1].split(":")[0]))
						{
							for (final L2DepartmentFilterWsDto oldL2DepartFilter : oldL1Filter.getChildFilters())
							{
								if (oldL2DepartFilter.getCategoryCode().equals(foundDeparts[2].split(":")[0]))
								{
									final L3DepartmentFilterWsDto l3DepartFilter = new L3DepartmentFilterWsDto();
									List<DepartmentFilterWsDto> l4List = new ArrayList<DepartmentFilterWsDto>();
									for (int i = 3; i < foundDeparts.length; i++)
									{
										final String[] newCategories = foundDeparts[i].split(":");
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
											final List<L3DepartmentFilterWsDto> l3List = new ArrayList<L3DepartmentFilterWsDto>();
											l3List.add(l3DepartFilter);
											oldL2DepartFilter.setChildFilters(l3List);
										}
										traversedDepartments.addAll(concateDepartmentString(departmentFil));

									}
								}
							}
						}
					}
				}
				else if (traverseString.equals(MarketplacecommerceservicesConstants.DEPT_L0))
				{
					final String[] newDeparts = departmentFil.split("/");
					final L1DepartmentFilterWsDto l1DepartFilter = new L1DepartmentFilterWsDto();
					final L2DepartmentFilterWsDto l2DepartFilter = new L2DepartmentFilterWsDto();
					final L3DepartmentFilterWsDto l3DepartFilter = new L3DepartmentFilterWsDto();
					List<DepartmentFilterWsDto> l4List = new ArrayList<DepartmentFilterWsDto>();
					final List<L3DepartmentFilterWsDto> l3List = new ArrayList<L3DepartmentFilterWsDto>();
					final List<L2DepartmentFilterWsDto> l2List = new ArrayList<L2DepartmentFilterWsDto>();
					final List<L1DepartmentFilterWsDto> l1List = new ArrayList<L1DepartmentFilterWsDto>();
					for (int i = 1; i < newDeparts.length; i++)
					{
						final String[] newCategories = newDeparts[i].split(":");
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
						else if (newCategories[2].equals(MarketplacecommerceservicesConstants.DEPT_L3))
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
					traversedDepartments.addAll(concateDepartmentString(departmentFil));

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
		final String[] categories = departmentFil.split(":");
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

	private List<String> concateDepartmentString(final String departmentFil)
	{
		final String[] categories = departmentFil.split(":");
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

	public ProductSearchPageWsDto setFilterData(final ProductSearchPageWsDto productSearchPage,
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData)
	{
		/*
		 * final List<SellerItemDetailWsDto> sellerItemDetailWsDtoList = new ArrayList<>(); final
		 * List<SellingItemDetailWsDto> searchProductDTOList = new ArrayList<>(); final String emiCuttOffAmount =
		 * configurationService.getConfiguration().getString("marketplace.emiCuttOffAmount");
		 */
		final List<FacetDataWsDTO> searchfacetDTOList = new ArrayList<>();
		if (null != searchPageData.getFacets())
		{
			for (final FacetData<SearchStateData> facate : searchPageData.getFacets())
			{
				if (facate.isVisible() && !facate.getCode().equalsIgnoreCase("snsCategory")
						&& !facate.getCode().equalsIgnoreCase("deptType") && !facate.getCode().equalsIgnoreCase("sellerId")
						&& !facate.getCode().equalsIgnoreCase("category") && !facate.getCode().equalsIgnoreCase("micrositeSnsCategory"))
				{
					final FacetDataWsDTO facetWsDTO = new FacetDataWsDTO();

					//	facetWsDTO.setCategory(facate.getCode());
					facetWsDTO.setMultiSelect(Boolean.valueOf((facate.isCategory())));
					if (null != facate.getName())
					{
						facetWsDTO.setName(facate.getName());
					}
					facetWsDTO.setCategory(Boolean.valueOf((facate.isCategory())));
					facetWsDTO.setPriority(Integer.valueOf((facate.getPriority())));
					facetWsDTO.setKey(facate.getCode());

					//Generic filter condition
					if (searchPageData.getDeptType().equalsIgnoreCase("Generic"))
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
							if (null != values.getQuery().getUrl())
							{
								facetValueWsDTO.setUrl(values.getQuery().getUrl().toString());
							}
							//If facet name is "Include out of stock"  value will be false
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


}
