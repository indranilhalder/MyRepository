package com.tisl.mpl.cockpits.cscockpit.validators.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.WrongValueException;

import com.tisl.mpl.core.enums.AddressType;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.core.Registry;
import de.hybris.platform.cscockpit.model.editor.validators.impl.AbstractCockpitValidator;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.enumeration.EnumerationService;

public class AddressTypeCockpitValidator extends AbstractCockpitValidator {

	private static final Logger LOG = Logger
			.getLogger(AddressTypeCockpitValidator.class);

	final EnumerationService enumerationService = Registry
			.getApplicationContext().getBean("enumerationService",
					EnumerationService.class);

	public void validate(Object value) throws WrongValueException {

		PropertyDescriptor propertyDescriptor = getPropertyDescriptor();

		if (propertyDescriptor == null) {
			LOG.warn("Unique attribute validation could not be performed. Reason: No property descriptor found.");
			return;
		}

		List<AddressType> values = enumerationService
				.getEnumerationValues(AddressType.class);
		Boolean flag = false;

		if ((value.toString() == null)) {
			throw new WrongValueException(getInputElement(),
					LabelUtils.getLabel("cscockpit.widget.address.create",
							"addressTypeValueField"));
		} else {
			for (AddressType addressType : values) {
				if (value.toString().equalsIgnoreCase(addressType.getCode())) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				throw new WrongValueException(getInputElement(),
						LabelUtils.getLabel("cscockpit.widget.address.create",
								"textboxValueInvalid"));
			}
		}

		if (!(LOG.isDebugEnabled()))
			return;
		LOG.debug("Validation seems to be ok.");
	}
}