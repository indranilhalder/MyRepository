/**
 *
 */
package com.tisl.mpl.storefront.web.forms;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 1256972
 *
 */
public class NPSFeedbackQRForm
{
	List<NPSFeedbackQuestion> npsFeedbackQuestionlist = new ArrayList<NPSFeedbackQuestion>();

	/**
	 * @return the npsFeedbackQuestionlist
	 */
	public List<NPSFeedbackQuestion> getNpsFeedbackQuestionlist()
	{
		return npsFeedbackQuestionlist;
	}

	/**
	 * @param npsFeedbackQuestionlist
	 *           the npsFeedbackQuestionlist to set
	 */
	public void setNpsFeedbackQuestionlist(final List<NPSFeedbackQuestion> npsFeedbackQuestionlist)
	{
		this.npsFeedbackQuestionlist = npsFeedbackQuestionlist;
	}
}