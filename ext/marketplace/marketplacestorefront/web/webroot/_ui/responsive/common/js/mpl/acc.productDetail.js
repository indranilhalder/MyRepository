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
		
		// added for jewellery PDP size dropdown 		
		/*$("#jewelleryvariant").change(function() {
			var url = "";
			var selectedIndex = 0;
			$("#jewelleryvariant option:selected").each(function() {
				url = $(this).attr('value');
				selectedIndex = $(this).attr("index");
			});
			if (selectedIndex != 0) {
				window.location.href = url;
			}
		});
*/
// added in merging.....
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
			/*$('#removeFromCart_Cart').show();*/
			$('#removeFromCart_Cart').css("display","inline-block"); /*TISSQAEE-245*/

			setTimeout(function() {
				  $("#removeFromCart_Cart").fadeOut().empty();
				}, 1500);
			}
			 localStorage.removeItem('removeFromCart_msgFromCart');
			 
// added in merging.....
			 




	// SizeGuide
		
		// Sise Guide Select Color
		   
		$(document).on("click", '.colorBox',
			function() {
			  var target = $(this).attr('data-producturl');
			//   console.log(target);
			  var productcode= $(this).attr('data-productcode');
			//   console.log(productcode);
			   $('body').on('hidden.bs.modal', '#popUpModal', function () {
					  $(this).removeData('bs.modal');
					});

			   // load the url and show modal on success
			   $("#popUpModal .modal-content").load(target, function() { 
				   	   $("#popUpModal").modal("show");
					  // buyboxDetailsForSizeGuide(productcode);
			    });
			  
		});
		//End
		//added for Size guide Variant select
		//Sprint 7 Sanity Issue fixing starts here
		//$(document).on("click", '.variant-select li span',




		  $(document).on("click", '.variant-select li span, .color-swatch li span',
		//Sprint 7 Sanity Issue fixing ends here


				function() {
				  var target = $(this).attr('data-producturl');
				//   console.log(target);
				  var productcode= $(this).attr('data-productcode');
				//   console.log(productcode);
				   $('body').on('hidden.bs.modal', '#popUpModal', function () {
						  $(this).removeData('bs.modal');
						});

				   // load the url and show modal on success
				   $("#popUpModal .modal-content").load(target, function() { 
					   	   $("#popUpModal").modal("show");
						  // buyboxDetailsForSizeGuide(productcode);
				    });
				  
			});
		
		
		// Sise Guide Select Size
		/*$(document).on("change", '.variant-select',function(){
		//	console.log($(this).find('option:selected').data('productcode1'));
//			var value = $("#variant .dsa").attr("value");
			var value = $(this).find('option:selected').data('producturl');
			
	//		console.log(value);
			var productcode = $(this).find('option:selected').data('productcode1')
	//		console.log(productcode);
			
		    // load the url and show modal on success
		    $("#popUpModal .modal-content").load(value, function() { 
		         $("#popUpModal").modal("show");
		     	//buyboxDetailsForSizeGuide(productcode);
		    });
		});*/
		
		//TISPRO-333
		/*$(document).on("click", 'a[data-target=#popUpModal] ',
				function() {
				   var target = $(this).attr("href");
			//	   console.log(target);
				   var productcode= $(this).attr("data-productcode");
			//	   console.log(productcode);
			 	   //$("#popUpModal").modal('hide');
				   $('body').on('hidden.bs.modal', '#popUpModal', function () {
						  $(this).removeData('bs.modal');
						});

				   // load the url and show modal on success
				  // $("#popUpModal .modal-content").load(target, function() { 
					   	   $("#popUpModal").modal("show");
						   buyboxDetailsForSizeGuide(productcode);
				   // }); 
				   
		 }); */
		//End
		
		
		/// Size Guide onload
		
/*		var qtyData = $("#pdpQty").val();
		localStorage.setItem("sizeguideselectvaluePdp", qtyData);
		
		var qtyData1 = $("#quantity").val();
		localStorage.setItem("sizeguideselectvalueQview", qtyData1);
		
		$("select#sizeGuideQty").on("change", function(){
			var x = $("select#sizeGuideQty").val();
			localStorage.setItem("sizeguideselectvalue", x);
		});
		var sizeGuide = localStorage.getItem('sizeguideselectvalue');
		var pdp = localStorage.getItem('sizeguideselectvaluePdp');
		var qview = localStorage.getItem('sizeguideselectvalueQview');
		
		if(sizeGuide == null || sizeGuide==undefined)
		{
			
			if(pdp == null || pdp == 'undefined')
			{
				if(qview == null || qview == 'undefined')
				{
					$("#sizeGuideQty").val("1");
				}
				else
				{
					$("#sizeGuideQty").val(qview);
				}
			}
			else
			{
				 $("#sizeGuideQty").val(pdp);
			}
			
		}
		else
		{
			$("#sizeGuideQty").val(sizeGuide);
		}
		var currentColour = '${product.colour}';
		$(".color-swatch li span").each(function(){
			var title = $(this).attr("title");
			if(currentColour == title){
				$(this).parent().parent().addClass("active");
			}			
		});
		 if($('body').find('input.wishlist#add_to_wishlist-sizeguide').length > 0){
				$('input.wishlist#add_to_wishlist-sizeguide').popover({ 
					html : true,
					content: function() {
						return $(this).parents().find('.add-to-wishlist-container-sizeguide').html();
					}
				});
			  }
		var category=$("#categoryType").val(); 
		 if(category!='Footwear'){ 
		
		var numLi= $(".modal.size-guide .sizes .tables li.header > ul").children().length;
		var sizeWidth= 88/(numLi-1) + "%";

		$(".modal.size-guide .sizes .tables li > ul > li").css("width",sizeWidth);
		$(".modal.size-guide .sizes .tables li > ul > li:first-child").css("width","12%");
	 	} 

		$("#add_to_wishlist-sizeguide").click(function(){
		 	$(".size-guide .modal-content").animate({ scrollTop: $('.size-guide .modal-content')[0].scrollHeight }, "slow");
			return false;
		});
		$("#noProductForSelectedSeller").hide();
		$("#productDetails").show();
		$("#sizePrice").show();
		
		
		$('body').on('hidden.bs.modal', '#popUpModal', function () {
			 localStorage.removeItem('sizeguideselectvaluePdp');
			 localStorage.removeItem('sizeguideselectvalueqview');
			 localStorage.removeItem('sizeguideselectvalue');
			 
			 });*/
		
		
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

	},
};
	
	//update the message for Freebie product TPR-1754
	var freebieMsg="";
	
/**
 * displaying thumb nails details
 */
var ussidValue = "";
var jwllryShowPrcBrkUp = "";
var jwlryPrcBrkUp = "";
$(document).on("click","#colorbox .productImageGallery .imageList img", function(e) {
	if($(this).attr("data-type")=='image'){
		 $("#player").hide();
		 $(".productImagePrimary .picZoomer-pic-wp img").show();
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
	 }
	 else {
		 var url = $(this).attr("data-videosrc");
	    	$("#player").show();
			$("#player").attr("src",url);
			$("#videoModal #player").attr("src",url);
			$("#videoModal").modal();
			$("#videoModal").addClass("active");
	 } 
});

$(".product-image-container .productImageGallery.pdp-gallery .imageList img").click(
		function(e) {
		   var thumbnail_type=$(this).attr("data-type");
		   var thumbnail_value = $(this).parents('li').attr('id');
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
				    zoomWindowFadeOut: 750,
				   /* onZoomedImageLoaded:function(z){
				    	var mouse_X = 0;
						var mouse_Y = 0;
						$('.picZoomer-pic-wp .picZoomer-pic').on('hover',function(e){ 
					        //console.log("e.pageX: " + e.pageX + ", e.pageY: " + e.pageY);
					        mouse_X = e.pageX;
					        mouse_Y = e.pageY;
						});
						$(".zoomContainer .zoomLens").top(mouse_X);
						$(".zoomContainer .zoomLens").l(mouse_X);
				    	$(".zoomContainer .zoomLens").show();
				    } */
				 });
			}
		    }else{
		    	var url = $(this).attr("data-videosrc");
		    	$("#player").show();
				$("#player").attr("src",url);
				$("#videoModal #player").attr("src",url);
				$("#videoModal").modal();
				$("#videoModal").addClass("active");
				$("#videoModal").css("z-index", "100003");
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
	var mainDiv = 'defaultWishName';
	var errorDiv = "#addedMessage";
	validateSpcharWlName(e,wishlistname,mainDiv,errorDiv);
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
	var addedWlList_pdp = [];
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
			addedWlList_pdp.push(wishName);
		} else {
			index++;
		  
			wishListContent = wishListContent
					+ "<tr><td><input type='radio' name='wishlistradio' id='radio_"
					+ i
					+ "' style='display: none' onclick='selectWishlist("
					+ i + ")'><label for='radio_"
					+ i + "'>"+wishName+"</label></td></tr>";
		}
		$("#alreadyAddedWlName_pdp").val(JSON.stringify(addedWlList_pdp));
	}

	$("#wishlistTbodyId").html(wishListContent);

}

function selectWishlist(i) {
	$("#hidWishlist").val(i);
}

function addToWishlist(alreadyAddedWlName_pdp) {
	
	var loggedIn=$("#loggedIn").val();

	var productCodePost = $("#productCodePost").val();
	var productcodearray =[];
	productcodearray.push(productCodePost);
	var wishName = "";
	
	var ussidValue=$("#ussid").val();
  
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
	
	}
	
	
	
	var requiredUrl = ACC.config.encodedContextPath + "/p"
			+ "-addToWishListInPDP";
    var sizeSelected=true;
    
    if(!$('#variant li').hasClass('selected')) {
    	sizeSelected=false;
    }
    if( $("#variant,#sizevariant option:selected").val()=="#"){
    	sizeSelected=false;
    }
	var dataString = 'wish=' + wishName + '&product=' + productCodePost
			+ '&ussid=' + ussidValue+'&sizeSelected=' + sizeSelected;

//	if(loggedIn == 'false') {
	if(!headerLoggedinStatus) {
		$(".wishAddLogin").addClass("active");
		setTimeout(function(){
			$(".wishAddLogin").removeClass("active")
		},3000)
	}
	else {
		var isInWishlist = getLastModifiedWishlist(ussidValue);
		if(isInWishlist) {
			removeFromWishlistInPdp(wishName, productCodePost, ussidValue,isMSDEnabled,isApparelExist,rootCategoryMSD,salesHierarchyCategoryMSD,priceformad,"INR");
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
					$(".wishAddSucess").addClass("active");
					setTimeout(function(){
						$(".wishAddSucess").removeClass("active")
					},3000)
					$("#add_to_wishlist").attr("disabled",true);
					$('.add_to_cart_form .out_of_stock #add_to_wishlist').addClass("wishDisabled");
					$('.product-info .picZoomer-pic-wp .zoom a,.product-image-container.device a.wishlist-icon').addClass("added");
					/*setTimeout(function() {
						  $("#addedMessage").fadeOut().empty();
						}, 1500);*/
					//$('#addedMessage').delay(3000).fadeOut('slow'); // TISTI-225
					populateMyWishlistFlyOut(wishName);
					
					//For MSD					
					if(isMSDEnabled === 'true')
					{
					
					if(Boolean(isMSDEnabled) && Boolean(isApparelExist) && (rootCategoryMSD === 'Clothing'))
						{					
						ACC.track.trackAddToWishListForMAD(productCodeMSD, salesHierarchyCategoryMSD, priceformad,"INR");
						}	
					}
					//End MSD
					
					/*TPR-656*/
					utag.link({

						link_text: 'add_to_wishlist_pdp' , 
						event_type : 'add_to_wishlist_pdp', 
						product_sku_wishlist : productcodearray
					});
				/*TPR-656 Ends*/

					dtmAddToWishlist("pdp"); 
					
					//openPop(ussidValue);
				//	$('#myModal').modal('hide');
				//	
				}
				/*else{
					$(".wishAlreadyAdded").addClass("active");
					setTimeout(function(){
						$(".wishAlreadyAdded").removeClass("active")
					},3000)
				}*/
			},
		});
	
	//$('a.wishlist#wishlist').popover('hide');
	//$('input.wishlist#add_to_wishlist').popover('hide');
	
		setTimeout(function() {
			$('a.wishlist#wishlist').popover('hide');
			$('input.wishlist#add_to_wishlist').popover('hide');

			}, 0);
	}
	}
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
	//alert(sellersArray[index].stock);
	var roundedSpPrice = Math.round(sellersArray[index].spPrice * 100) / 100;
	
	////TPR-275 starts
	if (mrp == "") {
		 $("#mrpPriceId").hide();
		 $("#savingsOnProductId").hide();
		 $('#addToCartButton-wrong').attr("disable",true);
		 $('#addToCartButton-wrong').show();
		 $('#addToCartButton').hide();
		 $("#buyNowButton").attr("disabled",true);
	} else {
//		$("#mrpPriceId").show();
//	}
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
		var freebiePriceThresVal = $("#freebiePriceThreshId").val();
		if (sellersArray[index].mopPrice != null
				&& sellersArray[index].mopPrice != 0 && sellersArray[index].mopPrice > freebiePriceThresVal) {
			if (sellersArray[index].mopPrice == mrp) {
				$("#mrpPriceId").append(mrp);
				$("#mopPriceId").html("");
				$("#spPriceId").html("");
			} else {
				$("#mrpPriceId").append("<strike>" + mrp + "</strike>");
				$("#mopPriceId").append(sellersArray[index].mopPrice);
				$("#spPriceId").html("");
			}
		} else if(sellersArray[index].mopPrice != 0 && sellersArray[index].mopPrice <= freebiePriceThresVal){
			 $(".size").hide(); 	
			 $(".color-swatch").hide();
			 $(".reviews").hide(); 	
			 $('#addToCartButton-wrong').attr("disable",true);
			 $('#addToCartButton-wrong').show();
			 $('#addToCartButton').hide();
			 $("#otherSellerInfoId").hide();
			 $(".wish-share").hide();
			 $(".fullfilled-by").hide();
			 $("#pdpPincodeCheck").hide();
			 $("#pin").attr("disabled",true);
			 $("#pdpPincodeCheckDList").show();
			 $("#buyNowButton").attr("disabled",true);
			 $("#mopPriceId").show();
			 $("#mrpPriceId").hide();
			 $("#savingsOnProductId").hide();
			 //$("#dListedErrorMsg").show(); //Need to Change	
			// $("#freebieProductMsgId").show();

			 var prodCode=$("#productCodePost").val();
			 var ussId=  $("#ussid").val();
			 
			//update the message for Freebie product TPR-1754
			 var freebieproductMsg =populateFreebieMsg(prodCode);
			
			 if($.isEmptyObject(freebieproductMsg)){
				 
				 $("#freebieProductMsgId").show();			 
						}else{
						
						$("#freebieProductMsgId").html(freebieMsg);
						$("#freebieProductMsgId").show();
					}
			 
		}else {
			$("#mrpPriceId").append(mrp);
			$("#mopPriceId").html("");
			$("#spPriceId").html("");
		 }
	}
 }
//TPR-275 ends
	
//	if (sellersArray[index].spPrice != null && sellersArray[index].spPrice != 0) {
//		if (sellersArray[index].mopPrice == mrp) {
//			$("#mrpPriceId").append("<strike>" + mrp + "</strike>");
//			$("#mopPriceId").html("");
//			$("#spPriceId").append(roundedSpPrice);
//		} else {
//			$("#mrpPriceId").append("<strike>" + mrp + "</strike>");
//			$("#mopPriceId").append(
//					"<strike>" + sellersArray[index].mopPrice + "</strike>");
//			$("#spPriceId").append(roundedSpPrice);
//		}
//
//	} else {
//		if (sellersArray[index].mopPrice != null
//				&& sellersArray[index].mopPrice != 0) {
//			if (sellersArray[index].mopPrice == mrp) {
//				$("#mrpPriceId").append(mrp);
//				$("#mopPriceId").html("");
//				$("#spPriceId").html("");
//			} else {
//				$("#mrpPriceId").append("<strike>" + mrp + "</strike>");
//				$("#mopPriceId").append(sellersArray[index].mopPrice);
//				$("#spPriceId").html("");
//			}
//		} else {
//			$("#mrpPriceId").append(mrp);
//			$("#mopPriceId").html("");
//			$("#spPriceId").html("");
//		}
//	}
//	
//	if (mrp == "") {
//		$("#mrpPriceId").hide();
//	} else {
//		$("#mrpPriceId").show();
//	}

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
	//console.log("dataArray"+dataArray+"ussid"+ussid);
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
	var skuIdForCNC = [];
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
				if (dataArray[i]['stockCount'] == 0) {
					//console.log("ussid"+dataArray[i]['ussid']);
					ussidListWithNoStock[++n] = "'" + dataArray[i]['ussid']
							+ "'";// setting all the skuIds without stock

				}
				var deliveryModes = dataArray[i]['validDeliveryModes'];
				for ( var j in deliveryModes) {
					var mode = deliveryModes[j]['type'];
					if (mode == 'HD') {
						skuIdsForHD[++indx] = "'" + dataArray[i]['ussid'] + "'";
					}
					if (mode == 'ED') {
						skuIdsForED[++indx] = "'" + dataArray[i]['ussid'] + "'";
					}
					if (mode == 'CNC') {
						skuIdForCNC[++indx] = "'" + dataArray[i]['ussid'] + "'";
					}
					var stockDataArray = {}
					stockDataArray.ussid = dataArray[i]['ussid'];
					stockDataArray.stock = dataArray[i]['stockCount'];
					stockDataArrayList[++stockIndx] = stockDataArray;
					$("#stockDataArray")
							.val(JSON.stringify(stockDataArrayList));
				}
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
	sessionStorage.setItem("notServicables",nonservicableussids);
	sessionStorage.setItem("skuIdsForED",skuIdsForED);
	sessionStorage.setItem("skuIdsForHD",skuIdsForHD);
	sessionStorage.setItem("skuIdForCNC",skuIdForCNC);
	sessionStorage.setItem("skuIdForCod",skuIdForCNC);
	sessionStorage.setItem("skuIdForCNC",skuIdForCNC);
	sessionStorage.setItem("skuIdsWithNoStock",ussidListWithNoStock);
	sessionStorage.setItem("pincodeChecked","true");
	sessionStorage.setItem("stockDataArrayList",stockDataArrayList);
	sessionStorage.setItem("pincodeChecked","true");
