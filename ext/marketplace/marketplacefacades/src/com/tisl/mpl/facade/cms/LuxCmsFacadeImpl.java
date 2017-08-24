/**
 *
 */
package com.tisl.mpl.facade.cms;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.lux.model.cms.components.ShopOnLuxuryElementModel;
import com.tisl.lux.model.cms.components.ShopOnLuxuryModel;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplCMSPageServiceImpl;
import com.tisl.mpl.wsdto.LuxuryComponentsListWsDTO;
import com.tisl.mpl.wsdto.ShopOnLuxuryElementListWsDTO;
import com.tisl.mpl.wsdto.ShopOnLuxuryElementWsDTO;



public class LuxCmsFacadeImpl implements LuxCmsFacade
{

	private MplCMSPageServiceImpl mplCMSPageService;

	private static final Logger LOG = Logger.getLogger(MplCmsFacadeImpl.class);

	/**
	 * @return the mplCMSPageService
	 */
	public MplCMSPageServiceImpl getMplCMSPageService()
	{
		return mplCMSPageService;
	}

	/**
	 * @param mplCMSPageService
	 *           the mplCMSPageService to set
	 */
	public void setMplCMSPageService(final MplCMSPageServiceImpl mplCMSPageService)
	{
		this.mplCMSPageService = mplCMSPageService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.cms.LuxCmsFacade#getLuxuryHomePage()
	 */
	@Override
	public LuxuryComponentsListWsDTO getLuxuryHomePage() throws CMSItemNotFoundException
	{

		final ContentPageModel contentPage = getMplCMSPageService().getPageByLabelOrId("homepage");
		final LuxuryComponentsListWsDTO luxuryHomePageDto = new LuxuryComponentsListWsDTO();
		if (contentPage != null)
		{

			final ArrayList<LuxuryComponentsListWsDTO> listOfComp = new ArrayList<LuxuryComponentsListWsDTO>();
			for (final ContentSlotForPageModel contentSlotForPage : contentPage.getContentSlots())
			{
				final ContentSlotModel contentSlot = contentSlotForPage.getContentSlot();
				final List<LuxuryComponentsListWsDTO> luxuryComponentsForASlot = getLuxuryComponentDtoForSlot(contentSlot);
				listOfComp.addAll(luxuryComponentsForASlot);
			}

		}
		return luxuryHomePageDto;
	}

	public List<LuxuryComponentsListWsDTO> getLuxuryComponentDtoForSlot(final ContentSlotModel contentSlot)
			throws CMSItemNotFoundException
	{

		final List<LuxuryComponentsListWsDTO> componentListForASlot = new ArrayList<LuxuryComponentsListWsDTO>();
		LuxuryComponentsListWsDTO luxuryComponent = new LuxuryComponentsListWsDTO();
		if (null != contentSlot)
		{
			//			final int count = 0;
			for (final AbstractCMSComponentModel abstractCMSComponentModel : contentSlot.getCmsComponents())
			{
				final String typecode = abstractCMSComponentModel.getTypeCode();
				if (typecode.equalsIgnoreCase("ShopOnLuxury"))
				{
					final ShopOnLuxuryModel luxuryShopOnLuxuryComponent = (ShopOnLuxuryModel) abstractCMSComponentModel;
					luxuryComponent = getShopOnLuxuryWsDTO(luxuryShopOnLuxuryComponent);
					componentListForASlot.add(luxuryComponent);
				}

			}

		}
		//}
		return componentListForASlot;
	}

	private LuxuryComponentsListWsDTO getShopOnLuxuryWsDTO(final ShopOnLuxuryModel luxuryShopOnLuxuryComponent)
	{
		final LuxuryComponentsListWsDTO luxuryComponent = new LuxuryComponentsListWsDTO();
		String title = null;
		final List<ShopOnLuxuryElementWsDTO> shopOnLuxuryElementList = new ArrayList<ShopOnLuxuryElementWsDTO>();

		if (StringUtils.isNotEmpty(luxuryShopOnLuxuryComponent.getShopOnLuxuryTitle()))
		{
			title = luxuryShopOnLuxuryComponent.getShopOnLuxuryTitle();
		}
		for (final ShopOnLuxuryElementModel element : luxuryShopOnLuxuryComponent.getShopOnLuxuryElements())
		{
			final ShopOnLuxuryElementWsDTO shopOnLuxuryelement = new ShopOnLuxuryElementWsDTO();
			String elementTitle = null;
			String elementImagePath = null;
			String elementDescription = null;

			if (StringUtils.isNotEmpty(element.getTitle()))
			{
				elementTitle = element.getTitle();
			}

			if (null != element.getImage() && StringUtils.isNotEmpty(element.getImage().getURL()))
			{
				elementImagePath = element.getImage().getURL();

			}
			if (StringUtils.isNotEmpty(element.getDescription()))
			{
				elementDescription = element.getDescription();
			}

			shopOnLuxuryelement.setTitle(elementTitle);
			shopOnLuxuryelement.setImagePath(elementImagePath);
			shopOnLuxuryelement.setDescription(elementDescription);

			shopOnLuxuryElementList.add(shopOnLuxuryelement);

		}
		luxuryComponent.setLuxuryShopOnLuxury((ShopOnLuxuryElementListWsDTO) shopOnLuxuryElementList);
		return luxuryComponent;
	}

}
