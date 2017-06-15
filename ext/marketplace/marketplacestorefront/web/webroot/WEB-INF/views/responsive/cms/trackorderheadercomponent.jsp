<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>



<c:url var="urlToAccess" value="${notificationUrl }"></c:url>

<c:choose>

<c:when test="${isSignedInUser eq 'yes' and not empty notificationCount }">

	<li class="track trackOrder">
	<a id="tracklink" href="${urlToAccess}" class="tracklinkcls">
	
	<span class="bell-icon"></span>&nbsp;Notifications&nbsp;(<span >${notificationCount}</span>)</a>	
		 <span id="mobile-menu-toggle"></span>
		<ul class="trackorder-dropdown"></ul> 
		</li>
		
		</c:when>
		<c:otherwise>
		<c:if test="${isSignedInUser eq 'yes' and  empty notificationCount}">		
		<li class="track trackOrder">
	<a id="tracklink" href="${urlToAccess}" class="tracklinkcls"><span class="bell-icon"></span>&nbsp;Notifications</a>
		 <span id="mobile-menu-toggle"></span>
		<ul class="trackorder-dropdown"></ul> 
		</li>
		</c:if>
		
		
		<c:if test="${ empty notificationCount and isSignedInUser eq 'no'}">
		
		<li class="track trackOrder">
	<a id="tracklink" href="/login" class="tracklinkcls"><span class="bell-icon"></span>&nbsp;Notifications</a>
		 <span id="mobile-menu-toggle"></span>
		<ul class="trackorder-dropdown"></ul> 
		</li>
		</c:if>
		

		</c:otherwise>
		
		</c:choose>