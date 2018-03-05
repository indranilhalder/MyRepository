/**
 * 
 */
package com.tisl.mpl.marketplacecommerceservices.egv.dao.cart.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.MplDeliveryAddressInfoModel;
import com.tisl.mpl.marketplacecommerceservices.egv.dao.cart.MplEGVCartDao;

/**
 * @author PankajS
 *
 */
public class MplEGVCartDaoImpl implements MplEGVCartDao
{

	
	/**
	 * 
	 */
	private static final String CART_MODEL_FACTING_FOR_BASE_CURRENT_SESSION2 = "Cart model facting for base current session ***";

	/**
	 * 
	 */
	private static final String CART_MODEL_FACTING_FOR_BASE_CURRENT_SESSION = "Cart model facting for base current session";

	/**
	 * 
	 */
	private static final String FACTING_CAR_MODEL_BASE_ON_GUID = "Facting Car Model Base On Guid ";

	private static final String MPL_EGV_CART_MODEL =  "Select {s:pk} from {Cart as s},{User as c} where {s.user}={c.pk} and {c.uid}=?customerId and {s.isEGVCart}=?isEGV";

	private static final Logger LOG = Logger.getLogger(MplEGVCartDaoImpl.class);
	
	
	private static final String CART_QUERY_BY_GUID = "SELECT {srm:" + CartModel.PK
			+ "}" + " FROM {" + CartModel._TYPECODE + " AS srm} " + "WHERE " + "{srm:"
			+ CartModel.GUID + "}=?guid ";

	@Autowired
	private FlexibleSearchService flexibleSearchService;
	
	@Autowired
   ModelService modelService;
	
	@Autowired
	UserService userService;
	
	@Override
	public void removeOldEGVCartCurrentCustomer()
	{
		try
		{

			UserModel currentCustomer = userService.getCurrentUser();

			if (LOG.isDebugEnabled())
			{
				LOG.debug(CART_MODEL_FACTING_FOR_BASE_CURRENT_SESSION2);
			}
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(MPL_EGV_CART_MODEL);
			fQuery.addQueryParameter("customerId", currentCustomer.getUid());
			fQuery.addQueryParameter("isEGV", Boolean.TRUE);
			final List<CartModel> listOfData = flexibleSearchService.<CartModel> search(fQuery).getResult();
			if (CollectionUtils.isNotEmpty(listOfData))
			{
				modelService.removeAll(listOfData);
			}

		}
	catch (final Exception e)
	{
		LOG.error(CART_MODEL_FACTING_FOR_BASE_CURRENT_SESSION );
	}
	}
	
	
	@Override
	public CartModel getEGVCartModel(final String guid)
	{
		CartModel cart = null;
		try{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(CART_QUERY_BY_GUID);
		fQuery.addQueryParameter("guid", guid);

		final List<CartModel> listOfData = flexibleSearchService.<CartModel> search(fQuery)
				.getResult();
		cart=listOfData.get(0);
		
		}catch(Exception exc){
			LOG.error(FACTING_CAR_MODEL_BASE_ON_GUID+exc);
		}
		return cart;
	}

}
