/**
 *
 */
package com.tisl.mpl.storefront.web.forms;



/**
 * @author TCS
 *
 */
public class NPSFeedbackQuestionForm
{


	private String questionCode;
	private String questionName;
	private String rating;

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



}