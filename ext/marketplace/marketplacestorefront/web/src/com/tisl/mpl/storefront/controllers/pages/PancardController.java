/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.core.model.PancardInformationModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
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
		model.addAttribute("orderreferancenumber", orderreferancenumber);
		model.addAttribute("customername", customername);
		//model.addAttribute("transactionid", transactionid);
		boolean ifRejected = true;
		final List<PancardInformationModel> pModelList = mplPancardFacade.getPanCardOredrId(orderreferancenumber);

		if (CollectionUtils.isNotEmpty(pModelList))
		{
			for (final PancardInformationModel pModel : pModelList)
			{
				if (null != pModel.getStatus() && StringUtils.isNotEmpty(pModel.getStatus()))
				{
					//					if (pModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.APPROVED))
					//					{
					//						model.addAttribute("status", pModel.getStatus());
					//						return ControllerConstants.Views.Pages.Pancard.panCardApproved;
					//					}
					//					if (pModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.PENDING_FOR_VERIFICATION)
					//							|| pModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.NA))
					//					{
					//						model.addAttribute("status", pModel.getStatus());
					//						return ControllerConstants.Views.Pages.Pancard.panCardApproved;
					//					}
					//
					if (pModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.REJECTED))
					{
						ifRejected = false;
						break;
						//return ControllerConstants.Views.Pages.Pancard.PanCardDetail;
					}
				}
			}
			if (!ifRejected)
			{
				return ControllerConstants.Views.Pages.Pancard.PanCardDetail;
			}
			else
			{
				model.addAttribute(MarketplacecommerceservicesConstants.APPROVED);
				return ControllerConstants.Views.Pages.Pancard.panCardApproved;
			}
		}

		return ControllerConstants.Views.Pages.Pancard.PanCardDetail;
	}

	//For uploading pancard image

	@RequestMapping(value = "/pancardupload", method = RequestMethod.POST)
	public String pancardDetailsUpload(@RequestParam("orderreferancenumber") final String orderreferancenumber,
			@RequestParam("Customer_name") final String customername, @RequestParam("Pancard_number") final String pancardnumber,
			@RequestParam("file") final MultipartFile file, final Model model)
	{
		model.addAttribute("orderreferancenumber", orderreferancenumber);
		model.addAttribute("filename", file.getOriginalFilename());

		boolean ifRejected = true;
		PancardInformationModel pancRejModel = null;
		final List<String> transEntryList = new ArrayList<String>();
		final List<PancardInformationModel> pModelList = mplPancardFacade.getPanCardOredrId(orderreferancenumber);

		if (null != pModelList)
		{
			for (final PancardInformationModel pModel : pModelList)
			{
				if (pModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.REJECTED))
				{
					ifRejected = false;
					pancRejModel = pModel;
					break;
				}
			}
			if (!ifRejected)
			{
				mplPancardFacade.refreshPanCardDetailsAndPIcall(pModelList, pancRejModel, pancardnumber, file);
				return ControllerConstants.Views.Pages.Pancard.panCardUploadSuccess;
			}
			//			if (pModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.REJECTED))
			//			{
			//				mplPancardFacade.refreshPanCardDetailsAndPIcall(pModel, pancardnumber, file);
			//				return ControllerConstants.Views.Pages.Pancard.panCardUploadSuccess;
			//			}
		}

		final List<OrderModel> oModelList = mplPancardFacade.getOrderForCode(orderreferancenumber);
		for (final OrderModel oModel : oModelList)
		{
			for (final OrderModel childOModel : oModel.getChildOrders())
			{
				for (final AbstractOrderEntryModel entry : childOModel.getEntries())
				{
					if (MarketplacecommerceservicesConstants.FINEJEWELLERY.equalsIgnoreCase(entry.getProduct()
							.getProductCategoryType()))
					{
						transEntryList.add(entry.getOrderLineId());
					}
				}
			}
		}
		mplPancardFacade.setPanCardDetailsAndPIcall(orderreferancenumber, transEntryList, customername, pancardnumber, file);
		return ControllerConstants.Views.Pages.Pancard.panCardUploadSuccess;
	}
}
