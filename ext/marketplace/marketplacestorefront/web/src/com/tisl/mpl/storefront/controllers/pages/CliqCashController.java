/**
 * 
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.pojo.response.WalletTrasacationsListData;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.pages.AbstractMplSearchPageController.ShowMode;
import com.tisl.mpl.storefront.web.forms.AddToCardWalletForm;

/**
 * @author Tech
 *
 */
@Controller
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(value = "cliqCash")
public class CliqCashController extends AbstractPageController
{
	
	@Resource(name = "mplWalletFacade")
	private MplWalletFacade mplWalletFacade;

	
	@RequestMapping(value = "/getcliqcashPage", method = RequestMethod.GET)
	@RequireHardLogIn
	public String getCliqCash(final Model model) throws CMSItemNotFoundException{
		System.out.println("HI>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		/*storeCmsPageInModel(model, getContentPageForLabelOrId("cliqcashPage"));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId("cliqcashPage"));
		model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);*/
		String balanceAmount ="0";
		
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
		
		//return getViewForPage(model);
		return "addon:/marketplacecheckoutaddon/pages/checkout/single/cliqcash";
	}
	

	
	

}
