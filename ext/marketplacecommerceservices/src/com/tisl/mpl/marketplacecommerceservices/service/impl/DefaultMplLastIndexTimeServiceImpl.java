/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import javax.annotation.Resource;

import com.tisl.mpl.marketplacecommerceservices.daos.MplLastIndexTimeDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplLastIndexTimeService;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public class DefaultMplLastIndexTimeServiceImpl implements MplLastIndexTimeService
{

	@Resource(name = "mplLastIndexTimeDao")
	private MplLastIndexTimeDao mplLastIndexTimeDao;



	@Override
	public MplConfigurationModel getMplConfiguration(final String code)
	{

		return mplLastIndexTimeDao.getMplConfigurationDetails(code);

	}

}
