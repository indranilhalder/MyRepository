<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>
 
<!--- START: INSERTED for MSD --->
<c:if test="${isMSDEnabled}">
<input type="hidden" value="${isMSDEnabled}" name="isMSDEnabled"/>
	<c:forEach items="${cartData.entries}" var="entryMSD">
		<c:if test="${entryMSD.product.rootCategory eq 'Clothing'}">
		        <c:set var="includeMSDJS" scope="request" value="true"/>
		        <input type="hidden" value="true" name="isApparelExist"/>
		        <input type="hidden" value="${pageType}" name="currentPageMSD"/>
		</c:if>
	</c:forEach>	
	<c:if test="${includeMSDJS eq 'true'}">
		<script type="text/javascript"	src="${msdjsURL}"></script>
	</c:if> 
</c:if>
<!--- END:MSD --->


<style type="text/css">
tr.d0 td {
  background-color:#E0E0E0 ;
  color: black;
}
</style>

<script type="text/javascript">
function openPopFromCart(entry,productCode,ussid) {
	
	//var productCode = $("#product").val();
	var requiredUrl = ACC.config.encodedContextPath + "/p"+"/viewWishlistsInPDP";
	var dataString = 'productCode=' + productCode+ '&ussid=' + ussid;//modified for ussid
	var entryNo = $("#entryNo").val(entry);
		$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		dataType : "json",
		success : function(data) {
			if(data==null)
			{
				$("#wishListNonLoggedInId").show();
				$("#wishListDetailsId").hide();
			}
			else if (data == "" || data == []) {
				loadDefaultWishLstForCart(productCode,ussid);
			}
			else
			{
				LoadWishListsFromCart(data, productCode,ussid);	
			}		

		},
		error : function(xhr, status, error) {
			$("#wishListNonLoggedInId").show();
			$("#wishListDetailsId").hide();
		}
	});
}

function loadDefaultWishLstForCart(productCode,ussid) {
		
	var wishListContent = "";
	var wishName = $("#defaultWishId").text();
	$("#wishListNonLoggedInId").hide();
	$("#wishListDetailsId").show();

	wishListContent = wishListContent
			+ "<tr><td><input type='text' id='defaultWishName' value='"
			+ wishName + "'/></td></td></tr>";
	$("#wishlistTbodyId").html(wishListContent); 
	$('#selectedProductCode').attr('value',productCode);
	$('#proUssid').attr('value',ussid);
}


//Added
function addToWishlistForCart(ussid,productCode)
{
	var wishName = "";
	var sizeSelected=true;
	
	if (wishListList == "") {
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
    	$("#wishlistErrorId").html("Please select a wishlist");
    	$("#wishlistErrorId").css("display","block");
    	return false;
    }
   	
	$("#wishlistErrorId").css("display","none");
    
    
	var requiredUrl = ACC.config.encodedContextPath + "/p"+ "/addToWishListInPDP";
	var dataString = 'wish='+wishName 
				    +'&product='+ productCode
				    +'&ussid='+ ussid 
				    +'&sizeSelected='+ sizeSelected;

	var entryNo = $("#entryNo").val();
	
	$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		dataType : "json",
		success : function(data) {
			if (data == true) {
				
				$("#radio_" + $("#hidWishlist").val()).prop("disabled", true);
				var msg=$('#wishlistSuccess').text() + wishName;
				$('#addedMessage').show();
				$('#addedMessage').html(msg);
				setTimeout(function() {
					  $("#addedMessage").fadeOut().empty();
					}, 5000);
				removefromCart(entryNo,wishName);
			}
		},
	})
	
	$('a.wishlist#wishlist').popover('hide');
} 
//End

function removefromCart(entryNo,wishName)
{
	$.ajax({
		contentType : "application/json; charset=utf-8",
		url :  ACC.config.encodedContextPath+"/cart/removeFromMinicart?entryNumber="+entryNo,
		dataType : "json",
		success : function(data) {
			
			var productName = $("#moveEntry_"+entryNo).parents(".item").find(".desktop .product-name > a").text();
			$("#moveEntry_"+entryNo).parents(".item").hide().empty();
			$(".product-block > li.header").append('<span>'+productName+' Moved to '+wishName+'</span>');
			
			//$('.moveToWishlistMsg').html("Item successfully moved to "+wishName);
			//$('.moveToWishlistMsg').show();
			setTimeout(function() {
				$(".product-block > li.header > span").fadeOut(6000).remove();
				//  $(".moveToWishlistMsg").fadeOut().empty();
				}, 6000);
			location.reload();
			
			
		},
		error:function(data){
			alert("error");
		}
		
	});
	
}

function gotoLogin() {
	window.open(ACC.config.encodedContextPath + "/login", "_self");
}

var wishListList = [];

