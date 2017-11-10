/**
 *
 */
package com.tisl.mpl.facade.product.impl;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.facade.product.MplProductFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.MplPincodeDistanceService;
import com.tisl.mpl.marketplacecommerceservices.service.MplProductService;
import com.tisl.mpl.marketplacecommerceservices.service.PincodeService;


/**
 * @author i313024
 *
 */
public class MplProductFacadeImpl implements MplProductFacade
{

	private MplProductService mplProductService;


	@Autowired
	@Qualifier("pincodeService")
	private PincodeService pincodeService;

	@Autowired
	@Qualifier("mplConfigFacade")
	private MplConfigFacade mplConfigFacade;

	@Autowired
	@Qualifier("mplPincodeDistanceService")
	private MplPincodeDistanceService mplPincodeDistanceService;

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(MplProductFacadeImpl.class);
	@Resource(name = MarketplaceFacadesConstants.SESSION_SERVICE)
	private SessionService sessionService;

	/**
	 * @return the mplProductService
	 */
	public MplProductService getMplProductService()
	{
		return mplProductService;
	}



	/**
	 * @param mplProductService
	 *           the mplProductService to set
	 */
	public void setMplProductService(final MplProductService mplProductService)
	{
		this.mplProductService = mplProductService;
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.product.MplProductFacade#getProductFeatureModelByProductAndQualifier(de.hybris.platform.
	 * commercefacades.product.data.ProductData, java.lang.String)
	 */
	@Override
	public ProductFeatureModel getProductFeatureModelByProductAndQualifier(final ProductData product, final String qualifier)
	{
		// YTODO Auto-generated method stub
		if (product != null & qualifier != null)
		{
			final String code = product.getCode();
			final List<ProductFeatureModel> productFeatures = mplProductService.findProductFeaturesByQualifierAndProductCode(code,
					qualifier);
			if (productFeatures != null && productFeatures.size() > 0)
			{
				return productFeatures.get(0);
			}
		}
		return null;
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.product.MplProductFacade#getPDPPincodeSession()
	 */
	@Override
	public String getPDPPincodeSession()
	{
		return (String) sessionService.getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.product.MplProductFacade#setPDPPincodeSession(java.lang.String)
	 */
	@Override
	public void setPDPPincodeSession(final String pincode)
	{
		sessionService.setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, pincode);
	}

	@Override
	public String getSizeForSTWProduct(final String productCode)
	{
		return mplProductService.getSizeForSTWProduct(productCode);

	}

	@Override
	public List<String> getVariantsForSTWProducts(final String productCode)
	{
		return mplProductService.getVariantsForSTWProducts(productCode);

	}

}
