<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<section>
	<footer>
		
		<div class="container footer-top text-center">
			<h1>Why shop on Luxury</h1>
			<div class="shop-on-luxuy">
				<div class="shop-box col-sm-4">
					<h2>1,500+ amazing brands</h2>
					<p>Authenticity & quality of the product is gauranteed. The products sold come with all the original</p>
				</div>
				<div class="shop-box col-sm-4">
					<h2>1,500+ amazing brands</h2>
					<p>Authenticity & quality of the product is gauranteed. The products sold come with all the original</p>
				</div>
				<div class="shop-box col-sm-4">
					<h2>1,500+ amazing brands</h2>
					<p>Authenticity & quality of the product is gauranteed. The products sold come with all the original</p>
				</div>
			</div>
		</div>
		
		<div class="container-fluid footer-middle">
			<div class="layout-fluid">
				<div class="row signup-email-wrapper">
					<div class="col-sm-6 send-awesome-email">
						<p class="email-tit">We send awesome emails.</p>
						<p>Be first to know about new arrivals, curated collections, and exclusive sales.</p>
					</div>
					<div class="col-sm-6 signup-email">
						<input type="email" class="col-sm-6 email" placeholder="Enter your email address">
						<input type="text" class="col-sm-4 gender" placeholder="Gender">
						<button class="btn col-sm-2 text-left">Sign Up</button>

					</div>
				</div>
			</div>
		</div>
	
		
		</div>
	</footer>
</section>
<!-- <footer id="footerByAjaxId"> -->
<%-- <footer>
<c:if test="${!fn:containsIgnoreCase(cmsPage.name, 'Cart Page')}">
<div class="callouts">
					  <div class="Padd">
						<ul>
						  <li> <p>TATA Promise</p> </li>
						  <li class="omni"> <p>Omni Channel</p> </li>
						  <li class="genuine"> <p>Genuine Brands</p> </li>
						  <li class="returns"> <p>30 Day Returns</p> </li>
						</ul>
					  </div>
					</div>
					</c:if>

	<div id="footerByAjaxId"></div>
	<!-- <footer> -->
		<cms:pageSlot position="Footer" var="feature">
		<cms:component component="${feature}"/>
	</cms:pageSlot>
	<cms:pageSlot position="Footer" var="feature">
		<cms:component component="${feature}" />
	</cms:pageSlot>
</footer> --%>
