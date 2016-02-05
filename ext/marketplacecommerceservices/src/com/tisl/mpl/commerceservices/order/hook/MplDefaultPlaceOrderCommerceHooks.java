/**
 *
 */
package com.tisl.mpl.commerceservices.order.hook;

import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CODPaymentInfoModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.order.AbstractOrderEntryTypeService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.order.strategies.ordercloning.CloneAbstractOrderStrategy;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionOrderEntryConsumedModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.sourceforge.pmd.util.StringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.JuspayEBSResponseModel;
import com.tisl.mpl.core.model.MplPaymentAuditEntryModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplOrderDao;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplFraudModelService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.NotifyPaymentGroupMailService;
import com.tisl.mpl.marketplacecommerceservices.service.RMSVerificationNotificationService;
import com.tisl.mpl.model.CustomProductBOGOFPromotionModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.util.OrderStatusSpecifier;




/**
 *
 * This class is used to split the parent order into one or multiple sub-orders and child orders with them
 */
public class MplDefaultPlaceOrderCommerceHooks implements CommercePlaceOrderMethodHook
{


	private static final Logger LOG = Logger.getLogger(MplDefaultPlaceOrderCommerceHooks.class);
	private CloneAbstractOrderStrategy cloneAbstractOrderStrategy;

	@Autowired
	private MplSellerInformationService mplSellerInformationService;

	@Autowired
	private AbstractOrderEntryTypeService abstractOrderEntryTypeService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private EventService eventService;
	@Autowired
	private MplOrderDao mplOrderDao;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private MplCommerceCartService mplCommerceCartService;

	@Autowired
	private MplFraudModelService mplFraudModelService;

	private static final String middleDigits = "000";
	private static final String middlecharacters = "-";

	@Autowired
	private BuyBoxService buyBoxService;

	@Autowired
	private OrderStatusSpecifier orderStatusSpecifier;

	@Autowired
	private NotifyPaymentGroupMailService notifyPaymentGroupMailService;

	@Autowired
	private RMSVerificationNotificationService rMSVerificationNotificationService;

	@Autowired
	private VoucherModelService voucherModelService;

