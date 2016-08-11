<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>


<c:if test="${not empty cartData.entries}">
    <c:url value="/cart/checkout" var="checkoutUrl" scope="session"/>
    <c:url value="${continueUrl}" var="continueShoppingUrl" scope="session"/>
    <%-- <input type="hidden" id="isServicableId" value="${isServicable}"> Not required --%>
    <c:set var="showTax" value="false"/>
	<h1 class="MyBagHeadingDesktop" ><spring:theme code="mpl.myBag" /></h1>
	<p class="desk-view"><spring:theme code="mpl.myBag.customer.cart.desc" /></p>
	<ul class="checkout-types">
			
			<!-- TISBOX-879 -->
			<li id="checkout-id" class="checkout-button">
				<!-- TISEE-6257 -->
				<a  id="checkout-enabled" style="line-height: 40px  !important;" class="checkoutButton checkout button red"  onclick="return checkPincodeServiceability('typeCheckout');"><spring:theme code="checkout.checkout" /></a>
				<input type="hidden" id="checkoutLinkURlId" value="${checkoutUrl}"> 
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
                    <span><spring:theme code="text.or"/></span> 
                    
                  <%-- TISBOX-1631   <form action="${request.contextPath }/checkout/multi/express"> --%> 
                    <button   id="expressCheckoutButtonId" class="express-checkout-button" onclick="return expressbutton();"><spring:theme code="express.checkout"/></button>
                    <input  type="hidden" name="expressCheckoutAddressSelector"  id="expressCheckoutAddressSelector" value="demo"/>
                    <input  type="hidden" name="isPincodeServicableId"  id="isPincodeServicableId" value="N"/>
                    
                    <p><spring:theme code="cart.checkout.shipment"/></p>
                   <!--  TISBOX-882 -->
                    <p id="expresscheckoutErrorDiv" style="display: none ; color: red;"><spring:theme code="cart.express.checkout.validation"/>  </p>
                    
                    
				     <select id="addressListSelectId" onclick="checkExpressCheckoutPincodeService('typeExpressCheckoutDD')">
				              <%--  <option value="" disabled><spring:theme code="cart.express.checkout.address"/></option> --%>
				               <c:forEach items="${Addresses}"  var="Address">
					                  <option value="${Address.key}" id="${Address.key}">
					                  ${Address.value}
					                  </option>
					                  </c:forEach>
				     </select>
				 	
                 <%--    </form> --%>
	         </c:if>     
            
            </li>
            
          </ul>
          </div>
		<!-- <a href="/store" class="continue-shopping"> Continue Shopping</a> --><!-- store url change -->
		<a href="/" class="continue-shopping"> Continue Shopping</a>
	
	
	<script>
   				window.onload =	function(){
   						checkIsServicable();
   				}
	</script>
<!-- TISCR-320 -->
<%-- <span class="continue-shopping">
<a href="${continueShoppingUrl}"><spring:theme code="general.continue.shopping"/></a>
   </span> --%>
  
   

	   <cart:cartItems cartData="${cartData}"/>
	   
						
		<cart:cartGiftYourself/>

</c:if>
