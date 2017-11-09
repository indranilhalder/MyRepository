/**
 *
 */
package com.tisl.mpl.integration.job;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.WalletCardApportionDetailModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.pojo.BulkSalesOrderXMLData;
import com.tisl.mpl.pojo.ChildOrderXMlData;
import com.tisl.mpl.pojo.MerchantInfoXMlData;
import com.tisl.mpl.pojo.SalesOrderXMLData;
import com.tisl.mpl.pojo.SubOrderXMLData;
import com.tisl.mpl.promotion.service.SellerBasedPromotionService;
import com.tisl.mpl.service.PaymentInfoCancelReversalImpl;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class SalesOrderReverseXMLUtility
{
	/**
	 * 
	 */
	private static final String SUCCESS = "SUCCESS";
	/**
	 * 
	 */
	private static final String CLIQ_CASH = "Cliq Cash";
	/**
	 * 
	 */
	private static final String SPLIT = "Split";
	/**
	 *
	 */
	private static final String PAYMENT_JUSPAY_MERCHANT_TYPE = "payment.juspay.merchantType";
	private static final String PAYMENT_QC_MERCHANT_TYPE = "payment.qc.merchantType";
	private static final String PAYMENT_QC_MERCHANT_ID = "payment.qc.merchantID";
	private static final String LOG_MSG_AND_GET_REFUNDED_SCHEDULE_DELIVERY_CHARGE_AMT = " and getRefundedScheduleDeliveryChargeAmt";
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SalesOrderReverseXMLUtility.class.getName());
	@Autowired
	private PaymentInfoCancelReversalImpl paymentInfoCancelService;
	@Resource
	private ModelService modelService;
	/*
	 * @Autowired private MplSellerInformationService mplSellerInformationService;
	 */

	private String payemntrefid = null;
	private boolean xmlToFico = true;
	private final String RET = MarketplacecommerceservicesConstants.RETURN_FLAG;
	private final String CAN = MarketplacecommerceservicesConstants.CANCEL_FLAG;
	private final String SDB = MarketplacecommerceservicesConstants.SDB_FLAG;
	private final String EDTOHD = MarketplacecommerceservicesConstants.EDTOHD_FLAG;

	//static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

	/**
	 * @Description: Generate XML Data for Order
	 * @param orderData
	 */
	public void generateCanellOrderData(final List<OrderModel> orderModelList)
	{

		List<SalesOrderXMLData> bulkSalesDataList = null;

		try
		{
			if (null != orderModelList && !orderModelList.isEmpty())
			{
				bulkSalesDataList = getParentOrderData(orderModelList);
				final int rowLimit = getConfigurationService().getConfiguration().getInt(
						MarketplacecommerceservicesConstants.PAYMENTINFO_R_ROWLIMIT,0);
				if (rowLimit > 0)
				{
					int startIndex = 0;
					final int listSize = bulkSalesDataList.size();
					while (startIndex < listSize)
					{
						final int endIndex = (startIndex + rowLimit) < listSize ? (startIndex + rowLimit) : listSize;
						final List<SalesOrderXMLData> partSalesData = bulkSalesDataList.subList(startIndex, endIndex);
						generatePartCanellOrderData(partSalesData);
						startIndex += rowLimit;
					}
				}
				else
				{
					generatePartCanellOrderData(bulkSalesDataList);
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

	private Map checkCanelReturn(final OrderModel orderModel)
	{
		String orderCancelReturn = null;
		//	orderModel.getType().equalsIgnoreCase("Suborder");
		final Map<String, Map<String, String>> returnCancelMap = new HashMap<String, Map<String, String>>();
		final SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String lineID = null;
		String cancelReturnDate = null;

		if (null != orderModel && null != orderModel.getHistoryEntries())
		{

			final List<OrderHistoryEntryModel> orderHistory = orderModel.getHistoryEntries();


			for (final OrderHistoryEntryModel orderHistoryEntryModel : orderHistory)
			{
				final String retRef = MarketplacecommerceservicesConstants.RETURN_COMPLETED;
				final String orderCancel = MarketplacecommerceservicesConstants.ORDER_CANCELLED;
				LOG.debug("Inside order history entry model");
				LOG.warn("Inside order history entry model" + orderHistoryEntryModel.getOrder());
				//	if (checkOrderHistoryEntryDate(orderHistoryEntryModel.getCreationtime().toString()))
				//	final Calendar calendar = Calendar.getInstance();
				//calendar.add(Calendar.HOUR_OF_DAY, -24);

				if ((orderHistoryEntryModel.getDescription().equals(orderCancel)))

				{
					lineID = orderHistoryEntryModel.getLineId(); // line ID is trnsction id
					orderCancelReturn = CAN;
					cancelReturnDate = sdformat.format(orderHistoryEntryModel.getModifiedtime());

					final Map<String, String> orderTagDateMap = new HashMap<String, String>();
					orderTagDateMap.put(orderCancelReturn, cancelReturnDate);
					LOG.debug("cancel map size>>>>>" + orderTagDateMap.size());
					returnCancelMap.put(lineID, orderTagDateMap);

				}
				if ((orderHistoryEntryModel.getDescription().equals(retRef)))
				{
					boolean refundedAtstore = true; // for RTS returns , if refund already given at store then no need to send that order line to fico  TISRLEE-2020
					if (null != orderModel.getReturnRequests())
					{
						for (final ReturnRequestModel returnRequest : orderModel.getReturnRequests())
						{
							if (null != returnRequest.getReturnEntries())
							{
								for (final ReturnEntryModel returnEntry : returnRequest.getReturnEntries())
								{
									if (null != returnEntry.getOrderEntry())
									{
										if (returnEntry.getOrderEntry().getTransactionID()
												.equalsIgnoreCase(orderHistoryEntryModel.getLineId()))
										{
											if (null != ((RefundEntryModel) returnEntry).getRefundMode()
													&& ((RefundEntryModel) returnEntry).getRefundMode().equalsIgnoreCase(
															MarketplacecommerceservicesConstants.REFUND_MODE_C))
											{
												refundedAtstore = true;
												break;
											}
											else
											{
												refundedAtstore = false;
												break;
											}
										}
									}
								}
							}

						}
					}
					if (!refundedAtstore)
					{
						lineID = orderHistoryEntryModel.getLineId();
						orderCancelReturn = RET;
						cancelReturnDate = sdformat.format(orderHistoryEntryModel.getModifiedtime());
						final Map<String, String> orderTagDateMap = new HashMap<String, String>();
						orderTagDateMap.put(orderCancelReturn, cancelReturnDate);
						LOG.debug("return map size>>>>>" + orderTagDateMap.size());
						returnCancelMap.put(lineID, orderTagDateMap);
					}
				}

			}
		}
		return returnCancelMap;
	}

	private boolean checkCOD(final OrderModel orderData)
	{
		boolean isCOD = false;


		if (null != orderData.getPaymentTransactions())
		{
			final List<PaymentTransactionModel> list = orderData.getPaymentTransactions();
			if (null != list && !list.isEmpty())
			{
				for (final PaymentTransactionModel oModel : list)
				{
					// check COD
					for (final PaymentTransactionEntryModel paymentObj : oModel.getEntries())
					{
						if (null != paymentObj.getPaymentMode()
								&& null != paymentObj.getPaymentMode().getMode()
								&& paymentObj.getPaymentMode().getMode()
										.equalsIgnoreCase(MarketplacecommerceservicesConstants.CASH_ON_DELIVERY))
						{
							isCOD = true;
						}

					}
				}
			}
		}
		return isCOD;
	}

	/**
	 * @Description : Fetch Parent Order Data
	 * @param listordermodel
	 * @return bulkSalesDataList
	 */
	private List<SalesOrderXMLData> getParentOrderData(final List<OrderModel> listordermodel)
	{

		final SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<SalesOrderXMLData> bulkSalesDataList = null;
		try
		{

			if (CollectionUtils.isEmpty(listordermodel))
			{
				xmlToFico = false;
			}
			else
			{
				bulkSalesDataList = new ArrayList<SalesOrderXMLData>();
				for (final OrderModel order : listordermodel)
				{
					xmlToFico = true;

					LOG.debug("checkReturnCancelMap and cod");
					if (!checkCOD(order))
					{
						// else { LOG.INFO("checkReturnCancelMap IS IEM")}
						SalesOrderXMLData salesXMLData = new SalesOrderXMLData();

						if (null != order.getParentReference() && null != order.getParentReference().getCode() && xmlToFico)
						{
							salesXMLData.setOrderId(order.getParentReference().getCode());
							LOG.info("order id in SalesOrderReverse" + order.getParentReference().getCode());
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
						if (null != order.getParentReference() && null != order.getParentReference().getPaymentTransactions()
								&& xmlToFico)
						{
							final List<PaymentTransactionModel> list = order.getParentReference().getPaymentTransactions();
							if (null != list && !list.isEmpty())
							{

								for (final PaymentTransactionModel oModel : list)
								{
									if (null != oModel.getStatus() && null != oModel.getPaymentProvider()
											&& oModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS)
											&& xmlToFico)
									{
										//salesXMLData.setMerchantCode(oModel.getPaymentProvider());
										//TISSQAEE-227
										if (MarketplacecommerceservicesConstants.MRUPEE_CODE.equalsIgnoreCase(oModel.getPaymentProvider()))
										{//New Changes  
											/*salesXMLData.setMerchantCode(getConfigurationService().getConfiguration().getString(
													MarketplacecommerceservicesConstants.MRUPEE_MERCHANT_CODE));*/
										}
										else
										{
											//salesXMLData.setMerchantCode(oModel.getPaymentProvider());//New Changes  
										}

										if (null != oModel.getCode())
										{
											payemntrefid = oModel.getCode();
											LOG.debug(">>>>>>>>>>>>>>" + payemntrefid);
										}
									}
								}
							}

						}
						else
						{
							xmlToFico = false;
						}
						LOG.debug("****calling suborder*****");
						if (null != order.getParentReference() && null != order.getParentReference().getChildOrders()
								&& !order.getParentReference().getChildOrders().isEmpty() && xmlToFico)
						{

							LOG.debug(" child order data not null");
							List<SubOrderXMLData> subOrderDataList = new ArrayList<SubOrderXMLData>();
							subOrderDataList = getSubOrderData(order);

							//if (subOrderDataList.size() == 0)
							if (CollectionUtils.isEmpty(subOrderDataList))
							{
								salesXMLData = null;
							}

							if (null != subOrderDataList && !subOrderDataList.isEmpty() && xmlToFico)
							{
								salesXMLData.setSubOrderList(subOrderDataList);
								LOG.debug("set sub order list");
							}
						}
						//TISSIT-1780
						if (salesXMLData != null && xmlToFico)
						{
							bulkSalesDataList.add(salesXMLData);
							LOG.debug("xml order:" + salesXMLData.getOrderId());
						}
						LOG.debug("bulkSalesDataList Size" + bulkSalesDataList.size());
					}

				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			throw (e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw (e);
		}
		catch (final Exception e)
		{
			throw (e);
		}

		return bulkSalesDataList;
	}

	/**
	 * @Description : Fetch Sub Order Data
	 * @param childOrders
	 * @return subOrderDataList
	 */
	private List<SubOrderXMLData> getSubOrderData(final OrderModel childOrders)
	{
		final List<SubOrderXMLData> subOrderDataList = new ArrayList<SubOrderXMLData>();
		List<ChildOrderXMlData> childOrderDataList = new ArrayList<ChildOrderXMlData>();
		try
		{
			//for (final OrderModel order : childOrders)

			final Map checkReturnCancelMap = checkCanelReturn(childOrders);
			final boolean sdbOrEdToHDFlag = getSDBOrEdToHdFlag(childOrders);
			//if (checkReturnCancelMap != null && checkReturnCancelMap.size() > 0)
			if (MapUtils.isNotEmpty(checkReturnCancelMap) || sdbOrEdToHDFlag)
			{
				/*
				 * if (null != order.getPaymentTransactions()) { final List<PaymentTransactionModel> list =
				 * order.getPaymentTransactions(); if (null != list && !list.isEmpty()) { for (final PaymentTransactionModel
				 * payTransModel : list) { final List<PaymentTransactionEntryModel> paymentTransactionEntryList =
				 * payTransModel.getEntries(); if (null != payTransModel.getCode() && null != payTransModel.getStatus() &&
				 * CollectionUtils.isNotEmpty(paymentTransactionEntryList) &&
				 * payTransModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS)) { for (final
				 * PaymentTransactionEntryModel ptModel : paymentTransactionEntryList) { if (null != ptModel.getType() &&
				 * ptModel.getType().equals(PaymentTransactionType.CANCEL) ||
				 * ptModel.getType().equals(PaymentTransactionType.REFUND_DELIVERY_CHARGES) ||
				 * ptModel.getType().equals(PaymentTransactionType.MANUAL_REFUND) ||
				 * ptModel.getType().equals(PaymentTransactionType.RETURN)) { reversepayemntrefid = payTransModel.getCode();
				 * LOG.info(reversepayemntrefid); LOG.debug(ptModel.getType()); break; } }
				 * 
				 * } } } }
				 */
				final SubOrderXMLData xmlData = new SubOrderXMLData();
				if (null != childOrders.getCode() && xmlToFico)
				{
					xmlData.setSubOrderId(childOrders.getCode());
					LOG.debug("suborder id" + childOrders.getCode());
				}
				else
				{
					xmlToFico = false;
				}

				if (null != childOrders.getEntries() && !childOrders.getEntries().isEmpty())
				{

					childOrderDataList = getChildOrderDataForXML(childOrders.getEntries(), checkReturnCancelMap,childOrders);

				}

				if (null != childOrderDataList && !childOrderDataList.isEmpty() && xmlToFico)
				{
					LOG.debug("before child order list set");
					xmlData.setTransactionInfoList(childOrderDataList);
					subOrderDataList.add(xmlData);
					LOG.debug("child order list set");
				}

				//	subOrderDataList.add(xmlData);
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			throw (e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw (e);
		}
		catch (final Exception e)
		{
			throw (e);
		}
		return subOrderDataList;
	}


	/**
	 * @param childOrders
	 * @return Boolean
	 */
	private boolean getSDBOrEdToHdFlag(final OrderModel childOrders)
	{
		LOG.info("Checking edToHd and sdb flag");
		boolean edToHdFlag = false;
		boolean sdbFlag = false;
		try
		{
			for (final AbstractOrderEntryModel entry : childOrders.getEntries())
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Checking EDToHd for transaction ID " + entry.getTransactionID());
					LOG.debug("Is EdToHd" + entry.getIsEDtoHD() + "IsSdbSendToFico" + entry.getIsEdToHdSendToFico()
							+ LOG_MSG_AND_GET_REFUNDED_SCHEDULE_DELIVERY_CHARGE_AMT + entry.getRefundedEdChargeAmt());
				}
				final boolean edAmountRefunded = getAmountRefunded(entry,
						PaymentTransactionType.REFUND_EXPRESS_DELIVERY_CHARGES.getCode());
				if (edAmountRefunded && null != entry.getIsEDtoHD() && entry.getIsEDtoHD().booleanValue())
				{
					edToHdFlag = true;
					return true;
				}
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Checking SDB for transaction ID " + entry.getTransactionID());
					LOG.debug("Is SDb" + entry.getIsSdb() + "IsSdbSendToFico" + entry.getIsSdbSendToFico()
							+ LOG_MSG_AND_GET_REFUNDED_SCHEDULE_DELIVERY_CHARGE_AMT + entry.getRefundedScheduleDeliveryChargeAmt());
				}
				final boolean sdAmountRefunded = getAmountRefunded(entry,
						PaymentTransactionType.REFUND_SCHEDULE_DELIVERY_CHARGES.getCode());
				if (sdAmountRefunded && null != entry.getIsSdbSendToFico() && !entry.getIsSdbSendToFico().booleanValue()
						&& null != entry.getScheduleChargesJuspayRequestId())
				{
					sdbFlag = true;
					return true;
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception while checking EDToHD Flag/SDB Flag for the order :" + childOrders.getCode() + e);
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("edToHdFlag" + edToHdFlag + "  sdbFlag" + sdbFlag);
		}
		return edToHdFlag || sdbFlag;
	}


	/**
	 * @param entry
	 */
	private List<ChildOrderXMlData> getChildOrderDataForXML(final List<AbstractOrderEntryModel> entries,
			final Map checkReturnCancelMap, OrderModel chaildModel)
	{
		final List<ChildOrderXMlData> childOrderDataList = new ArrayList<ChildOrderXMlData>();
		List<String> categoryList = new ArrayList<String>();
		try
		{
			if (null != entries && !entries.isEmpty())
			{
				for (final AbstractOrderEntryModel entry : entries)
				{
					List<MerchantInfoXMlData> merchantInfoList = new ArrayList<MerchantInfoXMlData>();

					boolean canOrRetflag = false; //flag for checking if order line is cancelled or returned. If flag is false the order line will not be set in the XML
					boolean returnFlag = false;
					boolean cancelFlag = false;

					/* Added in R2.3 START */
					final boolean sdbFlag = checkSdbFlag(entry);
					final boolean edToHdFlag = checkEdToHdFlag(entry);
					/* Added in R2.3 END */
					if (null != entry && null != entry.getProduct()
							&& ((null == entry.getIsSentToFico() || !entry.getIsSentToFico().booleanValue()) || sdbFlag || edToHdFlag)) // null ==  entry.getIsSentToFico() is added for n/a scenarios for previous placed orders
					{
						final ProductModel product = entry.getProduct();
						LOG.debug("inside AbstractOrderEntryModel");
						final ChildOrderXMlData xmlData = new ChildOrderXMlData();
						if (null != entry.getProduct() && null != product.getCode() && xmlToFico)
						{
							xmlData.setItemNumber(product.getCode());
						}
						else
						{
							xmlToFico = false;
						}

						if (null != payemntrefid && xmlToFico)
						{
							//xmlData.setPaymentRefID(payemntrefid);
						}
						else
						{
							xmlToFico = false;
						}

						//Newly Added Code For DeliveryMode
						if (null != entry.getMplDeliveryMode() && xmlToFico)
						{
							LOG.debug("DeliveryMode Setting into SalesOrderReverseXMLUtility ");
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
							LOG.info("DeliveryMode For FICO   " + entry.getMplDeliveryMode().getDeliveryMode().getCode());
						}else if(chaildModel.getIsEGVCart()!=null && chaildModel.getIsEGVCart().booleanValue()){
							    LOG.info("DeliveryMode Fico for Egv Order Inforamtion ");
							    xmlData.setDeliveryMode(MarketplacecommerceservicesConstants.HD);
						}
						else
						{
							xmlToFico = false;
						}


						if (null != entry.getSelectedUSSID() && xmlToFico)
						{
							LOG.info("entry.getSelectedUSSID()   " + entry.getSelectedUSSID());
							List<SellerInformationModel> productSellerData = null;
							xmlData.setUSSID(entry.getSelectedUSSID());
							LOG.info("getDefaultPromotionsManager().catalogData()  " + getDefaultPromotionsManager().catalogData());
							productSellerData = getSellerBasedPromotionService().fetchSellerInformation(entry.getSelectedUSSID(),
									getDefaultPromotionsManager().catalogData());
							if (null != productSellerData && !productSellerData.isEmpty())
							{
								for (final SellerInformationModel seller : productSellerData)
								{

									if (null != seller.getSellerID())
									{
										xmlData.setSellerCode(seller.getSellerID());
										LOG.info("seller id set ");
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

/*						LOG.info("total price call" + entry.getTotalPrice());
*/
						final String orderLineId = entry.getOrderLineId() != null ? entry.getOrderLineId() : entry.getTransactionID();

						if (checkReturnCancelMap.containsKey(orderLineId))
						{
							/*if (null != entry.getTotalPrice() && xmlToFico)
							{
								if (entry.getNetAmountAfterAllDisc().doubleValue() > 0)
								{
									LOG.info("*****total price with discount*****" + entry.getNetAmountAfterAllDisc());
									xmlData.setAmount(entry.getNetAmountAfterAllDisc().doubleValue());
								}
								else
								{
									LOG.info("total price call" + entry.getTotalPrice());
									xmlData.setAmount(entry.getTotalPrice().doubleValue());
								}
								LOG.info("after price set");
							}*/
						}

						/*
						 * final String ussId = entry.getSelectedUSSID();
						 * 
						 * final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(ussId);
						 * if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null &&
						 * ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0) != null &&
						 * ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0).getDeliveryFulfillModes() !=
						 * null) { final String fulfillmentType = ((List<RichAttributeModel>)
						 * sellerInfoModel.getRichAttribute()).get(0) .getDeliveryFulfillModes().getCode();
						 * LOG.debug("inside rich attribute model" + fulfillmentType.toUpperCase() + " for ussid :" + ussId);
						 * xmlData.setFulfillmentType(fulfillmentType.toUpperCase()); }
						 */


						//TISPRD-901 // TISSQAUAT-4104
						if (StringUtils.isNotEmpty(entry.getFulfillmentType()) && xmlToFico)
						{
							if (StringUtils.isNotEmpty(entry.getFulfillmentTypeP1())
									&& entry.getFulfillmentType().equalsIgnoreCase("Both"))
							{
								xmlData.setFulfillmentType(entry.getFulfillmentTypeP1().toUpperCase());
							}
							else
							{
								xmlData.setFulfillmentType(entry.getFulfillmentType().toUpperCase());
							}

							LOG.debug("set fulfilment mode");
						}



						//						if (null != product.getSellerInformationRelator() && xmlToFico)
						//						{
						//
						//
						//							final Collection<SellerInformationModel> sellerinfolist = product.getSellerInformationRelator();
						//							if (sellerinfolist.size() > 0)
						//							{
						//								for (final SellerInformationModel sellerEntry : sellerinfolist)
						//								{
						//									if (sellerEntry.getRichAttribute().size() > 0)
						//									{
						//										for (final RichAttributeModel richEntry : sellerEntry.getRichAttribute())
						//										{
						//											LOG.debug("inside rich attribute model" + richEntry.getDeliveryFulfillModes());
						//											xmlData.setFulfillmentType(richEntry.getDeliveryFulfillModes().toString().toUpperCase());
						//											LOG.debug("set fulfilment mode");
						//										}
						//									}
						//								}
						//							}
						//						}
						LOG.info(">>>>>>> before prodcatlist");
						final List<CategoryModel> productCategoryList = getDefaultPromotionsManager().getPrimarycategoryData(product);

						//if (null != productCategoryList && productCategoryList.size() > 0)
						if (CollectionUtils.isNotEmpty(productCategoryList))
						{

							categoryList = new ArrayList<>();
							for (final CategoryModel category : productCategoryList)
							{

								if (!(category instanceof ClassificationClassModel))
								{
									LOG.info("Category Data:>>>>>>>>" + category.getName() + "Code>>>" + category.getCode());
									categoryList.add(category.getCode());
								}

							}

							//if (categoryList.size() > 0)
							if (CollectionUtils.isNotEmpty(categoryList))
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
								if (categoryList.size() > 3)
								{
									xmlData.setPrimaryCategory(categoryList.get(categoryList.size() - 2));
									LOG.info("primary cat" + categoryList.get(categoryList.size() - 2));
									xmlData.setSecondaryCategory(categoryList.get(categoryList.size() - 3));
									LOG.info("secondary cat" + categoryList.get(categoryList.size() - 3));
								}

							}

						}

						if (null != (entry.getOrderLineId()) || null != (entry.getTransactionID()) && xmlToFico)
						{
							xmlData.setTransactionId((entry.getOrderLineId() != null) ? entry.getOrderLineId() : entry
									.getTransactionID());
						}
						else
						{
							xmlToFico = false;
						}

						//final String orderLineId = entry.getOrderLineId() != null ? entry.getOrderLineId() : entry.getTransactionID();

						if (checkReturnCancelMap.containsKey(orderLineId))
						{
							final Map<String, String> tagMap = (Map<String, String>) checkReturnCancelMap.get(orderLineId);

							if (tagMap.containsKey(RET))
							{
								xmlData.setOrderTag(RET);
								xmlData.setReturnDate(tagMap.get(RET).toString());
								returnFlag = true;
								LOG.info(tagMap.get(RET).toString());

							}
							else if (tagMap.containsKey(CAN))
							{
								xmlData.setOrderTag(CAN);
								xmlData.setCancelDate(tagMap.get(CAN).toString());
								cancelFlag = true;
								LOG.info(tagMap.get(CAN).toString());
							}

							canOrRetflag = true;
						}
						if (StringUtils.isNotEmpty(entry.getJuspayRequestId()))
						{
							//xmlData.setReversePaymentRefId(entry.getJuspayRequestId());
							//New Changes 
						}
						/*
						 * if (xmlData.getOrderTag().equals("NOR")) { xmlData.setReversePaymentRefId(" "); }
						 */
						if (returnFlag)
						{
							// For Return - refund delivery cost will be 0
							/*xmlData.setShipmentCharge(0.0);
							xmlData.setExpressdeliveryCharge(0.0);*///new chnages
						}
						if (cancelFlag)
						{
							LOG.info("Adding schedule delivery charges for orderLineId " + entry.getOrderLineId());
							if (null != entry.getScheduledDeliveryCharge() && entry.getScheduledDeliveryCharge().doubleValue() > 0.0D)
							{
								//xmlData.setScheduleDelCharge(entry.getScheduledDeliveryCharge().doubleValue());
							}
							else if (null != entry.getRefundedScheduleDeliveryChargeAmt()
									&& entry.getRefundedScheduleDeliveryChargeAmt().doubleValue() > 0.0D)
							{
								//xmlData.setScheduleDelCharge(entry.getRefundedScheduleDeliveryChargeAmt().doubleValue());
							}
						}
						
						 List<PaymentTransactionModel> list = chaildModel.getPaymentTransactions();
							if(StringUtils.isNotEmpty(chaildModel.getSplitModeInfo()) && SPLIT.equalsIgnoreCase(chaildModel.getSplitModeInfo())){
								
								LOG.info("Split merchantInfoXMlData and QC merchantInfoXMlData");
								if(entry.getWalletApportionReturnInfo() !=null && entry.getWalletApportionReturnInfo().getWalletCardList() !=null 
										&& entry.getWalletApportionReturnInfo().getStatus()!=null && SUCCESS.equalsIgnoreCase(entry.getWalletApportionReturnInfo().getStatus())  && entry.getWalletApportionReturnInfo().getStatusForQc().equalsIgnoreCase(SUCCESS)){
									LOG.info("Split merchantInfoXMlData for  QC data");
									for(WalletCardApportionDetailModel walletCardApportionDetailModel:entry.getWalletApportionReturnInfo().getWalletCardList()){
										LOG.debug("QC bucket data##"+getConfigurationService().getConfiguration().getString(PAYMENT_QC_MERCHANT_TYPE));
										MerchantInfoXMlData splitMerchantInfoXMlDataQC=new MerchantInfoXMlData();
										splitMerchantInfoXMlDataQC
										.setMerchantType(getConfigurationService().getConfiguration().getString(PAYMENT_QC_MERCHANT_TYPE));
										splitMerchantInfoXMlDataQC
										.setMerchantCode(getConfigurationService().getConfiguration().getString(PAYMENT_QC_MERCHANT_ID));
										if(list!=null){
										for (final PaymentTransactionModel oModel : list)
										{
											if (null != oModel.getStatus() && null != oModel.getPaymentProvider() && oModel.getPaymentProvider().equalsIgnoreCase(CLIQ_CASH)
													&& oModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
											{
												
								
												if (null != oModel.getCode())
												{
													payemntrefid = oModel.getCode();
												
												}
											}
										}
										}
										splitMerchantInfoXMlDataQC.setPaymentRefID(payemntrefid);
										splitMerchantInfoXMlDataQC.setBucketId(walletCardApportionDetailModel.getBucketType());
										
										LOG.debug("QC bucket data##"+walletCardApportionDetailModel.getBucketType());
									   String date=getCardExpDate(walletCardApportionDetailModel);
										splitMerchantInfoXMlDataQC.setCardExpiryDate(date);
										splitMerchantInfoXMlDataQC.setCardNumber(walletCardApportionDetailModel.getCardNumber());
										splitMerchantInfoXMlDataQC.setReversePaymentRefId(payemntrefid);
										
										double qcDelivery = 0;
										if (walletCardApportionDetailModel.getQcDeliveryValue() != null)
										{
											qcDelivery = Double.parseDouble(walletCardApportionDetailModel.getQcDeliveryValue());
										}
										LOG.debug("QC qcDelivery############"+qcDelivery);
										splitMerchantInfoXMlDataQC.setShipmentCharge(qcDelivery);
										double scheduleDelCharge = 0;
										if (walletCardApportionDetailModel.getQcSchedulingValue() != null)
										{
											scheduleDelCharge = Double.parseDouble(walletCardApportionDetailModel.getQcSchedulingValue());
										}
										splitMerchantInfoXMlDataQC.setScheduleDelCharge(scheduleDelCharge);
										LOG.debug("QC scheduleDelCharge############"+scheduleDelCharge);
										double shippingValue = 0;
										if (walletCardApportionDetailModel.getQcShippingValue() != null)
										{
											shippingValue = Double.parseDouble(walletCardApportionDetailModel.getQcShippingValue());
										}
										splitMerchantInfoXMlDataQC.setExpressDelCharge(shippingValue);
										LOG.debug("QC shippingValue############"+shippingValue);
										double totalAmount=0;
										if (walletCardApportionDetailModel.getQcApportionValue() != null)
										{
											totalAmount = Double.parseDouble(walletCardApportionDetailModel.getQcApportionValue());
										}
										
										LOG.debug("QC setProductAmount############"+totalAmount);
										splitMerchantInfoXMlDataQC.setProductAmount(totalAmount);
										merchantInfoList.add(splitMerchantInfoXMlDataQC);
									}
								}else{
									LOG.debug("XmltoFixo flag false setting ... split order information ");
									xmlToFico=false;
								}
								
								if(xmlToFico){
								LOG.info("Juspay MerchantInfoXMlData");
								MerchantInfoXMlData splitMerchantInfoXMlDataJuspay=new MerchantInfoXMlData();
								splitMerchantInfoXMlDataJuspay.setMerchantType(getConfigurationService().getConfiguration().getString(PAYMENT_JUSPAY_MERCHANT_TYPE));
								splitMerchantInfoXMlDataJuspay.setBucketId("");
								if (null != list && !list.isEmpty())
								{

									for (final PaymentTransactionModel oModel : list)
									{

										if (null != oModel.getStatus() && null != oModel.getPaymentProvider() &&  !oModel.getPaymentProvider().equalsIgnoreCase(CLIQ_CASH)
												&& oModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
										{
											if (MarketplacecommerceservicesConstants.MRUPEE_CODE.equalsIgnoreCase(oModel.getPaymentProvider()))
											{
												splitMerchantInfoXMlDataJuspay.setMerchantCode(getConfigurationService().getConfiguration()
														.getString(MarketplacecommerceservicesConstants.MRUPEE_MERCHANT_CODE));
											}
											else
											{
												splitMerchantInfoXMlDataJuspay.setMerchantCode(oModel.getPaymentProvider());
											}
											if (null != oModel.getCode())
											{
												payemntrefid = oModel.getCode();
												LOG.debug("Inside Parent order: Pyment Transaction model" + payemntrefid);
											}
										}

									}
								}

										splitMerchantInfoXMlDataJuspay.setPaymentRefID(payemntrefid);
										
										if (StringUtils.isNotEmpty(entry.getJuspayRequestId()))
										{
											splitMerchantInfoXMlDataJuspay.setReversePaymentRefId(entry.getJuspayRequestId());
										}
										
										double totalAmountJuspay=0;
										if (entry.getWalletApportionReturnInfo()!=null && entry.getWalletApportionReturnInfo().getQcApportionPartValue() != null)
										{
											totalAmountJuspay = Double.parseDouble(entry.getWalletApportionReturnInfo().getJuspayApportionValue());
										
										}
										LOG.info("Juspay split  MerchantInfoXMlData totalAmountJuspay"+totalAmountJuspay);
										splitMerchantInfoXMlDataJuspay.setProductAmount(totalAmountJuspay);
										double scheduleDelCharge = 0;
										if (entry.getWalletApportionReturnInfo()!=null && entry.getWalletApportionReturnInfo().getJuspaySchedulingValue() != null)
										{
											scheduleDelCharge = Double.parseDouble(entry.getWalletApportionReturnInfo().getJuspaySchedulingValue());
										}
										LOG.info("Juspay split  MerchantInfoXMlData scheduleDelCharge"+scheduleDelCharge);
										splitMerchantInfoXMlDataJuspay.setScheduleDelCharge(scheduleDelCharge);
										
										
										double shippingValue = 0;
										if (entry.getWalletApportionReturnInfo()!=null && entry.getWalletApportionReturnInfo().getJuspayShippingValue() != null)
										{
											shippingValue = Double.parseDouble(entry.getWalletApportionReturnInfo().getJuspayShippingValue());
										}
										splitMerchantInfoXMlDataJuspay.setExpressDelCharge(shippingValue);
										double juspayDelivery = 0;
										if (entry.getWalletApportionReturnInfo()!=null && entry.getWalletApportionReturnInfo().getJuspayDeliveryValue() != null)
										{
											juspayDelivery = Double.parseDouble(entry.getWalletApportionReturnInfo().getJuspayDeliveryValue());
										}
										splitMerchantInfoXMlDataJuspay.setShipmentCharge(juspayDelivery);
										
								      merchantInfoList.add(splitMerchantInfoXMlDataJuspay);
								}
									
								}
							
							   else if(StringUtils.isNotEmpty(chaildModel.getSplitModeInfo()) && CLIQ_CASH.equalsIgnoreCase(chaildModel.getSplitModeInfo()))
								{
									if(entry.getWalletApportionReturnInfo() !=null && entry.getWalletApportionReturnInfo().getWalletCardList() !=null   && entry.getWalletApportionReturnInfo().getStatusForQc().equalsIgnoreCase(SUCCESS)){
									LOG.info("QC merchantInfoXMlData");
									MerchantInfoXMlData merchantInfoXMlDataQC=new MerchantInfoXMlData();
									merchantInfoXMlDataQC
									.setMerchantType(getConfigurationService().getConfiguration().getString(PAYMENT_QC_MERCHANT_TYPE));
									merchantInfoXMlDataQC
									.setMerchantCode(getConfigurationService().getConfiguration().getString(PAYMENT_QC_MERCHANT_ID));
									if(entry.getWalletApportionReturnInfo()!=null && entry.getWalletApportionReturnInfo().getWalletCardList()!=null && StringUtils.isNotEmpty(entry.getWalletApportionReturnInfo().getStatus()) &&
											entry.getWalletApportionReturnInfo().getStatus().equalsIgnoreCase(SUCCESS) ){
										LOG.info("entry.getWalletApportionReturnInfo().getStatus().equalsIgnoreCase(SUCCESS)"+entry.getWalletApportionReturnInfo().getStatus());
										for(WalletCardApportionDetailModel walletCardApportionDetailQcData:entry.getWalletApportionReturnInfo().getWalletCardList()){
											LOG.info("walletCardApportionDetailQcData.getBucketType()"+walletCardApportionDetailQcData.getBucketType());
									merchantInfoXMlDataQC.setBucketId(walletCardApportionDetailQcData.getBucketType());
									String cardExpDate = getCardExpDate(walletCardApportionDetailQcData);
									merchantInfoXMlDataQC.setCardExpiryDate(cardExpDate);
									merchantInfoXMlDataQC.setCardNumber(walletCardApportionDetailQcData.getCardNumber());
									for (final PaymentTransactionModel oModel : list)
									{
										if (null != oModel.getStatus() && null != oModel.getPaymentProvider() && oModel.getPaymentProvider().equalsIgnoreCase(CLIQ_CASH)
												&& oModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
										{
											
											if (null != oModel.getCode())
											{
												payemntrefid = oModel.getCode();
											}
										}
									}
									   merchantInfoXMlDataQC.setPaymentRefID(payemntrefid);
										merchantInfoXMlDataQC.setReversePaymentRefId(payemntrefid);
										
										double qcDelivery = 0;
										if (walletCardApportionDetailQcData.getQcDeliveryValue() != null)
										{
											qcDelivery = Double.parseDouble(walletCardApportionDetailQcData.getQcDeliveryValue());
										}
										merchantInfoXMlDataQC.setShipmentCharge(qcDelivery);
										double scheduleDelCharge = 0;
										if (walletCardApportionDetailQcData.getQcSchedulingValue() != null)
										{
											scheduleDelCharge = Double.parseDouble(walletCardApportionDetailQcData.getQcSchedulingValue());
										}
										LOG.info("qc +++++scheduleDelCharge"+scheduleDelCharge);
										merchantInfoXMlDataQC.setScheduleDelCharge(scheduleDelCharge);
										double shippingValue = 0;
										if (entry.getWalletApportionReturnInfo().getWalletCardList().get(0).getQcShippingValue() != null)
										{
											shippingValue = Double.parseDouble(walletCardApportionDetailQcData.getQcShippingValue());
										}
										merchantInfoXMlDataQC.setExpressDelCharge(shippingValue);
										
										double qcJuspayProductData=0;
										if (walletCardApportionDetailQcData.getQcApportionValue() != null)
										{
											qcJuspayProductData = Double.parseDouble(walletCardApportionDetailQcData.getQcApportionValue());
										
										}
										LOG.info("qc +++++qcJuspayProductData"+qcJuspayProductData);
									  merchantInfoXMlDataQC.setProductAmount(qcJuspayProductData);
							
									}
									merchantInfoList.add(merchantInfoXMlDataQC);
									}
									
								 }else{
									 xmlToFico=false;
								 }
									
									
								}else{
									
									
									LOG.info("only Juspay cancel return order merchantInfoXMlData");
									MerchantInfoXMlData merchantInfoXMlDataJuspay=new MerchantInfoXMlData();
									merchantInfoXMlDataJuspay.setMerchantType(getConfigurationService().getConfiguration().getString(PAYMENT_JUSPAY_MERCHANT_TYPE));
									merchantInfoXMlDataJuspay.setBucketId("");
									if (null != list && !list.isEmpty())
									{
										for (final PaymentTransactionModel oModel : list)
										{
											LOG.debug("DeliveryMode oModel"+oModel);
											if (null != oModel.getStatus() && null != oModel.getPaymentProvider() && !oModel.getPaymentProvider().equalsIgnoreCase(CLIQ_CASH)
													&& oModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
											{
												
												LOG.debug("Inside Parent order: Pyment Transaction model");
												if (MarketplacecommerceservicesConstants.MRUPEE_CODE
														.equalsIgnoreCase(oModel.getPaymentProvider()))
												{
													merchantInfoXMlDataJuspay.setMerchantCode(getConfigurationService().getConfiguration()
															.getString(MarketplacecommerceservicesConstants.MRUPEE_MERCHANT_CODE));
												}
												else
												{
													merchantInfoXMlDataJuspay.setMerchantCode(oModel.getPaymentProvider());
												}
												if (null != oModel.getCode())
												{
													payemntrefid = oModel.getCode();
													LOG.debug("Inside Parent order: Pyment Transaction model" + payemntrefid);
												}
											}

										}
										merchantInfoXMlDataJuspay.setPaymentRefID(payemntrefid);	
										if (checkReturnCancelMap.containsKey(orderLineId))
										{
											if (null != entry.getTotalPrice() && xmlToFico)
											{
												if (entry.getNetAmountAfterAllDisc().doubleValue() > 0)
												{
													LOG.info("*****total price with discount*****" + entry.getNetAmountAfterAllDisc());
													merchantInfoXMlDataJuspay.setProductAmount(entry.getNetAmountAfterAllDisc().doubleValue());
												}
												else
												{
													LOG.info("total price call" + entry.getTotalPrice());
													merchantInfoXMlDataJuspay.setProductAmount(entry.getTotalPrice().doubleValue());
												}
												LOG.info("after price set");
											}
										}else{
											merchantInfoXMlDataJuspay.setProductAmount(0.0);
										}

										
										if (returnFlag)
										{
											merchantInfoXMlDataJuspay.setShipmentCharge(0.0);
											merchantInfoXMlDataJuspay.setExpressDelCharge(0.0);
										}
										
										if (cancelFlag)
										{
											LOG.info("Adding schedule delivery charges for orderLineId " + entry.getOrderLineId());
											if (null != entry.getScheduledDeliveryCharge() && entry.getScheduledDeliveryCharge().doubleValue() > 0.0D)
											{
												merchantInfoXMlDataJuspay.setScheduleDelCharge(entry.getScheduledDeliveryCharge().doubleValue());
											}
											else if (null != entry.getRefundedScheduleDeliveryChargeAmt()
													&& entry.getRefundedScheduleDeliveryChargeAmt().doubleValue() > 0.0D)
											{
												merchantInfoXMlDataJuspay.setScheduleDelCharge(entry.getRefundedScheduleDeliveryChargeAmt().doubleValue());
											}
										}
										
										
										if (StringUtils.isNotEmpty(entry.getJuspayRequestId()))
										{
											merchantInfoXMlDataJuspay.setReversePaymentRefId(entry.getJuspayRequestId());
										}
										
										if (null != entry.getMplDeliveryMode() && xmlToFico && cancelFlag)
										{
										LOG.info("inside del mode" + entry.getMplDeliveryMode());
										final MplZoneDeliveryModeValueModel zoneDelivery = entry.getMplDeliveryMode();
										if (null != zoneDelivery
												&& null != zoneDelivery.getDeliveryMode()
												&& entry.getCurrDelCharge() != null
												&& entry.getRefundedDeliveryChargeAmt() != null
												&& null != zoneDelivery.getDeliveryMode().getCode()
												&& zoneDelivery.getDeliveryMode().getCode()
														.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY))
										{
											if (entry.getCurrDelCharge().doubleValue() > 0)
											{
												merchantInfoXMlDataJuspay.setExpressDelCharge(entry.getCurrDelCharge().doubleValue());
											}
											else if (null != entry.getRefundedDeliveryChargeAmt()
													&& entry.getRefundedDeliveryChargeAmt().doubleValue() > 0.0D)
											{
												merchantInfoXMlDataJuspay.setExpressDelCharge(entry.getRefundedDeliveryChargeAmt().doubleValue());
											}
											LOG.info("set express del charge from curr del charge" + entry.getCurrDelCharge().doubleValue());// zoneDelivery.getValue().doubleValue()
										}
										else if (null != zoneDelivery
												&& null != zoneDelivery.getDeliveryMode()
												&& entry.getCurrDelCharge() != null
												&& entry.getRefundedDeliveryChargeAmt() != null
												&& null != zoneDelivery.getDeliveryMode().getCode()
												&& zoneDelivery.getDeliveryMode().getCode()
														.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
										{
											if (entry.getCurrDelCharge().doubleValue() > 0)
											{
												merchantInfoXMlDataJuspay.setShipmentCharge(entry.getCurrDelCharge().doubleValue());
											}
											else if (null != entry.getRefundedDeliveryChargeAmt()
													&& entry.getRefundedDeliveryChargeAmt().doubleValue() > 0.0D)
											{
												merchantInfoXMlDataJuspay.setShipmentCharge(entry.getRefundedDeliveryChargeAmt().doubleValue());
											}
											LOG.info("set del charge");
										}
										}

									}
									merchantInfoList.add(merchantInfoXMlDataJuspay);
								}
					
							LOG.info("setMerchantInfoList(merchantInfoList)"+merchantInfoList);
						xmlData.setMerchantInfoList(merchantInfoList);
						//add total amount
						double tAmount=0;
						tAmount = getTotalAmount(merchantInfoList, tAmount);
						xmlData.setAmount(tAmount);
						
						

						if (null != entry.getMplDeliveryMode() && xmlToFico && cancelFlag)
						{
							LOG.info("inside del mode" + entry.getMplDeliveryMode());
							final MplZoneDeliveryModeValueModel zoneDelivery = entry.getMplDeliveryMode();
							if (null != zoneDelivery
									&& null != zoneDelivery.getDeliveryMode()
									&& entry.getCurrDelCharge() != null
									&& entry.getRefundedDeliveryChargeAmt() != null
									&& null != zoneDelivery.getDeliveryMode().getCode()
									&& zoneDelivery.getDeliveryMode().getCode()
											.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY))
							{
								if (entry.getCurrDelCharge().doubleValue() > 0)
								{
									//xmlData.setExpressdeliveryCharge(entry.getCurrDelCharge().doubleValue());
								}
								else if (null != entry.getRefundedDeliveryChargeAmt()
										&& entry.getRefundedDeliveryChargeAmt().doubleValue() > 0.0D)
								{
									//xmlData.setExpressdeliveryCharge(entry.getRefundedDeliveryChargeAmt().doubleValue());
								}
								LOG.info("set express del charge from curr del charge" + entry.getCurrDelCharge().doubleValue());// zoneDelivery.getValue().doubleValue()
							}
							else if (null != zoneDelivery
									&& null != zoneDelivery.getDeliveryMode()
									&& entry.getCurrDelCharge() != null
									&& entry.getRefundedDeliveryChargeAmt() != null
									&& null != zoneDelivery.getDeliveryMode().getCode()
									&& zoneDelivery.getDeliveryMode().getCode()
											.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
							{
								if (entry.getCurrDelCharge().doubleValue() > 0)
								{
									//xmlData.setShipmentCharge(entry.getCurrDelCharge().doubleValue());
								}
								else if (null != entry.getRefundedDeliveryChargeAmt()
										&& entry.getRefundedDeliveryChargeAmt().doubleValue() > 0.0D)
								{
									//xmlData.setShipmentCharge(entry.getRefundedDeliveryChargeAmt().doubleValue());
								}
								LOG.info("set del charge");
							}
						}
						
						LOG.info("Cancel return data..........##order line id "+entry.getTransactionID());

						/* Added in R2.3 for sending EDTOHD / SDB charges to FICO START */
						try
						{
							if (sdbFlag)
							{
								final ChildOrderXMlData sdbXmlData = new ChildOrderXMlData();

								sdbXmlData.setDeliveryMode(xmlData.getDeliveryMode());
								sdbXmlData.setFulfillmentType(xmlData.getFulfillmentType());
								sdbXmlData.setItemNumber(xmlData.getItemNumber());
								//sdbXmlData.setPaymentRefID(xmlData.getPaymentRefID());
								sdbXmlData.setPrimaryCategory(xmlData.getPrimaryCategory());
								sdbXmlData.setSecondaryCategory(xmlData.getSecondaryCategory());
								sdbXmlData.setSellerCode(xmlData.getSellerCode());
								sdbXmlData.setUSSID(xmlData.getUSSID());
								sdbXmlData.setTransactionId(xmlData.getTransactionId());
								//sdbXmlData.setScheduleDelCharge(0.0D);
								//sdbXmlData.setExpressdeliveryCharge(0.0D);
								sdbXmlData.setOrderTag(SDB);
								if (null != entry.getRefundedScheduleDeliveryChargeAmt())
								{
									sdbXmlData.setAmount(entry.getRefundedScheduleDeliveryChargeAmt().doubleValue());
								}
								else if (null != entry.getScheduledDeliveryCharge())
								{
									sdbXmlData.setAmount(entry.getScheduledDeliveryCharge().doubleValue());
								}
								else
								{
									sdbXmlData.setAmount(0.0D);
								}

								if (null != entry.getScheduleChargesJuspayRequestId())
								{
									//sdbXmlData.setReversePaymentRefId(entry.getScheduleChargesJuspayRequestId());
								}
								LOG.info("Adding SDB data for transaction Id " + entry.getTransactionID());
								
								if(CollectionUtils.isNotEmpty(merchantInfoList)){
									List<MerchantInfoXMlData> sdbMerchantInfoList = new ArrayList<MerchantInfoXMlData>();
									for(MerchantInfoXMlData merchantData:merchantInfoList){									
										MerchantInfoXMlData sdbMerchantInfoXMlDatasdbXmlData =new MerchantInfoXMlData();
										sdbMerchantInfoXMlDatasdbXmlData.setMerchantCode(merchantData.getMerchantCode());
										sdbMerchantInfoXMlDatasdbXmlData.setMerchantType(merchantData.getMerchantType());
										sdbMerchantInfoXMlDatasdbXmlData.setPaymentRefID(merchantData.getPaymentRefID());
										sdbMerchantInfoXMlDatasdbXmlData.setReversePaymentRefId(entry.getScheduleChargesJuspayRequestId());
										sdbMerchantInfoXMlDatasdbXmlData.setExpressDelCharge(0.0D);
										sdbMerchantInfoXMlDatasdbXmlData.setScheduleDelCharge(0.0D);
										sdbMerchantInfoXMlDatasdbXmlData.setCardExpiryDate(merchantData.getCardExpiryDate());
										sdbMerchantInfoXMlDatasdbXmlData.setCardNumber(merchantData.getCardNumber());
										sdbMerchantInfoXMlDatasdbXmlData.setBucketId(merchantData.getBucketId());
										sdbMerchantInfoXMlDatasdbXmlData.setProductAmount(merchantData.getProductAmount());
										sdbMerchantInfoList.add(sdbMerchantInfoXMlDatasdbXmlData);
									}
									sdbXmlData.setMerchantInfoList(sdbMerchantInfoList);
								}
								childOrderDataList.add(sdbXmlData);
								entry.setIsSdbSendToFico(Boolean.TRUE);
								modelService.save(entry);
							}
							if (edToHdFlag)
							{
								final ChildOrderXMlData edToHdXmlData = new ChildOrderXMlData();

								edToHdXmlData.setDeliveryMode(xmlData.getDeliveryMode());
								edToHdXmlData.setFulfillmentType(xmlData.getFulfillmentType());
								edToHdXmlData.setItemNumber(xmlData.getItemNumber());
								//edToHdXmlData.setPaymentRefID(xmlData.getPaymentRefID());
								edToHdXmlData.setPrimaryCategory(xmlData.getPrimaryCategory());
								edToHdXmlData.setSecondaryCategory(xmlData.getSecondaryCategory());
								edToHdXmlData.setSellerCode(xmlData.getSellerCode());
								edToHdXmlData.setUSSID(xmlData.getUSSID());
								edToHdXmlData.setTransactionId(xmlData.getTransactionId());
								//edToHdXmlData.setScheduleDelCharge(0.0D);
								//edToHdXmlData.setExpressdeliveryCharge(0.0D);
								edToHdXmlData.setOrderTag(EDTOHD);
								if (null != entry.getRefundedDeliveryChargeAmt())
								{
									edToHdXmlData.setAmount(entry.getRefundedDeliveryChargeAmt().doubleValue());
								}
								else if (null != entry.getCurrDelCharge())
								{
									edToHdXmlData.setAmount(entry.getCurrDelCharge().doubleValue());
								}
								else
								{
									edToHdXmlData.setAmount(0.0D);
								}

								if (null != entry.getDelChargesJuspayRequestId())
								{
									//edToHdXmlData.setReversePaymentRefId(entry.getDelChargesJuspayRequestId());
								}
								LOG.info("Adding EdToHd data for transaction Id " + entry.getTransactionID());
								
								
								//EdToHdXmlData
								  if(CollectionUtils.isNotEmpty(merchantInfoList)){
									List<MerchantInfoXMlData> edToHdMerchantInfoList = new ArrayList<MerchantInfoXMlData>();
									for(MerchantInfoXMlData merchantData:merchantInfoList){									
										MerchantInfoXMlData edToHMerchantInfoXMlDatasdbXmlData =new MerchantInfoXMlData();
										edToHMerchantInfoXMlDatasdbXmlData.setMerchantCode(merchantData.getMerchantCode());
										edToHMerchantInfoXMlDatasdbXmlData.setMerchantType(merchantData.getMerchantType());
										edToHMerchantInfoXMlDatasdbXmlData.setPaymentRefID(merchantData.getPaymentRefID());
										edToHMerchantInfoXMlDatasdbXmlData.setCardExpiryDate(merchantData.getCardExpiryDate());
										edToHMerchantInfoXMlDatasdbXmlData.setCardNumber(merchantData.getCardNumber());
										edToHMerchantInfoXMlDatasdbXmlData.setBucketId(merchantData.getBucketId());
										edToHMerchantInfoXMlDatasdbXmlData.setReversePaymentRefId(entry.getDelChargesJuspayRequestId());
										edToHMerchantInfoXMlDatasdbXmlData.setProductAmount(merchantData.getProductAmount());
										edToHMerchantInfoXMlDatasdbXmlData.setExpressDelCharge(0.0D);
										edToHMerchantInfoXMlDatasdbXmlData.setScheduleDelCharge(0.0D);
										edToHdMerchantInfoList.add(edToHMerchantInfoXMlDatasdbXmlData);
									}
									edToHdXmlData.setMerchantInfoList(edToHdMerchantInfoList);
								}
								
								childOrderDataList.add(edToHdXmlData);
								entry.setIsEdToHdSendToFico(Boolean.TRUE);
								modelService.save(entry);
							}
						}
						catch (final Exception e)
						{
							LOG.error("Exception occcurred :" + e.getMessage());
						}
						/* Added in R2.3 for sending EDTOHD / SDB charges to FICO END */
						if (canOrRetflag)
						{
							LOG.info("Adding canOrRet data for transaction Id " + entry.getTransactionID());
							childOrderDataList.add(xmlData);
							entry.setIsSentToFico(Boolean.TRUE);
							modelService.save(entry);
						}
					}

				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			throw (e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw (e);
		}
		catch (final Exception e)
		{
			throw (e);
		}
		return childOrderDataList;

	}

	/**
	 * @param entry
	 * @return
	 */
	private boolean checkEdToHdFlag(final AbstractOrderEntryModel entry)
	{
		boolean isEdToHd = false;
		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Checking SDB for transaction ID " + entry.getTransactionID());
				LOG.debug("IsEdToHdSendToFico" + entry.getIsEdToHdSendToFico()
						+ LOG_MSG_AND_GET_REFUNDED_SCHEDULE_DELIVERY_CHARGE_AMT + entry.getRefundedEdChargeAmt());
			}
			final boolean isAmountRefunded = getAmountRefunded(entry,
					PaymentTransactionType.REFUND_EXPRESS_DELIVERY_CHARGES.getCode());
			if (isAmountRefunded && (null != entry && null != entry.getIsEDtoHD() && entry.getIsEDtoHD().booleanValue())
					&& (null == entry.getIsEdToHdSendToFico() || !entry.getIsEdToHdSendToFico().booleanValue())
					&& (null != entry.getRefundedEdChargeAmt() && entry.getRefundedEdChargeAmt().doubleValue() != 0.0D))
			{

				isEdToHd = true;
			}
			else
			{
				isEdToHd = false;
				return false;
			}
			if (LOG.isDebugEnabled())
			{
				LOG.debug("EdToHd flag for transaction Id :" + isEdToHd);
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception occurred while checking EdToHd Flag" + e.getMessage());
		}
		return isEdToHd;

	}

	/**
	 * @param entry
	 * @return
	 */
	private boolean checkSdbFlag(final AbstractOrderEntryModel entry)
	{
		boolean isSdb = false;
		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Checking SDB for transaction ID " + entry.getTransactionID());
				LOG.debug("IsSdb" + entry.getIsSdb() + "IsSdbSendToFico" + entry.getIsSdbSendToFico()
						+ LOG_MSG_AND_GET_REFUNDED_SCHEDULE_DELIVERY_CHARGE_AMT + entry.getRefundedScheduleDeliveryChargeAmt());
			}
			final boolean sdAmountRefunded = getAmountRefunded(entry,
					PaymentTransactionType.REFUND_SCHEDULE_DELIVERY_CHARGES.getCode());

			if (sdAmountRefunded
					&& (null == entry.getIsSdbSendToFico() || !entry.getIsSdbSendToFico().booleanValue())
					&& (null != entry.getRefundedScheduleDeliveryChargeAmt() && entry.getRefundedScheduleDeliveryChargeAmt()
							.doubleValue() != 0.0D))
			{
				isSdb = true;
				LOG.debug("SDB is true for transaction ID :" + entry.getTransactionID());
			}
			else
			{
				isSdb = false;
				LOG.debug("SDB is false for transaction ID :" + entry.getTransactionID());
				return false;
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception occurred while checking EdToHd Flag" + e.getMessage());
		}
		return isSdb;
	}

	/**
	 * @param entry
	 * @return
	 */
	private boolean getAmountRefunded(final AbstractOrderEntryModel entry, final String type)
	{
		final OrderModel order = (OrderModel) entry.getOrder();
		boolean isAmountRefunded = false;
		if (null != order.getPaymentTransactions())
		{
			final List<PaymentTransactionModel> list = order.getPaymentTransactions();
			if (null != list && !list.isEmpty())
			{
				for (final PaymentTransactionModel oModel : list)
				{
					for (final PaymentTransactionEntryModel paymentObj : oModel.getEntries())
					{
						if (type.equalsIgnoreCase(PaymentTransactionType.REFUND_EXPRESS_DELIVERY_CHARGES.getCode()))
						{
							if (null != paymentObj.getType() && null != paymentObj.getType().getCode()
									&& paymentObj.getType().getCode().equalsIgnoreCase(type))
							{
								if (null != paymentObj.getRequestId() && null != entry.getEdChargesJuspayRequestId())
								{
									if (paymentObj.getRequestId().equalsIgnoreCase(entry.getEdChargesJuspayRequestId()))
									{
										if (null != paymentObj.getTransactionStatus()
												&& paymentObj.getTransactionStatus().equalsIgnoreCase("SUCCESS"))
										{
											isAmountRefunded = true;
											return true;
										}
									}
								}
							}
						}
						else if (type.equalsIgnoreCase(PaymentTransactionType.REFUND_SCHEDULE_DELIVERY_CHARGES.getCode()))
						{
							if (null != paymentObj.getType() && null != paymentObj.getType().getCode()
									&& paymentObj.getType().getCode().equalsIgnoreCase(type))
							{
								if (null != paymentObj.getRequestId() && null != entry.getScheduleChargesJuspayRequestId())
								{
									if (paymentObj.getRequestId().equalsIgnoreCase(entry.getScheduleChargesJuspayRequestId()))
									{
										if (null != paymentObj.getTransactionStatus()
												&& paymentObj.getTransactionStatus().equalsIgnoreCase("SUCCESS"))
										{
											isAmountRefunded = true;
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return isAmountRefunded;
	}
	
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
			LOG.error("Exception occure while  gettting card ecpire date");
		}
		}
		return null;
	}

	private double getTotalAmount(List<MerchantInfoXMlData> merchantInfoList, double tAmount)
	{try{
		for(MerchantInfoXMlData merchantInfoXMlTotal:merchantInfoList){
			tAmount +=merchantInfoXMlTotal.getScheduleDelCharge()+merchantInfoXMlTotal.getShipmentCharge()+merchantInfoXMlTotal.getExpressDelCharge()+merchantInfoXMlTotal.getProductAmount();	
		}
		return tAmount;
	}catch(Exception exception){
		LOG.error("Error occure while sum of total amount"+exception.getMessage());
	}
		return tAmount;
	}
	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}

	protected SellerBasedPromotionService getSellerBasedPromotionService()
	{
		return Registry.getApplicationContext().getBean("sellerBasedPromotionService", SellerBasedPromotionService.class);
	}

	protected ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean("configurationService", ConfigurationService.class);
	}

	private void generatePartCanellOrderData(final List<SalesOrderXMLData> bulkSalesDataList)
	{

		BulkSalesOrderXMLData xmlData = null;
		String xmlString = MarketplacecommerceservicesConstants.EMPTYSPACE;
		boolean invalidXMLToFICO = false;

		try
		{
			xmlData = new BulkSalesOrderXMLData();
			if (null != bulkSalesDataList && !bulkSalesDataList.isEmpty())
			{
				xmlData.setOrderDataList(bulkSalesDataList);
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
					paymentInfoCancelService.paymentCancelRev(xmlString);
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
}
