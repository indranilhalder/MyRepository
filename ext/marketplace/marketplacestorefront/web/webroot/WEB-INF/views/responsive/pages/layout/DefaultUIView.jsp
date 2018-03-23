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

tr:nth-child(even) {
	background-color: #f2f2f2
}

th {
	background-color: #514848;
	color: white;
	height: 30px !important;
	text-align: center;
}
</style>

<template:page pageTitle="${pageTitle}">
	<html>

<div class="sub-brand">
	<div class="feature-collections">
		<div class="wrapper background">
			<cms:pageSlot position="DefaultNewUIContentSlot" var="feature">
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
											<td>${heroElements.title}</td>
											<td><a href="${heroElements.webURL}">${heroElements.webURL}</a></td>
											<td><img alt="" src="${heroElements.imageURL.URL}"></td>
											<td><img alt="" src="${heroElements.brandLogo.URL}"></td>
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
								<td>${feature.subType}</td>
								<td>${feature.title}</td>
								<td>${feature.description}</td>
								<td>${feature.btnText}</td>
								<td><img alt="" src="${feature.iconImageURL.URL}"></td>
								<td><img alt="" src="${feature.backgroundImageURL.URL}"></td>
								<td><a href="${feature.webURL}"> ${feature.webURL}"></a></td>
								<td>${feature.startHexCode}</td>
								<td>${feature.endHexCode}</td>
							</tr>
						</table>
					</div>
				</c:if>

				<c:if test="${feature.typeCode eq 'OffersWidgetComponent'}">
					<div>
						<h1>OffersWidgetComponent</h1>

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
									<th>Title</th>
									<th>Web URL</th>
									<th>Image URL</th>
									<th>Btn Text</th>
									<th>Discount Text</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}" var="offersWidgetElement">
								<div>
									<c:if
										test="${offersWidgetElement.typeCode eq 'OffersWidgetElement'}">
										<tr>
											<td>${offersWidgetElement.title}</td>
											<td><a href="${offersWidgetElement.webURL}">${offersWidgetElement.webURL}</a></td>
											<td><img alt="" src="${offersWidgetElement.imageURL.URL}"></td>
											<td>${offersWidgetElement.btnText}</td>
											<td>${offersWidgetElement.discountText}</td>
										</tr>
									</c:if>
								</div>
							</c:forEach>
						</table>
						<%-- 					</c:if> --%>
					</div>
				</c:if>
				
				<c:if test="${feature.typeCode eq 'SmartFilterWidgetComponent'}">
					<div>
						<h1>SmartFilterWidgetComponent</h1>
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
					<div>
						<h1>FlashSalesComponent</h1>
						<table class="">
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
								<td>${feature.title}</td>
							</tr>
							<tr>
								<td>${feature.description}</td>
							</tr>
							<tr>
								<td>${feature.backgroundHexCode}</td>
							</tr>
							<tr>
								<td><img alt="" src="${feature.backgroundImageURL.URL}"></td>
							</tr>
							<tr>
								<td>${feature.startDate}</td>
							</tr>
							<tr>
								<td>${feature.endDate}</td>
							</tr>
							<tr>
								<td>${feature.btnText}</td>
							</tr>
							<tr>
								<td><a href="${feature.webURL}"> ${feature.webURL}"></a></td>
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
						<table class="">
							<thead>
								<tr>
									<th>webURL</th>
									<th>title</th>
									<th>ProductCode</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}" var="flashsalesitemElements">
								<div>
									<c:if
										test="${flashsalesitemElements.typeCode eq 'FlashSalesItemElement'}">
										
										<tr>
											<td>${flashsalesitemElements.title}</td>
											<td><a href="${flashsalesitemElements.webURL}">${flashsalesitemElements.webURL}</a></td>
											<c:if test="flashsalesitemElements.ProductCode">
											<td>${flashsalesitemElements.ProductCode.code}</td>
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
											<td>${feature.btnText}</td>
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
						<table class="">
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
							</tr>
							<tr>
								<td><a href="${feature.webURL}">${feature.webURL}</a></td>
							</tr>
							<tr>
								<td><img alt="" src="${feature.imageURL.URL}"></td>
							</tr>
							<tr>
								<td>${feature.description}</td>
							</tr>
							<tr>
								<td>${feature.btnText}</td>
							</tr>
						</table>
						<table class="">
							<thead>
								<tr>
									<th>webURL</th>
									<th>title</th>
									<th>ProductCode</th>
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
											<c:if test="bannerProdCarouselElement.ProductCode">
											<td>${bannerProdCarouselElement.ProductCode.code}</td>
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
						<table class="">
							<thead>
								<tr>
									<th>title</th>
									<th>webURL</th>
									<th>imageURL</th>
									<th>description</th>
									<th>btnText</th>
									<th>videoURL</th>
									<th>brandLogo</th>
								</tr>
							</thead>
							<tr>
								<td>${feature.title}</td>
							</tr>
							<tr>
								<td><a href="${feature.webURL}">${feature.webURL}</a></td>
							</tr>
							<tr>
								<td><img alt="" src="${feature.imageURL.URL}"></td>
							</tr>
							<tr>
								<td>${feature.description}</td>
							</tr>
							<tr>
								<td>${feature.btnText}</td>
							</tr>
							<tr>
								<td><img alt="" src="${feature.brandLogo.URL}"></td>
							</tr>
							<tr>
								<td><img alt="" src="${feature.videoURL}"></td>
							</tr>
						</table>
						<table class="">
							<thead>
								<tr>
									<th>webURL</th>
									<th>title</th>
									<th>ProductCode</th>
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
											<c:if test="videoProductCarouselElement.ProductCode">
											<td>${videoProductCarouselElement.ProductCode.code}</td>
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
							</tr>
							<tr>
								<td>${feature.backgroundHexCode}</td>
							</tr>
							<tr>
								<td><img alt="" src="${feature.backgroundImageURL}"></td>
							</tr>
							<tr>
								<td>${feature.btnText}</td>
							</tr>
							<tr>
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
									<th>ProductCode</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}" var="themeOffersItemsElement">
								<div>
									<c:if
										test="${themeOffersItemsElement.typeCode eq 'ThemeOffersItemsElement'}">
										<tr>
											<td>${themeOffersItemsElement.title}</td>
											<td><a href="${themeOffersItemsElement.webURL}">${themeOffersItemsElement.webURL}</a></td>
											<c:if test="themeOffersItemsElement.ProductCode">
											<td>${themeOffersItemsElement.ProductCode.code}</td>
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
							</tr>
							<tr>
								<td><img alt="" src="${feature.brandLogo.URL}"></td>
							</tr>
							<tr>
								<td>${feature.btnText}</td>
							</tr>
							<tr>
								<td>${feature.title}</td>
							</tr>
						</table>
						<table class="">
							<thead>
								<tr>
									<th>webURL</th>
									<th>title</th>
									<th>ProductCode</th>
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
											<c:if test="themeProductWidgetElement.ProductCode">
											<td>${themeProductWidgetElement.ProductCode.code}</td>
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
								<td><a href="${feature.webURL}"> ${feature.webURL}"></a></td>
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
							</tr>
							<tr>
								<td><img alt="" src="${feature.brandLogo.URL}"></td>
							</tr>
							<tr>
								<td>${feature.btnText}</td>
							</tr>
							<tr>
								<td><a href="${feature.webURL}"> ${feature.webURL}"></a></td>
							</tr>
							<tr>
								<td>${feature.description}</td>
							</tr>
						</table>
						<table class="">
							<thead>
								<tr>
									<th>webURL</th>
									<th>title</th>
									<th>ProductCode</th>
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
											<c:if test="automatedBrandProductCarElement.ProductCode">
											<td>${automatedBrandProductCarElement.ProductCode.code}</td>
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
								<td><a href="${feature.webURL}"> ${feature.webURL}"></a></td>
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
													${subBrandBannerBLPElement.webURL}"></a></td>
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
													${topCategoriesWidgetElement.webURL}"></a></td>
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
							</tr>
							<tr>
								<td><a href="${feature.webURL}"> ${feature.webURL}"></a></td>
							</tr>
							<tr>
								<td>${feature.btnText}</td>
							</tr>
						</table>
						<table class="">
							<thead>
								<tr>
									<th>webURL</th>
									<th>title</th>
									<th>ProductCode</th>
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
											<c:if test="curatedProductsWidgetElement.ProductCode">
											<td>${curatedProductsWidgetElement.ProductCode.code}</td>
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
									<th>brands</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}" var="brandsTabAZElement">
								<div>
									<c:if
										test="${brandsTabAZElement.typeCode eq 'BrandsTabAZElement'}">
										<tr>
											<td>${brandsTabAZElement.subType}</td>
											<td>${brandsTabAZElement.brands}</td>
										</tr>
									</c:if>
								</div>

								<table class="">
									<thead>
										<tr>
											<th>webURL</th>
											<th>brandName</th>
										</tr>
									</thead>
									<c:forEach items="${brandsTabAZElement.items}"
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
													${landingPageHeaderElement.webURL}"></a></td>
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
							</tr>
							<tr>
								<td>${feature.btnText}</td>
							</tr>
							<tr>
								<td><a href="${feature.fetchURL}">
										${feature.fetchURL}</a></td>
							</tr>
							<tr>
								<td><a href="${feature.backupURL}">
										${feature.backupURL}</a></td>
							</tr>
							<tr>
								<td>${feature.widgetPlatform}</td>
							</tr>
						</table>
						<table class="">
							<thead>
								<tr>
									<th>title</th>
									<th>webURL</th>
									<th>ProductCode</th>
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
											<c:if test="autoProductRecommendationElement.ProductCode">
											<td>${autoProductRecommendationElement.ProductCode.code}</td>
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
													<td><a href="${landingPageHierarchyElementList.webURL}">${landingPageHierarchyElementList.webURL}</a></td>
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
	</html>
</template:page>