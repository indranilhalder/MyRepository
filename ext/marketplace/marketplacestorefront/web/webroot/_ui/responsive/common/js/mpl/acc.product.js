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
			ACC.product.sendAddToBag("addToCartForm");
			event.preventDefault();
			return false;
		});
		
	
		$(document).off('click', '#addToCartFormQuick').on('click', '#addToCartFormQuick', function(event) { 
		   
			 $("#qty1").val($("#quantity").val());
			 
				if($("#sizeSelected").val()!='no'){
				ACC.product.sendAddToBagQuick("addToCartFormQuick");
				
				}else{
					$("#addToCartFormQuickTitle").html("<font color='#ff1c47'>" + $('#selectSizeId').text() + "</font>");
					$("#addToCartFormQuickTitle").show().fadeOut(6000);
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
			 if($('#variant.size-g option:selected').val()!="#")
			 {
				ACC.product.sendAddToBagSizeGuide("addToCartSizeGuide");
			 
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
						if (data.indexOf("cnt:") >= 0) {
							(isBuyNow == true) ? isSuccess = true
									: isSuccess = false;
							
							
							//$("#" + formId + "TitleSuccess").html("");
							//$("#" + formId + "TitleSuccess").html("<font color='#00CBE9'>"+ $('#addtobag').text()+ "</font>");
							//$("#" + formId + "TitleSuccess").show().fadeOut(5000);
							//$("#" + formId + "Title.sellerAddToBagTitle").show().fadeOut(5000);
							//$("#" + formId + " " + ".addToCartSerpTitle").show().fadeOut(5000);
							if(!isBuyNow){
								ACC.product.showTransientCart(ussid);
								ACC.product.scrollForTransientCart();
							}
							
							// ACC.product.displayAddToCart(data,formId,false);
							$("span.js-mini-cart-count,span.js-mini-cart-count-hover,span.responsive-bag-count").text(data.substring(4));
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
						} else {
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
					},
					complete : function() {
						$('#ajax-loader').hide();
						forceUpdateHeader();
					},
					error : function(resp) {
						// alert("Add to Bag unsuccessful");
					}
				});
	}
},
	sendAddToBagQuick: function(formId){
		 var input_name="qty";
		  var stock_id="stock";
		 var dataString=$('#'+formId).serialize();	
		 var quantity = $("#"+formId+" :input[name='" + input_name +"']").val(); 
		 var stock = $("#"+formId+" :input[name='" +  stock_id +"']").val(); 
		 var quantity = $("#"+formId+" :input[name='" + input_name +"']").val(); 
		 var stock = $("#"+formId+" :input[name='" +  stock_id +"']").val(); 
		 var ussid=$('#ussid_quick').val();
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
		        $('#ajax-loader').show();
		    },
			success : function(data) {
				if(data.indexOf("cnt:") >= 0){
				//$("#"+formId+"TitleSuccess").html("");
				//$("#"+formId+"TitleSuccess").html("<font color='#00CBE9'>"+$('#addtobag').text()+"</font>");

				//$("#"+formId+"TitleSuccess").show().fadeOut(5000);

				//$("#"+formId+"Title.sellerAddToBagTitle").show().fadeOut(5000);
				//$("#"+formId+" "+".addToCartSerpTitle").show().fadeOut(5000);
				ACC.product.showTransientCart(ussid);
				ACC.product.scrollForTransientCart();
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
				}
				else if(data=="outofinventory"){
					 //$("#"+formId+"noInventory").html("<font color='#ff1c47'>" + $('#addToCartFormnoInventory').text() + "</font>");
					$("#addToCartFormnoInventory").show().fadeOut(6000);
			   	     return false;
				}
				else if(data=="willexceedeinventory"){
					 $("#"+formId+"excedeInventory").html("<font color='#ff1c47'>" + $('#addToCartFormexcedeInventory').text() + "</font>");
					 $("#"+formId+"excedeInventory").show().fadeOut(6000);
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
				//End MSD
				
			},
			complete: function(){
		        $('#ajax-loader').hide();
		    },
			error : function(resp) {
			//	alert("Add to Bag unsuccessful");
			}
		});
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
		$('input[class="brandSearchTxt"]').keyup(function(){
		    var that = this, $allListElements = $('ul > li.filter-brand').find("span.facet-label");
		    var $matchingListElements = $allListElements.filter(function(i, li){
		        var listItemText = $(li).text().toUpperCase(), searchText = that.value.toUpperCase();
		        return ~listItemText.indexOf(searchText);
		    });
		    if(($matchingListElements).size() > 0) {
			    $(this).parents(".js-facet").find(".js-facet-top-values").hide();
				$(this).parents(".js-facet").find(".js-facet-list-hidden").show();
	
				$(this).parents(".js-facet").find(".js-more-facet-values").hide();
				$(this).parents(".js-facet").find(".js-less-facet-values").show();
		    }
		    if(that.value.toUpperCase() == ''){
		    	$(this).parents(".js-facet").find(".js-facet-top-values").show();
				$(this).parents(".js-facet").find(".js-facet-list-hidden").hide();

				$(this).parents(".js-facet").find(".js-more-facet-values").show();
				$(this).parents(".js-facet").find(".js-less-facet-values").hide();
		    }
			    
		    $allListElements.hide();
		    $(".brand .js-facet-top-values").hide();
			$(".brand .js-facet-list.js-facet-list-hidden").show();
		    $matchingListElements.show();
		    
		    if($('input[class="brandSearchTxt"]').val() == "") {
		    	
		    	$(".brand .js-facet-top-values").show();
				$(".brand .js-facet-list.js-facet-list-hidden").hide();
		    }
		});
	},
	
