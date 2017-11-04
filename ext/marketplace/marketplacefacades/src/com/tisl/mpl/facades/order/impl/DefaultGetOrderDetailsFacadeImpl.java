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
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.strategies.CheckoutCustomerStrategy;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import de.hybris.platform.commercewebservicescommons.mapping.DataMapper;
import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.variants.model.VariantProductModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tis.mpl.facade.changedelivery.MplDeliveryAddressFacade;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.data.MplPaymentInfoData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.account.register.impl.DefaultMplOrderFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.data.AWBResponseData;
import com.tisl.mpl.facades.data.StatusRecordData;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.model.OrderStatusCodeMasterModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.order.facade.GetOrderDetailsFacade;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;
import com.tisl.mpl.wsdto.OrderDataWsDTO;
import com.tisl.mpl.wsdto.OrderProductWsDTO;
import com.tisl.mpl.wsdto.OrderTrackingWsDTO;
import com.tisl.mpl.wsdto.Ordershipmentdetailstdto;
import com.tisl.mpl.wsdto.SelectedDeliveryModeWsDTO;
import com.tisl.mpl.wsdto.StatusResponseDTO;
import com.tisl.mpl.wsdto.StatusResponseListDTO;
import com.tisl.mpl.wsdto.StatusResponseMessageDTO;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "PMD" })
public class DefaultGetOrderDetailsFacadeImpl implements GetOrderDetailsFacade
{
	private final static Logger LOG = Logger.getLogger(DefaultGetOrderDetailsFacadeImpl.class);
	@Resource
	private MplCheckoutFacade mplCheckoutFacade;
	@Resource(name = "productService")
	private ProductService productService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource
	private ConfigurationService configurationService;
	@Resource
	private MplOrderService mplOrderService;
	@Resource(name = "orderModelService")
	private OrderModelService orderModelService;
	@Resource
	private MplSellerInformationService mplSellerInformationService;
	@Resource
	private CancelReturnFacade cancelReturnFacade;

	@Resource
	private MplOrderFacade mplOrderFacade;

	@Resource(name = "mplDataMapper")
	protected DataMapper mplDataMapper;

	@Autowired
	private BaseStoreService baseStoreService;
	@Autowired
	private CheckoutCustomerStrategy checkoutCustomerStrategy;
	@Autowired
	private CustomerAccountService customerAccountService;

	@Autowired
	private DefaultMplOrderFacade defaultmplOrderFacade;
	@Autowired
	private MplDeliveryAddressFacade mplDeliveryAddressFacade;

	@Resource(name = "mplJewelleryService")
	private MplJewelleryService jewelleryService;


	
	//TPR-5975 starts here
	public static final String ZERODELCOST = "0.0".intern();//IQA

