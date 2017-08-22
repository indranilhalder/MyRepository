<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>

<%-- ${welcome_message} --%>
<c:if test="${not empty cartData.entries}">
    <c:url value="/cart/checkout" var="checkoutUrl" scope="session"/>
    <c:url value="${continueUrl}" var="continueShoppingUrl" scope="session"/>
    <%-- <input type="hidden" id="isServicableId" value="${isServicable}"> Not required --%>
    <c:set var="showTax" value="false"/>
    <c:set var="userName" value="${user.firstName}"/>
    <div class="MyBag-top-section">
    <!-- TPR3780 STARTS HERE -->
												<c:if test="${priceNotificationUpdateStatus!= null}">
												<p class="disclaimer-txt">
												<spring:theme code="cart.price.change.notification"></spring:theme>&nbsp;${cartData.totalPrice.formattedValue}
												</p>
												</c:if>
    <!-- TPR3780 ENDS HERE -->
    <div class="MyBag-buttons">
	<%-- <h1 class="MyBagHeadingDesktop" ><spring:theme code="mpl.myBag" /><span id="mybagcnt"></span></h1> --%>		<!-- commented for UF-62 -->
	<p class="desk-view">${welcome_message}</p>
	
	<%-- <c:choose>
	<c:when test="${isLoggedIn eq true}">
	<p class="desk-view"><spring:theme code="mpl.myBag.hi" /> ${userName}, <spring:theme code="mpl.myBag.customer.desc" /></p>
	</c:when>
	<c:otherwise>
	<p class="desk-view"><spring:theme code="mpl.myBag.customer.fulldesc" /></p>
	</c:otherwise>
	</c:choose> --%>
	
	<a href="/" class="continue-shopping mob-tab-view-shopping"> Continue Shopping</a>
	<ul class="checkout-types">
			
			<!-- TISBOX-879 -->
			<li id="checkout-id" class="checkout-button" style="cursor: not-allowed;">
				<!-- TISEE-6257 -->
				<a  id="checkout-enabled" class="checkoutButton checkout button red checkout-disabled" style="pointer-events: none; cursor: not-allowed; opacity: 0.5;"  onclick="return checkServiceabilityRequired('typeCheckout',this);"><spring:theme code="checkout.checkout" /></a>
				<input type="hidden" id="checkoutLinkURlId" value="${checkoutUrl}"> 
				
				<!-- error message position change as part of UF-61 -->
 				<p id="unserviceablepincode_tooltip" style="display:none">One or more item(s) are not available at this location. Please remove the item(s) to proceed or try another <span>pincode</span>?</p>
     			<p id="error-Id_tooltip" style="display:none" >Oops! Invalid <span>pincode</span>.Please enter a valid <span>pincode</span>.</p>
 				<p id="emptyId_tooltip" style="display:none">Enter <span class="red-text">pincode</span> on the <span class="pin-desktop">left</span><span class="pin-responsive">top</span> to continue</p> 
			</li>

			<!-- TISBOX-879
			 <li id="checkoutBtnIdButton" class="checkout-button">
				<button class="btn btn-primary checkoutButton"
					data-checkout-url="${checkoutUrl}" >
					<spring:theme code="checkout.checkout" />
				</button>
			</li>
			 -->
		 
		<li id="expresscheckoutid" class="express-checkout">
            
             <c:if test="${ not empty Addresses && isLoggedIn eq true}">
                   <%--  <span><spring:theme code="text.or"/></span>  --%>
                    
                  <%-- TISBOX-1631   <form action="${request.contextPath }/checkout/multi/express"> --%> 
                    <button   id="expressCheckoutButtonId" class="express-checkout-button" data-toggle="modal" data-target="#popUpExpAddress"><spring:theme code="express.checkout"/></button>
                    <input  type="hidden" name="expressCheckoutAddressSelector"  id="expressCheckoutAddressSelector" value="demo"/>
                    <input  type="hidden" name="isPincodeServicableId"  id="isPincodeServicableId" value="N"/>
                    
                   <%--  <p class="exp_checkout_msg"><spring:theme code="cart.checkout.shipment"/></p> --%>
                   <!--  TISBOX-882 -->
                  	  <p id="expresscheckoutErrorDiv" style="display: none ; color: red;"><spring:theme code="cart.express.checkout.validation"/>  </p>
                    
                    
				    <%--  <select id="addressListSelectId" onclick="checkExpressCheckoutPincodeService('typeExpressCheckoutDD')">
				               <option value="" disabled><spring:theme code="cart.express.checkout.address"/></option>
				               <c:forEach items="${Addresses}"  var="Address">
					                  <option value="${Address.key}" id="${Address.key}">
					                  ${Address.value}
					                  </option>
					                  </c:forEach>
				     </select> --%>
				     <div id="popUpExpAddress" class="modal fade" role="dialog">
						<div class="overlay" data-dismiss="modal"></div>
    					<!-- Modal content-->
   					 	<div class="modal-content content">
   					 	<p class="ship-to">Ship To</p>
   					 	<span class="close-modal" data-dismiss="modal">X</span>
   					 	<div class="exp-address-container">
				          <c:forEach items="${Addresses}"  var="Address" varStatus="status">
				          <input type="radio" class="address_radio" value="${Address.key}" id="${Address.key}" name="expaddress" data-index="${status.index}">
				          <label class="express_address_label" for="${Address.key}">${Address.value}</label>
					      </c:forEach>
					      </div>
					      <button  id="expressCheckoutButtonId" class="express-checkout-button" onclick="return expressbutton()"><spring:theme code="express.checkout"/></button>
				     	 </div>
          			</div>
				 	
                 <%--    </form> --%>
                 <%-- onclick="return expressbutton();" --%>
	         </c:if>     
            
            </li>
            
          </ul>
        <a href="/" class="continue-shopping desk-view-shopping"> Continue Shopping</a>
          </div>
		<!-- <a href="/store" class="continue-shopping"> Continue Shopping</a> --><!-- store url change -->
		<!-- <a href="/" class="continue-shopping"> Continue Shopping</a> -->

	<div class="top block MyBag-pincode" id="pinCodeDispalyDiv">
		<%-- <h2><spring:theme code="cart.delivery.options" /></h2> --%>		<!-- UF-63 -->
		<%-- <input type="hidden"  name = "defaultPinCodeIdsDefault" id= "defaultPinCodeIdsDefault"  value="${defaultPinCode}"/>
			<div id="defaultPinDiv">
				<p><spring:theme code="product.pincode" /> 
				<input id= "defaultPinCodeIdsq" name = "defaultPinCodeIdsq" style="font-weight: bold;" value="${defaultPinCode}"/></p> 
				<input id="pin" type="text" value="${defaultPinCode}" maxlength="6" onkeypress="return isNum(event)"/>
				<a id="changePinAnchor" onClick="pinCodeDiv()">Change </a> 
				<!-- <button class="orange submit" id="pdpPincodeCheck"  onClick="pinCodeDiv()">Change Pincode</button> -->
			</div> --%>
			<div id="changePinDiv">
				<%-- <p><spring:theme code="product.pincode.input" /></p> --%>
				<p id="cartPinCodeAvailable"><spring:theme code="product.pincode" /></p>
				<!-- TPR_1055 EQA -->
				<p id="AvailableMessage" style="display:none" class="availablePins"></p>
				<%-- <p id="unserviceablepincode" style="display:none"><spring:theme code="cart.unserviceable.pincode" /></p>
				<p id="error-Id" style="display:none" ><spring:theme code="product.invalid.pincode" /></p>
				<p id="emptyId" style="display:none"><spring:theme code="product.empty.pincode" /></p> --%>
				<c:choose>
		 		<c:when test="${not empty defaultPinCode}">
				<input type="text" id= "defaultPinCodeIds" name = "defaultPinCodeIds" style="" value="${defaultPinCode}" placeholder="Pincode" maxlength="6" onkeypress="return isNumber(event)" />
				</c:when>
		   		 <c:otherwise>
		    	<input type="text" id= "defaultPinCodeIds" name = "defaultPinCodeIds" style="" value="" placeholder="Pincode" maxlength="6" onkeypress="return isNumber(event)" />
		   		</c:otherwise>
				 </c:choose>
				 <input type="hidden" id="pinId" value="${defaultPinCode}"/>
				<button id= "pinCodeButtonIds" name="pinCodeButtonId" style="" type="" onclick="return checkPincodeServiceability('typeSubmit',this);"><spring:theme code="text.submit"/></button>
				
				<!-- error message position change as part of UF-61 -->
 				<p id="unserviceablepincode" style="display:none" class="unservicePins"><spring:theme code="cart.unserviceable.pincode" /></p>
 				<p id="error-Id" style="display:none" class="errorPins"><spring:theme code="product.invalid.pincode" /></p>
 				<p id="emptyId" style="display:none" class="emptyPins"><spring:theme code="product.empty.pincode" /></p>
			</div>
		
		
		
		<%-- <p id="error-Id" style="display:none" ><spring:theme code="product.invalid.pincode" /></p>
		<p id="emptyId" style="display:none"><spring:theme code="product.empty.pincode" /></p> --%>
		
		
	</div>
	</div>
	<script>

				//UF-70 Commenting onload call to checkIsServicable() and making it on ready in marketplacecheckoutaddon.js
   				//window.onload =	function(){
   						//checkIsServicable();   						
   				//}


                             /*   $(window).on('load',function(){ 
   						checkIsServicable();
   						
   				}); */

	</script>
<!-- TISCR-320 -->
<%-- <span class="continue-shopping">
<a href="${continueShoppingUrl}"><spring:theme code="general.continue.shopping"/></a>
   </span> --%>
  
   

	   <cart:cartItems cartData="${cartData}"/>
	   
						
		<cart:cartGiftYourself/>

</c:if>

<!-- UF-70 -->
<input type="hidden" name="isPincodeRestrictedPromoPresent" id="isPincodeRestrictedPromoPresentId" value="${isPincodeRestrictedPromoPresent}">
