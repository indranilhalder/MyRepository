package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.marketplacecommerceservices.daos.ContactUsDao;
import com.tisl.mpl.marketplacecommerceservices.service.ContactUsService;


/**
 * @author TCS
 *
 */
public class ContactUsServiceImpl implements ContactUsService
{


	private ContactUsDao contactUsDao;

	/**
	 * @return the contactUsDao
	 */
	public ContactUsDao getContactUsDao()
	{
		return contactUsDao;
	}

	/**
	 * @param contactUsDao
	 *           the contactUsDao to set
	 */
	@Required
	public void setContactUsDao(final ContactUsDao contactUsDao)
	{
		this.contactUsDao = contactUsDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.contactus.ContactUsTabComponentService#getOrderId(java.lang.String
	 * )
	 */
	@Override
	public List<OrderModel> getOrderModel(final String orderCode)
	{
		// YTODO Auto-generated method stub
		return contactUsDao.getOrderModel(orderCode);
	}

}