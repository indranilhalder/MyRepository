/**
 *
 */
package com.tils.luxury.service;

import java.util.List;

import com.tisl.lux.model.HomePageTypesModel;


/**
 * @author abhishek.singh
 *
 */
public interface HomePageTypesService
{
	List<HomePageTypesModel> getHomePageTypes();

	HomePageTypesModel getHomePageTypesCode(String code);

}
