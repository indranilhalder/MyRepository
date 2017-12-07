ACC.quickview = {

	_autoload: [
		"bindToUiCarouselLink"
	],
		
		
	initQuickviewLightbox:function(){
		ACC.product.enableAddToCartButton();
		//ACC.product.bindToAddToCartForm();
		ACC.product.addToBag();
		ACC.product.enableStorePickupButton();
	},
		
	refreshScreenReaderBuffer: function ()
	{
		// changes a value in a hidden form field in order
		// to trigger a buffer update in a screen reader
		$('#accesibility_refreshScreenReaderBufferField').attr('value', new Date().getTime());
		$("#showquick").click(function() {
			$("#showPriceBreakupquick").slideToggle("fast");
		})
	},
	onQuantitySelectDropDownQuick: function ()
	{
		$("select#quantity_dropdown.variant-select").on('change',function(e){
			//console.log("================>called while size is selected================>");
		$('input[name="qty"]').val(this.value);

		});
	},
	
	onSizeSelectDropDownQuickView: function ()//First Method to be called in size select ajax call
	{	//Attaching click event on size variant <li>
		$("select#variant_dropdown.variant-select").on('change',function(e){
			//console.log("================>called while size is selected================>");
			
			var currentSelectedElement=this;
			var productCode="";
			productCode=$(currentSelectedElement).find(':selected').attr('data-productCode');
			
			var href=this.value;
			
			
			$.ajax({
			    url: href,
			    dataType: "html",
			    success: function(data) {	
			        $("#cboxLoadedContent").html(data);
			        
			    }
			});	
		});
	},

	bindToUiCarouselLink: function ()
	{
		var titleHeader = $('#quickViewTitle').html();
		$(".js-reference-item,.comparison-table .product-tile").colorbox({
			close:'<span class="glyphicon glyphicon-remove"></span>',
			title: titleHeader,
			maxWidth:"100%",
			onComplete: function ()
			{
				quickviewGallery();
				ACC.quickview.refreshScreenReaderBuffer();
				ACC.quickview.initQuickviewLightbox();
				ACC.ratingstars.bindRatingStars($(".quick-view-stars"));
				ACC.quickview.onSizeSelectDropDownQuickView();
				ACC.quickview.onQuantitySelectDropDownQuick();
				//UF-24 thumbnail image load
				//Moved after quickviewGallery in case utag is undefined
				
				tealiumBrokenImageQuickview();
			},

			onClosed: function ()
			{
				ACC.quickview.refreshScreenReaderBuffer();
				if((window.top==window) && $("body").hasClass("page-cartPage")) {
				    // You're not in a frame, so you reload the site.
				    window.setTimeout('location.reload()'); 
			     }
				$('.zoomContainer').remove();			/*UF-50*/
				//console.log($('.zoomContainer').length);
			}
		});
	}
	
};