	@Autowired
	private VoucherService voucherService;


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook#afterPlaceOrder(de.hybris.platform
	 * .commerceservices.service.data.CommerceCheckoutParameter,
	 * de.hybris.platform.commerceservices.service.data.CommerceOrderResult)
	 */

	@Override
	public void afterPlaceOrder(final CommerceCheckoutParameter paramCommerceCheckoutParameter,
			final CommerceOrderResult commerceOrderResult) throws InvalidCartException
	{
		try
		{
			final OrderModel orderModel = commerceOrderResult.getOrder();
			if (null != orderModel && StringUtils.isNotEmpty(orderModel.getGuid())
					&& !(orderModel.getPaymentInfo() instanceof CODPaymentInfoModel))
			{
				updateFraudModel(orderModel);

			}

			if (null != orderModel)
			{
				final Collection<DiscountModel> voucherColl = voucherService.getAppliedVouchers(orderModel);
				final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>();
				if (CollectionUtils.isNotEmpty(voucherColl))
				{
					voucherList.addAll(voucherColl);
				}
				if (CollectionUtils.isNotEmpty(voucherList))
				{
					final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) voucherList.get(0);
					final VoucherInvalidationModel voucherInvalidationModel = voucherModelService.createVoucherInvalidation(
							(VoucherModel) voucherList.get(0), promotionVoucherModel.getVoucherCode(), orderModel);
					for (final DiscountValue discount : orderModel.getGlobalDiscountValues())
					{
						if (discount.getCode().equalsIgnoreCase(promotionVoucherModel.getCode()))
						{
							voucherInvalidationModel.setSavedAmount(Double.valueOf(discount.getAppliedValue()));
							break;
						}
					}
					getModelService().save(voucherInvalidationModel);
				}


			}
		}

		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, "E0007");
		}

	}



	/**
	 * This method updates Fraud Model
	 *
	 * @param orderModel
	 */
	private void updateFraudModel(final OrderModel orderModel)
	{
		final ArrayList<JuspayEBSResponseModel> riskList = new ArrayList<JuspayEBSResponseModel>();
		final MplPaymentAuditModel mplAudit = getMplOrderDao().getAuditList(orderModel.getGuid());
		if (null != mplAudit && null != mplAudit.getRisk() && !mplAudit.getRisk().isEmpty())
		{
			riskList.addAll(mplAudit.getRisk());

			if (!riskList.isEmpty() && StringUtils.isNotEmpty(riskList.get(0).getEbsRiskPercentage())
					&& !riskList.get(0).getEbsRiskPercentage().equalsIgnoreCase("-1.0"))
			{
				getMplFraudModelService().updateFraudModel(orderModel, mplAudit);
			}
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook#beforePlaceOrder(de.hybris.platform
	 * .commerceservices.service.data.CommerceCheckoutParameter)
	 */
	@Override
	public void beforePlaceOrder(final CommerceCheckoutParameter commerceCheckoutParameter) throws InvalidCartException
	{
		//Leaving empty as we need not to override anything in this method

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook#beforeSubmitOrder(de.hybris.platform
	 * .commerceservices.service.data.CommerceCheckoutParameter,
	 * de.hybris.platform.commerceservices.service.data.CommerceOrderResult)
	 */
	@Override
	public void beforeSubmitOrder(final CommerceCheckoutParameter paramCommerceCheckoutParameter,
			final CommerceOrderResult paramCommerceOrderResult) throws InvalidCartException
	{
		final OrderModel orderModel = paramCommerceOrderResult.getOrder();
		orderModel.setType("Parent");
		final List<OrderModel> orderList = getSubOrders(orderModel);

		//TISUTO-128
		setFreebieParentTransactionId(orderList);
		setBOGOParentTransactionId(orderList);
		final String sequenceGeneratorApplicable = getConfigurationService().getConfiguration()
				.getString(MarketplacecclientservicesConstants.GENERATE_ORDER_SEQUENCE).trim();
		//private method for seting Sub-order Total-TISEE-3986



		if (StringUtils.isNotEmpty(sequenceGeneratorApplicable)
				&& sequenceGeneratorApplicable.equalsIgnoreCase(MarketplacecclientservicesConstants.TRUE))
		{
			final String orderIdSequence = getMplCommerceCartService().generateOrderId();
			orderModel.setCode(orderIdSequence);
		}
		else
		{
			final Random rand = new Random();
			orderModel.setCode(Integer.toString((rand.nextInt(900000000) + 100000000)));
		}
		setSuborderTotalAfterOrderSplitting(orderList);

		orderModel.setChildOrders(orderList);
		getModelService().save(orderModel);

		final String realEbs = getConfigurationService().getConfiguration().getString("payment.ebs.chek.realtimecall");
		if (realEbs.equalsIgnoreCase("Y"))
		{

			if (orderModel.getPaymentInfo() instanceof CODPaymentInfoModel)
			{
				getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_SUCCESSFUL);
			}
			else
			{
				if (StringUtils.isNotEmpty(orderModel.getGuid()))
				{
					final MplPaymentAuditModel mplAudit = getMplOrderDao().getAuditList(orderModel.getGuid());
					if (null != mplAudit)
					{
						final List<MplPaymentAuditEntryModel> mplAuditEntryList = mplAudit.getAuditEntries();
						if (null != mplAuditEntryList && !mplAuditEntryList.isEmpty())
						{
							updateOrderStatus(mplAuditEntryList, orderModel);
						}
					}
				}
			}
		}
		else
		{
			getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_SUCCESSFUL);
		}
	}



	/**
	 * @description this private method is implemented for the purpose of setting the suborder order corresponding to
	 *              seller specific orders while splitting
	 * @param orderList
	 */
	private void setSuborderTotalAfterOrderSplitting(final List<OrderModel> orderList)
	{

		for (final OrderModel sellerOrderList : orderList)
		{
			double totalPrice = 0D;
			double totalDeliveryPrice = 0D;
			double totalCartLevelDiscount = 0D;
			double totalDeliveryDiscount = 0D;
			double totalPriceForSubTotal = 0D;
			double totalProductDiscount = 0D;
			double totalConvChargeForCOD = 0D;
			double totalCouponDiscount = 0D;
			for (final AbstractOrderEntryModel entryModelList : sellerOrderList.getEntries())
			{
				if (entryModelList.getTotalProductLevelDisc() != null && entryModelList.getTotalProductLevelDisc().doubleValue() > 0D)
				{
					totalProductDiscount += entryModelList.getTotalProductLevelDisc().doubleValue();
				}

				if (entryModelList.getCouponValue() != null && entryModelList.getCouponValue().doubleValue() > 0D)
				{
					totalCouponDiscount += entryModelList.getCouponValue().doubleValue();
				}


				if (entryModelList.getPrevDelCharge() != null && entryModelList.getPrevDelCharge().doubleValue() > 0D)
				{
					totalDeliveryDiscount += entryModelList.getPrevDelCharge().doubleValue()
							- entryModelList.getCurrDelCharge().doubleValue();
				}
				else
				{
					LOG.debug("totalDeliveryDiscount is either zero or empty");
				}
				if (entryModelList.getConvenienceChargeApportion() != null
						&& entryModelList.getConvenienceChargeApportion().doubleValue() > 0D)
				{
					totalConvChargeForCOD += entryModelList.getConvenienceChargeApportion().doubleValue();

				}
				else
				{
					LOG.debug("ConvenienceChargeApportion is either zero or empty");
				}
				//sellerOrderList.setSubtotal(Double.valueOf(totalDeliveryDiscount));
				if (entryModelList.getBasePrice() != null && entryModelList.getBasePrice().doubleValue() > 0D)
				{
					totalPriceForSubTotal += entryModelList.getBasePrice().doubleValue();
				}
				sellerOrderList.setSubtotal(Double.valueOf(totalPriceForSubTotal));


				if (entryModelList.getCartLevelDisc() != null && entryModelList.getCartLevelDisc().doubleValue() > 0D)
				{

					totalCartLevelDiscount += entryModelList.getCartLevelDisc().doubleValue();
				}
				else
				{
					LOG.debug("Cart level discount is either NULL or Zero");
				}
				LOG.info("Total Cart Level Discount>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + totalCartLevelDiscount);
				sellerOrderList.setTotalDiscounts(Double.valueOf(totalCartLevelDiscount + totalCouponDiscount));


				if (entryModelList.getCurrDelCharge() != null && entryModelList.getCurrDelCharge().doubleValue() > 0D)
				{
					totalDeliveryPrice += entryModelList.getCurrDelCharge().doubleValue();
				}
				else
				{
					LOG.debug("Delivery charge for the entry is either NULL or Zero");
				}

				sellerOrderList.setDeliveryCost(Double.valueOf(totalDeliveryPrice));
				totalPrice = totalPriceForSubTotal + totalConvChargeForCOD + totalDeliveryPrice
						- (totalDeliveryDiscount + totalCartLevelDiscount + totalProductDiscount + totalCouponDiscount);
				final DecimalFormat decimalFormat = new DecimalFormat("#.00");
				totalPrice = Double.valueOf(decimalFormat.format(totalPrice)).doubleValue();
				totalConvChargeForCOD = Double.valueOf(decimalFormat.format(totalConvChargeForCOD)).doubleValue();
				sellerOrderList.setTotalPrice(Double.valueOf(totalPrice));
				sellerOrderList.setTotalPriceWithConv(Double.valueOf(totalPrice));
				sellerOrderList.setConvenienceCharges(Double.valueOf(totalConvChargeForCOD));
				modelService.save(sellerOrderList);
			}
		}

	}



	/**
	 * This method checks auditEntryList and updates order status
	 *
	 * @param mplAuditEntryList
	 * @param orderModel
	 */
	private void updateOrderStatus(final List<MplPaymentAuditEntryModel> mplAuditEntryList, final OrderModel orderModel)
	{
		for (final MplPaymentAuditEntryModel mplAuditEntry : mplAuditEntryList)
		{
			if (null != mplAuditEntry.getResponseDate())
			{
				if (mplAuditEntry.getStatus().toString().equalsIgnoreCase(MarketplacecommerceservicesConstants.COMPLETED))
				{
					getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_SUCCESSFUL);
				}
				else if (mplAuditEntry.getStatus().toString().equalsIgnoreCase(MarketplacecommerceservicesConstants.PENDING))
				{
					getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.RMS_VERIFICATION_PENDING);

					try
					{
						//Alert to Payment User Group when order is put on HOLD
						getNotifyPaymentGroupMailService().sendMail(mplAuditEntry.getAuditId());
					}
					catch (final Exception e1)
					{
						LOG.error("Exception during sending Notification for RMS_VERIFICATION_PENDING>>> ", e1);
					}
					try
					{
						//send Notification
						getRMSVerificationNotificationService().sendRMSNotification(orderModel);
					}
					catch (final Exception e1)
					{
						LOG.error("Exception during sending Notification for RMS_VERIFICATION_PENDING>>> ", e1);
					}

				}
			}
		}
	}


	private void setBOGOParentTransactionId(final List<OrderModel> subOrderList) throws EtailNonBusinessExceptions
	{
		final List<String> uniquePromoCodeList = getUniquePromocode(subOrderList);
		if (!uniquePromoCodeList.isEmpty())
		{
			final List<String> ussidTransIDForNonBOGOMap = getUssidTransIDForNonBOGOMap(uniquePromoCodeList, subOrderList);
			String commaSeparetedParentTransId = null;
			int removeCount = 1;
			for (final String promoCode : uniquePromoCodeList)
			{
				for (final OrderModel subOrderModel : subOrderList)
				{
					for (final AbstractOrderEntryModel subOrderEntryModel : subOrderModel.getEntries())
					{
						if (subOrderEntryModel.getProductPromoCode() != null
								&& subOrderEntryModel.getProductPromoCode().equalsIgnoreCase(promoCode)
								&& subOrderEntryModel.getIsBOGOapplied().booleanValue())
						{
							final String parentUssId = getParentUssid(subOrderEntryModel.getAssociatedItems(), subOrderModel);
							final int setQualifyingCount = getSetQualifyingCount(subOrderModel, promoCode);
							final int promoFreeCount = getPromoFreeCount(subOrderModel, promoCode);
							if ((commaSeparetedParentTransId == null || commaSeparetedParentTransId.isEmpty())
									&& !ussidTransIDForNonBOGOMap.isEmpty())
							{
								commaSeparetedParentTransId = getParentTransId(parentUssId, setQualifyingCount, ussidTransIDForNonBOGOMap);
							}
							if (StringUtils.isNotEmpty(commaSeparetedParentTransId))
							{
								subOrderEntryModel.setParentTransactionID(commaSeparetedParentTransId);
								getModelService().save(subOrderEntryModel);
							}
							if (promoFreeCount == removeCount)
							{
								final String[] removeList = commaSeparetedParentTransId.split(",");
								for (int i = 0; i < removeList.length; i++)
								{
									ussidTransIDForNonBOGOMap.remove(removeList[i]);
								}
								removeCount = 0;
								commaSeparetedParentTransId = null;
							}
							removeCount++;
						}
					}
				}
			}
		}
	}

	/**
	 * @param subOrderModel
	 * @param promoCode
	 * @return int
	 */
	private int getSetQualifyingCount(final OrderModel subOrderModel, final String promoCode)
	{
		int setQualifyingCount = 0;
		int qualifyingCount = 0;
		for (final AbstractOrderEntryModel subEntry : subOrderModel.getParentReference().getEntries())
		{
			if (subEntry.getProductPromoCode() != null && subEntry.getProductPromoCode().equals(promoCode))
			{
				qualifyingCount += subEntry.getQualifyingCount().intValue();
			}

		}

		for (final PromotionResultModel allPromotion : subOrderModel.getAllPromotionResults())
		{
			if (allPromotion.getPromotion() != null && allPromotion.getPromotion().getCode().equals(promoCode))
			{
				final AbstractPromotionModel executingPromotion = allPromotion.getPromotion();
				if (executingPromotion instanceof CustomProductBOGOFPromotionModel)
				{
					final CustomProductBOGOFPromotionModel bogoPromotion = (CustomProductBOGOFPromotionModel) executingPromotion;
					final Integer promotionQualifingCount = bogoPromotion.getQualifyingCount();
					final Integer promotionFreeCount = bogoPromotion.getFreeCount();
					setQualifyingCount = qualifyingCount / (promotionQualifingCount.intValue() - promotionFreeCount.intValue());
				}
			}
		}
		setQualifyingCount = qualifyingCount / setQualifyingCount;
		return setQualifyingCount;
	}

	private int getPromoFreeCount(final OrderModel subOrderModel, final String promoCode)
	{
		int promotionFreeCount = 0;
		for (final PromotionResultModel allPromotion : subOrderModel.getAllPromotionResults())
		{
			if (allPromotion.getPromotion() != null && allPromotion.getPromotion().getCode().equals(promoCode))
			{
				final AbstractPromotionModel executingPromotion = allPromotion.getPromotion();
				if (executingPromotion instanceof CustomProductBOGOFPromotionModel)
				{
					final CustomProductBOGOFPromotionModel bogoPromotion = (CustomProductBOGOFPromotionModel) executingPromotion;
					promotionFreeCount = bogoPromotion.getFreeCount().intValue();

				}
			}
		}

		return promotionFreeCount;
	}

	private String getParentTransId(final String parentUssId, final int setQualifyingCount,
			final List<String> ussidTransIDForNonBOGOMap) throws EtailNonBusinessExceptions
	{
		String paranetTransId = null;
		final int parentCount = setQualifyingCount;
		int count = 0;
		for (final String transactionId : ussidTransIDForNonBOGOMap)
		{

			if (count == 0)
			{
				paranetTransId = transactionId;
			}
			else
			{
				paranetTransId = paranetTransId + "," + transactionId;
			}
			count++;
			if (count == parentCount)
			{
				break;
			}
		}

		return paranetTransId;
	}

	private List<String> getUssidTransIDForNonBOGOMap(final List<String> uniquePromoCodeList, final List<OrderModel> subOrderList)
			throws EtailNonBusinessExceptions
	{
		final List<String> ussidTransIDForNonBOGOMap = new ArrayList<String>();

		for (final String promoCode : uniquePromoCodeList)
		{
			for (final OrderModel subOrderModel : subOrderList)
			{
				for (final AbstractOrderEntryModel subOrderEntryModel : subOrderModel.getEntries())
				{
					if (subOrderEntryModel.getProductPromoCode() != null
							&& subOrderEntryModel.getProductPromoCode().equalsIgnoreCase(promoCode)
							&& !subOrderEntryModel.getIsBOGOapplied().booleanValue())
					{
						ussidTransIDForNonBOGOMap.add(subOrderEntryModel.getTransactionID());
					}
				}
			}
		}
		return ussidTransIDForNonBOGOMap;
	}

	private List<String> getUniquePromocode(final List<OrderModel> subOrderList) throws EtailNonBusinessExceptions
	{
		final HashSet masterSet = new HashSet();
		final List<String> innerList = new ArrayList<String>();

		for (final OrderModel subOrderModel : subOrderList)
		{
			for (final AbstractOrderEntryModel subOrderEntryModel : subOrderModel.getEntries())
			{
				if ((subOrderEntryModel.getQualifyingCount().intValue() > 0 || subOrderEntryModel.getFreeCount().intValue() > 0)
						&& subOrderEntryModel.getIsBOGOapplied().booleanValue())
				{
					if (!masterSet.contains(subOrderEntryModel.getProductPromoCode()))
					{
						masterSet.add(subOrderEntryModel.getProductPromoCode());
						innerList.add(subOrderEntryModel.getProductPromoCode());
					}
				}
			}
		}
		return innerList;
	}

	/*
	 * @Desc : this method is used to set freebie items parent transactionid TISUTO-128
	 * 
	 * @param orderList
	 * 
	 * @throws EtailNonBusinessExceptions
	 */
	private void setFreebieParentTransactionId(final List<OrderModel> subOrderList) throws EtailNonBusinessExceptions
	{
		final Map<String, List<String>> freebieParentMap = new HashMap<String, List<String>>();
		final Map<String, List<String>> associatedItemMap = new HashMap<String, List<String>>();

		// Populate transaction id for normal and freebie items parent order line
		for (final OrderModel subOrderModel : subOrderList)
		{
			for (final AbstractOrderEntryModel subOrderEntryModel : subOrderModel.getEntries())
			{
				if (!subOrderEntryModel.getGiveAway().booleanValue() && !subOrderEntryModel.getIsBOGOapplied().booleanValue())
				{
					final String selectedUssid = subOrderEntryModel.getSelectedUSSID();
					final String transactionId = subOrderEntryModel.getTransactionID();
					associatedItemMap.put(selectedUssid, subOrderEntryModel.getAssociatedItems());
					if (StringUtils.isNotEmpty(selectedUssid) && StringUtils.isNotEmpty(transactionId))
					{
						if (freebieParentMap.get(selectedUssid) == null)
						{
							final List<String> tempList = new ArrayList<String>();
							tempList.add(subOrderEntryModel.getTransactionID());
							freebieParentMap.put(selectedUssid, tempList);
						}
						else
						{
							freebieParentMap.get(selectedUssid).add(subOrderEntryModel.getTransactionID());
						}
					}
					else
					{
						LOG.debug((StringUtils.isEmpty(selectedUssid) ? "Ussid  null or empty for entry number"
								+ subOrderEntryModel.getEntryNumber() : ""));
						LOG.debug((StringUtils.isEmpty(transactionId) ? "transactionId  null or empty for entry number"
								+ subOrderEntryModel.getEntryNumber() : ""));
					}
				}
			}
		}

		// Populating parent transaction id for freebie items
		for (final OrderModel subOrderModel : subOrderList)
		{
			for (final AbstractOrderEntryModel subOrderEntryModel : subOrderModel.getEntries())
			{
				if (subOrderEntryModel.getGiveAway().booleanValue()
						&& CollectionUtils.isNotEmpty(subOrderEntryModel.getAssociatedItems()))
				{
					final String parentUssId = getParentUssid(subOrderEntryModel.getAssociatedItems(), subOrderModel);
					String parentTransactionId = null;

					if (subOrderEntryModel.getParentTransactionID() == null && parentUssId != null
							&& freebieParentMap.get(parentUssId) != null)
					{
						parentTransactionId = freebieParentMap.get(parentUssId).get(0);
						if (StringUtils.isNotEmpty(parentTransactionId))
						{
							subOrderEntryModel.setParentTransactionID(parentTransactionId);
							getModelService().save(subOrderEntryModel);


							for (final String freebieUssid : associatedItemMap.get(parentUssId))
							{
								if (!freebieUssid.equalsIgnoreCase(subOrderEntryModel.getSelectedUSSID()))
								{
									for (final AbstractOrderEntryModel subOrderEntryModel2 : subOrderModel.getEntries())
									{
										if (freebieUssid.equalsIgnoreCase(subOrderEntryModel2.getSelectedUSSID())
												&& subOrderEntryModel2.getParentTransactionID() == null
												&& subOrderEntryModel.getGiveAway().booleanValue())
										{
											subOrderEntryModel2.setParentTransactionID(parentTransactionId);
											getModelService().save(subOrderEntryModel2);
											break;
										}

									}
								}

							}

							freebieParentMap.get(parentUssId).remove(0);
						}
					}

				}
			}
		}
	}

	/**
	 * @param associatedItems
	 * @param subOrderEntryModel
	 * @return
	 */
	//TISUTO-163 --- Changes
	@SuppressWarnings("javadoc")
	private String getParentUssid(final List<String> associatedItems, final OrderModel subOrderModel)
	{
		String parentUssid = associatedItems.get(0);
		int count = 0;
		for (final String ussid : associatedItems)
		{
			int inCount = 0;
			for (final AbstractOrderEntryModel entries : subOrderModel.getEntries())
			{
				if (ussid.equalsIgnoreCase(entries.getSelectedUSSID()))
				{
					inCount++;

				}

			}
			if (inCount < count)
			{
				parentUssid = ussid;
			}
			count = inCount;
		}

		return parentUssid;
	}

	/**
	 *
	 * This method is responsible for creating a map for every seller and will store every order entry for that seller
	 *
	 * @param orderModel
	 * @return List<OrderModel>
	 *
	 */
	private List<OrderModel> getSubOrders(final OrderModel orderModel)
	{
		/*
		 * The sellerEntryMap holds the seller id as the key and the corresponding order line entries for that seller id
		 * as value
		 */
		final Map<String, SellerInformationModel> cachedSellerInfoMap = new HashMap<String, SellerInformationModel>();
		final Map<String, List<AbstractOrderEntryModel>> sellerEntryMap = createSellerEntryMap(orderModel, cachedSellerInfoMap);

		final List<OrderModel> subOrders = new ArrayList<OrderModel>();

		for (final Map.Entry<String, List<AbstractOrderEntryModel>> sellerEntry : sellerEntryMap.entrySet())
		{
			subOrders.add(createSubOrders(orderModel, sellerEntry.getValue(), cachedSellerInfoMap));
		}

		return subOrders;
	}

	/**
	 * This method is responsible for creating a map for every seller and will store every order entry for that USSID
	 *
	 * @param orderModel
	 * @return Map<String, List<AbstractOrderEntryModel>>
	 *
	 *
	 *
	 */
	private Map<String, List<AbstractOrderEntryModel>> createSellerEntryMap(final OrderModel orderModel,
			final Map<String, SellerInformationModel> cachedSellerInfoMap)
	{
		final Map<String, List<AbstractOrderEntryModel>> sellerEntryMap = new HashMap<String, List<AbstractOrderEntryModel>>();
		final Collection<AbstractOrderEntryModel> entryModelList = getCloneAbstractOrderStrategy().cloneEntries(
				getAbstractOrderEntryTypeService().getAbstractOrderEntryType(orderModel), orderModel);
		for (final AbstractOrderEntryModel abstractOrderEntryModel : entryModelList)
		{
			final SellerInformationModel sellerEntry = getMplSellerInformationService().getSellerDetail(
					abstractOrderEntryModel.getSelectedUSSID());
			cachedSellerInfoMap.put(abstractOrderEntryModel.getSelectedUSSID(), sellerEntry);

			if (StringUtils.isNotEmpty(sellerEntry.getSellerID()))
			{
				if (sellerEntryMap.get(sellerEntry.getSellerID()) != null)
				{
					sellerEntryMap.get(sellerEntry.getSellerID()).add(abstractOrderEntryModel);
				}
				else
				{
					final List<AbstractOrderEntryModel> subOrderEntryList = new ArrayList<AbstractOrderEntryModel>();
					subOrderEntryList.add(abstractOrderEntryModel);
					sellerEntryMap.put(sellerEntry.getSellerID(), subOrderEntryList);
				}
			}
		}

		return sellerEntryMap;
	}

	/**
	 * This method is responsible for creating Sub-orders and its is assigning order entries for a particular Sub-order.
	 * CloneAbstractOrderStrategy is responsible for cloning the subOrderModel orderService is responsible for adding the
	 * new entry in the cloned Sub orders.
	 *
	 * @param orderModel
	 * @param abstractOrderEntryModelList
	 * @return OrderModel
	 *
	 *
	 */
	private OrderModel createSubOrders(final OrderModel orderModel,
			final List<AbstractOrderEntryModel> abstractOrderEntryModelList,
			final Map<String, SellerInformationModel> cachedSellerInfoMap)
	{
		Double deliveryCharge = Double.valueOf(0.0);
		Double prevDelCharge = Double.valueOf(0.0);
		final OrderModel clonedSubOrder = getCloneAbstractOrderStrategy().clone(null, null, orderModel, generateSubOrderCode(),
				OrderModel.class, OrderEntryModel.class);
		getModelService().save(clonedSubOrder);
		if (clonedSubOrder.getEntries() != null && !clonedSubOrder.getEntries().isEmpty())
		{
			getModelService().removeAll(clonedSubOrder.getEntries());
			clonedSubOrder.setEntries(null);
		}

		clonedSubOrder.setType("SubOrder");
		clonedSubOrder.setParentReference(orderModel);
		getModelService().save(clonedSubOrder);
		final Set setSubPromo = new HashSet<PromotionResultModel>(clonedSubOrder.getAllPromotionResults());
		setSubPromo.clear();
		for (final AbstractOrderEntryModel abstractOrderEntryModel : abstractOrderEntryModelList)
		{
			int quantity = abstractOrderEntryModel.getQuantity().intValue();
			//TISEE-893
			deliveryCharge = abstractOrderEntryModel.getCurrDelCharge();
			LOG.debug(">> Order spliting : before apportoning delivery cost " + deliveryCharge);
			if (abstractOrderEntryModel.getIsBOGOapplied().booleanValue())
			{
				deliveryCharge = deliveryCharge.doubleValue() > 0.0 ? Double.valueOf(deliveryCharge.doubleValue()
						/ abstractOrderEntryModel.getQualifyingCount().doubleValue()) : deliveryCharge;
			}
			else
			{
				deliveryCharge = deliveryCharge.doubleValue() > 0.0 ? Double.valueOf(deliveryCharge.doubleValue()
						/ abstractOrderEntryModel.getQuantity().intValue()) : deliveryCharge;
			}
			LOG.debug(">> Order spliting : after apportoning  delivery cost  " + deliveryCharge);
			//TISEE-5298 -- Prev Delivery Charge
			prevDelCharge = abstractOrderEntryModel.getPrevDelCharge();
			if (abstractOrderEntryModel.getIsBOGOapplied().booleanValue())
			{
				prevDelCharge = prevDelCharge.doubleValue() > 0.0 ? Double.valueOf(prevDelCharge.doubleValue()
						/ abstractOrderEntryModel.getQualifyingCount().doubleValue()) : prevDelCharge;
			}
			else
			{
				prevDelCharge = prevDelCharge.doubleValue() > 0.0 ? Double.valueOf(prevDelCharge.doubleValue()
						/ abstractOrderEntryModel.getQuantity().intValue()) : prevDelCharge;
			}
			LOG.debug(">> Order spliting : after apportoning  delivery cost before Promotion " + prevDelCharge);

			final double price = abstractOrderEntryModel.getTotalSalePrice().doubleValue() / quantity;

			final double couponDiscount = abstractOrderEntryModel.getCouponValue().doubleValue();
			double couponApportionValue = 0;
			if (couponDiscount > 0)
			{
				couponApportionValue = couponDiscount / quantity;
			}


			// Looping through the order Model for single line single quantity at entry level

			if (!StringUtil.isEmpty(abstractOrderEntryModel.getCartPromoCode())
					|| !StringUtil.isEmpty(abstractOrderEntryModel.getProductPromoCode()))
			{
				final double cartvalue = abstractOrderEntryModel.getCartLevelDisc().doubleValue();
				double cartApportionValue = 0;
				if (cartvalue > 0)
				{
					cartApportionValue = cartvalue / quantity;
				}
				double bogoCartApportion = cartApportionValue;
				double bogoCouponApportion = couponApportionValue;
				if (!StringUtil.isEmpty(abstractOrderEntryModel.getProductPromoCode())
						&& !StringUtil.isEmpty(abstractOrderEntryModel.getQualifyingCount().toString()))
				{
					int qualifyingCount = abstractOrderEntryModel.getQualifyingCount().intValue()
							+ abstractOrderEntryModel.getFreeCount().intValue();
					double bogoCODPrice = abstractOrderEntryModel.getConvenienceChargeApportion().doubleValue() * qualifyingCount;
					quantity = quantity - qualifyingCount;
					double productApportionvalue = abstractOrderEntryModel.getTotalProductLevelDisc().doubleValue() / qualifyingCount;

					if (abstractOrderEntryModel.getFreeCount().intValue() > 0)
					{
						productApportionvalue = abstractOrderEntryModel.getTotalProductLevelDisc().doubleValue()
								/ abstractOrderEntryModel.getFreeCount().intValue();
						final int bogoCount = abstractOrderEntryModel.getFreeCount().intValue();
						bogoCartApportion = (cartApportionValue * qualifyingCount)
								/ (qualifyingCount - abstractOrderEntryModel.getFreeCount().intValue());
						bogoCouponApportion = (couponApportionValue * qualifyingCount)
								/ (qualifyingCount - abstractOrderEntryModel.getFreeCount().intValue());
						bogoCODPrice = abstractOrderEntryModel.getConvenienceChargeApportion().doubleValue() * qualifyingCount;
						qualifyingCount = qualifyingCount - bogoCount;
						createOrderLine(abstractOrderEntryModel, bogoCount, clonedSubOrder, cartApportionValue, productApportionvalue,
								price, true, qualifyingCount, deliveryCharge, cachedSellerInfoMap, 0, 0, prevDelCharge,
								couponApportionValue, 0);
						productApportionvalue = 0;
					}
					createOrderLine(abstractOrderEntryModel, qualifyingCount, clonedSubOrder, cartApportionValue,
							productApportionvalue, price, false, 0, deliveryCharge, cachedSellerInfoMap, bogoCODPrice,
							bogoCartApportion, prevDelCharge, couponApportionValue, bogoCouponApportion);


				}
				for (final PromotionResultModel promotion : clonedSubOrder.getAllPromotionResults())
				{
					if (promotion.getCertainty().floatValue() == 1f
							&& ((promotion.getPromotion() instanceof ProductPromotionModel
									&& abstractOrderEntryModel.getProductPromoCode() != null && abstractOrderEntryModel
									.getProductPromoCode().equalsIgnoreCase(promotion.getPromotion().getCode())) || promotion
										.getPromotion() instanceof OrderPromotionModel))
					{
						for (final PromotionOrderEntryConsumedModel promotionOrder : promotion.getConsumedEntries())
						{
							for (final AbstractOrderEntryModel matchline : clonedSubOrder.getEntries())
							{
								//TISEE-6353
								if (matchline != null && matchline.getProductPromoCode() != null
										&& matchline.getProductPromoCode().equalsIgnoreCase(promotion.getPromotion().getCode()))
								{
									promotionOrder.setOrderEntry(matchline);
									promotionOrder.setPromotionResult(promotion);
									getModelService().save(promotionOrder);
								}
							}

						}
						setSubPromo.add(promotion);
					}
				}

				for (final PromotionResultModel promotion : clonedSubOrder.getAllPromotionResults())
				{
					if ((promotion.getPromotion() instanceof OrderPromotionModel))
					{
						setSubPromo.add(promotion);
					}
				}

				createOrderLine(abstractOrderEntryModel, quantity, clonedSubOrder, cartApportionValue, 0, price, false, 0,
						deliveryCharge, cachedSellerInfoMap, 0, 0, prevDelCharge, couponApportionValue, 0);

			}
			else
			{
				createOrderLine(abstractOrderEntryModel, quantity, clonedSubOrder, 0, 0, abstractOrderEntryModel.getTotalPrice()
						.doubleValue() / quantity, false, 0, deliveryCharge, cachedSellerInfoMap, 0, 0, prevDelCharge,
						couponApportionValue, 0);

			}


		}
		clonedSubOrder.setAllPromotionResults(setSubPromo);
		getModelService().save(clonedSubOrder);

		return clonedSubOrder;

	}

	/**
	 * @param abstractOrderEntryModel
	 * @param quantity
	 * @param clonedSubOrder
	 * @param abstractOrderEntryModel
	 */
	@SuppressWarnings("javadoc")
	private void createOrderLine(final AbstractOrderEntryModel abstractOrderEntryModel, final int quantity,
			final OrderModel clonedSubOrder, final double cartApportionValue, final double productApportionvalue,
			final double price, final boolean isbogo, @SuppressWarnings("unused") final double bogoQualifying,
			final Double deliveryCharge, final Map<String, SellerInformationModel> cachedSellerInfoMap, final double bogoCODPrice,
			final double bogoCartApportion, final Double prevDelCharge, final double couponApportionValue,
			final double bogoCouponApportion)

	{

		final BuyBoxModel buyBoxInfo = getBuyBoxService().getpriceForUssid(abstractOrderEntryModel.getSelectedUSSID());
		for (int qty = 0; qty < quantity; qty++)
		{

			final OrderEntryModel orderEntryModel = getOrderService().addNewEntry(clonedSubOrder,
					abstractOrderEntryModel.getProduct(), 1, abstractOrderEntryModel.getUnit(), -1, false);
			orderEntryModel.setBasePrice(abstractOrderEntryModel.getBasePrice());
			final SellerInformationModel sellerDetails = cachedSellerInfoMap.get(abstractOrderEntryModel.getSelectedUSSID());
			final String sellerID = sellerDetails.getSellerID();

			if (null != buyBoxInfo && null != buyBoxInfo.getSellerName())
			{
				orderEntryModel.setSellerInfo(buyBoxInfo.getSellerName());
			}

			final String sequenceGeneratorApplicable = getConfigurationService().getConfiguration()
					.getString(MarketplacecclientservicesConstants.GENERATE_ORDER_SEQUENCE).trim();

			if (StringUtils.isNotEmpty(sequenceGeneratorApplicable)
					&& sequenceGeneratorApplicable.equalsIgnoreCase(MarketplacecclientservicesConstants.TRUE))
			{
				final String orderLineIdSequence = getMplCommerceCartService().generateOrderLineId();
				orderEntryModel.setOrderLineId(sellerID.concat(middleDigits).concat(orderLineIdSequence));
				orderEntryModel.setTransactionID(sellerID.concat(middleDigits).concat(orderLineIdSequence));
			}
			else
			{
				//Transaction ID generation
				final Random rand = new Random();
				final int num = (rand.nextInt(900000) + 100000);
				if (StringUtils.isNotEmpty(sellerID))
				{
					orderEntryModel.setOrderLineId(sellerID.concat(middleDigits).concat(Integer.toString(num)));
					orderEntryModel.setTransactionID(sellerID.concat(middleDigits).concat(Integer.toString(num)));
				}
			}
			if (StringUtils.isNotEmpty(String.valueOf(price)))
			{
				orderEntryModel.setTotalSalePrice(Double.valueOf(price));
			}
			if (StringUtils.isNotEmpty(abstractOrderEntryModel.getSelectedUSSID()))
			{
				orderEntryModel.setSelectedUSSID(abstractOrderEntryModel.getSelectedUSSID());
			}
			if (StringUtils.isNotEmpty(abstractOrderEntryModel.getProductPromoCode()))
			{
				orderEntryModel.setProductPromoCode(abstractOrderEntryModel.getProductPromoCode());
			}
			if (StringUtils.isNotEmpty(abstractOrderEntryModel.getCartPromoCode()))
			{
				orderEntryModel.setCartPromoCode(abstractOrderEntryModel.getCartPromoCode());
			}

			if (abstractOrderEntryModel.getMplDeliveryMode() != null)
			{
				orderEntryModel.setMplDeliveryMode(abstractOrderEntryModel.getMplDeliveryMode());
			}
			if (StringUtils.isNotEmpty(String.valueOf(cartApportionValue)))
			{
				orderEntryModel.setCartLevelDisc(Double.valueOf(cartApportionValue));
			}
			if (StringUtils.isNotEmpty(String.valueOf(productApportionvalue)))
			{

				orderEntryModel.setTotalProductLevelDisc(Double.valueOf(productApportionvalue));
			}
			if (StringUtils.isNotEmpty(abstractOrderEntryModel.getCouponCode()))
			{
				orderEntryModel.setCouponCode(abstractOrderEntryModel.getCouponCode());
			}
			if (StringUtils.isNotEmpty(abstractOrderEntryModel.getSellerForCoupon()))
			{
				orderEntryModel.setSellerForCoupon(abstractOrderEntryModel.getSellerForCoupon());
			}
			final DecimalFormat df = new DecimalFormat("#.##");
			final double netSellingPrice = Double.parseDouble(df.format(price - productApportionvalue));
			final double netAmountAfterAllDisc = Double.parseDouble(df.format(price - cartApportionValue - productApportionvalue
					- couponApportionValue));
			orderEntryModel.setCouponValue(Double.valueOf(couponApportionValue));
			orderEntryModel.setNetSellingPrice(Double.valueOf(netSellingPrice));
			orderEntryModel.setTotalPrice(Double.valueOf(netSellingPrice));
			orderEntryModel.setNetAmountAfterAllDisc(Double.valueOf(netAmountAfterAllDisc));
			orderEntryModel.setQualifyingCount(abstractOrderEntryModel.getQualifyingCount());
			orderEntryModel.setFreeCount(abstractOrderEntryModel.getFreeCount());
			orderEntryModel.setConvenienceChargeApportion(abstractOrderEntryModel.getConvenienceChargeApportion());
			orderEntryModel.setCurrDelCharge(deliveryCharge);
			orderEntryModel.setPrevDelCharge(prevDelCharge);

			if (abstractOrderEntryModel.getGiveAway() != null && abstractOrderEntryModel.getGiveAway().booleanValue())
			{
				orderEntryModel.setGiveAway(Boolean.TRUE);
			}
			if (abstractOrderEntryModel.getAssociatedItems() != null)
			{
				orderEntryModel.setAssociatedItems(abstractOrderEntryModel.getAssociatedItems());
			}
			if (isbogo)
			{
				orderEntryModel.setIsBOGOapplied(Boolean.TRUE);
				orderEntryModel.setConvenienceChargeApportion(Double.valueOf(0));
				orderEntryModel.setCartLevelDisc(Double.valueOf(0));
				orderEntryModel.setCouponValue(Double.valueOf(0));
				orderEntryModel.setNetAmountAfterAllDisc(Double.valueOf(0.01));
				//orderEntryModel.setBasePrice(Double.valueOf(0.01));
				orderEntryModel.setTotalPrice(Double.valueOf(0.01));
				orderEntryModel.setCurrDelCharge(Double.valueOf(0));
			}
			if (!isbogo && CollectionUtils.isNotEmpty(orderEntryModel.getAssociatedItems())
					&& !orderEntryModel.getGiveAway().booleanValue())
			{
				orderEntryModel.setConvenienceChargeApportion(Double.valueOf(bogoCODPrice
						/ orderEntryModel.getQualifyingCount().doubleValue()));
				orderEntryModel.setCartLevelDisc(Double.valueOf(bogoCartApportion));
				orderEntryModel.setCouponValue(Double.valueOf(bogoCouponApportion));
				orderEntryModel.setNetAmountAfterAllDisc(Double.valueOf(Double.parseDouble(df.format(price - bogoCartApportion
						- productApportionvalue - bogoCouponApportion))));
				orderEntryModel.setCurrDelCharge(deliveryCharge);
			}
		}
	}

	private String generateSubOrderCode()
	{
		String subOrderID = "";
		final DateFormat dateFormat = new SimpleDateFormat("YYMMdd");
		final Date date = new Date();
		LOG.debug(dateFormat.format(date));
		final String sequenceGeneratorApplicable = getConfigurationService().getConfiguration()
				.getString(MarketplacecclientservicesConstants.GENERATE_ORDER_SEQUENCE).trim();

		if (StringUtils.isNotEmpty(sequenceGeneratorApplicable)
				&& sequenceGeneratorApplicable.equalsIgnoreCase(MarketplacecclientservicesConstants.TRUE))
		{
			final String subOrderIdSequence = getMplCommerceCartService().generateSubOrderId();
			subOrderID = dateFormat.format(date).toString().concat(middlecharacters).concat(middleDigits).concat(middlecharacters)
					.concat(subOrderIdSequence);
		}
		else
		{
			final Random rand = new Random();
			final int num = (rand.nextInt(900000) + 100000);
			subOrderID = dateFormat.format(date).toString().concat(middlecharacters).concat(middleDigits).concat(middlecharacters)
					.concat(Integer.toString(num));
		}
		return subOrderID;
	}

	@Required
	public void setCloneAbstractOrderStrategy(final CloneAbstractOrderStrategy cloneAbstractOrderStrategy)
	{
		this.cloneAbstractOrderStrategy = cloneAbstractOrderStrategy;
	}

	/**
	 * @return the cloneAbstractOrderStrategy
	 */
	public CloneAbstractOrderStrategy getCloneAbstractOrderStrategy()
	{
		return cloneAbstractOrderStrategy;
	}

	/**
	 * @return the mplSellerInformationService
	 */
	public MplSellerInformationService getMplSellerInformationService()
	{
		return mplSellerInformationService;
	}

	/**
	 * @param mplSellerInformationService
	 *           the mplSellerInformationService to set
	 */
	public void setMplSellerInformationService(final MplSellerInformationService mplSellerInformationService)
	{
		this.mplSellerInformationService = mplSellerInformationService;
	}

	/**
	 * @return the abstractOrderEntryTypeService
	 */
	public AbstractOrderEntryTypeService getAbstractOrderEntryTypeService()
	{
		return abstractOrderEntryTypeService;
	}

	/**
	 * @param abstractOrderEntryTypeService
	 *           the abstractOrderEntryTypeService to set
	 */
	public void setAbstractOrderEntryTypeService(final AbstractOrderEntryTypeService abstractOrderEntryTypeService)
	{
		this.abstractOrderEntryTypeService = abstractOrderEntryTypeService;
	}

	/**
	 * @return the orderService
	 */
	public OrderService getOrderService()
	{
		return orderService;
	}

	/**
	 * @param orderService
	 *           the orderService to set
	 */
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the eventService
	 */
	public EventService getEventService()
	{
		return eventService;
	}

	/**
	 * @param eventService
	 *           the eventService to set
	 */
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

	/**
	 * @return the mplOrderDao
	 */
	public MplOrderDao getMplOrderDao()
	{
		return mplOrderDao;
	}

	/**
	 * @param mplOrderDao
	 *           the mplOrderDao to set
	 */
	public void setMplOrderDao(final MplOrderDao mplOrderDao)
	{
		this.mplOrderDao = mplOrderDao;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @return the mplCommerceCartService
	 */
	public MplCommerceCartService getMplCommerceCartService()
	{
		return mplCommerceCartService;
	}

	/**
	 * @param mplCommerceCartService
	 *           the mplCommerceCartService to set
	 */
	public void setMplCommerceCartService(final MplCommerceCartService mplCommerceCartService)
	{
		this.mplCommerceCartService = mplCommerceCartService;
	}

	/**
	 * @return the mplFraudModelService
	 */
	public MplFraudModelService getMplFraudModelService()
	{
		return mplFraudModelService;
	}

	/**
	 * @param mplFraudModelService
	 *           the mplFraudModelService to set
	 */
	public void setMplFraudModelService(final MplFraudModelService mplFraudModelService)
	{
		this.mplFraudModelService = mplFraudModelService;
	}

	/**
	 * @return the buyBoxService
	 */
	public BuyBoxService getBuyBoxService()
	{
		return buyBoxService;
	}

	/**
	 * @param buyBoxService
	 *           the buyBoxService to set
	 */
	public void setBuyBoxService(final BuyBoxService buyBoxService)
	{
		this.buyBoxService = buyBoxService;
	}

	/**
	 * @return the orderStatusSpecifier
	 */
	public OrderStatusSpecifier getOrderStatusSpecifier()
	{
		return orderStatusSpecifier;
	}

	/**
	 * @param orderStatusSpecifier
	 *           the orderStatusSpecifier to set
	 */
	public void setOrderStatusSpecifier(final OrderStatusSpecifier orderStatusSpecifier)
	{
		this.orderStatusSpecifier = orderStatusSpecifier;
	}

	/**
	 * @return the notifyPaymentGroupMailService
	 */
	public NotifyPaymentGroupMailService getNotifyPaymentGroupMailService()
	{
		return notifyPaymentGroupMailService;
	}

	/**
	 * @param notifyPaymentGroupMailService
	 *           the notifyPaymentGroupMailService to set
	 */
	public void setNotifyPaymentGroupMailService(final NotifyPaymentGroupMailService notifyPaymentGroupMailService)
	{
		this.notifyPaymentGroupMailService = notifyPaymentGroupMailService;
	}

	/**
	 * @return the notifyPaymentGroupMailService
	 */
	public RMSVerificationNotificationService getRMSVerificationNotificationService()
	{
		return rMSVerificationNotificationService;
	}

}
