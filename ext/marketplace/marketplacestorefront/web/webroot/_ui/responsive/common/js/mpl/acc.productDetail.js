ACC.productDetail = {

	_autoload : [ "initPageEvents", "bindVariantOptions" ],

	checkQtySelector : function(self, mode) {
		var input = $(self).parents(".js-qty-selector").find(
				".js-qty-selector-input");
		var inputVal = parseInt(input.val());
		var max = input.data("max");

		var minusBtn = $(self).parents(".js-qty-selector").find(
				".js-qty-selector-minus");
		var plusBtn = $(self).parents(".js-qty-selector").find(
				".js-qty-selector-plus");

		$(self).parents(".js-qty-selector").find(".btn").removeAttr("disabled");

		if (mode == "minus") {
			if (inputVal != 1) {
				ACC.productDetail.updateQtyValue(self, inputVal - 1)
				if (inputVal - 1 == 1) {
					minusBtn.attr("disabled", "disabled")
				}

			} else {
				minusBtn.attr("disabled", "disabled")
			}
		} else if (mode == "reset") {
			ACC.productDetail.updateQtyValue(self, 1)

		} else if (mode == "plus") {
			if (inputVal != max) {
				ACC.productDetail.updateQtyValue(self, inputVal + 1)
				if (inputVal + 1 == max) {
					plusBtn.attr("disabled", "disabled")
				}
			} else {
				plusBtn.attr("disabled", "disabled")
			}
		} else if (mode == "input") {
			if (inputVal == 1) {
				$(self).parents(".js-qty-selector").find(
						".js-qty-selector-minus").attr("disabled", "disabled")
			} else if (inputVal == max) {
				$(self).parents(".js-qty-selector").find(
						".js-qty-selector-plus").attr("disabled", "disabled")
			} else if (inputVal < 1) {
				ACC.productDetail.updateQtyValue(self, 1)
				$(self).parents(".js-qty-selector").find(
						".js-qty-selector-minus").attr("disabled", "disabled")
			} else if (inputVal > max) {
				ACC.productDetail.updateQtyValue(self, max)
				$(self).parents(".js-qty-selector").find(
						".js-qty-selector-plus").attr("disabled", "disabled")
			}
		}

	},
	updateQtyValue : function(self, value) {
		var input = $(self).parents(".js-qty-selector").find(
				".js-qty-selector-input");
		var addtocartQty = $(self).parents(".addtocart-component").find(
				"#addToCartForm").find(".js-qty-selector-input");
		;

		input.val(value);
		addtocartQty.val(value);
	},
	initPageEvents : function() {

		$(document).on("click", '.js-qty-selector .js-qty-selector-minus',
				function() {
					ACC.productDetail.checkQtySelector(this, "minus");
				})

		$(document).on("click", '.js-qty-selector .js-qty-selector-plus',
				function() {
					ACC.productDetail.checkQtySelector(this, "plus");
				})

		$(document)
				.on(
						"keydown",
						'.js-qty-selector .js-qty-selector-input',
						function(e) {

							if (($(this).val() != " " && ((e.which >= 48 && e.which <= 57) || (e.which >= 96 && e.which <= 105)))
									|| e.which == 8
									|| e.which == 46
									|| e.which == 37
									|| e.which == 39
									|| e.which == 9) {
							} else if (e.which == 38) {
								ACC.productDetail
										.checkQtySelector(this, "plus");
							} else if (e.which == 40) {
								ACC.productDetail.checkQtySelector(this,
										"minus");
							} else {
								e.preventDefault();
							}
						})

		$(document).on("keyup", '.js-qty-selector .js-qty-selector-input',
				function(e) {
					ACC.productDetail.checkQtySelector(this, "input");
					ACC.productDetail.updateQtyValue(this, $(this).val());

				})

		$("#Size").change(function() {
			var url = "";
			var selectedIndex = 0;
			$("#Size option:selected").each(function() {
				url = $(this).attr('value');
				selectedIndex = $(this).attr("index");
			});
			if (selectedIndex != 0) {
				window.location.href = url;
			}
		});

		$("#variant").change(function() {
			var url = "";
			var selectedIndex = 0;
			$("#variant option:selected").each(function() {
				url = $(this).attr('value');
				selectedIndex = $(this).attr("index");
			});
			if (selectedIndex != 0) {
				window.location.href = url;
			}
		});
		
		
		// Move to wish list msg
		//alert(localStorage.getItem("movedToWishlist_msg"));
		//alert(localStorage.getItem("removeFromCart_msgFromCart"));
		if(localStorage.getItem("movedToWishlist_msgFromCart")=="Y")
		{
		$('#movedToWishlist_Cart').show();
		setTimeout(function() {
			  $("#movedToWishlist_Cart").fadeOut().empty();
			}, 1500);
		}
		 localStorage.removeItem('movedToWishlist_msgFromCart');
		 
		 
		 
		 // remove from cart msg
		 
			if(localStorage.getItem("removeFromCart_msgFromCart")=="Y")
			{
			$('#removeFromCart_Cart').show();
			setTimeout(function() {
				  $("#removeFromCart_Cart").fadeOut().empty();
				}, 1500);
			}
			 localStorage.removeItem('removeFromCart_msgFromCart');
			 
		
		// Sise Guide Select Color
		   
		$(document).on("click", 'a[data-target=#popUpModal] ',
			function() {
			   var target = $(this).attr("href");
			   console.log(target);
			   var productcode= $(this).attr("data-productcode");
			   console.log(productcode);
		 	   //$("#popUpModal").modal('hide');
			   $('body').on('hidden.bs.modal', '#popUpModal', function () {
					  $(this).removeData('bs.modal');
					});

			   // load the url and show modal on success
			   $("#popUpModal .modal-content").load(target, function() { 
				   	   $("#popUpModal").modal("show");
					   buyboxDetailsForSizeGuide(productcode);
			    });
			  
		});
		//End
		
		// Sise Guide Select Size
		$(document).on("change", '.variant-select',function(){
			console.log($(this).find('option:selected').data('productcode1'));
//			var value = $("#variant .dsa").attr("value");
			var value = $(this).find('option:selected').data('producturl');
			
			console.log(value);
			var productcode = $(this).find('option:selected').data('productcode1')
			console.log(productcode);
			
		    // load the url and show modal on success
		    $("#popUpModal .modal-content").load(value, function() { 
		         $("#popUpModal").modal("show");
		     	buyboxDetailsForSizeGuide(productcode);
		    });
		});
		//End

	},

	bindVariantOptions : function() {
		ACC.productDetail.bindCurrentStyle();
		ACC.productDetail.bindCurrentSize();
		ACC.productDetail.bindCurrentType();
	},

	bindCurrentStyle : function() {
		var currentStyle = $("#currentStyleValue").data("styleValue");
		var styleSpan = $(".styleName");
		if (currentStyle != null) {
			styleSpan.text(": " + currentStyle);
		}
	},

	bindCurrentSize : function() {
		var currentSize = $("#currentSizeValue").data("sizeValue");
		var sizeSpan = $(".sizeName");
		if (currentSize != null) {
			sizeSpan.text(": " + currentSize);
		}

	},

	bindCurrentType : function() {
		var currentSize = $("#currentTypeValue").data("typeValue");
		var sizeSpan = $(".typeName");
		if (currentSize != null) {
			sizeSpan.text(": " + currentSize);
		}

	}

};

