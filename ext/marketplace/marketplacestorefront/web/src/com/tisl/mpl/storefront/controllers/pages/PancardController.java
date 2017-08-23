/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.core.model.PancardInformationModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
	private static final Logger LOG = Logger.getLogger(PancardController.class);

	@Resource(name = "mplPancardFacadeImpl")
	private MplPancardFacade mplPancardFacade;

	private static final String Checkstatus = "status";

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
	@RequestMapping(value = "/pancarddetailsupload/{orderReferanceNumber}/{customerName}", method = RequestMethod.GET)
	public String pancardDetailsUploadPage(@PathVariable final String orderReferanceNumber,
			@PathVariable final String customerName,
			@RequestParam(value = "status", defaultValue = "", required = false) final String status, final Model model)
	{
		model.addAttribute("orderreferancenumber", orderReferanceNumber);
		model.addAttribute("customername", customerName);

		if (StringUtils.isNotEmpty(status))
		{
			model.addAttribute("failure", "Something went wrong.Please try again!!.");
		}

		boolean ifRejected = false;
		boolean ifPending = false;
		final List<PancardInformationModel> pModelList = mplPancardFacade.getPanCardOredrId(orderReferanceNumber);

		if (CollectionUtils.isNotEmpty(pModelList))
		{
			for (final PancardInformationModel pModel : pModelList)
			{
				if (StringUtils.isNotEmpty(pModel.getStatus()))
				{
					if (pModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.REJECTED))
					{
						ifRejected = true;
						break;
						//return ControllerConstants.Views.Pages.Pancard.PanCardDetail;
					}
				}
			}
			if (ifRejected)
			{
				//model.addAttribute(status, MarketplacecommerceservicesConstants.REJECTED);
				return ControllerConstants.Views.Pages.Pancard.PanCardDetail;
			}
			else
			{
				//model.addAttribute(MarketplacecommerceservicesConstants.APPROVED);
				for (final PancardInformationModel pModel : pModelList)
				{
					if (StringUtils.isNotEmpty(pModel.getStatus()))
					{
						if (pModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.PENDING_FOR_VERIFICATION))
						{
							ifPending = true;
							break;
						}
					}
				}
				if (ifPending)
				{
					model.addAttribute(Checkstatus, MarketplacecommerceservicesConstants.PENDING_FOR_VERIFICATION);
					//return ControllerConstants.Views.Pages.Pancard.panCardApproved;
					return ControllerConstants.Views.Pages.Pancard.PanCardDetail;
				}
				else
				{
					model.addAttribute(Checkstatus, MarketplacecommerceservicesConstants.APPROVED);
					//return ControllerConstants.Views.Pages.Pancard.panCardApproved;
					return ControllerConstants.Views.Pages.Pancard.PanCardDetail;
				}
				//return ControllerConstants.Views.Pages.Pancard.panCardApproved;
			}
		}
		return ControllerConstants.Views.Pages.Pancard.PanCardDetail;
	}

	//For uploading pancard image

	@RequestMapping(value = "/pancardupload", method = RequestMethod.POST)
	public String pancardDetailsUpload(@RequestParam("orderreferancenumber") final String orderReferanceNumber,
			@RequestParam("Customer_name") final String customerName, @RequestParam("Pancard_number") final String pancardNumber,
			@RequestParam("file") final MultipartFile file, final Model model)
	{
		try
		{
			model.addAttribute("orderreferancenumber", orderReferanceNumber);
			model.addAttribute("filename", file.getOriginalFilename());
			model.addAttribute("customername", customerName);

			final List<String> transEntryList = new ArrayList<String>();
			final List<PancardInformationModel> pModelR = new ArrayList();
			final List<PancardInformationModel> pModelList = mplPancardFacade.getPanCardOredrId(orderReferanceNumber);

			if (null != pModelList && CollectionUtils.isNotEmpty(pModelList))
			{
				for (final PancardInformationModel pModel : pModelList)
				{
					if (pModel.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.REJECTED))
					{
						pModelR.add(pModel);
					}
				}
				if (CollectionUtils.isNotEmpty(pModelR))
				{
					mplPancardFacade.refreshPanCardDetailsAndPIcall(pModelList, pModelR, pancardNumber, file);
					return ControllerConstants.Views.Pages.Pancard.panCardUploadSuccess;
				}
			}

			final List<OrderModel> oModelList = mplPancardFacade.getOrderForCode(orderReferanceNumber);
			if (null != oModelList && CollectionUtils.isNotEmpty(oModelList))
			{
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
			}
			mplPancardFacade.setPanCardDetailsAndPIcall(orderReferanceNumber, transEntryList, customerName, pancardNumber, file);
		}
		catch (final JAXBException e)
		{
			LOG.error("the JAXBException exception is**" + e);
			return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.PANCARDREDIRECTURL
					+ orderReferanceNumber + "/" + customerName + MarketplacecommerceservicesConstants.PANCARDREDIRECTURLSUFFIX
					+ MarketplacecommerceservicesConstants.FAILURE;
		}
		catch (final IOException e)
		{
			LOG.error("the IOException exception is**" + e);
			return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.PANCARDREDIRECTURL
					+ orderReferanceNumber + "/" + customerName + MarketplacecommerceservicesConstants.PANCARDREDIRECTURLSUFFIX
					+ MarketplacecommerceservicesConstants.FAILURE;
		}
		catch (final IllegalStateException e)
		{
			LOG.error("the IllegalStateException exception is**" + e);
			return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.PANCARDREDIRECTURL
					+ orderReferanceNumber + "/" + customerName + MarketplacecommerceservicesConstants.PANCARDREDIRECTURLSUFFIX
					+ MarketplacecommerceservicesConstants.FAILURE;
		}
		catch (final IllegalArgumentException e)
		{
			LOG.error("the IllegalArgumentException exception is**" + e);
			return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.PANCARDREDIRECTURL
					+ orderReferanceNumber + "/" + customerName + MarketplacecommerceservicesConstants.PANCARDREDIRECTURLSUFFIX
					+ MarketplacecommerceservicesConstants.FAILURE;
		}
		catch (final Exception e)
		{
			LOG.error("the exception is**" + e);
			return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.PANCARDREDIRECTURL
					+ orderReferanceNumber + "/" + customerName + MarketplacecommerceservicesConstants.PANCARDREDIRECTURLSUFFIX
					+ MarketplacecommerceservicesConstants.FAILURE;
		}
		return ControllerConstants.Views.Pages.Pancard.panCardUploadSuccess;
	}
}
