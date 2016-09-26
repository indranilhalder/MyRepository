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
				
				homePageTealium+='<script type="text/javascript"> var utag_data ={"site_region":"'+site_region+'","user_type":"'+user_type+'","user_login_type":"'+user_login_type+'","user_id":"'+user_id+'","page_type":"'+pageTypeHome+'","page_name":"'+pageName+'","product_category":"'+product_category+'","page_subcategory_name":"'+page_subcategory_name+'","session_id":"'+session_id+'","visitor_ip":"'+visitor_ip+'","site_currency":"'+site_currency+'","site_section":"'+site_section+'","IA_company":"'+domain_name+'"}</script>';
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
						tealiumData += '"product_category":"'
							+ $("#product_category").val() + '",';
						tealiumData += '"page_subcategory_name":"'
								+ $("#page_subcategory_name").val() + '",';
						tealiumData += '"page_subcategory_name_l3":"'
							+ $("#page_subcategory_name_l3").val() + '",';
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
                //TPR-430
            	if($("#product_category").val() !=undefined){
                var product_category = $("#product_category").val();
                }
            	var page_subcategory_name = '';
            	var page_subcategory_name_l3 = '';
				
                genericPageTealium+='<script type="text/javascript"> var utag_data ={"site_region":"'+site_region+'","user_type":"'+user_type+'","user_login_type":"'+user_login_type+'","user_id":"'+user_id+'","page_type":"'+pageTypeGeneric+'","page_name":"'+pageName+'","product_category":"'+product_category+'","page_subcategory_name":"'+page_subcategory_name+'","page_subcategory_name_l3":"'+page_subcategory_name_l3+'","session_id":"'+session_id+'","visitor_ip":"'+visitor_ip+'","site_currency":"'+site_currency+'","site_section":"'+site_section+'","IA_company":"'+domain_name+'"}</script>';
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
								tealiumData += '"product_category":"'
									+ $("#product_category").val() + '",';
								tealiumData += '"page_subcategory_name":"'
									+ $("#page_subcategory_name").val() + '",';
								tealiumData += '"page_subcategory_name_l3":"'
									+ $("#page_subcategory_name_l3").val() + '"}';
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
						//TPR-430 Start
						tealiumData += '"product_category":"'
							+ $("#product_category").val() + '",';
						tealiumData += '"page_subcategory_name":"'		// TPR-430
							+ $("#page_subcategory_name").val() +'",';
						tealiumData += '"page_subcategory_name_l3":"'		// TPR-430
							+ $("#page_subcategory_name_l3").val() +'"}';
						//TPR-430 Start
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

						//TPR-430 Start
						tealiumData += '"product_category":'
							+ $("#product_category").val() + ',';
						tealiumData += '"page_subcategory_name":'
							+ $("#page_subcategory_name").val() + ',';
					tealiumData += '"page_subcategory_name_l3":'
						+ $("#page_subcategory_name_l3").val() + '}';
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
						tealiumData += '"product_category":'
							+ $("#product_category").val() + ',';
						tealiumData += '"page_subcategory_name":'
							+ $("#page_subcategory_name").val() + ',';
					tealiumData += '"page_subcategory_name_l3":'
						+ $("#page_subcategory_name_l3").val() + ',';
					//TPR-430 End
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
			
			/*TPR-694 starts*/
			$(document).on('click','.details',function(){

			
			//	alert("viewdetails......")
				utag.link({"link_obj": this, "link_text": 'product_offer_view_details', "event_type": 'product_offer_details'
							}); 
				});
			/*TPR-694 ends*/
			
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


$(document).on('mousedown','.btn-block.js-add-to-cart',function(){
	
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

/*TPR-650 Start*/
$(document).on('mousedown','.owl-prev,.owl-next',function(e){
	var direction='';
	var title='';
	if($(this).parents('.owl-carousel').parents('.trending').length > 0){
		title=$(this).parents('.owl-carousel').parents('.trending').find('h1>span').text().trim();
	}
	else{
		title=$(this).parents('.owl-carousel').parent('div').find('h1').text().trim();
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
		var header = $(this).parents('#brandsYouLove').find('h1').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
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
			/*cancel chat*/
			$(document).on("mousedown", ".bcancel", function(e) {
				
				var selectedOption = $('select[name="reason"] option:selected').val();
				//alert("You have selected the country - " + selectedOption);
				//alert("hi");
				
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
				  
				});
			
			/*cancel call*/
			$(document).on("mousedown", ".bcancel", function(e) {
				
				var selectedOption = $('select[name="reason"] option:selected').val();
				//alert("You have selected the country - " + selectedOption);
				//alert("hi");
				
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
