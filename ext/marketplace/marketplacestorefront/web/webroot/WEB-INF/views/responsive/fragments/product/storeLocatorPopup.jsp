<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%-- <c:url var="storeLocatorURL" value="/p-allStores/${pincode}" scope="request"></c:url> --%>
<div class="store-locator-block">
	<c:if test="${fn:length(storesAvailable) gt 0}" >
		<span class=""><spring:theme code="nearest.store" arguments="${pincode}"/></span>
		<c:forEach items="${storesAvailable}" var="store" varStatus="loop" >
			<c:if test="${loop.first}">
			<div class="storeName"><b>${store.name}</b></div>
			<div class="address">${store.address.formattedAddress}</div>
		    <div class="distance"><b>${store.distanceKm}</b></div>
		    </c:if>
		</c:forEach>
		
		<c:if test="${fn:length(storesAvailable) gt 1}" >
			<a href="javascript:showStoreLocatorModal();" data-toggle="modal"
				data-target="#storeLocatorModal"
				data-mylist="<spring:theme code="text.help" />"
				data-dismiss="modal" >
				<spring:theme code="more.stores.nearby" arguments="${fn:length(storesAvailable) - 1 }"/>
			</a>
														
		</c:if>

		<div id="storeLocatorModal" class="modal cancellation-request fade">
			  <div class="content">
				<!-- 	<button type="button" class="close pull-right" 		
		           aria-hidden="true" data-dismiss="modal">		
		            </button> -->
				<div class="cancellation-request-block">
					<h2 class="">Stores Nearby</h2>
					<div class="store-popup-block"></div>
					  <c:forEach items="${storesAvailable}" var="store" varStatus="storeLoop">
						 <c:if test="${not storeLoop.first}">
						 <div class="pull-left">
							 <span class="storeName"><b>${store.name}</b></span>
							 <span class="address">${store.address.formattedAddress}</span>
					         <span class="distance"><b>${store.distanceKm}</b></span>
					      </div>   
				         </c:if>
					  </c:forEach>
				  </div>
			  </div>
			 </div> 
		</div>
		
	</c:if>
</div>
	