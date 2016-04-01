/**
 *
 */
package com.tisl.mpl.facade.store.populator;

import de.hybris.platform.commercefacades.storelocator.converters.populator.PointOfServicePopulator;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import org.apache.commons.lang.StringUtils;


/**
 * @author TECH
 *
 */
public class MplPointOfServicePopulator extends PointOfServicePopulator
{

	/**
	 * Populates mpl new fiels fro PointOfService.
	 *
	 * @param pointOfServiceModel
	 * @param pointOfServiceData
	 *
	 */
	@Override
	public void populate(final PointOfServiceModel source, final PointOfServiceData target)
	{
		// populate existing fields
		super.populate(source, target);
		//populate mpl new fields
		if (null != source.getSlaveId())
		{
			target.setSlaveId(source.getSlaveId());
		}
		if (null != source.getSellerId())
		{
			target.setSellerId(source.getSellerId());
		}
		if (null != source.getClicknCollect())
		{
			target.setClicknCollect(source.getClicknCollect());
		}
		if (null != source.getMplOpeningTime())
		{
			target.setMplOpeningTime(source.getMplOpeningTime());
		}
		if (null != source.getMplClosingTime())
		{
			target.setMplClosingTime(source.getMplClosingTime());
		}
		if (null != source.getMplWorkingDays())
		{
			target.setMplWorkingDays(source.getMplWorkingDays());
		}
		if (null != source.getRemark())
		{
			target.setRemark(source.getRemark());
		}
		if (null != source.getOrderAcceptanceTAT())
		{
			target.setOrderAcceptanceTAT(source.getOrderAcceptanceTAT());
		}
		if (null != source.getOrderProcessingTAT())
		{
			target.setOrderProcessingTAT(source.getOrderProcessingTAT());
		}
		if (null != source.getOrderCutoffTimeHD())
		{
			target.setOrderCutoffTimeHD(source.getOrderCutoffTimeHD());
		}
		if (null != source.getOrderCutoffTimeED())
		{
			target.setOrderCutoffTimeED(source.getOrderCutoffTimeED());
		}
		if (null != source.getIsReturnable())
		{
			target.setIsReturnable(source.getIsReturnable());
		}
		if (null != source.getParkingAvailable())
		{
			target.setParkingAvailable(source.getParkingAvailable());
		}
		if (null != source.getLocation())
		{
			target.setLocation(source.getLocation());
		}
		if (null != source.getStoreSize())
		{
			target.setStoreSize(source.getStoreSize());
		}
		if (null != source.getFootFall())
		{
			target.setFootFall(source.getFootFall());
		}
		if (null != source.getNormalRetailSalesOfStore())
		{
			target.setNormalRetailSalesOfStore(source.getNormalRetailSalesOfStore());
		}
		if (null != source.getOwnerShip())
		{
			target.setOwnerShip(source.getOwnerShip());
		}
		if (null != source.getManagerName())
		{
			target.setManagerName(source.getManagerName());
		}
		if (null != source.getManagerPhone())
		{
			target.setManagerPhone(source.getManagerPhone());
		}
		if (null != source.getMplStoreImage())
		{
			target.setMplStoreImage(source.getMplStoreImage());
		}
		if (null != source.getStoreContactNumber())
		{
			target.setStoreContactNumber(source.getStoreContactNumber());
		}
		if (null != source.getReturnstoreID())
		{
			target.setReturnstoreID(source.getReturnstoreID());
		}
		if (null != source.getReturnAddress1())
		{
			target.setReturnAddress1(source.getReturnAddress1());
		}
		if (null != source.getReturnAddress2())
		{
			target.setReturnAddress2(source.getReturnAddress2());
		}
		if (null != source.getReturnCountry())
		{
			target.setReturnCountry(source.getReturnCountry());
		}
		if (null != source.getReturnState())
		{
			target.setReturnState(source.getReturnState());
		}
		if (null != source.getReturnCity())
		{
			target.setReturnCity(source.getReturnCity());
		}
		if (null != source.getReturnPin())
		{
			target.setReturnPin(source.getReturnPin());
		}
		if (null != source.getActive())
		{
			target.setActive(source.getActive());
		}
		if (null != source.getEmail0())
		{
			target.setEmail0(source.getEmail0());
		}
		if (null != source.getEmail1())
		{
			target.setEmail1(source.getEmail1());
		}
		if (null != source.getEmail2())
		{
			target.setEmail2(source.getEmail2());
		}
		if (null != source.getEmail3())
		{
			target.setEmail3(source.getEmail3());
		}
		if (null != source.getEmail4())
		{
			target.setEmail4(source.getEmail4());
		}
		if (null != source.getEmail5())
		{
			target.setEmail5(source.getEmail5());
		}
		if (null != source.getEmail6())
		{
			target.setEmail6(source.getEmail6());
		}
		if (null != source.getEmail7())
		{
			target.setEmail7(source.getEmail7());
		}
		if (null != source.getEmail8())
		{
			target.setEmail8(source.getEmail8());
		}
		if (null != source.getEmail9())
		{
			target.setEmail9(source.getEmail9());
		}
		if (null != source.getPhoneNo0())
		{
			target.setPhoneNo0(source.getPhoneNo0());
		}
		if (null != source.getPhoneNo1())
		{
			target.setPhoneNo1(source.getPhoneNo1());
		}
		if (null != source.getPhoneNo2())
		{
			target.setPhoneNo2(source.getPhoneNo2());
		}
		if (null != source.getPhoneNo3())
		{
			target.setPhoneNo3(source.getPhoneNo3());
		}
		if (null != source.getPhoneN4())
		{
			target.setPhoneNo4(source.getPhoneN4());
		}
		if (null != source.getPhoneNo5())
		{
			target.setPhoneNo5(source.getPhoneNo5());
		}
		if (null != source.getPhoneNo6())
		{
			target.setPhoneNo6(source.getPhoneNo6());
		}
		if (null != source.getPhoneNo7())
		{
			target.setPhoneNo7(source.getPhoneNo7());
		}
		if (null != source.getPhoneNo8())
		{
			target.setPhoneNo8(source.getPhoneNo8());
		}
		if (null != source.getPhoneNo9())
		{
			target.setPhoneNo9(source.getPhoneNo9());
		}

		//Added to handle image url.
		if (StringUtils.isNotBlank(source.getMplStoreImage()))
		{
			final String[] imgArr = source.getMplStoreImage().split("#");
			for (final String imgUrl : imgArr)
			{
				if (imgUrl.contains("_Regular"))
				{
					target.setRegularImgUrl(imgUrl);
				}
				else if (imgUrl.contains("_onHover"))
				{
					target.setOnHoverImgUrl(imgUrl);
				}
				else if (imgUrl.contains("_onClick"))
				{
					target.setOnClickImgUrl(imgUrl);
				}
			}
		}
	}

}
