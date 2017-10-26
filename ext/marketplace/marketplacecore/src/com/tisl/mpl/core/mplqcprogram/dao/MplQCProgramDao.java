/**
 * 
 */
package com.tisl.mpl.core.mplqcprogram.dao;

import com.tisl.mpl.core.model.MplQCProgramConfigModel;

/**
 * @author Tech
 *
 */
public interface MplQCProgramDao
{
	public MplQCProgramConfigModel getProgramIdConfigValueById(final String id);
}
