/**
 *
 */
package com.tisl.mpl.marketplacecommerceservice.shopbylook.impl;

import java.util.List;

import javax.annotation.Resource;

import com.tisl.mpl.core.model.MplShopByLookModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.ShopByLookDao;
import com.tisl.mpl.marketplacecommerceservices.service.ShopByLookService;


/**
 * @author TCS
 *
 */
public class ShopByLookServiceImpl implements ShopByLookService
{

	@Resource(name = "shopByLookDao")
	ShopByLookDao shopbyLookDao;

	/**
	 * @param collectionId
	 *           ID or the name of the page
	 * @Desc This method fetches all data pertaining to the supplied collection id
	 * @return List<MplShopByLookModel>
	 */
	@Override
	public List<MplShopByLookModel> getAllShopByLookProducts(final String collectionId) throws EtailNonBusinessExceptions
	{
		return shopbyLookDao.getAllShopByLookProducts(collectionId);
	}

}
