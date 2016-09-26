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
			},

			onClosed: function ()
			{
				ACC.quickview.refreshScreenReaderBuffer();
				if((window.top==window) && $("body").hasClass("page-cartPage")) {
				    // You're not in a frame, so you reload the site.
				    window.setTimeout('location.reload()'); 
			     }
			}
		});
	}
	
};



function quickviewGallery() {
	$(document).ready(function(){
		var mainImageHeight = $(".main-image").find("img.picZoomer-pic").height();
		var thumbnailImageHeight = (mainImageHeight / 5);
		$(".imageList ul li img").css("height", thumbnailImageHeight);
	 	$("#previousImage").css("opacity","0.5");
	 	$("#nextImage").css("opacity","1");
	 	var listHeight = $(".imageList li").height();
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
	 });
	
	
}

////jsp to js move

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
	var totalOptions = $("ul[label=sizes] li").length;
	totalOptions = totalOptions -1;
	var disabledOption = $("ul[label=sizes] li.strike").length;
	if(totalOptions == disabledOption){
		
		return true;
	}else{
		
		return false;
	}
}
function setBuyBoxDetails()
{
	var productCode = productCodeQuickView;//$("#productCodePost").val();
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
			data : {productCode:productCode,variantCode:variantCodesJson},
			cache : false,
			dataType : "json",
			success : function(data) {
				var stockInfo = data['availibility'];
				availibility = stockInfo;
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
				
				//alert("...>>"+data['sellerArticleSKU']+"<<"+productCode+"..."+productCodeQuickView);
				
				if(typeof data['sellerArticleSKU'] === 'undefined')
					{					
					$("#addToCartButtonQuick-wrong").show();
					$("#buyNowButtonQuick-wrong").show();
					$("#buyNowQv").hide();
					$("#addToCartButtonQuick").hide();
					$('#buyNowButton').hide();
					$("#dListedErrorMsg").show();				
					return false;
					}					
				
				if(data['sellerArticleSKU']==null)
					{
									
					$("#addToCartButtonQuick-wrong").show();
					$("#buyNowButtonQuick-wrong").show();
					$("#buyNowQv").hide();
					$("#addToCartButtonQuick").hide();
					$('#buyNowButton').hide();
					$("#dListedErrorMsg").show();					
					return false;
					}
				
				$("#addToCartButtonQuick-wrong").hide();
				$("#dListedErrorMsg").hide();	
				
				//alert(data['sellerArticleSKU']+".."+data['sellerName']);
				var spPrice = data['specialPrice'];
				var mrpPrice = data['mrp'];
				var mop = data['price'];
				var savingsOnProduct= data['savingsOnProduct'];
				
				$("#ussid_quick").val(data['sellerArticleSKU']);				
				$("#stock").val(data['availablestock']);					
				var allStockZero = data['allOOStock'];
				$("#sellerSelId").val(data['sellerId']); 
				
				
				//alert("--"+ $(".quickViewSelect").html());
				
				//if (allStockZero == 'Y' && data['othersSellersCount']>0) {
				if (isOOSQuick() && data['othersSellersCount']>0) {
					$("#addToCartButtonQuick").hide();
					$('#buyNowButton').hide();
					$("#outOfStockIdQuick").show();
				}else if (isOOSQuick() && data['othersSellersCount']==0){
					$("#addToCartButtonQuick").hide();
					$('#buyNowButton').hide();
					$("#outOfStockIdQuick").show();
				}else if (allStockZero == 'Y' && data['othersSellersCount']>0 && $("ul[label=sizes] li").length == 0) { //TPR-465
					//if( $(".quickViewSelect").html()!="Select") {  //TISPRD-1173
					$("#addToCartButtonQuick").hide();
					$('#buyNowButton').hide();
					$("#outOfStockIdQuick").show();
					//}					
				}
				else if (allStockZero == 'Y' && data['othersSellersCount']==0 && $("ul[label=sizes] li").length == 0){
					//if($(".quickViewSelect").html()!="Select"){	//TISPRD-1173 TPR-465
						$("#addToCartButton").hide();
						$('#buyNowButton').hide();
						$("#outOfStockIdQuick").show();
					//}					
				}
				else
					{
					$("#addToCartButtonQuick").show();
					$('#buyNowButton').show();
					$("#outOfStockIdQuick").hide();
					}				
				
				dispQuickViewPrice(mrpPrice, mop, spPrice, savingsOnProduct);
				
				
				var sellerName = data['sellerName'];
				//alert("seller name"+sellerName);
				$("#sellerNameIdQuick").html(sellerName);
				getRichAttributeQuickView(sellerName);
				
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
		$("#quickMrpPriceId").append(mrp.formattedValue);
	}
	if(null!= mop){
		$("#quickMopPriceId").append(mop.formattedValue);
	}
	if(null!= spPrice){
		$("#quickSpPriceId").append(spPrice.formattedValue);
	} 
////TISPRM-33
	if(null!= savingsOnProduct){
		$("#savingsOnProductIdQV").append("(-"+savingsOnProduct+" %)");
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
function openPop_quick(ussidfromSeller){

	
	var loggedIn=$("#loggedIn").val();

	var productCodePost = $("#productCodePost").val();

	var wishName = "";
	
	var ussidValue=$("#ussid_quick").val();
  
	/*if (wishListList == "") {
		wishName = $("#defaultWishName").val();
	} else {
		wishName = wishListList[$("#hidWishlist").val()];
	}
	if(wishName==""){
		var msg=$('#wishlistnotblank').text();
		$('#addedMessage').show();
		$('#addedMessage').html(msg);
		return false;
	}
    if(wishName==undefined||wishName==null){
    	if(alreadyAddedWlName_pdp!=undefined || alreadyAddedWlName_pdp!=""){
    		if(alreadyAddedWlName_pdp=="[]"){
    			$("#wishlistErrorId_pdp").html("Please select a wishlist");
    		}
    		else{
    			alreadyAddedWlName_pdp=alreadyAddedWlName_pdp.replace("[","");
    			alreadyAddedWlName_pdp=alreadyAddedWlName_pdp.replace("]","");
    			$("#wishlistErrorId_pdp").html("Product already added in your wishlist "+alreadyAddedWlName_pdp);
    		}
    		$("#wishlistErrorId_pdp").css("display","block");
    	}
    	return false;
    }*/
	var requiredUrl = ACC.config.encodedContextPath + "/p"
			+ "-addToWishListInPDP";
    var sizeSelected=true;
    if(!$("#quickViewVariant li ").hasClass("selected") && typeof($(".variantFormLabel").html())== 'undefined' && $("#ia_product_rootCategory_type").val()!='Electronics'){
    	sizeSelected=false;
    }
	var dataString = 'wish=' + wishName + '&product=' + productCodePost
			+ '&ussid=' + ussidValue+'&sizeSelected=' + sizeSelected;

	if(loggedIn == 'false') {
		$(".wishAddLoginQv").addClass("active");
		setTimeout(function(){
			$(".wishAddLoginQv").removeClass("active")
		},3000)
	}
	else {
			
		$.ajax({
			contentType : "application/json; charset=utf-8",
			url : requiredUrl,
			data : dataString,
			dataType : "json",
			success : function(data) {
				
				if (data == true) {
					
					//$("#radio_" + $("#hidWishlist").val()).prop("disabled", true);
					//var msg=$('#wishlistSuccess').text();
					//$('#addedMessage').show();
					//$('#addedMessage').html(msg);
					$(".wishAddSucessQv").addClass("active");
					$('.wishlist-icon-qv').addClass("added");
					setTimeout(function(){
						$(".wishAddSucessQv").removeClass("active")
					},3000)
					$("#add_to_wishlist_quick").attr("disabled",true);
					$('.add_to_cart_form .out_of_stock #add_to_wishlist_quick').addClass("wishDisabled");
					$('.product-info .picZoomer-pic-wp .zoom a,.product-image-container.device a.wishlist-icon').addClass("added");
					/*setTimeout(function() {
						  $("#addedMessage").fadeOut().empty();
						}, 1500);*/
					//$('#addedMessage').delay(3000).fadeOut('slow'); // TISTI-225
					populateMyWishlistFlyOut(wishName);
					
					//For MSD
					var isMSDEnabled =  $("input[name=isMSDEnabled]").val();								
					if(isMSDEnabled === 'true')
					{
					
					var isApparelExist  = $("input[name=isApparelExist]").val();
							
					var salesHierarchyCategoryMSD =  $("input[name=salesHierarchyCategoryMSD]").val();
					
					var rootCategoryMSD  = $("input[name=rootCategoryMSD]").val();
						
					var productCodeMSD =  $("input[name=productCodeMSD]").val();
							
					var priceformad =  $("input[id=price-for-mad]").val();
								
					
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
				else{
					$(".wishAlreadyAddedQv").addClass("active");
					setTimeout(function(){
						$(".wishAlreadyAddedQv").removeClass("active")
					},3000)
				}
			},
		});
	
	//$('a.wishlist#wishlist').popover('hide');
	//$('input.wishlist#add_to_wishlist').popover('hide');
	
		/*setTimeout(function() {
			$('a.wishlist#wishlist').popover('hide');
			$('input.wishlist#add_to_wishlist').popover('hide');

			}, 0);*/
	}

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
		$(".quickview .Emi > p").addClass("active")
	}
});
$(document).on("click",".quickview .Emi > #EMImodal-content",function(e){
	e.stopPropagation();
		$(".quickview .Emi > p").addClass("active")
});

$(document).on('click','#buyNowQv .js-add-to-cart-qv',function(event){
	
	 if(!$("#quickViewVariant li ").hasClass("selected") && typeof($(".variantFormLabel").html())== 'undefined' && $("#categoryType").val()!='Electronics'){
		 $("#addToCartFormQuickTitle").html("<font color='#ff1c47'>" + $('#selectSizeId').text() + "</font>");
		 				$("#addToCartFormQuickTitle").show();
		  				$("#addToCartFormQuickTitle").fadeOut(5000);
 	    return false;
 }else{			 
	ACC.product.sendToCartPageQuick("addToCartFormQuick",true);
}
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