
/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.domain.changedeliveryaddress.TransactionSDDto;
//import com.sap.security.core.server.csi.util.StringUtils;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplDeliveryAddressInfoModel;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao;
import com.tisl.mpl.marketplacecommerceservices.daos.changedeliveryaddress.MplDeliveryAddressDao;
//import com.tis.mpl.facade.changedelivery.Impl.ChangeDeliveryAddressFacadeImpl;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryAddressService;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerMasterService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerMasterModel;


/*import com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao;*/


/**
 * @author Techouts
 *
 */
public class MplDeliveryAddressServiceImpl implements MplDeliveryAddressService
{

	/**
	 *
	 */
	private static final String LOG_ERROR_MSG_MODEL_SAVING_EXCEPTION_WHILE_SETTING_STATUS = "ModelSavingException while setting status ";

	@Autowired
	private MplSellerInformationService mplSellerInformationService;

	@Autowired
	private MplDeliveryAddressDao mplDeliveryAddressDao;

	@Autowired
	private ModelService modelService;

	@Autowired
	OrderModelDao orderModelDao;

	@Autowired
	SessionService sessionService;
	@Autowired
	MplSellerMasterService sellerMasterService;

	@Autowired
	private MplDeliveryCostService mplDeliveryCostService;

	private static final Logger LOG = Logger.getLogger(MplDeliveryAddressServiceImpl.class);



