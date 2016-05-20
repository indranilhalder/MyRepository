/**
 *
 */
package com.tisl.mpl.facade.latestoffers;

import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;

import com.tisl.mpl.facades.data.LatestOffersData;


/**
 * @author TCS
 *
 */
public interface LatestOffersFacade
{

	/**
	 * @param contentSlot
	 * @return LatestOffersData
	 */
	LatestOffersData getLatestOffers(ContentSlotModel contentSlot);

}
