/**
 *
 */
package com.tisl.mpl.integration.job;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CODPaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.WalletApportionPaymentInfoModel;
import com.tisl.mpl.core.model.WalletCardApportionDetailModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.jalo.DefaultPromotionManager;
//import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.pojo.BulkSalesOrderXMLData;
import com.tisl.mpl.pojo.ChildOrderXMlData;
import com.tisl.mpl.pojo.MerchantInfoXMlData;
import com.tisl.mpl.pojo.SalesOrderXMLData;
import com.tisl.mpl.pojo.SubOrderXMLData;
import com.tisl.mpl.promotion.service.SellerBasedPromotionService;
import com.tisl.mpl.service.PaymentInfoRevWebServiceImpl;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class SalesOrderXMLUtility
{

	/**
	 * 
	 */
	private static final String ERROR_GETTING_EXCEPTION_WHILE_CHANING_DATE_FORMAT = "Error Getting Exception while  Chaning date Format";
	/**
	 * 
	 */
	private static final String CLIQ_CASH = "Cliq Cash";
	private static final String PAYMENT_JUSPAY_MERCHANT_TYPE = "payment.juspay.merchantType";
	private static final String PAYMENT_QC_MERCHANT_TYPE = "payment.qc.merchantType";
	private static final String PAYMENT_QC_MERCHANT_ID = "payment.qc.merchantID";
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SalesOrderXMLUtility.class.getName());
	@Autowired
	private PaymentInfoRevWebServiceImpl paymentInfoRevWebService;
	private String payemntrefid = null;
	private boolean xmlToFico = true;

	private static String REFUNDED_DELIVERY_MESSAGE = "setting refunded delivery charge...";

	//SONAR FIX
	//private static String EXPRESS_DELIVERY_CHARGE_MESSAGE="set express del charge from curr del charge";


	//@Autowired
	//private MplSellerInformationService mplSellerInformationService;

	/**
	 * @Description: Generate XML Data for Order
	 * @param orderData
	 */
	public void generateOrderData(final List<OrderModel> orderModelList)
	{
		List<SalesOrderXMLData> bulkSalesDataList = null;
		try
		{
			if (null != orderModelList && !orderModelList.isEmpty())
			{
				bulkSalesDataList = getParentOrderData(orderModelList);
				final int rowLimit = getConfigurationService().getConfiguration().getInt(
						MarketplacecommerceservicesConstants.PAYMENTINFO_F_ROWLIMIT,0);
				if (rowLimit > 0)
				{
					int startIndex = 0;
					final int listSize = bulkSalesDataList.size();
					while (startIndex < listSize)
					{
						final int endIndex = (startIndex + rowLimit) < listSize ? (startIndex + rowLimit) : listSize;
						final List<SalesOrderXMLData> partCustomerData = bulkSalesDataList.subList(startIndex, endIndex);
						generatePartOrderData(partCustomerData);
						startIndex += rowLimit;
					}
				}
				else
				{
					generatePartOrderData(bulkSalesDataList);
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
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
		}

	}

	private boolean checkCOD(final OrderModel orderModel)
	{
		boolean isCOD = false;
		//TISPRO-192
		if (null != orderModel.getPaymentInfo() && orderModel.getPaymentInfo() instanceof CODPaymentInfoModel)
		{
			isCOD = true;
			LOG.debug("After check cod");
		}
		return isCOD;
	}

	/**
	 * @Description : Fetch Parent Order Data
	 * @param listOrderData
	 * @return bulkSalesDataList
	 */
	private List<SalesOrderXMLData> getParentOrderData(final List<OrderModel> listOrderData)
	{
		final SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<SalesOrderXMLData> bulkSalesDataList = null;
		if (CollectionUtils.isEmpty(listOrderData))
		{
			xmlToFico = false;
		}
		else
		{
			bulkSalesDataList = new ArrayList<SalesOrderXMLData>();

			for (final OrderModel order : listOrderData)
			{
				//checkCanellation(orderData);
				xmlToFico = true;
				if (null != order.getPaymentInfo())
				{
					if (!checkCOD(order))
					{
						final SalesOrderXMLData salesXMLData = new SalesOrderXMLData();
						LOG.debug("COD Check done");
						if (null != order.getCode() && xmlToFico)
						{
							salesXMLData.setOrderId(order.getCode());
							LOG.info("order id in SalesOrder" + order.getCode());
						}
						else
						{
							xmlToFico = false;
						}

						if (null != order.getDate() && xmlToFico)
						{
							salesXMLData.setOrderDate(sdformat.format(order.getDate()));
						}
						else
						{
							xmlToFico = false;
						}
						salesXMLData.setOrderType(MarketplacecommerceservicesConstants.PREPAID_SPACE);
						if (null != order.getPaymentTransactions() && xmlToFico)
						{
							/*
							 * final List<PaymentTransactionModel> list = order.getPaymentTransactions(); if (null != list &&
							 * !list.isEmpty()) { for (final PaymentTransactionModel oModel : list) {
							 * 
							 * if (null != oModel.getStatus() && null != oModel.getPaymentProvider() &&
							 * oModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS)) {
							 * LOG.debug("Inside Parent order: Pyment Transaction model"); if
							 * (MarketplacecommerceservicesConstants.MRUPEE_CODE.equalsIgnoreCase(oModel.getPaymentProvider()))
							 * { salesXMLData.setMerchantCode(getConfigurationService().getConfiguration().getString(
							 * MarketplacecommerceservicesConstants.MRUPEE_MERCHANT_CODE)); } else {
							 * salesXMLData.setMerchantCode(oModel.getPaymentProvider()); } if (null != oModel.getCode()) {
							 * payemntrefid = oModel.getCode(); LOG.debug("Inside Parent order: Pyment Transaction model" +
							 * payemntrefid); } else { xmlToFico = false; }
							 * 
							 * }
							 * 
							 * } }
							 */
							//the Egv  wallet Change

						}
						else
						{
							xmlToFico = false;
						}

						if (null != order.getChildOrders() && !order.getChildOrders().isEmpty() && xmlToFico)
						{
							LOG.debug(" child order data not null");
							List<SubOrderXMLData> subOrderDataList = new ArrayList<SubOrderXMLData>();
							subOrderDataList = getSubOrderData(order.getChildOrders());
							LOG.debug("after sub order data list call");
							if (null != subOrderDataList && !subOrderDataList.isEmpty() && xmlToFico)
							{
								salesXMLData.setSubOrderList(subOrderDataList);
								LOG.debug("set sub order list");
							}
						}
						/*
						 * if (salesXMLData.getMerchantCode() != null && !(salesXMLData.getMerchantCode().isEmpty()) &&
						 * salesXMLData.getSubOrderList() != null && !(salesXMLData.getSubOrderList().isEmpty()))
						 *///INC144317909 Fix

						if (salesXMLData.getSubOrderList() != null && !(salesXMLData.getSubOrderList().isEmpty()))
						{
							bulkSalesDataList.add(salesXMLData);
						}
					}
				}
			}
		}

		return bulkSalesDataList;
	}

	/**
	 * @Description : Fetch Sub Order Data
	 * @param childOrders
	 * @return subOrderDataList
	 */
	private List<SubOrderXMLData> getSubOrderData(final List<OrderModel> childOrders)
	{
		final List<SubOrderXMLData> subOrderDataList = new ArrayList<SubOrderXMLData>();
		List<ChildOrderXMlData> childOrderDataList = new ArrayList<ChildOrderXMlData>();
		if (null != childOrders && !childOrders.isEmpty())
		{
			for (final OrderModel order : childOrders)
			{
				LOG.debug("inside sub order data method");
				final SubOrderXMLData xmlData = new SubOrderXMLData();
				if (null != order.getCode() && xmlToFico)
				{
					xmlData.setSubOrderId(order.getCode());
					LOG.debug("suborder id" + order.getCode());
				}
				else
				{
					xmlToFico = false;
				}

				if (null != order.getEntries() && !order.getEntries().isEmpty())
				{
					//	for (final AbstractOrderEntryModel entry : order.getEntries())
					{
						//content
						LOG.debug("call child order");
						childOrderDataList = getChildOrderDataForXML(order.getEntries(), order);

					}
				}

				if (null != childOrderDataList && !childOrderDataList.isEmpty() && xmlToFico)
				{
					LOG.debug(" before transaction list set");
					xmlData.setTransactionInfoList(childOrderDataList);
					LOG.debug("transaction list set");
				}

				subOrderDataList.add(xmlData);
			}
		}
		return subOrderDataList;
	}

	/**
	 * @param entries
	 */
	private List<ChildOrderXMlData> getChildOrderDataForXML(final List<AbstractOrderEntryModel> entries, OrderModel chaildModel)
	{
		final List<ChildOrderXMlData> childOrderDataList = new ArrayList<ChildOrderXMlData>();
		List<String> categoryList = new ArrayList<String>();
		LOG.debug("Abstract order entry " + entries);
		if (null != entries && !entries.isEmpty())
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				if (null != entry && null != entry.getProduct())
				{
					final ProductModel product = entry.getProduct();
					LOG.debug("inside AbstractOrderEntryModel");
					final ChildOrderXMlData xmlData = new ChildOrderXMlData();
					if (null != entry.getProduct() && null != product.getCode() && xmlToFico)
					{
						xmlData.setItemNumber(product.getCode());
						LOG.debug("product " + product.getCode());
					}
					else
					{
						xmlToFico = false;
					}
					//deliveryMode
					if (null != entry.getMplDeliveryMode() && xmlToFico)
					{
						LOG.debug("DeliveryMode Setting into childOrderDataforXml");
						if (entry.getMplDeliveryMode().getDeliveryMode().getCode()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
						{
							xmlData.setDeliveryMode(MarketplacecommerceservicesConstants.CnC);
						}
						if (entry.getMplDeliveryMode().getDeliveryMode().getCode()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
						{
							xmlData.setDeliveryMode(MarketplacecommerceservicesConstants.HD);
						}
						if (entry.getMplDeliveryMode().getDeliveryMode().getCode()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY))
						{
							xmlData.setDeliveryMode(MarketplacecommerceservicesConstants.ED);
						}
						LOG.info("DeliveryMode For FICO  >>>>>>>> " + entry.getMplDeliveryMode().getDeliveryMode().getCode());
					} else if(xmlToFico && null != chaildModel.getIsEGVCart() && chaildModel.getIsEGVCart().booleanValue()){
						
						LOG.info("DeliveryMode For FICO For  Egv orderrrrrrrr >>>>>>>> ");
						xmlData.setDeliveryMode(MarketplacecommerceservicesConstants.HD);
					}
					else
					{
						xmlToFico = false;
					}
					if (null != (entry.getOrderLineId()) || null != (entry.getTransactionID()) && xmlToFico)
					{
						xmlData.setTransactionId((entry.getOrderLineId() != null) ? entry.getOrderLineId() : entry.getTransactionID());
						LOG.debug("transaction id"
								+ ((entry.getOrderLineId() != null) ? entry.getOrderLineId() : entry.getTransactionID()));
					}
					else
					{
						xmlToFico = false;
					}
					/*
					 * if (null != payemntrefid && xmlToFico) { xmlData.setPaymentRefID(payemntrefid); } else { xmlToFico =
					 * false; }
					 */

					/*
					 * if (null != entry.getProduct() && null != entry.getProduct().getProductCategoryType()) {
					 * xmlData.setPrimaryCategory(entry.getProduct().getProductCategoryType()); }
					 */

					if (null != entry.getSelectedUSSID() && xmlToFico)
					{
						List<SellerInformationModel> productSellerData = null;
						xmlData.setUSSID(entry.getSelectedUSSID());
						productSellerData = getSellerBasedPromotionService().fetchSellerInformation(entry.getSelectedUSSID(),
								getDefaultPromotionsManager().catalogData());
						LOG.debug("after product seller data call ");
						if (null != productSellerData && !productSellerData.isEmpty())
						{
							for (final SellerInformationModel seller : productSellerData)
							{

								if (null != seller.getSellerID())
								{
									xmlData.setSellerCode(seller.getSellerID());
									LOG.debug("seller id set ");
								}
								else
								{
									xmlToFico = false;
								}
							}
						}
					}
					else
					{
						xmlToFico = false;
					}

					LOG.debug("total price call" + entry.getTotalPrice());
			/*		if (null != entry.getTotalPrice() && xmlToFico)
					{
						if (entry.getNetAmountAfterAllDisc().doubleValue() > 0)
						{
							LOG.debug("*****total price with discount*****" + entry.getNetAmountAfterAllDisc());
							xmlData.setAmount(entry.getNetAmountAfterAllDisc().doubleValue());
						}
						else
						{
							LOG.debug("total price call" + entry.getTotalPrice());
							xmlData.setAmount(entry.getTotalPrice().doubleValue());
						}
						LOG.debug("after price set");
					}*/
					//TISPRD-901 // TISSQAUAT-4104
					if (StringUtils.isNotEmpty(entry.getFulfillmentType()) && xmlToFico)
					{
						if (StringUtils.isNotEmpty(entry.getFulfillmentTypeP1()) && entry.getFulfillmentType().equalsIgnoreCase("Both"))
						{
							xmlData.setFulfillmentType(entry.getFulfillmentTypeP1().toUpperCase());
						}
						else
						{
							xmlData.setFulfillmentType(entry.getFulfillmentType().toUpperCase());
						}

						LOG.debug("set fulfilment mode");
					}

					/*
					 * if (null != entry.getMplDeliveryMode() && xmlToFico) { LOG.debug("inside del mode" +
					 * entry.getMplDeliveryMode()); final MplZoneDeliveryModeValueModel zoneDelivery =
					 * entry.getMplDeliveryMode(); if (null != zoneDelivery && null != zoneDelivery.getDeliveryMode() &&
					 * entry.getCurrDelCharge() != null && entry.getRefundedDeliveryChargeAmt() != null && null !=
					 * zoneDelivery.getDeliveryMode().getCode() &&
					 * zoneDelivery.getDeliveryMode().getCode().equalsIgnoreCase("express-delivery")) {
					 * LOG.debug("inside express del"); // TISPRDT-1186 START if (entry.getCurrDelCharge().doubleValue() > 0)
					 * { LOG.debug("setting current delivery charge..."); //
					 * xmlData.setExpressdeliveryCharge(entry.getCurrDelCharge().doubleValue());
					 * xmlData.setShipmentCharge(entry.getCurrDelCharge().doubleValue()); } else {
					 * LOG.debug(REFUNDED_DELIVERY_MESSAGE + entry.getRefundedDeliveryChargeAmt());
					 * xmlData.setShipmentCharge(entry.getRefundedDeliveryChargeAmt().doubleValue()); }
					 * xmlData.setExpressdeliveryCharge(0.00); // TISPRDT-1186 END
					 * LOG.debug("set express del charge from curr del charge" + entry.getCurrDelCharge().doubleValue());//
					 * zoneDelivery.getValue().doubleValue()
					 * 
					 * if (null != entry.getScheduledDeliveryCharge() && entry.getScheduledDeliveryCharge().doubleValue() >
					 * 0) { LOG.debug("setting schedule delivery charge..." + entry.getScheduledDeliveryCharge());
					 * xmlData.setScheduleDelCharge(entry.getScheduledDeliveryCharge().doubleValue()); } else if (null !=
					 * entry.getRefundedScheduleDeliveryChargeAmt() &&
					 * entry.getRefundedScheduleDeliveryChargeAmt().doubleValue() > 0)// INC144316465 STARTS {
					 * LOG.debug(REFUNDED_DELIVERY_MESSAGE + entry.getRefundedScheduleDeliveryChargeAmt());
					 * xmlData.setScheduleDelCharge(entry.getRefundedScheduleDeliveryChargeAmt().doubleValue()); } else {
					 * xmlData.setScheduleDelCharge(0.00); } // INC144316465 end } else if (null != zoneDelivery && null !=
					 * zoneDelivery.getDeliveryMode() && entry.getCurrDelCharge() != null &&
					 * entry.getRefundedDeliveryChargeAmt() != null && null != zoneDelivery.getDeliveryMode().getCode() &&
					 * zoneDelivery.getDeliveryMode().getCode().equalsIgnoreCase("home-delivery")) {
					 * 
					 * LOG.debug("inside home del"); if (entry.getCurrDelCharge().doubleValue() > 0) {
					 * LOG.debug("setting current delivery charge...");
					 * xmlData.setShipmentCharge(entry.getCurrDelCharge().doubleValue()); } else {
					 * LOG.debug(REFUNDED_DELIVERY_MESSAGE + entry.getRefundedDeliveryChargeAmt());
					 * xmlData.setShipmentCharge(entry.getRefundedDeliveryChargeAmt().doubleValue()); } // TISPRDT-1186 START
					 * xmlData.setExpressdeliveryCharge(0.00); // TISPRDT-1186 END if (null !=
					 * entry.getScheduledDeliveryCharge() && entry.getScheduledDeliveryCharge().doubleValue() > 0) {
					 * LOG.debug("setting schedule delivery charge..." + entry.getScheduledDeliveryCharge());
					 * xmlData.setScheduleDelCharge(entry.getScheduledDeliveryCharge().doubleValue()); } // INC144316465
					 * STARTS else if (null != entry.getRefundedScheduleDeliveryChargeAmt() &&
					 * entry.getRefundedScheduleDeliveryChargeAmt().doubleValue() > 0) { LOG.debug(REFUNDED_DELIVERY_MESSAGE
					 * + entry.getRefundedScheduleDeliveryChargeAmt());
					 * xmlData.setScheduleDelCharge(entry.getRefundedScheduleDeliveryChargeAmt().doubleValue()); } else {
					 * xmlData.setScheduleDelCharge(0.00); }
					 * 
					 * // INC144316465 end LOG.debug("set del charge"); } }
					 */

					List<MerchantInfoXMlData> merchantInfoList = new ArrayList<MerchantInfoXMlData>();
					if (entry.getWalletApportionPaymentInfo() != null)
					{
						WalletApportionPaymentInfoModel paymentInfoModel = entry.getWalletApportionPaymentInfo();
						if (!CollectionUtils.isEmpty(paymentInfoModel.getWalletCardList()))
						{
							for (WalletCardApportionDetailModel apporationWalllet : paymentInfoModel.getWalletCardList())
							{

								MerchantInfoXMlData merchantInfoXMlData = new MerchantInfoXMlData();
								merchantInfoXMlData
										.setMerchantType(getConfigurationService().getConfiguration().getString(PAYMENT_QC_MERCHANT_TYPE));
								merchantInfoXMlData
										.setMerchantCode(getConfigurationService().getConfiguration().getString(PAYMENT_QC_MERCHANT_ID));
								double qcDelivery = 0;
								if (apporationWalllet.getQcDeliveryValue() != null)
								{
									qcDelivery = Double.parseDouble(apporationWalllet.getQcDeliveryValue());
								}
								merchantInfoXMlData.setShipmentCharge(qcDelivery);
								double scheduleDelCharge = 0;
								if (apporationWalllet.getQcSchedulingValue() != null)
								{
									scheduleDelCharge = Double.parseDouble(apporationWalllet.getQcSchedulingValue());
								}
								merchantInfoXMlData.setScheduleDelCharge(scheduleDelCharge);
								double shippingValue = 0;
								if (apporationWalllet.getQcShippingValue() != null)
								{
									shippingValue = Double.parseDouble(apporationWalllet.getQcShippingValue());
								}
								merchantInfoXMlData.setExpressDelCharge(shippingValue);

								//merchantInfoXMlData.setReversePaymentRefId(payemntrefid); 


								double total = 0;
								if (apporationWalllet.getCardAmount() != null)
								{
									total = Double.parseDouble(apporationWalllet.getCardAmount());
								}
								
								merchantInfoXMlData.setBucketId(apporationWalllet.getBucketType());
								merchantInfoXMlData.setProductAmount(total);
								merchantInfoXMlData.setCardNumber(apporationWalllet.getCardNumber());
								if (chaildModel.getParentReference() != null)
								{
									merchantInfoXMlData.setPaymentRefID(chaildModel.getParentReference().getCode());
								}
								String cardExpDate = getCardExpDate(apporationWalllet);
								merchantInfoXMlData.setCardExpiryDate(cardExpDate);
								merchantInfoList.add(merchantInfoXMlData);
							}


							 if (StringUtils.isNotEmpty(paymentInfoModel.getJuspayApportionValue())
							         && Double.parseDouble((paymentInfoModel.getJuspayApportionValue())) > 0){
								MerchantInfoXMlData merchantInfoXMlData = new MerchantInfoXMlData();
								merchantInfoXMlData.setMerchantType(
										getConfigurationService().getConfiguration().getString(PAYMENT_JUSPAY_MERCHANT_TYPE));
								//need be check
								merchantInfoXMlData.setProductAmount((Double.parseDouble(paymentInfoModel.getJuspayApportionValue())));
						
								merchantInfoXMlData.setExpressDelCharge(Double.parseDouble(paymentInfoModel.getJuspayDeliveryValue()));
								merchantInfoXMlData.setScheduleDelCharge(Double.parseDouble(paymentInfoModel.getJuspaySchedulingValue()));
								merchantInfoXMlData.setShipmentCharge(Double.parseDouble(paymentInfoModel.getJuspayShippingValue()));

								final List<PaymentTransactionModel> list = chaildModel.getPaymentTransactions();
								if (null != list && !list.isEmpty())
								{
									LOG.debug("DeliveryMode list"+list);
									for (final PaymentTransactionModel oModel : list)
									{
										LOG.debug("DeliveryMode oModel"+oModel);
										if (null != oModel.getStatus() && null != oModel.getPaymentProvider() && !oModel.getPaymentProvider().equalsIgnoreCase(CLIQ_CASH)
												&& oModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
										{
											
											if (MarketplacecommerceservicesConstants.MRUPEE_CODE
													.equalsIgnoreCase(oModel.getPaymentProvider()))
											{
												merchantInfoXMlData.setMerchantCode(getConfigurationService().getConfiguration()
														.getString(MarketplacecommerceservicesConstants.MRUPEE_MERCHANT_CODE));
											}
											else
											{
												merchantInfoXMlData.setMerchantCode(oModel.getPaymentProvider());
											}
											if (null != oModel.getCode())
											{
												payemntrefid = oModel.getCode();
												LOG.debug("Inside Parent order: Pyment Transaction model" + payemntrefid);
											}
										}
									}
									merchantInfoXMlData.setPaymentRefID(payemntrefid);
								}
								merchantInfoList.add(merchantInfoXMlData);

							}
						}
					}
					else
					{
						MerchantInfoXMlData merchantInfoXMlData = getMarchantInfo(chaildModel, entry);
						merchantInfoXMlData
								.setMerchantType(getConfigurationService().getConfiguration().getString(PAYMENT_JUSPAY_MERCHANT_TYPE));
						merchantInfoList.add(merchantInfoXMlData);
					}

					xmlData.setMerchantInfoList(merchantInfoList);
					
					double tAmount=0;
					tAmount = getTotalAmount(merchantInfoList, tAmount);
					//setting Total amount of product
					xmlData.setAmount(tAmount);
					LOG.debug(">>>>>>> before prodcatlist");
					List<CategoryModel> productCategoryList = new ArrayList<>();
					if (xmlToFico)
					{
						productCategoryList = getDefaultPromotionsManager().getPrimarycategoryData(product);
					}
					LOG.debug(">>>>>>> after prodcatlist");

					if (null != productCategoryList && productCategoryList.size() > 0)
					{
						LOG.debug("prodcatlist" + productCategoryList.size());
						categoryList = new ArrayList<String>();
						for (final CategoryModel category : productCategoryList)
						{
							if (category != null && !(category instanceof ClassificationClassModel) && null != category.getCode())
							{
								LOG.debug("Category Data:>>>>>>>> Category Code>>>" + category.getCode());
								categoryList.add(category.getCode());
							}
						}

						if (!(categoryList.isEmpty()) && categoryList.size() > 0)
						{
							Collections.sort(categoryList, new Comparator()
							{

								@Override
								public int compare(final Object o1, final Object o2)
								{
									if (o1.toString().length() < o2.toString().length())
									{
										return 1;
									}
									else if (o1.toString().length() > o2.toString().length())
									{
										return -1;
									}
									else
									{
										return 0;
									}
								}

							});


							LOG.debug("Inside Category>>>>>");
							LOG.debug("Category size is >>>>> " + categoryList.size());
							if (categoryList.size() >= 3)
							{
								xmlData.setPrimaryCategory(categoryList.get(categoryList.size() - 2));
								LOG.debug("primary cat" + categoryList.get(categoryList.size() - 2));
								xmlData.setSecondaryCategory(categoryList.get(categoryList.size() - 3));
								LOG.debug("secondary cat" + categoryList.get(categoryList.size() - 3));
							}

						}

					}

					xmlData.setOrderTag("NOR");

					childOrderDataList.add(xmlData);
				}
			}
		}
		return childOrderDataList;

	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}

	protected SellerBasedPromotionService getSellerBasedPromotionService()
	{
		return Registry.getApplicationContext().getBean("sellerBasedPromotionService", SellerBasedPromotionService.class);
	}

	/**
	 * @return the configurationService
	 */
	protected ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean("configurationService", ConfigurationService.class);
	}
	
	private void generatePartOrderData(final List<SalesOrderXMLData> bulkSalesDataList)
	{


		BulkSalesOrderXMLData xmlData = null;
		String xmlString = MarketplacecommerceservicesConstants.EMPTYSPACE;
		boolean invalidXMLToFICO = false;

		try
		{

			xmlData = new BulkSalesOrderXMLData();
			if (null != bulkSalesDataList && !bulkSalesDataList.isEmpty())
			{
				LOG.debug("full order data");
				xmlData.setOrderDataList(bulkSalesDataList);
				LOG.debug("bulk sales list set");
				final JAXBContext context = JAXBContext.newInstance(BulkSalesOrderXMLData.class);
				final Marshaller m = context.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				final StringWriter sw = new StringWriter();
				m.marshal(xmlData, sw);
				xmlString = sw.toString();
				LOG.info(xmlString);

				final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setIgnoringComments(true);
				factory.setCoalescing(true); // Convert CDATA to Text nodes
				factory.setNamespaceAware(false); // No namespaces: this is default factory.setValidating(false); //
				// Don't validate DTD: also default

				final DocumentBuilder parser = factory.newDocumentBuilder();

				final Document document = parser.parse(new InputSource(new StringReader(xmlString)));

				NodeList nm = null;
				Node node = null;

				nm = document.getElementsByTagName("SalesOrders");

				if (null != nm)
				{
					node = nm.item(0);
					if (null != node && StringUtils.isEmpty(node.getTextContent()))
					{
						invalidXMLToFICO = true;
					}
				}

				if (!invalidXMLToFICO)
				{
					paymentInfoRevWebService.paymentInfoRev(xmlString);
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
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
		}

	}
	
	/**
	 * @param merchantInfoList
	 * @param tAmount
	 * @return
	 */
	private double getTotalAmount(List<MerchantInfoXMlData> merchantInfoList, double tAmount)
	{try{
		for(MerchantInfoXMlData merchantInfoXMlTotal:merchantInfoList){
			tAmount=merchantInfoXMlTotal.getScheduleDelCharge()+merchantInfoXMlTotal.getShipmentCharge()+merchantInfoXMlTotal.getExpressDelCharge()+merchantInfoXMlTotal.getProductAmount();	
		}
		return tAmount;
	}catch(Exception exception){
		LOG.error("Error occure while sum of total amount"+exception.getMessage());
	}
		return tAmount;
	}

	/**
	 * @param apporationWalllet
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("javadoc")
	private String getCardExpDate(WalletCardApportionDetailModel apporationWalllet) 
	{
		String cardExpDate = apporationWalllet.getCardExpiry();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(StringUtils.isNotEmpty(cardExpDate)){
		Date date = null;
		try {
			date = format1.parse(cardExpDate);
			LOG.info("Card Exp converting");
			return format2.format(date);
		} catch (ParseException e) {
			LOG.error(ERROR_GETTING_EXCEPTION_WHILE_CHANING_DATE_FORMAT);
		}
		}
		return null;
	}

	/**
	 * @param chaildModel
	 * @param entry
	 * @return
	 */
	
	private MerchantInfoXMlData getMarchantInfo(OrderModel chaildModel, final AbstractOrderEntryModel entry)
	{
		MerchantInfoXMlData merchantInfoXMlData = new MerchantInfoXMlData();
		
		if (null != entry.getTotalPrice())
		{
			if (entry.getNetAmountAfterAllDisc().doubleValue() > 0)
			{
				LOG.debug("*****total price with discount*****" + entry.getNetAmountAfterAllDisc());
				merchantInfoXMlData.setProductAmount(entry.getNetAmountAfterAllDisc().doubleValue());
			}
			else
			{
				LOG.debug("total price call" + entry.getTotalPrice());
				merchantInfoXMlData.setProductAmount(entry.getTotalPrice().doubleValue());
			}
			LOG.debug("after price set");
		}
		merchantInfoXMlData.setShipmentCharge(entry.getCurrDelCharge().doubleValue());
		final List<PaymentTransactionModel> list = chaildModel.getPaymentTransactions();
		if (null != list && !list.isEmpty())
		{

			for (final PaymentTransactionModel oModel : list)
			{

				if (null != oModel.getStatus() && null != oModel.getPaymentProvider() &&  !oModel.getPaymentProvider().equalsIgnoreCase(CLIQ_CASH)
						&& oModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
				{
					if (MarketplacecommerceservicesConstants.MRUPEE_CODE.equalsIgnoreCase(oModel.getPaymentProvider()))
					{
						merchantInfoXMlData.setMerchantCode(getConfigurationService().getConfiguration()
								.getString(MarketplacecommerceservicesConstants.MRUPEE_MERCHANT_CODE));
					}
					else
					{
						merchantInfoXMlData.setMerchantCode(oModel.getPaymentProvider());
					}
					if (null != oModel.getCode())
					{
						payemntrefid = oModel.getCode();
						LOG.debug("Inside Parent order: Pyment Transaction model" + payemntrefid);
					}
				}

			}

			merchantInfoXMlData.setPaymentRefID(payemntrefid);
		}

		if (null != entry.getMplDeliveryMode() && xmlToFico)
		{
			final MplZoneDeliveryModeValueModel zoneDelivery = entry.getMplDeliveryMode();
			if (null != zoneDelivery && null != zoneDelivery.getDeliveryMode() && entry.getCurrDelCharge() != null
					&& entry.getRefundedDeliveryChargeAmt() != null && null != zoneDelivery.getDeliveryMode().getCode()
					&& zoneDelivery.getDeliveryMode().getCode().equalsIgnoreCase("express-delivery"))
			{
				// TISPRDT-1186 START
				if (entry.getCurrDelCharge().doubleValue() > 0)
				{
					//	xmlData.setExpressdeliveryCharge(entry.getCurrDelCharge().doubleValue());
					merchantInfoXMlData.setShipmentCharge(entry.getCurrDelCharge().doubleValue());
				}
				else
				{
					merchantInfoXMlData.setShipmentCharge(entry.getRefundedDeliveryChargeAmt().doubleValue());
				}
				merchantInfoXMlData.setExpressDelCharge(0.00);
				//	TISPRDT-1186 END

				if (null != entry.getScheduledDeliveryCharge() && entry.getScheduledDeliveryCharge().doubleValue() > 0)
				{
					merchantInfoXMlData.setScheduleDelCharge(entry.getScheduledDeliveryCharge().doubleValue());
				}
				else if (null != entry.getRefundedScheduleDeliveryChargeAmt()
						&& entry.getRefundedScheduleDeliveryChargeAmt().doubleValue() > 0)// INC144316465 STARTS
				{
					merchantInfoXMlData.setScheduleDelCharge(entry.getRefundedScheduleDeliveryChargeAmt().doubleValue());
				}
				else
				{
					merchantInfoXMlData.setScheduleDelCharge(0.00);
				}
				// INC144316465 end
			}
			else if (null != zoneDelivery && null != zoneDelivery.getDeliveryMode() && entry.getCurrDelCharge() != null
					&& entry.getRefundedDeliveryChargeAmt() != null && null != zoneDelivery.getDeliveryMode().getCode()
					&& zoneDelivery.getDeliveryMode().getCode().equalsIgnoreCase("home-delivery"))
			{

				if (entry.getCurrDelCharge().doubleValue() > 0)
				{
					merchantInfoXMlData.setShipmentCharge(entry.getCurrDelCharge().doubleValue());
				}
				else
				{
					merchantInfoXMlData.setShipmentCharge(entry.getRefundedDeliveryChargeAmt().doubleValue());
				}
				// TISPRDT-1186 START
				merchantInfoXMlData.setExpressDelCharge(0.00);
				// TISPRDT-1186 END
				if (null != entry.getScheduledDeliveryCharge() && entry.getScheduledDeliveryCharge().doubleValue() > 0)
				{
					merchantInfoXMlData.setScheduleDelCharge(entry.getScheduledDeliveryCharge().doubleValue());
				}
				// INC144316465 STARTS
				else if (null != entry.getRefundedScheduleDeliveryChargeAmt()
						&& entry.getRefundedScheduleDeliveryChargeAmt().doubleValue() > 0)
				{
					merchantInfoXMlData.setScheduleDelCharge(entry.getRefundedScheduleDeliveryChargeAmt().doubleValue());
				}
				else
				{
					merchantInfoXMlData.setScheduleDelCharge(0.00);
				}
				// INC144316465 end
			}
		}
		return merchantInfoXMlData;
	}

}
