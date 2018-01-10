var digitalData;
	
$(document).ready(function(){
	console.log("lux dtm call");
	_satellite.pageBottom();
	alert("hello");
	var pageType= $('#pageType').val();
	var tealiumOrderFlag = $('#tealiumOrderFlag').val();
	var subdomain = window.location.href.split("/")[2].split(".")[0];
	var subDomain= "";
	if(subdomain != "undefined"){
		subDomain = subdomain;
	}
	var Promo_Id ="";
	if($("#product_applied_promotion_title").val() && $("#product_applied_promotion_code").val() !=undefined)
	{
		 var promo_title=$("#product_applied_promotion_title").val().toLowerCase().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
		 var promo_id=$("#product_applied_promotion_code").val().toLowerCase().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
		 Promo_Id = promo_title+":" + promo_id;
	}
	var pageName1 = $('#pageName').val().toLowerCase();
	//var pageName2 = $('#page_name').val().toLowerCase();
	var pageName='';
		if(pageType == "product" || pageType == "/sellersdetailpage" || pageType == "category" || pageType == "electronics" || pageType == "productsearch"){
			//pageName =pageName2;
			pageName =pageName1
		}
		else if(pageType =="multistepcheckoutsummary"){
			var checkoutPageName = pageName1 +":" + $('#checkoutPageName').val().toLowerCase();
			pageName=checkoutPageName;
		}
		else{
			pageName =pageName1;
		}
		var primaryCategoryN='';
		if(pageType =="multistepcheckoutsummary"){
			primaryCategoryN = "checkout";
		}
		else{
			primaryCategoryN =pageType;
		}

	
                     	//onPageLoad DigitalData definition
						digitalData = {
								page : {
									pageInfo : {
										pageName  : pageName,
										domain    : document.domain,
										subDomain : subDomain
									},
									category : {
										primaryCategory : primaryCategoryN
									}
								}
							}
	
		if (pageType == "homepage") {
			if(typeof _satellite !="undefined"){
			       _satellite.track('cpj_home_page');
			}
		
		}
		if(pageType =='productsearch'){
			
			if(typeof _satellite !="undefined"){
				_satellite.track('cpj_search_pages');
			}
			var searchTerm = $('#search_keyword').val().toLowerCase();
			var searchCategory = $('#searchCategory').val();	
			var searchResults = $('.container .plp-wrapper h4 span:first').text().replace(/"/g, "");
            //null search
		    var isVisible = $('.searchEmpty:visible').is(':visible');
			    if(isVisible && typeof _satellite !="undefined" ){
				         _satellite.track('null_search');
				         
				        digitalData.internal = {
						 search : {
							 term : searchTerm,
							 category : searchCategory
						  }
				  }
			 }
			   //for normal search 
			digitalData.internal = {
					search : {
						term : searchTerm,
						category : searchCategory,
						results  : searchResults
					}
			}	
			dtmLuxSearchTags();
			dtmLuxProductImpressionsSerp();		
			

		}
			
		if (pageType == "category" ){
			try{
				if(typeof _satellite !="undefined"){	
					_satellite.track('cpj_category_pages');
				}
				
				var product_category= $('#product_category').val();
				var page_subcategory_name_L2 = $('#page_subcategory_name').val();
				var page_subcategory_name_L3 = $('#page_subcategory_name_l3').val();
				/*  product impressions*/
				dtmProductImpressionsPlp();
				//digitalData.page.category.subCategory1 = ListValue("product_category");
				digitalData.page.category.subCategory1 = product_category;
				digitalData.page.category.subCategory2 = page_subcategory_name_L2; 
				digitalData.page.category.subCategory3 = page_subcategory_name_L3;
			}
			catch(e){
				console.log("ERROR on plp inside dtm call:"+e.message);
			}
		}
		
		// For PDP
		if (pageType == "product"
			|| pageType == "/sellersdetailpage") {
			try{
				var product_id = $("#product_id").val().toLowerCase();
				var product_category = $("#product_category").val().toLowerCase();
				var product_brand = $("#product_brand").val().toLowerCase();
				var product_discount = $("#product_discount").val();
				var pincode = $('#pin').val();
				var prevPageUrl = document.referrer;
				var findingMethod ="";
				if(typeof _satellite !="undefined"){
					_satellite.track('cpj_pdp');
				}

				/*onload data*/
				digitalData.cpj = {
						product : {
							id : product_id,
							category : product_category,
							discount : product_discount
						},
						brand : {
							name : product_brand
						}
				}

				if(prevPageUrl != "" && prevPageUrl != 'undefined'){
					if(prevPageUrl.indexOf('/c-lsh') > -1){
						findingMethod = 'product grid';
					}
					else if(prevPageUrl.indexOf("/s/") > -1 || prevPageUrl.indexOf('/m/') > -1){
						findingMethod = 'seller';
					}
					else if(prevPageUrl.indexOf('/b-mbh') > -1){
						findingMethod = 'brand';
					}
					else if(prevPageUrl == document.location.href){
						findingMethod = 'homePage';
					}
					else if(prevPageUrl.indexOf('/search/') > -1){
						findingMethod = 'search_results_page';
					}
					else if(prevPageUrl.indexOf('/cart') > -1){
						findingMethod = 'cart';
					}
					else if(prevPageUrl.indexOf('/checkout/orderConfirmation') > -1){
						findingMethod = 'orderConfirmation';
					}
					else if(prevPageUrl.indexOf('/my-account/viewParticularWishlist') > -1){
						findingMethod = 'wishlist';
					}
					else if(prevPageUrl.indexOf('/my-account/reviews') > -1 || prevPageUrl.indexOf('/my-account/orders') > -1){
						findingMethod = 'my-account';
					}
					else{
						findingMethod = $('#pageName').val().toLowerCase();
					}
				} 

				//for external campaigns,banners,recommendations
				if((currentPageURL.indexOf('icid2') > -1) && (currentPageURL.indexOf('icid') > -1 )){
					findingMethod = 'banner';
				}

				if(currentPageURL.indexOf('cid') > -1){
					findingMethod = 'external-campaign';
				}

				if(currentPageURL.indexOf('iaclick=true') > -1){
					findingMethod = 'recommendations';
				}


				if(findingMethod != ''){

					if(typeof digitalData.cpj.pdp != "undefined"){
						digitalData.cpj.pdp.findingMethod = findingMethod;
					}		
					else{
						digitalData.cpj.pdp = {
								findingMethod : findingMethod
						}
					}	
				}  

				//TPR-6333 | Track Geo-location of users
				if(pincode != ''){
				console.log("pdp:geolocation:");
					digitalData.geolocation = {
							pin : {
								code : pincode
							}
					}
				}
				if(Promo_Id != ""){
					if(typeof(digitalData.cpj.promo) != "undefined"){
						digitalData.cpj.promo.id = Promo_Id;
					}		
					else{
						digitalData.cpj.promo = {
								id : Promo_Id
						}
					}	
				}

					if(typeof(digitalData.cpj.promo) != "undefined"){
						digitalData.cpj.promo.id = Promo_Id;
					}		
					else{
						digitalData.cpj.promo = {
								id : Promo_Id
						}
					}	
				
				if($("#out_of_stock").val() == "true"){
					if(typeof _satellite !="undefined"){
						_satellite.track('out_of_stock');
					}
					// dtmErrorTracking("out_of_stock","errorname");

					if(typeof(digitalData.cpj.product) != "undefined"){
						digitalData.cpj.product.id = product_id;
						digitalData.cpj.product.category = product_category;
					}		
					else{
						digitalData.cpj.product = {
								id : product_id,
								category : product_category
						}
					}
				}

				digitalData.page.category.subCategory1 = product_category;
				digitalData.page.category.subCategory2 = page_subcategory_name_L2; 
				digitalData.page.category.subCategory3 = page_subcategory_name_L3;
			}
			catch(e){
				console.log("ERROR on pdp inside dtm call:"+e.message);	
			}

		}	
		// Cart page
		if(pageType == "cart"){
			try{
			//	alert("dtm cart lux js call");
				var pinCode = $('#defaultPinCodeIds').val();
				var product_id ='';
				var product_category ='';
				if(typeof _satellite != "undefined"){
				    _satellite.track('cpj_cart_page');
				}
				
				if($("#product_id").val()!= ''){
					 product_id = JSON.parse($("#product_id").val().toLowerCase());
				}
				if($("#product_category").val()!= ''){
					product_category =  $("#product_category").val().toLowerCase().split(",");
				}
				//product_category is coming empty for lux
				digitalData.cpj = {
						product : {
							id : product_id,
							category : product_category
						}
					}
				
				//TPR-6333 | Track Geo-location of users
				  if(pinCode != ''){
					   digitalData.geolocation = {
							pin : {
							code : pinCode
							}
					}
				}
				
			/*	//TPR-6371 | track promotions
				if($('#promolist').val() != '[]') {
					   digitalData.cpj.promo = {
							id : $('#promolist').val().toLowerCase()
					   }
				}*/
				digitalData.page.category.subCategory1 = product_category ;
				digitalData.page.category.subCategory2 =  page_subcategory_name_L2; 
				digitalData.page.category.subCategory3 = page_subcategory_name_L3;
				
				var sellerList = $('#checkoutSellerIDs').val().replace('_','|');
			  
				digitalData.product = {
						seller : {
							id   : sellerList
						}   
					}
			}
			catch(e)
			{
				console.log("ERROR in cart inside dtm call:"+e.message);
			}
		}
		//check in local first
		if(pageType =="orderconfirmation" && $('#orderIDString').val()!= ''){
			try{
				if(typeof _satellite !="undefined"){
					_satellite.track('cpj_order_successful');
				}	
				
				var product ='';
				var productCategory ='';
				var price ='';
				var quantity ='';
					
				if($("#product_sku").val()!= ''){
					product = JSON.parse($("#product_id").val().toLowerCase());
				}
				if($("#product_category").val()!= ''){
					productCategory =  $("#product_category").val().toLowerCase().split(",");
				}
				if($("#product_unit_price").val()!= ''){
					price = JSON.parse($("#product_unit_price").val());
				}
				if($("#product_quantity").val()!= ''){
					quantity = JSON.parse($("#product_quantity").val());
				}
				
				digitalData.cpj = {
						        product : {
							       id       :  product ,
					               category :  productCategory,
					               price    :  price
					          },
					             payment : {
					        	    quantity : quantity
					           } 
				  }
		
				digitalData.page.category.subCategory1 = product_category ;
				digitalData.page.category.subCategory2 =  page_subcategory_name_L2; 
				digitalData.page.category.subCategory3 = page_subcategory_name_L3;
			}
			catch(e){
				console.log("ERROR on orderPage in dtm call:"+e.message);
			}
		}
		
		if(pageType =='notfound' || pageType =='nonbusinesserrorfound' || pageType =='businesserrorfound' || digitalData == 'undefined' ){
			if(typeof(_satellite) !="undefined"){
				_satellite.track('404_error');
			}	
		}

		//brand page
		if(currentPageURL.indexOf("/b-mbh") > -1 &&
				pageType != 'notfound' && 
				pageType != 'nonbusinesserrorfound' &&
				pageType != 'businesserrorfound' )
		{ 
			if(typeof(_satellite) !="undefined"){
				_satellite.track('cpj_brand_pages');
			}

			var brandName = $('.filter-name').text();

			digitalData.cpj = {
					brand : {
						name : brandName
					}
			}
		}

		if($('#checkoutPageName').val()=='Choose Your Delivery Options'){
			if(typeof _satellite != "undefined"){
				_satellite.track('cpj_checkout_delivery_option');
			}
			var product = $('#product_id').val();
			   if(typeof(digitalData.cpj.product) != "undefined"){
				     digitalData.cpj.product.id = product;
				     digitalData.cpj.product.category = category;
	            }		
               else {
	                digitalData.cpj.product = {
	                		      id  : productId,
	        				 category : category
		              }
                   }

	    	}
		
		if($('#checkoutPageName').val()=='Select Address'){
			if(typeof _satellite != "undefined"){
				 _satellite.track('cpj_checkout_proceed_to_address');
			}
			var product = $('#product_id').val();
			   if(typeof(digitalData.cpj.product) != "undefined"){
				     digitalData.cpj.product.id = product;
				     digitalData.cpj.product.category = category;
	            }		
               else {
	                digitalData.cpj.product = {
	                		      id  : productId,
	        				 category : category
		              }
                   }
		     }
		
		if($('#checkoutPageName').val()=='Payment Options'){
			if(typeof _satellite != "undefined"){
				_satellite.track('cpj_checkout_proceed_to_payment');
			}
			var product = $('#product_id').val();
			   if(typeof(digitalData.cpj.product) != "undefined"){
				     digitalData.cpj.product.id = product;
				     digitalData.cpj.product.category = category;
	            }		
               else {
	                digitalData.cpj.product = {
	                		      id  : productId,
	        				 category : category
		              }
                   }
		     }
		//campaigns track
		var icid2Param = getIcid2FromUrl();
		if(typeof icid2Param != "undefined" && icid2Param != null){
			_satellite.track('internal_campaign');
			digitalData.internal = {
				campaign : {
					id : icid2Param
				}
			}
		}
				// For Display Hierarchy
				var breadcrum=[];
				$('.breadcrumbs.wrapper').find('li:not(.active)').each(function(){
					breadcrum.push($(this).find('a').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,""));
				})

				var leaf = $('.breadcrumbs.wrapper').find('li.active').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
				if(leaf != ""){
					breadcrum.push(leaf);
				}
				if(breadcrum.length > 0){
				
					if(typeof(digitalData.page.display) != "undefined"){
						digitalData.page.display.hierarchy = breadcrum;
					}		
					else {
						digitalData.page.display = {
								hierarchy :  breadcrum
						}
					}
				}
				
				if(tealiumOrderFlag !='undefined' && tealiumOrderFlag == 'true'){
			    	dtmErrorTracking("Order not placed: Unsuccesful error","errorName");
			    }			
});


