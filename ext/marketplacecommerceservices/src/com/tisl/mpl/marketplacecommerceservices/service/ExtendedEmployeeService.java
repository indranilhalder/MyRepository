package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;


public interface ExtendedEmployeeService extends UserService
{
	public List<EmployeeModel> getAllUsers();
}
