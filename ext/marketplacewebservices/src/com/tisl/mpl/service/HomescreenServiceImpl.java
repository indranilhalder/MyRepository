/**
 *
 */
package com.tisl.mpl.service;

import com.tisl.mpl.wsdto.HomescreenListData;


/**
 * @author TCS
 *
 *
 *
 */
public class HomescreenServiceImpl implements HomescreenService
{
	//	@Resource(name = "cmsComponentService")
	//	private CMSComponentService cmsComponentService;
	//	@Resource(name = "configurationService")
	//	private ConfigurationService configurationService;

	//private static final Logger LOG = Logger.getLogger(HomescreenServiceImpl.class);


	/*
	 * 
	 * 
	 * @see com.tisl.mpl.service.HomescreenService#getHomeScreenContent()
	 */
	@Override
	public HomescreenListData getHomeScreenContent()
	{


		final HomescreenListData homescreendata = new HomescreenListData();
		/*
		 * final List<ComponentHierarchyData> componentlist = new ArrayList<ComponentHierarchyData>(); Date modifiedTime =
		 * null; try { final MplSequentialBannerComponentModel homeComponent = getHomeScreenComponent(); if (null !=
		 * homeComponent) { modifiedTime = getModifiedTime(); } Collection<MplBigPromoBannerComponentModel>
		 * componentCollection = null; if (null != homeComponent.getBannersList()) { //componentCollection =
		 * homeComponent.getBannersList(); }
		 * 
		 * for (final MplBigPromoBannerComponentModel compModel : componentCollection) { if (null !=
		 * compModel.getModifiedtime() && compModel.getModifiedtime().compareTo(modifiedTime) > 0) { modifiedTime =
		 * compModel.getModifiedtime(); } final ComponentHierarchyData componentData = new ComponentHierarchyData(); if
		 * (null != compModel.getSequenceNumber()) { componentData.setSequence(compModel.getSequenceNumber().toString());
		 * } if (null != compModel.getBannerImage().getURL()) { componentData.setUrl(compModel.getBannerImage().getURL());
		 * 
		 * } if (null != compModel.getMajorPromoText()) {
		 * 
		 * componentData.setMajorPromoText(compModel.getMajorPromoText()); } if (null != compModel.getMinorPromo1Text()) {
		 * 
		 * componentData.setMinorPromo1Text(compModel.getMinorPromo1Text()); } if (null != compModel.getMinorPromo2Text())
		 * {
		 * 
		 * componentData.setMinorPromo2Text(compModel.getMinorPromo2Text()); }
		 * 
		 * componentlist.add(componentData); } if (null != modifiedTime) {
		 * homescreendata.setModifiedTime(modifiedTime.toString()); } homescreendata.setComponents(componentlist);
		 */
		/*
		 * } catch (final CMSItemNotFoundException e) { // YTODO Auto-generated catch block
		 * LOG.debug(MarketplacecommerceservicesConstants.EXCEPTION_IS + e); } catch (final Exception e) { // YTODO
		 * Auto-generated catch block LOG.debug(MarketplacecommerceservicesConstants.EXCEPTION_IS + e); }
		 */

		return homescreendata;
	}

	/**
	 * fetching
	 *
	 * @return MplSequentialBannerComponentModel
	 * @throws CMSItemNotFoundException
	 */

	//	private MplSequentialBannerComponentModel getHomeScreenComponent() throws CMSItemNotFoundException
	//	{
	//		String componentUid = null;
	//
	//		if (null != configurationService && null != configurationService.getConfiguration()
	//				&& null != MarketplacecommerceservicesConstants.HOMEPAGECOMPONENT)
	//		{
	//			componentUid = configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.HOMEPAGECOMPONENT,
	//					"");
	//		}
	//
	//		final MplSequentialBannerComponentModel homeComponent = (MplSequentialBannerComponentModel) cmsComponentService
	//				.getSimpleCMSComponent(componentUid);
	//		return homeComponent;
	//	}

	/**
	 * modified time for version response
	 *
	 * @throws CMSItemNotFoundException
	 * @return String
	 */
	/*
	 * private Date getModifiedTime() throws CMSItemNotFoundException { final MplSequentialBannerComponentModel
	 * homeComponent = getHomeScreenComponent(); Date modifiedTime = null; if (null != homeComponent && null !=
	 * homeComponent.getModifiedtime()) { modifiedTime = homeComponent.getModifiedtime(); } return modifiedTime; }
	 */
}