//function defination
function dtmLuxProductImpressionsSerp(){
	try{
		 
		var productArray= [];
		var count =0;
	   $('.product-grid-wrapper .product-grid').each(function(){
		   var count;
			var productID='';
		  
			  	if( count == 2){
			   		return false;
			   	}
			   	var url = $(this).find('.product-item > a').attr('href');
			  
				if(typeof(url) != "undefined"){
					url = url.split('/p-');
					var productIDlist = url[1].split('/');
					productID=productIDlist[0].toLowerCase();
				}
				productArray.push(productID);
				//alert("serp"+productArray);
			   	count++;
	   }) 
	     var impressions = productArray.join("|");
	   
	     	  digitalData.page.products = {
	     	    	impression : impressions
	          }
		
	     	  digitalData.cpj.product = {
	     		         id : productArray
	              }
	}
	catch(e){
        console.log("error fn:dtmLuxProductImpressionsSerp"+e.message);
      }
 }
function populateHeader(linkName){
	
	if(typeof _satellite !="undefined"){
	    _satellite.track('header_link_clicks');
	}
	
	digitalData.header ={
			link :{
				name : linkName
			}
	}
}

function populateFooter(footer){
	if(typeof _satellite !="undefined"){
		   _satellite.track('footer_link_clicks');
		}
		digitalData.footer = {
				link : {
				   name : footer
				}	
			}
}
function dtmCouponCheck(msg,couponCode){
	try{
		if(msg == 'success'){
			 if(typeof (_satellite)!= 'undefined'){
				_satellite.track('cpj_checkout_payment_coupon_success');
		    }
		 }
		else{
			  if(typeof (_satellite)!= 'undefined'){
				_satellite.track('cpj_checkout_payment_coupon_fail');
			 }
		}	
		
		if(typeof(digitalData.cpj)!= 'undefined'){
			  if(typeof (digitalData.cpj.coupon)!= 'undefined'){
				  digitalData.cpj.coupon.code = couponCode;
			  }
			else{
				  digitalData.cpj.coupon = {
						code : couponCode
					}
			  } 
		 }
	}
	catch(e){
          console.log("error fn:dtmCouponCheck"+e.message);
      }
}
function dtmPaymentModeSelection(mode){
	try{
		if(typeof (_satellite)!= 'undefined'){
	     	_satellite.track('cpj_checkout_payment_selection');
	      }
	
	if(typeof(digitalData.cpj)!= 'undefined' ){
		    if(typeof(digitalData.cpj.payment) != 'undefined' ){
			      digitalData.cpj.payment.mode = mode;
		     }
		  else{
			      digitalData.cpj.payment = {
				       mode : mode
			     }
		      }
	      }
	else{
		  digitalData.cpj = {
				     payment  : {
				         mode : mode 
				      }  
		       }
	    }
	}
	catch(e){
        console.log("error fn:dtmPaymentModeSelection"+e.message);
      }
  }

