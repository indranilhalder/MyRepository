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

function setBuyBoxDetails()
{
	var productCode = productCodeQuickView;//$("#productCodePost").val();
	var code = productCode+","+variantCodesPdp;
	var requiredUrl = ACC.config.encodedContextPath + "/p-" + code
	+ "/buybox";
	var availibility = null;
		var dataString = 'productCode=' + productCode;		
		$.ajax({
			contentType : "application/json; charset=utf-8",
			url : requiredUrl,
			data : dataString,
			cache : false,
			dataType : "json",
			success : function(data) {
				var stockInfo = data['availibility'];
				availibility = stockInfo;
				
				$.each(stockInfo,function(key,value){
					$("ul[label=sizes] li a").each(function(){
						
				if(typeof($(this).attr("href"))!= 'undefined' && $(this).attr("href").toUpperCase().indexOf(key)!= -1 && value == 0){ 
								
								$(this).attr("disabled",true);
								
								$(this).css({
									"color": "gray",
						  "text-decoration": "line-through"
						  
							});
								$(this).on("mouseenter",function(){
									$(this).parent("li").css("background","#fff");
									
								});
								
								 $(this).on('click', function(event) {
								      event.preventDefault();
								      return false;
								   });
							$(this).parent().css("border-color","gray");
							}
						
					});
				});
				
				//alert("...>>"+data['sellerArticleSKU']+"<<"+productCode+"..."+productCodeQuickView);
				
				if(typeof data['sellerArticleSKU'] === 'undefined')
					{
					$("#addToCartButtonQuick-wrong").show();
					$("#addToCartButtonQuick").hide();					
					//$("#dListedErrorMsg").show();				
					return false;
					}					
				
				if(data['sellerArticleSKU']==null)
					{
					$("#addToCartButtonQuick-wrong").show();
					$("#addToCartButtonQuick").hide();
					//$("#dListedErrorMsg").show();					
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
				if (allStockZero == 'Y') {
					if( $(".quickViewSelect").html()!="Select") {  //TISPRD-1173
					$("#addToCartButtonQuick").hide();
					$("#outOfStockIdQuick").show();
					}					
				}
				/*else if (allStockZero == 'Y' && data['othersSellersCount']==0){
					if($(".quickViewSelect").html()!="Select"){	//TISPRD-1173
						$("#addToCartButton").hide();
						$("#outOfStockIdQuick").show();
					}					
				}*/
				else
					{
					$("#addToCartButtonQuick").show();
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


function addToWishlist_quick() {
	var productCodePost = $("#product_quick").val();
//	var productCodePost = $("#productCodePostQuick").val();
	var wishName = "";
	
   
	if (wishListList == "") {
		wishName = $("#defaultWishName_quick").val().trim();
	} else {
		wishName = wishListList[$("#hidWishlist_quick").val().trim()];
	}
	if(wishName=="" || wishName.trim()==""){
		var msg=$('#wishlistnotblank_quick').text();
		$('#addedMessage_quick').show();
		$('#addedMessage_quick').html(msg);
		return false;
	}
    if(wishName==undefined||wishName==null){
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
		} else {
			index++;
		  
			wishListContent = wishListContent
					+ "<tr><td><input type='radio' name='wishlistradio' id='radio_"
					+ i
					+ "' style='display: none' onclick='selectWishlist_quick("
					+ i + ")'><label for='radio_"
					+ i + "'>"+wishName+"</label></td></tr>";
		}

	}

	$("#wishlistTbodyId_quick").html(wishListContent);

}


function selectWishlist_quick(i) {
	$("#hidWishlist_quick").val(i);
}


function openPop_quick(ussidfromSeller) {
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
	// for
	// ussid

	$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		dataType : "json",
		success : function(data) {
			if (data == null) {
				$("#wishListNonLoggedInId_quick").show();
				$("#wishListDetailsId_quick").hide();
			}

			else if (data == "" || data == []) {

				loadDefaultWishListName_quick();

			} else {
				LoadWishLists_quick(ussidValue, data, productCode);
			}
		},
		error : function(xhr, status, error) {
			$("#wishListNonLoggedInId_quick").show();
			$("#wishListDetailsId_quick").hide();
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
