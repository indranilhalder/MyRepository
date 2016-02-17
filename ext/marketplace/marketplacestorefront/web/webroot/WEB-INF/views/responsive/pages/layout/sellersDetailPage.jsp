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
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/responsive/storepickup"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%--<template:javaScriptVariables />
<script type="text/javascript"
	src="${commonResourcePath}/js/acc.productDetail.js"></script> --%>
<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.sellerDetails.js"></script> --%>
	<product:sellerForm></product:sellerForm>
	<input type="hidden" maxlength="10" size="1" id="pinCodeCheckedFlag" name="pinCodeCheckedFlag" value="${pincodeChecked}" />	
		<input type="hidden" value="${productCategoryType}" id="categoryType"/>
    <span id="deliveryPretext" style="display:none;"><spring:theme code="mpl.pdp.delivery.pretext"/></span>
    <span id="deliveryPosttext" style="display:none;"><spring:theme code="mpl.pdp.delivery.posttext"/></span>
	<script>
	var pincodeChecked=$("#pinCodeCheckedFlag").val();
	$("#isPinCodeChecked").val(pincodeChecked);
	var selectedSize='${selectedSize}';
	 var sellerskuidList=[];
	var stockUssidIds=[];
	var ussidIdsForED=[];
	var ussidIdsForHD=[];
	var ussidIdsForCOD=[];
	var stockDataArray=[];
	var si=-1;
    var sq=-1;
    var s=-1;
    var e=-1;
    var h=-1;
    var c=-1;
    var cod=-1;
    var codCounter=-1;
    var ns=-1
    var indices=-1;
	</script>
	
	<c:forEach items="${sellersSkuIdList}" var="notservicableSeller" varStatus="loop">
	<input type="hidden" id="notServicableUssid${loop.index}" value="${notservicableSeller}"/>	
	<script>
	 var loopStatus='${loop.index}';
	sellerskuidList[++indices]=$("#notServicableUssid"+loopStatus).val();
	//sellerskuidList[++indices]='${notservicableSeller}';
	  if(sellerskuidList!=""||sellerskuidList!=[]){
	    	$("#sellersSkuListId").val(sellerskuidList);
	    	//$("#notServicableUssid").val("");
		}
	</script>
	</c:forEach>
	 <c:forEach items="${stockData}" var="stockData">
    <script>
    var stockData=new Object();
    var ussid='${stockData.ussid}';
    var stock='${stockData.stockCount}';
    stockData.ussid=ussid;
    stockData.stock=stock;
    stockDataArray[++si]=stockData;
    </script>
	</c:forEach> 
	<script>
	$("#stockDataArray").val(stockDataArray);

	</script>
	<c:forEach items="${skuIdForED}" var="skuIdForED" varStatus="loop">
	<c:if test="${not empty skuIdForED}">
	 <input type="hidden" id="skuIdForEDs${loop.index}" value="${skuIdForED}"/>	
	<script>
	 var loopStatus='${loop.index}';
	//ussidIdsForED[++e]=${skuIdForED};
	ussidIdsForED[++e]=$("#skuIdForEDs"+loopStatus).val();
	if(ussidIdsForED!=""||ussidIdsForED!=[]){
		$("#skuIdForED").val(ussidIdsForED);
		//$("#skuIdForEDs").val("");
		
	}
	</script>
	</c:if>
	</c:forEach>
	<c:forEach items="${skuIdForHD}" var="skuIdForHD" varStatus="loop">
    <c:if test="${not empty skuIdForHD}">
    <input type="hidden" id="skuIdForHDs${loop.index}" value="${skuIdForHD}"/>
	<script>
	 var loopStatus='${loop.index}';
	ussidIdsForHD[++h]=$("#skuIdForHDs"+loopStatus).val();
    if(ussidIdsForHD!=""||ussidIdsForHD!=[]){
    	$("#skuIdForHD").val(ussidIdsForHD);
    //	$("#skuIdForHDs").val("");
	}
	</script>
	</c:if>
	</c:forEach>
	<c:forEach items="${skuIdsWithNoStock}" var="noStockSkuId" varStatus="loop">
    <c:if test="${not empty noStockSkuId}">	
      <input type="hidden" id="skuIdForNoStocks${loop.index}" value="${noStockSkuId}"/>
	<script>
	 var loopStatus='${loop.index}';
	 stockUssidIds[++ns]=$("#skuIdForNoStocks"+loopStatus).val();
	 if(stockUssidIds!=""||stockUssidIds!=[]){
	    	$("#skuIdsWithNoStock").val(stockUssidIds);
	    	//$("#skuIdForNoStocks").val("");
		}
	</script>
	</c:if>
	</c:forEach>
	<c:forEach items="${skuIdForCod}" var="codSkuId" varStatus="loop">
	<c:if test="${not empty codSkuId}">	
    <input type="text" id="codUssids${loop.index}" value="${codSkuId}"/>
	<script>
	 var loopStatus='${loop.index}';
	 ussidIdsForCOD[++codCounter]=$("#codUssids"+loopStatus).val();
	 if(ussidIdsForCOD!=""||ussidIdsForCOD!=[]){
		    $("#skuIdForCod").val(ussidIdsForCOD);
		}
	</script>
	</c:if>
	</c:forEach>
