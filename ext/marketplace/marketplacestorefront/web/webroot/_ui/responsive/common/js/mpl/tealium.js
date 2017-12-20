var productIdArray = [];
$(document).ready(
		

		function() {
			//TPR-4731 changes start
			if($('body').hasClass('pageType-ContentPage') && $('#ia_category_code').val() != '' && $('#ia_category_code').val().toLowerCase().indexOf('mbh') != -1){
 				var pageName = $('#pageName').val();
 				var brandName='';
 				//PRDI-564 Fix
 				if(typeof pageName != "undefined" && pageName != ''){
 					pageName=pageName.split('BrandStore-');
 					if(typeof pageName[1] != "undefined" && pageName[1] != ''){
 						brandName = pageName[1].trim().toLowerCase();
 					}
 					else{
 						brandName =	pageName[0];
 					}
 					if(typeof(Storage) !== "undefined") {
 						localStorage.setItem("brandName", brandName);
 					}
 				}
 			}
 			else if($('#pageType').val() != "category"){
 				if(typeof(Storage) !== "undefined") {
 					if(localStorage.getItem("brandName") != null){
 						localStorage.removeItem("brandName");
 					}
 				}
 			}
			//TPR-4731 changes end
			tealiumCallOnPageLoad();//Moving tealium on-load call to function so that it can be re-used.
});

