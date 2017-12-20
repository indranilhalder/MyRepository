/**
 *
 */
package com.tisl.mpl.facade.msd;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.data.MSDRequestdata;





/**
 * @author TCS
 *
 */
public interface MSDWidgetFacade
{
	public String getMSDWidgetFinalData(final MSDRequestdata msdRequest) throws EtailNonBusinessExceptions;
}
