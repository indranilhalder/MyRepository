/**
 *
 */
package com.tisl.mpl.facade.nps;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.NPSFeedbackQuestionModel;
import com.tisl.mpl.marketplacecommerceservices.service.NPSFeedbackQuestionService;


/**
 * @author 1256972
 *
 */
public class NPSFeedbackQuestionFacadeImpl implements NPSFeedbackQuestionFacade
{
	@Autowired
	private NPSFeedbackQuestionService npsFeedbackQuestionService;

	@Override
	public List<NPSFeedbackQuestionModel> getFeedbackQuestionFacade()
	{
		List<NPSFeedbackQuestionModel> npsFeedbackQuestion = new ArrayList<NPSFeedbackQuestionModel>();
		npsFeedbackQuestion = npsFeedbackQuestionService.getFeedbackQuestionService();
		return npsFeedbackQuestion;
	}
}