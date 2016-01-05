package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.user.EmployeeModel;

import java.util.List;


public interface ExtendedEmployeeDao
{
	List<EmployeeModel> findAllUsers();
}
