$(document).ready(
		

		function() {

            var UTAG_SCRIPT_PROD = "<script type='text/javascript'>(function(a,b,c,d){a='//tags.tiqcdn.com/utag/tataunistore/main/prod/utag.js';b=document;c='script';d=b.createElement(c);d.src=a;d.type='text/java'+c;d.async=true;a=b.getElementsByTagName(c)[0];a.parentNode.insertBefore(d,a);})();</script>";
			
			var UTAG_SCRIPT_DEV = "<script type='text/javascript'>(function(a,b,c,d){a='//tags.tiqcdn.com/utag/tataunistore/main/dev/utag.js';b=document;c='script';d=b.createElement(c);d.src=a;d.type='text/java'+c;d.async=true;a=b.getElementsByTagName(c)[0];a.parentNode.insertBefore(d,a);})();</script>";
			var session_id = ACC.config.SessionId;
			
			
			var visitor_ip = ACC.config.VisitorIp;
			
			var user_type = $.cookie("mpl-userType");
			var user_id = $.cookie("mpl-user");
			var site_region = 'en';
			var site_currency ='INR';
			var domain_name = document.domain;
			
			var pageType = $('#pageType').val();
			var pageName=$('#pageName').val();
			// TPR-668
			var user_login_type = $('#userLoginType').val().trim();
			
			//TPR-672 START
			var promo_title='';
			var promo_id='';
			
			if($("#product_applied_promotion_title").val() && $("#product_applied_promotion_code").val() !=undefined)
			{
				
			promo_title=$("#product_applied_promotion_title").val().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
			promo_id=$("#product_applied_promotion_code").val().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
			}
			//TPR-672 END
			
			//TPR-4726
			var offerCount = '';
			if( $('.on-sale').length > 0 ){
				offerCount =	$('.on-sale').length ;
			}
			
			var newCount = '';
			if( $('.new').length > 0 ){
				newCount =		$('.new').length ;
			}
		
			var onlineExclusive='';
			if($('.online-exclusive').length > 0){
				onlineExclusive = $('.online-exclusive').length ;
			}
			//Web Thumbnail Images
			var thumbnailImageCount=0;
			var pdp_video_product_id;
			$(".product-info > .product-image-container > .productImageGallery .imageListCarousel").find("li").each(function(){
				thumbnailImageCount++;
				if($(this).find('img').attr('data-type') == 'video'){
					pdp_video_product_id=$('#product_id').val();
				}
			})
			
			// Added for tealium
			if (pageType == "homepage") {
				
			
				// Added for tealium
				/*$
						.ajax({
							url : ACC.config.encodedContextPath
									+ "/getTealiumDataHome",
							type : 'GET',
							cache : false,
							success : function(data) {
								// console.log(data);
								$('#tealiumHome').html(data);
							}
						});*/
				// Added for TISPT-324
				
				
				
				var pageTypeHome = 'home';
				var site_section = 'home';
				var homePageTealium = '';
				//TPR-430
				var product_category = null;
				var page_subcategory_name = null;
				var page_subcategory_name_L3 = null;
				
				homePageTealium+='<script type="text/javascript"> var utag_data ={"site_region":"'+site_region+'","user_type":"'+user_type+'","user_login_type":"'+user_login_type+'","user_id":"'+user_id+'","page_type":"'+pageTypeHome+'","page_name":"'+pageName+'","product_category":"'+product_category+'","page_subcategory_name":"'+page_subcategory_name+'","page_subcategory_name_L3":"'+page_subcategory_name_L3+'", "session_id":"'+session_id+'","visitor_ip":"'+visitor_ip+'","site_currency":"'+site_currency+'","site_section":"'+site_section+'","IA_company":"'+domain_name+'"}</script>';
				var script="";
				if(domain_name =="www.tatacliq.com"){
					
					script=UTAG_SCRIPT_PROD;
				}
				else{
					
					script=UTAG_SCRIPT_DEV;
				}
				homePageTealium+=script;
				$('#tealiumHome').html(homePageTealium);
			}
			// Tealium end

			if (pageType == "product"
					|| pageType == "/sellersdetailpage") {
				// Added for tealium
				$.ajax({
					url : ACC.config.encodedContextPath
							+ "/getTealiumDataProduct",
					type : 'GET',
					cache : false,
					success : function(data) {

						var tealiumData = "";
						tealiumData += ',"user_login_type":"'	//TPR-668
							+ user_login_type + '",';
						tealiumData += '"product_unit_price":["'
								+ $("#product_unit_price").val() + '"],';
						tealiumData += '"site_section":"'
								+ $("#site_section").val() + '",';
						tealiumData += '"product_list_price":["'
								+ $("#product_list_price").val() + '"],';
						tealiumData += '"product_name":["'
								+ $("#product_name").val() + '"],';
						tealiumData += '"product_sku":["'
								+ $("#product_sku").val() + '"],';
						tealiumData += '"page_category_name":"'
								+ $("#page_category_name").val() + '",';
						tealiumData += '"page_section_name":"'
								+ $("#page_section_name").val() + '",';
						tealiumData += '"page_name":"' + $("#page_name").val()
								+ '",';
						tealiumData += '"product_id":["'
								+ $("#product_id").val() + '"],';
						//TPR-430 Start
						if($("#product_category").val() !=undefined || $("#product_category").val() !=null){ 
						tealiumData += '"product_category":'
							+ getListValue("product_category") + ',';            /*value passed as array instead of single string  INC_11511*/
							/*+ $("#product_category").val().replace(/_+/g, '_') + '",';*/
						}
						if($("#page_subcategory_name").val() !=undefined || $("#page_subcategory_name").val() !=null){ 
						tealiumData += '"page_subcategory_name":"'
								+ $("#page_subcategory_name").val().replace(/_+/g, '_') + '",';
						}
						if($("#page_subcategory_name_l3").val() !=undefined || $("#page_subcategory_name_l3").val() !=null){ 
						tealiumData += '"page_subcategory_name_L3":"'
							+ $("#page_subcategory_name_l3").val().replace(/_+/g, '_') + '",';
						}
						//TPR-430 End
						tealiumData += '"product_brand":["'
								+ $("#product_brand").val() + '"],';
						tealiumData += '"site_section_detail":"'
								+ $("#site_section_detail").val() + '",';
						//TPR-672 START
						tealiumData += '"promo_title":["'
							+promo_title+ '"],';
						tealiumData += '"promo_id":["'
							+promo_id+ '"],';
						//TPR-672 END
						
						//TPR-429 START
						tealiumData += '"buybox_seller_id":"'
							+ $("#pdpBuyboxWinnerSellerID").val() + '",';
						tealiumData += '"other_seller_ids":"'
							+ $("#pdpOtherSellerIDs").val() + '"}';
						//TPR-429 END
						data = data.replace("}<TealiumScript>", tealiumData);
						// console.log(data);
						
						$('#tealiumHome').html(data);
					}
				});
			}

			// Generic Page Script
			
			if (pageType != 'homepage' && pageType != 'product'
					&& pageType != '/sellersdetailpage' && pageType != 'productsearch'
					&& pageType != 'category' && pageType != 'cart'
					&& pageType != 'multistepcheckoutsummary'
					&& pageType != 'profile' 
					&& pageType != 'orderconfirmation'
					&& pageType != 'notfound'
					&& pageType != 'businesserrorfound'
					&& pageType != 'nonbusinesserrorfound'
					&& pageType != 'productsearch') {
				
				// Added for tealium
				/*$
						.ajax({
							url : ACC.config.encodedContextPath
									+ "/getTealiumDataGeneric",
							type : 'GET',
							data:'pageName='+pageName,
							cache : false,
							success : function(data) {
								// console.log(data);
								$('#tealiumHome').html(data);
							}
						});*/
			
				var pageTypeGeneric = 'generic';
				var site_section = pageName;
                var genericPageTealium = '';
                //TPR-430
            	if($("#product_category").val() !=undefined){
                var product_category = $("#product_category").val().replace(/_+/g, '_');
                }
            	if($("#product_category").val() == undefined){
                    var product_category = null;
                    }
            	var page_subcategory_name = null;
            	var page_subcategory_name_l3 = null;
				
                genericPageTealium+='<script type="text/javascript"> var utag_data ={"site_region":"'+site_region+'","user_type":"'+user_type+'","user_login_type":"'+user_login_type+'","user_id":"'+user_id+'","page_type":"'+pageTypeGeneric+'","page_name":"'+pageName+'","product_category":"'+product_category+'","page_subcategory_name":"'+page_subcategory_name+'","page_subcategory_name_L3":"'+page_subcategory_name_l3+'","session_id":"'+session_id+'","visitor_ip":"'+visitor_ip+'","site_currency":"'+site_currency+'","site_section":"'+site_section+'","IA_company":"'+domain_name+'"}</script>';
				var script="";
				if(domain_name =="www.tatacliq.com"){
					
					script=UTAG_SCRIPT_PROD;
				}
				else{
					
					script=UTAG_SCRIPT_DEV;
				}
				genericPageTealium+=script;
				$('#tealiumHome').html(genericPageTealium);
			}
			// Tealium end
			
			// Added for tealium
			if (pageType == "category") {
				// Added for tealium
				$
						.ajax({
							url : ACC.config.encodedContextPath
									+ "/getTealiumDataCategory",
							type : 'GET',
							cache : false,
							success : function(data) {
								// console.log(data);
								var tealiumData = "";
								tealiumData += ',"user_login_type":"'	//TPR-668
									+ user_login_type + '",';
								tealiumData += '"page_category_name":"'
										+ $("#page_category_name").val() + '",';
								tealiumData += '"site_section":"'
										+ $("#site_section").val() + '",';
								tealiumData += '"page_name":"'
									+ $("#page_name").val() + '",';
								tealiumData += '"categoryId":"'
									+ $("#categoryId").val() + '",';
								/*TPR-430 Start*/
								if($("#product_category").val() !=undefined || $("#product_category").val() !=null){ 
								tealiumData += '"product_category":'
									+ getListValue("product_category") + ',';                /*value passed as array instead of single string  INC_11511*/
									/*+ $("#product_category").val().replace(/_+/g, '_') + '",';*/
								}
								if($("#page_subcategory_name").val() !=undefined || $("#page_subcategory_name").val() !=null){ 
								tealiumData += '"page_subcategory_name":"'
									+ $("#page_subcategory_name").val().replace(/_+/g, '_') + '",';
								}
								if($("#page_subcategory_name_l3").val() !=undefined || $("#page_subcategory_name_l3").val() !=null){ 
								tealiumData += '"page_subcategory_name_L3":"'
									+ $("#page_subcategory_name_l3").val().replace(/_+/g, '_') + '"}';
								}
								/*TPR-430 End*/
								data = data.replace("}<TealiumScript>", tealiumData);
								$('#tealiumHome').html(data);
							}
						});
			}
			// Tealium end
			
			//Search
			if(pageType == "productsearch"){
				$
				.ajax({
					url : ACC.config.encodedContextPath
							+ "/getTealiumDataSearch",
					type : 'GET',
					cache : false,
					success : function(data) {
						// console.log(data);
						var tealiumData = "";
						//TPR-430
						var product_category = null;
						var page_subcategory_name = null;
						var page_subcategory_name_L3 = null;
						tealiumData += ',"user_login_type":"'	//TPR-668
							+ user_login_type + '",';
						tealiumData += '"search_keyword":"'
								+ $("#search_keyword").val() + '",';
						tealiumData += '"searchCategory":"'
								+ $("#searchCategory").val() + '",';
						tealiumData += '"page_name":"'
							+ $("#page_name").val() + '",';
						tealiumData += '"search_results":"'
							+ $("#search_results").val() + '",';
						tealiumData += '"search_type":"'		// TPR-666
							+ $("#search_type").val() + '",';
						tealiumData += '"out_of_stock_count":"'		// TPR-4722
							+ $("#out_of_stock_count").val() + '",';
						if(offerCount != undefined || offerCount !=null){ 
						tealiumData += '"offer_product_count":"'		// TPR-4726
							+ offerCount + '",';
						}
						if(newCount != undefined || newCount !=null){ 
							tealiumData += '"new_product_count":"'		// TPR-4726
								+ newCount + '",';
							}
						if(onlineExclusive != undefined || onlineExclusive !=null){ 
							tealiumData += '"onlineExclusive_product_count":"'		// TPR-4726
								+ onlineExclusive + '",';
							}
						//TPR-430 Start
						tealiumData += '"product_category":"'
							+ product_category + '",';
						tealiumData += '"page_subcategory_name":"'		
							+ page_subcategory_name +'",';
						tealiumData += '"page_subcategory_name_L3":"'		
							+ page_subcategory_name_L3 +'"}';
						data = data.replace("}<TealiumScript>", tealiumData);
						$('#tealiumHome').html(data);
					}
				});
				
			}
			
			if(pageType =="cart"){
				$
				.ajax({
					url : ACC.config.encodedContextPath
							+ "/getTealiumDataCart",
					type : 'GET',
					cache : false,
					success : function(data) {
						var qtyUpdated;
						if(window.sessionStorage.getItem("qtyUpdate")!=null){
							qtyUpdated=window.sessionStorage.getItem("qtyUpdate");
						}
						else{
							qtyUpdated=false;
						}
						var tealiumData = "";
						tealiumData += ',"user_login_type":"'	//TPR-668
							+ user_login_type + '",';
						tealiumData += '"cart_total":"'
								+ $("#cart_total").val() + '",';
						tealiumData += '"product_unit_price":'
								+ $("#product_unit_price").val() + ',';
						tealiumData += '"product_list_price":'
							+ $("#product_list_price").val() + ',';
						tealiumData += '"product_name":'
							+ $("#product_name").val() + ',';
						tealiumData += '"product_quantity":'
							+ $("#product_quantity").val() + ',';
						tealiumData += '"adobe_product":"'
							+ $("#adobe_product").val() + '",';
						tealiumData += '"product_sku":'
							+ $("#product_sku").val() + ',';
						tealiumData += '"product_id":'
							+ $("#product_id").val() + ',';
						tealiumData += '"product_brand":'
							+ $("#product_brand").val() + ',';				
						tealiumData += '"product_quantity_update":'
							+ qtyUpdated + ',';			
						tealiumData += '"checkout_seller_ids":"'		//for TPR-429
							+ $("#checkoutSellerIDs").val() + '",';
						//TPR-430 Start
						if($("#product_category").val() !=undefined || $("#product_category").val() !=null){ 
						tealiumData += '"product_category":'
							+ getListValue("product_category") + ',';	/*value passed as array instead of single string  INC_11511*/
							/*+ $("#product_category").val().replace(/_+/g, '_') + '",';*/
						}
						if($("#page_subcategory_name").val() !=undefined || $("#page_subcategory_name").val() !=null){ 
						tealiumData += '"page_subcategory_name":"'
							+ $("#page_subcategory_name").val().replace(/_+/g, '_') + '",';
						}
						if($("#page_subcategory_name_l3").val() !=undefined || $("#page_subcategory_name_l3").val() !=null){ 
					tealiumData += '"page_subcategory_name_L3":"'
						+ $("#page_subcategory_name_l3").val().replace(/_+/g, '_') + '"}';
						}
					//TPR-430 End
						data = data.replace("}<TealiumScript>", tealiumData);
						$('#tealiumHome').html(data);
					}
				});
			}
			
			if(pageType =="multistepcheckoutsummary"){
				var checkoutPageName=$('#checkoutPageName').val();
				$
				.ajax({
					url : ACC.config.encodedContextPath
							+ "/getTealiumDataCheckout",
					type : 'GET',
					data:'checkoutPageName='+checkoutPageName,
					cache : false,
					success : function(data) {
						var tealiumData = "";
						tealiumData += ',"user_login_type":"'	//TPR-668
							+ user_login_type + '",';
						tealiumData += '"cart_total":"'
								+ $("#cart_total").val() + '",';
						tealiumData += '"product_unit_price":'
								+ $("#product_unit_price").val() + ',';
						tealiumData += '"product_list_price":'
							+ $("#product_list_price").val() + ',';
						tealiumData += '"product_name":'
							+ $("#product_name").val() + ',';
						tealiumData += '"product_quantity":'
							+ $("#product_quantity").val() + ',';
						tealiumData += '"adobe_product":"'
							+ $("#adobe_product").val() + '",';
						tealiumData += '"product_sku":'
							+ $("#product_sku").val() + ',';
						tealiumData += '"product_id":'
							+ $("#product_id").val() + ',';
						tealiumData += '"product_brand":'
							+ $("#product_brand").val() + ',';
						//TPR-430 Start
						if($("#product_category").val() !=undefined || $("#product_category").val() !=null){ 
						tealiumData += '"product_category":'
							+ getListValue("product_category") + ',';                /*value passed as array instead of single string  INC_11511*/
						/*	+ $("#product_category").val().replace(/_+/g, '_') + '",';*/
						}
						if($("#page_subcategory_name").val() !=undefined || $("#page_subcategory_name").val() !=null){ 
						tealiumData += '"page_subcategory_name":"'
							+ $("#page_subcategory_name").val().replace(/_+/g, '_') + '",';
						}
						if($("#page_subcategory_name_l3").val() !=undefined || $("#page_subcategory_name_l3").val() !=null){ 
					tealiumData += '"page_subcategory_name_L3":"'
						+ $("#page_subcategory_name_l3").val().replace(/_+/g, '_') + '",';
						}
					//TPR-430 End
						tealiumData += '"checkout_seller_ids":"'		//for TPR-429
							+ $("#checkoutSellerIDs").val() + '"}';
						data = data.replace("}<TealiumScript>", tealiumData);
						$("#tealiumHome").html(data);
					
						
					}
				});
				
				
				
			}

			
			//tpr-668  --for order page
			
			if(pageType =="orderconfirmation"){
				$
				.ajax({
					url : ACC.config.encodedContextPath
							+ "/getTealiumDataOrder",
					type : 'GET',
					cache : false,
					success : function(data) {
						var tealiumData = "";
						tealiumData += ',"user_login_type":"'	//TPR-668
							+ user_login_type + '",';
					    tealiumData += '"order_id":"'
								+ $('#orderIDString').val() + '",';
					     tealiumData += '"order_subtotal":"'
							+ $('#orderSubTotal').val() + '",';
					     tealiumData += '"order_date":"'
						+ $("#orderDate").val() + '",';
					     tealiumData += '"product_quantity":'
						+ $("#product_quantity").val() + ',';
						 tealiumData += '"order_payment_type":"'
							+ $("#order_payment_type").val() + '",';
				        tealiumData += '"product_sku":'
						+ $("#product_sku").val() + ',';
					    tealiumData += '"product_id":'
							+ $("#product_id").val() + ',';
						tealiumData += '"product_brand":'
							+ $("#product_brand").val() + ',';
						 tealiumData += '"order_shipping_charges":'
								+ $('#order_shipping_charges').val() + ',';
						 tealiumData += '"order_tax":"'
								+ $('#order_tax').val() + '",';
						 tealiumData += '"transaction_id":"'
								+ $('#transaction_id').val() + '",';
						 tealiumData += '"order_total":"'
								+ $('#order_total').val() + '",';
						 tealiumData += '"order_discount":"'
								+ $('#order_discount').val() + '",';
						 tealiumData += '"order_currency":"'
								+ $('#order_currency').val() + '",';
						 tealiumData += '"product_unit_price":'
								+ $('#product_unit_price').val() + ',';
						 tealiumData += '"product_list_price":'
								+ $('#product_list_price').val() + ',';
						 tealiumData += '"product_name":'
								+ $('#product_name').val() + ',';
						 tealiumData += '"order_shipping_modes":'
								+ $('#order_shipping_modes').val() + ',';
						 
						//TPR-430 Start
						 if($("#product_category").val() !=undefined || $("#product_category").val() !=null){ 
						tealiumData += '"product_category":'
							+ getListValue("product_category") + ',';                             /*value passed as array instead of single string  INC_11511*/
							/*+ $("#product_category").val().replace(/_+/g, '_') + '",';*/
						}
						if($("#page_subcategory_name").val() !=undefined || $("#page_subcategory_name").val() !=null){ 
						tealiumData += '"page_subcategory_name":"'
							+ $("#page_subcategory_name").val().replace(/_+/g, '_') + '",';
						}
						if($("#page_subcategory_name_l3").val() !=undefined || $("#page_subcategory_name_l3").val() !=null){ 
					   tealiumData += '"page_subcategory_name_L3":"'
						+ $("#page_subcategory_name_l3").val().replace(/_+/g, '_') + '",';
						}
					//TPR-430 End
						tealiumData += '"checkout_seller_ids":"'		//for TPR-429
							+ $("#checkoutSellerIDs").val() + '"}';
						data = data.replace("}<TealiumScript>", tealiumData);
						$("#tealiumHome").html(data);
					
					}
				});
			}
			//for order page
			
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
			
			/*TPR-648 start*/
			$('.shop-promos .promos a').click(function(){
				var brandText=$(this).text().replace(/ /g,'').toLowerCase()+ "_viewdetails";
				var brandClick="abcde_click";
				utag.link({"link_obj": this, "link_text": brandText, "event_type" : brandClick
						});
					
			});
			/*TPR-648 end*/
			
			
			/*TPR-657 starts*/ /* TPR-4730 SERP*/
			$('.feedbackYes.blue').click(function(){				
				var msg="search_feedback_yes";
				utag.link({"link_text": msg, "event_type" : msg
						});
					
			});
			
			$('.feedbackNo.orange').click(function(){				
				var msg="search_feedback_no";
				utag.link({"link_text": msg, "event_type" : msg
						});
					
			});	
			
			/*$('.feedBack-block #feedBackFormNo .feed-back #submit_button').click(function(){				
				var msg="search_feedback_no_submit";
				utag.link({"link_obj": this, "link_text": msg, "event_type" : msg
						});
					
			});*/
			/*TPR-657 ends*/
			
			/*TPR- 659 starts*/
			$(document).on("click",".view-cliq-offers",function(){
		//		alert("viewcliq......")
				utag.link(
		    			{link_obj: this, link_text: 'home_top_deal_view_offers' , event_type : 'home_top_deal_view_offers'}
		    			);
				});
			/*TPR- 659  ends*/
		});

