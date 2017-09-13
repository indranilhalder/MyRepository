/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.constants.MarketplacecheckoutaddonConstants;
import com.tisl.mpl.core.model.CustomerWalletDetailModel;
import com.tisl.mpl.exception.QCServiceCallException;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.pojo.request.Customer;
import com.tisl.mpl.pojo.request.QCCustomerRegisterRequest;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.service.MplQCInitServiceImpl;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;


/**
 * @author Nirav Bhanushali TUL
 *
 */


@Controller
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(value = RequestMappingUrlConstants.LINK_WALLET)
@RequireHardLogIn
public class WalletController extends AbstractPageController
{

	@Resource(name = "mplPaymentFacade")
	private MplPaymentFacade mplPaymentFacade;
	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "checkoutFacade")
	private MplCheckoutFacade mplCheckoutFacade;
	@Resource(name = "cartService")
	private CartService cartService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "simpleBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = ModelAttributetConstants.SESSION_SERVICE)
	private SessionService sessionService;

	@Resource(name = "mplWalletFacade")
	private MplWalletFacade mplWalletFacade;


	@Resource(name = "mplQCInitService")
	private MplQCInitServiceImpl mplQCInitService;

	private static final Logger LOG = Logger.getLogger(WalletController.class);



	@SuppressWarnings("boxing")
	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	public String getWalletView(final Model model)

			throws CMSItemNotFoundException, QCServiceCallException
	{

		mplQCInitService.loadQCInit();

		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();

		if (null != currentCustomer.getIsWalletActivated())
		{
			model.addAttribute("isWalletActive", currentCustomer.getIsWalletActivated());
		}
		else
		{
			model.addAttribute("isWalletActive", false);
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId("TULWalletPage"));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId("TULWalletPage"));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				resourceBreadcrumbBuilder.getBreadcrumbs(MarketplacecheckoutaddonConstants.PAYMENTBREADCRUMB));
		model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}


	//	static
	//	{
	//		try
	//		{
	//			mplQCInitService.loadQCInit();
	//		}
	//		catch (final QCServiceCallException e)
	//		{
	//			e.printStackTrace();
	//		}
	//
	//	}

	/**
	 * @param request
	 * @param model
	 */
	@SuppressWarnings("boxing")
	@RequestMapping(value = "/registerCustomerWallet", method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody String registerCustomerWallet(final HttpServletRequest request, final Model model)
			throws UnsupportedEncodingException
	{

		String response = StringUtils.EMPTY;
		try
		{
			final QCCustomerRegisterRequest customerRegisterReq = new QCCustomerRegisterRequest();
			final Customer custInfo = new Customer();

			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();

			custInfo.setEmail(currentCustomer.getOriginalUid());
			custInfo.setEmployeeID(currentCustomer.getUid());
			custInfo.setCorporateName("Tata Unistore Ltd");

			if (null != currentCustomer.getFirstName())
			{

				custInfo.setFirstname(currentCustomer.getFirstName());
			}
			if (null != currentCustomer.getLastName())
			{

				custInfo.setLastName(currentCustomer.getLastName());
			}

			customerRegisterReq.setExternalwalletid(currentCustomer.getOriginalUid());

			customerRegisterReq.setCustomer(custInfo);

			customerRegisterReq.setNotes("Activating Customer " + currentCustomer.getOriginalUid());

			final QCCustomerRegisterResponse customerRegisterResponse = mplWalletFacade.createWalletContainer(customerRegisterReq);

			if (customerRegisterResponse.getResponseCode() == 0)
			{
				final CustomerWalletDetailModel custWalletDetail = modelService.create(CustomerWalletDetailModel.class);
				custWalletDetail.setWalletId(customerRegisterResponse.getWallet().getWalletNumber());
				custWalletDetail.setWalletState(customerRegisterResponse.getWallet().getStatus());
				custWalletDetail.setCustomer(currentCustomer);
				custWalletDetail.setServiceProvider("Tata Unistore Ltd");

				modelService.save(custWalletDetail);

				currentCustomer.setCustomerWalletDetail(custWalletDetail);
				currentCustomer.setIsWalletActivated(true);
				modelService.save(currentCustomer);

				response = "Success";
			}
			else
			{
				response = "Fail to Active Customer Wallet " + customerRegisterResponse.getErrorCode() + "|"
						+ customerRegisterResponse.getResponseMessage();
			}
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			response = "Fail";
			return response;
		}

		return response;
	}

}


