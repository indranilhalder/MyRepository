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
	
	<script>
   				window.onload =	function(){
   						checkIsServicable();
   				}
	</script>

<span class="continue-shopping">
<a href="${continueShoppingUrl}"><spring:theme code="general.continue.shopping"/></a>
   </span>
  
   

	   <cart:cartItems cartData="${cartData}"/>
	   
						
		<cart:cartGiftYourself/>

</c:if>
