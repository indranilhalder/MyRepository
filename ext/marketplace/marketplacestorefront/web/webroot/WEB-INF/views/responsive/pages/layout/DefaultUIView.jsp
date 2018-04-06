<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="javax.servlet.*,java.text.*"%>

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css">
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<link href="https://fonts.googleapis.com/css?family=Rubik:400,500"
	rel="stylesheet">
<link rel="stylesheet" type="text/css"
	href="/_ui/responsive/theme-blue/css/slick.css" />

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

/*new css*/
.wrapper {
	padding: 0 !important;
}

.slick-slide {
	height: auto !important;
}

body {
	font-family: 'Rubik', sans-serif;
}

.pad0 {
	padding: 0;
}

.mb40 {
	margin-bottom: 40px;
}

.hero-slider .slick-slide {
	margin: 0 5px;
	position: relative;
}

.hero-slider .slick-slide img {
	width: 100%;
	height: 190px;
	margin-top: 10px;
	display: block;
	border-radius: 4px;
}

.hero-slider .slick-current img {
	width: 100%;
	height: auto;
	display: block;
	margin-top: 0px;
}

.hero-slider .hero-subsection {
	position: absolute;
	top: 40%;
	left: 0;
}

.hero-slider .brand-logo {
	width: auto !important;
	height: auto !important;
	margin-left: 15px;
	border: 1px solid #000;
}

.hero-slider .banner-title {
	width: auto;
	height: auto;
	margin-left: 15px;
	margin-top: 10px;
	font-size: 30px;
	font-weight: 500;
	color: #ffffff;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
}

.offer-slider .slick-slide {
	position: relative;
}

.offer-slider .slick-slide img {
	max-width: 100%;
	border-radius: 4px;
}

.offer-slider .offer-subsection {
	position: absolute;
	bottom: 0;
	left: 0;
}

.offer-slider .shop-now {
	width: 141px;
	height: auto;
	margin: 20px 15px;
	border: 1px solid #000;
	border-radius: 100px;
	border: solid 2px #ffffff;
	padding: 10px 0;
	text-align: center;
	color: #ffffff;
	display: block;
	font-size: 14px;
}

.offer-slider .banner-title {
	width: auto;
	height: auto;
	margin-left: 15px;
	margin-top: 10px;
	font-size: 30px;
	font-weight: 500;
	color: #ffffff;
}

