/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.storefront.controllers.misc;

import de.hybris.platform.acceleratorcms.model.components.MiniCartComponentModel;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.promotions.model.AbstractPromotionModel;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.granule.json.JSONObject;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.promotion.service.SellerBasedPromotionService;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.util.ExceptionUtil;

import atg.taglib.json.util.JSONException;


/**
 * Controller for MiniCart functionality which is not specific to a page.
 */
@Controller
@Scope("tenant")
public class MiniCartController extends AbstractController
{
	protected static final Logger LOG = Logger.getLogger(MiniCartController.class);
	/**
	 * We use this suffix pattern because of an issue with Spring 3.1 where a Uri value is incorrectly extracted if it
	 * contains on or more '.' characters. Please see https://jira.springsource.org/browse/SPR-6164 for a discussion on
	 * the issue and future resolution.
	 */
	private static final String TOTAL_DISPLAY_PATH_VARIABLE_PATTERN = "{totalDisplay:.*}";
	private static final String COMPONENT_UID_PATH_VARIABLE_PATTERN = "{componentUid:.*}";

	private static final String SSHIP = "SShip";

	@Resource(name = "mplCartFacade")
	private MplCartFacade cartFacade;

	@Resource(name = "cmsComponentService")
	private CMSComponentService cmsComponentService;

	@Resource(name = "sellerBasedPromotionService")
	private SellerBasedPromotionService promotionService;

	@Autowired
	private MplSellerInformationService mplSellerInformationService;

	//	@Resource(name = "configurationService")
	//	private ConfigurationService configurationService;

	@RequestMapping(value = "/cart/miniCart/" + TOTAL_DISPLAY_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String getMiniCart(@PathVariable final String totalDisplay, final Model model)
	{
		final CartData cartData = cartFacade.getMiniCart();
		model.addAttribute("totalPrice", cartData.getTotalPrice());
		model.addAttribute("subTotal", cartData.getSubTotal());
		if (cartData.getDeliveryCost() != null)
		{
			final PriceData withoutDelivery = cartData.getDeliveryCost();
			withoutDelivery.setValue(cartData.getTotalPrice().getValue().subtract(cartData.getDeliveryCost().getValue()));
			model.addAttribute("totalNoDelivery", withoutDelivery);
		}
		else
		{
			model.addAttribute("totalNoDelivery", cartData.getTotalPrice());
		}
		model.addAttribute("totalItems", cartData.getTotalUnitCount());
		model.addAttribute("totalDisplay", totalDisplay);
		model.addAttribute("masterItems", cartData.getTotalItems());
		return ControllerConstants.Views.Fragments.Cart.MiniCartPanel;
	}

	@RequestMapping(value = "/cart/rollover/" + COMPONENT_UID_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String rolloverMiniCartPopup(@PathVariable final String componentUid, final Model model) throws CMSItemNotFoundException
	{
		final CartData cartData = cartFacade.getSessionCart();
		model.addAttribute("cartData", cartData);

		final MiniCartComponentModel component = (MiniCartComponentModel) cmsComponentService.getSimpleCMSComponent(componentUid);

		final List<OrderEntryData> entries = cartData.getEntries();
		if (entries != null)
		{
			Collections.reverse(entries);
			for (final OrderEntryData entry : entries)
			{
				final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(entry.getSelectedUssid());

				List<RichAttributeModel> richAttributeModel = null;
				if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
				{
					richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
					if (richAttributeModel != null && richAttributeModel.get(0).getDeliveryFulfillModes() != null)
					{
						String fulfillmentType = richAttributeModel.get(0).getDeliveryFulfillModes().getCode();

						if (fulfillmentType.equalsIgnoreCase(SSHIP))
						{
							fulfillmentType = sellerInfoModel.getSellerName();
						}
						entry.getProduct().setDeliveryFulfillMode(fulfillmentType);
					}
				}

			}
			model.addAttribute("entries", entries);

			model.addAttribute("numberItemsInCart", Integer.valueOf(entries.size()));
			if (entries.size() < component.getShownProductCount())
			{
				model.addAttribute("numberShowing", Integer.valueOf(entries.size()));
			}
			else
			{
				model.addAttribute("numberShowing", Integer.valueOf(component.getShownProductCount()));
			}
		}
		model.addAttribute("lightboxBannerComponent", component.getLightboxBannerComponent());

		return ControllerConstants.Views.Fragments.Cart.CartPopup;
	}


	@RequestMapping(value = RequestMappingUrlConstants.TRANSIENTCARTAJAX, method = RequestMethod.GET)
	public @ResponseBody JSONObject showTransientCart(@RequestParam("ussid") final String ussid)
			throws JSONException, CMSItemNotFoundException, UnsupportedEncodingException, com.granule.json.JSONException
	{
		final JSONObject transientCartJSON = new JSONObject();
		try
		{
			//final CartData cartData = cartFacade.getMiniCart();
			final CartData cartData = cartFacade.getSessionCartWithEntryOrdering(true);
			final OrderEntryData requiredCartEntry = cartFacade.getCartEntryByUssid(ussid, cartData);

			if (requiredCartEntry != null)
			{
				final ProductData productData = requiredCartEntry.getProduct();
				if (productData != null)
				{
					transientCartJSON.put("productTitle", productData.getProductTitle());
					transientCartJSON.put("productUrl", productData.getUrl());
					if (productData.getBrand() != null)
					{
						transientCartJSON.put("brand", productData.getBrand().getBrandname());
					}
				}

				final List<ImageData> images = (List<ImageData>) productData.getImages();
				String imageUrl = MarketplacecommerceservicesConstants.MISSING_IMAGE_URL;

				if (CollectionUtils.isNotEmpty(images))
				{
					for (final ImageData image : images)
					{
						if (image.getMediaPriority() != null && image.getMediaPriority().intValue() == 1
								&& image.getFormat().equalsIgnoreCase("cartPage"))
						{
							imageUrl = image.getUrl();
						}
					}
				}
				transientCartJSON.put("productImageUrl", imageUrl);

				if (StringUtils.isNotEmpty(requiredCartEntry.getProductPromoCode()))
				{
					try
					{
						List<AbstractPromotionModel> promotions = new ArrayList<AbstractPromotionModel>();
						promotions = promotionService.fetchPromotionDetails(requiredCartEntry.getProductPromoCode());
						if (promotions != null && promotions.size() > 0)
						{
							transientCartJSON.put("offer", promotions.get(0).getTitle());
						}
					}
					catch (final EtailBusinessExceptions e)
					{
						ExceptionUtil.etailBusinessExceptionHandler(e, null);

					}
					catch (final EtailNonBusinessExceptions e)
					{
						ExceptionUtil.etailNonBusinessExceptionHandler(e);

					}

					catch (final Exception e)
					{
						ExceptionUtil.etailNonBusinessExceptionHandler(
								new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000));

					}



				}
			}


		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);

		}
		catch (final Exception e)
		{
			ExceptionUtil
					.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000));

		}

		return transientCartJSON;
	}


}
