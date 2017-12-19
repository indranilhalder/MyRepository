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
<link rel="canonical" href="/pwamp">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<link href="https://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet">
<link rel="manifest" href="/manifest.json">
<meta name="viewport" content="width=device-width,minimum-scale=1,initial-scale=1,maximum-scale=1,user-scalable=no"><meta name="apple-mobile-web-app-capable" content="yes"/><meta name="apple-mobile-web-app-status-bar-style" content="black">

<!-- Latest compiled and minified CSS -->
<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" type="text/css" > -->
<!-- jQuery library -->
<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script> -->
<!-- Latest compiled JavaScript -->
<!-- <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> -->

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
  border: none;
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

.header-navigation-right ul li {list-style-type: none; display: inline-block; padding: 10px; font-size: 12px;}
.header-navigation-right ul {line-height: normal; margin: 0;}
.header-navigation-right ul li a {letter-spacing: .6px; color: #fff;}
.logo-image {
	left: 40px;
  top: 5px;
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
	width: 50%;
	float: right;
}

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

.header-search-btn {padding: 10px; background-color: #f8f8f8; border: 1px solid #ddd; border-left: none; cursor: pointer; height: 38px; float: left; width: 60px;}

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
.suggest {width: 31%; margin-left: 152px; position: absolute; top: 48px;}

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
	#topDealsCompCarousel amp-carousel, #whatToBuyCompCarousel amp-carousel, #hotNowCompCarousel amp-carousel {
		/*give important*/
		max-height: 180px;
	}

  #topDealsCompCarousel amp-list, #whatToBuyCompCarousel amp-list, #hotNowCompCarousel amp-list {
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
	.brandStudioTop {padding: 0px 15px;}
	.brandsYouLove {margin: 20px;}
	#brandsYouLove {display: none;}
	#brandsYouLoveMobileComp {display: block; background-color: #f2f2f2; padding: 5px 0px 15px;}
	.brandStudioImg {height: 180px;}
	.brandStudioImg img {width: 100%; height: 100%; border-top-left-radius: 4px; border-top-right-radius: 4px;}
	.brandStudioDescHeading, .brandStudioDescInfo, .brandStudioVisitStore {padding: 14px 8px 0px; text-align: left;}
	.brandStudioDescHeading {font-size: 13px;}
	.brandStudioDescInfo {font-size: 12px; color: #666666;white-space: pre-line; max-height: 60px;}
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
	.whatToBuyItem {width: 128px; height: 160px;}
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

.stayQuedBottom {display: block; text-align: center; padding: 10px;}
.stayQuedBottom a {display: -webkit-inline-box;}
.stayQuedViewAllBtn {background-color: grey; padding: 10px 30px; border: none; color: white; cursor: pointer;}

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
  .footer-top-content p {font-size: 12px;}
  .footer-top-content p:before {
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
