<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<style>
.showcase {
    height: auto;
    background: #F7F8FA;
    overflow: hidden;
    clear:both;
    padding:20px 0;
}
.showcase h1 {
    text-align: center;
    padding: 30px 0;
    font-weight: 500;
    font-size: 24px;
}
.showcase-switch> div{
width:33.33%;
float:left;
text-align:center;
}
.showcase-border{border-bottom: 1px solid;}
.showcase-heading {
   text-align: center;
    color: #2d2e32;
    text-transform: uppercase;
    font-size: 12px;
    letter-spacing: 1px;
    font-weight: 600;
	    overflow: hidden;
    padding-bottom: 30px;
}

.showcase section {
    margin: 40px 20px;
    text-align: center;
    overflow: hidden;
}

p.showcase-para {
    width: 80%;
    text-align: center;
    font-size: 16px;
    line-height: 21px;
    margin-left: 26px;
}

.showcase-heading > div {
    margin: 20px 0px;
}

.showcase-center {
    margin: 20px 0px;
}
.showcase-center h3, .showcase-center p{line-height: 20px;}
.showcase-section .desc-section{
width:33.33%;
float:left;
text-align:center;
}
a.button.trending-button {
    background: transparent;
    color: #333;
    border: 1px solid #4a4a4a;
    font-size: 12px;
    font-weight: 600;
    text-decoration: none;
	width: 80%;
    display: block;
    margin: 0px auto;
    height: 50px;
    line-height: 28px;
    letter-spacing: 1px;
    text-transform: uppercase;
}
a.button.maroon {
    color: #FBF4F6;
    background-color: #a9143c;
    font-size: 12px !important;
    text-decoration: none;
	width: 70%;
    display: block;
    margin: 20px auto;
    height: 50px;
    line-height: 28px;
    letter-spacing: 1px;
    font-weight: bold;
    text-transform: uppercase;
}
.desc-section img{
max-width:100%;
margin: 0px auto;
}
.showcase-switch> div a:hover{
cursor:pointer;
color:#a9143c;
}
@media(max-width:650px){
.showcase-section .desc-section,.showcase-switch> div{
width:100%;
float:none;
}
.showcase-section .desc-section{
padding: 15px 0;
}
}
#newAndExclusive h1{
    font-size: 30px;
    text-align: center;
}

.buy-banner {
    overflow: hidden;
    width: 100%;
    float: left;
}
.buy-banner p {
    padding: 35px;
    line-height: 20px;
    color: #fff;
    text-transform: uppercase;
    background: #a9143c;
    font-weight: 600;
    letter-spacing: 1.5px;
    text-align: center;
}

/*Tata cliq Stay Queu css starts*/
.qued {
    background-color: #e4E4E4;
    height: auto;
    padding-bottom: 20px;
	width:50%;
	overflow:hidden;
}
.qued h1 {
    text-align: center;
    font-size: 24px;
    padding: 40px 0;
    font-weight: 500;
	}
.h1-qued {
    position: relative;
    top: -15px;
    color: #2e2e2e;
}
.qued .qued-content h5, .qued .qued-content h2, 
.qued .qued-content h3 {
    color: #A9133B;
}
.qued-content h5 {
    font-size: 17px;
    line-height: 20px;
    text-align: left;
	    margin: 0px;
}
.qued-content h2 {
    font-size: 43px;
    text-transform: uppercase;
    font-weight: bold;
    line-height: 50px;
    text-align: left;
	    margin: 0px;
}
.qued-content h3 {
    text-transform: uppercase;
    font-size: 41px;
    line-height: 50px;
    text-align: left;
	    margin: 0px;
}
.qued-content {
    width: 50%;
	    position: relative;
    min-height: 1px;
	float:left;
    padding-left: 10px;
    padding-right: 10px;
	box-sizing:border-box;
  
}
.qued-content p {
    font-size: 14px;
    color: #2e2e2e;
    margin: 20px 0px;
    text-align: left;
}
.qued-content a.button.maroon {
    margin-top: 90px;
    width: 200px;
    padding-bottom: 20px;
}
a.button.maroon, button.maroon, button.maroon:hover {
    color: #FBF4F6;
    background-color: #a9143c;
    font-size: 12px !important;
    text-decoration: none;
}
a.button, button {
    border: none;
    display: block;
    font-size: 16px;
    cursor: pointer;
    font-weight: bold;
    line-height: 24px;
    text-align: center;
	 padding: 13px 18px;
    font-family: "Avenir Next";
    letter-spacing: 1px;
    vertical-align: middle;
    text-transform: uppercase;
    -webkit-transition: background 0.3s;
    -moz-transition: background 0.3s;
    transition: background 0.3s;
}
.qued-image{
	width:50%;
	position: relative;
	float: left;
    min-height: 1px;
    padding-left: 10px;
    padding-right: 10px;
	box-sizing:border-box;
}
.qued-image img {
    float: right;
}
.qued-image img:nth-child(2) {
    position: relative;
    margin-top: -136px;
    right: 129px;
}
.img-responsive{
    display: block;
    max-width: 100%;
    height: auto;
}

/*Tata cliq  qued css ends*/
</style>
<template:page pageTitle="${pageTitle}">

	<div class="no-space homepage-banner">
		<cms:pageSlot position="Section1" var="feature">
			<cms:component component="${feature}" />
		</cms:pageSlot>
	</div>

	<div class="shop-promos">
		<ul class="promos">
			<cms:pageSlot position="Section2A" var="feature">
				<cms:component component="${feature}" element="li" />
			</cms:pageSlot>
			<cms:pageSlot position="Section2B" var="feature">
				<cms:component component="${feature}" element="li" />
			</cms:pageSlot>
		</ul>
	</div>
	<cms:pageSlot position="Section2C" var="feature">
		<cms:component component="${feature}" />
	</cms:pageSlot>
	<div id="brandsYouLove" class="home-brands-you-love-wrapper feature-collections"></div>
	<div id="promobannerhomepage" class="buy-banner"></div>

	<div id="bestPicks" class="feature-collections"></div>
	<div class="feature-categories" id="ia_categories_favorites"></div>
	<div class="feature-collections">
	<div id="stayQued" class="qued"></div>
	<div id="newAndExclusive" class=""></div>
	</div>
	<!-- For Infinite Analytics Start -->
	<!-- <div class="brands" id="ia_brands_favorites"></div> -->
	<!-- <div class="feature-categories" id="ia_categories_favorites"></div>
	<div class="feature-collections" id="ia_collections"></div> -->
	<div class="trending" id="ia_products_hot"></div>
	<!-- For Infinite Analytics End -->
	<div id="showcase" class="showcase feature-collections"></div>
	

</template:page>