/*TPR-429 Start*/

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
	var msg='';
	
	if($(e.currentTarget).hasClass('owl-next')){
		direction="Next";
	}
	else{
		direction="Previous";
	}
	direction = direction.toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	
	var pageType=$('#pageType').val();
	//Changes for Analytics Data layer schema changes | PDP
	if(pageType=='product' || pageType=='/sellersdetailpage'){
		title="similar_products";
		msg = title;
	}
	else{
		if($(this).parents('.owl-carousel').parents('.trending').length > 0){
			title=$(this).parents('.owl-carousel').parents('.trending').find('h2>span').text().trim();
		}
		else{
			title=$(this).parents('.owl-carousel').parent('div').find('h2').text().trim();
		}
		title = title.toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
		msg = (title+"_"+direction);
	}
	
	var msg = (title+"_"+direction);
	if(title){
		utag.link({
			link_obj: this,
			link_text: msg,
			event_type : title+"_navigation_click"
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
					 utag.link({link_text: 'review_social_share_twitter' , event_type : 'review_social_share' ,product_id : productIdArray });
				  }
			     else if(selectedOption=="facebook")
				  {
			    	 utag.link({link_text: 'review_social_share_facebook' , event_type : 'review_social_share' ,product_id : productIdArray });
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

			 
/* Data Layer Schema Changes Starts*/

/*Thumbnail tracking*/
//$(document).on("click",".product-image-container .imageListCarousel .thumb",function(){
$(document).on("click",".product-info > .product-image-container > .productImageGallery .imageListCarousel .thumb",function(){
	var thumbnail_value = $(this).parent().attr('class');
	var thumbnail_type = $(this).find('img').attr('data-type');
	if(typeof(utag) != "undefined"){
	  utag.link({
		"link_text":"pdp_"+thumbnail_value+"_clicked",
		"event_type":"pdp_"+thumbnail_type+"_clicked",
		"thumbnail_value":thumbnail_value,
		product_id : productIdArray
	 });
	}
})

/*Product Specification*/
$(document).on("click",".nav-wrapper .nav.pdp",function(){
	if(typeof(utag) != 'undefined'){
		utag.link({"link_text":"product_specification", "event_type":"view_prod_specification",product_id : productIdArray});
	}
})

/*Out Of Stock During adding to bag*/
function errorAddToBag(errorMessage){
	if(typeof(utag) != "undefined"){
	   utag.link({"error_type":errorMessage});
	}
}

/*On Size selection | PDP*/
$(document).on('click',".variant-select > li", function(){
	var product_size = $(this).find('a').html();
	if(typeof(utag) != "undefined"){
	  utag.link({
		"link_text":"pdp_size_"+product_size,
		"event_type":"pdp_size_selected",
		"product_size":product_size,
		product_id : productIdArray
	});
  }
})


/*On Colour selection | PDP*/
$(document).on('click',".color-swatch > li", function(){
	var product_color = $(this).find('img').attr('title').toLowerCase();
	if(typeof(utag) != "undefined"){
	  utag.link({
		"link_text":"pdp_color_"+product_color,
		"event_type":"pdp_color_selected",
		"product_color":product_color,
		product_id : productIdArray
	  });
	}
})

/*TPR-4803| hot now | homepage*/
$(document).on('click','#ia_products_hot .hotShowHide',function(){
	if(typeof utag !="undefined"){
		 utag.link({ link_text : 'shop_the_hotlist_clicked' , event_type : 'shop_the_hotlist_clicked' });
	 }	
})


/*TPR-4803|Hot now home page,pdp recommendations generic method|*/
$(document).on("click",".trending .owl-stage .owl-item",function(){
	var productID='';
	var url = $(this).find('a').attr('href');
	if(typeof(url) != "undefined"){
		url = url.split('/p/');
		var productIDlist = url[1].split('/');
		productID=productIDlist[0]
	}
	var productArray=[];
	productArray.push(productID);
	
	
	if($('#pageType').val() == "product" || $('#pageType').val() == "/sellersdetailpage"){
	utag.link({
			link_text: "similar_products_clicked",
			event_type : "similar_products_clicked",
			similar_product_id : productArray
			
		});
      }
	
	if($('#pageType').val() == "homepage"){
		utag.link({
			link_text: "hotnow_product_clicked",
			event_type : "hotnow_product_clicked",
			product_id : productArray
			
		});
	}
})

/*PDP Add to bag & Buy Now*/
function utagAddProductToBag(triggerPoint,productCodeMSD){
	    var productCodeArray=[];
	    var productcode='';
		var pageName='';
		var pageType = $('#pageName').val();
		if( pageType == "Product Details"){
			pageName="pdp";
			productCodeArray.push(productCodeMSD);
		}
		if(  pageType == "View Seller Page"){
			pageName="pdp";
			 productCode= $('#productCode').val();
			 productCodeArray.push(productCode);
		}

		//PRDI-564 FIX
		if(typeof(utag) != "undefined"){
	          utag.link({
		          link_text: triggerPoint ,
		          event_type : triggerPoint+"_"+ pageName,
		          product_sku : productCodeArray,		// Product code passed as an array for Web Analytics - INC_11511  fix
		          product_id :  productCodeArray
	        });
		}
}


/*TPR-4740  Continue Shopping | Cart  */
$(document).on("click",".continue-shopping.desk-view-shopping",function() {
	utag.link({
		link_text: "continue_shopping_clicked",
		event_type : "continue_shopping_clicked"
	});
})

//TPR-4739 | Expresscheckout | cart
$(document).on("click","#expressCheckoutButtonId",function(){
	utag.link({
		link_text: "cart_express_checkout_button_start",
		event_type : "cart_express_checkout_button_start"
	});
})

 /*TPR-4745  | Add New Address | Checkout */
$(document).on('click','.pincode-button',function(){
	if(typeof(utag) != "undefined"){				 
    utag.link({ link_text : 'add_new_address_clicked' ,event_type : 'add_new_address_clicked'});
	}
})

/*TPR-4687 | Broken Image*/
$(window).load(function() {
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
		if(typeof utag !="undefined"){
			utag.link({ 
				error_type : msg
			});
		}
	}
});

/*TPR-4736|checkout|cart start*/
function pincodeServicabilityFailure(selectedPincode){
	if(typeof utag !="undefined")
		{
			//TPR-4736 | DataLAyerSchema changes | cart
			utag.link({

				"link_text": "cart_pincode_check_failure", 
				"event_type" : "cart_pincode_check_failure",
				"cart_pin_non_servicable" : selectedPincode
			});
		}
	
}

function pincodeServicabilitySuccess(selectedPincode){
	if(typeof utag !="undefined")
	{
		//TPR-4736 | DataLAyerSchema changes | cart
		utag.link({

			"link_text": "cart_pincode_check_success", 
			"event_type" : "cart_pincode_check_success",
			"cart_pin_servicable" : selectedPincode
		});
	}
}
/*TPR-4736|checkout|cart ends*/

/*Homepage changes starts*/	

/*TPR-4797 | Brand Studio changes Start*/
$(document).on("click", ".home-brands-you-love-carousel-brands", function() {
	var text = $(this).find('img').attr('alt');
	if(text != ""){
		text = $(this).find('img').attr('alt').toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
		//TISQAEE-59
		var header = $('#brandsYouLove').find('h2').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
		utag.link({
			link_text: text,
			event_type : header+"_brand_clicked",
		});
	}
})


$(document).on('click','.home-brands-you-love-side-image',function(){
	var productName=$(this).find('.product-name').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	var header = $('#brandsYouLove').find('h2').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	if(typeof utag !="undefined"){
		utag.link({ 
			link_text : productName, 
			event_type : header+"_clicked", 
		});
	} 
})

$(document).on('click','#brandsYouLove .visit-store',function(){
	var brandName ='';
	var header = $('#brandsYouLove').find('h2').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	$('#brandsYouLove').find('.home-brands-you-love-carousel-brands.item').each(function(){
		if($(this).parent().hasClass('center')){
			brandName = $(this).find('img').attr('alt').toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
		}
	})
	if(typeof utag !="undefined"){
		utag.link({ 
			link_text : brandName, 
			event_type : header+"_visit_store"
		});
	}
})
/*TPR-4797 | Brand Studio changes End*/

/*TPR-4798 | Top Deals changes start*/
$(document).on('click','.home-best-pick-carousel .owl-item a',function(){
	var productName = '';
	var header = $(this).parents('#bestPicks').children('h2').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	$(this).find('.short-info span').each(function(){
		if(productName == ''){
			productName = $(this).text().trim().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
		}
		else{
			productName = productName +"_"+ $(this).text().trim().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
		}
	})
	if(typeof utag !="undefined"){
		utag.link({ 
			link_text : productName, 
			event_type : header+"_image_clicked"
		});
	}
})
/*TPR-4798 | Top Deals changes end*/

/*TPR-4807 | Homepage banner ads*/
$(document).on('click','.home-rotatingImage .owl-item img',function(){
	var bannerName=$(this).attr('alt').trim().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	if(typeof utag !="undefined"){
		utag.link({ 
			link_text : 'banner_ad_'+bannerName, 
			event_type : 'banner_ad_clicked', 
		});
	}
})


 /*TPR-4805 | Inspire me changes start*/
 $(document).on('click','#showcase .button.maroon.btn-red',function(){
 	var header = $(this).parents('#showcase').children('h2').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		utag.link({ 
 			link_text : header+"_read_story_clicked", 
 			event_type : header+"_read_story_clicked"
 		});
 	}
 })
 
 $(document).on('click','#showcase .button.trending-button.btn-trans',function(){
 	var header = $(this).parents('#showcase').children('h2').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		utag.link({ 
 			link_text : header+"_read_more_clicked", 
 			event_type : header+"_read_more_clicked"
 		});
 	}
 })
 /*TPR-4805 | Inspire me changes end*/
 
 /*TPR-4800 | What to buy now changes start*/
 $(document).on('click','.home-product-you-care-carousel .owl-item a',function(){
 	var categoryName = $(this).find('.product-name > span').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	var header = $(this).parents('#productYouCare').children('h2').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		utag.link({ 
 			link_text : header+"_"+categoryName+"_clicked", 
 			event_type : header+"_"+categoryName+"_clicked"
 		});
 	}
 })
 /*TPR-4800 | What to buy now changes end*/
 
 
 /*TPR-4796 | Homepage banner carousel start*/
 $(document).on('click', '.home-rotatingImage .owl-dot', function(){
 	if(typeof utag !="undefined"){
 		utag.link({ 
 			link_text : 'home_carousel_clicked', 
 			event_type : 'home_carousel_clicked'
 		});
 	}
 })
 /*TPR-4796 | Homepage banner carousel end*/
 
 /*	TPR-4802|stay qued|homepage*/
 $(document).on('click','.qued-content a',function(){
 
 	if(typeof utag !="undefined"){
 		 utag.link({ link_text : 'read_the_story_clicked' , event_type : 'read_the_story_clicked'});
 	 }
 })
 
 /*	TPR-4801 | new in| homepage start*/
 $(document).on('click','.new_exclusive_viewAll', function(){
 	if(typeof utag !="undefined"){
 		 utag.link({ link_text : 'new_in_view_all_clicked' , event_type : 'new_in_view_all_clicked'});
 	 }	
 })
 
 $(document).on('click','#new_exclusive .newExclusiveElement a', function(){
 	var productName = $(this).find('.New_Exclusive_title').text();
 	if(typeof utag !="undefined"){
 		 utag.link({ link_text : productName , event_type : 'new_in_clicked' });
 	 }	
 	
 })
 /*	TPR-4801 | new in| homepage ends*/
 
 /*TPR-5062 Start*/
 $(document).on("click",".download-app",function(){
 	if(typeof utag !="undefined"){
 	utag.link({
 		"link_text": "download_app", "event_type" : "download_app_clicked"});
 	}
 });
 
 $(document).on("click",".store-locator-header",function(){
 	if(typeof utag !="undefined"){
 	utag.link({
 		"link_text": "our_stores", "event_type" : "our_stores_clicked"});
 	}
 });
 
 $(document).on("click",".tracklinkcls",function(){
 	if(typeof utag !="undefined"){
 	utag.link({
 		"link_text": "user_notifications", "event_type" : "user_notifications_clicked"});
 }
 });
 
 $(document).on("click",".logged_in.dropdown.ajaxloginhi",function(){
 if(typeof utag !="undefined"){
 utag.link({
 	"link_text": "my_account", "event_type" : "my_account_clicked"});
 }
 });
 
 $(document).on("click",".wishlist",function(){
 if(typeof utag !="undefined"){
 utag.link({
 	"link_text": "my_wishlists", "event_type" : "my_wishlists_clicked"});
 }
 });
 
 $(document).on("click",".bag",function(){
 if(typeof utag !="undefined"){
 utag.link({
 	"link_text": "my_bag", "event_type" : "my_bag_clicked"});
 }
 });
 /*TPR-5062 End*/
 
 /*TPR-4799 | offers page changes | Start*/
 /*Top Brands|Offer page*/
 $(document).on('click','.clp_top_brands .top_brands_section a',function(){
 	var sectionName = $(this).parents('.top_brands').find('span > span').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		 utag.link({ link_text : 'offers_'+sectionName+'_clicked' , event_type : 'offers_'+sectionName+'_clicked' });
 	 }	
 })
 /*Top offers|Offer page*/
 $(document).on('click','.top_categories',function(){
 	var sectionName = $(this).find('span > span').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		 utag.link({ link_text : 'offers_'+sectionName+'_clicked' , event_type : 'offers_'+sectionName+'_clicked' });
 	 }	
 })
 /*Half price Store|Offer page*/
 $(document).on('click','.clp_winter_launch_wrapper',function(){
 	var sectionName = $(this).parents('.winter_launch').find('.winter_launch_section .content').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		 utag.link({ link_text : 'offers_'+sectionName+'_clicked' , event_type : 'offers_'+sectionName+'_clicked' });
 	 }
 })
 /*under 999 store|Offer page*/
 $(document).on('click','.shop_for',function(){
 	var sectionName = $(this).find('.yCmsComponent.shop_for_component .content p').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		 utag.link({ link_text : 'offers_'+sectionName+'_clicked' , event_type : 'offers_'+sectionName+'_clicked' });
 	 }
 })
 
 
 /*Top Deals changes*/
 $(document).on('click','.top_deal',function(){
 	var header = $(this).find('.feature-collections > h2').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		utag.link({ 
 			link_text : "offers_"+header+"_clicked", 
 			event_type : "offers_"+header+"_clicked"
 		});
 	}
})

