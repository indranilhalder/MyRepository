package com.tisl.mpl.cockpits.cscockpit.services.search.meta.processor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cscockpit.model.data.DataObject;
import de.hybris.platform.cscockpit.services.search.meta.data.MetaProductInfo;
import de.hybris.platform.cscockpit.services.search.meta.data.impl.DefaultMetaProductInfo;
import de.hybris.platform.cscockpit.services.search.meta.processor.ProductStockLevelProcessor;
import de.hybris.platform.cscockpit.utils.LabelUtils;


public class MPLProductStockLevelProcessor  extends  ProductStockLevelProcessor {
	@Autowired
	  private BuyBoxFacade buyBoxFacade;





	public BuyBoxFacade getBuyBoxFacade() {
		return buyBoxFacade;
	}








	@Required
	public void setBuyBoxFacade(BuyBoxFacade buyBoxFacade) {
		this.buyBoxFacade = buyBoxFacade;
	}
	@Override
	public void  populateMetaData(List<DataObject<ProductModel>> metaItems, FacetSearchPageData<?, SearchResultValueData> providerSearchResult) {
		
	     int index = 0;
	     BuyBoxData data =null;
	     List<SearchResultValueData> results = providerSearchResult.getResults();
	     for (DataObject<ProductModel> metaItem : metaItems)
	     {
	       String stockLevel = (String)getValue(results.get(index++), "stockLevelStatus");
	 
	       if ((stockLevel == null) && (getCommerceStockService() != null) && (getBaseStoreService() != null))
	       {
	        /* BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
	         if ((baseStore != null) && (getCommerceStockService().isStockSystemEnabled(baseStore)))
	         {
	           stockLevel = getCommerceStockService().getStockLevelStatusForProductAndBaseStore((ProductModel)metaItem.getItem(), 
	             getBaseStoreService().getCurrentBaseStore()).getCode();
	         }*/
	    	   data=  getBuyBoxFacade().buyboxPrice(((ProductModel)metaItem.getItem()).getCode());
	    	   if(data==null ){
		    		continue;
		    	}
	    	   
	    	     if(data.getAvailable() ==null || data.getAvailable()==0){
	    	    	 stockLevel = StockLevelStatus.OUTOFSTOCK.getCode();//overriding again from buybox
	    	     }
	       }
	 
	       String message = "";
	       boolean stockFlag = true;
	 
	       if (StockLevelStatus.OUTOFSTOCK.getCode().equals(stockLevel))
	       {
	         stockFlag = false;
	         message = LabelUtils.getLabel("cscockpit.widget.basket.result", stockLevel, new Object[0]);
	       }
	 
	       MetaProductInfo meta = new DefaultMetaProductInfo(stockFlag, message);
	       metaItem.getMetaData().register(meta, new Class[] { MetaProductInfo.class });
	     }
	}

}
