<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user"%>

<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<template:page pageTitle="${pageTitle}">
	<div class="container r2-sitemap" style="color:#4a4a4a;">
		<h1 class="r2-center">Site Map</h1>
		<!-- code changes for TISPRD-3183 --> 
		<c:forEach var="megamap" items="${megaMap}">
			<section>
				<c:set var="catName" value="${fn:split(megamap.key.name, '||')}" />
				<%-- <h4 class="r2-borderBottom">
					<a href="/${catName[1]}/c-${fn:toLowerCase(megamap.key.code)}">${catName[0]}</a>
				</h4> --%>
				
				<!-- code changes for INC-10885 -->
 				<h4 class="r2-borderBottom">
 				<c:url value="${catName[1]}" var="l1Url" />
 					<a href="${l1Url}">${catName[0]}</a>
  				</h4>
  				<!-- code changes for INC-10885 -->
				<ul>
				<c:forEach var="l2MegaMap" items="${megamap.value}">
					<c:set var="catNameChildlevel2" value="${fn:split(l2MegaMap.key.name, '||')}" />
						<li>
							<ul>
							<!-- PRDI-462 -->
								<h5 class="toggle"><c:url var="l2Url" value="/${catNameChildlevel2[2]}/c-${fn:toLowerCase(l2MegaMap.key.code)}"/><a href="${l2Url}">${catNameChildlevel2[0]}</a></h5>
								<c:forEach var="l3MegaMap" items="${l2MegaMap.value}">
								<c:set var="catNameChildlevel3" value="${fn:split(l3MegaMap.name, '||')}" />
								<!-- PRDI-462 -->
								<c:url
								value="/${catNameChildlevel3[2]}/c-${fn:toLowerCase(l3MegaMap.code)}"
								var="l3Url" />
								<li><a href="${l3Url}">${catNameChildlevel3[0]}</a></li>
								</c:forEach>
							</ul>
						</li>
				</c:forEach>
				</ul>
			</section>
		</c:forEach>
	</div>
</template:page>


