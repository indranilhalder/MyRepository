/**
 *
 */
package com.tisl.mpl.facade.product.impl;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.location.impl.LocationDTO;
import de.hybris.platform.storelocator.location.impl.LocationDtoWrapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.facade.product.MplProductFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
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


	//private PincodeServiceFacade pincodeServiceFacade;

	@Autowired
	private Provider<PincodeServiceFacade> pincodeServiceFacadeProvider;

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
		final List<PointOfServiceData> filteredPosData = new ArrayList<PointOfServiceData>();
		final LocationDTO dto = new LocationDTO();
		try
		{

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
				final PincodeServiceFacade pincodeServiceFacade = pincodeServiceFacadeProvider.get();
				posData = pincodeServiceFacade.getStoresForPincode(myLocation.getGPS(), radius);
				for (final PointOfServiceData obj : posData)
				{
					if (obj.getClicknCollect().equalsIgnoreCase("Y"))
					{
						filteredPosData.add(obj);
					}
				}


				posData = mplPincodeDistanceService.pincodeDistance(filteredPosData, pincodeModel.getLatitude(),
						pincodeModel.getLongitude(), pincode);
			}
		}
		catch (final Exception e)
		{
			LOG.error("Something went wrong in calling getAllStoresForPincode");
		}
		return posData;
	}
}
