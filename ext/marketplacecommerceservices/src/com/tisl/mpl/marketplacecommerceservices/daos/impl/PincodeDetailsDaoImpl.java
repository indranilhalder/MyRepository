/**
 * 
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.PincodeDetailsDao;


/**
 * @author Dileep
 *
 */
public class PincodeDetailsDaoImpl implements PincodeDetailsDao
{

	@Autowired
	private FlexibleSearchService flexibleSearchService;
	private static final Logger LOG = Logger.getLogger(PincodeDetailsDaoImpl.class);

	
	@Override
	public PincodeModel getPincodeModel(String pincode)
	{
		LOG.info("PincodeDetails DAO Class");
		try
		{
			String query = "select {" + PincodeModel.PK + "} from {" + PincodeModel._TYPECODE + "} where {" + PincodeModel.PINCODE
					+ "}=?pincode";
			FlexibleSearchQuery flexQuery = new FlexibleSearchQuery(query);
			flexQuery.addQueryParameter("pincode", pincode);
			SearchResult<PincodeModel> result = flexibleSearchService.search(flexQuery);
			LOG.info("After the FlexibleSearchService Query");
			if (null != result && null != result.getResult() && !result.getResult().isEmpty())
			{
				return result.getResult().get(0);
			}
			throw new EtailBusinessExceptions();

		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailBusinessExceptions();
		}
		catch (Exception e) 
		{
			
			throw new EtailBusinessExceptions();
		}

	}
}
