<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/pwamp/footer"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!doctype html>
<html amp>
<head>
<meta charset="utf-8">
<script async src="https://cdn.ampproject.org/v0.js"></script>
<script async custom-element="amp-analytics" src="https://cdn.ampproject.org/v0/amp-analytics-0.1.js"></script>
<script async custom-element="amp-font" src="https://cdn.ampproject.org/v0/amp-font-0.1.js"></script>
<script async custom-element="amp-sidebar" src="https://cdn.ampproject.org/v0/amp-sidebar-0.1.js"></script>
<script async custom-element="amp-selector" src="https://cdn.ampproject.org/v0/amp-selector-0.1.js"></script>
<script async custom-element="amp-carousel" src="https://cdn.ampproject.org/v0/amp-carousel-0.1.js"></script>
<script async custom-element="amp-accordion" src="https://cdn.ampproject.org/v0/amp-accordion-0.1.js"></script>
<script async custom-element="amp-iframe" src="https://cdn.ampproject.org/v0/amp-iframe-0.1.js"></script>
<script async custom-element="amp-image-lightbox" src="https://cdn.ampproject.org/v0/amp-image-lightbox-0.1.js"></script>
<script async custom-element="amp-form" src="https://cdn.ampproject.org/v0/amp-form-0.1.js"></script>
<script async custom-template="amp-mustache" src="https://cdn.ampproject.org/v0/amp-mustache-0.1.js"></script>
<script async custom-element="amp-list" src="https://cdn.ampproject.org/v0/amp-list-0.1.js"></script>
<script async custom-element="amp-install-serviceworker" src="https://cdn.ampproject.org/v0/amp-install-serviceworker-0.1.js"></script>
<!--AMP HTML files require a canonical link pointing to the regular HTML. If no HTML version exists, it should point to itself.-->
<link rel="canonical" href="/pwamp">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:100,100i,300,300i,400,400i,500,500i,700,700i,900,900i">
<link rel="manifest" href="/manifest.json">
<meta name="viewport" content="width=device-width,minimum-scale=1,initial-scale=1,maximum-scale=1,user-scalable=no"><meta name="apple-mobile-web-app-capable" content="yes"/><meta name="apple-mobile-web-app-status-bar-style" content="black">
<%-- <c:set var= "host" value="statshost.publishersite.com"/> --%>
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('amp.analytics.host.adobe')" var="host"/>

<style amp-custom>body{font-family:'Montserrat', sans-serif; font-size:14px; background-color:#FFFFFF;}

p a{display:inline;}

ul {padding: 0;}

.footer-child ul li {
	list-style-type: none;
}

.footer-child a, .footer-child h4 {letter-spacing: .6px;}

a {
	display: block;
	position:relative;
}

p{
	font-weight:400;
	color:#666666;
	font-size:14px;
	margin-bottom:30px;
}

a{text-decoration:none;}

header{
	position:fixed;
	/*height:60px;*/
	background-color:#fff;
	width:100%;
	z-index:99999;
}

.header-icon-1, .header-icon-2, .header-icon-3, .header-search-section{
	position:absolute;
	color:#000000;
	line-height:60px;
	text-align:center;
	width:60px;
	display:block;
	font-size:14px;
	background-color:transparent;
}

.header-icon-1 {z-index: 10;}

.header-icon-2{
	right:0px;
	top:0px;
}

.header-logo{
	background-image:url(images/logo.png);
	background-size:35px 35px;
	width:35px;
	height:35px;
	display:block;
	margin:12px auto 0px auto;
}

.header-clear{
	height:98px;
}

/*Footer*/
.footer-logo{
	background-image:url(images/logo.png);
	background-size:40px 40px;
	width:40px;
	height:40px;
	display:block;
	margin:12px auto 20px auto;
}

#sidebar{
	width:250px;
	background-color:#FFFFFF;
}


.sidebar-menu {margin-left:15px; margin-bottom:0px;}

.sidebar-menu i{
	font-size:14px;
	width:35px;
	height:35px;
	line-height:35px;
	text-align:center;
	border-radius:35px;
}

.sidebar-menu ul li a .fa-circle{font-size:4px; margin-left:1px;}
.sidebar-menu .fa-circle, .sidebar-menu .fa-angle-down{
	width:35px;
	height:35px;
	position:absolute;
	right:0px;
	font-size:4px;
	color:#acacac;
}

.sidebar-menu .fa-angle-down{font-size:14px;}

.sidebar-menu section[expanded] .fa-angle-down{
	transform:rotate(180deg);
}

.sidebar-menu h4{
	background:none;
	border:none;
    color: #3a3a3a;
    line-height: 40px;
    font-size: 16px;
    font-weight: 400;
}