	@Override
	public boolean isDeliveryAddressChangable(final String orderId)
	{
		final List<String> ChangableOrdeStatus = Arrays.asList(OrderStatus.PAYMENT_SUCCESSFUL.getCode(),
				OrderStatus.ORDER_ALLOCATED.getCode(), OrderStatus.PICK_LIST_GENERATED.getCode(),
				OrderStatus.ORDER_REALLOCATED.getCode(), OrderStatus.ORDER_REJECTED.getCode(),
				OrderStatus.PENDING_SELLER_ASSIGNMENT.getCode());
		boolean isAddressChangable = false;

		try
		{
			LOG.info("Inside isDeliveryAddressChangable  method  , Checking for orderID " + orderId);
			final OrderModel orderModel = orderModelDao.getOrderModel(orderId);
			if (null != orderModel.getChildOrders())
			{
				for (final OrderModel sellerOrder : orderModel.getChildOrders())
				{
					for (final AbstractOrderEntryModel entry : sellerOrder.getEntries())
					{
						String deliveryMode = StringUtils.EMPTY;
						String entryStatus = orderModel.getStatus().getCode();
						String isCdAllowed = MarketplacecommerceservicesConstants.Y;

						if (null != entry && null != entry.getMplDeliveryMode() && null != entry.getMplDeliveryMode().getDeliveryMode())
						{
							deliveryMode = entry.getMplDeliveryMode().getDeliveryMode().getCode();
						}
						if (null != entry && null != entry.getSelectedUSSID())
						{
							final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(entry
									.getSelectedUSSID());
							if (null != sellerInfoModel)
							{
								final SellerMasterModel sellerMasterInfo = sellerMasterService.getSellerMaster(sellerInfoModel
										.getSellerID());
								if (null != sellerMasterInfo && null != sellerMasterInfo.getIsCDAllowed())
								{
									isCdAllowed = sellerMasterInfo.getIsCDAllowed();
									if (LOG.isDebugEnabled())
									{
										LOG.debug("Is CD allowed for seller " + sellerInfoModel.getSellerID() + " " + isCdAllowed);
									}
								}
								else
								{
									isCdAllowed = MarketplacecommerceservicesConstants.N;
								}
							}
							else
							{
								isCdAllowed = MarketplacecommerceservicesConstants.N;
							}


						}
						if (!deliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)
								&& entry.getQuantity().longValue() > 0)
						{
							if (isCdAllowed.equalsIgnoreCase(MarketplacecommerceservicesConstants.N))
							{
								isAddressChangable = false;
								LOG.debug("Delivery Address changable : " + isAddressChangable);
								return isAddressChangable;
							}

							if (CollectionUtils.isNotEmpty(entry.getConsignmentEntries()))
							{
								final ConsignmentStatus consignmentStatus = entry.getConsignmentEntries().iterator().next()
										.getConsignment().getStatus();
								LOG.debug("Consignment status for entry " + entry.getTransactionID() + " is"
										+ consignmentStatus.getCode());
								entryStatus = consignmentStatus.getCode();
							}
							else
							{
								LOG.debug("No Consignment entries found for  " + entry.getTransactionID());
							}
							if (!ChangableOrdeStatus.contains(entryStatus))
							{
								isAddressChangable = false;
								LOG.debug("Delivery Address changable : " + isAddressChangable);
								return isAddressChangable;
							}
							else
							{
								isAddressChangable = true;
							}
						}
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception occurred while checking whether address is changable or not for order id " + orderId + " "
					+ e.getCause());
		}
		LOG.debug("Delivery Address changable : " + isAddressChangable);
		return isAddressChangable;
	}

	/**
	 * This method is used to save the changed delivery Address
	 *
	 * @param newAddressModel
	 * @param orderModel
	 * @return boolean
	 */

	@Override
	public boolean saveDeliveryAddress(final AddressModel newAddressModel, final OrderModel orderModel, final boolean isNewAddress)
	{
		LOG.info("Inside saveDeliveryAddress Method");
		boolean isDeliveryAddressChanged = false;
		try
		{

			if (newAddressModel != null)
			{
				if (orderModel != null)
				{

					//Change Ed to HD Delivery Mode
					if (!newAddressModel.getPostalcode().equalsIgnoreCase(orderModel.getDeliveryAddress().getPostalcode()))
					{
						convertEDToHDDeliveryMode(orderModel);
					}

					final List<AddressModel> deliveryAddressesList = new ArrayList<AddressModel>();
					final Collection<AddressModel> deliveryAddresses = orderModel.getDeliveryAddresses();
					newAddressModel.setOwner(orderModel.getDeliveryAddress().getOwner());
					if (null != deliveryAddresses && !deliveryAddresses.isEmpty())
					{
						deliveryAddressesList.addAll(deliveryAddresses);
					}

					deliveryAddressesList.add(newAddressModel);
					orderModel.setDeliveryAddress(newAddressModel);
					orderModel.setDeliveryAddresses(deliveryAddressesList);
					modelService.save(orderModel);
					for (final OrderModel childOrder : orderModel.getChildOrders())
					{
						childOrder.setDeliveryAddress(newAddressModel);
						childOrder.setDeliveryAddresses(deliveryAddressesList);
						modelService.save(childOrder);
					}
					isDeliveryAddressChanged = true;

					//Mpl Delivery Address Report
					final MplDeliveryAddressInfoModel mplDeliveryAddressInfoModel = mplDeliveryAddressDao
							.getMplDeliveryAddressReportModelByOrderId(orderModel.getCode());
					if (mplDeliveryAddressInfoModel != null)
					{
						mplDeliveryAddressInfoModel.setChangeDeliveryTotalRequests(Integer.valueOf(mplDeliveryAddressInfoModel
								.getChangeDeliveryTotalRequests().intValue() + 1));
						modelService.save(mplDeliveryAddressInfoModel);
					}
					else
					{
						final MplDeliveryAddressInfoModel newDeliveryAddressInfoModel = modelService
								.create(MplDeliveryAddressInfoModel.class);
						newDeliveryAddressInfoModel.setChangeDeliveryTotalRequests(Integer.valueOf(1));
						newDeliveryAddressInfoModel.setOrderId(orderModel.getCode());
						modelService.save(newDeliveryAddressInfoModel);
					}

					final UserModel user = orderModel.getUser();
					final Collection<AddressModel> customerAddressesList = new ArrayList<AddressModel>();
					if (null != user && null != user.getAddresses())
					{
						customerAddressesList.addAll(user.getAddresses());
					}

					user.setAddresses(customerAddressesList);
					modelService.save(user);
					final boolean isEditAddress = getCustomerProfileAddress(customerAddressesList, newAddressModel, orderModel
							.getDeliveryAddress().getPk().toString());
					if (!isEditAddress)
					{
						customerAddressesList.add(newAddressModel);
						modelService.saveAll(customerAddressesList);
						user.setAddresses(customerAddressesList);
						modelService.save(user);

					}
					else
					{
						modelService.saveAll(customerAddressesList);
						user.setAddresses(customerAddressesList);
						modelService.save(user);
					}
					sessionService.removeAttribute(MarketplacecommerceservicesConstants.CHANGE_DELIVERY_ADDRESS);

				}
			}
		}
		catch (final ModelSavingException expection)
		{
			LOG.error("ModelSavingException Exception while saving changed delivery Address" + expection.getMessage());
		}
		return isDeliveryAddressChanged;
	}


	/**
	 * @param customerAddressesList
	 * @param newAddressModel
	 * @return
	 */
	private boolean getCustomerProfileAddress(final Collection<AddressModel> customerAddressesList,
			final AddressModel newAddressModel, final String addressID)
	{
		boolean isEditAddress = false;
		if (StringUtils.isNotBlank(addressID))
		{
			for (final AddressModel model : customerAddressesList)
			{
				if (model.getPk().toString().equalsIgnoreCase(addressID))
				{
					model.setFirstname(newAddressModel.getFirstname());
					model.setLastname(newAddressModel.getLastname());
					model.setStreetname(newAddressModel.getStreetname());
					model.setStreetnumber(newAddressModel.getStreetnumber());
					model.setAddressLine3(newAddressModel.getAddressLine3());
					model.setLandmark(newAddressModel.getLandmark());
					model.setTown(newAddressModel.getTown());
					model.setCity(newAddressModel.getCity());
					model.setState(newAddressModel.getState());
					model.setPostalcode(newAddressModel.getPostalcode());
					model.setPhone1(newAddressModel.getPhone1());
					model.setCellphone(newAddressModel.getCellphone());
					model.setPhone2(newAddressModel.getPhone2());
					isEditAddress = true;
				}
			}
		}
		else
		{
			return true;
		}
		return isEditAddress;
	}



	/**
	 * This method is used to save the failure requests of change delivery address
	 *
	 * @param orderModel
	 */

	@Override
	public void deliveryAddressFailureRequest(final OrderModel orderModel)
	{
		try
		{
			//Mpl Delivery Address Report
			final MplDeliveryAddressInfoModel mplDeliveryAddressInfoModel = mplDeliveryAddressDao
					.getMplDeliveryAddressReportModelByOrderId(orderModel.getCode());
			if (mplDeliveryAddressInfoModel != null)
			{
				mplDeliveryAddressInfoModel.setChangeDeliveryTotalRequests(Integer.valueOf(mplDeliveryAddressInfoModel
						.getChangeDeliveryTotalRequests().intValue() + 1));

				if (mplDeliveryAddressInfoModel.getChangeDeliveryRejectsCount() != null)
				{
					mplDeliveryAddressInfoModel.setChangeDeliveryRejectsCount(Integer.valueOf(mplDeliveryAddressInfoModel
							.getChangeDeliveryRejectsCount().intValue() + 1));
				}
				else
				{
					mplDeliveryAddressInfoModel.setChangeDeliveryRejectsCount(Integer.valueOf(1));
				}
				modelService.save(mplDeliveryAddressInfoModel);
			}
			else
			{
				final MplDeliveryAddressInfoModel newDeliveryAddressInfoModel = modelService
						.create(MplDeliveryAddressInfoModel.class);
				newDeliveryAddressInfoModel.setChangeDeliveryTotalRequests(Integer.valueOf(1));
				newDeliveryAddressInfoModel.setChangeDeliveryRejectsCount(Integer.valueOf(1));
				newDeliveryAddressInfoModel.setOrderId(orderModel.getCode());
				modelService.save(newDeliveryAddressInfoModel);
			}

			sessionService.removeAttribute(MarketplacecommerceservicesConstants.CHANGE_DELIVERY_ADDRESS);
		}
		catch (final ModelSavingException e)
		{
			LOG.error(LOG_ERROR_MSG_MODEL_SAVING_EXCEPTION_WHILE_SETTING_STATUS + e.getMessage());
		}
		catch (final NullPointerException nullPointerException)
		{
			LOG.error("Exception occure while setting totalRequest" + nullPointerException.getMessage());
		}
	}


	/**
	 * This method is used for , ehether the order is eligible for reScheduling Dates or not
	 */
	@Override
	public boolean checkScheduledDeliveryForOrder(final OrderModel orderModel)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("MplDeliveryAddressservicesImpl::Chekcing Order are  Eligible for ReScheduledDelivery Date");
		}
		boolean isEligibleScheduledDelivery = false;
		final List<OrderModel> chaidOrderList = orderModel.getChildOrders();
		for (final OrderModel subOrder : chaidOrderList)
		{
			for (final AbstractOrderEntryModel abstractOrderEntry : subOrder.getEntries())
			{
				if (!abstractOrderEntry.getMplDeliveryMode().getDeliveryMode().getCode()
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
				{
					if (StringUtils.isNotEmpty(abstractOrderEntry.getEdScheduledDate()))
					{
						isEligibleScheduledDelivery = true;
						break;
					}
				}
			}
			if (isEligibleScheduledDelivery)
			{
				break;
			}
		}
		return isEligibleScheduledDelivery;
	}