function differentiateLuxSellerDtm(){
	var sellerList = $('#pdpSellerIDs').val();
	var sellerArray='';
	if(sellerList != undefined &&  sellerList != '' && sellerList != null){
		 sellerArray = $('#pdpSellerIDs').val().split(',').join('|');
	}
	
	var buyboxSeller = $("#sellerSelId").val();
	sellerList = sellerList.substr(1,(sellerList.length)-2);
	sellerList = sellerList.split(',');
	
	var otherSellers='';
	for (var i=0;i<sellerList.length;i++){
		if(sellerList[i].trim() != buyboxSeller){
			if(otherSellers == ''){
				otherSellers = sellerList[i].trim();
			}
			else{
				otherSellers += '_'+sellerList[i].trim();
			}
		}
	}
	
	$('#pdpBuyboxWinnerSellerID').val(buyboxSeller);
	$('#pdpOtherSellerIDs').val(otherSellers);

	//TISCSXII-2186 | pdp fix
	digitalData.product = {
			seller : {
				list : sellerArray,
				id   : $("#pdpBuyboxWinnerSellerID").val(),
				buyBoxWinner : $("#sellerNameId").html().toLowerCase()
			}   
		}
}

function dtmLuxAddWL(pagetype,productId,category){
	try{
		console.log("luxdtm  add wislist:");
		if(typeof _satellite != "undefined") {
			_satellite.track('add_to_wishlist');
		}

		if(typeof(digitalData.cpj.product) != "undefined"){
			digitalData.cpj.product.id = productId;
			digitalData.cpj.product.category = category;
		}		
		else {
			digitalData.cpj.product = {
					id  : productId,
					category : category
			}
		}

		if(typeof(digitalData.page.wishList) != "undefined"){
			digitalData.page.wishList.location = pagetype;
		}		
		else {
			digitalData.page.wishList = {
					location : pagetype 
			}
		}
	}
	catch(e){
		console.log("error fn:dtmAddToWishlist"+e.message);
	}  
	}

