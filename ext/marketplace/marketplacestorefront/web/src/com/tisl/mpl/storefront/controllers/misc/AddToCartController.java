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

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tis.mpl.facade.data.ProductValidationData;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.helper.AddToCartHelper;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.web.forms.MplAddToCartForm;


/**
 * Controller for Add to Cart functionality which is not specific to a certain page.
 */
@Controller
@Scope("tenant")
public class AddToCartController extends AbstractController
{

	protected static final Logger LOG = Logger.getLogger(AddToCartController.class);

	@Resource(name = "mplCartFacade")
	private MplCartFacade mplCartFacade;

	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	@Resource(name = "addToCartHelper")
	private AddToCartHelper addToCartHelper;

	//@Resource(name = "accProductFacade")
	//private ProductFacade productFacade;

	//@Resource(name = "sessionService")
	//private SessionService sessionService;

	//TPR-5346 STARTS
	@Resource(name = "productService")
	private ProductService productService;

	@Autowired
	private SiteConfigService siteConfigService;

	//TPR-5346 ENDS


	@ResponseBody
	@RequestMapping(value = "/cart/add", method = RequestMethod.POST, produces = "application/json")
	public String addToCart(@RequestParam("productCodePost") final String code, final Model model,
			@Valid final MplAddToCartForm form, @RequestParam("wishlistNamePost") final String wishlistName,
			@RequestParam("ussid") final String ussid, @RequestParam(value = "l3", required = false) final String l3,
			@RequestParam(value = "exchangeParam", required = false) final String exchangeParam,
			@RequestParam(value = "brandParam", required = false) final String brand,
			@RequestParam(value = "pinParam", required = false) final String pincode)
	{
		try
		{
			model.addAttribute(ModelAttributetConstants.USSID, ussid);
			/*
			 * if (bindingErrors.hasErrors()) { return getViewWithBindingErrorMessages(model, bindingErrors); } if (null !=
			 * wishlistName && !wishlistName.equals("N"))// add to cart from wishlist { LOG.info(
			 * "added to cart from wishlist"); if (null ==
			 * sessionService.getAttribute(ModelAttributetConstants.WISHLISTDATA)) { LOG.info(
			 * "storing new wishlist data after login in session"); final List<WishlistData> wishlistDatas = new
			 * ArrayList<>(); sessionService.setAttribute(ModelAttributetConstants.WISHLISTDATA, wishlistDatas); }
			 * LOG.info("wishlsit name : " + wishlistName); LOG.info("product code:" + code);
			 *
			 * final WishlistData wishlistData = new WishlistData(); wishlistData.setParticularWishlistName(wishlistName);
			 * wishlistData.setProductCode(code); final Collection<WishlistData> wishlistDatas =
			 * sessionService.getAttribute(ModelAttributetConstants.WISHLISTDATA); final Iterator<WishlistData> iterator =
			 * wishlistDatas.iterator(); final List<WishlistData> list = new ArrayList<>(); while (iterator.hasNext()) {
			 * list.add(iterator.next()); } list.add(wishlistData);
			 * sessionService.setAttribute(ModelAttributetConstants.WISHLISTDATA, list); LOG.info(
			 * "added one data in the session wishlist datas"); }
			 */

			//INC144313608

			/*
			 * final boolean isProductFreebie = getAddToCartHelper().isProductFreebie(code); if (isProductFreebie)
			 * //freebie product or not { return MarketplacecommerceservicesConstants.ERROR_MSG_TYPE_FREEBIE;
			 *
			 * }
			 */
			/* cart not opening issue --due to ussid mismatch */

			final ProductValidationData isProductValid = getAddToCartHelper().isProductValid(code, ussid);

			if (isProductValid != null)
			{
				if (isProductValid.getFreebie().booleanValue())
				{
					return MarketplacecommerceservicesConstants.ERROR_MSG_TYPE_FREEBIE;
				}
				if (!isProductValid.getValidproduct().booleanValue())
				{
					return MarketplacecommerceservicesConstants.ERROR_MSG_TYPE_MISMATCHUSSID;
				}
			}



			final long qty = form.getQty();
			final long stock = form.getStock();
			if (qty <= 0)
			{
				return MarketplacecommerceservicesConstants.ERROR_MSG_TYPE;
				//model.addAttribute(MarketplacecommerceservicesConstants.ERROR_MSG_TYPE, "basket.error.quantity.invalid");
				//model.addAttribute(ModelAttributetConstants.QUANTITY, Long.valueOf(0L));
			}

			//TPR-5346 start
			//Configurable Cart Quantity Restrictions(max quantity added to be restricted at pdp page)
			String maxQuantityAlreadyAdded = "";

			String maxQuantityAdded = "";

			final int maximum_configured_quantiy = siteConfigService.getInt(

					MarketplacecommerceservicesConstants.MAXIMUM_CONFIGURED_QUANTIY, 0);

			final ProductModel product = productService.getProductForCode(code);

			final int maximum_configured_quantiy_jewellery = siteConfigService
					.getInt(MarketplacecommerceservicesConstants.MAXIMUM_CONFIGURED_QUANTIY_JEWELLERY, 0);

			if (product.getMaxOrderQuantity() == null || product.getMaxOrderQuantity().intValue() <= 0
					|| product.getMaxOrderQuantity().intValue() >= maximum_configured_quantiy)
			{
				maxQuantityAlreadyAdded = mplCartFacade.isMaxQuantityAlreadyAdded(code, qty, stock, ussid);

				//TISJEWST-10
				if (StringUtils.isNotEmpty(product.getProductCategoryType())
						&& MarketplacecommerceservicesConstants.FINEJEWELLERY.equalsIgnoreCase(product.getProductCategoryType())
						&& stock > 1)
				{
					if (StringUtils.isNotEmpty(maxQuantityAlreadyAdded))
					{
						maxQuantityAlreadyAdded = maxQuantityAlreadyAdded + "|" + maximum_configured_quantiy_jewellery;
					}
				}
			}
			else
			{
				final long checkMaxLimList = mplCartFacade.checkMaxLimit(code, null);
				maxQuantityAdded = mplCartFacade.isMaxProductQuantityAlreadyAdded(product.getMaxOrderQuantity(), code, qty, stock,
						ussid, checkMaxLimList);
				if (StringUtils.isNotEmpty(maxQuantityAdded))
				{
					maxQuantityAlreadyAdded = maxQuantityAdded + "|" + product.getMaxOrderQuantity();
				}
			}
			//TPR-5346 end

			//final String maxQuantityAlreadyAdded = mplCartFacade.isMaxQuantityAlreadyAdded(code, qty, stock, ussid);
			if (maxQuantityAlreadyAdded.isEmpty())
			{
				LOG.debug("We are allowed to add this product by checking max quantity");
				CartModificationData cartModification = null;
				//Normal Add to Cart Flow
				if (StringUtils.isEmpty(l3) && StringUtils.isEmpty(exchangeParam))
				{
					cartModification = mplCartFacade.addToCart(code, qty, ussid);
				}
				//Exchange Cart Flow
				else
				{
					cartModification = mplCartFacade.addToCartwithExchange(code, qty, ussid,
							l3 + "|" + exchangeParam + "|" + brand + "|" + pincode);

				}
				/*
				 * model.addAttribute(ModelAttributetConstants.QUANTITY, Long.valueOf(cartModification.getQuantityAdded()));
				 * model.addAttribute(ModelAttributetConstants.ENTRY, cartModification.getEntry());
				 * model.addAttribute(ModelAttributetConstants.CARTCODE, cartModification.getCartCode());
				 * model.addAttribute(ModelAttributetConstants.PRODUCT, productFacade.getProductForCodeAndOptions(code,
				 * Arrays.asList(ProductOption.BASIC)));
				 */
				if (cartModification.getQuantityAdded() == 0L)
				{
					LOG.error("in cart modification");
					return MarketplacecommerceservicesConstants.ERROR_MSG_TYPE;
					/*
					 * model.addAttribute(MarketplacecommerceservicesConstants.ERROR_MSG_TYPE,
					 * "basket.information.quantity.noItemsAdded." + cartModification.getStatusCode());
					 */
				}
				return "cnt:" + cartFacade.getSessionCart().getTotalItems();

			}
			else
			{
				LOG.debug("Maximum quantitiy already added AddTocartController with status " + maxQuantityAlreadyAdded);
				return maxQuantityAlreadyAdded;
			}
		}
		catch (final CommerceCartModificationException ex)
		{
			LOG.error("Error in add to cart controller : " + ex);
			return MarketplacecommerceservicesConstants.ERROR_MSG_TYPE;
		}
		catch (final Exception ex)
		{
			LOG.error("Error in add to cart controller : " + ex);
			return MarketplacecommerceservicesConstants.ERROR_MSG_TYPE;
		}
	}

