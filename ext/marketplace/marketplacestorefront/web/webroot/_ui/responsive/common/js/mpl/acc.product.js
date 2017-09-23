ACC.product = {
	_autoload: [
		//"bindToAddToCartForm",
		"addToBag",
		"enableStorePickupButton",
		"enableAddToCartButton",
		"enableVariantSelectors",
		"bindFacets",
		"resetAllPLP",
		"departmentRemoveJsHack",
		"brandFilter",
		"brandFilterCheckAll",
		"applyBrandFilter"
	],


	bindFacets:function(){
		$(document).on("click",".js-show-facets",function(e){
			e.preventDefault();

			ACC.colorbox.open("Select Refinements",{
				href: "#product-facet",
				inline: true,
				width:"320px",
				onComplete: function(){

					$(document).on("click",".js-product-facet .js-facet-name",function(e){
						e.preventDefault();
						$(".js-product-facet  .js-facet").removeClass("active");
						$(this).parents(".js-facet").addClass("active");
						$.colorbox.resize()
					})
				},
				onClosed: function(){
					$(document).off("click",".js-product-facet .js-facet-name");
				}
			});
		});



		enquire.register("screen and (min-width:"+screenSmMax+")",  function() {
			$("#cboxClose").click();
		});


	},



	enableAddToCartButton: function ()
	{
		$('.js-add-to-cart').removeAttr("disabled");
	},
	
	enableVariantSelectors: function ()
	{
		$('.variant-select').removeAttr("disabled");
	},
	
	bindToAddToCartForm: function ()
	{
		//alert("Hi-Bind-------------------->")
		var addToCartForm = $('.add_to_cart_form');
		addToCartForm.ajaxForm({success: ACC.product.displayAddToCartPopup});
	},

	bindToAddToCartStorePickUpForm: function ()
	{
		var addToCartStorePickUpForm = $('#colorbox #add_to_cart_storepickup_form');
		addToCartStorePickUpForm.ajaxForm({success: ACC.product.displayAddToCartPopup});
	},


	enableStorePickupButton: function ()
	{
		$('.js-pickup-in-store-button').removeAttr("disabled");
	},

	displayAddToCartPopup: function (cartResult, statusText, xhr, formElement)
	{
		$('#addToCartLayer').remove();

		if (typeof ACC.minicart.updateMiniCartDisplay == 'function')
		{
	//		ACC.minicart.updateMiniCartDisplay();
		}
		var titleHeader = $('#addToCartTitle').html();


		ACC.colorbox.open(titleHeader,{
			html: cartResult.addToCartLayer,
			width:"320px",
		});

		var productCode = $('[name=productCodePost]', formElement).val();
		var quantityField = $('[name=qty]', formElement).val();

		var quantity = 1;
		if (quantityField != undefined)
		{
			quantity = quantityField;
		}
		
		
		/***
		 * using this function to update the  product while clicking the Add to Bag button
		 *  without reloading the page
		 * 
		 */
		var existingCount = $("span.js-mini-cart-count,span.js-mini-cart-count-hover").html();
		existingCount = parseInt(existingCount);
		quantity = parseInt(quantity);
		$("span.js-mini-cart-count,span.js-mini-cart-count-hover").html(existingCount+quantity);
       // ACC.track.trackAddToCart(productCode, quantity, cartResult.cartData);

	},
	
	addToBag: function(){
	
		$(document).on('click','#addToCartForm .js-add-to-cart',function(event){
			//UF-160
			var isLargeApplnc = $("#isLargeAppliance").val();
			if(isLargeApplnc == "true" && pinCodeChecked == false){
				
				$("#addToCartFormTitle").html("<font color='#ff1c47'>" + $('#addToCartLargeAppliance').text() + "</font>");
				$("#addToCartFormTitle").show().fadeOut(6000);
				$('#pin').focus();
				$('#pin').css("border", "1px solid #000");
				return false;
			}
			//UF-160 ends
			ACC.product.sendAddToBag("addToCartForm");
			event.preventDefault();
			return false;
		});
		
	
		$(document).off('click', '#addToCartFormQuick').on('click', '#addToCartFormQuick', function(event) { 
		   
			 $("#qty1").val($("#quantity").val());
				if($("#sizeSelected").val()!='no'){
				
					/*TPR-681
					var productCodePost = $("#productCodePost").val();
					// Product code passed as an array for Web Analytics   INC_11511 
					var productCodeArray=[];
					productCodeArray.push(productCodePost);	// Product code passed as an array for Web Analytics
					utag.link({
						link_obj: this, 
						link_text: 'quick_view_addtobag' , 
						event_type : 'quick_view_addtobag', 
						product_sku_quick_view : productCodeArray
					});
					TPR-681 Ends*/
				
				ACC.product.sendAddToBagQuick("addToCartFormQuick");
				
				}else{
					$("#addToCartFormQuickTitle").html("<font color='#ff1c47'>" + $('#selectSizeId').text() + "</font>");
					$("#addToCartFormQuickTitle").show().fadeOut(6000);
					errorAddToBag("size_not_selected");
					dtmErrorTracking("size_not_selected","errorname");
				}
				event.preventDefault();
				return false;
		});
		
		// Size Guide addToCartSizeGuide
		//$(document).on('click','#addToCartSizeGuide .js-add-to-cart',function(event){
		$(document).off('click', '#addToCartSizeGuide .js-add-to-cart').on('click','#addToCartSizeGuide .js-add-to-cart',function(event){
			
			var selectedSizeFlag = $("#sizeSelectedVal").val();
			
			
			
			 $("#sizeQty").val($("#sizeGuideQty").val());
			//alert($('#variant.size-g option:selected').val());
			 // Sanity issue (Product is added to bag without selecting size guide)
			 //if($('#variant.size-g option:selected').val()!="#")
			 if ($('#variant.size-g li').hasClass('selected'))
			 {
				ACC.product.sendAddToBagSizeGuide("addToCartSizeGuide");
				$(".modal").modal('hide');	 //INC144315891
			 
			}else{
					$("#sizeSelectedSizeGuide").html("<font color='#ff1c47'>" + $('#sizeSelectedSizeGuide').text() + "</font>");
					$("#sizeSelectedSizeGuide").show();
			}
			event.preventDefault();
			return false;
		});
		
		//Exchange
		$(document).off('click', '#addToCartExchange .js-add-to-cart').on('click','#addToCartExchange .js-add-to-cart',function(event){
			
			var selectedSizeFlag = $("#sizeSelectedVal").val();
			
			
			
			 $("#sizeQty").val($("#sizeGuideQty").val());
			//alert($('#variant.size-g option:selected').val());
			 if($('#variant.size-g option:selected').val()!="#")
			 {
				ACC.product.sendAddToBagExchange("addToCartExchange");
			 
			}else{
					$("#sizeSelectedSizeGuide").html("<font color='#ff1c47'>" + $('#sizeSelectedSizeGuide').text() + "</font>");
					$("#sizeSelectedSizeGuide").show();
			}
			event.preventDefault();
			return false;
		});
				
		$(document).on('click','#addToCartFormId .js-add-to-cart',function(event){
			ACC.product.sendAddToBag("addToCartFormId",false);
			event.preventDefault();
			return false;
		});


		//change
	/*	$(document).ready(function(){
			
			var requiredserpUrl = ACC.config.encodedContextPath + "/search"+ "/showAllCartEntries";
			var resultData;
			$.ajax({
    			contentType : "application/json; charset=utf-8",
    			url : requiredserpUrl,
    			dataType : "json",
    			success : function(data) {
    				resultData = data;
    		  }
    		});
			//Iterate cart entries product list and disable product addToCart button
			$('form[id^="addToCartForm"]').each(function() {
				var isPresent=false;
				var productCodePost="productCodePost";
	 			var input = $("#"+this.id+" :input[name='" + productCodePost +"']"); 


	 			var productId="productCodePost";
				var product =input.val(); 
	            var dataString = 'productCode=' + product;






	            for ( var sProduct in resultData) {
					var entry = resultData[sProduct];
					if (product == entry) {
						isPresent = true;
						break;



					}
				}
	             if(isPresent){

	            	 
	            	 $("#addToCartButton"+product +".js-add-to-cart").hide();
	    				$("#addToCartButton"+product +".disabled.js-add-to-cart").show();





	             }
			});

			});*/

		 $(document).on('click','.serp_add_to_cart_form .js-add-to-cart',function(event){
				if(!$(this).hasClass("disabled")) {
				 var requiredserpUrl = ACC.config.encodedContextPath + "/search"+ "/showCartData";
				 var productId="productCodePost";
				 var product = $("#"+$(this).closest('form').attr("id")+" :input[name='" + productId +"']").val(); 
	             var dataString = 'productCode=' + product;
	             var dataValue='';
	            ACC.product.sendAddToBag($(this).closest('form').attr("id"));
				event.preventDefault();
				$(this).hide();
				$(this).siblings('.disabled').css("display","block");
				}
				return false;
			});
		
		$(document).on("click",".js-add-to-cart_wl",function(event){
			event.preventDefault();
			var formElem=$(this).closest(".add_to_cart_wl_form");	
			//For MSD
			$("#AddToBagFrmWl_isMSDEnabled").val($(this).parent().siblings('#isMSDEnabled_wl_AddToBag').val());
			$("#AddToBagFrmWl_isApparelExist").val($(this).parent().siblings('#isApparelExist_wl_AddToBag').val());
			$("#AddToBagFrmWl_salesHierarchyCategoryMSD").val($(this).parent().siblings('#salesHierarchyCategoryMSD_wl_AddToBag').val());
			$("#AddToBagFrmWl_productCodeForMSD").val($(this).parent().siblings('#productCodeForMSD_wl_AddToBag').val());
			$("#AddToBagFrmWl_sppriceForMSD").val($(this).parent().siblings('#sppriceForMSD_wl_AddToBag').val());
			$("#AddToBagFrmWl_moppriceForMSD").val($(this).parent().siblings('#moppriceForMSD_wl_AddToBag').val());
			$("#AddToBagFrmWl_rootCategoryMSD").val($(this).parent().siblings('#rootCategoryMSD_wl_AddToBag').val());
			//End MSD	
			
			/*TPR-646*/
			var productCode = $(this).closest(".add_to_cart_wl_form").find("input[name='productCodePost']").val();
			// Product code passed as an array for Web Analytics   INC_11511 
			var productCodeArray=[];
			productCodeArray.push(productCode);	// Product code passed as an array for Web Analytics
			utag.link({
				"link_obj" : this,
			    "link_text": 'add_tobag_wishlist',
			    "event_type": 'add_tobag_wishlist',
			    "product_sku_wishlist" : "" + productCodeArray
			});
			
			/*TPR-646 ends*/
			  ACC.product.sendAddToBagWl(formElem.attr("id"));
			return false;
		  });
		 
	 var $loading = $('#ajax-loader').hide();
		
	
	$('#popUpModal').on('shown.bs.modal', function () {
		 $('.sizes').focus();
	});
},

sendAddToBagWl: function(formId){
	
	var dataString=$('#'+formId).serialize();	
	$.ajax({
		url : ACC.config.encodedContextPath + "/cart/add",
		data : dataString,
		type : "POST",
		cache : false,
		beforeSend: function(){
	        $('#ajax-loader').show();
	    },
		success : function(data) {
			if(data.indexOf("cnt:") >= 0){
				$("#"+formId+"Title").html("");
				//$("#"+formId+"Title").html("<font color='#00CBE9'>"+$('#addtobagwl').text()+"</font>");
				//$("#"+formId+"Title").show().fadeOut(6000);
				var formId_splitdata = [];
				formId_splitdata = formId.split("_");
				ACC.product.showTransientCart(formId_splitdata[2]);
				ACC.product.addToBagFromWl(formId_splitdata[2],true);
				// TISPRD-9318
				var cartImageSrc=$("#"+formId+" input[name=cartIconWishlist]").val();
				var productCodePost=$("#"+formId+" input[name=productCodePost]").val();
				var productName=$("#"+formId+" input[name=productName]").val();
				console.log("cartImageSrc:"+cartImageSrc+" code:"+productCodePost+" name:"+productName);
				
				Header.showAddToBagPopOver(productCodePost, cartImageSrc, productName);
				// End TISPRD-9318
				//$("#"+formId+"Title").show().fadeOut(7000);
				//ACC.product.displayAddToCart(data,formId,false);				
				$("span.js-mini-cart-count,span.js-mini-cart-count-hover,span.responsive-bag-count").text(data.substring(4));
				//for MSD
				var isMSDEnabled = $("#AddToBagFrmWl_isMSDEnabled").val();
				var isApparelExist = $("#AddToBagFrmWl_isApparelExist").val();
				var salesHierarchyCategoryMSD = $("#AddToBagFrmWl_salesHierarchyCategoryMSD").val();
				var sppriceForMSD = $("#AddToBagFrmWl_sppriceForMSD").val();
				var moppriceForMSD = $("#AddToBagFrmWl_moppriceForMSD").val();
				var rootCategoryMSD = $("#AddToBagFrmWl_rootCategoryMSD").val();
				var productCodeMSD = $("#AddToBagFrmWl_productCodeForMSD").val();
				var price;
				if(typeof sppriceForMSD === 'undefined')
				{
				price = moppriceForMSD;
				}	
				else
				{
				price = sppriceForMSD;
				}
				
				if(typeof moppriceForMSD === 'undefined')
				{
				price = sppriceForMSD;
				}	
				else
				{
				price = moppriceForMSD
				}
				
				if(typeof isMSDEnabled === 'undefined')
				{
					isMSDEnabled = false;						
				}
				
				if(typeof isApparelExist === 'undefined')
				{
					isApparelExist = false;						
				}	
				
				if(Boolean(isMSDEnabled) && Boolean(isApparelExist) && (rootCategoryMSD === 'Clothing'))
					{					
					ACC.track.trackAddToCartForMAD(productCodeMSD, salesHierarchyCategoryMSD, price,"INR");					
					}	
				
				//End MSD
				
				
				}
				else if(data=="reachedMaxLimit") {
					$("#"+formId+"Title").html("");
					$("#"+formId+"Title").html("<br/><font color='#ff1c47'>"+$('#bagtofullwl').html()+"</font>");
					$("#"+formId+"Title").show();
				}
				else if(data=="crossedMaxLimit"){
					$("#"+formId+"Title").html("");
					$("#"+formId+"Title").html("<font color='#ff1c47'>"+$('#bagfullwl').text()+"</font>");
					$("#"+formId+"Title").show();
				}
				else if(data=="outofinventory"){
					 $("#"+formId+"noInventory").html("<font color='#ff1c47'>" + $('#addToCartFormnoInventory').text() + "</font>");
					 $("#"+formId+"noInventory").show().fadeOut(6000);
			   	     return false;
				}
				else if(data=="willexceedeinventory"){
					 $("#"+formId+"excedeInventory").html("<font color='#ff1c47'>" + $('#addToCartFormexcedeInventory').text() + "</font>");
					 $("#"+formId+"excedeInventory").show().fadeOut(6000);
			   		 return false;
				}
				else {
					
					$("#"+formId+"Title").html("");
					$("#"+formId+"Title").html("<br/><font color='#ff1c47'>"+$('#addtobagerrorwl').text()+"</font>");
					$("#"+formId+"Title").show();
				}
		},
		complete: function(){
	        $('#ajax-loader').hide();
	    },
		error : function(resp) {
		//	alert("Add to Bag unsuccessful");
		}
	});
},

addToBagFromWl: function(ussid, addedToCart) {
	var requiredUrl = ACC.config.encodedContextPath + "/my-account/addToBagFromWl";
	
	$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : {"ussid": ussid, "addedToCart":addedToCart},
		dataType : "json",
		success : function(response) {
			//alert("success_yipee");
		},
		complete:function(){
			forceUpdateHeader();
		}
	})
},


