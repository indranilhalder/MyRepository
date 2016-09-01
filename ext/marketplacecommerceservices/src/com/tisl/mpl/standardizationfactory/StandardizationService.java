/**
 *
 */
package com.tisl.mpl.standardizationfactory;



/**
 * @author TCS
 *
 */
public interface StandardizationService
{
	Double getStandardValue(final String stringValue, final String unitType);

	String getStandardValueNonNumeric(final String stringValue, final String propertyName, final String unitType);
}