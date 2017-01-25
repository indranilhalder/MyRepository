/**
 *
 */
package com.tisl.mpl.facade.nps;

import de.hybris.platform.core.model.user.CustomerModel;

import com.tisl.mpl.facades.data.NPSFeedbackQRData;



/**
 * @author TCS
 *
 */
public interface NPSFeedbackQuestionFacade
{
	public NPSFeedbackQRData getFeedbackQuestionFacade();

	public boolean saveFeedbackQuestionAnswer(NPSFeedbackQRData feedbackForm);

	public boolean saveFeedbackRating(String originalUid, String transactionId, String rating);

	public int getFeedback(final String transactionId);

	public CustomerModel validateCustomerForTransaction(final String transactionId);
}