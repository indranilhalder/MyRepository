/**
 * 
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.storelocator.exception.LocationInstantiationException;
import de.hybris.platform.storelocator.location.impl.DefaultLocation;
import de.hybris.platform.storelocator.model.PointOfServiceModel;


/**
 * @author Techouts
 *
 */
public class CustomLocation extends DefaultLocation
{
	private PointOfServiceModel posModel;

	/**
	 * @param posModel
	 * @param distance
	 * @throws LocationInstantiationException
	 */
	public CustomLocation(PointOfServiceModel posModel, Double distance) throws LocationInstantiationException
	{
		super(posModel, distance);
		this.posModel = posModel;
	}

	@Override
	public String getName()
	{
		if (this.posModel != null)
		{
			return this.posModel.getSlaveId();
		}
		return null;
	}

}