/**
 * displaying thumb nails details
 */
var ussidValue = "";
$(document).on("click","#colorbox .productImageGallery .imageList img", function(e) {
			$("#colorbox .main-image img.picZoomer-pic").attr("src",
					$(this).attr("data-primaryimagesrc"));
			$("#colorbox .productImageGallery .thumb").removeClass("active");
			$(this).parent(".thumb").addClass("active");
			$(".zoomContainer").remove();
			$('.picZoomer-pic').removeData('zoom-image');
			$("img.picZoomer-pic").attr('data-zoom-image',$(".quickview .product-image-container .productImageGallery .active img").attr("data-zoomimagesrc"));
			$('.quickview .picZoomer-pic').elevateZoom({
			    zoomType: "window",
			    cursor: "crosshair",
			    zoomWindowFadeIn: 500,
			    zoomWindowFadeOut: 750
			       });
});

$(".product-image-container .productImageGallery.pdp-gallery .imageList img").click(
		function(e) {
		   
		    if($(this).attr("data-type")=='image'){
		    	$("#player").hide();
		    	$(".productImagePrimary .picZoomer-pic-wp img").show();
			$(".productImagePrimary .picZoomer-pic-wp img").attr("src",
					$(this).attr("data-primaryimagesrc"));
			$("#player").attr("src","");
			$(".super_zoom img").attr("src",
					$(this).attr("data-zoomimagesrc"));
			
			$(".productImageGallery .thumb").removeClass("active");
			$(this).parent(".thumb").addClass("active");
			$(".zoomContainer").remove();
			$('.picZoomer-pic').removeData('zoom-image');
			$(".picZoomer-pic-wp img").attr('data-zoom-image',$(this).attr("data-zoomimagesrc"));
			if ($(window).width() > 789) {
				$('.picZoomer-pic-wp .picZoomer-pic').elevateZoom({
				    zoomType: "window",
				    cursor: "crosshair",
				    zoomWindowFadeIn: 500,
				    zoomWindowFadeOut: 750
				       });
			}
		    }else{
		    	var url = $(this).attr("data-videosrc");
		    	$("#player").show();
				$("#player").attr("src",url);
				$("#videoModal #player").attr("src",url);
				$("#videoModal").modal();
				$("#videoModal").addClass("active");
				//$(".productImagePrimary .picZoomer-pic-wp img").hide();
				/*$(".zoomContainer").remove();
				$('.picZoomer-pic').removeData('zoom-image');*/
		    }
		});

function openPop(ussidfromSeller) {
	var loggedIn=$("loggedIn").val();
	$('#addedMessage').hide();
	if (ussidfromSeller == null || ussidfromSeller == "") {
		ussidValue = $("#ussid").val();
	} else {
		ussidValue = ussidfromSeller;
	}
	var productCode = $("#product").val();

	var requiredUrl = ACC.config.encodedContextPath + "/p"
			+ "/viewWishlistsInPDP";

	var dataString = 'productCode=' + productCode + '&ussid=' + ussidValue;// modified
	// for
	// ussid

	$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		dataType : "json",
		success : function(data) {
		if (data==null) {

				$("#wishListNonLoggedInId").show();
				$("#wishListDetailsId").hide();
			}

			else if (data == "" || data == []) {

				loadDefaultWishListName();

			} else {
				LoadWishLists(ussidValue, data, productCode);
			}
		},
		error : function(xhr, status, error) {
			$("#wishListNonLoggedInId").show();
			$("#wishListDetailsId").hide();
		}
	});
}

function loadDefaultWishListName() {
	var wishListContent = "";
	var wishName = $("#defaultWishId").text();
	$("#wishListNonLoggedInId").hide();
	$("#wishListDetailsId").show();

	wishListContent = wishListContent
	+ "<tr><td><input type='text' id='defaultWishName'  value='"
	+ wishName + "'/></td></td></tr>"; 
	$("#wishlistTbodyId").html(wishListContent);
   
}

//TISSTRT-907  WishList Special character implementation
$(document).on("keypress",'#defaultWishName',function(e) {
	var isValid = false;
	var wishlistname = $("#defaultWishName").val();
		var key = e.keyCode;
		if((key>=33 && key<48) || (key>=58 && key<65) || (key>=91 && key<97)){
			e.preventDefault();
			 var start = this.selectionStart,
		         end = this.selectionEnd;
			$('#defaultWishName').val(wishlistname);
			$('#addedMessage').show();
			$('#addedMessage').html("<font color='#ff1c47'><b>Special characters are not allowed</b></font>");
			$("#addedMessage").show().fadeOut(3000);
			this.setSelectionRange(start, end);
		} 
	}) 

function gotoLogin() {
	window.open(ACC.config.encodedContextPath + "/login", "_self");
}

var wishListList = [];

// load wishlist of a particular user on opening popup

function LoadWishLists(ussid, data, productCode) {
    
	// modified for ussid
	var wishListContent = "";
	var wishName = "";
	$this = this;
	$("#wishListNonLoggedInId").hide();
	$("#wishListDetailsId").show();

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
					+ "' style='display: none' onclick='selectWishlist("
					+ i + ")' disabled><label for='radio_"
					+ i + "'>"+wishName+"</label></td></tr>";
		} else {
			index++;
		  
			wishListContent = wishListContent
					+ "<tr><td><input type='radio' name='wishlistradio' id='radio_"
					+ i
					+ "' style='display: none' onclick='selectWishlist("
					+ i + ")'><label for='radio_"
					+ i + "'>"+wishName+"</label></td></tr>";
		}

	}

	$("#wishlistTbodyId").html(wishListContent);

}

function selectWishlist(i) {
	$("#hidWishlist").val(i);
}

