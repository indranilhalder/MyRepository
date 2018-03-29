<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<link href="https://fonts.googleapis.com/css?family=Rubik:400,500"
	rel="stylesheet">
<link rel="stylesheet" type="text/css"
	href="/_ui/responsive/theme-blue/css/slick.css" />

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

tr:nth-child(even) {
	background-color: #f2f2f2
}

th {
	background-color: #514848;
	color: white;
	height: 30px !important;
	text-align: center;
}

/*new css*/
.wrapper{
	padding:0;
}
.slick-slide{
height:auto !important;
}
body {
	font-family: 'Rubik', sans-serif;
}

.pad0 {
	padding: 0;
}

.mb40 {
	margin-bottom: 40px;
}

.hero-slider .slick-slide {
	margin: 0 5px;
	position: relative;
}

.hero-slider .slick-slide img {
	width: 100%;
	height: 190px;
	margin-top: 10px;
	display: block;
	border-radius: 4px;
}

.hero-slider .slick-current img {
	width: 100%;
	height: auto;
	display: block;
	margin-top: 0px;
}

.hero-slider .hero-subsection {
	position: absolute;
	top: 40%;
	left: 0;
}

.hero-slider .brand-logo {
	width: auto !important;
	height: auto !important;
	margin-left: 15px;
	border: 1px solid #000;
}

.hero-slider .banner-title {
	width: auto;
	height: auto;
	margin-left: 15px;
	margin-top: 10px;
	font-size: 30px;
	font-weight: 500;
	color: #ffffff;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
}

.offer-slider .slick-slide {
	position: relative;
}

.offer-slider .slick-slide img {
	max-width: 100%;
	border-radius: 4px;
}

.offer-slider .offer-subsection {
	position: absolute;
	bottom: 0;
	left: 0;
}

.offer-slider .shop-now {
	width: 141px;
	height: auto;
	margin: 20px 15px;
	border: 1px solid #000;
	border-radius: 100px;
	border: solid 2px #ffffff;
	padding: 10px 0;
	text-align: center;
	color: #ffffff;
	display: block;
	font-size: 14px;
}

.offer-slider .banner-title {
	width: auto;
	height: auto;
	margin-left: 15px;
	margin-top: 10px;
	font-size: 30px;
	font-weight: 500;
	color: #ffffff;
}

