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
			
			//TPR-672 START
			var promo_title='';
			var promo_id='';
			
			if($("#product_applied_promotion_title").val() && $("#product_applied_promotion_code").val() !=undefined)
			{
				
			promo_title=$("#product_applied_promotion_title").val().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
			promo_id=$("#product_applied_promotion_code").val().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
			}
			//TPR-672 END
			

			
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
				
				homePageTealium+='<script type="text/javascript"> var utag_data ={"site_region":"'+site_region+'","user_type":"'+user_type+'","user_id":"'+user_id+'","page_type":"'+pageTypeHome+'","page_name":"'+pageName+'","session_id":"'+session_id+'","visitor_ip":"'+visitor_ip+'","site_currency":"'+site_currency+'","site_section":"'+site_section+'","IA_company":"'+domain_name+'"}</script>';
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
						tealiumData += ',"product_unit_price":["'
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
						tealiumData += '"page_subcategory_name":"'
								+ $("#page_subcategory_name").val() + '",';
						tealiumData += '"page_subcategory_name_L3":"'
							+ $("#page_subcategory_name_L3").val() + '",';
						tealiumData += '"product_brand":["'
								+ $("#product_brand").val() + '"],';
						tealiumData += '"site_section_detail":"'
								+ $("#site_section_detail").val() + '",';
						tealiumData += '"product_category":["'
								+ $("#product_category").val() + '"],';
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
					&& pageType != 'profile' && pageType != 'wishlist'
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
				
                genericPageTealium+='<script type="text/javascript"> var utag_data ={"site_region":"'+site_region+'","user_type":"'+user_type+'","user_id":"'+user_id+'","page_type":"'+pageTypeGeneric+'","page_name":"'+pageName+'","session_id":"'+session_id+'","visitor_ip":"'+visitor_ip+'","site_currency":"'+site_currency+'","site_section":"'+site_section+'","IA_company":"'+domain_name+'"}</script>';
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
								tealiumData += ',"page_category_name":"'
										+ $("#page_category_name").val() + '",';
								tealiumData += '"site_section":"'
										+ $("#site_section").val() + '",';
								tealiumData += '"page_name":"'
									+ $("#page_name").val() + '",';
								tealiumData += '"categoryId":"'
									+ $("#categoryId").val() + '",';
								/*TPR-430*/
								tealiumData += '"product_category":"'
									+ $("#product_category").val() + '",';
								tealiumData += '"page_subcategory_name":"'
									+ $("#page_subcategory_name").val() + '",';
								tealiumData += '"page_subcategory_name_L3":"'
									+ $("#page_subcategory_name_L3").val() + '"}';
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
						tealiumData += ',"search_keyword":"'
								+ $("#search_keyword").val() + '",';
						tealiumData += '"searchCategory":"'
								+ $("#searchCategory").val() + '",';
						tealiumData += '"page_name":"'
							+ $("#page_name").val() + '",';
						tealiumData += '"search_results":"'
							+ $("#search_results").val() + '",';
						tealiumData += '"product_category":"'
							+ $("#product_category").val() + '",';
						tealiumData += '"page_subcategory_name":"'		// TPR-430
							+ $("#page_subcategory_name").val() +'",';
						tealiumData += '"page_subcategory_name_L3":"'		// TPR-430
							+ $("#page_subcategory_name_L3").val() +'"}';
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
						var tealiumData = "";
						tealiumData += ',"cart_total":"'
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
//						TPR-430
						tealiumData += '"page_subcategory_name":"'
							+ $("#page_subcategory_name").val() + '",';
					tealiumData += '"page_subcategory_name_L3":"'
						+ $("#page_subcategory_name_L3").val() + '",';
					tealiumData += '"product_category":["'
						+ $("#product_category").val() + '"],';
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
						tealiumData += ',"cart_total":"'
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
						tealiumData += '"page_subcategory_name":'
							+ $("#page_subcategory_name").val() + ',';
						tealiumData += '"page_subcategory_name_L3":"'
							+ $("#page_subcategory_name_L3").val() + '",';
						tealiumData += '"product_category":'
							+ $("#product_category").val() + ',';
						tealiumData += '"checkout_seller_ids":"'		//for TPR-429
							+ $("#checkoutSellerIDs").val() + '"}';
						data = data.replace("}<TealiumScript>", tealiumData);
						$("#tealiumHome").html(data);
					
						
					}
				});
				
				
				
			}

			/*TPR-648 start*/
			$('.shop-promos .promos a').click(function(){
				var brandText=$(this).text().replace(/ /g,'').toLowerCase()+ "_viewdetails";
				var brandClick="abcde_click";
				utag.link({"link_obj": this, "link_text": brandText, "event_type" : brandClick
						});
					
			});
			/*TPR-648 end*/
			
			
			/*TPR-657 starts*/
			$('.feedBack-block .search-feedback ul li').click(function(){				
				var msg="search_feedback_start";
				utag.link({"link_obj": this, "link_text": msg, "event_type" : msg
						});
					
			});
			$('.feedBack-block #feedBackFormNo .feed-back #submit_button').click(function(){				
				var msg="search_feedback_submit";
				utag.link({"link_obj": this, "link_text": msg, "event_type" : msg
						});
					
			});
			/*TPR-657 ends*/
		
			
			
		});

/*TPR-429 Start*/
/*TPR-689 (Part of TPR-429) Start*/
$(document).on('click','#buyNowButton',function(){
	var productSKU = $('.add_to_cart_form').find('input[type="hidden"]#productCodePost').val();
	utag.link({
		link_obj: this,
		link_text: 'buynow' ,
		event_type : 'buynow_winner_seller',
		product_sku : productSKU
	});
})
/*TPR-689 End*/


$(document).on('click','.btn-block.js-add-to-cart',function(){
	
	var eventType;
	var parentWrap = $(this).parents('div.pdp').attr('class');
	if (typeof parentWrap != 'undefined'){
		eventType = 'addtobag_winner_seller';
	}
	else{
		eventType = 'addtobag_other_seller';
	}
	
	/*var productSKU = $(this).parents('form').find('input[type="hidden"]#ussid').val();*/
	var productSKU = $('#productCodePost').val();
	
	utag.link({
		link_obj: this,
		link_text: 'addtobag' ,
		event_type : eventType ,
		product_sku : productSKU
	});
})


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
		"link_text": "delivery_choose_address_top_id1", "event_type" : "delivery_choose_address"
	});
	utag.link({
		"link_text": "delivery_choose_address_bottom_id2", "event_type" : "delivery_choose_address"
	});
});


$('#newAddressButtonUp').click(function(){
	utag.link({
		"link_text": "proceed_pay_top_id1", "event_type" : "proceed_pay"
	});
	utag.link({
			
		"link_text": "proceed_pay_bottom_id2", "event_type" : "proceed_pay"
	});
	
});

/*TPR-663 END*/

/*TPR-645 Start*/
$(document).on('click','.jqtree-title.jqtree_common',function(){
	var val= $(this).text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['-]/g,"");
	var name=$(this).parents('form').siblings('div').find('h4').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/'/g,"");
	var msg = name+"_"+val;
	utag.link({
		link_obj: this,
		link_text: msg ,
		event_type : 'search_filter_usage',
		search_filter : msg 
	});
})
/*TPR-645 End*/