function tealiumBrokenImageQuickview(){
var brokenImageCount=0;	
	$('#cboxWrapper img').each(function(){
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

function quickviewGallery() {
	$(document).ready(function(){
		//UF-24 thumbnail image load
		$('.main-image a img.picZoomer-pic').on('load', function(){
			console.log("js image is loaded");
			var mainImageHeight = $(".main-image").find("img.picZoomer-pic").height();
			console.log("js mainImageHeight is " + mainImageHeight);
			var thumbnailImageHeight = (mainImageHeight / 5);
			$(".imageList ul li img").css("height", thumbnailImageHeight);
			console.log("js thumbnailImageHeight is " + thumbnailImageHeight);
		 	$("#previousImage").css("opacity","0.5");
		 	$("#nextImage").css("opacity","1");
		 	var listHeight = $(".imageList li").height();
		 	console.log("js listHeight is " + listHeight);
		 	console.log("js previousImage length is " + $("#previousImage").length);
		 	if($("#previousImage").length){
		 		$(".imageList").css("height",(listHeight*imagePageLimit)+"px");
		 		$(".productImageGallery").css("height",(listHeight*imagePageLimit+100)+"px");
		 	}
		 	$(".imageListCarousel").show();
		 	
		 	if ('ontouchstart' in window) {
		 		$(".quick-view-popup #variantForm .select-size span.selected").next("ul").hide();
				  $(".quick-view-popup #variantForm .select-size span.selected").click(function(){
					  $(this).next("ul").toggleClass("select_height_toggle");
				  });
				}
		 	
		 	/*$(".productImageGallery img").click(function(e) {
				TPR-643 starts
					utag.link({
						link_obj: this, 
						link_text: 'pdp_image_click' , 
						event_type : 'pdp_image_click' 
					});
					TPR-643 ends
			});*/
		}).each(function() {
	    	  if(this.complete) $(this).load();
	    });
	 	
	 });
	if($("#cboxContent #cboxLoadedContent .quickview.active")[0].offsetHeight < $("#cboxContent #cboxLoadedContent .quickview.active")[0].scrollHeight){
		$("#cboxContent #cboxLoadedContent .quickview.active").css("height",$("#cboxContent #cboxLoadedContent .quickview.active")[0].scrollHeight);
		$("#cboxContent").css("max-height",$("#cboxContent #cboxLoadedContent .quickview.active")[0].scrollHeight);
	}
	else{
		$("#cboxContent #cboxLoadedContent .quickview.active").css("height","565px");
		$("#cboxContent").css("max-height","565px");
	}
	
}

////jsp to js move
//Enable CTA's on page load (UF-88)

function removedisabled()
{
	    $(".js-add-to-cart-qv").removeAttr('disabled');
	    $("#addToCartButtonQuick").removeAttr('disabled');
}


function setSizeforAkamai()
{
	 //AKAMAI Fix
	 
	 	var url = window.location.href;	
		if (url.indexOf("selectedSize=true")>=0 && typeof productSizeQuickVar !== "undefined")//>= 0  ==-1
			{
			 $('.select-size').find('span').remove();
			 $(".select-size").append("<span class='selected quickViewSelect'>"+productSizeQuickVar+"</span>");
			} 
}
function isOOSQuick(){
	var totalOptions="";
	if(productCategoryType!='HomeFurnishing')
		{
	totalOptions = $("ul[label=sizes] li").length;
		}
	else if(document.getElementById("variant_dropdown")!=null)
		{
		totalOptions=document.getElementById("variant_dropdown").length;
		}
	//PRDI-445 fix ends
	totalOptions = totalOptions -1;
	var disabledOption = $("ul[label=sizes] li.strike").length;
	if(totalOptions == disabledOption){
		
		return true;
	}else{
		
		return false;
	}
}

//function setBuyBoxDetails()
function setBuyBoxDetails(msiteBuyBoxSeller) // CKD:TPR-250
{
	var productCode = productCodeQuickView;//$("#productCodePost").val();
	var sellerName = '';
	var sellerId = ''; 
	var spPrice ='';
	var mrpPrice = '';
	var mop = '';
	var savingsOnProduct= '';
	var stock='';
	
	var variantCodesJson = "";
	if(typeof(variantCodesPdp)!= 'undefined' && variantCodesPdp!= ""){
		variantCodes = variantCodesPdp.split(",");
		variantCodesJson = JSON.stringify(variantCodes);
	}
	//var code = productCode+","+variantCodesPdp;
	var requiredUrl = ACC.config.encodedContextPath + "/p-" + productCode
	+ "/buybox";
	var availibility = null;
		//var dataString = 'productCode=' + productCode;		
		$.ajax({
			contentType : "application/json; charset=utf-8",
			url : requiredUrl,
			data : {productCode:productCode,variantCode:variantCodesJson,
				sellerId:msiteBuyBoxSeller //CKD:TPR-250
				},
			cache : false,
			dataType : "json",
			success : function(data) {
				var oosMicro=data['isOOsForMicro'];
				var stockInfo = data['availibility'];
				availibility = stockInfo;
				if(productCategoryType!='HomeFurnishing')
					{
				$.each(stockInfo,function(key,value){
					$("ul[label=sizes] li a").each(function(){
						
				if(typeof($(this).attr("href"))!= 'undefined' && $(this).attr("href").toUpperCase().indexOf(key)!= -1 && value == 0){ 
								
								$(this).attr("disabled",true);
								$(this).parent("li").addClass("strike");
								
								
								/*$(this).css({
									"color": "gray"
							});*/
								$(this).on("mouseenter",function(){
									$(this).parent("li").css("background","#fff");
									
								});
								
								 $(this).on('click', function(event) {
								      event.preventDefault();
								      return false;
								   });
							//$(this).parent().css("border-color","gray");
							}
						
					});
				});
				
			}
				//alert("...>>"+data['sellerArticleSKU']+"<<"+productCode+"..."+productCodeQuickView);
				
				if(typeof data['sellerArticleSKU'] === 'undefined')
					{					
					$("#addToCartButtonQuick-wrong").show();
					/*TPR-1772 functionality change for non-saleable products*/
					/*$("#buyNowButtonQuick-wrong").show();*/
					/*$('#buyNowButton').hide();*/
					$(".js-add-to-cart-qv").attr("disabled","disabled");
					/*TPR-1772 functionality change for non-saleable products*/
					$("#addToCartButtonQuick").hide();
					$("#dListedErrorMsg").show();				
					return false;
					}					
				
				if(data['sellerArticleSKU']==null)
					{
					$("#addToCartButtonQuick-wrong").show();
					/*TPR-1772 functionality change for non-saleable products*/
					/*$("#buyNowButtonQuick-wrong").show();*/
					/*$('#buyNowButton').hide();*/
					$(".js-add-to-cart-qv").attr("disabled","disabled");
					/*TPR-1772 functionality change for non-saleable products*/
					$("#addToCartButtonQuick").hide();
					$("#dListedErrorMsg").show();				
					return false;
					}
				
				$("#addToCartButtonQuick-wrong").hide();
				$("#dListedErrorMsg").hide();	
				
				//alert(data['sellerArticleSKU']+".."+data['sellerName']);
				spPrice = data['specialPrice'];
				mrpPrice = data['mrp'];
				mop = data['price'];
				savingsOnProduct= data['savingsOnProduct'];
				
				$("#ussid_quick").val(data['sellerArticleSKU']);
				stock = data['availablestock'];
				//INC144316286
				//$("#stock").val(stock);				
				if($('#pageType').val() == 'wishlist' || $('#pageType').val() == 'product') 
				{ 
					$("#addToCartFormQuick").closest('[id]').find('#stock').val(stock); 
				} 
				else { 
					$("#stock").val(stock);
				} 
				
				
				var allStockZero = data['allOOStock'];
				sellerId = data['sellerId'];
				$("#sellerSelId").val(sellerId); 
				
				
				//alert("--"+ $(".quickViewSelect").html());
				
				//if (allStockZero == 'Y' && data['othersSellersCount']>0) {
				if (isOOSQuick() && data['othersSellersCount']>0 && productCategoryType!='HomeFurnishing') {
					$("#addToCartButtonQuick").hide();
					$('.js-add-to-cart-qv').hide();
					$("#outOfStockIdQuick").show();
				}else if (isOOSQuick() && data['othersSellersCount']==0 && productCategoryType!='HomeFurnishing'){
					$("#addToCartButtonQuick").hide();
					$('.js-add-to-cart-qv').hide();
					$("#outOfStockIdQuick").show();
				}else if (allStockZero == 'Y' && data['othersSellersCount']>0 && $("ul[label=sizes] li").length == 0 && productCategoryType!='HomeFurnishing') { //TPR-465
					//if( $(".quickViewSelect").html()!="Select") {  //TISPRD-1173
					$("#addToCartButtonQuick").hide();
					$('.js-add-to-cart-qv').hide();
					$("#outOfStockIdQuick").show();
					//}					
				}
				else if (allStockZero == 'Y' && data['othersSellersCount']==0 && $("ul[label=sizes] li").length == 0 && productCategoryType!='HomeFurnishing'){
					//if($(".quickViewSelect").html()!="Select"){	//TISPRD-1173 TPR-465
						$("#addToCartButton").hide();
						$('.js-add-to-cart-qv').hide();
						$("#outOfStockIdQuick").show();
					//}					
				}
				else if(allStockZero == 'Y' && data['othersSellersCount']>0  && productCategoryType=='HomeFurnishing')
					{
						$("#addToCartButton").hide();
						$('.js-add-to-cart-qv').hide();
						$("#outOfStockIdQuick").show();
					}
				else if(allStockZero == 'Y' && data['othersSellersCount']==0  && productCategoryType=='HomeFurnishing')
				{
					$("#addToCartButton").hide();
					$('.js-add-to-cart-qv').hide();
					$("#outOfStockIdQuick").show();
				}
				else
					{
					//UF-88
					
					$(document).ready(function() 
					{
						removedisabled();
					});
					
					$("#addToCartButtonQuick").show();
					$('.js-add-to-cart-qv').show();
					$("#outOfStockIdQuick").hide();
					}				
				if(oosMicro==true){ //TPR-250 change
					$("#addToCartButtonQuick").hide();
					$('#buyNowButton').hide();
					$("#outOfStockIdQuick").show();
				}
				dispQuickViewPrice(mrpPrice, mop, spPrice, savingsOnProduct);
				
				sellerName = data['sellerName'];
				$("#sellerNameIdQuick").html(sellerName);
				getRichAttributeQuickView(sellerName);
				

				
				

				/* PRICE BREAKUP STARTS HERE */
			/*	
				$("#showPrice").show();
				if(data['displayconfigattr'] == "Yes"){
					$("#showPricequick").show();
				}else if(data['displayconfigattr'] == "No"){
					$("#showPricequick").hide();
				}else{
					$("#showPricequick").hide();
				}
				
				var priceBreakupForPDP = data['priceBreakup'];
					$.each(priceBreakupForPDP,function(key,value) {	
						var pricebreakuplist = "<li><span>"+ key +"</span><strong>"+ value.formattedValue +"</strong></li>";
							$("#showPriceBreakupquick").append(pricebreakuplist);
							
						
				});
				*/
				
				
				/* PRICE BREAKUP ENDS HERE */

			},
			complete: function() {
				if($('#pageType').val() != "/compare"){
					//TPR-4712,4725 | quickview
					var priceMOP='';
					var priceMOPvalue='';
					var priceMRP=mrpPrice.formattedValueNoDecimal;
					var priceMRPvalue=mrpPrice.value;
					if(spPrice != '' && typeof(spPrice) != "undefined"){
						priceMOP = spPrice.formattedValueNoDecimal;
						priceMOPvalue = spPrice.value;
					}
					else{
						priceMOP = mop.formattedValueNoDecimal;
						priceMOPvalue = mop.value;
					}
					
					var discount= priceMRPvalue - priceMOPvalue;
					var discountPercentage = savingsOnProduct+"%";
					quickViewUtag(productCode,sellerId,sellerName,priceMRP,priceMOP,discount,discountPercentage,stock);
				}

			}
		});	
		
		$(".size-guide").on("click",function(){
			if(null!= availibility){
				$.each(availibility,function(key,value){
					$(".variant-select-sizeGuidePopUp option").each(function(){
						if(typeof($(this).attr("data-producturl"))!= 'undefined' && $(this).attr("data-producturl").indexOf(key)!= -1 && value == 0){
							$(this).attr("disabled","disabled");
							}
					});
				});
			}
		});
}

function quickViewUtag(productCode,sellerId,sellerName,priceMRP,priceMOP,discount,discountPercentage,stock){
	var productCodeArray=[];
	var sellerIdArray=[];
	productCodeArray.push(productCode);	// Product code passed as an array for Web Analytics
	sellerIdArray.push(sellerId);
	var sellerName = sellerName.toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	var thumbnailImageCount=0;
	$(".product-info > .product-image-container > .productImageGallery .imageListCarousel").find("li").each(function(){
		thumbnailImageCount++;
	})
	
	var sizeVariantCount=0;
	$(".quickview .product-content .sizeVariantForm").find("#quickViewVariant li").each(function(){
		if($(this).find('a').attr('href') != "#"){
			sizeVariantCount++;
		}
	})
	
	utag.link({
		link_text: 'quick_view_click' ,
		event_type : 'quick_view_click',
		product_sku_quick_view : productCodeArray,
		seller_id : sellerIdArray,
		seller_name : sellerName,
		product_image_count : thumbnailImageCount,
		product_mrp : priceMRP,
		product_mop : priceMOP,
		product_discount : discount,
		product_discount_percentage : discountPercentage,
		product_stock_count : stock,
		size_variant_count : sizeVariantCount
	});
	var category = $('#categoryType').val();
	var brand  = $('.product-detail .company').text().trim();
	dtmQVTrack(productCodeArray,category,brand);
}

function getRichAttributeQuickView(sellerName)
{
	//alert("----inside getRichAttributeQuickView"+sellerName);
	var buyboxSeller = $("#ussid_quick").val();
	var productCode = productCodeQuickView;//$("#productCode").val();
	var requiredUrl = ACC.config.encodedContextPath + "/p-" + productCode
			+ "/getRichAttributes";
	var dataString = 'buyboxid=' + buyboxSeller;
	$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		cache : true,
		dataType : "json",
		success : function(data) {
			if (data != null) {
				var fulFillment = data['fulfillment'];
				//alert("----value of fulFillment is"+fulFillment);
				if (null != fulFillment && fulFillment.toLowerCase() == 'tship') {
					$('#fulFilledByTshipQuick').show();
				} else {
					$('#fulFilledBySshipQuick').show();
					$('#fulFilledBySshipQuick').html(sellerName);
				}
				
				if (null != data['newProduct']
				&& data['newProduct'].toLowerCase() == 'y') {
						$('#newProduct').show();
						//$(".picZoomer-pic-wp #codId").css("top", "85" + "px");
				}
				if (data['onlineExclusive']) {
					$('.online-exclusive').show();
				}
		
		
			}
				
			}
		});
}

function dispQuickViewPrice(mrp, mop, spPrice, savingsOnProduct) {

	if(null!= mrp){
		$("#quickMrpPriceId").html(mrp.formattedValueNoDecimal);
	}
	if(null!= mop){
		$("#quickMopPriceId").html(mop.formattedValueNoDecimal);
	}
	if(null!= spPrice){
		$("#quickSpPriceId").html(spPrice.formattedValueNoDecimal);
	} 
////TISPRM-33
	if(null!= savingsOnProduct){
		$("#savingsOnProductIdQV").html("(-"+savingsOnProduct+" %)");
	} 

	if(null!= savingsOnProduct && savingsOnProduct != 0){
		$("#savingsOnProductIdQV").show();
	} 

	if (null!=spPrice && spPrice != 0) {		

		if (mop.value == mrp.value) {

			$('#quickMrpPriceId').css('text-decoration', 'line-through');
			$("#quickMrpPriceId").show();
			$("#quickSpPriceId").show();
		} else {

			$('#quickMrpPriceId').css('text-decoration', 'line-through');
			$("#quickMrpPriceId").show();
			$("#quickSpPriceId").show();
		}
		
		if(spPrice.value > emiCuttOffAmount)
			{
			$("#prodPrice").val(spPrice.value);
			$("#emiStickerId").show();
			
			}

	} else {
		if (null!=mop && mop.value != 0) {
			if (mop.value == mrp.value) {
				$("#quickMrpPriceId").removeClass("old").addClass("sale");
				$("#quickMrpPriceId").show();
			} else {
				$('#quickMrpPriceId').css('text-decoration', 'line-through');
				$("#quickMrpPriceId").show();
				$("#quickMopPriceId").show();
			}
			
			if(mop.value > emiCuttOffAmount)
			{
			$("#emiStickerId").show();
			$("#prodPrice").val(mop.value);
			}
			
		} else {
			$("#quickMrpPriceId").show();
			if(mrp.value > emiCuttOffAmount)
			{
			$("#emiStickerId").show();
			$("#prodPrice").val(mrp.value);
			}
		}
	}
	if (mrp.value == "") {
		$("#quickMrpPriceId").hide();
	} else {
		$("#quickMrpPriceId").show();
	}
}


function addToWishlist_quick(alreadyAddedWlName_quick) {
	var productCodePost = $("#product_quick").val();
	
	// Product code passed as an array for Web Analytics   INC_11511 
	var productCodeArray=[];
	productCodeArray.push(productCodePost);	// Product code passed as an array for Web Analytics
//	var productCodePost = $("#productCodePostQuick").val();
	var wishName = "";
	
   
	if (wishListList == "") {
		wishName = $("#defaultWishName_quick").val();
	} else {
		wishName = wishListList[$("#hidWishlist_quick").val()];
	}
	if(wishName==""){
		var msg=$('#wishlistnotblank_quick').text();
		$('#addedMessage_quick').show();
		$('#addedMessage_quick').html(msg);
		return false;
	}
    if(wishName==undefined||wishName==null){
    	if(alreadyAddedWlName_quick!=undefined || alreadyAddedWlName_quick!=""){
    		if(alreadyAddedWlName_quick=="[]"){
    			$("#wishlistErrorId_quick").html("Please select a wishlist");
    		}
    		else{
    			alreadyAddedWlName_quick=alreadyAddedWlName_quick.replace("[","");
    			alreadyAddedWlName_quick=alreadyAddedWlName_quick.replace("]","");
    			$("#wishlistErrorId_quick").html("Product already added in your wishlist "+alreadyAddedWlName_quick);
    		}
    		$("#wishlistErrorId_quick").css("display","block");
    	}
    	return false;
    }
	var requiredUrl = ACC.config.encodedContextPath + "/p"
			+ "-addToWishListInPDP";
    
    var sizeSelected=true;
	if(productCategoryType=='HomeFurnishing')
	{
	
	sizeSelected=true;
	}
	
     if($("#isSizeSelectedQV").val()==''){
    	sizeSelected=false;
	}
    
	var dataString = 'wish=' + wishName + '&product=' + productCodePost
			+ '&ussid=' + ussidValue+'&sizeSelected=' + sizeSelected;
  
	$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		dataType : "json",
		success : function(data) {
			if (data == true) {
				$("#radio_" + $("#hidWishlist_quick").val()).prop("disabled", true);
				var msg=$('#wishlistSuccess_quick').text();
				$('#addedMessage_quick').show();				
				$('#addedMessage_quick').html(msg);
				setTimeout(function() {
					  $("#addedMessage_quick").fadeOut().empty();
					 					  
					}, 1500);
				populateMyWishlistFlyOut(wishName);
				
				/*TPR-656*/
					utag.link({
						link_obj: this, 
						link_text: 'add_to_wishlist' , 
						event_type : 'add_to_wishlist', 
						product_sku_wishlist : productCodeArray
					});
				/*TPR-656 Ends*/
				
				
				//For MSD
				var isMSDEnabled =  $("input[name=isMSDEnabled]").val();								
				if(isMSDEnabled === 'true')
				{
		//		console.log(isMSDEnabled);
				var isApparelExist  = $("input[name=isApparelExist]").val();
		//		console.log(isApparelExist);				
				var salesHierarchyCategoryMSD =  $("input[name=salesHierarchyCategoryMSD]").val();
		//		console.log(salesHierarchyCategoryMSD);
				var rootCategoryMSD  = $("input[name=rootCategoryMSD]").val();
		//		console.log(rootCategoryMSD);				
				var productCodeMSD =  $("input[name=productCodeMSD]").val();
		//		console.log(productCodeMSD);				
				var priceformad =  $("input[id=price-for-mad]").val();
		//		console.log(priceformad);				
				
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
					ACC.track.trackAddToWishListForMAD(productCodeMSD, salesHierarchyCategoryMSD, priceformad,"INR");
					}	
				}
				//End MSD
				
				
				
				
				//openPop(ussidValue);
			//	$('#myModal').modal('hide');
			//	
			}
		},
	});
	
	setTimeout(function() {
		$('a.wishlist#wishlist_quick').popover('hide');
		$('input.wishlist#add_to_wishlist_quick').popover('hide');
		}, 1500);
}

