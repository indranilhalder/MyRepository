ACC.cartitem = {

	_autoload : [ "bindCartItem" ],

	bindCartItem : function() {

		$(".remove-entry-button").on("click",function() {
			 localStorage.setItem("removeFromCart_msgFromCart", "Y");								
					// var entryNumberMSD = $(this).attr('id').split("_");
					
					 /* $('#updateCartForm' + entryNumber[1]);
					 * 
					 * var productCode =
					 * form.find('input[name=productCode]').val(); var
					 * initialCartQuantity =
					 * form.find('input[name=initialQuantity]'); var
					 * cartQuantity = form.find('input[name=quantity]');
					 * 
					 * ACC.track.trackRemoveFromCart(productCode,
					 * initialCartQuantity.val());
					 * 
					 * cartQuantity.val(0); initialCartQuantity.val(0);
					 * form.submit();
					 */
					
					// for MSD	
					var x = $(this).closest("li.item").prev();
			        console.log(x);
			        
			        var productCodeMSD;
			        var salesHierarchyCategoryMSD;
			        var basePriceForMSD;
			        var rootCategoryMSD;
			        var subPriceForMSD;
			        
			        var y = x.find("input").each(function(index ){
			        	
			        	//var z1 = $(this).attr("name");
			        	if(index == '0')
			        		{
			        		salesHierarchyCategoryMSD = $(this).attr("value");
			        		}
			        	else if(index == '1')
			        		{
			        		rootCategoryMSD = $(this).attr("value");
			        		}
			        	
			        	else if(index == '2')
		        		{
			        		productCodeMSD = $(this).attr("value");
		        		}
			        	
			        	else if(index == '3')
		        		{
			        		basePriceForMSD = $(this).attr("value");
		        		}
			        	
			        	else if(index == '4')
		        		{
			        		subPriceForMSD = $(this).attr("value");
		        		}
			        	
			        	//var z = $(this).attr("value");
			        	//console.log( index + ": " + $( this ).text() );			        	
			        	//console.log(z1);
			        });
			        console.log(salesHierarchyCategoryMSD);
			        console.log(rootCategoryMSD);
			        console.log(productCodeMSD);
			        console.log(basePriceForMSD);
			        console.log(subPriceForMSD);		       
			        
			        var isMSDEnabled =  $("input[name=isMSDEnabled]").val();
					console.log(isMSDEnabled);
					var isApparelExist  = $("input[name=isApparelExist]").val();
					console.log(isApparelExist);
					
					if(typeof isMSDEnabled === 'undefined')
					{
						isMSDEnabled = false;						
					}
					
					if(typeof isApparelExist === 'undefined')
					{
						isApparelExist = false;						
					}
								
					//End MSD
					
					var entryNumber1 = $(this).attr('id').split("_");
					var entryNumber = entryNumber1[1];
					var form = $('#updateCartForm' + entryNumber[1]);
					var entryUssid = entryNumber1[2];
					$.ajax({
                       
						url : ACC.config.encodedContextPath
								+ "/cart/removeFromMinicart?entryNumber="
								+ entryNumber,
						type : 'GET',
						cache : false,
						success : function(response) {
							ACC.product.addToBagFromWl(entryUssid,false);
							//FOR MSD
							if(Boolean(isMSDEnabled) && Boolean(isApparelExist) && (rootCategoryMSD == 'Clothing'))
								{								
								trackMAD(productCodeMSD,salesHierarchyCategoryMSD,subPriceForMSD,"INR");
								}						
							window.location.reload();
						},
						error : function(resp) {
							console.log(resp);

						}
					});
				});

		/*
		 * $('.update-entry-quantity-input').on("change", function () { var
		 * entryNumber = $(this).attr('id').split("_") var form =
		 * $('#updateCartForm' + entryNumber[1]); var productCode =
		 * form.find('input[name=productCode]').val(); var initialCartQuantity =
		 * form.find('input[name=initialQuantity]').val(); var newCartQuantity =
		 * form.find('input[name=quantity]').val();
		 * 
		 * if(initialCartQuantity != newCartQuantity) {
		 * ACC.track.trackUpdateCart(productCode, initialCartQuantity,
		 * newCartQuantity); form.submit(); }
		 * 
		 * });
		 */
	}
};



//For MSD
var trackMAD = function(ProductId,CategoryId,Price,Currency) {
	var currentPageMSD = $("input[name=currentPageMSD]").val();
	console.log(currentPageMSD);
	if(currentPageMSD == 'CART')
	{
		try
		{
		track(['removeFromCart',ProductId,CategoryId,Price,Currency]);		
		}
		catch(err)
		{
			console.log('Error Adding trackers when remove from cart: '+err.message);	
		}
	}
}
