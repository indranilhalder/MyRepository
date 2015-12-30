/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.services.search.meta.processor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cscockpit.model.data.DataObject;
import de.hybris.platform.cscockpit.services.search.meta.PostSearchMetaProcessor;
import de.hybris.platform.util.PriceValue;

/**
 * @author 890223
 *
 */
public class MplProductPriceProcessor<PSR> implements PostSearchMetaProcessor<ProductModel, PSR> {

		@Autowired
		  private BuyBoxFacade buyBoxFacade;





		public BuyBoxFacade getBuyBoxFacade() {
			return buyBoxFacade;
		}








		@Required
		public void setBuyBoxFacade(BuyBoxFacade buyBoxFacade) {
			this.buyBoxFacade = buyBoxFacade;
		}









		public void populateMetaData(List<DataObject<ProductModel>> metaItems, PSR providerSearchResult)
		  {
		    for (DataObject<?> metaItem : metaItems)
		    {
		    	
		    	BuyBoxData buyBoxData=  getBuyBoxFacade().buyboxPrice(((ProductModel)metaItem.getItem()).getCode());
		     // PriceRowModel webPriceForProduct = getMplPriceRowService().getLeastPriceForProduct(((ProductModel)metaItem.getItem()));
		    	if(buyBoxData==null){
		    		continue;
		    	}
		      metaItem.getMetaData().register(new DefaultMplMetaProductPriceValue(new PriceValue(buyBoxData.getPrice().getCurrencyIso()	  ,buyBoxData.getPrice().getValue().doubleValue(),  false),buyBoxData.getSellerArticleSKU(),buyBoxData.getSellerName()), new Class[] { 
		    	  MplMetaProductPriceValue.class });
		    }

		  }
		
		
		
		
		
		
		
		
		
		/*private Double getCustomerPrice(buyBoxData   buyBox){
			
			int compareValue=Double.compare(buyBox.getPrice(), buyBox.getMrp());
			
		if(!(null==buyBox.getMrp() || null==buyBox.getPrice() || null==buyBox.getSpecialPrice())) {
			return buyBox.getSpecialPrice();
		} else {
			if(compareValue!=0) {
				return buyBox.getMrp();
			} else if(compareValue==0) {
				return buyBox.getPrice();
			}
		}
		return buyBox.getPrice();
		}
		*/
}
