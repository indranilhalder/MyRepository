<<<<<<< HEAD
/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.tisl.mpl.marketplacecommerceservices.strategy;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.externaltax.ExternalTaxesService;
import de.hybris.platform.commerceservices.order.CommercePlaceOrderStrategy;
import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;
import com.tisl.mpl.model.BuyAGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAandBGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAboveXGetPromotionOnShippingChargesModel;


public class MplCommercePlaceOrderStrategyImpl implements CommercePlaceOrderStrategy
{
	private static final Logger LOG = Logger.getLogger(MplCommercePlaceOrderStrategyImpl.class);
	private ModelService modelService;
	private DeliveryService deliveryService;
	private CommonI18NService commonI18NService;
	private PaymentService paymentService;
	private OrderService orderService;
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;
	private PromotionsService promotionsService;
	private CalculationService calculationService;
	private ExternalTaxesService externalTaxesService;
	private List<CommercePlaceOrderMethodHook> commercePlaceOrderMethodHooks;
	private ConfigurationService configurationService;
	@Autowired
	private Converter<OrderModel, OrderData> orderConverter;

	@Autowired
	private NotificationService notificationService;

	public CommerceOrderResult placeOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException
	{
		final CartModel cartModel = parameter.getCart();
		ServicesUtil.validateParameterNotNull(cartModel, "Cart model cannot be null");
		final CommerceOrderResult result = new CommerceOrderResult();
		try
		{
			beforePlaceOrder(parameter);
			if (this.calculationService.requiresCalculation(cartModel))
			{
				LOG.error(String.format("CartModel's [%s] calculated flag was false", new Object[]
				{ cartModel.getCode() }));
			}

			final CustomerModel customer = (CustomerModel) cartModel.getUser();
			ServicesUtil.validateParameterNotNull(customer, "Customer model cannot be null");

			final OrderModel orderModel = getOrderService().createOrderFromCart(cartModel);
			if (orderModel != null)
			{
				orderModel.setDate(new Date());

				orderModel.setSite(getBaseSiteService().getCurrentBaseSite());
				orderModel.setStore(getBaseStoreService().getCurrentBaseStore());
				orderModel.setLanguage(getCommonI18NService().getCurrentLanguage());

				if (parameter.getSalesApplication() != null)
				{
					orderModel.setSalesApplication(parameter.getSalesApplication());
				}

				orderModel.setAllPromotionResults(Collections.<PromotionResultModel> emptySet());

				getModelService().saveAll(new Object[]
				{ customer, orderModel });

				/*
				 * if ((cartModel.getPaymentInfo() != null) && (cartModel.getPaymentInfo().getBillingAddress() != null)) {
				 * final AddressModel billingAddress = cartModel.getPaymentInfo().getBillingAddress();
				 * orderModel.setPaymentAddress(billingAddress);
				 * orderModel.getPaymentInfo().setBillingAddress(getModelService().clone(billingAddress));
				 * getModelService().save(orderModel.getPaymentInfo()); } getModelService().save(orderModel);
				 */

				getPromotionsService().transferPromotionsToOrder(cartModel, orderModel, false);
				final Double subTotal = orderModel.getSubtotal();
				final boolean deliveryCostPromotionApplied = isDeliveryCostPromotionApplied(orderModel);
				Double totalPrice = Double.valueOf(0.0);

				if (deliveryCostPromotionApplied)
				{
					totalPrice = fetchTotalPriceForDelvCostPromo(orderModel);
				}

				try
				{
					getCalculationService().calculateTotals(orderModel, false);
					getExternalTaxesService().calculateExternalTaxes(orderModel);
				}
				catch (final CalculationException ex)
				{
					LOG.error("Failed to calculate order [" + orderModel + "]", ex);
				}

				getModelService().refresh(orderModel);
				getModelService().refresh(customer);

				orderModel.setSubtotal(subTotal);
				if (deliveryCostPromotionApplied)
				{
					orderModel.setTotalPrice(totalPrice);
				}

				getModelService().save(orderModel);

				result.setOrder(orderModel);

				beforeSubmitOrder(parameter, result);

				getOrderService().submitOrder(orderModel);
				getExternalTaxesService().clearSessionTaxDocument();

				afterPlaceOrder(parameter, result);
				//Added to trigger notification

				final String trackOrderUrl = configurationService.getConfiguration().getString(
						MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
						+ orderModel.getCode();
				try
				{
					notificationService.triggerEmailAndSmsOnOrderConfirmation(orderModel, trackOrderUrl);
					notificationService.sendMobileNotifications(orderModel);
				}
				catch (final JAXBException e)
				{
					LOG.error("Error while sending notifications>>>>>>", e);
				}
				catch (final Exception ex)
				{
					LOG.error("Error while sending notifications>>>>>>", ex);
				}



				return result;

			}

			throw new IllegalArgumentException(String.format("Order was not properly created from cart %s", new Object[]
			{ cartModel.getCode() }));

		}
		finally
		{
			getExternalTaxesService().clearSessionTaxDocument();
		}


	}

	private Double fetchTotalPriceForDelvCostPromo(final OrderModel orderModel)
	{
		final OrderData orderData = getOrderConverter().convert(orderModel);
		final Double subtotal = orderModel.getSubtotal();
		final Double deliveryCost = orderModel.getDeliveryCost();
		final Double discount = Double.valueOf(orderData.getTotalDiscounts().getValue().doubleValue());
		final Double totalPrice = Double.valueOf(subtotal.doubleValue() + deliveryCost.doubleValue() - discount.doubleValue());
		return totalPrice;
	}

	private boolean isDeliveryCostPromotionApplied(final AbstractOrderModel orderModel)
	{
		boolean isShippingPromoApplied = false;
		if (CollectionUtils.isNotEmpty(orderModel.getAllPromotionResults()))
		{
			final Set<PromotionResultModel> eligiblePromoList = orderModel.getAllPromotionResults();

			for (final PromotionResultModel promotionResultModel : eligiblePromoList)
			{
				final AbstractPromotionModel promotion = promotionResultModel.getPromotion();

				if (promotionResultModel.getCertainty().floatValue() == 1.0F
						&& (promotion instanceof BuyAGetPromotionOnShippingChargesModel
								|| promotion instanceof BuyAandBGetPromotionOnShippingChargesModel || promotion instanceof BuyAboveXGetPromotionOnShippingChargesModel))
				{
					isShippingPromoApplied = true;
					break;
				}
			}
		}

		return isShippingPromoApplied;
	}

	protected void beforeSubmitOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result)
			throws InvalidCartException
	{
		if ((getCommercePlaceOrderMethodHooks() == null)
				|| (!(parameter.isEnableHooks()))
				|| (!(getConfigurationService().getConfiguration().getBoolean(
						"commerceservices.commerceplaceordermethodhook.enabled", true))))
		{
			return;
		}
		for (final CommercePlaceOrderMethodHook commercePlaceOrderMethodHook : getCommercePlaceOrderMethodHooks())
		{
			commercePlaceOrderMethodHook.beforeSubmitOrder(parameter, result);
		}
	}

