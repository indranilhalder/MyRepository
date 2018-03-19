/**
 *
 */
package com.tils.luxury.dao;

import java.util.List;

import com.tisl.lux.model.HomePageTypesModel;


/**
 * @author abhishek.singh
 *
 */
public interface HomePageTypesDao
{
	List<HomePageTypesModel> getHomePageType();

	HomePageTypesModel getHomePageTypeCode(String code);

}
