/**
 *
 */
package com.tisl.mpl.facade.cms;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;

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

		LuxuryComponentsListWsDTO luxuryComponentsForASlot = new LuxuryComponentsListWsDTO();
		final ContentPageModel contentPage = getMplCMSPageService().getPageByLabelOrId("homepage");
		if (contentPage != null)
		{

			for (final ContentSlotForTemplateModel contentSlotForPage : contentPage.getMasterTemplate().getContentSlots())
			{
				final ContentSlotModel contentSlot = contentSlotForPage.getContentSlot();
				luxuryComponentsForASlot = getLuxuryComponentDtoForSlot(contentSlot);

			}


		}
		return luxuryComponentsForASlot;
	}

	public LuxuryComponentsListWsDTO getLuxuryComponentDtoForSlot(final ContentSlotModel contentSlot)
			throws CMSItemNotFoundException
	{

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
				}

			}

		}
		//}
		return luxuryComponent;
	}

	private LuxuryComponentsListWsDTO getShopOnLuxuryWsDTO(final ShopOnLuxuryModel luxuryShopOnLuxuryComponent)
	{
		final LuxuryComponentsListWsDTO luxuryComponent = new LuxuryComponentsListWsDTO();
		final List<ShopOnLuxuryElementWsDTO> shopOnLuxuryElementList = new ArrayList<ShopOnLuxuryElementWsDTO>();
		final ShopOnLuxuryElementListWsDTO shopOnLuxuryElementListObj = new ShopOnLuxuryElementListWsDTO();

		if (StringUtils.isNotEmpty(luxuryShopOnLuxuryComponent.getShopOnLuxuryTitle()))
		{
			luxuryShopOnLuxuryComponent.setShopOnLuxuryTitle(luxuryShopOnLuxuryComponent.getShopOnLuxuryTitle());
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
		shopOnLuxuryElementListObj.setShopOnLuxuryElements(shopOnLuxuryElementList);
		luxuryComponent.setLuxuryShopOnLuxury(shopOnLuxuryElementListObj);
		return luxuryComponent;
	}

}
