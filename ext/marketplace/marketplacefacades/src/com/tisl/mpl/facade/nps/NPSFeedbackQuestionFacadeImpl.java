/**
 *
 */
package com.tisl.mpl.facade.nps;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sourceforge.pmd.util.StringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.core.model.NPSFeedbackDetailModel;
import com.tisl.mpl.core.model.NPSFeedbackModel;
import com.tisl.mpl.core.model.NPSFeedbackQuestionModel;
import com.tisl.mpl.facades.data.NPSFeedbackQRData;
import com.tisl.mpl.facades.data.NPSFeedbackQRDetailData;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.NPSFeedbackQuestionService;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class NPSFeedbackQuestionFacadeImpl implements NPSFeedbackQuestionFacade
{
	@Resource
	private NPSFeedbackQuestionService npsFeedbackQuestionService;
	@Resource
	private ExtendedUserService extendedUserService;
	@Resource
	private ModelService modelService;

	@Override
	public NPSFeedbackQRData getFeedbackQuestionFacade()
	{
		final NPSFeedbackQRData npsFeedbackQuestion = new NPSFeedbackQRData();
		final List<NPSFeedbackQRDetailData> npsFeedbackQuestionDetail = new ArrayList<>();
		List<NPSFeedbackQuestionModel> npsFeedbackQuestionList = null;
		try
		{
			npsFeedbackQuestionList = npsFeedbackQuestionService.getFeedbackQuestionService();
			for (final NPSFeedbackQuestionModel npsfeedback : npsFeedbackQuestionList)
			{
				final NPSFeedbackQRDetailData npsFeedbackQuestionData = new NPSFeedbackQRDetailData();
				npsFeedbackQuestionData.setQuestionCode(npsfeedback.getQuestionCode());
				npsFeedbackQuestionData.setQuestionName(npsfeedback.getQuestion());
				npsFeedbackQuestionDetail.add(npsFeedbackQuestionData);
			}
			npsFeedbackQuestion.setFeedbackQRList(npsFeedbackQuestionDetail);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
		}
		return npsFeedbackQuestion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.nps.NPSFeedbackQuestionFacade#saveFeedbackQuestionAnswer()
	 */
	@Override
	public boolean saveFeedbackQuestionAnswer(final NPSFeedbackQRData feedbackForm)
	{
		final List<NPSFeedbackDetailModel> npsFeedbackModelList = new ArrayList<NPSFeedbackDetailModel>();
		NPSFeedbackModel npsFeedbackModel = null;
		NPSFeedbackDetailModel npsFeedback = null;
		try
		{
			if (feedbackForm.getTransactionId() != null)
			{
				npsFeedbackModel = npsFeedbackQuestionService.getFeedback(feedbackForm.getTransactionId());

			}
			if (npsFeedbackModel != null)
			{
				//saving anyother feedback
				if (StringUtils.isNotEmpty(feedbackForm.getAnyOtherFeedback()))
				{
					npsFeedbackModel.setAnyOtherFeedback(feedbackForm.getAnyOtherFeedback());
				}
				//saving rating against questions
				for (final NPSFeedbackQRDetailData formDetail : feedbackForm.getFeedbackQRList())
				{
					npsFeedback = modelService.create(NPSFeedbackDetailModel.class);
					if (StringUtil.isNotEmpty(formDetail.getRating()))
					{
						npsFeedback.setRating(formDetail.getRating());
					}
					if (StringUtil.isNotEmpty(formDetail.getQuestionName()))
					{
						npsFeedback.setQuestionDesc(formDetail.getQuestionName());
					}
					if (StringUtil.isNotEmpty(formDetail.getQuestionCode()))
					{
						npsFeedback.setQuestionCode(formDetail.getQuestionCode());
					}

					npsFeedbackModelList.add(npsFeedback);
				}
				if (CollectionUtils.isNotEmpty(npsFeedbackModelList))
				{
					npsFeedbackModel.setFeedbackDetails(npsFeedbackModelList);
					modelService.saveAll(npsFeedbackModelList, npsFeedbackModel);
				}
			}
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.nps.NPSFeedbackQuestionFacade#saveFeedbackRating(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean saveFeedbackRating(final String originalUid, final String transactionId, final String rating)
	{
		NPSFeedbackModel npsFeedbackModel = null;
		CustomerModel customer = null;
		try
		{
			npsFeedbackModel = npsFeedbackQuestionService.getFeedback(transactionId);
			if (npsFeedbackModel == null)
			{
				npsFeedbackModel = modelService.create(NPSFeedbackModel.class);
			}
			customer = (CustomerModel) extendedUserService.getUserForEmailid(originalUid);
			if (customer != null)
			{
				npsFeedbackModel.setEmailId(customer.getOriginalUid());
				npsFeedbackModel.setFirstName(customer.getFirstName());
				npsFeedbackModel.setLastName(customer.getLastName());
			}

			npsFeedbackModel.setNpsId(npsFeedbackQuestionService.getNPSId());

			npsFeedbackModel.setNpsRating(rating);
			npsFeedbackModel.setTransactionId(transactionId);
			npsFeedbackModel.setResponseTime(new Date());
			//npsFeedbackModel.setOriginalSurveyDate();
			modelService.save(npsFeedbackModel);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
		}
		return false;
	}
}