/*TPR-429 Start*/
/*TPR-689 (Part of TPR-429) Start*/
/*$(document).on('click','#buyNowButton',function(){
	var productSKU = $('.add_to_cart_form').find('input[type="hidden"]#productCodePost').val();
	utag.link({
		link_obj: this,
		link_text: 'buynow' ,
		event_type : 'buynow_winner_seller',
		product_sku : productSKU
	});
})
TPR-689 End


$(document).on('mousedown','.btn-block.js-add-to-cart',function(){
	
	var eventType;
	var parentWrap = $(this).parents('div.pdp').attr('class');
	if (typeof parentWrap != 'undefined'){
		eventType = 'addtobag_winner_seller';
	}
	else{
		if($(this).attr('id').indexOf("Quick") != -1){
			eventType = 'addtobag_winner_seller';
		}
		else{
			eventType = 'addtobag_other_seller';
		}
	}
	
	var productSKU = $(this).parents('form').find('input[type="hidden"]#ussid').val();
	var productSKU = $('#productCodePost').val();

	if($(this).attr('id').toLowerCase().indexOf("buynow") == -1){
		utag.link({
			link_obj: this,
			link_text: 'addtobag' ,
			event_type : eventType ,
			product_sku : productSKU
		});
	}
})*/


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
}

