/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import java.util.List;

import com.tisl.mpl.core.model.NPSFeedbackModel;
import com.tisl.mpl.core.model.NPSFeedbackQuestionModel;


/**
 * @author TCS
 *
 */
public interface NPSFeedbackQuestionDao
{
	public List<NPSFeedbackQuestionModel> getFeedbackQuestionDao();

	public NPSFeedbackModel getFeedback(String transactionId);
}