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
	var pageName=$('#pageName').val();
	var brandPagetype ='';
	if(pageName.includes("BrandStore")){
		brandPagetype = "brand";
	}
	
	var Promo_Id ="";
	if($("#product_applied_promotion_title").val() && $("#product_applied_promotion_code").val() !=undefined)
	{
		 var promo_title=$("#product_applied_promotion_title").val().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
		 var promo_id=$("#product_applied_promotion_code").val().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
		 Promo_Id = promo_title+":" + promo_id;
	}
    var script ="";
	var user_login_type = $('#userLoginType').val().trim();	
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
	digitalData = {
		page : {
			pageInfo : {
				pageName : pageName,
				domain : domain_name
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
	
	// For Homepage
	if (pageType == "homepage") {
		if(typeof _satellite !="undefined"){
		       _satellite.track('cpj_home_page');
		}
	}
	// For PDP
	if (pageType == "product"
			|| pageType == "/sellersdetailpage") {
		//TPR-6300
		var product_id = $("#product_id").val();
		var product_category = $("#product_category").val();
		var product_brand = $("#product_brand").val();
		var product_discount = $("#product_discount").val();
		var pincode = $('#pin').val();
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
		
	      if($("#out_of_stock").val() =='true' ){
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
		if(typeof _satellite !="undefined"){
		    _satellite.track('cpj_pdp');
		}
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
	
	// For PLP
	if (pageType == "category") {
		if(typeof _satellite !="undefined"){	
		   _satellite.track('cpj_category_pages');
		}
	}
		
	//Search
	if(pageType == "productsearch"){
		if(typeof _satellite !="undefined"){
		    _satellite.track('cpj_search_pages');
		}
		dtmSearchTags();
	}
	
	// Cart page
	if(pageType =="cart"){
		var pinCode = $('#pinId').val();
		if(typeof _satellite !="undefined"){
		    _satellite.track('cpj_cart_page');
		}
		digitalData.cpj = {
			product : {
				id : $("#product_id").val(),
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
	}
	
	// Checkout pages
	if(pageType =="multistepcheckoutsummary"){
		//
	}
	
	//brand pages
	if(brandPagetype != undefined  && brandPagetype == "brand"){
		 var brand = pageName.split('BrandStore-');
		 var brandName = brand[1].trim().toLowerCase();
		if(typeof _satellite !="undefined"){
			_satellite.track('cpj_brand_pages');
		}
		digitalData.cpj = {
				brand: {
					name : brandName
				}
		}
	}
	// checkout-login page
	if(pageType =="checkout-login"){
		if(typeof _satellite !="undefined"){
		    _satellite.track('cpj_checkout_login');
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
		var footer_link = $(this).find('a').html();
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
	var header_link = $(this).find('a').eq(0).text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"").trim();
	}
	digitalData = {
			header : {
				link : {
					name : header_link
				}
			}
		}
	});
	
	/*On Size selection | PDP #29*/ 
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
    
    // 404_error #8
    if(typeof digitalData == "undefined"){
    	if(typeof _satellite !="undefined"){
    	  _satellite.track('404_error');
    	}
    }
    
    
    // For product colour #30
    $(document).on('click',".color-swatch > li", function(){
    	var product_color = $(this).find('img').attr('title').toLowerCase();
    	if(typeof _satellite !="undefined"){
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
	       if(typeof _satellite !="undefined"){
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

function dtmSearchTags(){
	var offerCount = '';
	var newCount = '';
	if( $('.on-sale').length > 0 ){
		offerCount =	$('.on-sale').length ;
	}
	if( $('.new').length > 0 ){
		newCount =		$('.new').length ;
	}
	if(typeof digitalData.cpj != "undefined"){
         	digitalData.cpj = {
			 search: { 
				offersCount : offerCount,
				newCount    : newCount
			}
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
			channel_name = 'googleplus';
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

	/*PDP, quickview image hover*/
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
		}
	});	
	
	/*PDP thumbnail image click*/
	$(document).on("click",".product-info > .product-image-container > .productImageGallery .imageListCarousel .thumb",function(){
		var thumbnail_value = $(this).parent().attr('class');
		var thumbnail_type = $(this).find('img').attr('data-type');
		if(thumbnail_type == "image"){
			if (typeof _satellite != "undefined") {
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
	
	//TPR-6364 start | Track add and remove functionality from wishlist
	function dtmAddToWishlist(pagetype){
		if (typeof _satellite != "undefined") {
			_satellite.track('add_to_wishlist');
	    }
		   digitalData.cpj = {
			   product : {
				      id  :  $("#product_id").val(),
				 category :  $("#product_category").val()
			}
		}
		     digitalData.page = {
		    		wishList : {
		    		    location : pagetype 
		    		}
		    }
	}
	
	 function dtmRemoveFromWishlist(pagetype){
		if (typeof _satellite != "undefined") {
			_satellite.track('remove_from_wishlist');
	    }
		   digitalData.cpj = {
			   product : {
				      id  :  $("#product_id").val(),
				 category :  $("#product_category").val()
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
    