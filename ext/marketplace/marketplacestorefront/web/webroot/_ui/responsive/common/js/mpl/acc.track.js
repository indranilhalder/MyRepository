ACC.track = {
	trackAddToCart: function (productCode, quantity, cartData)
	{
		window.mediator.publish('trackAddToCart',{
			productCode: productCode,
			quantity: quantity,
			cartData: cartData
		});
	},
	trackRemoveFromCart: function(productCode, initialCartQuantity)
	{
		window.mediator.publish('trackRemoveFromCart',{
			productCode: productCode,
			initialCartQuantity: initialCartQuantity
		});
	},

	trackUpdateCart: function(productCode, initialCartQuantity, newCartQuantity)
	{
		window.mediator.publish('trackUpdateCart',{
			productCode: productCode,
			initialCartQuantity: initialCartQuantity,
			newCartQuantity: newCartQuantity
		});
	},
	
	//For MSD
	trackAddToCartForMAD: function(productCodeMSD,salesHierarchyCategoryMSD,subPriceForMSD,currencyIso)
	{
		try
		{
			track(['addToCart', productCodeMSD, salesHierarchyCategoryMSD, subPriceForMSD, currencyIso]);	
		}
		catch(err)
		{
			console.log('Error Adding trackers when remove from cart: '+err.message);	
		}	
	},
	
	trackAddToWishListForMAD: function(productCodeMSD,salesHierarchyCategoryMSD,subPriceForMSD,currencyIso)
	{
		try
		{
			track(['addToWishlist', productCodeMSD, salesHierarchyCategoryMSD, subPriceForMSD, currencyIso]);	
		}
		catch(err)
		{
			console.log('Error Adding trackers when remove from cart: '+err.message);	
		}	
	},
	
	trackCarouselAddToCartForMAD: function(current_pid,categoryID,recs_product_id,categoryCode)
	{
		try
		{
			track(['carouselAddToCart',current_pid,categoryID,recs_product_id,categoryCode]);	
		}
		catch(err)
		{
			console.log('Error Adding trackers when remove from cart: '+err.message);	
		}	
	}
	//End MSD
};