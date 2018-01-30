<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<head>
<title>${pageTitle}</title>
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
<script async custom-element="amp-bind" src="https://cdn.ampproject.org/v0/amp-bind-0.1.js"></script>
<script async custom-element="amp-install-serviceworker" src="https://cdn.ampproject.org/v0/amp-install-serviceworker-0.1.js"></script>
<script async custom-element="amp-lightbox" src="https://cdn.ampproject.org/v0/amp-lightbox-0.1.js"></script>
<!--AMP HTML files require a canonical link pointing to the regular HTML. If no HTML version exists, it should point to itself.-->
<link rel="canonical" href="https://www.tatacliq.com">
<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"> -->
<!-- <link href="https://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet"> -->
<link rel="manifest" href="/manifest.json">
<meta name="theme-color" content="#A9133d">
<meta name="viewport" content="width=device-width,minimum-scale=1,initial-scale=1,maximum-scale=1,user-scalable=no"><meta name="apple-mobile-web-app-capable" content="yes"/><meta name="apple-mobile-web-app-status-bar-style" content="black">

<!-- Latest compiled and minified CSS -->
<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" type="text/css" > -->
<!-- jQuery library -->
<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script> -->
<!-- Latest compiled JavaScript -->
<!-- <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> -->
<c:choose>
			<c:when test="${isCategoryPage}">
			
			<c:set var="titleSocialTags" value="${not empty metaPageTitle ?metaPageTitle:not empty pageTitle ? pageTitle : 'Tata'}"/>
			 
			</c:when>
			<c:otherwise>
			<c:set var="titleSocialTags" value="${not empty pageTitle ? pageTitle : not empty cmsPage.title ? cmsPage.title : 'Tata'}"/>
				 
			</c:otherwise>
</c:choose>	

<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('twitter.handle')" var="twitterHandle"/>
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('site.name')" var="siteName"/>
<meta name="apple-itunes-app" content="app-id=1101619385">
<meta name="google-play-app" content="app-id=com.tul.tatacliq">
<!-- <meta http-equiv="Content-Type" content="text/html"/> -->
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<meta name="keywords" content="${keywords}">
<meta name="description" content="${description}">
<meta name="google-site-verification" content="aArvRu0izzcT9pd1HQ5lSaikeYQ-2Uy1NcCNLuIJkmU" />
<meta itemprop="name" content="${titleSocialTags}">
<%-- <meta itemprop="description" content="${metaDescription}"> --%>
<meta name="twitter:card" content="summary_large_image">	
<meta name="twitter:title" content="${titleSocialTags}">
<meta name="twitter:site" content="${twitterHandle}">	
<%-- <meta name="twitter:description" content="${metaDescription}"> --%>
<meta property="og:title" content="${titleSocialTags}" />
<%-- <meta property="og:url" content="${canonical}" /> --%>

<%-- <meta property="og:description" content="${metaDescription}" /> --%>
<meta property="og:site_name" content="${siteName}" />
<meta property="fb:app_id" content="484004418446735"/>
<meta property="al:ios:app_store_id" content="1101619385" />
<%-- <meta property="al:ios:url" content="${canonical}" /> --%>
<meta property="al:ios:app_name" content="Tata Cliq" />
<meta property="al:android:package" content="com.tul.tatacliq" />
<%-- <meta property="al:android:url" content="${canonical}" /> --%>
<meta property="al:android:app_name" content="Tata Cliq" />
	<c:choose>
	<c:when test="${fn:contains(reqURI,'/p-')}">	
	<c:forEach items="${galleryImages}" var="container" varStatus="varStatus" end="0">
	<meta itemprop="image" content="${container.thumbnail.url}" />
	</c:forEach>	
	</c:when>
	<c:otherwise>
	<meta itemprop="image" content="${protocolString[0]}://${mediaHost}${seoMediaURL}" />
	</c:otherwise>
	</c:choose>
