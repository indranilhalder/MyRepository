
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<!-- New Homepage change -->
<script>
var homePageBannerTimeout='${timeout}';

</script>


<c:choose>
<c:when test="${empty timeout || timeout == 0}">
<div class="content-block-slider electronic-brand-slider">
	<div
		class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference"
		id="rotatingImage">
		<c:forEach items="${banners}" var="banner" varStatus="status">
			<c:if test="${ycommerce:evaluateRestrictions(banner)}">
				<c:url value="${banner.urlLink}" var="encodedUrl" />
				<div class="item slide">

					<!-- START: code for adding  3 sided and  4 sided banner component-->
					<c:choose>
						<c:when test="${ banner.type eq 'Big 3 Sided Banner Component'}">
							<div class="hero">
								<div class="image">
									<img src="${banner.bannerImage.url}">
								</div>
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
							<div class="hero fourPromoBanner">
								<div class="image">
									<img src="${banner.bannerImage.url}">
								</div>
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
							<a tabindex="-1" href="${encodedUrl}"
								<c:if test="${banner.external}"> target="_blank"</c:if>> <img
								src="${banner.media.url}"
								alt="${not empty banner.headline ? banner.headline : banner.media.altText}"
								title="${not empty banner.headline ? banner.headline : banner.media.altText}" />

							</a>
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
		class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference"
		id="rotatingImageTimeout">
		<c:forEach items="${banners}" var="banner" varStatus="status">
			<c:if test="${ycommerce:evaluateRestrictions(banner)}">
				<c:url value="${banner.urlLink}" var="encodedUrl" />
				<div class="item slide">

					<!-- START: code for adding  3 sided and  4 sided banner component-->
					<c:choose>
						<c:when test="${ banner.type eq 'Big 3 Sided Banner Component'}">
							<div class="hero">
								<div class="image">
									<img src="${banner.bannerImage.url}">
								</div>
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
							<div class="hero fourPromoBanner">
								<div class="image">
									Hello<img src="${banner.bannerImage.url}">
								</div>
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
							<a tabindex="-1" href="${encodedUrl}"
								<c:if test="${banner.external}"> target="_blank"</c:if>><img
								src="${banner.media.url}"
								alt="${not empty banner.headline ? banner.headline : banner.media.altText}"
								title="${not empty banner.headline ? banner.headline : banner.media.altText}" />

							</a>
						</c:otherwise>
					</c:choose>
				</div>
			</c:if>
		</c:forEach>
	</div>
	</div>
	</c:otherwise>
	</c:choose>
<script>

	$(document).ready(function(){
		$(".hero li").each(function() {
			if($(this).has("href")){
				var icid = $(this).attr("data-bannerid");
				var link = $(this).find("a").attr("href");
				link = link + "?icid="+icid;
				$(this).find("a").attr("href",link);
			}
		});
	});

</script> 