<style type="text/css">
tr.d0 td {
  background-color:#E0E0E0 ;
  color: black;
}
</style>	
<script>

var buyboxskuId='';  
var productCode = '${product.code}';
var ussidArray=[];
var seq = -1;
var pageLimit = '${pageLimit}';
var pageCount = 1;
var deliveryDates='${deliveryDates}';
var buyboxussid='${buyboxussid}';
var mrp = '${product.productMRP.value}';
var deliveryModeMap="";
var availableDeliveryATPForHD="";
var availableDeliveryATPForED="";
var ix=-1;
var s=-1;
var hdIndx=-1;
var edIndx=-1;
$(document).ready(function() {
	
   	getRating('${gigyaAPIKey}','${product.code}','${product.rootCategory}');
	 var stockMap="${skuIdsWithNoStock}"; 
	
	 fetchPrice();
     setSellerLimits(1);
     $(".selectQty").change(function() {
 		$("#qty").val($(".selectQty :selected").val());
 		
 	   var input_name = "qty";
 	    var stock_id="stock";
 		$('form[name^="addToCartFormId"]').each(function() {
 			
 			  var input = $("#"+this.name+" :input[name='" + input_name +"']"); 
 			  $(input).val($("#qty").val()); 
 			//  alert($("#"+this.name+" :input[name='" + stock_id +"']").val());
 			});
 		
 		//alert("--"+$(input).val());
 		//test ends
 		
 	});
	
	$(".share-wrapper-buybox").hide();
	$(".share-trigger-buybox").click(function(){
		$(".share-wrapper-buybox").toggle();
	});
});
function firstToUpperCase( str ) {
    return str.substr(0, 1).toUpperCase() + str.substr(1);
}
</script>
<script src="https://apis.google.com/js/platform.js" async defer></script>
<style>
.tooltip_wrapper {
	position: relative;
	cursor: pointer;
	margin-bottom: 5px;
}
.tooltip_pop {
	position: absolute;
	bottom: 100%;
	left: 0;
	display: block;
	background-color: #fff;
	padding: 5px 10px;
	border-collapse: separate;
	box-shadow: 0 0 9px #a9143c;
	visibility: hidden;
}
.tooltip_pop:after {
	content: "";
	display: block;
	width: 8px;
	height: 8px;
	background-color: #fff;
	-ms-transform: rotate(45deg);
	-webkit-transform: rotate(45deg);
	-moz-transform: rotate(45deg);
	transform: rotate(45deg);
	border-collapse: separate;
	box-shadow: 1px 1px 0px #a9143c;
	position: absolute;
	left: 50%;
	margin-left: -4px;
}
div.emailSend{

display:none;
}
</style> 
<input type="hidden" id="promotedSellerId" value="${product.potentialPromotions[0].allowedSellers}"/>
<input type="hidden" id="promotedSellerId" value="${product.potentialPromotions[0]}"/>


    <fmt:formatDate pattern="dd/MM/yyyy h:mm:ss a" 
                    value="${product.potentialPromotions[0].startDate}" var="strDate" />   
   <fmt:formatDate pattern="dd/MM/yyyy h:mm:ss a" value="${product.potentialPromotions[0].endDate}" var="endDate"/> 
	<script>
    var promoData=new Object();
    var allowedSellers='${product.potentialPromotions[0].allowedSellers}';
    var promoTitle='${product.potentialPromotions[0].title}';
    var promoDescription='${product.potentialPromotions[0].description}';
    var promoStartDate='${strDate}';
    var promoEndDate='${endDate}';
    promoData.allowedSellers=allowedSellers;
    promoData.promoTitle=promoTitle;
    promoData.promoDescription=promoDescription; 
    promoData.promoStartDate=promoStartDate; 
    promoData.promoEndDate=promoEndDate; 
    </script>
