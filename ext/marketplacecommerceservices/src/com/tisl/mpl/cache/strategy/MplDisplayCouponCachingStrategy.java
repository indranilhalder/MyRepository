package com.tisl.mpl.cache.strategy;

import java.util.List;

import com.tisl.mpl.wsdto.MplVisibleCouponsDTO;

public interface MplDisplayCouponCachingStrategy
{
	/**
	 * The Method Stores Coupon Data in Cache
	 * 
	 * @param key
	 * @param couponList
	 */
	void put(String key,List<MplVisibleCouponsDTO> couponList);
	
	/**
	 * The Method returns Coupon List from Cache
	 * 
	 * @param key
	 * @return List<MplVisibleCouponsDTO>
	 */
	List<MplVisibleCouponsDTO> get(String key);
	
	/**
	 * The Methods Deletes Coupon Data from Cache
	 * 
	 * @param key
	 */
	void remove(String key);
}