sendAddToBag : function(formId, isBuyNow) {
	
	 //For TISPRD-4631
	$('.js-add-to-cart').attr("disabled", "disabled");
	
	var staticHost=$('#staticHost').val();
	$("body").append("<div id='bag-clickSpin' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	$("body").append('<div class="loaderDiv" style="position: fixed; left: 45%;top:45%;"><img src="'+staticHost+'/_ui/responsive/common/images/red_loader.gif" class="bagspinner"></div>');  
	
	var input_name = "qty";
	var stock_id = "stock";
	var ussid="ussid";
	var dataString = $('#' + formId).serialize();
	var quantity = $("#" + formId + " :input[name='" + input_name + "']")
			.val();
	var stock = $("#" + formId + " :input[name='" + stock_id + "']").val();
	var quantity = $("#" + formId + " :input[name='" + input_name + "']")
			.val();
	var stock = $("#" + formId + " :input[name='" + stock_id + "']").val();
	var isSuccess = false;
	var ussid = $("#" + formId + " :input[name='" + ussid + "']").val();
	var utagError=false;
	/*
	 * if(parseInt(stock)<parseInt(quantity)){
	 * $("#"+formId+"noInventory").html("<font color='#ff1c47'>" +
	 * $('#inventory').text() + "</font>");
	 * $("#"+formId+"noInventory").show().fadeOut(6000); return false; }
	 */
	if ($("#variant,#sizevariant option:selected").val() == "#") {
		$("#" + formId + "Title").html(
				"<font color='#ff1c47'>" + $('#selectSizeId').text()
						+ "</font>");
		$("#" + formId + "Title").show();
		return false;
	} else {
		
		$.ajax({
					url : ACC.config.encodedContextPath + "/cart/add",
					data : dataString,
					type : "POST",
					cache : false,
					beforeSend : function() {
						$('#ajax-loader').show();
					},
					success : function(data) {
						//TPR-5346
						if(data.indexOf("|")){
							var values=data.split("|");
							}
						
						$('.js-add-to-cart').removeAttr("disabled");//For TISPRD-4631
						if (data.indexOf("cnt:") >= 0) {
							(isBuyNow == true) ? isSuccess = true
									: isSuccess = false;
							
							
							//$("#" + formId + "TitleSuccess").html("");
							//$("#" + formId + "TitleSuccess").html("<font color='#00CBE9'>"+ $('#addtobag').text()+ "</font>");
							//$("#" + formId + "TitleSuccess").show().fadeOut(5000);
							//$("#" + formId + "Title.sellerAddToBagTitle").show().fadeOut(5000);
							//$("#" + formId + " " + ".addToCartSerpTitle").show().fadeOut(5000);
							if(!isBuyNow){
								
								var isLuxury = $("#isLuxury").val();
								console.log("isLuxury"+ isLuxury);
								
								if(isLuxury) {
									var url = $(".mini-cart-link").data("miniCartUrl") + "?stamp="
									+ (new Date()).getTime();
									$.get(url, function(html) {
										var cartqtytext = $(html).find('.item-edit-details li:first-child').html();
										//var cartcount = cartqtytext;
										$('#addtocart-popup .lux-cart-btn span').html('('+cartqtytext+')');
									}); 
									$('#addtocart-popup,.luxury-over-lay').show(); 
									$("#no-click,.loaderDiv").remove();
								} else {
									ACC.product.showTransientCart(ussid);
									ACC.product.scrollForTransientCart();
								}
								
								
							}
							
							// ACC.product.displayAddToCart(data,formId,false);
							$("span.js-mini-cart-count,span.js-mini-cart-count-hover,span.responsive-bag-count").text(data.substring(4));

						}
						//TISJEWST-10
						else if(values[0]=="maxqtyaddedforfinejewellery"){
							$("#" + formId + "Title").html("<br/><font color='#ff1c47'>You can only order upto"+" "+values[1]+ " "+"pieces of this item.</font>");
							$("#" + formId + "Title").show().fadeOut(5000);
						}
						else if(data=="maxqtyexchange")
						{
							$("#"+formId+"Title").html("<br/><font color='#ff1c47'>"+$('#exchangeRestriction').html()+"</font>");
							$("#"+formId+"Title").show().fadeOut(5000);
						}
						else if (values[0] == "reachedMaxLimitforproduct") {//TPR-5346 STARTS
							$("#" + formId + "Title").html("You can only order upto" +" "+values[1]+ " "+"pieces of this item.");
							$("#" + formId + "Title").show().fadeOut(5000);//TPR-5346 ENDS
						} else if (data == "reachedMaxLimit") {
							$("#" + formId + "Title").html("");
							$("#" + formId + "Title").html(
									"<br/><font color='#ff1c47'>"
											+ $('#bagtofull').html()
											+ "</font>");
							$("#" + formId + "Title").show().fadeOut(5000);
						} else if (data == "crossedMaxLimit") {
							$("#" + formId + "Title").html("");
							$("#" + formId + "Title").html(
									"<font color='#ff1c47'>"
											+ $('#bagfull').text()
											+ "</font>");
							$("#" + formId + "Title").show().fadeOut(5000);
							errorAddToBag("bag_is_full");
							dtmErrorTracking("bag_is_full","errorname");
							utagError=true;
						} else if (data == "outofinventory") {
							$("#" + formId + "noInventory")
									.html(
											"<font color='#ff1c47'>"
													+ $(
															'#addToCartFormnoInventory')
															.text()
													+ "</font>");
							$("#" + formId + "noInventory").show().fadeOut(
									6000);
							errorAddToBag("out_of_stock");
							dtmErrorTracking("out_of_stock","errorname");
							utagError=true;
							return false;
						} else if (data == "willexceedeinventory") {
							$("#" + formId + "excedeInventory")
									.html(
											"<font color='#ff1c47'>"
													+ $(
															'#addToCartFormexcedeInventory')
															.text()
													+ "</font>");
							$("#" + formId + "excedeInventory").show()
									.fadeOut(6000);
							return false;
						} 
						else if(data == "freebieErrorMsg") { //freebie unable to add
							$("#" + formId + "Title").html("Freebie: This product is not on sale");						
							$("#" + formId + "Title").show().fadeOut(5000);
							return false;
						}
						else if(data == "mismatchUssid") { //mismatchUssid unable to add
							$("#" + formId + "Title").html("Something went wrong. Please try again");						
							$("#" + formId + "Title").show().fadeOut(5000);
							return false;
						}
						else {
							$("#" + formId + "Title").html("");
							$("#" + formId + "Title").html(
									"<br/><font color='#ff1c47'>"
											+ $('#addtobagerror').text()
											+ "</font>");
							$("#" + formId + "Title").show().fadeOut(5000);
						}

						// For MSD
						var isMSDEnabled = $("input[name=isMSDEnabled]")
								.val();
						if (isMSDEnabled === 'true') {
							// console.log(isMSDEnabled);
							var isApparelExist = $(
									"input[name=isApparelExist]").val();
							// console.log(isApparelExist);
							var salesHierarchyCategoryMSD = $(
									"input[name=salesHierarchyCategoryMSD]")
									.val();
							// console.log(salesHierarchyCategoryMSD);
							var rootCategoryMSD = $(
									"input[name=rootCategoryMSD]").val();
							// console.log(rootCategoryMSD);
							var productCodeMSD = $(
									"input[name=productCodeMSD]").val();
							// console.log(productCodeMSD);
							var priceformad = $("input[id=price-for-mad]")
									.val();
							// console.log(priceformad);

							if (typeof isMSDEnabled === 'undefined') {
								isMSDEnabled = false;
							}

							if (typeof isApparelExist === 'undefined') {
								isApparelExist = false;
							}

							if (Boolean(isMSDEnabled)
									&& Boolean(isApparelExist)
									&& (rootCategoryMSD === 'Clothing')) {
								ACC.track.trackAddToCartForMAD(
										productCodeMSD,
										salesHierarchyCategoryMSD,
										priceformad, "INR");
							}
						
						}
						// End MSD
						if (isSuccess == true) {
							var cartUrl = ACC.config.encodedContextPath
									+ "/cart";
							location.href = cartUrl;
						}
						
						if(!utagError){
							if(isBuyNow){
								utagAddProductToBag("buy_now",productCodeMSD);
								//TPR-6029
								dtmAddProductToBag("buy_now");
							}
							else{
								utagAddProductToBag("add_to_bag",productCodeMSD);
								//TPR-6029
								dtmAddProductToBag("add_to_bag");
							}
						}
						
						$("#bag-clickSpin,.loaderDiv").remove();			
					},
					complete : function() {
						$('#ajax-loader').hide();
						forceUpdateHeader();
						$("#bag-clickSpin,.loaderDiv").remove();
						$('.js-add-to-cart').removeAttr("disabled");//For TISPRD-4631
					},
					error : function(resp) {
						$("#bag-clickSpin,.loaderDiv").remove();
						$('.js-add-to-cart').removeAttr("disabled");//For TISPRD-4631
					}
				});
	}
},

sendAddToBagQuick:function(formId){
	
	 var input_name="qty";
	  var stock_id="stock";
	 var dataString=$('#'+formId).serialize();	
	 var quantity = $("#"+formId+" :input[name='" + input_name +"']").val(); 
	 var stock = $("#"+formId+" :input[name='" +  stock_id +"']").val(); 
	 var quantity = $("#"+formId+" :input[name='" + input_name +"']").val(); 
	 var stock = $("#"+formId+" :input[name='" +  stock_id +"']").val(); 
	 var ussid=$('#ussid_quick').val();
	var productCode = $("#productCode").val();
	var utagError=false;
	var digitalDataError=false;
		
	 /*if(parseInt(stock)<parseInt(quantity)){
		    $("#"+formId+"noInventory").html("<font color='#ff1c47'>" + $('#inventory').text() + "</font>");
		    $("#"+formId+"noInventory").show().fadeOut(6000);
  			 return false;
      	 }*/
	  
		  
	$.ajax({
		url : ACC.config.encodedContextPath + "/cart/add",
		data : dataString,
		type : "POST",
		cache : false,
		beforeSend: function(){
	       // $('#ajax-loader').show();
			//$('#ajax-loader').show();//mismatch issue
			var staticHost=$('#staticHost').val();
			$(".quickview").append("<div id='bag-clickSpin' style='opacity:0.15; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
			$(".quickview").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="bagspinner" style="position: fixed; left: 45%;top:45%; height: 30px;">'); 
	    },
		success : function(data) {
			
			if(data.indexOf("cnt:") >= 0){
			//$("#"+formId+"TitleSuccess").html("");
			//$("#"+formId+"TitleSuccess").html("<font color='#00CBE9'>"+$('#addtobag').text()+"</font>");

			//$("#"+formId+"TitleSuccess").show().fadeOut(5000);

			//$("#"+formId+"Title.sellerAddToBagTitle").show().fadeOut(5000);
			//$("#"+formId+" "+".addToCartSerpTitle").show().fadeOut(5000);
			//Sanity issue fixing
			$("span.js-mini-cart-count,span.js-mini-cart-count-hover,span.responsive-bag-count").text(data.substring(4));
			ACC.product.showTransientCart(ussid);
			ACC.product.scrollForTransientCart();
			//ACC.product.displayAddToCart(data,formId,false);
			
			}
			else if(data=="reachedMaxLimit") {
				$("#"+formId+"Title").html("");
				$("#"+formId+"Title").html("<br/><font color='#ff1c47'>"+$('#bagtofull').html()+"</font>");
				$("#"+formId+"Title").show().fadeOut(5000);
			}
			
			else if(data=="crossedMaxLimit"){
				$("#"+formId+"Title").html("");
				$("#"+formId+"Title").html("<font color='#ff1c47'>"+$('#bagfull').text()+"</font>");
				$("#"+formId+"Title").show().fadeOut(5000);
				errorAddToBag("bag_is_full");
				dtmErrorTracking("bag_is_full","errorname");
				utagError=true;
				digitalDataError=true;
			}
			else if(data=="outofinventory"){
				 //$("#"+formId+"noInventory").html("<font color='#ff1c47'>" + $('#addToCartFormnoInventory').text() + "</font>");
				$("#addToCartFormnoInventory").show().fadeOut(6000);
				errorAddToBag("out_of_stock");
				dtmErrorTracking("bag_is_full","errorname");
				utagError=true;
				digitalDataError=true;
		   	     return false;
			}
			else if(data=="willexceedeinventory"){
				 $("#"+formId+"excedeInventory").html("<font color='#ff1c47'>" + $('#addToCartFormexcedeInventory').text() + "</font>");
				 $("#"+formId+"excedeInventory").show().fadeOut(6000);
		   		 return false;
			}
			else if(data=="mismatchUssid"){ //mismatch issue
				$("#"+formId+"Title").html("");
				$("#"+formId+"Title").html("<font color='#ff1c47'>" + $('#addtobagerror').text() + "</font>");
				$("#"+formId+"Title").show().fadeOut(6000);
		   		 return false;
			}
			else{
				
				$("#"+formId+"Title").html("");
				$("#"+formId+"Title").html("<br/><font color='#ff1c47'>"+$('#addtobagerror').text()+"</font>");
				$("#"+formId+"Title").show().fadeOut(5000);
			}
		
			//For MSD
			var isMSDEnabled =  $("input[name=isMSDEnabled]").val();								
			if(isMSDEnabled === 'true')
			{
			//console.log(isMSDEnabled);
			var isApparelExist  = $("input[name=isApparelExist]").val();
			//console.log(isApparelExist);				
			var salesHierarchyCategoryMSD =  $("input[name=salesHierarchyCategoryMSD]").val();
			//console.log(salesHierarchyCategoryMSD);
			var rootCategoryMSD  = $("input[name=rootCategoryMSD]").val();
			//console.log(rootCategoryMSD);				
			var productCodeMSD =  $("input[name=productCodeMSD]").val();
			//console.log(productCodeMSD);				
			var priceformad =  $("input[id=price-for-mad]").val();
			//console.log(priceformad);				
		
			if(typeof isMSDEnabled === 'undefined')
			{
				isMSDEnabled = false;						
			}
			
			if(typeof isApparelExist === 'undefined')
			{
				isApparelExist = false;						
			}	
			
			if(Boolean(isMSDEnabled) && Boolean(isApparelExist) && (rootCategoryMSD === 'Clothing'))
				{					
				ACC.track.trackAddToCartForMAD(productCodeMSD, salesHierarchyCategoryMSD, priceformad,"INR");
				}	
			}
			/*TPR-681,TPR-4725*/
			// Product code passed as an array for Web Analytics   INC_11511 
			if(!utagError){
				var productCodeArray=[];
				productCodeArray.push(productCode);	// Product code passed as an array for Web Analytics
				utag.link({
					link_text: 'quick_view_addtobag' , 
					event_type : 'quick_view_addtobag', 
					product_sku_quick_view : productCodeArray
				});
			}
			/*TPR-681 Ends*/
			//End MSD
			//TPR-6029 | add to bag on quickview #42--start
			if(!digitalDataError){
				if(typeof(_satellite)!= "undefined"){
					_satellite.track('cpj_qw_add_to_bag');
				}
			}
			//TPR-6029 | add to bag on quickview #42--end
			
		},
		complete: function(){
	        //$('#ajax-loader').hide();//mismatch issue
			$("#bag-clickSpin,.bagspinner").remove();
	    },
		error : function(resp) {
			
		}
	});
	  
},

	sendToCartPageQuick: function(formId,isBuyNow){
		 var input_name="qty";
		  var stock_id="stock";
		 var dataString=$('#'+formId).serialize();	
		 var quantity = $("#"+formId+" :input[name='" + input_name +"']").val(); 
		 var stock = $("#"+formId+" :input[name='" +  stock_id +"']").val(); 
		 var quantity = $("#"+formId+" :input[name='" + input_name +"']").val(); 
		 var stock = $("#"+formId+" :input[name='" +  stock_id +"']").val(); 
		 var ussid=$('#ussid_quick').val();
		 var utagError=false;
		 var digitalDataError=false;
		 /*if(parseInt(stock)<parseInt(quantity)){
			    $("#"+formId+"noInventory").html("<font color='#ff1c47'>" + $('#inventory').text() + "</font>");
			    $("#"+formId+"noInventory").show().fadeOut(6000);
	   			 return false;
	       	 }*/
		  if(isBuyNow){
			  
		$.ajax({
			url : ACC.config.encodedContextPath + "/cart/add",
			data : dataString,
			type : "POST",
			cache : false,
			beforeSend: function(){
		        //$('#ajax-loader').show();//mismatch issue
				var staticHost=$('#staticHost').val();
				$(".quickview").append("<div id='bag-clickSpin' style='opacity:0.15; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
				$(".quickview").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="bagspinner" style="position: fixed; left: 45%;top:45%; height: 30px;">'); 
		    },
			success : function(data) {
				//TISQAEE-64
				var productCode = $('#productCode').val();
				// Product code passed as an array for Web Analytics   INC_11511 
				var productCodeArray=[];
				productCodeArray.push(productCode);	// Product code passed as an array for Web Analytics
				var isSuccess=true;
				if(data.indexOf("cnt:") >= 0){
				//$("#"+formId+"TitleSuccess").html("");
				//$("#"+formId+"TitleSuccess").html("<font color='#00CBE9'>"+$('#addtobag').text()+"</font>");

				//$("#"+formId+"TitleSuccess").show().fadeOut(5000);

				//$("#"+formId+"Title.sellerAddToBagTitle").show().fadeOut(5000);
				//$("#"+formId+" "+".addToCartSerpTitle").show().fadeOut(5000);
//				ACC.product.showTransientCart(ussid);
//				ACC.product.scrollForTransientCart();
				//ACC.product.displayAddToCart(data,formId,false);
				$("span.js-mini-cart-count,span.js-mini-cart-count-hover,span.responsive-bag-count").text(data.substring(4));
				}
				else if(data=="reachedMaxLimit") {
					$("#"+formId+"Title").html("");
					$("#"+formId+"Title").html("<br/><font color='#ff1c47'>"+$('#bagtofull').html()+"</font>");
					$("#"+formId+"Title").show().fadeOut(5000);
				}
				
				else if(data=="crossedMaxLimit"){
					$("#"+formId+"Title").html("");
					$("#"+formId+"Title").html("<font color='#ff1c47'>"+$('#bagfull').text()+"</font>");
					$("#"+formId+"Title").show().fadeOut(5000);
					errorAddToBag("bag_is_full");
					dtmErrorTracking("bag_is_full","errorname");
					utagError=true;
					digitalDataError=true;
				}
				else if(data=="outofinventory"){
					 //$("#"+formId+"noInventory").html("<font color='#ff1c47'>" + $('#addToCartFormnoInventory').text() + "</font>");
					$("#addToCartFormnoInventory").show().fadeOut(6000);
					errorAddToBag("out_of_stock");
					dtmErrorTracking("out_of_stock","errorname");
					utagError=true;
					digitalDataError=true;
			   	     return false;
				}
				else if(data=="willexceedeinventory"){
					 $("#"+formId+"excedeInventory").html("<font color='#ff1c47'>" + $('#addToCartFormexcedeInventory').text() + "</font>");
					 $("#"+formId+"excedeInventory").show().fadeOut(6000);
			   		 return false;
				}
				else if(data=="mismatchUssid"){ //mismatch issue
					$("#"+formId+"Title").html("");
					$("#"+formId+"Title").html("<font color='#ff1c47'>" + $('#addtobagerror').text() + "</font>");
					$("#"+formId+"Title").show().fadeOut(6000);
			   		 return false;
				}
				else{
					$("#"+formId+"Title").html("");
					$("#"+formId+"Title").html("<br/><font color='#ff1c47'>"+$('#addtobagerror').text()+"</font>");
					$("#"+formId+"Title").show().fadeOut(5000);
				}
			
				//For MSD
				var isMSDEnabled =  $("input[name=isMSDEnabled]").val();								
				if(isMSDEnabled === 'true')
				{
				//console.log(isMSDEnabled);
				var isApparelExist  = $("input[name=isApparelExist]").val();
				//console.log(isApparelExist);				
				var salesHierarchyCategoryMSD =  $("input[name=salesHierarchyCategoryMSD]").val();
				//console.log(salesHierarchyCategoryMSD);
				var rootCategoryMSD  = $("input[name=rootCategoryMSD]").val();
				//console.log(rootCategoryMSD);				
				var productCodeMSD =  $("input[name=productCodeMSD]").val();
				//console.log(productCodeMSD);				
				var priceformad =  $("input[id=price-for-mad]").val();
				//console.log(priceformad);	
			
				
				if(typeof isMSDEnabled === 'undefined')
				{
					isMSDEnabled = false;						
				}
				
				if(typeof isApparelExist === 'undefined')
				{
					isApparelExist = false;						
				}	
				
				if(Boolean(isMSDEnabled) && Boolean(isApparelExist) && (rootCategoryMSD === 'Clothing'))
					{					
					ACC.track.trackAddToCartForMAD(productCodeMSD, salesHierarchyCategoryMSD, priceformad,"INR");
					}	
				}
				//if(isSuccess){
				if(!utagError && !digitalDataError){
					//TISQAEE-64 Buy Now Quick View
					if(typeof utag !="undefined"){
						utag.link({
							link_text: 'quickview_buynow' ,
							event_type : 'quickview_buynow',
							product_sku : productCodeArray
						});
					}
					//TPR-6029 | buy now on quickview #41
					if(typeof(_satellite) != "undefined"){
						 _satellite.track('cpj_qw_buy_now');
					 }
					location.href=ACC.config.encodedContextPath + '/cart';
				}
				//End MSD
				
			},
			complete: function(){
				 // $('#ajax-loader').hide(); //mismatch issue
				$("#bag-clickSpin,.bagspinner").remove();
		    },
			error : function(resp) {
			//	alert("Add to Bag unsuccessful");
			}
		});
		  }
		  /*else{
			  alert('BuyNow Qv False');
		  }*/
	},
	
	//SizeGuide 
	sendAddToBagSizeGuide: function(formId){
		
		//alert("Size Guide sendAddToBagSizeGuide 3 "+formId);
		
		var input_name="qty";
		var stock_id="stock";
		var ussid="ussid";
		var dataString=$('#'+formId).serialize();	
		var quantity = $("#"+formId+" :input[name='" + input_name +"']").val(); 
		var stock = $("#"+formId+" :input[name='" +  stock_id +"']").val(); 
		var quantity = $("#"+formId+" :input[name='" + input_name +"']").val(); 
		var stock = $("#"+formId+" :input[name='" +  stock_id +"']").val(); 
		var ussid = $("#"+formId+" :input[name='" +  ussid +"']").val(); 
		//alert("dataString: "+dataString+" quantity: "+quantity+" stock: "+stock);
		$.ajax({
			url : ACC.config.encodedContextPath + "/cart/add",
			data : dataString,
			type : "POST",
			cache : false,
			beforeSend: function(){
		        $('#ajax-loader').show();
		    },
			success : function(data) {
				//alert("data: "+data);
				if(data.indexOf("cnt:") >= 0){
					//alert("addtobag");
				//$("#"+formId+"TitleSuccess").html("");
				//$("#"+formId+"TitleSuccess").html("<font color='#00CBE9'>"+$('#addtobag').text()+"</font>");

				//$("#"+formId+"TitleSuccess").show().fadeOut(5000);

				//$("#"+formId+"Title.sellerAddToBagTitle").show().fadeOut(5000);
				//$("#"+formId+" "+".addToCartSerpTitle").show().fadeOut(5000);

				//alert("data form id: "+$("#"+formId+" "+".addToCartSerpTitle"));
				ACC.product.showTransientCart(ussid);
				ACC.product.scrollForTransientCart();
				
					
				//ACC.product.displayAddToCart(data,formId,false);
				$("span.js-mini-cart-count,span.js-mini-cart-count-hover,span.responsive-bag-count").text(data.substring(4));
				}
				else if(data=="reachedMaxLimit") {
					//$("#"+formId+"Title").html("");
					$("#"+formId+"Titlebagtofull").html("<br/><font color='#ff1c47'>"+$('#addToCartSizeGuideTitlebagtofull').html()+"</font>");
					$("#"+formId+"Titlebagtofull").show().fadeOut(5000);
				}
				else if(data=="crossedMaxLimit"){
					//alert("bagfull:  "+ formId+"Titlebagfull");
					//$("#"+formId+"Titlebagfull").html("");
					$("#"+formId+"Titlebagfull").html("<font color='#ff1c47'>"+$('#addToCartSizeGuideTitlebagfull').text()+"</font>");
					$("#"+formId+"Titlebagfull").show().fadeOut(5000);
				}
				else if(data=="outofinventory"){
					
					//alert("outofinventory: "+data);
					 $("#"+formId+"noInventorySize").html("<font color='#ff1c47'>" + $('#addToCartSizeGuidenoInventorySize').text() + "</font>");
					 $("#"+formId+"noInventorySize").show().fadeOut(6000);
			   	     return false;
				}
				else if(data=="willexceedeinventory"){
					 $("#"+formId+"excedeInventorySize").html("<font color='#ff1c47'>" + $('#addToCartSizeGuideexcedeInventorySize').text() + "</font>");
					 $("#"+formId+"excedeInventorySize").show().fadeOut(6000);
			   		 return false;
				}
				else{
					$("#"+formId+"Titleaddtobagerror").html("");
					$("#"+formId+"Titleaddtobagerror").html("<br/><font color='#ff1c47'>"+$('#addToCartSizeGuideTitleaddtobagerror').text()+"</font>");
					$("#"+formId+"Titleaddtobagerror").show().fadeOut(5000);
				}
			
				//For MSD
				var isMSDEnabled =  $("input[name=isMSDEnabled]").val();								
				if(isMSDEnabled === 'true')
				{
				//console.log(isMSDEnabled);
				var isApparelExist  = $("input[name=isApparelExist]").val();
				//console.log(isApparelExist);				
				var salesHierarchyCategoryMSD =  $("input[name=salesHierarchyCategoryMSD]").val();
				//console.log(salesHierarchyCategoryMSD);
				var rootCategoryMSD  = $("input[name=rootCategoryMSD]").val();
				//console.log(rootCategoryMSD);				
				var productCodeMSD =  $("input[name=productCodeMSD]").val();
				//console.log(productCodeMSD);				
				var priceformad =  $("input[id=price-for-mad]").val();
				//console.log(priceformad);				
				
				if(typeof isMSDEnabled === 'undefined')
				{
					isMSDEnabled = false;						
				}
				
				if(typeof isApparelExist === 'undefined')
				{
					isApparelExist = false;						
				}	
				
				if(Boolean(isMSDEnabled) && Boolean(isApparelExist) && (rootCategoryMSD === 'Clothing'))
					{					
					ACC.track.trackAddToCartForMAD(productCodeMSD, salesHierarchyCategoryMSD, priceformad,"INR");
					}	
				}
				//End MSD
				
			},
			complete: function(){
		        $('#ajax-loader').hide();
		    },
			error : function(resp) {
				//alert("Add to Bag unsuccessful: "+resp.responseText);
			}
		});
	},
	
	//Exchange Start
	sendAddToBagExchange: function(formId){
		 $('#ajax-loader').show();	
		var input_name="qty";
		var stock_id="stock";
		var ussid="ussid";
		var dataString=$('#'+formId).serialize();	
		var quantity = $("#"+formId+" :input[name='" + input_name +"']").val(); 
		var stock = $("#"+formId+" :input[name='" +  stock_id +"']").val(); 
		var ussid = $("#"+formId+" :input[name='" +  ussid +"']").val(); 
		//TPR-5193 Analytics starts
		var brandExchange = $('#brandExchange').val();
		var couponValue = $('#priceselect').text();
		var l3 =$('#l3').val();
	    var selector	 = $('#activeselect option:selected').val();
	    var exchangeDropdown = selector.split('|');
	    var exchangeCondition = exchangeDropdown[1] ;
	    var productCode =  $('#product_id').val();
		var productArray =[];
		productArray.push(productCode);
		//TISPRDT-2439
		var isError=false;
		//TPR-5193 Analytics ends
		$.ajax({
			url : ACC.config.encodedContextPath + "/cart/add",
			data : dataString,
			type : "POST",
			cache : false,
			beforeSend: function(){
		        $('#ajax-loader').show();
		    },
			success : function(data) {
				//alert("data: "+data);
				if(data.indexOf("cnt:") >= 0){
					//alert("addtobag");
				//$("#"+formId+"TitleSuccess").html("");
				//$("#"+formId+"TitleSuccess").html("<font color='#00CBE9'>"+$('#addtobag').text()+"</font>");

				//$("#"+formId+"TitleSuccess").show().fadeOut(5000);

				//$("#"+formId+"Title.sellerAddToBagTitle").show().fadeOut(5000);
				//$("#"+formId+" "+".addToCartSerpTitle").show().fadeOut(5000);

				//alert("data form id: "+$("#"+formId+" "+".addToCartSerpTitle"));
				ACC.product.showTransientCart(ussid);
				ACC.product.scrollForTransientCart();
					
				//ACC.product.displayAddToCart(data,formId,false);
				$("span.js-mini-cart-count,span.js-mini-cart-count-hover,span.responsive-bag-count").text(data.substring(4));
				}
				else if(data=="maxqtyexchange")
				{
					isError=true;
					$("#"+formId+"Titlebagtofull").html("<br/><font color='#ff1c47'>"+$('#addToCartExchangeExceededmaxqtyExc').html()+"</font>");
					$("#"+formId+"Titlebagtofull").show().fadeOut(5000);
				}
				else if(data=="reachedMaxLimit") {
					//$("#"+formId+"Title").html("");
					isError=true;
					
				}
				else if(data=="crossedMaxLimit"){
					//alert("bagfull:  "+ formId+"Titlebagfull");
					//$("#"+formId+"Titlebagfull").html("");
					$("#"+formId+"Titlebagfull").html("<font color='#ff1c47'>"+$('#addToCartExchangeTitlebagfull').text()+"</font>");
					$("#"+formId+"Titlebagfull").show().fadeOut(5000);
					isError=true;
				}
				else if(data=="outofinventory"){
					
					//alert("outofinventory: "+data);
					 $("#"+formId+"noInventorySize").html("<font color='#ff1c47'>" + $('#addToCartExchangenoInventorySize').text() + "</font>");
					 $("#"+formId+"noInventorySize").show().fadeOut(6000);
					 isError=true;
			   	     return false;
				}
				else if(data=="willexceedeinventory"){
					 $("#"+formId+"excedeInventorySize").html("<font color='#ff1c47'>" + $('#addToCartExchangeexcedeInventorySize').text() + "</font>");
					 $("#"+formId+"excedeInventorySize").show().fadeOut(6000);
					 isError=true;
			   		 return false;
				}
				
				
				else{
					$("#"+formId+"Titleaddtobagerror").html("");
					$("#"+formId+"Titleaddtobagerror").html("<br/><font color='#ff1c47'>"+$('#addToCartExchangeTitleaddtobagerror').text()+"</font>");
					$("#"+formId+"Titleaddtobagerror").show().fadeOut(5000);
					isError=true;
					
				}
			
				//For MSD
				var isMSDEnabled =  $("input[name=isMSDEnabled]").val();								
				if(isMSDEnabled === 'true')
				{
				//console.log(isMSDEnabled);
				var isApparelExist  = $("input[name=isApparelExist]").val();
				//console.log(isApparelExist);				
				var salesHierarchyCategoryMSD =  $("input[name=salesHierarchyCategoryMSD]").val();
				//console.log(salesHierarchyCategoryMSD);
				var rootCategoryMSD  = $("input[name=rootCategoryMSD]").val();
				//console.log(rootCategoryMSD);				
				var productCodeMSD =  $("input[name=productCodeMSD]").val();
				//console.log(productCodeMSD);				
				var priceformad =  $("input[id=price-for-mad]").val();
				//console.log(priceformad);				
				
				if(typeof isMSDEnabled === 'undefined')
				{
					isMSDEnabled = false;						
				}
				
				if(typeof isApparelExist === 'undefined')
				{
					isApparelExist = false;						
				}	
				
				if(Boolean(isMSDEnabled) && Boolean(isApparelExist) && (rootCategoryMSD === 'Clothing'))
					{					
					ACC.track.trackAddToCartForMAD(productCodeMSD, salesHierarchyCategoryMSD, priceformad,"INR");
					}	
				}
				//End MSD
				//TPR-5193
				/*if(typeof utag !="undefined"){
					utag.link({
						event_type          : "exchange_success",
						exchange_brand      : brandExchange ,
						exchange_condition  : exchangeCondition ,
						exchange_l3         : l3 ,
						couponcode_exchange : couponValue ,
						product_id          : productCode
					});
				}*/
			},
			complete: function(){
		        $('#ajax-loader').hide();
		        //TISPRDT-2439
		        if(!isError)
		        	{
		        	$(".Exchange > p").removeClass("active mobile");
					$(".Exchange-overlay").remove();
					$("body").removeClass("no-scroll");
		        	}
		       
		    },
			error : function(resp) {
				//alert("Add to Bag unsuccessful: "+resp.responseText);
			}
		});
	},
	//Exchange Ends
	
	
	displayAddToCart: function (cartResult,formId,isUpdateCartCount)
	{
		var formElement=$("#"+formId);
		if (typeof ACC.minicart.updateMiniCartDisplay == 'function')
		{
			//ACC.minicart.updateMiniCartDisplay();
		}
		var productCode = $('[name=productCodePost]', formElement).val();
		var quantity = 0;
		
		$.ajax({
			url: ACC.config.encodedContextPath + "/cart/miniCart/TOTAL",
			returnType:"JSON",
			type:"GET",
			cache:false,
			success:function(cartData){
				cartData = $.parseJSON(cartData);
				quantity = parseInt(cartData.masterMiniCartCount);
				$("span.js-mini-cart-count,span.js-mini-cart-count-hover,span.responsive-bag-count").text(quantity);
		        //ACC.track.trackAddToCart(productCode, quantity, cartResult.cartData);
			}
		});
	},
	resetAllPLP:function(){
		if($('#pageType').val()==="category"){
			var resetURL = window.location.href;
			resetURL = resetURL.split("?");
			if(resetURL instanceof Array){
				resetURL = resetURL[0];
				$("a.reset").attr("href",resetURL);
		}
		}
	   if($('#pageType').val()==="offerlisting"){
			var resetURL = window.location.href;
			resetURL = resetURL.split("?");
			if(resetURL instanceof Array){
				resetURL = resetURL[0];
				//$("a.reset").attr("href",resetURL);
			}
			$("a.reset").attr("href",resetURL);
			var resetOfferURL = window.location.href;
			if(resetOfferURL.indexOf("/o") > -1)
			{
				resetOfferURL = resetOfferURL.split("&");
				if(resetOfferURL instanceof Array){
					resetOfferURL = resetOfferURL[0];
				}
				$("a.reset").attr("href",resetOfferURL);
			}
		}
	   if($('#pageType').val()==="sellerlisting"){
		   var resetURL = window.location.href;
			resetURL = resetURL.split("?");
			if(resetURL instanceof Array){
				resetURL = resetURL[0];
			}
			$("a.reset").attr("href",resetURL);
	   }
	   
		if((typeof(utag_data) !== "undefined")){
			var pageType = utag_data.page_type;
			if(pageType === "generic"){
				var resetURL = window.location.href;
				resetURL = resetURL.split("?");
				if(resetURL instanceof Array){
					resetURL = resetURL[0];
				}
				$("a.reset").attr("href",resetURL);
				var resetOfferURL = window.location.href;
				if(resetOfferURL.indexOf("/o") > -1)
				{
					resetOfferURL = resetOfferURL.split("&");
					if(resetOfferURL instanceof Array){
						resetOfferURL = resetOfferURL[0];
					}
					$("a.reset").attr("href",resetOfferURL);
				}
//				if(resetOfferURL.indexOf("/s") > -1)
//				{
//					resetOfferURL = resetOfferURL.split("&");
//					if(resetOfferURL instanceof Array){
//						resetOfferURL = resetOfferURL[0];
//					}
//					$("a.reset").attr("href",resetURL+"?resetAll=true");
//				}
			}
			}
	},
	departmentRemoveJsHack:function(){
		if((typeof(utag_data) !== "undefined")){
		var pageType = utag_data.page_type;
		//console.log(utag_data);
		if(pageType == "product"){
			/*$("div.facet-name.js-facet-name h4.tree-dept").css("display","none");*/
			/*$("li.facet.js-facet.deptType").remove();*/
		}
		}
	},
	brandFilter: function(){
		$(document).on("keyup",'input[class="brandSearchTxt"]',function(){
			var facetTopValuesCnt=$("#facetTopValuesCnt").val();		
			var that = this, $allListElements =$('.facet_desktop ul.js-facet-list > li.filter-brand').find("span.facet-label");
		    //get all matching elements
			var $matchingListElements = $allListElements.filter(function(i, li){
		    	var spanTxt=$(li).text().trim();		    	
		    	var lastIndexOfFirstBracket=spanTxt.lastIndexOf("(");
		    	var searchText = that.value.toUpperCase();		    	
		    	var listItemText = (lastIndexOfFirstBracket!=-1)?spanTxt.substring(0,lastIndexOfFirstBracket).trim().toUpperCase():spanTxt.toUpperCase();		    	
		    	if($(li).hasClass('marked')){	
		    		$(li).removeClass('marked');
		    	}		    	
		        return ~listItemText.indexOf(searchText);
		    });		    		    
			//If top value is configured to be greater than 0
		    if(~~facetTopValuesCnt!=0)
		    {
		    	var remainingFacetValuesCnt=$("#remainingFacetValuesCnt").val();
			    $(".brand .js-facet-top-values").hide();
				$(".brand .js-facet-list.js-facet-list-hidden").show();
				$allListElements.hide();
				var matchingListElementsSize=$matchingListElements.length;			
				if($('input[class="brandSearchTxt"]').val() == "") {//If text box is blank 				
					$(".brand .js-facet-top-values").show();
					$(".brand .js-facet-list.js-facet-list-hidden").hide();
					
					$(this).parents(".js-facet").find(".js-more-facet-values span").text((~~remainingFacetValuesCnt)-(~~facetTopValuesCnt));
					if($allListElements.length>facetTopValuesCnt)
					{
						$(this).parents(".js-facet").find(".js-more-facet-values").show();					
						$(this).parents(".js-facet").find(".js-less-facet-values").hide();
					}
			    }
				else{//If text box has data
					$.each($matchingListElements,function(index,element)
					{
						index<facetTopValuesCnt?$(this).css('display','block'):$(this).addClass('marked');					
					});
					if(matchingListElementsSize>0 && matchingListElementsSize>facetTopValuesCnt)
					{				
						$(this).parents(".js-facet").find(".js-more-facet-values span").text(~~matchingListElementsSize-~~facetTopValuesCnt);
						$(this).parents(".js-facet").find(".js-more-facet-values").show();
						$(this).parents(".js-facet").find(".js-less-facet-values").hide();
					}
					else if(matchingListElementsSize==0 || (matchingListElementsSize>0 && matchingListElementsSize<=facetTopValuesCnt))
					{
						$(this).parents(".js-facet").find(".js-more-facet-values").hide();
						$(this).parents(".js-facet").find(".js-less-facet-values").hide();
					}
				}
		    }
		    else
	    	{//If top value is configured to be 0
		    	if($('input[class="brandSearchTxt"]').val() == "")
		    	{
					$allListElements.show();
		    	}
				else{
					$allListElements.hide();
					$matchingListElements.show();
				}
	    	}
		});
	},
	
applyBrandFilter: function(){$allListElements = $('ul > li.filter-brand').find("span.facet-label");
	

  //Code changes done for TPR-432
  $(document).on("click",".applyBrandFilters",function() {	
	  var allBrands = "";
	  var fullQuery = "";
	  var brandQueryParams = "";
	  var finalQuery = "";
	  $('li.Brand').find('input[type="checkbox"]:checked').each(function(){	
			if ( $(this).parents('.facet-list').css('display') != 'none' ){
			var facetValue = $(this).parents('.filter-brand').find('input[name="facetValue"]').val();			
			fullQuery = $(this).parents('.filter-brand').find('input[name="q"]').val();
			allBrands = allBrands + ':brand:' + facetValue;	
			}		   					
	   });	  
	   //construct non brand query params
	    var currentQryParam = $('.currentQueryParamsApply').val();
	    var qryParamSplits = currentQryParam.split(':');
	    var newQryParam = '';
	    // take it as a key:value pair
	    for (var i = 0 ; i < qryParamSplits.length ; i = i + 2) {
	    	var paramKey = qryParamSplits[i];
	    	var paramValue = '';
	    	if (qryParamSplits.length > i + 1) {
	    		paramValue = qryParamSplits[i+1];
	    	}
	    	if (i == 0) {
	    		newQryParam += paramKey + ':' + paramValue + ':';
	    	} else if (paramKey !== 'brand') {
	    		newQryParam += paramKey + ':' + paramValue + ':';
	    	}
	    }
	    
	    if (newQryParam.endsWith(':')) {
	    	newQryParam = newQryParam.substring(0, newQryParam.length - 1);
	    }
	    
	    var finalQuery = newQryParam + allBrands;
	    //append non brand query and checked brands
	    $('.qValueForApply').val(finalQuery);
	   
	    // submit brand apply form
	    $('form#brandApply').submit();
     });
	},
	
	brandFilterCheckAll: function(){$allListElements = $('ul > li.filter-brand').find("span.facet-label");
	var url = "";
	var i = 0;
	var selectQueryParams = "";
	var searchCategory = "all";
	$(document).on("click",".brandSelectAll",function(){
		var checkText = $(this).text();
		var ischecked = null;
		if(checkText == "Check All"){
			ischecked = true;
		}else if(checkText == "Uncheck All"){
			ischecked = false;
		}
		//var ischecked= $(this).is(':checked');
		$(".filter-brand").each(function(){
			url = $(this).find("input[name=q]").val();	
			if(ischecked) {
				var arr = url.split(':');
				var currentBrand = "";
				if(i==0) {
					selectQueryParams = selectQueryParams + url;
					//get searchcategory value to append with brand checkall url
					if(url.indexOf(':category:') != -1){
						var urlAry = url.split(':');
						for (var j = 2; j <  urlAry.length; j = j + 2) { 
							if(urlAry[j].indexOf('category') != -1) {
								searchCategory = urlAry[j+1];
							}
						}
					}
				}
				else{
					//condition to avoid duplicate brand
					if(selectQueryParams.indexOf(arr[arr.length-1]) == -1) {
						currentBrand = arr[arr.length-2]+":"+arr[arr.length-1];
						selectQueryParams = selectQueryParams + ":"+currentBrand;
					}
				}
				i = i + 1;
				window.location.href = "?q="+selectQueryParams+"&searchCategory="+searchCategory+"&selectAllBrand=true";
			}
			else {
				var unselectQueryParams = "";
				var currentUrl = $(this).find("input[name=q]").val();
				var unselectArr = currentUrl.split(':');
				unselectQueryParams = unselectQueryParams + unselectArr[0] +":"+ unselectArr[1];
				
				for (var j = 2; j < unselectArr.length; j = j + 2) { 
					if(unselectArr[j].indexOf('brand') == -1) {
						unselectQueryParams = unselectQueryParams + ":" + unselectArr[j] +":"+ unselectArr[j+1];
					}
					if(unselectArr[j].indexOf('category') != -1) {
						searchCategory = unselectArr[j+1];
					}
				}
				window.location.href = "?q="+unselectQueryParams+"&searchCategory="+searchCategory+"&selectAllBrand=false";
			}
		});
	});
	},
	showTransientCart: function (ussid)
	{
		var dataString="ussid="+ussid;
		//AJAX CALL
		$
		.ajax({
			type : "GET",
			dataType : "json",
			url : ACC.config.encodedContextPath
					+ "/cart/showTransientCart",
			data:dataString,
			success : function(response) {
				
				
				
				$('.mini-transient-bag').remove();
				$('.mini-transient-bag-mobile').remove();		/*UF-47*/
				/*UF-277*/
				var transientCartHtml="<div class='mini-transient-bag' ><span class='mini-cart-close'>+</span><ul class='my-bag-ul'><li class='item'><ul><li><div class='product-img'><a href='"+ACC.config.encodedContextPath+response.productUrl+"'><img class='picZoomer-pic' src='"+response.productImageUrl+"'></a></div><div class='product'><p class='company'></p><h3 class='product-name'><a href='"+ACC.config.encodedContextPath+response.productUrl+"'>"+response.productTitle+"</a></h3><span class='addedText'>It's been added to your bag</span>";
				var transientCartHtmlMobile="<div class='mini-transient-bag-mobile'><span class='addedTextMobile'>Item added to Cart</span></div>";		/*UF-47*/
				if(typeof response.offer!=='undefined'){
					transientCartHtml+="<div class='transient-offer'>"+response.offer+"</div>";
				}
				
			
				transientCartHtml+="</div></li></ul><li class='view-bag-li'><a href='"+ACC.config.encodedContextPath+"/cart' class='go-to-bag mini-cart-checkout-button'>View Bag</a></li></ul></div>";
				$('.transient-mini-bag').append(transientCartHtml);
				$('body').append(transientCartHtmlMobile);
				/*LW-216*/
				if(typeof response.productType!=='undefined' && response.productType.toLowerCase() === "luxury"){
					$('.mini-transient-bag .product-img').append("<img class='luxury_ribbon' src='/_ui/responsive/common/images/Ribbon.png'>");
				}
				/*LW-216*/
				if ($("header .content .bottom").hasClass("active")){
					$("header .content .right>ul>li.transient-mini-bag .mini-transient-bag").css({
						"position": "fixed",
						"top": "80px",
						"right": "0px"
					});
		}
				
				setTimeout(function(){
					$('.mini-transient-bag').fadeOut(2000);
					$('.mini-transient-bag-mobile').fadeOut(2000);		/*UF-47*/
				},2000);
				setTimeout(function(){
					$('.mini-transient-bag').remove();
					$('.mini-transient-bag-mobile').remove();		/*UF-47*/
				},4000);
				
			},
			error : function() {
				
				
			}
		});

//END AJAX
		
	},	
	scrollForTransientCart: function ()
	{
		if($(window).width() > 773) {
			$("#cboxClose").click();
		} /*added as part of PRDI-90*/
			$('body.modal-open').css('overflow-y','hidden');
			$(".modal").modal('hide');
			$('body').css('overflow-y','auto');
		//} /*commented as part of PRDI-90*/
		
	}
};

/*TPR-655 START*/
$(document).on('click','.go-to-bag.mini-cart-checkout-button',function(){
	utag.link({
		link_obj: this,
		link_text: 'hover_goto_mybag',
		event_type : 'hover_mybag'
	});
})	
/*TPR-655 END*/

//Code changes start for TPR -168//

//For AJAX Call  
$(document).on("click",'#applyCustomPriceFilter',function(){
	 
	

					// construct custom price query params	
					/* var minPriceSearchTxt ="";
	                var maxPriceSearchTxt ="";
	                if(isNaN($("#customMinPriceMob").val()) && isNaN($("#customMaxPriceMob").val())){
					minPriceSearchTxt = ($('.minPriceSearchTxt').val() == null || $('.minPriceSearchTxt').val() == "") ? 0 : $('.minPriceSearchTxt').val() ;
				    maxPriceSearchTxt = ($('.maxPriceSearchTxt').val() == null || $('.maxPriceSearchTxt').val() == "") ? 99999999 : $('.maxPriceSearchTxt').val() ;	
	                }
	                else{
	                	minPriceSearchTxt = ($('#customMinPriceMob').val() == null || $('#customMinPriceMob').val() == "") ? 0 : $('#customMinPriceMob').val() ;
					    maxPriceSearchTxt = ($('#customMaxPriceMob').val() == null || $('#customMaxPriceMob').val() == "") ? 99999999 : $('#customMaxPriceMob').val() ;	
	                }*/
	 				//TISQAUATS-52 starts
	                var minPriceSearchTxt = ($('#customMinPrice').val() == null || $('#customMinPrice').val() == "") ? 0 : $('#customMinPrice').val() ;
	                var maxPriceSearchTxt = ($('#customMaxPrice').val() == null || $('#customMaxPrice').val() == "") ? 99999999 : $('#customMaxPrice').val() ;
	                //TISQAUATS-52 ends
					var currentQryParam = $('.currentPriceQueryParams').val();
					var facetValue = $('.facetValue').val();

					var queryParamsAry = currentQryParam.split(':');
					var nonPriceQueryParams = "";					
					

					if(minPriceSearchTxt > 99999999 || maxPriceSearchTxt > 99999999){						

						return false;
					}				
					else{
						facetValue = "₹" + minPriceSearchTxt + "-" + "₹"
								+ maxPriceSearchTxt;
						$('#facetValue').val(facetValue);
		
						// Iterate and get all checked brand values
						var Price = "₹" + minPriceSearchTxt + "-" + "₹"
								+ maxPriceSearchTxt;
						for (var i = 0; i < queryParamsAry.length; i = i + 2) {					
							if (queryParamsAry[i].indexOf('price') == -1) {								
								if (nonPriceQueryParams != "") {
									nonPriceQueryParams = nonPriceQueryParams + ':'
											+ queryParamsAry[i] + ':'
											+ queryParamsAry[i + 1];									
								} else {								
									nonPriceQueryParams = queryParamsAry[i] + ':'
											+ queryParamsAry[i + 1];
								
								}
							}
						}
						

						$('.qValueForCustomPrice').val(
								nonPriceQueryParams + ":price:" + Price);



						var dataString = null;
						var nonEmptyDataString= null;
						
						// generating datastring and postAjaxURL
						$("#customPriceFilter").find('input[type="hidden"]').each(function(){

							if(dataString == null){
								dataString = $(this).attr('name')+"="+$(this).val();
							}
							else{
								dataString = dataString + ("&"+$(this).attr('name')+"="+$(this).val());
							}
							
							if($(this).val().length >0){
								
								if(nonEmptyDataString == null){
									nonEmptyDataString = $(this).attr('name')+"="+$(this).val();
								//	alert("alert(nonEmptyDataString)"+nonEmptyDataString);
								}
								else{
									nonEmptyDataString = nonEmptyDataString + ("&"+$(this).attr('name')+"="+$(this).val());
								//	alert("nonEmptyDataString"+nonEmptyDataString);
								}
							}
						})
						
						// generating postAjaxURL
						var browserURL = window.location.href.split('?');
						
						//INC144319487 starts 
						var pageURL=null;
						if (browserURL[0].indexOf("c-")>0)
							pageURL = browserURL[0]+'/page-1?'+nonEmptyDataString.replace(/%/g,"%25").replace(/ - /g,"+-+").replace(/:/g,"%3A");
						else
							pageURL = browserURL[0]+'page-1?'+nonEmptyDataString.replace(/%/g,"%25").replace(/ - /g,"+-+").replace(/:/g,"%3A");
						 
						//INC144319487 ends
						
						// generating request mapping URL
						var requiredUrl="";
						var action = $("#customPriceFilter").attr('action');
						if($("#isCategoryPage").val() == 'true'){
							if ($("input[name=customSku]").val()) {
								var collectionId = $("input[name=customSkuCollectionId]").val();
								requiredUrl = '/CustomSkuCollection/'+collectionId+'/getFacetData';
							}
							else {
								action = action.split('/c-');
								action = action[1].split('/');
								requiredUrl = "/c-"+action[0];
								requiredUrl += "/getFacetData";
							}
							
						} else {
							if(action.indexOf("/getFacetData") == -1){
							
								//if(action.indexOf("offer") > -1 || action.indexOf("viewOnlineProducts") > -1 || action.indexOf('/s/') > -1 || action.indexOf('/collection/') > -1){
								if(action.indexOf("offer") > -1 || action.indexOf("viewOnlineProducts") > -1 || action.indexOf('/s/') > -1 ){
									requiredUrl = action.concat("/getFacetData");
								} 
								else if ($("input[name=customSku]").val()) {
									var collectionId = $("input[name=customSkuCollectionId]").val();
									requiredUrl = '/CustomSkuCollection/'+collectionId+'/getFacetData';
								}
								else{
									//requiredUrl = action.concat("getFacetData");
									// INC_11277 start
									if ((/.*\/$/g).test(action)) {
										requiredUrl = action.concat("getFacetData");
									}
									else{
										requiredUrl = action.concat("/getFacetData");
									} 
									// INC_11277 end
								}
							}
							else{
								requiredUrl = action;
							}
						}
						// AJAX call
						// Sprinner added for AJAX Call TPR-1105,TPR-1106
						var browserURL = window.location.href.split('?');
						var staticHost=$('#staticHost').val();
						$("body").append("<div id='no-click' style='opacity:0.60; background:black; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
						$("body").append('<div class="loaderDiv" style="position: fixed; left: 50%;top: 50%;"><img src="'+staticHost+'/_ui/responsive/common/images/red_loader.gif" class="spinner"></div>');
						
						//TPR-645 start  -- INC_11511  fix--h3 tag done
						filterValue = (minPriceSearchTxt+"-"+maxPriceSearchTxt).replace(/,/g,"");
						filterName = $(this).parents('li.facet.js-facet').find('div.facet-name.js-facet-name h3').text().trim();
						//onFilterAddAnalytics(filterName,filterValue);
						filterDataAjax(requiredUrl,encodeURI(dataString),pageURL);
						//TPR-645 end
					}
					
				});
//End of Custom Price Filter

function isNumber(evt) {
	evt = (evt) ? evt : window.event;
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	if (charCode > 31 && (charCode < 48 || charCode > 57 )) {
		return false;
	}
	return true;
}

$(document).on("change",'.facet_desktop .filter-price',function(){
			var prices = splitPrice($(this).find('form').find(
					'input[name=facetValue]').val());			
			$('#customMinPrice').val(prices[0]);
			$('#customMaxPrice').val(prices[1]);
			$('#applyCustomPriceFilter').click();
			return false;

		});

//Splits priceValue:Rsxxx-Rsyyy to [xxx, yyy]
function splitPrice(value) {
	var priceRange = null;	
	if(value.indexOf('-') > -1)  // Fix for TISPRD-8267
	{   
		priceRange = value.split("-");		
	}
	else if(value.indexOf("and Above") > -1)  // Fix for TISPRD-8267
	{
		priceRange = value.split("and Above");		
	}	
	var minPrice = priceRange[0].substring(1);
	var maxPrice = priceRange[1].substring(1);

	return [ minPrice, maxPrice ];
}


//Gets the value of a query string parameter.
function queryParam(name) {
	var reges = '[\\?&]' + name + '=([^&#]*)';
	rex = new RegExp(reges);
	results = rex.exec(window.location.search);
	if (results === null) {
		return '';
	} else {
		return decodeURIComponent(results[1].replace(/\+/g, ' '));
	}
}

//Loads the price range textboxes with previously
//selected ranges.
$(document).ready(function() {
	
	loadPriceRange();	
	
});


function loadPriceRange(){
	//console.log("Pricing")
	var q = queryParam('q');
	var priceRange = '';
	var pvStr = ':price:';	
	
	if (q.indexOf(pvStr) > -1) {		
		priceRange = q.substring(q.indexOf(pvStr) + pvStr.length);
		if (priceRange.indexOf(':') > -1) {
			priceRange = priceRange.substring(0, priceRange.indexOf(':'));
		}		
		var prices = splitPrice(priceRange);		
		$('.minPriceSearchTxt').val(prices[0]);
		$('.maxPriceSearchTxt').val(prices[1]);		
		/*$('li.price').find('div.facet-name').hide();*/
		$('li.price').find('div.facet-values .facet-list.js-facet-list').hide();
		$('.priceBucketExpand').show();
	}

}
//Code changes end for TPR -168//