//Best Seller changes start
$(document).on('mousedown','.best_seller .owl-prev,.owl-next',function(e){
	var direction='';
	if($(e.currentTarget).hasClass('owl-next')){
		direction="Next";
	}
	else{
		direction="Previous";
	}
	direction = direction.toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	var title=$(this).parents('.best_seller').find('.content > p').text().trim().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	if(typeof utag !="undefined"){
		utag.link({ 
			link_text : title+'_'+direction, 
			event_type : title+'_navigation_click'
		});
	}
})
 
 $(document).on("click", ".best_seller .Menu li", function() {
 	var text = $(this).text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	var title=$(this).parents('.best_seller').find('.content > p').text().trim().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		utag.link({ 
 			link_text : text, 
 			event_type : title+'_menu_clicked'
 		});
 	}
 })
 
 $(document).on('click','.best_seller .js-owl-carousel .owl-item a',function(){
 	var productName=$(this).find('img').attr('title').toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	var title=$(this).parents('.best_seller').find('.content > p').text().trim().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		utag.link({ 
 			link_text : productName, 
 			event_type : title+'_product_clicked', 
 		});
 	} 
 })
 
 //Best Seller changes end
 /*TPR-4799 | offers page changes | End*/ 
  /*Homepage changes Ends*/
  

/*Order History page changes Start*/
/*TPR-4751 & 4753 | Order history and track order changes*/
$(document).on('click','.pagination_ul .header .links a',function(){
	var message='';
	if($(this).attr('href').indexOf('viewOrder') >0){
		message='view_order_details';
	}
	else if($(this).attr('href').indexOf('trackOrder') >0){
		message='track_order_details';
	}
	if(typeof utag !="undefined"){
		utag.link({ 
			link_text : message, 
			event_type : message 
		});
	}
})

/*TPR-4752 |cancel order*/
$(document).on('click','.pagination_ul .item .actions a',function(){
if(typeof utag !="undefined"){
	 utag.link({ link_text : 'cancel_order' , event_type : 'cancel_order_clicked' });
}
})

/*TPR-4754|invite friends start*/
$(document).on('click','#lnInvite',function(){
if(typeof utag !="undefined"){
	 utag.link({ link_text : 'invite_your_friends_start' , event_type : 'invite_your_friends_start' });
}
})


