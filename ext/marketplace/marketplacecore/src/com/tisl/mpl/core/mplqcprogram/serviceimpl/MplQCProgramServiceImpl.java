/**
 * 
 */
package com.tisl.mpl.core.mplqcprogram.serviceimpl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.MplQCProgramConfigModel;
import com.tisl.mpl.core.mplconfig.service.impl.MplConfigServiceImpl;
import com.tisl.mpl.core.mplqcprogram.dao.MplQCProgramDao;
import com.tisl.mpl.core.mplqcprogram.service.MplQCProgramService;

/**
 * @author Tech
 *
 */
public class MplQCProgramServiceImpl implements MplQCProgramService
{

	private static final Logger LOGGER = Logger.getLogger(MplConfigServiceImpl.class);
	@Autowired
	private MplQCProgramDao mplQCProgramDao;
	
	@Override
	public MplQCProgramConfigModel getProgramIdConfigValueById(String programId)
	{
		return mplQCProgramDao.getProgramIdConfigValueById(programId);
	}

}
