package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.List;

import com.tisl.mpl.marketplacecommerceservices.daos.ExtendedEmployeeDao;


public class ExtendedEmployeeDaoImpl extends DefaultGenericDao<EmployeeModel> implements ExtendedEmployeeDao
{
	public ExtendedEmployeeDaoImpl()
	{
		super(EmployeeModel._TYPECODE);
	}

	@Override
	public List<EmployeeModel> findAllUsers()
	{
		final List<EmployeeModel> resList = find();
		return resList.isEmpty() ? null : resList;
	}
}
