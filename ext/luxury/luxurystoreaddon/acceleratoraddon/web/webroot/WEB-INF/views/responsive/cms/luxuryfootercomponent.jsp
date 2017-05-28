<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="container footer-bottom">
		<div class="row footer-inner-wrapper">
			<c:forEach items="${navigationNodes}" var="node">
				<div class="col-sm-2 col-md-2 col-lg-2 accordion">
					<h3 class="">${node.title}<span class="sprite sp-minus"></span></h3>
					<ul class="accordion-content">
						<c:forEach items="${node.links}" var="childlink">
							<li>
								<cms:component component="${childlink}"
									evaluateRestriction="true" />
							</li>
						</c:forEach>
					</ul>
				</div>
			</c:forEach>
		
			<div class="col-sm-4 col-md-4 col-lg-4 app-info">
				<h3 class="">${footerAppTitle}<span class="sprite sp-minus"></span></h3>
				<div class="">
				<ul>
					<c:forEach var="app" items="${footerAppImageList}" >
						<li><a href="javascript:;"><img src="${app.media.url}" alt="${app.title}"></a></li>
					</c:forEach>
				</ul>
				<div class="get-in-touch">
					<p>(OR)</p>
					<p>${footerAppText}</p>
					<h3>${footerSocialTitle}</h3>
					<p class="soc-links">
						<c:forEach var="socialIcon" items="${footerImageList}" >
							<a class="link" href="javascript:;">
								<img src="${socialIcon.media.url}" alt="${socialIcon.title}">
							</a>
						</c:forEach>
					</p>
				</div>
				</div>
			</div>
			<div class="col-sm-4 col-md-4 col-lg-4 about-product">
				<ul>
					<li><span>100% ORIGINAL </span>gaurantee for all products at luxury.com</li>
					<li><span>Return within 30days </span>of placing your order</li>
					<li><span>Get free delivery </span>for every order above Rs. 1,999</li>
				</ul>
			</div>
			
		</div>
	</div>
