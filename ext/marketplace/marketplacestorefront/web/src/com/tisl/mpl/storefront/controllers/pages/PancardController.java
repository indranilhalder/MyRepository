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
	@RequestMapping(value = "/pancarddetailsupload/{orderreferancenumber}/{customername}", method = RequestMethod.GET)
	public String pancardDetailsUploadPage(@PathVariable final String orderreferancenumber,
			@PathVariable final String customername, final Model model)
	{

		//System.out.println("***********************************pancard" + orderReferenceNumber);

		final PancardInformationModel oModel = mplPancardFacade.getPanCardOredrId(orderreferancenumber);

		model.addAttribute("orderreferancenumber", orderreferancenumber);
		model.addAttribute("customername", customername);

		if (oModel != null && StringUtils.equalsIgnoreCase(oModel.getStatus(), "Approved"))
		{
			return ControllerConstants.Views.Pages.Pancard.panCardApproved;//if the pancard is already approved by CRM
		}

		else
		{

			return ControllerConstants.Views.Pages.Pancard.PanCardDetail;//Open the pancard upload page
		}
	}

	//For uploading pancard image

	@RequestMapping(value = "/pancardupload", method = RequestMethod.POST)
	public String pancardDetailsUpload(@RequestParam("orderreferancenumber") final String orderreferancenumber,
			@RequestParam("Customer_name") final String customername, @RequestParam("Pancard_number") final String pancardnumber,
			@RequestParam("file") final MultipartFile file, final Model model)
	{
		model.addAttribute("orderreferancenumber", orderreferancenumber);
		model.addAttribute("filename", file.getOriginalFilename());

		/*
		 * if (file.isEmpty() && customername.isEmpty() && pancardnumber.isEmpty()) { //If any field is left empty //not
		 * needed return ControllerConstants.Views.Pages.Pancard.panCardUploadDetailsError;//If any field is left empty }
		 * else {
		 */

		//for fetching the model corresponding to the orderreferancenumber


		final PancardInformationModel oModel = mplPancardFacade.getPanCardOredrId(orderreferancenumber);


		//If the file correspond to the orderreferancenumber already exist and for pancard details updation and resnding ticket
		if (null != oModel && null != oModel.getOrderId()
				&& StringUtils.equalsIgnoreCase(orderreferancenumber, oModel.getOrderId()))
		{

			//for updating pancard number
			oModel.setPancardNumber(pancardnumber);

			mplPancardFacade.refreshPancardDetailsFacade(oModel, file);
			final String fileexist = "**** !!!!!  Pancard details updated ****";
			model.addAttribute("fileexist", fileexist);
			return ControllerConstants.Views.Pages.Pancard.panCardUploadUpdate;

		}
		//for new pancard details entry and ticket create
		else
		{
			mplPancardFacade.setPancardFileAndOrderId(orderreferancenumber, customername, pancardnumber, file);
			return ControllerConstants.Views.Pages.Pancard.panCardUploadSuccess;
		}

		//	}

	}


}