function addToWishlist() {

	var productCodePost = $("#productCodePost").val();

	var wishName = "";
  
	if (wishListList == "") {
		wishName = $("#defaultWishName").val().trim();
	} else {
		wishName = wishListList[$("#hidWishlist").val().trim()];
	}
	if(wishName=="" || wishName.trim()==""){
		var msg=$('#wishlistnotblank').text();
		$('#addedMessage').show();
		$('#addedMessage').html(msg);
		return false;
	}
    if(wishName==undefined||wishName==null){
    	return false;
    }
	var requiredUrl = ACC.config.encodedContextPath + "/p"
			+ "/addToWishListInPDP";
    var sizeSelected=true;
    if( $("#variant,#sizevariant option:selected").val()=="#"){
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
				$("#radio_" + $("#hidWishlist").val()).prop("disabled", true);
				var msg=$('#wishlistSuccess').text();
				$('#addedMessage').show();
				$('#addedMessage').html(msg);
				setTimeout(function() {
					  $("#addedMessage").fadeOut().empty();
					}, 1500);
				populateMyWishlistFlyOut(wishName);
				
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
	
	//$('a.wishlist#wishlist').popover('hide');
	//$('input.wishlist#add_to_wishlist').popover('hide');
	
	setTimeout(function() {
		$('a.wishlist#wishlist').popover('hide');
		$('input.wishlist#add_to_wishlist').popover('hide');
		}, 1500);
}


function populateMyWishlistFlyOut(wishName) {
	var requiredUrl = ACC.config.encodedContextPath + "/my-account/wishlistAndItsItems";
	var dataString = 'wishlistName=' + wishName;
	
	$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		dataType : "json",
		success : function(response) {
			$('#DropDownMyWishList').empty();		
			for(var i in response) {
			var name=response[i].wishlistName;
			var size=response[i].wishlistSize;
			var url=response[i].wishlistUrl;
			 $('#DropDownMyWishList').append('<li><a href="'+url+'">'+name+'<br><span>'+size+'&nbsp;items</span></a></li>');
			}
		},
	})
}


function showUrlInDialog(url) {
	var tag = $("<div></div>");
	$.ajax({
		url : url,
		success : function(data) {
			tag.html(data).dialog({
				modal : true
			}).dialog('open');
		}
	});
}

/* Determining strike out price and product actual price */
function setValidPrice(sellersArray, index) {

	var roundedSpPrice = Math.round(sellersArray[index].spPrice * 100) / 100;
	if (sellersArray[index].spPrice != null && sellersArray[index].spPrice != 0) {
		if (sellersArray[index].mopPrice == mrp) {
			$("#mrpPriceId").append("<strike>" + mrp + "</strike>");
			$("#mopPriceId").html("");
			$("#spPriceId").append(roundedSpPrice);
		} else {
			$("#mrpPriceId").append("<strike>" + mrp + "</strike>");
			$("#mopPriceId").append(
					"<strike>" + sellersArray[index].mopPrice + "</strike>");
			$("#spPriceId").append(roundedSpPrice);
		}

	} else {
		if (sellersArray[index].mopPrice != null
				&& sellersArray[index].mopPrice != 0) {
			if (sellersArray[index].mopPrice == mrp) {
				$("#mrpPriceId").append(mrp);
				$("#mopPriceId").html("");
				$("#spPriceId").html("");
			} else {
				$("#mrpPriceId").append("<strike>" + mrp + "</strike>");
				$("#mopPriceId").append(sellersArray[index].mopPrice);
				$("#spPriceId").html("");
			}
		} else {
			$("#mrpPriceId").append(mrp);
			$("#mopPriceId").html("");
			$("#spPriceId").html("");
		}
	}
	
	if (mrp == "") {
		$("#mrpPriceId").hide();
	} else {
		$("#mrpPriceId").show();
	}

}

// pricedetails display at the time of changing the seller in pdp
function setSeller(index) {
	$("#inventory").hide();
	setValidPrice(sellersArray, index);

	$("#sellerNameId").html(sellersArray[index].sellername);
	$("#ussid").val(sellersArray[index].ussid);
	$("#stock").val(sellersArray[index].stock);

	populateSellers(index);

}

/* refeshing seller data after getting response from pincode service */
/* refeshing seller data after getting response from pincode service */
function refreshSellers(dataArray, ussid) {
	var stockArray = [];

	var nonservicableussids = [];
	var isproductPage = $("#isproductPage").val();
	var usidList = [];

	var newSellersArray = [];
	var seq = -1;
	var ussidListWithNoStock = [];
	var n = -1;
	var m = -1;
	var indx = -1;
	var priceAmount = [];
	var count = 0;
	var k = -1;
	var contentData = '';
	var priceDataList = [];
	var skuIdsForED = [];
	var skuIdsForHD = [];
	var skuForCodList = [];
	var stockDataArrayList = [];
	var stockIndx = -1;
	var c = -1
	for (var i = 0; i < dataArray.length; i++) {
		if (ussid != dataArray[i]['ussid']) {
			if (dataArray[i]['isServicable'] == 'Y') {
				if (dataArray[i]['cod'] == 'Y') {
					skuForCodList[++c] = "'" + dataArray[i]['ussid'] + "'";
				}
				usidList[i] = "'" + dataArray[i]['ussid'] + "'";
				count = count + 1;
				/*
				 * if (dataArray[i]['validDeliveryModes'] == "" ||
				 * dataArray[i]['validDeliveryModes'] == null) {
				 */
				if (dataArray[i]['stockCount'] == 0) {
					ussidListWithNoStock[++n] = "'" + dataArray[i]['ussid']
							+ "'";// setting all the skuIds without stock

				}
				// else{
               
				var deliveryModes = dataArray[i]['validDeliveryModes'];
				for ( var j in deliveryModes) {
					var mode = deliveryModes[j]['type'];
					if (mode == 'HD') {
						skuIdsForHD[++indx] = "'" + dataArray[i]['ussid'] + "'";
					}
					if (mode == 'ED') {
						skuIdsForED[++indx] = "'" + dataArray[i]['ussid'] + "'";
					}
					// var stockDataArray=new Object();
					var stockDataArray = {}
					stockDataArray.ussid = dataArray[i]['ussid'];
					stockDataArray.stock = dataArray[i]['stockCount'];
					stockDataArrayList[++stockIndx] = stockDataArray;
					$("#stockDataArray")
							.val(JSON.stringify(stockDataArrayList));

				}

				// }
			} else {
				nonservicableussids[++m] = dataArray[i]['ussid'];
			}

		}
	}

	// refreshing seller list after getting response from pincode
	if (count == 0) {
		$("#otherSellerInfoId").hide();
	} else {

		$("#otherSellerInfoId").show();
		$("#otherSellersId").html(count);

	}
	$("#otherSellersCount").html(count);
	var usidList = usidList.join(",");
	var ussidListWithNoStock = ussidListWithNoStock.join(",");

	$("#sellersSkuListId").val(nonservicableussids);
	$("#skuIdForED").val(skuIdsForED);
	$("#skuIdForHD").val(skuIdsForHD);
	$("#skuIdForCod").val(skuForCodList);
	$("#skuIdsWithNoStock").val(ussidListWithNoStock);
	$("#isPinCodeChecked").val("true");
	$("#sellerListId").html(contentData);
	if (isproductPage == 'false') {
		$("#sellerTable").show();
		$("#other-sellers-id").show();
		if (count > 0) {
			fetchAllSellers(stockDataArrayList);
		} else {
			$("#sellerTable").hide();
			$("#other-sellers-id").hide();
		}
	}

}



