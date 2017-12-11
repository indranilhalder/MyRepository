<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/pwamp/footer"%>
<!doctype html>
<html amp>
<head>
<meta charset="utf-8">
<script async src="https://cdn.ampproject.org/v0.js"></script>
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

<style amp-custom>body{font-family:'Montserrat', sans-serif; font-size:14px; background-color:#FFFFFF;}



.bg-teal-light{ background-color: #1abc9c; color:#FFFFFF}
.bg-teal-dark{  background-color: #16a085; color:#FFFFFF}
.border-teal-light{ border:solid 1px #1abc9c;}
.border-teal-dark{  border:solid 1px #16a085;}
.color-teal-light{ color: #1abc9c;}
.color-teal-dark{  color: #16a085;}
.bg-green-light{background-color: #2ecc71; color:#FFFFFF}
.bg-green-dark{background-color: #2abb67; color:#FFFFFF}
.border-green-light{border:solid 1px #2ecc71;}
.border-green-dark{ border:solid 1px #2abb67;}
.color-green-light{color: #2ecc71;}
.color-green-dark{color: #2abb67;}
.bg-blue-light{background-color: #3498db; color:#FFFFFF}
.bg-blue-dark{background-color: #2980b9; color:#FFFFFF;}
.border-blue-light{border:solid 1px #3498db;}
.border-blue-dark{ border:solid 1px #2980b9;}
.color-blue-light{color: #3498db;}
.color-blue-dark{color: #2980b9;}
.bg-magenta-light{background-color: #9b59b6; color:#FFFFFF}
.bg-magenta-dark{background-color: #8e44ad; color:#FFFFFF}
.border-magenta-light{border:solid 1px #9b59b6;}
.border-magenta-dark{ border:solid 1px #8e44ad;}
.color-magenta-light{color: #9b59b6;}
.color-magenta-dark{color: #8e44ad;}
.bg-night-light{background-color: #34495e; color:#FFFFFF}
.bg-night-dark{background-color: #2c3e50; color:#FFFFFF}
.border-night-light{border:solid 1px #34495e;}
.border-night-dark{ border:solid 1px #2c3e50;}
.color-night-light{color: #34495e;}
.color-night-dark{color: #2c3e50;}
.bg-yellow-light{background-color: #E67E22; color:#FFFFFF}
.bg-yellow-dark{background-color: #e86f2a; color:#FFFFFF}
.border-yellow-light{border:solid 1px #E67E22;}
.border-yellow-dark{ border:solid 1px #F27935;}
.color-yellow-light{color: #f1c40f;}
.color-yellow-dark{color: #f39c12;}
.bg-orange-light{background-color: #F9690E; color:#FFFFFF}
.bg-orange-dark{background-color: #D35400; color:#FFFFFF}
.border-orange-light{border:solid 1px #F9690E;}
.border-orange-dark{ border:solid 1px #D35400;}
.color-orange-light{color: #e67e22;}
.color-orange-dark{color: #d35400;}
.bg-red-light{background-color: #e74c3c; color:#FFFFFF}
.bg-red-dark{background-color: #c0392b; color:#FFFFFF}
.border-red-light{border:solid 1px #e74c3c;}
.border-red-dark{ border:solid 1px #c0392b;}
.color-red-light{color: #e74c3c;}
.color-red-dark{color: #c0392b;}
.bg-pink-light{background-color: #fa6a8e ; color:#FFFFFF}
.bg-pink-dark{background-color: #FB3365 ; color:#FFFFFF}
.border-pink-light{border:solid 1px #fa6a8e ;}
.border-pink-dark{ border:solid 1px #FB3365 ;}
.color-pink-light{color: #fa6a8e;}
.color-pink-dark{color: #FB3365;}
.bg-gray-light{background-color: #bdc3c7; color:#FFFFFF}
.bg-gray-dark{background-color: #95a5a6; color:#FFFFFF}
.border-gray-light{border:solid 1px #bdc3c7;}
.border-gray-dark{ border:solid 1px #95a5a6;}
.color-gray-light{color: #bdc3c7;}
.color-gray-dark{color: #95a5a6;}
.bg-white{background-color:#FFFFFF;}
.color-white{color:#FFFFFF;}
.border-white{border:solid 1px #FFFFFF;}
.bg-black{background-color:#000000;}
.color-black{color:#000000;}
.border-black{border:solid 1px #000000;}

/*Social Icons*/
.facebook-bg{background-color:#3b5998; color:#FFFFFF;}
.linkedin-bg{background-color:#0077B5; color:#FFFFFF;}
.twitter-bg{background-color:#4099ff; color:#FFFFFF;}
.google-bg{ background-color:#d34836; color:#FFFFFF;}
.whatsapp-bg{ background-color:#34AF23; color:#FFFFFF;}
.pinterest-bg{ background-color:#C92228; color:#FFFFFF;}
.sms-bg{ background-color:#27ae60; color:#FFFFFF;}
.mail-bg{ background-color:#3498db; color:#FFFFFF;}
.dribbble-bg{ background-color:#EA4C89; color:#FFFFFF;}
.tumblr-bg{ background-color:#2C3D52; color:#FFFFFF;}
.reddit-bg{ background-color:#336699; color:#FFFFFF;}
.youtube-bg{ background-color:#D12827; color:#FFFFFF;}
.phone-bg{ background-color:#27ae60; color:#FFFFFF;}
.skype-bg{ background-color:#12A5F4; color:#FFFFFF;}
.facebook-color{    color:#3b5998;}
.linkedin-color{    color:#0077B5;}
.twitter-color{     color:#4099ff;}
.google-color{      color:#d34836;}
.whatsapp-color{    color:#34AF23;}
.pinterest-color{   color:#C92228;}
.sms-color{         color:#27ae60;}
.mail-color{        color:#3498db;}
.dribbble-color{    color:#EA4C89;}
.tumblr-color{      color:#2C3D52;}
.reddit-color{      color:#336699;}
.youtube-color{     color:#D12827;}
.phone-color{       color:#27ae60;}
.skype-color{       color:#12A5F4;}

/*Background Images*/
.bg-1{background-image:url(images/pictures/1.jpg)}
.bg-2{background-image:url(images/pictures/2.jpg)}
.bg-3{background-image:url(images/pictures/3.jpg)}
.bg-4{background-image:url(images/pictures/4.jpg)}
.bg-5{background-image:url(images/pictures/5.jpg)}
.bg-6{background-image:url(images/pictures/6.jpg)}
.bg-7{background-image:url(images/pictures/7.jpg)}
.bg-8{background-image:url(images/pictures/8.jpg)}
.bg-9{background-image:url(images/pictures/9.jpg)}
.bg-body{background-image:url(images/pictures_vertical/bg2.jpg)}
.overlay{background-color:rgba(0,0,0,0.8); position:absolute; top:0px; right:0px; bottom:0px; left:0px;}

/*Font Settings*/
h1{ font-size:24px; line-height:34px; font-weight:500;}
h2{ font-size:22px; line-height:32px; font-weight:500;}
h3{ font-size:20px; line-height:30px; font-weight:500;}
h4{ font-size:18px; line-height:28px; font-weight:500;}
h5{ font-size:16px; line-height:26px; font-weight:500;}
h6{ font-size:14px; line-height:22px; font-weight:800;}
.ultrathin{font-weight:200;}
.thin{font-weight:300;}
.thiner{font-weight:400;}
.boder{font-weight:600;}
.bold{font-weight:700;}
.ultrabold{font-weight:800;}
.capitalize{text-transform: capitalize;}
.italic{font-style: italic;}
.small-text{font-size:12px; display:block;}
.center-text{text-align:center; display:block;}
.right-text{text-align:right;}
.uppercase{text-transform: uppercase;}
.boxed-text{width:80%; margin:0px auto 30px auto;}
.round-image{border-radius:500px;}
p a{display:inline;}

/*Content Settings*/
.content{padding:0px 20px 0px 20px}
.container{margin-bottom:30px}
.full-bottom{margin-bottom:25px}
.no-bottom{margin-bottom:0px}
.full-top{margin-top:25px}
.half-bottom{margin-bottom:15px}
.half-top{margin-top:15px}
.quarter-bottom{margin-bottom:15px}
.hidden{display:none}
.left-column{width:45%; margin-right:5%; float:left}
.right-column{width:45%; margin-left:5%; float:left}
.one-third-left{float:left; width:29%;  margin-right:1%}
.one-third-center{float:left; width:29%; margin-left:5%; margin-right:5%}
.one-third-right{float:left; width:29%; margin-left:1%}
.clear{clear:both}

* {
	margin: 0;
	padding: 0;
	border: 0;
	font-size: 100%;
	vertical-align: baseline;
	outline: none;
	font-size-adjust: none;
	-webkit-text-size-adjust: none;
	-moz-text-size-adjust: none;
	-ms-text-size-adjust: none;
	-webkit-tap-highlight-color: rgba(0,0,0,0);
    -webkit-font-smoothing: antialiased;
    -webkit-transform: translate3d(1,1,1);
    transform:translate3d(1,1,1);
    text-rendering: auto;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
}

div, a, p, img, blockquote, form, fieldset, textarea, input, label, iframe, code, pre {
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

/*Lists*/
.icon-list{list-style:none; font-size:14px; line-height:28px; color:#666666;}
.icon-list i{width:30px;}

.center-icon{
	width:80px;
	height:80px;
	border-radius:80px;
	border:solid 1px rgba(0,0,0,0.5);
	text-align:center;
	line-height:80px;
	font-size:24px;
	margin:0px auto 30px auto;
	display:block;
}

.decoration, .decoration-no-bottom{
	height:1px;
	background-color:rgba(0,0,0,0.1);
}

.decoration{margin-bottom:30px;}
.decoration-margins{margin:0px 20px 30px 20px}

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

.footer-socials a{
	width:40px;
	height:40px;
	line-height:40px;
	margin-left:2px;
	margin-right:2px;
	text-align:center;
	float:left;
}

.footer-socials{
	width:265px;
	margin:0px auto 30px auto;
}

.news-slider .caption{
	background-color:rgba(0,0,0,0.8);
}

.caption{
	position:absolute;
	bottom:0px;
	left:0px;
	right:0px;
	height:65px;
	padding-left:20px;
	padding-right:20px;
	background-color:rgba(0,0,0,0.5);
}

.caption h4{
	font-size:14px;
	color:#FFFFFF;
	line-height:20px;
	margin-top:12px;
}

.caption h3{
	color:#FFFFFF;
	margin-bottom:5px;
	font-size:16px;
	padding-top:23px;
	line-height:0px;
}

.caption p{
	font-size:12px;
	color:rgba(255,255,255,0.5);
}

.call-to-action a{
	width:33.3%;
	float:left;
	text-align:center;
	border-bottom:solid 1px rgba(0,0,0,0.1);
	height:50px;
	line-height:50px;
	color:#1f1f1f;
	font-size:12px;
}

.call-to-action a i{
	padding-right:10px;
}

.social-icons{
	width:150px;
	margin:0 auto;
}

.social-round a{border-radius:50px;}

.social-icons-small{
	width:95px;
	margin:0 auto;
}

.social-icons a{
	line-height:40px;
	width:40px;
	height:40px;
	margin-left:5px;
	margin-right:5px;
	float:left;
}

.social-icons-small a{
	line-height:35px;
	width:35px;
	height:35px;
	margin-left:5px;
	margin-right:5px;
	float:left;
}

/*Heading Block*/
.heading-block{
	padding:30px 20px;
	margin-bottom:30px;
}

.heading-block h4{
	position:relative;
	z-index:10;
	color:#FFFFFF;
}

.heading-block p{
	position:relative;
	z-index:10;
	color:rgba(255,255,255,0.5);
	margin-bottom:0px;
}

.heading-block a{
	z-index:10;
	width:100px;
	height:30px;
	line-height:30px;
	color:#FFFFFF;
	text-align:center;
	font-size:12px;
	margin:20px auto 0px auto;
	border:solid 1px rgba(255,255,255,0.5);
	border-radius:5px;
    display:block;
}

.icon-heading h4{margin-bottom:5px}
.icon-heading h4 i{
	font-size:16px;
	padding-right:20px;
}

.quote-style h4{
	font-weight:300;
	margin-left:40px;
	margin-right:40px;
	text-align:center;
	line-height:40px;
}

.rating{
	width:95px;
	margin-left:auto;
	margin-right:auto;
	margin-bottom:10px;
	display:block;
}


.half-column-left .half-left-img{
	position:absolute;
	border-radius:150px;
	margin-left:-50px;
	left:0px;
}

.half-column-left{
	padding-left:70px;
	padding-right:20px;
	min-height:110px;
    overflow:hidden;
}


.half-column-right .half-right-img{
	position:absolute;
	border-radius:150px;
	margin-right:-50px;
	right:0px;
}

.half-column-right{
	padding-right:70px;
	padding-left:20px;
	min-height:110px;
    overflow:hidden;
}

/*Gallery*/

.gallery-thumb{
	width:31%;
	float:left;
	margin-bottom:3%;
}
.gallery-round .gallery-thumb{border-radius:100px}
.gallery-wide .gallery-thumb-wide{margin-bottom:5px;}

.gallery-thumb:nth-child(3n-1){
	margin-left:3%;
	margin-right:3%;
}

::-webkit-scrollbar { width: 0; }

#sidebar{
	width:250px;
	background-color:#FFFFFF;
}

.sidebar-header a{
	width:50px;
	float:left;
    padding-top:60px;
	line-height:60px;
	color:#1f1f1f;
	text-align:center;
	border-bottom:solid 1px rgba(0,0,0,0.05);
}

.sidebar-logo{
	background-image:url(//assets.tatacliq.com/medias/sys_master/images/9906406817822.png);
	height:31px;
	width:150px;
	background-size:150px 31px;
	display:block;
	margin:25px 0px 25px 15px;
}

.sidebar-menu{margin-left:15px; margin-bottom:0px;}

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

.sidebar-divider{
    font-family: 'Roboto', sans-serif;
    margin-bottom: 20px;
    font-size: 10px;
    padding-left: 20px;
    font-weight: 800;
    text-transform: uppercase;
    color:#495254;
    border-bottom: solid 1px rgba(0,0,0,0.1);
    border-top: solid 1px rgba(0,0,0,0.1);
	line-height:40px;
	margin-bottom:10px;
}

.sidebar-divider-item{
    font-family: 'Roboto', sans-serif;
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

.active-menu, .active-item{
    font-weight:800;
}

.active-menu .fa-circle, .active-item .fa-circle{
    color:#27ae60;
}


/*Splash Page*/
.splash-content .splash-logo{
	background-image:url(images/logo.png);
	background-size:90px 90px;
	width:90px;
	height:90px;
	margin:0px auto 20px auto;
}

.splash-content{
	position:absolute;
	width:240px;
	height:350px;
	left:50%;
	top:50%;
	margin-left:-120px;
	margin-top:-175px;
}

.splash-button{
	width:130px;
	margin:0 auto;
	text-align:center;
	height:40px;
	line-height:40px;
	font-size:12px;
}

/*Landing Content*/
.landing-content{
	width:300px;
	margin:30px auto 30px auto;
	border-bottom:solid 1px rgba(255,255,255,0.1);
}

.landing-content a{
	width:70px;
	height:70px;
	float:left;
	margin:0px 15px 60px 15px;
	border-radius:70px;
	line-height:70px;
	font-size:21px;
	text-align:center;
}

.landing-content a em{
	position:absolute;
	font-size:14px;
	width:70px;
	text-align:center;
	bottom:-60px;
	left:0px;
	right:0px;
	font-style:normal;
}

/*Accordion Styles*/
.accordion h4{
	background-color:transparent;
	border:none;
}

.accordion h4{
	font-size:16px;
	line-height:40px;
}

.accordion h4 i{
	height:40px;
	line-height:40px;
	position:absolute;
	right:0px;
	font-size:12px;
}

.nested-accordion h4{
	font-size:14px;
}

section[expanded] .fa-plus{	transform:rotate(45deg);}
section[expanded] .fa-angle-down{	transform:rotate(180deg);}
section[expanded] .fa-chevron-down{	transform:rotate(180deg);}

/*Fonts*/
.demo-icons a{
	color:#FFFFFF;
	width:20%;
	height:50px;
	float:left;
}
.demo-icons a i{
	color:#1f1f1f;
	font-size:21px;
	width:50px;
	height:50px;
	float:left;
	text-align:center;
	overflow:hidden;
}

/*User Notifications*/
.user-notification{
	text-align:left;
	padding-top:5px;
	padding-left:10px;
	padding-right:10px;
	background-color:#27ae60;
	height:50px;
	color:#FFFFFF;
	font-size:12px;
	line-height:24px;
	width:70%;
	float:left;
}

.user-notification button{
	background-color:#27ae60;
	color:#FFFFFF;
	height:55px;
	position:fixed;
	right:0px;
	bottom:0px;
	width:25%;
}

/*Dropcaps*/

.dropcaps-1:first-letter{
    float:left;
    font-size:57px;
	padding:14px 15px 0px 0px;
    font-weight:800;
    color:#1f1f1f;
}

.dropcaps-2:first-letter{
    font-family: 'Times New Roman', sans-serif;
    float:left;
    font-size:42px;
	padding:15px 15px 0px 0px;
    font-weight:800;
    color:#1f1f1f;
}

.dropcaps-3:first-letter{
    background-color:#1f1f1f;
	padding:10px 15px 10px 15px;
	margin:5px 12px 0px 0px;
    float:left;
    font-size:24px;
    font-weight:800;
    color:#FFFFFF;
}

.dropcaps-4:first-letter{
    font-family: 'Times New Roman', sans-serif;
    font-weight:800;
    background-color:#1f1f1f;
	padding:8px 17px 8px 17px;
	margin:5px 12px 0px 0px;
    float:left;
    font-size:20px;
    font-weight:400;
    color:#FFFFFF;
}

/*Highlights*/
.highlight{margin-bottom:10px;}
.highlight span{padding:3px 5px 3px 5px; margin-right:2px;}
ol ul{	padding-left:5px;}
ol, ul{line-height:24px; margin-left:20px;}
.icon-list{list-style:none; margin-left:0px; padding-left:0px;}
.icon-list i{font-size:10px;}
.icon-list ul{list-style:none; padding-left:10px;}
.icon-list ul ul{padding-left:10px;}

/*Blockquotes*/
.blockquote-1{border-left:solid 3px #1f1f1f; padding:10px 0px 10px 20px;}
.blockquote-1 a{text-align:right; margin-top:-20px;  font-size:12px;}
.blockquote-2 .blockquote-image{position:absolute; border-radius:50px;}
.blockquote-2 h5{padding-left:60px;}
.blockquote-2 .first-icon{padding-left:60px;}
.blockquote-2 a{text-align:right; margin-top:-20px; font-size:12px;}
.blockquote-3 .blockquote-image{width:150px; border-radius:150px; margin:0 auto; display:block;}
.blockquote-3 h5{margin:10px 0px 10px 0px;}
.blockquote-3 .ratings{width:100px; margin:10px auto 10px auto;}
.blockquote-3 .ratings i{font-size:18px;}
.blockquote-4 i{font-size:24px; position:absolute; margin-top:10px;}
.blockquote-4 p{padding-left:50px;}

/*Buttons*/
.button{
	display:inline-block;
	padding:13px 20px;
	margin:0px 0px 25px 0px;
	font-size:12px;
}

.button-round{border-radius:30px;}
.button-full{display: block; text-align: center;}
.button-center{width:100px; margin-left:auto; margin-right:auto; display:block; text-align:center;}
.button:hover{opacity:0.9;}

.icon-square, .icon-round{
	width:40px;
	height:40px;
	line-height:40px;
	text-align:center;
	display:inline-block;
	margin-left:6px;
	margin-right:6px;
	margin-bottom:10px;
	font-size:14px;
}
.icon-square:hover, .icon-round:hover{opacity:0.9;}
.icon-round{border-radius:45px;}

/*Page 404*/
.page-404 h1{font-size:60px; line-height:70px; margin-top:50px;}
.page-soon h1{font-size:60px; line-height:70px; margin-top:50px;}
.page-soon h6{font-size:24px;}

/*Profile Page*/

.profile-gradient{
    background: -moz-linear-gradient(top,rgba(255,255,255,0) 0%,rgba(255,255,255,0.95) 75%,rgba(255,255,255,1) 100%);
    background: -webkit-linear-gradient(top,rgba(255,255,255,0) 0%,rgba(255,255,255,0.95) 75%,rgba(255,255,255,1) 100%);
    background: linear-gradient(to bottom,rgba(255,255,255,0) 0%,rgba(255,255,255,0.95) 75%,rgba(255,255,255,1) 100%);
    filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#00ffffff',endColorstr='#ffffff',GradientType=0);
    height: 250px;
    margin-top: -235px;
}

.profile-overlay .profile-header{margin-top:-80px}
.profile-header h1{font-size:30px;}
.profile-header h6{letter-spacing:2px; opacity:0.5;}
.profile-header h5{font-size:12px;}
.profile-header i{margin-right:10px;}
.profile-header p{font-size:18px;}
.profile-followers a{float:left; width:33%; color:#1f1f1f; font-size:18px;}
.profile-followers em{display:block; font-style:normal; font-size:12px;}
.profile-thumb{margin-top:-50px; width:100px; margin-left:auto; margin-right:auto; display:block; border-radius:100px; border-radius:100px; border:solid 3px #FFFFFF;}

/*Timeline 1*/

.timeline-1{overflow:hidden; padding:20px }

.timeline-1 .timeline-deco{
	position:absolute;
	top:0px;
	left:50%;
	width:1px;
	bottom:0px;
	background-color:rgba(0,0,0,0.15);
}

.timeline-1 .timeline-icon{
	width:60px;
	height:60px;
	border-radius:60px;
	line-height:60px;
	text-align:center;
	font-size:18px;
	background-color:#FFFFFF;
	border:solid 1px rgba(0,0,0,0.2);
	margin:0px auto 30px auto;
}

.timeline-1 .container{background-color:#FFFFFF; padding:30px 0px 1px 0px}
.timeline-2{overflow:hidden; padding:50px 20px 0px 20px; }

.timeline-2 .timeline-deco{
	position:absolute;
	top:0px;
	left:50px;
	width:1px;
	bottom:0px;
	background-color:rgba(0,0,0,0.15);
}

.timeline-2 .timeline-icon{
	width:40px;
	height:40px;
	border-radius:40px;
	line-height:40px;
	text-align:center;
	font-size:18px;
	background-color:#FFFFFF;
	border:solid 1px rgba(0,0,0,0.2);
	margin-left:10px;
}

.timeline-2 .container{
	background-color:#FFFFFF;
	margin-left:70px;
	margin-top:-60px;
	padding-bottom:30px;
}


/*News Slider*/

.news-slider .amp-carousel-button{display:none;}
.news-slider{margin-bottom:10px;}

/*News Thumbs*/

.news-thumbs .news-item{
	min-height:125px;
	color:#1f1f1f;
}

.news-thumbs .news-item .responsive-img{
	width:95px;
	position:absolute;
	margin-top:5px;
}

.news-thumbs .news-item h5{
	margin-left:110px;
	font-size:15px;
}

.news-thumbs .news-item p{
	margin-left:110px;
	line-height:27px;
	margin-bottom:0px;
	font-size:13px;
}

/*News Strip*/

.news-strip{
	background-color:#000000;
	padding:20px 0px 20px 0px;
	margin-bottom:30px;
}

.news-strip h5{
	font-weight:800;
	color:#FFFFFF;
	padding:0px 20px 20px 20px;
}

/*News Cateogry*/

.news-category{
	margin:0px 20px 0px 20px;
}

.news-category p{
	display:inline-block;
	padding:5px 25px 0px 25px;
	font-size:13px;
	margin:0px;
}

.news-category div{
	height:5px;
	width:100%;
}

/*News Block*/

.news-blocks .news-item{
	min-height:125px;
	color:#1f1f1f;
}

.news-blocks .news-item h5{
	font-size:18px;
	padding:15px 0px 5px 0px;
}

/*News full*/

.news-full .news-item{margin-top:1px;}

.news-full .news-item h6{
	position:absolute;
	background-color:rgba(0,0,0,0.8);
	bottom:0px;
	width:100%;
	color:#FFFFFF;
	padding:10px 10px 10px 10px;
}

.news-full .titles{
	position:absolute;
	background-color:#FFFFFF;
	width:250px;
	height:65px;
	margin-top:-65px;
}

.news-full h5{
	font-size:13px;
	padding:10px 20px 0px 20px;
	color:#000000;
}
.news-full em a{display:inline;}
.news-full em{font-size:10px; padding-left:20px; display:block;}
.news-full p{padding:10px 20px 0px 20px;}
.news-full .read-more{
	padding-right:20px;
	text-align:right;
	font-size:12px;
	padding-bottom:30px;
}

/*News Posts*/

.news-post-info{
	font-style:normal;
	font-size:12px;
	padding:5px 0px 15px 0px;
	display:block;
}

.news-post-info a{
	display:inline;

}

/*Contact Page*/

.contactField{
	font-family:'Roboto', sans-serif;
	height:40px;
	line-height:40px;
	line-height:100%;
	width:100%;
	display:block;
	border:solid 1px rgba(0,0,0,0.1);
	text-indent:10px;
	font-size:13px;
	transition:all 250ms ease;
	margin-bottom:20px;
}

.contactField:focus{
	border:solid 1px rgb(39, 174, 96);
	transition:all 250ms ease;
}

.contactTextarea{
	font-family:'Roboto', sans-serif;
	padding-top:10px;
	min-height:80px;
	line-height:40px;
	line-height:100%;
	width:100%;
	display:block;
	border:solid 1px rgba(0,0,0,0.1);
	text-indent:10px;
	font-size:13px;
	transition:all 250ms ease;
	margin-bottom:30px;
}

.contactTextarea:focus{
	transition:all 250ms ease;
	border:solid 1px rgb(39, 174, 96);
}

.field-title{
	font-size:13px;
	margin-bottom:5px;
}

.field-title span{
	font-size:10px;
	color:#cacaca;
	position:absolute;
	right:0px;
	margin-top:2px;
}

.buttonWrap{
	width:100%;
	display:block;
	text-align:center;
	margin-bottom:30px;
    appearance:none;
    -webkit-appearance:none;
}

.contact-icon{
	color:#666666;
	line-height:30px;
}

.contact-icon i{
	color:#1f1f1f;
	width:30px;
}

.sidebar-header a{
padding-top:0px;
}

/*CSS by Vamshi*/
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

.shop_by_department_l3_content > li > ul >li.sbd_l3_headings {font-weight: 600; text-transform: uppercase; color: #000;}

.shop_by_department_l3_content > li > ul >li:hover {color: #a9133d;}

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

.header-search-right, .header-category {
	padding-top: 15px;
  padding-bottom: 5px;
  margin-right: 50px;
}

.header-search-center {padding-top: 10px; padding-bottom: 10px;}

.mobile-item {
	display: none;
}

.my-bag {
	background-color: #000;
	color: #fff;
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
.homeViewHeading {text-align: center; font-size: 28px; padding-bottom: 20px;}
p {margin: 0;}
.brandsYouLove {margin: 20px;}
#brandsYouLove {display: none}
#brandsYouLoveMobileComp {display: block; background-color: #f2f2f2;padding: 20px; }
.brandsCarouselItem {width: 260px; height: 320px; margin: 0px 10px; border-radius: 4px; background-color: white; box-shadow: 0 2px 3px 0 rgba(0, 0, 0, 0.1);}
.brandStudioImg {height: 180px;}
.brandStudioImg a {height: inherit;}
.brandStudioImg img {width: 100%; height: 100%; border-top-left-radius: 4px; border-top-right-radius: 4px;}
.brandStudioDescHeading, .brandStudioDescInfo, .brandStudioVisitStore {padding: 14px 8px 0px; text-align: left;}
.brandStudioDescHeading {font-size: 13px; font-weight: bold;}
.brandStudioDescInfo {font-size: 12px; color: #666666;white-space: pre-line;}
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
.footer-child-last {width: 34%; float: right;}

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
		<section class="col-xs-12 header-search-section">
			<section class="header-search-left">
				<section class="header-search-left-child logo-container">
					<amp-img class="logo-image" width="70" height="40" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/9906406817822.png"></amp-img>
				</section>
				<section class="header-category header-search-left-child desktop-item shop-department">
					SHOP BY<br />
					<strong>DEPARTMENT<i class="fa fa-chevron-down"></i></strong>
          <section id="shop_by_department" class="desktop-item shop-by-department">
            <section class="shop-by-department-l2">
              <ul>
                <li>
                  Men
                  <section class="shop-by-department-l3">
                    <ul class="shop_by_department_l3_content">
                      <li>
                        <ul>
                          <li class="sbd_l3_headings">Ethnic Wear</li>
                          <li>Kurtis And Kurtas</li>
                          <li>Suit Sets</li>
                          <li>Fusion Wear</li>
                          <li>Sarees</li>
                          <li>Bottoms</li>
                          <li>Dupattas</li>
                          <li class="sbd_l3_headings">Inner & Nightwear</li>
                          <li>Bras</li>
                          <li>Panties</li>
                          <li>Lingerie Sets</li>
                          <li>Camisole</li>
                          <li>Sleepwear & Robes</li>
                          <li>Shapewear</li>
                          <li class="sbd_l3_headings">Swim & Beachwear</li>
                          <li class="sbd_l3_headings">Active & Sportswear</li>
                        </ul>
                      </li>
                      <li>
                        <ul>
                          <li class="sbd_l3_headings">Top Brands</li>
                          <li>Westside</li>
                          <li>Hunkemoller</li>
                          <li>Clarks</li>
                          <li>New Look</li>
                          <li>Baggit</li>
                          <li>Titan</li>
                          <li class="sbd_l3_headings">Value Stores</li>
                          <li>Kurtas Under 899</li>
                          <li>Dresses Under 999</li>
                          <li>Footwear Under 799</li>
                          <li>Sleepwear Under 699</li>
                          <li>Bags Under 999</li>
                          <li>Watches Under 1999</li>
                          <li class="sbd_l3_headings">Winter Wear Store</li>
                          <li class="sbd_l3_headings">The Wedding Store</li>
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
					SHOP BY<br />
					<strong>BRAND <i class="fa fa-chevron-down"></i></strong>
          <section id="shop_by_brand" class="desktop-item shop-by-brand">
            <section class="shop-by-brand-l2">
              <ul>
                <li>
                  Appliances
                  <section class="shop-by-brand-l3">
                    <a class="shop-by-brand-l3-component" href="#">
                      <amp-img width="auto" height="195" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/10987709300766.png"></amp-img>
                      <amp-img width="80" height="28" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/9448599846942.png"></amp-img>
                    </a>
                    <a class="shop-by-brand-l3-component" href="#">
                      <amp-img width="auto" height="195" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/10987709366302.jpg"></amp-img>
                      <amp-img width="80" height="28" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/10523118829598.png"></amp-img>
                    </a>
                    <a class="shop-by-brand-l3-component" href="#">
                      <amp-img width="auto" height="195" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/10987709890590.png"></amp-img>
                      <amp-img width="80" height="28" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/10701061423134.png"></amp-img>
                    </a>
                    <a class="shop-by-brand-l3-component" href="#">
                      <amp-img width="auto" height="195" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/10987709464606.png"></amp-img>
                      <amp-img width="80" height="28" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/9461676769310.png"></amp-img>
                    </a>
                    <a class="shop-by-brand-l3-component" href="#">
                      <amp-img width="auto" height="195" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/10987709530142.png"></amp-img>
                      <amp-img width="80" height="28" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/10791666155550.png"></amp-img>
                    </a>
                    <a class="shop-by-brand-l3-component" href="#">
                      <amp-img width="auto" height="195" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/10987709300766.png"></amp-img>
                      <amp-img width="80" height="28" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/9448599846942.png"></amp-img>
                    </a>
                    <a class="shop-by-brand-l3-component" href="#">
                      <amp-img width="auto" height="195" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/10987709366302.jpg"></amp-img>
                      <amp-img width="80" height="28" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/10523118829598.png"></amp-img>
                    </a>
                    <a class="shop-by-brand-l3-component" href="#">
                      <amp-img width="auto" height="195" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/10987709890590.png"></amp-img>
                      <amp-img width="80" height="28" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/10701061423134.png"></amp-img>
                    </a>
                    <a class="shop-by-brand-l3-component" href="#">
                      <amp-img width="auto" height="195" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/10987709464606.png"></amp-img>
                      <amp-img width="80" height="28" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/9461676769310.png"></amp-img>
                    </a>
                    <a class="shop-by-brand-l3-component" href="#">
                      <amp-img width="auto" height="195" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/10987709530142.png"></amp-img>
                      <amp-img width="80" height="28" layout="flex-item" src="//assets.tatacliq.com/medias/sys_master/images/10791666155550.png"></amp-img>
                    </a>
                  </section>
                </li>
                <li>
                  Electronics
                  <section class="shop-by-brand-l3">
                    Electronics
                  </section>
                </li>
                <li>Watches and Accessories</li>
                <li>Women's Wear</li>
                <li>Lingerie</li>
                <li>Men's Wear</li>
                <li>Footwear</li>
                <li>Kids</li>
                <li>A-Z List</li>
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

</body>
</html>