.offer-slider .offer {
	position: absolute;
	top: 20px;
	left: 20px;
	width: 78px;
	height: 78px;
	background-image: linear-gradient(137deg, #fd2c7a, #ff7255);
	text-align: center;
	color: #ffffff;
	border-radius: 50%;
}

.offer-slider .offer span {
	position: absolute;
	left: 12px;
	top: 20px;
	width: 55px;
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.43;
	letter-spacing: normal;
}

.offer-widget-title {
	font-size: 20px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: left;
	color: #212121;
	margin: 0px 0px 25px 15px;
}

.flash-sales-widget {
	background-color: #962343;
	color: #ffffff;
	padding: 25px 20px;
	background-repeat: no-repeat;
	background-size: cover;
}

.flash-sales-widget-title {
	font-size: 20px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: left;
	color: #ffffff;
	text-transform: capitalize;
}

.flash-sales-widget-timer {
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: right;
	color: #ffffff;
}

.flash-sales-widget-text {
	font-size: 14px;
	font-weight: normal;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.57;
	letter-spacing: normal;
	text-align: left;
	color: #ffffff;
	margin: 10px 0 25px 0;
	width: 195px;
}

.flash-sales-widget-product:nth-child(even) {
	padding: 0 12px 0 0;
}

.flash-sales-widget-product:nth-child(odd) {
	padding: 0 0 0 12px;
}

.flash-sales-widget-product img {
	border-radius: 4px;
}

.flash-sales-widget-product-offer {
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.43;
	letter-spacing: normal;
	color: #ffffff;
	padding: 0;
	margin: 15px 0 10px 0;
}

.flash-sales-widget-product-name {
	font-size: 14px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.57;
	letter-spacing: normal;
	text-align: left;
	color: #ffffff;
	padding: 0;
	margin-bottom: 25px;
}

.flash-sales-widget .shop-all-btn {
	width: 120px;
	padding: 8px 0;
	border-radius: 100px;
	border: solid 2px #ffffff;
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: center;
	color: #ffffff;
	float: left;
	margin-bottom: 5px;
}

.flash-sales-widget .fa-clock-o {
	font-size: 20px;
	margin-right: 5px;
}

.flash-sales-widget-timer .fa-clock-o {
	font-size: 20px;
	margin-right: 10px;
}

.flash-sales-widget-timer .time-digits {
	float: right;
	margin-top: 2px;
}

.connect-banner {
	width: calc(100% - 30px);
	margin-left: 15px;
	margin-right: 15px;
	border-radius: 4.2px;
	background-image: -webkit-linear-gradient(202deg, #48dfe6, #4facfe);
	background-image: -o-linear-gradient(202deg, #48dfe6, #4facfe);
	background-image: linear-gradient(292deg, #48dfe6, #4facfe);
	padding: 25px;
	background-repeat: no-repeat;
	background-size: cover;
}

.connect-banner-text1 {
	font-size: 14.3px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #ffffff;
	padding-right: 0;
}

.connect-banner-text2 {
	font-size: 12.5px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #ffffff;
	margin: 10px 0 15px 0;
}

.connect-banner-more {
	font-size: 12.5px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #ffffff;
	padding-left: 15px;
	display: inline-block;
	width: auto;
}

.connect-banner-more::after {
	content: "";
	display: block;
	width: auto;
	border: 0.5px solid #fff;
	margin-top: 2px;
}

.connect-banner-full-width {
	background-image: -webkit-linear-gradient(202deg, #48dfe6, #4facfe);
	background-image: -o-linear-gradient(202deg, #48dfe6, #4facfe);
	background-image: linear-gradient(292deg, #48dfe6, #4facfe);
	padding: 25px;
}

.connect-banner-full-width .connect-banner-text1 {
	font-size: 16px;
}

.connect-banner-full-width .connect-banner-more,
	.connect-banner-full-width .connect-banner-text2 {
	font-size: 14px;
}
</style>

<template:page pageTitle="${pageTitle}">
	<html>
<div class="sub-brand">
	<div class="feature-collections">
		<div class="wrapper background">
			<cms:pageSlot position="DefaultNewUIContentSlot" var="feature">
				<c:if test="${feature.typeCode eq 'HeroBannerComponent'}">
					<div class="hero-slider">
						<c:forEach items="${feature.items}" var="heroElements">
							<div>
								<c:if test="${heroElements.typeCode eq 'HeroBannerElement'}">
									<a href="${heroElements.webURL}"> <img alt=""
										src="${heroElements.imageURL.URL}">
										<div class="hero-subsection">
											<img class="brand-logo" src="${heroElements.brandLogo.URL}">
											<div class="banner-title">${heroElements.title}</div>
										</div>
									</a>
								</c:if>
							</div>
						</c:forEach>
					</div>
				</c:if>
				<c:if test="${feature.typeCode eq 'ConnectBannerComponent'}">


					<div
						class="col-xs-12 mb40 ${fn:containsIgnoreCase(feature.subType, 'endToEnd') ? 'connect-banner-full-width':'connect-banner'}"
						style="background-image:url('${feature.backgroundImageURL.URL}') , linear-gradient(292deg, ${feature.startHexCode}, ${feature.endHexCode});">
						<div class="col-xs-2 pad0">
							<img src="${feature.iconImageURL.URL}">
						</div>
						<div class="col-xs-10 pad0">
							<div class="col-xs-12 connect-banner-text1">${feature.title}</div>
							<div class="col-xs-12 connect-banner-text2">${feature.description}</div>
							<a href="${not empty feature.webURL ? feature.webURL : '#'}" class="connect-banner-more">${feature.btnText}</a>
						</div>
					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'OffersWidgetComponent'}">
				
				
					<div>
						<h1>OffersWidgetComponent</h1>


						<div class="offer-widget-title">${feature.title}</div>
						<div class="offer-slider mb40">
							<c:forEach items="${feature.items}" var="offersWidgetElement">
								<div>
									<c:if
										test="${offersWidgetElement.typeCode eq 'OffersWidgetElement'}">
										<div>
											<img src="${offersWidgetElement.imageURL.URL}" />
											<div class="offer">
												<span>${offersWidgetElement.discountText}</span>
											</div>
											<div class="offer-subsection">
												<div class="banner-title">${offersWidgetElement.title}</div>
												<a href="${offersWidgetElement.webURL}" class="shop-now">${offersWidgetElement.btnText}</a>
											</div>
										</div>
									</c:if>
								</div>
							</c:forEach>
						</div>

					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'SmartFilterWidgetComponent'}">
					<div>
						<h1>SmartFilterWidgetComponent</h1>
						<table class="" border="1">
							<thead>
								<tr>
									<th>title</th>
								</tr>
							</thead>
							<tr>
								<td>${feature.title}</td>
							</tr>
						</table>
						<table class="" border="1">
							<thead>
								<tr>
									<th>Title</th>
									<th>Web URL</th>
									<th>Image URL</th>
									<th>Description</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}" var="smartfilterElements">
								<div>
									<c:if
										test="${smartfilterElements.typeCode eq 'SmartFilterWidgetElement'}">
										<tr>
											<td>${smartfilterElements.title}</td>
											<td><a href="${smartfilterElements.webURL}">${smartfilterElements.webURL}</a></td>
											<td><img alt=""
												src="${smartfilterElements.imageURL.URL}"></td>
											<td>${smartfilterElements.description}</td>
										</tr>
									</c:if>
								</div>
							</c:forEach>
						</table>
					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'FlashSalesComponent'}">
					<!--flash sales widget-->
					<div class="col-xs-12 flash-sales-widget mb40"
						style="background-image: url('${feature.backgroundImageURL.URL}');">
						<div class="col-xs-6 pad0 flash-sales-widget-title">${feature.title}</div>
						<div class="col-xs-6 pad0 flash-sales-widget-timer">
							<i class="fa fa-clock-o" aria-hidden="true"></i>
							<div class="time-digits">22 : 48 : 53</div>
						</div>
						<div class="col-xs-12 pad0 flash-sales-widget-text">${feature.description}</div>
						<div class="col-xs-6 flash-sales-widget-product">
							<a href="#"> <img src="https://via.placeholder.com/148x200"
								class="img-responsive" />
								<div class="col-xs-12 flash-sales-widget-product-offer">Under Rs. 999</div>
								<div class="col-xs-12 flash-sales-widget-product-name">Tommy
									Hilfiger TH1791163J Watch</div>
							</a>
						</div>
						<div class="col-xs-6 flash-sales-widget-product">
							<a href="#"> <img src="https://via.placeholder.com/148x200"
								class="img-responsive" />
								<div class="col-xs-12 flash-sales-widget-product-offer">Under
									Rs. 999</div>
								<div class="col-xs-12 flash-sales-widget-product-name">Tommy
									Hilfiger TH1791163J Watch</div>
							</a>
						</div>
						<div class="col-xs-6 flash-sales-widget-product">
							<a href="#"> <img src="https://via.placeholder.com/148x200"
								class="img-responsive" />
								<div class="col-xs-12 flash-sales-widget-product-offer">Under
									Rs. 999</div>
								<div class="col-xs-12 flash-sales-widget-product-name">Tommy
									Hilfiger TH1791163J Watch</div>
							</a>
						</div>
						<div class="col-xs-6 flash-sales-widget-product">
							<a href="#"> <img src="https://via.placeholder.com/148x200"
								class="img-responsive" />
								<div class="col-xs-12 flash-sales-widget-product-offer">Under
									Rs. 999</div>
								<div class="col-xs-12 flash-sales-widget-product-name">Tommy
									Hilfiger TH1791163J Watch</div>
							</a>
						</div>
						<a href="#" class="shop-all-btn">Shop all</a>
					</div>
					
					<div>
						<h1>FlashSalesComponent</h1>
						<table class="" border="1">
							<thead>
								<tr>
									<th>title</th>
									<th>description</th>
									<th>backgroundHexCode</th>
									<th>backgroundImageURL</th>
									<th>startDate</th>
									<th>endDate</th>
									<th>btnText</th>
									<th>webURL</th>
								</tr>
							</thead>
							<tr>
<%-- 								<td>${feature.title}</td> --%>
<%-- 								<td>${feature.description}</td> --%>
								<td>${feature.backgroundHexCode}</td>
								<td><img alt="" src="${feature.backgroundImageURL.URL}"></td>
								<td>${feature.startDate}</td>
								<td>${feature.endDate}</td>
								<td>${feature.btnText}</td>
								<td><a href="${feature.webURL}"> ${feature.webURL}</a></td>
							</tr>
						</table>
						<table class="" border="1">
							<thead>
								<tr>
									<th>title</th>
									<th>description</th>
									<th>imageURL</th>
									<th>webURL</th>
								</tr>
							</thead>
							<c:forEach items="${feature.offers}" var="flashsalesElements">
								<div>
									<c:if
										test="${flashsalesElements.typeCode eq 'FlashSalesElement'}">
										<tr>
											<td>${flashsalesElements.title}</td>
											<td>${flashsalesElements.description}</td>
											<td><img alt="" src="${flashsalesElements.imageURL.URL}"></td>
											<td><a href="${flashsalesElements.webURL}">${flashsalesElements.webURL}</a></td>
										</tr>
									</c:if>
								</div>
							</c:forEach>
						</table>
						<table class="" border="1">
							<thead>
								<tr>
									<th>webURL</th>
									<th>title</th>
									<th>productCode</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}" var="flashsalesitemElements">
								<div>
									<c:if
										test="${flashsalesitemElements.typeCode eq 'FlashSalesItemElement'}">

										<tr>
											<td>${flashsalesitemElements.title}</td>
											<td><a href="${flashsalesitemElements.webURL}">${flashsalesitemElements.webURL}</a></td>

											<c:if test="${not empty flashsalesitemElements.productCode}">
												<td>${flashsalesitemElements.productCode.code}</td>
											</c:if>
										</tr>
									</c:if>
								</div>
							</c:forEach>
						</table>

					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'ContentWidgetComponent'}">
					<div>
						<h1>ContentWidgetComponent</h1>
						<table class="" border="1">
							<thead>
								<tr>
									<th>title</th>
								</tr>
							</thead>
							<tr>
								<td>${feature.title}</td>
							</tr>
						</table>
						<table class="" border="1">
							<thead>
								<tr>
									<th>title</th>
									<th>webURL</th>
									<th>imageURL</th>
									<th>description</th>
									<th>btnText</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}" var="contentwidgetElement">
								<div>
									<c:if
										test="${contentwidgetElement.typeCode eq 'ContentWidgetElement'}">
										<tr>
											<td>${contentwidgetElement.title}</td>
											<td><a href="${contentwidgetElement.webURL}">${contentwidgetElement.webURL}</a></td>
											<td><img alt=""
												src="${contentwidgetElement.imageURL.URL}"></td>
											<td>${contentwidgetElement.description}</td>
											<td>${contentwidgetElement.btnText}</td>
										</tr>
									</c:if>
								</div>
							</c:forEach>
						</table>
					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'BannerProductCarouselComponent'}">
					<div>
						<h1>BannerProductCarouselComponent</h1>
						<table class="" border="1">
							<thead>
								<tr>
									<th>title</th>
									<th>webURL</th>
									<th>imageURL</th>
									<th>description</th>
									<th>btnText</th>
								</tr>
							</thead>
							<tr>
								<td>${feature.title}</td>
								<td><a href="${feature.webURL}">${feature.webURL}</a></td>
								<td><img alt="" src="${feature.imageURL.URL}"></td>
								<td>${feature.description}</td>
								<td>${feature.btnText}</td>
							</tr>
						</table>
						<table class="" border="1">
							<thead>
								<tr>
									<th>webURL</th>
									<th>title</th>
									<th>productCode</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}"
								var="bannerProdCarouselElement">
								<div>
									<c:if
										test="${bannerProdCarouselElement.typeCode eq 'BannerProdCarouselElementComp'}">
										<tr>
											<td>${bannerProdCarouselElement.title}</td>
											<td><a href="${bannerProdCarouselElement.webURL}">${bannerProdCarouselElement.webURL}</a></td>
											<c:if
												test="${not empty bannerProdCarouselElement.productCode}">
												<td>${bannerProdCarouselElement.productCode.code}</td>
											</c:if>
										</tr>
									</c:if>
								</div>
							</c:forEach>
						</table>
					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'VideoProductCarouselComponent'}">
					<div>
						<h1>VideoProductCarouselComponent</h1>
						<table class="" border="1">
							<thead>
								<tr>
									<th>title</th>
									<th>webURL</th>
									<th>imageURL</th>
									<th>description</th>
									<th>btnText</th>
									<th>brandLogo</th>
									<th>videoURL</th>
								</tr>
							</thead>
							<tr>
								<td>${feature.title}</td>
								<td><a href="${feature.webURL}">${feature.webURL}</a></td>
								<td><img alt="" src="${feature.imageURL.URL}"></td>
								<td>${feature.description}</td>
								<td>${feature.btnText}</td>
								<td><img alt="" src="${feature.brandLogo.URL}"></td>
								<td><a href="${feature.videoURL}">${feature.videoURL}</a></td>
							</tr>
						</table>
						<table class="">
							<thead>
								<tr>
									<th>webURL</th>
									<th>title</th>
									<th>productCode</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}"
								var="videoProductCarouselElement">
								<div>
									<c:if
										test="${videoProductCarouselElement.typeCode eq 'VideoProductCarouselElement'}">
										<tr>
											<td>${videoProductCarouselElement.title}</td>
											<td><a href="${videoProductCarouselElement.webURL}">${videoProductCarouselElement.webURL}</a></td>
											<c:if
												test="${not empty videoProductCarouselElement.productCode}">
												<td>${videoProductCarouselElement.productCode.code}</td>
											</c:if>
										</tr>
									</c:if>
								</div>
							</c:forEach>
						</table>
					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'ThemeOffersComponent'}">
					<div>
						<h1>ThemeOffersComponent</h1>
						<table class="">
							<thead>
								<tr>
									<th>Title</th>
									<th>backgroundHexCode</th>
									<th>backgroundImageURL</th>
									<th>btnText</th>
									<th>webURL</th>
								</tr>
							</thead>
							<tr>
								<td>${feature.title}</td>
								<td>${feature.backgroundHexCode}</td>
								<td><img alt="" src="${feature.backgroundImageURL.URL}"></td>
								<td>${feature.btnText}</td>
								<td><a href="${feature.webURL}">${feature.webURL}</a></td>
							</tr>
						</table>
						<table class="">
							<thead>
								<tr>
									<th>title</th>
									<th>description</th>
									<th>imageURL</th>
									<th>webURL</th>
								</tr>
							</thead>
							<c:forEach items="${feature.offers}"
								var="themeOffersCompOfferElement">
								<div>
									<c:if
										test="${themeOffersCompOfferElement.typeCode eq 'ThemeOffersCompOfferElement'}">
										<tr>
											<td>${themeOffersCompOfferElement.title}</td>
											<td>${themeOffersCompOfferElement.description}</td>
											<td><img alt=""
												src="${themeOffersCompOfferElement.imageURL.URL}"></td>
											<td><a href="${themeOffersCompOfferElement.webURL}">${themeOffersCompOfferElement.webURL}</a></td>
										</tr>
									</c:if>
								</div>
							</c:forEach>
						</table>
						<table class="">
							<thead>
								<tr>
									<th>title</th>
									<th>webURL</th>
									<th>productCode</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}" var="themeOffersItemsElement">
								<div>
									<c:if
										test="${themeOffersItemsElement.typeCode eq 'ThemeOffersItemsElement'}">
										<tr>
											<td>${themeOffersItemsElement.title}</td>
											<td><a href="${themeOffersItemsElement.webURL}">${themeOffersItemsElement.webURL}</a></td>
											<c:if test="${not empty themeOffersItemsElement.productCode}">
												<td>${themeOffersItemsElement.productCode.code}</td>
											</c:if>
										</tr>
									</c:if>
								</div>
							</c:forEach>
						</table>

					</div>
				</c:if>


				<c:if test="${feature.typeCode eq 'ThemeProductWidgetComponent'}">
					<div>
						<h1>ThemeProductWidgetComponent</h1>
						<table class="">
							<thead>
								<tr>
									<th>imageURL</th>
									<th>brandLogo</th>
									<th>btnText</th>
									<th>title</th>
								</tr>
							</thead>
							<tr>
								<td><img alt="" src="${feature.imageURL.URL}"></td>
								<td><img alt="" src="${feature.brandLogo.URL}"></td>
								<td>${feature.btnText}</td>
								<td>${feature.title}</td>
							</tr>
						</table>
						<table class="">
							<thead>
								<tr>
									<th>webURL</th>
									<th>title</th>
									<th>productCode</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}"
								var="themeProductWidgetElement">
								<div>
									<c:if
										test="${themeProductWidgetElement.typeCode eq 'ThemeProductWidgetElement'}">
										<tr>
											<td><a href="${themeProductWidgetElement.webURL}">${themeProductWidgetElement.webURL}</a></td>
											<td>${themeProductWidgetElement.title}</td>
											<c:if
												test="${not empty themeProductWidgetElement.productCode}">
												<td>${themeProductWidgetElement.productCode.code}</td>
											</c:if>
										</tr>
									</c:if>
								</div>
							</c:forEach>
						</table>
					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'BannerSeparatorComponent'}">
					<div>
						<h1>BannerSeparatorComponent</h1>
						<table class="">
							<thead>
								<tr>
									<th>Title</th>
									<th>Description</th>
									<th>iconImageURL</th>
									<th>Start Hex Code</th>
									<th>End Hex Code</th>
									<th>Web URL</th>
								</tr>
							</thead>
							<tr>
								<td>${feature.title}</td>
								<td>${feature.description}</td>
								<td><img alt="" src="${feature.iconImageURL.URL}"></td>
								<td>${feature.startHexCode}</td>
								<td>${feature.endHexCode}</td>
								<td><a href="${feature.webURL}"> ${feature.webURL}</a></td>
							</tr>
						</table>
					</div>
				</c:if>

				<c:if
					test="${feature.typeCode eq 'AutomatedBrandProductCarouselComponent'}">
					<div>
						<h1>AutomatedBrandProductCarouselComponent</h1>
						<table class="">
							<thead>
								<tr>
									<th>imageURL</th>
									<th>brandLogo</th>
									<th>btnText</th>
									<th>webURL</th>
									<th>Description</th>
								</tr>
							</thead>
							<tr>
								<td><img alt="" src="${feature.imageURL.URL}"></td>
								<td><img alt="" src="${feature.brandLogo.URL}"></td>
								<td>${feature.btnText}</td>
								<td><a href="${feature.webURL}"> ${feature.webURL}</a></td>
								<td>${feature.description}</td>
							</tr>
						</table>
						<table class="">
							<thead>
								<tr>
									<th>webURL</th>
									<th>title</th>
									<th>productCode</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}"
								var="automatedBrandProductCarElement">
								<div>
									<c:if
										test="${automatedBrandProductCarElement.typeCode eq 'AutomatedBrandProductCarElement'}">
										<tr>
											<td><a href="${automatedBrandProductCarElement.webURL}">${automatedBrandProductCarElement.webURL}</a></td>
											<td>${automatedBrandProductCarElement.title}</td>
											<c:if
												test="${not empty automatedBrandProductCarElement.productCode}">
												<td>${automatedBrandProductCarElement.productCode.code}</td>
											</c:if>
										</tr>
									</c:if>
								</div>
							</c:forEach>
						</table>
					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'CuratedListingStripComponent'}">
					<div>
						<h1>CuratedListingStripComponent</h1>
						<table class="">
							<thead>
								<tr>
									<th>Title</th>
									<th>Start Hex Code</th>
									<th>Web URL</th>
								</tr>
							</thead>
							<tr>
								<td>${feature.title}</td>
								<td>${feature.startHexCode}</td>
								<td><a href="${feature.webURL}"> ${feature.webURL}</a></td>
							</tr>
						</table>
					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'MonoBLPBannerComponent'}">
					<div>
						<h1>MonoBLPBannerComponent</h1>
						<table class="">
							<thead>
								<tr>
									<th>title</th>
								</tr>
							</thead>
							<tr>
								<td>${feature.title}</td>
							</tr>
						</table>
						<table class="">
							<thead>
								<tr>
									<th>btnText</th>
									<th>hexCode</th>
									<th>title</th>
									<th>webURL</th>
									<th>imageURL</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}" var="monoBLPBannerElement">
								<div>
									<c:if
										test="${monoBLPBannerElement.typeCode eq 'MonoBLPBannerElement'}">
										<tr>
											<td>${monoBLPBannerElement.btnText}</td>
											<td>${monoBLPBannerElement.hexCode}</td>
											<td>${monoBLPBannerElement.title}</td>
											<td><a href="${monoBLPBannerElement.webURL}">
													${monoBLPBannerElement.webURL}"></a></td>
											<td><img alt=""
												src="${monoBLPBannerElement.imageURL.URL}"></td>
										</tr>
									</c:if>
								</div>
							</c:forEach>
						</table>
					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'SubBrandBannerBLPComponent'}">
					<div>
						<h1>SubBrandBannerBLPComponent</h1>
						<table class="">
							<thead>
								<tr>
									<th>title</th>
								</tr>
							</thead>
							<tr>
								<td>${feature.title}</td>
							</tr>
						</table>
						<table class="">
							<thead>
								<tr>
									<th>webURL</th>
									<th>imageURL</th>
									<th>brandLogo</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}"
								var="subBrandBannerBLPElement">
								<div>
									<c:if
										test="${subBrandBannerBLPElement.typeCode eq 'SubBrandBannerBLPElement'}">
										<tr>
											<td><a href="${subBrandBannerBLPElement.webURL}">
													${subBrandBannerBLPElement.webURL}</a></td>
											<td><img alt=""
												src="${subBrandBannerBLPElement.imageURL.URL}"></td>
											<td><img alt=""
												src="${subBrandBannerBLPElement.brandLogo.URL}"></td>
										</tr>
									</c:if>
								</div>
							</c:forEach>
						</table>
					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'TopCategoriesWidgetComponent'}">
					<div>
						<h1>TopCategoriesWidgetComponent</h1>
						<table class="">
							<thead>
								<tr>
									<th>title</th>
								</tr>
							</thead>
							<tr>
								<td>${feature.title}</td>
							</tr>
						</table>
						<table class="">
							<thead>
								<tr>
									<th>title</th>
									<th>webURL</th>
									<th>imageURL</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}"
								var="topCategoriesWidgetElement">
								<div>
									<c:if
										test="${topCategoriesWidgetElement.typeCode eq 'TopCategoriesWidgetElement'}">
										<tr>
											<td>${topCategoriesWidgetElement.title}</td>
											<td><a href="${topCategoriesWidgetElement.webURL}">
													${topCategoriesWidgetElement.webURL}</a></td>
											<td><img alt=""
												src="${topCategoriesWidgetElement.imageURL.URL}"></td>
										</tr>
									</c:if>
								</div>
							</c:forEach>
						</table>
					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'CuratedProductsWidgetComponent'}">
					<div>
						<h1>CuratedProductsWidgetComponent</h1>
						<table class="">
							<thead>
								<tr>
									<th>title</th>
									<th>webURL</th>
									<th>btnText</th>
								</tr>
							</thead>
							<tr>
								<td>${feature.title}</td>
								<td><a href="${feature.webURL}"> ${feature.webURL}</a></td>
								<td>${feature.btnText}</td>
							</tr>
						</table>
						<table class="">
							<thead>
								<tr>
									<th>webURL</th>
									<th>title</th>
									<th>productCode</th>
									<th>description</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}"
								var="curatedProductsWidgetElement">
								<div>
									<c:if
										test="${curatedProductsWidgetElement.typeCode eq 'CuratedProductsWidgetElement'}">
										<tr>
											<td><a href="${curatedProductsWidgetElement.webURL}">${curatedProductsWidgetElement.webURL}</a></td>
											<td>${curatedProductsWidgetElement.title}</td>
											<c:if
												test="${not empty curatedProductsWidgetElement.productCode}">
												<td>${curatedProductsWidgetElement.productCode.code}</td>
											</c:if>
											<td>${curatedProductsWidgetElement.description}</td>
										</tr>
									</c:if>
								</div>
							</c:forEach>
						</table>
					</div>
				</c:if>


				<c:if test="${feature.typeCode eq 'MSDComponent'}">
					<div>
						<h1>MSDComponent</h1>
						<table class="">
							<thead>
								<tr>
									<th>subType</th>
									<th>num_results</th>
									<th>details</th>
								</tr>
							</thead>
							<tr>
								<td>${feature.subType}</td>
								<td>${feature.num_results}</td>
								<td>${feature.details}</td>
							</tr>
						</table>
					</div>
				</c:if>


				<c:if test="${feature.typeCode eq 'AdobeTargetComponent'}">
					<div>
						<h1>AdobeTargetComponent</h1>
						<table class="">
							<thead>
								<tr>
									<th>mbox</th>
								</tr>
							</thead>
							<tr>
								<td>${feature.mbox}</td>
							</tr>
						</table>
					</div>
				</c:if>




				<c:if test="${feature.typeCode eq 'BrandsTabAZListComponent'}">
					<div>
						<h1>BrandsTabAZListComponent</h1>
						<table class="">
							<thead>
								<tr>
									<th>subType</th>

								</tr>

							</thead>
							<c:forEach items="${feature.items}" var="brandsTabAZElement">
								<div>
									<c:if
										test="${brandsTabAZElement.typeCode eq 'BrandsTabAZElement'}">
										<tr>
											<td style="font-weight: bold;">${brandsTabAZElement.subType}</td>

										</tr>
										<br>
									</c:if>
								</div>

								<table class="">
									<thead>
										<tr>
											<th>webURL</th>
											<th>brandName</th>
										</tr>
									</thead>
									<c:forEach items="${brandsTabAZElement.brands}"
										var="brandTabAZBrandElement">
										<div>
											<c:if
												test="${brandTabAZBrandElement.typeCode eq 'BrandTabAZBrandElement'}">
												<tr>
													<td><a href="${brandTabAZBrandElement.webURL}">${brandTabAZBrandElement.webURL}</a></td>
													<td>${brandTabAZBrandElement.brandName}</td>
												</tr>
											</c:if>
										</div>
									</c:forEach>
								</table>

								<c:forEach items="${brandsTabAZElement.items}"
									var="brandTabAZHeroBannerElement">


									<c:if
										test="${brandTabAZHeroBannerElement.typeCode eq 'HeroBannerComponent'}">
										<div>
											<c:forEach items="${brandTabAZHeroBannerElement.items}"
												var="heroElements">
												<c:if
													test="${not empty heroElements && heroElements.typeCode eq 'HeroBannerElement'}">
													<h1>HeroBannerComponent oF BrandsTabAZListComponent--
														${brandsTabAZElement.subType}</h1>
													<table class="">
														<thead>
															<tr>
																<th>title</th>
																<th>webURL</th>
																<th>imageURL</th>
																<th>brandLogo</th>
															</tr>
														</thead>

														<tr>
															<td>${heroElements.title}</td>
															<td><a href="${heroElements.webURL}">${heroElements.webURL}</a></td>
															<td><img alt="" src="${heroElements.imageURL.URL}"></td>
															<td><img alt="" src="${heroElements.brandLogo.URL}"></td>
														</tr>

													</table>
												</c:if>

											</c:forEach>

										</div>
									</c:if>


									<div>
										<c:if
											test="${brandTabAZBrandElement.typeCode eq 'BrandTabAZBrandElement'}">
											<tr>
												<td><a href="${brandTabAZBrandElement.webURL}">${brandTabAZBrandElement.webURL}</a></td>
												<td>${brandTabAZBrandElement.brandName}</td>
											</tr>
										</c:if>
									</div>
								</c:forEach>

							</c:forEach>
						</table>


					</div>
				</c:if>


				<c:if test="${feature.typeCode eq 'LandingPageTitleComponent'}">
					<div>
						<h1>LandingPageTitleComponent</h1>
						<table class="">
							<thead>
								<tr>
									<th>title</th>
								</tr>
							</thead>
							<tr>
								<td>${feature.title}</td>
							</tr>
						</table>
					</div>
				</c:if>


				<c:if test="${feature.typeCode eq 'LandingPageHeaderComponent'}">
					<div>
						<h1>LandingPageHeaderComponent</h1>
						<table class="">
							<thead>
								<tr>
									<th>title</th>
									<th>webURL</th>
									<th>imageURL</th>
									<th>brandLogo</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}"
								var="landingPageHeaderElement">
								<div>
									<c:if
										test="${landingPageHeaderElement.typeCode eq 'LandingPageHeaderElement'}">
										<tr>
											<td>${landingPageHeaderElement.title}</td>
											<td><a href="${landingPageHeaderElement.webURL}">
													${landingPageHeaderElement.webURL}</a></td>
											<td><img alt=""
												src="${landingPageHeaderElement.imageURL.URL}"></td>
											<td><img alt=""
												src="${landingPageHeaderElement.brandLogo.URL}"></td>
										</tr>
									</c:if>
								</div>
							</c:forEach>
						</table>
					</div>
				</c:if>

				<c:if
					test="${feature.typeCode eq 'AutoProductRecommendationComponent'}">
					<div>
						<h1>AutoProductRecommendationComponent</h1>
						<table class="">
							<thead>
								<tr>
									<th>title</th>
									<th>btnText</th>
									<th>fetchURL</th>
									<th>backupURL</th>
									<th>widgetPlatform</th>
								</tr>
							</thead>
							<tr>
								<td>${feature.title}</td>
								<td>${feature.btnText}</td>
								<td><a href="${feature.fetchURL}"> ${feature.fetchURL}</a></td>
								<td><a href="${feature.backupURL}">
										${feature.backupURL}</a></td>
								<td>${feature.widgetPlatform}</td>
							</tr>
						</table>
						<table class="">
							<thead>
								<tr>
									<th>title</th>
									<th>webURL</th>
									<th>productCode</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}"
								var="autoProductRecommendationElement">
								<div>
									<c:if
										test="${autoProductRecommendationElement.typeCode eq 'AutoProductRecommendationElement'}">
										<tr>
											<td>${autoProductRecommendationElement.title}</td>
											<td><a href="${autoProductRecommendationElement.webURL}">${autoProductRecommendationElement.webURL}</a></td>
											<c:if
												test="${not empty autoProductRecommendationElement.productCode}">
												<td>${autoProductRecommendationElement.productCode.code}</td>
											</c:if>
										</tr>
									</c:if>
								</div>
							</c:forEach>
						</table>
					</div>
				</c:if>


				<c:if test="${feature.typeCode eq 'LandingPageHierarchyComponent'}">
					<div>
						<h1>LandingPageHierarchyComponent</h1>
						<table class="">
							<thead>
								<tr>
									<th>title</th>
								</tr>
							</thead>
							<tr>
								<td>${feature.title}</td>
							</tr>
						</table>
						<table class="">
							<thead>
								<tr>
									<th>title</th>
									<th>webURL</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}"
								var="landingPageHierarchyElement">
								<div>
									<c:if
										test="${landingPageHierarchyElement.typeCode eq 'LandingPageHierarchyElement'}">
										<tr>
											<td>${landingPageHierarchyElement.title}</td>
											<td><a href="${landingPageHierarchyElement.webURL}">${landingPageHierarchyElement.webURL}</a></td>
										</tr>
									</c:if>
								</div>
								<table class="">
									<thead>
										<tr>
											<th>title</th>
											<th>webURL</th>
										</tr>
									</thead>
									<c:forEach items="${landingPageHierarchyElement.items}"
										var="landingPageHierarchyElementList">
										<div>
											<c:if
												test="${landingPageHierarchyElementList.typeCode eq 'LandingPageHierarchyElementList'}">
												<tr>
													<td>${landingPageHierarchyElementList.title}</td>
													<td><a
														href="${landingPageHierarchyElementList.webURL}">${landingPageHierarchyElementList.webURL}</a></td>
												</tr>
											</c:if>
										</div>
									</c:forEach>
								</table>
							</c:forEach>
						</table>

					</div>
				</c:if>

			</cms:pageSlot>
		</div>
	</div>
</div>
<script type="text/javascript"
	src="_ui/responsive/common/js/mpl/slick.min.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$('.hero-slider').slick({
			centerMode : true,
			centerPadding : '60px',
			slidesToShow : 3,
			responsive : [ {
				breakpoint : 480,
				settings : {
					arrows : false,
					centerMode : true,
					centerPadding : '20px',
					slidesToShow : 1,
					useCSS : true,
					cssEase : 'ease'
				}
			} ]
		});

		$('.offer-slider').slick({
			centerMode : true,
			centerPadding : '60px',
			slidesToShow : 1,
			responsive : [ {
				breakpoint : 480,
				settings : {
					arrows : false,
					centerMode : true,
					centerPadding : '10px',
					slidesToShow : 1,
					useCSS : true,
					cssEase : 'ease'
				}
			} ]
		});
	});
</script>
	</html>
</template:page>