/*TPR-4754|invite friends end*/
/*Order History page changes End*/
/*TPR-4731 | Brand page changes | Start*/
 $(document).on('click','.call-to-action-link',function(){
 	if($('body').hasClass('pageType-ContentPage') && $('#ia_category_code').val() != '' && $('#ia_category_code').val().toLowerCase().indexOf('mbh') != -1){
 		var pageName = $('#pageName').val();
 		var brandName='';
 		if(typeof pageName != undefined && pageName != ''){
 			pageName=pageName.split('BrandStore-');
 			brandName = pageName[1].trim();
 		}
 		var linkName = $(this).text().trim();
 		var linkText = $(this).parents('.call-to-action-parent').find('.call-to-action-name').text().trim();
 		var msg = '';
 		if(brandName != undefined && brandName != ''){
 			msg = brandName +"_";
 		}
 		if(linkText != undefined && linkText != ''){
 			msg = msg+"_"+linkText + "_";
 		}
 		if(linkName != undefined && linkName != ''){
 			msg = msg + linkName + "_clicked";
 		}
 		msg = msg.toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 		if(msg != undefined && msg != '' && typeof utag != undefined){
 			utag.link({
 				link_text: msg,
 				event_type : msg
 			});
 		}
 	}
 })
 
 
 $(document).on('click','.call-to-action-banner',function(){
 	if($('body').hasClass('pageType-ContentPage') && $('#ia_category_code').val() != '' && $('#ia_category_code').val().toLowerCase().indexOf('mbh') != -1){
 		var pageName = $('#pageName').val();
 		var brandName='';
 		if(typeof pageName != undefined && pageName != ''){
 			pageName=pageName.split('BrandStore-');
 			brandName = pageName[1].trim();
 		}
 		var bannerName = $(this).attr('alt').trim();
 		
 		var msg = '';
 		if(brandName != undefined && brandName != ''){
 			msg = brandName + "_";
 		}
 		if(bannerName != undefined && bannerName != ''){
 			msg = msg + bannerName + "_clicked";
 		}
 		msg = msg.toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 		if(msg != undefined && msg != '' && typeof utag != undefined){
 			utag.link({
 				link_text: msg,
 				event_type : "brand_banner_clicked"
 			});
 		}
 	}
 })
 
 
 function brandSubCategoryPageCheck(){
 	var brandName='';
 	var subCategoryName = '';
 	if(typeof(Storage) !== "undefined") {
 		if(localStorage.getItem("brandName") != null){
 			brandName = localStorage.getItem("brandName");
 			localStorage.removeItem("brandName");
 		}
 	}
 	if($(".facet-list.filter-opt").find('.Brand') && $(".facet-list.filter-opt").find('.Brand').next().val()){
 		if($(".facet-list.filter-opt").find('.Brand').next().val().toLowerCase() == brandName){
 			if(typeof brandName != undefined && brandName!= ''){
 				if($('#product_category').val() != undefined && $('#product_category').val() != ''){
 					subCategoryName = brandName +"_"+$('#product_category').val().trim();
 				}
 				if($('#page_subcategory_name').val() != undefined && $('#page_subcategory_name').val() != ''){
 					subCategoryName = subCategoryName +"_"+$('#page_subcategory_name').val().trim();
 				}
 				if($('#page_subcategory_name_l3').val() != undefined && $('#page_subcategory_name_l3').val() != ''){
 					subCategoryName = subCategoryName +"_"+$('#page_subcategory_name_l3').val().trim();
 				}
 			}
 		}
 	}
 	return subCategoryName;
 }
 /*TPR-4731 | Brand page changes | End*/ 

/* Data Layer Schema Changes Ends*/

