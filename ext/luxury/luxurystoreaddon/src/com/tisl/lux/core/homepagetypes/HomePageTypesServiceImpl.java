/**
 *
 */
package com.tisl.lux.core.homepagetypes;

import java.util.List;

import javax.annotation.Resource;

import com.tisl.lux.model.HomePageTypesModel;


/**
 * @author abhishek.singh
 *
 */
public class HomePageTypesServiceImpl implements HomePageTypesService
{
	@Resource
	HomePageTypesDao homePageTypesDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.lux.core.homepagetypes.HomePageTypesService#getHomePageTypes()
	 */
	@Override
	public List<HomePageTypesModel> getHomePageTypes()
	{
		return homePageTypesDao.getHomePageType();
	}

}
