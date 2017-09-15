/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
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
import com.tisl.mpl.pojo.response.CustomerWalletDetailResponse;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.service.MplQCInitServiceImpl;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.web.forms.AddToCardWalletForm;

import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

import com.tisl.mpl.pojo.response.WalletTrasacationsListData;
/**
 * @author TUL
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
	protected static final String REDIM_WALLET_CODE_PATTERN = "/redimWallet";


//	@SuppressWarnings("boxing")
//	@RequestMapping(method = RequestMethod.GET)
//	@RequireHardLogIn
//	public String getWalletView(final Model model)
//
//			throws CMSItemNotFoundException, QCServiceCallException
//	{
//
//		//mplQCInitService.loadQCInit();
//
//		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
//
//		if (null != currentCustomer.getIsWalletActivated())
//		{
//			model.addAttribute("isWalletActive", currentCustomer.getIsWalletActivated());
//		}
//		else
//		{
//			model.addAttribute("isWalletActive", false);
//		}
//
//		storeCmsPageInModel(model, getContentPageForLabelOrId("TULWalletPage"));
//		setUpMetaDataForContentPage(model, getContentPageForLabelOrId("TULWalletPage"));
//		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
//				resourceBreadcrumbBuilder.getBreadcrumbs(MarketplacecheckoutaddonConstants.PAYMENTBREADCRUMB));
//		model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
//		model.addAttribute("addToCardWalletForm", new AddToCardWalletForm());
//		return getViewForPage(model);
//	}


	
	@RequestMapping(value = REDIM_WALLET_CODE_PATTERN, method = RequestMethod.POST)
	public String getRedimWalletView(@ModelAttribute("addToCardWalletForm")  AddToCardWalletForm addToCardWalletForm,  Model model) throws CMSItemNotFoundException{
		System.out.println("**************cardNumber*********************************"+addToCardWalletForm.getCardNumber());
		System.out.println("**************cardPin*********************************"+addToCardWalletForm.getCardPin());
		String balanceAmount ="0";
		balanceAmount = mplWalletFacade.getRedimWallet(addToCardWalletForm.getCardNumber(),addToCardWalletForm.getCardPin());
	   if(balanceAmount.isEmpty()){
	   	balanceAmount="0";
	   }

	   List<WalletTrasacationsListData> walletTrasacationsListData = mplWalletFacade.getWalletTransactionList();
		System.out.println("walletTrasacationsListData List :"+walletTrasacationsListData.size());
		
		 List<WalletTrasacationsListData> cashBackWalletTrasacationsList =mplWalletFacade.getCashBackWalletTrasacationsList(walletTrasacationsListData ,"ADD CARD TO WALLET");
	    System.out.println("***************cashBackWalletTrasacationsListData:"+cashBackWalletTrasacationsList.size());
		final ContentPageModel contentPage = getContentPageForLabelOrId("cliqcashPage");
		storeCmsPageInModel(model, contentPage);
		setUpMetaDataForContentPage(model, contentPage);
		model.addAttribute("WalletBalance", balanceAmount);
		model.addAttribute("walletTrasacationsListData", walletTrasacationsListData);
		model.addAttribute("cashBackWalletTrasacationsList", cashBackWalletTrasacationsList);
		
		//return "addon:/marketplacecheckoutaddon/pages/checkout/single/cliqcash";
		
		return REDIRECT_PREFIX +"/wallet/getcliqcashPage";
	}

@SuppressWarnings("boxing")
	@RequestMapping(value = "/getcliqcashPage", method = RequestMethod.GET)
	@RequireHardLogIn
	public String getCliqCash(final Model model) throws CMSItemNotFoundException, QCServiceCallException{
		
		mplQCInitService.loadQCInit();
      double balanceAmount =0;
		
		List<WalletTrasacationsListData> walletTrasacationsListData = null;
		List<WalletTrasacationsListData> cashBackWalletTrasacationsList = null;
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		
		if (null != currentCustomer.getIsWalletActivated()){
			
			CustomerWalletDetailResponse customerWalletDetailData= mplWalletFacade.getCustomerWallet(currentCustomer.getCustomerWalletDetail().getWalletId());
			
			if(customerWalletDetailData.getResponseCode() == 0){
				
				balanceAmount = customerWalletDetailData.getWallet().getBalance();
			}
			
		
			
		   walletTrasacationsListData = mplWalletFacade.getWalletTransactionList();
			System.out.println("walletTrasacationsListData List :"+walletTrasacationsListData.size());
			cashBackWalletTrasacationsList = mplWalletFacade.getCashBackWalletTrasacationsList(walletTrasacationsListData ,"ADD CARD TO WALLET");
		   System.out.println("***************cashBackWalletTrasacationsListData:"+cashBackWalletTrasacationsList.size());
		
		}else{
			  try{
				final QCCustomerRegisterRequest customerRegisterReq = new QCCustomerRegisterRequest();
				final Customer custInfo = new Customer();
				custInfo.setEmail(currentCustomer.getOriginalUid());
				custInfo.setEmployeeID(currentCustomer.getUid());
				custInfo.setCorporateName("Tata Unistore Ltd");
				
				if (null != currentCustomer.getFirstName()){
					custInfo.setFirstname(currentCustomer.getFirstName());
				}if (null != currentCustomer.getLastName()){
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
				}
			}
			catch (final Exception ex)
			{
				ex.printStackTrace();
			}
		}
		final ContentPageModel contentPage = getContentPageForLabelOrId("cliqcashPage");
		storeCmsPageInModel(model, contentPage);
		setUpMetaDataForContentPage(model, contentPage);
		model.addAttribute("WalletBalance", balanceAmount);
		model.addAttribute("walletTrasacationsListData", walletTrasacationsListData);
		model.addAttribute("cashBackWalletTrasacationsList", cashBackWalletTrasacationsList);
		
		//return getViewForPage(model);
		return "addon:/marketplacecheckoutaddon/pages/checkout/single/cliqcash";
	}
}


