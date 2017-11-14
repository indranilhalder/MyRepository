/**
 *
 */
package com.tisl.mpl.service;

import com.tisl.mpl.facades.data.MSDRequestdata;
import com.tisl.mpl.facades.data.MSDResponsedata;




/**
 * @author TCS
 *
 */
public interface MSDService
{
	public MSDResponsedata checkMSDServiceResponse(final MSDRequestdata msdRequest);
}
