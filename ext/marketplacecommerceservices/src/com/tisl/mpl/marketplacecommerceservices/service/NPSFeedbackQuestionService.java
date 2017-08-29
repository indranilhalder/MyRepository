/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.user.CustomerModel;

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

	public int getFeedback(String transactionId);

	public String getNPSId();

	public CustomerModel validateCustomerForTransaction(final String transactionId);

	//Added for 6081


	/**
	 * @param transactionId
	 * @return
	 */
	public NPSFeedbackModel getFeedbackModel(String transactionId);


}