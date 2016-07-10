/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import java.util.List;

import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;


/**
 * @author TCS
 *
 */
public interface MplDeliveryCostDao
{
	MplZoneDeliveryModeValueModel getDeliveryCost(String deliveryCode, String currencyIsoCode, String sellerArticleSku);

	List<MplZoneDeliveryModeValueModel> getDeliveryModesAndCost(final String currencyIsoCode, String sellerArticleSku);
}
