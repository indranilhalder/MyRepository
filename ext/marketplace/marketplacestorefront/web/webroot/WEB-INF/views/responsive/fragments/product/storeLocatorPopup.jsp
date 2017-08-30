<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>

<%-- <c:url var="storeLocatorURL" value="/p-allStores/${pincode}" scope="request"></c:url> --%>
<div class="content">
	<div class="cancellation-request-block">
	<c:if test="${fn:length(storesAvailable) gt 0}" >
		<span class=""><spring:theme code="nearest.store" arguments="${pincode}"/></span>
		<c:forEach items="${storesAvailable}" var="store" begin="0" end="0" >
			<div class="storeName"><b>${store.name}</b></div>
			<div class="address">${store.address.formattedAddress}</div>
		    <div class="distance"><b>${store.distanceKm}</b></div>
		</c:forEach>
	<c:if test="${fn:length(storesAvailable) gt 1}" >
		<a class="store-locator" href="" data-toggle="modal"
			data-target="#storeLocatorModal"
			data-mylist="<spring:theme code="text.help" />"
			data-dismiss="modal" >
			
			<spring:theme code="more.stores.nearby" arguments="${fn:length(storesAvailable) - 1 }"/>
		</a>
	</c:if>

		<div id="storeLocatorModal" class="modal fade storeDetails">
			  <c:forEach items="${storesAvailable}" var="store" begin="1">
				 <div class="storeName"><b>${store.name}</b></div>
				 <div class="address">${store.address.formattedAddress}</div>
		         <div class="distance"><b>${store.distanceKm}</b></div>
			  </c:forEach>
		</div>
		
	</c:if>
	</div>
</div>	