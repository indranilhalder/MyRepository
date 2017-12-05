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
package com.tisl.mpl.controllers;

/**
 */
public interface MarketplacecheckoutaddonControllerConstants
{
	// implement here controller constants used by this extension
	String ADDON_PREFIX = "addon:/marketplacecheckoutaddon/";

	String LUXURY_ADDON_PREFIX = "addon:/luxurycheckoutaddon/";


	interface Views
	{

		interface Pages
		{

			interface MultiStepCheckout
			{
				String AddEditDeliveryAddressPage = ADDON_PREFIX + "pages/checkout/multi/addEditDeliveryAddressPage";
				String ChooseDeliveryMethodPage = ADDON_PREFIX + "pages/checkout/multi/chooseDeliveryMethodPage";
				String MplCheckOutLoginPage = ADDON_PREFIX + "pages/checkout/multi/mplCheckOutLoginPage";
				String ChoosePickupLocationPage = ADDON_PREFIX + "pages/checkout/multi/choosePickupLocationPage";
				String AddPaymentMethodPage = ADDON_PREFIX + "pages/checkout/multi/addPaymentMethodPage";
				String CheckoutSummaryPage = ADDON_PREFIX + "pages/checkout/multi/checkoutSummaryPage";
				String HostedOrderPageErrorPage = ADDON_PREFIX + "pages/checkout/multi/hostedOrderPageErrorPage";
				String HostedOrderPostPage = ADDON_PREFIX + "pages/checkout/multi/hostedOrderPostPage";
				String SilentOrderPostPage = ADDON_PREFIX + "pages/checkout/multi/silentOrderPostPage";
				String GiftWrapPage = ADDON_PREFIX + "pages/checkout/multi/giftWrapPage";
				String PAYMENTDETAILSPAGE = ADDON_PREFIX + "pages/checkout/multi/enterPaymentDetailsPage";
				String ChooseAddNewAddressPage = ADDON_PREFIX + "pages/checkout/multi/addNewAddressPage";
				String ChooseDeliveryMethodEditPage = ADDON_PREFIX + "pages/checkout/multi/editAddressDetailsPage";
				String ChooseDeliverySlotPage = ADDON_PREFIX + "pages/checkout/multi/chooseDeliverySlotPage";
			}

			interface SingleStepCheckout
			{
				String SinglePageCheckoutPage = ADDON_PREFIX + "pages/checkout/single/singlePageCheckout";
				String AddPaymentMethodPage = ADDON_PREFIX + "pages/checkout/single/showAddPaymentMethodPage";
			}
		}

		interface Fragments
		{

			interface Checkout
			{
				String TermsAndConditionsPopup = ADDON_PREFIX + "fragments/checkout/termsAndConditionsPopup";
				String BillingAddressForm = ADDON_PREFIX + "fragments/checkout/billingAddressForm";
				String NetbankingPanel = ADDON_PREFIX + "fragments/checkout/netbankingPanel"; //TISPT-235
				String CODPanel = ADDON_PREFIX + "fragments/checkout/codPanel"; //TISPT-235

				interface Single
				{
					String AddAddressForm = ADDON_PREFIX + "fragments/checkout/single/showAddAddressDetails";
					String EditAddressForm = ADDON_PREFIX + "fragments/checkout/single/showEditAddressDetails";
					String DeliveryAddressCarousel = ADDON_PREFIX + "fragments/checkout/single/showDeliveryAddressDetailsPage";
					String DeliveyModesSelectionPanel = ADDON_PREFIX + "fragments/checkout/single/showDeliveryModesDetails"; //TISPT-235
					String PickupLocationSelectionPanel = ADDON_PREFIX + "fragments/checkout/single/showPickupLocation";
					String PickupLocationFragmentPanel = ADDON_PREFIX + "fragments/checkout/single/showPickupLocationFragments";
					String PaymentPageOfferPanel = ADDON_PREFIX + "fragments/checkout/single/showOffersInPaymentPage";
					String PickupPersonForm = ADDON_PREFIX + "fragments/checkout/single/showPickupPersonDetails";
					String ReviewOrder = ADDON_PREFIX + "fragments/checkout/single/showReviewOrder"; //TISPT-235
					String OrderTotals = ADDON_PREFIX + "fragments/checkout/single/showOrderTotalDetailsPage";
				}
			}


		}
	}
}
