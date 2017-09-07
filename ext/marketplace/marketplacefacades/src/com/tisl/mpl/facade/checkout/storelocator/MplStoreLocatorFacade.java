/**
 *
 */
package com.tisl.mpl.facade.checkout.storelocator;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

import com.tisl.mpl.facades.data.ProudctWithPointOfServicesData;
import com.tisl.mpl.facades.data.StoreLocationResponseData;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TECH
 *
 */
public interface MplStoreLocatorFacade
{
	/**
	 *
	 * @param abstractCartEntry
	 * @return map having freebie product ussid and qty.
	 */
	public Map<String, Long> addFreebieProducts(final AbstractOrderEntryModel abstractCartEntry);

	/**
	 *
	 * @param abstractCartEntry
	 * @param storeLocationResponseData
	 */
	public void filterStoresWithQtyGTSelectedUserQty(final AbstractOrderEntryModel abstractCartEntry,
			final StoreLocationResponseData storeLocationResponseData);

	/**
	 *
	 * @param ussid
	 * @param model
	 * @param freebieProductsWithQuant
	 * @param posModelList
	 * @param sellerInfoModel
	 * @return Product with stores
	 */
	public ProudctWithPointOfServicesData populateProductWithStoresForUssid(final String ussid, final Model model,
			final Map<String, Long> freebieProductsWithQuant, final List<PointOfServiceModel> posModelList,
			final SellerInformationModel sellerInfoModel);

	/**
	 *
	 * @param posModel
	 * @param ussId
	 * @return yes if successfully otherwise no
	 */
	public String saveStoreForSelectedProduct(final PointOfServiceModel posModel, final String ussId);

	/**
	 * Find CartEntryModel for a Ussid.
	 *
	 * @param ussId
	 * @return abstractOrderEntryModel
	 */
	public AbstractOrderEntryModel getCartEntry(final String ussId);

}