<style amp-custom>
@font-face {
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 400;
  src: local('Montserrat Regular'), local('Montserrat-Regular'), url(https://fonts.gstatic.com/s/montserrat/v12/zhcz-_WihjSQC0oHJ9TCYL3hpw3pgy2gAi-Ip7WPMi0.woff) format('woff');
}
body{font-family:'Montserrat', sans-serif; font-size:14px; background-color:#FFFFFF;}
/*ICONS CSS*/
@font-face {
    font-family: 'FontAwesome';
    src: url('https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/fonts/fontawesome-webfont.eot?v=4.7.0');
    src: url('https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/fonts/fontawesome-webfont.eot?#iefix&v=4.7.0') format('embedded-opentype'), url('https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/fonts/fontawesome-webfont.woff2?v=4.7.0') format('woff2'), url('https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/fonts/fontawesome-webfont.woff?v=4.7.0') format('woff'), url('https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/fonts/fontawesome-webfont.ttf?v=4.7.0') format('truetype'), url('https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/fonts/fontawesome-webfont.svg?v=4.7.0#fontawesomeregular') format('svg');
    font-weight: normal;
    font-style: normal
}

.fa {
    display: inline-block;
    font: normal normal normal 14px/1 FontAwesome;
    font-size: inherit;
    text-rendering: auto;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale
}

.fa-navicon:before, .fa-reorder:before, .fa-bars:before {content: "\f0c9";}
.fa-search:before {content: "\f002";}
.fa-user:before {content: "\f007";}
.fa-heart:before {content: "\f08a";}
.fa-mobile-phone:before, .fa-mobile:before {content: "\f10b";}
.fa-angle-right:before {content: "\f105";}
.fa-angle-down:before {content: "\f107";}
/*social icons*/
.fa-google-plus:before {content: "\f0d5";}
.fa-facebook-f:before, .fa-facebook:before {content: "\f09a";}
.fa-twitter:before {content: "\f099";}
.fa-instagram:before {content: "\f16d";}
.fa-youtube:before {content: "\f167";}
.fa-android:before {content: "\f17b";}
.fa-apple:before {content: "\f179";}
/*ICONS CSS*/

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

.top-header {display: -webkit-box; border-bottom: 2px solid #000;}

.top-header > a {
  width: 50%;
  display: inline;
  padding: 4px 0;
  float: left;
  text-align: center;
  text-transform: uppercase;
  letter-spacing: .6px;
  font-size: 11px;
}

.top-header > a:first-child {color: #a9143c;}
.top-header > a:last-child {color: #fff; background-color: #000;}

.amp-analytics-class {position:absolute;top:75%;}

header{
	position:sticky;
	/*height:60px;*/
	background-color:#fff;
  display: table;
	width:100%;
	z-index:99;
  top: 0;
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
  border: none;
}

.header-icon-3 i {
  background: url('https://static.tatacliq.com/_ui/responsive/theme-blue/images/search_image.png') no-repeat;
  width: 30px;
  line-height: 25px;
  display: inline-block;
}

.header-icon-2 a {
  background: url('https://static.tatacliq.com/_ui/responsive/theme-blue/images/Sprite-combined.png') no-repeat scroll -383px -145px;
  width: 20px;
  line-height: 30px;
  display: inline-block;
  margin-left: 10px;
}

.sidebar-divider-item span, .sidebar-myaccount {
	width: 25px;
    display: inline-block;
    vertical-align: middle;
    height: 26px;
    filter: invert(100%);
    margin-right: 5px;
}

.sidebar-myaccount {margin-left: -5px;}

.sidebar-profile span, .sidebar-myaccount {
	background: url('https://static.tatacliq.com/_ui/responsive/theme-blue/images/Sprite-combined.png') no-repeat scroll -352px -176px;
 }
 
.sidebar-download-app span {
	background: url('https://static.tatacliq.com/_ui/responsive/theme-blue/images/Sprite-combined.png') no-repeat scroll -302px -175px;
}

.header-icon-2 a span {
  margin-left: -20px;
  width: 22px;
  display: inline-block;
  background-color: #000;
  line-height: 16px;
  font-size: 11px;
  color: #fff;
  text-align: center;
  border: 2px solid #fff;
  border-radius: 10px;
}

.header-icon-1 {z-index: 10;}

.header-icon-1 .fa-navicon {font-size: 20px;}

.header-icon-2{
	right:0px;
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
	height:60px;
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
	width:100%;
	background-color:#FFFFFF;
}

.sidebar-menu {margin-bottom:0px;}

.sidebar-menu i.fa-angle-right {font-weight: 600;}

.l2-options .fa-angle-right:before, .l3-options .fa-angle-right:before {color: #3a3a3a;}

.l1-accordian section[expanded] .l1-options, .l1-accordian section[expanded] .l1-options i:before {background-color: #a9133d; color: #fff;}

.l2-accordian {background-color: #d6d6d6;}

.l3-accordian { background-color: #c5c4c4;}

.sidebar-menu amp-accordion > section > ul > li {padding-left: 35px; line-height: 40px; background-color: #b9b8b8; border-bottom: 1px solid #e6e6e6;}

.l1-options {padding-left: 15px;}
.l2-options {padding-left: 20px;}
.l3-options {padding-left: 30px;}

.sidebar-menu i{
	font-size:14px;
	width:35px;
	height:50px;
	line-height:50px;
	text-align:center;
	border-radius:35px;
}

.sidebar-menu ul li a .fa-circle{font-size:4px; margin-left:1px;}
.sidebar-menu .fa-circle, .sidebar-menu .fa-angle-down, .fa-angle-right{
	/*width:35px;
	height:35px;*/
	position:absolute;
	right:0px;
	/*font-size:4px;*/
	color:#acacac;
}

.sidebar-menu .fa-angle-down {font-size:14px;}

.sidebar-menu section[expanded] .fa-angle-down{
	transform:rotate(180deg);
}

.sidebar-menu section[expanded] .l1-my-account {color: #fff;}

.user-information > section > div > ul {margin: 0;}

.user-information section[expanded] .sidebar-myaccount {filter: none;}

.user-information > section > div > ul > li {
    padding-left: 20px;
    line-height: 50px;
    border-bottom: 1px solid #e6e6e6;
    list-style-type: none;
}

.user-information > section > div > ul > li a {line-height: 50px;}

.sidebar-menu h4{
	background:none;
	border:none;
  color: #3a3a3a;
  line-height: 50px;
  font-size: 14px;
  border-bottom: 1px solid #e6e6e6;
}

.sidebar-menu h4 a {color: #3a3a3a;}

.sidebar-menu ul li a{
	color:#3a3a3a;
	font-size:14px;
	line-height:30px;
  font-weight: 600;
}

.sidebar-menu i:first-child{
	font-size:14px;
}

.sidebar-menu li a .fa-circle{margin-top:-5px;}

.sidebar-divider-item{
    font-family: 'Montserrat';
    font-size: 14px;
    padding-left: 15px;
    color: #3a3a3a;
    border-bottom: solid 1px rgba(0,0,0,0.1);
	line-height:50px;
}
.sidebar-divider-item a {color: #3a3a3a;}
.sidebar-divider-item a i {width: 20px;}

.sidebar-menu .sidebar-item{
	color:#1f1f1f;
	font-size:12px;
	line-height:60px;
}

section[expanded] .fa-plus{	transform:rotate(45deg);}
.l1-accordian > section[expanded] .l1-options .fa-angle-right, .l2-accordian > section[expanded] .l2-options .fa-angle-right, .l3-accordian > section[expanded] .l3-options .fa-angle-right {	transform:rotate(90deg);}
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

.header-navigation-right ul li {list-style-type: none; display: inline-block; padding: 10px; font-size: 12px;}
.header-navigation-right ul {line-height: normal; margin: 0;}
.header-navigation-right ul li a {letter-spacing: .6px; color: #fff;}
.logo-image {
	left: 40px;
  top: 5px;
}

.header-icon-3{
	right:60px;
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
	width: 50%;
	float: right;
}

/*Auto Collapse Menu CSS*/
.left-accordion-menu {margin: 0;}
.left-accordion-menu li {list-style-type: none;}
.left-accordion-menu li label {line-height: 50px; display: inline-block; width: 100%; font-size: 14px;border-bottom: solid 1px rgba(0,0,0,0.1)}
.left-accordion-menu > li > label > span {padding-left: 15px;}
.l1-menu-section > li > label {border-bottom: 1px solid #e6e6e6; background-color: #d6d6d6;}
.l1-menu-section > li > label > span {padding-left: 25px;}
.l2-menu-section > li > label, .l2-menu-section > li > a {line-height: 40px; border-bottom: 1px solid #e6e6e6; background-color: #c5c4c4;}
.l2-menu-section > li > label > span, .l2-menu-section > li > a {padding-left: 35px; color: #000;}
.l3-menu-section > li {border-bottom: 1px solid #e6e6e6; background-color: #b9b8b8;}
.l3-menu-section > li > a {padding-left: 45px; color: #000; line-height: 30px;}
.left-accordion-menu li label i {float: right; margin-right: 15px;margin-top: 15px;}
.l2-menu-section li label i {margin-top: 10px;}
.left-accordion-menu input[type=radio], .left-accordion-menu input[type=checkbox] {display: none;}
.left-accordion-menu input[type=radio]:checked + label + ul, .left-accordion-menu input[type=checkbox]:checked + label + ul,
.left-accordion-menu input[type=radio]:checked + label .fa-angle-right:before, .left-accordion-menu input[type=checkbox]:checked + label .fa-angle-right:before {content: "\f107";}
.left-accordion-menu input[type=radio]:checked + label:nth-of-type(n) + ul, .left-accordion-menu input[type=checkbox]:checked + label:nth-of-type(n) + ul {display: block;}
.left-accordion-menu > li > input[type=radio]:checked + label, .left-accordion-menu > li > input[type=checkbox]:checked + label {background-color: #a9133d; color: #fff;}
.left-accordion-menu ul {display: none; margin: 0;}
.left-accordion-menu .a2zTabContent ul {display: block;}
/*Auto Collapse Menu CSS*/
.department-menu {
	width: 150px;
	float: left;
	border: 1px solid #ddd;
	text-align: left;
	height: 36px;
	font-size: 12px;
}

.department-menu > span {padding: 10px; width: 100%; line-height: 36px;}

.department-menu i  {float: right; padding-right: 10px;  line-height: 36px;}

.department-menu-items {display: none; margin: 0; border: 1px solid #ddd; background-color: #fff;}

.department-menu-items li {list-style-type: none; line-height: 38px; cursor: pointer; padding-left: 10px;}

.department-menu-items li:hover {color: #fff; background-color: #a9143c;}

.department-menu:hover .department-menu-items {display: block;}

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
	border-left: none;
  padding: 10px;
  width: 60%;
	height: 16px;
	float: left;
}

.header-search-btn {background-color: #f8f8f8; border: 1px solid #ddd; border-left: none; cursor: pointer; height: 38px; float: left; width: 60px;}

.header-search-btn a i {color: #000; font-size: 20px;}

.shop-by-department, .shop-by-brand {display: none; height: 500px; width: 100%; position: absolute; border-top: 1px solid #ddd; background: #fff; z-index: 1; left: 0; top: 60px;}

.shop-by-department-l2, .shop-by-brand-l2 {width: 17%; float: left; background: #f6f6f6; height: inherit;}

.shop-by-department-l2 ul, .shop-by-brand-l2 ul {margin: 0;}

.shop-by-department-l2 ul li, .shop-by-brand-l2 ul li {list-style-type: none; font-size: 14px; padding: 10px 20px;}

.shop-by-department-l2 ul li:hover, .shop-by-brand-l2 ul li:hover {background-color: #444; color: white;}

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

/*Autocomplete*/
.suggest {width: 31%; margin-left: 152px; position: absolute; top: 48px;z-index: 1;}

.suggest amp-selector {margin: 0; padding: 0; text-align: left;}
.suggest amp-list {background-color: #fff;}

.autosuggest-container {
  position: relative;
}

.autosuggest-box {
  position: absolute;
  width: 100%;
  /* make the overlay opaque */
  background-color: #fafafa;
}

.autosuggest-box amp-selector {padding: 10px 15px;}
.autosuggest-box > div {overflow: auto;}

.select-option.no-outline[selected] {
  outline: initial;
}

.select-option a {color: #000;}

.hidden {
  display: none;
}

.display-visible {
  display: block;
}

.pincode-check {
  position: fixed; z-index: 2147483648; top: 200px; right: 30px; left: 30px; background: white;
}

.pincode-check > section {padding: 0 10px 10px;}

.pincode-check h3 {text-align: center;}

.pincode-check h3 span {float: right; font-size: 28px; margin-top: -10px}

.pincode_text {border: 1px solid #dfd1d5; line-height: 1; padding: 0; height: 40px; outline: 0; padding-left: 10px; cursor: text; text-overflow: ellipsis;}

.pincode_submit {border: none; font-size: 12px; background: #a9143c; color: #fff; float: right; line-height: 0; padding: 0 15px; height: 39px;}

.background-layer {position: absolute; height: 100%; width: 100%; background: rgba(0,0,0,0.65); z-index: 2000; top: 0;}

.sidebar-zindex {z-index: 1000;}

.close-menubar {left: 80vw; position: fixed; top: 0; padding: 0px 10px; z-index: 2147483647; font-size: 34px; background-color: #fff; border: 1px solid #ddd; width: 50px; line-height: 50px;}

.search-submit {
  margin: 10px 0;
  padding: .5em 1em;
  color: #444;
  border: 1px solid #999;
  background-color: #fafafa;
  text-decoration: none;
  border-radius: 2px;
}

.autosuggest-box {
  box-shadow: 0px 2px 6px rgba(0,0,0,.3);
}

.select-option {
  box-sizing: border-box;
  height: 30px;
  line-height: 30px;
  padding-left: 10px;
}

.select-option:hover {
  background-color: #ddd;
}

.select-option.empty {
  text-align: center;
}
/*Autocomplete*/

.signup-lightbox {
	float: right;
	width: 400px;
	border: 1px solid #ddd;
	margin-top: 36px;
	background-color: #fff;
}

@media(max-width: 480px) {
	.desktop-item {
		display: none;
	}
	.mobile-item {
		display: block;
	}
	.header-search-center {
		width: 100%;
		padding-top: 0;
		/*ToggleSearch*/
	}

  .suggest {
    width: auto;
    margin: 0;
    top: 98px;
    right: 44px;
    left: 16px;
  }

  header {border-bottom: 1px solid #ddd;}

  .header-search-input {
    width: calc(100% - 110px);
    border-left: 1px solid #ddd;
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
	#topDealsCompCarousel amp-carousel, #topDealsCompCarousel amp-list, #hotNowCompCarousel amp-carousel, #hotNowCompCarousel amp-list {
		/*give important*/
		max-height: 220px;
	}

  #whatToBuyCompCarousel amp-carousel, #whatToBuyCompCarousel amp-list {
		/*give important*/
		max-height: 180px;
	}
}

.shop-promos {
	height: 100px;
}

.shop-promos > ul {margin: 0; display: flex;}

.shop-promos > ul> li {width: 20%; list-style-type: none; border: 1px solid #ddd; border-left: none;
	text-align: center; padding: 10px 0; font-size: 12px; display: table-cell; float: left; position: relative;}

.shop-promos > ul> li > a {color: #444;}
.shop-promos > ul> li > a:hover {color: #a9143c;}

/*Top Deals*/
.topDealsTopSection {padding: 0px 15px;}

.topDealsItem {
	width: 232px; height: 320px; margin: 0px 10px; box-shadow: 0 2px 3px 0 rgba(0, 0, 0, 0.1);
}

.topDealsItemImg {height: inherit;}
.topDealsItemImg a {height: inherit;}
#topDealsComp {display: block; padding: 10px 0px 20px; padding: 20px 60px;}

@media(max-width: 480px) {
	#topDealsComp {padding: 5px 0px 15px;}
	.topDealsItem {width: 140px; height: 200px;}
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
.brandStudioDescHeading {font-size: 13px; font-weight: bold; text-overflow: ellipsis; overflow: hidden; white-space: nowrap;}
.brandStudioDescInfo {font-size: 12px; color: #666666; white-space: pre-line; height: 75px;}
.brandStudioVisitStore, .brandStudioVisitStore:hover {font-size: 13px; color: #0066c0;}

.brandStudioBottom {display: block; text-align: center; padding: 30px;}
.brandStudioBottom a {display: -webkit-inline-box;}
.brandStudioViewAllBtn {background-color: grey; padding: 10px 30px; border: none; color: white; cursor: pointer;}

@media(max-width: 480px) {
	.brandStudioBottom {display: none;}
	.homeViewAllBtn a, .homeViewAllBtn a:hover {display: block; font-size: 14px;}
	.homeViewHeading {text-align: left; font-size: 18px; padding-bottom: 5px;}
	.brandStudioTop {padding: 0px 15px;}
	.brandsYouLove {margin: 20px;}
	#brandsYouLove {display: none;}
	#brandsYouLoveMobileComp {display: block; background-color: #f2f2f2; padding: 5px 0px 15px;}
	.brandStudioImg {height: 180px;}
	.brandStudioImg img {width: 100%; height: 100%; border-top-left-radius: 4px; border-top-right-radius: 4px;}
	.brandStudioDescHeading, .brandStudioDescInfo, .brandStudioVisitStore {padding: 14px 8px 0px; text-align: left;}
	.brandStudioDescHeading {font-size: 13px; min-height: 15px;}
	.brandStudioDescInfo {font-size: 12px; color: #666666;white-space: pre-line; max-height: 60px; overflow-y: hidden;}
	.brandStudioVisitStore, .brandStudioVisitStore:hover {font-size: 13px; color: #0066c0;}

	.amp-scrollable-carousel-slide:first-child {
		margin-left: 10px;
	}
	.amp-scrollable-carousel-slide:last-child {
		margin-right: 10px;
	}
}

/*What To Buy Now*/
.whatToBuyTopSection {padding: 0px 15px;}

.whatToBuyItem {
	width: 300px; height: 400px; margin: 0px; padding: 0 5px;
}

.whatToBuyItem:first-child {padding-left: 0;}

.whatToBuyItemImg, .whatToBuyItemImg a {height: inherit;}
#whatToBuyComp {display: block; padding: 10px 0px 20px; padding: 20px 60px;}

@media(max-width: 480px) {
	#whatToBuyComp {padding: 5px 0px;}
	.whatToBuyItem {width: 140px; height: 180px;}
}

/*New In*/
.newInTopSection {padding: 0px 15px;}

.compContainer {width: 100%;}
#newInComp {display: block; padding: 10px 0px 20px; padding: 20px 60px; width: 43%; float: left;}

.newInDesc {padding: 0 5px;}
.newInDescName {padding: 5px 0; text-overflow: ellipsis; overflow: hidden;}
.newInDescPrice {text-decoration: line-through;}
.price-right {text-decoration: none; float: right;}

@media(max-width: 480px) {
	#newInComp {padding: 5px 0px 15px; width:100%;}
	.newInItem {width: 140px; height: 200px; margin: 0; padding: 0 5px;}
}

/*Stay Qued && Inspire Me Mobile*/
.stayOne {width: 50%; float: left; padding: 10px;}
.stayTwo {width: 40%; float: left; padding: 10px;}

.stayQuedTopSection, .inspireMeMobileTopSection {padding: 0px 15px;}

.stayQuedItemImg {height: inherit;}
#stayQuedComp, #inspireMeMobileComp {display: block; padding: 10px 20px 10px 0; width: 45%; float: left;}
.stayQuedCenter {background-color: #f2f2f2; padding: 0 0 0 10px;}

.stayQuedBottom {display: block; padding: 10px; padding-left: 0;}
.stayQuedBottom a {display: -webkit-inline-box;}
.stayQuedViewAllBtn {background-color: #444; padding: 10px 30px; border: none; color: white; cursor: pointer;}

@media(max-width: 480px) {
	#stayQuedComp, #inspireMeMobileComp {background-color: #f2f2f2; padding: 5px 0px 15px; width: 100%;}
	.stayQuedHeading {font-size: 13px; line-height: 1.25; text-transform: uppercase; letter-spacing: .6px; color: #000; margin-bottom: 8px;}
	.stayQuedViewAllBtn {padding: 5px 15px;}
	.stayOne, .stayTwo {padding: 0px; width: 50%; float: left;}
  .stayOne {padding-left: 15px;}
	.stayQuedCenter {padding: 0 5%; display: inline-flex;}
  .inspireMeCenter {display: flex; padding: 0 5%; }
  #inspireMeMobileComp .amp-carousel-button { display: none }
}

/*Hot Now*/
.hotNowTopSection {padding: 0px 15px;}

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

.a2z-section {
  color: #3a3a3a;
  background-color: #c5c4c4;
  font-weight: 600;
}

.a2zTabContainer {
    display: flex;
    flex-wrap: wrap;
    margin: 0;
}

.a2zTabButton[selected] {
    outline: none;
    color: #a9133d;
    border-bottom: 2px solid #a9133d;
}

.a2zTabButton {
    list-style: none;
    flex-grow: 1;
    text-align: center;
    cursor: pointer;
}

.a2zTabContent {
    padding-left: 10px;
    padding-top: 20px;
    display: none;
    width: -webkit-fill-available;
    order: 1; /* must be greater than the order of the tab buttons to flex to the next line */
    border-top: 2px solid #000;
    max-height: 300px;
    overflow: auto;
}

.a2zTabContent a {color: #000; padding: 5px;}

.a2zTabContent ul li {list-style-type: none; padding-left: 15px;}

.a2zTabContent ul h3 {font-size: 24px; font-weight: 600; margin: 0;}

.a2zTabButton[selected]+.a2zTabContent {
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

.footer-top-content #footerLink {
    border-top: 0;
    padding: 30px 26px;
    margin-left: 0;
    margin-right: 0;
}

footer #footerLink div.column ul li {
	display: inline-block;
	padding: 7px 0;
}

footer #footerLink div.column ul li:first-child, footer #footerLink div.column ul li:first-child a {color: #000;}

footer #footerLink div.column ul li a {color: #878787;}

footer #footerLink div.column ul li:first-child {padding-top: 10px; text-transform: capitalize;}

footer #footerLink div.column ul li {
    display: inline-block;
    color: #878787;
    padding: 7px 0;
}

.footer-top-content {clear: both;}
.footer-top-child {padding: 0 24px 20px; float: left; background: #f9f9f9; margin: 0;line-height: normal;}
.footer-top-content p:before {
  content: "";
  display: inline-block;
  height: 47px;
  line-height: 47px;
  margin-left: -55px;
  margin-right: 10px;
  position: relative;
  top: 22px;
  width: 47px;
}

.footer-top-child li {list-style-type: none; display: inline-block; float: left; width: 50%;}

.footer-top-child li p {padding: 0 0 0 45px; line-height: normal;}

.footer-top-content .footer-top-child li:nth-child(1) p:before {
  background: url(https://static.tatacliq.com/_ui/responsive/theme-blue/images/Sprite-combined.png) no-repeat scroll -7px -175px;
}

.footer-top-content .footer-top-child li:nth-child(2) p:before {
  background: url(https://static.tatacliq.com/_ui/responsive/theme-blue/images/Sprite-combined.png) no-repeat scroll -73px -175px;
}

.footer-top-content .footer-top-child li:nth-child(3) p:before {
  background: url(https://static.tatacliq.com/_ui/responsive/theme-blue/images/Sprite-combined.png) no-repeat scroll -205px -175px;
}

.footer-top-content .footer-top-child li:nth-child(4) p:before {
  background: url(https://static.tatacliq.com/_ui/responsive/theme-blue/images/Sprite-combined.png) no-repeat scroll -139px -175px;
}

.footer-main-content {clear: both; display:flex; padding: 20px 40px;}
.footer-child {clear: both; width: 22%; float: left;}
.footer-child-last {width: 34%; float: right; letter-spacing: .6px; font-size: 12px;}

.footer-child h4 {background-color: white; padding: 5px; border: none; font-size: 12px;}
.footer-child h4 i {float: right;}
.footer-child h4 i:last-child {display: none;}
.footer-child ul {margin-left: 5px; font-size: 11px;}
.footer-child ul a {color: #3a3a3a;}

.footer-last-input {
  display: inline;
  border: 1px solid #ddd;
  padding: 10px;
  margin-top: 20px;
  margin-bottom: 10px;
}

.footer-child-last > form {margin-bottom: 30px;}

.footer-last-btn {padding: 10px; background-color: #444; border: 1px solid #444; color: white;}

.footer-child-last a i {
  color: white;
  background: #444;
  border-radius: 4px;
  margin-right: 5px;
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

.newsletter-success {color: green;}
.newsletter-error {color: red;}

.footer-child h4 span {color: #a9143c;}

@media (max-width: 480px){
  .footer-section-content {
    margin: 20px 15px; clear: both;
  }
  .footer-top-content p {font-size: 12px;}
  .footer-top-content p:before {
    transform: scale(0.6);
  }
  .footer-main-content {display: block; padding: 0;}
  .footer-main-content .footer-child:first-child h4 {border-top: 1px solid #ccc;}
  .footer-child h4 {border-top: 1px solid #ccc; padding: 0 25px; line-height: 48px; letter-spacing: 1px;}
  .footer-child section:last-child h4 {border-bottom: 1px solid #ccc;}
  .footer-child section[expanded] > h4 {border-bottom: none;}
  .footer-child section[expanded] h4 .fa-angle-down {transform:rotate(180deg);}
  .footer-child section:first-child h4 {font-size: 18px;}
  .footer-child h4 i:last-child {display: block;}
  .footer-child ul li {padding-left: 15px;}
  .footer-child ul {margin-bottom: 15px;}
  .footer-copyright {padding: 0 25px; line-height: 48px; margin-bottom: 20px; font-size: 11px; letter-spacing: .6px;}
  .footer-child h4 i, .footer-child h4 i:before {font-size: 18px; line-height: 48px;}
  .footer-child {width: 100%; float: left;}
  .footer-child-last {
    width: auto; float: left;
  }
  .footer-child-last {padding: 20px;}
}
/* start style definiton for TISPRDT-8283 */
.MsoNormal {
	margin-bottom: 0.0001pt; 
	text-align: justify; 
	line-height: normal; 
	background-image: initial; 
	background-position: initial; 
	background-size: initial; 
	background-repeat: initial; 
	background-attachment: initial; 
	background-origin: initial; 
	background-clip: initial; 
	vertical-align: baseline;
}
.MsoNormalSpan {
	font-size:9pt;
	font-family:Montserrat;
	mso-fareast-font-family:"Times New Roman";
	mso-bidi-font-family:"Times New Roman";
	color:#222222;
	border:none windowtext 1pt;
	mso-border-alt:none windowtext 0cm;
	padding:0cm;
	mso-fareast-language:EN-IN;
}
.MsoNormalAnchor {
	color: #000;
	text-decoration: none;
}
.helloSpan {
	color:#1155CC;
}
/* end style definiton for TISPRDT-8283 */
</style>
<style amp-boilerplate>body{-webkit-animation:-amp-start 8s steps(1,end) 0s 1 normal both;-moz-animation:-amp-start 8s steps(1,end) 0s 1 normal both;-ms-animation:-amp-start 8s steps(1,end) 0s 1 normal both;animation:-amp-start 8s steps(1,end) 0s 1 normal both}@-webkit-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@-moz-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@-ms-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@-o-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}</style><noscript><style amp-boilerplate>body{-webkit-animation:none;-moz-animation:none;-ms-animation:none;animation:none}</style></noscript>
</head>
