/**
 *
 */
package com.tisl.mpl.facade.shopbylook;

import java.util.List;

import com.tisl.mpl.core.model.MplShopByLookModel;


/**
 * @author TCS
 *
 */
public interface ShopByLookFacade
{
	public List<MplShopByLookModel> getAllShopByLookProducts(String collectionId);
}
