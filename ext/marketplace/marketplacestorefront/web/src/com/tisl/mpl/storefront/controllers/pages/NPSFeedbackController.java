/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

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
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
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
	@Resource(name = ModelAttributetConstants.SESSION_SERVICE)
	private SessionService sessionService;
	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;

	@RequestMapping(value = "/NPSFeedbackForm", method = RequestMethod.GET)
	public String getFeedbackQuestionDetails(final Model model, @RequestParam final String originalUid,
			@RequestParam final String transactionId, @RequestParam final String rating) throws CMSItemNotFoundException,
			UnsupportedEncodingException

	{
		String returnStatement = null;
		NPSFeedbackQRData npsFeedbackQRData = null;
		final List<NPSFeedbackQuestionForm> npsFeedbackQRDetail = new ArrayList<NPSFeedbackQuestionForm>();
		final NPSFeedbackQRForm npsFeedbackQRForm = new NPSFeedbackQRForm();
		try
		{
			npsFeedbackQuestionFacade.saveFeedbackRating(originalUid, transactionId, rating);
			npsFeedbackQRData = npsFeedbackQuestionFacade.getFeedbackQuestionFacade();
			for (final NPSFeedbackQRDetailData npsQuestionDetail : npsFeedbackQRData.getFeedbackQRList())
			{
				final NPSFeedbackQuestionForm npsQRDetail = new NPSFeedbackQuestionForm();
				npsQRDetail.setQuestionCode(npsQuestionDetail.getQuestionCode());
				npsQRDetail.setQuestionName(npsQuestionDetail.getQuestionName());
				npsFeedbackQRDetail.add(npsQRDetail);
			}
			npsFeedbackQRForm.setNpsQuestionlist(npsFeedbackQRDetail);
			npsFeedbackQRForm.setTransactionId(transactionId);
			//npsFeedbackQRForm.setFirstName(firstName);

			model.addAttribute("npsFeedbackForm", npsFeedbackQRForm);
			returnStatement = ControllerConstants.Views.Fragments.NPS_Emailer.NPSFeedback;
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
		//return "feedbackQuestion";
	}

	@RequestMapping(value = "/NPSFeedbackForm", method = RequestMethod.POST)
	public String getFeedbackCapturedData(@ModelAttribute("npsFeedbackForm") final NPSFeedbackQRForm feedbackForm,
			final BindingResult result, final Model model) throws CMSItemNotFoundException
	{
		String returnStatement = null;
		final NPSFeedbackQRData feedbackData = new NPSFeedbackQRData();
		final List<NPSFeedbackQRDetailData> feedbackDataDetail = new ArrayList<>();
		try
		{
			if (result.hasErrors())
			{
				return ControllerConstants.Views.Fragments.NPS_Emailer.NPSFeedback;
			}
			else
			{
				feedbackData.setTransactionId(feedbackForm.getTransactionId());
				feedbackData.setAnyOtherFeedback(feedbackForm.getOtherFeedback());
				for (final NPSFeedbackQuestionForm formDetail : feedbackForm.getNpsQuestionlist())
				{
					final NPSFeedbackQRDetailData feedbackDetailData = new NPSFeedbackQRDetailData();
					feedbackDetailData.setQuestionCode(formDetail.getQuestionCode());

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