	/*
	 * protected String getViewWithBindingErrorMessages(final Model model, final BindingResult bindingErrors) { for
	 * (final ObjectError error : bindingErrors.getAllErrors()) { if (isTypeMismatchError(error)) {
	 * model.addAttribute(MarketplacecommerceservicesConstants.ERROR_MSG_TYPE,
	 * MarketplacecommerceservicesConstants.QUANTITY_INVALID_BINDING_MESSAGE_KEY); } else {
	 * model.addAttribute(MarketplacecommerceservicesConstants.ERROR_MSG_TYPE, error.getDefaultMessage()); } } return
	 * ControllerConstants.Views.Fragments.Cart.AddToCartPopup; }
	 *
	 * protected boolean isTypeMismatchError(final ObjectError error) { return
	 * error.getCode().equals(MarketplacecommerceservicesConstants.TYPE_MISMATCH_ERROR_CODE); }
	 */
	/**
	 * @return the addToCartHelper
	 */
	public AddToCartHelper getAddToCartHelper()
	{
		return addToCartHelper;
	}


	/**
	 * @param addToCartHelper
	 *           the addToCartHelper to set
	 */
	public void setAddToCartHelper(final AddToCartHelper addToCartHelper)
	{
		this.addToCartHelper = addToCartHelper;
	}

}
