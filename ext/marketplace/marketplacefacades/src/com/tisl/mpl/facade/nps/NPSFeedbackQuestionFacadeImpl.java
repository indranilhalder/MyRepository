/**
 *
 */
package com.tisl.mpl.facade.nps;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

	private static final String DATEFORMAT = "dd/MM/yyyy hh:mm:ss a";

	//sonar fix
	/*
	 * @Autowired private OrderService orderService;
	 */

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
				npsFeedbackQuestionData.setNpsDeliveryModeType(npsfeedback.getNpsDeliveryModeType().getCode());
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
		CustomerModel customer = null;
		try
		{
			final NPSFeedbackModel npsModel = npsFeedbackQuestionService.getFeedbackModel(feedbackForm.getTransactionId());
			if (npsModel != null)
			{
				npsFeedbackModel = npsModel;
			}
			else
			{
				npsFeedbackModel = modelService.create(NPSFeedbackModel.class);
				npsFeedbackModel.setNpsId(npsFeedbackQuestionService.getNPSId());
			}
			if (StringUtils.isNotEmpty(feedbackForm.getTransactionId()))
			{
				npsFeedbackModel.setTransactionId(feedbackForm.getTransactionId());
			}
			if (StringUtils.isNotEmpty(feedbackForm.getOverAllRating()))
			{
				npsFeedbackModel.setNpsRating(feedbackForm.getOverAllRating());
			}
			if (StringUtils.isNotEmpty(feedbackForm.getOriginalUid()))
			{
				customer = extendedUserService.getUserForOriginalUid(feedbackForm.getOriginalUid());
				if (customer != null)
				{
					npsFeedbackModel.setEmailId(feedbackForm.getOriginalUid());
					npsFeedbackModel.setFirstName(customer.getFirstName());
					npsFeedbackModel.setLastName(customer.getLastName());
				}
			}
			final Random rnd = new Random();
			final int responseId = 100000 + rnd.nextInt(90000000);
			//sonar fix
			//final Integer responseIdInt = new Integer(responseId);

			final Integer responseIdInt = Integer.valueOf(responseId);

			npsFeedbackModel.setResponseId(responseIdInt.toString());
			final SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
			final SimpleDateFormat dateFormatParse = new SimpleDateFormat(DATEFORMAT);
			final Date date = new Date();
			final String SurveyDate = dateFormat.format(date);


			npsFeedbackModel.setOriginalSurveyDate(dateFormatParse.parse(SurveyDate));
			npsFeedbackModel.setResponseTime(dateFormatParse.parse(SurveyDate));
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
				modelService.saveAll(npsFeedbackModel);
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
			npsFeedbackModel = modelService.create(NPSFeedbackModel.class);
			customer = extendedUserService.getUserForOriginalUid(originalUid);
			if (null != customer)
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
			//TISPRDT-2140 starts
			final SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
			final SimpleDateFormat dateFormatParse = new SimpleDateFormat(DATEFORMAT);
			final Date date = new Date();
			final String SurveyDate = dateFormat.format(date);
			npsFeedbackModel.setOriginalSurveyDate(dateFormatParse.parse(SurveyDate));
			//TISPRDT-2140 ends
			modelService.save(npsFeedbackModel);
		}
		catch (final ModelSavingException e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
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
	 * @see com.tisl.mpl.facade.nps.NPSFeedbackQuestionFacade#getFeedback(java.lang.String)
	 */
	@Override
	public int getFeedback(final String transactionId)
	{
		return npsFeedbackQuestionService.getFeedback(transactionId);
	}

	@Override
	public CustomerModel validateCustomerForTransaction(final String transactionId)
	{
		return npsFeedbackQuestionService.validateCustomerForTransaction(transactionId);
	}
}