function dtmLuxRemoveWL(pagetype,productId,category){
	try{
		console.log("remove call wishl:");
		if (typeof _satellite != "undefined") {
			_satellite.track('remove_from_wishlist');
		}

		if(typeof(digitalData.cpj.product) != "undefined"){
			digitalData.cpj.product.id = productId;
			digitalData.cpj.product.category = category;
		}		
		else {
			digitalData.cpj.product = {
					id  : productId,
					category : category
			}
		}

		if(typeof(digitalData.page.wishList) != "undefined"){
			digitalData.page.wishList.location = pagetype;
		}		
		else {
			digitalData.page.wishList = {
					location : pagetype 
			}
		}
	}
	catch(e){
		console.log("error fn:dtmRemoveFromWishlist"+e.message);
	} 
}

function luxSocialSharePdp(channel){
	if(typeof _satellite != "undefined") {
		_satellite.track('social_share');
	}	
	if(typeof channel != "undefined" && channel != null){
		digitalData.social = {
				channel : {
					name : channel
				}
		}
		digitalData.cpj.product = {
				id:  $('#product_id').val().toLowerCase(),
				category : $('#product_category').val().toLowerCase()
		}
	}
}

function dtmLuxEmiTrack(emiBankSelected){
	try{
		if(typeof(_satellite) != "undefined"){
			_satellite.track('cpj_pdp_emi');
		}

		if(typeof(digitalData.cpj.emi) != "undefined"){
			digitalData.cpj.emi.bank = emiBankSelected;
		}		
		else {
			digitalData.cpj.emi = {
					bank : emiBankSelected
			}
		}
	  }
	catch(e){
	    console.log("error fn:dtmEmiBankTrack"+e.message);
	   }
	
  }
