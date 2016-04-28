/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.commercefacades.product.data.PincodeServiceData;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.model.PincodeServiceabilityDataModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPincodeServiceDao;


/**
 * @author TCS
 *
 */
public class MplPincodeServiceDaoImpl implements MplPincodeServiceDao
{

	@Autowired
	private FlexibleSearchService flexibleSearchService;
	private static final Logger LOG = Logger.getLogger(MplPincodeServiceDaoImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.MplPincodeServiceDao#getPincodeServicableDataAtCommerce(com.tisl
	 * .mpl.wsdto.PinCodeDeliveryModeListRequest)
	 */
	@Override
	public List<PincodeServiceabilityDataModel> getPincodeServicableDataAtCommerce(final String pin,
			final List<PincodeServiceData> pincodeServiceDataList)
	{

		try
		{

			final StringBuilder query = new StringBuilder(500);
			int count = 0;
			for (final PincodeServiceData pincodeServiceData : pincodeServiceDataList)
			{
				if (count != 0)
				{
					query.append(" UNION ALL ");
				}

				query.append(" {{").append("select {pk} from {PincodeServiceabilityData} where {pincode} ='").append(pin)
						.append("\'").append(" and {sellerId}='").append(pincodeServiceData.getSellerId()).append("\'")
						.append(" and {fulfillmentType}='").append(pincodeServiceData.getFullFillmentType().toUpperCase()).append("\'")
						.append(" and {deliveryMode} in (").append(getdeliveryModes(pincodeServiceData.getDeliveryModes())).append(')')
						.append("}} ");

				++count;
			}

			final String queryString = "select AA.pk from (" + query.toString() + " ) AA";
			LOG.debug("==================" + queryString);
			List<PincodeServiceabilityDataModel> PincodeServiceabilityDataModelList = null;
			final SearchResult<PincodeServiceabilityDataModel> searchRes = flexibleSearchService.search(queryString);
			if (searchRes != null && searchRes.getCount() > 0)
			{
				PincodeServiceabilityDataModelList = searchRes.getResult();
			}

			return PincodeServiceabilityDataModelList;

		}

		catch (final Exception ex)
		{
			LOG.debug(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}


	private String getdeliveryModes(final List<MarketplaceDeliveryModeData> marketplaceDeliveryModeDataList)
	{
		String deliveryModes = "";
		String deliveryModataString = null;
		for (final MarketplaceDeliveryModeData deliveryModeData : marketplaceDeliveryModeDataList)
		{

			if ("Home Delivery".equalsIgnoreCase(deliveryModeData.getName()))
			{
				deliveryModes += "'HD',";
			}
			if ("Express Delivery".equalsIgnoreCase(deliveryModeData.getName()))
			{
				deliveryModes += "'ED',";
			}
		}

		if (deliveryModes.length() > 0)
		{
			deliveryModataString = deliveryModes.substring(0, deliveryModes.length() - 1);
		}

		return deliveryModataString;
	}


	/**
	 * Fetch data to invalidate
	 *
	 * @param sysdate
	 * @param jobLastRunDate
	 */
	@Override
	public List<PincodeServiceabilityDataModel> getPincodeData(final Date jobLastRunDate, final Date sysdate)
	{
		try
		{
			String queryString = MarketplacecommerceservicesConstants.EMPTY;
			boolean flag = false;

			if (null != jobLastRunDate)
			{
				queryString = "SELECT {pin.pk} FROM {PincodeServiceabilityData as pin} WHERE ( {pin.modifiedtime} between ?jobTime and ?currenttime )";
				flag = true;
			}
			else
			{
				queryString = "SELECT {pin.pk} FROM {PincodeServiceabilityData as pin} ";
			}

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

			if (flag)
			{
				query.addQueryParameter("jobTime", jobLastRunDate);
				query.addQueryParameter("currenttime", sysdate);
			}


			LOG.debug("Query" + queryString);

			final List<PincodeServiceabilityDataModel> dataList = flexibleSearchService.<PincodeServiceabilityDataModel> search(
					query).getResult();


			if (CollectionUtils.isNotEmpty(dataList))
			{
				LOG.debug("Data List pks size" + dataList.size());

				return dataList;
			}
			else
			{
				return null;
			}
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}




}