//	$("#sellersSkuListId").val(nonservicableussids);
//	$("#skuIdForED").val(skuIdsForED);
//	$("#skuIdForHD").val(skuIdsForHD);
//	$("#skuIdForCNC").val(skuIdForCNC);
//	$("#skuIdForCod").val(skuForCodList);
//	$("#skuIdsWithNoStock").val(ussidListWithNoStock);
//	$("#isPinCodeChecked").val("true");
//	$("#sellerListId").html(contentData);
	if (isproductPage == 'false') {
		$("#sellerTable").show();
		$("#other-sellers-id").show();
		if (count > 0) {
			fetchAllSellers();
		//	repopulateSellers(stockDataArrayList,nonservicableussids,skuIdsForED,skuIdsForHD,skuIdForCNC,skuForCodList,ussidListWithNoStock);
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

function pincodeServiceability(){
	        var regExp = /^([1-9])([0-9]){5}$/;
            //INC144314017
			$(".submit").data('clicked', true);
			pinCodeChecked = true;
			
			//$("#home").hide();
			//$("#homeli").hide();
			$("#homeli").css("opacity","0.5");
			$("#homeli").removeClass("selected");
			//$("#express").hide();
			//$("#expressli").hide();
			$("#expressli").css("opacity","0.5");
			$("#expressli").removeClass("selected");
			//$("#collect").hide();
			//$("#collectli").hide();
			$("#collectli").css("opacity","0.5");
			$("#collectli").removeClass("selected");
			//TPR-6654
			$("#CNCstores").empty();
			$("#codId").hide();
			$('#wrongPin,#unableprocessPin,#unsevisablePin,#emptyPin').hide();
			$('#addToCartButton-wrong').attr("disable", true);
			$('#addToCartButton-wrong').hide();
			$("#outOfStockId").hide();



			// $('#addToCartButton').show();
			var checkBuyBoxIdPresent = false;
			var buyboxSeller = $("#ussid").val();
			var pin = $("#pin").val();
			
			var requiredUrl = ACC.config.encodedContextPath + "/p"
					+ "-checkPincode";
			if (pin == "") {
				$('#unsevisablePin,#unableprocessPin,#wrongPin,#serviceablePin')
						.hide();
				$("#emptyPin").show();
				$("#pdpPinCodeAvailable").hide();
				
				$('#addToCartButton').show();
				$('#buyNowButton').attr("disabled",false);
				//TPR-6654
				$("#CNCstores").empty(); 
				//TPR-794
				//$("#pdpPinCodeAvailable").html("Enter your pincode to see your available delivery options.");
				//$("#pdpPinCodeAvailable").show();
				return false;
			} else if (!regExp.test(pin)) {
				$('#unsevisablePin,#unableprocessPin,#emptyPin').hide();
				$("#wrongPin").show();
				$("#pdpPinCodeAvailable").hide();
				$("#serviceablePin").hide();
			//	$("#pdpPinCodeAvailable").hide();

				$('#addToCartButton').show();
				$('#buyNowButton').attr("disabled",false);
				//TPR-6654
				$("#CNCstores").empty(); 
				//TPR-794
				//$("#pdpPinCodeAvailable").show();
				//$("#pdpPinCodeAvailable").html("Enter your pincode to see your available delivery options.");
				return false;
			}
			//TISPRDT-1606
			var productCode = $('#product').val();
			var dataString  = "pin=" + pin + "&productCode="+ productCode;
			
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
								//$("#home").hide();
								//$("#homeli").hide();
								$("#homeli").css("opacity","0.5");
								$("#homeli").removeClass("selected");
								//$("#express").hide();
								//$("#expressli").hide();
								$("#expressli").css("opacity","0.5");
								$("#expressli").removeClass("selected");
								//$("#collect").hide();
								//$("#collectli").hide();
								$("#collectli").css("opacity","0.5");
								$("#collectli").removeClass("selected");
								//TPR-6654
								$("#CNCstores").empty();
								$('#wrongPin,#unableprocessPin,#emptyPin,#serviceablePin').hide();


								$('#addToCartFormTitle').hide();


								$('#addToCartButton-wrong').show();
								$('#addToCartButton').hide();
								$('#unsevisablePin').show();
								$("#pdpPinCodeAvailable").hide();


								$('#buyNowButton').attr("disabled",true); 
								//TPR-794
								$("#pdpPinCodeAvailable").html("Available delivery options for the pincode " +pin+ " are");

								
								/*TPR-642 & 640*/
								utag.link({
									link_text: 'pdp_pincode_check_failure' , 
									event_type : 'pdp_pincode_check' , 
									pdp_pin_sku : productCode, 
									pdp_pin_status : 'not_servicable', 
									pdp_pin_value : pin, 
									pdp_pincode_non_serviceable : pin,
									pdp_pin_delivery : 'error'
									
								});
							/*TPR-642 & 640 ends*/
								return false;
							}
							// check if oms service is down
							else if (data[0]['isServicable'] == 'NA') {
								$("#home").show();
								$("#homeli").show();
								$("#homeli").addClass("selected");
							    $("#homeli").css("opacity","1");
								$("#express").show();
								$("#expressli").show();
								$("#expressli").addClass("selected");
							    $("#expressli").css("opacity","1");
								$("#collect").show();
								$("#collectli").show();
								$("#collectli").addClass("selected");
							    $("#collectli").css("opacity","1");
								$("#codId").show();
								//TPR-794

								$("#pdpPinCodeAvailable").html("Available delivery options for the pincode " +pin+ " are");
								
								/*TPR-642 & 640*/
								utag.link({
									link_text: 'pdp_pincode_check_failure' , 
									event_type : 'pdp_pincode_check' , 
									pdp_pin_sku : productCode, 
									pdp_pin_status : 'not_servicable', 
									pdp_pin_value : pin, 
									pdp_pincode_non_serviceable : pin,
									pdp_pin_delivery : 'error'
								});
							/*TPR-642 & 640 ends*/
								return false;
								
							} else {
								// TPR-1375
								//populating  buybox details agian after checking pincode response
								repopulateBuyBoxDetails(data,buyBoxList);
								var buyboxSeller = $("#ussid").val();
								//alert("buyboxseller"+buyboxSeller);
								// refreshing seller list after
								refreshSellers(data, buyboxSeller);
								deliverModeTealium = new Array();
								for ( var i in data) {
									var pincodedata = data[i];
									ussid = pincodedata['ussid'];
									if (ussid == buyboxSeller) {
										if (pincodedata['isServicable'] == 'Y') {
											
											 $('#serviceablePin').show();  //TISPRM-20::PDP show pincode serviceability msg  
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
												$("#addToCartButton").hide();
												$("#outOfStockId").show();
												$("#buyNowButton").hide();
												$("#stock").val(0);
											} else {
												$("#addToCartButton").show();
												$('#buyNowButton').attr("disabled",false);
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


												else if (deliveryModeName == 'CNC') {


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
												$("#homeli").show();
												$("#homeli").addClass("selected");
											    $("#homeli").css("opacity","1");

												deliverModeTealium.push("home");
											} else {
												//$("#home").hide();
												//$("#homeli").hide();
												$("#homeli").css("opacity","0.5");
												$("#homeli").removeClass("selected");
											}

											if (exp == true) {
												$("#express").show();
												$("#expressli").show();
												$("#expressli").addClass("selected");
											    $("#expressli").css("opacity","1");
												deliverModeTealium.push("express");
											} else {
												//$("#express").hide();
												//$("#expressli").hide();
												$("#expressli").css("opacity","0.5");
												$("#expressli").removeClass("selected");

											}if (click == true) {
												$("#collect").show();
												$("#collectli").show();
												$("#collectli").addClass("selected");
											    $("#collectli").css("opacity","1");



												deliverModeTealium.push("clickandcollect");
												//TPR-6654
												var requiredUrl = ACC.config.encodedContextPath + "/p-allStores/"+pin;
												$.ajax({
													url : requiredUrl,
													type : "GET",
													success : function(response) {
														$("#CNCstores").html(response);
													}
												});		
											} else {

												//$("#collect").hide();
												//$("#collectli").hide();
												$("#collectli").css("opacity","0.5");
												$("#collectli").removeClass("selected");
												//TPR-6654
												$("#CNCstores").empty();
											}
											// }
											
											/*TPR- 642 & 640*/
												utag.link({
													link_text: 'pdp_pincode_check_success' , 
													event_type : 'pdp_pincode_check' , 
													pdp_pin_sku : productCode, 
													pdp_pin_status : 'servicable', 
													pdp_pin_value : pin, 
													pdp_pincode_serviceable :pin,
													pdp_pin_delivery : deliverModeTealium.join("_")
												});
											/*TPR-642 & 640 ends*/
												//TPR-6029 |DTM IMPLEMENTATION
												 dtmPdpPincode("success",productCode,pin);

										} else {
											//$("#home").hide();
											//$("#homeli").hide();
											$("#homeli").css("opacity","0.5");
											$("#homeli").removeClass("selected");
											$("#click").hide();
											//$("#expressli").hide();
											$("#expressli").css("opacity","0.5");
											$("#expressli").removeClass("selected");
											//$("#express").hide();
											//$("#collectli").hide();
											$("#collectli").css("opacity","0.5");
											$("#collectli").removeClass("selected");
											//TPR-6654
											$("#CNCstores").empty(); 
											$('#wrongPin,#unableprocessPin,#emptyPin,#serviceablePin')
													.hide();
											$('#addToCartFormTitle')
													.hide();
											if ($("#stock").val() > 0) {
												$('#addToCartButton-wrong').show();
												$('#buyNowButton').attr("disabled",true);
											} else {
												$("#outOfStockId").show();
												$("#buyNowButton").hide();
											}
											$('#addToCartButton').hide();
											$('#unsevisablePin').show();
											/*TPR-642 & 640 */
											utag.link({
												link_text: 'pdp_pincode_check_failure' , 
												event_type : 'pdp_pincode_check' , 
												pdp_pin_sku : productCode, 
												pdp_pin_status : 'not_servicable', 
												pdp_pin_value : pin, 
												pdp_pincode_non_serviceable : pin,
												pdp_pin_delivery : 'error'
											});
										/*TPR-642 & 640 ends*/
											//tpr-6029| DTM
											 dtmPdpPincode("failure",productCode,pin);
										}
									}

								}
								if (!checkBuyBoxIdPresent) {
									//$("#home").hide();
									//$("#homeli").hide();
									$("#homeli").css("opacity","0.5");
									$("#homeli").removeClass("selected");
									$("#click").hide();
									//$("#express").hide();
									//$("#expressli").hide();
									$("#expressli").css("opacity","0.5");
									$("#expressli").removeClass("selected");
									$(
											'#wrongPin,#unableprocessPin,#emptyPin')
											.hide();
									$('#addToCartFormTitle').hide();
									if ($("#stock").val() > 0) {
										$('#addToCartButton-wrong').show();
									} else {
										$("#outOfStockId").show();
										$("#buyNowButton").hide();
									}
									// $('#addToCartButton-wrong').show();
									$('#addToCartButton').hide();
									$('#unsevisablePin').show();
									$('#pdpPinCodeAvailable').hide();
								}
								
							}
							$("#pinCodeChecked")
									.val(pinCodeChecked);
							//TPR-794
							$("#pdpPinCodeAvailable").html("Available delivery options for the pincode " +pin+ " are");
						},
						error : function(xhr, status, error) {
							$('#wrongPin,#unsevisablePin,#emptyPin')
									.hide();
							$('#unableprocessPin').show();
							
							//TPR-794
							$("#pdpPinCodeAvailable").html("Available delivery options for the pincode " +pin+ " are");
							var error =$('#unableprocessPin').val();
							if(typeof utag !="undefined"){
							utag.link({error_type: error  });
                            }
							//TPR-6369 |Error tracking dtm
			 				dtmErrorTracking("Pin Code Servicability Error",error);
						}
					});

			//TPR-900
			$('#pin').blur();
			
			if ( $('#pin').val() == "") {
				//$("#pdpPincodeCheck").text("Check")				/*UF-42*/
				document.getElementById("pdpPincodeCheck").className = "Check";//UF-71
			} else {
			
				//$("#pdpPincodeCheck").text("Change Pincode")
				$("#pdpPincodeCheck").text("Check"); //UF-71
			}
			//TPR-900
}

var pinCodeChecked = false;
$(function() {
	var regExp = /^([1-9])([0-9]){5}$/;
	$("#codId").hide();
	$(".submit").click(function(){
		if($("#pdpPincodeCheck").hasClass("Check"))//UF-71
			
		{
			pincodeServiceability();
		}
		else
		{
			 $('#pin').focus();
			 $('#emptyPin').hide();


		}
	});

});

function isOOS(){
	var skuOOS = false;
	var totalOptions = $("#variant li").length;
	//totalOptions = totalOptions -1; // UI got changed from select option to li strike off 
	var disabledOption = $("#variant li.strike").length;
	
	//if(availibility!=undefined && availibility.length > 0){


	// availibility.length  was coming undefined even if availability was NOT Undefined
	if(availibility!=undefined && typeof availibility === 'object')/* Change for TISSQAUAT-687 :: IE throws error*/ 
		/*Object.keys(availibility).length > 0)*/



	{
		if(Object.keys(availibility).length > 0){

		$.each(availibility,function(k,v){
			if(window.location.pathname.endsWith(k.toLowerCase()) && v == 0){
				skuOOS = true;
			}
		});
		}
	}
	
	if(totalOptions == disabledOption && totalOptions!=0){
		return true;
	}else if(skuOOS){
		return true;
	} else{
		return false;
	}
}

/**
 * This method is used to display delivery modes against a sku id
 */
var availibility = null;
var buyBoxList=[];
$( document ).ready(function() {
//function fetchPrice() {
	//$("#outOfStockPinCodeMsg").hide();
	var categoryType = $("#categoryType").val();
	var selectedSize = "";
	if ($("#variant,#sizevariant option:selected").val() != "#") {
		selectedSize = true;
	}
	
	var isproductPage = $("#isproductPage").val();
	$("#addToCartButton").show();
	$("#outOfStockId").hide();
	var productCode = $("#product").val();
	var variantCodes = $("#product_allVariantsListingId").val();
	var variantCodesJson = "";
	var msiteBuyBoxSeller = $("#msiteBuyBoxSellerId").val(); //CKD:TPR-250
	if(typeof(variantCodes)!= 'undefined' && variantCodes!= ""){
		variantCodes = variantCodes.split(",");
		variantCodesJson = JSON.stringify(variantCodes);
	}
	//changes done to restrict buybox AJAX call from every page.
	if(typeof productCode === 'undefined' || $('#pageType').val()=='cart')
		{
		return false;
		}
	
	var requiredUrl = ACC.config.encodedContextPath + "/p-" + productCode
			+ "/buybox";
	//var dataString = 'productCode=' + productCode;

	/*var data =*/ getBuyBoxDataAjax(productCode,variantCodesJson);//Moving buybox call on load in a method so that it can be reused.UF-60



//}
	$(".size-guide").click(function(){
		if(null!= availibility){
			setTimeout(function(){
			$.each(availibility,function(key,value){
				$(".variant-select-sizeGuidePopUp li span").each(function(){
					if(typeof($(this).attr("data-producturl"))!= 'undefined' && $(this).attr("data-producturl").indexOf(key)!= -1 && value == 0){
						
						$(this).attr("disabled","disabled");
						$(this).css({
							"color": "gray"
					});
						$(this).removeAttr("data-producturl");

						$(this).parent().addClass('strike');
						}
				});
			});	
			},2000);
			
			setTimeout(function(){
				if(isOOS()){
					$("#outOfStockText").html("<font color='#ff1c47'>" + $('#outOfStockText').text() + "</font>");
					$("#addToCartSizeGuideTitleoutOfStockId").show();
					$("#addToCartSizeGuide #addToCartButton").hide();
				}
			},3000);
		}
	});
	
}); 


 

