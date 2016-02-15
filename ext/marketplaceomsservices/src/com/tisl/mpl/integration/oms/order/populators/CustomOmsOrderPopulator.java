package com.tisl.mpl.integration.oms.order.populators;

import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.externaltax.TaxCodeStrategy;
import de.hybris.platform.commerceservices.model.PickUpDeliveryModeModel;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CODPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.DebitCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.EMIPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.NetbankingPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.integration.commons.services.OndemandTaxCalculationService;
import de.hybris.platform.integration.oms.order.model.OmsZoneDeliveryModeValueModel;
import de.hybris.platform.omsorders.services.delivery.OmsZoneDeliveryModeValueStrategy;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.PaymentInfo;
import com.tisl.mpl.constants.MarketplaceomsordersConstants;
import com.tisl.mpl.constants.MarketplaceomsservicesConstants;
import com.tisl.mpl.globalcodes.utilities.MplCodeMasterUtility;


public class CustomOmsOrderPopulator implements Populator<OrderModel, Order>
{

	private static final Logger LOG = Logger.getLogger(CustomOmsOrderPopulator.class);

	private Converter<OrderEntryModel, OrderLine> orderLineConverter;
	private Converter<PaymentInfoModel, PaymentInfo> paymentInfoConverter;
	private Converter<AddressModel, Address> addressConverter;
	private CustomerNameStrategy customerNameStrategy;
	private CustomerEmailResolutionService customerEmailResolutionService;
	private OndemandTaxCalculationService ondemandTaxCalculationService;
	private OmsZoneDeliveryModeValueStrategy omsZoneDeliveryModeValueStrategy;
	private TaxCodeStrategy taxCodeStrategy;

	//private MplCodeMasterUtility mplCodeMaterUtility;