function populateSellers(index) {
	var contentData = '';
	for (var i = 0; i < sellersArray.length; i++) {
		if (i != index) {
			contentData += '<tr>';
			contentData += "<td> <a href='javascript: void(0)'	onclick='setSeller("
					+ i + ")'>" + sellersArray[i].sellername + "</a></td>";

			contentData += "<td>" + sellersArray[i].ussid + "</td>";
			contentData += "<td>&nbsp;" + sellersArray[i].stock + "</td>";
			contentData += '</tr>';
		}
	}

	$("#sellerListId").html(contentData);

}

function isNum(evt) {
		//var fieldLength = document.getElementById('pin').value.length;
	    evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
	        return false;
	    }
	    if(evt.keyCode === 13){
	    	$(".submit").click();
	    }
	  /*  if(fieldLength > 5){
            return false;
	    }*/
	    return true;
}


var pinCodeChecked = false;
$(function() {

	var regExp = /^([1-9])([0-9]){5}$/;
	$("#codId").hide();

	$(".submit")
			.click(
					function() {
						pinCodeChecked = true;
						$("#home").hide();
						$("#express").hide();
						$("#codId").hide();
						$(
								'#wrongPin,#unableprocessPin,#unsevisablePin,#emptyPin')
								.hide();
						$('#addToCartButton-wrong').attr("disable", true);
						$('#addToCartButton-wrong').hide();
						$("#outOfStockId").hide();

						// $('#addToCartButton').show();
						var checkBuyBoxIdPresent = false;
						var buyboxSeller = $("#ussid").val();
						var pin = $("#pin").val();
						var requiredUrl = ACC.config.encodedContextPath + "/p"
								+ "/checkPincode";

						if (pin == "") {
							$('#unsevisablePin,#unableprocessPin,#wrongPin')
									.hide();
							$("#emptyPin").show();
							$('#addToCartButton').show();
							$('#buyNowButton').attr("disabled",false);

							return false;
						} else if (!regExp.test(pin)) {
							$('#unsevisablePin,#unableprocessPin,#emptyPin')
									.hide();
							$("#wrongPin").show();
							$('#addToCartButton').show();
							$('#buyNowButton').attr("disabled",false);

							return false;
						}
						var dataString = "pin=" + pin + "&productCode="
								+ productCode;
						jQuery
								.ajax({
									// type: 'POST',
									contentType : "application/json; charset=utf-8",
									url : requiredUrl,
									data : dataString,
									success : function(data) {

										if (data == "" || data == []
												|| data == null) {
											refreshSellers(data, buyboxSeller);
											$("#home").hide();
											$("#express").hide();
											$(
													'#wrongPin,#unableprocessPin,#emptyPin')
													.hide();
											$('#addToCartFormTitle').hide();
											$('#addToCartButton-wrong').show();
											$('#addToCartButton').hide();
											$('#unsevisablePin').show();
											$('#buyNowButton').attr("disabled",true);
											return false;
										}
										// check if oms service is down
										else if (data[0]['isServicable'] == 'NA') {
											$("#home").show();
											$("#express").show();
											$("#codId").show();

											return false;
										} else {
											// refreshing seller list after
											// getting pincode response
											refreshSellers(data, buyboxSeller);
											for ( var i in data) {
												var pincodedata = data[i];
												ussid = pincodedata['ussid'];

												if (ussid == buyboxSeller) {

													if (pincodedata['isServicable'] == 'Y') {
														checkBuyBoxIdPresent = true;
														deliveryModes = pincodedata['validDeliveryModes'];
														var home = false;
														var exp = false;
														var click = false;

														/*
														 * if (deliveryModes == "" ||
														 * deliveryModes ==
														 * null||pincodedata['stockCount']==0) {
														 */
														if (pincodedata['stockCount'] == 0) {
															$(
																	"#addToCartButton")
																	.hide();
															$("#outOfStockId")
																	.show();
															$("#buyNowButton").hide();
															$("#stock").val(0);

														} else {
															$(
																	"#addToCartButton")
																	.show();
															$("#buyNowButton").show();
														}
														if (pincodedata['cod'] == 'Y') {

															$("#codId").show();
														}

														for ( var j in deliveryModes) {
															var mode = deliveryModes[j];

															deliveryModeName = mode['type'];

															$("#stock")
																	.val(
																			pincodedata['stockCount']);
															// checking
															// home-delivery(HD)
															// mode
															if (deliveryModeName == 'HD') {
																home = true;

															}
															// checking
															// click&collect(CnC)
															// mode
															else if (deliveryModeName == 'CnC') {

																click = true;
																/*
																 * $(".Click")
																 * .show();
																 */

															}
															// checking
															// express(express)
															// mode
															else {
																exp = true;

															}
														}
														if (home == true) {
															$("#home").show();
														} else {
															$("#home").hide();
														}
														if (exp == true) {
															$("#express")
																	.show();
														} else {
															$("#express")
																	.hide();
														}

														// }

													} else {
														$("#home").hide();
														$("#click").hide();
														$("#express").hide();
														$(
																'#wrongPin,#unableprocessPin,#emptyPin')
																.hide();
														$('#addToCartFormTitle')
																.hide();
														if ($("#stock").val() > 0) {
															$(
																	'#addToCartButton-wrong')
																	.show();
															$('#buyNowButton').attr("disabled",true);
														} else {
															$("#outOfStockId")
																	.show();
															$("#buyNowButton").hide();
														}
														$('#addToCartButton')
																.hide();
														$('#unsevisablePin')
																.show();

													}
												}

											}
											if (!checkBuyBoxIdPresent) {
												$("#home").hide();
												$("#click").hide();
												$("#express").hide();
												$(
														'#wrongPin,#unableprocessPin,#emptyPin')
														.hide();
												$('#addToCartFormTitle').hide();
												if ($("#stock").val() > 0) {
													$('#addToCartButton-wrong')
															.show();
												} else {
													$("#outOfStockId").show();
													$("#buyNowButton").hide();
												}
												// $('#addToCartButton-wrong').show();
												$('#addToCartButton').hide();
												$('#unsevisablePin').show();

											}
										}
										$("#pinCodeChecked")
												.val(pinCodeChecked);
									},
									error : function(xhr, status, error) {
										$('#wrongPin,#unsevisablePin,#emptyPin')
												.hide();
										$('#unableprocessPin').show();

									}
								});
					});

});



/**
 * This method is used to display delivery modes against a sku id
 */
