/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.nps.NPSFeedbackQuestionFacade;
import com.tisl.mpl.facades.data.NPSFeedbackQRData;
import com.tisl.mpl.facades.data.NPSFeedbackQRDetailData;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.storefront.web.forms.NPSFeedbackQRForm;
import com.tisl.mpl.storefront.web.forms.NPSFeedbackQuestionForm;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/feedback")
public class NPSFeedbackController
{

	@Resource
	private NPSFeedbackQuestionFacade npsFeedbackQuestionFacade;
	//sonar fix
	/*
	 * @Resource(name = ModelAttributetConstants.SESSION_SERVICE) private SessionService sessionService;
	 */
	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;

	/**
	 *
	 * @param model
	 * @param transactionId
	 * @param deliveryMode
	 * @param rating
	 * @param originalUid
	 * @return String
	 * @throws CMSItemNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/NPSFeedbackForm", method = RequestMethod.GET)
	public String getFeedbackQuestionDetails(final Model model, @RequestParam(required = true) final String transactionId,
			@RequestParam(required = false) final String deliveryMode, @RequestParam(required = true) final String rating,
			@RequestParam(required = true) final String originalUid) throws CMSItemNotFoundException, UnsupportedEncodingException

	{
		String returnStatement = null;
		NPSFeedbackQRData npsFeedbackQRData = null;
		final List<NPSFeedbackQuestionForm> npsFeedbackQRDetail = new ArrayList<NPSFeedbackQuestionForm>();
		final NPSFeedbackQRForm npsFeedbackQRForm = new NPSFeedbackQRForm();
		final NPSFeedbackQRData feedbackData = new NPSFeedbackQRData();
		//Added for TPR-6081
		int npsFeedBackModelCount = 0;
		npsFeedBackModelCount = npsFeedbackQuestionFacade.getFeedback(transactionId);
		try
		{
			if (npsFeedBackModelCount > 0)
			{
				return ControllerConstants.Views.Fragments.NPS_Emailer.NpsFeedbackExists;
			}
			else
			{
				feedbackData.setOverAllRating(rating);
				feedbackData.setTransactionId(transactionId);
				feedbackData.setOriginalUid(originalUid);
				//Save NPS rating for transactionId
				npsFeedbackQuestionFacade.saveFeedbackRating(originalUid, transactionId, rating);
			}


			if (StringUtils.isEmpty(transactionId))
			{
				GlobalMessages.addErrorMessage(model, "Invalid Transaction , User or Rating. ");
				model.addAttribute("npsFeedbackForm", npsFeedbackQRForm);
				returnStatement = ControllerConstants.Views.Fragments.NPS_Emailer.NPSFeedback;
			}
			else
			{
				npsFeedbackQRData = npsFeedbackQuestionFacade.getFeedbackQuestionFacade();
				for (final NPSFeedbackQRDetailData npsQuestionDetail : npsFeedbackQRData.getFeedbackQRList())
				{
					final NPSFeedbackQuestionForm npsQRDetail = new NPSFeedbackQuestionForm();
					boolean addQuestion = true;

					if ((npsQuestionDetail.getNpsDeliveryModeType().equalsIgnoreCase("cnc") && (deliveryMode
							.equalsIgnoreCase("home-delivery") || deliveryMode.equalsIgnoreCase("express-delivery")))
							|| (npsQuestionDetail.getNpsDeliveryModeType().equalsIgnoreCase("od") && deliveryMode
									.equalsIgnoreCase("click-and-collect")))
					{
						addQuestion = false;
					}

					if (addQuestion)
					{
						npsQRDetail.setQuestionCode(npsQuestionDetail.getQuestionCode());
						npsQRDetail.setQuestionName(npsQuestionDetail.getQuestionName());
						npsFeedbackQRDetail.add(npsQRDetail);
					}



				}
				npsFeedbackQRForm.setNpsQuestionlist(npsFeedbackQRDetail);
				npsFeedbackQRForm.setTransactionId(transactionId);


				model.addAttribute("npsFeedbackForm", npsFeedbackQRForm);
				returnStatement = ControllerConstants.Views.Fragments.NPS_Emailer.NPSFeedback;
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			returnStatement = frontEndErrorHelper.callBusinessError(model, e.getErrorMessage());

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			returnStatement = frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_PDP_ERROR_PAGE_NON_BUSINESS);


		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			returnStatement = frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_PDP_ERROR_PAGE_NON_BUSINESS);

		}
		return returnStatement;

	}

	/**
	 * This method accepts both GET and POST in case of POST the method accepts data and on GET it redirects to the base
	 * website
	 *
	 * @param feedbackForm
	 * @param result
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/NPSFeedbackForm", method = RequestMethod.POST)
	public String getFeedbackCapturedData(@ModelAttribute("npsFeedbackForm") final NPSFeedbackQRForm feedbackForm,
			final BindingResult result, final Model model) throws CMSItemNotFoundException
	{
		String returnStatement = null;

		final NPSFeedbackQRData feedbackData = new NPSFeedbackQRData();
		final List<NPSFeedbackQRDetailData> feedbackDataDetail = new ArrayList<>();
		try
		{
			//1. Stop the form submission if any of these values are missing
			if (StringUtils.isEmpty(feedbackForm.getTransactionId()) || StringUtils.isEmpty(feedbackForm.getOriginalUid())
					|| StringUtils.isEmpty(feedbackForm.getRating()))
			{
				GlobalMessages.addErrorMessage(model, "Invalid transactions , user or rating. ");
				return ControllerConstants.Views.Fragments.NPS_Emailer.NPSFeedback;
			}//2. Check if a feedback already exist for this transaction ID


			if (result.hasErrors())
			{
				GlobalMessages.addErrorMessage(model, "Your form has errors . Please rectify and submit the form again. ");
				return ControllerConstants.Views.Fragments.NPS_Emailer.NPSFeedback;

			}

			else if (!npsFeedbackQuestionFacade.validateCustomerForTransaction(feedbackForm.getTransactionId()).getOriginalUid()
					.equals(feedbackForm.getOriginalUid()))
			{
				returnStatement = ControllerConstants.Views.Fragments.NPS_Emailer.NPSFeedback;
				GlobalMessages.addErrorMessage(model, "Transaction and associated customer not found.");
			}
			else
			{
				feedbackData.setOverAllRating(feedbackForm.getRating());
				feedbackData.setOriginalUid(feedbackForm.getOriginalUid());
				feedbackData.setTransactionId(feedbackForm.getTransactionId());
				feedbackData.setAnyOtherFeedback(feedbackForm.getOtherFeedback());


				for (final NPSFeedbackQuestionForm formDetail : feedbackForm.getNpsQuestionlist())
				{
					final NPSFeedbackQRDetailData feedbackDetailData = new NPSFeedbackQRDetailData();
					feedbackDetailData.setQuestionCode(formDetail.getQuestionCode());
					feedbackDetailData.setQuestionName(formDetail.getQuestionName());
					feedbackDetailData.setRating(formDetail.getRating());
					feedbackDataDetail.add(feedbackDetailData);
				}
				feedbackData.setFeedbackQRList(feedbackDataDetail);
				if (npsFeedbackQuestionFacade.saveFeedbackQuestionAnswer(feedbackData))
				{
					returnStatement = ControllerConstants.Views.Fragments.NPS_Emailer.SubmitSuccess;
				}
				else
				{
					GlobalMessages.addErrorMessage(model, MessageConstants.NPSFEEDBACK_SAVE_ERROR);
					returnStatement = ControllerConstants.Views.Fragments.NPS_Emailer.SubmitSuccess;
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			returnStatement = frontEndErrorHelper.callBusinessError(model, e.getErrorMessage());

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			returnStatement = frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_PDP_ERROR_PAGE_NON_BUSINESS);


		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			returnStatement = frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_PDP_ERROR_PAGE_NON_BUSINESS);

		}
		return returnStatement;
	}
}