.offer-slider .offer {
	position: absolute;
	top: 20px;
	left: 20px;
	width: 78px;
	height: 78px;
	background-image: linear-gradient(137deg, #fd2c7a, #ff7255);
	text-align: center;
	color: #ffffff;
	border-radius: 50%;
}

.offer-slider .offer span {
	position: absolute;
	left: 12px;
	top: 20px;
	width: 55px;
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.43;
	letter-spacing: normal;
}

.offer-widget-title {
	font-size: 20px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: left;
	color: #212121;
	margin: 0px 0px 25px 15px;
}

.flash-sales-widget {
	color: #ffffff;
	padding: 25px 20px;
	background-repeat: no-repeat;
	background-size: cover;
}

.flash-sales-widget-title {
	font-size: 20px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: left;
	color: #ffffff;
	text-transform: capitalize;
}

.flash-sales-widget-timer {
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: right;
	color: #ffffff;
}

.flash-sales-widget-text {
	font-size: 14px;
	font-weight: normal;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.57;
	letter-spacing: normal;
	text-align: left;
	color: #ffffff;
	margin: 10px 0 25px 0;
	width: 195px;
}

.flash-sales-widget-product:nth-child(even) {
	padding: 0 12px 0 0;
}

.flash-sales-widget-product:nth-child(odd) {
	padding: 0 0 0 12px;
}

.flash-sales-widget-product img {
	border-radius: 4px;
}

.flash-sales-widget-product-offer {
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.43;
	letter-spacing: normal;
	color: #ffffff;
	padding: 0;
	margin: 15px 0 10px 0;
}

.flash-sales-widget-product-name {
	font-size: 14px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.57;
	letter-spacing: normal;
	text-align: left;
	color: #ffffff;
	padding: 0;
	margin-bottom: 25px;
}

.flash-sales-widget .shop-all-btn {
	width: 120px;
	padding: 8px 0;
	border-radius: 100px;
	border: solid 2px #ffffff;
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: center;
	color: #ffffff;
	float: left;
	clear: both;
	margin-bottom: 5px;
}

.flash-sales-widget .fa-clock-o {
	font-size: 20px;
	margin-right: 5px;
}

.flash-sales-widget-timer .fa-clock-o {
	font-size: 20px;
	margin-right: 10px;
}

.flash-sales-widget-timer .time-digits {
	float: right;
	margin-top: 2px;
}

.connect-banner {
	width: calc(100% - 30px);
	margin-left: 15px;
	margin-right: 15px;
	border-radius: 4.2px;
	padding: 25px;
	background-repeat: no-repeat;
	background-size: cover;
}

.connect-banner-text1 {
	font-size: 14.3px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #ffffff;
	padding-right: 0;
}

.connect-banner-text2 {
	font-size: 12.5px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #ffffff;
	margin: 10px 0 15px 0;
}

.connect-banner-more {
	font-size: 12.5px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #ffffff;
	padding-left: 15px;
	display: inline-block;
	width: auto;
}

.connect-banner-more::after {
	content: "";
	display: block;
	width: auto;
	border: 0.5px solid #fff;
	margin-top: 2px;
}

.connect-banner-full-width {
	background-repeat: no-repeat;
	background-size: cover;
	padding: 25px;
}

.connect-banner-full-width .connect-banner-text1 {
	font-size: 16px;
}

.connect-banner-full-width .connect-banner-more,
	.connect-banner-full-width .connect-banner-text2 {
	font-size: 14px;
}

.banner-product-slider {
	margin: 30px 0 35px 0;
}

.banner-product-carousel, .video-product-carousel,
	.auto-brand-product-carousel {
	background-color: #e4e4e4;
}

.banner-product-carousel .overlay-text {
	position: absolute;
	top: 100px;
	text-align: center;
}

.banner-product-carousel .banner-product-title {
	font-size: 24px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #ffffff;
}

.banner-product-carousel .banner-product-desc {
	font-size: 14px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #ffffff;
}

.banner-product-slider .slick-slide img {
	border-radius: 4px;
}

.banner-product-slider .product-name {
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.57;
	letter-spacing: normal;
	text-align: left;
	color: #212121;
	margin: 10px 0 15px 0;
}

.banner-product-slider .product-price {
	font-size: 14px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.43;
	letter-spacing: normal;
	text-align: left;
	color: #212121;
}

.banner-product-carousel .shop-all-btn {
	width: 120px;
	padding: 8px 0;
	border-radius: 100px;
	border: solid 2px #212121;
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: center;
	color: #212121;
	float: left;
	clear: both;
	margin-left: 15px;
	margin-bottom: 35px;
}

.video-product-slider {
	margin: 30px 0 35px 0;
}

.video-product-carousel-container {
	position: absolute;
	top: 50px;
	z-index: 1;
	width: 100%;
}

.video-product-carousel .video-product-title {
	text-align: center;
	font-size: 24px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #ffffff;
}

.video-product-carousel .video-product-desc {
	text-align: center;
	font-size: 14px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #ffffff;
}

.video-product-slider .slick-slide img {
	border-radius: 4px;
}

.video-product-slider .product-name {
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.57;
	letter-spacing: normal;
	text-align: left;
	color: #212121;
	margin: 10px 0 15px 0;
}

.video-product-slider .product-price {
	font-size: 14px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.43;
	letter-spacing: normal;
	text-align: left;
	color: #212121;
}

.video-product-carousel .shop-all-btn {
	width: 120px;
	padding: 8px 0;
	border-radius: 100px;
	border: solid 2px #212121;
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: center;
	color: #212121;
	float: left;
	clear: both;
	margin-left: 15px;
	margin-bottom: 35px;
}

.theme-offers {
	background-repeat: no-repeat;
	background-size: cover;
}

.theme-offers .heading {
	font-size: 20px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #ffffff;
	margin: 20px 0 25px 15px;
}

.theme-offers-slider .slick-slide img {
	border-radius: 4px;
}

.theme-offers-slider .brand-name {
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.71;
	letter-spacing: normal;
	color: #ffffff;
	margin-top: 15px;
}

.theme-offers-slider .product-name {
	font-size: 14px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.43;
	letter-spacing: normal;
	color: #ffffff;
	margin: 5px 0;
	width: calc(100% - 15px);
}

.theme-offers-slider .product-price {
	font-size: 14px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.71;
	letter-spacing: normal;
	color: #ffffff;
	margin-top: 5px;
}

.theme-offers .shop-all-btn {
	width: 120px;
	padding: 8px 0;
	border-radius: 100px;
	border: solid 2px #ffffff;
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: center;
	color: #ffffff;
	float: left;
	clear: both;
	margin: 25px 0 35px 15px;
}

.line-through {
	text-decoration: line-through;
}

.theme-product-widget {
	background-repeat: no-repeat;
	background-size: cover;
}

.theme-product-widget .brand-logo {
	margin: 25px 0 0 15px;
}

.theme-product-widget-container {
	padding: 0;
	margin-top: 160px;
}

.theme-product-widget-slider {
	margin: 30px 0 25px 0;
}

.theme-product-widget-slider .slick-slide img {
	border-radius: 50%;
}

.theme-product-widget .heading {
	font-size: 20px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #ffffff;
}

.theme-product-widget .shop-all-btn {
	float: right;
	width: 120px;
	padding: 8px 0;
	border-radius: 100px;
	border: solid 2px #ffffff;
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: center;
	color: #ffffff;
	margin-top: -5px;
}

.theme-product-widget .product-name {
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.71;
	letter-spacing: normal;
	text-align: center;
	color: #ffffff;
	margin: 10px 0 5px 0;
	padding-right: 15px;
}

.theme-product-widget .product-price {
	font-size: 14px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.43;
	letter-spacing: normal;
	text-align: center;
	color: #ffffff;
	padding-right: 15px;
}

.banner-separator {
	padding: 25px 0 25px 15px;
	background-repeat: no-repeat;
	background-size: cover;
}

.banner-separator .title {
	font-size: 16px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.25;
	letter-spacing: normal;
	color: #ffffff;
	margin-bottom: 5px;
}

.banner-separator .desc {
	font-size: 14px;
	font-weight: normal;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #ffffff;
}

.mt2 {
	margin-top: 2px;
}

.auto-brand-product-slider {
	margin: 40px 0 25px 0;
}
/*.auto-brand-product-carousel .overlay-text{
		    position: absolute;
		    top: 100px;
		    text-align: center;
		}*/
.auto-brand-product-carousel .auto-brand-product-logo {
	font-size: 24px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #ffffff;
	position: absolute;
	top: 20px;
}

.auto-brand-product-carousel .auto-brand-product-desc {
	width: 328px;
	border-radius: 4px;
	background-color: #212121;
	padding: 15px 30px;
	font-size: 14px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.43;
	letter-spacing: normal;
	color: #ffffff;
	position: absolute;
	top: 115px;
	/*left: calc((100% - 328px)/2);*/
	margin: 0 15px;
}

.auto-brand-product-slider .slick-slide img {
	border-radius: 4px;
}

.auto-brand-product-slider .product-name {
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.57;
	letter-spacing: normal;
	text-align: left;
	color: #212121;
	margin: 10px 0 5px 0;
}

.auto-brand-product-slider .product-price {
	font-size: 14px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.43;
	letter-spacing: normal;
	text-align: left;
	color: #212121;
}

.auto-brand-product-carousel .shop-all-btn {
	width: 120px;
	padding: 8px 0;
	border-radius: 100px;
	border: solid 2px #212121;
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: center;
	color: #212121;
	float: left;
	clear: both;
	margin-left: 15px;
	margin-bottom: 35px;
}

.mtb15 {
	margin: 15px 0;
}

#play-video {
	color: #ffffff;
}

