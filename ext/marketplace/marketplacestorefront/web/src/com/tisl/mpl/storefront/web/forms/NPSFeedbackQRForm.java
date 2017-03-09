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
	private String originalUid;
	private String rating;
	private String deliveryMode;

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

	/**
	 * @return the originalUid
	 */
	public String getOriginalUid()
	{
		return originalUid;
	}

	/**
	 * @param originalUid
	 *           the originalUid to set
	 */
	public void setOriginalUid(final String originalUid)
	{
		this.originalUid = originalUid;
	}

	/**
	 * @return the rating
	 */
	public String getRating()
	{
		return rating;
	}

	/**
	 * @param rating
	 *           the rating to set
	 */
	public void setRating(final String rating)
	{
		this.rating = rating;
	}

	/**
	 * @return the deliveryMode
	 */
	public String getDeliveryMode()
	{
		return deliveryMode;
	}

	/**
	 * @param deliveryMode the deliveryMode to set
	 */
	public void setDeliveryMode(String deliveryMode)
	{
		this.deliveryMode = deliveryMode;
	}
}