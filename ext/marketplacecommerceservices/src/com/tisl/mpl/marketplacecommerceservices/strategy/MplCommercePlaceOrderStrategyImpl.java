/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.tisl.mpl.marketplacecommerceservices.strategy;


import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.externaltax.ExternalTaxesService;
import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.jalo.PromotionsManager.AutoApplyMode;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.util.Helper;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.voucher.model.PromotionVoucherModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.enums.WalletEnum;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplOrderDao;
import com.tisl.mpl.marketplacecommerceservices.service.AgentIdForStore;
import com.tisl.mpl.marketplacecommerceservices.service.ExchangeGuideService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;
import com.tisl.mpl.marketplacecommerceservices.service.PriceBreakupService;
import com.tisl.mpl.model.BuyAGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAandBGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAboveXGetPromotionOnShippingChargesModel;


public class MplCommercePlaceOrderStrategyImpl implements MplCommercePlaceOrderStrategy
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
	/*
	 * @Autowired private MplDeliveryCostService deliveryCostService;
	 */
	@Autowired
	private NotificationService notificationService;

	@Autowired
	private MplOrderDao mplOrderDao;

	//INC144315079
	@Autowired
	private MplCommerceCartService mplCommerceCartService;

	@Autowired
	private ExchangeGuideService exchangeGuideService;

	@Resource(name = "voucherService")
	private VoucherService voucherService;

	@Resource
	private AgentIdForStore agentIdForStore;

	private PriceBreakupService priceBreakupService;


	@Override
	public CommerceOrderResult placeOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException,
			EtailNonBusinessExceptions
	{

		final CartModel cartModel = parameter.getCart();
		ServicesUtil.validateParameterNotNull(cartModel, "Cart model cannot be null");
		final CommerceOrderResult result = new CommerceOrderResult();

		String storeId = StringUtils.EMPTY;
		String agentId = agentIdForStore
				.getAgentIdForStore(MarketplacecommerceservicesConstants.CSCOCKPIT_USER_GROUP_STOREMANAGERAGENTGROUP);
		if (StringUtils.isEmpty(agentId))
		{
			agentId = agentIdForStore
					.getAgentIdForStore(MarketplacecommerceservicesConstants.CSCOCKPIT_USER_GROUP_STOREADMINAGENTGROUP);
		}

		final JaloSession jSession = JaloSession.getCurrentSession();
		if (jSession != null)
		{
			final String loginId = (String) jSession.getAttribute("sellerId");
			if (StringUtils.isNotEmpty(loginId) && loginId.contains("-"))
			{
				storeId = loginId.split("-")[1];
			}
		}

		try
		{
			final String modeOfPayment = cartModel.getModeOfPayment();
			beforePlaceOrder(parameter);
			if (this.calculationService.requiresCalculation(cartModel))
			{
				LOG.debug(String.format("CartModel's [%s] calculated flag was false", new Object[]
				{ cartModel.getCode() }));
			}

			final CustomerModel customer = (CustomerModel) cartModel.getUser();
			ServicesUtil.validateParameterNotNull(customer, "Customer model cannot be null");

			//TISPRD-958

			for (final AbstractOrderEntryModel abstractOrderEntryModel : cartModel.getEntries())
			{
				if (null != abstractOrderEntryModel.getProduct()
						&& MarketplacecommerceservicesConstants.FINEJEWELLERY.equalsIgnoreCase(abstractOrderEntryModel.getProduct()
								.getProductCategoryType()))
				{
					priceBreakupService.createPricebreakupOrder(abstractOrderEntryModel, null);

				}
				if (null != abstractOrderEntryModel.getProduct()
						&& CollectionUtils.isNotEmpty(abstractOrderEntryModel.getProduct().getSupercategories()))
				{
					for (final CategoryModel cat : abstractOrderEntryModel.getProduct().getSupercategories())
					{
						if (StringUtils.isNotEmpty(cat.getCode()) && (cat.getCode().length() >= 5) && (cat.getCode().startsWith("MPH")))
						{
							abstractOrderEntryModel.setProductRootCatCode(cat.getCode().substring(0, 5));
							break;
						}
					}
				}
			}
			//End of changes for TPR-3782

			final OrderModel orderModelExists = isOrderAlreadyExists(cartModel);
			if (orderModelExists != null)
			{
				result.setOrder(orderModelExists);
				LOG.error(String.format("Order guid  [%s] already exists ", new Object[]
				{ cartModel.getGuid() }));
				return result;
			}

			//TISPRD-958

			final OrderModel orderModel = getOrderService().createOrderFromCart(cartModel);

			//TISPRO-540
			final boolean isValidOrder = checkOrder(orderModel);

			if (!isValidOrder)
			{
				LOG.error("****** MplCommercePlaceOrderStrategyImpl : placeOrder :Order is not Valid!!"
						+ (orderModel != null && StringUtils.isNotEmpty(orderModel.getGuid()) ? orderModel.getGuid()
								: MarketplacecommerceservicesConstants.EMPTY));
			}


			if (orderModel != null && isValidOrder)
			{
				try
				{
					//It is moved below //PRDI-70
					//result.setOrder(orderModel);
					// OrderIssues:- 9 digit Order Id getting populated after Order Split and Submit order process for cod, hence moved here
					//				afterPlaceOrder(parameter, result);
					//INC144315079
					orderIdGenerator(orderModel);
					orderModel.setDate(new Date());

					orderModel.setSite(getBaseSiteService().getCurrentBaseSite());
					orderModel.setStore(getBaseStoreService().getCurrentBaseStore());
					orderModel.setLanguage(getCommonI18NService().getCurrentLanguage());

					if (parameter.getSalesApplication() != null)
					{
						orderModel.setSalesApplication(parameter.getSalesApplication());
					}

					//orderModel.setAllPromotionResults(Collections.<PromotionResultModel> emptySet());

					//PRDI-70
					LOG.info("Mode of Payment in placeOrder is -- " + modeOfPayment);
					orderModel.setModeOfOrderPayment(modeOfPayment);
					orderModel.setType(MarketplacecommerceservicesConstants.PARENTORDER);
					if (MarketplacecommerceservicesConstants.MRUPEE.equalsIgnoreCase(modeOfPayment))
					{
						orderModel.setIsWallet(WalletEnum.MRUPEE);
					}

					else
					{
						orderModel.setIsWallet(WalletEnum.NONWALLET);
					}
					getModelService().save(orderModel);

					result.setOrder(orderModel);
					//PRDI-70

					orderModel.setAllPromotionResults(Collections.<PromotionResultModel> emptySet());
					getModelService().saveAll(new Object[]
					{ customer, orderModel });

					/*
					 * if ((cartModel.getPaymentInfo() != null) && (cartModel.getPaymentInfo().getBillingAddress() != null))
					 * { final AddressModel billingAddress = cartModel.getPaymentInfo().getBillingAddress();
					 * orderModel.setPaymentAddress(billingAddress);
					 * orderModel.getPaymentInfo().setBillingAddress(getModelService().clone(billingAddress));
					 * getModelService().save(orderModel.getPaymentInfo()); } getModelService().save(orderModel);
					 */

					//getPromotionsService().transferPromotionsToOrder(cartModel, orderModel, false);

					//Changes for CAR-262 + INC_10922, Update order for Promotion & Coupon
					updateOrderForPromotion(cartModel, orderModel);
					updateOrderForCoupon(cartModel, orderModel);

					final Double subTotal = orderModel.getSubtotal();
					LOG.info("order subTotal is -- " + subTotal);
					final boolean deliveryCostPromotionApplied = isDeliveryCostPromotionApplied(orderModel);
					Double totalPrice = Double.valueOf(0.0);


					if (deliveryCostPromotionApplied)
					{
						totalPrice = fetchTotalPriceForDelvCostPromo(orderModel);
					}
					else
					{
						totalPrice = fetchTotalPrice(orderModel);
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
					final Double totalPriceWithconv = Double.valueOf(totalPrice.doubleValue()
							+ orderModel.getDeliveryCost().doubleValue() + orderModel.getConvenienceCharges().doubleValue());

					getModelService().refresh(orderModel);
					getModelService().refresh(customer);
					getModelService().save(orderModel);
					orderModel.setSubtotal(subTotal);
					//				if (deliveryCostPromotionApplied)
					//				{
					//					orderModel.setTotalPrice(totalPrice);
					//				}

					//orderModel.setTotalPrice(totalPrice);
					//orderModel.setTotalPrice(totalPriceWithconv);
					orderModel.setDeliveryCost(Double.valueOf(getDeliveryCost(orderModel)));
					orderModel.setTotalPriceWithConv(totalPriceWithconv);

					//orderModel.setModeOfOrderPayment(modeOfPayment);

					LOG.info("Mode of Payment in placeOrder is -- " + modeOfPayment);

					if (MarketplacecommerceservicesConstants.MRUPEE.equalsIgnoreCase(modeOfPayment))
					{
						orderModel.setIsWallet(WalletEnum.MRUPEE);
					}
					else
					{
						orderModel.setIsWallet(WalletEnum.NONWALLET);

					}
					//storing agent id in the order model in case of store manager login in cscockpit
					if (StringUtils.isNotEmpty(agentId))
					{

						orderModel.setAgentId(agentId);
					}
					if (StringUtils.isNotEmpty(storeId))
					{
						orderModel.setStoreId(storeId);
					}
					getModelService().save(orderModel);

					/*
					 * result.setOrder(orderModel); // OrderIssues:- 9 digit Order Id getting populated after Order Split and
					 * Submit order process for cod, hence moved here afterPlaceOrder(parameter, result);
					 */
					LOG.info("Mode of Order Payment in placeOrder is -- " + orderModel.getModeOfOrderPayment());
				}
				catch (final Exception e)
				{

					LOG.error("Error after cart to order conversation and before calling afterplaceorder for order ID :"
							+ orderModel.getCode() + "mode of payment :" + modeOfPayment);
					LOG.error("Error while submit order:" + e.getMessage());
				}

				afterPlaceOrder(parameter, result);

				if (StringUtils.isNotEmpty(orderModel.getModeOfOrderPayment())
						&& (orderModel.getModeOfOrderPayment().equalsIgnoreCase("COD") || orderModel.getModeOfOrderPayment()
								.equalsIgnoreCase("JusPay")))

				{
					//Order splitting and order fulfilment process will only be triggered for COD orders from here - TPR-629
					try
					{
						beforeSubmitOrder(parameter, result);
					}
					catch (final CalculationException e)
					{
						LOG.error("Error while submit order", e);
					}
					try
					{
						exchangeGuideService.getExchangeRequestID(orderModel);
					}
					catch (final Exception e)
					{
						LOG.error("Error while generating ExChange Request Id", e);
					}
					getOrderService().submitOrder(orderModel);
				}

				getExternalTaxesService().clearSessionTaxDocument();


				//afterPlaceOrder(parameter, result);  // 9 digit Order Id getting populated after Order Split and Submit order process for cod, hence moved before

				if ((StringUtils.isNotEmpty(orderModel.getModeOfOrderPayment()) && orderModel.getModeOfOrderPayment()
						.equalsIgnoreCase("COD")) || StringUtils.isNotEmpty(agentId))
				{
					//Added to trigger notification for only COD orders TPR-629
					final String trackOrderUrl = configurationService.getConfiguration().getString(
							MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
							+ orderModel.getCode();
					try
					{
						notificationService.triggerEmailAndSmsOnOrderConfirmation(orderModel, trackOrderUrl);
						//notificationService.sendMobileNotifications(orderModel);
					}
					catch (final JAXBException e)
					{
						LOG.error("Error while sending notifications>>>>>>", e);
					}
					catch (final Exception ex)
					{
						LOG.error("Error while sending notifications>>>>>>", ex);
					}
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

	/**
	 * @param orderModel
	 * @return delCost
	 */
	private double getDeliveryCost(final OrderModel orderModel)
	{
		// YTODO Auto-generated method stub
		final Double delCost = Double.valueOf(orderModel.getDeliveryCost().doubleValue());
		//		for (final AbstractOrderEntryModel entry : orderModel.getEntries())
		//		{
		//			final MplZoneDeliveryModeValueModel valueModel = deliveryCostService.getDeliveryCost(entry.getMplDeliveryMode()
		//					.getDeliveryMode().getCode(), orderModel.getCurrency().getIsocode(), entry.getSelectedUSSID());
		//
		//			if (delCost.doubleValue() <= 0)
		//			{
		//				if (entry.getGiveAway() != null && !entry.getGiveAway().booleanValue() && !entry.getIsBOGOapplied().booleanValue())
		//				{
		//					delCost = Double.valueOf((valueModel.getValue().doubleValue() * entry.getQuantity().intValue()));
		//					entry.setCurrDelCharge(delCost);
		//				}
		//				if (entry.getGiveAway() != null && !entry.getGiveAway().booleanValue() && entry.getIsBOGOapplied().booleanValue())
		//				{
		//					delCost = Double.valueOf((valueModel.getValue().doubleValue() * (entry.getQuantity().intValue() - entry
		//							.getQualifyingCount().intValue())));
		//					entry.setCurrDelCharge(delCost);
		//				}
		//				if (entry.getGiveAway() != null && entry.getGiveAway().booleanValue())
		//				{
		//					final Double deliveryCost = Double.valueOf(0.0D);
		//					entry.setCurrDelCharge(deliveryCost);
		//					LOG.warn("skipping deliveryCost for freebee [" + entry.getSelectedUSSID() + "] due toeb freebee ");
		//				}
		//			}
		//
		//		}
		return delCost.doubleValue();
	}

	private boolean checkOrder(final OrderModel order)
	{
		boolean status = true;
		if (order != null && CollectionUtils.isEmpty(order.getEntries()))
		{
			status = false;
		}
		//		else if (order.getPaymentInfo() == null)
		//		{
		//			status = false;
		//		}
		else if (order.getTotalPrice().doubleValue() <= 0.0 || order.getTotalPriceWithConv().doubleValue() <= 0.0)
		{
			status = false;
		}
		else
		{
			status = checkDeliveryOptions(order);
		}

		return status;
	}

	private boolean checkDeliveryOptions(final OrderModel order)
	{
		boolean deliveryOptionCheck = true;

		if (order != null && CollectionUtils.isNotEmpty(order.getEntries()))
		{
			for (final AbstractOrderEntryModel entry : order.getEntries())
			{
				if (null == entry.getMplDeliveryMode())
				{
					deliveryOptionCheck = false;
					break;
				}
			}
		}
		else
		{
			deliveryOptionCheck = false;
			LOG.error("MplCommercePlaceOrderStrategyImpl placeorder Order model null or entries not present for  order  guid "
					+ (StringUtils.isNotEmpty(order.getGuid()) ? order.getGuid() : MarketplacecommerceservicesConstants.EMPTY));
		}

		if (!deliveryOptionCheck)
		{
			LOG.error("MplCommercePlaceOrderStrategyImpl placeorder Delivery mode not present for order  guid "
					+ (StringUtils.isNotEmpty(order.getGuid()) ? order.getGuid() : MarketplacecommerceservicesConstants.EMPTY));
		}
		if (order.getDeliveryAddress() == null)
		{
			for (final AbstractOrderEntryModel entry : order.getEntries())
			{
				if (entry.getDeliveryPointOfService() == null && entry.getDeliveryAddress() == null)
				{
					// Order and Entry have no delivery address and some entries are not for pickup
					deliveryOptionCheck = false;
					break;
				}
			}
		}
		return deliveryOptionCheck;
	}


	/*
	 * @Desc To identify if already a order model exists with same cart guid //TISPRD-181
	 * 
	 * 
	 * @param cartModel
	 * 
	 * 
	 * @return boolean
	 */
	private OrderModel isOrderAlreadyExists(final CartModel cartModel)
	{
		final List<OrderModel> orderModelList = mplOrderDao.getOrderForGuid(cartModel);
		return (CollectionUtils.isNotEmpty(orderModelList)) ? orderModelList.get(0) : null;

	}


	private Double fetchTotalPriceForDelvCostPromo(final OrderModel orderModel)
	{
		Double totalPrice = Double.valueOf(0);
		//final OrderData orderData = getOrderConverter().convert(orderModel);
		final Double subtotal = orderModel.getSubtotal();
		Double deliveryCost = orderModel.getDeliveryCost();
		if (deliveryCost.doubleValue() <= 0.0)
		{
			deliveryCost = Double.valueOf(getDeliveryCost(orderModel));
		}
		//		final Double discount = Double.valueOf(orderData.getTotalDiscounts().getValue().doubleValue());
		//		final Double totalPrice = Double.valueOf(subtotal.doubleValue() + deliveryCost.doubleValue() - discount.doubleValue());

		final Double discount = getTotalDiscount(orderModel.getEntries(), true);

		totalPrice = Double.valueOf(subtotal.doubleValue() + deliveryCost.doubleValue() - discount.doubleValue());
		return totalPrice;
	}

	private Double fetchTotalPrice(final OrderModel orderModel)
	{
		Double totalPrice = Double.valueOf(0);
		final double scheduleDeliveryCharge = 0.0D;
		//final OrderData orderData = getOrderConverter().convert(orderModel);
		final Double subtotal = orderModel.getSubtotal();
		Double deliveryCost = orderModel.getDeliveryCost();
		//		final Double discount = Double.valueOf(orderData.getTotalDiscounts().getValue().doubleValue());
		//		final Double totalPrice = Double.valueOf(subtotal.doubleValue() + deliveryCost.doubleValue() - discount.doubleValue());
		if (deliveryCost.doubleValue() <= 0.0)
		{
			deliveryCost = Double.valueOf(getDeliveryCost(orderModel));
		}
		final Double discount = getTotalDiscount(orderModel.getEntries(), false);

		totalPrice = Double.valueOf(subtotal.doubleValue() + scheduleDeliveryCharge + deliveryCost.doubleValue()
				- discount.doubleValue());
		LOG.info("totalPrice for order entry in fetchTotalPrice is = " + totalPrice);

		return totalPrice;
	}

	//SONAR FIX

	/*
	 * private Double getTotalDiscountForTotalPrice(final List<AbstractOrderEntryModel> entries) { Double discount =
	 * Double.valueOf(0);
	 * 
	 * 
	 * double promoDiscount = 0.0D; double couponDiscount = 0.0D;
	 * 
	 * 
	 * if (CollectionUtils.isNotEmpty(entries)) { for (final AbstractOrderEntryModel oModel : entries) { if (null !=
	 * oModel && !oModel.getGiveAway().booleanValue()) { couponDiscount += (null == oModel.getCouponValue() ? 0.0d :
	 * oModel.getCouponValue().doubleValue()); promoDiscount += (null == oModel.getTotalProductLevelDisc() ? 0.0d :
	 * oModel.getTotalProductLevelDisc() .doubleValue()) + (null == oModel.getCartLevelDisc() ? 0.0d :
	 * oModel.getCartLevelDisc().doubleValue()); } } discount = Double.valueOf(couponDiscount + promoDiscount); } return
	 * discount; }
	 */


	private Double getTotalDiscount(final List<AbstractOrderEntryModel> entries, final boolean deliveryFlag)
	{
		Double discount = Double.valueOf(0);

		double deliveryCost = 0.0D;
		double promoDiscount = 0.0D;
		double couponDiscount = 0.0D;

		if (CollectionUtils.isNotEmpty(entries))
		{
			for (final AbstractOrderEntryModel oModel : entries)
			{
				if (null != oModel && !oModel.getGiveAway().booleanValue())
				{
					if (deliveryFlag)
					{
						deliveryCost += (oModel.getCurrDelCharge().doubleValue() - oModel.getPrevDelCharge().doubleValue()) < 0 ? (-1)
								* (oModel.getCurrDelCharge().doubleValue() - oModel.getPrevDelCharge().doubleValue()) : (oModel
								.getCurrDelCharge().doubleValue() - oModel.getPrevDelCharge().doubleValue());
					}

					couponDiscount += (null == oModel.getCouponValue() ? 0.0d : oModel.getCouponValue().doubleValue());
					promoDiscount += (null == oModel.getTotalProductLevelDisc() ? 0.0d : oModel.getTotalProductLevelDisc()
							.doubleValue()) + (null == oModel.getCartLevelDisc() ? 0.0d : oModel.getCartLevelDisc().doubleValue());
				}
			}
			LOG.info("deliveryCost for order entry in getTotalDiscount is = " + deliveryCost);
			LOG.info("promoDiscount for order entry in getTotalDiscount is = " + promoDiscount);
			LOG.info("couponDiscount for order entry in getTotalDiscount is = " + couponDiscount);

			discount = Double.valueOf(deliveryCost + couponDiscount + promoDiscount);
			LOG.info("discount for order entry in getTotalDiscount is = " + discount);
		}
		return discount;
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

	/**
	 * This method calls before submit order of hooks. This method is changed to public so that it can be accessed from
	 * elsewhere in case of prepaid orders TPR-629
	 *
	 * @param parameter
	 * @param result
	 * @throws CalculationException
	 * @throws InvalidCartException
	 *
	 */
	@Override
	public void beforeSubmitOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result)
			throws InvalidCartException, CalculationException
	{
		//Commented out as it is not required at this stage, hence creating issues with order calculation.
		// New Changes added for Promotion+ Sub total Fix
		//		final OrderModel order = result.getOrder();
		//		final Double subTotal = (null != order && null != order.getSubtotal()) ? order.getSubtotal() : Double.valueOf(0);

		//
		//		getCalculationService().calculateTotals(result.getOrder(), false);

		//
		//		if (subTotal.doubleValue() > 0)

		//		{
		//			order.setSubtotal(subTotal);
		//			getModelService().save(order);

		//	   }

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

	//INC144315079
	//Order ID generator
	private void orderIdGenerator(final OrderModel orderModel)
	{
		//Set order-id
		final String sequenceGeneratorApplicable = getConfigurationService().getConfiguration()
				.getString(MarketplacecclientservicesConstants.GENERATE_ORDER_SEQUENCE).trim();
		//private method for seting Sub-order Total-TISEE-3986
		if (StringUtils.isNotEmpty(sequenceGeneratorApplicable)
				&& sequenceGeneratorApplicable.equalsIgnoreCase(MarketplacecclientservicesConstants.TRUE))
		{
			LOG.debug("Order Sequence Generation True");
			final String orderIdSequence = getMplCommerceCartService().generateOrderId();
			LOG.debug("Order Sequence Generated:- " + orderIdSequence);

			orderModel.setCode(orderIdSequence);
		}
		else
		{
			LOG.debug("Order Sequence Generation False");
			final Random rand = new Random();
			orderModel.setCode(Integer.toString((rand.nextInt(900000000) + 100000000)));
		}
	}


	/**
	 * This method updates order for promotion, Changes for CAR-262 + INC_10922
	 *
	 * @param cartModel
	 * @param orderModel
	 *
	 */
	private void updateOrderForPromotion(final CartModel cartModel, final OrderModel orderModel)
	{
		//orderModel.setAllPromotionResults(Collections.<PromotionResultModel> emptySet());

		boolean applyPromo = false;

		if (CollectionUtils.isNotEmpty(cartModel.getAllPromotionResults()))
		{
			try
			{
				getPromotionsService().transferPromotionsToOrder(cartModel, orderModel, false);
				resetCustomFields(orderModel);
			}
			catch (final Exception exception)
			{
				resetCustomFields(orderModel);
			}
		}
		else
		{
			for (final AbstractOrderEntryModel aoe : cartModel.getEntries())
			{
				if (aoe.getNetAmountAfterAllDisc().doubleValue() > 0D || StringUtils.isNotEmpty(aoe.getProductPromoCode())
						|| StringUtils.isNotEmpty(aoe.getCartPromoCode()))
				{
					applyPromo = true;
					break;
				}
			}

			if (applyPromo)
			{
				LOG.error("Promotion Result not present----Recalculating the Cart");
				//				resetCustomData(cartModel);
				//				final PromotionGroupModel pg = getPromotionsService().getPromotionGroup(
				//						MarketplacecommerceservicesConstants.PROMOGROUP);
				//				getPromotionsService().updatePromotions(Collections.singletonList(pg), cartModel, true, AutoApplyMode.APPLY_ALL,
				//						AutoApplyMode.APPLY_ALL, Helper.getDateNowRoundedToMinute());
				resetCustomFields(cartModel);
				getPromotionsService().transferPromotionsToOrder(cartModel, orderModel, false);
			}
		}
	}

	//	private void updateOrderForCoupon(final CartModel cartModel, final OrderModel orderModel)
	//	{
	//		resetCustomFieldsForCoupon(cartModel, orderModel);
	//		boolean applyCoupon = false;
	//		final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(getVoucherService().getAppliedVouchers(cartModel));
	//
	//		if (CollectionUtils.isNotEmpty(voucherList) && StringUtils.isNotEmpty(cartModel.getGlobalDiscountValuesInternal()))
	//		{
	//			String appliedVoucherCode = null;
	//
	//			if (CollectionUtils.isNotEmpty(voucherList))
	//			{
	//				final DiscountModel discount = voucherList.get(0);
	//				if (discount instanceof PromotionVoucherModel)
	//				{
	//					final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
	//					appliedVoucherCode = promotionVoucherModel.getVoucherCode();
	//				}
	//			}
	//
	//			resetCustomFieldsForCoupon(cartModel, orderModel);
	//		}
	//		else
	//		{
	//			for (final AbstractOrderEntryModel aoe : cartModel.getEntries())
	//			{
	//				if (aoe.getNetAmountAfterAllDisc().doubleValue() > 0D || StringUtils.isNotEmpty(aoe.getProductPromoCode())
	//						|| StringUtils.isNotEmpty(aoe.getCartPromoCode()))
	//				{
	//					applyCoupon = true;
	//					break;
	//				}
	//			}
	//
	//			if (applyCoupon)
	//			{
	//				LOG.error("Promotion Result not present----Recalculating the Cart");
	//				resetCustomFieldsForCoupon(cartModel, orderModel);
	//			}
	//
	//		}

	//}

	/**
	 * Resetting Custom Fields
	 *
	 * @param orderModel
	 */
	private void resetCustomFields(final AbstractOrderModel orderModel)
	{
		if (CollectionUtils.isEmpty(orderModel.getAllPromotionResults()))
		{
			resetCustomData(orderModel);
			LOG.error("Promotion Result not present---- Resetting the Custom Fields");
			final PromotionGroupModel pg = getPromotionsService().getPromotionGroup(MarketplacecommerceservicesConstants.PROMOGROUP);
			getPromotionsService().updatePromotions(Collections.singletonList(pg), orderModel, true, AutoApplyMode.APPLY_ALL,
					AutoApplyMode.APPLY_ALL, Helper.getDateNowRoundedToMinute());
		}
	}

	/**
	 * @param oModel
	 */
	private void resetCustomData(final AbstractOrderModel oModel)
	{
		final List<AbstractOrderEntryModel> cartEntryList = new ArrayList<AbstractOrderEntryModel>();

		for (final AbstractOrderEntryModel cartEntry : oModel.getEntries())
		{
			cartEntry.setQualifyingCount(Integer.valueOf(0));
			cartEntry.setFreeCount(Integer.valueOf(0));
			cartEntry.setAssociatedItems(Collections.<String> emptyList());
			cartEntry.setCartLevelDisc(Double.valueOf(0.00D));
			cartEntry.setTotalSalePrice(Double.valueOf(0.00D));
			cartEntry.setNetSellingPrice(Double.valueOf(0.00D));
			cartEntry.setIsBOGOapplied(Boolean.FALSE);
			cartEntry.setProdLevelPercentageDisc(Double.valueOf(0.00D));
			cartEntry.setCartLevelPercentageDisc(Double.valueOf(0.00D));
			cartEntry.setNetAmountAfterAllDisc(Double.valueOf(0.00D));
			cartEntry.setProductPromoCode(MarketplacecommerceservicesConstants.EMPTY);
			cartEntry.setCartPromoCode(MarketplacecommerceservicesConstants.EMPTY);
			cartEntry.setIsPercentageDisc(Boolean.FALSE);
			cartEntry.setTotalProductLevelDisc(Double.valueOf(0.00D));

			//TPR-7408 starts here
			cartEntry.setPromoProductCostCentreOnePercentage(Double.valueOf(0.00D));
			cartEntry.setPromoProductCostCentreTwoPercentage(Double.valueOf(0.00D));
			cartEntry.setPromoProductCostCentreThreePercentage(Double.valueOf(0.00D));
			cartEntry.setPromoCartCostCentreOnePercentage(Double.valueOf(0.00D));
			cartEntry.setPromoCartCostCentreTwoPercentage(Double.valueOf(0.00D));
			cartEntry.setPromoCartCostCentreThreePercentage(Double.valueOf(0.00D));
			//TPR-7408 ends here

			cartEntryList.add(cartEntry);
		}

		if (CollectionUtils.isNotEmpty(cartEntryList))
		{
			modelService.saveAll(cartEntryList);
		}

		modelService.refresh(oModel);
		modelService.save(oModel);
	}

	/**
	 * Resetting Custom Fields
	 *
	 * @param orderModel
	 */
	private void updateOrderForCoupon(final CartModel cartModel, final OrderModel orderModel)
	{
		String appliedCouponCode = null;
		final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(getVoucherService().getAppliedVouchers(cartModel));

		if (CollectionUtils.isNotEmpty(voucherList) && CollectionUtils.isEmpty(getVoucherService().getAppliedVouchers(orderModel)))
		{
			LOG.error("Coupon present in cartmodel but NOT present ordermodel ---- Applying Coupon and resetting the Custom Fields on ordermodel");

			final DiscountModel discountCart = voucherList.get(0);
			if (discountCart instanceof PromotionVoucherModel)
			{
				final PromotionVoucherModel promotionVoucher = (PromotionVoucherModel) discountCart;
				appliedCouponCode = promotionVoucher.getVoucherCode();
			}

			//Apply voucher on order with coupon code from cart
			getVoucherService().redeemVoucher(appliedCouponCode, orderModel);
			//Set custom attributes of order from cart
			for (final AbstractOrderEntryModel cartEntry : cartModel.getEntries())
			{
				if (cartEntry.getNetAmountAfterAllDisc().doubleValue() > 0D && StringUtils.isNotEmpty(cartEntry.getCouponCode()))
				{
					for (final AbstractOrderEntryModel orderEntry : orderModel.getEntries())
					{
						if (cartEntry.getSelectedUSSID().equalsIgnoreCase(orderEntry.getSelectedUSSID()))
						{
							//appliedCouponCode = cartEntry.getCouponCode();
							orderEntry.setCouponCode(cartEntry.getCouponCode());
							orderEntry.setCouponValue(cartEntry.getCouponValue());

							//TPR-7408 starts here
							if (StringUtils.isNotEmpty(String.valueOf(cartEntry.getCouponCostCentreOnePercentage())))
							{
								orderEntry.setCouponCostCentreOnePercentage(cartEntry.getCouponCostCentreOnePercentage());
							}
							if (StringUtils.isNotEmpty(String.valueOf(cartEntry.getCouponCostCentreTwoPercentage())))
							{
								orderEntry.setCouponCostCentreTwoPercentage(cartEntry.getCouponCostCentreTwoPercentage());
							}
							if (StringUtils.isNotEmpty(String.valueOf(cartEntry.getCouponCostCentreThreePercentage())))
							{
								orderEntry.setCouponCostCentreThreePercentage(cartEntry.getCouponCostCentreThreePercentage());
							}
							//TPR-7408 ends here

						}
					}
				}
			}
			//getVoucherService().redeemVoucher(appliedCouponCode, orderModel);
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

	//INC144315079
	/**
	 * @return the mplCommerceCartService
	 */
	public MplCommerceCartService getMplCommerceCartService()
	{
		return mplCommerceCartService;
	}

	public VoucherService getVoucherService()
	{
		return voucherService;
	}


	public void setVoucherService(final VoucherService voucherService)
	{
		this.voucherService = voucherService;
	}

	/**
	 * @return the priceBreakupService
	 */
	public PriceBreakupService getPriceBreakupService()
	{
		return priceBreakupService;
	}

	/**
	 * @param priceBreakupService
	 *           the priceBreakupService to set
	 */
	public void setPriceBreakupService(final PriceBreakupService priceBreakupService)
	{
		this.priceBreakupService = priceBreakupService;
	}


}
