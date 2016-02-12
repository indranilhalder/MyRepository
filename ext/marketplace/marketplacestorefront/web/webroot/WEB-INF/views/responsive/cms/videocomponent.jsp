<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:if test="${not empty component.videoTitle}">
	<h3>${component.videoTitle}</h3>
</c:if>
<div class="video-wrapper">
	<div class="video-container">
		<iframe class="iframe-video" src="${component.videoUrl}" id="player"></iframe>
	</div>
	<c:if test="${not empty component.videoDescription}">
<div class="brand-description">
	<p>${component.videoDescription}</p>
	</div>
</c:if>
</div>



