/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.MplleadVariantWeightService;
import com.tisl.mpl.model.SellerInformationModel;



/**
 * @author Tcs
 *
 */
public class MplleadVariantWeightServiceImpl implements MplleadVariantWeightService
{

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.PriceBreakupService#getPricebreakup(java.lang.String,
	 * java.lang.String)
	 */
	@Autowired
	private ModelService modelService;
	@Autowired
	private MplSellerInformationService mplSellerInformationService;

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}




	//	private static final Integer LISTINGID = Integer.valueOf("0");//SONAR FIX JEWELLERY
	private static final Integer USSID = Integer.valueOf("1");


	@Override
	public boolean updateSellerinfoHasvariant(final Map<Integer, String> line)
	{
		try
		{
			final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(line.get(USSID));

			//	sellerInfoModel.setHasVariant(Boolean.valueOf(true)); SONAR FIX JEWELLERY
			sellerInfoModel.setHasVariant(Boolean.TRUE);
			modelService.save(sellerInfoModel);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex);
		}

		return true;
	}
}