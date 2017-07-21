/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.user.CustomerModel;

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

	public int getFeedback(String transactionId);

	public CustomerModel validateCustomerForTransaction(String transactionId);

	//Added for 6081

	public List<NPSFeedbackModel> getFeedbackModel(String transactionId);
}