applyBrandFilter: function(){$allListElements = $('ul > li.filter-brand').find("span.facet-label");
	
//	$(document).on("click",".applyBrandFilters",function(){
//		
//		//Iterate and get all checked brand values
//		   var allBrands = "";
//		  $('li.Brand').find('input[type="checkbox"]:checked').each(function(){	
//				if ( $(this).parents('.facet-list').css('display') != 'none' ){
//				var facetValue = $(this).parents('.filter-brand').find('input[name="facetValue"]').val();
//				allBrands = allBrands + ':brand:' + facetValue;
//				}		   					
//		   });
//		  
//		//construct non brand query params
//		  var currentQryParam = $('.currentQueryParamsApply').val();
//		  var queryParamsAry = currentQryParam.split(':');
//		  var nonBrandQueryParams = "";
//			for (var i = 0; i <  queryParamsAry.length; i = i + 2) { 
//				if(queryParamsAry[i].indexOf('brand') == -1) {
//					if(nonBrandQueryParams != ""){
//						nonBrandQueryParams = nonBrandQueryParams +':'+ queryParamsAry[i] +':'+queryParamsAry[i+1];
//					}else{
//						nonBrandQueryParams = queryParamsAry[i] +':'+queryParamsAry[i+1];
//					}
//				}
//			}
//		   //append non brand query and checked brands
//		   $('.qValueForApply').val(nonBrandQueryParams+allBrands);
//		   // submit brand apply form
//		   $('form#brandApply').submit();
//	});

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
				var transientCartHtml="<div class='mini-transient-bag' ><span class='mini-cart-close'>+</span><ul class='my-bag-ul'><li class='item'><ul><li><div class='product-img'><a href='"+ACC.config.encodedContextPath+response.productUrl+"'><img class='picZoomer-pic' src='"+response.productImageUrl+"'></a></div><div class='product'><p class='company'></p><h3 class='product-name'><a href='"+ACC.config.encodedContextPath+response.productUrl+"'>"+response.productTitle+"</a></h3><span class='addedText'>has been added to your cart</span>";
				
				if(typeof response.offer!=='undefined'){
					transientCartHtml+="<div class='transient-offer'>"+response.offer+"</div>";
				}
				
				transientCartHtml+="</div></li></ul><li class='view-bag-li'><a href='"+ACC.config.encodedContextPath+"/cart' class='go-to-bag mini-cart-checkout-button'>View Bag</a></li></ul></div>";
				$('.transient-mini-bag').append(transientCartHtml);
				
				if ($("header .content .bottom").hasClass("active")){
					$("header .content .right>ul>li.transient-mini-bag .mini-transient-bag").css({
						"position": "fixed",
						"top": "80px",
						"right": "0px"
					});
		}
				
				setTimeout(function(){
					$('.mini-transient-bag').fadeOut(2000);
				},2000);
				setTimeout(function(){
					$('.mini-transient-bag').remove();
				},4000);
				
			},
			error : function() {
				console.log("Error while getting transient cart");
				
			}
		});