function dtmLuxSearchTags(){
	try{
		var finalOfferCount ='';
		var finalNewCount='';
		var offerCount ='';
		var newCount='';
		if( $('.onsale').length > 0 ){
		    offerCount = $('.onsale').length ;
		}
		if(offerCount !='undefined' && offerCount !='null' && offerCount != ''){
			finalOfferCount =offerCount;
		}
		if( $('.newarrival').length > 0 ){
		    newCount =	$('.newarrival').length ;
		}
		if(newCount != 'undefined' && newCount != 'null'  && newCount != ''){
			finalNewCount =newCount;
		}
		
			digitalData.cpj = {
					search : {
						offersCount : finalOfferCount,
						newCount    : finalNewCount
					}
				}
	}
	 catch(e){
	        console.log("error fn:dtmSearchTags"+e.message);
       } 
}

function dtmLuxOfferClick(){
	if(typeof(_satellite) != "undefined"){
		_satellite.track('offers_link_click');
	}	
}

//for pdp pincode check
function dtmLuxPdpPincode(msg,productCode,pin){
	try{
		if(typeof _satellite != "undefined"){
			if(msg == 'success'){
				_satellite.track('pin_successful');
			}	
			else{
				_satellite.track('pin_failed');
			}	
		} 
		if(typeof(digitalData.page.pin) != "undefined"){
			digitalData.page.pin.value = pin;
		}		
		else {
			digitalData.page.pin = {
					value : pin
			}
		}
		if(typeof(digitalData.cpj.product) != "undefined"){
			digitalData.cpj.product.id =productCode;
			digitalData.cpj.product.category = $('#product_category').val().toLowerCase();
		}		
		else {
			digitalData.cpj.product = {
					id      :  productCode,
					category : $('#product_category').val().toLowerCase()
			}
		}
	}
	catch(e){
		console.log("error fn:dtmPdpPincode"+e.message);
	}   
}

