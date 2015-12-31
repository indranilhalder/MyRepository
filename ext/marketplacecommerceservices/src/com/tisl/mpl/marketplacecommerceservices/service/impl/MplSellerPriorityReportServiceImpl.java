/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.hmc.model.SavedValuesModel;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.MplSellerPriorityReportDAO;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerPriorityReportService;


/**
 * @author TCS
 *
 */
public class MplSellerPriorityReportServiceImpl implements MplSellerPriorityReportService
{
	@Autowired
	private MplSellerPriorityReportDAO mplSellerPriorityReportDAO;

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MplSellerPriorityReportServiceImpl.class.getName());

	/**
	 * It gets the list of Seller Priority Changed Details with in date renge
	 *
	 * @return List<SavedValuesModel>
	 *
	 */
	@Override
	public List<SavedValuesModel> getSellerPriorityDetails(final Date startDate, final Date endDate)
	{
		return getMplSellerPriorityReportDAO().getSellerPriorityDetails(startDate, endDate);
	}


	/**
	 * It gets the list of Seller Priority Changed Details with in date renge
	 *
	 * @return List<SavedValuesModel>
	 *
	 */
	@Override
	public List<SavedValuesModel> getAllSellerPriorityDetails()
	{
		return getMplSellerPriorityReportDAO().getAllSellerPriorityDetails();
	}



	/**
	 * @return the mplSellerPriorityReportDAO
	 */
	public MplSellerPriorityReportDAO getMplSellerPriorityReportDAO()
	{
		return mplSellerPriorityReportDAO;
	}

	/**
	 * @param mplSellerPriorityReportDAO
	 *           the mplSellerPriorityReportDAO to set
	 */
	public void setMplSellerPriorityReportDAO(final MplSellerPriorityReportDAO mplSellerPriorityReportDAO)
	{
		this.mplSellerPriorityReportDAO = mplSellerPriorityReportDAO;
	}

}
