/**
 *
 */
package com.tisl.mpl.facade.homeapi;

import com.tisl.mpl.wsdto.ComponentRequestDTO;
import com.tisl.mpl.wsdto.ThemeOffersDTO;
import com.tisl.mpl.wsdto.ThemeOffersRequestDTO;


/**
 * @author TCS
 *
 */
public interface HomePageAppFacade
{
	public void getAdobeTargetDataOfferWidget(ComponentRequestDTO componentRequestDTO);

	public ThemeOffersDTO getThemeOffersComponentDTO(ThemeOffersRequestDTO themeOffersRequestDTO, String themeOffersJSONString);
}
