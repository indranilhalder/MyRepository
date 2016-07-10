package com.tisl.mpl.cockpits.cscockpit.services.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.type.TypeService;

public class EmployeeValidateInterceptor implements ValidateInterceptor {

	private TypeService typeService;
	public static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
	private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*&.]).{8,16})";
	private static final String REGEX = "[a-zA-Z ]+";

	@Override
	public void onValidate(final Object model, final InterceptorContext ctx)
			throws InterceptorException {
		if (model instanceof EmployeeModel) {
			final EmployeeModel employee = (EmployeeModel) model;
			Pattern namepattern = Pattern.compile(REGEX);
			final Pattern passwordpattern = Pattern.compile(PASSWORD_PATTERN);
			final Pattern emailpattern = Pattern.compile(EMAIL_REGEX);
			if (!StringUtils.isEmpty(employee.getUid())) {
				if (!NumberUtils.isNumber(employee.getUid())) {
					throw new InterceptorException(
							"Login Id should only be numeric");
				}
			}
			if (StringUtils.isEmpty(employee.getName())) {
				throw new InterceptorException("Name is a mandatory field");
			} else {
				Matcher namematcher = namepattern.matcher(employee.getName());
				if (!namematcher.matches()) {
					throw new InterceptorException(
							"Name cannot contain special characters");
				}
			}
			if (employee.getGroups().isEmpty()) {
				throw new InterceptorException("Group is a mandatory field");
			}
			if (!StringUtils.isEmpty(employee.getEmail())) {
				Matcher emailmatcher = emailpattern
						.matcher(employee.getEmail());
				if (!emailmatcher.matches()) {
					throw new InterceptorException("Email Id is invalid");
				}
			}
			if (!StringUtils.isEmpty(employee.getEncodedPassword())) {
				Matcher passwordmatcher = passwordpattern.matcher(employee
						.getEncodedPassword());
				if (!passwordmatcher.matches()) {
					throw new InterceptorException("Password format is Invalid");
				}
			}
		}
	}

	protected TypeService getTypeService() {
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService) {
		this.typeService = typeService;
	}
}

