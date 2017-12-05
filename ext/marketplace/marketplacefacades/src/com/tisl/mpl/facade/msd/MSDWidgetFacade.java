/**
 *
 */
package com.tisl.mpl.facade.msd;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.data.MSDRequestdata;
import com.tisl.mpl.facades.data.MSDResponsedata;





/**
 * @author TCS
 *
 */
public interface MSDWidgetFacade
{
	public MSDResponsedata getMSDWidgetFinalData(final MSDRequestdata msdRequest) throws EtailNonBusinessExceptions;
}
