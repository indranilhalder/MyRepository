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

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.facade.checkout.MplCartFacade;
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

	//@Resource(name = "accProductFacade")
	//private ProductFacade productFacade;

	//@Resource(name = "sessionService")
	//private SessionService sessionService;


	@ResponseBody
	@RequestMapping(value = "/cart/add", method = RequestMethod.POST, produces = "application/json")
	public String addToCart(@RequestParam("productCodePost") final String code, final Model model,
			@Valid final MplAddToCartForm form, @RequestParam("wishlistNamePost") final String wishlistName,
			@RequestParam("ussid") final String ussid)
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
			final long qty = form.getQty();
			final long stock = form.getStock();
			if (qty <= 0)
			{
				return MarketplacecommerceservicesConstants.ERROR_MSG_TYPE;
				//model.addAttribute(MarketplacecommerceservicesConstants.ERROR_MSG_TYPE, "basket.error.quantity.invalid");
				//model.addAttribute(ModelAttributetConstants.QUANTITY, Long.valueOf(0L));
			}
			final String maxQuantityAlreadyAdded = mplCartFacade.isMaxQuantityAlreadyAdded(code, qty, stock, ussid);
			if (maxQuantityAlreadyAdded.isEmpty())
			{
				LOG.debug("We are allowed to add this product by checking max quantity");
				final CartModificationData cartModification = mplCartFacade.addToCart(code, qty, ussid);
				/*
				 * model.addAttribute(ModelAttributetConstants.QUANTITY, Long.valueOf(cartModification.getQuantityAdded()));
				 * model.addAttribute(ModelAttributetConstants.ENTRY, cartModification.getEntry());
				 * model.addAttribute(ModelAttributetConstants.CARTCODE, cartModification.getCartCode());
				 * model.addAttribute(ModelAttributetConstants.PRODUCT, productFacade.getProductForCodeAndOptions(code,
				 * Arrays.asList(ProductOption.BASIC)));
				 */
				if (cartModification.getQuantityAdded() == 0L)
				{
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

}
