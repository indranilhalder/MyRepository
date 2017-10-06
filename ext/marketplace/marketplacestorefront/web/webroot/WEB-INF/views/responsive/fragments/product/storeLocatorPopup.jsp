<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="store-locator-block">
	<c:if test="${fn:length(storesAvailable) gt 0}" >
		<span class=""><b><spring:theme code="nearest.store" arguments="${pincode}"/></b></span>
		<c:forEach items="${storesAvailable}" var="store" varStatus="loop" >
			<c:if test="${loop.first}">
			<div class="store-address">
			<span>1</span>
			<c:if test="${not empty store.displayName}"><div class="store-name"><b>${store.displayName}</b></div></c:if>
			<c:if test="${not empty store.address.line1}"><div class="addressLine1">${store.address.line1}</div></c:if>
			<c:if test="${not empty store.address.line2}"><div class="addressLine2">${store.address.line2}</div></c:if>
			<c:if test="${not empty store.address.postalCode}"><div class="pin_code">${store.address.postalCode}</div></c:if>
		    <%-- <c:if test="${not empty store.distanceKm}"><div class="distance"><b>${store.distanceKm} ${store.status}</b></div></c:if> --%>
		   </div>
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
		<div class="overlay" data-dismiss="modal"></div>
			  <div class="content">
					<button type="button" class="close pull-right" 		
		           aria-hidden="true" data-dismiss="modal">		
		            </button>
				<div class="cancellation-request-block">
					<p class="nearby-stores">Stores Near &nbsp; ${pincode}</p>
					<div class="store-popup-block"></div>
					  <c:forEach items="${storesAvailable}" var="store" varStatus="storeLoop">
						 <c:if test="${not storeLoop.first}">
						 <div class="store-address-block">
						     <c:if test="${not empty store.displayName}"><div class="store-name"><b>${store.displayName}</b></div></c:if>
					         <c:if test="${not empty store.address.line1}"><div class="addressLine1">${store.address.line1}</div></c:if>
			                 <c:if test="${not empty store.address.line2}"><div class="addressLine2">${store.address.line2}</div></c:if>
			                 <c:if test="${not empty store.address.city && not empty store.address.postalCode}"><div class="city">${store.address.city} - ${store.address.postalCode}</div></c:if>
			                 <%-- <c:if test="${not empty store.address.country.name}"><div class="country">${store.address.country.name}</div></c:if> --%>
			                 <c:if test="${not empty store.address.country.name}"><div class="country">IN</div></c:if>
			                 <c:if test="${not empty store.distanceKm}"><div class="distance"><b>${store.distanceKm}&nbsp;${store.status}</b></div></c:if>
					      </div>   
				         </c:if>
					  </c:forEach>
				  </div>
			  </div>
			 </div> 
	</c:if>
</div>