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

	public ThemeOffersDTO getThemeOffersComponentDTO(HomepageComponentRequestDTO themeOffersRequestDTO,
			String themeOffersJSONString);

	/**
	 * @param bannerCarouselRequestDTO
	 * @param bannerProductcarouselJsonString
	 * @return
	 */
	public BannersCarouselDTO getBannerProductCarouselDTO(HomepageComponentRequestDTO bannerCarouselRequestDTO,
			String bannerProductcarouselJsonString);

	/**
	 * @param videoProductCaraouselRequestDTO
	 * @param videoProductcarouselJsonString
	 * @return
	 */
	public VideoProductCaraouselDTO getVideoProductCarouselDTO(HomepageComponentRequestDTO videoProductCaraouselRequestDTO,
			String videoProductcarouselJsonString);

	/**
	 * @param automatedBrandRequestDTO
	 * @param automatedBrandCaraouselJsonString
	 * @return
	 */
	public AutomatedBrandProductCarouselDTO getautomatedBrandCarouselDTO(HomepageComponentRequestDTO automatedBrandRequestDTO,
			String automatedBrandCaraouselJsonString);


}