function LoadWishLists_quick(ussid, data, productCode) {
	// modified for ussid
	var addedWlList_quick = [];
	var wishListContent = "";
	var wishName = "";
	$this = this;
	$("#wishListNonLoggedInId_quick").hide();
	$("#wishListDetailsId_quick").show();

	for ( var i in data) {
		var index = -1;
		var checkExistingUssidInWishList = false;
		var wishList = data[i];
		wishName = wishList['particularWishlistName'];
		wishListList[i] = wishName;
		var entries = wishList['ussidEntries'];
		for ( var j in entries) {
			var entry = entries[j];

			if (entry == ussid) {

				checkExistingUssidInWishList = true;
				break;

			}
		}
		if (checkExistingUssidInWishList) {
			index++;
            
			wishListContent = wishListContent
					+ "<tr class='d0'><td ><input type='radio' name='wishlistradio' id='radio_"
					+ i
					+ "' style='display: none' onclick='selectWishlist_quick("
					+ i + ")' disabled><label for='radio_"
					+ i + "'>"+wishName+"</label></td></tr>";
			addedWlList_quick.push(wishName);
		} else {
			index++;
		  
			wishListContent = wishListContent
					+ "<tr><td><input type='radio' name='wishlistradio' id='radio_"
					+ i
					+ "' style='display: none' onclick='selectWishlist_quick("
					+ i + ")'><label for='radio_"
					+ i + "'>"+wishName+"</label></td></tr>";
		}
		$("#alreadyAddedWlName_quick").val(JSON.stringify(addedWlList_quick));
	}

	$("#wishlistTbodyId_quick").html(wishListContent);

}


