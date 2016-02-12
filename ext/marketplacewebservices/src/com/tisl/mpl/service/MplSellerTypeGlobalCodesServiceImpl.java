/**
 *
 */
package com.tisl.mpl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.dao.MplSellerTypeGlobalCodesDAO;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.model.SellerTypeGlobalCodesModel;


/**
 * @author TCS
 *
 */
public class MplSellerTypeGlobalCodesServiceImpl implements MplSellerTypeGlobalCodesService
{

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.service.MplSellerTypeGlobalCodesService#getDescription()
	 */
	@Autowired
	private MplSellerTypeGlobalCodesDAO mplSellerTypeGlobalCodesDAO;

	/*
	 * Returns a SellerTypeGlobalCodes model object based on global code input (non-Javadoc)
	 *
	 * @see com.tisl.mpl.service.MplSellerTypeGlobalCodesService#getDescription(java.lang.String)
	 *
	 * @code
	 *
	 * @return
	 */
	@Override
	public SellerTypeGlobalCodesModel getDescription(final String code) throws EtailBusinessExceptions, EtailNonBusinessExceptions
	{
		final List<SellerTypeGlobalCodesModel> result = mplSellerTypeGlobalCodesDAO.findSellerTypeDescriptionByCode(code);
		if (null != result && !result.isEmpty())
		{
			return result.get(0);
		}
		else
		{
			return null;
		}
	}

	/**
	 * @return the sellerTypeGlobalCodesDAO
	 */
	public MplSellerTypeGlobalCodesDAO getSellerTypeGlobalCodesDAO()
	{
		return mplSellerTypeGlobalCodesDAO;
	}

	/**
	 * @param sellerTypeGlobalCodesDAO
	 *           the sellerTypeGlobalCodesDAO to set
	 */
	public void setSellerTypeGlobalCodesDAO(final MplSellerTypeGlobalCodesDAO sellerTypeGlobalCodesDAO)
	{
		this.mplSellerTypeGlobalCodesDAO = sellerTypeGlobalCodesDAO;
	}

}
