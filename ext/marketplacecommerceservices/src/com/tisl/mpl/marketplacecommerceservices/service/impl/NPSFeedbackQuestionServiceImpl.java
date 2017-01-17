/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;

import java.util.List;

import javax.annotation.Resource;

import com.tisl.mpl.core.model.NPSFeedbackModel;
import com.tisl.mpl.core.model.NPSFeedbackQuestionModel;
import com.tisl.mpl.marketplacecommerceservices.daos.NPSFeedbackQuestionDao;
import com.tisl.mpl.marketplacecommerceservices.service.NPSFeedbackQuestionService;


/**
 * @author TCS
 *
 */
public class NPSFeedbackQuestionServiceImpl implements NPSFeedbackQuestionService
{
	@Resource(name = "npsFeedbackQuestionDao")
	private NPSFeedbackQuestionDao npsFeedbackQuestionDao;

	@Resource
	private PersistentKeyGenerator npsIdGenerator;

	@Override
	public List<NPSFeedbackQuestionModel> getFeedbackQuestionService()
	{
		return npsFeedbackQuestionDao.getFeedbackQuestionDao();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.NPSFeedbackQuestionService#getFeedback(java.lang.String)
	 */
	@Override
	public NPSFeedbackModel getFeedback(final String transactionId)
	{
		// YTODO Auto-generated method stub
		return npsFeedbackQuestionDao.getFeedback(transactionId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.NPSFeedbackQuestionService#getNPSId()
	 */
	@Override
	public String getNPSId()
	{
		return npsIdGenerator.generate().toString();
	}
}