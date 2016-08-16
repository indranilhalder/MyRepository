/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.impl;

import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.TemproryAddressModel;
import com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.MplDeliveryAddressDao;


/**
 * @author pankajk
 *
 */
public class MplDeliveryAddressDaoImpl implements MplDeliveryAddressDao
{
	private static final Logger LOG = Logger.getLogger(MplDeliveryAddressDaoImpl.class);
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@Autowired
	private ModelService modelService;

	//	/***
	//	 * OrderId Based On We will get TemproryAddressModel
	//	 *
	//	 * @param orderCode
	//	 * @return TemproryAddressModel
	//	 */
	//
	//	@Override
	//	public void saveDeliveryAddress(final OrderModel orderModel, final AddressModel address)
	//	{
	//		try
	//		{
	//			orderModel.setDeliveryAddress(address);
	//			OrderModel parentorder = orderModel.getParentReference();
	//			UserModel user = orderModel.getUser();
	//			parentorder.setDeliveryAddress(address);
	//			List<AddressModel> deliveryAddressesList = new ArrayList<AddressModel>();
	//			Collection<AddressModel> customerAddressesList = new ArrayList<AddressModel>();
	//			Collection<AddressModel> deliveryAddresses = orderModel.getParentReference().getDeliveryAddresses();
	//			if (null != deliveryAddresses)
	//			{
	//				deliveryAddressesList.addAll(deliveryAddresses);
	//			}
	//			if (null != user.getAddresses())
	//			{
	//				customerAddressesList.addAll(user.getAddresses());
	//			}
	//			if (null != address)
	//			{
	//				deliveryAddressesList.add(address);
	//				customerAddressesList.add(address);
	//			}
	//			user.setAddresses(customerAddressesList);
	//			orderModel.getParentReference().setDeliveryAddresses(deliveryAddressesList);
	//			modelService.save(orderModel);
	//			modelService.save(orderModel.getParentReference());
	//			modelService.save(user);
	//		}
	//		catch (final ModelSavingException e)
	//		{
	//			LOG.debug("Model saving Exception" + e.getMessage());
	//
	//		}
	//		catch (final Exception e)
	//		{
	//			LOG.debug("Exception while saving Address" + e.getMessage());
	//		}
	//	}


	/***
	 * OrderId Based On We will get Temporary AddressModel
	 *
	 *
	 * @return TemproryAddressModel
	 */
	@Override
	public TemproryAddressModel getTemporaryAddressModel(String orderId)
	{

		try
		{
			final String queryString = "SELECT {o:" + TemproryAddressModel.PK
					+ "} "+ "FROM {" + TemproryAddressModel._TYPECODE + " AS o} " + "WHERE {o." + TemproryAddressModel.ORDERID
					+ "}=?orderId" + " AND {o." + TemproryAddressModel.ISAPPROVAL + "}=true";

			FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryString);
			fQuery.addQueryParameter("orderId", orderId);			
			SearchResult<TemproryAddressModel> searchResult = flexibleSearchService.search(fQuery);
			List<TemproryAddressModel> tempAddrlist = searchResult.getResult();
			return !tempAddrlist.isEmpty() ? tempAddrlist.get(0) : null;

		}
		catch (final FlexibleSearchException e)
		{
			LOG.error(" FlSearchException exception " + e.getMessage());
		}
		catch (final Exception e)
		{
			LOG.error("Exception occurree getting the temparory address " + e.getMessage());
		}
		return null;
	}



}