<%-- 	</c:forEach> --%>


<!-- Displaying wishlist -->
<input type="hidden" id="isproductPage" value="false"/>
<div id="sellersSkuListId"></div>
<span id="defaultWishId" style="display:none"><spring:theme code="wishlist.defaultname"/></span>
<c:forEach var="delivery" items="${deliveryModeMap}">
<c:if test="${delivery.key eq 'home-delivery'}">
		 <c:forEach var="homeEntry" items="${delivery.value}">
			 <c:if test="${homeEntry.key eq 'startForHome'}">
			 <input type="hidden" value="${homeEntry.value}" id="homeStartId"/>
			 </c:if>
			  <c:if test="${homeEntry.key eq 'endForHome'}">
			  <input type="hidden" value="${homeEntry.value}" id="homeEndId"/>
		     </c:if>
		 </c:forEach>
		 <script>
		 var deliveryKey=firstToUpperCase('${delivery.key}');
		// availableDeliveryATPForHD=deliveryKey.concat("-").concat(deliveryValue);
		 availableDeliveryATPForHD=deliveryKey;
		 </script>
	 </c:if>
  <c:if test="${delivery.key eq 'express-delivery'}">
			 <c:forEach var="expressEntry" items="${delivery.value}">
			 <c:if test="${expressEntry.key eq 'startForExpress'}">
			 <input type="hidden" value="${expressEntry.value}" id="expressStartId"/>
			 </c:if>
			  <c:if test="${expressEntry.key eq 'endForExpress'}">
			  <input type="hidden" value="${expressEntry.value}" id="expressEndId"/>
		     </c:if>
		    </c:forEach>
		     <script>
		 var deliveryKey=firstToUpperCase('${delivery.key}');
		 var start=$("#expressStartId").val();
		 var end=$("#expressEndId").val();
		 var deliveryValue="Delivered in"+start+"-"+end+"business days";
		 availableDeliveryATPForED=deliveryKey.concat("-").concat(deliveryValue);
 </script>
</c:if> 
<script>
//var deliveryKey=firstToUpperCase('${delivery.key}');
//var deliveryValue=firstToUpperCase('${delivery.value}');
//availableDeliveryATP[++ix]=deliveryKey.concat("-").concat(deliveryValue);

var allSellers='${allsellers}';
</script>
</c:forEach> 		
<%-- <form>
	<input type="hidden" value="${product.code}" id="product" />
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="overlay"></div>
		<div class="modal-dialog" style="top:-80%; background-color: whitesmoke;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close pull-right" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">
						<b><spring:theme code="product.wishlist" /></b>
					</h4>
				</div>
				<div class="modal-body" id="modelId">

					<div id="wishListDetailsId" class="other-sellers" style="display: none">
						<table class="other-sellers-table">
							<thead>
								<tr>
									<th><spring:theme code="product.wishlist"/></th>
									<th><spring:theme code="product.select"/></th>
								</tr>
							</thead>

							<tbody id="wishlistTbodyId">
							</tbody>
						</table>

						<br> <input type="hidden" name="hidWishlist" id="hidWishlist">
						<span id="addedMessage" style="display:none"></span><button type='button' onclick="addToWishlist()"
							style="     
										margin: 0 auto;
										color: #fff;
									    background-color: #00cbe9;
									    width: 200px;
									    text-align: center;
									    cursor: pointer;
									    font-size: 12px;
									    font-weight: 600;"
							name='saveToWishlist' id='saveToWishlist'><spring:theme code="product.wishlistBt"/></button>
					</div>

					<div id="wishListNonLoggedInId" style="display: none"><spring:theme code="product.wishListNonLoggedIn"/></div>

				</div>
				<div class="modal-footer">
					<!--   <button type="button" class="btn btn-default" 
               data-dismiss="modal">Close
            </button> -->
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->

</form> --%>



<%-- <cms:pageSlot position="PinCodeService" var="component">
				<cms:component component="${component}" />
			</cms:pageSlot>
 --%>
<div class="pdp other-sellers">
<div class="product-info wrapper">
<div class="product-image-container">

<!-- Displaying thumbnail images -->
	<cms:pageSlot position="ConfigureImagesCount" var="component">
		<cms:component component="${component}" />
	</cms:pageSlot>
<!-- Displaying main image -->
	<product:productImagePanel galleryImages="${galleryImages}"
		product="${product}" />

	<input id="emiCuttOffAmount" type="hidden" value="${emiCuttOffAmount}"/>
				<!-- EMI section -->
				<product:emiDetail product="${product}" />
