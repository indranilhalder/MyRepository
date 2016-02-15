package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.impl.ExtendedEmployeeDaoImpl;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedEmployeeService;


public class ExtendedEmployeeServiceImpl extends DefaultUserService implements ExtendedEmployeeService
{
	@Autowired
	private ExtendedEmployeeDaoImpl extendedEmployeeDao;

	@Override
	public List<EmployeeModel> getAllUsers()
	{
		return this.extendedEmployeeDao.findAllUsers();
	}
}