.curated-listing-strip {
	padding: 15px;
}

.curated-listing-strip a {
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: left;
	color: #ffffff;
}

.landing-page-header-component {
	background-repeat: no-repeat;
	background-size: cover;
}

.landing-page-header-component .follow-brand {
	float: right;
	margin: 15px 15px 0 0;
	width: 100px;
	padding: 8px 20px;
	border-radius: 100px;
	border: solid 2px #ffffff;
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: center;
	color: #ffffff;
}

.landing-page-header-component .banner-logo {
	margin: 121px 0 0 15px;
}

.landing-page-header-component .banner-desc {
	font-size: 30px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: left;
	color: #ffffff;
	margin: 15px 0 15px 15px;
}

.sub-brand-banner-blp {
	background-color: #ececec;
	padding: 40px 0;
}

.sub-brand-banner-blp .sub-brand-heading {
	font-size: 20px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: left;
	color: #212121;
	margin-bottom: 25px;
}

.subbrand-banner-blp-slider .sub-brand-img {
	border-radius: 50%;
}

.subbrand-banner-blp-slider .sub-brand-logo {
	margin: 15px auto 0 auto;
	padding-right: 15px;
}

.pr0 {
	padding-right: 0;
}

.pl0 {
	padding-left: 0;
}

.mb16 {
	margin-bottom: 16px;
}

.top-categories-widget .sub-brand-heading {
	font-size: 20px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #212121;
	margin: 25px 0;
}

.top-categories-widget .category-title {
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.29;
	letter-spacing: normal;
	text-align: center;
	color: #ffffff;
	position: absolute;
	top: 45%;
	left: 0;
}

.br4 {
	border-radius: 4px;
}

.curated-products-widget .heading {
	font-size: 20px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #212121;
	margin: 25px 0;
}

.curated-products-widget .brand-name {
	font-size: 16px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.5;
	letter-spacing: normal;
	color: #181818;
	margin-top: 10px;
}