/**
 * This method is used to display delivery modes against a sku id
 */
function displayDeliveryDetails(sellerName) {

	var buyboxSeller = $("#ussid").val();
	var productCode = $("#product").val();
	var productCategoryType=$('#productCategoryType').val();
	var requiredUrl = ACC.config.encodedContextPath + "/p-" + productCode
			+ "/getRichAttributes";
	var dataString = 'buyboxid=' + buyboxSeller;
	$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		dataType : "json",
		success : function(data) {
			if (data != null) {
				//console.log("success");
				var pretext=$("#deliveryPretext").text();
				var posttext=$("#deliveryPosttext").text();
				var fulFillment = data['fulfillment'];
				var deliveryModes = data['deliveryModes'];
				
				 /*TISPRDT-878 start*/
				var fulFillmentP1 = data['fulfillmentType1'];
				/*TISPRDT-878 END*/


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
				/*if (null != fulFillment && fulFillment.toLowerCase() == 'tship') {
					$('#fulFilledByTship').show();
				} else {
					$('#fulFilledBySship').show();
					$('#fulFilledBySship').html(sellerName);

				}*/


				/*TISPRDT-878 Start*/
				if (null != fulFillment && fulFillment.toLowerCase() == 'both') {
					   if (null != fulFillmentP1 && fulFillmentP1.toLowerCase() == 'tship') {
					   $('#fulFilledBySship').hide();  //INC_12055
					   $('#fulFilledByTship').show();
					  }else {
					   $('#fulFilledByTship').hide();  //INC_12055
					   $('#fulFilledBySship').show();
					   $('#fulFilledBySship').html(sellerName);
					  }
					    } else if (null != fulFillment && fulFillment.toLowerCase() == 'tship') {
					   $('#fulFilledBySship').hide();  //INC_12055
					   $('#fulFilledByTship').show();
					 }else {
					   $('#fulFilledByTship').hide();  //INC_12055
					   $('#fulFilledBySship').show();
					   $('#fulFilledBySship').html(sellerName);
					  }
				/*TISPRDT-878 END*/

				//INC144314017 start
				 //TPR-6654
				if(typeof $('#pdpPincodeCheck').data('clicked') != "undefined" && !$('#pdpPincodeCheck').data('clicked')) {
					var start_hd=parseInt($("#homeStartId").val())+leadTime;
					var end_hd=parseInt($("#homeEndId").val())+leadTime;
				if (null != deliveryModes && deliveryModes.indexOf("HD") == -1) {
					//$("#home").hide();
					//$("#homeli").hide();
				
					$("#homeDate").html(pretext+start_hd+"-"+end_hd+posttext);
					$("#homeli").css("opacity","0.5");
					$("#homeli").removeClass("selected");
				} else {
					//var start=parseInt($("#homeStartId").val())+leadTime;
					//var end=parseInt($("#homeEndId").val())+leadTime;
					$("#homeDate").html(pretext+start_hd+"-"+end_hd+posttext);
					$("#home").show();
					$("#homeli").show();
					$("#homeli").addClass("selected");
				    $("#homeli").css("opacity","1");
					
				}
				var start_ed=$("#expressStartId").val();
				var end_ed=$("#expressEndId").val();
				if (null != deliveryModes && deliveryModes.indexOf("ED") == -1) {
					//$("#express").hide();
					//$("#expressli").hide();
					//var start=$("#expressStartId").val();
					//var end=$("#expressEndId").val();
					$("#expressDate").html(pretext+start_ed+"-"+end_ed+posttext);
					$("#expressli").css("opacity","0.5");
					$("#expressli").removeClass("selected");
				} else {		


					$("#expressDate").html(pretext+start_ed+"-"+end_ed+posttext);
					$("#express").show();
					$("#expressli").show();
					$("#expressli").addClass("selected");
				    $("#expressli").css("opacity","1");

					if (null != deliveryModes && deliveryModes.indexOf("HD") == -1) {
						//$("#home").hide();
						//$("#homeli").hide();
						$("#homeli").css("opacity","0.5");
						$("#homeli").removeClass("selected");
					} else {
						var start=parseInt($("#homeStartId").val())+leadTime;
						var end=parseInt($("#homeEndId").val())+leadTime;
						$("#homeDate").html(pretext+start+"-"+end+posttext);
						$("#home").show();
						$("#homeli").show();
						$("#homeli").addClass("selected");
						$("#homeli").css("opacity","1");
					}
					
					if (null != deliveryModes && deliveryModes.indexOf("ED") == -1) {
						$("#express").hide();
						$("#expressli").hide();
						//$("#expressli").css("opacity","0.5");
						//$("#expressli").removeClass("selected");
					} else {
						var start=$("#expressStartId").val();
						var end=$("#expressEndId").val();
						$("#expressDate").html(pretext+start+"-"+end+posttext);
						$("#express").show();
						$("#expressli").show();
						$("#expressli").addClass("selected");
						$("#expressli").css("opacity","1");
					}
					
					if (null != deliveryModes && deliveryModes.indexOf("CNC") == -1) {
						
						//$("#collect").hide();
						//$("#collectli").hide();
						$("#collectli").css("opacity","0.5");
						$("#collectli").removeClass("selected");
						//TPR-6654
						$("#CNCstores").empty();
					} else{
						var start=$("#clickStartId").val();
						var end=$("#clickEndId").val();
						$("#clickDate").html(pretext+start+"-"+end+posttext);
						$("#collect").show();
						$("#collectli").show();
						$("#collectli").css("opacity","1");
						$("#collectli").addClass("selected");
					}

				}
//				if (null != deliveryModes){
//					//		console.log(deliveryModes.indexOf("CNC") );
//				}

				
				var start_cnc=$("#clickStartId").val();
				var end_cnc=$("#clickEndId").val();
				if (null != deliveryModes && deliveryModes.indexOf("CNC") == -1) {
					
					//$("#collect").hide();
					//$("#collectli").hide();
					
					$("#clickDate").html(pretext+start_cnc+"-"+end_cnc+posttext);
					$("#collectli").css("opacity","0.5");
					$("#collectli").removeClass("selected");
					//TPR-6654
					$("#CNCstores").empty();
				} else {
				    //var start=$("#clickStartId").val();
		        	//var end=$("#clickEndId").val();
					$("#clickDate").html(pretext+start_cnc+"-"+end_cnc+posttext);
					$("#collect").show();
					$("#collectli").show();
					$("#collectli").addClass("selected");
				    $("#collectli").css("opacity","1");
				  }
				}


				//INC144314017 end

				// enable COD flag if COD enabled
				if (data['isCod'] == 'Y') {
					$("#codId").show();
					var codLink = "/p-" + productCode + "/viewSellers"; //TPR-6907
					$("#codLink").attr("href",codLink);
					$("#codEli").show();
				} else {
					$("#codId").hide();
					$("#codEli").hide(); //TPR-6907
				}
				if(null != data['returnWindow'])
				{
				//TISCR-414 - Chairmans demo feedback 10thMay CR starts
				var rWindowValue = data['returnWindow'];
				
				if(rWindowValue=="LINGERIE1")
					{
					$("#lingerieKnowMoreLi1").show();
					$("#defaultKnowMoreLi").hide();
					}
				else if(rWindowValue=="LINGERIE2")
					{
					$("#lingerieKnowMoreLi2").show();
					$("#defaultKnowMoreLi").hide();
					}
				//Added for UF-98_start
					else if(rWindowValue=="0")
					{ 
						var catType = $("#categoryType").val();
						if(catType != 'FineJewellery' && catType != 'FashionJewellery'){
							$("#defaultKnowMoreLi4").show();
						}
						//$("#defaultKnowMoreLi4").show();
						$("#defaultKnowMoreLi").hide();
						$("#defaultRetRefLi4").show();
						$("#defaultRetRefLi").hide();
						$("#defaultRetLi4").show();
						$("#defaultRetLi").hide();
				}
				else
					{
						$("#returnWindow").text(data['returnWindow']);
						$("#returnWindowRefRet").text(data['returnWindow']);
						$("#returnWindowRet").text(data['returnWindow']);
					}
				//TISCR-414 - Chairmans demo feedback 10thMay CR ends
				}
				else
					{
					$("#defaultKnowMoreLi4").show();
					$("#defaultKnowMoreLi").hide();
					$("#defaultRetRefLi4").show();
					$("#defaultRetRefLi").hide();
					$("#returnWindow").text("0");
					$("#returnWindowRefRet").text("0");
				}
			}
		}
	});
}

function dispPrice(mrp, mop, spPrice, savingsOnProduct) {
	//alert("mrp "+ mrp.formattedValue +"mop "+mop.formattedValue +"spPrice "+spPrice.formattedValue +"savingsOnProduct "+ savingsOnProduct.formattedValue);
	
/*Change for INC_11127*/
	
	$("#mrpPriceId").html("");
	$("#mopPriceId").html("");
	$("#spPriceId").html("");
	$("#savingsOnProductId").html("");
	if(typeof savingsOnProduct === 'undefined'){
	
		if(null!=mrp && null!=spPrice){
			savingPriceCal=(mrp.doubleValue-spPrice.doubleValue);
			savingPriceCalPer=(savingPriceCal/mrp.doubleValue)*100;
			savingsOnProduct=Math.round((savingPriceCalPer*100)/100);
		}
		else if(null!=mrp && null!=mop){
			savingPriceCal=(mrp.doubleValue-mop.doubleValue);
			savingPriceCalPer=(savingPriceCal/mrp.doubleValue)*100;
			savingsOnProduct=Math.round((savingPriceCalPer*100)/100);
		}
	}
	
	if(null!= mrp){
		//$("#mrpPriceId").html("");
		$("#mrpPriceId").append(mrp.formattedValueNoDecimal);
	}
	if(null!= mop){
		//$("#mopPriceId").html("");
		$("#mopPriceId").append(mop.formattedValueNoDecimal);
	}
	if(null!= spPrice){
		//$("#spPriceId").html("");
		$("#spPriceId").append(spPrice.formattedValueNoDecimal);
	} 
	////TISPRM-33 , TPR-140
	if(null!= savingsOnProduct && savingsOnProduct != 0){
		//$("#savingsOnProductId").html("");
		$("#savingsOnProductId").append("(-"+savingsOnProduct+" %)");
	} 
	

	if(null!= savingsOnProduct && savingsOnProduct != 0){
		$("#savingsOnProductId").show();
	}
	/*else
	{
		$("#savingsOnProductId").hide();
	}*/
	
	//TPR-275 starts
	if (mrp.value == "") {			
		 $("#mrpPriceId").hide();
		 $("#savingsOnProductId").hide();
		 $('#addToCartButton-wrong').attr("disable",true);
		 $('#addToCartButton-wrong').show();
		 $('#addToCartButton').hide();
		 $("#buyNowButton").attr("disabled",true);
	} else {	
		//$("#mrpPriceId").show();
		//}	
		//TISPRM-33
		if (null!=spPrice && spPrice != 0) {

			if (mop.value == mrp.value) {

				$('#mrpPriceId').css('text-decoration', 'line-through');
				$("#mrpPriceId").show();
				$("#spPriceId").show();
				$("#mopPriceId").hide();//UF-60
				$("#mrpPriceId").removeClass("sale").addClass("old");//UF-60
			} else {

				$('#mrpPriceId').css('text-decoration', 'line-through');
				$("#mrpPriceId").show();
				$("#spPriceId").show();
				$("#mopPriceId").hide();//UF-60
				$("#mrpPriceId").removeClass("sale").addClass("old");//UF-60
			}

		} else {			
			var freebiePriceThresVal = $("#freebiePriceThreshId").val();		
			if (null!=mop && mop.value != 0 && mop.value > freebiePriceThresVal) {
				if (mop.value == mrp.value) {
					$("#mrpPriceId").removeClass("old").addClass("sale");
					$("#mrpPriceId").show();
					$('#mrpPriceId').css('text-decoration', '');//UF-60
					$("#mopPriceId").hide();//UF-60
					$("#spPriceId").hide();//UF-60
				} else {
					$('#mrpPriceId').css('text-decoration', 'line-through');
					$("#mrpPriceId").show();
					$("#mopPriceId").show();
					$("#spPriceId").hide();//UF-60
					$("#mrpPriceId").removeClass("sale").addClass("old");//UF-60
				}
			} else if(mop.value != 0 && mop.value <= freebiePriceThresVal){
				 $(".size").hide(); 	
				 $(".color-swatch").hide();
				 $(".reviews").hide(); 	
				 $('#addToCartButton-wrong').attr("disable",true);
				 $('#addToCartButton-wrong').show();
				 $('#addToCartButton').hide();
				 $("#otherSellerInfoId").hide();
				 $(".wish-share").hide();
				 $(".fullfilled-by").hide();
				 $("#pdpPincodeCheck").hide();
				 $("#pin").attr("disabled",true);
				 $("#pdpPincodeCheckDList").show();
				 $("#buyNowButton").attr("disabled",true);
				 $("#mopPriceId").hide();
				 $("#mrpPriceId").hide();
				 $("#savingsOnProductId").hide();
				 $(".delivery-block").hide();
				 $(".seller").hide();
				 $(".star-review").hide();
				 $("#spPriceId").hide();//UF-60
				 //$("#dListedErrorMsg").show();	//Need to Change
				// $("#freebieProductMsgId").show();
				 var prodCode=$("#productCodePost").val();
			     var ussId=  $("#ussid").val();
				
			//	$("#ussid").val(data['sellerArticleSKU']);
				 
				//update the message for Freebie product TPR-1754
				 var freebieproductMsg =populateFreebieMsg(prodCode);			 

				 if($.isEmptyObject(freebieproductMsg)){	
					 
					 $("#freebieProductMsgId").show();			 
							}else{
							
							$("#freebieProductMsgId").html(freebieMsg);
							$("#freebieProductMsgId").show();
						}
				 
			}else{
				$("#mrpPriceId").show();
			 }
		}
	}
//TPR-275 ends
	
	//TISPRM-33
//	if (null!=spPrice && spPrice != 0) {
//
//		if (mop.value == mrp.value) {
//
//			$('#mrpPriceId').css('text-decoration', 'line-through');
//			$("#mrpPriceId").show();
//			$("#spPriceId").show();
//		} else {
//
//			$('#mrpPriceId').css('text-decoration', 'line-through');
//			$("#mrpPriceId").show();
//			$("#spPriceId").show();
//		}
//
//	} else {
//		if (null!=mop && mop.value != 0) {
//			if (mop.value == mrp.value) {
//				$("#mrpPriceId").removeClass("old").addClass("sale");
//				$("#mrpPriceId").show();
//			} else {
//				$('#mrpPriceId').css('text-decoration', 'line-through');
//				$("#mrpPriceId").show();
//				$("#mopPriceId").show();
//			}
//		} else {
//			$("#mrpPriceId").show();
//		}
//	}
	
//	if (mrp.value = "") {
//		$("#mrpPriceId").hide();
//	} else {
//		$("#mrpPriceId").show();
//	}

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
	else
	{  //UF-60
		$("#emiStickerId").hide();
	}
	// EMI change ends

}

function openPopForBankEMI() {
	var productVal = $("#prodPrice").val();
	var optionData = "<option value='select' disabled selected>Select</option>";
	$("#EMITermTable").hide();
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
				link_obj: this,
				link_text: 'emi_more_information' ,
				event_type : 'emi_more_information',
				product_id : productIdArray
			});

			dtmEmiTrack();
		},
		error : function(xhr, status, error) {

		}
	});
}


