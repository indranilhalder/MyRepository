/**
 *
 */
package com.tisl.mpl.helper;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author TCS
 *
 */
public class MplEnumerationHelper
{
	@Autowired
	private EnumerationService enumService;

	@Autowired
	private TypeService typeService;

	/**
	 * Gets the enumeration values for code.
	 *
	 * @param code
	 *           the code
	 * @return the enumeration values for code
	 */
	public List<EnumerationValueModel> getEnumerationValuesForCode(final String code)
	{
		final List<EnumerationValueModel> enumValueList = new ArrayList<EnumerationValueModel>();

		final List<HybrisEnumValue> list = enumService.getEnumerationValues(code);
		for (int i = 0; i < list.size(); i++)
		{
			final HybrisEnumValue residential = list.get(i);
			enumValueList.add(typeService.getEnumerationValue(residential));
		}
		return enumValueList;
	}
}
