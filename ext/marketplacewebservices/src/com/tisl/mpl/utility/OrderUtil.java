/**
 *
 */
package com.tisl.mpl.utility;


import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.user.data.AddressData;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.MplPaymentInfoData;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.wsdto.BillingAddressWsDTO;
import com.tisl.mpl.wsdto.OrderConfirmationWsDTO;
import com.tisl.mpl.wsdto.OrderProductWsDTO;



/**
 * @author TCS
 *
 */
public class OrderUtil
{

	@Resource
	private MplSellerInformationService mplSellerInformationService;

	/*
	 * @description Setting DeliveryAddress
	 * 
	 * @param orderDetail
	 * 
	 * @param type (1-Billing, 2-Shipping)
	 * 
	 * @return BillingAddressWsDTO
	 */
	public BillingAddressWsDTO setAddress(final OrderData orderDetail, final int type)
	{
		final BillingAddressWsDTO billingAddress = new BillingAddressWsDTO();
		final String countrycode = "91";
		if (null != orderDetail.getDeliveryAddress() && null != orderDetail.getDeliveryAddress().getId()
				&& StringUtils.isNotEmpty(orderDetail.getDeliveryAddress().getId()) && type == 2)
		{
			billingAddress.setDefaultAddress(Boolean.valueOf(orderDetail.getDeliveryAddress().isDefaultAddress()));
			billingAddress.setAddressType(orderDetail.getDeliveryAddress().getAddressType());
			billingAddress.setFirstName(orderDetail.getDeliveryAddress().getFirstName());
			billingAddress.setLastName(orderDetail.getDeliveryAddress().getLastName());
			if (null != orderDetail.getDeliveryAddress().getCountry())
			{
				billingAddress.setCountry(orderDetail.getDeliveryAddress().getCountry().getName());
			}
			billingAddress.setTown(orderDetail.getDeliveryAddress().getTown());
			billingAddress.setPostalcode(orderDetail.getDeliveryAddress().getPostalCode());
			billingAddress.setState(orderDetail.getDeliveryAddress().getState());
			billingAddress.setAddressLine1(orderDetail.getDeliveryAddress().getLine1());
			billingAddress.setAddressLine2(orderDetail.getDeliveryAddress().getLine2());
			billingAddress.setAddressLine3(orderDetail.getDeliveryAddress().getLine3());
			billingAddress.setPhone(countrycode + orderDetail.getDeliveryAddress().getPhone());

			billingAddress.setShippingFlag(Boolean.valueOf(orderDetail.getDeliveryAddress().isShippingAddress()));
			billingAddress.setId(orderDetail.getDeliveryAddress().getId());
		}

		if (null != orderDetail.getMplPaymentInfo() && null != orderDetail.getMplPaymentInfo().getBillingAddress() && type == 1)
		{
			final AddressData billAddress = orderDetail.getMplPaymentInfo().getBillingAddress();
			billingAddress.setFirstName(billAddress.getFirstName());
			billingAddress.setLastName(billAddress.getLastName());
			if (null != billAddress.getCountry())
			{
				billingAddress.setCountry(billAddress.getCountry().getName());
			}
			billingAddress.setTown(billAddress.getTown());
			billingAddress.setPostalcode(billAddress.getPostalCode());
			billingAddress.setState(billAddress.getState());
			billingAddress.setAddressLine1(billAddress.getLine1());
			billingAddress.setAddressLine2(billAddress.getLine2());
			billingAddress.setAddressLine3(billAddress.getLine3());
			billingAddress.setPhone(countrycode + billAddress.getPhone());
			billingAddress.setShippingFlag(Boolean.valueOf(billAddress.isShippingAddress()));
			billingAddress.setId(billAddress.getId());
		}
		return billingAddress;
	}

	/* Setting Seller Information */
	public void setSellerInfo(final OrderEntryData product, final OrderProductWsDTO reportDTO)
	{
		//Freebie and non-freebie seller detail population
		SellerInformationModel sellerInfoModel = null;
		if (StringUtils.isNotEmpty(product.getSelectedUssid()))
		{
			sellerInfoModel = mplSellerInformationService.getSellerDetail(product.getSelectedUssid());

			if (null != sellerInfoModel && StringUtils.isNotEmpty(sellerInfoModel.getSellerID()))
			{
				reportDTO.setSellerID(sellerInfoModel.getSellerID());
			}
			if (null != sellerInfoModel && StringUtils.isNotEmpty(sellerInfoModel.getUSSID()))
			{
				reportDTO.setUSSID(sellerInfoModel.getUSSID());
			}
			if (null != sellerInfoModel && StringUtils.isNotEmpty(sellerInfoModel.getSellerName()))
			{
				reportDTO.setSellerName(sellerInfoModel.getSellerName());
			}
		}
	}



