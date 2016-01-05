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
package com.tisl.mpl.storefront.controllers.cms;

import de.hybris.platform.acceleratorcms.model.components.ProductReferencesComponentModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 * Controller for CMS ProductReferencesComponent
 */
@Controller(ModelAttributetConstants.PRODUCT_REFERENCES_COMPONENT_CONTROLLER)
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(value = ControllerConstants.Actions.Cms.ProductReferencesComponent)
public class ProductReferencesComponentController extends AbstractCMSComponentController<ProductReferencesComponentModel>
{
	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);

	@Resource(name = ModelAttributetConstants.ACC_PRODUCT_FACADE)
	private ProductFacade productFacade;
	@Resource(name = ModelAttributetConstants.SESSION_SERVICE)
	private SessionService sessionService;
	@Resource
	private UserService userService;
	@Autowired
	private WishlistFacade wishlistFacade;

	/**
	 * @description this is called to set CrossSelling & UpSelling
	 * @param request
	 *           ,model,component
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final ProductReferencesComponentModel component)
	{
		String myAccountFlag = null;
		if (sessionService.getAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG) != null)
		{
			myAccountFlag = sessionService.getAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG);
		}
		String myWishlistFlag = null;
		if (sessionService.getAttribute(ModelAttributetConstants.MY_WISHLIST_FLAG) != null)
		{
			myWishlistFlag = sessionService.getAttribute(ModelAttributetConstants.MY_WISHLIST_FLAG);
		}
		final ProductModel currentProduct = getRequestContextData(request).getProduct();
		if (currentProduct != null)
		{
			final List<ProductReferenceData> productReferences = productFacade.getProductReferencesForCode(currentProduct.getCode(),
					component.getProductReferenceTypes(), PRODUCT_OPTIONS, component.getMaximumNumberProducts());

			model.addAttribute(ModelAttributetConstants.TITLE, component.getTitle());
			model.addAttribute(ModelAttributetConstants.PRODUCT_REFERENCES, productReferences);
			model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.N_CAPS_VAL);
			sessionService.setAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.N_CAPS_VAL);
		}
		else if (null != myAccountFlag && myAccountFlag.equals(ModelAttributetConstants.Y_CAPS_VAL))
		{
			//	check to identify the my account overview page
			if (null != myWishlistFlag && myWishlistFlag.equals(ModelAttributetConstants.N_CAPS_VAL))
			{
				final CustomerModel customerModel = (CustomerModel) userService.getCurrentUser();

				final List<OrderModel> orderModels = (List<OrderModel>) customerModel.getOrders();
				if (orderModels != null && !orderModels.isEmpty())
				{
					OrderModel orderModel1 = null;
					for (final OrderModel orderModel : orderModels)
					{
						orderModel1 = orderModel;
					}

					final List<AbstractOrderEntryModel> abstractOrderEntryModels = orderModel1.getEntries();
					if (abstractOrderEntryModels != null && !abstractOrderEntryModels.isEmpty())
					{
						final ProductModel productModel = abstractOrderEntryModels.get(0).getProduct();
						if (productModel != null)
						{
							final List<ProductReferenceData> productReferences = productFacade.getProductReferencesForCode(
									productModel.getCode(), component.getProductReferenceTypes(), PRODUCT_OPTIONS,
									component.getMaximumNumberProducts());

							model.addAttribute(ModelAttributetConstants.TITLE, component.getTitle());
							model.addAttribute(ModelAttributetConstants.PRODUCT_REFERENCES, productReferences);
							model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.Y_CAPS_VAL);
						}
					}
				}
			}
			//	check to identify the my account wishlist page
			else if (null != myWishlistFlag && myWishlistFlag.equals(ModelAttributetConstants.Y_CAPS_VAL))
			{
				final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
				final List<Wishlist2EntryModel> allEntries = new ArrayList<Wishlist2EntryModel>();
				for (final Wishlist2Model wl : allWishlists)
				{
					final Wishlist2Model particularWishlist = wishlistFacade.getWishlistForName(wl.getName());
					final List<Wishlist2EntryModel> entryModels = particularWishlist.getEntries();

					if (entryModels.size() >= 0)
					{
						for (final Wishlist2EntryModel entryModel : entryModels)
						{
							allEntries.add(entryModel);
						}
					}
				}

				final List<ProductReferenceData> allProductReferences = new ArrayList<ProductReferenceData>();
				for (final Wishlist2EntryModel entryModelNew : allEntries)
				{
					final ProductModel productModel = entryModelNew.getProduct();
					if (null != productModel)
					{
						final List<ProductReferenceData> productReferences = productFacade.getProductReferencesForCode(
								productModel.getCode(), component.getProductReferenceTypes(), PRODUCT_OPTIONS,
								component.getMaximumNumberProducts());

						for (final ProductReferenceData prefData : productReferences)
						{
							allProductReferences.add(prefData);
						}
					}
				}
				model.addAttribute(ModelAttributetConstants.TITLE, component.getTitle());
				model.addAttribute(ModelAttributetConstants.PRODUCT_REFERENCES, allProductReferences);
				model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.Y_CAPS_VAL);
			}
		}
		else if (null == myAccountFlag && null != myWishlistFlag && myWishlistFlag.equals(ModelAttributetConstants.Y_CAPS_VAL))
		{
			final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
			final List<Wishlist2EntryModel> allEntries = new ArrayList<Wishlist2EntryModel>();
			for (final Wishlist2Model wl : allWishlists)
			{
				final Wishlist2Model particularWishlist = wishlistFacade.getWishlistForName(wl.getName());
				final List<Wishlist2EntryModel> entryModels = particularWishlist.getEntries();

				if (entryModels.size() >= 0)
				{
					for (final Wishlist2EntryModel entryModel : entryModels)
					{
						allEntries.add(entryModel);
					}
				}
			}

			final List<ProductReferenceData> allProductReferences = new ArrayList<ProductReferenceData>();
			for (final Wishlist2EntryModel entryModelNew : allEntries)
			{
				final ProductModel productModel = entryModelNew.getProduct();
				if (null != productModel)
				{
					final List<ProductReferenceData> productReferences = productFacade.getProductReferencesForCode(
							productModel.getCode(), component.getProductReferenceTypes(), PRODUCT_OPTIONS,
							component.getMaximumNumberProducts());

					for (final ProductReferenceData prefData : productReferences)
					{
						allProductReferences.add(prefData);
					}
				}
			}
			model.addAttribute(ModelAttributetConstants.TITLE, component.getTitle());
			model.addAttribute(ModelAttributetConstants.PRODUCT_REFERENCES, allProductReferences);
			model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.Y_CAPS_VAL);
		}
	}
}
