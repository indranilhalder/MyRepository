/**
 *
 */
package com.tisl.lux.facades.homepagetypes;

import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.tisl.lux.core.homepagetypes.HomePageTypesService;
import com.tisl.lux.facades.homepagetypes.data.HomePageTypesData;
import com.tisl.lux.model.HomePageTypesModel;


/**
 * @author abhishek.singh
 *
 */
public class HomePageTypesFacadeImpl implements HomePageTypesFacade
{

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.lux.facades.homepagetypes.HomePageTypesFacade#getHomePageType()
	 */
	@Resource
	private Converter<HomePageTypesModel, HomePageTypesData> homePageTypesConverter;
	@Resource
	HomePageTypesService homePageTypesService;

	@Override
	public List<HomePageTypesData> getHomePageType()
	{
		// YTODO Auto-generated method stub

		final List<HomePageTypesData> listOfHomePageTypes = new ArrayList<HomePageTypesData>();
		if (null != homePageTypesService.getHomePageTypes())
		{
			for (final HomePageTypesModel homePageTypesModel : homePageTypesService.getHomePageTypes())
			{
				final HomePageTypesData HomePageTypesData = new HomePageTypesData();
				listOfHomePageTypes.add(homePageTypesConverter.convert(homePageTypesModel, HomePageTypesData));
			}
			return listOfHomePageTypes;
		}

		return null;
	}

}