function getIcid2FromUrl()
{
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++)
    {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars["icid2"];
   
}

function dtmErrorTracking(errorType,errorName){
	try{
		if (typeof (_satellite)!= "undefined") {
			_satellite.track('error_tracking');
	    }
		/*digitalData.page = {
			error : {
				type  : errorType,
				name  : errorName
			}
		}*/
		   if(typeof(digitalData.page.error) != "undefined"){
		         digitalData.page.error.type = errorType;
		         digitalData.page.error.name = errorName; 
             }		
          else {
   	              digitalData.page.error = {
				          type :  errorType,
				          name  : errorName
		           }
               }
		//order failure track
		if(errorType == "Order not placed: Unsuccesful error"){
			if (typeof(_satellite)!= "undefined") {
				_satellite.track('cpj_order_fail');
		    }
			var product ='';
			var productCategory ='';
			if($("#product_id").val()!= ''){
				product = JSON.parse($("#product_id").val().toLowerCase());
			}
			if($("#product_category").val()!= ''){
				productCategory =  $("#product_category").val().toLowerCase().split(",");	
			}	
			/*digitalData.cpj = {
		    		    product : {
		    			 	         id  :  product,
		    		            category :  productCategory,	
		    		              price  :  $('#product_unit_price').val()
		    	               } 
		                 }*/
			
			 if(typeof(digitalData.cpj.product) != "undefined"){
		           digitalData.cpj.product.id = product;
		           digitalData.cpj.product.category = productCategory;
		           digitalData.cpj.product.price = $('#product_unit_price').val();
	            }		
	          else {
	                  digitalData.cpj.product = {
	                		        id  :  product,
	         	    		 category   :  productCategory,
	         	    		     price  :  $('#product_unit_price').val()
		                }
	               }
			if(typeof (digitalData.cpj.payment)!= "undefined"){
				digitalData.cpj.payment.quantity =$('#product_quantity').val();
			}
			if(typeof (digitalData.cpj.order)!= "undefined"){
				digitalData.cpj.order.failureReason = errorName;
			}
		}
	}
	catch(e){
             console.log("error fn:dtmErrorTracking"+e.message);
          }
}

function dtmOrderCancelSuccess(productId,category,reasonCancel){
	try{
		if (typeof (_satellite) != "undefined") {
			_satellite.track('order_cancellation');
	      }
		//TISPRDT-6213 fix
				digitalData.cpj = {
				    		product : {
				    				id  :  productId,
				    		 category   :  category	
				    	 }
				    	}
		
				digitalData.order = {
						cancellation : {
							reason  : reasonCancel
						}
				}
	}
	catch(e){
        console.log("error fn:dtmOrderCancelSuccess"+e.message);
     }	
	
}
//direct call Rules
//header clicks
$(document).on('mouseup','.header-account-link',function(){
	populateHeader('signin');
})
$(document).on('click','.header-search-link',function(){
	populateHeader('search');

})
$(document).on('click','.header-right li.logged_in',function(){
	populateHeader('welcome');
})
$(document).on('click','.header-right li.wishlist',function(){
	populateHeader('wishlist');
})
$(document).on('click','.header-right li.bag',function(){
	populateHeader('my_bag');
})
//footer clicks
$(document).on('click','.col-sm-2 .col-md-2 .col-lg-2 ul li ',function(){
	var footer = $(this).find('a').attr('title').html().toLowerCase();
	populateFooter(footer);
})
$(document).on('click','.col-sm-4 .col-md-4 .col-lg-4 .app-info ul .app-link li ',function(){
	populateFooter(footer);
})

