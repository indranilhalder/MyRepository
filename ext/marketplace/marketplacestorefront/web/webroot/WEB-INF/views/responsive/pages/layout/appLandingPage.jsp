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
		<section class="r2-landing-banner">
			<div class="col-md-6 col-sm-12 col-xs-12 xol-lg-6 r2-app-mob">
				<img src="./images/r2/banner-2.png"
					class="r2-app-mob-up hidden-xs hidden-sm"> <img
					src="./images/r2/banner-app-2.png"
					class="r2-app-brand-img r2-marginTop img-responsive visible-xs visible-sm">
			</div>

			<div class="col-md-6 col-sm-12 col-xs-12 xol-lg-6 r2-app-text">
				<h1>
					THE tata cliq <br>mobile app is here.
				</h1>
				<h5>Now Available for both iOS and Android devices.</h5>
				<div class="container r2-marginTop r2-marginBottom r2-padding">
					<span class="col-md-4 col-lg-4 col-sm-6 col-xs-6"> <a
						href="#"> <img class="img-responsive"
							src="./images/r2/desktop_badge_mac.png">
					</a>
					</span> <span class="col-md-4 col-lg-4 col-sm-6 col-xs-6"> <a
						href="#"> <img class="img-responsive"
							src="./images/r2/desktop_badge_play.png">
					</a>
					</span>
				</div>

				<div class="col-md-8 r2-marginBottom">
					<article>
						<p class="r2-minusmarginLeft">TataCliq is the new amazing
							shopping experience from Tata. Tons of features at your
							fingertips wherever you are.</p>
						<ul class="r2-paddingTop">
							<li>Hundreds of your favourite brands</li>
							<li>A seamless and secure shopping experience</li>
							<li>Great savings and Personalised offers</li>
						</ul>
						<div class="r2-minusmarginLeft">
							<a class="button maroon r2-left r2-marginTop"><span
								class="glyphicon glyphicon-earphone r2-left "></span><span>Download
									the app by calling 0011 225577 8844</span></a>
						</div>
					</article>
				</div>
			</div>
		</section>

		<div class="r2-buy-banner r2-marginTop r2-paddingTop row">
			<p>"One of the best shopping experiences on the market day" -
				Wired magazine</p>
		</div>

		<section class="r2-app-slider r2-left">
			<div class="col-md-12">
				<h1>
					20% Off for your<br> first purchase
				</h1>
				<p class="r2-paddingTop  r2-padding-bottom">Lorem ipsum dolor
					sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget
					dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis
					parturient montes, nascetur ridiculus mus.</p>
			</div>

			<div class="r2-mob-carousel r2-paddingTop r2-marginTop">
				<div id="myCarousel" class="carousel slide hidden-md col-xs-12"
					data-ride="carousel">
					<!-- Indicators -->
					<ol class="carousel-indicators">
						<li data-target="#myCarousel" data-slide-to="0" class=""></li>
						<li data-target="#myCarousel" data-slide-to="1" class="active"></li>
						<li data-target="#myCarousel" data-slide-to="2" class=""></li>
					</ol>
					<!-- Wrapper for slides -->
					<div class="carousel-inner" role="listbox">
						<div class="item">
							<img src="./images/r2/mob-slider1.png" class="r2-marginTop">
						</div>

						<div class="item active">
							<img src="./images/r2/mob-slider2.png" class="r2-marginTop">
						</div>

						<div class="item">
							<img src="./images/r2/mob-slider3.png" class="r2-marginTop">
						</div>

					</div>


					<div class="r2-whiteText row">
						<h2 class="r2-paddingTop col-sm-12 col-xs-12 ">Browse</h2>
						<p class="col-sm-12 col-xs-12 ">Lorem ipsum dolor sit amet,
							consectetuer adipiscing elit. Aenean commodo ligula eget dolor.
							Aenean massa.</p>
					</div>
					<div class="r2-whiteText row">
						<h2 class="r2-paddingTop col-sm-12 col-xs-12 ">Shop</h2>
						<p class="col-sm-12 col-xs-12 ">Lorem ipsum dolor sit amet,
							consectetuer adipiscing elit. Aenean commodo ligula eget dolor.
							Aenean massa.</p>
					</div>
					<div class="r2-whiteText row">
						<h2 class="r2-paddingTop col-sm-12 col-xs-12 ">Share</h2>
						<p class="col-sm-12 col-xs-12 ">Lorem ipsum dolor sit amet,
							consectetuer adipiscing elit. Aenean commodo ligula eget dolor.
							Aenean massa.</p>
					</div>
				</div>
				<div
					class="hidden-xs hidden-sm row r2-paddingTop r2-center r2-marginTop">
					<div class="container r2-marginTop ">
						<div class="col-md-4 r2-whiteText r2-marginTop">
							<img src="./images/r2/mob-slider1.png" class="r2-marginTop">
							<div class="col-md-12 r2-marginAuto">
								<h2>Browse</h2>
								<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit.
									Aenean commodo ligula eget dolor. Aenean massa.</p>
							</div>
						</div>
						<div class="col-md-4 r2-whiteText r2-marginTop">
							<img src="./images/r2/mob-slider2.png" class="r2-marginTop">
							<div class="col-md-12 r2-marginAuto">
								<h2>Shop</h2>
								<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit.
									Aenean commodo ligula eget dolor. Aenean massa.</p>
							</div>
						</div>
						<div class="col-md-4 r2-whiteText r2-marginTop">
							<img src="./images/r2/mob-slider3.png" class="r2-marginTop">
							<div class="col-md-12 r2-marginAuto">
								<h2>Share</h2>
								<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit.
									Aenean commodo ligula eget dolor. Aenean massa.</p>
							</div>

						</div>
					</div>
				</div>

			</div>

		</section>

		<section class="r2-landing-brands">
			<div class="col-md-6 col-sm-12 col-xs-12 col-lg-6">
				<img src="./images/r2/app-feature.png"
					class="r2-app-brand-img hidden-xs"> <img
					src="./images/r2/app-feature-mob.png"
					class="r2-app-brand-img visible-xs">
			</div>
			<div
				class="col-md-6 col-sm-12 col-xs-12 col-lg-6 r2-right-align r2-padding">
				<img src="./images/r2/app-brands.png" class="r2-app-brand-img">
				<div class="col-md-8 r2-marginAuto r2-center r2-right">
					<h1 class="r2-landing-heading">Brands you love</h1>
					<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit.
						Aenean commodo ligula eget dolor. Aenean massa.Lorem ipsum dolor
						sit amet, consectetuer adipiscing elit.</p>
				</div>
			</div>
			<div
				class="col-md-7 col-sm-6 col-xs-12 col-lg-7 r2-marginAuto r2-center r2-padding">
				<h1 class="r2-landing-heading">Hundreds of features</h1>
				<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit.
					Aenean commodo ligula eget dolor. Aenean massa. Aenean massa.Lorem
					ipsum dolor sit amet, consectetuer adipiscing elit.</p>
			</div>
		</section>


		<section class="r2-landing-banner r2-left">
			<div class="col-md-6 col-sm-12 col-xs-12 xol-lg-6 r2-app-mob">
				<img src="./images/r2/app_last.png"
					class="img-responsive r2-app-mob-down">
			</div>

			<div class="col-md-6 col-sm-12 col-xs-12 xol-lg-6 r2-app-text">
				<h1>
					THE tata cliq <br>mobile app is here.
				</h1>
				<h5>Download it now on your favourite device and indulge in a
					seamless shopping experience.</h5>
				<div class="container r2-marginTop r2-marginBottom r2-padding">
					<span class="col-md-4 col-lg-4 col-sm-6 col-xs-6"> <img
						class="img-responsive" src="./images/r2/desktop_badge_mac.png">
					</span> <span class="col-md-4 col-lg-4 col-sm-6 col-xs-6"> <img
						class="img-responsive" src="./images/r2/desktop_badge_play.png">
					</span>
				</div>

				<div class="col-md-8 r2-marginBottom">
					<article>
						<div class="r2-minusmarginLeft r2-padding-bottom">
							<a class="button maroon r2-left"><span
								class="glyphicon glyphicon-earphone r2-left "></span><span>Download
									the app by calling 0011 225577 8844</span></a>
						</div>
					</article>
				</div>
			</div>
		</section>
	</div>

</template:page>