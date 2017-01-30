/**
 *
 */
package com.tisl.mpl.storefront.web.forms;








/**
 * @author 1256972
 *
 */
public class NPSFeedbackQuestion
{



	private String questionCode;
	private String questionName;
	private String rating;
	private String transactionId;
	private String firstName;
	private String lastName;

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
	 * @return the questionCode
	 */
	public String getQuestionCode()
	{
		return questionCode;
	}

	/**
	 * @param questionCode
	 *           the questionCode to set
	 */
	public void setQuestionCode(final String questionCode)
	{
		this.questionCode = questionCode;
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
	 * @return the questionName
	 */
	public String getQuestionName()
	{
		return questionName;
	}

	/**
	 * @param questionName
	 *           the questionName to set
	 */
	public void setQuestionName(final String questionName)
	{
		this.questionName = questionName;
	}



}