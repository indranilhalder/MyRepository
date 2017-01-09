/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.NPSFeedbackQuestionModel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.NPSFeedbackQuestionDao;
import com.tisl.mpl.marketplacecommerceservices.service.NPSFeedbackQuestionService;


/**
 * @author 1256972
 *
 */
public class NPSFeedbackQuestionServiceImpl implements NPSFeedbackQuestionService
{
	@Autowired
	private NPSFeedbackQuestionDao npsFeedbackQuestionDao;

	@Override
	public List<NPSFeedbackQuestionModel> getFeedbackQuestionService()
	{
		List<NPSFeedbackQuestionModel> npsFeedbackQuestion = new ArrayList<NPSFeedbackQuestionModel>();
		npsFeedbackQuestion = npsFeedbackQuestionDao.getFeedbackQuestionDao();
		return npsFeedbackQuestion;
	}
}