function selectWishlist_quick(i) {
	$("#hidWishlist_quick").val(i);
}


/*function openPop_quick(ussidfromSeller) {
	alert("HEllo");
	$(".wishAddSucessQV").addClass("active");
	setTimeout(function(){
		$(".wishAddSucessQV").removeClass("active")
	},3000);
	$('#addedMessage_quick').hide();
	if (ussidfromSeller == null || ussidfromSeller == "") {
		ussidValue = $("#ussid_quick").val();
	} else {
		ussidValue = ussidfromSeller;
	}
	
	var productCode = $("#product_quick").val();
	var requiredUrl = ACC.config.encodedContextPath + "/p"
			+ "-viewWishlistsInPDP";

	var dataString = 'productCode=' + productCode + '&ussid=' + ussidValue;// modified
	console.log(dataString);
	console.log(requiredUrl);
	// for
	// ussid

	$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		dataType : "json",
		success : function(data) {
			if (data == null) {
				alert("Login");
				$("#wishListNonLoggedInId_quick").show();
				$("#wishListDetailsId_quick").hide();
			}

			else if (data == "" || data == []) {
			
		
				alert("Login 2");
				loadDefaultWishListName_quick();

			} else {alert("Login 3");
				LoadWishLists_quick(ussidValue, data, productCode);
			}
		},
		error : function(xhr, status, error) {alert(xhr.status+"---"+error);
			$("#wishListNonLoggedInId_quick").show();
			$("#wishListDetailsId_quick").hide();
		}
	});
}*/

