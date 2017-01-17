/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import java.util.List;

import com.tisl.mpl.core.model.NPSFeedbackModel;
import com.tisl.mpl.core.model.NPSFeedbackQuestionModel;


/**
 * @author TCS
 *
 */
public interface NPSFeedbackQuestionService
{
	public List<NPSFeedbackQuestionModel> getFeedbackQuestionService();

	public NPSFeedbackModel getFeedback(String transactionId);

	public String getNPSId();


}