/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import java.util.List;

import com.tisl.mpl.core.model.MplShopByLookModel;


/**
 * @author TCS
 *
 */
public interface ShopByLookDao
{
	public List<MplShopByLookModel> getAllShopByLookProducts(final String collectionId);
}