function openPop_quick(ussidfromSeller) {

	var loggedIn = $("#loggedIn").val();

	var productCodePostQuickView = productCodeQuickView;
	var productcodearray = [];
	productcodearray.push(productCodePostQuickView);

	var wishName = "";

	var ussidValue = $("#ussid_quick").val();

	var isMSDEnabled = $("input[name=isMSDEnabled]").val();
	if (isMSDEnabled === 'true') {

		var isApparelExist = $("input[name=isApparelExist]").val();

		var salesHierarchyCategoryMSD = $(
				"input[name=salesHierarchyCategoryMSD]").val();

		var rootCategoryMSD = $("input[name=rootCategoryMSD]").val();

		var productCodeMSD = $("input[name=productCodeMSD]").val();

		var priceformad = $("input[id=price-for-mad]").val();

		if (typeof isMSDEnabled === 'undefined') {
			isMSDEnabled = false;
		}

		if (typeof isApparelExist === 'undefined') {
			isApparelExist = false;
		}
	}

	var requiredUrl = ACC.config.encodedContextPath + "/p"
			+ "-addToWishListInPDP";
	var sizeSelected = true;

	if (!$("#quickViewVariant li ").hasClass("selected")
			&& typeof ($(".variantFormLabel").html()) == 'undefined'
			&& $("#ia_product_rootCategory_type").val() != 'Electronics') {
		sizeSelected = false;
	}

	var dataString = 'wish=' + wishName + '&product=' + productCodePostQuickView
			+ '&ussid=' + ussidValue + '&sizeSelected=' + sizeSelected;
	
	// if(loggedIn == 'false') {
	if (!headerLoggedinStatus) {
		$("div.wishAddLoginQv").addClass("active");
		setTimeout(function() {
			$("div.wishAddLoginQv").removeClass("active")
		}, 3000);
	} else {
		var isInWishlist = getLastModifiedWishlistQuick(ussidValue);
		if (isInWishlist) {
			removeFromWishlistInQuickView(wishName, productCodePostQuickView,
					ussidValue, isMSDEnabled, isApparelExist, rootCategoryMSD,
					salesHierarchyCategoryMSD, priceformad, "INR");
		} else {
			$
					.ajax({
						contentType : "application/json; charset=utf-8",
						url : requiredUrl,
						data : dataString,
						dataType : "json",
						success : function(data) {
							if (data == true) {
								// $("#radio_" +
								// $("#hidWishlist").val()).prop("disabled",
								// true);
								// var msg=$('#wishlistSuccess').text();
								// $('#addedMessage').show();
								// $('#addedMessage').html(msg);
								$("div.wishAddSucessQv").addClass("active");
								$('.wishlist-icon-qv').addClass("added");
								setTimeout(function() {
									$("div.wishAddSucessQv").removeClass(
											"active")
								}, 3000);
								$("#add_to_wishlist_quick").attr("disabled",
										true);
								$(
										'.add_to_cart_form .out_of_stock #add_to_wishlist_quick')
										.addClass("wishDisabled");
								$(
										'.product-info .picZoomer-pic-wp .zoom a,.product-image-container.device a.wishlist-icon')
										.addClass("added");
								/*
								 * setTimeout(function() {
								 * $("#addedMessage").fadeOut().empty(); },
								 * 1500);
								 */
								// $('#addedMessage').delay(3000).fadeOut('slow');
								// // TISTI-225
								populateMyWishlistFlyOut(wishName);

								// For MSD
								if (isMSDEnabled === 'true') {
									if (Boolean(isMSDEnabled)
											&& Boolean(isApparelExist)
											&& (rootCategoryMSD === 'Clothing')) {
										ACC.track.trackAddToWishListForMAD(
												productCodeMSD,
												salesHierarchyCategoryMSD,
												priceformad, "INR");
									}
								}
								// End MSD

								if (typeof utag != "undefined") {
									utag
											.link({
												link_obj : this,
												link_text : "add_to_wishlist_quickview",
												event_type : "add_to_wishlist_quickview",
												product_sku_wishlist : productarray
											});
								}

								// openPop(ussidValue);
								// $('#myModal').modal('hide');
								//	
							}
							/*
							 * else{
							 * $("div.wishAlreadyAddedQv").addClass("active");
							 * setTimeout(function(){
							 * $("div.wishAlreadyAddedQv").removeClass("active")
							 * },3000); if(typeof utag !="undefined"){
							 * utag.link({error_type : 'wishlist_error'}); } }
							 */
						},
					});

			// $('a.wishlist#wishlist').popover('hide');
			// $('input.wishlist#add_to_wishlist').popover('hide');

			setTimeout(function() {
				$('a.wishlist#wishlist').popover('hide');
				$('input.wishlist#add_to_wishlist').popover('hide');
			}, 0);

		}

	}
}