//TISPRO-533
function populateEMIDetailsForPDP(){
//$( "#bankNameForEMI" ).change(function() {
	var productVal = $("#prodPrice").val();
		var selectedBank = $('#bankNameForEMI :selected').text();
		var contentData = '';
		var productId=[];
		productId.push($('#product_id').val());
		if (selectedBank != "select") {
			var dataString = 'selectedEMIBank=' + selectedBank + '&productVal=' + productVal;
			$.ajax({
				url : ACC.config.encodedContextPath + "/p-getTerms",
				data : dataString,
				/*data : {
					'selectedEMIBank' : selectedBank,
					'productVal' : productVal
				},*/
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
						$("#EMITermTable").show();
					} else {
						$('#emiNoData').show();
					}
					
					/*TPR-641 starts  */
					emiBankSelectedTealium = "emi_option_" + selectedBank.replace(/ /g, "").replace(/[^a-z0-9\s]/gi, '').toLowerCase();
					/* TPR-4725  quick view emi*/
					emiBankSelected = selectedBank.toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/[',."]/g,"");
					
					if(typeof utag !="undefined"){
					utag.link({
						link_text: emiBankSelectedTealium , 
						event_type : 'emi_option_selected',
						emi_selected_bank : emiBankSelected,
						product_id :productId
					});
					}
					/*TPR-641 ends*/
					//track pdp and qw emi bank details
					dtmEmiBankTrack(emiBankSelected);
				},
				error : function(resp) {
					$('#emiSelectBank').show();
				}
			});
		} else {

		}
	//});
		}


//TISPRO-533




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

function CheckonReload()
{
	//CODE change as part of PT defect TISPT-180
	var user_id	     	= getCookie("mpl-user");
	var user_type		= getCookie("mpl-userType");
	//alert(user_id+"=="+user_type);
	if(user_type =='session' && user_id=='anonymous'){
		//Hiding the Comment Box if the User is not Logged In
		//TISUATPII-470 fix
		$('#commentsDiv .gig-comments-composebox').hide();
	}
	else{
		//Showing the Comment Box if the User is  Logged In
		$('#commentsDiv .gig-comments-composebox').show();
	}
	
//	var contentData = '';
//	 $.ajax({
//				url : ACC.config.encodedContextPath + "/p-checkUser",
//				data : {
//				},
//				type : "GET",
//				cache : false,
//				success : function(data) {
//					if(!data)							
//						{
//							//Hiding the Comment Box if the User is not Logged In
//							//TISUATPII-470 fix
//							$('#commentsDiv .gig-comments-composebox').hide();
//							//$('#commentsDiv .gig-comments-composebox').show();
//						}
//						else
//						{
//							//Showing the Comment Box if the User is  Logged In
//							$('#commentsDiv .gig-comments-composebox').show();
//							}
//				},
//				error : function(resp) {
//					console.log( "Error Occured" );
//				}
//			});
}



