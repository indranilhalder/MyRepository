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

$(document).ready(function(){
	 //AKAMAI Fix	 
 	var url = window.location.href;	
	if (url.indexOf("selectedSize=true")==-1 && typeof productSizeQuickVar !== "undefined")//>= 0  ==-1
		{
		 $('.select-size').find('span').remove();
		 $(".select-size").append("<span class='selected quickViewSelect'>"+productSizeQuickVar+"</span>");
		}
 });


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
	var productVal = $("#productPrice").val();
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
 var productVal = $("#productPrice").val();
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
