<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>

<c:if test="${fn:length(storesAvailable) gt 0}" >
	<span class=""><spring:theme code="nearest.store" arguments="${pincode}"/></span>
	<c:forEach items="${storesAvailable}" var="store" begin="0" end="0" >
		<p>${store.name}</p>
		<p>${store.address.formattedAddress}</p>
	</c:forEach>

	<a class="store-locator" href="${storeLocatorURL}" role="button"
	data-toggle="modal" data-target="#storeLocatorModal" data-pincode="${pincode}"> 
	<spring:theme code="more.stores.nearby" arguments="${fn:length(storesAvailable) -1 }"/>
	</a>

	<div id="storeLocatorModal" class="modal cancellation-request fade storeDetails">
		  <c:forEach items="${storesAvailable}" var="stores" begin="1">
			 <div class="storeName"><b>${stores.name}</b></div><br>
			 <div class="addressLine1">${stores.address.line1}</div><br>
			 <div class="addressLine2">${stores.address.line2}</div><br>
			 <div class="addressLine3">${stores.address.line3}</div><br>
			 <div class="state">${stores.address.state} - ${stores.address.postalCode}</div><br>
	         <div class="country">${stores.address.country}</div><br>
	         <div class="distance"><b>${stores.distanceKm}</b>></div>
		  </c:forEach>
	</div>
	
</c:if>
