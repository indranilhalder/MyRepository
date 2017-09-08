/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tisl.mpl.constants.MarketplacecheckoutaddonConstants;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.pojo.request.QCRedeemRequest;
import com.tisl.mpl.pojo.response.QCInitializationResponse;
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

	private static final Logger LOG = Logger.getLogger(WalletController.class);



	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	public String getWalletView(final Model model)

			throws CMSItemNotFoundException
	{
		if (false)
		{
			final QCInitializationResponse qcresp = mplWalletFacade.getWalletInitilization();
			qcresp.getTransactionId();
		}

		if (false)
		{
			mplWalletFacade.createWalletContainer();
		}

		if (false)
		{
			mplWalletFacade.purchaseEGV();
		}

		//mplWalletFacade.addEGVToWallet();

		mplWalletFacade.getQCBucketBalance();
		final QCRedeemRequest qw = new QCRedeemRequest();
		mplWalletFacade.getWalletRedeem("", "", qw);


		storeCmsPageInModel(model, getContentPageForLabelOrId("TULWalletPage"));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId("TULWalletPage"));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				resourceBreadcrumbBuilder.getBreadcrumbs(MarketplacecheckoutaddonConstants.PAYMENTBREADCRUMB));
		model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}


	static
	{

		System.out.println("QC Initilization ******************");

	}

}


