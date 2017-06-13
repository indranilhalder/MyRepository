<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
		 pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>



<!-- Optional theme -->
<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="style.css"> -->


<%-- <div class="content bestCollections-content">
  <div class="bestCollectionsImage"><img src="${media.url}" alt="${media.altText}"/></div>
 <br><b>${content}</b>
   <div class="bestCollections">${content}</div>
</div>
 --%>
<c:choose>
	<c:when test="${empty medias}">
		<c:if test="${not empty url}">
			<a href="${url}"><img src="${media.url}"
								  alt="${media.altText}" /> ${content}</a>
		</c:if>
		<c:if test="${empty url}">
			<img src="${media.url}" alt="${media.altText}" />
			${content}</a>
		</c:if>
	</c:when>
	<c:otherwise>

		<section class="trav-edition double-banner mb-40 clearfix container">
			<c:forEach var="media" items="${medias}" varStatus="status">
				<div class="col"><a href="${url}"><img src="${media.media.url}"
													   alt="${media.media.altText}" /> ${content}</a> </div>
			</c:forEach>
		</section>
	</c:otherwise>
</c:choose>

