/**
 *
 */
package com.tisl.mpl.storefront.web.forms;

import java.util.ArrayList;
import java.util.List;


/**
 * @author TCS
 *
 */
public class NPSFeedbackQRForm
{
	List<NPSFeedbackQuestionForm> npsQuestionlist = new ArrayList<NPSFeedbackQuestionForm>();

	private String transactionId;
	private String userId;
	private String firstName;
	private String lastName;
	private String otherFeedback;

	/**
	 * @return the npsQuestionlist
	 */
	public List<NPSFeedbackQuestionForm> getNpsQuestionlist()
	{
		return npsQuestionlist;
	}

	/**
	 * @param npsQuestionlist
	 *           the npsQuestionlist to set
	 */
	public void setNpsQuestionlist(final List<NPSFeedbackQuestionForm> npsQuestionlist)
	{
		this.npsQuestionlist = npsQuestionlist;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId()
	{
		return transactionId;
	}

	/**
	 * @param transactionId
	 *           the transactionId to set
	 */
	public void setTransactionId(final String transactionId)
	{
		this.transactionId = transactionId;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * @param firstName
	 *           the firstName to set
	 */
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * @param lastName
	 *           the lastName to set
	 */
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * @return the userId
	 */
	public String getUserId()
	{
		return userId;
	}

	/**
	 * @param userId
	 *           the userId to set
	 */
	public void setUserId(final String userId)
	{
		this.userId = userId;
	}

	/**
	 * @return the otherFeedback
	 */
	public String getOtherFeedback()
	{
		return otherFeedback;
	}

	/**
	 * @param otherFeedback
	 *           the otherFeedback to set
	 */
	public void setOtherFeedback(final String otherFeedback)
	{
		this.otherFeedback = otherFeedback;
	}


}