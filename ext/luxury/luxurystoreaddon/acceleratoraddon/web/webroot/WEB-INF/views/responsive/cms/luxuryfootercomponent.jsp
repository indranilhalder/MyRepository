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
				<ul class="app-link">
					<c:forEach var="app" items="${footerAppImageList}" >
						<li><a href="${app.urlLink}"><img src="${app.media.url}" alt="${app.title}"></a></li>
					</c:forEach>
				</ul>
				<div class="get-in-touch">
				
					<p>${footerAppText}</p>
					<h3>${footerSocialTitle}</h3>
					<p class="soc-links">
						<c:forEach var="socialIcon" items="${footerImageList}" >
							<a class="link" href="${socialIcon.urlLink}">
								<img src="${socialIcon.media.url}" alt="${socialIcon.title}">
							</a>
						</c:forEach>
					</p>
				</div>
				</div>
			</div>
			<div class="col-sm-4 col-md-4 col-lg-4 about-product">
				<cms:component component="${footerBarContent}"
									evaluateRestriction="true" />
			</div>
			
		</div>
	</div>
	
	<div class="container footer-popular-search">
	    <div class="footer-bottom-links">
		   <h5><span>POPULAR SEARCHES</span></h5>
	   </div>
	</div>
	
	<div class="modal fade" id="popUpModal" style="z-index:1000000000;" tabindex="-1" role="modal" aria-labelledby="popUpModalLabel" aria-hidden="true">
	<div class="overlay" data-dismiss="modal"></div>
		<div class="modal-dialog modal-lg">
		<div class="modal-content content" style="width:90%; max-width:90%;">
			
		</div>
		<!-- /.modal-content -->
	
	<!-- /.modal-dialog -->
	</div>
</div>