	/**
	 * Give List Of OrderModel Based one Mode
	 */
	@Override
	public List<MplDeliveryAddressInfoModel> getMplDeliveryAddressReportModels(final Date fromDate, final Date toDate)
	{
		return mplDeliveryAddressDao.getMplDeliveryAddressReportModels(fromDate, toDate);
	}

	/**
	 * This method used for save Date and time information into AbstractOrderEntryModel selected by customer
	 *
	 * @param transactionSDDtoList
	 * @param orderModel
	 */

	@Override
	public void saveSelectedDateAndTime(final OrderModel orderModel, final List<TransactionSDDto> transactionSDDtoList)
	{
		try
		{
			String ussID = null;
			for (final OrderModel subOrder : orderModel.getChildOrders())
			{
				for (final AbstractOrderEntryModel entryModel : subOrder.getEntries())
				{
					if (!MarketplacecommerceservicesConstants.CLICK_COLLECT.equalsIgnoreCase(entryModel.getMplDeliveryMode()
							.getDeliveryMode().getCode())
							&& entryModel.getEdScheduledDate() != null)
					{


						//get Entry related(OrderLine) Information
						final TransactionSDDto transactionSDDto = getEntryData(transactionSDDtoList, entryModel.getTransactionID());
						if (transactionSDDto != null)
						{
							//Save Transaction level  Entry model
							entryModel.setEdScheduledDate(transactionSDDto.getPickupDate());
							entryModel.setTimeSlotFrom(transactionSDDto.getTimeSlotFrom());
							entryModel.setTimeSlotTo(transactionSDDto.getTimeSlotTo());
							modelService.save(entryModel);
							if (!entryModel.getSelectedUSSID().equalsIgnoreCase(ussID))
							{
								saveMainOrderEntry(orderModel, transactionSDDto, entryModel.getSelectedUSSID());
								ussID = entryModel.getSelectedUSSID();
							}
						}

					}
				}
			}


		}
		catch (final ModelSavingException e)
		{
			LOG.error(LOG_ERROR_MSG_MODEL_SAVING_EXCEPTION_WHILE_SETTING_STATUS + e.getMessage());
		}
		catch (final NullPointerException nullPointerException)
		{
			LOG.error("Exception occure while setting " + nullPointerException.getMessage());
		}

	}