function getLastModifiedWishlistQuick(ussidValue) {
	var isInWishlist = false;
	var requiredUrl = ACC.config.encodedContextPath + "/p"
			+ "-getLastModifiedWishlistByUssid";
	var dataString = 'ussid=' + ussidValue;
	$
			.ajax({
				contentType : "application/json; charset=utf-8",
				url : requiredUrl,
				data : dataString,
				dataType : "json",
				async : false,
				success : function(data) {
					if (data == true) {
						isInWishlist = true;
						$(
								'.product-info .picZoomer-pic-wp .zoom a,.product-image-container.device a.wishlist-icon')
								.addClass("added");
						$("#add_to_wishlist_quick").attr("disabled", true);
						$(
								'.add_to_cart_form .out_of_stock #add_to_wishlist_quick')
								.addClass("wishDisabled");
					}

				},
				error : function(xhr, status, error) {
					$("#wishlistErrorId_pdp").html(
							"Could not add the product in your wishlist");
				}
			});
	return isInWishlist;
}

function removeFromWishlistInQuickView(wishlistName, productCode, ussid,
		isMSDEnabled, isApparelExist, rootCategoryMSD, catID, price, currency) {
	var requiredUrl = ACC.config.encodedContextPath + "/p" + "-removeFromWl";
	var dataString = 'wish=' + wishlistName + '&product=' + productCode
			+ '&ussid=' + ussid;

	$
			.ajax({
				url : requiredUrl,
				type : "GET",
				data : dataString,
				dataType : "json",
				cache : false,
				contentType : "application/json; charset=utf-8",
				success : function(data) {
					// FOR MSD
					if (Boolean(isMSDEnabled) && Boolean(isApparelExist)
							&& (rootCategoryMSD == 'Clothing')) {
						try {
							track([ 'removeFromWishlist', productCode, catID,
									price, currency ]);
						} catch (err) {
							console
									.log('Error Adding trackers when remove from cart: '
											+ err.message);
						}
					}

					$(".wishRemoveSucessQV").addClass("active");
					$('.wishlist-icon-qv').removeClass("added");
					setTimeout(function() {
						$(".wishRemoveSucessQV").removeClass("active")
					}, 3000)
					$("#add_to_wishlist_quick").attr("disabled", false);
					$('.add_to_cart_form .out_of_stock #add_to_wishlist_quick')
							.removeClass("wishDisabled");
					$(
							'.product-info .picZoomer-pic-wp .zoom a,.product-image-container.device a.wishlist-icon')
							.removeClass("added");
					populateMyWishlistFlyOut(wishlistName);

					/* TPR-646 Changes */
					utag.link({
						"link_obj" : this,
						"link_text" : 'remove_from_wishlist_quickview',
						"event_type" : 'remove_from_wishlist_quickview',
						"product_sku_wishlist" : "" + productCode
					});

					// END MSD
					// window.location.href = ACC.config.encodedContextPath +
					// "/my-account/wishList";
					//window.location.href = ACC.config.encodedContextPath + "/my-account/viewParticularWishlist?particularWishlist="+wishlistName;
				},
				error : function(xhr, status, error) {
					if (status == "parsererror") {
						window.location.href = ACC.config.encodedContextPath
								+ "/login";
					} else {

						alert("Some issues are there with Wishlist at this time. Please try later or contact our helpdesk");
					}

				}
			});
}

