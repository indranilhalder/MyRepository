/**
 *
 */
package com.tisl.mpl.search.feedback.facades.impl;

import de.hybris.platform.servicelayer.model.ModelService;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.core.enums.FeedbackCategory;
import com.tisl.mpl.core.model.FeedBackStoringModel;
import com.tisl.mpl.search.feedback.facades.UpdateFeedbackFacade;


public class UpdateFeedbackFacadeImpl implements UpdateFeedbackFacade
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.search.feedback.facades.UpdateFeedbackFacade#updateFeedback(com.tisl.mpl.core.model.FeedBackModel)
	 */


	@Autowired
	private ModelService modelService;

	@Override
	public String updateFeedbackNo(final String comment, final String category, final String customerEmail,
			final String searchCategory, final String searchText)
	{

		final FeedBackStoringModel feedBackStoringModel = modelService.create(FeedBackStoringModel.class);
		feedBackStoringModel.setFeedbackComment(comment);
		feedBackStoringModel.setCategory(FeedbackCategory.valueOf(category));
		feedBackStoringModel.setCustomerEmail(customerEmail);
		feedBackStoringModel.setSearchCategory(searchCategory);
		feedBackStoringModel.setSearchTerm(searchText);
		modelService.save(feedBackStoringModel);
		return MarketplaceCoreConstants.SUCCESS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.search.feedback.facades.UpdateFeedbackFacade#updateFeedbackYes(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public String updateFeedbackYes(final String email, final String comment, final String name)
	{
		final FeedBackStoringModel feedBackStoringModel = modelService.create(FeedBackStoringModel.class);
		feedBackStoringModel.setCustomerEmail(email);
		feedBackStoringModel.setFeedbackComment(comment);
		feedBackStoringModel.setCustomerName(name);
		modelService.save(feedBackStoringModel);
		return MarketplaceCoreConstants.SUCCESS;
	}
}