	//Save SelectedDateAndTime
	private void saveMainOrderEntry(final OrderModel orderModel, final TransactionSDDto transactionSDDto, final String ussID)
	{
		try
		{
			for (final AbstractOrderEntryModel entryModel : orderModel.getEntries())
			{
				if (StringUtils.isNotEmpty(entryModel.getSelectedUSSID()) && ussID.equalsIgnoreCase(entryModel.getSelectedUSSID()))
				{
					//Save Transaction level  Entry model
					entryModel.setEdScheduledDate(transactionSDDto.getPickupDate());
					entryModel.setTimeSlotFrom(transactionSDDto.getTimeSlotFrom());
					entryModel.setTimeSlotTo(transactionSDDto.getTimeSlotTo());
					modelService.save(entryModel);
				}
			}
		}
		catch (final ModelSavingException e)
		{
			LOG.error(LOG_ERROR_MSG_MODEL_SAVING_EXCEPTION_WHILE_SETTING_STATUS + e.getMessage());
		}
		catch (final NullPointerException nullPointerException)
		{
			LOG.error("Exception occure while setting " + nullPointerException.getMessage());
		}

	}


	/**
	 * This method used for check Entry(OrderLine) Related information
	 *
	 * @param transactionSDDtoList
	 * @param transactionID
	 */
	private TransactionSDDto getEntryData(final List<TransactionSDDto> transactionSDDtoList, final String transactionID)
	{
		for (final TransactionSDDto transactionSDDto : transactionSDDtoList)
		{
			if (StringUtils.isNotEmpty(transactionID) && StringUtils.isNotEmpty(transactionSDDto.getTransactionID()))
			{
				if (transactionSDDto.getTransactionID().equalsIgnoreCase(transactionID))
				{
					return transactionSDDto;
				}
			}
		}
		return null;
	}