	@SuppressWarnings("static-access")
	@Override
	public void populate(final OrderModel source, final Order target) throws ConversionException
	{

		final List<OrderLine> ondemandOrderEntrys = new ArrayList<OrderLine>();
		target.setEmailid(((CustomerModel) source.getUser()).getOriginalUid());
		setFirstAndLastName(source, target);
		target.setMiddleName(((CustomerModel) source.getUser()).getMiddleName());
		target.setCartID(source.getGuid());
		if (source.getDeliveryAddress() != null)
		{
			target.setPhoneNumber(source.getDeliveryAddress().getPhone1() != null ? source.getDeliveryAddress().getPhone1() : source
					.getDeliveryAddress().getPhone2());
		}
		else
		{
			LOG.info("CustomOmsOrderPopulator Delivery address is null ");
		}
		target.setCustomerID(((CustomerModel) source.getUser()).getUid());

		if (source.getSalesApplication() != null && source.getSalesApplication().getCode() != null)
		{
			/*
			 * target.setChannel(MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(source.getSalesApplication().getCode().
			 * toUpperCase()) != null ? MplGlobalCodeConstants.GLOBALCONSTANTSMAP
			 * .get(source.getSalesApplication().getCode().toUpperCase()) : MplGlobalCodeConstants.GLOBALCONSTANTSMAP
			 * .get(MarketplaceomsservicesConstants.DEFAULT_CHANNEL_CONSTANTS));
			 */
			target.setChannel(MplCodeMasterUtility.getglobalCode(source.getSalesApplication().getCode().toUpperCase()));
		}
		else
		{
			target.setChannel(MarketplaceomsservicesConstants.DEFAULT_CHANNEL_CONSTANTS);
			LOG.info("CustomOmsOrderPopulator Channel name is null for order " + source.getCode());
		}

		target.setOrderType(MplCodeMasterUtility.getglobalCode(MarketplaceomsservicesConstants.ORDER_TYPE_NEW_CONSTANTS
				.toUpperCase()));
		target.setOrderId(source.getCode());
		/*
		 * target.setOrderType(MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(MarketplaceomsservicesConstants.
		 * ORDER_TYPE_NEW_CONSTANTS .toUpperCase()));
		 */
		target.setSubmissionDateTime(source.getModifiedtime());
		target.setIssueDate(source.getCreationtime());
		for (final OrderModel subOrder : source.getChildOrders())
		{
			for (final AbstractOrderEntryModel entrie : subOrder.getEntries())
			{
				if (entrie.getQuantity() != null && entrie.getQuantity().doubleValue() > 0)
				{
					ondemandOrderEntrys.add(getOrderLineConverter().convert((OrderEntryModel) entrie));
				}
			}
		}
		target.setOrderLines(ondemandOrderEntrys);

		final AddressModel address = source.getDeliveryAddress();

		if (address != null)
		{
			target.setShippingAddress(getAddressConverter().convert(address));
		}
		else
		{
			LOG.info("CustomOmsOrderPopulator : Delivery address is null ");
		}
		final List<PaymentInfo> paymentInfos = new ArrayList<PaymentInfo>();
		for (final PaymentTransactionModel paymentTransSource : source.getPaymentTransactions())
		{
			if (paymentTransSource.getStatus().equalsIgnoreCase("SUCCESS"))
			{
				final PaymentInfo paymentInfo = new PaymentInfo();
				String paymentMode = null;

				/*
				 * paymentInfo.setPaymentStatus(MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(paymentTransSource.getStatus()
				 * .toUpperCase()));
				 */
				paymentInfo.setPaymentStatus(MplCodeMasterUtility.getglobalCode(paymentTransSource.getStatus().toUpperCase()));

				paymentInfo.setPaymentDate(paymentTransSource.getCreationtime());
				paymentInfo.setId(paymentTransSource.getCode());
				for (final PaymentTransactionEntryModel paymentTransactionEntry : paymentTransSource.getEntries())
				{
					paymentMode = paymentTransactionEntry.getPaymentMode().getMode();
					paymentInfo.setPaymentCost(paymentTransactionEntry.getAmount().doubleValue());
					paymentInfo.setPaymentInfo(paymentTransactionEntry.getCode());

					if (source.getPaymentInfo() != null)
					{
						paymentInfo.setPaymentMode(getPaymentMode(source.getPaymentInfo()));
					}
					else
					{
						LOG.info("CustomOmsOrderPopulator Payment info is null ");
						paymentInfo.setPaymentMode("DD");
					}
					//if (paymentMode != null && paymentMode.equalsIgnoreCase(MarketplaceomsordersConstants.COD))
					//{
					//	paymentInfo.setPaymentMode(MarketplaceomsordersConstants.COD);
					//}
					//else
					//{
					//	paymentInfo.setPaymentMode(MarketplaceomsordersConstants.PREPAID);
					//}
				}


				final PaymentInfoModel paymentInfoSource = source.getPaymentInfo();
				Address billingAddress = null;
				if (paymentMode != null && !paymentMode.equalsIgnoreCase(MarketplaceomsordersConstants.COD))
				{
					if (paymentInfoSource != null)
					{
						if (paymentInfoSource.getBillingAddress() != null)
						{
							billingAddress = getAddressConverter().convert(paymentInfoSource.getBillingAddress());

						}
						else
						{
							if (address != null)
							{
								billingAddress = getAddressConverter().convert(address);
							}
						}
						if(billingAddress != null)
						{
						  paymentInfo.setBillingAddress(billingAddress);
						}
						else
						{
							LOG.debug("Billing address is null");
						}
					}
				}
				else
				{
					if (address != null)
					{
						billingAddress = getAddressConverter().convert(address);
					}
					if(billingAddress != null)
					{
					    paymentInfo.setBillingAddress(billingAddress);
					}
					else
					{
						LOG.debug("Billing address is null");
					}
				}
				paymentInfos.add(paymentInfo);

				target.setPaymentInfos(paymentInfos);
			}
			else
			{
				LOG.info("CustomOmsOrderPopulator Payment transaction status is not success " + paymentTransSource.getStatus());
			}
		}

		if (source.getUser() instanceof CustomerModel)
		{
			target.setUsername(((CustomerModel) source.getUser()).getCustomerID());
		}

		if (source.getDeliveryAddress() != null)
		{
			//stubbed as there is not there in user or address table
			target.setFirstName(source.getDeliveryAddress().getFirstname());
			target.setLastName(source.getDeliveryAddress().getLastname());
		}

		//target.setCancellable(true);
	}

