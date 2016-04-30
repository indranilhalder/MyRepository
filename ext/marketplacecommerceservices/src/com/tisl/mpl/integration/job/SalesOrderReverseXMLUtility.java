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
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.pojo.BulkSalesOrderXMLData;
import com.tisl.mpl.pojo.ChildOrderXMlData;
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
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SalesOrderReverseXMLUtility.class.getName());
	@Autowired
	private PaymentInfoCancelReversalImpl paymentInfoCancelService;
	/*
	 * @Autowired private MplSellerInformationService mplSellerInformationService;
	 */

	private String payemntrefid = null;
	private boolean xmlToFico = true;
	private final String RET = MarketplacecommerceservicesConstants.RETURN_FLAG;
	private final String CAN = MarketplacecommerceservicesConstants.CANCEL_FLAG;

	//static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

	/**
	 * @Description: Generate XML Data for Order
	 * @param orderData
	 */
	public String generateCanellOrderData(final List<OrderModel> orderData)
	{

		BulkSalesOrderXMLData xmlData = null;
		List<SalesOrderXMLData> bulkSalesDataList = null;
		String xmlString = MarketplacecommerceservicesConstants.EMPTYSPACE;
		boolean invalidXMLToFICO = false;

		try
		{
			if (null != orderData && !orderData.isEmpty())
			{
				xmlData = new BulkSalesOrderXMLData();
				bulkSalesDataList = getParentOrderData(orderData);
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

		return xmlString;
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
				//	if (checkOrderHistoryEntryDate(orderHistoryEntryModel.getCreationtime().toString()))

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
										salesXMLData.setMerchantCode(oModel.getPaymentProvider());
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
							subOrderDataList = getSubOrderData(order.getParentReference().getChildOrders());

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
	private List<SubOrderXMLData> getSubOrderData(final List<OrderModel> childOrders)
	{
		final List<SubOrderXMLData> subOrderDataList = new ArrayList<SubOrderXMLData>();
		List<ChildOrderXMlData> childOrderDataList = new ArrayList<ChildOrderXMlData>();
		try
		{
			for (final OrderModel order : childOrders)
			{
				final Map checkReturnCancelMap = checkCanelReturn(order);
				//if (checkReturnCancelMap != null && checkReturnCancelMap.size() > 0)
				if (MapUtils.isNotEmpty(checkReturnCancelMap))
				{
					/*
					 * if (null != order.getPaymentTransactions()) { final List<PaymentTransactionModel> list =
					 * order.getPaymentTransactions(); if (null != list && !list.isEmpty()) { for (final
					 * PaymentTransactionModel payTransModel : list) { final List<PaymentTransactionEntryModel>
					 * paymentTransactionEntryList = payTransModel.getEntries(); if (null != payTransModel.getCode() && null
					 * != payTransModel.getStatus() && CollectionUtils.isNotEmpty(paymentTransactionEntryList) &&
					 * payTransModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS)) { for (final
					 * PaymentTransactionEntryModel ptModel : paymentTransactionEntryList) { if (null != ptModel.getType() &&
					 * ptModel.getType().equals(PaymentTransactionType.CANCEL) ||
					 * ptModel.getType().equals(PaymentTransactionType.REFUND_DELIVERY_CHARGES) ||
					 * ptModel.getType().equals(PaymentTransactionType.MANUAL_REFUND) ||
					 * ptModel.getType().equals(PaymentTransactionType.RETURN)) { reversepayemntrefid =
					 * payTransModel.getCode(); LOG.info(reversepayemntrefid); LOG.debug(ptModel.getType()); break; } }
					 *
					 * } } } }
					 */
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

						childOrderDataList = getChildOrderDataForXML(order.getEntries(), checkReturnCancelMap);

					}

					if (null != childOrderDataList && !childOrderDataList.isEmpty() && xmlToFico)
					{
						LOG.debug("before child order list set");
						xmlData.setTransactionInfoList(childOrderDataList);
						LOG.debug("child order list set");
					}

					subOrderDataList.add(xmlData);
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
		return subOrderDataList;
	}

	/**
	 * @param entry
	 */
	private List<ChildOrderXMlData> getChildOrderDataForXML(final List<AbstractOrderEntryModel> entries,
			final Map checkReturnCancelMap)
	{
		final List<ChildOrderXMlData> childOrderDataList = new ArrayList<ChildOrderXMlData>();
		List<String> categoryList = new ArrayList<String>();
		try
		{
			if (null != entries && !entries.isEmpty())
			{
				for (final AbstractOrderEntryModel entry : entries)
				{

					boolean canOrRetflag = false; //flag for checking if order line is cancelled or returned. If flag is false the order line will not be set in the XML
					boolean returnFlag = false;
					boolean cancelFlag = false;
					if (null != entry && null != entry.getProduct())
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
							xmlData.setPaymentRefID(payemntrefid);
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
						}
						else
						{
							xmlToFico = false;
						}


						if (null != entry.getSelectedUSSID() && xmlToFico)
						{
							List<SellerInformationModel> productSellerData = null;
							xmlData.setUSSID(entry.getSelectedUSSID());
							productSellerData = getSellerBasedPromotionService().fetchSellerInformation(entry.getSelectedUSSID(),
									getDefaultPromotionsManager().catalogData());
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
						if (null != entry.getTotalPrice() && xmlToFico)
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


						//TISPRD-901
						if (StringUtils.isNotEmpty(entry.getFulfillmentType()) && xmlToFico)
						{
							xmlData.setFulfillmentType(entry.getFulfillmentType().toUpperCase());
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
						LOG.debug(">>>>>>> before prodcatlist");
						final List<CategoryModel> productCategoryList = getDefaultPromotionsManager().getPrimarycategoryData(product);

						//if (null != productCategoryList && productCategoryList.size() > 0)
						if (CollectionUtils.isNotEmpty(productCategoryList))
						{

							categoryList = new ArrayList<>();
							for (final CategoryModel category : productCategoryList)
							{

								if (!(category instanceof ClassificationClassModel))
								{
									LOG.debug("Category Data:>>>>>>>>" + category.getName() + "Code>>>" + category.getCode());
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
									LOG.debug("primary cat" + categoryList.get(categoryList.size() - 2));
									xmlData.setSecondaryCategory(categoryList.get(categoryList.size() - 3));
									LOG.debug("secondary cat" + categoryList.get(categoryList.size() - 3));
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

						final String orderLineId = entry.getOrderLineId() != null ? entry.getOrderLineId() : entry.getTransactionID();



						if (checkReturnCancelMap.containsKey(orderLineId))
						{
							final Map<String, String> tagMap = (Map<String, String>) checkReturnCancelMap.get(orderLineId);

							if (tagMap.containsKey(RET))
							{
								xmlData.setOrderTag(RET);
								xmlData.setReturnDate(tagMap.get(RET).toString());
								returnFlag = true;
								LOG.debug(tagMap.get(RET).toString());

							}
							else if (tagMap.containsKey(CAN))
							{
								xmlData.setOrderTag(CAN);
								xmlData.setCancelDate(tagMap.get(CAN).toString());
								cancelFlag = true;
								LOG.debug(tagMap.get(CAN).toString());
							}

							canOrRetflag = true;
						}

						if (StringUtils.isNotEmpty(entry.getJuspayRequestId()))
						{
							xmlData.setReversePaymentRefId(entry.getJuspayRequestId());
						}
						/*
						 * if (xmlData.getOrderTag().equals("NOR")) { xmlData.setReversePaymentRefId(" "); }
						 */
						if (returnFlag)
						{
							// For Return - refund delivery cost will be 0
							xmlData.setShipmentCharge(0.0);
							xmlData.setExpressdeliveryCharge(0.0);
						}
						if (null != entry.getMplDeliveryMode() && xmlToFico && cancelFlag)
						{
							LOG.debug("inside del mode" + entry.getMplDeliveryMode());
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
									xmlData.setExpressdeliveryCharge(entry.getCurrDelCharge().doubleValue());
								}
								else
								{
									xmlData.setExpressdeliveryCharge(entry.getRefundedDeliveryChargeAmt().doubleValue());
								}
								LOG.debug("set express del charge from curr del charge" + entry.getCurrDelCharge().doubleValue());// zoneDelivery.getValue().doubleValue()
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
									xmlData.setShipmentCharge(entry.getCurrDelCharge().doubleValue());
								}
								else
								{
									xmlData.setShipmentCharge(entry.getRefundedDeliveryChargeAmt().doubleValue());
								}
								LOG.debug("set del charge");
							}
						}

						if (canOrRetflag)
						{
							childOrderDataList.add(xmlData);
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

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}

	protected SellerBasedPromotionService getSellerBasedPromotionService()
	{
		return Registry.getApplicationContext().getBean("sellerBasedPromotionService", SellerBasedPromotionService.class);
	}

}