.curated-products-widget .product-name {
	font-size: 14px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.43;
	letter-spacing: normal;
	color: #212121;
	margin: 5px 0;
}

.curated-products-widget .product-price {
	font-size: 14px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.71;
	letter-spacing: normal;
	color: #212121;
	margin-bottom: 25px;
}

.curated-products-widget .shop-all-btn {
	width: 120px;
	padding: 8px 0;
	border-radius: 100px;
	border: solid 2px #212121;
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: center;
	color: #212121;
	float: left;
	clear: both;
	margin-left: 15px;
	margin-bottom: 35px;
}

/**/
.product-recommendation-widget .heading {
	font-size: 20px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #212121;
	margin: 25px 0;
}

.product-recommendation-widget .brand-name {
	font-size: 16px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.5;
	letter-spacing: normal;
	color: #181818;
	margin: 20px 18px 0 0;
}

.product-recommendation-widget .product-name {
	font-size: 14px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.43;
	letter-spacing: normal;
	color: #212121;
	margin: 5px 15px 5px 0;
}

.product-recommendation-widget .product-price {
	font-size: 14px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.71;
	letter-spacing: normal;
	color: #212121;
	margin: 0 15px 25px 0;
}

.product-recommendation-widget .shop-all-btn {
	width: 120px;
	padding: 8px 0;
	border-radius: 100px;
	border: solid 2px #212121;
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: center;
	color: #212121;
	float: left;
	clear: both;
	margin-left: 15px;
	margin-bottom: 35px;
}

.smart-filter-widget {
	padding: 25px 15px;
}

.smart-filter-widget .overlay-div {
	position: absolute;
	top: 55px;
	width: calc(100% - 10px);
}

.smart-filter-widget .heading {
	font-size: 20px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #212121;
	margin-bottom: 25px;
}

.smart-filter-widget .category-title {
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.57;
	letter-spacing: normal;
	text-align: center;
	color: #ffffff;
}

.smart-filter-widget .category-desc {
	font-size: 14px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: 1.29;
	letter-spacing: normal;
	text-align: center;
	color: #ffffff;
}

/**/
.content-widget .heading {
	font-size: 20px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: left;
	color: #212121;
	margin: 25px 0;
}

.content-widget-slider {
	clear: both;
	padding-bottom: 25px;
}

.content-widget-slider .slick-slide {
	margin: 0 -5px;
	position: relative;
}

.content-widget-slider .content-subsection {
	width: 100%;
	position: absolute;
	top: 80px;
}

.content-widget-slider .content-title {
	font-size: 20px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: center;
	color: #ffffff;
}

.content-widget-slider .content-desc {
	font-size: 12px;
	font-weight: normal;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: justify;
	color: #ffffff;
	margin: 80px 0 20px 0;
	padding: 0 23px;
}

.content-widget-slider .content-link {
	font-size: 14px;
	font-weight: normal;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: center;
	color: #ffffff;
	text-decoration: underline;
}

.content-widget-slider .slick-slide img {
	width: 100%;
	display: block;
	opacity: 0.11;
	height: 348px;
	margin-top: 15px;
}

.content-widget-slider .slick-current img {
	opacity: 1;
	width: 100%;
	height: auto;
	display: block;
	margin-top: 0px;
	box-shadow: 0 5px 25px 0 rgba(0, 0, 0, 0.32);
}

.right0 {
	right: 0;
}

.mono-blp-banner {
	padding-bottom: 40px;
}

.mono-blp-banner .heading {
	font-size: 20px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #212121;
	margin: 25px 0;
}

.mono-blp-banner-container {
	border-radius: 4px;
	background-repeat: no-repeat;
	background-size: cover;
}

.mono-blp-banner-container .banner-title {
	margin-top: 190px;
	font-size: 30px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #ffffff;
}

.mono-blp-banner-container .banner-btn {
	margin: 15px 0 20px 15px;
	width: 141px;
	padding: 8px 0;
	border-radius: 100px;
	background-color: #ff1744;
	float: left;
	clear: both;
	color: #ffffff;
	text-align: center;
}

.landing-page-hierarchy-title {
	font-size: 16px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	text-align: left;
	color: #212121;
	margin-bottom: 15px;
}

.category-l1 {
	float: left;
	width: 100%;
	padding: 0 16px;
	margin: 0;
}

.category-l1, .category-l2, .category-l3 {
	list-style-type: none;
}

.category-l2, .category-l3 {
	display: none;
}

.category-l1 a {
	display: block;
	float: left;
	width: 100%;
	margin: 10px 0;
	font-size: 16px;
	font-weight: 400;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #212121;
	position: relative;
	text-decoration: none;
}

.category-l2 a {
	font-size: 15px;
	font-weight: 300;
}

