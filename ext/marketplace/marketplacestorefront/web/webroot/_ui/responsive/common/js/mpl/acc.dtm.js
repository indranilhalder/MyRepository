var digitalData;
var isImageHoverTriggered = false;		// flag to identify mouse hover action
$(document).ready(function(){

    var DTM_SCRIPT_DEV = "<script type='text/javascript' src='//assets.adobedtm.com/9fd06d4068c619c47b289b9c496761efd086a233/satelliteLib-9d27bc3356d4973d37a14f45dd3a41618b35a35a-staging.js'></script>";
    var DTM_SCRIPT_PROD = "<script src='//assets.adobedtm.com/9fd06d4068c619c47b289b9c496761efd086a233/satelliteLib-9d27bc3356d4973d37a14f45dd3a41618b35a35a.js'></script>";
    
    var visitor_ip = ACC.config.VisitorIp;
	
	var user_type = $.cookie("mpl-userType");
	var user_id = $.cookie("mpl-user");
	var site_region = $("#site_section").val();
	var site_currency ='INR';
	var domain_name = document.domain;
	
	var user_login_type = $('#userLoginType').val().trim();
	var pageType = $('#pageType').val();
	var pageName= $('#pageName').val().toLowerCase();
	var pageNameU = $('#pageName').val();
	var tealiumOrderFlag = $('#tealiumOrderFlag').val();
	var Promo_Id ="";
	if($("#product_applied_promotion_title").val() && $("#product_applied_promotion_code").val() !=undefined)
	{
		 var promo_title=$("#product_applied_promotion_title").val().toLowerCase().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
		 var promo_id=$("#product_applied_promotion_code").val().toLowerCase().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
		 Promo_Id = promo_title+":" + promo_id;
	}
    var script ="";
	var user_login_type = $('#userLoginType').val().trim().toLowerCase();	
	var product_category ="";
	var page_subcategory_name_L2 ="";
	var page_subcategory_name_l3 ="";
	if($("#product_category").val() !=undefined || $("#product_category").val() !=null){ 
		product_category = $("#product_category").val().replace(/_+/g, '_') ;  
	}
	if($("#page_subcategory_name").val() !=undefined || $("#page_subcategory_name").val() !=null){ 
		page_subcategory_name = $("#page_subcategory_name").val().replace(/_+/g, '_') ;
	}
	if($("#page_subcategory_name_l3").val() !=undefined || $("#page_subcategory_name_l3").val() !=null){ 
		page_subcategory_name_L3 = $("#page_subcategory_name_l3").val().replace(/_+/g, '_');
	}
	
	var buyboxSellerid = $("#sellerSelId").val();
	var buyboxSellerName = $("#sellerNameId").val();
	var sellerList = $('#pdpSellerIDs').val();
	var subdomain = window.location.href.split("/")[2].split(".")[0];
	var subDomain= "";
	if(subdomain != "undefined"){
		subDomain = subdomain;
	}
	 var currentPageURL = window.location.href;

  
	
   /*onload generic variables for all pages*/
	digitalData = {
		page : {
			pageInfo : {
				pageName  : pageName,
				domain    : domain_name,
				subDomain : subDomain
			},
			category : {
				primaryCategory : pageType
			}
		}
	}
	
	if(user_id != "anonymous"){
		digitalData.account = {
			login : {
				customerID : user_id
			}	
		}
	}
	
	/*DTM Implementation page wise for on load variables starts*/
	
	// TPR-6295 | For Homepage
	if (pageType == "homepage") {
		if(typeof _satellite !="undefined"){
		       _satellite.track('cpj_home_page');
		}
	}
	
	// For PDP
	if (pageType == "product"
			|| pageType == "/sellersdetailpage") {
		
		   var product_id = $("#product_id").val().toLowerCase();
		   var product_category = $("#product_category").val().toLowerCase();
		   var product_brand = $("#product_brand").val().toLowerCase();
		   var product_discount = $("#product_discount").val();
		   var pincode = $('#pin').val();
		   var prevPageUrl = document.referrer;
		   var findingMethod ="";
		   
        //TPR-6300 | Track pdp starts
		      if(typeof _satellite !="undefined"){
		          _satellite.track('cpj_pdp');
		       }	
			
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
			           if(prevPageUrl.indexOf('/c-msh') > -1){
				           findingMethod = 'product grid';
			            }
			           else if(prevPageUrl.indexOf("/s/") > -1 || prevPageUrl.indexOf('/m/') > -1){
				           findingMethod = 'seller';
			            }
			           else if(prevPageUrl.indexOf('/c-mbh') > -1){
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
				           findingMethod = '';
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
		            digitalData.cpj = {
				            pdp : {
			               findingMethod  : findingMethod
				          }
		              }
	           }  
	     //TPR-6300 | Track pdp ends
	             
		//TPR-6333 | Track Geo-location of users
		if(pincode != ''){
			digitalData.geolocation = {
					pin : {
					code : pincode
					}
			}
		}
		if(Promo_Id != "" && Promo_Id !=""){
		   digitalData.cpj.promo = {
				id : Promo_Id
		   }
		}
		
	      if($("#out_of_stock").val() == "true"){
			  if(typeof _satellite !="undefined"){
			   _satellite.track('out_of_stock');
		      }
			    digitalData.cpj = {
					product : {
						id     :  product_id ,
				      category :  product_category
				 }
			  }
			  
		}
		
		digitalData.product = {
			seller : {
				list : sellerList,
				id   : buyboxSellerid,
				buyBoxWinner : buyboxSellerName
			}   
		}
		
		digitalData.page.category.subCategory1 = product_category;
		digitalData.page.category.subCategory2 = page_subcategory_name; 
		digitalData.page.category.subCategory3 = page_subcategory_name_L3;
		
		
	}		
	// Generic Page Script
	if (pageType != 'homepage' && pageType != 'product' && pageType != '/sellersdetailpage' && pageType != 'productsearch'
			&& pageType != 'category' && pageType != 'cart'
			&& pageType != 'multistepcheckoutsummary'
			&& pageType != 'profile' 
			&& pageType != 'orderconfirmation'
			&& pageType != 'notfound'
			&& pageType != 'businesserrorfound'
			&& pageType != 'nonbusinesserrorfound') {
		
		//
			
	}
	
	// TPR-6297 | For PLP
	if (pageType == "category" || pageType == "electronics") {
		if(typeof _satellite !="undefined"){	
		   _satellite.track('cpj_category_pages');
		}
		  /*  product impressions*/
		    dtmProductImpressionsPlp();
	}
		
	//Search
	if(pageType == "productsearch"){
		if(typeof _satellite !="undefined"){
		    _satellite.track('cpj_search_pages');
		}
		 
		//TPR-6367  | for null search
	     var isVisible = $('.search-empty.no-results.wrapper:visible').is(':visible');
	     var searchTerm = $('#search_keyword').val().toLowerCase();
	     var searchCategory = $('#searchCategory').val();
	     var searchResults =  $('.search-result h2 span:first').text().replace(/"/g, "");
		    if(isVisible && typeof _satellite !="undefined" ){
			         _satellite.track('null_search');
			        digitalData.internal = {
					 search : {
						 term : searchTerm,
						 category : searchCategory
					  }
			  }
		 }
		//TPR-6367  | for normal  search
		       digitalData.internal = {
					 search : {
						 term : searchTerm,
						 category : searchCategory,
						 results  : searchResults
			 		 }
			  }
		      /*offercount,newcount */
		  dtmSearchTags();
		       /*  product impressions*/
		  dtmProductImpressionsSerp();	
	
	  }
	
	// Cart page
	if(pageType == "cart"){
		var pinCode = $('#pinId').val();
		if(typeof _satellite != "undefined"){
		    _satellite.track('cpj_cart_page');
		}
		digitalData.cpj = {
			product : {
				id : $("#product_id").val().toLowerCase(),
				category : getListValue("product_category")
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
		//TPR-6371 | track promotions
		if($('#promolist').val() != '[]') {
			   digitalData.cpj.promo = {
					id : $('#promolist').val().toLowerCase().replace(/'/g, "")
			   }
			}
	}
	
	// Checkout pages
	if(pageType =="multistepcheckoutsummary"){
		var checkoutPageName = pageName +":" + $('#checkoutPageName').val().toLowerCase();
		
		digitalData = {
				page : {
					pageInfo : {
						pageName  : checkoutPageName,
						domain    : domain_name,
						subDomain : subDomain
					},
					category : {
						primaryCategory : "checkout"
					}
				}
			}
		
		
		var product_id = $("#product_id").val().toLowerCase();
		var product_category = $("#product_category").val().toLowerCase();
		digitalData.cpj = {
				product : {
					id     :  product_id ,
			      category :  product_category
			 }
		  }
	}
	
	//TPR-6296 | brand pages
	
	 if(currentPageURL.indexOf("/c-mbh") > -1 &&
		pageType != 'notfound' && 
		pageType != 'nonbusinesserrorfound' &&
		pageType != 'businesserrorfound' )
		 
	 { 
		     if(typeof(_satellite) !="undefined"){
				  _satellite.track('cpj_brand_pages');
			  }
		     
		       if(pageName !='undefined'){
		    	    var brand = pageName.split('BrandStore-');
			        var brandName ="";
			          if((brand.length) > 1 && typeof(brand[1]) != "undefined" && brand[1] != ''){
						   brandName = brand[1].trim().toLowerCase();
					    }
			          else{
						   brandName =	pageName;
					    } 
		          }
		    
		    	     digitalData.cpj = {
						   brand : {
							  name : brandName
						 }
				     }
		}
	
	//TPR-6029|Checkout changes
	if(pageType =="orderconfirmation" && $('#orderIDString').val()!= ''){
		if(typeof _satellite !="undefined"){
			_satellite.track('cpj_order_successful');
		}	
		
		digitalData.cpj = {
				   product : {
					   id       :  $('#product_sku').val().toLowerCase() ,
			           category :  $('#product_category').val().toLowerCase(),
			           price    :  $('#product_unit_price').val()
			 }
		  }
		
		if(typeof digitalData.cpj.payment !='undefined'){
			digitalData.cpj.payment = {
					quantity : $('#product_quantity').val()
			}
			
		}
	}
	
	//TPR-6707 | track 404 pages
	if(pageType =='notfound' || pageType =='nonbusinesserrorfound' || pageType =='businesserrorfound' || digitalData == 'undefined' ){
		if(typeof(_satellite) !="undefined"){
			_satellite.track('404_error');
		}	
	}
	
	 //TPR-6299 | for merchandising pages
	
     if(pageNameU != 'Product Grid' &&   pageNameU != 'Product Details' &&
    		 pageNameU != 'Cart Page'    &&   pageNameU != 'Checkout-Login Page'  &&  pageNameU != 'Login Page' &&
    		 pageNameU != 'Multi Checkout Summary Page' && pageNameU != 'Order Confirmation Page')
      {	 
            	if(typeof(_satellite) != "undefined"){
    		      _satellite.track('cpj_merchandising_pages');
	  	        }
      }
     
   //TPR-6369 |Error tracking dtm
     if(pageType == 'login'){
    	    if(currentPageURL.indexOf('/login?error=true') > -1  || currentPageURL.indexOf('/login/register') > -1 ){
    			  dtmErrorTracking("login error","login error");
    		  }
        }
     
   //for checkout login page error
     if(pageType == 'checkout-login'){
    	 if(currentPageURL.indexOf('/login?error=true') > -1){
    		 dtmErrorTracking("login error","checkoutlogin error"); 
    	 }
     }
    	
   //checkout register error 
     if(pageType == "notfound"){
    	 if(currentPageURL.indexOf('/checkoutRegister') > -1){
        	 dtmErrorTracking("login error","checkout register error"); 
    	 }
     }
    	
/*  Direct call rule starts here*/
	
    // For icid
	var url_string = window.location.href;
	var url = new URL(url_string);
	var icid2Param = url.searchParams.get("icid2");
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
		digitalData.page.display = {
			hierarchy : JSON.stringify( breadcrum )
		}
	}
	
	// Assignment of digial data object
	var DTMscript ='<script type="text/javascript"> var digitalData = '+JSON.stringify(digitalData)+'</script>';
	
	if(domain_name =="www.tatacliq.com"){
		script=DTM_SCRIPT_PROD;
	}
	else{
		script=DTM_SCRIPT_DEV;
	}
	DTMscript+=script;
	
	$('#DTMhome').html(DTMscript);
	
	//for footer clicks
	$(document).on('click','.yCmsComponent',function(){
		var footer_link = $(this).find('a').html().toLowerCase();
		if(typeof _satellite !="undefined"){
		   _satellite.track('footer_link_clicks');
		}
		digitalData.footer = {
				link : {
				   name : footer_link
				}	
			}
	})
	
	//for header clicks
    $(document).on("click",".headerUl li",function(){
    var header_link = "";	
	var link_name =  $(this).find("a").eq(0).attr("href");
	var id_name =  $(this).find("a").eq(0).attr("id");
	var class_name =$(this).find("span").eq(0).attr("class");
	if(id_name == "socialLogin" || class_name == "material-icons"){
		if(typeof _satellite !="undefined"){
		    _satellite.track('header_link_clicks');
		}
		header_link = "login";
	}
	else if(link_name == "/cart"){
		if(typeof _satellite !="undefined"){
		     _satellite.track('header_link_clicks');
		}
			header_link = "my_bag";
	}
	else{
		if(typeof _satellite !="undefined"){
	      _satellite.track('header_link_clicks');
		}
	   header_link = $(this).find('a').eq(0).text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"").trim();
	}
	digitalData = {
			header : {
				link : {
					name : header_link
				}
			}
		}
	});
	
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
	 // TPR-6029 |quick view  size|serp#39
	   $(document).on('click',"#quickViewVariant > li", function(){
	   	var product_size = $(this).find('a').html();
	   	if(typeof _satellite != "undefined"){
	   	_satellite.track('cpj_qw_product_size');
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
	// TPR-6366 | need help starts
	$(document).on('click',"#up",function(){
		if(typeof _satellite !="undefined"){
		  _satellite.track('need_help');
		}
	 });
	
	/*live chat*/
	$(document).on("click","#chatMe",function(){
		if(typeof _satellite !="undefined"){
		  _satellite.track('need_help_live_chat');
		}
	});
	
	/*call*/
	$(document).on("click", "#callMe", function(e) {
		if(typeof _satellite !="undefined"){
		   _satellite.track('need_help_call');
		}
	});
	
	$(document).on('click','#gcbChatRegister',function(){
		if(typeof _satellite !="undefined"){
		  _satellite.track('need_help_connect');
		}
	});
	
    $(document).on('click','.gwc-chat-registration-skip #gcbChatSkipRegistration',function(){
    	if(typeof _satellite !="undefined"){
    	    _satellite.track('need_help_cancel');
    	}
	});
    $(document).on('click','.button_fwd_wrapper.actions .bcancel',function(){
    	if(typeof _satellite !="undefined"){
    	    _satellite.track('need_help_cancel');
    	}
    })
	//need help ends
    
   /* // 404_error #8
    if(typeof digitalData == "undefined"){
    	if(typeof _satellite !="undefined"){
    	  _satellite.track('404_error');
    	}
    }*/
    
    
    // For product colour #30 |TPR-6302
    $(document).on('click',".color-swatch > li", function(){
    	var product_color = $(this).find('img').attr('title').toLowerCase();
    	if(typeof(_satellite)!="undefined" &&  pageType == "product"){
    	    _satellite.track('cpj_pdp_product_color');
    	}
    	if(typeof digitalData.cpj.product != "undefined"){
    		digitalData.cpj.product.color = product_color;
    	}
    	else{
    		digitalData.cpj.product = {
    			color : product_color
    		}
    	}
    })
    /*TPR-6029 | colour selection on quickview #40*/
    $(document).on('click',".color-swatch > li", function(){
     	var product_color = $(this).find('img').attr('title').toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
     	if(typeof _satellite != "undefined" && pageType != "product"){
     	   	_satellite.track('cpj_qw_product_color');
     	 }
     	if(typeof digitalData.cpj.product != "undefined"){
    		digitalData.cpj.product.color = product_color;
    	}
    	else{
    		digitalData.cpj.product = {	
    			color : product_color
    		}
    	}
     });
    
    //TPR-6334 | Login-logout start
    var dtm_Login_status = $.cookie("dtmSigninStatus");
	var dtm_Logout_status = $.cookie("dtmSignoutStatus");
	if(typeof dtm_Login_status != "undefined"){
		loginStatusCookieCheck("dtmSigninStatus",dtm_Login_status);
    }
	
    if(typeof dtm_Logout_status != "undefined"){
    	if(typeof _satellite !="undefined"){
    		_satellite.track('logout_successful');
      	}
		deleteCookie("dtmSignoutStatus");
    }
    
    if(pageType == 'login'){
    	window.sessionStorage.setItem('isLoginPageVisited','yes');
    }
    
    if(pageType != 'login' && pageType != 'checkout-login'){
		if(typeof $.cookie("dtmRegistrationJourney") != "undefined"){
    		deleteCookie("dtmRegistrationJourney=Started");
    	}
	}
    // TPR-6338 | signup-start track
    $(document).on('click','.logIn-hi .sign-in-dropdown .headeruserdetails , .logIn-hi .sign-in-dropdown .signin-dropdown-body .register_link',function(){
    	if(typeof $.cookie("dtmRegistrationJourney") == "undefined"){
    		if(typeof _satellite !="undefined"){
    			_satellite.track('signup_start');
    	  	}
    		document.cookie = "dtmRegistrationJourney=Started; path=/;";
    	}
    })
    //TPR-6334 | Login-logout end
	//TPR-6369 |error tracking for unsuccessful order placed
    if(tealiumOrderFlag !='undefined' && tealiumOrderFlag == 'true'){
    	dtmErrorTracking("Order not placed: Unsuccesful error","errorName");
    }
   
    //tpr-TPR-6362 | track checkout activity
    if($("#checkoutPageName").val()=="Select Address"){
    	if(typeof _satellite != "undefined") {  
    		_satellite.track('cpj_checkout_proceed_to_address');
    	}	
    }

   /* if($("#checkoutPageName").val()=="Payment Options"){
    	if(typeof _satellite != "undefined") {  
    		_satellite.track('cpj_checkout_proceed_to_payment');
    	}	
    }

    if($("#checkoutPageName").val()=="Choose Your Delivery Options"){
    	if(typeof _satellite != "undefined") {  
    		_satellite.track('cpj_checkout_delivery_option');
    	}	
    }*/
    
    /* if($("#checkoutPageName").val()=="Review Order"){
     	if(typeof (_satellite)!= "undefined") {  
     		_satellite.track('cpj_checkout_proceed_to_review');
     	}	
     }*/
});
function differentiateSeller(){
	var sellerList = $('#pdpSellerIDs').val();
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

	return "["+finalCategoryArray+"]";
}

// For add to bag and buy now #31, 32
function dtmAddProductToBag(triggerValue){
	var eventName='cpj_';
	var pageType = $('#pageName').val();
	if( pageType == "Product Details" || pageType == "View Seller Page"){
		eventName+="pdp_"+triggerValue;
	}
	if(typeof _satellite !="undefined"){
     	_satellite.track(eventName);
	}

}

//for pdp pincode check
function dtmPdpPincode(msg,productCode,pin){
	       if(typeof _satellite != "undefined"){
                if(msg == 'success'){
                     _satellite.track('pin_successful');
                     }	
             else{
                  _satellite.track('pin_failed');
              }	
            } 
                digitalData.page.pin = {
	                    value : pin
                      }
     
                digitalData.cpj = {
        		            product : {
		                       id      :  productCode,
		                       category : $('#product_category').val()
    	                  } 
                      }
 }

function dtmCartPincodeCheck(selectedPincode,msg){
	 if(typeof(_satellite) != "undefined"){
         if(msg == 'success'){
              _satellite.track('pin_successful');
          }	
      else{
           _satellite.track('pin_failed');
          }	
       } 
         digitalData.page.pin = {
                 value : selectedPincode
        }
         
         digitalData.cpj = {
		            product : {
                    id      :  $('#product_id').val().toLowerCase(),
                    category : $('#product_category').val().toLowerCase()
               } 
           }
}


function dtmSearchTags(){
	if( $('.on-sale').length > 0 ){
	   var offerCount = $('.on-sale').length ;
	}
	if( $('.new').length > 0 ){
	   var newCount =	$('.new').length ;
	}
	
	digitalData.cpj = {
			search : {
				offersCount : offerCount,
				newCount    : newCount
			}
		}
	
}
	/*Product Specification*/
	$(document).on("click",".nav-wrapper .nav.pdp",function(){
		if(typeof _satellite !="undefined"){
	      _satellite.track('product_specification_link_click');
		}
	})
	
	/*Search Feedback*/
	$('.feedbackYes.blue').click(function(){	
		if(typeof _satellite !="undefined"){
	       _satellite.track('feedback_submit');
		}
			
	});

      /*Social share pdp*/

	$(document).on('click','.share li',function(){
		if(typeof _satellite !="undefined"){
			  _satellite.track('social_share');
		}
		var selector = $(this).find('a').attr('class');
		var channel_name='';
		if (selector == 'tw'){
			channel_name ='twitter';
		}
		if (selector == 'fb'){
			channel_name ='facebook';
		}
		if (selector == 'gp'){
			channel_name = 'googleplus';
		}
		if (selector == 'mail mailproduct'){
			channel_name = 'email';
		}
		
		
	    	if(typeof channel_name != "undefined" && channel_name != null){
			
			   digitalData.social = {
					channel : {
						name : channel_name
				}
			  }
			   digitalData.cpj.product = {
					id:  $('#product_id').val(),
					category : $('#product_category').val()
		    	}
	    	  }
	 });


/*	Review Link click on pdp*/
   $(document).on('click','.gig-rating-readReviewsLink_pdp',function(){
	   if(typeof _satellite !="undefined"){
	     _satellite.track('review_link_click');	
	   }
  });
   
/*Compare now page*/
   $(document).on('click','.shop-now',function(){
		 if(typeof _satellite !="undefined"){
			  _satellite.track('shop_now_clicks');
		 }
	 });
   
  /*Sort by SERP/PLP */
	$(document).on('click', '.list_title .UlContainer .sort', function() {
	    if (typeof _satellite != "undefined") {
	        _satellite.track('sort_option_tracking');
	    }
	    var value = $(this).text().trim().toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g, "_").replace(/['"]/g, "");
	    digitalData.page.sort = {
	        option: value
	    }
	});
 
   //Comma separated strings changed to array of strings
	function getListValue(divId){
		var categoryList = $("#"+divId).val().replace(/_+/g, '_');
		var categoryArray = categoryList.split(",");
		var finalCategoryArray=[];
		for (i = 0; i < categoryArray.length; i++) { 
			finalCategoryArray.push("\""+categoryArray[i]+"\"");
		}
		return "["+finalCategoryArray+"]";
	}

	/*PDP, quickview image hover- TPR-6340*/
	$(document).on("mouseover",".zoomContainer",function(e) {
		if($('#pageType').val() != "/compare"){
			if($('#pageType').val() == "product"
				|| $('#pageType').val() == "/sellersdetailpage"){
				if(!isImageHoverTriggered){
					if(typeof _satellite != "undefined"){
						_satellite.track('cpj_pdp_image_hover');
						isImageHoverTriggered = true;
					}
				}
			}
			else {
				   if(typeof _satellite != "undefined" && (!isImageHoverTriggered)){
					    _satellite.track('cpj_qw_image_hover');
					  isImageHoverTriggered = true;
			        }
			    }
		   }
	});	
	
	/*PDP thumbnail image click*/
	$(document).on("click",".product-info > .product-image-container > .productImageGallery .imageListCarousel .thumb",function(){
		var thumbnail_value = $(this).parent().attr('class');
		var thumbnail_type = $(this).find('img').attr('data-type');
		if(thumbnail_type == "image"){
			if (typeof _satellite != "undefined"  && pageType == 'product') {
				_satellite.track('cpj_pdp_image_click');
		    }
		}
		/*TPR-6289 - video tracking*/
		else if(thumbnail_type == "video"){
			if (typeof _satellite != "undefined") {
				_satellite.track('product_video');
		    }
			var productCode = $("input[name=productCodeMSD]").val();
			var productCategory = $('#product_category').val().trim().toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g, "_").replace(/['"]/g, "");
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
				digitalData.product.video.name = thumbnail_value;
			}
			else{
				digitalData.product.video = {
					name :  thumbnail_value
	         	}
			}
		}
	})
	
	//TPR-6340 |qv image click
	$(document).on("click",".quick-view-popup > .product-image-container > .productImageGallery .imageListCarousel .thumb",function(){
		if(typeof _satellite != "undefined") {
			_satellite.track('cpj_qw_image_click');
	    }
   });
	
	// TPR-6287 | filter tacking start
	var restrictionFlag='false';
	
	$(document).on('click','.jqtree-title.jqtree_common',function(){
		var filter_value= $(this).text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['-]/g,"");
		var filter_type=$(this).parents('form').siblings('div.facet-name.js-facet-name.facet_desktop').find('h3').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/'/g,"");
		if (typeof _satellite != "undefined") {
			_satellite.track('filter_temp');
	    }
		if(typeof digitalData.filter != "undefined"){
			if(typeof digitalData.filter.temp != "undefined"){
				digitalData.filter.temp.type = filter_type;
				digitalData.filter.temp.value = filter_value;
			}
			else{
				digitalData.filter.temp = {
					type : filter_type,
					value : filter_value
				}
			}
		}
		else{
			digitalData.filter = {
				temp :  {
					type : filter_type,
					value : filter_value
				}
	     	}
		}
		restrictionFlag='true';
	})
	
	
	function setupSessionValuesDtm(){
		if($('.bottom-pagination .facet-list.filter-opt').children().length > 0){
			var filterTypeList=[];
			var filterTypeFinalList=[];
			var filterValueList=[];
			var sessionPageUrl=window.location.href;
			var finalFilterCombination='';
			
			$('.bottom-pagination .facet-list.filter-opt').children().each(function(){
				var filterType = $(this).children().eq(0).attr('class').toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
				filterTypeList.push(filterType);
				var filterValue = $(this).children().eq(1).attr('value').toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
				filterValueList.push(filterValue);
				if(finalFilterCombination == ''){
					finalFilterCombination = filterType +":"+ filterValue;
				}
				else{
					finalFilterCombination = finalFilterCombination +"|"+ filterType +":"+ filterValue;
				}
			})
			
			$.each(filterTypeList, function(i, el){
				if($.inArray(el, filterTypeFinalList) === -1){
					filterTypeFinalList.push(el)
				};
			});
			if(filterValueList.length > 0 && filterTypeList.length > 0){
				if(typeof digitalData.filter != "undefined"){
					if(typeof digitalData.filter.final != "undefined"){
						digitalData.filter.final.type = filterTypeFinalList.toString();
						digitalData.filter.final.value = filterValueList.toString();
						digitalData.filter.final.combination = finalFilterCombination;
					}
					else{
						digitalData.filter.final = {
							type : filterTypeFinalList.toString(),
							value : filterValueList.toString(),
							combination : finalFilterCombination
						}
					}
				}
				else{
					digitalData.filter = {
						final :  {
							type : filterTypeFinalList.toString(),
							value : filterValueList.toString(),
							combination : finalFilterCombination
						}
			     	}
				}
			}
			
			if (typeof _satellite != "undefined") {
				_satellite.track('filter_final');
		    }
		}
	}
	
	
	$(window).unload(function(event) {
		var pageType = $('#pageType').val();
		if(pageType == 'category' || pageType == 'productsearch'){
			if(restrictionFlag != 'true'){
				setupSessionValuesDtm();
			}
		}
	});
	// TPR-6287 | filter tacking end
	
	//for TPR-6334
	function loginStatusCookieCheck(cookieName, cookieValue){
    	var location = "header_login_popup";
    	
    	var user_login_type = $('#userLoginType').val().trim();
    	if(user_login_type == "guest_user"){
    		user_login_type = "email";
    	}
    	
    	if(typeof _satellite !="undefined"){
    		if(cookieValue == "AccountLoginSuccess"){
    			_satellite.track('login_successful');
    			if(window.sessionStorage.getItem('isLoginPageVisited') != null){
    				location = "login_page";
    				window.sessionStorage.removeItem('isLoginPageVisited');
    	    	}
        	}
    		//For TPR-6362
    		else if(cookieValue == "CheckoutLoginSuccess"){
    			_satellite.track('cpj_checkout_login');
    			location = "checkout_login_page";
        	}
    		else if(cookieValue == "RegistrationSuccess"){
    			_satellite.track('signup_success');
    			if(typeof $.cookie("dtmRegistrationJourney") != "undefined"){
    				deleteCookie("dtmRegistrationJourney");
    			}
        	}
    		else if(cookieValue == "LoginFailure"){
    			_satellite.track('login_failed');
        	}
    	}
    		
		if(typeof digitalData.account != "undefined"){
			if(typeof digitalData.account.login != "undefined"){
				digitalData.account.login.type = user_login_type;
				digitalData.account.login.location = location;
			}
			else{
				digitalData.account.login = {
					type : user_login_type,
					location : location
				}
			}
		}
		else{
			digitalData.account = {
				login : {
					type : user_login_type,
					location : location
				}
			}
		}
    	deleteCookie(cookieName);
	}

	//TPR-6364 start | Track add and remove functionality from wishlist
	function dtmAddToWishlist(pagetype,productId,category){
		if(typeof _satellite != "undefined") {
			_satellite.track('add_to_wishlist');
	    }
		   digitalData.cpj = {
			   product : {
				      id  :  productId,
				 category :  category
			}
		}
		     digitalData.page = {
		    		wishList : {
		    		    location : pagetype 
		    		}
		    }
	}
	
	 function dtmRemoveFromWishlist(pagetype,productId,category){
		if (typeof _satellite != "undefined") {
			_satellite.track('remove_from_wishlist');
	    }
		   digitalData.cpj = {
			   product : {
				      id  : productId,
				 category : category
			}
		}
		     digitalData.page = {
		    		  wishList : {
		    		  location : pagetype 
		    		}
		    }
	}
	
	 //TPR-6290 |Product Comparison Tracking
    function dtmAddToComparedList(productList){
    	 digitalData.product = {
    		 comparison : {
    			 array   : productList
    		 }
    	 }
	 }
    
    function dtmAddToCompare(productId,category){
    	digitalData.cpj = {
    		product : {
    				id  :  productId,
    		 category   :  category	
    	 }
    	}
    }
    
  //TPR-6305 |track emi options starts
    function  dtmEmiTrack(){
    	if(typeof(_satellite) != "undefined"){
	 		_satellite.track('cpj_pdp_emi');
	 	}
    }
    
    function dtmEmiBankTrack(emiBankSelected){
    	digitalData.cpj = {
    		    	emi :  {
    			    bank : emiBankSelected
    			}
    	}
    }
    //track emi options ends
    
  //TPR-6367  | for  search
	$(document).on('click','#searchButton',function(){
		if (typeof _satellite != "undefined") {
			_satellite.track('internal_search');
	    }
		
	})
	
	//TPR-6369 | Error Tracking
	function dtmErrorTracking(errorType,errorName){
		if (typeof (_satellite)!= "undefined") {
			_satellite.track('error_tracking');
	    }
		digitalData.page = {
			error : {
				type  : errorType,
				name  : errorName
			}
		}
		//order failure track
		if(errorType == "Order not placed: Unsuccesful error"){
			if (typeof(_satellite)!= "undefined") {
				_satellite.track('cpj_order_fail');
		    }
			digitalData.cpj = {
		    		    product : {
		    			 	         id  :  $("#product_id").val(),
		    		            category :  $("#product_category").val(),	
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
   //TPR-6288 | My account Order cancel
	function dtmOrderCancelSuccess(productId,category,reasonCancel){
		if (typeof (_satellite) != "undefined") {
			_satellite.track('order_cancellation');
			
	    }
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
	  //TPR-6288 | My account Order Return
	function dtmOrderReturn(dtmReturnReason,dtmReturnProduct,dtmReturnProductCat){
		if (typeof (_satellite) != "undefined") {
			_satellite.track('order_returns');
			
	    }
		digitalData.cpj = {
	    		product : {
	    				id  :  dtmReturnProduct,
	    		 category   :  dtmReturnProductCat	
	    	 }
	    	}
		digitalData.order = {
				return : {
				reason : dtmReturnReason
				}
		}
	}
	
	//TPR-6029|DTM CHECKOUT Changes
	function dtmPaymentModeSelection(mode){
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
	}
	
	function dtmCouponCheck(msg,couponCode){
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
	
	function dtmStoreSelection(storeName){
		if(typeof (_satellite)!= 'undefined'){
			_satellite.track('cpj_checkout_store_selection');
		 }
		
		digitalData.cpj = {
				  checkout: {
					 storeName : storeName
				}
		}
	}
	
     //TPR-6372 | Track widgets powered by recommendation
	$(document).on("click",".trending .owl-stage .owl-item",function(){
		var name = $('#ia_products_hot').find('span:first').text();
		var partner  = "IA";
		var location = $('#pageType').val();
		var widgetName = name+":"+partner+":"+location ;
		
		if(typeof _satellite != "undefined") {
			_satellite.track('widget_tracking');
			
	    }
		
		digitalData.page = {
				widget : {
					name : widgetName
				}
		}
		
	})
	
	//TPR-6308 | Product Recommendation
  $(document).on('click','.iaAddToCartButton.ia_both',function(){
	   if(typeof(_satellite) != "undefined") {  
	          if($('#pageType').val() == "product" ){
		           _satellite.track('cpj_pdp_product_reco_cart_add');
	            }
	  
	          if($('#pageType').val() == "cart" ){
	         _satellite.track('cpj_cart_product_reco_cart_add');
	           }
	 }
  });

	//tpr-6362 |track checkout activity |8
$(document).on('click','.add_address_button',function(){
	if(typeof (_satellite)!= "undefined") {  
		_satellite.track('cpj_checkout_add_address');
	}
})	
	

/*$(document).on('click','#newAddressButton .button',function(){
	if(typeof (_satellite)!= "undefined") {  
		_satellite.track('cpj_checkout_save_address');
	}
})*/

/*product impressions start*/
function dtmProductImpressionsSerp(){
	var count = 10; 
	var productArray= [];
    var searchResult = $("ul.product-list li.product-item").length;
	if(searchResult < count ){
		count = searchResult;
    }
   for(var i=0;i< count;i++)
   {
	 var selector = 'ul.product-list li.product-item:eq('+i+') span.serpProduct #productCode';
	  product = $(selector).val();
	  productArray.push(product);

   }
     var impressions = productArray.join("|");
   
     	  digitalData.page.products = {
     	    	impression : impressions
          }
	
     	  digitalData.cpj.product = {
     		         id : productArray
              }
 }

function dtmProductImpressionsPlp(){
	var count = 10; 
	var productArray= [];
    var searchResult = $("ul.product-listing.product-grid li.product-item").length;
	if(searchResult < count ){
		count = searchResult;
    }
   for(var i=0;i< count;i++)
   {
      var  selector = 'ul.product-listing.product-grid li.product-item:eq('+i+') span.serpProduct #productCode';
      if(typeof selector !="undefined"){
  	    product = $(selector).val();
      }
      productArray.push(product);
    }
     var impressions = productArray.join("|");
         digitalData.page = {
    	     	products : { 
    		    impression : impressions
		       }
            }
 
    	digitalData.cpj = {
    			 product : { 
    		       id : productArray
		      }
         }
}	
/*product impressions end*/	

// TPR-6292 | product qv
function dtmQVTrack(productCodeArray,category,brand){
	if(typeof(_satellite) != "undefined"){
		_satellite.track('cpj_qw');
  	}
	
	digitalData.cpj = {
    		product : {
    				     id  :  productCodeArray,
    		      category   :  category	
    	      },
    	    brand : { 
    		   name : brand
    		}      
    	}
	
}

	