/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

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
	public int getFeedback(final String transactionId)
	{
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

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.NPSFeedbackQuestionService#validateCustomerForTransaction(java
	 * .lang.String)
	 */
	@Override
	public CustomerModel validateCustomerForTransaction(final String transactionId)
	{
		return npsFeedbackQuestionDao.validateCustomerForTransaction(transactionId);
	}

	//Added for 6081
	@Override
	public NPSFeedbackModel getFeedbackModel(final String transactionId)
	{
		final List<NPSFeedbackModel> npsList = npsFeedbackQuestionDao.getFeedbackModel(transactionId);
		if (CollectionUtils.isNotEmpty(npsList))
		{
			return npsList.get(0);
		}
		else
		{
			return null;
		}

	}


}