.category-l3 a {
	font-size: 14px;
}

.category-l1 a.has-carrot::after {
	font-family: FontAwesome;
	content: "\f107";
	font-size: 18px;
	position: absolute;
	display: block;
	width: auto;
	height: auto;
	top: 0;
	color: #000000;
	right: 5px;
}

.category-l1 a.active-ul::after, #following-brands.show-brands::after {
	transform: rotate(180deg);
}

.active-ul {
	font-weight: 500 !important;
}

.view-more-categories, .view-less-categories {
	font-size: 14px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #ff1744;
	width: auto;
	float: left;
	display: inline-block;
	margin: 15px 0 15px 15px;
}

.view-more-categories::after, .view-less-categories::after {
	content: " ";
	width: auto;
	border-bottom: 1px solid;
	display: block;
	padding-bottom: 2px;
}

.landing-page-hierarchy {
	padding: 25px 0 5px 0;
}

.brand-tab-azlist .nav-tabs>li {
	width: 25%;
	text-align: center;
}

.brand-tab-azlist .nav-tabs>li>a {
	font-size: 14px;
	font-weight: 500;
	color: #21212159;
}

.brand-tab-azlist .nav-tabs>li.active>a {
	border: none;
	border-bottom: 2px solid #ff1744;
	color: #ff1744;
}

.brands-slider {
	margin: 20px 0;
}

.brands-slider .slick-slide {
	margin: 0 5px;
	position: relative;
}

.brands-slider .slick-slide img {
	width: 100%;
	height: 130px;
	margin-top: 5px;
	display: block;
	border-radius: 4px;
}

.brands-slider .slick-current img {
	width: 100%;
	height: auto;
	display: block;
	margin-top: 0px;
}

.brands-slider .brands-slider-subsection {
	position: absolute;
	top: 50px;
	left: 60px;
}

.brands-slider .brand-logo {
	width: auto !important;
	height: auto !important;
	margin-left: 15px;
}

#following-brands {
	font-size: 16px;
	font-weight: 500;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #212121;
	margin-bottom: 15px;
	text-decoration: none;
}

#following-brands::after {
	font-family: FontAwesome;
	content: "\f107";
	font-size: 18px;
	position: absolute;
	display: block;
	width: auto;
	height: auto;
	top: 0;
	color: #000000;
	right: 15px;
}

.following-brands .brand-logo-container {
	width: 90px;
	height: 90px;
	background-color: #000000;
	border-radius: 50%;
	position: relative;
}

.following-brands .brand-logo {
	position: absolute;
	left: 0;
	right: 0;
	bottom: 0;
	top: 0;
	margin: auto;
}

.following-brands-slider {
	float: left;
	width: 100%;
	display: block;
	margin-bottom: 20px;
}

#search-for-brand {
	margin-bottom: 20px;
	width: 100%;
	padding: 8px 10px;
	border-radius: 2px;
	border: solid 1px #d2d2d2;
}

#search-for-brand::placeholder {
	font-size: 14px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	line-height: normal;
	letter-spacing: normal;
	color: #8d8d8d;
}

.searchicon-for-brand {
	position: absolute;
	top: 10px;
	right: 30px;
	color: #8d8d8d;
}

.all-brands-list ul, .all-brands-list ul li {
	list-style-type: none;
}

.all-brands-list ul {
	padding: 0;
}

.mb20 {
	margin-bottom: 20px;
}

.brandname-list li {
	font-size: 14px;
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	letter-spacing: normal;
	color: #212121;
	margin-bottom: 10px;
}

.brandname-list {
	margin-bottom: 20px;
}

.brandslist-leftsection {
	height: 540px;
	overflow-y: auto;
}

#brandinitials-section li a {
	font-weight: 300;
	font-style: normal;
	font-stretch: normal;
	letter-spacing: normal;
	color: #000000;
	border-radius: 50%;
	width: 20px;
	height: 20px;
	display: inline-block;
	text-align: center;
	text-decoration: none;
}

.text-bold {
	font-weight: 500;
}