</div>


		<div class="product-detail">
			<ycommerce:testId code="productDetails_productNamePrice_label_${product.code}">
			<h2 class="company">${product.brand.brandname} by <span id="sellerNameId"></span></h2>
				<h3 class="product-name">${product.productTitle}</h3>
			</ycommerce:testId>
			<ycommerce:testId
				code="productDetails_productNamePrice_label_${product.code}">
				<product:productPricePanel product="${product}" /> <!-- Displaying buybox price -->
			</ycommerce:testId>
			<div class="fullfilled-by">
			<spring:theme code="mpl.pdp.fulfillment"></spring:theme>&nbsp;
			<span id="fulFilledByTship" style="display:none;"><spring:theme code="product.default.fulfillmentType"></spring:theme></span>
			<span id="fulFilledBySship"  style="display:none;"></span>
			</div>
			<!-- Returning to the PDP page -->
			<p class="other-sellers back">
			<a href="${request.contextPath}/${product.url}"><spring:theme code="product.returnpdp"></spring:theme></a></p>
</div>
<div class="product-content">
	<!-- Displaying product variants -->
<!-- 	<div class="swatch"> -->
				<%-- <cms:pageSlot position="VariantSelector" var="component">
					<cms:component component="${component}" />
				</cms:pageSlot> --%>
				<product:productMainVariant /> 
				<cms:pageSlot position="AddToCart" var="component">
					<cms:component component="${component}" />
				</cms:pageSlot>
<!-- 
			</div> -->
		<%-- <product:productVariant/>
	  
			
						<!-- Information to be passed after clicking to the "Add to Bag" button-->
						 <cms:pageSlot position="AddToCart" var="component">
					          <cms:component component="${component}" />
				        </cms:pageSlot>  --%>
		   <sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
			<input type="hidden" id="loggedIn" value="false"/> 
		</sec:authorize>
		<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
		<input type="hidden" id="loggedIn" value="true"/> 
		</sec:authorize>   
				       
		   <ul class="wish-share">
				<li>
				<span id="wishlistSuccess" style="display:none"><spring:theme code="wishlist.success"/></span>
				<!-- <span id="addedMessage" style="display:none"></span> -->
				<a onClick="openPop();" id="wishlist" class="wishlist" data-toggle="popover" data-placement='bottom'>Add to Wishlist</a></li>
				<li>
				<product:socialSharing product="${product}" />
					
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
			
  </script>
				
<input type="hidden" id="productCode" value="${product.code}" />
<cms:pageSlot position="PinCodeService" var="component">
				<cms:component component="${component}" />
			</cms:pageSlot>

	</div>
 </div>
