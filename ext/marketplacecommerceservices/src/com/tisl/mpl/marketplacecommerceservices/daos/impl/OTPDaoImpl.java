/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisl.mpl.core.model.OTPModel;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.marketplacecommerceservices.daos.OTPDao;


/**
 * @author TCS
 *
 */
@Component(value = "otpDao")
public class OTPDaoImpl implements OTPDao
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(OTPDaoImpl.class);

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/**
	 * This method fetches the OTP with respect to the customer
	 *
	 * @param customerPK
	 * @param OTPType
	 * @return List<OTPModel>
	 */
	@Override
	public List<OTPModel> fetchOTP(final String customerPK, final OTPTypeEnum OTPType)
	{
		final String queryString = "SELECT {o:" + OTPModel.PK + "} " + "FROM {" + OTPModel._TYPECODE + " AS o} " + "WHERE ({o:"
				+ OTPModel.CUSTOMERID + "})=?customerPK"
				+ /*
				   * Wrong logic to get only invalidated OTP models rather we required to find the latest OTP model
				   * irrespective of its validation state. " AND ({o:" + OTPModel.ISVALIDATED + "})='0'
				   */" AND ({o:" + OTPModel.OTPTYPE + "})=?OTPType" + " ORDER BY {o:" + OTPModel.CREATIONTIME + "} DESC";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("customerPK", customerPK);
		query.addQueryParameter("OTPType", OTPType.getCode());
		return getFlexibleSearchService().<OTPModel> search(query).getResult();

	}

	/**
	 * This method fetches the OTP with respect to the email and mobileNo
	 *
	 * @param emailId
	 * @param mobileNo
	 * @param OTPType
	 * @return List<OTPModel>
	 */
	@Override
	public List<OTPModel> fetchOTP(final String emailId, final String mobileNo, final OTPTypeEnum OTPType)
	{
		final String queryString = "SELECT {o:" + OTPModel.PK + "} "//
				+ "FROM {" + OTPModel._TYPECODE + " AS o} " + "WHERE ({o:" + OTPModel.EMAILID + "})=?emailId" + " AND ({o:"
				+ OTPModel.ISVALIDATED + "})='0' AND ({o:" + OTPModel.MOBILENO + "})=?mobileNo" + " AND ({o:" + OTPModel.OTPTYPE
				+ "})=?OTPType" + " ORDER BY {o:" + OTPModel.CREATIONTIME + "} DESC";

		LOG.debug("Query :::::::::: " + queryString);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("emailId", emailId);
		query.addQueryParameter("mobileNo", mobileNo);
		query.addQueryParameter("OTPType", OTPType.getCode());
		return getFlexibleSearchService().<OTPModel> search(query).getResult();
	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}




}
