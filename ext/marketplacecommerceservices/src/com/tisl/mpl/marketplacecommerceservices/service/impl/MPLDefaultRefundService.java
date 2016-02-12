/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.refund.impl.DefaultRefundService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReplacementEntryModel;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.tisl.mpl.core.model.OrderRefundConfigModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MPLOrderCancelDao;
import com.tisl.mpl.marketplacecommerceservices.daos.MPLRefundDao;
import com.tisl.mpl.marketplacecommerceservices.daos.MPLReplacementDao;
import com.tisl.mpl.marketplacecommerceservices.service.MPLRefundService;



/**
 * @author 890223
 *
 */
public class MPLDefaultRefundService extends DefaultRefundService implements MPLRefundService
{

	@Resource
	private MPLRefundDao mplrefundDao;
	@Resource
	private MPLReplacementDao mplreplacmentDao;
	@Resource
	private MPLOrderCancelDao mplorderCancelDao;


	/**
	 * @return the mplrefundDao
	 */
	public MPLRefundDao getMplrefundDao()
	{
		return mplrefundDao;
	}




	/**
	 * @param mplrefundDao
	 *           the mplrefundDao to set
	 */
	public void setMplrefundDao(final MPLRefundDao mplrefundDao)
	{
		this.mplrefundDao = mplrefundDao;
	}




	/**
	 * @return the mplreplacmentDao
	 */
	public MPLReplacementDao getMplreplacmentDao()
	{
		return mplreplacmentDao;
	}




	/**
	 * @param mplreplacmentDao
	 *           the mplreplacmentDao to set
	 */
	public void setMplreplacmentDao(final MPLReplacementDao mplreplacmentDao)
	{
		this.mplreplacmentDao = mplreplacmentDao;
	}




	/**
	 * @return the mplorderCancelDao
	 */
	public MPLOrderCancelDao getMplorderCancelDao()
	{
		return mplorderCancelDao;
	}




	/**
	 * @param mplorderCancelDao
	 *           the mplorderCancelDao to set
	 */
	public void setMplorderCancelDao(final MPLOrderCancelDao mplorderCancelDao)
	{
		this.mplorderCancelDao = mplorderCancelDao;
	}




	@Override
	public OrderRefundConfigModel getOrderRefundConfiguration() throws Exception
	{
		return mplrefundDao.getOrderRefundConfiguration();
	}




	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MPLRefundService#getAllRefund(java.util.Date)
	 */
	@Override
	public List<RefundEntryModel> getAllRefund(final Date startDate) throws Exception
	{
		return mplrefundDao.getAllRefunds(startDate);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MPLRefundService#getAllRefund(java.util.Date,
	 * java.util.Date)
	 */
	@Override
	public List<RefundEntryModel> getAllRefund(final Date startDate, final Date endDate) throws Exception
	{
		return mplrefundDao.getAllRefunds(startDate, endDate);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MPLRefundService#getAllReplacement(java.util.Date,
	 * java.util.Date)
	 */
	@Override
	public List<ReplacementEntryModel> getAllReplacement(final Date startDate, final Date endDate) throws Exception
	{
		return mplreplacmentDao.getAllReplacement(startDate, endDate);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MPLRefundService#getAllReplacement(java.util.Date)
	 */
	@Override
	public List<ReplacementEntryModel> getAllReplacement(final Date startDate) throws Exception
	{
		return mplreplacmentDao.getAllReplacement(startDate);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MPLRefundService#getAllCancelled(java.util.Date,
	 * java.util.Date)
	 */
	@Override
	public List<OrderCancelRecordEntryModel> getAllCancelled(final Date startDate, final Date endDate) throws Exception
	{
		// YTODO Auto-generated method stub
		return mplorderCancelDao.getAllCancelled(startDate, endDate);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MPLRefundService#getAllCancelled(java.util.Date)
	 */
	@Override
	public List<OrderCancelRecordEntryModel> getAllCancelled(final Date startDate) throws Exception
	{
		// YTODO Auto-generated method stub
		return mplorderCancelDao.getAllCancelled(startDate);
	}




	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MPLRefundService#getAllRefund()
	 */
	@Override
	public List<RefundEntryModel> getAllRefund() throws Exception
	{
		// YTODO Auto-generated method stub
		return mplrefundDao.getAllRefunds();
	}




	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MPLRefundService#getAllReplacement()
	 */
	@Override
	public List<ReplacementEntryModel> getAllReplacement() throws Exception
	{
		// YTODO Auto-generated method stub
		return mplreplacmentDao.getAllReplacement();
	}




	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MPLRefundService#getAllCancelled()
	 */
	@Override
	public List<OrderCancelRecordEntryModel> getAllCancelled() throws Exception
	{
		// YTODO Auto-generated method stub
		return mplorderCancelDao.getAllCancelled();
	}
}
