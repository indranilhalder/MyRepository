package com.tisl.mpl.interceptor;

/**
 *
 */

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;


public class DeliveryModeCreateInterceptor implements PrepareInterceptor
{

	//private static final Logger LOG = Logger.getLogger(DeliveryModeCreateInterceptor.class);

	//	@Resource(name = "deliveryModeService")
	//	private DeliveryModeService deliveryModeService;

	/**
	 * @return the deliveryModeService
	 */
	//	public DeliveryModeService getDeliveryModeService()
	//	{
	//		return deliveryModeService;
	//	}

	//	@Resource(name = "modelService")
	//	private ModelService modelService;

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	//	@Required
	//	public void setModelService(final ModelService modelService)
	//	{
	//		this.modelService = modelService;
	//	}

	/**
	 * @param deliveryModeService
	 *           the deliveryModeService to set
	 */
	//	@Required
	//	public void setDeliveryModeService(final DeliveryModeService deliveryModeService)
	//	{
	//		this.deliveryModeService = deliveryModeService;
	//	}

	/**
	 * @Description : The Method checks the Promotion Priority prior to its creation from HMC
	 * @param: object
	 * @param: arg1
	 * @return: void
	 */
	@Override
	@Deprecated
	public void onPrepare(final Object object, final InterceptorContext arg1) throws InterceptorException
	{/*
	  * LOG.debug(Localization.getLocalizedString("promotion.deliverymodecreateinterceptor.message")); if (object
	  * instanceof RichAttributeModel) { final Set<DeliveryModeModel> deliveryModes = new HashSet<DeliveryModeModel>();
	  * final RichAttributeModel richAttribute = (RichAttributeModel) object; String homeDelivery = null; if
	  * (richAttribute.getHomeDelivery() != null) { if (richAttribute.getHomeDeliveryCharge() != null &&
	  * Double.parseDouble(richAttribute.getHomeDeliveryCharge()) <= 0) {
	  * richAttribute.setHomeDelivery(HomeDeliveryEnum.NO); }
	  *
	  * else if (richAttribute.getHomeDeliveryCharge() != null &&
	  * Double.parseDouble(richAttribute.getHomeDeliveryCharge()) > 0) {
	  * richAttribute.setHomeDelivery(HomeDeliveryEnum.YES); }
	  *
	  * homeDelivery = richAttribute.getHomeDelivery().toString(); if
	  * (homeDelivery.equalsIgnoreCase(MarketplacecommerceservicesConstants.YES)) { homeDelivery =
	  * MarketplacecommerceservicesConstants.HOME_DELIVERY;
	  *
	  * } else { homeDelivery = null; } } String expressDelivery = null; if (richAttribute.getExpressDelivery() != null)
	  * {
	  *
	  * if (richAttribute.getExpressDeliveryCharge() != null &&
	  * Double.parseDouble(richAttribute.getExpressDeliveryCharge()) <= 0) {
	  * richAttribute.setExpressDelivery(ExpressDeliveryEnum.NO); } else if (richAttribute.getExpressDeliveryCharge() !=
	  * null && Double.parseDouble(richAttribute.getExpressDeliveryCharge()) > 0) {
	  * richAttribute.setExpressDelivery(ExpressDeliveryEnum.YES); } expressDelivery =
	  * richAttribute.getExpressDelivery().toString(); if
	  * (expressDelivery.equalsIgnoreCase(MarketplacecommerceservicesConstants.YES)) { expressDelivery =
	  * MarketplacecommerceservicesConstants.EXPRESS_DELIVERY;
	  *
	  * } else { expressDelivery = null; } } String clickDelivery = null; if (richAttribute.getClickAndCollect() != null)
	  * { clickDelivery = richAttribute.getClickAndCollect().toString(); if
	  * (clickDelivery.equalsIgnoreCase(MarketplacecommerceservicesConstants.YES)) { clickDelivery =
	  * MarketplacecommerceservicesConstants.CLICK_COLLECT; } else { clickDelivery = null; } }
	  *
	  *
	  * if (null != homeDelivery) { deliveryModes.add(deliveryModeService.getDeliveryModeForCode(homeDelivery)); } if
	  * (null != expressDelivery) { deliveryModes.add(deliveryModeService.getDeliveryModeForCode(expressDelivery)); } if
	  * (null != clickDelivery) { deliveryModes.add(deliveryModeService.getDeliveryModeForCode(clickDelivery)); }
	  * PcmProductVariantModel productVarient; if (null != richAttribute.getProductSource()) { productVarient =
	  * (PcmProductVariantModel) richAttribute.getProductSource();
	  *
	  * } else { productVarient = (PcmProductVariantModel) richAttribute.getSellerInfo().getProductSource(); } if
	  * (productVarient != null) { productVarient.setDeliveryModes(deliveryModes); modelService.save(productVarient); } }
	  */

	}

}