</div>
<div class="other-sellers" id="other-sellers-id">
		<div class="header "><h2><spring:theme code="product.othersellers"></spring:theme></h2>
		<p><span id="otherSellersCount"></span>&nbsp;<span class="other-sellers-info"><spring:theme code="product.othersellers"></spring:theme></span>&nbsp;<spring:theme code="product.available"></spring:theme>&nbsp;<span id="minPrice" ></span></p>
		</div>
		<div id="sort" class="sort-by" style="display:none"><label><spring:theme code="seller.sort"/></label>
		<select id="sellerSort" onchange="sortSellers(this.value);">
				<%-- <option><spring:theme code="product.select"/></option> --%>
				<option value="1"><spring:theme code="seller.sort.priceasc"/></option>
				<option value="2"><spring:theme code="seller.sort.pricedesc"/></option>
   		</select>
	</div>
	
		 <table id ="sellerTable" class="other-sellers-table">
			<thead>
				<tr >
					<th><spring:theme code="product.sellersinfo"></spring:theme></th>
					<th><spring:theme code="product.warrentytab.price"></spring:theme></th>
					<th><spring:theme code="product.deliveryinformation"></spring:theme></th>
					<th><spring:theme code="product.buyingoption"></spring:theme></th>
				</tr>
			</thead>

			<tbody id="sellerDetailTbdy">
			</tbody>
		</table>
    <span id="addtocartid" style="display:none"><spring:theme code="product.addtocart.success"/></span>
    <span id="addtocartaboutfullid"  style="display:none"><spring:theme code="product.addtocart.aboutfull"/></span>
    <span id="addtocartfullid"  style="display:none"><spring:theme code="product.bag"/></span>
    <span id="addtocartfailedid"  style="display:none"><spring:theme code="product.addtobag.unsuccessful"/></span>
    <span id="replacementguranteeid"  style="display:none"><spring:theme code="seller.product.replacement"/></span>
    <span id="cashondeliveryid"  style="display:none"><spring:theme code="seller.product.cashondelivery"/></span>
    <span id="emiavailableid"  style="display:none"><spring:theme code="marketplace.emiavailable"/></span>
    <span id="addtobagid"  style="display:none"><spring:theme code="seller.product.addtobag"/></span>
    <span id="addwishlistid"  style="display:none"><spring:theme code="product.addwishlist"/></span>
    <span id="deliveryratesid"  style="display:none"><spring:theme code="seller.product.deliveryrates"/></span>
    <span id="returnpolicyid"  style="display:none"><spring:theme code="seller.product.returnpolicy"/></span>
    <%-- <span id="sharepretext" style="display:none"><spring:theme code="share.pretext"/></span>
	<span id="shareposttext" style="display:none"><spring:theme code="share.posttext"/></span>
	<span id="productUrl" style="display:none">${request.contextPath}${product.url}</span> --%>
	
	<div id="sellerCount" class="navigation">
		<div class="wrapper">
			<p></p>
		</div>

		<div class="btn-placement">
			<button type="button" class="previous white"
				name="previousPageDevBtn" id="previousPageDev"
				onclick="previousPage()">
				<spring:theme code="product.othersellers.previous" />
			</button>

			<button type="button" class="next white" name="nextPageDevBtn"
				id="nextPageDev" onclick="nextPage()">
				<spring:theme code="product.othersellers.next" />
			</button>
		</div>
	</div>



	<div id="hiddenIdForStock" style="display:none">	
										<input type="hidden" maxlength="3" size="1" id="qty"
											name="qty" class="qty js-qty-selector-input" value="1">					
										<input type="hidden" maxlength="3" size="1"
											id="pinCodeChecked" name="pinCodeChecked" value="false">
										<input type="hidden" name="productCodePost"
											value="${product.code}" />
												<input type="hidden" id="productCode"
											value="${product.code}" />
										<input type="hidden" name="wishlistNamePost" value="N" />																		
										
										<span id="addToCartButtonId">
	
	</span>
																	
	
	</div> 
	<div id="hiddenIdForNoStock" style="display:none">	
										<input type="hidden" maxlength="3" size="1" id="qty"
											name="qty" class="qty js-qty-selector-input" value="1">					
										<input type="hidden" maxlength="3" size="1"
											id="pinCodeChecked" name="pinCodeChecked" value="false">
										<input type="hidden" name="productCodePost"
											value="${product.code}" />
												<input type="hidden" id="productCode"
											value="${product.code}" />
										<input type="hidden" name="wishlistNamePost" value="N" />																		
										
										<span id="outOfStockId"><p class="out_of_stock">
			<spring:theme code="product.product.outOfStock" /></p> </span>
								
	
	</div>
	
	
<!-- Comments and review section -->
<%-- <product:reviewComments/> --%>
	
	<div id="hiddenWishListId" style="display:none;">	
	<div class="product-info">
	<div class="product-content" style="width:118% !important;">
			</div>
			</div>
	</div>
</div>

<!-- For Infinite Analytics Start -->
	<div class="trending"  id="ia_products"></div>
<!-- For Infinite Analytics End -->

<div class="add-to-wishlist-container">
<form>
	<input type="hidden" value="${product.code}" id="product" />

				

					<div id="wishListDetailsId" class="other-sellers" style="display: none">
					<h3 class="title-popover">Select Wishlist:</h3>
						<table class="other-sellers-table add-to-wishlist-popover-table">
							<%-- <thead>
								<tr>
									<th><spring:theme code="product.wishlist"/></th>
									<th><spring:theme code="product.select"/></th>
								</tr>
							</thead> --%>

							<tbody id="wishlistTbodyId">
							</tbody>
						</table>

						 <input type="hidden" name="hidWishlist" id="hidWishlist">
						<span id="addedMessage" style="display:none;color:blue"></span>
						
						<button type='button' onclick="addToWishlist()" name='saveToWishlist' id='saveToWishlist' class="savetowishlistbutton"><spring:theme code="product.wishlistBt"/></button>
					</div>

					<div id="wishListNonLoggedInId" style="display: none"><spring:theme code="product.wishListNonLoggedIn"/></div>

				
				
			
			<!-- /.modal-content -->
		
		<!-- /.modal-dialog -->

	<!-- /.modal -->

</form>
</div>

