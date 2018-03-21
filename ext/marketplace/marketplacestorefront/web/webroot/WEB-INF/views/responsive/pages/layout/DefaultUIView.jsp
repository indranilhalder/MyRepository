<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>

<style>

table {
    border-collapse: collapse;
    width: 100%;
    padding-bottom: 5%;
}
th, td {
    text-align: left;
    padding: 8px;
}
tr:nth-child(even){background-color: #f2f2f2}
th {
    background-color: #514848;
    color: white;
    height : 30px !important;
    text-align: center;
}
</style>

<template:page pageTitle="${pageTitle}">
<html>
	<div class="sub-brand">
		<div class="feature-collections">
			<div class="wrapper background">
				<cms:pageSlot position="first" var="feature">
					<c:if test="${feature.typeCode eq 'HeroBannerComponent'}">
					<div>
					<h1>HeroBannerComponent</h1>
					<table class="">
					<thead>
					<tr>
						<th>title</th>
						<th>webURL</th>	
						<th>imageURL</th>
						<th>brandLogo</th>
						</tr>
					</thead>
					<c:forEach items="${feature.items}" var="heroElements">
					<div>
						<c:if test="${heroElements.typeCode eq 'HeroBannerElement'}">
						<tr>
						    <td>  ${heroElements.title}</td>
						     <td> <a href="${heroElements.webURL}">${heroElements.webURL}</a></td>
						    <td><img alt="" src="${heroElements.imageURL.URL}"></td>
						     <td> <img alt="" src="${heroElements.brandLogo.URL}"></td>
						</tr>
						</c:if>
					</div>
					</c:forEach>
					</table>
					</div>
					</c:if>
					
					
					<c:if test="${feature.typeCode eq 'ConnectBannerComponent'}">
					<div>
					<h1>ConnectBannerComponent</h1>
					<table class="">
					<thead>
					<tr>
						<th>Sub Type</th>
						<th>Title</th>	
						<th>Description</th>
						<th>Btn Text</th>
						<th>Icon Image URL</th>
						<th>Background Image URL</th>
						<th>Web URL</th>
						<th>Start Hex Code</th>
						<th>End Hex Code</th>
						</tr>
					</thead>
						<tr>
						    <td>  ${feature.subType}</td>
						     <td> ${feature.title}</td>
						     <td>${feature.description}</td>
						     <td> ${feature.btnText}</td>
						     <td> <img alt="" src="${feature.iconImageURL}"></td>
						     <td> <img alt="" src="${feature.backgroundImageURL}"></td>
						     <td><a href="${feature.webURL}"> ${feature.webURL}"></a></td>
						     <td>${feature.startHexCode}</td>
						     <td> ${feature.endHexCode}</td>
						</tr>
					</table>
					</div>
					</c:if>	
					
					<c:if test="${feature.typeCode eq 'OffersWidgetComponent'}">
					<div>
					<h1> OffersWidgetComponent</h1>
					
					<table class="">
					<thead>
					<tr>
						<th>title</th>
					</tr>
					</thead>
					<tr><td>${feature.title}</td></tr>
					</table>
					
<%-- 					<c:if test="${feature.items"> --%>
					<table class="">
					<thead>
					<tr>
					    <th>Title</th>
						<th>Web URL</th>	
						<th>Image URL</th>
						<th>Btn Text</th>
						<th>Discount Text</th>
						</tr>
					</thead>
					<c:forEach items="${feature.items}" var="offersWidgetElement">
					<div>
						<c:if test="${offersWidgetElement.typeCode eq 'OffersWidgetElement'}">
						<tr>
						     <td> ${offersWidgetElement.title}</td>
						     <td> <a href="${offersWidgetElement.webURL}">${offersWidgetElement.webURL}</a></td>
						     <td><img alt="" src="${offersWidgetElement.imageURL}"></td>
						     <td> ${offersWidgetElement.btnText}</td>
						     <td> ${offersWidgetElement.discountText}</td>
						</tr>
						</c:if>
					</div>
					</c:forEach>
					</table>
<%-- 					</c:if> --%>
					</div>
					</c:if>
				</cms:pageSlot>

			</div>
		</div>
	</div>
	</html>
</template:page>
