/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import java.util.List;

import com.tisl.mpl.core.model.MplShopByLookModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public interface ShopByLookService
{
	public List<MplShopByLookModel> getAllShopByLookProducts(String collectionId) throws EtailNonBusinessExceptions;
}
