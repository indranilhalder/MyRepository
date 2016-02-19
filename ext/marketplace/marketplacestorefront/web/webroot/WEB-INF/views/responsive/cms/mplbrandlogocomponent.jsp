<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>



<div class="brands">
	<div class="wrapper">
		<c:if test="${not empty component.title }">
			<h1>${component.title}</h1>
		</c:if>

		<c:if test="${component.displayType eq 'BRAND_LOGO_IMAGE'}">



			<ul class="feature-brands">

				<c:forEach items="${component.categories}" var="category">
					<c:url value="/Categories/c/${category.code}" var="categoryUrl" />
					<li><a href="${categoryUrl }" class="feature-brands-link">
							<c:forEach items="${category.medias}" var="media">
								<c:set var="logoQualifier"
									value="${media.mediaFormat.qualifier}" />

								<c:if test="${ not empty logoQualifier}">


									<c:choose>
										<c:when test="${logoQualifier eq '206Wx206H' }">
											<div class="feature-brands-main-image">
												<img class="image" src="${media.url2 }">
											</div>
											<span>${media.description}</span>
										</c:when>
										<c:when test="${logoQualifier eq '135Wx60H' }">
											<img class="logo" src="${media.url2 }">
										</c:when>
										<c:otherwise>

										</c:otherwise>
									</c:choose>




								</c:if>
							</c:forEach>
					</a></li>


				</c:forEach>

			</ul>


		</c:if>

		<c:if test="${component.displayType eq 'BRAND_IMAGE'}">

			<ul class="feature-brands">


				<c:forEach items="${component.categories}" var="category">
					<c:url value="/Categories/c/${category.code}" var="categoryUrl" />
					<li><c:forEach items="${category.medias}" var="media">
							<c:set var="logoQualifier" value="${media.mediaFormat.qualifier}" />

							<c:if test="${ not empty logoQualifier}">

								<a href="${categoryUrl }"> <c:choose>
										<c:when test="${logoQualifier eq '206Wx206H' }">
											<img class="image" src="${media.url2 }">

										</c:when>
										<c:otherwise>

										</c:otherwise>
									</c:choose>


								</a>

							</c:if>
						</c:forEach></li>


				</c:forEach>

			</ul>
		</c:if>



		<c:if test="${component.displayType eq 'BRANDIMAGE_DESCRIPTION'}">

			<ul class="feature-brands">

				<c:forEach items="${component.categories}" var="category">
					<c:url value="/Categories/c/${category.code}" var="categoryUrl" />
					<li><c:forEach items="${category.medias}" var="media">
							<c:set var="logoQualifier" value="${media.mediaFormat.qualifier}" />

							<c:if test="${ not empty logoQualifier}">

								<a href="${categoryUrl }"> <c:choose>
										<c:when test="${logoQualifier eq '206Wx206H' }">
											<img class="image" src="${media.url2 }">
											<span>${media.description}</span>
										</c:when>
										<c:otherwise>

										</c:otherwise>
									</c:choose>


								</a>

							</c:if>
						</c:forEach></li>

				</c:forEach>


			</ul>
		</c:if>


		<c:if test="${component.displayType eq 'BRANDIMAGE_LOGO'}">

			<ul class="feature-brands">

				<c:forEach items="${component.categories}" var="category">
					<c:url value="/Categories/c/${category.code}" var="categoryUrl" />
					<li><a href="${categoryUrl }" class="feature-brands-link">
							<c:forEach items="${category.medias}" var="media">
								<c:set var="logoQualifier"
									value="${media.mediaFormat.qualifier}" />

								<c:if test="${ not empty logoQualifier}">


									<c:choose>
										<c:when test="${logoQualifier eq '206Wx206H' }">
											<div class="feature-brands-main-image">
												<img class="image" src="${media.url2 }">
											</div>
										</c:when>
										<c:when test="${logoQualifier eq '135Wx60H' }">
											<img class="logo" src="${media.url2 }">
										</c:when>
										<c:otherwise>

										</c:otherwise>
									</c:choose>




								</c:if>
							</c:forEach>
					</a></li>

				</c:forEach>

			</ul>
		</c:if>



		<c:if test="${component.displayType eq 'LOGO_DESCRIPTION'}">

			<ul class="feature-brands">


				<c:forEach items="${component.categories}" var="category">
					<c:url value="/Categories/c/${category.code}" var="categoryUrl" />
					<li><c:forEach items="${category.medias}" var="media">
							<c:set var="logoQualifier" value="${media.mediaFormat.qualifier}" />

							<c:if test="${ not empty logoQualifier}">

								<a href="${categoryUrl }"> <c:choose>
										<c:when test="${logoQualifier eq '135Wx60H' }">
											<img class="logo" src="${media.url2 }">
											<span>${media.description}</span>
										</c:when>
										<c:otherwise>

										</c:otherwise>
									</c:choose>


								</a>

							</c:if>
						</c:forEach></li>

				</c:forEach>



			</ul>
		</c:if>

		<c:if test="${component.displayType eq 'LOGO_IMAGE'}">

			<ul class="feature-brands">


				<c:forEach items="${component.categories}" var="category">
					<c:url value="/Categories/c/${category.code}" var="categoryUrl" />
					<li><c:forEach items="${category.medias}" var="media">
							<c:set var="logoQualifier" value="${media.mediaFormat.qualifier}" />

							<c:if test="${ not empty logoQualifier}">

								<a href="${categoryUrl }"> <c:choose>
										<c:when test="${logoQualifier eq '135Wx60H' }">
											<img class="logo" src="${media.url2 }">
										</c:when>
										<c:otherwise>

										</c:otherwise>
									</c:choose>


								</a>

							</c:if>
						</c:forEach></li>

				</c:forEach>



			</ul>
		</c:if>

	</div>
</div>






