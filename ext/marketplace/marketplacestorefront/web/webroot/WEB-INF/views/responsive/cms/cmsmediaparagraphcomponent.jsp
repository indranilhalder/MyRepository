<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<%-- <div class="content bestCollections-content">
  <div class="bestCollectionsImage"><img src="${media.url}" alt="${media.altText}"/></div>
 <br><b>${content}</b>
   <div class="bestCollections">${content}</div>
</div>
 --%>
<c:if test="${not empty component.url}">
	<a href="${component.url}"><img src="${media.url}"
		alt="${media.altText}" /> ${content}</a>
</c:if>
<c:if test="${empty component.url}">
	<img src="${media.url}" alt="${media.altText}" />
  ${content}</a>
</c:if>