.sidebar-menu h4 a {color: #3a3a3a;}

.sidebar-menu ul{
	padding:0px 0px 20px 10px;
}

.sidebar-menu ul li a{
	color:#6b757d;
	font-size:14px;
	line-height:30px;
}

.sidebar-menu i:first-child{
	font-size:14px;
}

.sidebar-menu li a .fa-circle{margin-top:-5px;}

.sidebar-divider-item{
    font-family: 'Montserrat';
    font-size: 14px;
    padding-left: 20px;
    color: #3a3a3a;
    border-top: solid 1px rgba(0,0,0,0.1);
	line-height:40px;
}
.sidebar-divider-item a {color: #3a3a3a;}
.sidebar-divider-item a i {width: 20px;}

.sidebar-menu .sidebar-item{
	color:#1f1f1f;
	font-size:12px;
	line-height:60px;
}

section[expanded] .fa-plus{	transform:rotate(45deg);}
section[expanded] .fa-angle-down{	transform:rotate(180deg);}
section[expanded] .fa-chevron-down{	transform:rotate(180deg);}

ol, ul{line-height:24px; margin-left:20px;}

.sidebar-header a{
padding-top:0px;
}

/*CSS by Vamshi*/
.header-navigation-section {
	position: static;
	width: 100%;
	line-height: normal;
	margin: 0px;
	background-color: #a9143c;
	display: inline-block;
}

.header-navigation-tab {width: 150px; float: left; text-align: center;}

.header-navigation-tab a {
	text-transform: uppercase;
	padding: 11px 20px;
	font-size: 11px;
  letter-spacing: .6px;
  line-height: 14px;
  font-weight: 400;
	color: #a9143c;
}

.header-navigation-tab a:hover {text-decoration: underline;}
.marketplace-tab {background-color: #fff;}
.luxury-tab {color: #fff; background-color: #000;}
.luxury-tab a {color: #fff;}
.luxury-tab:hover {opacity: 0.4;}

.header-navigation-right {
	float: right;
}

.header-navigation-right ul li {list-style-type: none; display: inline-block; padding: 10px 15px;; font-size: 12px;}
.header-navigation-right ul {line-height: normal; margin: 0;}
.header-navigation-right ul li a {letter-spacing: .6px; color: #fff;}
.logo-image {
	left: 40px;
}

.header-icon-3{
	right:60px;
	top:0px;
}

.header-search-section {
	width: 100%;
	position: static;
	margin: 0px;
	line-height: normal;
}

.header-search-left {
	width: 35%;
	float: left;
}

.header-search-center {
	width: 40%;
	float: right;
}

.header-search-center p {}

.header-search-right {
	width: 15%;
	float: right;
}

.header-search-left-child {
	float: left;
	text-align: left;
	font-size: 12px;
  padding: 10px 0px;
  height: 40px;
  margin-right: 100px;
}

.header-search-left-child strong {
	cursor: pointer;
}

.header-search-input {
  display: inline;
  border: 1px solid #ddd;
  padding: 10px;
  width: 70%;
}

.header-search-btn {padding: 10px; background-color: #444; border: 1px solid #444; color: white;}

.shop-by-department, .shop-by-brand {display: none; height: 500px; width: 100%; position: absolute; border-top: 1px solid #ddd; background: #fff; z-index: 1; left: 0; top: 60px;}

.shop-by-department-l2, .shop-by-brand-l2 {width: 17%; float: left; background: #f6f6f6; height: inherit;}

.shop-by-department-l2 > ul, .shop-by-brand-l2 > ul {margin: 0;}

.shop-by-department-l2 > ul > li, .shop-by-brand-l2 > ul > li {list-style-type: none; font-size: 16px; padding: 10px 20px;}

.shop-by-department-l2 > ul > li:hover, .shop-by-brand-l2 > ul > li:hover {background-color: #444; color: white;}

.shop-department:hover > #shop_by_department, .shop-brand:hover > #shop_by_brand {display: block;}

.shop-by-department-l3, .shop-by-brand-l3 {
  display: none; width: 78%; position: absolute; z-index: 2; top: 0px; color: #333; left: 17%; padding: 10px 20px;
  max-height: 450px;
  overflow: auto;
}

.shop_by_department_l3_content, .shop_by_department_l3_content > li > ul  {margin-left: 0;}

.shop_by_department_l3_content > li {
  display: inline-block;
  list-style-type: none;
  width: 18%;
  float: left;
  padding: 5px;
}

.sbd_l3_headings {font-weight: 600; text-transform: uppercase; color: #000;}

.shop_by_department_l3_content > li > ul >li > ul {margin-left: 0;}

.shop_by_department_l3_content > li > ul >li > ul > li {list-style-type: none;}

.shop_by_department_l3_content > li > ul >li > ul > li:hover, .sbd_l3_headings:hover {color: #a9133d;}

.shop_by_department_l3_content > li > ul >li {
  list-style-type: none;
  color: #777;
  font-size: 12px;
  letter-spacing: .6px;
  cursor: pointer;
}

/* .shop-by-brand, .shop-by-brand-l3 {display: block;}*/
.shop-by-department-l2 ul li:hover .shop-by-department-l3, .shop-by-brand-l2 ul li:hover .shop-by-brand-l3 {display: block;}

.shop-by-brand-l3 .shop-by-brand-l3-component {
  display: inline-block;
  margin-right: 20px;
  margin-top: 20px;
  width: 18%;
  float: left;
}

.shop-by-brand-l3-component:hover {box-shadow: #d0d0d0 0 0 4px 4px}

.shop-by-brand-l3-component amp-img:last-child {
  margin: 5px 60px;
}

span.letter-spacing {color: #666; line-height: 15px; font-size: 11px;}
.letter-spacing {letter-spacing: .6px;}

.header-category {
	padding-top: 15px;
  padding-bottom: 5px;
  margin-right: 50px;
}

.header-search-right {padding: 10px 0;}

.header-search-center {padding-top: 10px; padding-bottom: 10px;}

.mobile-item {
	display: none;
}

.my-bag {
	background-color: #000;
	color: #fff;
}

.logo-container {
	margin-left: -20px;
}

@media(max-width: 748px) {
	.desktop-item {
		display: none;
	}
	.mobile-item {
		display: block;
	}
	.header-search-center {
		width: 100%;
		padding-top: 10px;
		/*ToggleSearch*/
		display: none;
	}

	.logo-container {
		width: 100%;
		margin-left: 0;
	}

	/*.header-clear{
		height:100px;
	}*/
}

@media (max-width: 480px) {
	#topDealsCompCarousel amp-carousel, #whatToBuyCompCarousel amp-carousel, #newInCompCarousel amp-carousel, #hotNowCompCarousel amp-carousel {
		/*give important*/
		max-height: 180px;
	}

  #topDealsCompCarousel amp-list, #whatToBuyCompCarousel amp-list, #newInCompCarousel amp-list, #hotNowCompCarousel amp-list {
		/*give important*/
		max-height: 180px;
	}
}

/*Top Deals*/
.topDealsTopSection {padding: 0px 20px;}

.topDealsItem {
	width: 232px; height: 320px; margin: 0px 10px; box-shadow: 0 2px 3px 0 rgba(0, 0, 0, 0.1);
}

.topDealsItemImg {height: inherit;}
.topDealsItemImg a {height: inherit;}
#topDealsComp {display: block; padding: 10px 0px 20px; padding: 20px 60px;}

@media(max-width: 480px) {
	#topDealsComp {padding: 5px 0px 15px;}
	.topDealsItem {width: 120px; height: 180px;}
}

/*Brand Studio CSS*/
.homeViewAllBtn a, .homeViewAllBtn a:hover {float: right; font-size: 18px; color: #4990e2; text-decoration: none; display: none;}
.homeViewHeading {text-align: center; font-size: 28px; padding-bottom: 20px; font-weight: 400;
}
p {margin: 0;}
.brandsYouLove {margin: 20px;}
#brandsYouLove {display: none}
#brandsYouLoveMobileComp {display: block; background-color: #f2f2f2;padding: 20px; }
.brandsCarouselItem {width: 260px; height: 320px; margin: 0px 10px; border-radius: 4px; background-color: white; box-shadow: 0 2px 3px 0 rgba(0, 0, 0, 0.1);}
.brandStudioImg {height: 180px;}
.brandStudioImg a {height: inherit;}
.brandStudioImg img {width: 100%; height: 100%; border-top-left-radius: 4px; border-top-right-radius: 4px;}
.brandStudioDescHeading, .brandStudioDescInfo, .brandStudioVisitStore {padding: 10px 8px 0px; text-align: left;}
.brandStudioDescHeading {font-size: 13px; font-weight: bold;}
.brandStudioDescInfo {font-size: 12px; color: #666666; white-space: pre-line; height: 75px;}
.brandStudioVisitStore, .brandStudioVisitStore:hover {font-size: 13px; color: #0066c0;}

.brandStudioBottom {display: block; text-align: center; padding: 30px;}
.brandStudioBottom a {display: -webkit-inline-box;}
.brandStudioViewAllBtn {background-color: grey; padding: 10px 30px; border: none; color: white; cursor: pointer;}

@media(max-width: 480px) {
	.brandStudioBottom {display: none;}
	.homeViewAllBtn a {display: block; font-size: 14px;}
	.homeViewHeading {text-align: left; font-size: 18px; padding-bottom: 5px;}
	.brandStudioTop {padding: 0px 20px;}
	.brandsYouLove {margin: 20px;}
	#brandsYouLove {display: none;}
	#brandsYouLoveMobileComp {display: block; background-color: #f2f2f2; padding: 5px 0px 15px;}
	.brandStudioImg {height: 180px;}
	.brandStudioImg img {width: 100%; height: 100%; border-top-left-radius: 4px; border-top-right-radius: 4px;}
	.brandStudioDescHeading, .brandStudioDescInfo, .brandStudioVisitStore {padding: 14px 8px 0px; text-align: left;}
	.brandStudioDescHeading {font-size: 13px;}
	.brandStudioDescInfo {font-size: 12px; color: #666666;white-space: pre-line;}
	.brandStudioVisitStore, .brandStudioVisitStore:hover {font-size: 13px; color: #0066c0;}

	.amp-scrollable-carousel-slide:first-child {
		margin-left: 10px;
	}
	.amp-scrollable-carousel-slide:last-child {
		margin-right: 10px;
	}
}

/*What To Buy Now*/
.whatToBuyTopSection {padding: 0px 20px;}

.whatToBuyItem {
	width: 300px; height: 400px; margin: 0px 10px; box-shadow: 0 2px 3px 0 rgba(0, 0, 0, 0.1);
}

.whatToBuyItemImg, .whatToBuyItemImg a {height: inherit;}
#whatToBuyComp {display: block; padding: 10px 0px 20px; padding: 20px 60px;}

@media(max-width: 480px) {
	#whatToBuyComp {padding: 5px 0px 15px;}
	.whatToBuyItem {width: 140px; height: 180px;}
}

/*New In*/
.newInTopSection {padding: 0px 20px;}

.newInItem {
	width: 180px; height: 320px; margin: 0px 12px; box-shadow: 0 2px 3px 0 rgba(0, 0, 0, 0.1);
}

.compContainer {width: 100%;}
.newInItemImg {height: 85%;}
.newInItemImg a {height: inherit;}
#newInComp {display: block; padding: 10px 0px 20px; padding: 20px 60px; width: 43%; float: left;}

.newInDesc {padding: 0 5px;}
.newInDescName {padding: 5px 0; text-overflow: ellipsis; overflow: hidden;}
.newInDescPrice {text-decoration: line-through;}
.price-right {text-decoration: none; float: right;}

@media(max-width: 480px) {
	#newInComp {padding: 5px 0px 15px; width:100%;}
	.newInItem {width: 120px; height: 180px;}
}

/*Stay Qued*/
.stayOne {width: 50%; float: left; padding: 10px;}
.stayTwo {width: 40%; float: left; padding: 10px;}

.stayQuedTopSection {padding: 0px 20px;}

.stayQuedItemImg {height: inherit;}
#stayQuedComp {display: block; padding: 10px 20px 10px 0; width: 45%; float: left;}
.stayQuedCenter {background-color: #f2f2f2; height:320px; padding: 0 0 0 10px;}

.stayQuedBottom {display: block; text-align: center; padding: 10px;}
.stayQuedBottom a {display: -webkit-inline-box;}
.stayQuedViewAllBtn {background-color: grey; padding: 10px 30px; border: none; color: white; cursor: pointer;}

@media(max-width: 480px) {
	#stayQuedComp {padding: 5px 0px 15px; width: 100%;}
	.stayQuedHeading {font-size: 18px;}
	.stayQuedViewAllBtn {padding: 5px 15px;}
	.stayOne {float: right;}
	.stayOne, .stayTwo {padding: 10px; width: 42%;}
	.stayQuedCenter {padding: 0 10px 0 0;}
}

/*Hot Now*/
.hotNowTopSection {padding: 0px 20px;}

.hotNowItem {
	width: 200px; height: 320px; margin: 0px 10px; box-shadow: 0 2px 3px 0 rgba(0, 0, 0, 0.1);
}

.hotNowItemImg {height: 80%;}
.hotNowItemImg a {height: inherit;}
#hotNowComp {display: block; padding: 10px 0px 20px; padding: 20px; width: 100%; float: left;}

.hotNowMenu {float: right;}
.hotNowDesc {padding: 0 5px;}
.hotNowDescName, .hotNowDescTitle {padding: 2px 0; text-overflow: ellipsis; overflow: hidden; font-size: 12px;}
.hotNowDescPrice {text-decoration: line-through; font-size: 12px;}
.price-right {text-decoration: none; float: right;}

@media(max-width: 480px) {
	#hotNowComp {padding: 5px 0px 15px; width:100%;}
	.hotNowItem {width: 120px; height: 180px;}
}

/*Inspire Me*/
#inspireMeComp {width: 100%; float: left;}
.inspireMeCompSection {background-color: #f2f2f2; padding: 30px 150px;}

.inspireMeOne, .inspireMeTwo, .inspireMeThree {width: 30%; float: left; padding: 0 10px;}

.inspireMeBottom {padding-top: 20px;}

.inspireMeReadMoreBtn {
	background-color: white;
	padding: 10px 30px;
	border: 1px solid grey;
	color: grey;
}

@media(max-width: 480px) {
	.inspireMeCompSection {background-color: #f2f2f2; padding: 10px;}
	#inspireMeComp {display: none;}
	.inspireMeThree {display: none;}
}

.ampTabContainer {
    display: flex;
    flex-wrap: wrap;
}

.tabButton[selected] {
    outline: none;
    border-bottom: 2px solid grey;
}

.tabButton {
		margin: 10px 20px;
    list-style: none;
    flex-grow: 1;
    text-align: center;
    cursor: pointer;
}

.tabContent {
		margin: 10px 20px;
		padding: 20px;
    display: none;
    width: 100%;
    order: 1; /* must be greater than the order of the tab buttons to flex to the next line */
    border: 1px solid #ccc;
}

.tabButton[selected]+.tabContent {
    display: block;
}

/* For example below (not required) */
.itemCustom {
    border: 1px solid #000;
    height: 280px;
    width: 380px;
    margin: 10px;
    text-align: center;
    padding-top: 140px;
}
amp-selector {
  padding: 1rem;
  margin: 1rem;
}
amp-selector [option][selected] {
	outline: none;
}

/*Bootstrap*/
.col-xs-1, .col-sm-1, .col-md-1, .col-lg-1, .col-xs-2, .col-sm-2, .col-md-2, .col-lg-2, .col-xs-3, .col-sm-3, .col-md-3, .col-lg-3, .col-xs-4, .col-sm-4, .col-md-4, .col-lg-4, .col-xs-5, .col-sm-5, .col-md-5, .col-lg-5, .col-xs-6, .col-sm-6, .col-md-6, .col-lg-6, .col-xs-7, .col-sm-7, .col-md-7, .col-lg-7, .col-xs-8, .col-sm-8, .col-md-8, .col-lg-8, .col-xs-9, .col-sm-9, .col-md-9, .col-lg-9, .col-xs-10, .col-sm-10, .col-md-10, .col-lg-10, .col-xs-11, .col-sm-11, .col-md-11, .col-lg-11, .col-xs-12, .col-sm-12, .col-md-12, .col-lg-12 {
  position: relative;
  min-height: 1px;
  padding-right: 15px;
  padding-left: 15px;
}
.col-xs-1, .col-xs-2, .col-xs-3, .col-xs-4, .col-xs-5, .col-xs-6, .col-xs-7, .col-xs-8, .col-xs-9, .col-xs-10, .col-xs-11, .col-xs-12 {
  float: left;
}
.col-xs-12 {
  width: 100%;
}

.btn {
  display: inline-block;
  padding: 6px 12px;
  margin-bottom: 0;
  font-size: 14px;
  font-weight: normal;
  line-height: 1.42857143;
  text-align: center;
  white-space: nowrap;
  vertical-align: middle;
  -ms-touch-action: manipulation;
      touch-action: manipulation;
  cursor: pointer;
  -webkit-user-select: none;
     -moz-user-select: none;
      -ms-user-select: none;
          user-select: none;
  background-image: none;
  border: 1px solid transparent;
  border-radius: 4px;
}
.btn:focus,
.btn:active:focus,
.btn.active:focus,
.btn.focus,
.btn:active.focus,
.btn.active.focus {
  outline: 5px auto -webkit-focus-ring-color;
  outline-offset: -2px;
}
.btn:hover,
.btn:focus,
.btn.focus {
  color: #333;
  text-decoration: none;
}
.btn:active,
.btn.active {
  background-image: none;
  outline: 0;
  -webkit-box-shadow: inset 0 3px 5px rgba(0, 0, 0, .125);
          box-shadow: inset 0 3px 5px rgba(0, 0, 0, .125);
}


/*FOOTER*/
.footer-section-content {
  margin: 20px 40px;
}

.footer-section-content section p:first-child {
  padding-top: 20px;
  padding-bottom: 5px;
}

.footer-top-content {clear: both;}
.footer-top-child {padding: 20px 0 40px; width: 25%; float: left; background: #f9f9f9;}
.footer-top-content p:before {
  content: "";
  display: inline-block;
  height: 47px;
  line-height: 47px;
  margin-left: 40px;
  margin-right: 10px;
  position: relative;
  top: 16px;
  width: 47px;
}

.footer-top-content .footer-top-child:nth-child(1) p:before {
  background: url(https://static.tatacliq.com/_ui/responsive/theme-blue/images/Sprite-combined.png) no-repeat scroll -7px -175px;
}

.footer-top-content .footer-top-child:nth-child(2) p:before {
  background: url(https://static.tatacliq.com/_ui/responsive/theme-blue/images/Sprite-combined.png) no-repeat scroll -73px -175px;
}

.footer-top-content .footer-top-child:nth-child(3) p:before {
  background: url(https://static.tatacliq.com/_ui/responsive/theme-blue/images/Sprite-combined.png) no-repeat scroll -205px -175px;
}

.footer-top-content .footer-top-child:nth-child(4) p:before {
  background: url(https://static.tatacliq.com/_ui/responsive/theme-blue/images/Sprite-combined.png) no-repeat scroll -139px -175px;
}

.footer-main-content {clear: both; display:flex; padding: 20px 40px;}
.footer-child {clear: both; width: 22%; float: left;}
.footer-child-last {width: 34%; float: right; letter-spacing: .6px; font-size: 12px;}

.footer-child h4 {background-color: white; padding: 5px; border: none; font-size: 12px;}
.footer-child h4 i {float: right; padding-left: 10px;}
.footer-child h4 i:last-child {display: none;}
.footer-child ul {margin-left: 5px; font-size: 11px;}
.footer-child ul a {color: #3a3a3a;}

.footer-last-input {
  display: inline;
  border: 1px solid #ddd;
  padding: 10px;
  margin-top: 20px;
  margin-bottom: 40px;
}

.footer-last-btn {padding: 10px; background-color: #444; border: 1px solid #444; color: white;}

.footer-child-last a i {
  color: white;
  background: #444;
  border-radius: 4px;
  padding: 6px;
  width: 16px;
  text-align: center;
  border:1px solid #444;
  margin-top: 15px;
  margin-bottom: 40px;
}

.footer-child-last a i:hover {
  color: #444;
  background: white;
}

.tata-title {color: #a9143c;}

@media (max-width: 480px){
  .footer-section-content {
    margin: 20px 10px; clear: both;
  }
  .footer-top-child {width: 50%; float: left; padding-top: 0; padding-bottom: 20px;}
  .footer-top-content p {font-size: 12px;}
  .footer-top-content p:before {
    margin-left: 10px;
    margin-right: 0;
    transform: scale(0.6);
  }
  .footer-main-content {display: block; padding-left: 20px;}
  .footer-main-content .footer-child:first-child h4 {border-top: 1px solid #ccc;}
  .footer-child h4 {border-bottom: 1px solid #ccc;}
  .footer-child h4 i:last-child {display: block;}
  .footer-child h4 i, .footer-child h4 i:before {margin-top: 2%;}
  .footer-child, .footer-child-last {
    width: 100%; float: left;
  }
}

</style>
<style amp-boilerplate>body{-webkit-animation:-amp-start 8s steps(1,end) 0s 1 normal both;-moz-animation:-amp-start 8s steps(1,end) 0s 1 normal both;-ms-animation:-amp-start 8s steps(1,end) 0s 1 normal both;animation:-amp-start 8s steps(1,end) 0s 1 normal both}@-webkit-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@-moz-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@-ms-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@-o-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}</style><noscript><style amp-boilerplate>body{-webkit-animation:none;-moz-animation:none;-ms-animation:none;animation:none}</style></noscript>
</head>


<body>
<amp-install-serviceworker src="/cliq-service-worker.js" layout="nodisplay"></amp-install-serviceworker>

	<header>
		<button class="header-icon-1 mobile-item" on='tap:sidebar.open'><i class="fa fa-navicon"></i></button>
		<!-- <a href="index.php" class="header-logo"><img src="./images/logo.png" /></a> -->
		<section class="header-navigation-section desktop-item">
			<section class="header-navigation-tab marketplace-tab">
				<a href="#">Marketplace</a>
			</section>
			<section class="header-navigation-tab luxury-tab">
				<a href="#">Luxury</a>
			</section>
			<section class="header-navigation-right">
				<ul>
					<li><a href="#"><i class="fa fa-map-marker"></i> Enter Your Pincode</a></li>
					<li><a href="#"><i class="fa fa-mobile-phone"></i> Download App</a></li>
					<li><a href="#">Track Order</a></li>
					<li><a href="#"><i class="fa fa-bell"></i> Notifications</a></li>
					<li><a href="#"><i class="fa fa-heart"></i> My Wishlists</a></li>
					<li><a href="#"><i class="fa fa-user"></i> Sign In/Sign Up</a></li>
				</ul>
			</section>
		</section>
		<section class="col-xs-12 header-search-section">
			<section class="header-search-left">
				<section class="header-search-left-child logo-container">
					<amp-img class="logo-image" width="70" height="40" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/9906406817822.png"></amp-img>
				</section>
				<section class="header-category header-search-left-child desktop-item shop-department">
					<span class="letter-spacing">SHOP BY</span><br />
					<strong class="letter-spacing">DEPARTMENT<i class="fa fa-chevron-down"></i></strong>
          <section id="shop_by_department" class="desktop-item shop-by-department">
            <section class="shop-by-department-l2">
              <ul>
                <li>
                  Men
                  <section class="shop-by-department-l3">
                    <ul class="shop_by_department_l3_content">
                      <li>
                        <ul>
                          <li>
                            <span class="sbd_l3_headings">Ethnic Wear</span>
                            <ul>
                              <li>Kurtis And Kurtas</li>
                              <li>Suit Sets</li>
                              <li>Fusion Wear</li>
                              <li>Sarees</li>
                              <li>Bottoms</li>
                              <li>Dupattas</li>
                            </ul>
                          </li>
                          <li>
                            <span class="sbd_l3_headings">Ethnic Wear</span>
                            <ul>
                              <li>Kurtis And Kurtas</li>
                              <li>Suit Sets</li>
                              <li>Fusion Wear</li>
                              <li>Sarees</li>
                              <li>Bottoms</li>
                              <li>Dupattas</li>
                            </ul>
                          </li>
                        </ul>
                      </li>
                      <li>
                        <ul>
                          <li>
                            <span class="sbd_l3_headings">Ethnic Wear</span>
                            <ul>
                              <li>Kurtis And Kurtas</li>
                              <li>Suit Sets</li>
                              <li>Fusion Wear</li>
                              <li>Sarees</li>
                              <li>Bottoms</li>
                              <li>Dupattas</li>
                            </ul>
                          </li>
                          <li>
                            <span class="sbd_l3_headings">Ethnic Wear</span>
                            <ul>
                              <li>Kurtis And Kurtas</li>
                              <li>Suit Sets</li>
                              <li>Fusion Wear</li>
                              <li>Sarees</li>
                              <li>Bottoms</li>
                              <li>Dupattas</li>
                            </ul>
                          </li>
                        </ul>
                      </li>
                      <li>
                        <ul>
                          <li>
                            <span class="sbd_l3_headings">Ethnic Wear</span>
                            <ul>
                              <li>Kurtis And Kurtas</li>
                              <li>Suit Sets</li>
                              <li>Fusion Wear</li>
                              <li>Sarees</li>
                              <li>Bottoms</li>
                              <li>Dupattas</li>
                            </ul>
                          </li>
                          <li>
                            <span class="sbd_l3_headings">Ethnic Wear</span>
                            <ul>
                              <li>Kurtis And Kurtas</li>
                              <li>Suit Sets</li>
                              <li>Fusion Wear</li>
                              <li>Sarees</li>
                              <li>Bottoms</li>
                              <li>Dupattas</li>
                            </ul>
                          </li>
                        </ul>
                      </li>
                      <li>
                        <ul>
                          <li>
                            <span class="sbd_l3_headings">Ethnic Wear</span>
                            <ul>
                              <li>Kurtis And Kurtas</li>
                              <li>Suit Sets</li>
                              <li>Fusion Wear</li>
                              <li>Sarees</li>
                              <li>Bottoms</li>
                              <li>Dupattas</li>
                            </ul>
                          </li>
                          <li>
                            <span class="sbd_l3_headings">Ethnic Wear</span>
                            <ul>
                              <li>Kurtis And Kurtas</li>
                              <li>Suit Sets</li>
                              <li>Fusion Wear</li>
                              <li>Sarees</li>
                              <li>Bottoms</li>
                              <li>Dupattas</li>
                            </ul>
                          </li>
                        </ul>
                      </li>
                      <li>
                        <ul>
                          <li>
                            <span class="sbd_l3_headings">Ethnic Wear</span>
                            <ul>
                              <li>Kurtis And Kurtas</li>
                              <li>Suit Sets</li>
                              <li>Fusion Wear</li>
                              <li>Sarees</li>
                              <li>Bottoms</li>
                              <li>Dupattas</li>
                            </ul>
                          </li>
                          <li>
                            <span class="sbd_l3_headings">Ethnic Wear</span>
                            <ul>
                              <li>Kurtis And Kurtas</li>
                              <li>Suit Sets</li>
                              <li>Fusion Wear</li>
                              <li>Sarees</li>
                              <li>Bottoms</li>
                              <li>Dupattas</li>
                            </ul>
                          </li>
                        </ul>
                      </li>
                    </ul>
                  </section>
                </li>
                <li>
                  Women
                  <section class="shop-by-department-l3">
                    Women
                  </section>
                </li>
                <li>Electronics</li>
                <li>Appliances</li>
                <li>Kids</li>
                <li>Fashion Jewellery</li>
              </ul>
            </section>
          </section>
				</section>
				<section class="header-category header-search-left-child desktop-item shop-brand">
					<span class="letter-spacing">SHOP BY</span><br />
					<strong class="letter-spacing">BRAND <i class="fa fa-chevron-down"></i></strong>
          <section id="shop_by_brand" class="desktop-item shop-by-brand">
            <section class="shop-by-brand-l2">
              <ul>
              <amp-list src="/marketplacewebservices/v2/mpl/catalogs/allBrandsAmp" height="420" layout="fixed-height">
                <template type="amp-mustache">
                <li>
                  {{menu_brand_name}}
                  <section class="shop-by-brand-l3">
                    <a class="shop-by-brand-l3-component" href="#">
                      <amp-img width="auto" height="195" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/10987709300766.png"></amp-img>
                      <amp-img width="80" height="28" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/9448599846942.png"></amp-img>
                    </a>
                  </section>
                </li>
                </template>
                </amp-list>
              </ul>
            </section>
          </section>
				</section>
			</section>
      <section class="header-search-right desktop-item">
				<button class="btn my-bag"><i class="fa fa-shopping-bag"></i> &nbsp; My Bag</button>
			</section>
      <section class="header-search-center">
        <p><input class="header-search-input" placeholder="Search in Marketplace" /><button class="header-search-btn"><i class="fa fa-search"></i></button></p>
      </section>
		</section>
		<a href="#" class="header-icon-2 mobile-item"><i class="fa fa-shopping-bag"></i></a>
		<a href="#" class="header-icon-3 mobile-item"><i class="fa fa-search simpleSearchToggle"></i></a>
	</header>


	<div class="header-clear"></div>

	<amp-sidebar id="sidebar" layout="nodisplay" side="left">
		<amp-accordion class="sidebar-menu">
			<section>
				<h4>Departments<i class="fa fa-angle-down"></i></h4>
			  	<ul>
  					<li><a href="#">Women</a></li>
  					<li><a href="#">Men</a></li>
  					<li><a href="#">Electronics</a></li>
            <li><a href="#">Applicances</a></li>
            <li><a href="#">Kids</a></li>
            <li><a href="#">Fashion Jewellery</a></li>
  				</ul>
			</section>
			<section>
				<h4>Brand<i class="fa fa-angle-down"></i></h4>
			  	<ul>
  					<li><a href="#">Appliances</a></li>
            <li><a href="#">Electronics</a></li>
            <li><a href="#">Watches & Accessories</a></li>
            <li><a href="#">Women's Wear</a></li>
            <li><a href="#">Lingerie</a></li>
            <li><a href="#">Men's Wear</a></li>
            <li><a href="#">Footwear</a></li>
            <li><a href="#">Kids</a></li>
            <li><a href="#">A-Z List</a></li>
  				</ul>
			</section>
		</amp-accordion>
    <p class="sidebar-divider-item"><a href="#"><i class="fa fa-user"></i>Sign In/Sign Up</a></p>
    <p class="sidebar-divider-item"><a href="#"><i class="fa fa-heart"></i>My Wishlists</a></p>
    <p class="sidebar-divider-item"><a href="#">Track Order</a></p>
    <p class="sidebar-divider-item"><a href="#"><i class="fa fa-mobile-phone"></i>Download App</a></p>
    <p class="sidebar-divider-item"><a href="#">Enter Your Pincode</a></p>
	</amp-sidebar>
	<!-- Banners -->
	<c:choose>
	<c:when test="${currentDetectedDevice.mobileBrowser}">
		<div class="sliders main-content">
	<amp-list src="/pwamp/getHomePageBanners?version=Online" height="420" layout="fixed-height">
	<template type="amp-mustache">
	<amp-carousel class="slider" width="400" height="120" layout="responsive" type="slides" controls autoplay loop delay="3000">
	  {{#desktopBanners}}
	    <div>
			<amp-img class="responsive-img" src="{{url}}" layout="fill"></amp-img>
		</div>
		{{/desktopBanners}}
	</amp-carousel>
	</template>
	</amp-list>
	</div>
	</c:when>
	<c:otherwise>
		<div class="sliders main-content">
	<amp-list src="/pwamp/getHomePageBanners?version=Online" height="420" layout="fixed-height">
	<template type="amp-mustache">
	<amp-carousel class="slider" width="400" height="120" layout="responsive" type="slides" controls autoplay loop delay="3000">
	  {{#desktopBanners}}
	    <div>
			<amp-img class="responsive-img" src="{{url}}" layout="fill"></amp-img>
		</div>
		{{/desktopBanners}}
	</amp-carousel>
	</template>
	</amp-list>
	</div>
	</c:otherwise>
	</c:choose>
	


	<!-- Top Deals -->
	<div id="topDealsComp">
		<div class="topDealsTopSection">
			<h2 class="homeViewHeading">Top Deals <small class="homeViewAllBtn"><a href="#">View All</a></small></h2>
		</div>
		<div>
			<div id="topDealsCompCarousel">
			<amp-list src="/pwamp/getBestPicks?version=Online" height="320" layout="fixed-height">
			<template type="amp-mustache">
				<amp-carousel height="320" layout="fixed-height" type="carousel">
				{{#subItems}}
					<div class="topDealsItem">
						<div class="topDealsItemImg">
							<a href="{{url}}"><amp-img class="responsive-img" layout="fill" src="{{imageUrl}}" alt="Brand Image"></amp-img></a>
						</div>
					</div>
				{{/subItems}}
				</amp-carousel>
				</template>
				</amp-list>
			</div>
		</div>
    <!-- <div class="brandStudioBottom">
      <amp-list src="/json/bestpicks.json" height="40" layout="fixed-height">
        <template type="amp-mustache">
    			<a href="{{buttonLink}}"><button class="brandStudioViewAllBtn">{{buttonText}}</button></a>
        </template>
      </amp-list>
		</div> -->
	</div>

	<!-- Brands You Love -->
	<div id="brandsYouLoveMobileComp" class="">
		<div class="brandStudioTop">
			<h2 class="homeViewHeading">Brand Studio <small class="homeViewAllBtn"><a href="#">View All</a></small></h2>
		</div>
		<div>
			<div id="brandsYouLoveMobileCompCarousel">
			<amp-list src="/pwamp/getBrandsYouLove?version=Online" height="320" layout="fixed-height">
				<template type="amp-mustache">
			    <amp-carousel height="320" layout="fixed-height" type="carousel">
			    	{{#subComponents}}
					<div class="brandsCarouselItem">
						<div class="brandStudioImg">
							<a href="{{bannerUrl}}"><amp-img class="responsive-img" layout="fill" src="{{bannerImageUrl}}" alt="{{brandLogoAltText}}"></amp-img></a>
						</div>
						<div class="brandStudioDesc">
							<p class="brandStudioDescHeading">{{bannerText}}</p>
							<p class="brandStudioDescInfo">{{text}}</p>
							<p class="brandStudioVisitStore"><a href="{{bannerUrl}}">Visit Store</a></p>
						</div>
					</div>
					{{/subComponents}}
				  </amp-carousel>
				  </template>
				  </amp-list>
			</div>
		</div>
		<!-- <div class="brandStudioBottom">
			<button class="brandStudioViewAllBtn">View All Brands</button>
		</div> -->
	</div>

  <br />&nbsp;
	<!-- What To Buy Now -->
	<div id="whatToBuyComp">
		<div class="whatToBuyTopSection">
			<h2 class="homeViewHeading">What To Buy Now <small class="homeViewAllBtn"><a href="#">View All</a></small></h2>
		</div>
		<div>
			<div id="whatToBuyCompCarousel">

				<amp-list src="/pwamp/getProductsYouCare?version=Online" height="400" layout="fixed-height">
				<template type="amp-mustache">
				<amp-carousel height="400" layout="fixed-height" type="carousel">
				{{#categories}}
					<div class="whatToBuyItem">
						<div class="whatToBuyItemImg">
							<a href="{{imageURL}}"><amp-img class="responsive-img" layout="fill" src="{{mediaURL}}" alt="Brand Image"></amp-img></a>
						</div>
					</div>
				{{/categories}}
				</amp-carousel>
					</template>
				</amp-list>

			</div>
		</div>
	</div>

  <br />

  <div class="compContainer">
    <!-- New In -->
  	<div id="newInComp">
  		<div class="newInTopSection">
  			<h2 class="homeViewHeading">New In <small class="homeViewAllBtn"><a href="#">View All</a></small></h2>
  		</div>
  		<div>
  			<div id="newInCompCarousel">
  			<amp-list src="/pwamp/getNewAndExclusive?version=Online" height="320" layout="fixed-height">
  				<template type="amp-mustache">
  				<amp-carousel height="320" layout="fixed-height" type="carousel">
  				{{#newAndExclusiveProducts}}
  					<a href="{{productUrl}}"><div class="newInItem">
  						<div class="newInItemImg">
  							<amp-img class="responsive-img" layout="fill" src="{{productImageUrl}}" alt="Brand Image"></amp-img>
  						</div>
  						<div class="newInDesc">
  							<p class="newInDescName">{{productTitle}}</p>
  							{{#productPrice}}
  							<p class="newInDescPrice">{{dispPrice}}<span class="price-right">{{strikePrice}}</span></p>
  							{{/productPrice}}
  						</div>
  					</div></a>
  					{{/newAndExclusiveProducts}}
  				</amp-carousel>
  				</template>
  				</amp-list>
  			</div>
  		</div>
  		<div class="brandStudioBottom">
  			<button class="brandStudioViewAllBtn">View All</button>
  		</div>
  	</div>

  	<!-- Stay Qued -->
  	<div id="stayQuedComp">
  		<div class="stayQuedTopSection">
  			<h2 class="homeViewHeading">Stay Qued</h2>
  		</div>
  		<amp-list src="/pwamp/getStayQuedHomepage?version=Online" height="320" layout="fixed-height">
  		<template type="amp-mustache">
  		<div class="stayQuedCenter">
  			<div class="stayOne">
  				<div>
  					<p>&nbsp;</p>
  					<p>&nbsp;</p>
  					<p class="h2 stayQuedHeading">{{promoText1}}</p>
  					<p>{{promoText2}}</p>
  					<div class="stayQuedBottom">
  						<a href="{{bannerUrlLink}}"><button class="stayQuedViewAllBtn">Read The Story</button></a>
  					</div>
  				</div>
  			</div>
  			<div class="stayTwo">
  				<div>
  					<amp-img class="responsive-img" width="auto" height="300" layout="flex-item" src="{{bannerImage}}" alt="{{bannerAltText}}"></amp-img>
  				</div>
  			</div>
  		</div>
  		</template>
  		</amp-list>
  	</div>
  </div>

  <br />
	<!-- Hot Now -->
	<!--  <div id="hotNowComp">
		<div class="hotNowTopSection">
			<h2 class="homeViewHeading">Hot Now</h2>
		</div>
		<div>
			<div id="hotNowCompCarousel">
			<amp-list src="/pwamp/getBestPicks?version=Online" height="320" layout="fixed-height">
				<template type="amp-mustache">
				<amp-list src="/pwamp/getProductsYouCare?version=Online" height="320" layout="fixed-height">
				<template type="amp-mustache">
				<amp-carousel height="320" layout="fixed-height" type="carousel">
				{{#subItems}}
					<div class="hotNowItem">
						<div class="hotNowItemImg">
							<amp-img class="responsive-img" layout="fill" src="{{imageUrl}}" alt="Brand Image"></amp-img>
						</div>
						<div class="hotNowDesc">
							<p class="hotNowTitle">Vivo</p>
							<p class="hotNowDescName">{{text}}</p>

						</div>
					</div>
					{{/subItems}}
				</amp-carousel>
				</template>
				</amp-list>
				</template>
				</amp-list>

			</div>
		</div>
		<div class="brandStudioBottom">
			<button class="brandStudioViewAllBtn">Shop the Hot List</button>
		</div>
	</div>-->

	<!-- Inspire Me -->
	<div id="inspireMeComp">
		<div class="newInTopSection">
			<h2 class="homeViewHeading">Inspire Me</h2>
		</div>
		<div class="inspireMeCompSection">
			<amp-list src="/pwamp/getCollectionShowcase?version=Online" height="320" layout="fixed-height">
			<template type="amp-mustache">
			<amp-selector role="tablist"
				layout="container"
				class="ampTabContainer">
				{{#subComponents}}
				<div role="tab"
					class="tabButton"
					selected
					option="a">{{headerText}}
        </div>
					{{#details}}
				<div role="tabpanel"
					class="tabContent">
					<div class="inspireMeOne">
						<a href="{{bannerUrl}}"><amp-img width="280" height="360" layout="flex-item" src="{{bannerImageUrl}}" alt="Brand Image"></amp-img></a>
					</div>
					<div class="inspireMeTwo">
						<div class="inspireMeCenter">
							<div>
								<p class="h4 stayQuedHeading"><strong>{{firstProductTitle}}</strong></p>
								<p>&nbsp;</p>
								<p>{{text}}</p>
								<div class="inspireMeBottom">
									<button class="stayQuedViewAllBtn">Read The Story</button>
								</div>
								<div class="inspireMeBottom">
									<button class="inspireMeReadMoreBtn">Read More</button>
								</div>
							</div>
						</div>
					</div>
					<div class="inspireMeThree">
						<a href="{{firstProductUrl}}"><amp-img width="300" height="360" layout="flex-item" src="{{firstProductImageUrl}}" alt="Brand Image"></amp-img></a>
					</div>
				</div>
				{{/details}}
				{{/subComponents}}
			</amp-selector>
			</template>
		</amp-list>
		</div>
	</div>
<footer:footer/>


	<!-- <DTM Amp-Analytics starts> -->
<amp-analytics type="adobeanalytics_nativeConfig">
	<script type="application/json">
	{
		"requests": {
			"base": "https://${host}",           
			"iframeMessage": "${base}/stats.html?pageURL=${ampdocUrl}&ref=${documentReferrer}"  
		},
		"vars": {
			"host": "${host}"           
		},
		"extraUrlParams": {
			"pageName": "HomePage",        
			"page_type": "home",
            "user_login_type" : "${userLoginType}",
            "site_currency "  : "INR"
		}
	}
	</script>
</amp-analytics> 

</body>
</html>