	public void setFirstAndLastName(final OrderModel source, final Order target)
	{
		String firstName = null;
		String lastName = null;
		String shippingFirstName = null;
		String shippingLastName = null;

		if (isGuestCustomerOrder(source))
		{
			if (source.getPaymentAddress() != null)
			{
				firstName = source.getPaymentAddress().getFirstname();
				lastName = source.getPaymentAddress().getLastname();
			}
		}
		else
		{
			final String[] names = getCustomerNameStrategy().splitName(source.getUser().getName());
			if (names != null)
			{
				firstName = names[0];
				lastName = names[1];
			}
		}

		if (source.getDeliveryAddress() != null)
		{
			shippingFirstName = source.getDeliveryAddress().getFirstname();
			shippingLastName = source.getDeliveryAddress().getLastname();
		}
		else
		{
			shippingFirstName = firstName;
			shippingLastName = lastName;
		}

		target.setFirstName(firstName);
		target.setLastName(lastName);
		target.setShippingFirstName(shippingFirstName);
		target.setShippingLastName(shippingLastName);
	}

	protected boolean isGuestCustomerOrder(final OrderModel order)
	{
		return ((order.getUser() instanceof CustomerModel) && (CustomerType.GUEST.equals(((CustomerModel) order.getUser())
				.getType())));
	}

	public String getShippingMethod(final OrderModel source)
	{
		if (!(source.getDeliveryMode() instanceof PickUpDeliveryModeModel))
		{
			final OmsZoneDeliveryModeValueModel omsZoneDeliveryModeValue = getOmsZoneDeliveryModeValueStrategy()
					.getZoneDeliveryModeValueForOrder(source, source.getDeliveryMode());
			return ((omsZoneDeliveryModeValue.getSpecificCarrierShippingMethod() == null) ? source.getDeliveryMode().getCode()
					: omsZoneDeliveryModeValue.getSpecificCarrierShippingMethod());
		}
		return source.getDeliveryMode().getCode();
	}

	private String getPaymentMode(final PaymentInfoModel paymentInfoModel)
	{

		if (paymentInfoModel != null)
		{
			if (paymentInfoModel instanceof CODPaymentInfoModel)
			{
				//return MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(MarketplaceomsordersConstants.PAYMENTMETHOD_COD);
				return MplCodeMasterUtility.getglobalCode(MarketplaceomsordersConstants.PAYMENTMETHOD_COD);
			}
			else if (paymentInfoModel instanceof CreditCardPaymentInfoModel)
			{
				/*
				 * return
				 * MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(MarketplaceomsordersConstants.PAYMENTMETHOD_CREDIT_CARD
				 * .toUpperCase());
				 */
				return MplCodeMasterUtility.getglobalCode(MarketplaceomsordersConstants.PAYMENTMETHOD_CREDIT_CARD.toUpperCase());

			}
			else if (paymentInfoModel instanceof DebitCardPaymentInfoModel)
			{
				return MplCodeMasterUtility.getglobalCode(MarketplaceomsordersConstants.PAYMENTMETHOD_DEBIT_CARD.toUpperCase());
				/*
				 * return
				 * MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(MarketplaceomsordersConstants.PAYMENTMETHOD_DEBIT_CARD
				 * .toUpperCase());
				 */
			}
			else if (paymentInfoModel instanceof NetbankingPaymentInfoModel)
			{
				/*
				 * return
				 * MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(MarketplaceomsordersConstants.PAYMENTMETHOD_NETBANKING
				 * .toUpperCase());
				 */
				return MplCodeMasterUtility.getglobalCode(MarketplaceomsordersConstants.PAYMENTMETHOD_NETBANKING.toUpperCase());
			}
			else if (paymentInfoModel instanceof EMIPaymentInfoModel)
			{
				//return MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(MarketplaceomsordersConstants.PAYMENTMETHOD_EMI.toUpperCase());
				return MplCodeMasterUtility.getglobalCode(MarketplaceomsordersConstants.PAYMENTMETHOD_EMI);
			}
			else
			{
				return "CC";
			}
		}
		return "CC";
	}