.active-brandinitial {
	border: 1px solid #ff1744;
}
</style>

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
						<h1>OffersWidgetComponent</h1>


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
							<c:set var="status" value="${loop}" />
						</c:forEach>

						<c:forEach items="${feature.items}" var="flashsalesitemElements"
							begin="0" end="${3 - loop}">
							<c:if
								test="${flashsalesitemElements.typeCode eq 'FlashSalesItemElement'}">
								<c:if test="${not empty flashsalesitemElements.productCode}">
									<div class="col-xs-6 flash-sales-widget-product">
										<a href="${flashsalesitemElements.webURL}"> <c:if
												test="${not empty flashsalesitemElements.productCode}">
												<img src="${flashsalesitemElements.productCode.picture.URL}"
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
														src="${bannerProdCarouselElement.productCode.picture.URL}" />
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
													src="${videoProdCarouselElement.productCode.picture.URL}" />
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
								<td>${feature.backgroundHexCode}</td>
								<td><img alt="" src="${feature.backgroundImageURL.URL}"></td>
								<td>${feature.btnText}</td>
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
									<th>productCode</th>
								</tr>
							</thead>
							<c:forEach items="${feature.items}" var="themeOffersItemsElement">
								<div>
									<c:if
										test="${themeOffersItemsElement.typeCode eq 'ThemeOffersItemsElement'}">
										<tr>
											<td>${themeOffersItemsElement.title}</td>
											<td><a href="${themeOffersItemsElement.webURL}">${themeOffersItemsElement.webURL}</a></td>
											<c:if test="${not empty themeOffersItemsElement.productCode}">
												<td>${themeOffersItemsElement.productCode.code}</td>
											</c:if>
										</tr>
									</c:if>
								</div>
							</c:forEach>
						</table>

					</div>
				</c:if>


				<c:if test="${feature.typeCode eq 'ThemeProductWidgetComponent'}">

					<div class="col-xs-12 pad0 theme-product-widget mb40"
						style="background-image:url('${feature.imageURL.URL}'),linear-gradient(to bottom, #5b0627, #5b0627);">
						<img src="${feature.brandLogo}" class="brand-logo" />
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
															src="${themeProductWidgetElement.productCode.picture.URL}" />
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
													src="${automatedBrandProductCarElement.productCode.picture.URL}" />
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
													${topCategoriesWidgetElement.webURL}</a></td>
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


					<div class="col-xs-12 pad0 mb40 curated-products-widget">
						<div class="col-xs-12 heading">${feature.title}</div>
						<c:forEach items="${feature.items}"
							var="curatedProductsWidgetElement">
							<div class="col-xs-6">
								<a href="${feature.webURL}"> <c:if
										test="${not empty curatedProductsWidgetElement.productCode}">

										<img
											src="${curatedProductsWidgetElement.productCode.picture.URL}"
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
					<div>
						<h1>BrandsTabAZListComponent</h1>
						<table class="">
							<thead>
								<tr>
									<th>subType</th>

								</tr>

							</thead>
							<c:forEach items="${feature.items}" var="brandsTabAZElement">
								<div>
									<c:if
										test="${brandsTabAZElement.typeCode eq 'BrandsTabAZElement'}">
										<tr>
											<td style="font-weight: bold;">${brandsTabAZElement.subType}</td>

										</tr>
										<br>
									</c:if>
								</div>

								<table class="">
									<thead>
										<tr>
											<th>webURL</th>
											<th>brandName</th>
										</tr>
									</thead>
									<c:forEach items="${brandsTabAZElement.brands}"
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

								<c:forEach items="${brandsTabAZElement.items}"
									var="brandTabAZHeroBannerElement">


									<c:if
										test="${brandTabAZHeroBannerElement.typeCode eq 'HeroBannerComponent'}">
										<div>
											<c:forEach items="${brandTabAZHeroBannerElement.items}"
												var="heroElements">
												<c:if
													test="${not empty heroElements && heroElements.typeCode eq 'HeroBannerElement'}">
													<h1>HeroBannerComponent oF BrandsTabAZListComponent--
														${brandsTabAZElement.subType}</h1>
													<table class="">
														<thead>
															<tr>
																<th>title</th>
																<th>webURL</th>
																<th>imageURL</th>
																<th>brandLogo</th>
															</tr>
														</thead>

														<tr>
															<td>${heroElements.title}</td>
															<td><a href="${heroElements.webURL}">${heroElements.webURL}</a></td>
															<td><img alt="" src="${heroElements.imageURL.URL}"></td>
															<td><img alt="" src="${heroElements.brandLogo.URL}"></td>
														</tr>

													</table>
												</c:if>

											</c:forEach>

										</div>
									</c:if>


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
								<td>${feature.btnText}</td>
								<td><a href="${feature.fetchURL}"> ${feature.fetchURL}</a></td>
								<td><a href="${feature.backupURL}">
										${feature.backupURL}</a></td>
								<td>${feature.widgetPlatform}</td>
							</tr>
						</table>
						<table class="">
							<thead>
								<tr>
									<th>title</th>
									<th>webURL</th>
									<th>productCode</th>
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
											<c:if
												test="${not empty autoProductRecommendationElement.productCode}">
												<td>${autoProductRecommendationElement.productCode.code}</td>
											</c:if>
										</tr>
									</c:if>
								</div>
							</c:forEach>
						</table>
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
<script type="text/javascript"
	src="https://static.tatacliq.com/_ui/responsive/common/js/jquery-2.1.1.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="/_ui/responsive/common/js/mpl/slick.min.js"></script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {

						//dropdown menu js starts 
						$('.category-l1 a').click(
								function(e) {
									e.preventDefault();
									if ($(this).next('ul').length > 0) {

										$(this).parent().siblings().find('ul')
												.hide();
										$(this).parent().siblings().find('a')
												.removeClass('active-ul');

										$(this).next('ul').toggle();
										$(this).toggleClass('active-ul');

									}
								});

						if ($('.category-l1 a').next('ul').length > 0) {
							$('.category-l1 a').next('ul').prev('a').addClass(
									'has-carrot');
						}

						if ($('.category-l1 > li').length > 8) {
							$('.category-l1 > li').slice(8).hide();
						} else {
							$('.view-more-categories').hide();
						}

						var hidden_categories = $('.category-l1 > li').length - 8;
						$('#hidden_categories_count').text(hidden_categories);

						$(document).on(
								'click',
								'.view-more-categories',
								function(e) {
									$('.category-l1 > li').slice(8).show();
									$(this).removeClass('view-more-categories')
											.addClass('view-less-categories');
									$('#hidden_categories_text').text('less');
								});

						$(document).on(
								'click',
								'.view-less-categories',
								function(e) {
									$('.category-l1 > li').slice(8).hide();
									$(this).removeClass('view-less-categories')
											.addClass('view-more-categories');
									$('#hidden_categories_text').text('more');
								});

						//dropdown menu js ends

						//toggle following brands
						$('#following-brands').click(function() {
							$(this).toggleClass('show-brands');
							$('.following-brands-slider').slick('refresh');
						});

						//search within brand list
						$('#search-for-brand')
								.keyup(
										function(e) {
											var input, filter, ul, li, a, i;
											input = document
													.getElementById("search-for-brand");
											filter = input.value.toUpperCase();
											ul = document
													.getElementById("brandslist-leftsection");
											li = ul.getElementsByTagName("li");
											for (i = 0; i < li.length; i++) {
												//a = li[i].getElementsByTagName("a")[0];
												if (li[i].innerHTML
														.toUpperCase().indexOf(
																filter) > -1) {
													li[i].style.display = "";
													li[i].parentElement.previousElementSibling.style.display = "";
													li[i].parentElement.style.display = "";
												} else {
													li[i].style.display = "none";
													li[i].parentElement.previousElementSibling.style.display = "none";
													li[i].parentElement.style.display = "none";
												}
											}
										});

						$('#brandinitials-section li a')
								.click(
										function(e) {
											e.preventDefault();
											var brandinitial = $(this).attr(
													'href');
											if ($(brandinitial).length > 0) {
												var brandinitial_offset = document
														.getElementById(brandinitial
																.substr(1)).offsetTop;
												document
														.getElementById('brandslist-leftsection').scrollTop = brandinitial_offset;
											}
											$('#brandinitials-section li a')
													.removeClass(
															'active-brandinitial');
											$(this).addClass(
													'active-brandinitial');
										});

						$('.following-brands-slider').slick({
							centerMode : true,
							centerPadding : '20px',
							slidesToShow : 3,
							responsive : [ {
								breakpoint : 480,
								settings : {
									arrows : false,
									centerMode : true,
									centerPadding : '12px',
									slidesToShow : 3,
									useCSS : true,
									cssEase : 'ease'
								}
							} ]
						});

						$('.hero-slider').slick({
							centerMode : true,
							centerPadding : '60px',
							slidesToShow : 1,
							responsive : [ {
								breakpoint : 480,
								settings : {
									arrows : false,
									centerMode : true,
									centerPadding : '20px',
									slidesToShow : 1,
									useCSS : true,
									cssEase : 'ease'
								}
							} ]
						});

						$('.offer-slider').slick({
							centerMode : true,
							centerPadding : '60px',
							slidesToShow : 1,
							responsive : [ {
								breakpoint : 480,
								settings : {
									arrows : false,
									centerMode : true,
									centerPadding : '15px',
									slidesToShow : 1,
									useCSS : true,
									cssEase : 'ease'
								}
							} ]
						});

						$('.banner-product-slider').slick({
							centerMode : true,
							centerPadding : '60px',
							slidesToShow : 2,
							responsive : [ {
								breakpoint : 480,
								settings : {
									arrows : false,
									centerMode : true,
									centerPadding : '15px',
									slidesToShow : 2,
									slidesToScroll : 2,
									useCSS : true,
									cssEase : 'ease'
								}
							} ]
						});

						$('.video-product-slider').slick({
							centerMode : true,
							centerPadding : '60px',
							slidesToShow : 2,
							responsive : [ {
								breakpoint : 480,
								settings : {
									arrows : false,
									centerMode : true,
									centerPadding : '15px',
									slidesToShow : 2,
									slidesToScroll : 2,
									useCSS : true,
									cssEase : 'ease'
								}
							} ]
						});

						$('.theme-offers-slider').slick({
							centerMode : true,
							centerPadding : '60px',
							slidesToShow : 2,
							responsive : [ {
								breakpoint : 480,
								settings : {
									arrows : false,
									centerMode : true,
									centerPadding : '15px',
									slidesToShow : 2,
									slidesToScroll : 2,
									useCSS : true,
									cssEase : 'ease'
								}
							} ]
						});

						$('.theme-product-widget-slider').slick({
							centerMode : true,
							centerPadding : '60px',
							slidesToShow : 2,
							responsive : [ {
								breakpoint : 480,
								settings : {
									arrows : false,
									centerMode : true,
									centerPadding : '15px',
									slidesToShow : 2,
									slidesToScroll : 2,
									useCSS : true,
									cssEase : 'ease'
								}
							} ]
						});

						$('.auto-brand-product-slider').slick({
							centerMode : true,
							centerPadding : '60px',
							slidesToShow : 2,
							responsive : [ {
								breakpoint : 480,
								settings : {
									arrows : false,
									centerMode : true,
									centerPadding : '15px',
									slidesToShow : 2,
									slidesToScroll : 2,
									useCSS : true,
									cssEase : 'ease'
								}
							} ]
						});

						$('.subbrand-banner-blp-slider').slick({
							centerMode : true,
							centerPadding : '60px',
							slidesToShow : 2,
							responsive : [ {
								breakpoint : 480,
								settings : {
									arrows : false,
									centerMode : true,
									centerPadding : '15px',
									slidesToShow : 2,
									slidesToScroll : 2,
									useCSS : true,
									cssEase : 'ease'
								}
							} ]
						});

						$('.product-recommendation-slider').slick({
							centerMode : true,
							centerPadding : '60px',
							slidesToShow : 2,
							responsive : [ {
								breakpoint : 480,
								settings : {
									arrows : false,
									centerMode : true,
									centerPadding : '15px',
									slidesToShow : 2,
									slidesToScroll : 2,
									useCSS : true,
									cssEase : 'ease'
								}
							} ]
						});

						//content widget
						$('.content-widget-slider').slick({
							centerMode : true,
							centerPadding : '60px',
							slidesToShow : 1,
							responsive : [ {
								breakpoint : 480,
								settings : {
									arrows : false,
									centerMode : true,
									centerPadding : '35px',
									slidesToShow : 1,
									useCSS : true,
									cssEase : 'ease'
								}
							} ]
						});

						$('.brands-slider').slick({
							centerMode : true,
							centerPadding : '60px',
							slidesToShow : 1,
							responsive : [ {
								breakpoint : 480,
								settings : {
									arrows : false,
									centerMode : true,
									centerPadding : '20px',
									slidesToShow : 1,
									useCSS : true,
									cssEase : 'ease'
								}
							} ]
						});

						//countdown js starts
						// Set the date we're counting down to
						var end_time = $('#end_time').val();
						if (end_time) {
							var countDownDate = new Date(end_time).getTime();
							//var countDownDate = new Date("2018-04-03 13:00:00").getTime();
							// Update the count down every 1 second
							var x = setInterval(
									function() {
										// Get todays date and time
										var now = new Date().getTime();
										// Find the distance between now an the count down date
										var distance = countDownDate - now;
										// Time calculations for days, hours, minutes and seconds
										var days = Math.floor(distance
												/ (1000 * 60 * 60 * 24));
										var hours = Math
												.floor((distance % (1000 * 60 * 60 * 24))
														/ (1000 * 60 * 60));
										var minutes = Math
												.floor((distance % (1000 * 60 * 60))
														/ (1000 * 60));
										var seconds = Math
												.floor((distance % (1000 * 60)) / 1000);
										// Output the result in an element with id="countdown"
										document.getElementById("countdown").innerHTML = hours
												+ ":" + minutes + ":" + seconds;
										// If the count down is over, write some text 
										if (distance < 0) {
											clearInterval(x);
											document
													.getElementById("countdown").innerHTML = "Sale Started";
										}
									}, 1000);
						}
						//countdown js ends

						//play video js starts
						function playPause(myVideo) {
							if (myVideo.paused) {
								myVideo.play();
							} else {
								myVideo.pause();
							}
						}
						$('#play-video').click(function() {
							var myVideo = document.getElementById("video-el");
							playPause(myVideo);
							$('#vpc-container').hide();
						});
						//play video js ends

					});
</script>
	</html>
</template:page>