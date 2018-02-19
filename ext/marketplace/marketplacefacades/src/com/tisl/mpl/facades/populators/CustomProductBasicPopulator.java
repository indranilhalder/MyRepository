/**
 * @author TCS
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.BrandData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.variants.model.VariantProductModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.core.model.BrandModel;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.core.model.RatingReviewModel;
import com.tisl.mpl.core.model.SeoContentModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPriceRowService;
import com.tisl.mpl.marketplacecommerceservices.services.product.MplCustomerReviewService;



/**
 *
 * @author TCS
 *
 * @param <SOURCE>
 * @param <TARGET>
 */
public class CustomProductBasicPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		AbstractProductPopulator<SOURCE, TARGET>
{


	protected static final Logger LOG = Logger.getLogger(CustomProductBasicPopulator.class);

	@Resource
	private MplPriceRowService mplPriceRowService;

	@Resource
	private ConfigurationService configurationService;

	private PriceDataFactory priceDataFactory;

	@Resource(name = "mplDeliveryCostService")
	private MplDeliveryCostService mplDeliveryCostService;

	@Autowired
	private MplCustomerReviewService mplCustomerReviewService;

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	private static int seoTitleLimit = 100;
	private static int seoDescLimit = 200;
	//TISPRD-4977
	private static int seoKeywordLimit = 150;

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	@Required
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

	/**
	 * @return the mplPriceRowService
	 */
	public MplPriceRowService getMplPriceRowService()
	{
		return mplPriceRowService;
	}

	/**
	 * @param mplPriceRowService
	 *           the mplPriceRowService to set
	 */
	public void setMplPriceRowService(final MplPriceRowService mplPriceRowService)
	{
		this.mplPriceRowService = mplPriceRowService;
	}

	/**
	 * @description method is to populate basic product details in pdp
	 * @param productModel
	 * @param productData
	 * @throws ConversionException
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		productData.setName((String) getProductAttribute(productModel, ProductModel.NAME));
		productData.setArticleDescription((String) getProductAttribute(productModel, ProductModel.ARTICLEDESCRIPTION));
		productData.setManufacturer((String) getProductAttribute(productModel, ProductModel.MANUFACTURERNAME));
		//Listing Id wont be populated
		productData.setListingId((String) getProductAttribute(productModel, ProductModel.CODE));

		//TISEE-5406 fix
		productData.setProductTitle(productModel.getTitle() == null ? productData.getName() : productModel.getTitle());

		//Electronics may have variants.
		if (productModel instanceof PcmProductVariantModel)
		{
			final PcmProductVariantModel pcmProductVariantModel = (PcmProductVariantModel) productModel;
			//Size will not be populated for Electronics
			if (pcmProductVariantModel.getSize() != null)
			{
				productData.setSize(pcmProductVariantModel.getSize());
			}
			productData.setColour(pcmProductVariantModel.getColour());
		}
		/* Populating rating review details */
		if (null != productModel.getRatingReview())
		{
			final RatingReviewModel ratingReviewModel = productModel.getRatingReview();
			productData.setAverageRating(ratingReviewModel.getMplAverageRating());
			productData.setRatingCount(ratingReviewModel.getMplReviewCount());


		}