	/* Checking payment type and then setting payment info */
	public void setPaymentInfo(final OrderData orderDetail, final OrderConfirmationWsDTO orderWsDTO)
	{
		MplPaymentInfoData paymentInfo = null;

		if (null != orderDetail.getMplPaymentInfo())
		{
			paymentInfo = orderDetail.getMplPaymentInfo();

			if (null != paymentInfo.getPaymentOption())
			{
				orderWsDTO.setPaymentMethod(paymentInfo.getPaymentOption());
			}
			if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.CREDIT))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setCardholdername(paymentInfo.getCardAccountHolderName());
				}

				if (StringUtils.isNotEmpty(paymentInfo.getCardIssueNumber()))
				{
					orderWsDTO.setPaymentCardDigit(paymentInfo.getCardIssueNumber());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardCardType()))
				{
					orderWsDTO.setPaymentCard(paymentInfo.getCardCardType());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardExpirationMonth().toString())
						&& StringUtils.isNotEmpty(paymentInfo.getCardExpirationYear().toString()))
				{
					orderWsDTO.setPaymentCardExpire(paymentInfo.getCardExpirationMonth() + "/" + paymentInfo.getCardExpirationYear());
				}
			}

			else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.EMI))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setCardholdername(paymentInfo.getCardAccountHolderName());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardIssueNumber()))
				{
					orderWsDTO.setPaymentCardDigit(paymentInfo.getCardIssueNumber());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardCardType()))
				{
					orderWsDTO.setPaymentCard(paymentInfo.getCardCardType());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardExpirationMonth().toString())
						&& StringUtils.isNotEmpty(paymentInfo.getCardExpirationYear().toString()))
				{
					orderWsDTO.setPaymentCardExpire(paymentInfo.getCardExpirationMonth() + "/" + paymentInfo.getCardExpirationYear());
				}
			}
			else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.NETBANKING))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setCardholdername(paymentInfo.getCardAccountHolderName());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getBank()))
				{
					orderWsDTO.setPaymentCardDigit(paymentInfo.getBank());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardCardType()))
				{
					orderWsDTO.setPaymentCard(paymentInfo.getCardCardType());
				}

				orderWsDTO.setPaymentCardExpire(MarketplacecommerceservicesConstants.NA);
			}
			else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.DEBIT))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setCardholdername(paymentInfo.getCardAccountHolderName());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardIssueNumber()))
				{
					orderWsDTO.setPaymentCardDigit(paymentInfo.getCardIssueNumber());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardCardType()))
				{
					orderWsDTO.setPaymentCard(paymentInfo.getCardCardType());
				}
				if (StringUtils.isNotEmpty(paymentInfo.getCardExpirationMonth().toString())
						&& StringUtils.isNotEmpty(paymentInfo.getCardExpirationYear().toString()))
				{
					orderWsDTO.setPaymentCardExpire(paymentInfo.getCardExpirationMonth() + "/" + paymentInfo.getCardExpirationYear());
				}
			}
			else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.COD))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setCardholdername(paymentInfo.getCardAccountHolderName());
				}

			}
			else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.WALLET))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setCardholdername(paymentInfo.getCardAccountHolderName());
				}
				orderWsDTO.setPaymentCardDigit(MarketplacecommerceservicesConstants.NA);
				orderWsDTO.setPaymentCardExpire(MarketplacecommerceservicesConstants.NA);
			}
		}
		else
		{

			orderWsDTO.setPaymentCard(MarketplacecommerceservicesConstants.NA);
			orderWsDTO.setPaymentCardDigit(MarketplacecommerceservicesConstants.NA);
			orderWsDTO.setPaymentCardExpire(MarketplacecommerceservicesConstants.NA);
			orderWsDTO.setCardholdername(MarketplacecommerceservicesConstants.NA);
		}
	}


}

