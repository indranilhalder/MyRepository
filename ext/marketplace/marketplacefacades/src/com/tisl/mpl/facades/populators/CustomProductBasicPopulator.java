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
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.BrandModel;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.core.model.RatingReviewModel;
import com.tisl.mpl.core.model.SeoContentModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPriceRowService;



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

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	private static int seoTitleLimit = 65;
	private static int seoDescLimit = 165;

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

		if (null != productModel.getMrp())
		{
			productData.setProductMRP(formPriceData(productModel.getMrp()));
		}

		if (null != productModel.getBrands())
		{
			final List<BrandModel> brands = new ArrayList<BrandModel>(productModel.getBrands());
			final BrandData brandData = new BrandData();
			if (!brands.isEmpty())
			{
				brandData.setBrandname(brands.get(0).getName());
				brandData.setBrandtype(brands.get(0).getTypeDescription());
				productData.setBrand(brandData);

			}


		}
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
		priceData.setCurrencyIso(MarketplaceFacadesConstants.INR);
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
		final StringBuilder seoMetaTitle = new StringBuilder(200);
		if (null != seoContents && !(seoContents.isEmpty()))
		{
			final SeoContentModel seoContentModel = seoContents.get(0);
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
			seoMetaTitle.append(MarketplaceFacadesConstants.SPACE);
			seoMetaTitle.append((String) getProductAttribute(productModel, ProductModel.CODE));
			// Sonar Major This is an inefficient use of StringBuffer.toString; call StringBuffer.length instead.
			//if (seoMetaTitle.toString().length() > seoTitleLimit)
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
		}
		else
		{
			if (null != productModel.getTitle())
			{
				seoMetaTitle.append((String) getProductAttribute(productModel, ProductModel.TITLE));
			}
			seoMetaTitle.append(MarketplaceFacadesConstants.SPACE);
			seoMetaTitle.append((String) getProductAttribute(productModel, ProductModel.CODE));
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