/*TPR-429 End*/

/*TPR-663 START*/
$('#deliveryMethodSubmit').click(function(){
	utag.link({
		"link_text": "delivery_choose_address_bottom_id2", "event_type" : "delivery_choose_address"
	});
});

$('#deliveryMethodSubmitUp').click(function(){
	utag.link({
		"link_text": "delivery_choose_address_top_id1", "event_type" : "delivery_choose_address"
	});
});

$('#deliveryAddressSubmitUp').click(function(){
	utag.link({
		"link_text": "proceed_pay_top_id1", "event_type" : "proceed_pay"
	});
});

$('#deliveryAddressSubmit').click(function(){
	utag.link({
		"link_text": "proceed_pay_bottom_id2", "event_type" : "proceed_pay"
	});
});



/*$('#newAddressButtonUp').click(function(){
	utag.link({
		"link_text": "proceed_pay_top_id1", "event_type" : "proceed_pay"
	});
	utag.link({
			
		"link_text": "proceed_pay_bottom_id2", "event_type" : "proceed_pay"
	});
	
});*/

/*TPR-663 END*/

/*TPR-645 Start*/
$(document).on('click','.jqtree-title.jqtree_common',function(){
	var filter_value= $(this).text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['-]/g,"");
	var filter_type=$(this).parents('form').siblings('div.facet-name.js-facet-name.facet_desktop').find('h3').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/'/g,"");
	//var msg = name+"_"+val;
	utag.link({
		link_obj: this,
		link_text: 'search_filter_applied' ,
		event_type : 'search_filter_applied',
		"filter_type" : filter_type,
		"filter_value" : filter_value
	});
	restrictionFlag='true';
})
/*TPR-645 End*/

/*TPR-650 Start*/
$(document).on('mousedown','.owl-prev,.owl-next',function(e){
	var direction='';
	var title='';
	if($(this).parents('.owl-carousel').parents('.trending').length > 0){
		title=$(this).parents('.owl-carousel').parents('.trending').find('h2>span').text().trim();
	}
	else{
		title=$(this).parents('.owl-carousel').parent('div').find('h2').text().trim();
	}
	
	if($(e.currentTarget).hasClass('owl-next')){
		direction="Next";
	}
	else{
		direction="Previous";
	}
	title = title.toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	direction = direction.toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	var msg = (title+"_"+direction);
	if(title){
		utag.link({
			link_obj: this,
			link_text: msg,
			event_type : title+"_navigation_click"
		});
	}
})

$(document).on("click", ".home-brands-you-love-carousel-brands", function() {
	var text = $(this).find('img').attr('alt');
	if(text != ""){
		text = $(this).find('img').attr('alt').toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
		//TISQAEE-59
		var header = $('#brandsYouLove').find('h2').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
		utag.link({
			link_obj: this,
			link_text: header+":"+text,
			event_type : header+"_click"
		});
	}
})

/*TPR-650 End*/

/*TPR-691 Starts(Few Functionality)*/
			/*live chat*/
			$(document).on("click","#chatMe",function(){
				utag.link(
						{link_obj: this,link_text: 'support_chat_click_on_chat', event_type : 'support_chat_click'}
						);
			});
			/*call*/
			$(document).on("click", "#callMe", function(e) {
				utag.link(
						{link_obj: this,link_text: 'support_call_click_on_call', event_type : 'support_call_click'}
						);
			});
			/*connect chat*/
			$(document).on("click", "#submitC2C", function(e) {
				//alert("hi");
				  var selectedOption = $('select[name="reason"] option:selected').val();
				  //alert("You have selected the country - " + selectedOption);
				  
				  if(selectedOption=="Order enquiry/ Place or cancel order")
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Order_enquiry_Place_or_cancel_order_connect', event_type : 'support_chat_click'}
							  );
					  }
				  else if(selectedOption=="Return  Product")
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Return_Product_connect', event_type : 'support_chat_click'}
							  );
					  }
				  else if(selectedOption=="Refund Enquiry")
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Refund_Enquiry_connect', event_type : 'support_chat_click'}
							  );
					  }
				  else if(selectedOption=="Product Information")
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Product_Information_connect', event_type : 'support_chat_click'}
							  );
					  }
				  else
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Other_Assistance_connect', event_type : 'support_chat_click'}
							  );
					  }
				});
			
			/*cancel call*/
			$(document).on("mousedown", ".bcancel", function(e) {
				var id = $(this).attr('id');
			    //alert(id);
				var selectedOption = $('select[name="reason"] option:selected').val();
				//alert("You have selected the country - " + selectedOption);
				//alert("hi");
				if(id=="call"){
					if(selectedOption=="Order enquiry/ Place or cancel order")
					{
						utag.link(
								{link_obj: this,link_text: 'support_call_Order_enquiry_Place_or_cancel_order_cancel', event_type : 'support_call_click'}
						  	);
					}
					else if(selectedOption=="Return  Product")
					{
						utag.link(
								{link_obj: this,link_text: 'support_call_Return_Product_cancel', event_type : 'support_call_click'}
						  	);
					}
					else if(selectedOption=="Refund Enquiry")
					{
						utag.link(
								{link_obj: this,link_text: 'support_call_Refund_Enquiry_cancel', event_type : 'support_call_click'}
						  	);
					}
					else if(selectedOption=="Product Information")
					{
						utag.link(
								{link_obj: this,link_text: 'support_call_Product_Information_cancel', event_type : 'support_call_click'}
							);
					}
					else
					{
						utag.link(
								{link_obj: this,link_text: 'support_call_Other_Assistance_cancel', event_type : 'support_call_click'}
						  	);
					}
				  } 
				if(id=="chat"){
					if(selectedOption=="Order enquiry/ Place or cancel order")
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Order_enquiry_Place_or_cancel_order_cancel', event_type : 'support_chat_click'}
							  );
					  }
				  else if(selectedOption=="Return  Product")
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Return_Product_cancel', event_type : 'support_chat_click'}
							  );
					  }
				  else if(selectedOption=="Refund Enquiry")
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Refund_Enquiry_cancel', event_type : 'support_chat_click'}
							  );
					  }
				  else if(selectedOption=="Product Information")
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Product_Information_cancel', event_type : 'support_chat_click'}
							  );
					  }
				  else
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Other_Assistance_cancel', event_type : 'support_chat_click'}
							  );
					  }
					
					}
				});
			
			/*call generate OTP*/
			$(document).on("click","#generateOTPBtn",function(){
				//alert("hi");
				  var selectedOption = $('select[name="reason"] option:selected').val();
				  //alert("You have selected the country - " + selectedOption);
				  
				  if(selectedOption=="Order enquiry/ Place or cancel order")
				  {
					  utag.link(
						  {link_obj: this,link_text: 'support_call_Order_enquiry_Place_or_cancel_order_generate_otp', event_type : 'support_call_click'}
						  );
				  }
			     else if(selectedOption=="Return  Product")
				  {
			    	 utag.link(
						  {link_obj: this,link_text: 'support_call_Return_Product_generate_otp', event_type : 'support_call_click'}
						  );
				  }
			     else if(selectedOption=="Refund Enquiry")
				  {
			    	 utag.link(
						  {link_obj: this,link_text: 'support_call_Refund_Enquiry_generate_otp', event_type : 'support_call_click'}
						  );
				  }
			     else if(selectedOption=="Product Information")
				  {
			    	 utag.link(
						  {link_obj: this,link_text: 'support_call_Product_Information_generate_otp', event_type : 'support_call_click'}
						  );
				  }
			     else
				  {
			    	 utag.link(
						  {link_obj: this,link_text: 'support_call_Other_Assistance_generate_otp', event_type : 'support_call_click'}
						  );
				  }
				  
			});
			/*call validate OTP*/
			$(document).on("click","#validateOTPBtn",function(){
				//alert("hi");
				  var selectedOption = $('select[name="reason"] option:selected').val();
				  //alert("You have selected the country - " + selectedOption);
				  
				  if(selectedOption=="Order enquiry/ Place or cancel order")
				  {
					  utag.link(
						  {link_obj: this,link_text: 'support_call_Order_enquiry_Place_or_cancel_order_validate_otp', event_type : 'support_call_click'}
						  );
				  }
			     else if(selectedOption=="Return  Product")
				  {
			    	 utag.link(
						  {link_obj: this,link_text: 'support_call_Return_Product_validate_otp', event_type : 'support_call_click'}
						  );
				  }
			     else if(selectedOption=="Refund Enquiry")
				  {
			    	 utag.link(
						  {link_obj: this,link_text: 'support_call_Refund_Enquiry_validate_otp', event_type : 'support_call_click'}
						  );
				  }
			     else if(selectedOption=="Product Information")
				  {
			    	 utag.link(
						  {link_obj: this,link_text: 'support_call_Product_Information_validate_otp', event_type : 'support_call_click'}
						  );
				  }
			     else
				  {
			    	 utag.link(
						  {link_obj: this,link_text: 'support_call_Other_Assistance_validate_otp', event_type : 'support_call_click'}
						  );
				  }
			});
			
			/*TPR-691 Ends(Few Functionality)*/
			
			/*TPR-675 starts 2nd part*/
			$(document).on("click", ".gig-comments-share-provider-checkbox.gig-comments-checkbox", function(e) {
				var selectedOption = $(this).attr('data-provider'); 
				//alert(selectedOption)
				 if(selectedOption=="twitter")
				  {
					 utag.link({link_text: 'review_social_share_twitter' , event_type : 'review_social_share'});
				  }
			     else if(selectedOption=="facebook")
				  {
			    	 utag.link({link_text: 'review_social_share_facebook' , event_type : 'review_social_share'});
				  }
				});
			/*TPR-675 ends 2nd part*/ 
			
			

			
			
			/**TPR-654*---ShopByDepartment	*/
			$(document).on('click', 'nav ul li.ShopByDepartmentone div a', function(e){
			//$("nav ul li.ShopByDepartmentone div a").click(function(e) {
				var that = $(this);
				var target = $(e.target);
				var hAr = "";
				var x= $.trim($(".toggle.shop_dept").text().replace(/[\t\n\']+/g,' '));
				x = x.replace(" ","").toLowerCase();
				x = x.replace(" ","_");
				//alert(x);
				
				var y = $.trim($(this).text().replace(/[\t\n\'\-]+/g,' ')).toLowerCase();
				y = y.replace(/[\s\-]/g,"");
				//alert(y);
				var navigationClick= "top_navigation_click";
				
				if($(target).parent().hasClass("toggle departmenthover L1"))
				          {
					        hAr+= x+">>>>"+ ">>>>"+y;
					utag.link({"link_text":x+"_"+y,"event_type" : navigationClick});
					//alert(hAr);
				          }
				
				if($(target).parent().hasClass("toggle L2"))
				{
					var itsParentL1 = $.trim(that.parents().siblings(".departmenthover.L1").text().replace(/[\t\n\']+/g,' ')).toLowerCase();
					itsParentL1 = itsParentL1.replace(/[\s\-]/g,"");
					hAr+= x+">>>"+">>"+itsParentL1 +" >> "+ y;
					utag.link({"link_text":x+"_"+itsParentL1+"_"+y, "event_type" : navigationClick});
				}
				
				if(that.parent().hasClass("toggle L3")){
					var itsParentL1 =$.trim(that.parents().siblings(".departmenthover.L1").text().replace(/[\t\n\'\-]+/g,' ')).toLowerCase();
					itsParentL1 = itsParentL1.replace(/[\s\-]/g,"");
					var itsParentL2 = $.trim(that.parent().parent().prevAll("li.short.words:first").text().replace(/[\t\n\'\-]+/g,' ')).toLowerCase();
					itsParentL2 = itsParentL2.replace(/[\s\-]/g,"");
					console.log("dress")
					
					hAr+= x+">>>>" +">>>>>"+itsParentL1 +" >> "+ ">>"+itsParentL2 +$(this).text();
					utag.link({"link_text":x+"_"+itsParentL1+"_"+itsParentL2+"_"+y,"event_type" : navigationClick});
				}
				//console.log(hAr);
				//alert(hAr);
			});
			
			/*TPR-654*/	
						
			
			$(document).on('click', 'nav ul li.ShopByBrand ul li.short.images a', function(e){
				 var navigationClick= "top_navigation_click";
				 var lastItem = $.trim($(this).text().replace(/[\t\n\'\-]+/g,' ')).toLowerCase();
				 lastItem = lastItem.replace(/[\s]/g,"");
				 //alert(lastItem);
				
				 var parentItem = '';
				 if ($(this).parents().hasClass('lazy-brands')) {
					 var parentObj = $(this).closest('.lazy-brands');
					 if (parentObj.find('.toggle.brandClass').length) {
						 parentItem = $.trim(parentObj.find('.toggle.brandClass').text().replace(/[\t\n\'\-]+/g,' ')).toLowerCase(); 
					 }
					 else if(parentObj.find('.toggle.A-ZBrands').length){
						 parentItem = $.trim(parentObj.find('.toggle.A-ZBrands').text().replace(/[\t\n\'\-]+/g,' ')).toLowerCase(); 
					 }
					 parentItem = parentItem.replace(/[\s]/g,"");
					 //alert(parentItem);
				 }
				 
				 var grandParentItem = $.trim($('div.toggle.shop_brand').text().replace(/[\t\n\']+/g,' ')).toLowerCase();
				 grandParentItem = grandParentItem.replace(" ","").toLowerCase();
				 grandParentItem = grandParentItem.replace(" ","_");
				 //alert(grandParentItem);
				 
				 utag.link({"link_text":grandParentItem+"_"+parentItem+"_"+lastItem,"event_type" : navigationClick});
			});

			 $('nav ul li.ShopByBrand div a').on('click', function(){
				 var navigationClick= "top_navigation_click";
				 var lastItem = $.trim($(this).text().replace(/[\t\n\'\-]+/g,' ')).toLowerCase();
				 lastItem = lastItem.replace(/[\s]/g,"");
				 //alert(lastItem);
		
				 var parentItem = $.trim($('div.toggle.shop_brand').text().replace(/[\t\n\']+/g,' ')).toLowerCase();
				 parentItem = parentItem.replace(" ","").toLowerCase();
				 parentItem = parentItem.replace(" ","_");
				 //alert(parentItem);
				 
				 utag.link({"link_text":parentItem+"_"+lastItem,"event_type" : navigationClick});
			 }); 
			 
			/* TPR-4729 | SERP |Need help starts*/
			 
			 $(document).on('click',"#up",function(){
				 utag.link({link_text: 'need_help_clicked', event_type : 'need_help_clicked'});
			 });
			 /* TPR-4729 | SERP  ends*/ 

			 /* TPR-4724 |Add to Bag |serp*/ 
			 $(document).on('click','.serp_add_to_cart_form .js-add-to-cart',function(event){
				 var productId="productCodePost";
				 var product = $("#"+$(this).closest('form').attr("id")+" :input[name='" + productId +"']").val(); 
				 var productarray=[];
				     productarray.push(product);
				 if(typeof utag !="undefined"){
					utag.link({ link_text : "add_to_bag_serp" , event_type : "add_to_bag_serp" , product_sku : productarray });
				} 
			 });

/*TPR-4721, TPR-4706 | Sort By in SERP|PLP*/
$(document).on('change','#sortOptions1', function() {
  if(typeof utag !="undefined"){
	var value = $(this).find('option:selected').text().trim().toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	utag.link({ 
		link_text : "sort_by_"+value , 
		event_type : "sort_by_selected" , 
		"sort_by_value" : value 
	});
	restrictionFlag='true';
  } 
})


/*TPR-4719, TPR-4704 | Search Filter in SERP|PLP*/
//On click of view page size dropdown
$(document).on('change','#pageSizeOptions1', function() {
	restrictionFlag='true';
})
//On click of pagination
$(document).on('click','.pagination_a_link',function(){
	restrictionFlag='true';
})

window.onbeforeunload = function(event) {
	var pageType = $('#pageType').val();
	
	if(pageType == 'category' || pageType == 'productsearch'){
		if(restrictionFlag != 'true'){
			setupSessionValues();
		}
	}
}; 

var restrictionFlag='false';

function setupSessionValues(){
	if($('.bottom-pagination .facet-list.filter-opt').children().length > 0){
		var filterTypeList=[];
		var filterValueList=[];
		var sessionPageUrl=window.location.href;
		
		$('.bottom-pagination .facet-list.filter-opt').children().each(function(){
			filterTypeList.push($(this).children().eq(0).attr('class').toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,""));
			filterValueList.push($(this).children().eq(1).attr('value').toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,""))
		})
		if(filterValueList.length > 0 && filterTypeList.length > 0){
			if(typeof(utag) !="undefined"){
				utag.link({ 
					link_text : "final_filter_list" , 
					event_type : "final_filter_list" , 
					"filter_types_final":filterTypeList,
					"filter_values_final":filterValueList
				});
			}
		}
	}
}

//TPR-4725 | quickview
$(document).on('click','.quick-view-prod-details-link',function(){
	 if(typeof utag !="undefined"){
		 utag.link({ link_text : "quick_view_full_details_clicked" , event_type : "quick_view_full_details_clicked" });
	 }
})
//TPR-4727 | add to compare 2nd part
$(document).on('click','.compareRemoveLink',function(){
	if(typeof utag !="undefined"){
		 utag.link({ link_text : "add_to_compare_removed" , event_type : "add_to_compare_removed" });
	 }	
})
//TPR-4727 | add to compare 3rd part
$(document).on('click','#compareBtn',function(){
	if(typeof utag !="undefined"){
		 utag.link({ link_text : "compare_now_clicked" , event_type : "compare_now_clicked" });
	 }	
})
//TPR-4720 | Display First 5 Products  |serp 
function populateFirstFiveProductsSerp(){
	var count = 5; 
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
   if(typeof utag !="undefined"){
		 utag.link({ event_type: 'top_five_products_serp',serp_first_5_products : productArray });
	 }
}

$( window ).load(function() {
	if($('#pageType').val() == "productsearch"){
		populateFirstFiveProductsSerp();	
	}
	
	if($('#pageType').val() == "category"){
		populateFirstFiveProductsPlp();
	}
});

//TPR-4705 | Display first 5  products |plp 
function populateFirstFiveProductsPlp(){
	var count = 5; 
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
   if(typeof utag !="undefined"){
		 utag.link({ event_type :'top_five_products_plp',plp_first_5_products : productArray });
	 }	
}

//TPR-4725 |quick view  size|serp
$(document).on('click',"#quickViewVariant > li", function(){
	var product_size = $(this).find('a').html();
	utag.link({
		"link_text":"quick_view_size_"+product_size,
		"event_type":"quick_view_size_selected",
		"product_size":product_size
	});
})


/*Thumbnail tracking*/
//$(document).on("click",".product-image-container .imageListCarousel .thumb",function(){
$(document).on("click",".quick-view-popup > .product-image-container > .productImageGallery .imageListCarousel .thumb",function(){
	var thumbnail_value = $(this).parent().attr('class');
	var thumbnail_type = $(this).find('img').attr('data-type');
	utag.link({
		"link_text":"quickview_"+thumbnail_value+"_clicked",
		"event_type":"quickview_"+thumbnail_type+"_clicked",
		"thumbnail_value":thumbnail_value
	});
})

/*TPR-4725,4712 | colour selection on quickview*/
$(document).on('click',".color-swatch > li", function(){
	var page='';
	if($('#pageType').val() == "product"){
		page="pdp";
	}
	else{
		page="quickview";
	}
 	var product_color = $(this).find('img').attr('title').toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	utag.link({
 		"link_text":page+"_color_"+product_color,
 		"event_type":page+"_color_selected",
 		"product_color":product_color
 	});
 })
 

  /*TPR-4728 | add to compare page |1st part*/
 $('ul.jump li a').on('click',function(){
	 var categorySelected = $(this).text();
	 if(typeof utag !="undefined"){
		 utag.link({ link_text : categorySelected+"_clicked" , event_type :'jump_to_section_clicked' });
	 }	
 })
 /*TPR-4728 | add to compare page |2nd part*/
 $(document).on('click','.shop-now',function(){
	 if(typeof utag !="undefined"){
		 utag.link({ link_text : 'shop_now_clicked' , event_type :'shop_now_clicked' });
	 }
 })

 
 /*Out Of Stock During adding to bag*/
function errorAddToBag(errorMessage){
	utag.link({"error_type":errorMessage});
}

/*TPR-4687 | Broken Image*/
$(window).load(function() {
	tealiumBrokenImage();
});


function tealiumBrokenImage(){
	var brokenImageCount=0;	
	
	$('.mainContent-wrapper img').each(function(){
		var url = $(this).attr('src');
		if(url){
			if (!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0) {
			      // image is broken
			    	brokenImageCount++;
			    }
		}

	});
	if(brokenImageCount > 0){
		var msg = brokenImageCount+" broken_image_found";
		utag.link({ 
			error_type : msg
		});
	}
}
/*TPR-4728 | add to compare page  3rd part */
$(".product-tile.cboxElement").click(function(){
	 var url = $(this).attr('href').split("/")[2];
	var productID = url.split("p-");
	var productArray=[];
	productArray.push(productID[1]);
	if(typeof utag !="undefined"){
		 utag.link({ link_text : 'shop_now_pop_up' , shop_now_product_id : productArray});
	 }
	}) 
