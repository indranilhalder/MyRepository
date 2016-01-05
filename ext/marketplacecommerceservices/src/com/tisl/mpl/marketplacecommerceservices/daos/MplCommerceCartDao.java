/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.InvalidCartException;

import java.util.List;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.model.StateModel;


/**
 * @author TCS
 *
 */
public interface MplCommerceCartDao
{

	/**
	 * Method for cart details for cart id
	 *
	 * @param cartCode
	 *
	 * @return CartModel
	 *
	 * @throws InvalidCartException
	 *
	 */
	CartModel getCart(String cartCode) throws InvalidCartException;

	/**
	 * @param gUid
	 * @return
	 * @throws InvalidCartException
	 */
	CartModel getCartByGuid(String gUid) throws InvalidCartException;

	/**
	 * @param guid
	 * @param site
	 * @return CartModel for a particular guid,user and site
	 */
	CartModel getCartForGuidAndSiteAndUser(String guid, BaseSiteModel site, UserModel user) throws InvalidCartException;

	/*
	 * @Desc fetching state details for a state name
	 *
	 * @param stateName
	 *
	 * @return StateModel
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */

	List<StateModel> fetchStateDetails(String stateName) throws EtailNonBusinessExceptions;

}