	//convert Ed To HD Order Bug-ID TATA-679
	private void convertEDToHDDeliveryMode(final OrderModel orderModel)
	{
		try
		{
			for (final OrderModel subOrder : orderModel.getChildOrders())
			{
				for (final AbstractOrderEntryModel entry : subOrder.getEntries())
				{
					if (MarketplacecommerceservicesConstants.EXPRESS_DELIVERY.equalsIgnoreCase(entry.getMplDeliveryMode()
							.getDeliveryMode().getCode()))
					{
						final MplZoneDeliveryModeValueModel deliveryModel = mplDeliveryCostService.getDeliveryCost(
								MarketplacecommerceservicesConstants.HOME_DELIVERY, MarketplacecommerceservicesConstants.INR,
								entry.getSelectedUSSID());
						entry.setMplDeliveryMode(deliveryModel);
						entry.setIsEDtoHD(Boolean.TRUE);
						modelService.saveAll(entry);
					}

				}
			}

			for (final AbstractOrderEntryModel mainEntry : orderModel.getEntries())
			{
				if (MarketplacecommerceservicesConstants.EXPRESS_DELIVERY.equalsIgnoreCase(mainEntry.getMplDeliveryMode()
						.getDeliveryMode().getCode()))
				{
					final MplZoneDeliveryModeValueModel deliveryModel = mplDeliveryCostService.getDeliveryCost(
							MarketplacecommerceservicesConstants.HOME_DELIVERY, MarketplacecommerceservicesConstants.INR,
							mainEntry.getSelectedUSSID());
					mainEntry.setMplDeliveryMode(deliveryModel);
					modelService.saveAll(mainEntry);
				}
			}

		}
		catch (final ModelSavingException e)
		{
			LOG.error(LOG_ERROR_MSG_MODEL_SAVING_EXCEPTION_WHILE_SETTING_STATUS + e.getMessage());
		}
		catch (final NullPointerException nullPointerException)
		{
			LOG.error("Exception occure while setting " + nullPointerException.getMessage());
		}
	}

}