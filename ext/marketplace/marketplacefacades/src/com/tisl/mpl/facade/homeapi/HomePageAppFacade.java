/**
 *
 */
package com.tisl.mpl.facade.homeapi;

import com.tisl.mpl.wsdto.AutomatedBrandProductCarouselDTO;
import com.tisl.mpl.wsdto.BannersCarouselDTO;
import com.tisl.mpl.wsdto.ComponentRequestDTO;
import com.tisl.mpl.wsdto.HomepageComponentRequestDTO;
import com.tisl.mpl.wsdto.ThemeOffersDTO;
import com.tisl.mpl.wsdto.VideoProductCaraouselDTO;


/**
 * @author TCS
 *
 */
public interface HomePageAppFacade
{
	public void getAdobeTargetDataOfferWidget(ComponentRequestDTO componentRequestDTO);

	public ThemeOffersDTO getThemeOffersComponentDTO(HomepageComponentRequestDTO themeOffersRequestDTO);

	/**
	 * @param bannerCarouselRequestDTO
	 * @return
	 */
	public BannersCarouselDTO getBannerProductCarouselDTO(HomepageComponentRequestDTO bannerCarouselRequestDTO);

	/**
	 * @param videoProductCaraouselRequestDTO
	 * @return
	 */
	public VideoProductCaraouselDTO getVideoProductCarouselDTO(HomepageComponentRequestDTO videoProductCaraouselRequestDTO);

	/**
	 * @param automatedBrandRequestDTO
	 * @return
	 */
	public AutomatedBrandProductCarouselDTO getautomatedBrandCarouselDTO(HomepageComponentRequestDTO automatedBrandRequestDTO);


}
