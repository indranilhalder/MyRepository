/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tisl.mpl.core.model.NPSFeedbackModel;
import com.tisl.mpl.core.model.NPSFeedbackQuestionModel;
import com.tisl.mpl.facade.nps.NPSFeedbackQuestionFacade;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.web.forms.NPSFeedbackQRForm;
import com.tisl.mpl.storefront.web.forms.NPSFeedbackQuestion;


/**
 * @author 1256972
 *
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/feedback")
public class NPSFeedbackController
{

	@Autowired
	private NPSFeedbackQuestionFacade npsFeedbackQuestionFacade;
	@Resource(name = ModelAttributetConstants.SESSION_SERVICE)
	private SessionService sessionService;
	@Resource
	private ModelService modelService;

	@RequestMapping(value = "/NPSFeedbackForm", method = RequestMethod.GET)
	public String getFeedbackQuestionDetails(final Model model, @RequestParam("firstName") final String firstName,
			@RequestParam("transactionId") final String transactionId)

	{

		System.out.println("222222222");
		System.out.println(firstName);
		System.out.println(transactionId);
		/*
		 * final String name = request.getParameter("firstName"); final String transactionId =
		 * request.getParameter("transactionID"); System.out.println(name); System.out.println(transactionId);
		 */


		List<NPSFeedbackQuestionModel> npsFeedbackQuestionList = new ArrayList<NPSFeedbackQuestionModel>();
		npsFeedbackQuestionList = npsFeedbackQuestionFacade.getFeedbackQuestionFacade();

		final List<NPSFeedbackQuestion> npsFeedbackQuestionDatalist = new ArrayList<NPSFeedbackQuestion>();

		for (final NPSFeedbackQuestionModel npsfeedback : npsFeedbackQuestionList)
		{
			final NPSFeedbackQuestion npsFeedbackQuestionData = new NPSFeedbackQuestion();
			npsFeedbackQuestionData.getFirstName();
			npsFeedbackQuestionData.setQuestionCode(npsfeedback.getQuestionCode());
			npsFeedbackQuestionData.setQuestionName(npsfeedback.getQuestion());
			npsFeedbackQuestionData.setTransactionId(transactionId);
			npsFeedbackQuestionData.setFirstName(firstName);
			npsFeedbackQuestionDatalist.add(npsFeedbackQuestionData);
		}
		final NPSFeedbackQRForm npsFeedbackQRForm = new NPSFeedbackQRForm();
		npsFeedbackQRForm.setNpsFeedbackQuestionlist(npsFeedbackQuestionDatalist);
		//form.setNpsFeedbackQuestionlist(npsFeedbackQuestionDatalist);
		//model.addAttribute("NPSFeedbackQuestion", npsFeedbackQuestionDatalist);
		//model.addAttribute("NPSFeedbackOneForm", new NPSFeedbackQuestionForm());
		//model.addAttribute("NPSFeedbackOneForm", form);
		model.addAttribute("NPSFeedbackOneForm", npsFeedbackQRForm);



		System.out.println("aaaaaaa");
		return ControllerConstants.Views.Fragments.NPS_Emailer.NPSFeedback;
		//return "feedbackQuestion";
	}

	@RequestMapping(value = "/NPSFeedbackOne", method = RequestMethod.GET)
	public String getFeedbackCapturedData(@ModelAttribute("NPSFeedbackOne") final NPSFeedbackQRForm form,
			final BindingResult result)
	{

		if (result.hasErrors())
		{
			return ControllerConstants.Views.Fragments.NPS_Emailer.NPSFeedback;
		}
		else
		{

			final List<NPSFeedbackModel> npsFeedbackModelList = new ArrayList<NPSFeedbackModel>();
			for (final NPSFeedbackQuestion formList : form.getNpsFeedbackQuestionlist())
			{
				final NPSFeedbackModel npsFeedback = modelService.create(NPSFeedbackModel.class);
				npsFeedback.setFirstName(formList.getFirstName());
				npsFeedback.setTransactionId(formList.getTransactionId());
				npsFeedbackModelList.add(npsFeedback);
			}

			modelService.saveAll(npsFeedbackModelList);
			return ControllerConstants.Views.Fragments.NPS_Emailer.SubmitSuccess;
		}


	}
}