//Moved tealium on-load call to function so that it can be re-used.
function tealiumCallOnPageLoad()
{

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
	var isImageHoverTriggered = false;
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
				tealiumData += '"product_mrp":["'
						+ $("#product_unit_price").val() + '"],';
				tealiumData += '"site_section":"'
						+ $("#site_section").val() + '",';
				tealiumData += '"product_mop":["'
						+ $("#product_list_price").val() + '"],';
				tealiumData += '"product_discount":["'
					+ $("#product_discount").val() + '"],';
				tealiumData += '"product_discount_percentage":"'
					+ $("#product_discount_percentage").val() + '%",';
				tealiumData += '"product_name":["'
						+ $("#product_name").val() + '"],';
				tealiumData += '"product_sku":["'
						+ $("#product_sku").val() + '"],';
				if ($("#page_category_name").val() != 'undefined' && $("#page_category_name").val() != ''){
					tealiumData += '"page_category_name":"'
						+ $("#page_category_name").val() + '",';
				}
				if ($("#page_section_name").val() != 'undefined' && $("#page_section_name").val() != ''){
					tealiumData += '"page_section_name":"'
						+ $("#page_section_name").val() + '",';
				}
				tealiumData += '"page_name":"' + $("#page_name").val()
						+ '",';
				if(typeof(promo_title) != 'undefined' && promo_title !=''){ 
					tealiumData += '"offer_title":"'       //added for analytics schema
						+ promo_title + '",';
				}
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
				//For KIDSWEAR L4 needs to be populated 
				if($("#page_subcategory_name_l4").val() !=undefined || $("#page_subcategory_name_l4").val() !=null){ 
					tealiumData += '"page_subcategory_name_L4":"'
						+ $("#page_subcategory_name_l4").val().replace(/_+/g, '_') + '",';
					}
				tealiumData += '"product_brand":["'
						+ $("#product_brand").val() + '"],';
				tealiumData += '"site_section_detail":"'
						+ $("#site_section_detail").val() + '",';
				//TPR-672 START
				if(typeof(promo_title) != 'undefined' && promo_title !=''){ 
				tealiumData += '"promo_title":["'
					+promo_title+ '"],';
				}
				if(typeof(promo_id) != 'undefined' && promo_id !=''){ 
				tealiumData += '"promo_id":["'
					+promo_id+ '"],';
				}
				//TPR-672 END
				
				//Data Layer Schema changes
				tealiumData += '"product_stock_count":["'
					+ $("#product_stock_count").val() + '"],';
				tealiumData += '"out_of_stock":["'
					+ $("#out_of_stock").val() + '"],';
				tealiumData += '"product_image_count":"'
					+ thumbnailImageCount + '",';
				if (typeof(pdp_video_product_id) != 'undefined' || pdp_video_product_id != null){
					tealiumData += '"pdp_video_product_id":["'
						+ pdp_video_product_id + '"],';
				}
				
				//TPR-429 START
				tealiumData += '"seller_id":"'				//variable name changed | Data Layer Schema Changes 
					+ $("#pdpBuyboxWinnerSellerID").val() + '",';
				tealiumData += '"seller_name":"'
					+ $("#sellerNameId").html() + '",';
				if($("#pdpOtherSellerIDs").val() != 'undefined' && $("#pdpOtherSellerIDs").val() !=''){ 
					tealiumData += '"other_seller_ids":"'
					+ $("#pdpOtherSellerIDs").val() + '",';
				}
				//TPR-429 END
				//TPR-5193|Req-1 starts
				if($("#tealiumExchangeVar").val() != 'undefined' && $("#tealiumExchangeVar").val() !='' &&  $("#tealiumExchangeVar").val()!= "notAvailable"){ 
					tealiumData += '"exchange_value":"'
					+ $("#tealiumExchangeVar").val() + '",';
				}
				//TPR-5193|Req-1 ends
				//TPR-4688
				var sizeVariantList=$('#variant').find('li');
				if(sizeVariantList.length > 0){
					tealiumData += '"size_variant_count":"'
						+ sizeVariantList.length + '",';
				}
				
				//TPR-4692 | Breadcrumb 
				var breadcrum=[];
				$('.breadcrumbs.wrapper').find('li:not(.active)').each(function(){
					breadcrum.push($(this).find('a').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,""));
				})
				if(typeof(breadcrum) != 'undefined' || breadcrum != null){
					for(var i=0;i<breadcrum.length;i++){
						if(i!=0){
							var fieldName = "page_subcategory_L"+i;
							tealiumData += '"'+fieldName+'":"'
								+ breadcrum[i] + '",';
						}
					}
					tealiumData += '"product_display_hierarchy":"'
						+ breadcrum + '"}';
				}
				data = data.replace("}<TealiumScript>", tealiumData);
				// console.log(data);
				
				$('#tealiumHome').html(data);
				var productCodeArray = [];
 				productCodeArray.push($("#product_id").val());
 				productIdArray = productCodeArray;
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
		var currentPageURL = window.location.href;
		var pageTypeGeneric = '';
 		//done for PRDI-95
 		if ( pageType == 'checkout-login'){
 			pageTypeGeneric = 'login';
 		}
 		else if(pageType.indexOf("electronics") !== -1){
 			pageTypeGeneric = 'category';
 		}
 		else if(currentPageURL.indexOf("/c-mbh") > -1){
 			pageTypeGeneric = 'brand';
 		}
 		else if(currentPageURL.indexOf("/c-msh") > -1){
 			pageTypeGeneric = 'category';
 		}
 		else{
 			pageTypeGeneric = 'generic';
 		}
 
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
						try{
						// console.log(data);
						var tealiumData = "";
						var topFivePlpData = populateFirstFiveProductsPlpOnLoad();
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
						tealiumData += '"plp_first_5_products":'            //tisprdt-6227 starts
							+ topFivePlpData + ',';
						tealiumData += '"product_id":'
							+ topFivePlpData + ',';           //tisprdt-6227 ends
						if($("#out_of_stock_count").val() != undefined  && $("#out_of_stock_count").val() != null && $("#out_of_stock_count").val() != ''){
						tealiumData += '"out_of_stock_totalcount":"'		// TPR-4707
							+ $("#out_of_stock_count").val() + '",';
						}
						if(offerCount != undefined && offerCount != null && offerCount != ''){ 
						tealiumData += '"offer_product_count":"'		// TPR-4714
							+ offerCount + '",';
						}
						if(newCount != undefined && newCount != null && newCount != ''){ 
							tealiumData += '"new_product_count":"'		// TPR-4714
								+ newCount + '",';
							}
						if(onlineExclusive != undefined && onlineExclusive != null && onlineExclusive != '' ){ 
							tealiumData += '"onlineExclusive_product_count":"'		// TPR-4726
								+ onlineExclusive + '",';
							}
						var brand_sub_category = brandSubCategoryPageCheck();
 						if(brand_sub_category != undefined && brand_sub_category != null && brand_sub_category != ''){ 
 							tealiumData += '"brand_sub_category":"'
 								+ brand_sub_category + '",';
 							}
						/*TPR-430 Start*/
						if($("#product_category").val() != undefined && $("#product_category").val() != null && $("#product_category").val() != ''){ 
						tealiumData += '"product_category":'
							+ getListValue("product_category") + ',';                /*value passed as array instead of single string  INC_11511*/
							/*+ $("#product_category").val().replace(/_+/g, '_') + '",';*/
						}
						if($("#page_subcategory_name").val() != undefined && $("#page_subcategory_name").val() != null && $("#page_subcategory_name").val() != ''){ 
						tealiumData += '"page_subcategory_name":"'
							+ $("#page_subcategory_name").val().replace(/_+/g, '_') + '",';
						}
						if($("#page_subcategory_name_l3").val() != undefined && $("#page_subcategory_name_l3").val() != null){ 
						tealiumData += '"page_subcategory_name_L3":"'
							+ $("#page_subcategory_name_l3").val().replace(/_+/g, '_') + '"}';
						}
						/*TPR-430 End*/
						data = data.replace("}<TealiumScript>", tealiumData);
						$('#tealiumHome').html(data);
					}
						catch(e){
							console.log("error:tealium plp pageLoad"+e.message);
						}
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
			try{
				// console.log(data);
				var tealiumData = "";
				var topFiveSerpData = populateFirstFiveProductsSerpOnLoad();
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
				tealiumData += '"serp_first_5_products":'            //tisprdt-6227 starts
					+ topFiveSerpData + ',';
				tealiumData += '"product_id":'
					+ topFiveSerpData + ',';           //tisprdt-6227 ends
				if($("#out_of_stock_count").val() != undefined  && $("#out_of_stock_count").val() != null && $("#out_of_stock_count").val() != ''){
				tealiumData += '"out_of_stock_count":"'		// TPR-4722
					+ $("#out_of_stock_count").val() + '",';
				}
				if(offerCount != undefined && offerCount != null && offerCount != ''){ 
				tealiumData += '"offer_product_count":"'		// TPR-4726
					+ offerCount + '",';
				}
				if(newCount != undefined && newCount != null && newCount != ''){ 
					tealiumData += '"new_product_count":"'		// TPR-4726
						+ newCount + '",';
					}
				if(onlineExclusive != undefined && onlineExclusive != null && onlineExclusive != '' ){ 
					tealiumData += '"onlineExclusive_product_count":"'		// TPR-4726
						+ onlineExclusive + '",';
					}
				//TPR-430 Start
				if(page_subcategory_name != undefined && page_subcategory_name != null && page_subcategory_name != ''){ 
				tealiumData += '"product_category":"'
					+ product_category + '",';
				}
				if(page_subcategory_name != undefined && page_subcategory_name != null && page_subcategory_name != ''){ 
				tealiumData += '"page_subcategory_name":"'		
					+ page_subcategory_name +'",';
				}
				tealiumData += '"page_subcategory_name_L3":"'		
					+ page_subcategory_name_L3 +'"}';
				
				data = data.replace("}<TealiumScript>", tealiumData);
				$('#tealiumHome').html(data);
			}
			catch(e){
				console.log("error:tealium serp pageLoad"+e.message);
			   }
			}
		});
		
	}
	
