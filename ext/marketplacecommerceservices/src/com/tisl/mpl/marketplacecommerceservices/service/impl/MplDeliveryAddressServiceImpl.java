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

//import com.sap.security.core.server.csi.util.StringUtils;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao;
/*import com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao;*/
import com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.MplDeliveryAddressDao;
//import com.tis.mpl.facade.changedelivery.Impl.ChangeDeliveryAddressFacadeImpl;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryAddressService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author Techouts
 *
 */
public class MplDeliveryAddressServiceImpl implements MplDeliveryAddressService
{

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
	private static final Logger LOG = Logger.getLogger(MplDeliveryAddressServiceImpl.class);



	@Override
	public boolean isDeliveryAddressChangable(final String orderId)
	{
		final List<String> ChangableOrdeStatus = Arrays.asList(OrderStatus.PAYMENT_SUCCESSFUL.getCode(),
				OrderStatus.ORDER_ALLOCATED.getCode(), OrderStatus.PICK_LIST_GENERATED.getCode(),
				OrderStatus.ORDER_REALLOCATED.getCode(), OrderStatus.PICK_CONFIRMED.getCode(), OrderStatus.ORDER_REJECTED.getCode(),
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
							if (null != sellerInfoModel && null != sellerInfoModel.getSellerMaster()
									&& null != sellerInfoModel.getSellerMaster().getIsCDAllowed())
							{
								isCdAllowed = sellerInfoModel.getSellerMaster().getIsCDAllowed();
								if (LOG.isDebugEnabled())
								{
									LOG.debug("Is CD allowed for seller " + sellerInfoModel.getSellerID() + " " + isCdAllowed);
								}
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
@Override
	public void saveChangeDeliveryRequests(final String orderId, final String status)
	{
		final OrderModel order = orderModelDao.getOrder(orderId);
		try
		{
			if (null != order.getChangeDeliveryTotalRequests())
			{
				final int previousRequests = order.getChangeDeliveryTotalRequests().intValue();
				order.setChangeDeliveryTotalRequests(Integer.valueOf(previousRequests + 1));
			}
			else
			{
				order.setChangeDeliveryTotalRequests(Integer.valueOf(1));
			}
			if (status.equalsIgnoreCase(MarketplacecommerceservicesConstants.FAILED))
			{
				final int previousRejects = order.getChangeDeliveryTotalRequests().intValue();
				order.setChangeDeliveryTotalRequests(Integer.valueOf(previousRejects + 1));
			}
			modelService.save(order);
		}
		catch (final ModelSavingException e)
		{
			LOG.error("Model saving Exception while saveing change delivery requests for the order id :" + order.getCode());
		}
		catch (final Exception e)
		{
			LOG.error("Exception while saveing change delivery requests for the order id :" + order.getCode());
		}
	}

	@Override
	public boolean saveDeliveryAddress(AddressModel newAddressModel, OrderModel orderModel)
	{
		LOG.info("Inside saveDeliveryAddress Method");
		boolean isDeliveryAddressChanged = false;
		try
		{

			if (newAddressModel != null)
			{
				if (orderModel != null)
				{
					UserModel user = orderModel.getUser();
					List<AddressModel> deliveryAddressesList = new ArrayList<AddressModel>();
					Collection<AddressModel> customerAddressesList = new ArrayList<AddressModel>();
					Collection<AddressModel> deliveryAddresses = orderModel.getDeliveryAddresses();
					AddressModel deliveryAddress = orderModel.getDeliveryAddress();
					boolean isEligibleScheduledDelivery = checkScheduledDeliveryForOrder(orderModel);
					deliveryAddress = changedDeliveryAddress(deliveryAddress, newAddressModel);
					if (null != deliveryAddresses && !deliveryAddresses.isEmpty())
					{
						deliveryAddressesList.addAll(deliveryAddresses);
					}
					if (null != user.getAddresses())
					{
						customerAddressesList.addAll(user.getAddresses());
					}
					deliveryAddressesList.add(deliveryAddress);
					customerAddressesList.add(deliveryAddress);
					orderModel.setDeliveryAddress(deliveryAddress);
					for (OrderModel childOrder : orderModel.getChildOrders())
					{
						childOrder.setDeliveryAddress(deliveryAddress);
						childOrder.setDeliveryAddresses(deliveryAddressesList);
						if (isEligibleScheduledDelivery)
						{
							for (AbstractOrderEntryModel abstractOrderEntryModel : childOrder.getEntries())
							{
								modelService.save(abstractOrderEntryModel);
							}
						}
						modelService.save(childOrder);
					}
					if (null != orderModel.getChangeDeliveryTotalRequests())
					{
					 orderModel.setChangeDeliveryTotalRequests(new Integer(orderModel.getChangeDeliveryTotalRequests().intValue() + 1));
					}
					else
					{
						orderModel.setChangeDeliveryTotalRequests(Integer.valueOf(1));
					}
					user.setAddresses(customerAddressesList);
					orderModel.setDeliveryAddresses(deliveryAddressesList);
					modelService.save(orderModel);
					modelService.save(user);
					sessionService.removeAttribute(MarketplacecommerceservicesConstants.CHANGE_DELIVERY_ADDRESS);
					isDeliveryAddressChanged = true;
				}
			}
		}
		catch (final ModelSavingException expection)
		{
			LOG.error("ModelSavingException Exception while saving changed delivery Address" + expection.getMessage());
		}
		return isDeliveryAddressChanged;
	}


	@Override
	public void deliveryAddressFailureRequest(OrderModel orderModel)
	{
		try
		{
			
			if (null != orderModel.getChangeDeliveryTotalRequests())
			{
				orderModel.setChangeDeliveryTotalRequests(new Integer(orderModel.getChangeDeliveryTotalRequests().intValue() + 1));
			}
			else
			{
				orderModel.setChangeDeliveryTotalRequests(Integer.valueOf(1));
			}

			if (null != orderModel.getChangeDeliveryRejectsCount())
			{
				orderModel.setChangeDeliveryRejectsCount(new Integer(orderModel.getChangeDeliveryRejectsCount().intValue() + 1));
			}
			else
			{
				orderModel.setChangeDeliveryRejectsCount(Integer.valueOf(1));
			}

			modelService.save(orderModel);
			sessionService.removeAttribute(MarketplacecommerceservicesConstants.CHANGE_DELIVERY_ADDRESS);
		}
		catch (final ModelSavingException e)
		{
			LOG.error("ModelSavingException while setting status for TemporaryAddress" + e.getMessage());
		}
		catch (NullPointerException nullPointerException)
		{
			LOG.error("Exception occure while setting totalRequest" + nullPointerException.getMessage());
		}
	}

	
   	@Override
	public boolean checkScheduledDeliveryForOrder(OrderModel orderModel)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("MplDeliveryAddressservicesImpl::Chekcing Order are  Eligible for ReScheduledDelivery Date");
		}
		boolean isEligibleScheduledDelivery = false;
		List<OrderModel> chaidOrderList = orderModel.getChildOrders();
		for (OrderModel subOrder : chaidOrderList)
		{
			for (AbstractOrderEntryModel abstractOrderEntry : subOrder.getEntries())
			{
				if (!abstractOrderEntry.getMplDeliveryMode().getDeliveryMode().getCode()
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
				{
					if (abstractOrderEntry.getEdScheduledDate() != null)
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

	public AddressModel changedDeliveryAddress(AddressModel oldAddress, AddressModel newDeliveryAddress)
	{
		if (newDeliveryAddress != null)
		{
			if (newDeliveryAddress.getFirstname() != null)
			{
				oldAddress.setFirstname(newDeliveryAddress.getFirstname());
			}
			if (newDeliveryAddress.getLastname() != null)
			{
				oldAddress.setLastname(newDeliveryAddress.getLastname());
			}
			if (newDeliveryAddress.getStreetname() != null)
			{
				oldAddress.setStreetname(newDeliveryAddress.getStreetname());
			}
			if (newDeliveryAddress.getStreetnumber() != null)
			{
				oldAddress.setStreetnumber(newDeliveryAddress.getStreetnumber());
			}
			if (newDeliveryAddress.getAddressLine3() != null)
			{
				oldAddress.setAddressLine3(newDeliveryAddress.getAddressLine3());
			}
			if (newDeliveryAddress.getLandmark() != null)
			{
				oldAddress.setLandmark(newDeliveryAddress.getLandmark());
			}
			if (newDeliveryAddress.getFirstname() != null)
			{
				oldAddress.setFirstname(newDeliveryAddress.getFirstname());
			}
			if (newDeliveryAddress.getState() != null)
			{
				oldAddress.setState(newDeliveryAddress.getState());
			}
			if (newDeliveryAddress.getDistrict() != null)
			{
				oldAddress.setDistrict(newDeliveryAddress.getDistrict());
			}
			if (newDeliveryAddress.getPostalcode() != null)
			{
				oldAddress.setPostalcode(newDeliveryAddress.getPostalcode());
			}
			if (newDeliveryAddress.getCity() != null)
			{
				oldAddress.setCity(newDeliveryAddress.getCity());
			}
			if (newDeliveryAddress.getPhone1() != null)
			{
				oldAddress.setPhone1(newDeliveryAddress.getPhone1());
			}
		}
		return oldAddress;
	}

	
	/**
	 * Give List Of OrderModel Based one Mode
	 */
	@Override
	public List<OrderModel> getOrderModelList(Date fromDate, Date toDate)
	{
		return mplDeliveryAddressDao.getOrderModelList(fromDate, toDate);
	}



}