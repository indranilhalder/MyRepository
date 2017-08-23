<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>


<script>
var homePageBannerTimeout='${timeout}';
$(window).on("load", function(){
	setTimeout(function(){


	
	}, 2000);
});
</script>




		
<c:choose>
<c:when test="${empty timeout || timeout == 0}">
<div class="content-block-slider electronic-brand-slider">
	<div
		class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference electronic-rotatingImage"
		id="rotatingImage">
		<c:forEach items="${desktopView}" var="banner" varStatus="status">
			<c:if test="${ycommerce:evaluateRestrictions(banner)}">
				<c:url value="${banner.urlLink}" var="encodedUrl" />
				<div class="item slide">

					<%-- START: code for adding  3 sided and  4 sided banner component--%>
					<c:choose>
						<c:when test="${ banner.type eq 'Big 3 Sided Banner Component'}">
							<div class="hero icid">
								<div class="image">
									<img src="${banner.bannerImage.url}">
								</div> 
								
							<%--  <common:bannerImage view="${banner.bannerView.code}" image="${banner.bannerImage.url}"/> --%>
								
								<ul class="major-promos">
									<li data-bannerid="${banner.pk}">${banner.majorPromoText}</li>
								</ul>
								<ul class="minor-promos">
									<li data-bannerid="${banner.pk}">${banner.minorPromo1Text}</li>
									<li data-bannerid="${banner.pk}">${banner.minorPromo2Text}</li>
								</ul>
							</div>
						</c:when>


						<c:when test="${ banner.type eq 'Big 4 Sided Banner Component'}">
							<div class="hero icid fourPromoBanner">
								<div class="image">
									<img src="${banner.bannerImage.url}">
								</div> 
								
							 <%-- <common:bannerImage view="${banner.bannerView.code}" image="${banner.bannerImage.url}"/> --%>
								
								<ul class="major-promos">
									<li data-bannerid="${banner.pk}">${banner.promoText4}</li>
								</ul>

								<ul class="minor-promos">
									<li data-bannerid="${banner.pk}">${banner.promoText2}</li>
								</ul>

								<ul class="top-promos">
									<li data-bannerid="${banner.pk}">${banner.promoText1}</li>
								</ul>
								<ul class="bottom-promos">
									<li data-bannerid="${banner.pk}">${banner.promoText3}</li>
								</ul>
							</div>

						</c:when>

						<c:otherwise>
						<c:choose>
							<c:when test="${fn:contains(encodedUrl,'?')}">
								<span class="style_edit_title">${banner.headline}</span>
								<a tabindex="-1" href="${encodedUrl}&icid=${banner.pk}"
								<c:if test="${banner.external}"> target="_blank"</c:if>><img
								src="${banner.media.url}"
								alt="${not empty banner.headline ? banner.headline : banner.media.altText}"
								<%-- title="${not empty banner.headline ? banner.headline : banner.media.altText}" --%> class="call-to-action-banner" />
								
								</a>
							</c:when>
							<c:otherwise>
								<span class="style_edit_title">${banner.headline}</span>
								<a tabindex="-1" href="${encodedUrl}?icid=${banner.pk}"
								<c:if test="${banner.external}"> target="_blank"</c:if>><img
								src="${banner.media.url}"
								alt="${not empty banner.headline ? banner.headline : banner.media.altText}"
								<%-- title="${not empty banner.headline ? banner.headline : banner.media.altText}" --%> class="call-to-action-banner" />
							</a>
							</c:otherwise>
						</c:choose>
						</c:otherwise>
					</c:choose>
				</div>
			</c:if>
		</c:forEach>
	</div>
	</div>
	
	
	<div class="content-block-slider electronic-brand-slider">
	<div
		class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference electronic-rotatingImage"
		id="rotatingImageMobile">
		<c:forEach items="${mobileView}" var="banner" varStatus="status">
			<c:if test="${ycommerce:evaluateRestrictions(banner)}">
				<c:url value="${banner.urlLink}" var="encodedUrl" />
				<div class="item slide">

					<%-- START: code for adding  3 sided and  4 sided banner component--%>
					<c:choose>
						<c:when test="${ banner.type eq 'Big 3 Sided Banner Component'}">
							<div class="hero icid">
							<div class="image">
									<img src="${banner.bannerImage.url}">
								</div>
								
							<%--  <common:bannerImage view="${banner.bannerView.code}" image="${banner.bannerImage.url}"/> --%>
								
								<ul class="major-promos">
									<li data-bannerid="${banner.pk}">${banner.majorPromoText}</li>
								</ul>
								<ul class="minor-promos">
									<li data-bannerid="${banner.pk}">${banner.minorPromo1Text}</li>
									<li data-bannerid="${banner.pk}">${banner.minorPromo2Text}</li>
								</ul>
							</div>
						</c:when>


						<c:when test="${ banner.type eq 'Big 4 Sided Banner Component'}">
							<div class="hero icid fourPromoBanner">
								<div class="image">
									<img src="${banner.bannerImage.url}">
								</div>
								
							 <%-- <common:bannerImage view="${banner.bannerView.code}" image="${banner.bannerImage.url}"/> --%>
								
								<ul class="major-promos">
									<li data-bannerid="${banner.pk}">${banner.promoText4}</li>
								</ul>

								<ul class="minor-promos">
									<li data-bannerid="${banner.pk}">${banner.promoText2}</li>
								</ul>

								<ul class="top-promos">
									<li data-bannerid="${banner.pk}">${banner.promoText1}</li>
								</ul>
								<ul class="bottom-promos">
									<li data-bannerid="${banner.pk}">${banner.promoText3}</li>
								</ul>
							</div>

						</c:when>

						<c:otherwise>
						<c:choose>
							<c:when test="${fn:contains(encodedUrl,'?')}">
								<a tabindex="-1" href="${encodedUrl}&icid=${banner.pk}"
								<c:if test="${banner.external}"> target="_blank"</c:if>><img
								src="${banner.media.url}"
								alt="${not empty banner.headline ? banner.headline : banner.media.altText}"
								<%-- title="${not empty banner.headline ? banner.headline : banner.media.altText}" --%> class="call-to-action-banner" />
								</a>
							</c:when>
							<c:otherwise>
								<a tabindex="-1" href="${encodedUrl}?icid=${banner.pk}"
								<c:if test="${banner.external}"> target="_blank"</c:if>><img
								src="${banner.media.url}"
								alt="${not empty banner.headline ? banner.headline : banner.media.altText}"
								<%-- title="${not empty banner.headline ? banner.headline : banner.media.altText}" --%>  class="call-to-action-banner"/>
							</a>
							</c:otherwise>
						</c:choose>
						</c:otherwise>
					</c:choose>
				</div>
			</c:if>
		</c:forEach>
	</div>
	</div>
	</c:when>
	<c:otherwise>
	<div class="timeout-slider">
	<div
		class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference home-rotatingImage"
		id="rotatingImageTimeout">
		<c:forEach items="${desktopView}" var="banner" varStatus="status">
		
			<c:if test="${ycommerce:evaluateRestrictions(banner)}">
				<c:url value="${banner.urlLink}" var="encodedUrl" />
				<div class="item slide">
					<%-- START: code for adding  3 sided and  4 sided banner component--%>
					<c:choose>
						<c:when test="${ banner.type eq 'Big 3 Sided Banner Component'}">
							<div class="hero icid">
								
								
								<c:choose>
									<c:when test="${status.index eq 0}">
										 <div class="image">
											<img src="${banner.bannerImage.url}" />
										</div>
									</c:when>
									<c:otherwise>
										<div class="image">
											<img data-src="${banner.bannerImage.url}" style="display:none;" />
										</div>
									</c:otherwise>	
								</c:choose>
								
								
							 <%-- <common:bannerImage view="${banner.bannerView.code}" image="${banner.bannerImage.url}"/> --%>
								
								<ul class="major-promos">
									<li data-bannerid="${banner.pk}">${banner.majorPromoText}</li>
								</ul>
								<ul class="minor-promos">
									<li data-bannerid="${banner.pk}">${banner.minorPromo1Text}</li>
									<li data-bannerid="${banner.pk}">${banner.minorPromo2Text}</li>
								</ul>
							</div>
						</c:when>


						<c:when test="${ banner.type eq 'Big 4 Sided Banner Component'}">
							<div class="hero icid fourPromoBanner">
								<c:choose>
									<c:when test="${status.index eq 0}">
										 <div class="image">
											<img src="${banner.bannerImage.url}">
										</div>
									</c:when>
									<c:otherwise>
										<div class="image">
											<img data-src="${banner.bannerImage.url}" style="display:none;" />
										</div>
									</c:otherwise>	
								</c:choose>
								
							<%--  <common:bannerImage view="${banner.bannerView.code}" image="${banner.bannerImage.url}"/> --%>
								
								<ul class="major-promos">
									<li data-bannerid="${banner.pk}">${banner.promoText4}</li>
								</ul>

								<ul class="minor-promos">
									<li data-bannerid="${banner.pk}">${banner.promoText2}</li>
								</ul>

								<ul class="top-promos">
									<li data-bannerid="${banner.pk}">${banner.promoText1}</li>
								</ul>
								<ul class="bottom-promos">
									<li data-bannerid="${banner.pk}">${banner.promoText3}</li>
								</ul>
							</div>

						</c:when>

						<c:otherwise>
						<c:choose>
							<c:when test="${fn:contains(encodedUrl,'?')}">
								<span class="style_edit_title">${banner.headline}</span>
								<a tabindex="-1" href="${encodedUrl}&icid=${banner.pk}"
								<c:if test="${banner.external}"> target="_blank"</c:if>>
								<c:choose>
									<c:when test="${status.index eq 0}">
										 <img src="${banner.media.url}" alt="${not empty banner.headline ? banner.headline : banner.media.altText}" />
									</c:when>
									<c:otherwise>
										 <img data-src="${banner.media.url}" alt="${not empty banner.headline ? banner.headline : banner.media.altText}" style="display:none;" />
									</c:otherwise>	
								</c:choose>
								</a>
							</c:when>
							<c:otherwise>
							<c:set var="urlWithIcid" value="${encodedUrl}"/>
								<c:if test="${not empty encodedUrl}">
									<c:set var="urlWithIcid" value="${encodedUrl}?icid=${banner.pk}"/>
								</c:if>
								<span class="style_edit_title">${banner.headline}</span>
								<a tabindex="-1" href="${urlWithIcid}"
								<c:if test="${banner.external}"> target="_blank"</c:if>>
								<c:choose>
									<c:when test="${status.index eq 0}">
										 <img src="${banner.media.url}" alt="${not empty banner.headline ? banner.headline : banner.media.altText}" />
									</c:when>
									<c:otherwise>
										 <img data-src="${banner.media.url}" alt="${not empty banner.headline ? banner.headline : banner.media.altText}" style="display:none;" />
									</c:otherwise>	
								</c:choose>
							</a>
							</c:otherwise>
						</c:choose>
						</c:otherwise>
					</c:choose>
				</div>
			</c:if>
		</c:forEach>
	</div>
		<div
		class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference home-rotatingImage"
		id="rotatingImageTimeoutMobile">
		<c:forEach items="${mobileView}" var="banner" varStatus="status">
			<c:if test="${ycommerce:evaluateRestrictions(banner)}">
				<c:url value="${banner.urlLink}" var="encodedUrl" />
				<div class="item slide">

					<%-- START: code for adding  3 sided and  4 sided banner component--%>
					<c:choose>
						<c:when test="${ banner.type eq 'Big 3 Sided Banner Component'}">
							<div class="hero icid">
								<c:choose>
								<c:when test="${status.index eq 0}">
									 <div class="image">
										<img src="${banner.bannerImage.url}">
									</div>
								</c:when>
								<c:otherwise>
									<div class="image">
										<img data-src="${banner.bannerImage.url}" style="display:none;" />
									</div>
								</c:otherwise>	
								</c:choose>
								 
								
							<%--  <common:bannerImage view="${banner.bannerView.code}" image="${banner.bannerImage.url}"/> --%>
								
								<ul class="major-promos">
									<li data-bannerid="${banner.pk}">${banner.majorPromoText}</li>
								</ul>
								<ul class="minor-promos">
									<li data-bannerid="${banner.pk}">${banner.minorPromo1Text}</li>
									<li data-bannerid="${banner.pk}">${banner.minorPromo2Text}</li>
								</ul>
							</div>
						</c:when>


						<c:when test="${ banner.type eq 'Big 4 Sided Banner Component'}">
							<div class="hero icid fourPromoBanner">
								<c:choose>
								<c:when test="${status.index eq 0}">
									 <div class="image">
										<img src="${banner.bannerImage.url}" />
									</div>
								</c:when>
								<c:otherwise>
									<div class="image">
										<img data-src="${banner.bannerImage.url}" style="display:none;" />
									</div>
								</c:otherwise>	
								</c:choose>
								
						<%-- 	 <common:bannerImage view="${banner.bannerView.code}" image="${banner.bannerImage.url}"/> --%>
								
								<ul class="major-promos">
									<li data-bannerid="${banner.pk}">${banner.promoText4}</li>
								</ul>

								<ul class="minor-promos">
									<li data-bannerid="${banner.pk}">${banner.promoText2}</li>
								</ul>

								<ul class="top-promos">
									<li data-bannerid="${banner.pk}">${banner.promoText1}</li>
								</ul>
								<ul class="bottom-promos">
									<li data-bannerid="${banner.pk}">${banner.promoText3}</li>
								</ul>
							</div>

						</c:when>

						<c:otherwise>
						<c:choose>
							<c:when test="${fn:contains(encodedUrl,'?')}">
								<a tabindex="-1" href="${encodedUrl}&icid=${banner.pk}"
								<c:if test="${banner.external}"> target="_blank"</c:if>>
								<c:choose>
								<c:when test="${status.index eq 0}">
									 <img src="${banner.media.url}" alt="${not empty banner.headline ? banner.headline : banner.media.altText}" />
								</c:when>
								<c:otherwise>
									<img data-src="${banner.media.url}" alt="${not empty banner.headline ? banner.headline : banner.media.altText}" style="display:none;" />
								</c:otherwise>	
								</c:choose>
								</a>
							</c:when>
							<c:otherwise>
								<a tabindex="-1" href="${encodedUrl}?icid=${banner.pk}"
								<c:if test="${banner.external}"> target="_blank"</c:if>>
								<c:choose>
								<c:when test="${status.index eq 0}">
									 <img src="${banner.media.url}" alt="${not empty banner.headline ? banner.headline : banner.media.altText}" title="${not empty banner.headline ? banner.headline : banner.media.altText}" />
								</c:when>
								<c:otherwise>
									<img data-src="${banner.media.url}" alt="${not empty banner.headline ? banner.headline : banner.media.altText}" title="${not empty banner.headline ? banner.headline : banner.media.altText}" style="display:none;" />
								</c:otherwise>	
								</c:choose>
							</a>
							</c:otherwise>
						</c:choose>
						</c:otherwise>
					</c:choose>
				</div>
			</c:if>
		</c:forEach>
	</div>
	</div>
	</c:otherwise>
	</c:choose>
	
