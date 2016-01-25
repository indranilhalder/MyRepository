package com.tisl.mpl.cockpits.cscockpit.validators.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.WrongValueException;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cscockpit.model.editor.validators.impl.AbstractCockpitValidator;
import de.hybris.platform.cscockpit.utils.LabelUtils;

public class PostalCodeCockpitValidator extends AbstractCockpitValidator {

	private static final Logger LOG = Logger
			.getLogger(PostalCodeCockpitValidator.class);
	private static final String PIN_REGEX = "^([0-9]{6})$";

	public void validate(Object value) throws WrongValueException {

		PropertyDescriptor propertyDescriptor = getPropertyDescriptor();

		if (propertyDescriptor == null) {
			LOG.warn("Unique attribute validation could not be performed. Reason: No property descriptor found.");
			return;
		}

		if (StringUtils.isBlank(value.toString())
				|| StringUtils.isBlank(value.toString().trim())) {
			throw new WrongValueException(getInputElement(),
					LabelUtils.getLabel("cscockpit.widget.address.create",
							"textboxValueField"));
		} else {
			if (value.toString().length() > 255) {
				throw new WrongValueException(getInputElement(),
						LabelUtils.getLabel("cscockpit.widget.address.create",
								"textboxValueInvalid"));
			}
			if (!(value.toString().matches(PIN_REGEX))) {
				throw new WrongValueException(getInputElement(),
						LabelUtils.getLabel("cscockpit.widget.address.create",
								"postalCodeValueIncorrect"));
			}
		}

		if (!(LOG.isDebugEnabled()))
			return;
		LOG.debug("Validation seems to be ok.");
	}
}