function fetchPrice() {
	var categoryType = $("#categoryType").val();
	var selectedSize = "";
	if ($("#variant,#sizevariant option:selected").val() != "#") {
		selectedSize = true;
	}
	var isproductPage = $("#isproductPage").val();
	$("#addToCartButton").show();
	$("#outOfStockId").hide();
	var productCode = $("#product").val();
	var requiredUrl = ACC.config.encodedContextPath + "/p" + "/" + productCode
			+ "/buybox";
	var dataString = 'productCode=' + productCode;
	$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		dataType : "json",
		success : function(data) {
			if (data['sellerArticleSKU'] != undefined) {
				if (data['errMsg'] != "") {

					return false;
				} else {
					var promorestrictedSellers = $("#promotedSellerId").val();
					if (promorestrictedSellers == null
							|| promorestrictedSellers == undefined
							|| promorestrictedSellers == "") {
						$("#promotionDetailsId").show();
					} else {
						if (promorestrictedSellers.length > 0
								&& !(promorestrictedSellers
										.indexOf(data['sellerId']) == -1))
							$("#promotionDetailsId").show();
					}
					var allStockZero = data['allOOStock'];
					// var codEnabled = data['isCod'];
					var sellerName = data['sellerName'];
					var sellerID = data['sellerId'];
					
					$("#sellerNameId").html(sellerName);
					$("#sellerSelId").val(sellerID);
					
					if (allStockZero == 'Y' && data['othersSellersCount']>0) {
						$("#addToCartButton").hide();
						$("#outOfStockId").show();
						$("#buyNowButton").hide();
						$("#otherSellerInfoId").hide();
						$("#otherSellerLinkId").show();
					}
					else if (allStockZero == 'Y' && data['othersSellersCount']==0) {
						$("#addToCartButton").hide();
						$("#buyNowButton").hide();
						$("#outOfStockId").show();
						$("#otherSellerInfoId").hide();
						$("#otherSellerLinkId").hide();
					}
					else if (data['othersSellersCount'] == 0) {
						$("#otherSellerInfoId").hide();
						$("#otherSellerLinkId").hide();
					} 
					else if(data['othersSellersCount'] == -1) {
						$("#otherSellerInfoId").hide();
						$("#otherSellerLinkId").show();
					}
					else {

						$("#otherSellersId").html(data['othersSellersCount']);
						$("#minPriceId").html(data['minPrice'].formattedValue);
					}

					$("#ussid").val(data['sellerArticleSKU']);
					$("#sellerSkuId").val(data['sellerArticleSKU']);

					var spPrice = data['specialPrice'];
					var mrpPrice = data['mrp'];
					var mop = data['price'];

					$("#stock").val(data['availablestock']);
					$(".selectQty").change(function() {
						$("#qty").val($(".selectQty :selected").val());
					});
					displayDeliveryDetails(sellerName);
					dispPrice(mrpPrice, mop, spPrice);
					if (isproductPage == 'false') {
						fetchAllSellers();
						$("#minPrice").html(data['minPrice'].formattedValue);
					}
				}

			} 
				else {
				 $('#addToCartButton-wrong').attr("disable",true);
				 $('#addToCartButton-wrong').show();
				 $('#addToCartButton').hide();
				 $("#otherSellerInfoId").hide();
				 $(".wish-share").hide();
				 $(".fullfilled-by").hide();
				// TISST-13959 fix
				 $("#dListedErrorMsg").show();
				 // TISEE-6552 fix
				 $("#pdpPincodeCheck").hide();
				 $("#pin").attr("disabled",true);
				 $("#pdpPincodeCheckDList").show();
				 $("#buyNowButton").attr("disabled",true);
				 
				 
				
			}
		}

	});
}
/**
 * This method is used to display delivery modes against a sku id
 */
function displayDeliveryDetails(sellerName) {
	var buyboxSeller = $("#ussid").val();
	var productCode = $("#product").val();
	var requiredUrl = ACC.config.encodedContextPath + "/p" + "/" + productCode
			+ "/getRichAttributes";
	var dataString = 'buyboxid=' + buyboxSeller;
	$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		dataType : "json",
		success : function(data) {
			if (data != null) {
				var pretext=$("#deliveryPretext").text();
				var posttext=$("#deliveryPosttext").text();
				var fulFillment = data['fulfillment'];
				var deliveryModes = data['deliveryModes'];
				var leadTime=0;
				if(null!=data['leadTimeForHomeDelivery']){
					leadTime=data['leadTimeForHomeDelivery'];
				}
				if (null != data['newProduct']
						&& data['newProduct'].toLowerCase() == 'y') {
					$('#newProduct').show();
					$(".picZoomer-pic-wp #codId").css("top", "85" + "px");
				}
				if (data['onlineExclusive']) {
					$('.online-exclusive').show();
				}
				if (fulFillment.toLowerCase() == 'tship') {
					$('#fulFilledByTship').show();
				} else {
					$('#fulFilledBySship').show();
					$('#fulFilledBySship').html(sellerName);
				}
				if (deliveryModes.indexOf("HD") == -1) {
					$("#home").hide();
					$(".hdclass").hide();
				} else {
					var start=parseInt($("#homeStartId").val())+leadTime;
					var end=parseInt($("#homeEndId").val())+leadTime;
					$("#homeDate").html(pretext+start+"-"+end+posttext);
					$("#home").show();
				}
				
				if (deliveryModes.indexOf("ED") == -1) {
					$("#express").hide();
					$(".edclass").hide();
				} else {
					var start=$("#expressStartId").val();
					var end=$("#expressEndId").val();
					
					//alert(pretext);
					$("#expressDate").html(pretext+start+"-"+end+posttext);
					$("#express").show();
				}

				// enable COD flag if COD enabled
				if (data['isCod'] == 'Y') {
					$("#codId").show();
				} else {
					$("#codId").hide();
				}
				if(null != data['returnWindow'])
					{
					$("#returnWindow").text(data['returnWindow']);
					}
				else
					{
					$("#returnWindow").text("0");
					}
			}
		}
	});
}
function dispPrice(mrp, mop, spPrice) {
	
	if(null!= mrp){
		$("#mrpPriceId").append(mrp.formattedValue);
	}
	if(null!= mop){
		$("#mopPriceId").append(mop.formattedValue);
	}
	if(null!= spPrice){
		$("#spPriceId").append(spPrice.formattedValue);
	} 

	if (null!=spPrice && spPrice != 0) {

		if (mop.value == mrp.value) {

			$('#mrpPriceId').css('text-decoration', 'line-through');
			$("#mrpPriceId").show();
			$("#spPriceId").show();
		} else {

			$('#mrpPriceId').css('text-decoration', 'line-through');
			$("#mrpPriceId").show();
			$("#spPriceId").show();
		}

	} else {
		if (null!=mop && mop.value != 0) {
			if (mop.value == mrp.value) {
				$("#mrpPriceId").removeClass("old").addClass("sale");
				$("#mrpPriceId").show();
			} else {
				$('#mrpPriceId').css('text-decoration', 'line-through');
				$("#mrpPriceId").show();
				$("#mopPriceId").show();
			}
		} else {
			$("#mrpPriceId").show();
		}
	}
	if (mrp.value == "") {
		$("#mrpPriceId").hide();
	} else {
		$("#mrpPriceId").show();
	}

	// EMI change starts
	if (spPrice != undefined || null!=spPrice) {
		$("#prodPrice").val(spPrice.value);
		$("#price-for-mad").val(spPrice.value); 
	} else if (mop != undefined || null!=mop) {
		$("#prodPrice").val(mop.value);
		$("#price-for-mad").val(mop.value); 
	} else {
		$("#prodPrice").val(mrp.value);
		$("#price-for-mad").val(mrp.value); 
	}

	if (parseInt($("#prodPrice").val()) > emiCuttOffAmount.value) { 
		$("#emiStickerId").show();
	}
	// EMI change ends

}


