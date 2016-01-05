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
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.pojo.BulkSalesOrderXMLData;
import com.tisl.mpl.pojo.ChildOrderXMlData;
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
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SalesOrderXMLUtility.class.getName());
	@Autowired
	private PaymentInfoRevWebServiceImpl paymentInfoRevWebService;
	private String payemntrefid = null;
	private boolean xmlToFico = true;

	@Autowired
	private MplSellerInformationService mplSellerInformationService;

	/**
	 * @Description: Generate XML Data for Order
	 * @param orderData
	 */
	public String generateOrderData(final List<OrderModel> orderData)
	{


		final BulkSalesOrderXMLData xmlData = new BulkSalesOrderXMLData();
		List<SalesOrderXMLData> bulkSalesDataList = new ArrayList<SalesOrderXMLData>();
		String xmlString = MarketplacecommerceservicesConstants.EMPTYSPACE;

		try
		{
			if (null != orderData && !orderData.isEmpty())
			{

				bulkSalesDataList = getParentOrderData(orderData);
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
					LOG.debug(xmlString);
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

		return xmlString;
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
						if (null != paymentObj.getPaymentMode() && null != paymentObj.getPaymentMode().getMode()
								&& paymentObj.getPaymentMode().getMode().equalsIgnoreCase("COD"))
						{
							isCOD = true;
							LOG.debug("After check cod");
						}

					}
				}
			}
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
		final List<SalesOrderXMLData> bulkSalesDataList = new ArrayList<SalesOrderXMLData>();

		for (final OrderModel order : listOrderData)
		{
			//checkCanellation(orderData);
			xmlToFico = true;

			if (!checkCOD(order))
			{
				final SalesOrderXMLData salesXMLData = new SalesOrderXMLData();
				LOG.debug("COD Check done");
				if (null != order.getCode() && xmlToFico)
				{
					salesXMLData.setOrderId(order.getCode());
					LOG.debug("order id" + order.getCode());
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
					final List<PaymentTransactionModel> list = order.getPaymentTransactions();
					if (null != list && !list.isEmpty())
					{
						for (final PaymentTransactionModel oModel : list)
						{

							if (null != oModel.getStatus() && null != oModel.getPaymentProvider()
									&& oModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
							{
								LOG.debug("Inside Parent order: Pyment Transaction model");
								salesXMLData.setMerchantCode(oModel.getPaymentProvider());
								if (null != oModel.getCode())
								{
									payemntrefid = oModel.getCode();
									LOG.debug("Inside Parent order: Pyment Transaction model" + payemntrefid);
								}
								else
								{
									xmlToFico = false;
								}

							}

						}
					}

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
				bulkSalesDataList.add(salesXMLData);
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
						childOrderDataList = getChildOrderDataForXML(order.getEntries());

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
	private List<ChildOrderXMlData> getChildOrderDataForXML(final List<AbstractOrderEntryModel> entries)
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
					if (null != payemntrefid && xmlToFico)
					{
						xmlData.setPaymentRefID(payemntrefid);
					}
					else
					{
						xmlToFico = false;
					}

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

					if (xmlToFico)
					{
						final String ussId = entry.getSelectedUSSID();

						final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(ussId);
						if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null
								&& ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0) != null
								&& ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0)
										.getDeliveryFulfillModes() != null)
						{
							final String fulfillmentType = ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0)
									.getDeliveryFulfillModes().getCode();
							LOG.debug("inside rich attribute model" + fulfillmentType.toUpperCase() + " for ussid :" + ussId);
							xmlData.setFulfillmentType(fulfillmentType.toUpperCase());
						}
					}

					//	if (null != product.getSellerInformationRelator() && xmlToFico)
					//					{
					//						final Collection<SellerInformationModel> sellerinfolist = product.getSellerInformationRelator();
					//						if (sellerinfolist.size() > 0)
					//						{
					//							for (final SellerInformationModel sellerEntry : sellerinfolist)
					//							{
					//								if (sellerEntry.getRichAttribute().size() > 0)
					//								{
					//									for (final RichAttributeModel richEntry : sellerEntry.getRichAttribute())
					//									{
					//										LOG.debug("inside rich attribute model" + richEntry.getDeliveryFulfillModes());
					//										xmlData.setFulfillmentType(richEntry.getDeliveryFulfillModes().toString().toUpperCase());
					//										LOG.debug("set fulfilment mode");
					//									}
					//								}
					//							}
					//						}
					//					}
					//
					if (null != entry.getMplDeliveryMode() && xmlToFico)
					{
						LOG.debug("inside del mode" + entry.getMplDeliveryMode());
						final MplZoneDeliveryModeValueModel zoneDelivery = entry.getMplDeliveryMode();
						if (null != zoneDelivery && null != zoneDelivery.getDeliveryMode() && entry.getCurrDelCharge() != null
								&& entry.getRefundedDeliveryChargeAmt() != null && null != zoneDelivery.getDeliveryMode().getCode()
								&& zoneDelivery.getDeliveryMode().getCode().equalsIgnoreCase("express-delivery"))
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
						else if (null != zoneDelivery && null != zoneDelivery.getDeliveryMode() && entry.getCurrDelCharge() != null
								&& entry.getRefundedDeliveryChargeAmt() != null && null != zoneDelivery.getDeliveryMode().getCode()
								&& zoneDelivery.getDeliveryMode().getCode().equalsIgnoreCase("home-delivery"))
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

}