// load wishlist of a particular user on opening popup
/* function LoadWishListsFromCart(data, productCode) {
	var ussid = $("#ussid").val();//modified for ussid
	var wishListContent = "";
	var wishName = "";
	$this = this;
	
	$("#wishListNonLoggedInId").hide();
	$("#wishListDetailsId").show();
	
	for ( var i in data) {
		var index=-1;
        var checkExistingUssidInWishList=false;
		var wishList = data[i];
		wishName = wishList['particularWishlistName'];
		wishListList[i] = wishName;
		var entries=wishList['ussidEntries'];
		for ( var i in entries) {
			var entry=entries[i];
			if (entry == ussid) {	
				checkExistingUssidInWishList=true;
				break;
			} 
		}
		if (checkExistingUssidInWishList) {
			index++;
            
			wishListContent = wishListContent
					+ "<tr><td><input type='radio' name='wishlistradio' id='radio_"
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
} */



function LoadWishListsFromCart(data, productCode,ussid) {
    
	// modified for ussid
	
	//var ussid = $("#ussid").val()
	
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
	$('#selectedProductCode').attr('value',productCode);
	$('#proUssid').attr('value',ussid);

}

function selectWishlist(i,productCode, ussid)
{
	$("#hidWishlist").val(i);	
}

// adding product to a wishlist
function addToWishlistFromCart() {
	var productCode = $("#product").val();
	var ussid = $("#ussid").val();
	alert("Into addToWishlistFromCart>>>"+ussid);
	var wishName =wishListList[$("#hidWishlist").val()] ;
	var requiredUrl = ACC.config.encodedContextPath + "/cart"+"/addToWishListFromCart";
	var dataString = 'wish=' + wishName + '&product=' + productCode+ '&ussid=' + ussid;
	$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		dataType : "json",
		success : function(data) {
			
			if (data == true) {				
				alert("Product Added into wishlist "+wishName);
				$("#radio_" + $("#hidWishlist").val()).prop("disabled", true);
				window.location.reload();
			}
		},
	})
}
</script>
<ul class="product-block">

   <li class="header">
   <ul>
   
   <li class="productInfo"><spring:theme code="cart.product.information"/></li>
   <li class="quantity"><spring:theme code="order.quantity"/></li>
   <li class="delivery"><spring:theme code="cart.delivery.options"/></li>
   <li class="price"><spring:theme code="cart.price"/></li>
   
   </ul>
   
   
   
   </li>
   
  
   
   <li class="moveToWishlistMsg" style="display: none;"></li>
   
   <c:if test="${not empty cartLevelDiscountModified}">
   	<li># ${cartLevelDiscountModified}</li>
   </c:if>
   
  <c:forEach items="${cartData.entries}" var="entry">
   <c:url value="${entry.product.url}" var="productUrl" />
   <input type="hidden" value="${entry.selectedSellerInformation.ussid}" id=ussid />
   <input type="hidden" value="${entry.product.code}" id="product" />
   <input type="hidden" name="hidWishlist" id="hidWishlist">
   
   
   <!-- for MSD -->
   <div>
   <c:forEach items="${entry.product.categories}" var="categoryForMSD">
   <c:if test="${fn:startsWith(categoryForMSD.code, 'MSH')}">
   	<input type="hidden" value="${categoryForMSD.code}" name="salesHierarchyCategoryMSD" />   
	</c:if>
   </c:forEach>
   <input type="hidden" value="${entry.product.rootCategory}" name="rootCategoryMSD" />   
   <input type="hidden" name="productCodeMSD" class="cartMSD"	value="${entry.product.code}" />										
	<input type="hidden" name="basePriceForMSD" class="cartMSD"	value="${entry.basePrice.formattedValue}" />
	<input type="hidden" name="subPriceForMSD"  class="cartMSD" value="${entry.basePrice.value}" />
	</div>										<!-- End MSD -->  
   <!-- End MSD -->
      
    
   <li class="item">
   <ul class="desktop">
   
   <li>
   
   <div class="product-img">
   
   <a href="${productUrl}"><product:productPrimaryImage
												product="${entry.product}" format="cartPage" /></a>
   
   </div>
   <span id="defaultWishId" style="display:none"><spring:theme code="wishlist.defaultname"/></span>
   
   <div class="product">
		                <p class="company"> </p>
		                <h3 class="product-brand-name"><a href="${entryProductUrl}">${entry.product.brand.brandname}</a></h3>
		                <h3 class="product-name">
		                <ycommerce:testId code="cart_product_name">
											<a href="${productUrl}">${entry.product.name}</a>
											<input type="hidden" name="productArrayForIA" value="${entry.product.code}"/>
						</ycommerce:testId>
			                </h3>
			              
			             <!-- TISEE-246   
		                <p class="item-info">
		                  <span><ycommerce:testId code="cart_product_colour">
										<spring:theme code="product.sellersname"/>&nbsp;${entry.selectedSellerInformation.sellername}
										</ycommerce:testId></span>
		                </p>
		                -->
		                <p class="item-info">
			                <c:forEach items="${fullfillmentData}" var="fullfillmentData">
								<c:if test="${fullfillmentData.key == entry.entryNumber}">
									<c:set var="fulfilmentValue" value="${fn:toLowerCase(fullfillmentData.value)}"> </c:set>
									<c:choose>
										<c:when test="${fulfilmentValue eq 'tship'}">
												<div class="name">
													<spring:theme code="mpl.myBag.fulfillment"/>&nbsp;<spring:theme code="product.default.fulfillmentType"></spring:theme>
												</div>
										</c:when>
										<c:otherwise>
												<div class="name">
													<spring:theme code="mpl.myBag.fulfillment" /> &nbsp; ${entry.selectedSellerInformation.sellername} 
												</div>	
										</c:otherwise>
									</c:choose>
								</c:if>
							</c:forEach>
		                </p>
		                
		                <c:if test="${not empty entry.product.size}">
		                 <p class="size"><ycommerce:testId code="cart_product_size">
											<spring:theme code="product.variant.size"/>&nbsp;${entry.product.size}
										</ycommerce:testId>
										</p>
						</c:if>
		              </div>
		              
		                
		              <ul class="item-edit-details">
		              	<c:if test="${entry.updateable}">
							<ycommerce:testId code="cart_product_removeProduct">
		                  		<li> 
			              			<a class="remove-entry-button" id="removeEntry_${entry.entryNumber}_${entry.selectedSellerInformation.ussid}"><spring:theme code="cart.remove"/></a>
			              		</li>
			              </ycommerce:testId>
			          	</c:if>
			           	<c:if test="${!entry.giveAway}">
			           		<li>
                    			 <span id="addedMessage" style="display:none"></span>
                    	 	 	<a class="move-to-wishlist-button cart_move_wishlist" id="moveEntry_${entry.entryNumber}" onclick="openPopFromCart('${entry.entryNumber}','${entry.product.code}','${entry.selectedSellerInformation.ussid}');" data-toggle="popover" data-placement='bottom'><spring:theme code="basket.move.to.wishlist"/></a>
                    		</li>
			           	</c:if>
                      </ul>
   
   </li>
   <!-- TISUTO-124 -->
    <c:choose>
		<c:when test="${entry.giveAway}">
				<li id ="${entry.selectedSellerInformation.ussid}_qty_${entry.giveAway}" class="qty">
		</c:when>
		<c:otherwise>
				<li id ="${entry.selectedSellerInformation.ussid}_qty" class="qty">
		</c:otherwise>	
	</c:choose>		
   <!-- TISUTO-124 -->
    
    <c:url value="/cart/update" var="cartUpdateFormAction" />
        
   <c:choose>
		<c:when test="${entry.giveAway}">
			<c:set var="updateFormId" value="updateCartForm${entry.selectedSellerInformation.ussid}_${entry.giveAway}" />
		</c:when>
		<c:otherwise>
			<c:set var="updateFormId" value="updateCartForm${entry.selectedSellerInformation.ussid}" />
		</c:otherwise>	
  </c:choose>				
    
    	<form:form id="${updateFormId}" action="${cartUpdateFormAction}" method="post" commandName="updateQuantityForm${entry.entryNumber}">
				<input type="hidden" name="entryNumber"		value="${entry.entryNumber}" />
				<input type="hidden" name="productCode"		value="${entry.product.code}" />
				<input type="hidden" name="initialQuantity" value="${entry.quantity}" />
				
				<ycommerce:testId code="cart_product_quantity">
					<c:set var="priceBase" value="${entry.basePrice.formattedValue}" />
					<c:set var="subPrice" value="${entry.basePrice.value}" />
					<fmt:parseNumber var="price" type="number" value="${subPrice}" />
											
					<c:choose>
							<c:when test="${price lt 0.1 && entry.giveAway}">
								<form:select path="quantity" id="quantity_${entry.selectedSellerInformation.ussid}_${entry.giveAway}"	cssClass="update-entry-quantity-input" disabled="true" onchange="updateCart(this.id);">
									<c:forEach items="${configuredQuantityList}"
										var="quantity">
										<form:option value="${quantity}"></form:option>
									</c:forEach>
								</form:select>
							</c:when>
							<c:when test="${price lt 0.1}">
								<form:select path="quantity" id="quantity_${entry.selectedSellerInformation.ussid}"	cssClass="update-entry-quantity-input" disabled="true" onchange="updateCart(this.id);">
									<c:forEach items="${configuredQuantityList}"
										var="quantity">
										<form:option value="${quantity}"></form:option>
									</c:forEach>
								</form:select>
							</c:when>
							<c:otherwise>
								<form:select path="quantity" id="quantity_${entry.selectedSellerInformation.ussid}"	cssClass="update-entry-quantity-input" onchange="updateCart(this.id);">
									<c:forEach items="${configuredQuantityList}"
										var="quantity">
										<form:option value="${quantity}"></form:option>
									</c:forEach>
								</form:select>
							</c:otherwise>
					</c:choose>
				</ycommerce:testId>
			</form:form>
		</li>
		            
		            
	            	<c:choose>
	            		<c:when test="${entry.giveAway}"> <!-- For Freebie item delivery mode will no tbe displayed -->
	            			<li id ="${entry.selectedSellerInformation.ussid}_li_${entry.giveAway}" class="delivery">
	            				<ul id="${entry.selectedSellerInformation.ussid}_${entry.giveAway}">	
						</c:when>
						<c:otherwise>
							<li id ="${entry.selectedSellerInformation.ussid}_li" class="delivery">
							<p class="mobile-delivery"><spring:theme code="basket.delivery.options"/></p>
								<ul id="${entry.selectedSellerInformation.ussid}">
						</c:otherwise>
					</c:choose>	
	             	 
		            <c:choose>
		            			<c:when test="${entry.giveAway}"> <!-- For Freebie item delivery mode will no tbe displayed -->
								</c:when>
								
								<c:when test="${empty selectedPincode ||  fn:length(selectedPincode) == 0  }"> 
									<spring:theme code="cart.pincode.blank"/>
								</c:when>
								
		            			<c:otherwise>
		            		    	<c:if
										test="${not empty productDeliveryModeMap}">
										<c:forEach items="${productDeliveryModeMap}"
											var="productDeliveryMode">
											
											<c:choose>												
												<c:when	test="${productDeliveryMode.key == entry.entryNumber}">
													<c:if test="${productDeliveryMode.key == entry.entryNumber}">
														<c:forEach items="${productDeliveryMode.value}"
															var="productDeliveryModeMapValue">
															<li class="method${ productDeliveryModeMapValue.name}">${ productDeliveryModeMapValue.name}</li>
														</c:forEach>
													</c:if>
												</c:when>
											</c:choose>
										</c:forEach>
									</c:if>
		            		    </c:otherwise>
							</c:choose>
		            	</ul>
		            </li>
		            
		          <li class="price">
					<ul>
						<c:set var="quantity" value="${entry.quantity}"/>
						<c:set var="subPrice" value="${entry.basePrice.value}" />
						<fmt:parseNumber var="price" type="number" value="${subPrice}" />
					  	<c:set var="tot_price" value="${quantity * price}" />
					  	
						<ycommerce:testId code="cart_totalProductPrice_label">
								<c:choose>
									<c:when test="${entry.giveAway}"> <!-- For Freebie item price will be shown as free -->
										<spring:theme code="text.free" text="FREE"/>
									</c:when>
									<c:otherwise>
										<c:choose>
										<c:when test="${entry.isBOGOapplied eq true}">
											<del>
												 <format:price priceData="${strikeoffprice}" displayFreeForZero="true" />
											</del>
											<c:choose>
												 <c:when test="${entry.totalPrice.value<'1.00'}">
													<span>Free</span>
												</c:when>
												<%-- TISEE-936
													 <c:otherwise> <span> <format:price priceData="${entry.totalPrice}" /></span> 	</c:otherwise> 
												 TISEE-936 --%>
											</c:choose>
											<%-- TISEE-936 
										   	<span><format:price priceData="${entry.productLevelDisc}" displayFreeForZero="true"/></span><span class="discount-off">Off</span> 
											--%>
										</c:when>
										<c:when test="${entry.basePrice.formattedValue == entry.totalPrice.formattedValue}">
												<span><format:price priceData="${entry.totalPrice}"/></span>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${entry.basePrice.formattedValue == entry.totalPrice.formattedValue}">
												<span><format:price priceData="${entry.totalPrice}"/></span>
											</c:when>
													<c:otherwise>
														<c:if test="${entry.basePrice.formattedValue != entry.totalPrice.formattedValue}">
															<c:forEach items="${basePriceMap}" var="baseprice">
																<c:choose>	
																	<c:when	test="${baseprice.key == entry.entryNumber}">
																		<c:if test="${baseprice.value.formattedValue != entry.totalPrice.formattedValue||not empty entry.cartLevelDisc}">
																		 	<li><del> <format:price priceData="${baseprice.value}" displayFreeForZero="true" /></del></li>
																		</c:if>
																	</c:when>
																</c:choose>
															</c:forEach>
															<c:if test="${empty entry.cartLevelDisc && empty entry.productLevelDisc}">
															 <span><format:price priceData="${entry.totalPrice}"/></span>
															 </c:if>
														</c:if>
													</c:otherwise>
								
												</c:choose>
											</c:otherwise>			
										</c:choose>	
									</c:otherwise>
								</c:choose>
								<%-- <c:if	test="${empty itemLevelDiscount}">
									<c:forEach items="${priceModified}" var="priceModified">
											<c:if	test="${priceModified.key == entry.entryNumber}"><br/>
												<spring:theme code="order.price.change"/><li><del>${priceModified.value}</del></li>
											</c:if>
									</c:forEach>
								</c:if> --%>
								<%-- <c:if test="${not empty entry.cartLevelDisc && not empty entry.productLevelDisc}">
								<format:price priceData="${entry.totalSalePrice}"/>
								</c:if> --%>
								<c:choose>
								<c:when test="${not empty entry.cartLevelDisc}">
								<c:choose>
								<c:when test="${not empty entry.productLevelDisc && not empty entry.prodLevelPercentage}">
								<span class="off-bag">${entry.prodLevelPercentage}<spring:theme code="off.item.percentage"/><del><format:price priceData="${entry.netSellingPrice}"/></del></span>
								</c:when>
								<c:otherwise>
								<c:if test="${not empty entry.productLevelDisc}">
								<span class="off-bag"><format:price priceData="${entry.productLevelDisc}"/><spring:theme code="off.item"/><del><format:price priceData="${entry.netSellingPrice}"/></del></span>
								</c:if>
								</c:otherwise>
								</c:choose>
								</c:when>
								<c:otherwise>
								<c:choose>
								<c:when test="${not empty entry.productLevelDisc && not empty entry.prodLevelPercentage}">
								<span class="off-bag">${entry.prodLevelPercentage}<spring:theme code="off.item.percentage"/><format:price priceData="${entry.netSellingPrice}"/></span>
								</c:when>
								<c:otherwise>
								<c:if test="${not empty entry.productLevelDisc}">
								<span class="off-bag"><format:price priceData="${entry.productLevelDisc}"/><spring:theme code="off.item"/><format:price priceData="${entry.netSellingPrice}"/></span>
								</c:if>
								</c:otherwise>
								</c:choose>
								</c:otherwise>
								</c:choose>
								<c:if test="${not empty entry.cartLevelDisc}">
							<c:choose>
								<c:when test="${not empty entry.cartLevelDisc && not empty entry.cartLevelPercentage}">
								<c:if test="${entry.amountAfterAllDisc.value gt 0.1}">
								<span class="off-bag">${entry.cartLevelPercentage}<spring:theme code="off.bag.percentage"/><format:price priceData="${entry.amountAfterAllDisc}"/></span>
								</c:if>
								</c:when>
								<c:otherwise>
								<c:if test="${entry.amountAfterAllDisc.value gt 0.1}">
								<span class="off-bag"><format:price priceData="${entry.cartLevelDisc}"/><spring:theme code="off.bag"/><format:price priceData="${entry.amountAfterAllDisc}"/></span>
								</c:if>
								</c:otherwise>
							</c:choose></c:if>
								<%--  <c:if test="${not empty entry.cartLevelDisc}">
									<span class="off-bag"><format:price priceData="${entry.cartLevelDisc}"/><spring:theme code="off.bag"/><format:price priceData="${entry.amountAfterAllDisc}"/></span>
								</c:if>  --%>
							</ycommerce:testId>
					</ul>
				  </li>  
   
   </ul>
   <ul>
		<c:forEach items="${promoModified}" var="promoModified">
			<c:choose>
				<c:when	test="${promoModified.key == entry.entryNumber}">
					<li># ${promoModified.value}</li>
				</c:when>
			</c:choose>
		</c:forEach>
		<c:if	test="${empty promoModified}">
			<c:forEach items="${priceModifiedMssg}" var="priceModifiedMssg">
				<c:choose>
					<c:when	test="${priceModifiedMssg.key == entry.entryNumber}">
						<li># ${priceModifiedMssg.value}</li>
					</c:when>
				</c:choose>
			</c:forEach>
		</c:if>
	</ul>
   
   </li>
   	</c:forEach>
