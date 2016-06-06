<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user"%>

<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>

<template:page pageTitle="${pageTitle}">

	<div class="r2-app-landing">    
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('appLandingPage.pathForMac')" var="pathToReferForMac"/>
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('appLandingPage.pathForPlay')" var="pathToReferForPlay"/>     
		<section class="r2-landing-banner">
			<div class="col-md-6 col-sm-12 col-xs-12 xol-lg-6 r2-app-mob">
				<img src="${commonResourcePath}/images/banner-2.1.png"
					class="r2-app-mob-up hidden-xs hidden-sm"> <img
					src="${commonResourcePath}/images/banner-app-2.1.png"
					class="r2-app-brand-img r2-marginTop img-responsive visible-xs visible-sm">
			</div>

			<div class="col-md-6 col-sm-12 col-xs-12 xol-lg-6 r2-app-text">
				<h1>
					THE tata cliq <br>mobile app is here.
				</h1>
				<h5>Available for both Android and iOS devices</h5>
				<div class="container r2-marginTop r2-marginBottom r2-padding">
					<span class="col-md-4 col-lg-4 col-sm-6 col-xs-6"> <a
						href="${pathToReferForMac}"> <img class="img-responsive"
							src="${commonResourcePath}/images/desktop_badge_mac.png">
					</a>
					</span> <span class="col-md-4 col-lg-4 col-sm-6 col-xs-6"> <a
						href="${pathToReferForPlay}"> <img class="img-responsive"
							src="${commonResourcePath}/images/desktop_badge_play.png">
					</a>
					</span>
				</div>

				<div class="col-md-8 r2-marginBottom">
					<article>
						<p class="r2-minusmarginLeft">Shop from a wide range of CAMELS
						 (Certified Authentic Merchandise Everybody Loves) and
						  get great deals across apparel, footwear, mobiles, tech gadgets and appliances. 
						  And we have loads of exclusives in store for you.</p>
						<!-- <ul class="r2-paddingTop">
							<li>Hundreds of your favourite brands</li>
							<li>A seamless and secure shopping experience</li>
							<li>Great savings and Personalised offers</li>
						</ul>
						<div class="r2-minusmarginLeft">
							<a class="button maroon r2-left r2-marginTop"><span
								class="glyphicon glyphicon-earphone r2-left "></span><span>Download
									the app by calling 0011 225577 8844</span></a>
						</div> -->
					</article>
				</div>
			</div>
		</section>


		<section class="r2-app-slider r2-left">
				<div class="r2-buy-banner r2-marginTop r2-paddingTop row">
			<p>REAL GOOD BRANDS AT FEEL GOOD PRICES</p>
		</div>
		
			<div class="col-md-12">
				<h1>
					Welcome to India's only<br> PHYGITAL shopping destination
				</h1>
				<p class="r2-paddingTop  r2-padding-bottom">We've paired the joys (and assurance) of buying stuff PHYSICALLY with the
				 convenience of DIGITAL shopping, to bring you a PHYGITAL world! Shop anywhere,
				 anytime with Tata CLiQ, the go-to shop for fashion, technology, and accessories.
				 We not only deliver to your doorstep, but make it easier for you to PIQ up,
				 exchange and return stuff whenever, wherever you are.</p>
			</div>

			<div class="r2-mob-carousel r2-paddingTop r2-marginTop">
				<div id="myCarousel" class="carousel slide hidden-md col-xs-12"
					data-ride="carousel">
					<!-- Indicators -->
					<!-- <ol class="carousel-indicators">
						<li data-target="#myCarousel" data-slide-to="0" class=""></li>
						<li data-target="#myCarousel" data-slide-to="1" class="active"></li>
						<li data-target="#myCarousel" data-slide-to="2" class=""></li>
					</ol> -->
					<!-- Wrapper for slides -->
					<div class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference" id="appLanding_carousel">
						<!-- <div class="item">
							<img src="${commonResourcePath}/images/mob-slider1.png" class="r2-marginTop">
						</div>

						<div class="item">
							<img src="${commonResourcePath}/images/mob-slider2.png" class="r2-marginTop">
						</div>

						<div class="item">
							<img src="${commonResourcePath}/images/mob-slider3.png" class="r2-marginTop">
						</div> -->
						
						<div class="item">
							<img src="${commonResourcePath}/images/mob-slider1.1.png" class="r2-marginTop">
						</div>

						<div class="item">
							<img src="${commonResourcePath}/images/mob-slider2.1.png" class="r2-marginTop">
						</div>

						<div class="item">
							<img src="${commonResourcePath}/images/mob-slider3.1.png" class="r2-marginTop">
						</div>
					</div>


					<div class="r2-whiteText row">
						<h2 class="r2-paddingTop col-sm-12 col-xs-12 ">Store Locator</h2>
						<p class="col-sm-12 col-xs-12 ">PIQ up, return or exchange stuff at a store near you with the help of
							our interactive map.</p>
					</div>
					<div class="r2-whiteText row">
						<h2 class="r2-paddingTop col-sm-12 col-xs-12 ">Image Search</h2>
						<p class="col-sm-12 col-xs-12 ">Click or pick a picture and find similar products from our
						exclusive, handpicked collections.</p>
					</div>
					<div class="r2-whiteText row">
						<h2 class="r2-paddingTop col-sm-12 col-xs-12 ">#QueMag</h2>
						<p class="col-sm-12 col-xs-12 ">Flip through our cool magazine to get 
						the latest trends and tips on fashion,
						tech, culture and lifestyle.</p>
					</div>
				</div>
				<div
					class="hidden-xs hidden-sm row r2-paddingTop r2-center r2-marginTop">
					<div class="container r2-marginTop ">
						<div class="col-md-4 r2-whiteText r2-marginTop">
							<img src="${commonResourcePath}/images/mob-slider1.1.png" class="r2-marginTop">
							<div class="col-md-12 r2-marginAuto">
								<h2>Store Locator</h2>
								<p>PIQ up, return or exchange stuff at
									a store near you with the help of
							our interactive map.</p>
							</div>
						</div>
						<div class="col-md-4 r2-whiteText r2-marginTop">
							<img src="${commonResourcePath}/images/mob-slider2.1.png" class="r2-marginTop">
							<div class="col-md-12 r2-marginAuto">
								<h2>Image Search</h2>
								<p>Click or pick a picture and find
						similar products from our
						exclusive, handpicked collections.</p>
							</div>
						</div>
						<div class="col-md-4 r2-whiteText r2-marginTop">
							<img src="${commonResourcePath}/images/mob-slider3.1.png" class="r2-marginTop">
							<div class="col-md-12 r2-marginAuto">
								<h2 style="margin-top: 55px;">#QueMag</h2>
								<p>Flip through our cool magazine to get 
						the latest trends and tips on fashion,
						tech, culture and lifestyle.</p>
							</div>

						</div>
					</div>
				</div>

			</div>

		</section>

		<section class="r2-landing-brands">
			<div class="col-md-6 col-sm-12 col-xs-12 col-lg-6">
				<img src="${commonResourcePath}/images/app-feature1.png"
					class="r2-app-brand-img hidden-xs r2-app-brand-img-left"> <img
					src="${commonResourcePath}/images/app-feature-mob1.png"
					class="r2-app-brand-img visible-xs">
			</div>
			<div
				class="col-md-6 col-sm-12 col-xs-12 col-lg-6 r2-right-align r2-padding">
				<img src="${commonResourcePath}/images/app-brands1.png" class="r2-app-brand-img">
				<div class="col-md-8 r2-marginAuto r2-center r2-right">
					<h1 class="r2-landing-heading">Brand Studio</h1>
					<p>Browse our brand stores for a 
						curated shopping experience 
						that'll keep you hooked.</p>
				</div>
			</div>
			<div
				class="col-md-7 col-sm-6 col-xs-12 col-lg-7 r2-marginAuto r2-center r2-padding">
				<h1 class="r2-landing-heading">We're the #SureThing</h1>
				<p>What you want is what you'll get: camels and exclusive 
					collections hand-picked just for you, straight from the 
					brands; so you are sure its all top of the line and 100% 
					authentic.</p>
			</div>
		</section>


		<section class="r2-landing-banner r2-left">
			<div class="col-md-6 col-sm-12 col-xs-12 xol-lg-6 r2-app-mob">
				<img src="${commonResourcePath}/images/app_last11.png"
					class="img-responsive r2-app-mob-down">
			</div>

			<div class="col-md-6 col-sm-12 col-xs-12 xol-lg-6 r2-app-text">
				<h1>
					THE tata cliq <br>mobile app is here.
				</h1>
				<h5>Download it now on your favourite device and indulge in a
					seamless shopping experience</h5>
				<div class="container r2-marginTop r2-marginBottom r2-padding">
					<span class="col-md-4 col-lg-4 col-sm-6 col-xs-6"> <a
						href="${pathToReferForMac}"> <img
						class="img-responsive" src="${commonResourcePath}/images/desktop_badge_mac.png"></a>
					</span> <span class="col-md-4 col-lg-4 col-sm-6 col-xs-6"> <a
						href="${pathToReferForPlay}"> <img
						class="img-responsive" src="${commonResourcePath}/images/desktop_badge_play.png"></a>
					</span>
				</div>

				<div class="col-md-8 r2-marginBottom">
					<article>
						<!-- <div class="r2-minusmarginLeft r2-padding-bottom">
							<a class="button maroon r2-left"><span
								class="glyphicon glyphicon-earphone r2-left "></span><span>Download
									the app by calling 0011 225577 8844</span></a>
						</div> -->
					</article>
				</div>
			</div>
		</section>
	</div>

</template:page>
<script>
$("#appLanding_carousel").owlCarousel({
	navigation:false,
	rewindNav: false,
	navigationText :[],
	pagination:true,
	autoPlay: true,
	items:1,
	itemsDesktop : false, 
	itemsDesktopSmall : false, 
	itemsTablet: false, 
	itemsMobile : false
});
</script>