//	if(pageType =="cart"){
//		$
//		.ajax({
//			url : ACC.config.encodedContextPath
//					+ "/getTealiumDataCart",
//			type : 'GET',
//			cache : false,
//			success : function(data) {
//				var qtyUpdated;
//				if(window.sessionStorage.getItem("qtyUpdate")!=null){
//					qtyUpdated=window.sessionStorage.getItem("qtyUpdate");
//				}
//				else{
//					qtyUpdated=false;
//				}
//				var tealiumData = "";
//				tealiumData += ',"user_login_type":"'	//TPR-668
//					+ user_login_type + '",';
//				tealiumData += '"cart_total":"'
//						+ $("#cart_total").val() + '",';
//				tealiumData += '"product_unit_price":'
//						+ " " + ',';
//				tealiumData += '"product_list_price":'
//					+ " "  + ',';
//				tealiumData += '"product_name":'
//					+ $("#product_name").val() + ',';
//				tealiumData += '"product_quantity":'
//					+ " " + ',';
//				tealiumData += '"adobe_product":"'
//					+ $("#adobe_product").val() + '",';
//				tealiumData += '"product_sku":'
//					+ $("#product_sku").val() + ',';
//				tealiumData += '"product_id":'
//					+ $("#product_id").val() + ',';
//				tealiumData += '"product_brand":'
//					+ $("#product_brand").val() + ',';				
//				tealiumData += '"product_quantity_update":'
//					+ qtyUpdated + ',';			
//				tealiumData += '"checkout_seller_ids":"'		//for TPR-429
//					+ $("#checkoutSellerIDs").val() + '",';
//				//L1 L2 L3 For cart  TPR-4831
//				if($("#page_subcategory_L1").val() !=undefined || $("#page_subcategory_L1").val() !=null){ 
//					tealiumData += '"page_subcategory_L1":'
//						+ getListValue("page_subcategory_L1") + ',';           
//					}
//				if($("#page_subcategory_L2").val() !=undefined || $("#page_subcategory_L2").val() !=null){ 
//					tealiumData += '"page_subcategory_L2":'
//						+ getListValue("page_subcategory_L2") + ',';            
//					}
//				if($("#page_subcategory_l3").val() !=undefined || $("#page_subcategory_l3").val() !=null){ 
//					tealiumData += '"page_subcategory_l3":'
//						+ getListValue("page_subcategory_l3") + ',';            
//					}
//				//added for kidswear:L4 needs to be populated
//				if($("#page_subcategory_l4").val() !=undefined || $("#page_subcategory_l4").val() !=null){ 
//					tealiumData += '"page_subcategory_l4":'
//						+ getListValue("page_subcategory_l4") + ',';            
//					}
//				//L1 L2 L3 cart ends
//				//TPR-430 Start
//				if($("#product_category").val() !=undefined || $("#product_category").val() !=null){ 
//					tealiumData += '"product_category":'
//					+ getListValue("product_category") + ',';	/*value passed as array instead of single string  INC_11511*/
//					/*+ $("#product_category").val().replace(/_+/g, '_') + '",';*/
//				}
//				if($("#page_subcategory_name").val() !=undefined || $("#page_subcategory_name").val() !=null){ 
//					tealiumData += '"page_subcategory_name":"'
//					+ $("#page_subcategory_name").val().replace(/_+/g, '_') + '",';
//				}
//				if($("#page_subcategory_name_l3").val() !=undefined || $("#page_subcategory_name_l3").val() !=null){ 
//					tealiumData += '"page_subcategory_name_L3":"'
//				+ $("#page_subcategory_name_l3").val().replace(/_+/g, '_') + '",'; //TISPRDT-1462
//				}
//				//added for kidswear:L4 needs to be populated
//				if($("#page_subcategory_name_l4").val() !=undefined || $("#page_subcategory_name_l4").val() !=null){ 
//					tealiumData += '"page_subcategory_name_L4":"'
//				+ $("#page_subcategory_name_l4").val().replace(/_+/g, '_') + '"}';
//				}
//			//TPR-430 End
//				data = data.replace("}<TealiumScript>", tealiumData);
//				$('#tealiumHome').html(data);
//			}
//		});
//	}
	