</ul>
   	

<div class="add-to-wishlist-container">
<form>
	<input type="hidden" value="${product.code}" id="product" />
	<input type="hidden" id="entryNo" />
	<div id="wishListDetailsId" class="other-sellers" style="display: none">
		<h3 class="title-popover">Select Wishlist:</h3>
		<table class="other-sellers-table add-to-wishlist-popover-table">
			<tbody id="wishlistTbodyId"> </tbody>
		</table>

		<input type="hidden" name="hidWishlist" id="hidWishlist">
		<p id='wishlistErrorId' style="display: none ; color:red ;"> </p>
		<input type="hidden" id="proUssid" value=""/>
		<input type="hidden" id="selectedProductCode" value=""/>
		<button type='button' onclick="addToWishlistForCart($('#proUssid').val() , $('#selectedProductCode').val() )" name='saveToWishlist' id='saveToWishlist_${entry.entryNumber}' class="savetowishlistbutton" >
			<spring:theme code="product.wishlistBt"/>
		</button>
	</div>

	<div id="wishListNonLoggedInId" style="display: none"><spring:theme code="product.wishListNonLoggedIn"/></div>
	
</form>
</div>
								
								
								 <form id="modalForm">
									<input type="hidden" value="${entry.product.code}" id="product" />
									<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" >
										<div class="modal-dialog" style="  background-color: white;">
											<!-- Modal content-->
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal"aria-hidden="true">&times;</button>
												<h4 class="modal-title" id="myModalLabel">
													<b> <spring:theme code="cart.modal.my.wishlist"/></b>
												</h4>
											</div>
											<div class="modal-body" id="modelId"></div>
											<div class="modal-footer">												
												<button type="button" class="btn btn-default"data-dismiss="modal"><spring:theme code="popup.close"/></button>
											</div>
										</div>
									</div>
								</form>	 
								
								<%--	

							</td>
							<td headers="header3" style="  width: 12%;">
								<div class="qty" style="  margin-left: 10px;  margin-right: 20px;">
									<c:url value="/cart/update" var="cartUpdateFormAction" />
									<form:form id="updateCartForm${entry.entryNumber}"
										action="${cartUpdateFormAction}" method="post"
										commandName="updateQuantityForm${entry.entryNumber}">
										<input type="hidden" name="entryNumber"
											value="${entry.entryNumber}" />
										<input type="hidden" name="productCode"
											value="${entry.product.code}" />
										<input type="hidden" name="initialQuantity"
											value="${entry.quantity}" />
										<input type="hidden" name="initialQuantity"
											value="${entry.quantity}" />
										<ycommerce:testId code="cart_product_quantity">
										<c:set var="priceBase" value="${entry.basePrice.formattedValue}" />
											<c:set var="subPrice" value="${fn:substring(priceBase, 3, 14)}" />
											<fmt:parseNumber var="price" type="number" value="${subPrice}" />
											<c:choose>
												<c:when test="${price lt 0.1}">
													<form:select path="quantity" id="quantity_${entry.entryNumber}"	cssClass="update-entry-quantity-input" disabled="true">
														<c:forEach items="${configuredQuantityList}"
															var="quantity">
															<form:option value="${quantity}"></form:option>
														</c:forEach>
													</form:select>
												</c:when>
												<c:otherwise>
													<form:select path="quantity" id="quantity_${entry.entryNumber}"	cssClass="update-entry-quantity-input">
														<c:forEach items="${configuredQuantityList}"
															var="quantity">
															<form:option value="${quantity}"></form:option>
														</c:forEach>
													</form:select>
												</c:otherwise>
											</c:choose>




										</ycommerce:testId>
									</form:form>
								</div>
							</td>
							<td headers="header4" style="  width: 25%;">
								<div style="  margin-left: 10px;  margin-right: 20px;">

									<c:if
										test="${not empty defaultPinCode and not empty productDeliveryModeMap}">
										<c:forEach items="${productDeliveryModeMap}"
											var="productDeliveryModeMap">
											<c:if
												test="${productDeliveryModeMap.key == entry.entryNumber}">
												<c:forEach items="${productDeliveryModeMap.value}"
													var="productDeliveryModeMapValue">
													<div class="checout-delivery-options"><span class="glyphicon glyphicon-home" style="  margin-right: 5px; color:rgb(0,201,232);"></span>${ productDeliveryModeMapValue}</div>
												</c:forEach>
											</c:if>
										</c:forEach>
									</c:if>
									
									<c:if test="${defaultPinCode eq emtpy}">Please  enter pincode to find the delivery modes</c:if>
								</div>
							</td>

							<td headers="header5" style="  width: 18%;">
								<div class="item-price" style="  margin-left: 10px;  margin-right: 20px;">
									<ycommerce:testId code="cart_totalProductPrice_label">
									<c:if test="${entry.basePrice.formattedValue == entry.totalPrice.formattedValue}"><format:price priceData="${entry.totalPrice}"/></c:if>
									<c:if test="${entry.basePrice.formattedValue != entry.totalPrice.formattedValue}">
									<del><format:price priceData="${entry.basePrice}" displayFreeForZero="true" /></del>
											<format:price priceData="${entry.totalPrice}"/></c:if>
									</ycommerce:testId>
									
								</div>
							</td>
						</tr>

					</c:forEach>

				</tbody>

			</table>


			<div class="col-md-6">
                    <div class="pickup">
                        <c:choose>
                             <c:when test="${entry.product.purchasable}">
                             	<div class="radio-column">
                           			<c:if test="${not empty entryStock and entryStock ne 'outOfStock'}">
                                        <c:if test="${entry.deliveryPointOfService eq null or not entry.product.availableForPickup}">
									   		<label for="pick0_${entry.entryNumber}">
									   		<span class="glyphicon glyphicon-gift text-gray"></span> 
									   		<span class="name"><spring:theme code="basket.page.shipping.ship"/></span>
									   		</label>
							    		</c:if>
									</c:if>
								    <c:if test="${not empty entry.deliveryPointOfService.name}">
                                        <label for="pick1_${entry.entryNumber}"> 
                                            <span class="glyphicon glyphicon-home"></span> 
                                            <span class="name"><spring:theme code="basket.page.shipping.pickup"/></span>
                                        </label>
								    </c:if>
                                </div>                
                                
                                <div class="store-column">
                                    <c:choose>
                                        <c:when test="${entry.product.availableForPickup}">
                                            <c:choose>
                                             <c:when test="${not empty entry.deliveryPointOfService.name}">
                                                <div class="store-name">${entry.deliveryPointOfService.name}</div>
                                             </c:when>
                                             <c:otherwise>
                                                 <div class="store-name"></div>
                                             </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
               
               <!--Added the below code to get mobile view for demo purpose   --> 
                <c:forEach items="${cartData.entries}" var="entry">
						<c:url value="${entry.product.url}" var="productUrl" />
						
						
						
                <div class="exp" style="">
                	
									<div class="exp-thumb">
										<a href="${productUrl}"><product:productPrimaryImage
												product="${entry.product}" format="cartPage" /></a>
									</div>
								<div class="exp-details-wrapper">
									<div class="exp-details" >
										<ycommerce:testId code="cart_product_name">
											<a href="${productUrl}"><div class="name">${entry.product.name}</div></a>
										</ycommerce:testId>
										
										<c:forEach items="${fullfillmentData}"
											var="fullfillmentData">
											
											
											<c:if
												test="${fullfillmentData.key == entry.entryNumber}">
												<c:forEach items="${fullfillmentData.value}"
													var="fullfillmentData">
										<div class="name"><spring:theme code="mpl.myBag.fulfillment" />${fullfillmentData}</div>
												</c:forEach>
											</c:if>
										</c:forEach>
									
										
													
										<c:if
											test="${ycommerce:doesPotentialPromotionExistForOrderEntry(cartData, entry.entryNumber)}">
											<c:forEach items="${cartData.potentialProductPromotions}"
												var="promotion">
												<c:set var="displayed" value="false" />
												<c:forEach items="${promotion.consumedEntries}"
													var="consumedEntry">
													<c:if
														test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber && not empty promotion.description}">
														<c:set var="displayed" value="true" />

														<div class="promo">
															<ycommerce:testId code="cart_potentialPromotion_label">
		                                             ${promotion.description}
		                                         </ycommerce:testId>
														</div>
													</c:if>
												</c:forEach>
											</c:forEach>
										</c:if>
										<c:if
											test="${ycommerce:doesAppliedPromotionExistForOrderEntry(cartData, entry.entryNumber)}">
											<c:forEach items="${cartData.appliedProductPromotions}"
												var="promotion">
												<c:set var="displayed" value="false" />
												<c:forEach items="${promotion.consumedEntries}"
													var="consumedEntry">
													<c:if
														test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
														<c:set var="displayed" value="true" />
														<div class="promo">
															<ycommerce:testId code="cart_appliedPromotion_label">
	                                            ${promotion.description}
	                                        </ycommerce:testId>
														</div>
													</c:if>
												</c:forEach>
											</c:forEach>
										</c:if>

										<c:set var="entryStock"
											value="${entry.product.stock.stockLevelStatus.code}" />

										<c:forEach items="${entry.product.baseOptions}" var="option">
											<c:if
												test="${not empty option.selected and option.selected.url eq entry.product.url}">
												<c:forEach
													items="${option.selected.variantOptionQualifiers}"
													var="selectedOption">
													<div>
														<strong>${selectedOption.name}:</strong> <span>${selectedOption.value}</span>
													</div>
													<c:set var="entryStock"
														value="${option.selected.stock.stockLevelStatus.code}" />
												</c:forEach>
											</c:if>
										</c:forEach>

										<div>
											<c:choose>
												<c:when
													test="${not empty entryStock and entryStock ne 'outOfStock'}">
													<spring:theme code="basket.page.availability" />: <span
														class="stock"><spring:theme
															code="product.variants.in.stock" /></span>
												</c:when>
												<c:otherwise>
													<spring:theme code="basket.page.availability" />: <span
														class="stock"><spring:theme
															code="product.variants.out.of.stock" /></span>
												</c:otherwise>
											</c:choose>
										</div>



									
									<form:form
										action="${request.contextPath}/my-account/${entry.product.code}/wishList"
										method="GET">
										<a style="padding-left: 15px;"
											href="${request.contextPath}/my-account/${entry.product.code}/wishList"><spring:theme
												code="basket.move.to.wishlist" /></a>
										<button  style="background-color: #1a618b; border-color: #1a618b;color: white;"  id="wishlist" type="submit"></button>
									</form:form>
									<c:if
										test="${not empty sellerInfoMap}">
										<c:forEach items="${sellerInfoMap}"
											var="sellerInfoMap">
											<c:if
												test="${sellerInfoMap.key == entry.entryNumber}">
												<c:forEach items="${sellerInfoMap.value}"
													var="sellerInfoMapValue">
													<div><span><b>Seller Name : </b></span>${sellerInfoMapValue}</div>
												</c:forEach>
												
												<div class="size"><b>Size:${entry.product.size}</b></div>
										<div class="colour"><b>Color:${entry.product.colour}</b></div>
											</c:if>
										</c:forEach>
									</c:if>
									
									<div class="exp-item-price" style=" margin-top: 5px;">
									<ycommerce:testId code="cart_totalProductPrice_label">
										<format:price priceData="${entry.totalPrice}"
											displayFreeForZero="true" />
									</ycommerce:testId>
									
								</div>
									
								</div>
					
						
           
           			
           						
								
								<div>
								
								<div style="margin-bottom: 5px;">Available Delivery Options:</div>

									<c:if
										test="${not empty defaultPinCode and not empty productDeliveryModeMap}">
										<c:forEach items="${productDeliveryModeMap}"
											var="productDeliveryModeMap">
											<c:if
												test="${productDeliveryModeMap.key == entry.entryNumber}">
												<c:forEach items="${productDeliveryModeMap.value}"
													var="productDeliveryModeMapValue">
													<div class="checout-delivery-options"><span class="glyphicon glyphicon-home" style="  margin-right: 5px; color:rgb(0,201,232);"></span>${ productDeliveryModeMapValue}</div>
												</c:forEach>
											</c:if>
										</c:forEach>
									</c:if>
									
									<c:if test="${defaultPinCode eq emtpy}">Please  enter pincode to find the delivery modes</c:if>
								</div>
           			 
										</div>
							<div class="modify-order" style="  height: 50px; clear:both;   padding: 0 10px;">
							
								<div class="modify-order-options" style=" padding-top: 4px;"><span style="display: inline-block;float: left; margin-right: 8px;">Qty:</span>
									<c:url value="/cart/update" var="cartUpdateFormAction" />
									<div style="  width: 60%; float: left; display: inline-block;">
									<form:form id="updateCartForm${entry.entryNumber}"
										action="${cartUpdateFormAction}" method="post"
										commandName="updateQuantityForm${entry.entryNumber}">
										<input type="hidden" name="entryNumber"
											value="${entry.entryNumber}" />
										<input type="hidden" name="productCode"
											value="${entry.product.code}" />
										<input type="hidden" name="initialQuantity"
											value="${entry.quantity}" />
										<input type="hidden" name="initialQuantity"
											value="${entry.quantity}" />
										<ycommerce:testId code="cart_product_quantity">
											
												<form:select  path="quantity" id="quantity_${entry.entryNumber}" cssClass="update-entry-quantity-input" >
												
												<c:forEach items="${configuredQuantityList}"  var="quantity">
												<form:option value="${quantity}"  ></form:option>
												
												</c:forEach>
												</form:select>
												
										</ycommerce:testId>
									</form:form>
									</div>
								</div>		
								
								<div  class="modify-order-options">
									<c:if test="${entry.updateable}">
										<ycommerce:testId code="cart_product_removeProduct">
											<button class="btn btn-primary remove-entry-button"
												id="removeEntry_${entry.entryNumber}">
												<span>Remove</span>
											</button>
											
											
										</ycommerce:testId>

									</c:if>
								
								</div>
									<!-- Move to Wish List Button -->
								<div class="modify-order-options">
									<button class="btn btn-primary move-to-wishlist-button"
													id="moveEntry_${entry.entryNumber}" onclick="openPopFromCart();" data-toggle="modal" data-target="#myModal">
										<span>Move To Wishlist</span>
									</button>
								</div>
									<!-- End call Move to Wish List -->
									
									
									--%>
									
								 <form id="modalForm">
									<input type="hidden" value="${entry.product.code}" id="product" />
									<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" >
										<div class="modal-dialog" style="  background-color: white;">
											<!-- Modal content-->
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal"aria-hidden="true">&times;</button>
												<h4 class="modal-title" id="myModalLabel">
													<b> <spring:theme code="cart.modal.my.wishlist"/></b>
												</h4>
											</div>
											<div class="modal-body" id="modelId"></div>
											<div class="modal-footer">												
												<button type="button" class="btn btn-default"data-dismiss="modal"><spring:theme code="popup.close"/></button>
											</div>
										</div>
									</div>
								</form>		 
								<%-- 
										</div>
										<!-- <div style="clear:both;"></div> -->
                </div>
                
                </c:forEach>
                <!--Added the above code to get mobile view for demo purpose   --> 
           
        </li>

 --%>


<storepickup:pickupStorePopup />

