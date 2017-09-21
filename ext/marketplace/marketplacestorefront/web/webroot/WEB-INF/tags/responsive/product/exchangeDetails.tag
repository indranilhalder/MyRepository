<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<%-- JS Moved to acc.productDetail.js --%>
<c:set var="l3code" value="${product.level3CategoryCode}"/>
<c:set var="l3name" value="${product.level3CategoryName}"/>
<c:set var="isExchangeavailable" value="true"/>
<c:if test="${empty l3code || empty l3code}">
<c:set var="isExchangeavailable" value="false"/>
</c:if>

<c:if test="${isExchangeavailable ne false}">
<div class="Exchange Exchange_wrapper" id="exchangeStickerId">
<p  onclick="" >
	<spring:theme code="marketplace.exchange" />
	
				
		<input id="l3code" type="hidden" value="${l3code}" />
		<input id="l3name" type="hidden" value="${l3name}" />
		<input id="productcode" type="hidden" value="${product.code}"/>
</p>

<div id="ExchangeModal-content" class="modal-content">
<span class="Close"></span>
  <div class="modal-header">   
    <h3 class="modal-title" id="myModalLabel"> 
    	<span class="Exchange-tableTitle"><spring:theme code="marketplace.exchange"/></span>
    
    	<br>    
    	
    	<span class="Exchange-subTitle"><spring:theme code="marketplace.exchange.prepincodecheck"/></span>	
    </h3>  	<!-- UF-48 -->	 
  </div>
  
  
   
  <div class="modal-body" id="modelId">

	<div class="inline-form">
	
		 <c:choose>
		 	<c:when test="${not empty pincode}">
			<input id="pinExc" type="text" value="${pincode}" maxlength="6" onkeypress="return isNum(event)"/>
		    </c:when>
		    <c:otherwise>
		    	<input id="pinExc" type="text" placeholder="Enter pincode" maxlength="6" onkeypress="return isNum(event)"/>
		    </c:otherwise>
		 </c:choose>
	   <!-- TISEE-6552 fix  -->
		<button class="orange submit" id="pdpPincodeCheckExchnage"><spring:theme code="text.submit"/></button>
		
		<button class="gray submitDList" id="pdpPincodeCheckDListExchange" style="display:none;"><spring:theme code="text.submit"/></button>
		    <div class="pincodeErrorMsg">
		<!-- <span class="pincodeErrorMsg"> -->
			<span id="emptyPinExc" style="display:none;color:#ff1c47"><spring:theme code="product.empty.pincode"/></span>
			<span id="wrongPinExc" style="display:none;color:#ff1c47"><spring:theme code="pincode.invalid"/></span>
			<span id="unsevisablePinExc" style="display:none;color:#ff1c47"><spring:theme code="pincode.unableprocess.exchange"/></span>
			<span id="exPinnotserviceable" style="display:none;color:#ff1c47">Sorry!! exchange offer is not serviceable</span>
			<span id="serviceablePinExc" style="display:none;color:#339933"><spring:theme code="pincode.serviceable.exchange"/></span> <!-- Changes for TISPRM-20,65 -->
			<span id="unableprocessPinExc" style="display:none;color:#ff1c47"><spring:theme code="pincode.unableprocess.exchange"/></span>
		<!-- </span> -->
	</div>
	</div>
        
<div id="exchangeDetails" style="display:none">
  <div class="half">
  <label for="l3">Product Category</label>
  <input type="text" id="l3" value="${l3name}" disabled="disabled">
  </div>
   <div class="half">
   <label id="lbrand" for="brandExchange">Brand</label>
 <input type="text" id="brandExchange">
	</div>
	 <div class="half">
	 <label id="ll4select" for="l4select">Type</label>
 <select name="l4select" id="l4select" onchange="changeWorking(this.value);">
 <option value="" disabled selected>Select</option>
 </select>
 </div>
  <div class="half">
  <label id="lactiveselect" for="activeselect">Working Condition</label>
 <select name="activeselect" id="activeselect" onchange="changePrice(this.value);">
  <option value="" disabled selected>Select</option>
</select>
</div>

<div id="submit&Condition">
<label id="error_tc" for="exchange_tc"></label>
<input type="checkbox" name="terms&condition" id="exchange_tc" >
<label for="exchange_tc"><span>I agree to the</span></label>
	<cms:pageSlot position="ExchangeSlot" var="component">
				<cms:component component="${component}" />
			</cms:pageSlot>	
			<br>
			<input type="button" id="submitExchange" onClick="onSubmitExc();" value="Submit">
</div>
</div>