	protected void afterPlaceOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result)
			throws InvalidCartException
	{
		if ((getCommercePlaceOrderMethodHooks() == null)
				|| (!(parameter.isEnableHooks()))
				|| (!(getConfigurationService().getConfiguration().getBoolean(
						"commerceservices.commerceplaceordermethodhook.enabled", true))))
		{
			return;
		}
		for (final CommercePlaceOrderMethodHook commercePlaceOrderMethodHook : getCommercePlaceOrderMethodHooks())
		{
			commercePlaceOrderMethodHook.afterPlaceOrder(parameter, result);
		}
	}

	protected void beforePlaceOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException
	{
		if ((getCommercePlaceOrderMethodHooks() == null) || (!(parameter.isEnableHooks())))
		{
			return;
		}
		for (final CommercePlaceOrderMethodHook commercePlaceOrderMethodHook : getCommercePlaceOrderMethodHooks())
		{
			commercePlaceOrderMethodHook.beforePlaceOrder(parameter);
		}
	}

	protected ModelService getModelService()
	{
		return this.modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected DeliveryService getDeliveryService()
	{
		return this.deliveryService;
	}

	@Required
	public void setDeliveryService(final DeliveryService deliveryService)
	{
		this.deliveryService = deliveryService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return this.commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected PaymentService getPaymentService()
	{
		return this.paymentService;
	}

	@Required
	public void setPaymentService(final PaymentService paymentService)
	{
		this.paymentService = paymentService;
	}

	protected OrderService getOrderService()
	{
		return this.orderService;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return this.baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService siteService)
	{
		this.baseSiteService = siteService;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return this.baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService service)
	{
		this.baseStoreService = service;
	}

	protected PromotionsService getPromotionsService()
	{
		return this.promotionsService;
	}

	@Required
	public void setPromotionsService(final PromotionsService promotionsService)
	{
		this.promotionsService = promotionsService;
	}

	protected CalculationService getCalculationService()
	{
		return this.calculationService;
	}

	@Required
	public void setCalculationService(final CalculationService calculationService)
	{
		this.calculationService = calculationService;
	}

	public ExternalTaxesService getExternalTaxesService()
	{
		return this.externalTaxesService;
	}

	@Required
	public void setExternalTaxesService(final ExternalTaxesService externalTaxesService)
	{
		this.externalTaxesService = externalTaxesService;
	}

	protected List<CommercePlaceOrderMethodHook> getCommercePlaceOrderMethodHooks()
	{
		return this.commercePlaceOrderMethodHooks;
	}

	public void setCommercePlaceOrderMethodHooks(final List<CommercePlaceOrderMethodHook> commercePlaceOrderMethodHooks)
	{
		this.commercePlaceOrderMethodHooks = commercePlaceOrderMethodHooks;
	}

	protected ConfigurationService getConfigurationService()
	{
		return this.configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @return the orderConverter
	 */
	public Converter<OrderModel, OrderData> getOrderConverter()
	{
		return orderConverter;
	}

	/**
	 * @param orderConverter
	 *           the orderConverter to set
	 */
	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

}
=======
/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.tisl.mpl.marketplacecommerceservices.strategy;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.externaltax.ExternalTaxesService;
import de.hybris.platform.commerceservices.order.CommercePlaceOrderStrategy;
import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.model.BuyAGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAandBGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAboveXGetPromotionOnShippingChargesModel;


public class MplCommercePlaceOrderStrategyImpl implements CommercePlaceOrderStrategy
{
	private static final Logger LOG = Logger.getLogger(MplCommercePlaceOrderStrategyImpl.class);
	private ModelService modelService;
	private DeliveryService deliveryService;
	private CommonI18NService commonI18NService;
	private PaymentService paymentService;
	private OrderService orderService;
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;
	private PromotionsService promotionsService;
	private CalculationService calculationService;
	private ExternalTaxesService externalTaxesService;
	private List<CommercePlaceOrderMethodHook> commercePlaceOrderMethodHooks;
	private ConfigurationService configurationService;
	@Autowired
	private Converter<OrderModel, OrderData> orderConverter;


	public CommerceOrderResult placeOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException
	{
		final CartModel cartModel = parameter.getCart();
		ServicesUtil.validateParameterNotNull(cartModel, "Cart model cannot be null");
		final CommerceOrderResult result = new CommerceOrderResult();
		try
		{
			beforePlaceOrder(parameter);
			if (this.calculationService.requiresCalculation(cartModel))
			{
				LOG.error(String.format("CartModel's [%s] calculated flag was false", new Object[]
				{ cartModel.getCode() }));
			}

			final CustomerModel customer = (CustomerModel) cartModel.getUser();
			ServicesUtil.validateParameterNotNull(customer, "Customer model cannot be null");

			final OrderModel orderModel = getOrderService().createOrderFromCart(cartModel);
			if (orderModel != null)
			{
				orderModel.setDate(new Date());

				orderModel.setSite(getBaseSiteService().getCurrentBaseSite());
				orderModel.setStore(getBaseStoreService().getCurrentBaseStore());
				orderModel.setLanguage(getCommonI18NService().getCurrentLanguage());

				if (parameter.getSalesApplication() != null)
				{
					orderModel.setSalesApplication(parameter.getSalesApplication());
				}

				orderModel.setAllPromotionResults(Collections.<PromotionResultModel> emptySet());

				getModelService().saveAll(new Object[]
				{ customer, orderModel });

				/*
				 * if ((cartModel.getPaymentInfo() != null) && (cartModel.getPaymentInfo().getBillingAddress() != null)) {
				 * final AddressModel billingAddress = cartModel.getPaymentInfo().getBillingAddress();
				 * orderModel.setPaymentAddress(billingAddress);
				 * orderModel.getPaymentInfo().setBillingAddress(getModelService().clone(billingAddress));
				 * getModelService().save(orderModel.getPaymentInfo()); } getModelService().save(orderModel);
				 */

				getPromotionsService().transferPromotionsToOrder(cartModel, orderModel, false);
				final Double subTotal = orderModel.getSubtotal();
				final boolean deliveryCostPromotionApplied = isDeliveryCostPromotionApplied(orderModel);
				Double totalPrice = Double.valueOf(0.0);

				if (deliveryCostPromotionApplied)
				{
					totalPrice = fetchTotalPriceForDelvCostPromo(orderModel);
				}

				try
				{
					getCalculationService().calculateTotals(orderModel, false);
					getExternalTaxesService().calculateExternalTaxes(orderModel);
				}
				catch (final CalculationException ex)
				{
					LOG.error("Failed to calculate order [" + orderModel + "]", ex);
				}

				getModelService().refresh(orderModel);
				getModelService().refresh(customer);

				orderModel.setSubtotal(subTotal);
				if (deliveryCostPromotionApplied)
				{
					orderModel.setTotalPrice(totalPrice);
				}

				getModelService().save(orderModel);

				result.setOrder(orderModel);

				beforeSubmitOrder(parameter, result);

				getOrderService().submitOrder(orderModel);
				getExternalTaxesService().clearSessionTaxDocument();

				afterPlaceOrder(parameter, result);
				//Added to trigger notification

				return result;

			}

			throw new IllegalArgumentException(String.format("Order was not properly created from cart %s", new Object[]
			{ cartModel.getCode() }));

		}
		finally
		{
			getExternalTaxesService().clearSessionTaxDocument();
		}


	}

	private Double fetchTotalPriceForDelvCostPromo(final OrderModel orderModel)
	{
		final OrderData orderData = getOrderConverter().convert(orderModel);
		final Double subtotal = orderModel.getSubtotal();
		final Double deliveryCost = orderModel.getDeliveryCost();
		final Double discount = Double.valueOf(orderData.getTotalDiscounts().getValue().doubleValue());
		final Double totalPrice = Double.valueOf(subtotal.doubleValue() + deliveryCost.doubleValue() - discount.doubleValue());
		return totalPrice;
	}

	private boolean isDeliveryCostPromotionApplied(final AbstractOrderModel orderModel)
	{
		boolean isShippingPromoApplied = false;
		if (CollectionUtils.isNotEmpty(orderModel.getAllPromotionResults()))
		{
			final Set<PromotionResultModel> eligiblePromoList = orderModel.getAllPromotionResults();

			for (final PromotionResultModel promotionResultModel : eligiblePromoList)
			{
				final AbstractPromotionModel promotion = promotionResultModel.getPromotion();

				if (promotionResultModel.getCertainty().floatValue() == 1.0F
						&& (promotion instanceof BuyAGetPromotionOnShippingChargesModel
								|| promotion instanceof BuyAandBGetPromotionOnShippingChargesModel
								|| promotion instanceof BuyAboveXGetPromotionOnShippingChargesModel))
				{
					isShippingPromoApplied = true;
					break;
				}
			}
		}

		return isShippingPromoApplied;
	}

	protected void beforeSubmitOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result)
			throws InvalidCartException
	{
		if ((getCommercePlaceOrderMethodHooks() == null) || (!(parameter.isEnableHooks())) || (!(getConfigurationService()
				.getConfiguration().getBoolean("commerceservices.commerceplaceordermethodhook.enabled", true))))
		{
			return;
		}
		for (final CommercePlaceOrderMethodHook commercePlaceOrderMethodHook : getCommercePlaceOrderMethodHooks())
		{
			commercePlaceOrderMethodHook.beforeSubmitOrder(parameter, result);
		}
	}

	protected void afterPlaceOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result)
			throws InvalidCartException
	{
		if ((getCommercePlaceOrderMethodHooks() == null) || (!(parameter.isEnableHooks())) || (!(getConfigurationService()
				.getConfiguration().getBoolean("commerceservices.commerceplaceordermethodhook.enabled", true))))
		{
			return;
		}
		for (final CommercePlaceOrderMethodHook commercePlaceOrderMethodHook : getCommercePlaceOrderMethodHooks())
		{
			commercePlaceOrderMethodHook.afterPlaceOrder(parameter, result);
		}
	}

	protected void beforePlaceOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException
	{
		if ((getCommercePlaceOrderMethodHooks() == null) || (!(parameter.isEnableHooks())))
		{
			return;
		}
		for (final CommercePlaceOrderMethodHook commercePlaceOrderMethodHook : getCommercePlaceOrderMethodHooks())
		{
			commercePlaceOrderMethodHook.beforePlaceOrder(parameter);
		}
	}

	protected ModelService getModelService()
	{
		return this.modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected DeliveryService getDeliveryService()
	{
		return this.deliveryService;
	}

	@Required
	public void setDeliveryService(final DeliveryService deliveryService)
	{
		this.deliveryService = deliveryService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return this.commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected PaymentService getPaymentService()
	{
		return this.paymentService;
	}

	@Required
	public void setPaymentService(final PaymentService paymentService)
	{
		this.paymentService = paymentService;
	}

	protected OrderService getOrderService()
	{
		return this.orderService;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return this.baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService siteService)
	{
		this.baseSiteService = siteService;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return this.baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService service)
	{
		this.baseStoreService = service;
	}

	protected PromotionsService getPromotionsService()
	{
		return this.promotionsService;
	}

	@Required
	public void setPromotionsService(final PromotionsService promotionsService)
	{
		this.promotionsService = promotionsService;
	}

	protected CalculationService getCalculationService()
	{
		return this.calculationService;
	}

	@Required
	public void setCalculationService(final CalculationService calculationService)
	{
		this.calculationService = calculationService;
	}

	public ExternalTaxesService getExternalTaxesService()
	{
		return this.externalTaxesService;
	}

	@Required
	public void setExternalTaxesService(final ExternalTaxesService externalTaxesService)
	{
		this.externalTaxesService = externalTaxesService;
	}

	protected List<CommercePlaceOrderMethodHook> getCommercePlaceOrderMethodHooks()
	{
		return this.commercePlaceOrderMethodHooks;
	}

	public void setCommercePlaceOrderMethodHooks(final List<CommercePlaceOrderMethodHook> commercePlaceOrderMethodHooks)
	{
		this.commercePlaceOrderMethodHooks = commercePlaceOrderMethodHooks;
	}

	protected ConfigurationService getConfigurationService()
	{
		return this.configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @return the orderConverter
	 */
	public Converter<OrderModel, OrderData> getOrderConverter()
	{
		return orderConverter;
	}

	/**
	 * @param orderConverter
	 *           the orderConverter to set
	 */
	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

}
>>>>>>> BRANCH_TCS-HYCOMM-R1PS-BN-38