$(document).on('click','.col-sm-4 .col-md-4 .col-lg-4 .app-info .get-in-touch .soc-links',function(){
	populateFooter(footer);
})


/*On Size selection | PDP #29  | tpr- 6301*/ 
	   $(document).on('click',".variant-select > li", function(){
		var product_size = $(this).find('a').html();
		if(typeof _satellite !="undefined"){
		   _satellite.track('cpj_pdp_product_size');
		}
		if(typeof digitalData.cpj.product != "undefined"){
			digitalData.cpj.product.size = product_size;
		}
		else{
			digitalData.cpj.product = {
				size : product_size
			}
	    }
	   });

// For product colour #30 |TPR-6302
$(document).on('click',".color-swatch > li", function(){
	var product_color = $(this).find('a').attr('title').toLowerCase();
	//alert(product_color);
/*	if(typeof(_satellite)!="undefined" &&  pageType == "product"){
	    _satellite.track('cpj_pdp_product_color');
	}
	if(typeof digitalData.cpj.product != "undefined"){
		digitalData.cpj.product.color = product_color;
	}
	else{
		digitalData.cpj.product = {
			color : product_color
		}
	}*/
})
//image click
$(document).on("click",".product-image-container >.clearfix > .pdp-img-nav ",function(){
	if(typeof(_satellite)!="undefined" &&  pageType == "product"){
		_satellite.track('cpj_pdp_image_click');
	}
})

$(document).on('click','.pincode-button',function(){
	if(typeof(_satellite)!="undefined"){
		_satellite.track('cpj_checkout_add_address');
	}
})

$(document).on("mouseup",".gp",function(){
   luxSocialSharePdp("googleplus");
})

$(document).on("mouseup",".tw",function(){
	luxSocialSharePdp("twitter");
})

$(document).on("mouseup",".fb",function(){
	luxSocialSharePdp("facebook");
})

$(document).on("mouseup",".review-accordion",function(){
	if(typeof(_satellite)!="undefined"){
		_satellite.track('review_link_click');
	}
});
//serp sort track
$(document).on("change", ".responsiveSort", function(){
	if(typeof(_satellite)!="undefined"){
		_satellite.track('sort_option_tracking');
	}
	var value = $(".responsiveSort option:selected").text();
	digitalData.page.sort = {
			option: value
	}
}); 

$(document).on("mouseup",".tabs-block .accordion-title",function() {
	if(typeof(_satellite)!="undefined"){
		_satellite.track('product_specification_link_click');
	}
})
//internal search link

$(document).on('click','.product-item',function(){
	if(pageType =='productsearch' && typeof(_satellite)!="undefined"){
		_satellite.track('internal_search_link_clicks');
	}
})

//video track
$(document).on('click','.play',function(){
	//var video =$(this).parent().find('a').attr('data-video-url');
	if(typeof(_satellite)!="undefined"){
		_satellite.track('product_video');
	}
	var productCode = $("input[name=productCodeMSD]").val();
	var productCategory = $('#product_category').val().trim().toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g, "_").replace(/['"]/g, "");
	var product_video = productCode+"_video";
			if(typeof digitalData.cpj.product != "undefined"){
				digitalData.cpj.product.id = productCode;
				digitalData.cpj.product.category = productCategory;
			}
			else{
				digitalData.cpj.product = {
		     		id :  productCode,
		     		category : productCategory
		     	}
			}
			if(typeof digitalData.product.video != "undefined"){
				digitalData.product.video.name = product_video;
			}
			else{
				digitalData.product.video = {
					name :  product_video
		     	}
			}
})

//signup start
$(document).on('mouseup','#header-account .h4 a',function(){
	if(typeof(_satellite)!="undefined"){
		_satellite.track('signup_start');
	}
})