<div id="couponValue" style="display:none">
<div class="couponMsg">
<spring:theme code="marketplace.exchange.success"/>
<br>
 <spring:theme code="marketplace.exchange.couponWorth"/><span id="priceselect"></span><span> (Applicable on your next purchase) will be issued within 7 days of successful exchange</span>
 </div>
<!--   <input type="button" value="Add to Bag" onclick="generateExchnangId()"> -->
 
<form:form method="post" id="addToCartExchange" class="add_to_cart_form" action="#">
		
	<c:if test="${product.purchasable}">
	
	<input type="hidden" maxlength="3" size="1" name="qty" class="qty js-qty-selector-input" value="1" />
  	<!-- <input type="hidden" maxlength="3" size="1" id="pinCodeChecked"
		name="pinCodeChecked" value="false"> -->
	</c:if>
	<input type="hidden" maxlength="3" size="1" id="exStock" name="stock" value="" />
	<input type="hidden" name="productCodePost" id="productCode" value="${product.code}" /> 
	<input type="hidden" name="wishlistNamePost" id="wishlistNamePost" value="N" />
	<input type="hidden" maxlength="3" size=""  name="ussid" id="ussidExchange" value="" />
	<input type="hidden" maxlength="14" size=""  name="l3" id="l3Exchange" value="${product.level3CategoryCode}" />
	<input type="hidden" maxlength="50" size=""  name="exchangeParam" id="isWorkingExchange" value="" />
	<input type="hidden" maxlength="50" size=""  name="brandParam" id="brandExchangeParam" value="" />
	<input type="hidden" maxlength="6" size=""  name="pinParam" id="pincodeExchangeParam" value="" />
	
   		<span id="addToCartExchangeExceededmaxqtyExc" style="display: none" class="exchange-message">
   		<spring:theme code="product.addtocart.exchange.qty.error"/>
		</span>
   		<span id="addToCartExchangenoInventorySize" style="display: none" class="no_inventory exchange-message"><p class="inventory">
			<font color="#ff1c47"><spring:theme code="Product.outofinventory" /></font>
		</p></span>
		<span id="addToCartExchangeexcedeInventorySize" style="display: none" class="exchange-message"><p class="inventory">
			<font color="#ff1c47">Please decrease the quantity</font>
		</p></span>
		
		<span id="addToCartExchangeTitleaddtobag" style="display: none" class="exchange-message"><p class="inventory">
			<spring:theme code="product.addtocart.success"/>
		</p></span>
		<span id="addToCartExchangeTitleaddtobagerror" style="display: none" class="exchange-message"><p class="inventory">
			<spring:theme code="product.error"/>
		</p></span>
		<span id="addToCartExchangeTitlebagtofull" style="display: none" class="exchange-message"><p class="inventory">
			<spring:theme code="product.addtocart.aboutfull"/>
		</p></span>
		<span id="addToCartExchangeTitlebagfull" style="display: none" class="exchange-message"><p class="inventory">
			<spring:theme code="product.bag"/>
		</p></span>
		<span id="pinNotServicableExchange" style="display: none" class="exchange-message">
			<font color="#ff1c47">We're sorry. We don't service this pin code currently. Would you like to try entering another pin code that also works for you?</font>
		</span>
		<span id="addToCartExchangeTitleoutOfStockId" style="display: none"><p class="inventory">
			<span id="outOfStockText" class="exchange-message">
			<spring:theme code="product.product.outOfStock" />
			</span>
		<input type="button" onClick="openPop_SizeGuide();" id="add_to_wishlist-sizeguide" class="wishlist" data-toggle="popover" data-placement="bottom" value="<spring:theme code="text.add.to.wishlist"/>"/>
			<!-- <font color="#ff1c47">Product is out of stock for the selected size</font> -->
		</p></span>
			
	<span id="sizeSelectedSizeGuide"   class="exchange-message" style="display: none;color:#ff1c47"><spring:theme code="variant.pleaseselectsize"/></span>
	<span id="addToCartButtonIdExc">
	<!-- <span id="addToCartFormSizeTitleSuccess"></span> -->
	<button style="display: block;"
			id="addToCartButtonExc" type="button"
			class="btn-block js-add-to-cart">
		<spring:theme code="basket.add.to.basket.exchange" />
	</button>
	</span>
	<span id="addToCartSizeGuideTitleSuccess"></span>
</form:form>

		  </div> 
    
  </div>
</div>
</div>
</c:if>
 <!--TPR- 5193 -->
 <c:choose>
<c:when test="${isExchangeavailable ne false}">
   <input id="tealiumExchangeVar" type="hidden" value="available"/>
 </c:when>
  <c:otherwise>
 <input id="tealiumExchangeVar" type="hidden" value="notAvailable"/>
</c:otherwise>
</c:choose>
