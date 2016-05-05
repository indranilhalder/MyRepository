/**
 *
 */
package com.tisl.mpl.facades.order.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import de.hybris.platform.commercewebservicescommons.mapping.DataMapper;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.variants.model.VariantProductModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.data.MplPaymentInfoData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.data.AWBResponseData;
import com.tisl.mpl.facades.data.StatusRecordData;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.model.OrderStatusCodeMasterModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.order.facade.GetOrderDetailsFacade;
import com.tisl.mpl.util.GenericUtilityMethods;
import com.tisl.mpl.wsdto.OrderDataWsDTO;
import com.tisl.mpl.wsdto.OrderProductWsDTO;
import com.tisl.mpl.wsdto.Ordershipmentdetailstdto;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "PMD" })
public class DefaultGetOrderDetailsFacadeImpl implements GetOrderDetailsFacade
{
	private final static Logger LOG = Logger.getLogger(DefaultGetOrderDetailsFacadeImpl.class);
	@Autowired
	private MplCheckoutFacade mplCheckoutFacade;
	@Resource(name = "productService")
	private ProductService productService;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private MplOrderService mplOrderService;
	@Resource(name = "orderModelService")
	private OrderModelService orderModelService;
	@Autowired
	private MplSellerInformationService mplSellerInformationService;
	@Autowired
	private CancelReturnFacade cancelReturnFacade;
	
	@Resource(name = "mplDataMapper")
	protected DataMapper mplDataMapper;

