/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.impl;

import com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.MplDeliveryAddressDao;


/**
 * @author pankajk
 *
 */
public class MplDeliveryAddressDaoImpl implements MplDeliveryAddressDao
{
//{
//	private static final Logger LOG = Logger.getLogger(MplDeliveryAddressDaoImpl.class);
//	@Autowired
//	private FlexibleSearchService flexibleSearchService;
//	@Autowired
//	private ModelService modelService;
//
//	//	/***
//	//	 * OrderId Based On We will get TemproryAddressModel
//	//	 *
//	//	 * @param orderCode
//	//	 * @return TemproryAddressModel
//	//	 */
//	//
//	//	@Override
//	//	public void saveDeliveryAddress(final OrderModel orderModel, final AddressModel address)
//	//	{
//	//		try
//	//		{
//	//			orderModel.setDeliveryAddress(address);
//	//			OrderModel parentorder = orderModel.getParentReference();
//	//			UserModel user = orderModel.getUser();
//	//			parentorder.setDeliveryAddress(address);
//	//			List<AddressModel> deliveryAddressesList = new ArrayList<AddressModel>();
//	//			Collection<AddressModel> customerAddressesList = new ArrayList<AddressModel>();
//	//			Collection<AddressModel> deliveryAddresses = orderModel.getParentReference().getDeliveryAddresses();
//	//			if (null != deliveryAddresses)
//	//			{
//	//				deliveryAddressesList.addAll(deliveryAddresses);
//	//			}
//	//			if (null != user.getAddresses())
//	//			{
//	//				customerAddressesList.addAll(user.getAddresses());
//	//			}
//	//			if (null != address)
//	//			{
//	//				deliveryAddressesList.add(address);
//	//				customerAddressesList.add(address);
//	//			}
//	//			user.setAddresses(customerAddressesList);
//	//			orderModel.getParentReference().setDeliveryAddresses(deliveryAddressesList);
//	//			modelService.save(orderModel);
//	//			modelService.save(orderModel.getParentReference());
//	//			modelService.save(user);
//	//		}
//	//		catch (final ModelSavingException e)
//	//		{
//	//			LOG.debug("Model saving Exception" + e.getMessage());
//	//
//	//		}
//	//		catch (final Exception e)
//	//		{
//	//			LOG.debug("Exception while saving Address" + e.getMessage());
//	//		}
//	//	}
//
//	/***
//	 * OrderId Based On We will get Temporary AddressModel
//	 *
//	 *
//	 * @return TemproryAddressModel
//	 */
//	@Override
//	public TemproryAddressModel getTemporaryAddressModel(final String orderId)
//	{
//
//		try
//		{
//			final String queryString = "SELECT {o:" + TemproryAddressModel.PK + "} " + "FROM {" + TemproryAddressModel._TYPECODE
//					+ " AS o} " + "WHERE ({o:" + TemproryAddressModel.ORDERID + "})=?orderId" + " ORDER BY {o:"
//					+ TemproryAddressModel.CREATIONTIME + "} DESC";
//
//			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryString);
//			fQuery.addQueryParameter("orderId", orderId);
//			final SearchResult<TemproryAddressModel> searchResult = flexibleSearchService.search(fQuery);
//			final List<TemproryAddressModel> tempAddrlist = searchResult.getResult();
//			return !tempAddrlist.isEmpty() ? tempAddrlist.get(0) : null;
//		}
//		catch (final FlexibleSearchException e)
//		{
//			LOG.error(" FlexibleSearchException while getting the temprory Address " + e.getMessage());
//		}
//		catch (final Exception e)
//		{
//			LOG.error("Exception occurree getting the temparory address " + e.getMessage());
//		}
//		return null;
//	}
//
//	@Override
//	public List<TemproryAddressModel> getTemporaryAddressModelList(final String fromDate, final String toDate)
//	{
//		List<TemproryAddressModel> tempAddrlist = null;
//		try
//		{
//			final String SHORT_URL_REPORT_QUERY_BETWEEN_TWO_DATES = "SELECT {cdrm:" + TemproryAddressModel.PK + "}" + " FROM {"
//					+ TemproryAddressModel._TYPECODE + " AS cdrm} " + "WHERE " + "{cdrm:" + TemproryAddressModel.CREATIONTIME
//					+ "} between ?fromDate and ?toDate and" + "{cdrm:" + TemproryAddressModel.ISPROCESSED + "}  IS NOT NULL";
//
//			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SHORT_URL_REPORT_QUERY_BETWEEN_TWO_DATES);
//			fQuery.addQueryParameter("fromDate", fromDate);
//			fQuery.addQueryParameter("toDate", toDate);
//			final SearchResult<TemproryAddressModel> searchResult = flexibleSearchService.search(fQuery);
//			tempAddrlist = searchResult.getResult();
//			return !tempAddrlist.isEmpty() ? tempAddrlist : null;
//
//		}
//		catch (final FlexibleSearchException e)
//		{
//			LOG.error(" FlexibleSearch Exception while geting the temprory Address  " + e.getMessage());
//		}
//		catch (final Exception e)
//		{
//			LOG.error("Exception occurred while  getting the temparory address " + e.getMessage());
//		}
//		return null;
//	}
//
//
//
}