function loadDefaultWishListName_quick() {
	var wishListContent = "";
	var wishName = $("#defaultWishId_quick").text();
	$("#wishListNonLoggedInId_quick").hide();
	$("#wishListDetailsId_quick").show();

	wishListContent = wishListContent
			+ "<tr><td><input type='text' id='defaultWishName_quick' value='"
			+ wishName + "'/></td></td></tr>";
	$("#wishlistTbodyId_quick").html(wishListContent);
   
}


function openPopForBankEMI_quick() {
	var productVal = $("#prodPrice").val();
	var optionData = "<option value='select' disabled selected>Select</option>";
	$("#emiTableTHead").hide();
	$("#emiTableTbody").hide();
	var requiredUrl = ACC.config.encodedContextPath + "/p" + "-enlistEMIBanks";
	var dataString = 'productVal=' + productVal;
	$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		dataType : "json",
		success : function(data) {
			for (var i = 0; i < data.length; i++) {
				optionData += "<option value='" + data[i] + "'>" + data[i]
						+ "</option>";
			}
			$("#bankNameForEMI").html(optionData);
			/*TPR-641*/
			utag.link({
				link_text: 'emi_more_information' ,
				event_type : 'emi_more_information'
			});
			//TPR-6029-EMI link click#43--start
			   if(typeof(_satellite) != "undefined"){
			    	_satellite.track('cpj_qw_emi');
				}
			//TPR-6029-EMI link click#43--end
		},
		error : function(xhr, status, error) {

		}
	});
}


