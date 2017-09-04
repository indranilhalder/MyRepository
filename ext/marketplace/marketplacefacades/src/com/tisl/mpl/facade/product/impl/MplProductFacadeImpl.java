/**
 *
 */
package com.tisl.mpl.facade.product.impl;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.location.impl.LocationDTO;
import de.hybris.platform.storelocator.location.impl.LocationDtoWrapper;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.facade.product.MplProductFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.MplPincodeDistanceService;
import com.tisl.mpl.marketplacecommerceservices.service.MplProductService;
import com.tisl.mpl.marketplacecommerceservices.service.PincodeService;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;


/**
 * @author i313024
 *
 */
public class MplProductFacadeImpl implements MplProductFacade
{

	private MplProductService mplProductService;
	@Resource(name = "pincodeServiceFacade")
	private PincodeServiceFacade pincodeServiceFacade;

	@Resource(name = "pincodeService")
	private PincodeService pincodeService;
	@Resource
	private MplConfigFacade mplConfigFacade;
	@Resource
	private MplPincodeDistanceService mplPincodeDistanceService;
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(MplProductFacadeImpl.class);
	@Resource
	private ConfigurationService configurationService;

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

	/**
	 * @param pincode
	 * @return
	 */
	@Override
	public List<PointOfServiceData> storeLocatorPDP(final String pincode)
	{

		if (LOG.isDebugEnabled())
		{
			LOG.debug("from getAllStoresForPincode method");
		}
		List<PointOfServiceData> posData = new ArrayList<PointOfServiceData>();
		final LocationDTO dto = new LocationDTO();
		try
		{
			final boolean distanceFlag = configurationService.getConfiguration().getBoolean("google.distance.enable");
			String radius = mplConfigFacade.getCongigValue(MarketplaceFacadesConstants.CONFIGURABLE_RADIUS);
			LOG.debug("configurableRadius is:" + radius);
			if (null == radius)
			{
				radius = "0";
			}
			Location myLocation = null;
			//fetch latitude and longitude for a pincode from comm
			final PincodeModel pincodeModel = pincodeService.getLatAndLongForPincode(pincode);

			if (null != pincodeModel)
			{
				dto.setLatitude(pincodeModel.getLatitude().toString());
				dto.setLongitude(pincodeModel.getLongitude().toString());
				myLocation = new LocationDtoWrapper(dto);
				posData = pincodeServiceFacade.getStoresForPincode(myLocation.getGPS(), radius);
				if (distanceFlag)
				{
					posData = mplPincodeDistanceService.pincodeDistance(posData, pincodeModel.getLatitude(),
							pincodeModel.getLongitude(), pincode);
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Something went wrong in calling getAllStoresForPincode");
		}
		return posData;
	}
}
