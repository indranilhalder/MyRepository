/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.hmc.model.SavedValuesModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;

import java.util.Date;
import java.util.Map;
import java.util.Set;


/**
 * @author TCS
 *
 */
public interface PromotionalReportCreationDao
{
	public Set<Map<AbstractPromotionModel, SavedValuesModel>> getAllPromotions(Date startDate, Date endDate);
}