	protected CustomerEmailResolutionService getCustomerEmailResolutionService()
	{
		return this.customerEmailResolutionService;
	}

	@Required
	public void setCustomerEmailResolutionService(final CustomerEmailResolutionService customerEmailResolutionService)
	{
		this.customerEmailResolutionService = customerEmailResolutionService;
	}

	/**
	 * @return the orderLineConverter
	 */
	public Converter<OrderEntryModel, OrderLine> getOrderLineConverter()
	{
		return orderLineConverter;
	}

	/**
	 * @param orderLineConverter
	 *           the orderLineConverter to set
	 */
	@Required
	public void setOrderLineConverter(final Converter<OrderEntryModel, OrderLine> orderLineConverter)
	{
		this.orderLineConverter = orderLineConverter;
	}

	/**
	 * @return the addressConverter
	 */
	public Converter<AddressModel, Address> getAddressConverter()
	{
		return addressConverter;
	}

	/**
	 * @param addressConverter
	 *           the addressConverter to set
	 */
	@Required
	public void setAddressConverter(final Converter<AddressModel, Address> addressConverter)
	{
		this.addressConverter = addressConverter;
	}

	/**
	 * @return the paymentInfoConverter
	 */
	public Converter<PaymentInfoModel, PaymentInfo> getPaymentInfoConverter()
	{
		return paymentInfoConverter;
	}

	/**
	 * @param paymentInfoConverter
	 *           the paymentInfoConverter to set
	 */
	@Required
	public void setPaymentInfoConverter(final Converter<PaymentInfoModel, PaymentInfo> paymentInfoConverter)
	{
		this.paymentInfoConverter = paymentInfoConverter;
	}

	protected CustomerNameStrategy getCustomerNameStrategy()
	{
		return this.customerNameStrategy;
	}

	@Required
	public void setCustomerNameStrategy(final CustomerNameStrategy customerNameStrategy)
	{
		this.customerNameStrategy = customerNameStrategy;
	}


	protected OndemandTaxCalculationService getOndemandTaxCalculationService()
	{
		return this.ondemandTaxCalculationService;
	}

	@Required
	public void setOndemandTaxCalculationService(final OndemandTaxCalculationService ondemandTaxCalculationService)
	{
		this.ondemandTaxCalculationService = ondemandTaxCalculationService;
	}

	protected OmsZoneDeliveryModeValueStrategy getOmsZoneDeliveryModeValueStrategy()
	{
		return this.omsZoneDeliveryModeValueStrategy;
	}

	@Required
	public void setOmsZoneDeliveryModeValueStrategy(final OmsZoneDeliveryModeValueStrategy omsZoneDeliveryModeValueStrategy)
	{
		this.omsZoneDeliveryModeValueStrategy = omsZoneDeliveryModeValueStrategy;
	}

	public TaxCodeStrategy getTaxCodeStrategy()
	{
		return this.taxCodeStrategy;
	}

	@Required
	public void setTaxCodeStrategy(final TaxCodeStrategy taxCodeStrategy)
	{
		this.taxCodeStrategy = taxCodeStrategy;
	}


	/**
	 * @return the orderLineConverter
	 */




}