//	if(pageType =="multistepcheckoutsummary"){
//		try
//		{
//		var checkoutPageName=$('#checkoutPageName').val();
//		$
//		.ajax({
//			url : ACC.config.encodedContextPath
//					+ "/getTealiumDataCheckout",
//			type : 'GET',
//			data:'checkoutPageName='+checkoutPageName,
//			cache : false,
//			success : function(data) {
//				var tealiumData = "";
//				tealiumData += ',"user_login_type":"'	//TPR-668
//					+ user_login_type + '",';
//				tealiumData += '"cart_total":"'
//						+ $("#cart_total").val() + '",';
//				tealiumData += '"product_unit_price":'
//						+ $("#product_unit_price").val() + ',';
//				tealiumData += '"product_list_price":'
//					+ $("#product_list_price").val() + ',';
//				tealiumData += '"product_name":'
//					+ $("#product_name").val() + ',';
//				tealiumData += '"product_quantity":'
//					+ $("#product_quantity").val() + ',';
//				tealiumData += '"adobe_product":"'
//					+ $("#adobe_product").val() + '",';
//				tealiumData += '"product_sku":'
//					+ $("#product_sku").val() + ',';
//				tealiumData += '"product_id":'
//					+ $("#product_id").val() + ',';
//				tealiumData += '"product_brand":'
//					+ $("#product_brand").val() + ',';
//				
//				//kidswear
//				//L1 L2 L3 For cart
//				if($("#page_subcategory_L1").val() !=undefined || $("#page_subcategory_L1").val() !=null){ 
//					tealiumData += '"page_subcategory_L1":'
//						+ getListValue("page_subcategory_L1") + ',';           
//					}
//				if($("#page_subcategory_L2").val() !=undefined || $("#page_subcategory_L2").val() !=null){ 
//					tealiumData += '"page_subcategory_L2":'
//						+ getListValue("page_subcategory_L2") + ',';            
//					}
//				if($("#page_subcategory_l3").val() !=undefined || $("#page_subcategory_l3").val() !=null){ 
//					tealiumData += '"page_subcategory_l3":'
//						+ getListValue("page_subcategory_l3") + ',';            
//					}
//				//added for kidswear:L4 needs to be populated
//				if($("#page_subcategory_l4").val() !=undefined || $("#page_subcategory_l4").val() !=null){ 
//					tealiumData += '"page_subcategory_l4":'
//						+ getListValue("page_subcategory_l4") + ',';            
//					}
//				//L1 L2 L3 cart ends
//				//kidswear
//				
//				//TPR-430 Start
//				if($("#product_category").val() !=undefined || $("#product_category").val() !=null){ 
//					tealiumData += '"product_category":'
//					+ getListValue("product_category") + ',';                /*value passed as array instead of single string  INC_11511*/
//				/*	+ $("#product_category").val().replace(/_+/g, '_') + '",';*/
//				}
//				if($("#page_subcategory_name").val() !=undefined || $("#page_subcategory_name").val() !=null){ 
//					tealiumData += '"page_subcategory_name":"'
//					+ $("#page_subcategory_name").val().replace(/_+/g, '_') + '",';
//				}
//				if($("#page_subcategory_name_l3").val() !=undefined || $("#page_subcategory_name_l3").val() !=null){ 
//					tealiumData += '"page_subcategory_name_L3":"'
//				+ $("#page_subcategory_name_l3").val().replace(/_+/g, '_') + '",';
//				}
//				//added for kidswear:L4 needs to be populated
//				if($("#page_subcategory_name_l4").val() !=undefined || $("#page_subcategory_name_l4").val() !=null){ 
//					tealiumData += '"page_subcategory_name_L4":"'
//				+ $("#page_subcategory_name_l4").val().replace(/_+/g, '_') + '",'; //TISPRDT-1462
//				}
//			//TPR-430 End
//				tealiumData += '"checkout_seller_ids":"'		//for TPR-429
//					+ $("#checkoutSellerIDs").val() + '"}';
//				data = data.replace("}<TealiumScript>", tealiumData);
//				$("#tealiumHome").html(data);
//			
//				
//			}
//		});
//		}
//		catch(e)
//		{
//			console.log(e);
//		}
//		
//		
//	}

	
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
				 
				 //kidswear
				//L1 L2 L3 For cart
					if($("#page_subcategory_L1").val() !=undefined || $("#page_subcategory_L1").val() !=null){ 
						tealiumData += '"page_subcategory_L1":'
							+ getListValue("page_subcategory_L1") + ',';           
						}
					if($("#page_subcategory_L2").val() !=undefined || $("#page_subcategory_L2").val() !=null){ 
						tealiumData += '"page_subcategory_L2":'
							+ getListValue("page_subcategory_L2") + ',';            
						}
					if($("#page_subcategory_l3").val() !=undefined || $("#page_subcategory_l3").val() !=null){ 
						tealiumData += '"page_subcategory_l3":'
							+ getListValue("page_subcategory_l3") + ',';            
						}
					//added for kidswear:L4 needs to be populated
					if($("#page_subcategory_l4").val() !=undefined || $("#page_subcategory_l4").val() !=null){ 
						tealiumData += '"page_subcategory_l4":'
							+ getListValue("page_subcategory_l4") + ',';            
						}
					//L1 L2 L3 cart ends
					//kidswear

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
				//added for kidswear:L4 needs to be populated
				if($("#page_subcategory_name_l4").val() !=undefined || $("#page_subcategory_name_l4").val() !=null){ 
					tealiumData += '"page_subcategory_name_L4":"'
				+ $("#page_subcategory_name_l4").val().replace(/_+/g, '_') + '",'; //TISPRDT-1462
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
}
	//TPR-5251  on size select utag data issue fix starts
    function populateSizeSelectUtagObject(){
	var productCodeArray = [];
	productCodeArray.push($("#product_id").val());
	productIdArray = productCodeArray;
	
	if(typeof utag_data !="undefined"){
		var utilityArray = [];
		utilityArray.push($("#product_sku").val());
		utag_data.product_sku = utilityArray;
		utilityArray = [];
		utilityArray.push($("#product_unit_price").val());
		utag_data.product_mrp = utilityArray;
		utilityArray = [];
		utilityArray.push($("#product_list_price").val());
		utag_data.product_mop = utilityArray;
		utilityArray = [];
		utilityArray.push($("#product_discount").val());
		utag_data.product_discount = utilityArray;
		utag_data.product_discount_percentage = $("#product_discount_percentage").val() + '%';
		utag_data.product_id = productIdArray;
		if($("#product_applied_promotion_code").val() != '' && $("#product_applied_promotion_code").val() !=undefined)
		{
			promo_id=$("#product_applied_promotion_code").val().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
			utilityArray = [];
			utilityArray.push(promo_id);
			utag_data.promo_id = utilityArray;
		}
		else{
			if(utag_data.promo_id != undefined){
				delete utag_data.promo_id;
			}
		}
		if($("#product_applied_promotion_title").val() != '' && $("#product_applied_promotion_title").val() !=undefined)
		{
			promo_title=$("#product_applied_promotion_title").val().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
			utilityArray = [];
			utilityArray.push(promo_title);
			utag_data.offer_title = utilityArray;
			utag_data.promo_title = utilityArray;
		}
		else{
			if(utag_data.offer_title != undefined){
				delete utag_data.offer_title;
			}
			if(utag_data.promo_title != undefined){
				delete utag_data.promo_title;
			}
		}
		
		utilityArray = [];
		utilityArray.push($("#product_stock_count").val());
		utag_data.product_stock_count = utilityArray;
		utag_data.out_of_stock = $("#out_of_stock").val();
		utag_data.seller_id = $("#pdpBuyboxWinnerSellerID").val();
		utag_data.seller_name = $("#sellerNameId").html();
		if($("#pdpOtherSellerIDs").val() != '' && $("#pdpOtherSellerIDs").val() !=undefined)
		{
			otherSellerIds=$("#pdpOtherSellerIDs").val();
			utag_data.other_seller_ids = $("#pdpOtherSellerIDs").val();
		}
		else{
			if(utag_data.other_seller_ids != undefined){
				delete utag_data.other_seller_ids;
			}
		}
	}
}
//TPR-5251  on size select utag data issue fix ends

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
$(document).on('click','.list_title .UlContainer .sort',function(){
	if(typeof utag !="undefined"){
		var value = $(this).text().trim().toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
		utag.link({ 
			link_text : "sort_by_"+value , 
			event_type : "sort_by_selected" , 
			"sort_by_value" : value 
		});
	 }
})


