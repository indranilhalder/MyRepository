/**
 *
 */
package com.tisl.mpl.facade.shopbylook.impl;

import java.util.List;

import javax.annotation.Resource;

import com.tisl.mpl.core.model.MplShopByLookModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.shopbylook.ShopByLookFacade;
import com.tisl.mpl.marketplacecommerceservices.service.ShopByLookService;


/**
 * @author TCS
 *
 */
public class ShopByLookFacadeImpl implements ShopByLookFacade
{

	@Resource(name = "shopByLookService")
	private ShopByLookService shopByLookService;

	/**
	 * @param collectionId
	 *           ID or the name of the page
	 * @Desc This method fetches all data pertaining to the supplied collection id
	 * @return List<MplShopByLookModel>
	 *
	 */
	@Override
	public List<MplShopByLookModel> getAllShopByLookProducts(final String collectionId) throws EtailNonBusinessExceptions
	{
		return shopByLookService.getAllShopByLookProducts(collectionId);
	}

}