		productData.setAverageRating(productModel.getAverageRating());
		if (null != productModel.getAverageRating() && productModel.getAverageRating().doubleValue() > 0.0)
		{
			final List<List<Object>> list = mplCustomerReviewService.getGroupByRatingsForProd(productModel);
			if (list.isEmpty())
			{
				LOG.debug("No reviews forund for this product hence list is empty");
			}
			else
			{
				populateGroupedRatings(productData, list);
			}

		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("No reviews found for this product hence average rating is 0");
			}
		}

		if (null != productModel.getMrp())
		{
			productData.setProductMRP(formPriceData(productModel.getMrp()));
		}

		//Brand name population - TISPRO-363
		productData.setBrand(populateBrandData(productModel));

		//		if (null != productModel.getBrands())
		//		{
		//			final List<BrandModel> brands = new ArrayList<BrandModel>(productModel.getBrands());
		//			final BrandData brandData = new BrandData();
		//			if (!brands.isEmpty())
		//			{
		//				brandData.setBrandname(brands.get(0).getName());
		//				brandData.setBrandtype(brands.get(0).getTypeDescription());
		//				productData.setBrand(brandData);
		//			}
		//		}
		//Populating SEO title and description
		if (null != productModel.getSeoContents())
		{
			final List<SeoContentModel> seoContents = new ArrayList<SeoContentModel>(productModel.getSeoContents());
			populateSEOContent(seoContents, productModel, productData);
		}

		if (productModel.getVariantType() != null)
		{
			productData.setVariantType(productModel.getVariantType().getCode());
		}
		if (productModel instanceof VariantProductModel)
		{
			final VariantProductModel variantProduct = (VariantProductModel) productModel;
			productData.setBaseProduct(variantProduct.getBaseProduct() != null ? variantProduct.getBaseProduct().getCode() : null);
		}
		productData.setPurchasable(Boolean.valueOf(productModel.getVariantType() == null && isApproved(productModel)));
		//}

		//Added for Luxury Product, LW-216
		if ((productModel.getLuxIndicator() != null && productModel.getLuxIndicator().getCode() != null && productModel
				.getLuxIndicator().getCode().equalsIgnoreCase(MarketplaceCoreConstants.LUXURY)))
		{
			productData.setLuxIndicator(MarketplaceCoreConstants.LUXURY);
		}
	}

	private void populateGroupedRatings(final ProductData productData, final List<List<Object>> list)
	{
		for (final List<Object> row : list)
		{
			final Double rating = (Double) row.get(0);
			final Integer ratingCount = (Integer) row.get(1);
			final Float ratingPercent = (Float) row.get(2);
			if (rating.doubleValue() == 1.0)
			{
				productData.setMplOneStar(ratingCount);
				productData.setMplOneStarFill(ratingPercent);
			}
			else if (rating.doubleValue() == 2.0)
			{
				productData.setMplTwoStar(ratingCount);
				productData.setMplTwoStarFill(ratingPercent);
			}
			else if (rating.doubleValue() == 3.0)
			{
				productData.setMplThreeStar(ratingCount);
				productData.setMplThreeStarFill(ratingPercent);
			}
			else if (rating.doubleValue() == 4.0)
			{
				productData.setMplFourStar(ratingCount);
				productData.setMplFourStarFill(ratingPercent);
			}
			else if (rating.doubleValue() == 5.0)
			{
				productData.setMplFiveStar(ratingCount);
				productData.setMplFiveStarFill(ratingPercent);
			}
		}
	}

	/**
	 * @description method is to populate brand data in pdp
	 * @param productModel
	 * @return BrandData
	 */
	private BrandData populateBrandData(final ProductModel productModel)
	{
		final BrandData brandData = new BrandData();
		//		final List<CategoryModel> categoryModels = new ArrayList<CategoryModel>(productModel.getSupercategories());
		//		if (!CollectionUtils.isEmpty(categoryModels))
		//		{
		//			for (final CategoryModel categoryModel : categoryModels)
		//			{
		//				if (null != categoryModel.getCode() && categoryModel.getCode().startsWith(MarketplaceFacadesConstants.BRANDCODE))
		//				{
		//					brandData.setBrandname(categoryModel.getName());
		//					brandData.setBrandtype(categoryModel.getDescription());
		//					break;
		//				}
		//			}
		//		}

		//This is fallback case i.e. if brand category is missing in CategorySystem then it will pick from product core attribute
		if (null == brandData.getBrandname())
		{
			if (null != productModel.getBrands())
			{
				final List<BrandModel> brands = new ArrayList<BrandModel>(productModel.getBrands());
				if (!brands.isEmpty())
				{
					brandData.setBrandname(brands.get(0).getName());
					brandData.setBrandtype(brands.get(0).getTypeDescription());
					brandData.setBrandDescription(brands.get(0).getDescription()); // CKD:TPR-250
					brandData.setBrandCode((brands.get(0).getBrandCode())); // CKD:TPR-250
				}
			}
		}

		return brandData;
	}

	protected boolean isApproved(final SOURCE productModel)
	{
		final ArticleApprovalStatus approvalStatus = productModel.getApprovalStatus();
		return ArticleApprovalStatus.APPROVED.equals(approvalStatus);
	}


	/**
	 * Converting datatype of price
	 *
	 * @param price
	 * @return pData
	 */

	public PriceData formPriceData(final Double price)
	{
		final PriceData priceData = new PriceData();
		priceData.setPriceType(PriceDataType.BUY);
		priceData.setValue(new BigDecimal(price.doubleValue()));
		priceData.setCurrencyIso("\u20B9");
		final CurrencyModel currency = new CurrencyModel();
		currency.setIsocode(priceData.getCurrencyIso());
		currency.setSymbol(priceData.getCurrencyIso());
		final PriceData pData = priceDataFactory.create(PriceDataType.BUY, priceData.getValue(), currency);
		return pData;
	}

	/**
	 * @return the mplDeliveryCostService
	 */
	public MplDeliveryCostService getMplDeliveryCostService()
	{
		return mplDeliveryCostService;
	}

	/**
	 * @param mplDeliveryCostService
	 *           the mplDeliveryCostService to set
	 */
	public void setMplDeliveryCostService(final MplDeliveryCostService mplDeliveryCostService)
	{
		this.mplDeliveryCostService = mplDeliveryCostService;
	}

	private void populateSEOContent(final List<SeoContentModel> seoContents, final ProductModel productModel,
			final ProductData productData)
	{
		// TISPRD-4335
		seoTitleLimit = Integer.parseInt(configurationService.getConfiguration().getString("seo.title.limit",
				String.valueOf(seoTitleLimit)));
		seoDescLimit = Integer.parseInt(configurationService.getConfiguration().getString("seo.desc.limit",
				String.valueOf(seoDescLimit)));
		//TISPRD-4977
		seoKeywordLimit = Integer.parseInt(configurationService.getConfiguration().getString("seo.keyword.limit",
				String.valueOf(seoKeywordLimit)));
		final StringBuilder seoMetaTitle = new StringBuilder(200);
		if (null != seoContents && !(seoContents.isEmpty()))
		{
			final SeoContentModel seoContentModel = seoContents.get(seoContents.size() - 1);
			if (null != seoContentModel.getSeoMetaTitle())
			{
				//Populating title from SeoMetaTitle
				seoMetaTitle.append(seoContentModel.getSeoMetaTitle());
			}
			else
			{
				//Populating title from Title
				if (null != productModel.getTitle())
				{
					seoMetaTitle.append((String) getProductAttribute(productModel, ProductModel.TITLE));
				}
			}
			// Sonar Major This is an inefficient use of StringBuffer.toString; call StringBuffer.length instead.
			if (seoMetaTitle.length() > seoTitleLimit)
			{
				productData.setSeoMetaTitle(seoMetaTitle.toString().substring(0, seoTitleLimit - 1));
			}
			else
			{
				productData.setSeoMetaTitle(seoMetaTitle.toString());
			}

			if (null != seoContentModel.getSeoMetaDescription())
			{
				//Populating description from SeoMetaDescription
				final String seoDesc = seoContentModel.getSeoMetaDescription();
				if (seoDesc.length() > seoDescLimit)
				{
					productData.setSeoMetaDescription(seoDesc.substring(0, seoDescLimit - 1));
				}
				else
				{
					productData.setSeoMetaDescription(seoDesc);
				}
			}
			else
			{
				//Populating description from ArticleDescription
				if (null != productModel.getArticleDescription())
				{
					if (productModel.getArticleDescription().length() > seoDescLimit)
					{
						final String seoDesc = (String) getProductAttribute(productModel, ProductModel.ARTICLEDESCRIPTION);
						productData.setSeoMetaDescription(seoDesc.substring(0, seoDescLimit - 1));
						//productData.setSeoMetaDescription((String) getProductAttribute(productModel,
						//ProductModel.ARTICLEDESCRIPTION.substring(0, seoDescLimit - 1)));
					}
					else
					{
						productData.setSeoMetaDescription((String) getProductAttribute(productModel, ProductModel.ARTICLEDESCRIPTION));
					}
				}
			}
			//TISPRD-4977
			if (null != seoContentModel.getSeoMetaKeyword())
			{
				//Populating keyword from SeoMetaDescription
				final String seoKeyword = seoContentModel.getSeoMetaKeyword();
				if (seoKeyword.length() > seoKeywordLimit)
				{
					productData.setSeoMetaKeyword(seoKeyword.substring(0, seoKeywordLimit - 1));
				}
				else
				{
					productData.setSeoMetaKeyword(seoKeyword);
				}
			}

		}
		else
		{
			if (null != productModel.getTitle())
			{
				seoMetaTitle.append((String) getProductAttribute(productModel, ProductModel.TITLE));
			}
			//			seoMetaTitle.append(MarketplaceFacadesConstants.SPACE);
			//			seoMetaTitle.append((String) getProductAttribute(productModel, ProductModel.CODE));
			if (seoMetaTitle.toString().length() > seoTitleLimit)
			{
				productData.setSeoMetaTitle(seoMetaTitle.toString().substring(0, seoTitleLimit - 1));
			}
			else
			{
				productData.setSeoMetaTitle(seoMetaTitle.toString());
			}
			//Populating description from ArticleDescription
			if (null != productModel.getArticleDescription())
			{
				if (productModel.getArticleDescription().length() > seoDescLimit)
				{
					//ProductModel.ARTICLEDESCRIPTION.substring(0, seoDescLimit - 1)
					final String seoDesc = (String) getProductAttribute(productModel, ProductModel.ARTICLEDESCRIPTION);
					productData.setSeoMetaDescription(seoDesc.substring(0, seoDescLimit - 1));
				}
				else
				{
					productData.setSeoMetaDescription((String) getProductAttribute(productModel, ProductModel.ARTICLEDESCRIPTION));
				}
			}
		}

	}
}