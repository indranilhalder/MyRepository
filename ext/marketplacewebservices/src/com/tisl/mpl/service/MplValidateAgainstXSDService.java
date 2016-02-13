/**
 *
 */
package com.tisl.mpl.service;

import java.io.InputStream;
import java.util.Map;


/**
 * @author TCS
 *
 */
public interface MplValidateAgainstXSDService
{
	Map validateAgainstXSD(final InputStream xml, final String absoluteDiskPath);


}