function openPopForBankEMI() {
	var productVal = $("#prodPrice").val();
	var optionData = "<option value='select' disabled selected>Select</option>";
	$("#emiTableTHead").hide();
	$("#emiTableTbody").hide();
	var requiredUrl = ACC.config.encodedContextPath + "/p" + "/enlistEMIBanks";
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

/**
 * This method picks up the selected bank for EMI, fetches the term period
 * against the bank and creates a dynamic EMI table
 */
function getSelectedEMIBankForPDP() {
	var productVal = $("#prodPrice").val();
	var selectedBank = $('#bankNameForEMI :selected').text();
	var contentData = '';
	if (selectedBank != "select") {
		$.ajax({
			url : ACC.config.encodedContextPath + "/p/getTerms",
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

function CheckonReload()
{
	var contentData = '';
	 $.ajax({
				url : ACC.config.encodedContextPath + "/p/checkUser",
				data : {
				},
				type : "GET",
				cache : false,
				success : function(data) {
					if(!data)							
						{
							//Hiding the Comment Box if the User is not Logged In
							//TISUATPII-470 fix
							$('#commentsDiv .gig-comments-composebox').hide();
							//$('#commentsDiv .gig-comments-composebox').show();
						}
						else
						{
							//Showing the Comment Box if the User is  Logged In
							$('#commentsDiv .gig-comments-composebox').show();
							}
				},
				error : function(resp) {
					console.log( "Error Occured" );
				}
			});
}



function getRating(key,productCode,category)
{
	//alert('test');
	var url = "https://comments.us1.gigya.com/comments.getStreamInfo?apiKey="+key+"&categoryID="+category+"&streamId="+productCode+"&includeRatingDetails=true&format=jsonp&callback=?";
	 
	$.getJSON(url, function(data){
		console.log(data);
	  	var totalCount=data.streamInfo.ratingCount;
		//Reverse the source array
		var ratingArray = data.streamInfo.ratingDetails._overall.ratings;
		ratingArray  = ratingArray.reverse();
		
		  $("div.rate-details div.after").each(function(count){			  
				var countIndiv=ratingArray[count];								
				$("div.rate-bar div.rating").eq(count).css({width:countIndiv/totalCount*100+"%"});
				$("div.rate-details div.after").eq(count).text(ratingArray[count]);
				
			})
			
			var avgreview=data.streamInfo.avgRatings._overall;
			var raingcount=data.streamInfo.ratingCount;
			$(".product-detail ul.star-review a").empty();
			$(".product-detail ul.star-review li").attr("class","empty");
			
 			var rating = Math.floor(avgreview);
	 		var ratingDec = avgreview - rating;
	 		for(var i = 0; i < rating; i++) {
	 			$("#pdp_rating"+" li").eq(i).removeClass("empty").addClass("full");
	 			}
	 		if(ratingDec!=0)
	 			{
	 			$("#pdp_rating"+" li").eq(rating).removeClass("empty").addClass("half");
	 			} 
	 		
	 		//TISUATPII-471 fix
	 		
	 		if(raingcount == 1){
				$(".gig-rating-readReviewsLink_pdp").text(raingcount+" REVIEW");
				$('#ratingDiv .gig-rating-readReviewsLink').text(data.streamInfo.ratingCount+" REVIEW");
				}
				else if(raingcount > 0)
					{
					$(".gig-rating-readReviewsLink_pdp").text(raingcount+" REVIEWS");
					$('#ratingDiv .gig-rating-readReviewsLink').text(data.streamInfo.ratingCount+" REVIEWS");
					}
			$('#customer').text("Customer Reviews (" + data.streamInfo.ratingCount + ")");
			
			
			
			
			
	  });
	  
	//TISUATPII-471 fix
	  var ratingsParams = {
		categoryID : category,
		streamID : productCode,
		containerID : 'ratingDiv',
		linkedCommentsUI : 'commentsDiv',
		showCommentButton : 'true',
		onAddReviewClicked:	function(response) {
			CheckUserLogedIn();
	 }
			
	
	  }  
	  
	  gigya.comments.showRatingUI(ratingsParams);
//	$.getJSON("https://comments.us1.gigya.com/comments.getStreamInfo?apiKey="+key+"&categoryID="+category+"&streamId="+productCode+"&includeRatingDetails=true&format=jsonp&callback=hello",
//	         function(data) {
//		
//		$(".rate-details .after").each(function(count){
//			var totalCount=data.streamInfo.ratingCount;
//			var countIndiv=data.streamInfo.ratingDetails._overall.ratings[count];
//			$(".rate-bar .rating").eq(count).css({width:countIndiv/totalCount*100});
//			$(".rate-details .after").eq(count).text(data.streamInfo.ratingDetails._overall.ratings[count]);
//			
//		})
//		
//		var avgreview=data.streamInfo.avgRatings._overall;
//		var raingcount=data.streamInfo.ratingCount;
//		
//		rating(avgreview,raingcount);
//		
//		$('#customer').text("Customer Reviews (" + data.streamInfo.ratingCount + ")");
//		
//	          });
	

}


function CheckUserLogedIn() {
	var contentData = '';
 $.ajax({
			url : ACC.config.encodedContextPath + "/p/checkUser",
			data : {
				
			},
			type : "GET",
			cache : false,
			success : function(data) {
				if(!data)							
					{
						gotoLogin();
						
					}
					else
					{
							
						//$('.gig-comments-composebox').show();
							
						}
					
								
			},
			error : function(resp) {
				//alert("Error Occured");
				console.log( "Error Occured" );
			}
		});
	
}
function nextImage()
{
	var item_height = $(".product-info .imageListCarousel li").height();
	var item_count = $(".product-info .imageListCarousel li").length/2;
	var top = parseInt($(".product-info .imageListCarousel").css("top"));
	if(!(top % item_height)){
		if(parseInt($(".product-info .imageListCarousel").css("top")) > -item_height* (item_count-imagePageLimit)){
		$(".product-info .imageListCarousel").animate({top: "-="+item_height+"px"},"250");
		$("#previousImage,#zoomModal #previousImage").css("opacity","1");
		}
		else{
			$("#nextImage,#zoomModal #nextImage").css("opacity","0.5");
		}
	}
	if(parseInt($(".product-info .imageListCarousel").css("top")) == (item_count-imagePageLimit-1)*(-item_height)){
		$("#nextImage,#zoomModal #nextImage").css("opacity","0.5");
	}
	
}
/*Display previous list of sellers after clicking previous link*/	
function previousImage()
{
	var item_height = $(".product-info .imageListCarousel li").height();
	var top = parseInt($(".product-info .imageListCarousel").css("top"));
	if(!(top % item_height)){
		if(parseInt($(".product-info .imageListCarousel").css("top")) < 0 ){
			$("#nextImage,#zoomModal #nextImage").css("opacity","1");
			$(".product-info .imageListCarousel").animate({top: "+="+item_height+"px"},"250");
		}
		else{
			$("#previousImage,#zoomModal #previousImage").css("opacity","0.5");
		}
	}
	if(parseInt($(".product-info .imageListCarousel").css("top")) == -item_height){
		$("#previousImage,#zoomModal #previousImage").css("opacity","0.5");
	}
	
}

$(".showDate").click(function(){
	$(".show-date").toggle();
});

function sendmail(){
	$('#emailSuccess,#emailUnSuccess,#emailError,#validateemail,#emailEmpty').hide();
	var email = $('#emailId').val();
	var productId = $('#pId').val();
	var dataString = 'emailList=' + email+"&productId="+productId;
	if(email!=""){
	if(validEmail(email)){
		//var emailList = email.split(";");
		$.ajax({
			url : ACC.config.encodedContextPath + "/p/sendEmail",
			data : dataString,			
			type : "GET",
			cache : false,
			success : function(data) {
				if (data == true) {	
					$('#emailUnSuccess,#emailError,#validateemail,#emailEmpty').hide();
					$('#emailSuccess').show();
				}
				else{
					$('#emailSuccess,#emailError,#validateemail,#emailEmpty').hide();
					$('#emailUnSuccess').show();
					return false;
				}
			},
			error : function(resp) {
				$('#emailSuccess,#emailUnSuccess,#validateemail,#emailEmpty').hide();
				$('#emailError').show();
				return false;
			}
		});
	}
	else{
		$('#emailSuccess,#emailUnSuccess,#emailError,#emailEmpty').hide();
		$('#validateemail').show();
		return false;
	}
	}
	else{
		$('#emailSuccess,#emailUnSuccess,#emailError,#validateemail').hide();
		$('#emailEmpty').show();
		return false;
	}
}

function validEmail(email) {
	  var arr = email.split(',');
	  for(var i=0;i<arr.length;i++){
	  var emaili = arr[i];
	 
	  var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
	  if(!emailReg.test(emaili)){
		  return false;
	  }
	  }	  
	  return true;	  
	}
function dispPriceForSizeGuide(mrp, mop, spPrice) {
	//alert("mrp: "+mrp +" Mop: "+mop+" spPrice: "+spPrice);
	if(null!= mrp){
		$("#sizemrpPriceId").append(mrp);
	}
	if(null!= mop){
		$("#sizemopPriceId").append(mop);
	}
	if(null!= spPrice){
		$("#sizespPriceId").append(spPrice);
	} 

	if (null!=spPrice && spPrice != 0) {

		if (mop == mrp) {
			$('#sizemrpPriceId').css('text-decoration', 'line-through');
			$("#sizemrpPriceId").show();
			$("#sizespPriceId").show();
		} else {
			//alert("mop!=mrp sp");
			$('#sizemrpPriceId').css('text-decoration', 'line-through');
			$("#sizemrpPriceId").show();
			$("#sizespPriceId").show();
		}

	} else {
		if (null!=mop && mop != 0) {
			if (mop == mrp) {
				$("#sizemrpPriceId").removeClass("old").addClass("sale");
				$("#sizemrpPriceId").show();
			} else {
				//alert("mop!=mrp");
				$('#sizemrpPriceId').css('text-decoration', 'line-through');
				$("#sizemrpPriceId").show();
				$("#sizemopPriceId").show();
			}
		} else {
			$("#sizemrpPriceId").show();
		}
	}
	if (mrp == "") {
		$("#sizemrpPriceId").hide();
	} else {
		$("#sizemrpPriceId").show();
	}

}
function buyboxDetailsForSizeGuide(productCode){
	var sellerID= $("#sellerSelId").val();
	var productCode = productCode;//$("#product").val();
	
	console.log(sellerID +" "+productCode);
	var requiredUrl = ACC.config.encodedContextPath + "/p/buyboxDataForSizeGuide";
	var dataString = 'productCode=' + productCode+'&sellerId='+sellerID;
	
		$.ajax({
			contentType : "application/json; charset=utf-8",
			url : requiredUrl,
			data : dataString,
			dataType : "json",
			success : function(data) {
				var sellerName = data['sellerName'];
				var sellerID = data['sellerId'];
				var mopPrice = data['price'];
				var mrpPrice = data['mrp'];
				var specialPrice = data['specialPrice'];
				var availableStock = data['availablestock'];
				var ussid = data['sellerArticleSKU'];
				var nosellerData = data['noseller'];
				//var sizeSelected=true;
				
				var count =0;


//				if (!($(".size-guide.modal").is(":visible")) && $(".pdp #variant option:selected").val() == "#") {
//					$('#variant option#select-option').attr("selected", "selected");
//					sizeSelected=false;
//				}
				
				//$("#sizeSelectedVal").val(sizeSelected);
				
				if(sellerName=="undefined" || sellerName==null || sellerName=="")
				{
					$("#productDetails").hide();
					$("#sizePrice").hide();
					$("#addToCartSizeGuide").hide();
					$("#noProductForSelectedSeller").show();
					$("#addToCartSizeGuide #addToCartButton").attr("style", "display:none");
				}
				if (specialPrice != null){
					$("#specialSelPrice").html(specialPrice);
				}
				else{
					$("#specialSelPrice").html(mopPrice);
				}
//				if(data['isPinCodeServicable']=='N'){
//					$("#pinNotServicableSizeGuide").show();
//					$("#addToCartSizeGuide #addToCartButton").attr('disabled','disabled');
//				}
//				else{
//					$("#addToCartSizeGuide #addToCartButton").removeAttr('disabled');
//				}
				$("#sellerSelName").html(sellerName);
				$("#sellerIdSizeGuide").html(sellerID);
				$("#mopSelPrice").html(mopPrice);
				$("#mrpSelPrice").html(mrpPrice);
				$("#sizeStock").val(availableStock);
				$("#sellerSelArticleSKU").html(ussid);
				$("#sellerSelArticleSKUVal").val(ussid);
				$("#nosellerVal").val(nosellerData);
				dispPriceForSizeGuide(mrpPrice, mopPrice, specialPrice);
				if(availableStock==0){
					$("#outOfStockText").html("<font color='#ff1c47'>" + $('#outOfStockText').text() + "</font>");
					$("#addToCartSizeGuideTitleoutOfStockId").show();
					$("#addToCartSizeGuide #addToCartButton").attr("style", "display:none");
				}
				else{
					$("#addToCartSizeGuide #addToCartButton").removeAttr('style');
				} 
			}
		});
}


// Size Guide Popup

function openPop_SizeGuide() {
	//alert("sellerSelArticleSKUVal local: "+ $("#sellerSelArticleSKUVal").val());
	
	var loggedIn=$("loggedIn").val();
	
	//alert(ussidfromSeller);
	
	$('#addedMessage_sizeGuide').hide();
	//if (ussidfromSeller == null || ussidfromSeller == "") {
		ussidValue = $("#sellerSelArticleSKUVal").val();
	//} else {
	//	ussidValue = ussidfromSeller;
	//}
	var productCode = $("#productCode").val(); // '${product.code}';

	var requiredUrl = ACC.config.encodedContextPath + "/p"
			+ "/viewWishlistsInPDP";

	var dataString = 'productCode=' + productCode + '&ussid=' + ussidValue;// modified
	//alert("localdata: "+dataString);
	// for
	// ussid

	$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		dataType : "json",
		success : function(data) {
		if (data==null) {
				$("#wishListNonLoggedInId_sizeGuide").show();
				$("#wishListDetailsId_sizeGuide").hide();
			}

			else if (data == "" || data == []) {
				//alert("fasle");
				loadDefaultWishListName_SizeGuide();

			} else {
				LoadWishLists_SizeGuide(ussidValue, data, productCode);
				//alert("true");
			}
		},
		error : function(xhr, status, error) {
			$("#wishListNonLoggedInId_sizeGuide").show();
			$("#wishListDetailsId_sizeGuide").hide();
		}
	});
} 


function LoadWishLists_SizeGuide(ussid, data, productCode) {
	// modified for ussid
	
	//alert(ussid+" : "+data+" : "+productCode);
	
	var wishListContent = "";
	var wishName = "";
	$this = this;
	$("#wishListNonLoggedInId_sizeGuide").hide();
	$("#wishListDetailsId_sizeGuide").show();

	for ( var i in data) {
		var index = -1;
		var checkExistingUssidInWishList = false;
		var wishList = data[i];
		wishName = wishList['particularWishlistName'];
		
		//alert(wishName+" : wishName");
		
		wishListList[i] = wishName;
		var entries = wishList['ussidEntries'];
		for ( var j in entries) {
			var entry = entries[j];

			if (entry == ussid) {

				checkExistingUssidInWishList = true;
				break;

			}
		}
		//alert("checkExistingUssidInWishList : "+checkExistingUssidInWishList);
	
		if (checkExistingUssidInWishList) {
			index++;
            
			wishListContent = wishListContent
					+ "<tr class='d0'><td ><input type='radio' name='wishlistradio' id='radio_"
					+ i
					+ "' style='display: none' onclick='selectWishlist_SizeGuide("
					+ i + ")' disabled><label for='radio_"
					+ i + "'>"+wishName+"</label></td></tr>";
		} else {
			index++;
		  
			wishListContent = wishListContent
					+ "<tr><td><input type='radio' name='wishlistradio' id='radio_"
					+ i
					+ "' style='display: none' onclick='selectWishlist_SizeGuide("
					+ i + ")'><label for='radio_"
					+ i + "'>"+wishName+"</label></td></tr>";
					
		}

	}

	$("#wishlistTbodyId_sizeGuide").html(wishListContent);

}

function loadDefaultWishListName_SizeGuide() {
	var wishListContent = "";
	var wishName = $("#defaultWishId_sizeGuide").text();
	

	
	$("#wishListNonLoggedInId_sizeGuide").hide();
	$("#wishListDetailsId_sizeGuide").show();

	wishListContent = wishListContent
			+ "<tr><td><input type='text' id='defaultWishName_sizeGuide' value='"
			+ wishName + "'/></td></td></tr>";
	$("#wishlistTbodyId_sizeGuide").html(wishListContent);
	//alert(wishListContent+" wishListContent");

	}



	function selectWishlist_SizeGuide(i) {
		//alert(i+" : sizeguide");
	$("#hidWishlist_sizeGuide").val(i);

	}

	function addToWishlist_SizeGuide() {
	var productCodePost = $("#productCode").val(); //'${product.code}'; //
	//var productCodePost = $("#productCodePostQuick").val();
	//alert(productCodePost);
	var wishName = "";


	if (wishListList == "") {
		wishName = $("#defaultWishName_sizeGuide").val();
	} else {
		wishName = wishListList[$("#hidWishlist_sizeGuide").val()];
	}
	
	//alert("wishListList add: "+wishListList);
	
	if(wishName==""){
		var msg=$('#wishlistnotblank_sizeGuide').text();
		$('#addedMessage_sizeGuide').show();
		$('#addedMessage_sizeGuide').html(msg);
		//alert("1");
		return false;
	}
	if(wishName==undefined||wishName==null){
		//alert("2");
		return false;
	}
	var requiredUrl = ACC.config.encodedContextPath + "/p"
			+ "/addToWishListInPDP";
	var sizeSelected=true;
	if( $("#variant.size-g option:selected").val()=="#"){
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
				$("#radio_" + $("#hidWishlist_sizeGuide").val()).prop("disabled", true);
				var msg=$('#wishlistSuccess_sizeGuide').text();
				$('#addedMessage_sizeGuide').show();
				$('#addedMessage_sizeGuide').html(msg);
				setTimeout(function() {
					  $("#addedMessage_sizeGuide").fadeOut().empty();
					}, 1500);
				populateMyWishlistFlyOut(wishName);
				
				
				//For MSD
				var isMSDEnabled =  $("input[name=isMSDEnabled]").val();								
				if(isMSDEnabled === 'true')
				{
				console.log(isMSDEnabled);
				var isApparelExist  = $("input[name=isApparelExist]").val();
				console.log(isApparelExist);				
				var salesHierarchyCategoryMSD =  $("input[name=salesHierarchyCategoryMSD]").val();
				console.log(salesHierarchyCategoryMSD);
				var rootCategoryMSD  = $("input[name=rootCategoryMSD]").val();
				console.log(rootCategoryMSD);				
				var productCodeMSD =  $("input[name=productCodeMSD]").val();
				console.log(productCodeMSD);				
				var priceformad =  $("input[id=price-for-mad]").val();
				console.log(priceformad);				
				
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
			}
		},
	});

	setTimeout(function() {
		$('a.wishlist#wishlist').popover('hide');
		$('input.wishlist#add_to_wishlist-sizeguide').popover('hide');
		}, 1500);
	}
	
	$(document).on('click','#buyNow .js-add-to-cart',function(event){
		//var cartReturn = ACC.product.sendAddToBag("addToCartForm");
		ACC.product.sendAddToBag("addToCartForm",true);
	});
