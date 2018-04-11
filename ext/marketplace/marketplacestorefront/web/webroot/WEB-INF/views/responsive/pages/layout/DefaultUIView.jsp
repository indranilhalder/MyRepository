<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css">
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<link href="https://fonts.googleapis.com/css?family=Rubik:400,500" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/_ui/responsive/common/css/slick.css" />
<link rel="stylesheet" type="text/css" href="/_ui/responsive/common/css/preview-style.css" />

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
							<a href="${not empty feature.webURL ? feature.webURL : '#'}"
								class="connect-banner-more">${feature.btnText}</a>
						</div>
					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'OffersWidgetComponent'}">
					<div>
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
					<div class="col-xs-12 smart-filter-widget mb40">
						<div class="col-xs-12 pad0 heading">${feature.title}</div>
						<c:forEach items="${feature.items}" var="smartfilterElements">
							<!--smart filter widget-->
							<div class="col-xs-6 pad0 mb16">
								<a href=#"> <img src="${smartfilterElements.imageURL.URL}"
									class="img-responsive" />
									<div class="col-xs-12 pad0 overlay-div right0">
										<div class="col-xs-12 category-title">${smartfilterElements.title}</div>
										<div class="col-xs-12 category-desc">${smartfilterElements.description}</div>
									</div>
								</a>
							</div>

						</c:forEach>
					</div>


				</c:if>

				<c:if test="${feature.typeCode eq 'FlashSalesComponent'}">
					<!--flash sales widget-->
					<!--pass background url,color values as dynamic-->
					<div class="col-xs-12 flash-sales-widget mb40"
						style="background-image:url(${feature.backgroundImageURL.URL}),linear-gradient(to bottom, ${feature.backgroundHexCode},${feature.backgroundHexCode});">
						<div class="col-xs-6 pad0 flash-sales-widget-title">${feature.title}</div>
						<div class="col-xs-6 pad0 flash-sales-widget-timer">
							<!--paas here dynamic values for end time-->
							<%-- 	 	<jsp:useBean id="now" class="java.util.Date" /> --%>
							<%-- 		<fmt:formatDate type="date" value="${now}" /> --%>
							<input type="hidden" name="end_time" id="end_time"
								value="2018-04-03 13:00:00"> <i class="fa fa-clock-o"
								aria-hidden="true"></i>
							<div class="time-digits" id="countdown"></div>
						</div>
						<div class="col-xs-12 pad0 flash-sales-widget-text">${feature.description}</div>
						<c:set var="status" value="0" />
						<c:forEach items="${feature.offers}" var="flashsalesElements"
							begin="0" end="3" varStatus="loop">
							<c:if
								test="${flashsalesElements.typeCode eq 'FlashSalesElement'}">
								<div class="col-xs-6 flash-sales-widget-product">
									<a href="${flashsalesElements.webURL}"> <img
										src="${flashsalesElements.imageURL.URL}"
										class="img-responsive" />
										<div class="col-xs-12 flash-sales-widget-product-offer">${flashsalesElements.title}</div>
										<div class="col-xs-12 flash-sales-widget-product-name">${flashsalesElements.description}</div>
									</a>
								</div>
							</c:if>
							<c:set var="status" value="${loop.index}" />
						</c:forEach>

						<c:forEach items="${feature.items}" var="flashsalesitemElements"
							begin="0" end="${3 - loop.index}">
							<c:if
								test="${flashsalesitemElements.typeCode eq 'FlashSalesItemElement'}">
								<c:if test="${not empty flashsalesitemElements.productCode}">
									<div class="col-xs-6 flash-sales-widget-product">
										<a href="${flashsalesitemElements.webURL}"> <c:if
												test="${not empty flashsalesitemElements.productCode}">
												<img src="${flashsalesitemElements.productCode.thumbnail.URL}"
													class="img-responsive" />
											</c:if>
											<div class="col-xs-12 flash-sales-widget-product-offer">${flashsalesitemElements.title}</div>
											<c:if test="${not empty flashsalesitemElements.productCode}">
												<div class="col-xs-12 flash-sales-widget-product-name">${flashsalesitemElements.productCode.name}</div>
											</c:if>
										</a>
									</div>
								</c:if>
							</c:if>
						</c:forEach>
						<a href="${feature.webURL}" class="shop-all-btn">${feature.btnText}</a>
					</div>
				</c:if>


				<c:if test="${feature.typeCode eq 'ContentWidgetComponent'}">

					<!--content widget-->
					<div class="col-xs-12 mb40 content-widget">
						<div class="col-xs-12 pad0 heading">${feature.title}</div>
						<div class="content-widget-slider">

							<c:forEach items="${feature.items}" var="contentwidgetElement">
								<div>
									<c:if
										test="${contentwidgetElement.typeCode eq 'ContentWidgetElement'}">
										<div>
											<img src="${contentwidgetElement.imageURL.URL}" class="br4" />
											<div class="content-subsection">
												<div class="col-xs-12 pad0 content-title">${contentwidgetElement.title}</div>
												<div class="col-xs-12 pad0 content-desc">${contentwidgetElement.description}
												</div>
												<a href="${contentwidgetElement.webURL}"
													class="col-xs-12 pad0 content-link">${contentwidgetElement.btnText}</a>
											</div>
										</div>
									</c:if>
								</div>
							</c:forEach>
						</div>
					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'BannerProductCarouselComponent'}">
					<!--banner product carousel-->
					<div class="col-xs-12 mb40 pad0 banner-product-carousel">
						<img src="${feature.imageURL.URL}">
						<div class="col-xs-12 pad0 overlay-text">
							<div class="col-xs-12 banner-product-title">${feature.title}</div>
							<div class="col-xs-12 banner-product-desc">${feature.description}</div>
						</div>
						<div class="banner-product-slider">
							<c:forEach items="${feature.items}"
								var="bannerProdCarouselElement">
								<div>
									<c:if
										test="${bannerProdCarouselElement.typeCode eq 'BannerProdCarouselElementComp'}">
										<div>
											<a href="#"> <c:if
													test="${not empty bannerProdCarouselElement.productCode}">
													<img
														src="${bannerProdCarouselElement.productCode.thumbnail.URL}" />
												</c:if>
												<div class="product-name">${bannerProdCarouselElement.title}</div>
												<c:if
													test="${not empty bannerProdCarouselElement.productCode}">
													<fmt:parseNumber var="productPrice" type="number"
														value="${bannerProdCarouselElement.productCode.mrp}" />

													<div class="product-price">${productPrice}</div>
												</c:if>
											</a>
										</div>
									</c:if>
								</div>
							</c:forEach>
						</div>
						<a href="${feature.webURL}" class="shop-all-btn">${feature.btnText}</a>
					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'VideoProductCarouselComponent'}">
					<!--video product carousel-->
					<div class="col-xs-12 mb40 pad0 video-product-carousel">
						<video id="video-el" width="100%" src="${feature.videoURL}"
							poster="${feature.imageURL.URL}"></video>
						<!-- 						<img src=""> -->
						<div class="video-product-carousel-container" id="vpc-container">
							<div class="col-xs-12 video-product-title">${feature.title}</div>
							<div class="col-xs-12 text-center mtb15">
								<i class="fa fa-play-circle-o fa-3x" aria-hidden="true"
									id="play-video"></i>
							</div>
							<div class="col-xs-12 video-product-desc">${feature.description}</div>
						</div>

						<div class="video-product-slider">
							<c:forEach items="${feature.items}"
								var="videoProdCarouselElement">
								<div>
									<c:if
										test="${videoProdCarouselElement.typeCode eq 'VideoProductCarouselElement'}">
										<a href="#"> <c:if
												test="${not empty videoProdCarouselElement.productCode}">
												<img
													src="${videoProdCarouselElement.productCode.thumbnail.URL}" />
											</c:if>
											<div class="product-name">${videoProdCarouselElement.title}</div>
											<c:if
												test="${not empty videoProdCarouselElement.productCode}">
												<fmt:parseNumber var="productPrice" type="number"
													value="${videoProdCarouselElement.productCode.mrp}" />
												<div class="product-price">${productPrice}</div>
											</c:if>
										</a>
									</c:if>
								</div>
							</c:forEach>
						</div>
						<a href="${feature.webURL}" class="shop-all-btn">${feature.btnText}</a>
					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'ThemeOffersComponent'}">


					<!--  ============================================= -->
					<div class="col-xs-12 pad0 theme-offers mb40"
						style="background-image:url(${feature.backgroundImageURL.URL}),linear-gradient(to bottom, ${feature.backgroundHexCode}, ${feature.backgroundHexCode});">
						<div class="heading">${feature.title}</div>
						<div class="theme-offers-slider">
							<c:forEach items="${feature.offers}"
								var="themeOffersCompOfferElement">
								<c:if
									test="${themeOffersCompOfferElement.typeCode eq 'ThemeOffersCompOfferElement'}">
									<div>
										<a href="${themeOffersCompOfferElement.webURL}"> <img
											src="${themeOffersCompOfferElement.imageURL.URL}">
											<div class="brand-name">${themeOffersCompOfferElement.title}</div>
											<div class="product-name">${themeOffersCompOfferElement.description}</div>
										</a>
									</div>
								</c:if>

							</c:forEach>
							<c:forEach items="${feature.items}" var="themeOffersItemsElement">
								<c:if
									test="${themeOffersItemsElement.typeCode eq 'ThemeOffersItemsElement'}">
									<div>
										<a href="#"> <c:if
												test="${not empty themeOffersItemsElement.productCode}">
												<img
													src="${themeOffersItemsElement.productCode.thumbnail.URL}">
											</c:if>
											<div class="brand-name">${themeOffersItemsElement.title}</div>
											<div class="product-name">${themeOffersCompOfferElement.description}</div>
											<c:if test="${not empty themeOffersItemsElement.productCode}">
												<fmt:parseNumber var="productPrice" type="number"
													value="${themeOffersItemsElement.productCode.mrp}" />
												<div class="product-price">
													Rs. ${productPrice} <span class="line-through">Rs.
														${productPrice}</span>
												</div>
											</c:if>
										</a>
									</div>
								</c:if>

							</c:forEach>
						</div>
						<a href="${feature.webURL}" class="shop-all-btn">${feature.btnText}</a>
					</div>

					<!-- ================================================= -->

				</c:if>


				<c:if test="${feature.typeCode eq 'ThemeProductWidgetComponent'}">

					<div class="col-xs-12 pad0 theme-product-widget mb40"
						style="background-image:url('${feature.imageURL.URL}'),linear-gradient(to bottom, #5b0627, #5b0627);">
						<img src="${feature.brandLogo.URL}" class="brand-logo" />
						<div class="col-xs-12 theme-product-widget-container">
							<div class="col-xs-6 heading">${feature.title}</div>
							<div class="col-xs-6">
								<a href="#" class="shop-all-btn">${feature.btnText}</a>
							</div>
							<div class="col-xs-12 pad0 theme-product-widget-slider">

								<c:forEach items="${feature.items}"
									var="themeProductWidgetElement">
									<div>
										<c:if
											test="${themeProductWidgetElement.typeCode eq 'ThemeProductWidgetElement'}">
											<div>
												<a href="#"> <c:if
														test="${not empty themeProductWidgetElement.productCode}">
														<img
															src="${themeProductWidgetElement.productCode.thumbnail.URL}" />
													</c:if>
													<div class="product-name">${themeProductWidgetElement.title}</div>
													<c:if
														test="${not empty themeProductWidgetElement.productCode}">
														<fmt:parseNumber var="productPrice" type="number"
															value="${themeProductWidgetElement.productCode.mrp}" />
														<div class="product-price">${productPrice}</div>
													</c:if>
												</a>
											</div>
										</c:if>
									</div>
								</c:forEach>
							</div>
						</div>
					</div>

				</c:if>

				<c:if test="${feature.typeCode eq 'BannerSeparatorComponent'}">
					<div class="col-xs-12 pad0 mb40 banner-separator"
						style="background-image: linear-gradient(285deg, ${feature.startHexCode}, ${feature.endHexCode});">
						<a href="${feature.webURL}">
							<div class="col-xs-2">
								<img src="${feature.iconImageURL.URL}" class="pull-left mt2">
							</div>
							<div class="col-xs-10 pad0">
								<div class="col-xs-12 pad0 title">${feature.title}</div>
								<div class="col-xs-12 pad0 desc">${feature.description}</div>
							</div>
						</a>
					</div>
				</c:if>

				<c:if
					test="${feature.typeCode eq 'AutomatedBrandProductCarouselComponent'}">

					<!--Automated brand product carousel-->
					<div class="col-xs-12 mb40 pad0 auto-brand-product-carousel">
						<img src="${feature.imageURL.URL}">

						<div class="col-xs-12 auto-brand-product-logo">
							<img src="${feature.brandLogo.URL}" />
						</div>
						<div class="col-xs-12 auto-brand-product-desc">
							${feature.description}</div>

						<div class="auto-brand-product-slider">
							<c:forEach items="${feature.items}"
								var="automatedBrandProductCarElement">
								<c:if
									test="${automatedBrandProductCarElement.typeCode eq 'AutomatedBrandProductCarElement'}">
									<div>
										<a href="${automatedBrandProductCarElement.webURL}"> <c:if
												test="${not empty automatedBrandProductCarElement.productCode}">
												<img
													src="${automatedBrandProductCarElement.productCode.thumbnail.URL}" />
											</c:if>
											<div class="product-name">${automatedBrandProductCarElement.title}</div>
											<%-- <div class="product-price">${automatedBrandProductCarElement.productCode.code}</div> --%>
											<c:if
												test="${not empty automatedBrandProductCarElement.productCode}">
												<fmt:parseNumber var="productPrice" type="number"
													value="${automatedBrandProductCarElement.productCode.mrp}" />
												<div class="product-price">${productPrice}</div>
											</c:if>
										</a>
									</div>

								</c:if>
							</c:forEach>
						</div>
						<a href="${feature.webURL}" class="shop-all-btn">${feature.btnText}</a>
					</div>
				</c:if>


				<c:if test="${feature.typeCode eq 'CuratedListingStripComponent'}">
					<div class="col-xs-12 pad0 mb40 curated-listing-strip"
						style="background-color:${feature.startHexCode};">
						<a href="${feature.webURL}"> ${feature.title} <span
							class="pull-right"><i class="fa fa-arrow-right"
								aria-hidden="true"></i></span>
						</a>
					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'MonoBLPBannerComponent'}">
					<div class="col-xs-12 mono-blp-banner">
						<div class="col-xs-12 pad0 heading">${feature.title}</div>
						<!--mono blp banner-->

						<c:forEach items="${feature.items}" var="monoBLPBannerElement">
							<div class="col-xs-12 mono-blp-banner">
								<div class="col-xs-12 pad0 mono-blp-banner-container"
									style="background-image:url(${monoBLPBannerElement.imageURL.URL});linear-gradient(to bottom, ${monoBLPBannerElement.hexCode});">
									<div class="col-xs-12 banner-title">${monoBLPBannerElement.title}.</div>
									<a href="${monoBLPBannerElement.webURL}" class="banner-btn">${monoBLPBannerElement.btnText}</a>
								</div>
							</div>
						</c:forEach>
					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'SubBrandBannerBLPComponent'}">

					<div class="col-xs-12 pad0 sub-brand-banner-blp mb40">
						<div class="col-xs-12 sub-brand-heading">${feature.title}</div>
						<div class="col-xs-12 pad0 subbrand-banner-blp-slider">

							<c:forEach items="${feature.items}"
								var="subBrandBannerBLPElement">
								<c:if
									test="${subBrandBannerBLPElement.typeCode eq 'SubBrandBannerBLPElement'}">
									<div>
										<a href="${subBrandBannerBLPElement.webURL}"> <img
											src="${subBrandBannerBLPElement.imageURL.URL}"
											class="sub-brand-img" /> <img
											src="${subBrandBannerBLPElement.brandLogo.URL}"
											class="sub-brand-logo" />
										</a>
									</div>

								</c:if>

							</c:forEach>

						</div>
					</div>

				</c:if>

				<c:if test="${feature.typeCode eq 'TopCategoriesWidgetComponent'}">
					<div class="col-xs-12 pad0 mb40 top-categories-widget">
						<div class="col-xs-12 sub-brand-heading">${feature.title}</div>
						<c:forEach items="${feature.items}"
							var="topCategoriesWidgetElement" varStatus="loop">
							<c:if
								test="${topCategoriesWidgetElement.typeCode eq 'TopCategoriesWidgetElement'}">
								<c:if test="${loop.index eq 0}">
									<div class="col-xs-6 pr0">
										<a href="${topCategoriesWidgetElement.webURL}"> <img
											src="${topCategoriesWidgetElement.imageURL.URL}"
											class="img-responsive br4" />
											<div class="col-xs-12 category-title">${topCategoriesWidgetElement.title}s</div>
										</a>
									</div>
								</c:if>
								<c:if test="${loop.index gt 0}">
									<div class="col-xs-6 pl0">
										<c:if test="${loop.index eq 1}">
											<div class="col-xs-12 pad0 mb16">
												<a href="${topCategoriesWidgetElement.webURL}"> <img
													src="${topCategoriesWidgetElement.imageURL.URL}"
													class="img-responsive pull-right br4" />
													<div class="col-xs-12 category-title">${topCategoriesWidgetElement.title}</div>
												</a>
											</div>
										</c:if>
										<c:if test="${loop.index gt 1}">
											<div class="col-xs-12 pad0">
												<a href="${topCategoriesWidgetElement.webURL}"> <img
													src="${topCategoriesWidgetElement.imageURL.URL}"
													class="img-responsive pull-right br4" />
													<div class="col-xs-12 category-title">${topCategoriesWidgetElement.title}</div>
												</a>
											</div>
										</c:if>
									</div>
								</c:if>
							</c:if>
						</c:forEach>
					</div>
				</c:if>


				<c:if test="${feature.typeCode eq 'CuratedProductsWidgetComponent'}">


					<div class="col-xs-12 pad0 mb40 curated-products-widget">
						<div class="col-xs-12 heading">${feature.title}</div>
						<c:forEach items="${feature.items}"
							var="curatedProductsWidgetElement">
							<div class="col-xs-6">
								<a href="${feature.webURL}"> <c:if
										test="${not empty curatedProductsWidgetElement.productCode}">

										<img
											src="${curatedProductsWidgetElement.productCode.thumbnail.URL}"
											class="br4" />
									</c:if>
								</a>
								<c:if
									test="${curatedProductsWidgetElement.typeCode eq 'CuratedProductsWidgetElement'}">
									<div class="brand-name">${curatedProductsWidgetElement.title}
										<a href="#" class="pull-right"> <i
											class="fa fa-bookmark-o" aria-hidden="true"></i>
										</a>
									</div>
									<a href="${curatedProductsWidgetElement.webURL}">
										<div class="product-name">${curatedProductsWidgetElement.description}</div>
									</a>
									<div class="product-price">
										<c:if
											test="${not empty curatedProductsWidgetElement.productCode}">
											<fmt:parseNumber var="productPrice" type="number"
												value="${curatedProductsWidgetElement.productCode.mrp}" />
											<div class="product-price">${productPrice}</div>
										</c:if>
									</div>
								</c:if>
							</div>
						</c:forEach>
						<a href="#" class="shop-all-btn">${feature.btnText}</a>
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


					<!-- =========================== -->

					<div class="col-xs-12 pad0 brand-tab-azlist mb40">
						<ul class="nav nav-tabs" role="tablist">
							<c:forEach items="${feature.items}" var="brandsTabAZElement"
								varStatus="loop">
								<c:if
									test="${brandsTabAZElement.typeCode eq 'BrandsTabAZElement'}">

									<li role="presentation"
										class="${loop.index eq 0 ? 'active' : ''}"><a
										class="mytablist" href="#mytab${loop.index + 1}"
										aria-controls="mytab${loop.index + 1}" role="tab"
										data-toggle="tab"> ${brandsTabAZElement.subType} </a></li>
								</c:if>
							</c:forEach>
						</ul>
						
						<div class="tab-content">
						<c:forEach items="${feature.items}" var="brandsTabAZElement"
							varStatus="loop">
							
								<div role="tabpanel"
									class="tab-pane ${loop.index eq 0 ? ' active' : ''}"
									id="mytab${loop.index + 1}">
									<!--brands slider starts-->

									<c:forEach items="${brandsTabAZElement.items}"
										var="brandTabAZHeroBannerElement" begin="0" end="1">
										<c:if
											test="${brandTabAZHeroBannerElement.typeCode eq 'HeroBannerComponent'}">
											<div class="brands-slider mb40">
												<c:forEach items="${brandTabAZHeroBannerElement.items}"
													var="heroElements">
													<c:if
														test="${not empty heroElements && heroElements.typeCode eq 'HeroBannerElement'}">
														<div>
															<a href="${heroElements.webURL}"> <img
																src="${heroElements.imageURL.URL}">
																<div class="brands-slider-subsection">
																	<img class="brand-logo"
																		src="${heroElements.brandLogo.URL}">
																</div> <%-- 								    	<div class="banner-title">${heroElements.title}</div> --%>
															</a>
														</div>

													</c:if>
												</c:forEach>
											</div>
										</c:if>
									</c:forEach>
								
								<div class="col-xs-12">
									<input type="text" id="search-for-brand${loop.index + 1}"
										class="search-for-brand" data-id="${loop.index + 1}"
										placeholder="Search your brand"> <i
										class="fa fa-search searchicon-for-brand" aria-hidden="true"></i>
								</div>
								<div class="col-xs-12 all-brands-list">
									<div class="col-xs-11 pad0 brandslist-leftsection"
										id="brandslist-leftsection${loop.index + 1}">
										<c:set var="number" value="${fn:split('1,2,3,4,5,6,7,8,9', ',')}" />
										<c:forEach items="${number}" var="num">
											<div class="col-xs-2 pad0 text-bold" id="search_${num}">${num}</div>
											<ul class="col-xs-10 brandname-list">
												<c:forEach items="${brandsTabAZElement.brands}"
													var="brandTabAZBrandElement">
													<c:if
														test="${brandTabAZBrandElement.typeCode eq 'BrandTabAZBrandElement' and fn:startsWith(brandTabAZBrandElement.brandName, num)}">
														<li><a href="${brandTabAZBrandElement.webURL}">${brandTabAZBrandElement.brandName}</a></li>
													</c:if>
												</c:forEach>
											</ul>
										</c:forEach>

										<c:set var="alphabet"
											value="${fn:split('A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z', ',')}"
											scope="application" />
										<c:forEach items="${alphabet}" var="alpha">
											<div class="col-xs-2 pad0 text-bold" id="search_${alpha}">${alpha}</div>
											<ul class="col-xs-10 brandname-list">
												<c:forEach items="${brandsTabAZElement.brands}"
													var="brandTabAZBrandElement">
													<c:if
														test="${brandTabAZBrandElement.typeCode eq 'BrandTabAZBrandElement' and fn:startsWith(brandTabAZBrandElement.brandName, alpha)}">
														<li><a href="${brandTabAZBrandElement.webURL}">${brandTabAZBrandElement.brandName}</a></li>
													</c:if>
												</c:forEach>
											</ul>
										</c:forEach>
									</div>
									<div class="col-xs-1 pad0">
										<ul class="brandinitials-section"
											id="brandinitials-section${loop.index + 1}">
											<li><a href="#search_1"> # </a></li>
											<li><a href="#search_A"> A </a></li>
											<li><a href="#search_B"> B </a></li>
											<li><a href="#search_C"> C </a></li>
											<li><a href="#search_D"> D </a></li>
											<li><a href="#search_E"> E </a></li>
											<li><a href="#search_F"> F </a></li>
											<li><a href="#search_G"> G </a></li>
											<li><a href="#search_H"> H </a></li>
											<li><a href="#search_I"> I </a></li>
											<li><a href="#search_J"> J </a></li>
											<li><a href="#search_K"> K </a></li>
											<li><a href="#search_L"> L </a></li>
											<li><a href="#search_M"> M </a></li>
											<li><a href="#search_N"> N </a></li>
											<li><a href="#search_O"> O </a></li>
											<li><a href="#search_P"> P </a></li>
											<li><a href="#search_Q"> Q </a></li>
											<li><a href="#search_R"> R </a></li>
											<li><a href="#search_S"> S </a></li>
											<li><a href="#search_T"> T </a></li>
											<li><a href="#search_U"> U </a></li>
											<li><a href="#search_V"> V </a></li>
											<li><a href="#search_W"> W </a></li>
											<li><a href="#search_X"> X </a></li>
											<li><a href="#search_Y"> Y </a></li>
											<li><a href="#search_Z"> Z </a></li>
										</ul>
									</div>
								</div>
							</div>
						</c:forEach>
						</div>
					</div>

					<!--========================================================  -->

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
					<!--Landing Page Title Component,Landing Page Header Component-->

					<div>
						<c:forEach items="${feature.items}" var="landingPageHeaderElement">
							<div>
								<c:if
									test="${landingPageHeaderElement.typeCode eq 'LandingPageHeaderElement'}">
									<div class="col-xs-12 pad0 mb40 landing-page-header-component"
										style="background-image:url(${landingPageHeaderElement.imageURL.URL})">
										<a href="${landingPageHeaderElement.webURL}"
											class="follow-brand">follow</a> <img class="banner-logo"
											src="${landingPageHeaderElement.brandLogo.URL}" />
										<div class="banner-desc">
											${landingPageHeaderElement.title}</div>
									</div>
								</c:if>
							</div>
						</c:forEach>
						<!-- 						</table> -->
					</div>
				</c:if>

				<c:if
					test="${feature.typeCode eq 'AutoProductRecommendationComponent'}">
					<div class="col-xs-12 pad0 mb40 product-recommendation-widget">
						<div class="col-xs-12 heading">${feature.title}</div>
						<div class="col-xs-12 pad0 product-recommendation-slider">
							<c:forEach items="${feature.items}"
								var="autoProductRecommendationElement">
								<div>
									<c:if
										test="${autoProductRecommendationElement.typeCode eq 'AutoProductRecommendationElement'}">

										<a href="${autoProductRecommendationElement.webURL}"> <img
											src="${autoProductRecommendationElement.productCode.thumbnail.URL}"
											class="br4" />
										</a>
										<div class="brand-name">${autoProductRecommendationElement.title}
											<a href="#" class="pull-right"> <i
												class="fa fa-bookmark-o" aria-hidden="true"></i>
											</a>
										</div>
										<a href="${autoProductRecommendationElement.webURL}">
											<div class="product-name">${autoProductRecommendationElement.productCode.name}</div>
										</a>
										<!-- <div class="product-price">Rs. 4,950</div> -->
										<c:if
											test="${not empty autoProductRecommendationElement.productCode}">
											<fmt:parseNumber var="productPrice" type="number"
												value="${autoProductRecommendationElement.productCode.mrp}" />
											<div class="product-price">${productPrice}</div>
										</c:if>
									</c:if>
								</div>
							</c:forEach>
						</div>
						<a href="${feature.fetchURL}" class="shop-all-btn">${feature.btnText}</a>
					</div>
				</c:if>


				<c:if test="${feature.typeCode eq 'LandingPageHierarchyComponent'}">
					<div class="col-xs-12 pad0 landing-page-hierarchy">
						<div class="col-xs-12 landing-page-hierarchy-title">${feature.title}</div>
						<c:forEach items="${feature.items}"
							var="landingPageHierarchyElement">
							<c:if
								test="${landingPageHierarchyElement.typeCode eq 'LandingPageHierarchyElement'}">
								<ul class="category-l1">
									<li><a href="${landingPageHierarchyElement.webURL}"
										class="has-carrot">${landingPageHierarchyElement.title}</a> <c:forEach
											items="${landingPageHierarchyElement.items}"
											var="landingPageHierarchyElementList">
											<c:if
												test="${landingPageHierarchyElementList.typeCode eq 'LandingPageHierarchyElementList'}">
												<ul class="category-l2">
													<li><a
														href="${landingPageHierarchyElementList.webURL}">${landingPageHierarchyElementList.title}</a>
													</li>
												</ul>
											</c:if>
										</c:forEach></li>
								</ul>
							</c:if>
						</c:forEach>
						<div class="view-more-categories">
							View <span id="hidden_categories_count">2</span> <span
								id="hidden_categories_text">more</span> categories
						</div>
					</div>
				</c:if>

			</cms:pageSlot>
		</div>
	</div>
</div>
<!-- <script type="text/javascript" src="https://static.tatacliq.com/_ui/responsive/common/js/jquery-2.1.1.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> -->
<script type="text/javascript" src="/_ui/responsive/common/js/mpl/slick.min.js"></script>
<script type="text/javascript" src="/_ui/responsive/common/js/mpl/preview.js"></script>
	</html>
</template:page>