	/**
	 * @description method is called to fetch the details of a particular orders for the user
	 * @param orderCode
	 * @return OrderTrackingWsDTO
	 */
	@Override
	public OrderDataWsDTO getOrderdetails(final String orderCode)
	{

		OrderDataWsDTO orderTrackingWsDTO = null;
		final List<OrderProductWsDTO> orderproductdtos = new ArrayList<OrderProductWsDTO>();
		List<Ordershipmentdetailstdto> ordershipmentdetailstdtos = null;
		OrderProductWsDTO orderproductdto = null;
		OrderModel orderModel = null;
		OrderData orderDetails = null;
		String isGiveAway = "N";
		String consignmentStatus = MarketplacecommerceservicesConstants.NA;
		ConsignmentModel consignmentModel = null;
		String formattedProductDate = MarketplacecommerceservicesConstants.EMPTY;
		String formattedActualProductDate = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			if (null != orderCode)
			{

				orderDetails = mplCheckoutFacade.getOrderDetailsForCode(orderCode);

				if (null != orderDetails && StringUtils.isNotEmpty(orderDetails.getType())
						&& orderDetails.getType().equalsIgnoreCase("Parent"))
				{
					orderTrackingWsDTO = new OrderDataWsDTO();
					//moved to generic utility
					orderTrackingWsDTO.setBillingAddress(GenericUtilityMethods.setAddress(orderDetails, 1));
					orderTrackingWsDTO.setDeliveryAddress(GenericUtilityMethods.setAddress(orderDetails, 2));
				   //add pickup person details
					orderTrackingWsDTO.setPickupPersonName(orderDetails.getPickupName());				
				    orderTrackingWsDTO.setPickupPersonMobile(orderDetails.getPickupPhoneNumber());
					
					if (null != orderDetails.getCreated())
					{
						orderTrackingWsDTO.setOrderDate(orderDetails.getCreated());
					}
					if (StringUtils.isNotEmpty(orderDetails.getCode()))
					{
						orderTrackingWsDTO.setOrderId(orderDetails.getCode());
					}
					if (StringUtils.isNotEmpty(orderDetails.getDeliveryAddress().getLastName())
							&& StringUtils.isNotEmpty(orderDetails.getDeliveryAddress().getFirstName()))
					{
						final String name = orderDetails.getDeliveryAddress().getFirstName().concat(" ")
								.concat(orderDetails.getDeliveryAddress().getLastName());
						orderTrackingWsDTO.setRecipientname(name);
					}

					if (null != orderDetails.getDeliveryCost() && StringUtils.isNotEmpty(orderDetails.getDeliveryCost().toString()))
					{
						orderTrackingWsDTO.setDeliveryCharge(orderDetails.getDeliveryCost().getValue().toString());
					}
					//TISST-13769
					//					if (null != orderDetails.getTotalPrice()
					//							&& StringUtils.isNotEmpty(orderDetails.getTotalPrice().getValue().toString()))
					//					{
					//						orderTrackingWsDTO.setTotalOrderAmount(orderDetails.getTotalPrice().getValue().toString());
					//					}
					if (null != orderDetails.getTotalPriceWithConvCharge()
							&& StringUtils.isNotEmpty(orderDetails.getTotalPriceWithConvCharge().getValue().toString()))
					{
						orderTrackingWsDTO.setTotalOrderAmount(orderDetails.getTotalPriceWithConvCharge().getValue().toString());
					}
					//TISEE-4660 starts
					if (null != orderDetails.getConvenienceChargeForCOD()
							&& StringUtils.isNotEmpty(orderDetails.getConvenienceChargeForCOD().getValue().toString()))
					{
						orderTrackingWsDTO.setConvenienceCharge(orderDetails.getConvenienceChargeForCOD().getValue().toString());
					}
					//TISEE-4660 ends
					if (null != orderDetails.getSubTotal() && StringUtils.isNotEmpty(orderDetails.getSubTotal().getValue().toString()))
					{
						orderTrackingWsDTO.setSubTotal(orderDetails.getSubTotal().getValue().toString());
					}
					//TISST-13769
					if (null != orderDetails.getTotalDiscounts()
							&& StringUtils.isNotEmpty(orderDetails.getTotalDiscounts().getValue().toString()))
					{
						orderTrackingWsDTO.setTotalDiscounts(orderDetails.getTotalDiscounts().getValue().toString());
					}
					final List<OrderData> subOrderList = orderDetails.getSellerOrderList();
					if (orderDetails.getSellerOrderList() != null && !orderDetails.getSellerOrderList().isEmpty())
					{
						for (final OrderData subOrder : subOrderList)
						{
							orderModel = orderModelService.getOrder(subOrder.getCode());
							for (final OrderEntryData entry : subOrder.getEntries())
							{
								List<String> parentTransactionIds = new ArrayList<>();
								orderproductdto = new OrderProductWsDTO();
								//seller order no
								orderproductdto.setSellerorderno(subOrder.getCode());
								
							if(null !=entry.getDeliveryPointOfService()){
								orderproductdto.setStoreDetails(mplDataMapper.map(entry.getDeliveryPointOfService(), PointOfServiceWsDTO.class, "DEFAULT"));
							}							
								final ProductData product = entry.getProduct();
								ordershipmentdetailstdtos = new ArrayList<Ordershipmentdetailstdto>();
								if (null != product)
								{
									final List<ImageData> images = (List<ImageData>) product.getImages();
									if (null != images)
									{

										for (final ImageData imageData : product.getImages())
										{
											if (imageData.getFormat().equalsIgnoreCase(MarketplacecommerceservicesConstants.THUMBNAIL))
											{
												orderproductdto.setImageURL(imageData.getUrl());
												break;
											}

										}
										if (null == orderproductdto.getImageURL())
										{
											orderproductdto.setImageURL(images.get(0).getUrl());
										}
									}
									if (StringUtils.isNotEmpty(entry.getAmountAfterAllDisc().toString()))
									{
										orderproductdto.setPrice(entry.getAmountAfterAllDisc().getValue().toString());
									}
									if (null != product.getBrand() && StringUtils.isNotEmpty(product.getBrand().toString()))
									{

										orderproductdto.setProductBrand(product.getBrand().getBrandname());
									}
									if (null != product.getCode() && StringUtils.isNotEmpty(product.getCode()))
									{

										orderproductdto.setProductcode(product.getCode());
									}
									if (StringUtils.isNotEmpty(product.getName()))
									{

										orderproductdto.setProductName(product.getName());
									}
									if (StringUtils.isNotEmpty(product.getSize()))
									{

										orderproductdto.setProductSize(product.getSize());
									}
									if (StringUtils.isNotEmpty(product.getVariantType()))
									{

										orderproductdto.setVariantOptions(product.getVariantType());
									}
									if (StringUtils.isNotEmpty(product.getColour()))
									{

										orderproductdto.setProductColour(product.getColour());
									}
									if (entry.isGiveAway())
									{
										isGiveAway = "Y";
									}
									else
									{
										isGiveAway = "N";
									}
									orderproductdto.setIsGiveAway(isGiveAway);

									//final List<OrderEntryData> associatedProducts = cancelReturnFacade.associatedEntriesData(orderModel,
									//			entry.getOrderLineId());

									if (null != entry.getAssociatedItems())
									{
										orderproductdto.setAssociatedProducts(entry.getAssociatedItems());
									}

									//final ProductModel productModel = productService.getProductForCode(entry.getProduct().getCode());
									final ProductModel productModel = mplOrderService.findProductsByCode(product.getCode());
									if (productModel instanceof PcmProductVariantModel)
									{
										final PcmProductVariantModel selectedVariantModel = (PcmProductVariantModel) productModel;
										final String selectedCapacity = selectedVariantModel.getCapacity();
										final ProductModel baseProduct = selectedVariantModel.getBaseProduct();
										if (null != baseProduct.getVariants() && null != selectedCapacity)
										{
											for (final VariantProductModel vm : baseProduct.getVariants())
											{
												final PcmProductVariantModel pm = (PcmProductVariantModel) vm;
												if (selectedCapacity.equals(pm.getCapacity()))
												{
													orderproductdto.setCapacity(pm.getCapacity());
												}

											}
										}
									}
									/* Fulfillment type */
									/*
									 * final List<RichAttributeModel> richAttributeModel = (List<RichAttributeModel>)
									 * productModel .getRichAttribute(); if (richAttributeModel != null &&
									 * richAttributeModel.get(0).getDeliveryFulfillModes() != null) { final String
									 * fullfillmentData = richAttributeModel.get(0).getDeliveryFulfillModes().getCode()
									 * .toUpperCase(); if (fullfillmentData != null && !fullfillmentData.isEmpty()) {
									 * orderproductdto.setFulfillment(fullfillmentData); } }
									 */
									/* Delivery date as per history */
									if (null != entry.getMplDeliveryMode())
									{
										if (null != entry.getMplDeliveryMode().getDescription()
												&& StringUtils.isNotEmpty(entry.getMplDeliveryMode().getDescription()))
										{
											orderproductdto.setDeliveryDate(entry.getMplDeliveryMode().getDescription());
										}
									}

									/*
									 * if (null != orderDetails.getSellerOrderList()) { for (final OrderData childOrder :
									 * orderDetails.getSellerOrderList()) { if (null != childOrder.getCode()) {
									 *
									 * orderproductdto.setSellerorderno(childOrder.getCode()); } }
									 *
									 * if (null != orderproductdto.getUSSID()) {
									 *
									 * orderproductdto.setSerialno(orderproductdto.getUSSID()); } else {
									 * orderproductdto.setSerialno(MarketplacecommerceservicesConstants.NA); }
									 *
									 * }
									 */


									SellerInformationModel sellerInfoModel = null;
									if (StringUtils.isNotEmpty(entry.getSelectedUssid()))
									{
										sellerInfoModel = getMplSellerInformationService().getSellerDetail(entry.getSelectedUssid());
									}
									if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null
											&& ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0)
													.getDeliveryFulfillModes() != null)
									{
										/* Fulfillment type */
										final String fulfillmentType = ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute())
												.get(0).getDeliveryFulfillModes().getCode();
										if (StringUtils.isNotEmpty(fulfillmentType))
										{
											orderproductdto.setFulfillment(fulfillmentType);
										}
										//Seller info
										if (sellerInfoModel.getUSSID() != null
												&& sellerInfoModel.getUSSID().equalsIgnoreCase(entry.getSelectedUssid()))
										{
											if (null != sellerInfoModel.getSellerID())
											{
												orderproductdto.setSellerID(sellerInfoModel.getSellerID());
											}
											else
											{
												orderproductdto.setSellerID(MarketplacecommerceservicesConstants.NA);
											}

											if (null != sellerInfoModel.getSellerName())
											{
												orderproductdto.setSellerName(sellerInfoModel.getSellerName());
											}
											else
											{
												orderproductdto.setSellerName(MarketplacecommerceservicesConstants.NA);
											}

											if (null != sellerInfoModel.getUSSID())
											{
												orderproductdto.setUSSID(sellerInfoModel.getUSSID());
												//orderproductdto.setSerialno(orderproductdto.getUSSID());
											}
											else
											{
												orderproductdto.setUSSID(MarketplacecommerceservicesConstants.NA);
												//orderproductdto.setSerialno(MarketplacecommerceservicesConstants.NA);
											}
											for (final RichAttributeModel rm : sellerInfoModel.getRichAttribute())
											{

												if (null == entry.getConsignment() && entry.getQuantity().doubleValue() != 0
														&& null != subOrder.getStatus() && null != rm.getCancellationWindow())
												{
													consignmentStatus = subOrder.getStatus().getCode();
													final Date sysDate = new Date();
													final int cancelWindow = GenericUtilityMethods
															.noOfDaysCalculatorBetweenDates(subOrder.getCreated(), sysDate);
													final int actualCancelWindow = Integer.parseInt(rm.getCancellationWindow());
													if (cancelWindow < actualCancelWindow
															&& checkOrderStatus(subOrder.getStatus().getCode(),
																	MarketplacecommerceservicesConstants.CANCEL_ORDER_STATUS).booleanValue()
															&& !entry.isGiveAway() && !entry.isIsBOGOapplied())
													{
														orderproductdto.setCancel(Boolean.TRUE);

													}
													else
													{
														orderproductdto.setCancel(Boolean.FALSE);
													}

												}
												else if (null != entry.getConsignment() && null != entry.getConsignment().getStatus()
														&& null != rm.getCancellationWindow())
												{
													consignmentStatus = entry.getConsignment().getStatus().getCode();
													final Date sysDate = new Date();
													final int cancelWindow = GenericUtilityMethods
															.noOfDaysCalculatorBetweenDates(subOrder.getCreated(), sysDate);
													final int actualCancelWindow = Integer.parseInt(rm.getCancellationWindow());
													if (cancelWindow < actualCancelWindow
															&& checkOrderStatus(consignmentStatus,
																	MarketplacecommerceservicesConstants.CANCEL_STATUS).booleanValue()
															&& !entry.isGiveAway() && !entry.isIsBOGOapplied())

													{
														orderproductdto.setCancel(Boolean.TRUE);

													}
													else
													{
														orderproductdto.setCancel(Boolean.FALSE);
													}
												}
												else
												{
													orderproductdto.setCancel(Boolean.FALSE);
												}


												if (null != rm.getExchangeAllowedWindow())
												{
													orderproductdto.setExchangePolicy(rm.getExchangeAllowedWindow());

												}
												/*
												 * if (null != sellerEntry.getReplacement()) {
												 * orderproductdto.setReplacement(sellerEntry.getReplacement());
												 *
												 * }
												 */
												//for return
												if (null != entry.getConsignment() && null != entry.getConsignment().getStatus()
														&& null != rm.getReturnWindow())
												{
													consignmentStatus = entry.getConsignment().getStatus().getCode();
													orderproductdto.setReturnPolicy(rm.getReturnWindow());

													consignmentModel = mplOrderService.fetchConsignment(entry.getConsignment().getCode());
													if (null != consignmentModel)
													{
														final Date sDate = new Date();
														final int returnWindow = GenericUtilityMethods
																.noOfDaysCalculatorBetweenDates(consignmentModel.getDeliveryDate(), sDate);
														final int actualReturnWindow = Integer.parseInt(rm.getReturnWindow());
														if (!entry.isGiveAway() && !entry.isIsBOGOapplied() && returnWindow < actualReturnWindow
																&& !checkOrderStatus(consignmentStatus,
																		MarketplacecommerceservicesConstants.VALID_RETURN).booleanValue()
																&& consignmentStatus
																		.equalsIgnoreCase(MarketplacecommerceservicesConstants.DELIVERED))

														{
															//orderproductdto.setReturnPolicy(sellerEntry.getReturnPolicy());
															orderproductdto.setIsReturned(Boolean.TRUE);
														}
														else
														{
															orderproductdto.setIsReturned(Boolean.FALSE);
														}
													}
													else
													{
														orderproductdto.setIsReturned(Boolean.FALSE);
													}
													//												if (null != consignmentModel.getTrackingID())
													//												{
													//													orderproductdto.setTrackingAWB(consignmentModel.getTrackingID());
													//												}
													//												if (null != consignmentModel.getReturnAWBNum())
													//												{
													//													orderproductdto.setReturnAWB(consignmentModel.getReturnAWBNum());
													//												}
												}
												else
												{
													orderproductdto.setIsReturned(Boolean.FALSE);
												}


											}
										}
									}
								}
								//set Serial no
								if (null != entry.getImeiDetails() && null != entry.getImeiDetails().getSerialNum())
								{
									orderproductdto.setSerialno(entry.getImeiDetails().getSerialNum());
								}
								else
								{
									orderproductdto.setSerialno(MarketplacecommerceservicesConstants.EMPTY);
								}
								//Set the transaction id
								if (entry.getTransactionId() != null)
								{
									orderproductdto.setTransactionId(entry.getTransactionId());
								}
								else
								{
									orderproductdto.setTransactionId(MarketplacecommerceservicesConstants.EMPTY);
								}
								//Setting parent transaction ID
								for (final AbstractOrderEntryModel orderEntry : orderModel.getEntries())
								{
									if (entry.getTransactionId().equalsIgnoreCase(orderEntry.getTransactionID())
											&& null != orderEntry.getParentTransactionID())
									{
										parentTransactionIds = Arrays.asList(orderEntry.getParentTransactionID().split("\\s*,\\s*"));
										break;
									}
								}
								orderproductdto.setParentTransactionId(parentTransactionIds);
								//Check if invoice is available
								if (entry.getConsignment() != null)
								{
									if (entry.getConsignment().getStatus() != null
											&& (entry.getConsignment().getStatus().equals(ConsignmentStatus.HOTC)
													|| entry.getConsignment().getStatus().equals(ConsignmentStatus.OUT_FOR_DELIVERY)
													|| entry.getConsignment().getStatus().equals(ConsignmentStatus.REACHED_NEAREST_HUB)
													|| entry.getConsignment().getStatus().equals(ConsignmentStatus.DELIVERED)))
									{
										orderproductdto.setIsInvoiceAvailable(Boolean.TRUE);
									}
									else
									{
										orderproductdto.setIsInvoiceAvailable(Boolean.FALSE);
									}
								}
								else
								{
									orderproductdto.setIsInvoiceAvailable(Boolean.FALSE);
								}
								//End
								//estimated delivery date
								if (null != consignmentModel)
								{
									formattedProductDate = GenericUtilityMethods.getFormattedDate(consignmentModel.getEstimatedDelivery());
									formattedActualProductDate = GenericUtilityMethods
											.getFormattedDate(consignmentModel.getDeliveryDate());
									if (!formattedProductDate.equalsIgnoreCase(MarketplacecommerceservicesConstants.EMPTY))
									{
										orderproductdto.setEstimateddeliverydate(formattedProductDate);
									}
									//delivery date
									if (!formattedActualProductDate.equalsIgnoreCase(MarketplacecommerceservicesConstants.EMPTY))
									{
										orderproductdto.setDeliveryDate(formattedActualProductDate);
									}
								}
								//moved to method
								setPaymentInfo(orderDetails, orderTrackingWsDTO); //TODO set payment Info

								if (null != orderDetails.getConsignments())
								{
									for (final ConsignmentData shipdetails : orderDetails.getConsignments())
									{
										final Ordershipmentdetailstdto ordershipmentdetailstdto = new Ordershipmentdetailstdto();
										if (null != shipdetails.getStatus() && StringUtils.isNotEmpty(shipdetails.getStatus().toString()))
										{
											ordershipmentdetailstdto.setStatus(shipdetails.getStatus().toString());
										}

										if (null != shipdetails.getStatusDate())
										{
											ordershipmentdetailstdto.setStatusDate(shipdetails.getStatusDate());
										}

										ordershipmentdetailstdtos.add(ordershipmentdetailstdto);
									}
									if (ordershipmentdetailstdtos.size() > 0)
									{
										orderproductdto.setShipmentdetails(ordershipmentdetailstdtos.get(0));
									}
									else
									{
										final Ordershipmentdetailstdto ordershipmentdetailstdto1 = new Ordershipmentdetailstdto();
										ordershipmentdetailstdto1.setStatus(MarketplacecommerceservicesConstants.NA);
										ordershipmentdetailstdto1.setStatusDate(new Date());
										ordershipmentdetailstdtos.add(ordershipmentdetailstdto1);
										orderproductdto.setShipmentdetails(ordershipmentdetailstdtos.get(0));
									}
								}
								orderproductdtos.add(orderproductdto);

							}
							orderTrackingWsDTO.setProducts(orderproductdtos);
						}
					}
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);

		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
		}
		return orderTrackingWsDTO;

	}

	/* Checking payment type and then setting payment info */
	protected void setPaymentInfo(final OrderData orderDetail, final OrderDataWsDTO orderWsDTO)
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
					orderWsDTO.setAccountHolderName(paymentInfo.getCardAccountHolderName());
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
					orderWsDTO.setAccountHolderName(paymentInfo.getCardAccountHolderName());
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
					orderWsDTO.setAccountHolderName(paymentInfo.getCardAccountHolderName());
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
					orderWsDTO.setAccountHolderName(paymentInfo.getCardAccountHolderName());
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
					orderWsDTO.setAccountHolderName(paymentInfo.getCardAccountHolderName());
				}

			}
			else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.WALLET))
			{
				if (StringUtils.isNotEmpty(paymentInfo.getCardAccountHolderName()))
				{
					orderWsDTO.setAccountHolderName(paymentInfo.getCardAccountHolderName());
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
			orderWsDTO.setAccountHolderName(MarketplacecommerceservicesConstants.NA);
		}
	}

	public static boolean isNumeric(final String str)
	{
		try
		{
			//final double isNumber = Double.parseDouble(str);
			//OG.debug("skipping OOTB user id" + isNumber);
			Integer.parseInt(str);
		}
		catch (final NumberFormatException nfe)
		{
			return false;
		}
		return true;
	}

	/**
	 * @description method is to set Thumbnail image on Product
	 * @param product
	 * @return String
	 */
	protected String setImageURL(final ProductData product)
	{
		String image = "";
		final List<ImageData> images = (List<ImageData>) product.getImages();
		if (null != images)
		{
			for (final ImageData imageData : product.getImages())
			{
				if (imageData.getFormat().equalsIgnoreCase(MarketplacecommerceservicesConstants.THUMBNAIL))
				{
					image = imageData.getUrl();
					break;
				}
			}
			if (image.equalsIgnoreCase(""))
			{
				image = images.get(0).getUrl();
			}
		}
		return image;
	}

	private AWBResponseData orderTrackingDetails(final OrderStatusCodeMasterModel orderStatusCode,
			final OrderHistoryEntryModel orderHistoryEntry, final OrderData subOrder)
	{
		final AWBResponseData trackingData = new AWBResponseData();
		List<StatusRecordData> statusRecords = new ArrayList<>();
		StatusRecordData statusRecord = new StatusRecordData();
		try
		{

			final SimpleDateFormat smdfDate = new SimpleDateFormat(MarketplacecclientservicesConstants.DATE_FORMAT_AWB);
			final SimpleDateFormat smdfTime = new SimpleDateFormat(MarketplacecclientservicesConstants.TIME_FORMAT_AWB);

			trackingData.setId(orderStatusCode.getId());
			trackingData.setIsEnabled(orderStatusCode.getEnable().booleanValue());
			trackingData.setIsSelected(orderStatusCode.getDisplay().booleanValue());

			trackingData.setShipmentStatus(orderStatusCode.getResponseStatus());
			trackingData.setResponseCode(orderStatusCode.getStatusCode());
			trackingData.setIsEnabled(orderStatusCode.getEnable().booleanValue());
			trackingData.setColorCode(orderStatusCode.getColorCode());
			trackingData.setDotId(orderStatusCode.getDotId());
			statusRecords = new ArrayList<>();
			statusRecord = new StatusRecordData();

			if (null != orderHistoryEntry)
			{
				statusRecord.setDate(smdfDate.format(orderHistoryEntry.getCreationtime()));
				statusRecord.setTime(smdfTime.format(orderHistoryEntry.getCreationtime()));
			}
			else
			{
				statusRecord.setDate(smdfDate.format(subOrder.getCreated()));
				statusRecord.setTime(smdfTime.format(subOrder.getCreated()));
			}
			statusRecord.setStatusDescription(orderStatusCode.getStatusMessage());
			statusRecords.add(statusRecord);
			trackingData.setStatusRecords(statusRecords);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return trackingData;
	}

	/*
	 * private AWBResponseData orderTrackingDetails(final OrderStatusCodeMasterModel orderStatusCode, final OrderData
	 * subOrder, final ConsignmentData consignment) { final AWBResponseData trackingData = new AWBResponseData();
	 * List<StatusRecordData> statusRecords = new ArrayList<>(); StatusRecordData statusRecord = new StatusRecordData();
	 * try {
	 *
	 * final SimpleDateFormat smdfDate = new SimpleDateFormat(MarketplacecclientservicesConstants.DATE_FORMAT_AWB); final
	 * SimpleDateFormat smdfTime = new SimpleDateFormat(MarketplacecclientservicesConstants.TIME_FORMAT_AWB);
	 *
	 * trackingData.setIsEnabled(orderStatusCode.getEnable().booleanValue());
	 * trackingData.setIsSelected(orderStatusCode.getDisplay().booleanValue());
	 *
	 * trackingData.setShipmentStatus(orderStatusCode.getResponseStatus());
	 * trackingData.setResponseCode(orderStatusCode.getStatusCode());
	 * trackingData.setIsEnabled(orderStatusCode.getEnable().booleanValue()); statusRecords = new ArrayList<>();
	 * statusRecord = new StatusRecordData();
	 *
	 * if (null != consignment) { statusRecord.setDate(smdfDate.format(consignment.getStatusDate()));
	 * statusRecord.setTime(smdfTime.format(consignment.getStatusDate())); } else {
	 * statusRecord.setDate(smdfDate.format(subOrder.getCreated()));
	 * statusRecord.setTime(smdfTime.format(subOrder.getCreated())); }
	 * statusRecord.setStatusDescription(orderStatusCode.getStatusMessage()); statusRecords.add(statusRecord);
	 * trackingData.setStatusRecords(statusRecords); } catch (final Exception e) { throw new
	 * EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000); }
	 *
	 * return trackingData; }
	 */


	/*
	 * @param orderCode
	 *
	 * @return
	 */
	/*
	 * @param orderCode
	 *
	 * @return
	 */
	@Override
	public Map<String, List<AWBResponseData>> getOrderStatusTrack(final OrderEntryData orderEntryDetail, final OrderData subOrder,
			final OrderData parentOrder)
	{

		final Map<String, List<AWBResponseData>> returnMap = new HashMap<String, List<AWBResponseData>>();

		List<AWBResponseData> approvalMapList = new ArrayList<AWBResponseData>();
		List<AWBResponseData> processingMapList = new ArrayList<AWBResponseData>();
		List<AWBResponseData> shippingMapList = new ArrayList<AWBResponseData>();
		List<AWBResponseData> cancelMapList = new ArrayList<AWBResponseData>();
		List<AWBResponseData> returnMapList = new ArrayList<AWBResponseData>();
		OrderStatusCodeMasterModel trackModel = null;

		final Map<String, AWBResponseData> awbApprovalMap = new LinkedHashMap<String, AWBResponseData>();
		final Map<String, AWBResponseData> awbProcessingMap = new LinkedHashMap<String, AWBResponseData>();
		final Map<String, AWBResponseData> awbShippingMap = new LinkedHashMap<String, AWBResponseData>();
		final Map<String, AWBResponseData> awbCancelMap = new LinkedHashMap<String, AWBResponseData>();
		final Map<String, AWBResponseData> awbReturnMap = new LinkedHashMap<String, AWBResponseData>();

		try
		{
			final OrderModel orderMod = orderModelService.getOrder(subOrder.getCode());
			final Map<String, OrderStatusCodeMasterModel> orderStatusCodeMap = orderModelService.getOrderStausCodeMasterList();

			if (orderMod.getHistoryEntries().size() > 0)
			{
				for (final OrderHistoryEntryModel orderHistoryEntry : orderMod.getHistoryEntries())
				{
					if (orderEntryDetail.getOrderLineId().equalsIgnoreCase(orderHistoryEntry.getLineId()))
					{
						//****************************** Approval Block
						trackModel = orderStatusCodeMap.get(MarketplaceFacadesConstants.APPROVED
								+ MarketplacecommerceservicesConstants.STRINGSEPARATOR + orderHistoryEntry.getDescription());
						if (null != trackModel && trackModel.getStage().equalsIgnoreCase(MarketplaceFacadesConstants.APPROVED)
								&& !isStatusAlradyExists(awbApprovalMap, trackModel) && trackModel.getDisplay().booleanValue())
						{
							awbApprovalMap.put(trackModel.getDotId().trim().toUpperCase(),
									orderTrackingDetails(trackModel, orderHistoryEntry, subOrder));
						}

						//****************************** PROCESSING Block
						trackModel = orderStatusCodeMap.get(MarketplaceFacadesConstants.PROCESSING
								+ MarketplacecommerceservicesConstants.STRINGSEPARATOR + orderHistoryEntry.getDescription());
						if (null != trackModel && trackModel.getStage().equalsIgnoreCase(MarketplaceFacadesConstants.PROCESSING)
								&& !isStatusAlradyExists(awbProcessingMap, trackModel) && trackModel.getDisplay().booleanValue())
						{
							awbProcessingMap.put(trackModel.getDotId().trim().toUpperCase(),
									orderTrackingDetails(trackModel, orderHistoryEntry, subOrder));
						}

						//****************************** SHIPPING Block
						trackModel = orderStatusCodeMap.get(MarketplaceFacadesConstants.SHIPPING
								+ MarketplacecommerceservicesConstants.STRINGSEPARATOR + orderHistoryEntry.getDescription());
						if (null != trackModel && trackModel.getStage().equalsIgnoreCase(MarketplaceFacadesConstants.SHIPPING)
								&& !isStatusAlradyExists(awbShippingMap, trackModel) && trackModel.getDisplay().booleanValue())
						{
							awbShippingMap.put(trackModel.getDotId().trim().toUpperCase(),
									orderTrackingDetails(trackModel, orderHistoryEntry, subOrder));
						}


						trackModel = orderStatusCodeMap.get(MarketplaceFacadesConstants.CANCEL
								+ MarketplacecommerceservicesConstants.STRINGSEPARATOR + orderHistoryEntry.getDescription());

						//****************************** CANCEL Block
						if (null != trackModel && trackModel.getStage().equalsIgnoreCase(MarketplaceFacadesConstants.CANCEL)
								&& !isStatusAlradyExists(awbCancelMap, trackModel) && trackModel.getDisplay().booleanValue())
						{
							if ((trackModel.getStatusCode().equalsIgnoreCase(ConsignmentStatus.REFUND_INITIATED.getCode())
									|| trackModel.getStatusCode().equalsIgnoreCase(ConsignmentStatus.REFUND_IN_PROGRESS.getCode())))
							{
								if (awbCancelMap.size() >= 1)
								{
									awbCancelMap.put(trackModel.getDotId(), orderTrackingDetails(trackModel, orderHistoryEntry, subOrder));
								}
							}
							else
							{
								awbCancelMap.put(trackModel.getDotId().trim().toUpperCase(),
										orderTrackingDetails(trackModel, orderHistoryEntry, subOrder));
							}
						}

						//****************************** RETURN Block
						trackModel = orderStatusCodeMap.get(MarketplaceFacadesConstants.RETURN
								+ MarketplacecommerceservicesConstants.STRINGSEPARATOR + orderHistoryEntry.getDescription());
						if (null != trackModel && trackModel.getStage().equalsIgnoreCase(MarketplaceFacadesConstants.RETURN)
								&& !isStatusAlradyExists(awbReturnMap, trackModel) && trackModel.getDisplay().booleanValue())
						{
							if ((trackModel.getStatusCode().equalsIgnoreCase(ConsignmentStatus.REFUND_INITIATED.getCode())
									|| trackModel.getStatusCode().equalsIgnoreCase(ConsignmentStatus.REFUND_IN_PROGRESS.getCode())))
							{
								if (awbReturnMap.size() >= 1)
								{
									awbReturnMap.put(trackModel.getDotId(), orderTrackingDetails(trackModel, orderHistoryEntry, subOrder));
								}
							}
							else
							{
								awbReturnMap.put(trackModel.getDotId().trim().toUpperCase(),
										orderTrackingDetails(trackModel, orderHistoryEntry, subOrder));
							}
						}
					}
				}
			}

			approvalMapList = getTrackOrderList(awbApprovalMap);
			processingMapList = getTrackOrderList(awbProcessingMap);
			shippingMapList = getTrackOrderList(awbShippingMap);
			cancelMapList = getTrackOrderList(awbCancelMap);
			returnMapList = getTrackOrderList(awbReturnMap);

			LOG.info("************************approvalMapList: " + approvalMapList.size());
			LOG.info("************************processingMapList: " + processingMapList.size());
			LOG.info("************************shippingMapList: " + shippingMapList.size());
			LOG.info("************************cancelMapList: " + cancelMapList.size());
			LOG.info("************************returnMapList: " + returnMapList.size());

			returnMap.put(MarketplaceFacadesConstants.APPROVED, approvalMapList);
			returnMap.put(MarketplaceFacadesConstants.PROCESSING, processingMapList);
			returnMap.put(MarketplaceFacadesConstants.SHIPPING, shippingMapList);
			returnMap.put(MarketplaceFacadesConstants.CANCEL, cancelMapList);
			returnMap.put(MarketplaceFacadesConstants.RETURN, returnMapList);
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, e);
			return returnMap;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, e);
			return returnMap;
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, e);
			return returnMap;
		}
		return returnMap;
	}

	private List<AWBResponseData> getTrackOrderList(final Map<String, AWBResponseData> awbMap)
	{
		final List<AWBResponseData> list = new ArrayList<AWBResponseData>();

		if (awbMap != null && awbMap.size() > 0)
		{
			for (final Map.Entry<String, AWBResponseData> entry : awbMap.entrySet())
			{
				list.add(entry.getValue());
			}
		}

		return list;
	}

	private boolean isStatusAlradyExists(final Map<String, AWBResponseData> awbExistingMap,
			final OrderStatusCodeMasterModel trackModel)
	{
		boolean alreadyExists = false;
		Long awbNumber = Long.valueOf(0);

		if (awbExistingMap != null && awbExistingMap.size() > 0 && trackModel != null)
		{
			for (final Map.Entry<String, AWBResponseData> entry : awbExistingMap.entrySet())
			{
				final AWBResponseData awbResponseData = entry.getValue();
				if (awbResponseData.getDotId().equalsIgnoreCase(trackModel.getDotId())
						&& Long.valueOf(awbResponseData.getId()).longValue() > awbNumber.longValue())
				{
					awbNumber = Long.valueOf(Long.parseLong(awbResponseData.getId()));
					alreadyExists = true;
				}
			}
			// Checking if the current code is latest or not .. If latest then only insert for the same status
			if (alreadyExists && Long.valueOf(trackModel.getId()).longValue() > awbNumber.longValue())
			{
				alreadyExists = false;
			}
		}
		return alreadyExists;
	}

	/**
	 * @description: To find the Cancellation is enabled/disabled on Order status
	 * @param: currentStatus
	 * @return: currentStatus
	 */
	public Boolean checkOrderStatus(final String currentStatus, final String status)
	{
		String cancelStatus = "";
		cancelStatus = configurationService.getConfiguration().getString(status);
		if (cancelStatus.indexOf(currentStatus) == -1)
		{
			return Boolean.FALSE;
		}
		else
		{
			return Boolean.TRUE;
		}


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

}