/*TPR-4719, TPR-4704 | Search Filter in SERP|PLP*/
var restrictionFlag='false';

function setupSessionValuesUtag(){
	if($('.bottom-pagination .facet-list.filter-opt').children().length > 0){
		var filterTypeList=[];
		var filterTypeFinalList=[];
		var filterValueList=[];
		var sessionPageUrl=window.location.href;
		
		$('.bottom-pagination .facet-list.filter-opt').children().each(function(){
			filterTypeList.push($(this).children().eq(0).attr('class').toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,""));
			filterValueList.push($(this).children().eq(1).attr('value').toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,""))
		})
		
		$.each(filterTypeList, function(i, el){
			if($.inArray(el, filterTypeFinalList) === -1){
				filterTypeFinalList.push(el)
			};
		});
		if(filterValueList.length > 0 && filterTypeList.length > 0){
			if(typeof(utag) !="undefined"){
				utag.link({ 
					link_text : "final_filter_list" , 
					event_type : "final_filter_list" , 
					"filter_types_final":filterTypeFinalList.toString(),
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
		 utag.link({ serp_first_5_products : productArray,product_id : productArray  });
	 }
   
   if(productArray.length == 0){
	   if(typeof utag !="undefined"){
			 utag.link({ error_type: 'NoProductsFound' });
		 }  
   }
}

//tisprdt-6227 |onload top 5 products fix
function populateFirstFiveProductsSerpOnLoad(){
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

	var finalproductArray=[];
	for (i = 0; i < productArray.length; i++) { 
		finalproductArray.push("\""+productArray[i]+"\"");
	}
	
   if(productArray.length == 0){
	   if(typeof utag !="undefined"){
			 utag.link({ error_type: 'NoProductsFound' });
		 }  
   }
   
   return "["+finalproductArray+"]";
}

$( window ).load(function() {
	/*if($('#pageType').val() == "productsearch"){
		populateFirstFiveProductsSerp();	
	}
	
	if($('#pageType').val() == "category" || $('#pageType').val() == "electronics" ){
		populateFirstFiveProductsPlp();
	}*/
	
	if($('#pageType').val() == "productsearch" ){
		  var isVisible = $('.search-empty.no-results.wrapper:visible').is(':visible');
		   if(isVisible && typeof utag !="undefined" ){
			     utag.link({"error_type":'nullSearch'});
		}
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
		 utag.link({ plp_first_5_products : productArray,product_id : productArray });
	 }	
   
   if(productArray.length == 0){
	   if(typeof utag !="undefined"){
			 utag.link({ error_type: 'NoProductsFound' });
		 }  
   }
}

//tisprdt-6227 |onload top 5 products fix
function populateFirstFiveProductsPlpOnLoad(){
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
	if(productArray.length == 0){
		if(typeof utag !="undefined"){
			utag.link({ error_type: 'NoProductsFound' });
		}  
	}
	var finalproductArray=[];
	for (i = 0; i < productArray.length; i++) { 
		finalproductArray.push("\""+productArray[i]+"\"");
	}
		  
	return "["+finalproductArray+"]";
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
	 var url = $(this).parents().find(".product-tile.cboxElement").attr('href').split("/")[2];
		var productIDlist = url.split("p-");
		var productID = productIDlist[1];
		var productArray=[];
		productArray.push(productID);
	 if(typeof utag !="undefined"){
		 utag.link({ link_text : 'shop_now_clicked' , event_type :'shop_now_clicked',product_id : productArray ,shop_now_product_id : productArray });
	 }
 })

 
 /*Out Of Stock During adding to bag*/
function errorAddToBag(errorMessage){
	if(typeof(utag) != "undefined"){
	utag.link({"error_type":errorMessage});
	}
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
		if(typeof(utag) != "undefined"){
		  utag.link({ 
			  error_type : msg
		   });

		}
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

/*PDP, quickview image hover*/
$(document).on("mouseover",".zoomContainer",function(e) {
	if($('#pageType').val() != "/compare"){
		var page='';
		if($('#pageType').val() == "product"){
			page = "pdp";
		}
		else {
			page = "quickview";
		}
		if(typeof(utag)!="undefined" && (!isImageHoverTriggered)){
			utag.link({
				link_text: page+"_image_hover",
				event_type : page+"_image_hover"
			});
			isImageHoverTriggered = true;
		}
	}
});	


$(window).unload(function(event) {
	var pageType = $('#pageType').val();
	if(pageType == 'category' || pageType == 'productsearch'){
		if(restrictionFlag != 'true'){
			setupSessionValuesUtag();
		}
	}
});

//UF-398
$(document).on('click','#selectedAddressDivId span:last-child',function(){
	if(typeof utag !="undefined"){
		utag.link({
			link_text: "change_address_clicked",
			event_type : "change_address_clicked"
		});
	   }
});

$(document).on('click','#selectedDeliveryOptionsDivId span:last-child',function(){
	if(typeof utag !="undefined"){
		utag.link({
			link_text: "change_delivery_option_clicked",
			event_type : "change_delivery_option_clicked"
		});
	   }
	});
//UF-398 ends

//UF-472|Responsive search icon starts
$(document).on('mouseup','.simpleSearchToggle',function(){
	if(typeof(utag) !="undefined"){
		utag.link({
			link_text: "searchToggle_clicked",
			event_type : "searchToggle_clicked"
		});
	   }
});
//UF-472|Responsive search icon ends

//TPR-5271 starts| edit address
$(document).on('click','#changeAddressLink',function(){
	if(typeof(utag) !="undefined"){
		utag.link({
			link_text: "edit_shipping_address_clicked",
			event_type : "edit_shipping_address_clicked"
		});
	   }
});
