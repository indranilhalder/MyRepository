<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="showTax" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>
<script type="text/javascript">		
$(document).ready(function() {
    $("#pinCodeButtonIds").click(function() {
        var zipcode = $("#defaultPinCodeIds").val();
        var regPostcode = /^([1-9])([0-9]){5}$/;
        if(regPostcode.test(zipcode) == true){
        	return true;  
        }
        else  
        {  
        	$("#defaultPinCodeIds").css("color","red");
            $( "#errorId").show();
             
            return false;  
        }
    });
    
    $("select#addressListSelectId option:first-child").attr("selected","selected");
});



 
function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}

</script>
<c:if test= "${empty defaultPinCode}">
	<!-- <div><b><h4>Please enter pincode to proceed to checkout</h4></b></div> -->
</c:if>
<%-- <form:form id="cartFormId" action="${request.contextPath}/cart/setPincode" method="GET">   --%>

  
	<div class="top block">
		<h2><spring:theme code="cart.delivery.options" /></h2>
		<p><spring:theme code="product.pincode" /></p>
		
		<input type="text" id= "defaultPinCodeIds" name = "defaultPinCodeId" style="" value="${defaultPinCode}" placeholder="Pincode" maxlength="6" onkeypress="return isNumber(event)" /> 

		<button id= "pinCodeButtonIds" name="pinCodeButtonId" style="" type="" onclick="return checkPincodeServiceability('typeSubmit');"><spring:theme code="product.submit"/></button>
		
		<p id="error-Id" style="display:none" ><spring:theme code="product.invalid.pincode" /></p>
		<p id="emptyId" style="display:none"><spring:theme code="product.empty.pincode" /></p>
		
		
	</div>
<%-- </form:form> --%>
<div class="middle block">
	<h2><spring:theme code="mpl.orderDetails" /></h2>
	<ul class="totals">
            <li id="subtotal"><spring:theme code="basket.page.totals.subtotal"/> <span class="amt"><ycommerce:testId code="Order_Totals_Subtotal"><format:price priceData="${cartData.subTotal}"/></ycommerce:testId></span></li>
            
            
         <c:if test="${cartData.totalDiscounts.value > 0}">
        <li id="discount"><spring:theme code="basket.page.totals.savings"/> <span class="amt">
        
       
        
        
        
        -<ycommerce:testId code="Order_Totals_Savings"><format:price priceData="${cartData.totalDiscounts}"/></ycommerce:testId>
        
         </c:if> 
        </span></li>
            
            <li id="total"><spring:theme code="basket.page.totals.total"/><span class="amt"><ycommerce:testId code="cart_totalPrice_label">
                <c:choose>
                    <c:when test="${showTax}">
                        <format:price priceData="${cartData.totalPriceWithTax}"/>
                    </c:when>
                    <c:otherwise>
                        <format:price priceData="${cartData.totalPrice}"/>
                    </c:otherwise>
                </c:choose>
            </ycommerce:testId></span></li>
          </ul>

          <ul class="checkout-types">
			
			<!-- TISBOX-879 -->
			<li id="checkout-id" class="checkout-button">
				<!-- TISEE-6257 -->
				<a  id="checkout-enabled" style="line-height: 30px  !important;" class="checkoutButton checkout button red"  onclick="return checkPincodeServiceability('typeCheckout');"><spring:theme code="checkout.checkout" /></a>
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
                    
                    
				     <select id="addressListSelectId" onclick="checkExpressCheckoutPincodeService('typeExpressCheckoutDD')" onchange="checkExpressCheckoutPincodeService('typeExpressCheckoutDD')">
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
