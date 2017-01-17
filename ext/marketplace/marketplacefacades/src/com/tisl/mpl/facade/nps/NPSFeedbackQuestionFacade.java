/**
 *
 */
package com.tisl.mpl.facade.nps;

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
}