function getRating(key,productCode,category)
{
	var url = "https://comments.us1.gigya.com/comments.getStreamInfo?apiKey="+key+"&categoryID="+category+"&streamId="+productCode+"&includeRatingDetails=true&format=jsonp&callback=?";
	 
	$.getJSON(url, function(data){
	//	console.log(data);
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
	
	//CODE change as part of PT defect TISPT-180
	var user_id	     	= getCookie("mpl-user");
	var user_type		= getCookie("mpl-userType");
	if(user_type =='session' && user_id=='anonymous'){
		gotoLogin();
	}
	
	
//	var contentData = '';
// $.ajax({
//			url : ACC.config.encodedContextPath + "/p-checkUser",
//			data : {
//				
//			},
//			type : "GET",
//			cache : false,
//			success : function(data) {
//				if(!data)							
//					{
//						gotoLogin();
//						
//					}
//					else
//					{
//							
//						//$('.gig-comments-composebox').show();
//							
//						}
//					
//								
//			},
//			error : function(resp) {
//				//alert("Error Occured");
//				console.log( "Error Occured" );
//			}
//		});
	
}
function nextImage(elem)
{
	var grandParentId = $(elem).parent().parent().attr('id');
	if (grandParentId == 'pdp_gallery') {
		$("#pdp_gallery .imageListCarousel li img").each(function(){
			$(this).show();
		});
		var item_height = $("#pdp_gallery .imageListCarousel li").height();
		var item_count = $("#pdp_gallery .imageListCarousel:first li").length;
		var top = parseInt($("#pdp_gallery .imageListCarousel").css("top"));
		if(!(top % item_height)){
			if(parseInt($("#pdp_gallery .imageListCarousel").css("top")) > -item_height* (item_count-imagePageLimit)){
			$("#pdp_gallery .imageListCarousel").animate({top: "-="+item_height+"px"},"250");
			$("#pdp_gallery #previousImage,#pdp_gallery #zoomModal #previousImage").css("opacity","1");
			}
			else{
				$("#pdp_gallery #nextImage,#pdp_gallery #zoomModal #nextImage").css("opacity","0.5");
			}
		}
		if(parseInt($("#pdp_gallery .imageListCarousel").css("top")) == (item_count-imagePageLimit-1)*(-item_height)){

			$("#pdp_gallery #nextImage,#pdp_gallery #zoomModal #nextImage").css("opacity","0.5");
		}
	}
	else if (grandParentId == 'zoom_gallery') {
		$("#zoom_gallery .imageListCarousel li img").each(function() {
			//if ($(this).attr('data-type') == "image") {
				$(this).show();
			//}
		});
		var item_height = $("#zoom_gallery .imageListCarousel li").height();
		var item_count = $("#zoom_gallery .imageListCarousel li").length;
		var top = parseInt($("#zoom_gallery .imageListCarousel").css("top"));
		if(!(top % item_height)){
			if(parseInt($("#zoom_gallery .imageListCarousel").css("top")) > -item_height* (item_count-imagePageLimit)){
			$("#zoom_gallery .imageListCarousel").animate({top: "-="+item_height+"px"},"250");
			$("#zoom_gallery #previousImage,#zoom_gallery #zoomModal #previousImage").css("opacity","1");
			}
			else{
				$("#zoom_gallery #nextImage,#zoom_gallery #zoomModal #nextImage").css("opacity","0.5");
			}
		}
		if(parseInt($("#zoom_gallery .imageListCarousel").css("top")) == (item_count-imagePageLimit-1)*(-item_height)){
			$("#zoom_gallery #nextImage,#zoom_gallery #zoomModal #nextImage").css("opacity","0.5");
		}
	}

}

/*Display previous list of sellers after clicking previous link*/	
function previousImage(elem)
{
	var grandParentId = $(elem).parent().parent().attr('id');
	if (grandParentId == 'pdp_gallery') {
		var item_height = $("#pdp_gallery .imageListCarousel li").height();
		var top = parseInt($("#pdp_gallery .imageListCarousel").css("top"));
		if(!(top % item_height)){
			if(parseInt($("#pdp_gallery .imageListCarousel").css("top")) < 0 ){
				$("#pdp_gallery #nextImage,#pdp_gallery #zoomModal #nextImage").css("opacity","1");
				$("#pdp_gallery .imageListCarousel").animate({top: "+="+item_height+"px"},"250");
			}
			else{
				$("#pdp_gallery #previousImage,#pdp_gallery #zoomModal #previousImage").css("opacity","0.5");
			}
		}
		if(parseInt($("#pdp_gallery .imageListCarousel").css("top")) == -item_height){

			$("#pdp_gallery #previousImage,#pdp_gallery #zoomModal #previousImage").css("opacity","0.5");
		}
	}
	else if (grandParentId == 'zoom_gallery') {
		var item_height = $("#zoom_gallery .imageListCarousel li").height();
		var top = parseInt($("#zoom_gallery .imageListCarousel").css("top"));
		if(!(top % item_height)){
			if(parseInt($("#zoom_gallery .imageListCarousel").css("top")) < 0 ){
				$("#zoom_gallery #nextImage,#zoom_gallery #zoomModal #nextImage").css("opacity","1");
				$("#zoom_gallery .imageListCarousel").animate({top: "+="+item_height+"px"},"250");
			}
			else{
				$("#zoom_gallery #previousImage,#zoom_gallery #zoomModal #previousImage").css("opacity","0.5");
			}
		}
		if(parseInt($("#zoom_gallery .imageListCarousel").css("top")) == -item_height){
			$("#zoom_gallery #previousImage,#zoom_gallery #zoomModal #previousImage").css("opacity","0.5");
		}
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
			url : ACC.config.encodedContextPath + "/p-sendEmail",
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
function dispPriceForSizeGuide(mrp, mop, spPrice, savingsOnProduct) {
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

	////TISPRM-33
	if(null!= savingsOnProduct){
		$("#savingsOnProductIdSG").append("(-"+savingsOnProduct+" %)");
	} 

	if(null!= savingsOnProduct && savingsOnProduct != 0){
		$("#savingsOnProductIdSG").show();
	} 
	
	if (null!=spPrice && spPrice != 0) {

		if (mop == mrp) {
			$('#sizemrpPriceId').css('text-decoration', 'line-through');
			$("#sizemrpPriceId").show();
			$("#sizespPriceId").show();
		} else {
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
function isOOSSizeGuide(){
		
	var skuOOS = false;
	var totalOptions = $(".variant-select-sizeGuidePopUp li").length;
	//totalOptions = totalOptions -1; // UI got changed from select option to li strike off 
	var disabledOption = $(".variant-select-sizeGuidePopUp li.strike").length;
	
	//if(availibility!=undefined && availibility.length > 0){

	// availibility.length  was coming undefined even if availability was NOT Undefined
	if(availibility!=undefined && Object.keys(availibility).length > 0){
		$.each(availibility,function(k,v){
			if(window.location.pathname.endsWith(k.toLowerCase()) && v == 0){
				skuOOS = true;
			}
		});
	}
	
	if(totalOptions == disabledOption && totalOptions!=0){
		return true;
	}else if(skuOOS){
		return true;
	} else{
		return false;
	}
	
}



function isOOSQuicks(){
	var totalOptions = $("ul[label=sizes] li").length;
	totalOptions = totalOptions -1;
	var disabledOption = $("ul[label=sizes] li").find("[style]").length;
	if(totalOptions == disabledOption){
		return true;
	}else{
		return false;
	}
}
function buyboxDetailsForSizeGuide(productCode){
	var sellerID= $("#sellerSelId").val();
	var productCode = productCode;//$("#product").val();
	
//	console.log(sellerID +" "+productCode);
	var requiredUrl = ACC.config.encodedContextPath + "/p-buyboxDataForSizeGuide";
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
				var savingsOnProduct= data['savingsOnProduct'];
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
				dispPriceForSizeGuide(mrpPrice, mopPrice, specialPrice,savingsOnProduct);
				//if(availableStock==0  && $(".variant-select-sizeGuidePopUp option:selected").val()!="#"){	//changes for TISPRO-338
				if(null!= availibility){
				$.each(availibility,function(key,value){
				$(".variant-select-sizeGuidePopUp option").each(function(){
					if(typeof($(this).attr("data-producturl"))!= 'undefined' && $(this).attr("data-producturl").indexOf(key)!= -1 && value == 0){
						$(this).attr("disabled","disabled");
						}
				});
				});	
		
				}
				$(".variant-select-sizeGuidePopUp").trigger('click');
				if(isOOSSizeGuide()){	//changes for TPR-465	
				$("#outOfStockText").html("<font color='#ff1c47'>" + $('#outOfStockText').text() + "</font>");
					$("#addToCartSizeGuideTitleoutOfStockId").show();
					//$("#addToCartSizeGuide #addToCartButton").attr("style", "display:none");
					$(".btn-block.js-add-to-cart").hide();
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
			+ "-viewWishlistsInPDP";

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
		return false;
	}
	if(wishName==undefined||wishName==null){
		return false;
	}
	var requiredUrl = ACC.config.encodedContextPath + "/p"
			+ "-addToWishListInPDP";
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
		var isShowSize= $("#showSize").val();
		if(!$("#variant li ").hasClass("selected") && typeof($(".variantFormLabel").html())== 'undefined' && $("#ia_product_rootCategory_type").val()!='Electronics'&& $("#ia_product_rootCategory_type").val()!='Watches' && isShowSize=='true'){
			$("#addToCartFormTitle").html("<font color='#fff'>" + $('#selectSizeId').text() + "</font>");
			//alert('here');
			$("#addToCartFormTitle").show();
			//$('#addToCartFormTitle').remove();
			if ($(window).width() < 768) {
				setTimeout(function(){
					$('#addToCartFormTitle').fadeOut(2000);		
				},2000);
				//UF-390
				$('html, body').animate({
		              scrollTop: $('.product-info .product-image-container.device').height()
		        }, 1000);
			}
			
			/*setTimeout(function(){
				$('#addToCartFormTitle').remove();		
			},4000);*/
			//For pdp analytics changes
			utag.link({"error_type":"size_not_selected"});
	 	    return false;
	 }
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


		ACC.product.sendAddToBag("addToCartForm",true);
	});

	
	//AKAMAI Fix	
	$(document).ready(function(){		
		var url = window.location.href;		
		
		if (url.indexOf("selectedSize=true")>=0 && typeof productSizeVar !== "undefined")//>= 0  ==-1
		{
			/*$("#variant option:contains("+productSizeVar+")").attr('selected', true); 
			$("#sizevariant option:contains("+productSizeVar+")").attr('selected', true); */
			$("#variant option").each(function() {
				  if($(this).text().trim() == productSizeVar) {
				    $(this).attr('selected', 'selected');            
				  }                        
				});
			
		
		//Other Sellers
		$("#sizevariant option").each(function() {
			  if($(this).text().trim() == productSizeVar) {
			    $(this).attr('selected', 'selected');            
			  }                        
			});
		}
	});
	//fix for TISPT-332
	function openSizeGuidePopuponLoad()
	{
		 
				   var productcode= productCodeSG;
				  // alert('/////'+productcode)
				  
				   $('body').on('hidden.bs.modal', '#popUpModal', function () {
						  $(this).removeData('bs.modal');
						});
				   $("#popUpModal").modal("show");
				   buyboxDetailsForSizeGuide(productcode);	
	} 
/*TPR-630*/
	$(document).ready(function(){
		$(".pdp .Emi > p").on("click",function(e){
			e.stopPropagation();
			if(!$(this).hasClass("active") && $(window).width() > 1024){
				$(this).addClass("active");
				openPopForBankEMI();
			}
		});
		$(".pdp .Emi .modal-content .Close").on("click",function(e){
			e.stopPropagation();
			$(".Emi > p").removeClass("active mobile");
			$(".emi-overlay").remove();
			});
		$(".pdp .Emi > #EMImodal-content").on("click",function(e){
			e.stopPropagation();
			if($(window).width() > 1024){
				$(".pdp .Emi > p").addClass("active")
			}
		});
		/*$(".Emi > #EMImodal-content").on("click",".Emi .modal-content .Close",function(){
			$(".Emi > p").removeClass("active")
			});*/
		$(document).on("click", function(e){
			//console.log($(e.currentTarget).attr('class'))
			if(!$(e.currentTarget).parents(".Emi").hasClass("Emi_wrapper")) {
				$(".pdp .Emi > p").removeClass("active")
			} else {
				$(".pdp .Emi > p").addClass("active")
			}
		});
		
		$(".pdp .Emi > p").on("click",function(){
			if($(window).width() <= 1024){
				$(this).addClass("active mobile");
				$("body").append("<div class='emi-overlay' style='opacity:0.65; background:black; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
				openPopForBankEMI();
				$("body").addClass("no-scroll");
				
			}
		});
		$(document).on("click",".pdp .emi-overlay,.pdp .Emi .modal-content .Close",function(){
			$(".pdp .Emi > p").removeClass("active mobile");
			$(".emi-overlay").remove();
			$("body").removeClass("no-scroll");
		});
		
		$(window).resize(function(){
			if($(window).width() > 1024){
				$(".pdp .Emi > p").removeClass("active mobile");
				$(".pdp .emi-overlay").remove();
				/*$(".Emi > p").removeClass("active mobile");
				$(".emi-overlay").remove();*/
				$("body").removeClass("no-scroll");
			}
		})
		
		$(document).on("click",".product-detail .promo-block .pdp-promo-title, .pdp-promo-title-link",function(e){
			e.preventDefault();
			
			/*TPR-694*/
			utag.link({"link_obj": this, "link_text": 'product_offer_view_details', "event_type": 'product_offer_details'}); 
			
			/*TPR-694 ends */
			offerPopup($("#promotionDetailsId").html());
		});
		$(document).on('hide.bs.modal', function () {
		    $("#offerPopup").remove();
		}); 
		
		$("#pin").focus(function(){
			//$("#pdpPincodeCheck").text("Check")
			document.getElementById("pdpPincodeCheck").className = "Check";//UF-71
		});
/*		$("#pin").blur(function() {
			if ($(this).val() == "") {
				$("#pdpPincodeCheck").text("Check Availability")
			} else {
				$("#pdpPincodeCheck").text("Change Pincode")
			}

		});*/
		
		//price breakup in PDP of fine jewellery starts
		$("#show").click(function() {
			$("#showPriceBreakup").slideToggle("fast");
			$(".pricebreakup-link").toggleClass("expand-breakup");
		});
		//price breakup in PDP of fine jewellery starts
		
			/*UF-32*/
		 $("a.otherSellersFont").click(function(){
		 
		 $("#sellerForm").submit();
						 
			});
/*TPR-1083*/
		 
		 $('#pinExc').focus(function(){
				//$("#pdpPincodeCheck").text("Check")
				document.getElementById("pdpPincodeCheckExchnage").className = "Check";//UF-71
			});
	 $('#pinExc').val( $('#pdpPincodeCheck').val());
	 
	 $(".pdp .Exchange > p").on("click",function(e){
			e.stopPropagation();
			if(!$(this).hasClass("active") && $(window).width() > 1024)
			{
				$(this).addClass("active");
				//openPopForBankEMI();
			}
		});
		$(".pdp .Exchange .modal-content .Close").on("click",function(e){
			e.stopPropagation();
			$(".Exchange > p").removeClass("active mobile");
			$(".Exchange-overlay").remove();
			$("body").removeClass("no-scroll");
			});
		$(".pdp .Exchange > #Exchangemodal-content").on("click",function(e){
			e.stopPropagation();
			if($(window).width() > 1024){
				$(".pdp .Exchange > p").addClass("active")
			}
		});
		
		$(".pdp .Exchange > p").on("click",function(){
			if($(window).width() <= 1024){
				$(this).addClass("active mobile");
				$("body").append("<div class='Exchange-overlay' style='opacity:0.65; background:black; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
				//openPopForBankEMI();
				$("body").addClass("no-scroll");
				
			}
			//tpr-5193
			var productId=[];
			productId.push($('#product_id').val());
			if(typeof utag !="undefined"){
				utag.link({
					link_text: "exchange_clicked",
					event_type : "exchange_clicked",
					product_id : productId
				});
			   }
		});
		$(document).on("click",".Exchange-overlay,.pdp .Exchange .modal-content .Close",function(){
			$(".pdp .Exchange > p").removeClass("active mobile");
			$(".Exchange-overlay").remove();
			$("body").removeClass("no-scroll");
		});
		
		$(window).resize(function(){
			if($(window).width() > 1024){
				$(".pdp .Exchange > p").removeClass("active mobile");
				$(".pdp .Exchange-overlay").remove();
				/*$(".Emi > p").removeClass("active mobile");
				$(".emi-overlay").remove();*/
				$("body").removeClass("no-scroll");
			}
		});
});
	/*Wishlist In PDP changes*/
	function getLastModifiedWishlist(ussidValue) {
		var isInWishlist = false;
		var requiredUrl = ACC.config.encodedContextPath + "/p"
				+ "-getLastModifiedWishlistByUssid";
		var dataString = 'ussid=' + ussidValue;
		$.ajax({
			contentType : "application/json; charset=utf-8",
			url : requiredUrl,
			data : dataString,
			dataType : "json",
			async: false,
			success : function(data) {
			if (data == true) {
				isInWishlist = true;
				$('.product-info .picZoomer-pic-wp .zoom a,.product-image-container.device a.wishlist-icon').addClass("added");
				$("#add_to_wishlist").attr("disabled",true);
				$('.add_to_cart_form .out_of_stock #add_to_wishlist').addClass("wishDisabled");
			}
			else if(data == false)//TPR-5787
			{
				$('.product-info .picZoomer-pic-wp .zoom a,.product-image-container.device a.wishlist-icon').removeClass("added");
				$("#add_to_wishlist").attr("disabled",false);
				$('.add_to_cart_form .out_of_stock #add_to_wishlist').removeClass("wishDisabled");
			}
			},
			error : function(xhr, status, error) {
				$("#wishlistErrorId_pdp").html("Could not add the product in your wishlist");
			}
		});
		return isInWishlist;
	}
	
	
	function removeFromWishlistInPdp(wishlistName, productCode, ussid,isMSDEnabled,isApparelExist,rootCategoryMSD,catID,price,currency){	
		var requiredUrl = ACC.config.encodedContextPath+"/p" + "-removeFromWl";
		var dataString = 'wish=' + wishlistName + '&product=' + productCode
		+ '&ussid=' + ussid;
		
		$.ajax({
			url: requiredUrl,
			type: "GET",
			data: dataString,
			dataType : "json",
			cache: false,
			contentType : "application/json; charset=utf-8",
			success : function(data) {
				//FOR MSD
				if(Boolean(isMSDEnabled) && Boolean(isApparelExist) && (rootCategoryMSD == 'Clothing'))
				{
					try
					{
					track(['removeFromWishlist',productCode,catID,price,currency]);	
					}
					catch(err)
					{
						console.log('Error Adding trackers when remove from cart: '+err.message);					
					}
				}
				
				$(".wishRemoveSucess").addClass("active");
				setTimeout(function(){
					$(".wishRemoveSucess").removeClass("active")
				},3000)
				$("#add_to_wishlist").attr("disabled",false);
				$('.add_to_cart_form .out_of_stock #add_to_wishlist').removeClass("wishDisabled");
				$('.product-info .picZoomer-pic-wp .zoom a,.product-image-container.device a.wishlist-icon').removeClass("added");
				populateMyWishlistFlyOut(wishlistName);
				
				/*TPR-646 Changes*/
				utag.link({
					"link_obj" : this,
			        "link_text": 'remove_from_wishlist',
			        "event_type": 'remove_from_wishlist',
			        "product_sku_wishlist": "" + productCode
			    });
				
				dtmRemoveFromWishlist(pdp);
				//END MSD
//				window.location.href = ACC.config.encodedContextPath + "/my-account/wishList";
				//window.location.href = ACC.config.encodedContextPath + "/my-account/viewParticularWishlist?particularWishlist="+wishlistName;
			},
			error: function (xhr, status, error) {
				if(status == "parsererror"){
					window.location.href = ACC.config.encodedContextPath + "/login";
				} else {

					 alert("Some issues are there with Wishlist at this time. Please try later or contact out helpdesk");	
				}
	           
	        }
		});
	}
	
	
	/*TPR-1375*/
	 //TPR-1375 populating the buybox details after chceking servicable seller list
	function repopulateBuyBoxDetails(data,buyBoxList){
		var isproductPage = $("#isproductPage").val();
		var servicableUssids=[];
		var servicableList=[];
		var sellerList=[];
		var nonServicableList=[];
		for (var i = 0; i < data.length; i++) {
				if (data[i]['isServicable'] == 'Y') {
					servicableUssids.push(data[i]['ussid']);
				}
	      }
		var allOosFlag=true;
		for(var i in buyBoxList){
			if(buyBoxList[i]['available']>0){
				allOosFlag=false;
			}
			//alert(servicableUssids+"###"+buyBoxList[i]['sellerArticleSKU']+"$$$"+servicableUssids.indexOf(buyBoxList[i]['sellerArticleSKU']));
			if(servicableUssids!="" && servicableUssids.indexOf(buyBoxList[i]['sellerArticleSKU'])!=-1){
			servicableList.push(buyBoxList[i]);
			}
			if(servicableUssids!="" && servicableUssids.indexOf(buyBoxList[0]['sellerArticleSKU'])==-1){
				    nonServicableList.push(buyBoxList[i]);
			}
		}
		if(!$.isEmptyObject(servicableList)){
			sessionStorage.setItem('servicableList', JSON.stringify(servicableList[0]));
		}
		sessionStorage.setItem('isproductPage', isproductPage);
		sessionStorage.setItem('allOosFlag', allOosFlag);
		sessionStorage.setItem('otherSellerCount', servicableList.length-1);
		sessionStorage.setItem('pincodeChecked', 'Y');
		//TPR-1375 populating buybox details so that buybox seller should be servicable
		if(!$.isEmptyObject(servicableList)){
		populateBuyBoxData(JSON.parse(sessionStorage.getItem("servicableList")),servicableList.length-1,isproductPage,allOosFlag);
		}
	}
	
	/*Offer popup*/
	function offerPopup(comp) {
/*		$("body").append('<div class="modal fade" id="offerPopup"><div class="content offer-content" style="padding: 40px;max-width: 650px;">'+comp+'<button class="close" data-dismiss="modal"></button></div><div class="overlay" data-dismiss="modal"></div></div>');
*/		$("body").append('<div class="modal fade" id="offerPopup"><div class="content offer-content" style="padding: 40px;min-width: 45%;">'+comp+'<button class="close" data-dismiss="modal" style="border:0px !important;margin: 0px !important;"></button></div><div class="overlay" data-dismiss="modal"></div></div>');
		/*if($("#OfferWrap .Inner .Left").children().length == 0) {
			$("#OfferWrap .Inner .Left").remove();
		}*/
		/*INC144313502*/
		if($("#OfferWrap .Inner .Left").children().length == 0) {
			$("#OfferWrap .Inner .Left").remove();
		} else {	
			if($('.primary_promo_img').css('display') != 'none'){ 
			  $(".offercalloutdesc").css({'float': 'right'});
			  $(".offercalloutdesc").css({"padding": "30px 5px 30px 12px", "width": "58%"});
			}
		}
		$("#offerPopup").modal('show');
	} 
	function setDetailsForStock(){
		var productCode = $('#productCodeSizeGuid').val();//$("#productCodePost").val();
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
				success:function(data){
					var stockInfo = data['availibility'];
					availibility = stockInfo;
					$.each(stockInfo,function(key,value){
						$("#variant>li>span").each(function(){
							
							if($(this).data("productcode1").toString().toUpperCase().indexOf(key)!= -1){  
									
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
					},
					error:function(err){
						
					}
				
			});
		
		
		
		
	}

//TPR-978
function getProductContents() {
		
	var requiredUrl = ACC.config.encodedContextPath + "/p"
			+ "-fetchPageContents";
	var dataString = 'productCode=' + productCode;
	var renderHtml = "";
	$.ajax({
		url : requiredUrl,
		data : dataString,
		success : function(data) {
			if(data){ 
				$('#productContentDivId').html(data);
				//UF-403 starts
				if ($(".youtube-player").length) {
					var div, n,
	                v = document.getElementsByClassName("youtube-player");
		            for (n = 0; n < v.length; n++) {
		                div = document.createElement("div");
		                div.setAttribute("data-id", v[n].dataset.id);
		                div.innerHTML = labnolThumb(v[n].dataset.id); //labnolThumb function implementation in videocomponent.jsp
		                div.onclick = labnolIframe; //labnolIframe function implementation in videocomponent.jsp
		                v[n].appendChild(div);
		            }
				}
				//UF-403 ends
				 if (data.indexOf('class="Manufacturer Temp07"') > -1 ) {
					 $(".every-scene-carousel").owlCarousel({
							items:3,
							loop: true,
							nav:true,
							dots:false,
							navText:[],
							responsive : {
								// breakpoint from 0 up
								0 : {
									items:1,
								},	
								// breakpoint from 767 up
								768 : {
									items:2,
								},

								// breakpoint from 1122 up
								1123 : {
									items:3,
								}			
							}	
							/*navigation:true,
							rewindNav: false,
							navigationText :[],
							pagination:false,
							items:4,
							itemsDesktop : false, 
							itemsDesktopSmall : false, 
							itemsTablet: false, 
							itemsMobile : true
						itemsDesktop : [5000,4], 
						itemsDesktopSmall : [1400,4], 
						itemsTablet: [650,2], 
						itemsMobile : [480,2],*/
					});
				 }
				//TPR-4701 | utag event for A+ products
				var productId=[];
				productId.push($('#product_id').val());
				utag.link({
					"link_text": "a_plus_product",
					"event_type": "a_plus_product",
					"a_plus_product_id":productId
				});
				//TPR-6029 | dtm for A+ products
				if (typeof(_satellite) != "undefined") {
					_satellite.track('cpj_pdp_a_plus');
			    }
			}
				 
		},
		error : function(xhr, status, error) {
			
		}
	});
}


function lazyLoadProductContents(){
	if ($(window).scrollTop() + $(window).height() >= $('#productContentDivId').offset().top) {
		if(!$('#productContentDivId').attr('loaded')) {
		    //not in ajax.success due to multiple sroll events
		    $('#productContentDivId').attr('loaded', true);

		
		    //ajax goes here
		    //by theory, this code still may be called several times
		    if ($('#productContentDivId').children().length == 0) {
		    	getProductContents();
		    }
		}
	}
}

if($('#pageTemplateId').val() == 'ProductDetailsPageTemplate'){
	$(window).on('scroll load',function() {
		lazyLoadProductContents();
	});
}


//PDP Specifications arrow
$(document).ready(function(){
var width=0;
var windowWidth = $(".SpecWrap .Padd").innerWidth() - $(".SpecWrap .Padd").width();
if (windowWidth > 60){
$(".SpecWrap .Padd .tabs-block .nav > li").each(
		function() {
			width = width + $(this).width();
		});

}
else{
$(".SpecWrap .Padd .tabs-block .nav > li").each(
		function() {
			width = width + $(this).width() + 15;
		});
}
//console.log(width);
var winWidth = $(".SpecWrap .nav-wrapper").innerWidth();
//console.log(winWidth);
if (width <= winWidth){
	$(".SpecWrap .Padd .tabs-block .nav-wrapper > span").css("display","none");
	$(".SpecWrap .Padd .tabs-block .nav-wrapper").css("padding-top","0px");
}
if (width > winWidth){
	var l = 0;
	var value = 200;
	$(".SpecWrap .Padd .tabs-block .nav-wrapper > span").css("display","inline-block");
	$(".SpecWrap .Padd .tabs-block .nav-wrapper").css("padding-top","15px");
	$('.SpecWrap .Padd .tabs-block .nav').animate({'left':0});
	$(".SpecWrap .Padd .tabs-block .nav-wrapper > span").unbind().click(function() {
		l = l + value;
		if(!$('.SpecWrap .Padd .tabs-block .nav li:last-child').visible() && l <= width ){
            $('.SpecWrap .Padd .tabs-block .nav').animate({'left':-l});			
		}else{
			l = 0;
			$('.SpecWrap .Padd .tabs-block .nav').animate({'left':0});
		}
        });	
}






});
$(window).on("resize", function() {
	var width=0;
	var windowWidth = $(".SpecWrap .Padd").innerWidth() - $(".SpecWrap .Padd").width();
	if (windowWidth > 60){
	$(".SpecWrap .Padd .tabs-block .nav > li").each(
			function() {
				width = width + $(this).width();
			});






	}
	else{
	$(".SpecWrap .Padd .tabs-block .nav > li").each(
			function() {
				width = width + $(this).width() + 15;
			});
	}
	//console.log(width);
	var winWidth = $(".SpecWrap .nav-wrapper").innerWidth();
	//console.log(winWidth);
if (width <= winWidth){
	$(".SpecWrap .Padd .tabs-block .nav-wrapper > span").css("display","none");
	$(".SpecWrap .Padd .tabs-block .nav-wrapper").css("padding-top","0px");
}
if (width > winWidth){
	var l = 0;
	var value = 200;
	$(".SpecWrap .Padd .tabs-block .nav-wrapper > span").css("display","inline-block");
	$(".SpecWrap .Padd .tabs-block .nav-wrapper").css("padding-top","15px");
	$('.SpecWrap .Padd .tabs-block .nav').animate({'left':0});
	$(".SpecWrap .Padd .tabs-block .nav-wrapper > span").unbind().click(function() {
		l = l + value;
		if(!$('.SpecWrap .Padd .tabs-block .nav li:last-child').visible() && l <= width ){
            $('.SpecWrap .Padd .tabs-block .nav').animate({'left':-l});			
		}else{
			l = 0;
			$('.SpecWrap .Padd .tabs-block .nav').animate({'left':0});
		}
        });	
}
});


















	//update the message for Freebie product TPR-1754
	function  populateFreebieMsg(ussId){
		var requiredUrl = ACC.config.encodedContextPath + "/p-" + ussId
		                  + "/getFreebieMessage";		
		var dataString = 'ussId=' + ussId;	
		$.ajax({
			contentType : "application/json; charset=utf-8",
			url : requiredUrl,
			async: false,
			data : dataString,
			cache : false,
			dataType : "json",
			success : function(data){
				if (data != null) {			
				    var freebieMessageMap = data['offerMessageMap'];
				    if(!$.isEmptyObject(freebieMessageMap)){
				    	
				    	$.each( freebieMessageMap, function(key,value){		
				    		
						//	$.each(value, function(keyInternal,valueInternal){
								// if(keyInternal == 'freebieMsg'){
									 freebieMsg = value;		 
									 
								// }				 
							// });
				    	})
				    	
				    	}
			}
		}
	});
		 return freebieMsg;
	}

////Start of UF-60 changes
$(document).ready(function(){
	onSizeSelectPopulateDOM();
});
function onSizeSelectPopulateDOM()//First Method to be called in size select ajax call
{	//Attaching click event on size variant <li>
	$("ul#variant.variant-select li a").on('click',function(e){
		//console.log("================>called while size is selected================>");
		
		var currentSelectedElement=this;
		var productCode="";
		productCode=$(currentSelectedElement).attr("data-productCode");
		
		//console.log("================>data-productCode================>"+productCode);
		
		if(typeof(productCode)!='undefined' && productCode!="" && $('#pageType').val()=='product')
		{//console.log("================>data-productCode in here================>"+productCode);
			e.preventDefault();
			var href = $(this).attr('href');
			var params=getParamsFromUrl(href);
			
			
			var prvsProductCode = $("#product").val();
			
			var staticHost = $('#staticHost').val();
			//Below 2 lines for adding spinner
			$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
			$("body").append('<div class="loaderDiv" style="position: fixed; left: 45%;top:45%;z-index: 10000"><img src="'+staticHost+'/_ui/responsive/common/images/red_loader.gif" class="spinner"></div>'); //UF-263
			//$("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');
			
			if($('#promolist').val()!='')
			{	//If promotion exist for the existing product
				$("#promolist").val("");//Set it empty as we are not sure if the new selected product has promotion yet
			}
			
			//To get product code from URL
			//productCode=getProductCodeFromPdpUrl(href);
			//To get original URL
			//var a = $('<a>', { href:href } )[0];
			if(window.location.port!="")
			{
				var port= ":"+window.location.port;
			}
			else
			{
				var port= "";
			}
			var hostName = window.location.hostname;
			var baseUrl="//"+hostName+port;
			//var originalUrl=baseUrl+a.pathname+"?selectedSize=true";
			//TISPRDT-781 issue fix
			var originalUrl=baseUrl+href;
			$("#addToCartFormTitle").hide(); //Hide 'please select size' on selecting size
			
			
			//Ajax call happening here
			var xhrAjaxProductData=getProductDetailOnSizeChange(productCode);
			//On ajax call failure
			xhrAjaxProductData.fail(function(xhr, textStatus, errorThrown) {
				console.log("ERROR:"+textStatus + ': ' + errorThrown);
				window.location.href=originalUrl;
			});
			//On ajax call success
			xhrAjaxProductData.done(function(data) {
				try{
					//Populating productPromotionSection.tag
					$('#productPromotionSection').html(data);
					var jsonData= JSON.parse($('#sizeSelectAjaxData').text());
					var pin = $("#pin").val();
					$('#sizeSelectAjaxData').remove();
					if(typeof(jsonData['error'])=='undefined')
					{
						
						//UF-33 starts//   //TISSTRT-1587//
						//Update Page Title
						document.title = jsonData['mapConfigurableAttribute']['metaTitle'];
						//UF-33 ends//
						//If no server side error occured the below code executes
						var responseProductCode=jsonData['code'];
						var responseProductUrl=jsonData['url'];
						var responseProductArticleDescription=jsonData['articleDescription'];
						var responsePdpSellerIds=jsonData['pdpSellerIDs'];
						//var responseMsdRESTURL=jsonData['msdRESTURL'];
						if(typeof(jsonData['productSize'])!='undefined')
						{
							var responseProductSize=jsonData['productSize'];
						}
						else
						{
							var responseProductSize="";
						}
						if(typeof(jsonData['potentialPromotions'])!='undefined')
						{
							var responsePotentialPromotions=jsonData['potentialPromotions'];
						}
						else{
							var responsePotentialPromotions=[];
						}
						

						$('#selectedSize').val("true");
						
						$("input[name=productCodeMSD]").val(responseProductCode);
						$("#product").val(responseProductCode);
						$('#pdpSellerIDs').val('['+responsePdpSellerIds+']');// needed in differentiateSeller() method
						$('meta[itemprop="sku"]').attr('content', responseProductCode);
						$('a.size-guide').attr('href','/p-sizeGuide?productCode='+responseProductCode+'&sizeSelected=true');
						$('a.size-guide').data("productcode",responseProductCode);
						$('a.size-guide').data("sizeSelected",'true');
						//TPR-6654
						//$('#pdpPincodeCheck').data('clicked', false);
						$('#ia_product_code').val(responseProductCode);
						$("#dListedErrorMsg").hide();//TISSTRT-1469
						$(".reviews").show();
						$(".fullfilled-by").show();
						//CKD:TPR-250 :Start
						var msiteSeller = $("#msiteBuyBoxSellerId").val(); 
						var msiteSellerQueryString = '';
						if (!$.isEmptyObject(msiteSeller)){
							msiteSellerQueryString='?sellerId='+msiteSeller;
						}
						
						/*$("a#submit.otherSellersFont").attr("href","/p/"+responseProductCode+"/viewSellers");
						$("#sellerForm").attr("action","/p/"+responseProductCode+"/viewSellers");*/
						
						$("a#submit.otherSellersFont").attr("href","/p/"+responseProductCode+"/viewSellers"+msiteSellerQueryString);
						$("#sellerForm").attr("action","/p/"+responseProductCode+"/viewSellers"+msiteSellerQueryString);
						
						//CKD:TPR-250 :End
						//Deselect the previously selected li and highlight the current li
						$('ul#variant.variant-select li').each(function (index, value) { 
							if($(this).hasClass("selected"))
							{
								$(this).removeClass("selected");
							}
						});
						$(currentSelectedElement).parent("li").addClass("selected"); //adding selected class to the selected li
						
						productSizeVar=responseProductSize;//productSizeVar is an existing Global Variable used at multiple places
						
						$('#productCodePost').val(responseProductCode);
						$('#selectedSizeVariant').val(responseProductCode);
						
						//Populate tealium data part-1 / part-2 is populated through data obtained during in buybox call /part-3 in populatePromotionsInProductDetailsPanel  
						$('#product_sku').val(responseProductCode);
						$('#product_id').val(responseProductCode);
						
						//Remove highlighted pincode error/success messages
						$("#wrongPin,#unableprocessPin,#unsevisablePin,#emptyPin,#serviceablePin").hide();
						//TPR-6654
						if(typeof(jsonData['pincode'])!='undefined'){
						$("#pdpPinCodeAvailable").html("Available delivery options for the pincode " +pin+ " are");
						}
						else
						{
						$("#pdpPinCodeAvailable").html("Enter your pincode to see your available delivery options  ");
						}
						$("#pdpPinCodeAvailable").show();
						//Populating productStyleNotesTab.tag,productDescriptionTab.tag with articleDescription
						if(responseProductArticleDescription !="" && typeof($('.tab-details span:eq(0)').text())!='undefined')
						{
							$('.tab-details span:eq(0)').text(responseProductArticleDescription);
						}
						if(typeof($('.tab-details li.stylenote').text())!='undefined')
						{
							$('.tab-details li.stylenote').text(responseProductArticleDescription);
						}
						
						//Social Sharing
						$('#productUrl').text(responseProductUrl);				// social sharing product url
						$(".g-interactivepost").attr("data-contenturl",window.location.host+responseProductUrl);
						$(".g-interactivepost").attr("data-calltoactionurl",window.location.host+responseProductUrl);
						$('div#emailSend #pId').val(responseProductCode);// Needed in social share tag
						
							
						//Gigya data
						if(typeof($('#rating_review'))!='undefined') // To check if gigya is enabled
						{
							$('#rating_review').val(responseProductCode);
							$('input[name=gigya_product_code]').val(responseProductCode);
						}
						
						
						//Promotions handled here
						//populateProductPromotionsData(potentialPromotions)
						populatePromotionsInProductDetailsPanel(responsePotentialPromotions);
						
						//Used to re-write the browser URL so that onReload the user stays on the same page
					    changeBrowserUrl(originalUrl);
						
						
						//Calling to check if the product already exists in the wishlist,If so fill/unfill heart icon
						//getLastModifiedWishlistForPLP(responseProductCode);//CALL FROM BUYBOX..TPR-5787
						
						//Prepare data for buybox call
						var variantCodes = $("#product_allVariantsListingId").val();
						var variantCodesJson = "";
						var currentProductCode=responseProductCode;
						
						//Fix for TISSIT-2200
//						if(typeof(variantCodes)!= 'undefined' && variantCodes!= ""){
//							variantCodes = variantCodes.split(",");
//						}
						if(typeof(prvsProductCode)!='undefined' && currentProductCode!="")
						{
							//Fix for TISSIT-2200
							if(typeof(variantCodes)!= 'undefined' && variantCodes!= ""){
								variantCodes = variantCodes.split(",");
								
								variantCodes.push(prvsProductCode);
								var index = variantCodes.indexOf(currentProductCode);
								variantCodes.splice(index, 1);
								variantCodesJson = JSON.stringify(variantCodes);
								$("#product_allVariantsListingId").val(variantCodes.join());
							}	
							//Calling buybox
							var xhrBuyBox=getBuyBoxDataAjax(currentProductCode,variantCodesJson);
							//On buybox failure
							xhrBuyBox.fail(function(xhr, textStatus, errorThrown) {
								console.log("ERROR:"+textStatus + ': ' + errorThrown);
								window.location.href=originalUrl;
							});
							//On buybox success
							xhrBuyBox.done(function(data){
								//START:Populate tealium data part-2, If size is selected UF-60		
								var spPrice = typeof(data['specialPrice'])!='undefined'?data['specialPrice']:null;
								var mrpPrice =typeof(data['mrp'])!='undefined'?data['mrp']:null;
								var mop = typeof(data['price'])!='undefined'?data['price']:null;
								if(mrpPrice!=null)
								{
									$('#product_unit_price').val(mrpPrice.doubleValue.toFixed(2));
								}
								if (spPrice != null)
								{
									$('#product_list_price').val(spPrice.doubleValue.toFixed(2));
								}
								else if (null != mop)
								{
									$('#product_list_price').val(mop.doubleValue.toFixed(2));
								}
								if(typeof(data['availablestock'])!='undefined')
								{
									$('#product_stock_count').val(parseInt(data['availablestock']));
								}
								var savingPriceCal;
								var savingPriceCalPer;
								var savingsOnProduct;
								if(null!=mrpPrice && null!=spPrice){
									savingPriceCal=(mrpPrice.doubleValue-spPrice.doubleValue);
									savingPriceCalPer=(savingPriceCal/mrpPrice.doubleValue)*100;
									savingsOnProduct=Math.round((savingPriceCalPer*100)/100);
								}
								else if(null!=mrpPrice && null!=mop){
									savingPriceCal=(mrpPrice.doubleValue-mop.doubleValue);
									savingPriceCalPer=(savingPriceCal/mrpPrice.doubleValue)*100;
									savingsOnProduct=Math.round((savingPriceCalPer*100)/100);
								}
								$('#product_discount').val(Math.round((savingPriceCal*100)/100));
								$('#product_discount_percentage').val(savingsOnProduct);
								//END:Populate tealium data part-2, If size is selected UF-60
							});
							//On buybox complete
							xhrBuyBox.always(function(){
								//showing/hiding buttons on page change
								//$('#pin').val("");  //TPR-6654
								//$("#addToCartButton").show(); // commented for TPR-250 :was overriding

								$('#addToCartButton-wrong').hide();
								$('#buyNowButton').attr("disabled",false);
								//$("#buyNowButton").show();  // commented for TPR-250 :was overriding

								//Hiding fullfilled by as they will be populated during richAttribute population
								$('#fulFilledByTship').hide();
								$('#fulFilledBySship').hide();
								//hiding badges on page change
								$('.online-exclusive').hide();
								$('#newProduct').hide();
								$(".picZoomer-pic-wp #codId").css("top", "");
								
								
								//Removing spinner as tealium/Classattributes/giggya call can continue in backend
									//$("#no-click,.spinner").remove();

								$("#no-click,.loaderDiv").remove(); //UF-263
								
								//Start classification attributes
								xhrClassAttrib=getClassificationAttributes(responseProductCode);
								xhrClassAttrib.done(function(data){
									jsonData=$.parseJSON(data);
									if(typeof(jsonData['error'])=='undefined')
									{
										//Used to populate tabs in productPageTabs.tag using data obtained by an ajax call
										populateProductPageTabs(jsonData);
									}
									else
									{
										console.log("ERROR:Server side error occured while fetching classification attributes.")
									}
								});
								xhrClassAttrib.fail(function(xhr, textStatus, errorThrown) {
									console.log("ERROR:"+textStatus + ': ' + errorThrown);
									window.location.href=originalUrl;
								});
								//End classification attributes
								
								//Calling Gigya
								if(typeof($('input[name=isGigyaEnabled]').val())!='undefined' && $('input[name=isGigyaEnabled]').val()=="Y")
								{
									//Gigya call after 200 milisec
									setTimeout(function(){getPdpRatingOnSizeSelect($('input[name=gigya_api_key]').val(),$('input[name=gigya_product_code]').val(),$('input[name=gigya_product_root_category]').val());},200);
									$('#ReviewSecion .commentcontent').attr('loaded', false);
									$('#ReviewSecion ul#commentsDiv').empty();
									attachOnScrollEventForPdpOnSizeSelect();
								}
								
								//Calling Tealium after 500 milisec //TPR-5251
								setTimeout(function(){
	 								//tealiumCallOnPageLoad();
	 								populateSizeSelectUtagObject();
	 							}, 500)
								
								//Calling MSD if MSD is enabled
//								if(typeof($('input[name=isMSDEnabled]').val())!='undefined' && $('input[name=isMSDEnabled]').val()=="true")
//								{
//									//Calling MSD on size select after 3000 milisec
//									setTimeout(callMSDOnLoad(),3000); 
//								}
							});//End of buybox and tealium call
						}

					}//End of server side error check if
					else
					{	
						//Removing spinner as tealium/Classattributes/giggya call can continue in backend
						//$("#no-click,.spinner").remove();
						$("#no-click,.loaderDiv").remove(); //UF-263

						console.log("ERROR:Server side exception thrown while changing size using ajax:"+jsonData['error']);
						console.log("ERROR:Resorting to page load as fall back");
						window.location.href=originalUrl;
					}//End of server side error check else
				}//End of try
				catch(e)
				{
					console.log("ERROR:Client side error occured while fetching product data for size select using ajax:"+e);
					console.log("ERROR:Resorting to page load as fall back.");
					window.location.href=originalUrl;
				}//End of catch
			});//End of sizeChange ajax call done method
			
		}
	});
	//Removing spinner incase it was not removed earlier due to some exception
	//$("#no-click,.spinner").remove();
	$("#no-click,.loaderDiv").remove(); //UF-263

}//End onSizeSelect function
    
//Ajax call to get classification attributes
function getClassificationAttributes(productCode)
{
	var requiredUrl=ACC.config.encodedContextPath + "/p-productClassAttribs?productCode=" + productCode;
	return $.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		cache : true,//it will not append timestamp and will help to make it AKAMAI cached
		dataType : "json",
	});
}
//Moving buybox ajax call into a function so that it can be reused UF-60
function getBuyBoxDataAjax(productCode,variantCodesJson)
{
	var isproductPage = $("#isproductPage").val();
	var msiteBuyBoxSeller = $("#msiteBuyBoxSellerId").val(); //CKD:TPR-250
	var requiredUrl = ACC.config.encodedContextPath + "/p-" + productCode+ "/buybox";
	$("#allVariantOutOfStock").hide();
	$("#outOfStockId").hide();
	$("#addToCartButton").show();
	$("#addToCartButton").show();
	$('#buyNowButton').show();

	return $.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : {productCode:productCode,variantCode:variantCodesJson
			,sellerId:msiteBuyBoxSeller //CKD:TPR-250	
		},

		cache : false,//added to resolve browser specific the OOS issue
		dataType : "json",
		success : function(data) {
			//TPR-1375
			for(var i in data['buyboxList'] ){
				buyBoxList.push(data['buyboxList'][i]);
			}
		//	alert(buyBoxList);
			if($("#isProductPage").val()=='true'){
			sessionStorage.setItem('servicableList',"");
			}
			//TISPRM-56
			var stockInfo = data['availibility'];
		


			availibility = stockInfo;
			$.each(stockInfo,function(key,value){

				$("#variant li a").each(function(){
					if(typeof($(this).attr("href"))!= 'undefined' && $(this).attr("href").toUpperCase().indexOf(key)!= -1 && value == 0){ 

					$(this).removeAttr("href");
					$(this).attr("title","out of stock");		/*UF-30*/
					$(this).parent().addClass('strike');
				//$(this).parent().css("border-color","gray");
				$("#outOfStockId").hide();
				
					}
				});
				
				
				/*		$(".capacity-box a").each(function(){
							if($(this).attr("href").indexOf(key) != -1 && value == 0){
								$(this).removeAttr("href");
								$(this).css({
										"color": "gray",
							  "text-decoration": "line-through"
								});
								$(this).parent().css("border-color","gray");
							}
							});*/
						
						$(".variant-select-sizeGuidePopUp option").each(function(){
							if(typeof($(this).attr("data-producturl"))!= 'undefined' && $(this).attr("data-producturl").indexOf(key)!= -1 && value == 0){
								$(this).attr("disabled","disabled");
								}
						});
					});

				/* PRICE BREAKUP STARTS HERE */
			    jwllryShowPrcBrkUp = data['displayconfigattr'];
			    jwlryPrcBrkUp = data['priceBreakup'];

				if(jwllryShowPrcBrkUp == "Yes"){
					$("#pricebreakupIdSpan").show();
					$("#showPrice").show();
					var priceBreakUp= '<p>Price Breakup</p>'
					$('#show').empty();
					$("#show").append( priceBreakUp);
				}else if(data['displayconfigattr'] == "No"){
					$("#showPrice").hide();				
				}else{
					$("#showPrice").hide();
				}				
				
				try{
					$('#showPriceBreakup').empty();
					var priceBreakupList = jwlryPrcBrkUp;
					var priceBreakup;
					
					if(null!=priceBreakupList && undefined!=priceBreakupList && !priceBreakupList==""){
						priceBreakup = JSON.parse(priceBreakupList);
					
						if(null!=priceBreakup && undefined != priceBreakup && !priceBreakup==""){
							priceBreakup.forEach(function(entry) {
								if(undefined!=entry){
									if(undefined!=entry.name){
										var html1 = '<tr><td class="title">'+entry.name+'</td>';
									}else {
										html1 = '<tr><td class="title">-</td>';
									}
									 var list;
									 var weightRateList = entry.weightRateList;
									 if(undefined!=weightRateList) {
										 	weightRateList.forEach(function(entry1) {
										 		if(undefined!=list){
										 			list=list+"<br>"+entry1;
										 		}else{
										 			list = entry1;
										 		}
									});
									var html2 = "<td>"+list+"</td>";
									}else {
										html2 = "<td>-</td>";
									}
									if(undefined!=entry.price){
										var html3 = "<td>"+entry.price.formattedValue+"</td></tr>";
									}else {
										html3 = "<td>-</td></tr>";
									}
									var preFinalHtml = html1.concat(html2);
									var finalHtml = preFinalHtml.concat(html3);
									$("#showPriceBreakup").append(finalHtml);
								}
							});
						}else {
							console.log("priceBreakup is undefined*******");	
						}
					}
				}		
				catch(err) {
					console.log("exception is:"+err);  
				}
				/* PRICE BREAKUP ENDS HERE */
					
					/* JewelleryDetail STARTS HERE */
					/*var jwelPDP = $("#jwelPDP").val();
					if (jwelPDP == "FineJewellery"){
						var jewelInfoKey = [], jewelInfoValue = [], jewelHeadingValue = [], jewelHeadingKey = [];
						var j=0;
						var jewelDetailslistForPDP = data['jewelDescription'];
						$.each(jewelDetailslistForPDP,function(key,value) {	
								jewelInfoKey[j] = key	;
								jewelInfoValue[j] = value;
								j++;
					});
					if (prop){
						var property = prop.split(',');
						var keyLOV = '' , valueLOV= '';
						for (var i=0; i<property.length; i++){
							var lovSplit = property[i].split("=");
							valueLOV = lovSplit[lovSplit.length-1];
							keyLOV = lovSplit[lovSplit.length-2];
							jewelHeadingKey[i] = keyLOV;
							jewelHeadingValue[i] = valueLOV;
					}
						for (var i=0; i<property.length; i++){
							if (jewelHeadingValue[i] == "null"){
								$(".key-label").append('<span>'+ jewelHeadingKey[i] +'</span>')
								}
							else if (jQuery.inArray(jewelHeadingValue[i], jewelInfoKey ) >= 0){
								var index = jQuery.inArray(jewelHeadingValue[i], jewelInfoKey );
								$(".key-label").append('<span>'+ jewelHeadingKey[i]+'(' + jewelInfoValue[index]+ ') </span>')
								}
							}
						}
					}
				}
				  catch(err) {
					  
					}
				  */
				/* JewelleryDetail ENDS HERE */
			if (data['sellerArticleSKU'] != undefined) {
				if (data['errMsg'] != "") {

					return false;
				} else {
					var channelTypeWeb = $("#promolist").val();//added for TISTE-143
					var promorestrictedSellers = $("#promotedSellerId").val();
					var promoindentifier = $("#product_applied_promotion_code").val();
					

					//added for TISTE-143
					if(channelTypeWeb != undefined)
					{



					 if (promorestrictedSellers == null
							|| promorestrictedSellers == undefined
							|| promorestrictedSellers == "") {
						//TPR-772
						$(".promo-block").show();
						if(promoindentifier != '') {
							  $(".pdp-promo-title-link").show();
						} 

					 } else {
						if (promorestrictedSellers.length > 0
								&& !(promorestrictedSellers
										.indexOf(data['sellerId']) == -1))
							{
							  //TPR-772 seller id matched with promotion ///*INC144313502*/
						       $(".pdp-promo-title-link").show();
						       $(".promo-block").show();
							}
						    else {
						    	 $(".offercalloutdesc").css({'float': 'right'});
									if($('#product_applied_promotion_code').val() != '') { //no potentialPromotions available  
										$(".primary_promo_desc").hide();
										$(".primary_promo_title").hide();
										$(".primary_promo_img").hide();
										
										
									}
								
							}

					 }



					}//added for TISTE-143
					var allStockZero = data['allOOStock'];
					// var codEnabled = data['isCod'];
					var sellerName = data['sellerName'];
					var sellerID = data['sellerId'];
					var oosMicro=data['isOOsForMicro']; // TPR-250
					
					
					$("#sellerNameId").html(sellerName);
					$("#sellerSelId").val(sellerID);
				//	alert(data['othersSellersCount']);
					if(data['othersSellersCount']>0 && oosMicro==true){ //TPR-250 change
						$("#addToCartButton").hide();
						$('#buyNowButton').hide();
						$("#outOfStockId").show();
						$("#allVariantOutOfStock").show();
						$("#otherSellerInfoId").show();
						$("#otherSellersId").html(data['othersSellersCount']);
						$("#otherSellerLinkId").show();
					}
					


					else if (isOOS() && data['othersSellersCount']>0) {
						//if( $("#variant,#sizevariant option:selected").val()!="#") {  //TISPRD-1173 TPR-465
						$("#addToCartButton").hide();
						$("#outOfStockId").show();
						$("#buyNowButton").hide();
						//}
						$("#otherSellerInfoId").hide();
						$("#otherSellerLinkId").show();
						//TPR-805
						//$("#outOfStockPinCodeMsg").show();
						//$("#availableStockPinCodeMsg").hide();
						//TPR-805
						 $("#pdpPincodeCheck").hide();
						 $("#pin").attr("disabled",true);
						 $("#pdpPincodeCheckDList").show();
						 $("#buyNowButton").attr("disabled",true);
						 $("#allVariantOutOfStock").show();
						
					}
					else if (isOOS() && data['othersSellersCount']==0){
						//if($("#variant,#sizevariant option:selected").val()!="#"){	//TISPRD-1173 TPR-465
							$("#addToCartButton").hide();
							$("#buyNowButton").hide();
							$("#outOfStockId").show();
						//}
						$("#otherSellerInfoId").hide();
						$("#otherSellerLinkId").hide();
						//TPR-805
						// $("#outOfStockPinCodeMsg").show();
						// $("#availableStockPinCodeMsg").hide();
						//TPR-805
						 $("#pdpPincodeCheck").hide();
						 $("#pin").attr("disabled",true);
						 $("#pdpPincodeCheckDList").show();
						 $("#buyNowButton").attr("disabled",true);
						 $("#allVariantOutOfStock").show();
						 
						
					}else if (allStockZero == 'Y' && data['othersSellersCount']>0 && ($("#variant li").length == $("#variant li.strike").length)) {
						//if( $("#variant,#sizevariant option:selected").val()!="#") {  //TISPRD-1173 TPR-465
						
						$("#addToCartButton").hide();
						$("#outOfStockId").show();
						$("#allVariantOutOfStock").show();
						$("#buyNowButton").hide();
						//}
						$("#otherSellerInfoId").hide();
						$("#otherSellerLinkId").show();
						//TPR-805
						// $("#outOfStockPinCodeMsg").show();
						// $("#availableStockPinCodeMsg").hide();
						//TPR-805
						 $("#pdpPincodeCheck").hide();
						 $("#pin").attr("disabled",true);
						 $("#pdpPincodeCheckDList").show();
						 $("#buyNowButton").attr("disabled",true);
						 $("#variant li a").each(function(){
								$(this).removeAttr("href");
								$(this).parent().addClass('strike');
								//$("#outOfStockId").hide();
						});
						
					}
					else if (allStockZero == 'Y' && data['othersSellersCount']==0 && ($("#variant li").length == $("#variant li.strike").length)){
						//if($("#variant,#sizevariant option:selected").val()!="#"){	//TISPRD-1173 TPR-465
							$("#addToCartButton").hide();
							$("#buyNowButton").hide();
							$("#outOfStockId").show();
							$("#allVariantOutOfStock").show();
							$("#variant li a").each(function(){
							$(this).removeAttr("href");
							$(this).parent().addClass('strike');
							//$("#outOfStockId").hide();
							});
						//}
						$("#otherSellerInfoId").hide();
						$("#otherSellerLinkId").hide();
						//TPR-805
						// $("#outOfStockPinCodeMsg").show();
						// $("#availableStockPinCodeMsg").hide();
						//TPR-805
						 $("#pdpPincodeCheck").hide();
						 $("#pin").attr("disabled",true);
						 $("#pdpPincodeCheckDList").show();
						 $("#buyNowButton").attr("disabled",true);
						
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
					//	alert(data['othersSellersCount']);
						$("#otherSellerInfoId").show();
						$("#otherSellersId").html(data['othersSellersCount']);
						$("#minPriceId").html(data['minPrice'].formattedValueNoDecimal);
					}

					$("#ussid").val(data['sellerArticleSKU']);
					$("#sellerSkuId").val(data['sellerArticleSKU']);
					//Added for Fine Jewellery Details Section
					if(data['sellerArticleSKU'] != undefined){
					$("#jewelDetailsUssid").html(data['sellerArticleSKU'].substring(6));
					}

					var spPrice = data['specialPrice'];
					var mrpPrice = data['mrp'];
					var mop = data['price'];
					var savingsOnProduct= data['savingsOnProduct'];
					$("#stock").val(data['availablestock']);
					$(".selectQty").change(function() {
						$("#qty").val($(".selectQty :selected").val());
					});
					displayDeliveryDetails(sellerName);
					//TISPRM-33 savingsOnProduct added
					dispPrice(mrpPrice, mop, spPrice, savingsOnProduct);
					
					//Add to Wishlist PDP CR
					var ussIdWishlist = data['sellerArticleSKU'];
					getLastModifiedWishlist(ussIdWishlist);
					//Ended here//
					
					if (isproductPage == 'false') {
						fetchAllSellers();
						$("#minPrice").html(data['minPrice'].formattedValueNoDecimal);
					}
					//Added for displaying offer messages other than promotion, TPR-589	
				//	ACC.productDetail.
					populateOfferMsgWrapper(productCode, sellerID, null);
					
				}	

			} 
				else {
				 $(".reviews").hide(); 	
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
				//INC144316346
				 if($.isEmptyObject(data['availibility'])){
					 $("#variant li a").removeAttr("href");
					 $("#variant li a").removeAttr("title");
					 $("#variant li a").attr("disabled", true);
					 $("#variant li a").parent().addClass("strike");
				 }
				 //INC144316346
			}
		},
		// For buybox seller and other seller in PDP
		//TPR-429
		complete: function() {
			differentiateSeller();
			//TPR-6654
			var pin = $('#pin').val();
			if(pin != ""){
				pincodeServiceability();
				$("#pdpPinCodeAvailable").html("Available delivery options for the pincode " +pin+ " are");				
			}
		}
		
	});
}
//Used to populate productDetailsTab.tag by ajax
function populateProductDetailsTab(jsonData){
	var rootCategory=$('input[name=rootCategoryMSD]').val();
	var htmlCode="";
	var productCategoryType=$('#productCategoryType').val();
	htmlCode=htmlCode+'<div class="tab-details">';
	htmlCode=htmlCode+'<ul>';
	htmlCode=htmlCode+'<input type="hidden" value="'+productCategoryType+'" id="productCategoryType"/>';
	if('Clothing' ==rootCategory || 'Footwear' == rootCategory || 'Accessories' == rootCategory)
	{
		$.each(jsonData['mapConfigurableAttributes'],function(key,value){
			if(value!="")
			{
				if('Accessories' == rootCategory && (key.toUpperCase().includes('Feature1'.toUpperCase()) || key.toUpperCase().includes('Feature2'.toUpperCase()) ||key.toUpperCase().includes('Feature3'.toUpperCase())))
				{
					$.each(value,function(key1,value1){
						htmlCode=htmlCode+'<li>'+ key1 +' &nbsp;&nbsp;'+value1+'</li>';
					});
				}
				else
				{
					$.each(value,function(key1,value1){
						htmlCode=htmlCode+'<li> '+ key +' - '+ key1 +' &nbsp;&nbsp;'+value1+'</li>';
					});
				}
			}
			else
			{
				htmlCode=htmlCode+'<li>'+ key+'</li>';
			}
		});
	}
	else
	{
		$.each(jsonData['mapConfigurableAttributes'],function(key,value){
			htmlCode=htmlCode+'<li>'+ key+' - '+value+'</li>';
		});
	}
	htmlCode=htmlCode+'<li>Product Listing Id :'+$('#product').val()+'</li>';
	htmlCode=htmlCode+'</ul>';
	htmlCode=htmlCode+'</div>';
	return htmlCode;
};
//Used to populate productDescriptionTab.tag by ajax
function populateProductDescriptionTab(jsonData)
{
	var htmlCode="";
	htmlCode=htmlCode+'<div class="tab-details">';
	if(typeof($('.tab-details span:eq(0)').text())!='undefined')
	{
		htmlCode=htmlCode+'<span itemprop="description">'+$('.tab-details span:eq(0)').text()+'</span>';
	}
	htmlCode=htmlCode+'<ul>';
	$.each(jsonData['mapConfigurableAttributes'],function(key,value){
		htmlCode=htmlCode+'<li>'+value+'</li>';
	});
	htmlCode=htmlCode+'</ul>';
	htmlCode=htmlCode+'</div>';
	return htmlCode;
}
//Used to populate productWarrantyTab.tag by ajax
function populateProductWarrantyTab(jsonData)
{
	var htmlCode="";
	htmlCode=htmlCode+'<div class="tabs_warranty">';
	$.each(jsonData['Warranty'],function(index,value){
		htmlCode=htmlCode+'<ul>';
		htmlCode=htmlCode+'<li>'+value+'</li>';
		htmlCode=htmlCode+'</ul>';
	});
	htmlCode=htmlCode+'</div>';
	return htmlCode;
}
function getProductDetailOnSizeChange(productCode)
{
	url=ACC.config.encodedContextPath + "/p-ajaxProductData?productCode=" + productCode;
	return $.ajax({
		url : url,
		method :"GET",
		cache : false,
	});
}
function changeBrowserUrl(originalUrl)
{
	//Used to re-write the browser URL so that onReload the user stays on the same page
	// Detect if pushState is available
	if(history.pushState) {
		history.pushState(null, null,originalUrl);
	}
}
function populatePromotionsInProductDetailsPanel(responsePotentialPromotions)
{
	//console.log("responsePotentialPromotions="+responsePotentialPromotions);
	var responseProductPromotionTiltle="";
	var responseProductPromotionCode="";
	if(!$.isEmptyObject(responsePotentialPromotions)&&responsePotentialPromotions.length>0)
	{	//If the current selected listing id has potential promotions
		responseProductPromotionTiltle=responsePotentialPromotions[0].title;
		responseProductPromotionCode=responsePotentialPromotions[0].code;
		var promoTitleDiv='<div class="pdp-promo-title pdp-title"><b>OFFER:</b> '+responsePotentialPromotions[0].title+'</div>';
		if(responsePotentialPromotions[0].channels!=null && responsePotentialPromotions[0].channels!="")
		{ 
			//if channels are present
			$.each(responsePotentialPromotions[0].channels,function(index, value){
				if(value=='Web' || value=='' || value==null)
				{
					$('div.pdp-promo-block.promo-block').html(promoTitleDiv);
				}
			});
		}
		else{
			//if channels are not present
			$('div.pdp-promo-block.promo-block').html(promoTitleDiv);
		}
		$("#promolist").val("[object Object]");//This stores object storing a dummy value
	}
	else{
		if(typeof($('div.pdp-promo-title.pdp-title'))!='undefined')
		{
			//If the current selected listing id doesn't have potential promotions and previous listing id had one
			$('div.pdp-promo-block.promo-block').html("");
		}
	}
	//Removing messages displayed through populateOfferMsgWrapper() method
	if(typeof($('div.pdp-offer-title.pdp-title'))!='undefined' && $('div.pdp-offer-title.pdp-title').length>0)
	{	
		$('div.pdp-offer-title.pdp-title').remove();
	}
	//disabling view more link
	$('a.pdp-promo-title-link').css('display','none');
	//part-3 of tealium data population
	$('#product_applied_promotion_title').val(responseProductPromotionTiltle);
	$('#product_applied_promotion_code').val(responseProductPromotionCode);
}
//Used to populate tabs in productPageTabs.tag using data retrieved using ajax
function populateProductPageTabs(jsonData)
{
	//if(jsonData['validTabs'].includes('stylenote'))
	if(jsonData['validTabs'].includes('details'))
	{
		//$('ul.tabs.pdp>li:eq(1)').html(populateProductDetailsTab(jsonData));
		var index=$('ul.nav.pdp>li').index($('#tabs_details'));
		if(index!=-1)
		{
			var selector='ul.tabs.pdp>li:eq('+index+')';
			$(selector).html(populateProductDetailsTab(jsonData));
			//UF-377
			$("#detailsAccordion").html(populateProductDetailsTab(jsonData));
		}
	}
	if(jsonData['validTabs'].includes('description'))
	{
		var index=$('ul.nav.pdp>li').index($('#tabs_description'));
		if(index!=-1)
		{
			var selector='ul.tabs.pdp>li:eq('+index+')';
			$(selector).html(populateProductDescriptionTab(jsonData));
			//UF-377
			$("#descriptionAccordion").html(populateProductDetailsTab(jsonData));
		}
	}
	if(jsonData['validTabs'].includes('warranty'))
	{
		var index=$('ul.nav.pdp>li').index($('#tabs_warranty'));
		if(index!=-1)
		{
			var selector='ul.tabs.pdp>li:eq('+index+')';
			$(selector).html(populateProductWarrantyTab(jsonData));
			//UF-377
			$("#warrantyAccordion").html(populateProductDetailsTab(jsonData));
		}
	}
	
	var catType = $("#categoryType").val();
	if((catType != undefined) && (("FINEJEWELLERY" == catType.toUpperCase()) || ("FASHIONJEWELLERY" == catType.toUpperCase()))){
		populateClassificationForJewellery(jsonData);
	}
	
}
function attachOnScrollEventForPdpOnSizeSelect()
{
	$(window).on('scroll load',function() {
		if(typeof($('input[name=isGigyaEnabled]').val())!='undefined' && $('input[name=isGigyaEnabled]').val()=="Y" && typeof(params)!='undefined')
		{
			lazyLoadGigyaCommentsUi();
		}
	});
}
//Function to get parameters form URL
function getParamsFromUrl(href)
{
	var regex = '[^&?]*?=[^&?]*';//Regex to get params from URL
	return href.match(regex);
}
//Function to get product code from Pdp Url ex:http://localhost:9001/white-van-heusen-mens-casual-shirt/p-987654324?selectedSize=true
function getProductCodeFromPdpUrl(url)
{
	//To get product code
	var a = $('<a>', { href:url } )[0];
//	console.log("a.hostname="+a.hostname);
//	console.log("a.pathname="+a.pathname);
//	console.log("a.queyparam="+a.search);
//	console.log("a.hash="+a.hash);
	var lastIndexOfp=a.pathname.lastIndexOf("/p-");
	if(lastIndexOfp!=-1)
	productCode=a.pathname.substring(lastIndexOfp+3);
	//console.log("ProductCode="+productCode);
	return productCode;
}

function showStoreLocatorModal(){
	$('body').on('hidden.bs.modal', '#storeLocatorModal', function () {
		  $(this).removeData('bs.modal');
		});
	$("#storeLocatorModal").modal("show");
}
//End of UF-60 changes
//Size Select For jewellery
function populateClassificationForJewellery(jsonData)
{
	var classification = jsonData['fineJewelleryDeatils'];
	var ussid = $("#ussid").val();
	if(ussid != undefined){
		ussid = ussid.substring(6);
	}
	var htmlCode="";
	if(typeof(classification) != "undefined") {
		$.each(classification, function(key,value){
			htmlCode=htmlCode+'<div class="item accordion_in">';
			htmlCode=htmlCode+'<div class="title acc_head">' + '<div class="acc_icon_expand"></div>';
			htmlCode=htmlCode+'<p>' + key + '</p></div>';
			htmlCode=htmlCode+'<div class="detail acc_content" style="display: none;">';
			if(key == 'Product Details'){
				htmlCode=htmlCode+'<div class="title">'+ 'PRODUCT CODE' + '<span id="jewelDetailsUssid" >' + ussid + '</span></div>';
				htmlCode=htmlCode+'<table><tbody>';
				$.each(value, function(innerKey,innerValue){
					htmlCode=htmlCode+'<tr><td class="title">' + innerKey + '</td>';
					$.each(innerValue, function(innerKey1,innerValue1){
						htmlCode=htmlCode+'<td>' + innerValue1 + '</td>';
					});
					htmlCode=htmlCode+'</tr>';
				});
				htmlCode=htmlCode+'</tbody></table>';
			}
			else if(key == 'Diamond Details'){
				$.each(value, function(innerKey,innerValue){
					if(innerKey == 'Total Count' || innerKey == 'Total Weight'){
						htmlCode=htmlCode+'<div class="t-d-d">' + innerKey + '<span>';
						$.each(innerValue, function(innerKey1,innerValue1){
							htmlCode=htmlCode+'<td>' + innerValue1 + '</td>';
						});
						htmlCode=htmlCode+'</span></div>';
					}
					else {
						htmlCode=htmlCode+'<table><tbody><tr><td class="title">' + innerKey + '</td>';
						$.each(innerValue, function(innerKey1,innerValue1){
							htmlCode=htmlCode+'<td>' + innerValue1 + '</td>';
						});
						htmlCode=htmlCode+'</tr>';
						htmlCode=htmlCode+'</tbody></table>';
					}
				});
			}
			else {
				htmlCode=htmlCode+'<table><tbody>';
				$.each(value, function(innerKey,innerValue){
					htmlCode=htmlCode+'<tr><td class="title">' + innerKey + '</td>';
					$.each(innerValue, function(innerKey1,innerValue1){
						htmlCode=htmlCode+'<td>' + innerValue1 + '</td>';
					});
					htmlCode=htmlCode+'</tr>';
				});
				htmlCode=htmlCode+'</tbody></table>';
			}
			htmlCode=htmlCode+'</div>'+'</div>';
		});
	}
	if(jwllryShowPrcBrkUp == "Yes"){
		htmlCode = htmlCode + '<div id="showPrice" class="item accordion_in">';
		htmlCode = htmlCode + '<p id="show" class="title acc_head"><span>Price BreakUp</span></p>';
		try{
			var priceBreakupList = jwlryPrcBrkUp;
			if(null!=priceBreakupList && undefined!=priceBreakupList && !priceBreakupList==""){
				$('#showPriceBreakup').empty();
				var priceBreakup = JSON.parse(priceBreakupList);
				if(null!=priceBreakup && undefined != priceBreakup && !priceBreakup==""){
					htmlCode = htmlCode + '<div class="detail acc_content" style="display: none;"><table id="showPriceBreakup" style="display:block"><tbody>';
					priceBreakup.forEach(function(entry) {
						if(undefined!=entry.name){
							htmlCode = htmlCode + '<tr><td class="title">'+entry.name+'</td>';
						}else{
							htmlCode = htmlCode + '<tr><td class="title">-</td>';
						}
						//htmlCode = htmlCode + '<tr><td class="title">'+entry.name+'</td>';
						 var list;	
						 var weightRateList = entry.weightRateList;
						 if(undefined!=weightRateList) {
							 	weightRateList.forEach(function(entry1) {
							 		if(undefined!=list){
							 			list=list+"<br>"+entry1;
							 		}else{
							 			list = entry1;
							 		}
						});
							 	htmlCode = htmlCode + '<td>' +list+ '</td>';
						}else {
							htmlCode = htmlCode + '<td>-</td>';
						}
						 if(undefined!=entry.price){
							 htmlCode = htmlCode + '<td>'+entry.price.formattedValue+'</td></tr>';
						 }else{
							 htmlCode = htmlCode + "<td>-</td></tr>";
						 }
						// htmlCode = htmlCode + '<td>'+entry.price.formattedValue+'</td></tr>';
					});
					htmlCode = htmlCode + '</tbody> </table></div>';
				}
				else {
					console.log("priceBreakup is undefined*******");
				}
			}
		}
		catch(err){
			console.log("exception is:"+err);  
		}
		htmlCode=htmlCode+'</div>';
	}
	$(".accordin").html(htmlCode);
	
	var mapConfig = jsonData['mapConfigurableAttributes'];
	var jwlryTitle = "";
	$.each(mapConfig, function(key,value){
		if(key == "Feature"){
			$.each(value, function(innerKey,innerValue){
				jwlryTitle = jwlryTitle + innerKey;
			});
		}
	});
	$(".product-detail .product-desc .key-label p#jwlryTitle").text(jwlryTitle);
	var pTag = document.createElement("p");
	pTag.innerHTML=jwlryTitle;
	$( ".tab-details ul .product-desc .key-label" ).prepend(pTag);
	if(jwlryTitle == ""){
		$(".product-detail .product-desc .key-label .more-link").css("display","none");
	}
}

//TPR-1083 Start
$(document).ready(function(){
	
	jQuery('#brandExchange').on('input', function() {
		 $("#brandExchangeParam").val($("#brandExchange").val());
		 $("#lbrand").text("Brand");
			document.getElementById('lbrand').style.color = "#999999";
	});
	

	var pdppin = document.getElementById("pin");
	var pinform=document.getElementById("pincodeExchangeParam");
	$("#pinExc").keyup(function() {
		document.getElementById("pdpPincodeCheck").className = "Check";
		$("#exchangeDetails").hide();
		$("#couponValue").hide();
	    $("#exPinnotserviceable").hide();
					
		pdppin.value = this.value;	
		pinform.value=this.value;
		
	});
	
	$('#exchange_tc').change(function(){
		
		$("#error_tc").hide();
		
	});

	
$("#pdpPincodeCheckExchnage").on("click",function(){
	//click on PDP pincode Check
	$( "#pdpPincodeCheck" ).trigger( "click" );
    
	$.when(pinCodeCheckajax).done(function(pincoeData){
		
		var pinExc = $('#pinExc').val();
		var productCode =  $('#product_id').val();
		var productArray =[];
		productArray.push(productCode);
	for (var i = 0; i < pincoeData.length; i++) {
		
		//check pincodeData if it is servicable
		if (pincoeData[i]['isServicable'] == 'Y') 
				
{
  //ajax call to check greendust
	var dataString = 'pin=' + $("#pinExc").val();
    var reversecheck=false;

	
var req1=$.ajax({
		url : ACC.config.encodedContextPath + "/p-checkReversePincode",
		data : dataString,
		type : "GET",
		cache : false,
		success : function(data) {
			if (data != null) {
				reversecheck=data;
				//alert("successs");
				//var pinExc = $('#pinExc').val();
				//alert("exchnge pin boss "+pinExc);
			} else {
				alert("no data");
			}
														
		},
		error : function(resp) {
			alert("error")
		}
	});
$.when(req1).done(function(data1){
	if(data1)
    {
		//Message for successful pincode check
		$("#serviceablePinExc").show();
    	populateExchangeDetails();
    	if(typeof utag !="undefined"){
			utag.link({
				event_type : "exchange_pincode_true",
				pincode  : pinExc,
				product_id : productArray
			});
		}
    }
    else{
    	//Message for unsuccessful exchange Pincode check
    $("#exPinnotserviceable").show();
	$("#serviceablePinExc").hide();
    	if(typeof utag !="undefined"){
			utag.link({
				event_type : "exchange_pincode_false",
				pincode  : pinExc,
				product_id : productArray
			});
		}
    }
});
  
 // return false;
				}


				
				}
	




//Message for unsuccessful exchange Pincode check forward check
	if(typeof utag !="undefined"){
		utag.link({
			event_type : "exchange_pincode_false",
			pincode  : pinExc,
			product_id : productArray
		});
	}
    
});

});
//end document ready

});
function populateExchangeDetails()
{
	
	$("#ussidExchange").val($("#ussid").val());
	   $("#exStock").val($("#stock").val());
	
	var l3code = $('#l3code').val();
	 var prodCode = $('#productcode').val();
	var dataString = 'l3code=' + l3code;
	var reversecheck=false;
    var pinExc = $('#pinExc').val();
	var productCode =  $('#product_id').val();
	var productArray =[];
	productArray.push(productCode);
$.ajax({
		url : ACC.config.encodedContextPath + "/p-exchange",
		data : dataString,
		type : "GET",
		cache : false,
		success : function(data) {
			if (data != null) {
				
				
				 var catOptions = "<option value= disabled selected>Select</option>";
				   
			        for (i = 0; i < data.l4categorylist.length; i++) {
			        	    	           catOptions += "<option value='"+data.l4categorylist[i]+"'>" + data.l4categorylist[i] + "</option>";
			        }
			        document.getElementById("l4select").innerHTML = catOptions;
			        
			       
			     activelist=data.isWorkinglist;
			     pricelist=data.priceList;
			        
			      
				$("#exchangeDetails").show();
				document.getElementById('submit&Condition').style.display = "block";
				
				//alert("successs");
				//var pinExc = $('#pinExc').val();
				//alert("exchnge pin boss "+pinExc);
			} else {
				alert("no data");
			}
														
		},
		error : function(resp,error) {
			alert("error:" + error);
			//tpr-5193|exchange error
			if(typeof utag !="undefined"){
				utag.link({
					error_type : "exchange_unavailable"
				});
			}
		}
	});
}



function changeWorking(value) {
	document.getElementById('submit&Condition').style.display = "block";
	 document.getElementById('couponValue').style.display = "none";
	  $("#l4Exchange").val(value);
	  $("#ll4select").text("Type");
		document.getElementById('ll4select').style.color = "#999999";
	    if (value.length == 0) document.getElementById("activeselect").innerHTML = "<option></option>";
    else {
    	
        var catOptions = "<option value= disabled selected>Select</option>";
   
        for (i = 0; i < activelist.length; i++) {
           	          catOptions += "<option value='"+activelist[i].trim()+"'>" +activelist[i].trim()+ "</option>";
        }
        document.getElementById("activeselect").innerHTML = catOptions;
    }
}

function changePrice(value) {
	document.getElementById('submit&Condition').style.display = "block";
	 document.getElementById('couponValue').style.display = "none";
	 $("#lactiveselect").text("Working Condition");
		document.getElementById('lactiveselect').style.color = "#999999";
	
	l4val=$('#l4select').val();
	l4wokinval=l4val+"|"+value;
	 $("#isWorkingExchange").val(l4wokinval);
	    var catOptions = "";
        for (i = 0; i < pricelist.length; i++)
        {
        	var price=pricelist[i].split(":");
        	
        	 if(l4wokinval===price[0].trim())
       	   	{
        		 
        		 document.getElementById('priceselect').innerHTML =  price[1];
        		
       	   	}
        	
       }
     
}

function onSubmitExc()
{
	var brand= $("#brandExchangeParam").val();


	var l4select=$('#l4select').val();
	var isError=false;
	var isWorking=$('#activeselect').val();
	//TPR-5193 Analytics starts
	var brandExchange = $('#brandExchange').val();
	var couponValue = $('#priceselect').text();
	var l3 =$('#l3').val();
    var selector	 = $('#activeselect option:selected').val();
  //  var exchangeDropdown = selector.split('|');
  //  var exchangeCondition = exchangeDropdown[1] ;
    var productCode =  $('#product_id').val();
	var productArray =[];
	productArray.push(productCode);
	//TPR-5193 Analytics ends
	//JWLSPCUAT-1590
		if(!brand || brand.trim().length==0)
			{
			$("#lbrand").text("Please Enter Brand");
			document.getElementById('lbrand').style.color = "red";
			isError=true;
			}
		if(l4select==='disabled' ||!l4select )
			{
			$("#ll4select").text("Please Select Type");
			document.getElementById('ll4select').style.color = "red";
			isError=true;
			}
		if(isWorking==='disabled' ||!isWorking)
			{
			$("#lactiveselect").text("Please Select Working/Non Working");
			document.getElementById('lactiveselect').style.color = "red";
			isError=true;
			}
		if(!$('input#exchange_tc').is(':checked'))
			{ $("#error_tc").text("Please Agree to the Terms & Condition");
				document.getElementById('error_tc').style.color = "red";
				isError=true;
			}
		if(!isError)
			{
	          document.getElementById('couponValue').style.display = "block";
	          document.getElementById('submit&Condition').style.display = "none";
     	//TPR-5193
	   if(typeof utag !="undefined"){
		  utag.link({
			event_type          : "exchange_success",
			exchange_brand      : brandExchange ,
			exchange_condition  : selector ,
			exchange_l3         : l3 ,
			couponcode_exchange : couponValue ,
			product_id          : productArray
		});
	   }
			}
	
}
//TPR-1083 End