/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.hmc.model.SavedValuesModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.tisl.mpl.marketplacecommerceservices.daos.PromotionalReportCreationDao;
import com.tisl.mpl.marketplacecommerceservices.service.PromotionalReportCreationService;


/**
 * @author TCS
 *
 */
public class DefaultPromotionalReportCreationService implements PromotionalReportCreationService
{
	@Resource
	private PromotionalReportCreationDao promotionalReportCreationDao;

	/**
	 * @Descriptiion: Getting All Promotions
	 * @return : List of all promotions
	 */
	@Override
	public Set<Map<AbstractPromotionModel, SavedValuesModel>> getAllPromotions(final Date startDate, final Date endDate)
	{
		// YTODO Auto-generated method stub
		return promotionalReportCreationDao.getAllPromotions(startDate, endDate);
	}
}
