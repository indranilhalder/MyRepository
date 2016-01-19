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

<!-- <ul class="header-trackOrder"> -->

<c:choose>
<c:when test="${isSignedInUser eq 'yes' }">
	<li class="track trackOrder">
	<a id="tracklink" href="#"><span class="bell-icon"></span>&nbsp;(<span >${notificationCount}</span>)</a>
		 <span id="mobile-menu-toggle"></span>
		<ul class="trackorder-dropdown"></ul> 
		</li>
		
		</c:when>
		<c:otherwise>
		<li class="track trackOrder">
	<a id="tracklink" href="<c:url value="/login"/>"><span class="bell-icon"></span></a>
		 <span id="mobile-menu-toggle"></span>
		<ul class="trackorder-dropdown"></ul> 
		</li>
		</c:otherwise>
		
		</c:choose>
	
		
			




		<!-- </ul> -->

