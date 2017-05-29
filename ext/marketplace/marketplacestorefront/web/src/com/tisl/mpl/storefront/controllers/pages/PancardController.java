/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.core.model.PancardInformationModel;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tisl.mpl.facade.pancard.MplPancardFacade;
import com.tisl.mpl.storefront.controllers.ControllerConstants;



@Controller
@Scope("tenant")
@RequestMapping(value = "/pancard")
public class PancardController
{


	@Resource(name = "mplPancardFacadeImpl")
	private MplPancardFacade mplPancardFacade;

	/*
	 * @ResponseBody
	 *
	 * @RequestMapping(value = "/pancardupload/test123", method = RequestMethod.GET) public String getTest123() throws
	 * CMSItemNotFoundException { System.out.println("Test123****************"); return "test123"; }
	 */

	/**
	 * @return the mplPancardFacade
	 */
	public MplPancardFacade getMplPancardFacade()
	{
		return mplPancardFacade;
	}

	/**
	 * @param mplPancardFacade
	 *           the mplPancardFacade to set
	 */
	public void setMplPancardFacade(final MplPancardFacade mplPancardFacade)
	{
		this.mplPancardFacade = mplPancardFacade;
	}

	//For displaying pancard upload page
	@RequestMapping(value = "/pancarddetailsupload/{orderreferancenumber}/{transactionid}/{customername}", method = RequestMethod.GET)
	public String pancardDetailsUploadPage(@PathVariable final String orderreferancenumber,
			@PathVariable final String transactionid, @PathVariable final String customername, final Model model)
	{
		String status = null;
		String crmStatus = null;
		model.addAttribute("orderreferancenumber", orderreferancenumber);
		model.addAttribute("customername", customername);
		model.addAttribute("transactionid", transactionid);

		final PancardInformationModel oModel = mplPancardFacade.getPanCardOredrId(orderreferancenumber);

		if (null != oModel && StringUtils.isNotEmpty(oModel.getStatus()))
		{
			status = oModel.getStatus();
		}

		if (null != oModel && StringUtils.isNotEmpty(status) && StringUtils.equalsIgnoreCase(status, "Approved"))
		{
			model.addAttribute("status", status);
			return ControllerConstants.Views.Pages.Pancard.panCardApproved;//if the pancard is already approved by CRM
		}

		else
		{
			if (null != oModel)
			{
				//CRM call and status update in commerce
				crmStatus = mplPancardFacade.getCrmStatusForPancardDetailsFacade(oModel);
			}

			if (null != oModel && StringUtils.isNotEmpty(status) && StringUtils.equalsIgnoreCase(status, "Approved"))
			{
				model.addAttribute("status", status);
				return ControllerConstants.Views.Pages.Pancard.panCardApproved;//if the pancard is already approved by CRM
			}
			else if (null != oModel && StringUtils.isNotEmpty(crmStatus) && StringUtils.equalsIgnoreCase(crmStatus, "Locked"))
			{
				model.addAttribute("status", crmStatus);
				return ControllerConstants.Views.Pages.Pancard.panCardApproved;//if the pancard varification is locked by CRM
			}
			else
			{
				return ControllerConstants.Views.Pages.Pancard.PanCardDetail;//pancard upload page In_progress,Rejected or FirstTime upload
			}
		}
	}

	//For uploading pancard image

	@RequestMapping(value = "/pancardupload", method = RequestMethod.POST)
	public String pancardDetailsUpload(@RequestParam("orderreferancenumber") final String orderreferancenumber,
			@RequestParam("Customer_name") final String customername, @RequestParam("Pancard_number") final String pancardnumber,
			@RequestParam("transactionid") final String transactionid, @RequestParam("file") final MultipartFile file,
			final Model model)
	{
		model.addAttribute("orderreferancenumber", orderreferancenumber);
		model.addAttribute("filename", file.getOriginalFilename());

		final PancardInformationModel oModel = mplPancardFacade.getPanCardOredrId(orderreferancenumber);


		//If the file correspond to the orderreferancenumber already exist and for pancard details updation and resending ticket
		//i.e. for rejected and in progress
		if (null != oModel && StringUtils.isNotEmpty(pancardnumber) && (!file.isEmpty())
				&& StringUtils.equalsIgnoreCase(orderreferancenumber, oModel.getOrderId()))
		{

			mplPancardFacade.refreshPancardDetailsFacade(oModel, file, pancardnumber);

			final String fileexist = "**** !!!!!  Pancard details updated ****";
			model.addAttribute("fileexist", fileexist);
			return ControllerConstants.Views.Pages.Pancard.panCardUploadUpdate;

		}
		//for new pancard details entry and ticket create
		else
		{
			mplPancardFacade.setPancardFileAndOrderId(orderreferancenumber, transactionid, customername, pancardnumber, file);
			return ControllerConstants.Views.Pages.Pancard.panCardUploadSuccess;
		}

	}


}
