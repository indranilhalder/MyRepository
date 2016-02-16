/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.PDPEmailProcessModel;


/**
 * @author TCS
 *
 */
public class DefaultPDPEmailNotificationService implements PDPEmailNotificationService
{
	@Autowired
	private UserService userService;

	@Autowired
	private BusinessProcessService businessProcessService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private BaseSiteService baseSiteService;

	@Autowired
	private BaseStoreService baseStoreService;

	@Autowired
	private CommonI18NService commonI18NService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.PDPEmailNotificationService#sendMail(de.hybris.platform.
	 * commerceservices.model.process.PDPEmailProcessModel)
	 */
	@Override
	public void sendMail(final List<String> emailList, final ProductModel productModel, final String url)
	{
		for (final String emailId : emailList)
		{
			final PDPEmailProcessModel pDPEmailProcessModel = (PDPEmailProcessModel) businessProcessService.createProcess(
					"PDPEmail-" + System.currentTimeMillis(), "PDPEmailProcess");
			//	final ProductModel productModel = productService.getProductForCode(productId);
			pDPEmailProcessModel.setProduct(productModel);
			final CustomerModel customer = (CustomerModel) userService.getCurrentUser();
			pDPEmailProcessModel.setCustomer(customer);
			pDPEmailProcessModel.setSite(baseSiteService.getCurrentBaseSite());
			pDPEmailProcessModel.setCustomerEmail(emailId);
			pDPEmailProcessModel.setProductUrl(url);
			//	pDPEmailProcessModel.setProductUrl(contextPath);
			final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();
			if (currentBaseStore != null)
			{
				pDPEmailProcessModel.setStore(currentBaseStore);
			}
			pDPEmailProcessModel.setLanguage(commonI18NService.getCurrentLanguage());

			modelService.save(pDPEmailProcessModel);
			businessProcessService.startProcess(pDPEmailProcessModel);
		}
	}

}