//END AJAX
		
	},	
	
	scrollForTransientCart: function ()
	{
		if($(window).width() > 773) {
			$("#cboxClose").click();
			$('body.modal-open').css('overflow-y','hidden');
			$(".modal").modal('hide');
			$('body').css('overflow-y','auto');
		} 
		
	}
};

//Code changes start for TPR -168//

var button_my_button = "#applyCustomPriceFilter";
$(button_my_button)
		.click(
				function() {
					// construct custom price query params
					var minPriceSearchTxt = $('.minPriceSearchTxt').val();
					var maxPriceSearchTxt = $('.maxPriceSearchTxt').val();
					var currentQryParam = $('.currentQueryParamsApply').val();
					var facetValue = $('.facetValue').val();

					var queryParamsAry = currentQryParam.split(':');
					var nonPriceQueryParams = "";
					facetValue = "₹" + minPriceSearchTxt + "-" + "₹"
							+ maxPriceSearchTxt;
					$('#facetValue').val(facetValue);

					// Iterate and get all checked brand values
					var Price = "₹" + minPriceSearchTxt + "-" + "₹"
							+ maxPriceSearchTxt;

					$('li.Price').find('input[type="text"]').each(
							function() {
								if ($(this).parents('.facet-list').css(
										'display') != 'none') {
									var facetValue = $(this).parents(
											'.filter-Price').find(
											'input[name="facetValue"]').val();
									Price = Price;
								}
							});

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
							nonPriceQueryParams + ":priceValue:" + Price);

					// submit brand apply form
					$('form#customPriceFilter').submit();
				});
//End of Custom Price Filter

function isNumber(evt) {
	evt = (evt) ? evt : window.event;
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	if (charCode > 31 && (charCode < 48 || charCode > 57)) {
		return false;
	}
	return true;
}

$(".filter-price").click(
		function() {
			var prices = splitPrice($(this).find('form').find(
					'input[name=facetValue]').val());
			$('#customMinPrice').val(prices[0]);
			$('#customMaxPrice').val(prices[1]);
			$('#applyCustomPriceFilter').click();
			return false;

		});

//Splits priceValue:Rsxxx-Rsyyy to [xxx, yyy]
function splitPrice(value) {
	var priceRange = value.split('-');
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
	var q = queryParam('q');
	var priceRange = '';
	var pvStr = ':priceValue:';
	if (q.indexOf(pvStr) > -1) {
		priceRange = q.substring(q.indexOf(pvStr) + pvStr.length);
		if (priceRange.indexOf(':') > -1) {
			priceRange = priceRange.substring(0, priceRange.indexOf(':'));
		}
		var prices = splitPrice(priceRange);
		$('#customMinPrice').val(prices[0]);
		$('#customMaxPrice').val(prices[1]);
		$('li.price').find('div.facet-name').remove();
		$('li.price').find('div.facet-values').remove();
	}
});

//Code changes end for TPR -168//




	





