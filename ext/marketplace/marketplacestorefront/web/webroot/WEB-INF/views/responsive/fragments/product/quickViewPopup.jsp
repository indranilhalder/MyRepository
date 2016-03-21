<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<script type="text/javascript"
	src="${commonResourcePath}/bootstrap/js/popover.js"></script>
<script type="text/javascript" src="http://apis.google.com/js/plusone.js"></script>
 <style type="text/css">
tr.d0 td {
  background-color:#E0E0E0 ;
  color: black;
}
</style>
 <script type="text/javascript">
      (function() {
       var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
       po.src = 'https://apis.google.com/js/client:plusone.js';
       var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
     })();
    
    </script>
<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/acc.productDetail.js"></script> --%>
 <script>
 function openPopForBankEMI_quick() {
		var productVal = $("#productPrice").val();
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
 
 
 function getSelectedEMIBankForPDP() {
	 var productVal = $("#productPrice").val();
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
 function openPop_quick(ussidfromSeller) {
		$('#addedMessage_quick').hide();
		if (ussidfromSeller == null || ussidfromSeller == "") {
			ussidValue = $("#ussid_quick").val();
		} else {
			ussidValue = ussidfromSeller;
		}
		
		var productCode = $("#product_quick").val();
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

	function gotoLogin() {
		window.open(ACC.config.encodedContextPath + "/login", "_self");
	}

	var wishListList = [];

	// load wishlist of a particular user on opening popup

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
	
 
 
 var imagePageLimit = ${imgCount};
/*  $(document).ready(function(){
 	 $("#previousImage").css("opacity","0.5");
 	$("#nextImage").css("opacity","1");
 	var listHeight = $(".imageList li").height();
 	if($("#previousImage").length){
 		$(".imageList").css("height",(listHeight*imagePageLimit)+"px");
 		$(".productImageGallery").css("height",(listHeight*imagePageLimit+100)+"px");
 	}
 	$(".imageListCarousel").show(); 
 }); */
 function nextImage()
 {
 	var item_height = $(".product-info .imageListCarousel li").height();
 	var item_count = $(".product-info .imageListCarousel li").length;
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
 $( document ).ready(function() {
	 
	 getRating_Qview('${gigyaAPIKey}','${product.code}','${product.rootCategory}');
	 
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
	 
	
	$(document).on('show.bs.modal', "#modalProd", function() {
		$(".zoomContainer").remove();
		$('.picZoomer-pic').removeData('zoom-image');
	});
	$(document).on('hide.bs.modal', "#modalProd", function() {
		$('.quickview .picZoomer-pic').elevateZoom({
			zoomType : "window",
			cursor : "crosshair",
			zoomWindowFadeIn : 500,
			zoomWindowFadeOut : 750
		});
	});
	
	 	$("#previousImage").css("opacity","0.5");
	 	$("#nextImage").css("opacity","1");
	 	var listHeight = $(".imageList li").height();
	 	if($("#previousImage").length){
	 		$(".imageList").css("height",(listHeight*imagePageLimit)+"px");
	 		$(".productImageGallery").css("height",(listHeight*imagePageLimit+100)+"px");
	 	}
	 	$(".imageListCarousel").show();
	
		$(".black-arrow").change(function() {
			 
			$("#qty1").val($(".black-arrow :selected").val());
		});
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
	 $("#cboxClose").click(function(){
		$(".zoomContainer").remove();
		$('.picZoomer-pic').removeData('zoom-image');
		$(".picZoomer-pic-wp img").attr('data-zoom-image',$(".product-image-container .productImageGallery .imageList .active img").attr("data-zoomimagesrc")); 
		$('.picZoomer-pic-wp .picZoomer-pic').elevateZoom({
	    zoomType: "window",
	    cursor: "crosshair",
	    zoomWindowFadeIn: 500,
	    zoomWindowFadeOut: 750
	       }); 
	       //TISEE-882
	       if(window.location.href.toLowerCase().indexOf('cart')>=0)
	       {
	    	   location.reload();
	       }
	       
	  });
 if( $("#variant option:selected").val()=="#")
	  {
		//  alert("Please select size!");
	 //return false;
	  }	 
 
 $('a.wishlist#wishlist_quick').popover({ 
	    html : true,
	    content: function() {
	      return $(this).parents().find('.add-to-wishlist-container_quick').html();
	    }
	  });
 $('input.wishlist#add_to_wishlist_quick').popover({ 
		html : true,
		content: function() {
			return $(this).parents().find('.add-to-wishlist-container_quick').html();
		}
	});
 
 function getRating_Qview(key,productCode,category)
 {
 	
 	var url = "https://comments.us1.gigya.com/comments.getStreamInfo?apiKey="+key+"&categoryID="+category+"&streamId="+productCode+"&includeRatingDetails=true&format=jsonp&callback=?";
 	  $.getJSON(url, function(data){
 	  	var totalCount=data.streamInfo.ratingCount;
 		//Reverse the source array
 		var ratingArray = data.streamInfo.ratingDetails._overall.ratings;
 		ratingArray  = ratingArray.reverse();
 		
 		  $(".rate-details .after").each(function(count){			  
 				
 				var countIndiv=ratingArray[count];	
 				$(".rate-bar .rating").eq(count).css({width:countIndiv/totalCount*100+"%"});
 				$(".rate-details .after").eq(count).text(ratingArray[count]);
 				
 			})
 			
 			var avgreview=data.streamInfo.avgRatings._overall;
 			var raingcount=data.streamInfo.ratingCount;
 			$(".product-detail ul.star-review a").empty();
 			$(".product-detail ul.star-review li").attr("class","empty");
 			$('#customer').text("Customer Reviews (" + data.streamInfo.ratingCount + ")");
 			//$("#gig-rating-readReviewsLink_quick").html(raingcount);
 		//	alert("count"+data.streamInfo.ratingCount);
 			//rating(avgreview,raingcount);
 			
 			
 			var rating = Math.floor(avgreview);
	 		var ratingDec = avgreview - rating;
	 		for(var i = 0; i < rating; i++) {
	 			$("#quick_view_rating"+" li").eq(i).removeClass("empty").addClass("full");
	 			}
	 		if(ratingDec!=0)
	 			{
	 			$("#quick_view_rating"+" li").eq(rating).removeClass("empty").addClass("half");
	 			} 
 		
 		
 			//TISUATPII-471 fix
 			var count=data.streamInfo.ratingCount;
 			if(count!= 0 && count == 1){
 				//$('#ratingDiv .gig-rating-readReviewsLink').text(data.streamInfo.ratingCount+" REVIEW");
 				$('.gig-rating-readReviewsLink_quick').text(count+" REVIEW")
 			}
 			else if(count!= 0){
 				//$('#ratingDiv .gig-rating-readReviewsLink').text(data.streamInfo.ratingCount+" REVIEWS");
 				$('.gig-rating-readReviewsLink_quick').text(count+" REVIEWS")
 			}
 			
 			
 			
 			
 	  });
 	  
 	// var avgrating = '${product.averageRating}';
 		//alert(":-:"+avgrating);
 		
 }
 	  
 </script>
 <style type="text/css">
tr.d0 td {
  background-color:#E0E0E0 ;
  color: black;
}
div.emailSend{

display:none;
}
</style>

<c:set var="image"><spring:theme code='quickview.image'/></c:set>
<c:set var="video"><spring:theme code='quickview.video'/></c:set>
<c:set var="cod_y"><spring:theme code='cod.eligible.yes'/></c:set>
<c:set var="stock_y"><spring:theme code='quickview.allOOStock'/></c:set>
<span id="defaultWishId_quick" style="display:none"><spring:theme code="wishlist.defaultname"/></span>
<span id="wishlistSuccess_quick" style="display:none"><spring:theme code="wishlist.success"/></span>
<span id="wishlistnotblank_quick" style="display:none"><spring:theme code="wishlist.notblank"/></span>
<span id="sharepretext" style="display:none"><spring:theme code="share.pretext"/></span>
<span id="shareposttext" style="display:none"><spring:theme code="share.posttext"/></span>
<span id="productUrl" style="display:none">${request.contextPath}${product.url}</span>
<span id="googleClientid" style="display:none">${googleClientid}</span>
<span id="facebookAppid" style="display:none">${facebookAppid}</span>

<span id="inventory" style="display: none"><p class="inventory">
			<font color="#ff1c47"><spring:theme code="Product.outofinventory" /></font>
		</p></span>
<input type="hidden" value="false"  id="isBinded"/>
<input type="hidden" value="true"  id="isQuickView"/>
<input type="hidden" value="${product.rootCategory}" id="categoryType"/>
<c:url value="${product.url}" var="productUrl" />
<input type="hidden" value=""  id="ussidInQuickView"/>
<input type="hidden" value="${product.code}"  id="productCode"/>
<%-- <input type="hidden" maxlength="3" size="1" id="stock" name="stock"
		value="${availablestock}" /> --%>
<div class="quickview active">
<div class="content">
<div class="quick-view-popup product-info wrapper">

<div class="product-image-container">
   <c:set var="increment" value="0"/>
<c:set var="thumbNailImageLength" value="${fn:length(galleryImages)}" />

<div class="productImageGallery image-list" style="position: relative; /* max-height: 390px; */">
<c:if test="${thumbNailImageLength > imgCount}"> 
	<button type="button" class="previous white" style="margin: 0 auto;"
				name="previousImageDevBtn" id="previousImage" onclick="previousImage()">
		<img src="${commonResourcePath}/images/thin_top_arrow_333.png"/><%-- <spring:theme code="product.othersellers.previous" /> --%>
	</button>
</c:if>
	<div class="imageList" style="overflow: hidden;">
		<ul class="jcarousel-skin imageListCarousel" style="display:none; position: relative; top: 0; width: 100%;"> 
			<c:forEach items="${galleryImages}" var="container" varStatus="varStatus" begin="0" end="${thumbNailImageLength}">
				<li id="addiImage${varStatus.index}">
					<span class="thumb ${(varStatus.index==0)? "active":""}">
						<c:if test="${container.thumbnail.mediaType.code eq 'Image'}">
							<img src="${container.thumbnail.url}" data-type="image" data-zoomimagesrc="${container.superZoom.url}"  data-primaryimagesrc="${container.product.url}" data-galleryposition="${varStatus.index}" alt="${container.thumbnail.altText}" title="${container.thumbnail.altText}" />	
						</c:if>
						<c:if test="${container.thumbnail.mediaType.code eq 'Video'}">
							<img src="${commonResourcePath}/images/video-play.png"  data-type="video" data-videosrc="${container.thumbnail.url}" />
					    </c:if>
					</span>
				</li>
			</c:forEach>
		</ul>
	</div>
	<input type="hidden" id="totalAdditionalImage" name="totalAdditionalImage" value="${thumbNailImageLength}">

<c:if test="${thumbNailImageLength > imgCount}"> 
	<button type="button" class="next white" name="nextImageDevBtn" style="/* position:absolute; */ margin: 0 auto; bottom: 0; /* left: 18px; */" id="nextImage" onclick="nextImage()"> 
		<img src="${commonResourcePath}/images/thin_bottom_arrow_333.png"/>
	</button>
</c:if>
</div>

    <div class="main-image">
    <a href="${productUrl}"> <product:productPrimaryImage
				product="${product}" format="product" />
		</a>
		 <%-- <c:if test="${isCodEligible=='Y'}">
          <div class="cod" id="codId">
		  <span ><spring:theme code="product.cod"/></span> 
		  </div>
	 </c:if> --%>
		<c:if test="${isNew=='true'}">
		 <div id ="newProduct" style="z-index: 1;display:none;" class="new new-product">
					<img class="brush-strokes-sprite sprite-New"
					src="/store/_ui/responsive/common/images/transparent.png"><span>New</span>
					</div>
	 	   </c:if> 
	   <c:if test="${isOnline=='true'}">
		 	<div style="z-index: 1;" class="online-exclusive">
					<img class="brush-strokes-sprite sprite-Vector_Smart_Object"
						src="/store/_ui/responsive/common/images/transparent.png"> <span><spring:theme code="quickview.onlineexclusive"/></span>
				</div>
		</c:if>
		</div>
	
		<c:choose>
		<c:when test="${spPrice ne null}">
			<c:if test="${spPrice.value>emiCuttOffAmount}">
			<div id="emiStickerId" class="emi">
							<spring:theme code="marketplace.emiavailable" />&nbsp;
							<a type="button" name="yes" id="prodEMI"
		data-target="#modalProd" onclick="openPopForBankEMI_quick()"
		data-toggle="modal"><spring:theme code="marketplace.emiinfo"></spring:theme></a> <input id="prodPrice" type="hidden" />
						</div>
				<product:emiDetail product="${product}" />
			</c:if>
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${mopPrice ne null}">
					<%-- <input type="hidden" id="mopId" value="${mopPrice}"/> --%>
					<%-- <div>${mopPrice.formattedValue}</div> --%>
					<c:if test="${mopPrice.value>emiCuttOffAmount}">
						<div id="emiStickerId" class="emi">
							<spring:theme code="marketplace.emiavailable" />&nbsp;
							<a type="button" name="yes" id="prodEMI"
							data-target="#modalProd" onclick="openPopForBankEMI_quick()"
							data-toggle="modal"><spring:theme code="marketplace.emiinfo"></spring:theme></a> <input id="prodPrice" type="hidden" />
						</div>
						<product:emiDetail product="${product}" />
					</c:if>
				</c:when>
				<c:otherwise>
					<%-- <input type="hidden" id="mrpId" value="${mrp}"/> --%>
					<%-- <div>${mrpPrice.formattedValue}</div> --%>
					<c:if test="${mrpPrice.value>emiCuttOffAmount}">
						<div id="emiStickerId" class="emi">
							<spring:theme code="marketplace.emiavailable" />&nbsp;
							<a type="button" name="yes" id="prodEMI"
		data-target="#modalProd" onclick="openPopForBankEMI_quick()"
		data-toggle="modal"><spring:theme code="marketplace.emiinfo"></spring:theme></a> <input id="prodPrice" type="hidden" />
						</div>
					<product:emiDetail product="${product}" />
			
					</c:if>
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
		
	</div>	
    <div class="product-detail">
    <h2 class="company">
              <span class="logo"></span>${product.brand.brandname}&nbsp;<spring:theme code="product.by"/>&nbsp;${sellerName}</h2>
    <h3 class="product-name"><a href="${productUrl}">${product.name}</a></h3>
    <div class="price">
    <c:choose>
    <c:when test="${not empty spPrice.value && spPrice.value ne 0}">
     <c:choose>
     	<c:when test="${mopPrice.value eq mrpPrice.value}">
     	    <input type="hidden" value="${spPrice.value}"  id="productPrice"/>
     		<span class="old">${mrpPrice.formattedValue}</span>&nbsp;<span class="sale">${spPrice.formattedValue}</span>
     	</c:when>
     	<c:otherwise>
     		<span class="old"> ${mrpPrice.formattedValue}</span>&nbsp;<span class="sale">${spPrice.formattedValue}</span>
     		 <input type="hidden" value="${spPrice.value}"  id="productPrice"/>
     	</c:otherwise>
     </c:choose>
     </c:when>
     <c:otherwise>
      <c:choose>
     	<c:when test="${not empty mopPrice.value && mopPrice.value ne 0}">
     		<c:choose>
     			<c:when test="${mopPrice.value eq mrpPrice.value}">
     			     <input type="hidden" value="${mrpPrice.value}"  id="productPrice"/>
     				<span class="sale">${mrpPrice.formattedValue}</span>
     			</c:when>
     			<c:otherwise>
     			     <input type="hidden" value="${mrpPrice.value}"  id="productPrice"/>
     				<span class="old">${mrpPrice.formattedValue}</span>&nbsp;<span class="sale">${mopPrice.formattedValue}</span>
     			</c:otherwise>
    		</c:choose>
     	</c:when>
     	<c:otherwise>
     	     <input type="hidden" value="${mrpPrice.value}"  id="productPrice"/>
     		<span class="sale">${mrpPrice.formattedValue}</span>
     	</c:otherwise>
     	</c:choose>
     </c:otherwise>
    </c:choose>
  </div>   
<a href="#" class="gig--readReviewsLink"></a>
	<span id="gig-rating-readReviewsLink_quick" ></span>	
  <input type="hidden" id="rating_review" value="${product.code}">
		 <ul class="star-review" id="quick_view_rating">
				<li class="empty"></li>
				<li class="empty"></li>
				<li class="empty"></li>
				<li class="empty"></li>
				<li class="empty"></li>
		
		<%-- 	<c:choose>
				<c:when test="${not empty product.ratingCount}">
			
					<span id="gig-rating-readReviewsLink_quick" >  <spring:theme code="rating.reviews"/></span>
				</c:when>
				<c:otherwise> --%>
					<span class="gig-rating-readReviewsLink_quick"> <spring:theme code="rating.noreviews"/></span>
				<%-- </c:otherwise>
			</c:choose> --%>
			</ul> 
			
<!-- 			 <script>
				var avgrating = '${product.averageRating}';
				//alert(":-:"+avgrating);
				var rating = Math.floor(${product.averageRating});
				//alert("::"+rating);
				var ratingDec = ${product.averageRating} - rating;
				var star_id = $("#rating_review").val();
				for(var count = 0; count < rating; count++) {
					$("#quick_view_rating"+" li").eq(count).removeClass("empty").addClass("full");
					}
				if(ratingDec!=0)
					{
					$("#quick_view_rating"+" li").eq(rating).removeClass("empty").addClass("half");
					} 
				
			</script>  -->
 
 	<div class="fullfilled-by">
		<spring:theme code="mpl.pdp.fulfillment"></spring:theme>&nbsp;
		<c:choose>
		<c:when test="${fn:toLowerCase(fullfilmentType) == fn:toLowerCase('sship')}">
			<span id="fullFilledById">${sellerName}</span>
		</c:when>
		<c:otherwise>
			<span id="fullFilledById"><spring:theme code="product.default.fulfillmentType"/></span>
		</c:otherwise>
		</c:choose>
	</div>
   <div class="product-content" style="margin-top:15px;">
	   <div class="swatch">
	<product:viewQuickViewVariant/>
	<div class="qty">
		<p> <spring:theme code="product.configureproductscount.qty"/></p>
		<select id="quantity">
			<option>1</option>
			<option>2</option>
			<option>3</option>
			<option>4</option>
			<option>5</option>
			<option>6</option>
			<option>7</option>
			<option>8</option>
			<option>9</option>
			<option>10</option>
		</select>
	</div>

</div>
<%--  <div id="ajax-loader" style="margin: 0 auto; height:20px; width: 20px;"><img src="${commonResourcePath}/images/ajax-loader.gif"></div> --%>     
<!-- add to cart functionality -->
<div id="addToCartFormQuickTitle" class="addToCartTitle">
		</div>
	<ycommerce:testId code="quickview_addToCart_button_${product.code}">
		<%-- <form:form id="addToCartFormQuick" action="${request.contextPath }/cart/add" method="post" class="add_to_cart_form"> --%>
		<form:form id="addToCartFormQuick" action="#" method="post" class="add_to_cart_form">
		<span id="addToCartFormQuickTitleSuccess" class="addToCartTitle">
		</span>
		<%-- <form:form method="post" id="addToCartFormQuick" class="add_to_cart_form"
		action="${request.contextPath }/cart/add"> --%>
		<c:if test="${product.purchasable}">
			<input type="hidden" maxlength="3" size="1" id="qty1" name="qty"
				class="qty js-qty-selector-input" value="">
		</c:if>
		<input type="hidden" name="productCodePost" id="productCodePost" value="${product.code}" />
		<input type="hidden" name="wishlistNamePost" id="wishlistNamePost" value="N" />
		<input type="hidden" name="ussid" id="ussid_quick"	value="${buyboxUssid}"/>
		<input type="hidden" maxlength="3" size="1" id="stock" name="stock"
		value="${availablestock}" />
		<c:choose>
			<c:when test="${allOOStock==stock_y}">
			<%-- <span id="outOfStockId" style="display: block"  class="out_of_stock">
				<font color="red"><spring:theme code="product.product.outOfStock" /></font>
				<input type="button" id="add_to_wishlist_quick" onClick="openPop_quick('${buyboxUssid}');scrollbottom();" class="wishlist" data-toggle="popover" data-placement="bottom" value="<spring:theme code="text.add.to.wishlist"/>"/>
			</span>
				<button type="${buttonType}"
					class="btn-block js-add-to-cart outOfStock"
					disabled="disabled">
					<spring:theme code="product.variants.out.of.stock" />
				</button> --%>
			</c:when>
			<c:otherwise>			
					<button id="addToCartButton" type="${buttonType}"
						class="btn-block js-add-to-cart">
						<spring:theme code="basket.add.to.basket" />
					</button>
				
			</c:otherwise>
		</c:choose>
		
	<%-- 	<c:choose>
		<c:when test="${selectedSize!=null || product.rootCategory=='Electronics'}">
		<c:choose>
			<c:when test="${allOOStock==stock_y}">
			<span id="outOfStockId" style="display: block"  class="out_of_stock">
				<font color="red"><spring:theme code="product.product.outOfStock" /></font>
				<input type="button" id="add_to_wishlist" onClick="openPop('${buyboxUssid}');scrollbottom();" class="wishlist" data-toggle="popover" data-placement="bottom" value="<spring:theme code="text.add.to.wishlist"/>"/>
			</span>
				<button type="${buttonType}"
					class="btn-block js-add-to-cart outOfStock"
					disabled="disabled">
					<spring:theme code="product.variants.out.of.stock" />
				</button>
			</c:when>
			<c:otherwise>			
					<button id="addToCartButton" type="${buttonType}"
						class="btn-block js-add-to-cart">
						<spring:theme code="basket.add.to.basket" />
					</button>
				
			</c:otherwise>
		</c:choose>
		</c:when>
		<c:otherwise>
		<button id="addToCartButton" type="${buttonType}"
						class="btn-block js-add-to-cart">
						<spring:theme code="basket.add.to.basket" />
					</button>
		</c:otherwise>
	</c:choose> --%>
	</form:form>
	<c:if test="${allOOStock==stock_y}">
			<span id="outOfStockId" style="display: block"  class="out_of_stock">
				<font color="red"><spring:theme code="product.product.outOfStock" /></font>
				<input type="button" id="add_to_wishlist_quick" onClick="openPop_quick('${buyboxUssid}');scrollbottom();" class="wishlist" data-toggle="popover" data-placement="bottom" value="<spring:theme code="text.add.to.wishlist"/>"/>
			</span>
				<button type="${buttonType}"
					class="btn-block js-add-to-cart outOfStock"
					disabled="disabled">
					<spring:theme code="product.variants.out.of.stock" />
				</button>
	</c:if>
	</ycommerce:testId>    
<!-- adding to wishlist -->
				<ul class="wish-share">
					<li><!-- <span id="addedMessage" style="display:none"></span> -->
						<a onClick="openPop_quick('${buyboxUssid}');scrollbottom();" id="wishlist_quick" class="wishlist" data-toggle="popover" data-placement='bottom'><spring:theme code="text.add.to.wishlist"/></a></li>
				<%-- <a onClick="openPop();" id="wishlist" class="wishlist" data-toggle="popover" data-placement='bottom'><spring:theme code="text.add.to.wishlist"/></a></li> --%>
					<li>
						<div class="share">
							<span><spring:theme code="product.socialmedia.share"/></span>
							<ul style="width: 200px;">
								<li>
							<a class="tw" onclick="return openPopup('https://twitter.com/intent/tweet?text='+ $('#sharepretext').text() + ' ' +window.location.host+ $('#productUrl').text() + ' ' + $('#shareposttext').text())"></a>
							</li>
							<li>
							<a class="fb" onclick="return openPopup('https://www.facebook.com/dialog/feed?link=' + window.location.host+ $('#productUrl').text() + '&amp;app_id=' + $('#facebookAppid').text() + '&amp;description='+$('#sharepretext').text()+' '+$('#shareposttext').text()+' &amp;redirect_uri=http://www.facebook.com/')"></a> 
							 <!-- <a class="fb" onclick="return openPopup('https://www.facebook.com/dialog/feed?link=' + window.location + '&amp;app_id=145634995501895&amp;description='+$('#sharepretext').text()+' '+$('#shareposttext').text()+' &amp;redirect_uri=https://developers.facebook.com/tools/explorer')"></a>  -->				
							
							</li>
								<li>
							<button class="g-interactivepost"
                            data-contenturl=""
                            data-clientid='${googleClientid}'
                            data-cookiepolicy="single_host_origin"
                            data-prefilltext="<spring:theme code="share.pretext"/><spring:theme code="share.posttext"/>"
                            data-calltoactionlabel="OPEN"
                            data-calltoactionurl="">
                           <a class="gp"></a>
                           </button>
							<!-- <a class="gp" onclick="return openPopup('https://plusone.google.com/_/+1/confirm?url=https://www.dev.tataunistore.com&amp;clientid=888304528479-qdh1rp8r9o5fvh3dlabr7ebdbr02se6e.apps.googleusercontent.com&amp;prefilltext=helooo&amp;calltoactionurl=https://www.google.com')"></a>  -->
							<!-- <a href="" class="gp" onclick="return openPopup('https://plusone.google.com/_/+1/confirm?url=' + window.location)"></a> -->
							</li>
							
							<li><%-- <a class="mail" data-target="#popUpModal" data-url="${SHARED_PATH}"></a> --%>
							<a class="mail" id="mailQuick" role="button" data-toggle="popover" data-placement="bottom" onClick="scrollbottom();"></a></li>
							</ul>
						</div>
					</li>
				</ul>
				
	<script>
			$(".g-interactivepost").attr("data-contenturl",window.location.host+$('#productUrl').text());
			$(".g-interactivepost").attr("data-calltoactionurl",window.location.host+$('#productUrl').text());
			//$(".wish-share .share a.tw").attr("href","https://twitter.com/intent/tweet?text=Wow! I found this amazing product - check it out here"+window.location+". Like or comment to tell me what you guys think. Hit share to spread the love. ");
			
			var popUpWidth=500;
			var popUpHeight=450;
				 var PopUpLeftPosition = screen.width/2 - popUpWidth/2;
				    var PopUpTopPosition= screen.height/2 - popUpHeight/2;
			function openPopup(url) {
				    window.open(url, 'popup_id','scrollbars,resizable,height='+popUpHeight+',width='+ popUpWidth +',left='+ PopUpLeftPosition +',top='+ PopUpTopPosition);
			      return false;
			    }
			
			function scrollbottom() {
				//alert();
				  $("#cboxLoadedContent").animate({ scrollTop: $('#cboxLoadedContent')[0].scrollHeight }, "slow");
				 return false;
				};
			
  </script>

<div class="add-to-wishlist-container_quick">
<form>
	<input type="hidden" value="${product.code}" id="product_quick" />

				

					<div id="wishListDetailsId_quick" class="other-sellers" style="display: none">
					<h3 class="title-popover"><spring:theme code="wishlist.select"/></h3>
						<table class="other-sellers-table add-to-wishlist-popover-table">
							<%-- <thead>
								<tr>
									<th><spring:theme code="product.wishlist"/></th>
									<th><spring:theme code="product.select"/></th>
								</tr>
							</thead> --%>

							<tbody id="wishlistTbodyId_quick">
							</tbody>
						</table>

						 <input type="hidden" name="hidWishlist" id="hidWishlist_quick">
						<span id="addedMessage_quick" style="display:none;color:blue"></span>
						
						<button type='button' onclick="addToWishlist_quick()" name='saveToWishlist' id='saveToWishlist' class="savetowishlistbutton"><spring:theme code="product.wishlistBt"/></button>
					</div>

					<div id="wishListNonLoggedInId_quick" style="display: none"><spring:theme code="product.wishListNonLoggedIn"/></div>

				
				
			
			<!-- /.modal-content -->
		
		<!-- /.modal-dialog -->

	<!-- /.modal -->

</form>
</div>

				<!-- Social sharing -->


				<div id="fb-root"></div>
				<script>
				$("#cboxClose").empty();
				$("#cboxClose").addClass("close");
				
					(function(d, s, id) {
						var js, fjs = d.getElementsByTagName(s)[0];
						if (d.getElementById(id))
							return;
						js = d.createElement(s);
						js.id = id;
						js.src = "//connect.facebook.net/en_GB/sdk.js#xfbml=1&version=v2.3";
						fjs.parentNode.insertBefore(js, fjs);
					}(document, 'script', 'facebook-jssdk'));
				</script>

				<script>
					!function(d, s, id) {
						var js, fjs = d.getElementsByTagName(s)[0], p = /^http:/
								.test(d.location) ? 'http' : 'https';
						if (!d.getElementById(id)) {
							js = d.createElement(s);
							js.id = id;
							js.src = p + '://platform.twitter.com/widgets.js';
							fjs.parentNode.insertBefore(js, fjs);
						}
					}(document, 'script', 'twitter-wjs');
				</script>
</div>


   
	</div>

</div></div>

		<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
			<input type="hidden" id="loggedInQuick" value="false"/> 
		</sec:authorize>
		<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
		<input type="hidden" id="loggedInQuick" value="true"/> 
		</sec:authorize>
 	
<div class="quick-view-prod-details-container">
<a href="${productUrl}" class="quick-view-prod-details-link"><spring:theme code="quickview.productdetails"/></a>
</div>
<span id="addtobag" style="display:none"><spring:theme code="product.addtocart.success"/></span>
<span id="addtobagerror" style="display:none"><spring:theme code="product.error"/></span>
<span id="bagtofull" style="display:none"><spring:theme code="product.addtocart.aboutfull"/></span>
<span id="bagfull" style="display:none"><spring:theme code="product.bag"/></span>

</div>

 <div id="emailLoggedInIdquick" style="display: none"><spring:theme code="product.emailNonLoggedIn"/></div>
<div id="emailSendQuick" class="emailSend">
	
				<div class="click2chat-container send-email-container" id="myModalLabel">
					<h1><spring:theme code="email.mail"/></h1>
					
					<form>
				
				
				<label for="forgotPassword_email"><spring:theme code="email.mailto"/></label>
				<input type="text" id="emailId_quick" placeholder="abc@xyz.com,abc@xyz.com">
				<input type="hidden" id="pId_quick" value="${product.code}"/>
				
				<!-- <button type="button" id="sendEmail" class="blue" onClick="sendmail()">Send Mail</button> -->
				<div class="actions">
				
				<button id="sendEmail"  onClick="return sendmail_quick()" type="button">
				<spring:theme code="email.send"/>
				</button>
				<span id="email_quick" style="display:none"></span>
				<span id="emailError_quick" style="display:none;color:#ff1c47"><spring:theme code="email.emailError"/></span>
				<span id="emailSuccess_quick" style="display:none;color:#60A119"><spring:theme code="email.emailSuccess"/></span>
				<span id="emailUnSuccess_quick" style="display:none;color:#ff1c47"><spring:theme code="email.emailUnSuccess"/></span>
				<span id="emailEmpty_quick" style="display:none;color:#ff1c47"><spring:theme code="email.emailEmpty"/></span>
				<span id="validateemail_quick" style="display:none;color:#ff1c47"><spring:theme code="email.validate"/></span>

				</div>
				</form>
				</div>
	
</div>
<script>

$('ul.wish-share div.share').mouseleave(function(){
 $(this).find('[data-toggle="popover"]').popover('hide');
});
function sendmail_quick(){
	$('#emailSuccess_quick,#emailUnSuccess_quick,#emailError_quick,#validateemail_quick,#emailEmpty_quick').hide();
	var email = $('#emailId_quick').val();
	var productId = $('#pId_quick').val();
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
					$('#emailUnSuccess_quick,#emailError_quick,#validateemail_quick,#emailEmpty_quick').hide();
					$('#emailSuccess_quick').show();
				}
				else{
					$('#emailSuccess_quick,#emailError_quick,#validateemail_quick,#emailEmpty_quick').hide();
					$('#emailUnSuccess_quick').show();
					return false;
				}
			},
			error : function(resp) {
				$('#emailSuccess_quick,#emailUnSuccess_quick,#validateemail_quick,#emailEmpty_quick').hide();
				$('#emailError_quick').show();
				return false;
			}
		});
	}
	else{
		$('#emailSuccess_quick,#emailUnSuccess_quick,#emailError_quick,#emailEmpty_quick').hide();
		$('#validateemail_quick').show();
		return false;
	}
	}
	else{
		$('#emailSuccess_quick,#emailUnSuccess_quick,#emailError_quick,#validateemail_quick').hide();
		$('#emailEmpty_quick').show();
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
$('ul.wish-share a#mailQuick').popover({ 
	html : true,
	content: function() {
		var loggedIn = $("#loggedInQuick").val();
		if(loggedIn=='true') {
			return $(this).parents().find('#emailSendQuick').html();
		} else {
			return '<div style="padding: 10px;">'+$(this).parents().find('#emailLoggedInIdquick').html()+'</div>';
			
		}
	}
});


$(document).on("keypress","#defaultWishName_quick",function(e) {
	var wishlistname = $("#defaultWishName_quick").val();
	var key = e.keyCode;
	if((key>=33 && key<48) || (key>=58 && key<65) || (key>=91 && key<97)){
		e.preventDefault();
		 var start = this.selectionStart,
         end = this.selectionEnd;
		$('#defaultWishName_quick').val(wishlistname);
		$("#addedMessage_quick").show();
		$("#addedMessage_quick").html("<font color='#ff1c47'><b>Special characters are not allowed</b></font>");
		$("#addedMessage_quick").show().fadeOut(3000);
		this.setSelectionRange(start, end);
	} 
}); 


</script>
