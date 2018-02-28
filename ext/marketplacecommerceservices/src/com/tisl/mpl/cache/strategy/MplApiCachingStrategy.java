package com.tisl.mpl.cache.strategy;



import com.tisl.mpl.wsdto.MplNewProductDetailMobileWsData;



public interface MplApiCachingStrategy
{
	public void put(String paramString, MplNewProductDetailMobileWsData paramList);

	public MplNewProductDetailMobileWsData get(String paramString);

	public void remove(String paramString);
}