function getSelectedEMIBankForPDP() {
 var productVal = $("#prodPrice").val();
 
	var selectedBank = $('#bankNameForEMI :selected').text();
	var contentData = '';
	if (selectedBank != "select") {
		$.ajax({
			url : ACC.config.encodedContextPath + "/p-getTerms",
			data : {
				'selectedEMIBank' : selectedBank,
				'productVal' : productVal
			},
			type : "GET",
			cache : false,
			success : function(data) {
				if (data != null) {
					$("#emiTableTHead").show();
					$("#emiTableTbody").show();
					for (var index = 0; index < data.length; index++) {
						contentData += '<tr>';
						contentData += "<td>" + data[index].term + "</td>";
						contentData += "<td>" + data[index].interestRate
								+ "</td>";
						contentData += "<td>" + data[index].monthlyInstallment
								+ "</td>";
						contentData += "<td>" + data[index].interestPayable
								+ "</td>";
						contentData += '</tr>';
					}

					$("#emiTableTbody").html(contentData);
				} else {
					$('#emiNoData').show();
				}
			},
			error : function(resp) {
				$('#emiSelectBank').show();
			}
		});
	} else {

	}
}
/**/
$(document).on("click","#variantForm div ul li a,.color-swatch-container .color-swatch li a",function(){
	setTimeout(function(){
	$(".zoomContainer").remove();
	$('.picZoomer-pic').removeData('zoom-image');
	$("img.picZoomer-pic").attr('data-zoom-image',$(".quickview .product-image-container .productImageGallery .active img").attr("data-zoomimagesrc")); 
	$('.quickview .picZoomer-pic').elevateZoom({
	    zoomType: "window",
	    cursor: "crosshair",
	    zoomWindowFadeIn: 500,
	    zoomWindowFadeOut: 750
    });
	var mainImageHeight = $(".main-image").find("img.picZoomer-pic").height();
	var thumbnailImageHeight = (mainImageHeight / 5);
	$(".imageList ul li img").css("height", thumbnailImageHeight);		
	}, 1000); 
});
/* Start of Quickview EMI  */
$(document).on("click",".quickview #emiStickerId p",function(e){
	e.stopPropagation();
	$(this).addClass("active")
	openPopForBankEMI_quick();
	$(".quickview #emiTableDiv #EMITermTable").hide();
		
	});
$(document).on("click",".quickview .Emi .modal-content .Close",function(e){
	$(".quickview .Emi > p").removeClass("active mobile");
	e.stopPropagation();
	
	
});
$(document).on("click", function(e){
	//console.log($(e.currentTarget).attr('class'))
	if(!$(e.currentTarget).parents(".Emi").hasClass("Emi_wrapper")) {
		$(".quickview .Emi > p").removeClass("active")
	} else {
		$(".quickview .Emi > p").addClass("active");
		
	}
});
$(document).on("click",".quickview .Emi > #EMImodal-content",function(e){
	e.stopPropagation();
		$(".quickview .Emi > p").addClass("active");

});

$(document).on('click','#buyNowQv .js-add-to-cart-qv',function(event){
	var isShowSize= $("#showSizeQuickView").val();
		 /*if(!$("#quickViewVariant li ").hasClass("selected")  && typeof($(".variantFormLabel").html())== 'undefined' && $("#categoryType").val()!='Electronics' && $("#categoryType").val()!='Watches' && $("#categoryType").val()!='TravelAndLuggage' && isShowSize=='true') {
			 $("#addToCartFormQuickTitle").html("<font color='#ff1c47'>" + $('#selectSizeId').text() + "</font>");
			 				$("#addToCartFormQuickTitle").show();
			  				$("#addToCartFormQuickTitle").fadeOut(5000);
	 	    return false;
	     }
		 
		ACC.product.sendToCartPageQuick("addToCartFormQuick",true);*/

	
	 if(!$("#quickViewVariant li ").hasClass("selected") && typeof($(".variantFormLabel").html())== 'undefined' && $("#categoryType").val()!='Electronics' && $("#categoryType").val()!='Watches' && $("#categoryType").val()!='Accessories' && isShowSize=='true' && $("#categoryType").val()!='HomeFurnishing'){
		$("#addToCartFormQuickTitle").html("<font color='#ff1c47'>" + $('#selectSizeId').text() + "</font>");
		 				$("#addToCartFormQuickTitle").show();
		  				$("#addToCartFormQuickTitle").fadeOut(5000);
		  				errorAddToBag("size_not_selected"); //Error for tealium analytics
 	    return false;
 	    //console.log("QucikView Here");
	 }	 
	ACC.product.sendToCartPageQuick("addToCartFormQuick",true);


});
/*End of quickview Emi*/
/*TPR-924*/
/*Wishlist icon activation*/
function isItemInWishList(ussidfromSeller) {
	
	var loggedIn=$("loggedIn").val();
	$('#addedMessage').hide();
	if ($.isEmptyObject(ussidfromSeller)) {
		ussidValue = $("#ussid_quick").val();
	} else {
		ussidValue = ussidfromSeller;
	}
	var productCode = $("#productCodePost").val();

	var requiredUrl = ACC.config.encodedContextPath + "/p"
			+ "-viewWishlistsInPDP";

	var dataString = 'productCode=' + productCode + '&ussid=' + ussidValue;// modified
	// for
	// ussid

	$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		dataType : "json",
		success : function(data) {
		if (!$.isEmptyObject(data)) {			
				LoadWishLists(ussidValue, data, productCode);
			}
		},
		error : function(xhr, status, error) {
			
		}
	});
}

function LoadWishLists(ussid, data, productCode) {
	    
	// modified for ussid
	var addedWlList_pdp = [];
	var wishListContent = "";
	var wishName = "";
	$this = this;
	

	for ( var i in data) {
		
		var index = -1;
		var checkExistingUssidInWishList = false;
		var wishList = data[i];
		wishName = wishList['particularWishlistName'];
		wishListList[i] = wishName;
		var entries = wishList['ussidEntries'];
		for ( var j in entries) {
			var entry = entries[j];

			if (entry == ussid) {

				checkExistingUssidInWishList = true;
				break;

			}
		}
		
		if (checkExistingUssidInWishList) {
			
			$('.wishlist-icon-qv').addClass("added");
			           
		} 
		index++;
	}

	

}
$(document).ready(function() {
	$("#showquick").click(function() {
		$("#showPriceBreakupquick").slideToggle("fast");
	});
});