	//TPR-5975 ends here

	
	/**
	 * @description method is called to fetch the details of a particular orders for the user
	 * @param orderDetails
	 * @return OrderTrackingWsDTO
	 */
	@Override
	public OrderDataWsDTO getOrderdetails(final OrderData orderDetails)
	{

		OrderDataWsDTO orderTrackingWsDTO = null;
		final List<OrderProductWsDTO> orderproductdtos = new ArrayList<OrderProductWsDTO>();
		List<Ordershipmentdetailstdto> ordershipmentdetailstdtos = null;
		OrderProductWsDTO orderproductdto = null;
		OrderModel orderModel = null;
		String isGiveAway = "N";
		String consignmentStatus = MarketplacecommerceservicesConstants.NA;
		ConsignmentModel consignmentModel = null;
		String formattedProductDate = MarketplacecommerceservicesConstants.EMPTY;
		String formattedActualProductDate = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			if (null != orderDetails && StringUtils.isNotEmpty(orderDetails.getType())
					&& orderDetails.getType().equalsIgnoreCase("Parent"))
			{
				orderTrackingWsDTO = new OrderDataWsDTO();
				//moved to generic utility
				orderTrackingWsDTO.setBillingAddress(GenericUtilityMethods.setAddress(orderDetails, 1));
				orderTrackingWsDTO.setDeliveryAddress(GenericUtilityMethods.setAddress(orderDetails, 2));
				//add pickup person details
				
				/*Added For EGV Functionality Start*/ 
				if (StringUtils.isNotEmpty(orderDetails.getEgvCardNumber()))
				{
					orderTrackingWsDTO.setEgvCartNumber(orderDetails.getEgvCardNumber());
				}
				if (StringUtils.isNotEmpty(orderDetails.getEgvCardExpDate()))
				{
					orderTrackingWsDTO.setCartExpiryDate(orderDetails.getEgvCardExpDate());
				}
				if (orderDetails.isResendEgvMailAvailable())
				{
					orderTrackingWsDTO.setResendAvailable(true);
				}
				
				if (orderDetails.isIsEGVOrder())
				{
					orderTrackingWsDTO.setIsEgvOrder(true);
				}
				
				/*Added For EGV Functionality End*/ 
				if (StringUtils.isNotEmpty(orderDetails.getPickupName()))
				{
					orderTrackingWsDTO.setPickupPersonName(orderDetails.getPickupName());
				}
				if (StringUtils.isNotEmpty(orderDetails.getPickupPhoneNumber()))
				{
					orderTrackingWsDTO.setPickupPersonMobile(orderDetails.getPickupPhoneNumber());
				}
				if (null != orderDetails.getCreated())
				{
					orderTrackingWsDTO.setOrderDate(orderDetails.getCreated());
				}
				if (StringUtils.isNotEmpty(orderDetails.getCode()))
				{
					orderTrackingWsDTO.setOrderId(orderDetails.getCode());
				}
				if (null != orderDetails.getDeliveryAddress()
						&& StringUtils.isNotEmpty(orderDetails.getDeliveryAddress().getLastName())
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

				//TPR-6117 exchange field added
				//				if (CollectionUtils.isNotEmpty(orderDetails.getEntries()))
				//				{
				//					for (final OrderEntryData entry : orderDetails.getEntries())
				//					{
				//						if (StringUtils.isNotEmpty(entry.getExchangeApplied()))
				//						{
				//							orderTrackingWsDTO.setExchangeId(entry.getExchangeApplied());
				//						}
				//					}
				//				}

				if (CollectionUtils.isNotEmpty(orderDetails.getSellerOrderList()))
				{
					for (final OrderData subOrder : orderDetails.getSellerOrderList())
					{
						orderModel = orderModelService.getOrder(subOrder.getCode());
						for (final OrderEntryData entry : subOrder.getEntries())
						{
							List<String> parentTransactionIds = new ArrayList<>();
							orderproductdto = new OrderProductWsDTO();
							//seller order no
							orderproductdto.setSellerorderno(subOrder.getCode());

							if (null != entry.getDeliveryPointOfService())
							{
								orderproductdto.setStoreDetails(mplDataMapper.map(entry.getDeliveryPointOfService(),
										PointOfServiceWsDTO.class, "DEFAULT"));
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

								//TISJEW-3519 && TPR-1083 && TPR-6117
								if (StringUtils.isNotEmpty(entry.getExchangeApplied()))
								{
									orderproductdto.setExchangeId(entry.getExchangeApplied());
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
								 * final List<RichAttributeModel> richAttributeModel = (List<RichAttributeModel>) productModel
								 * .getRichAttribute(); if (richAttributeModel != null &&
								 * richAttributeModel.get(0).getDeliveryFulfillModes() != null) { final String fullfillmentData
								 * = richAttributeModel.get(0).getDeliveryFulfillModes().getCode() .toUpperCase(); if
								 * (fullfillmentData != null && !fullfillmentData.isEmpty()) {
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
								String ussid = "";
								if (StringUtils.isNotEmpty(entry.getSelectedUssid()))
								{
									//sellerInfoModel = getMplSellerInformationService().getSellerDetail(entry.getSelectedUssid());
									if (((MarketplacecommerceservicesConstants.FINEJEWELLERY)).equalsIgnoreCase(productModel
											.getProductCategoryType()))
									{
										final List<JewelleryInformationModel> jewelleryInfo = jewelleryService
												.getJewelleryInfoByUssid(entry.getSelectedUssid());
										if (CollectionUtils.isNotEmpty(jewelleryInfo))
										{
											sellerInfoModel = getMplSellerInformationService().getSellerDetail(
													jewelleryInfo.get(0).getPCMUSSID());
											ussid = jewelleryInfo.get(0).getUSSID();
										}
										else
										{
											LOG.error("No entry in JewelleryInformationModel for ussid " + entry.getSelectedUssid());
										}
									}
									else
									{
										sellerInfoModel = getMplSellerInformationService().getSellerDetail(entry.getSelectedUssid());
										ussid = sellerInfoModel.getUSSID();
									}
								}
								if (sellerInfoModel != null
										&& sellerInfoModel.getRichAttribute() != null
										&& ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0).getDeliveryFulfillModes() != null)
								{
									/* Fulfillment type */
									final String fulfillmentType = ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0)
											.getDeliveryFulfillModes().getCode();
									if (StringUtils.isNotEmpty(fulfillmentType))
									{
										orderproductdto.setFulfillment(fulfillmentType);
									}
									//Seller info
									/*
									 * if (sellerInfoModel.getUSSID() != null &&
									 * sellerInfoModel.getUSSID().equalsIgnoreCase(entry.getSelectedUssid()))
									 */
									if (sellerInfoModel.getUSSID() != null && entry.getSelectedUssid().equalsIgnoreCase(ussid))
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
												final int cancelWindow = GenericUtilityMethods.noOfDaysCalculatorBetweenDates(
														subOrder.getCreated(), sysDate);
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
												final int cancelWindow = GenericUtilityMethods.noOfDaysCalculatorBetweenDates(
														subOrder.getCreated(), sysDate);
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
													final int returnWindow = GenericUtilityMethods.noOfDaysCalculatorBetweenDates(
															consignmentModel.getDeliveryDate(), sDate);
													final int actualReturnWindow = Integer.parseInt(rm.getReturnWindow());
													if (!entry.isGiveAway()
															&& !entry.isIsBOGOapplied()
															&& returnWindow < actualReturnWindow
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
												|| entry.getConsignment().getStatus().equals(ConsignmentStatus.REACHED_NEAREST_HUB) || entry
												.getConsignment().getStatus().equals(ConsignmentStatus.DELIVERED)))
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
								formattedActualProductDate = GenericUtilityMethods.getFormattedDate(consignmentModel.getDeliveryDate());
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
				else
				{
					orderModel = orderModelService.getOrder(orderDetails.getCode());
					for (final OrderEntryData entry : orderDetails.getEntries())
					{
						List<String> parentTransactionIds = new ArrayList<>();
						orderproductdto = new OrderProductWsDTO();
						//seller order no
						orderproductdto.setSellerorderno(orderDetails.getCode());

						if (null != entry.getDeliveryPointOfService())
						{
							orderproductdto.setStoreDetails(mplDataMapper.map(entry.getDeliveryPointOfService(),
									PointOfServiceWsDTO.class, "DEFAULT"));
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
							 * final List<RichAttributeModel> richAttributeModel = (List<RichAttributeModel>) productModel
							 * .getRichAttribute(); if (richAttributeModel != null &&
							 * richAttributeModel.get(0).getDeliveryFulfillModes() != null) { final String fullfillmentData =
							 * richAttributeModel.get(0).getDeliveryFulfillModes().getCode() .toUpperCase(); if
							 * (fullfillmentData != null && !fullfillmentData.isEmpty()) {
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
							if (sellerInfoModel != null
									&& sellerInfoModel.getRichAttribute() != null
									&& ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0).getDeliveryFulfillModes() != null)
							{
								/* Fulfillment type */
								final String fulfillmentType = ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0)
										.getDeliveryFulfillModes().getCode();
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
												&& null != orderDetails.getStatus() && null != rm.getCancellationWindow())
										{
											consignmentStatus = orderDetails.getStatus().getCode();
											final Date sysDate = new Date();
											final int cancelWindow = GenericUtilityMethods.noOfDaysCalculatorBetweenDates(
													orderDetails.getCreated(), sysDate);
											final int actualCancelWindow = Integer.parseInt(rm.getCancellationWindow());
											if (cancelWindow < actualCancelWindow
													&& checkOrderStatus(orderDetails.getStatus().getCode(),
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
											final int cancelWindow = GenericUtilityMethods.noOfDaysCalculatorBetweenDates(
													orderDetails.getCreated(), sysDate);
											final int actualCancelWindow = Integer.parseInt(rm.getCancellationWindow());
											if (cancelWindow < actualCancelWindow
													&& checkOrderStatus(consignmentStatus, MarketplacecommerceservicesConstants.CANCEL_STATUS)
															.booleanValue() && !entry.isGiveAway() && !entry.isIsBOGOapplied())

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
												final int returnWindow = GenericUtilityMethods.noOfDaysCalculatorBetweenDates(
														consignmentModel.getDeliveryDate(), sDate);
												final int actualReturnWindow = Integer.parseInt(rm.getReturnWindow());
												if (!entry.isGiveAway()
														&& !entry.isIsBOGOapplied()
														&& returnWindow < actualReturnWindow
														&& !checkOrderStatus(consignmentStatus,
																MarketplacecommerceservicesConstants.VALID_RETURN).booleanValue()
														&& consignmentStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.DELIVERED))

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
											|| entry.getConsignment().getStatus().equals(ConsignmentStatus.REACHED_NEAREST_HUB) || entry
											.getConsignment().getStatus().equals(ConsignmentStatus.DELIVERED)))
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
							formattedActualProductDate = GenericUtilityMethods.getFormattedDate(consignmentModel.getDeliveryDate());
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
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
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
			final OrderModel subOrderModel)
	{

		final Map<String, List<AWBResponseData>> returnMap = new HashMap<String, List<AWBResponseData>>();

		List<AWBResponseData> approvalMapList = new ArrayList<AWBResponseData>();
		List<AWBResponseData> processingMapList = new ArrayList<AWBResponseData>();
		List<AWBResponseData> shippingMapList = new ArrayList<AWBResponseData>();
		List<AWBResponseData> cancelMapList = new ArrayList<AWBResponseData>();
		List<AWBResponseData> returnMapList = new ArrayList<AWBResponseData>();
		List<AWBResponseData> paymentMapList = new ArrayList<AWBResponseData>();
		OrderStatusCodeMasterModel trackModel = null;

		final Map<String, AWBResponseData> awbApprovalMap = new LinkedHashMap<String, AWBResponseData>();
		final Map<String, AWBResponseData> awbProcessingMap = new LinkedHashMap<String, AWBResponseData>();
		final Map<String, AWBResponseData> awbShippingMap = new LinkedHashMap<String, AWBResponseData>();
		final Map<String, AWBResponseData> awbCancelMap = new LinkedHashMap<String, AWBResponseData>();
		final Map<String, AWBResponseData> awbReturnMap = new LinkedHashMap<String, AWBResponseData>();
		final Map<String, AWBResponseData> paymentMap = new LinkedHashMap<String, AWBResponseData>();

		try
		{
			//TISPT-385
			//final OrderModel orderMod = orderModelService.getOrder(subOrder.getCode());
			final Map<String, OrderStatusCodeMasterModel> orderStatusCodeMap = orderModelService.getOrderStausCodeMasterList();

			if (subOrderModel.getHistoryEntries().size() > 0)
			{
				for (final OrderHistoryEntryModel orderHistoryEntry : subOrderModel.getHistoryEntries())
				{
					//****************************** Payment Block
					trackModel = orderStatusCodeMap.get(MarketplaceFacadesConstants.PAYMENT
							+ MarketplacecommerceservicesConstants.STRINGSEPARATOR + orderHistoryEntry.getDescription());
					if (null != trackModel && trackModel.getStage().equalsIgnoreCase(MarketplaceFacadesConstants.PAYMENT)
							&& !isStatusAlradyExists(paymentMap, trackModel) && trackModel.getDisplay().booleanValue())
					{
						paymentMap.put(trackModel.getDotId().trim().toUpperCase(),
								orderTrackingDetails(trackModel, orderHistoryEntry, subOrder));
					}

					if (StringUtils.isNotEmpty(orderEntryDetail.getOrderLineId())
							&& StringUtils.isNotEmpty(orderHistoryEntry.getLineId())
							&& orderEntryDetail.getOrderLineId().equalsIgnoreCase(orderHistoryEntry.getLineId()))
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
							if ((trackModel.getStatusCode().equalsIgnoreCase(ConsignmentStatus.REFUND_INITIATED.getCode()) || trackModel
									.getStatusCode().equalsIgnoreCase(ConsignmentStatus.REFUND_IN_PROGRESS.getCode())))
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
							if ((trackModel.getStatusCode().equalsIgnoreCase(ConsignmentStatus.REFUND_INITIATED.getCode()) || trackModel
									.getStatusCode().equalsIgnoreCase(ConsignmentStatus.REFUND_IN_PROGRESS.getCode())))
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

			paymentMapList = getTrackOrderList(paymentMap);
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

			returnMap.put(MarketplaceFacadesConstants.PAYMENT, paymentMapList);
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
	 * @description: To find the pickup button is updatable or not
	 * @param: OrderData
	 * @return: true or false
	 */
	@Override
	public boolean isPickUpButtonEditable(final OrderData orderDetail)
	{
		LOG.debug("in check for pickup button updatable or not :" + orderDetail.getCode());
		for (final OrderData subOrder : orderDetail.getSellerOrderList())
		{
			for (final OrderEntryData entry : subOrder.getEntries())
			{
				if (null != entry.getMplDeliveryMode()
						&& MarketplacecommerceservicesConstants.CLICK_COLLECT.equalsIgnoreCase(entry.getMplDeliveryMode().getCode()))
				{
					final List<ConsignmentStatus> statuses = getPickUpButtonDisableOptions();

					if (null != entry.getConsignment())
					{
						LOG.info("status code:" + entry.getConsignment().getStatus() + " for txn id " + entry.getTransactionId());
					}
					///if atleast one entry does not contain the listed disabling options,
					//then enable the update pickup button.
					if (null != entry.getConsignment() && null != entry.getConsignment().getStatus()
							&& !statuses.contains(entry.getConsignment().getStatus()))
					{
						LOG.debug("update pickup button is enabled");
						return true;
					}
				}
			}
		}
		LOG.debug("update pickup button is disabled");
		return false;
	}

	/**
	 * @description: it gives the possible pickup button disable options
	 * @param:
	 * @return: List<ConsignmentStatus>
	 */
	@Override
	public List<ConsignmentStatus> getPickUpButtonDisableOptions()
	{
		final List<ConsignmentStatus> neededStatus = new ArrayList<ConsignmentStatus>();
		neededStatus.add(ConsignmentStatus.RETURN_INITIATED);
		neededStatus.add(ConsignmentStatus.COD_CLOSED_WITHOUT_REFUND);
		neededStatus.add(ConsignmentStatus.RETURN_TO_ORIGIN);
		neededStatus.add(ConsignmentStatus.LOST_IN_TRANSIT);
		neededStatus.add(ConsignmentStatus.REVERSE_AWB_ASSIGNED);
		neededStatus.add(ConsignmentStatus.RETURN_RECEIVED);
		neededStatus.add(ConsignmentStatus.RETURN_CLOSED);
		neededStatus.add(ConsignmentStatus.RETURN_CANCELLED);
		neededStatus.add(ConsignmentStatus.REDISPATCH_INITIATED);
		neededStatus.add(ConsignmentStatus.CLOSED_ON_RETURN_TO_ORIGIN);
		neededStatus.add(ConsignmentStatus.REFUND_INITIATED);
		neededStatus.add(ConsignmentStatus.REFUND_IN_PROGRESS);
		neededStatus.add(ConsignmentStatus.RETURN_REJECTED);
		neededStatus.add(ConsignmentStatus.QC_FAILED);
		neededStatus.add(ConsignmentStatus.CLOSED_ON_CANCELLATION);
		neededStatus.add(ConsignmentStatus.CANCELLATION_INITIATED);
		neededStatus.add(ConsignmentStatus.RETURN_COMPLETED);
		neededStatus.add(ConsignmentStatus.ORDER_CANCELLED);
		neededStatus.add(ConsignmentStatus.ORDER_COLLECTED);
		neededStatus.add(ConsignmentStatus.ORDER_UNCOLLECTED);
		neededStatus.add(ConsignmentStatus.RETURNINITIATED_BY_RTO);
		return neededStatus;
	}

	@Override
	public OrderTrackingWsDTO getOrderDetailsWithTracking(final HttpServletRequest request, final String orderCode)
	{
		final OrderTrackingWsDTO orderTrackingWsDTO = new OrderTrackingWsDTO();
		final List<OrderProductWsDTO> orderproductdtos = new ArrayList<OrderProductWsDTO>();
		List<Ordershipmentdetailstdto> ordershipmentdetailstdtos = null;
		OrderProductWsDTO orderproductdto = null;
		String consignmentStatus = "";
		OrderStatusCodeMasterModel customerStatusModel = null;
		OrderData orderDetail = null;
		OrderModel orderModel = null;
		OrderModel subOrderModel = null;
		String isGiveAway = "N", formattedProductDate = MarketplacecommerceservicesConstants.EMPTY, formattedActualProductDate = MarketplacecommerceservicesConstants.EMPTY;
		ConsignmentModel consignmentModel = null;
		List<OrderData> subOrderList = null;
		SellerInformationModel sellerInfoModel = null;
        	/* TPR-5975 starts here */
		MarketplaceDeliveryModeData mplDeliveryMode = null;
		/* TPR-5975 ends here */
		String ussid = "";
		boolean isFineJwlry = false;
		try
		{
			//TPR-815
			final BaseStoreModel baseStoreModel = baseStoreService.getCurrentBaseStore();
			orderModel = customerAccountService.getOrderForCode((CustomerModel) userService.getCurrentUser(), orderCode,
					baseStoreModel);

			if (orderModel != null)
			{
				orderDetail = mplCheckoutFacade.getOrderDetailsForCode(orderModel);
			}
			if (null == orderDetail)
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.E9047);
			}
			else
			{
				
				/*Added For EGV Functionality Start */
				if (null != orderDetail.getEgvCardExpDate())
				{
					orderTrackingWsDTO.setCartExpiryDate(orderDetail.getEgvCardExpDate());
				}
				if(orderDetail.isResendEgvMailAvailable()) {
					orderTrackingWsDTO.setResendAvailable(true);
				}else {
					orderTrackingWsDTO.setResendAvailable(false);
				}
				if(null != orderDetail.getEgvCardNumber()){
					orderTrackingWsDTO.setEgvCartNumber(orderDetail.getEgvCardNumber());
				}
				if(orderDetail.isIsEGVOrder() ){
					orderTrackingWsDTO.setIsEgvOrder(true);
				}
				if(null !=orderModel.getSplitModeInfo() && orderModel.getSplitModeInfo().trim().equalsIgnoreCase(MarketplaceFacadesConstants.CLIQ_CASH.trim()) && null != orderModel.getModeOfOrderPayment()){
					orderTrackingWsDTO.setPaymentMethod(orderModel.getModeOfOrderPayment());
				}
				
			//	setUpStatementData(orderTrackingWsDTO,orderDetail);
				
				/*Added For EGV Functionality End */
				//orderTrackingWsDTO.setRecipientname(recipientname);
				if (null != orderDetail.getDeliveryAddress())
				{
					orderTrackingWsDTO.setDeliveryAddress(GenericUtilityMethods.setAddress(orderDetail, 2));
				}
				if (null != orderDetail.getPickupName())
				{
					orderTrackingWsDTO.setPickupPersonName(orderDetail.getPickupName());
				}
				if (null != orderDetail.getPickupPhoneNumber())
				{
					orderTrackingWsDTO.setPickupPersonMobile(orderDetail.getPickupPhoneNumber());
				}
				//Not implemented
				orderTrackingWsDTO.setGiftWrapCharge(MarketplacecommerceservicesConstants.ZERO);

				if (null != orderDetail.getCreated())
				{
					orderTrackingWsDTO.setOrderDate(orderDetail.getCreated());
				}
				if (StringUtils.isNotEmpty(orderDetail.getCode()))
				{
					orderTrackingWsDTO.setOrderId(orderDetail.getCode());
				}

				//Removed due to TISJEW-3519
				//TPR-6117 exchange field added
				//				if (CollectionUtils.isNotEmpty(orderDetail.getEntries()))
				//				{
				//					for (final OrderEntryData entry : orderDetail.getEntries())
				//					{
				//						if (StringUtils.isNotEmpty(entry.getExchangeApplied()))
				//						{
				//							orderTrackingWsDTO.setExchangeId(entry.getExchangeApplied());
				//						}
				//					}
				//				}

				//not required
				//orderTrackingWsDTO.setCancelflag(MarketplacecommerceservicesConstants.YES);
				if (null != orderDetail.getDeliveryAddress()
						&& StringUtils.isNotEmpty(orderDetail.getDeliveryAddress().getLastName())
						&& StringUtils.isNotEmpty(orderDetail.getDeliveryAddress().getFirstName()))
				{
					final String name = orderDetail.getDeliveryAddress().getFirstName().concat(" ")
							.concat(orderDetail.getDeliveryAddress().getLastName());
					orderTrackingWsDTO.setRecipientname(name);
				}
				if (null != orderDetail.getDeliveryCost() && StringUtils.isNotEmpty(orderDetail.getDeliveryCost().toString()))
				{
					orderTrackingWsDTO.setDeliveryCharge(orderDetail.getDeliveryCost().getValue().toString());
				}

				if (null != orderDetail.getTotalPriceWithConvCharge()
						&& StringUtils.isNotEmpty(orderDetail.getTotalPriceWithConvCharge().getValue().toString()))
				{
					orderTrackingWsDTO.setTotalOrderAmount(orderDetail.getTotalPriceWithConvCharge().getValue().toString());
				}
				//TISEE-4660 starts
				if (null != orderDetail.getConvenienceChargeForCOD()
						&& StringUtils.isNotEmpty(orderDetail.getConvenienceChargeForCOD().getValue().toString()))
				{
					orderTrackingWsDTO.setConvenienceCharge(orderDetail.getConvenienceChargeForCOD().getValue().toString());
				}
				if (null != orderDetail.getSubTotal() && StringUtils.isNotEmpty(orderDetail.getSubTotal().getValue().toString()))
				{
					orderTrackingWsDTO.setSubTotal(orderDetail.getSubTotal().getValue().toString());
				}
				//TISEE-4660 ends
				//TISST-13769
				if (null != orderDetail.getTotalDiscounts()
						&& StringUtils.isNotEmpty(orderDetail.getTotalDiscounts().getValue().toString()))
				{
					orderTrackingWsDTO.setTotalDiscount(orderDetail.getTotalDiscounts().getValue().toString());
				}
				if (null != orderDetail.getMplPaymentInfo())
				{
					if (null != orderDetail.getMplPaymentInfo().getBillingAddress())
					{
						orderTrackingWsDTO.setBillingAddress(GenericUtilityMethods.setAddress(orderDetail, 1));
					}
					if (null != orderDetail.getMplPaymentInfo().getPaymentOption())
					{
						final String paymentOption = orderDetail.getMplPaymentInfo().getPaymentOption();
						orderTrackingWsDTO.setPaymentMethod(paymentOption);

						if (paymentOption.equalsIgnoreCase(MarketplacecommerceservicesConstants.CREDIT)
								|| paymentOption.equalsIgnoreCase(MarketplacecommerceservicesConstants.EMI)
								|| paymentOption.equalsIgnoreCase(MarketplacecommerceservicesConstants.DEBIT))
						{

							orderTrackingWsDTO.setAccountHolderName(orderDetail.getMplPaymentInfo().getCardAccountHolderName());
							orderTrackingWsDTO.setPaymentCard(orderDetail.getMplPaymentInfo().getCardCardType());
							orderTrackingWsDTO.setPaymentCardDigit(orderDetail.getMplPaymentInfo().getCardIssueNumber());
							orderTrackingWsDTO.setPaymentCardExpire(orderDetail.getMplPaymentInfo().getCardExpirationMonth()
									+ MarketplacecommerceservicesConstants.FRONTSLASH
									+ orderDetail.getMplPaymentInfo().getCardExpirationYear());
						}
					}
				}
				//TPR-815 check parent order first
				subOrderList = orderDetail.getSellerOrderList();
				if (CollectionUtils.isNotEmpty(subOrderList))
				{
					for (final OrderData subOrder : subOrderList)
					{
						//TISPT-385
						subOrderModel = orderModelService.getOrder(subOrder.getCode());
						for (final OrderEntryData entry : subOrder.getEntries())
						{
							List<String> parentTransactionIds = new ArrayList<>();
							final ProductData product = entry.getProduct();
							//getting the product code
							//							final ProductModel productModel = productService.getProductForCode(entry.getProduct().getCode());
							final ProductModel productModel = mplOrderFacade.getProductForCode(entry.getProduct().getCode());


							if (null == product)
							{
								throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.E9048);
							}
							else
							{
								orderproductdto = new OrderProductWsDTO();
								if(null !=entry.getWalletApportionPaymentData() ){
									orderproductdto.setWalletApportionPaymentData(entry.getWalletApportionPaymentData());
								}
								if(null !=entry.getWalletApportionforReverseData() ){
									orderproductdto.setWalletApportionforReverseData(entry.getWalletApportionforReverseData());
								}
								ordershipmentdetailstdtos = new ArrayList<Ordershipmentdetailstdto>();
								//								if (!entry.isGiveAway())
								//								{
								orderproductdto.setImageURL(setImageURL(product));

								if (null != entry.getDeliveryPointOfService())
								{
									orderproductdto.setStoreDetails(mplDataMapper.map(entry.getDeliveryPointOfService(),
											PointOfServiceWsDTO.class, "DEFAULT"));
								}

								if (StringUtils.isNotEmpty(entry.getAmountAfterAllDisc().toString()))
								{
									orderproductdto.setPrice(entry.getAmountAfterAllDisc().getValue().toString());
								}
								if (null != product.getBrand() && StringUtils.isNotEmpty(product.getBrand().getBrandname()))
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
								/* Fulfillment type */
								/*
								 * final List<RichAttributeModel> richAttributeModel = (List<RichAttributeModel>) productModel
								 * .getRichAttribute(); if (richAttributeModel != null &&
								 * richAttributeModel.get(0).getDeliveryFulfillModes() != null) { final String fullfillmentData
								 * = richAttributeModel.get(0).getDeliveryFulfillModes().getCode() .toUpperCase(); if
								 * (fullfillmentData != null && !fullfillmentData.isEmpty()) {
								 * orderproductdto.setFulfillment(fullfillmentData); } }
								 */
								if (entry.isGiveAway())
								{
									isGiveAway = "Y";
								}
								else
								{
									isGiveAway = "N";
								}
								orderproductdto.setIsGiveAway(isGiveAway);
								if (null != entry.getAssociatedItems())
								{
									orderproductdto.setAssociatedProducts(entry.getAssociatedItems());
								}
								//Delivery date is the final delivery date
								/*

								 * if (null != entry.getMplDeliveryMode()) {
								 *
								 * if (null != entry.getMplDeliveryMode().getDescription() &&
								 * StringUtils.isNotEmpty(entry.getMplDeliveryMode().getDescription())) {
								 *
								 * orderproductdto.setDeliveryDate(entry.getMplDeliveryMode().getDescription()); } }
								 */

								/* capacity */
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
								/*
								 * if (null != orderDetail.getSellerOrderList()) { for (final OrderData childOrder :
								 * orderDetail.getSellerOrderList()) {
								 */
								if (null != subOrder.getCode())
								{
									orderproductdto.setSellerorderno(subOrder.getCode());
								}
								//}

								/*
								 * if (null != orderproductdto.getUSSID()) {
								 *
								 * orderproductdto.setSerialno(orderproductdto.getUSSID()); } else {
								 * orderproductdto.setSerialno(MarketplacecommerceservicesConstants.NA); }
								 */

								//}
								//TISJEW-3519 && TPR-1083 && TPR-6117
								if (StringUtils.isNotEmpty(entry.getExchangeApplied()))
								{
									orderproductdto.setExchangeId(entry.getExchangeApplied());
								}
								if (StringUtils.isNotEmpty(entry.getSelectedUssid()))
								{
									//sellerInfoModel = getMplSellerInformationService().getSellerDetail(entry.getSelectedUssid());
									if (((MarketplacecommerceservicesConstants.FINEJEWELLERY)).equalsIgnoreCase(productModel
											.getProductCategoryType()))
									{
										isFineJwlry = true;
										final List<JewelleryInformationModel> jewelleryInfo = jewelleryService
												.getJewelleryInfoByUssid(entry.getSelectedUssid());
										if (CollectionUtils.isNotEmpty(jewelleryInfo))
										{
											sellerInfoModel = getMplSellerInformationService().getSellerDetail(
													jewelleryInfo.get(0).getPCMUSSID());
											ussid = jewelleryInfo.get(0).getUSSID();
										}
										else
										{
											LOG.error("No entry in JewelleryInformationModel for ussid " + entry.getSelectedUssid());
										}
									}
									else
									{
										sellerInfoModel = getMplSellerInformationService().getSellerDetail(entry.getSelectedUssid());
										ussid = sellerInfoModel.getUSSID();
									}
								}
								if (sellerInfoModel != null
										&& sellerInfoModel.getRichAttribute() != null
										&& ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0).getDeliveryFulfillModes() != null)
								{
									/* Fulfillment type */
									final String fulfillmentType = ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0)
											.getDeliveryFulfillModes().getCode();
									if (StringUtils.isNotEmpty(fulfillmentType))
									{
										orderproductdto.setFulfillment(fulfillmentType);
									}
									//Seller info
									/*
									 * if (sellerInfoModel.getUSSID() != null &&
									 * sellerInfoModel.getUSSID().equalsIgnoreCase(entry.getSelectedUssid()))
									 */
									if (sellerInfoModel.getUSSID() != null && entry.getSelectedUssid().equalsIgnoreCase(ussid))
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
											if (!mplOrderFacade.isChildCancelleable(subOrder, entry.getTransactionId()))
											{
												orderproductdto.setCancel(Boolean.FALSE);
											}
											if (null == entry.getConsignment() && entry.getQuantity().doubleValue() != 0
													&& null != subOrder.getStatus())
											{
												consignmentStatus = subOrder.getStatus().getCode();
												//cancellation window not required
												/*
												 * if (null != rm.getCancellationWindow()) { final Date sysDate = new Date(); final
												 * int cancelWindow = GenericUtilityMethods.noOfDaysCalculatorBetweenDates(
												 * subOrder.getCreated(), sysDate); final int actualCancelWindow =
												 * Integer.parseInt(rm.getCancellationWindow()); if (cancelWindow <
												 * actualCancelWindow && checkOrderStatus(subOrder.getStatus().getCode(),
												 * MarketplacecommerceservicesConstants.CANCEL_ORDER_STATUS).booleanValue() &&
												 * !entry.isGiveAway() && !entry.isIsBOGOapplied()) {

												 * orderproductdto.setCancel(Boolean.TRUE);
												 *
												 * } else { orderproductdto.setCancel(Boolean.FALSE); } } else {
												 * orderproductdto.setCancel(Boolean.FALSE); }
												 */
												if (checkOrderStatus(subOrder.getStatus().getCode(),
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
											else if (null != entry.getConsignment() && null != entry.getConsignment().getStatus())
											{
												consignmentStatus = entry.getConsignment().getStatus().getCode();
												//cancellation window not required
												/*
												 * if (null != rm.getCancellationWindow()) { final Date sysDate = new Date(); final
												 * int cancelWindow = GenericUtilityMethods.noOfDaysCalculatorBetweenDates(
												 * subOrder.getCreated(), sysDate); final int actualCancelWindow =
												 * Integer.parseInt(rm.getCancellationWindow()); if (cancelWindow <
												 * actualCancelWindow && checkOrderStatus(consignmentStatus,
												 * MarketplacecommerceservicesConstants.CANCEL_STATUS).booleanValue() &&
												 * !entry.isGiveAway() && !entry.isIsBOGOapplied())
												 *
												 * { orderproductdto.setCancel(Boolean.TRUE);
												 *
												 * } else { orderproductdto.setCancel(Boolean.FALSE); } } else {
												 * orderproductdto.setCancel(Boolean.FALSE); }
												 */
												if (checkOrderStatus(consignmentStatus, MarketplacecommerceservicesConstants.CANCEL_STATUS)
														.booleanValue() && !entry.isGiveAway() && !entry.isIsBOGOapplied())

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
											if (null != entry.getConsignment() && null != entry.getConsignment().getStatus())
											{
												consignmentStatus = entry.getConsignment().getStatus().getCode();
												consignmentModel = mplOrderService.fetchConsignment(entry.getConsignment().getCode());

												if (null != consignmentModel && rm.getReturnWindow() != null)
												{
													final Date sDate = new Date();
													final int returnWindow = GenericUtilityMethods.noOfDaysCalculatorBetweenDates(
															consignmentModel.getDeliveryDate(), sDate);
													final int actualReturnWindow = Integer.parseInt(rm.getReturnWindow());
													if (!entry.isGiveAway()
															&& !entry.isIsBOGOapplied()
															&& returnWindow < actualReturnWindow
															&& !checkOrderStatus(consignmentStatus,
																	MarketplacecommerceservicesConstants.VALID_RETURN).booleanValue()
															&& (consignmentStatus
																	.equalsIgnoreCase(MarketplacecommerceservicesConstants.DELIVERED) || consignmentStatus
																	.equalsIgnoreCase(MarketplacecommerceservicesConstants.ORDER_COLLECTED)))

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
												// set window
												if (rm.getReturnWindow() != null)
												{
													orderproductdto.setReturnPolicy(rm.getReturnWindow());
												}
											}
											else
											{
												orderproductdto.setIsReturned(Boolean.FALSE);
											}
										}
									}
								}

								// display order tracking messages
								if (null != consignmentModel)
								{
									formattedProductDate = GenericUtilityMethods.getFormattedDate(consignmentModel.getEstimatedDelivery());
									formattedActualProductDate = GenericUtilityMethods
											.getFormattedDate(consignmentModel.getDeliveryDate());
									if (null != consignmentModel.getTrackingID())
									{
										orderproductdto.setTrackingAWB(consignmentModel.getTrackingID());
									}
									if (null != consignmentModel.getReturnAWBNum())
									{
										orderproductdto.setReturnAWB(consignmentModel.getReturnAWBNum());
									}
									if (null != consignmentModel.getCarrier())
									{
										orderproductdto.setLogisticName(consignmentModel.getCarrier());
									}
									if (null != consignmentModel.getReturnCarrier())
									{
										orderproductdto.setReverseLogisticName(consignmentModel.getReturnCarrier());
									}
								}

								//Showing Delivery date for freebies and give aways TISEE-5520
								/*
								 * if (null != entry.getConsignment() && (entry.isGiveAway() || entry.isIsBOGOapplied())) {
								 * consignmentModel = mplOrderService.fetchConsignment(entry.getConsignment().getCode());
								 * formattedProductDate =
								 * GenericUtilityMethods.getFormattedDate(consignmentModel.getEstimatedDelivery());
								 * formattedActualProductDate = GenericUtilityMethods
								 * .getFormattedDate(consignmentModel.getDeliveryDate()); if (null !=
								 * consignmentModel.getTrackingID()) {
								 * orderproductdto.setTrackingAWB(consignmentModel.getTrackingID()); } if (null !=
								 * consignmentModel.getReturnAWBNum()) {
								 * orderproductdto.setReturnAWB(consignmentModel.getReturnAWBNum()); } if (null !=
								 * consignmentModel.getCarrier()) {
								 * orderproductdto.setLogisticName(consignmentModel.getCarrier()); } if (null !=
								 * consignmentModel.getReturnCarrier()) {

								 * orderproductdto.setReverseLogisticName(consignmentModel.getReturnCarrier()); }
								 *
								 * }
								 */
								//End
								final Map<String, List<AWBResponseData>> returnMap = getOrderStatusTrack(entry, subOrder, subOrderModel);
								orderproductdto.setStatusDisplayMsg(setStatusDisplayMessage(returnMap, consignmentModel));
								//setting current product status Display
								if ((consignmentStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.REFUND_INITIATED) || consignmentStatus
										.equalsIgnoreCase(MarketplacecommerceservicesConstants.REFUND_IN_PROGRESS))
										&& returnMap.get(MarketplaceFacadesConstants.CANCEL) != null
										&& returnMap.get(MarketplaceFacadesConstants.CANCEL).size() > 0)
								{
									orderproductdto.setStatusDisplay(MarketplaceFacadesConstants.CANCEL);
								}
								else if ((consignmentStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.REFUND_INITIATED) || consignmentStatus
										.equalsIgnoreCase(MarketplacecommerceservicesConstants.REFUND_IN_PROGRESS))
										&& returnMap.get(MarketplaceFacadesConstants.RETURN) != null
										&& returnMap.get(MarketplaceFacadesConstants.RETURN).size() > 0)
								{
									orderproductdto.setStatusDisplay(MarketplaceFacadesConstants.RETURN);
								}
								else
								{
									customerStatusModel = orderModelService.getOrderStausCodeMaster(consignmentStatus);
									if (customerStatusModel != null)
									{
										orderproductdto.setStatusDisplay(customerStatusModel.getStage());
									}
									else
									{
										orderproductdto.setStatusDisplay(MarketplacecommerceservicesConstants.NA);
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

								/* TPR-5975 starts here */
								if (entry.getMplDeliveryMode() != null)
								{
									mplDeliveryMode = entry.getMplDeliveryMode();
									final SelectedDeliveryModeWsDTO selectedDeliveryModeWsDTO = new SelectedDeliveryModeWsDTO();
									if (StringUtils.isNotEmpty(mplDeliveryMode.getCode()))
									{
										selectedDeliveryModeWsDTO.setCode(mplDeliveryMode.getCode());
									}
									String value = "";
									if (!entry.isGiveAway() && entry.getCurrDelCharge() != null
											&& StringUtils.isNotEmpty(value = entry.getCurrDelCharge().getValue().toString()))//IQA
									{
										LOG.debug("The Delivery Cost is not empty block1" + value);
										//selectedDeliveryModeWsDTO.setDeliveryCost(mplDeliveryMode.getDeliveryCost().getValue().toString());
										selectedDeliveryModeWsDTO.setDeliveryCost(value);
									}
									else
									{
										selectedDeliveryModeWsDTO.setDeliveryCost(ZERODELCOST);//IQA
									}
									if (StringUtils.isNotEmpty(mplDeliveryMode.getDescription()))
									{
										selectedDeliveryModeWsDTO.setDesc(mplDeliveryMode.getDescription());
									}
									if (StringUtils.isNotEmpty(mplDeliveryMode.getName()))
									{
										selectedDeliveryModeWsDTO.setName(mplDeliveryMode.getName());
									}
									orderproductdto.setSelectedDeliveryMode(selectedDeliveryModeWsDTO);
								}
								/* TPR-5975 ends here */

								//Set the transaction id
								if (entry.getTransactionId() != null)
								{
									orderproductdto.setTransactionId(entry.getTransactionId());
								}
								else
								{
									orderproductdto.setTransactionId(MarketplacecommerceservicesConstants.EMPTY);
								}
								//Setting parent transaction ID ---TISPT-385
								if (StringUtils.isNotEmpty(entry.getParentTransactionID()))
								{
									parentTransactionIds = Arrays.asList(entry.getParentTransactionID().split("\\s*,\\s*"));
								}
								orderproductdto.setParentTransactionId(parentTransactionIds);

								//ADDED R2.3 New attribute
								if (entry.getSelectedDeliverySlotDate() != null)
								{
									orderproductdto.setScheduleDeliveryDate(entry.getSelectedDeliverySlotDate());
									if (StringUtils.isNotEmpty(entry.getTimeSlotFrom()))
									{
										if (StringUtils.isNotEmpty(entry.getTimeSlotTo())
												&& StringUtils.isNotEmpty(entry.getTimeSlotFrom()))
										{
											orderproductdto.setScheduleDeliveryTime(entry.getTimeSlotFrom().concat(" to ")
													.concat(entry.getTimeSlotTo()));
										}

									}
								}

								//R2.3 Changes-Start
								orderproductdto
										.setSelfCourierDocumentLink(getSelfCourierDocumentUrl(request,subOrder.getCode(), entry.getTransactionId()));


								String returnType = getAwbPopupLink(entry, subOrder.getCode());
								if (MarketplacecommerceservicesConstants.SELF_COURIER.equalsIgnoreCase(returnType)
										&& !entry.isIsRefundable())
								{
									orderproductdto.setAwbPopupLink(MarketplacecommerceservicesConstants.Y);
								}
								else
								{
									orderproductdto.setAwbPopupLink(MarketplacecommerceservicesConstants.N);
								}
								//R2.3 Changes-END

								if (isFineJwlry && (MarketplacecommerceservicesConstants.SELF_COURIER).equalsIgnoreCase(returnType))
								{
									orderproductdto.setAwbPopupLink(MarketplacecommerceservicesConstants.N);
									for (final Entry<String, StatusResponseListDTO> statusEntry : orderproductdto.getStatusDisplayMsg()
											.entrySet())
									{
										if (MarketplaceFacadesConstants.RETURN.equalsIgnoreCase(statusEntry.getKey()))
										{
											final StatusResponseListDTO innerEntry = statusEntry.getValue();
											for (final StatusResponseDTO status : innerEntry.getStatusList())
											{
												if (("RETURN_INITIATED").equalsIgnoreCase(status.getResponseCode()))
												{
													for (final StatusResponseMessageDTO statRes : status.getStatusMessageList())
													{
														if ((MarketplacecommerceservicesConstants.FINEJEW_ORDER_RETURN)
																.equalsIgnoreCase(statRes.getStatusDescription()))
														{
															statRes
																	.setStatusDescription(MarketplacecommerceservicesConstants.FINEJEW_SELFCOURIER_ERRORMSG);
														}
													}

												}
											}
										}
									}
								}
								//TISPRDT-2546 ends

								//Check if invoice is available
								if (entry.getConsignment() != null)
								{
									if (entry.getConsignment().getStatus() != null
											&& (entry.getConsignment().getStatus().equals(ConsignmentStatus.HOTC)
													|| entry.getConsignment().getStatus().equals(ConsignmentStatus.OUT_FOR_DELIVERY)
													|| entry.getConsignment().getStatus().equals(ConsignmentStatus.REACHED_NEAREST_HUB) || entry
													.getConsignment().getStatus().equals(ConsignmentStatus.DELIVERED)))
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
								if (!formattedProductDate.equalsIgnoreCase(MarketplacecommerceservicesConstants.EMPTY))
								{
									orderproductdto.setEstimateddeliverydate(formattedProductDate);
								}
								//delivery date
								if (!formattedActualProductDate.equalsIgnoreCase(MarketplacecommerceservicesConstants.EMPTY))
								{
									orderproductdto.setDeliveryDate(formattedActualProductDate);
								}

								if (null != orderDetail.getConsignments())
								{
									for (final ConsignmentData shipdetails : orderDetail.getConsignments())
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
						}
					}
					orderTrackingWsDTO.setProducts(orderproductdtos);
					orderTrackingWsDTO.setIsPickupUpdatable(isPickUpButtonEditable(orderDetail));
					orderTrackingWsDTO.setStatusDisplay(orderDetail.getStatusDisplay());
					orderTrackingWsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
					//R2.3 FLO1 new attribute Added
					orderTrackingWsDTO.setIsCDA(mplDeliveryAddressFacade.isDeliveryAddressChangable(orderCode));
				}
				else
				{
					for (final OrderEntryData orderEntry : orderDetail.getEntries())
					{
						final ProductData product = orderEntry.getProduct();
						if (null == product)
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.E9048);
						}
						else
						{
							//getting the product code
							final ProductModel productModel = mplOrderFacade.getProductForCode(product.getCode());
							if(null !=orderEntry.getWalletApportionPaymentData() ){
								orderproductdto.setWalletApportionPaymentData(orderEntry.getWalletApportionPaymentData());
							}
							if(null !=orderEntry.getWalletApportionforReverseData() ){
								orderproductdto.setWalletApportionforReverseData(orderEntry.getWalletApportionforReverseData());
							}
							orderproductdto = new OrderProductWsDTO();
							
							ordershipmentdetailstdtos = new ArrayList<Ordershipmentdetailstdto>();
							//set image
							orderproductdto.setImageURL(setImageURL(product));

							if (null != orderEntry.getDeliveryPointOfService())
							{
								orderproductdto.setStoreDetails(mplDataMapper.map(orderEntry.getDeliveryPointOfService(),
										PointOfServiceWsDTO.class, "DEFAULT"));
							}
							//changes
							if (null != orderEntry.getAmountAfterAllDisc()
									&& StringUtils.isNotEmpty(orderEntry.getAmountAfterAllDisc().getValue().toString())
									&& orderEntry.getAmountAfterAllDisc().getValue().doubleValue() > 0)
							{
								orderproductdto.setPrice(orderEntry.getAmountAfterAllDisc().getValue().toString());
							}
							else if (null != orderEntry.getTotalPrice().getValue()
									&& orderEntry.getTotalPrice().getValue().doubleValue() > 0)
							{
								orderproductdto.setPrice(orderEntry.getTotalPrice().getValue().toString());
							}
							else
							{
								orderproductdto.setPrice(MarketplacecommerceservicesConstants.NA);
							}

							if (null != product.getBrand() && StringUtils.isNotEmpty(product.getBrand().getBrandname()))
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
							/* Fulfillment type */
							if (orderEntry.isGiveAway())
							{
								isGiveAway = "Y";
							}
							else
							{
								isGiveAway = "N";
							}
							orderproductdto.setIsGiveAway(isGiveAway);
							if (null != orderEntry.getAssociatedItems())
							{
								orderproductdto.setAssociatedProducts(orderEntry.getAssociatedItems());
							}
							//Delivery date is the final delivery date
							/* capacity */
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
							String ussidJwlry = "";

							if (StringUtils.isNotEmpty(orderEntry.getSelectedUssid()))
							{
								/*
								 * sellerInfoModel =
								 * getMplSellerInformationService().getSellerDetail(orderEntry.getSelectedUssid());
								 */
								if (((MarketplacecommerceservicesConstants.FINEJEWELLERY)).equalsIgnoreCase(productModel
										.getProductCategoryType()))
								{
									isFineJwlry = true;
									final List<JewelleryInformationModel> jewelleryInfo = jewelleryService
											.getJewelleryInfoByUssid(orderEntry.getSelectedUssid());
									if (CollectionUtils.isNotEmpty(jewelleryInfo))
									{
										sellerInfoModel = getMplSellerInformationService().getSellerDetail(
												jewelleryInfo.get(0).getPCMUSSID());
										ussidJwlry = jewelleryInfo.get(0).getUSSID();
									}
									else
									{
										LOG.error("No entry in JewelleryInformationModel for ussid " + orderEntry.getSelectedUssid());
									}
								}
								else
								{
									sellerInfoModel = getMplSellerInformationService().getSellerDetail(orderEntry.getSelectedUssid());
									ussidJwlry = sellerInfoModel.getUSSID();
								}
							}
							if (sellerInfoModel != null
									&& sellerInfoModel.getRichAttribute() != null
									&& ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0).getDeliveryFulfillModes() != null)
							{
								/* Fulfillment type */
								final String fulfillmentType = ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0)
										.getDeliveryFulfillModes().getCode();
								if (StringUtils.isNotEmpty(fulfillmentType))
								{
									orderproductdto.setFulfillment(fulfillmentType);
								}
								//Seller info
								/*
								 * if (sellerInfoModel.getUSSID() != null &&
								 * sellerInfoModel.getUSSID().equalsIgnoreCase(orderEntry.getSelectedUssid()))
								 */
								if (orderEntry.getSelectedUssid() != null && orderEntry.getSelectedUssid().equalsIgnoreCase(ussidJwlry))
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
										if (!mplOrderFacade.isChildCancelleable(orderDetail, orderEntry.getTransactionId()))
										{
											orderproductdto.setCancel(Boolean.FALSE);
										}
										if (null == orderEntry.getConsignment() && orderEntry.getQuantity().doubleValue() != 0
												&& null != orderDetail.getStatus())
										{
											consignmentStatus = orderDetail.getStatus().getCode();
											//cancellation window not required
											if (checkOrderStatus(orderDetail.getStatus().getCode(),
													MarketplacecommerceservicesConstants.CANCEL_ORDER_STATUS).booleanValue()
													&& !orderEntry.isGiveAway() && !orderEntry.isIsBOGOapplied())
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
									}
								}
							}
							final Map<String, List<AWBResponseData>> returnMap = getOrderStatusTrack(orderEntry, orderDetail, orderModel);
							orderproductdto.setStatusDisplayMsg(setStatusDisplayMessage(returnMap, consignmentModel));
							//setting current product status Display
							if ((consignmentStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.REFUND_INITIATED) || consignmentStatus
									.equalsIgnoreCase(MarketplacecommerceservicesConstants.REFUND_IN_PROGRESS))
									&& returnMap.get(MarketplaceFacadesConstants.CANCEL) != null
									&& returnMap.get(MarketplaceFacadesConstants.CANCEL).size() > 0)
							{
								orderproductdto.setStatusDisplay(MarketplaceFacadesConstants.CANCEL);
							}
							else if ((consignmentStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.REFUND_INITIATED) || consignmentStatus
									.equalsIgnoreCase(MarketplacecommerceservicesConstants.REFUND_IN_PROGRESS))
									&& returnMap.get(MarketplaceFacadesConstants.RETURN) != null
									&& returnMap.get(MarketplaceFacadesConstants.RETURN).size() > 0)
							{
								orderproductdto.setStatusDisplay(MarketplaceFacadesConstants.RETURN);
							}
							else
							{
								customerStatusModel = orderModelService.getOrderStausCodeMaster(consignmentStatus);
								if (customerStatusModel != null)
								{
									orderproductdto.setStatusDisplay(customerStatusModel.getStage());
								}
								else
								{
									orderproductdto.setStatusDisplay(MarketplacecommerceservicesConstants.NA);
								}
							}
							//Check if invoice is available
							orderproductdto.setIsInvoiceAvailable(Boolean.FALSE);

							/* TPR-5975 starts here */
							if (orderEntry.getMplDeliveryMode() != null)
							{
								mplDeliveryMode = orderEntry.getMplDeliveryMode();
								final SelectedDeliveryModeWsDTO selectedDeliveryModeWsDTO = new SelectedDeliveryModeWsDTO();
								if (StringUtils.isNotEmpty(mplDeliveryMode.getCode()))
								{
									selectedDeliveryModeWsDTO.setCode(mplDeliveryMode.getCode());
								}
								String value = "";
								if (!orderEntry.isGiveAway() && orderEntry.getCurrDelCharge() != null
										&& StringUtils.isNotEmpty(value = orderEntry.getCurrDelCharge().getValue().toString()))//IQA
								{
									LOG.debug("The Delivery Cost is not empty block2" + value);
									//selectedDeliveryModeWsDTO.setDeliveryCost(mplDeliveryMode.getDeliveryCost().getValue().toString());
									selectedDeliveryModeWsDTO.setDeliveryCost(value);
								}
								else
								{
									selectedDeliveryModeWsDTO.setDeliveryCost(ZERODELCOST);//IQA
								}
								if (StringUtils.isNotEmpty(mplDeliveryMode.getDescription()))
								{
									selectedDeliveryModeWsDTO.setDesc(mplDeliveryMode.getDescription());
								}
								if (StringUtils.isNotEmpty(mplDeliveryMode.getName()))
								{
									selectedDeliveryModeWsDTO.setName(mplDeliveryMode.getName());
								}
								orderproductdto.setSelectedDeliveryMode(selectedDeliveryModeWsDTO);
							}
							/* TPR-5975 ends here */


							//Check if Shipment details is available
							final Ordershipmentdetailstdto ordershipmentdetailstdto1 = new Ordershipmentdetailstdto();
							ordershipmentdetailstdto1.setStatus(MarketplacecommerceservicesConstants.NA);
							ordershipmentdetailstdto1.setStatusDate(new Date());
							ordershipmentdetailstdtos.add(ordershipmentdetailstdto1);
							orderproductdto.setShipmentdetails(ordershipmentdetailstdtos.get(0));

							orderproductdtos.add(orderproductdto);
							//R2.3 Changes-Start
							orderproductdto
									.setSelfCourierDocumentLink(getSelfCourierDocumentUrl(request,orderDetail.getCode(), orderEntry.getTransactionId()));


							String returnType = getAwbPopupLink(orderEntry, orderDetail.getCode());
							if (MarketplacecommerceservicesConstants.SELF_COURIER.equalsIgnoreCase(returnType)
									&& !orderEntry.isIsRefundable())
							{
								orderproductdto.setAwbPopupLink(MarketplacecommerceservicesConstants.Y);
							}
							else
							{
								orderproductdto.setAwbPopupLink(MarketplacecommerceservicesConstants.N);
							}
							//R2.3 Changes-END
							//TISPRDT-2546
							if (isFineJwlry && (MarketplacecommerceservicesConstants.SELF_COURIER).equalsIgnoreCase(returnType))
							{
								orderproductdto.setAwbPopupLink(MarketplacecommerceservicesConstants.N);
								for (final Entry<String, StatusResponseListDTO> statusEntry : orderproductdto.getStatusDisplayMsg()
										.entrySet())
								{
									if (MarketplaceFacadesConstants.RETURN.equalsIgnoreCase(statusEntry.getKey()))
									{
										final StatusResponseListDTO innerEntry = statusEntry.getValue();
										for (final StatusResponseDTO status : innerEntry.getStatusList())
										{
											if (("RETURN_INITIATED").equalsIgnoreCase(status.getResponseCode()))
											{
												for (final StatusResponseMessageDTO statRes : status.getStatusMessageList())
												{
													if ((MarketplacecommerceservicesConstants.FINEJEW_ORDER_RETURN).equalsIgnoreCase(statRes
															.getStatusDescription()))
													{
														statRes
																.setStatusDescription(MarketplacecommerceservicesConstants.FINEJEW_SELFCOURIER_ERRORMSG);
													}
												}

											}
										}
									}
								}
							}
							//TISPRDT-2546 ends
						}
					}
					orderTrackingWsDTO.setProducts(orderproductdtos);
					orderTrackingWsDTO.setIsPickupUpdatable(isPickUpButtonEditable(orderDetail));
					orderTrackingWsDTO.setStatusDisplay(orderDetail.getStatusDisplay());
					orderTrackingWsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				}
			}
		}
		
	 
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9300);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9300);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9300);
		}

		return orderTrackingWsDTO;
	}


	/**
	 * @param entry
	 * @param orderModel
	 */
	private String getAwbPopupLink(final OrderEntryData entry, final String subOrderCode)
	{
		try
		{
			final List<ReturnRequestModel> returnRequestModelList = cancelReturnFacade.getListOfReturnRequest(subOrderCode);
			if (null != returnRequestModelList && returnRequestModelList.size() > 0)
			{
				for (final ReturnRequestModel mm : returnRequestModelList)
				{
					for (final ReturnEntryModel mmmodel : mm.getReturnEntries())
					{
						if (entry.getTransactionId().equalsIgnoreCase(mmmodel.getOrderEntry().getTransactionID()))
						{
							if (null != mm.getTypeofreturn() && null != mm.getTypeofreturn().getCode())
							{
								return mm.getTypeofreturn().getCode();
							}
						}
					}

				}
			}
		}
		catch (final Exception exception)
		{
			LOG.error("Exception Oucer Get getAwbPopupLink DefaultGetOrderDetailsFacadeImpl");

		}
		return null;
	}

	/**
	 * @param orderModel
	 */
	private String getSelfCourierDocumentUrl(final HttpServletRequest request, final String orderCode, final String transactionID)
	{
		try {


			String scheme = request.getScheme();
			String serverName = request.getServerName();
			String portNumber = String.valueOf(request.getServerPort());
			StringBuilder sb = new StringBuilder(scheme);
			sb.append(MarketplaceFacadesConstants.COLON);
			sb.append(MarketplaceFacadesConstants.FORWARD_SLASHES);
			sb.append(serverName);
			if (null != portNumber)
			{
				sb.append(MarketplaceFacadesConstants.COLON);
				sb.append(portNumber);
			}
			sb.append(MarketplaceFacadesConstants.RETURN_SELF_COURIER_FILE_DOWNLOAD_URL);
			sb.append(orderCode);
			sb.append(MarketplaceFacadesConstants.AMPERSAND);
			sb.append(MarketplaceFacadesConstants.TRANSACTION_ID);
			sb.append(MarketplaceFacadesConstants.EQUALS_TO);
			sb.append(transactionID);
			String SelfCourierDocumentLink = String.valueOf(sb);
			if(LOG.isDebugEnabled()) {


				LOG.debug("Self Courier return file download location for transaction id "+transactionID+" with order code  "+orderCode+" is "+SelfCourierDocumentLink);


			}
			return SelfCourierDocumentLink;

		}
		catch (final Exception expection)
		{
			LOG.error("Exception Oucer Get fileDownloadLocation DefaultGetOrderDetailsFacadeImpl");
		}

		return null;
	}

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
							if ((trackModel.getStatusCode().equalsIgnoreCase(ConsignmentStatus.REFUND_INITIATED.getCode()) || trackModel
									.getStatusCode().equalsIgnoreCase(ConsignmentStatus.REFUND_IN_PROGRESS.getCode())))
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
							if ((trackModel.getStatusCode().equalsIgnoreCase(ConsignmentStatus.REFUND_INITIATED.getCode()) || trackModel
									.getStatusCode().equalsIgnoreCase(ConsignmentStatus.REFUND_IN_PROGRESS.getCode())))
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

	/**
	 * @param statusResponse
	 * @param consignment
	 * @param awbEnabled
	 * @param reverseawbEnabled
	 * @return statusMessages
	 */
	public List<StatusResponseDTO> setStatusResponse(final List<AWBResponseData> statusResponse,
			final ConsignmentModel consignment, final boolean awbEnabled, final boolean reverseawbEnabled)
	{
		final List<StatusResponseDTO> statusMessages = new ArrayList<StatusResponseDTO>();
		List<StatusResponseMessageDTO> statusMessageList = null;
		AWBResponseData responseData = null;
		for (final AWBResponseData resp : statusResponse)
		{
			final StatusResponseDTO statusMessage = new StatusResponseDTO();
			statusMessageList = new ArrayList<StatusResponseMessageDTO>();
			statusMessage.setCurrentFlag(true);
			statusMessage.setResponseCode(resp.getResponseCode());
			statusMessage.setShipmentStatus(resp.getShipmentStatus());
			if (resp.getStatusRecords().size() > 0)
			{
				for (final StatusRecordData statusResp : resp.getStatusRecords())
				{
					final StatusResponseMessageDTO statusRespMessage = new StatusResponseMessageDTO();
					statusRespMessage.setDate(statusResp.getDate());
					statusRespMessage.setTime(statusResp.getTime());
					statusRespMessage.setLocation(statusResp.getLocation());
					statusRespMessage.setStatusDescription(statusResp.getStatusDescription());
					statusMessageList.add(statusRespMessage);
				}
			}
			if (null != consignment && null != consignment.getTrackingID() && null != consignment.getCarrier() && awbEnabled)
			{
				responseData = mplOrderService.prepAwbStatus(consignment.getTrackingID(), consignment.getCarrier());
				for (final StatusRecordData statusResp : responseData.getStatusRecords())
				{
					final StatusResponseMessageDTO statusRespMessage = new StatusResponseMessageDTO();
					statusRespMessage.setDate(statusResp.getDate());
					statusRespMessage.setTime(statusResp.getTime());
					statusRespMessage.setLocation(statusResp.getLocation());
					statusRespMessage.setStatusDescription(statusResp.getStatusDescription());
					statusMessageList.add(statusRespMessage);
				}
			}
			if (null != consignment && null != consignment.getReturnAWBNum() && null != consignment.getReturnCarrier()
					&& reverseawbEnabled)
			{
				responseData = mplOrderService.prepAwbStatus(consignment.getReturnAWBNum(), consignment.getReturnCarrier());
				for (final StatusRecordData statusResp : responseData.getStatusRecords())
				{
					final StatusResponseMessageDTO statusRespMessage = new StatusResponseMessageDTO();
					statusRespMessage.setDate(statusResp.getDate());
					statusRespMessage.setTime(statusResp.getTime());
					statusRespMessage.setLocation(statusResp.getLocation());
					statusRespMessage.setStatusDescription(statusResp.getStatusDescription());
					statusMessageList.add(statusRespMessage);
				}
			}
			statusMessage.setStatusMessageList(statusMessageList);
			statusMessages.add(statusMessage);

		}
		return statusMessages;
	}

	/**
	 * @Description set Mobile end tracking message
	 * @param returnMap
	 * @param consignment
	 * @return responseList
	 */
	public Map<String, StatusResponseListDTO> setStatusDisplayMessage(final Map<String, List<AWBResponseData>> returnMap,
			final ConsignmentModel consignment)

	{
		StatusResponseListDTO responseList = new StatusResponseListDTO();
		final Map<String, StatusResponseListDTO> displayMsg = new HashMap<>();
		List<StatusResponseDTO> statusMessages = null;
		List<AWBResponseData> statusResponse = null;
		try
		{
			//final boolean flag = false; TPR-815
			if (returnMap.get(MarketplaceFacadesConstants.PAYMENT) != null)
			{
				responseList = new StatusResponseListDTO();
				statusMessages = new ArrayList<StatusResponseDTO>();
				statusResponse = returnMap.get(MarketplaceFacadesConstants.PAYMENT);
				statusMessages = setStatusResponse(statusResponse, consignment, false, false);
				responseList.setStatusList(statusMessages);
				if (statusMessages.size() > 0)
				{
					displayMsg.put(MarketplaceFacadesConstants.PAYMENT, responseList);
				}
			}
			//final boolean flag = false;
			if (returnMap.get(MarketplaceFacadesConstants.APPROVED) != null)
			{
				responseList = new StatusResponseListDTO();
				statusMessages = new ArrayList<StatusResponseDTO>();
				statusResponse = returnMap.get(MarketplaceFacadesConstants.APPROVED);
				statusMessages = setStatusResponse(statusResponse, consignment, false, false);
				responseList.setStatusList(statusMessages);
				if (statusMessages.size() > 0)
				{
					displayMsg.put(MarketplaceFacadesConstants.APPROVED, responseList);
				}
			}
			if (returnMap.get(MarketplaceFacadesConstants.PROCESSING) != null)
			{
				responseList = new StatusResponseListDTO();
				statusMessages = new ArrayList<StatusResponseDTO>();
				statusResponse = returnMap.get(MarketplaceFacadesConstants.PROCESSING);
				statusMessages = setStatusResponse(statusResponse, consignment, false, false);
				responseList.setStatusList(statusMessages);
				if (statusMessages.size() > 0)
				{
					displayMsg.put(MarketplaceFacadesConstants.PROCESSING, responseList);
				}
			}
			if (returnMap.get(MarketplaceFacadesConstants.SHIPPING) != null)
			{
				responseList = new StatusResponseListDTO();
				statusMessages = new ArrayList<StatusResponseDTO>();
				statusResponse = returnMap.get(MarketplaceFacadesConstants.SHIPPING);
				statusMessages = setStatusResponse(statusResponse, consignment, true, false);
				responseList.setStatusList(statusMessages);
				if (statusMessages.size() > 0)
				{
					displayMsg.put(MarketplaceFacadesConstants.SHIPPING, responseList);
				}
			}
			if (returnMap.get(MarketplaceFacadesConstants.CANCEL) != null)
			{
				responseList = new StatusResponseListDTO();
				statusMessages = new ArrayList<StatusResponseDTO>();
				statusResponse = returnMap.get(MarketplaceFacadesConstants.CANCEL);
				statusMessages = setStatusResponse(statusResponse, consignment, false, false);
				responseList.setStatusList(statusMessages);
				if (statusMessages.size() > 0)
				{
					displayMsg.put(MarketplaceFacadesConstants.CANCEL, responseList);
				}
			}
			if (returnMap.get(MarketplaceFacadesConstants.RETURN) != null)
			{
				responseList = new StatusResponseListDTO();
				statusMessages = new ArrayList<StatusResponseDTO>();
				statusResponse = returnMap.get(MarketplaceFacadesConstants.RETURN);
				statusMessages = setStatusResponse(statusResponse, consignment, false, true);
				responseList.setStatusList(statusMessages);
				if (statusMessages.size() > 0)
				{
					displayMsg.put(MarketplaceFacadesConstants.RETURN, responseList);
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
		}

		return displayMsg;
	}


	@Override
	public List<OrderProductWsDTO> getOrderdetailsForApp(final String orderCode)
	{
		OrderProductWsDTO orderproductdto = null;
		OrderData orderDetails = null;
		OrderModel orderModel = null;
		final List<OrderProductWsDTO> orderproductdtos = new ArrayList<OrderProductWsDTO>();

		try
		{
			if (null != orderCode)
			{
				orderDetails = mplCheckoutFacade.getOrderDetailsForCode(orderCode);
				if (null != orderDetails && StringUtils.isNotEmpty(orderDetails.getType())
						&& orderDetails.getType().equalsIgnoreCase("SubOrder"))
				{

					for (final ConsignmentData consignmentData : orderDetails.getConsignments())
					{
						if (consignmentData.getStatus() != null && consignmentData.getStatus().getCode() == "DELIVERED")
						{

							orderModel = orderModelService.getOrder(orderDetails.getCode());


							for (final OrderEntryData entry : orderDetails.getEntries())
							{
								List<String> parentTransactionIds = new ArrayList<>();
								orderproductdto = new OrderProductWsDTO();


								//seller order no
								orderproductdto.setSellerorderno(orderDetails.getCode());


								final ProductData product = entry.getProduct();

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

								}
								if (null != entry.getAssociatedItems())
								{
									orderproductdto.setAssociatedProducts(entry.getAssociatedItems());
								}


								SellerInformationModel sellerInfoModel = null;
								if (StringUtils.isNotEmpty(entry.getSelectedUssid()))
								{
									sellerInfoModel = getMplSellerInformationService().getSellerDetail(entry.getSelectedUssid());
								}
								if (sellerInfoModel != null
										&& sellerInfoModel.getRichAttribute() != null
										&& ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0).getDeliveryFulfillModes() != null)
								{

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

										}
										else
										{
											orderproductdto.setUSSID(MarketplacecommerceservicesConstants.NA);

										}

									}

									if (entry.getTransactionId() != null)
									{
										orderproductdto.setTransactionId(entry.getTransactionId());
									}
									else
									{
										orderproductdto.setTransactionId(MarketplacecommerceservicesConstants.EMPTY);
									}

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

									orderproductdtos.add(orderproductdto);
								}
							}
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
		return orderproductdtos;
	}

	@Override
	public List<OrderProductWsDTO> getOrderdetailsForApp(final String orderCode, final String transactionId,
			final String returnCancelFlag)
	{

		OrderData subOrderDetail = null;
		OrderEntryData orderEntry = new OrderEntryData();
		List<OrderEntryData> returnOrderEntrys = null;
		OrderModel subOrderModel = null;
		boolean cancelStatusFlag = false;
		List<OrderProductWsDTO> orderproductdtos = new ArrayList<OrderProductWsDTO>();
		String consignmentStatus = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			subOrderModel = orderModelService.getOrder(orderCode);
			if (null != subOrderModel)
			{
				subOrderDetail = mplCheckoutFacade.getOrderDetailsForCode(subOrderModel);
				if (null != subOrderDetail && StringUtils.isNotEmpty(subOrderDetail.getType())
						&& subOrderDetail.getType().equalsIgnoreCase("SubOrder"))
				{
					final List<OrderEntryData> subOrderEntries = subOrderDetail.getEntries();
					for (final OrderEntryData entry : subOrderEntries)
					{
						if (entry.getTransactionId().equalsIgnoreCase(transactionId))
						{
							orderEntry = entry;
							returnOrderEntrys = cancelReturnFacade.associatedEntriesData(subOrderModel, transactionId);
						}
					}

				}

				if (returnCancelFlag.equalsIgnoreCase("R") && orderEntry.getConsignment() != null)
				{
					consignmentStatus = orderEntry.getConsignment().getStatus().getCode();

					if (consignmentStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.DELIVERED)
							|| consignmentStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.ORDER_COLLECTED))
					{
						orderproductdtos = setProductDetails(returnOrderEntrys, subOrderDetail, subOrderModel);
					}
				}
				else if (returnCancelFlag.equalsIgnoreCase("C"))
				{
					if (null == orderEntry.getConsignment() && orderEntry.getQuantity().longValue() != 0)
					{
						if (subOrderDetail.getStatus() != null)
						{
							cancelStatusFlag = defaultmplOrderFacade.checkCancelStatus(subOrderDetail.getStatus().getCode(),
									MarketplacecommerceservicesConstants.CANCEL_ORDER_STATUS);
						}
					}
					else if (null != orderEntry.getConsignment() && null != orderEntry.getConsignment().getStatus())
					{
						consignmentStatus = orderEntry.getConsignment().getStatus().getCode();
						cancelStatusFlag = defaultmplOrderFacade.checkCancelStatus(consignmentStatus,
								MarketplacecommerceservicesConstants.CANCEL_STATUS);
					}


					if (cancelStatusFlag)
					{
						orderproductdtos = setProductDetails(returnOrderEntrys, subOrderDetail, subOrderModel);
					}

				}
				//if (subOrderDetail.getStatus() != null
				//	&& mplOrderFacade.checkCancelStatus(orderCode, MarketplacecommerceservicesConstants.CANCEL_ORDER_STATUS))

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
		return orderproductdtos;
	}

	public List<OrderProductWsDTO> setProductDetails(final List<OrderEntryData> returnOrderEntry, final OrderData orderDetail,
			final OrderModel subOrderModel)
	{
		final List<OrderProductWsDTO> orderproductdtos = new ArrayList<>();
		OrderProductWsDTO orderproductdto = null;

		for (final OrderEntryData entry : returnOrderEntry)
		{
			List<String> parentTransactionIds = new ArrayList<>();
			orderproductdto = new OrderProductWsDTO();

			//seller order no
			orderproductdto.setSellerorderno(orderDetail.getCode());
			final ProductData product = entry.getProduct();
			String ussid = "";
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

			}
			if (null != entry.getAssociatedItems())
			{
				orderproductdto.setAssociatedProducts(entry.getAssociatedItems());
			}


			SellerInformationModel sellerInfoModel = null;
			if (StringUtils.isNotEmpty(entry.getSelectedUssid()))
			{
				if ((MarketplacecommerceservicesConstants.FINEJEWELLERY).equalsIgnoreCase(product.getRootCategory()))
				{
					final List<JewelleryInformationModel> jewelleryInfo = jewelleryService.getJewelleryInfoByUssid(entry
							.getSelectedUssid());
					if (CollectionUtils.isNotEmpty(jewelleryInfo))
					{
						sellerInfoModel = getMplSellerInformationService().getSellerDetail(jewelleryInfo.get(0).getPCMUSSID());
						ussid = jewelleryInfo.get(0).getUSSID();
					}
					else
					{
						LOG.error("No entry in JewelleryInformationModel for ussid " + entry.getSelectedUssid());
					}
				}
				else
				{
					sellerInfoModel = getMplSellerInformationService().getSellerDetail(entry.getSelectedUssid());
					ussid = sellerInfoModel.getUSSID();
				}

			}
			if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null
					&& ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0).getDeliveryFulfillModes() != null)
			{

				//Seller info
				//if (sellerInfoModel.getUSSID() != null && sellerInfoModel.getUSSID().equalsIgnoreCase(entry.getSelectedUssid()))
				if (sellerInfoModel.getUSSID() != null && ussid.equalsIgnoreCase(entry.getSelectedUssid()))
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

					}
					else
					{
						orderproductdto.setUSSID(MarketplacecommerceservicesConstants.NA);

					}

				}

				if (entry.getTransactionId() != null)
				{
					orderproductdto.setTransactionId(entry.getTransactionId());
				}
				else
				{
					orderproductdto.setTransactionId(MarketplacecommerceservicesConstants.EMPTY);
				}

				for (final AbstractOrderEntryModel orderEntry : subOrderModel.getEntries())
				{
					if (entry.getTransactionId().equalsIgnoreCase(orderEntry.getTransactionID())
							&& null != orderEntry.getParentTransactionID())
					{
						parentTransactionIds = Arrays.asList(orderEntry.getParentTransactionID().split("\\s*,\\s*"));
						break;
					}
				}
				orderproductdto.setParentTransactionId(parentTransactionIds);

				orderproductdtos.add(orderproductdto);
			}
		}
		return orderproductdtos;
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
