<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%-- <c:url value="/search/helpmeshop" var="searchForHelpMeShopUrl" /> --%>


  <div class="close" id="closeConceirge">close</div>
  <h1><spring:theme code="header.latestOffers.offersOfWeek.title" /></h1>
 	
 <div class="topConcierge">
 	<c:forEach items="${latestOffersData.latestOfferEntry}" var="latestOffersEntry">
 		<c:if test="${not empty latestOffersEntry.url}">
 			<c:set var="url" value="${latestOffersEntry.url}" />
 			<c:choose>
	 			<c:when test="${fn:contains(url, '?')}">
	 				<c:set var="originalUrl" value="${url}&icid=${latestOffersEntry.icid}" />
	 			</c:when>
	 			<c:otherwise>
	 				<c:set var="originalUrl" value="${url}?icid=${latestOffersEntry.icid}" />
	 			</c:otherwise>
 			</c:choose> 			
 		</c:if>
 		<a href="${originalUrl}" class='item'>
 			<div class="latest-offer-carousal-image">
 				<img src="${latestOffersEntry.imageUrl}" >
 			</div>
 			<div class="latest-offer-info">
 				${latestOffersEntry.text}
 			</div>
 		</a>
 	</c:forEach>
 
 </div>
	