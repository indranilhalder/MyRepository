/**
 *
 */
package com.tils.luxury.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.tils.luxury.dao.HomePageTypesDao;
import com.tils.luxury.service.HomePageTypesService;
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

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.lux.core.homepagetypes.HomePageTypesService#getHomePageTypesCode(java.lang.String)
	 */
	@Override
	public HomePageTypesModel getHomePageTypesCode(final String code)
	{
		// YTODO Auto-generated method stub
		return homePageTypesDao.getHomePageTypeCode(code);
	}

}
