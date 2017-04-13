/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.impl;

//import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.MplDeliveryAddressInfoModel;
import com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.MplDeliveryAddressDao;



/**
 * @author pankajk
 *
 */
public class MplDeliveryAddressDaoImpl implements MplDeliveryAddressDao
{

	private static final Logger LOG = Logger.getLogger(MplDeliveryAddressDaoImpl.class);
	
	private static final String MPL_DELIVERY_ADDRESS_REPORT_QUERY_BY_ORDERID = "SELECT {srm:" + MplDeliveryAddressInfoModel.PK + "}" + " FROM {"
			+ MplDeliveryAddressInfoModel._TYPECODE + " AS srm} " + "WHERE " + "{srm:" + MplDeliveryAddressInfoModel.ORDERID + "}=?code ";

 
	private static final String MPL_DELIVERY_ADDRESS_REPORT_QUERY_BETWEEN_TWO_DATES= "SELECT {srm:" + MplDeliveryAddressInfoModel.PK + "}" + " FROM {"
			+ MplDeliveryAddressInfoModel._TYPECODE + " AS srm} " + "WHERE " + "{srm:" + MplDeliveryAddressInfoModel.CREATIONTIME + "} between ?fromDate and ?toDate ";

	

	@Autowired
	private FlexibleSearchService flexibleSearchService;
	/*@Autowired
	private ModelService modelService;*/

	
	
	/**
	 * @Description Get the report model by order id
	 * @param orderCode
	 * @return TULDeliveryAddressReportModel
	 */
	@Override
	public MplDeliveryAddressInfoModel getMplDeliveryAddressReportModelByOrderId(final String orderCode)
	{
		try
		{
			if(LOG.isDebugEnabled()){
				LOG.debug("In getMplDeliveryAddressReportModelByOrderId - orderCode ***"+orderCode);
			}
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(MPL_DELIVERY_ADDRESS_REPORT_QUERY_BY_ORDERID);
			fQuery.addQueryParameter("code", orderCode);

			final List<MplDeliveryAddressInfoModel> listOfData = flexibleSearchService.<MplDeliveryAddressInfoModel> search(fQuery).getResult();
			return !listOfData.isEmpty() ? listOfData.get(0) : null;
		}
		catch (final Exception e)
		{
			LOG.error("�rror while searching for MplDeliveryAddress  Report model for order  id" + orderCode);
		}
		return null;
	}

	
	
	
	
	
	
	/**
	 * @Description Get the report models between two dates
	 * @param fromDate
	 * @param toDate
	 * @return MplDeliveryAddressInfoModel
	 */
	@Override
	public List<MplDeliveryAddressInfoModel> getMplDeliveryAddressReportModels(Date fromDate,Date toDate)
	{
		try
		{
			if(LOG.isDebugEnabled()){
				LOG.debug("In getMplDeliveryAddressReportModels - fromDate: ="+fromDate +"todate :="+toDate);
			}
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(MPL_DELIVERY_ADDRESS_REPORT_QUERY_BETWEEN_TWO_DATES);
			fQuery.addQueryParameter("fromDate", fromDate);
			fQuery.addQueryParameter("toDate", toDate);
			return  flexibleSearchService.<MplDeliveryAddressInfoModel> search(fQuery).getResult();
		}
		catch (final Exception e)
		{
			LOG.error("�rror while searching for MplDeliveryAddress Report models between From Date:"+fromDate +"toDate:"+toDate);
		}
		return null;
	}
	
}
