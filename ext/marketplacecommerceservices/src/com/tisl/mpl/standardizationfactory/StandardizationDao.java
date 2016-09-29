/**
 *
 */
package com.tisl.mpl.standardizationfactory;

import java.util.List;

import com.tisl.mpl.core.model.SizesystemModel;


/**
 * @author TCS
 *
 */
public interface StandardizationDao
{

	Double findConversionUnit(final String to, final String from);

	List<SizesystemModel> getStandardValue(final String propertyName);
}