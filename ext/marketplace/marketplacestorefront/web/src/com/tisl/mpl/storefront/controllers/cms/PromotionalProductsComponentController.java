/*
 * (non-Javadoc)
 *
 * @see
 * com.tisl.mpl.storefront.controllers.cms.AbstractCMSComponentController#fillModel(javax.servlet.http.HttpServletRequest
 * , org.springframework.ui.Model, de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)
 */


package com.tisl.mpl.storefront.controllers.cms;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.enums.OnlineExclusiveEnum;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.model.EtailSellerSpecificRestrictionModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.model.cms.components.PromotionalProductsComponentModel;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "PMD" })
@Controller("PromotionalProductsComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.PromotionalProductsComponent)
public class PromotionalProductsComponentController extends AbstractCMSComponentController<PromotionalProductsComponentModel>
{



	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE,
			ProductOption.VARIANT_FULL);

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;
	@Autowired
	private BuyBoxFacade buyBox;
	@Autowired
	private ConfigurationService configurationService;





	/**
	 * @description It is used for populating Promotional Products in Brand landing page
	 * @param model
	 *
	 *
	 *
	 */

	@SuppressWarnings(
	{ "boxing", "deprecation" })
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final PromotionalProductsComponentModel component)
	{

		final String allowNew = configurationService.getConfiguration().getString("attribute.new.display", "Y");
		final CategoryModel brand = component.getBrand();
		final SellerMasterModel seller = component.getSeller();
		String brandName = "";
		String sellerId = "";
		if (brand != null)
		{
			brandName = brand.getName();
		}
		if (seller != null)
		{
			sellerId = seller.getId();
		}

		if (component.getPromotion() != null)
		{
			final AbstractPromotionModel promotion = component.getPromotion();

			LOG.debug("Promotion : " + promotion);

			final String identifier = component.getPromotion().getCode();
			String offerId = null;
			try
			{
				offerId = URLEncoder.encode(identifier, "UTF-8");
			}
			catch (final UnsupportedEncodingException e)
			{
				// YTODO Auto-generated catch block
				e.printStackTrace();
			}
			LOG.debug("Encoded offer id is:" + offerId);

			final List<ProductModel> productList = new ArrayList();


			if (promotion instanceof ProductPromotionModel)
			{
				final ProductPromotionModel productPromotionModel = (ProductPromotionModel) promotion;

				//STRAT:seller restriction check
				final Collection<AbstractPromotionRestrictionModel> Restriction = productPromotionModel.getRestrictions();
				Collection<SellerMasterModel> sellerRestrictions = new ArrayList<SellerMasterModel>();

				boolean sellerRestrictionExists = Boolean.FALSE;
				for (final AbstractPromotionRestrictionModel restrictionModel : Restriction)
				{
					if (restrictionModel instanceof EtailSellerSpecificRestrictionModel)
					{
						final EtailSellerSpecificRestrictionModel etailSellerRestriction = (EtailSellerSpecificRestrictionModel) restrictionModel;
						sellerRestrictions = new ArrayList<SellerMasterModel>(etailSellerRestriction.getSellerMasterList());

					}

				}

				if (sellerRestrictions.size() > 0)
				{

					sellerRestrictionExists = Boolean.TRUE;


				}


				model.addAttribute("sellerRestrictionExists", sellerRestrictionExists);
				model.addAttribute("SellerRestriction", sellerRestrictions);
				//END:seller restriction check

				final Date currentDate = new Date();


				if ((productPromotionModel.getStartDate().before(currentDate))
						&& (productPromotionModel.getEndDate().after(currentDate)) && productPromotionModel.getEnabled().booleanValue())

				{


					if (productPromotionModel.getCategories() != null && !productPromotionModel.getCategories().isEmpty())
					{
						for (final CategoryModel promotionalCategory : productPromotionModel.getCategories())
						{
							if (promotionalCategory.getProducts() != null)
							{
								for (final ProductModel promotionalProduct : promotionalCategory.getProducts())
								{
									productList.add(promotionalProduct);
								}
							}
						}

					}
					else
					{
						if (productPromotionModel.getProducts() != null)
						{
							for (final ProductModel promotionalProduct : productPromotionModel.getProducts())
							{
								productList.add(promotionalProduct);
							}
						}
					}

					final List<ProductData> productDataList = new ArrayList<>();

					if (productList.size() > 0)
					{

						for (final ProductModel productModel : productList)
						{

							final boolean isOnlineExclusive = checkOnlineExclusive(productModel, sellerId);
							final boolean isNew = checkNew(productModel, sellerId, allowNew);
							try
							{
								if (productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS) != null)
								{
									final ProductData productData = productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS);
									if (!sellerId.isEmpty() && productData.getSeller() != null)
									{

										for (final SellerInformationData sellerData : productData.getSeller())
										{
											if (sellerData.getSellerID().equals(sellerId))
											{
												final BuyBoxData buyBoxData = buyBox.buyboxPrice(productData.getCode());
												productData.setProductMOP(buyBoxData.getSpecialPrice());
												productData.setProductMRP(buyBoxData.getMrpPriceValue());
												LOG.debug("Checking product " + productData.getCode() + "for online Exclusive:::"
														+ isOnlineExclusive);
												productData.setIsOnlineExclusive(isOnlineExclusive);
												LOG.debug("Checking product " + productData.getCode() + "for new:::" + isNew);
												productData.setIsProductNew(isNew);
												productDataList.add(productData);
											}


										}
									}
									else if (productData.getBrand() != null
											&& productData.getBrand().getBrandname().equalsIgnoreCase(brandName))
									{
										productDataList.add(productData);
									}

								}
							}
							catch (final Exception exception)
							{
								LOG.error("Exception is : " + exception);
								throw new EtailNonBusinessExceptions(exception);
							}
						}


					}


					if (sellerRestrictionExists == Boolean.TRUE)
					{

						for (final SellerMasterModel sellerMaster : sellerRestrictions)
						{
							if (sellerMaster.getId().equalsIgnoreCase(sellerId))
							{
								model.addAttribute("productData", productDataList);
							}

						}

					}
					else if (sellerRestrictionExists == Boolean.FALSE)
					{
						model.addAttribute("productData", productDataList);
					}

				}

			}
			model.addAttribute("offerId", offerId);
		}

	}

	/**
	 * @param productModel
	 * @param sellerId
	 * @param allowNew
	 * @return
	 */
	@SuppressWarnings("javadoc")
	private boolean checkNew(final ProductModel productModel, final String sellerId, final String allowNew)
	{
		for (final SellerInformationModel seller : productModel.getSellerInformationRelator())
		{
			if (seller.getSellerID().equalsIgnoreCase(sellerId) && StringUtils.isNotEmpty(allowNew)
					&& allowNew.equalsIgnoreCase("Y") && seller.getStartDate() != null && isNew(seller.getStartDate()))
			{
				return true;
			}
		}
		return false;
	}





	/**
	 * @param productModel
	 * @param sellerId
	 * @return
	 */
	@SuppressWarnings("javadoc")
	private boolean checkOnlineExclusive(final ProductModel productModel, final String sellerId)
	{
		for (final SellerInformationModel seller : productModel.getSellerInformationRelator())
		{
			if (seller.getSellerID().equalsIgnoreCase(sellerId))
			{
				if (null != seller.getOnlineExclusive()
						&& (OnlineExclusiveEnum.YES).toString().equalsIgnoreCase(seller.getOnlineExclusive().getCode()))
				{
					return true;
				}
			}

		}
		return false;
	}

	private boolean isNew(final Date existDate)
	{
		boolean newAttr = false;
		if (null != existDate)
		{
			final Date sysDate = new Date();
			final long dayDiff = calculateDays(existDate, sysDate);

			LOG.info("******" + existDate + "  --- dayDiff: " + dayDiff);
			final String validDaysSt = configurationService.getConfiguration().getString("attribute.new.validDays");
			final int validDays = validDaysSt == null ? 0 : Integer.parseInt(validDaysSt);

			if (validDays > dayDiff)
			{
				newAttr = true;
			}
		}

		return newAttr;
	}

	private long calculateDays(final Date dateEarly, final Date dateLater)
	{
		return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);
	}




}