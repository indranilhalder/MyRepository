<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="g" uri="http://granule.com/tags/accelerator"%>
<%@ taglib prefix="compressible" tagdir="/WEB-INF/tags/responsive/template/compressible" %>
<%@ taglib prefix="cms" tagdir="/WEB-INF/tags/responsive/template/cms" %>

<style id="onloadcss">
body {
	    font-family: "Montserrat";
	    font-size: 12px;
	    -webkit-text-size-adjust: none;
	    -webkit-font-smoothing: subpixel-antialiased;
	}
	
	div,span,h2,h4,p,a,img,b,ul,li,form,label,header,nav {
	    margin: 0;
	    padding: 0;
	    border: 0;
	    font-size: 100%;
	    font: inherit;
	    vertical-align: baseline
	}
	
	b {
	    font-weight: 700
	}
	
	img {
	    vertical-align: middle
	}
	
	header,nav {
	    display: block
	}
	
	ul {
	    list-style: none
	}
	
	* {
	    -webkit-box-sizing: border-box;
	    -moz-box-sizing: border-box;
	    box-sizing: border-box
	}
	
	:before,:after {
	    -webkit-box-sizing: border-box;
	    -moz-box-sizing: border-box;
	    box-sizing: border-box
	}
	
	a.button, button,select, input {
	    -webkit-appearance: none;
	    -moz-appearance: none;
	    -ms-appearance: none;
	    -o-appearance: none;
	    height: 50px;
	    line-height: 50px;
	    vertical-align: middle;
	    font-size: 12px;
	    border-radius: 0;
	    background-color: #fff;
	    border: 2px solid #000;
	    padding: 13px 18px;
	    cursor: pointer;
	    font-family: "Montserrat";
	    -webkit-transition: background .3s;
	    -moz-transition: background .3s;
	    transition: background .3s
	}
	
	input {
	    cursor: text;
	}
	
	a.button, button {
	    border: 0;
	    display: block;
	    font-size: 16px;
	    cursor: pointer;
	    font-weight: 700;
	    line-height: 24px;
	    text-align: center;
	    font-family: "Montserrat";
	    letter-spacing: 1px;
	    vertical-align: middle;
	    text-transform: uppercase;
	    -webkit-transition: background .3s;
	    -moz-transition: background .3s;
	    transition: background .3s
	}
	
	a {
	    text-decoration: none;
	    color: #000;
	    cursor: pointer
	}
	
	a,a:after {
	    -webkit-transition: color .3s, border-color .3s, background .3s;
	    -moz-transition: color .3s, border-color .3s, background .3s;
	    transition: color .3s, border-color .3s, background .3s
	}
	
	:focus {
	    outline: 0
	}
	
	input:focus,button:focus,select:focus {
	    outline: 0
	}
	
	input[type="text"], input[type="password"], input[type="email"], input[type="tel"] {
	    border: 1px solid #dfd1d5;
	    line-height: 1;
	    padding: 0;
	    height: 40px;
	    outline: 0;
	    padding-left: 10px;
	    cursor: text;
	    text-overflow: ellipsis
	}
	
	select {
	    border: 1px solid #dfd1d5;
	    background-color: #fff;
	    height: 32px;
	    padding: 0 15px;
	    background-image: url(${siteRootUrl}/theme-blue/images/arrow.png);
	    background-repeat: no-repeat;
	    background-position: 95% 53%;
	    line-height: 30px;
	    text-indent: .01px;
	    text-overflow: ellipsis;
	    -moz-appearance: none
	}
	
	select::-ms-expand {
	    display: none
	}
	
	input::-ms-clear {
	    display: none
	}
	
	label {
	    display: inline-block
	}
	
	input,select,.select-view {
	    display: inline
	}
	
	.select-view select {
	    display: inline-block
	}
	
	@media (min-width:651px) {
	    .select-view select {
	        display: none
	    }
	}
	
	.select-view .select-list {
	    display: none;
	    float: left;
	    text-align: left;
	    border: 1px solid #dfd1d5;
	    position: relative;
	    padding-top: 48px;
	    font-size: 16px;
	    font-weight: 500;
	    height: 50px;
	    width: 240px;
	    -webkit-transition: border .3s, z-index 0 .3s;
	    -moz-transition: border .3s, z-index 0 .3s;
	    transition: border .3s, z-index 0 .3s
	}
	
	.select-view .select-list>.selected {
	    position: absolute;
	    top: 0;
	    left: 0;
	    height: 50px;
	    width: 100%;
	    width: -webkit-calc(100% - 1px);
	    width: calc(100% - 1px);
	    line-height: 50px;
	    padding-left: 10px;
	    font-size: 13px;
	    margin: -1px;
	    background-color: transparent;
	    padding-right: 20px
	}
	
	.select-view .select-list>.selected:after {
	    position: absolute;
	    top: 0;
	    right: 16px;
	    font-weight: lighter;
	    font-size: 20px;
	    content: "\f107";
	    font-family: FontAwesome;
	    display: inline-block;
	    color: #000
	}
	
	.select-view .select-list ul {
	    max-height: 0;
	    overflow: hidden;
	    width: calc(100% - -2px);
	    width: -webkit-calc(100% - -2px);
	    margin: 2px -1px -2px;
	    border-right: 1px solid transparent;
	    border-left: 1px solid transparent;
	    cursor: pointer;
	    -webkit-transition: max-height .3s, border .3s;
	    -moz-transition: max-height .3s, border .3s;
	    transition: max-height .3s, border .3s
	}
	
	.select-view .select-list ul:last-child {
	    border-bottom: 1px solid transparent
	}
	
	.select-view .select-list li {
	    width: -webkit-calc(100% - -4px);
	    width: calc(100% - -4px);
	    background-color: #fff;
	    line-height: 46px;
	    padding-left: 10px;
	    position: relative;
	    border: 1px solid transparent;
	    margin: 0 -1px -1px
	}
	
	@media (max-width:900px) {
	    .select-view .select-list li {
	        padding-left: 10px
	    }
	}
	
	.select-view .select-list:hover ul {
	    max-height: 500px;
	    border-color: #dfd1d5;
	    -webkit-transition: max-height .15s, border 0;
	    -moz-transition: max-height .15s, border 0;
	    transition: max-height .15s, border 0
	}
	
	@media (min-width:791px) {
	    .select-view .select-list {
	        display: inline-block
	    }
	}
	
	h2 {
	    font-size: 32px;
	    font-weight: 500
	}
	
	@media (min-width:791px) {
	    header {
	        margin-top: 0;
	        position: relative;
	        -webkit-transition: margin .3s;
	        -moz-transition: margin .3s;
	        transition: margin .3s
	    }
	    header .content .top::after,header .content .bottom::after {
	        clear: both;
	        content: "";
	        display: table
	    }
	    header .content .left,header .content .right {
	        display: inline-block;
	        line-height: 36px;
	        vertical-align: middle;
	        height: 36px;
	    }
	    header .content .right a {
	        font-weight: 500
	    }
	    header .content .left {
	        float: left
	    }
	    header .content .right {
	        float: right
	    }
	    header .content .right>ul {
	        clear: both
	    }
	    header .content .right>ul::after {
	        clear: both;
	        content: "";
	        display: table
	    }
	    header .content .right>ul>li {
	        float: right;
	        padding: 0 20px;
	        position: relative;
	        -webkit-transition: background .3s;
	        -moz-transition: background .3s;
	        transition: background .3s
	    }
	}
	
	@media (min-width:791px) and (max-width:855px) {
	    header .content .right>ul>li {
	        padding: 0 10px
	    }
	}
	
	@media (min-width:791px) {
	    header .content .right>ul>li:first-of-type {
	        margin-right: 10px
	    }
	    header .content .right>ul>li.wishlist>a:before {
	        font-family: 'icomoon';
	        -webkit-font-smoothing: antialiased;
	        -moz-osx-font-smoothing: grayscale;
	        content: "\e602";
	        font-size: 14px;
	        line-height: 18px;
	        margin-right: 5px;
	        top: 3px;
	        position: relative;
	        font-weight: lighter
	    }
	}

	
	@media (min-width:791px) {
	    header .content .bottom {
	        background-color: #fff;
	        max-height: 64px;
	        -webkit-transition: max-height .3s;
	        -moz-transition: max-height .3s;
	        transition: max-height .3s
	    }
	    header .content .bottom .bag {
	        display: none
	    }
	    header .content .marketplace,
	    header .content nav {
	        float: left;
	        vertical-align: middle;
	    }
	    header .content .marketplace {
	        font-size: 20px;
	        font-weight: 500;
	        letter-spacing: 1.5px;
	        text-transform: uppercase;
	        margin-right: 25px;
	        margin-left: 30px;
	        height: 64px;
    		line-height: 64px;
    		width: auto;
	    }
	    header .content .marketplace span {
	        text-transform: none;
	        color: #a9143c;
	        font-family: inherit;
	        font-weight: 100
	    }
	}
	
	@media (min-width:791px) and (max-width:900px) {
	    header .content .marketplace {
	        margin-left: 5px;
	       /*  width: 215px */
	    }
	}
	.bell-icon:after {
    font-family: 'FontAwesome';
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    content: "\f0a2";
    font-size: 17px;
    line-height: 41px;
    height: 41px;
    top: 0;
    position: relative;
    font-weight: lighter;
}
	@media (min-width:791px) {
	.bell-icon:after {
    line-height: 33px;
    height: 36px;
    color: rgba(255,255,255,0.7);
		}
	    header .content nav>ul {
	        clear: both;
	        padding-top: 18px
	    }
	    header .content nav>ul::after {
	        clear: both;
	        content: "";
	        display: table
	    }
	    header .content nav>ul>li {
	        float: left;
	        padding: 0;
	        width: 120px;
	        -webkit-transition: background-color .3s .3s;
	        -moz-transition: background-color .3s .3s;
	        transition: background-color .3s .3s
	    }
	    header .content nav>ul>li:first-child>ul {
	        left: 205px;
	        width: 150px
	    }
	    header .content nav>ul>li:first-child+li {
	        margin-right: 0
	    }
	    header .content nav>ul>li:first-child+li>ul {
	        left: 375px;
	        width: 150px
	    }
	    header .content nav>ul>li>.toggle {
	        font-weight: 500;
	        font-size: 11.5px;
	        line-height: 20px;
	        height: 50px;
	        text-transform: uppercase;
	        letter-spacing: 1.5px;
	        -webkit-transition: color .3s;
	        -moz-transition: color .3s;
	        transition: color .3s;
	        white-space: nowrap;
	        padding-bottom: 0px;
			position: relative;
	    }
	    header .content nav>ul>li>.toggle:after {
	        font-family: 'FontAwesome';
	        -webkit-font-smoothing: antialiased;
	        -moz-osx-font-smoothing: grayscale;
	        font-weight: 100;
	        content: "\f107";
	        display: inline-block;
	        color: #000;
	        position: relative;
	        margin-left: 5px;
	        top: 3px;
	        font-size: 20px;
	        line-height: 10px
	    }
	    header .content .right>ul>li.wishlist .wishlist-info{
	    	max-height: 0;
    		overflow: hidden;
    		position: absolute;
	    }
	}
	
	@media (min-width:791px) and (max-width:1050px) {
	    header .content nav>ul>li {
	        margin-right: 0;
	        width: 115px
	    }
	    header .content nav>ul>li>.toggle:after {
	        margin-left: 3px;
	        top: 3px
	    }
	    header .content nav>ul>li:first-child {
	        padding-right: 0
	    }
	    header .content nav>ul>li:first-child>ul {
	        left: 205px;
	        width: 150px
	    }
	}
	
	@media (min-width:791px) and (max-width:990px) {
	    header .content nav>ul>li:first-child>ul {
	        left: 205px
	    }
	}
	
	@media (min-width:791px) and (max-width:1050px) {
	    header .content nav>ul>li:first-child+li {
	        padding-right: 0
	    }
	    header .content nav>ul>li:first-child+li>ul {
	        left: 351px;
	        width: 150px
	    }
	}
	
	@media (min-width:791px) and (max-width:900px) {
	    header .content nav>ul>li:first-child>ul {
	        left: 220px
	    }
	    header .content nav>ul>li:first-child+li>ul {
	        left: 330px
	    }
	}
	
	@media (min-width:791px) {
	    header .content nav>ul>li>ul {
	        position: absolute;
	        left: 0;
	        top: 70px;
	        height: 0;
	        z-index: 1;
	        width: 100%;
	        overflow: hidden;
	        font-size: 18px;
	        line-height: 50px;
	        -webkit-transition: height .3s .3s;
	        -moz-transition: height .3s .3s;
	        transition: height .3s .3s
	    }
	}
	
	@media (min-width:791px) {
	    header .content .search {
	        float: right;
	        max-width: 675px;
	        min-width: 349px;
	        width: -webkit-calc(100% - 690px);
	        width: calc(100% - 690px)
	    }
	    header .content .search::after {
	        clear: both;
	        content: "";
	        display: table
	    }
	}
	
	@media (min-width:791px) and (max-width:920px) {
	    header .content .search {
	        min-width: 260px
	    }
	    header .content .search form {
	        margin-right: 0
	    }
	}
	
	@media (min-width:791px) and (max-width:1024px) {
	    header .content .search {
	        width: -webkit-calc(100% - 540px);
	        width: calc(100% - 540px)
	    }
	}
	
	@media (min-width:791px) and (max-width:900px) {
	    header .content .search {
	        width: -webkit-calc(100% - 515px);
	        width: calc(100% - 515px)
	    }
	}
	
	@media (min-width:791px) {
	    header .content .search form {
	        height: 42px;
	        margin: 12px 20px 0 0;
	        position: relative;
	        clear: both
	    }
	    header .content .search form::after {
	        clear: both;
	        content: "";
	        display: table
	    }
	    header .content .search button,header .content .search .select-list,header .content .search input {
	        display: inline-block;
	        float: left
	    }
	    header .content .search button {
	        position: relative;
	        width: 51px;
	        border: 1px solid #bbb;
	        border-left: 0;
	        float: right
	    }
	    header .content .search button:focus {
	        -webkit-box-shadow: inset #eee 0 0 2px 2px;
	        -moz-box-shadow: inset #eee 0 0 2px 2px;
	        -o-box-shadow: inset #eee 0 0 2px 2px;
	        box-shadow: inset #eee 0 0 2px 2px
	    }
	    header .content .search button:focus:before {
	        font-size: 17px
	    }
	    header .content .search button:before {
	        content: "";
	        background: url(${siteRootUrl}/theme-blue/images/Sprite-combined.png) no-repeat scroll -300px -207px;
	        display: inline-block;
	        height: 18px;
	        width: 18px;
	        position: absolute;
	        top: 50%;
	        left: 50%;
	        -webkit-transform: translateX(-50%) translateY(-50%);
	        -moz-transform: translateX(-50%) translateY(-50%);
	        -ms-transform: translateX(-50%) translateY(-50%);
	        -o-transform: translateX(-50%) translateY(-50%);
	        transform: translateX(-50%) translateY(-50%)
	    }
	    header .content .search input,header .content .search .select-list {
	        border-width: 1px;
	        border-color: #bbb;
	        font-size: 13px;
	        font-weight: 500;
	        height: 50px
	    }
	}
	
	@media (min-width:791px) {
	    header .content .search input {
	        line-height: 50px;
	        width: 40%;
	        border-radius: 0;
	        width: -webkit-calc(100% - 204px);
	        width: calc(100% - 204px)
	    }
	}
	
	@media (min-width:791px) and (max-width:990px) {
	    header .content .search input {
	        width: -webkit-calc(100% - 124px);
	        width: calc(100% - 124px)
	    }
	}
	
	@media (min-width:791px) {
	    header .content .search .select-list {
	        border: 1px solid #bbb;
	        margin-left: -1px;
	        margin-right: -1px;
	        width: 155px;
	        z-index: 3;
	        -webkit-transition: border .3s;
	        -moz-transition: border .3s;
	        transition: border .3s
	    }
	}
	
	@media (min-width:791px) and (max-width:990px) {
	    header .content .search .select-list {
	        width: 75px
	    }
	}
	
	@media (min-width:1023px) and (max-width:1100px) {
	    header .content .search {
	        width: -webkit-calc(100% - 567px);
	        width: calc(100% - 567px)
	    }
	    header .content nav>ul>li {
	        margin-right: 20px
	    }
	}
	
	@media (min-width:791px) {
	    header .content .bottom .bag, .brand-sub header .content .bottom .bag {
	        float: right;
	        width: 126px;
	        margin-right: 23px;
	        line-height: 40px;
	        margin-top: 11px;
	        border-radius: 4px;
	        background-color: #444;
	        box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.24), 0 0 4px 0 rgba(0, 0, 0, 0.12);
	        border: solid 1px #979797;
	        -webkit-transition: z-index 0 .3s, border .3s .3s, opacity .15s;
	        -moz-transition: z-index 0 .3s, border .3s .3s, opacity .15s;
	        transition: z-index 0 .3s, border .3s .3s, opacity .15s
	    }
	}
	
	@media (min-width:791px) {
	    header .content .bottom .bag {
	        display: block;
	        -webkit-transition: opacity .15s .15s;
	        -moz-transition: opacity .15s .15s;
	        transition: opacity .15s .15s
	    }
	    header .content .bottom .bag .mini-cart-link:before {
	        content: "";
	        background: url(${siteRootUrl}/theme-blue/images/Sprite-combined.png) no-repeat scroll -380px -176px;
	        display: inline-block;
	        width: 23px;
	        height: 23px;
	        position: relative;
	        top: 6px;
	        right: 2px
	    }
	}
	
	@media (max-width:790px) {
	    header>.content {
	        padding: 0 15px 15px;
	        position: relative
	    }
	    header>.content .top {
	        position: absolute;
	        z-index: 2;
	        top: 0;
	        background-color: #fff;
	        width: -webkit-calc(100% - 50px);
	        width: calc(100% - 50px);
	        left: -webkit-calc(50px - 100%);
	        left: calc(50px - 100%)
	    }
	    header>.content .top>.toggle {
	        position: absolute;
	        width: 50px;
	        height: 51px;
	        right: -51px;
	        display: inline-block
	    }
	    header>.content .top .toggle span {
	        display: block;
	        background: #000;
	        position: absolute;
	        top: 25px;
	        left: 0;
	        width: -webkit-calc(100% - 20px);
	        width: calc(100% - 20px);
	        height: 4px;
	        border-radius: 2px;
	        margin: 0 10px;
	        -webkit-transition: top .15s .3s, left .15s .3s, opacity .15s .3s, -webkit-transform .15s .15s;
	        -moz-transition: top .15s .3s, left .15s .3s, opacity .15s .3s, -moz-transform .15s .15s;
	        transition: top .15s .3s, left .15s .3s, opacity .15s .3s, transform .15s .15s
	    }
	    header>.content .top>div:not(.toggle) li,header>.content .top ul li {
	        padding-left: 15px;
	        line-height: 50px;
	        cursor: pointer;
	        border-bottom: 1px solid #e6e6e6
	    }
	    header>.content .top ul li.sign-in.sign-in-dropdown {
	        border-bottom: 0
	    }
	    header>.content .top ul li.sign-in.sign-in-dropdown a#socialLogin {
	        font-size: 14px;
	        font-weight: 400
	    }
	    header>.content .top>div:not(.toggle) li.wishlist .wishlist-info,header>.content .top>div:not(.toggle) li.sign-in .sign-in-info,header>.content .top ul li.wishlist .wishlist-info,header>.content .top ul li.sign-in .sign-in-info {
	        max-height: 0;
	        overflow: hidden;
	        -webkit-transition: max-height .3s;
	        -moz-transition: max-height .3s;
	        transition: max-height .3s
	    }
	    header>.content .top>div:not(.toggle) li.wishlist .wishlist-info,header>.content .top ul li.wishlist .wishlist-info {
	        display: block;
	        overflow: hidden;
	        padding: 0 25px;
	        line-height: 25px;
	        -webkit-transition: max-height .3s, z-index 0 .3s;
	        -moz-transition: max-height .3s, z-index 0 .3s;
	        transition: max-height .3s, z-index 0 .3s
	    }
	    header>.content .top>div:not(.toggle) li.sign-in .sign-in-info,header>.content .top ul li.sign-in .sign-in-info {
	        display: block;
	        padding: 0;
	        overflow: hidden;
	        -webkit-transition: max-height .3s, padding .3s, z-index 0 .3s;
	        -moz-transition: max-height .3s, padding .3s, z-index 0 .3s;
	        transition: max-height .3s, padding .3s, z-index 0 .3s
	    }
	    header>.content .bottom::after {
	        clear: both;
	        content: "";
	        display: table
	    }
	    header>.content .bottom .marketplace span {
	        text-transform: none;
	        font-family: "Montserrat"
	    }
	    header>.content .bottom .bag {
	        display: none;
	        position: absolute;
	        top: 0;
	        right: 15px;
	        height: 60px;
	        line-height: 60px;
	        vertical-align: middle
	    }
	    header>.content .bottom .bag a {
	        float: left;
	        font-size: 20px
	    }
	    header>.content .bottom nav::after {
	        clear: both;
	        content: "";
	        display: table
	    }
	    header>.content .bottom nav>ul>li {
	        max-width: 50%;
	        font-size: 20px;
	        font-size: 12px;
	        font-weight: 500;
	        width: 50%;
	        padding: 20px 0
	    }
	    header>.content .bottom nav>ul>li:nth-of-type(1) {
	        float: left
	    }
	    header>.content .bottom nav>ul>li:nth-last-of-type(1) {
	        float: right;
	        text-align: right
	    }
	    header>.content .bottom nav>ul>li:nth-last-of-type(1)>.toggle {
	        width: auto
	    }
	    header>.content .bottom nav>ul>li .toggle {
	        background-color: #fff;
	        z-index: 0;
	        font-size: 14px;
	        position: relative;
	        width: 100%;
	        left: 0;
	        -webkit-transition: width .3s, z-index 0 .3s, left .3s;
	        -moz-transition: width .3s, z-index 0 .3s, left .3s;
	        transition: width .3s, z-index 0 .3s, left .3s
	    }
	    header>.content .bottom nav>ul>li>.toggle span {
	        text-transform: uppercase
	    }
	    header>.content .bottom nav>ul>li .toggle:after {
	        font-family: 'FontAwesome';
	        -webkit-font-smoothing: antialiased;
	        -moz-osx-font-smoothing: grayscale;
	        content: "\f105";
	        font-size: 19px;
	        padding-left: 5px;
	        font-weight: lighter;
	        color: #000;
	        position: relative;
	        top: 2px
	    }
	    header>.content .bottom nav>ul>li .toggle:before {
	        content: "close";
	        font-weight: 400;
	        display: inline-block;
	        opacity: 0;
	        position: absolute;
	        right: 0;
	        top: 3px;
	        -webkit-transition: opacity .15s;
	        -moz-transition: opacity .15s;
	        transition: opacity .15s
	    }
	    header>.content .bottom nav>ul>li>ul {
	        position: absolute;
	        overflow: hidden;
	        max-height: 0;
	        background: #fff;
	        z-index: 2;
	        left: 0;
	        width: 100%;
	        padding: 0 15px;
	        text-align: left;
	        font-weight: 400;
	        line-height: 30px;
	        -webkit-transition: max-height .3s;
	        -moz-transition: max-height .3s;
	        transition: max-height .3s
	    }
	    header>.content .bottom nav>ul>li>ul>li {
	        padding: 0 15px
	    }
	    header>.content .bottom nav>ul>li>ul>li:first-child {
	        padding-top: 10px
	    }
	    header>.content .bottom nav>ul>li>ul>li:last-child {
	        padding-bottom: 10px
	    }
	    header>.content .bottom nav>ul>li>ul>li .toggle:before {
	        line-height: 38px
	    }
	    header>.content .bottom nav>ul>li>ul li.long.words>.toggle:before {
	        display: none
	    }
	    header>.content .bottom nav>ul>li>ul>li>ul {
	        max-height: 0;
	        overflow: hidden;
	        font-size: 14px;
	        padding-left: 20px
	    }
	    header>.content .bottom nav>ul>li>ul>li>ul>li {
	        overflow: hidden;
	        padding-bottom: 0!important;
	        -webkit-transition: max-height .3s;
	        -moz-transition: max-height .3s;
	        transition: max-height .3s
	    }
	    header>.content .bottom nav>ul>li>ul>li>ul>li.images::after {
	        clear: both;
	        content: "";
	        display: table
	    }
	    header>.content .bottom .search form {
	        display: inline-block;
	        margin: 0 auto;
	        width: 100%;
	        position: relative
	    }
	    header>.content .bottom .search form::after {
	        clear: both;
	        content: "";
	        display: table
	    }
	    header>.content .bottom .search form button,
	    header>.content .bottom .search form select,
	    header>.content .bottom .search form input {
	        display: inline-block;
	        float: left
	    }
	    header>.content .bottom .search form button {
	        position: relative;
	        width: 55px;
	        border: 1px solid #AAA;
	        background-color: #fff;
	        float: right;
	        border-left: 0
	    }
	    header>.content .bottom .search form button:before {
	        font-size: 18px;
	        color: #a9143c;
	        position: absolute;
	        top: 50%;
	        left: 50%;
	        -webkit-transform: translateX(-50%) translateY(-50%);
	        -moz-transform: translateX(-50%) translateY(-50%);
	        -ms-transform: translateX(-50%) translateY(-50%);
	        -o-transform: translateX(-50%) translateY(-50%);
	        transform: translateX(-50%) translateY(-50%)
	    }
	    header>.content .bottom .search form select {
	        border-right: 0;
	        border-width: 1px;
	        border-color: #AAA;
	        height: 40px;
	        font-size: 12px;
	        font-weight: 500;
	        width: -webkit-calc(100% - 65%);
	        width: calc(100% - 65%);
	        padding-right: 20px
	    }
	    header>.content .bottom .search form input {
	        border: 1px solid #AAA;
	        border-left: 1px solid #AAA;
	        height: 40px;
	        font-size: 12px;
	        line-height: 24px;
	        border-radius: 0;
	        width: -webkit-calc(65% - 40px);
	        width: calc(65% - 40px);
	        background-color: #fff
	    }
	}
	
	@media (max-width:650px) {
	    header>.content .bottom .mobile-bag.bag {
	        display: block;
	        position: absolute;
	        right: 15px
	    }
	    header>.content .bottom .mobile-bag.bag a {
	        text-transform: capitalize;
	        font-size: 16px
	    }
	}
	
	@media (max-width:375px) {
	    header>.content .bottom .search form select {
	        width: -webkit-calc(100% - 205px);
	        width: calc(100% - 205px);
	        padding-left: 5px
	    }
	}
			
	.content b {
	    font-weight: 600
	}
	
	.select-view .select-list li {
	    width: -webkit-calc(100% - -4px);
	    width: calc(100% - -4px);
	    height: auto!important;
	    background-color: #fff;
	    line-height: 46px;
	    padding-left: 15px;
	    position: relative;
	    border: 2px solid transparent;
	    margin: 0 -2px -2px;
	    white-space: nowrap;
	    text-overflow: ellipsis;
	    overflow: hidden;
	    color: #666
	}
	
	@media (min-width:791px) {
	    .marketplace img {
	        width: 100%
	    }
	    header .content nav>ul>li:nth-of-type(1)>ul {
	        z-index: 99999
	    }
	    header .content nav>ul>li:nth-of-type(2)>ul {
	        z-index: 99999
	    }
	}
	
	@media (max-width:425px) {
	    .bottom nav ul li .toggle {
	        font-size: 13px
	    }
	    .marketplace img {
	        height: 48px
	    }
	}
	
	@media (min-width:315px) and (max-width:375px) {
	    header>.content .bottom .search form select {
	        width: -webkit-calc(100% - 65%);
	        width: calc(100% - 65%);
	        padding-left: 5px
	    }
	}
	
	@media (min-width:791px) and (max-width:900px) {
	    header .content nav>ul>li:nth-of-type(1)>ul {
	        left: 185px
	    }
	    header .content nav>ul>li:nth-of-type(2)>ul {
	        left: 365px
	    }
	}
	
	header .content .right>ul>li.logIn-hi {
	    padding: 0
	}
	
	@media (max-width:790px) {
	    header .content .bottom .marketplace {
	        font-size: 23px
	    }
	}
	
	ul#ui-id-1 {
	    position: absolute!important;
	    background: #fff!important;
	    z-index: 100;
	    border-top: 0
	}
	
	@media (min-width:791px) {
	    ul#ui-id-1 {
	        max-height: 500px;
	        overflow: auto
	    }
	}
	
	.words li.long .toggle a {
	    text-transform: capitalize!important
	}
	
	@media (min-width:791px) {
	    .form-control {
	        -webkit-box-shadow: none!important;
	        box-shadow: none!important
	    }
	    .form-control:focus {
	        outline: 0;
	        -webkit-box-shadow: none!important;
	        box-shadow: none!important
	    }
	    header .content .right .sign-in-dropdown {
	        margin-right: 0!important
	    }
	}
	
	@media (min-width:791px) {
	    header .content .compact-toggle.mobile {
	        display: none
	    }
	    
	    header .content .marketplace.compact a {
	        position: relative;
	        opacity: 0;
	        z-index: -1;
	        -webkit-transition: opacity .3s, z-index 0 .3s;
	        -moz-transition: opacity .3s, z-index 0 .3s;
	        transition: opacity .3s, z-index 0 .3s
	    }
	    header .content .marketplace.compact {
	        margin: 0;
	        position: absolute;
	        width: 100%;
	        height: 50px;
	        line-height: 50px;
	        text-align: center;
	        margin-left: 0
	    }
	    header .content .marketplace.compact a img {
	        width: 130px;
	        margin-top: 7px
	    }
	}
	
	@media (max-width:790px) {
	    header>.content .bottom .search form input {
	        border-left: 1px solid #AAA
	    }
	    header {
	        margin-top: 0;
	        position: relative;
	        -webkit-transition: margin .3s;
	        -moz-transition: margin .3s;
	        transition: margin .3s
	    }
	    header>.content {
	        top: 0
	    }
	    header>.content .top .marketplace.compact {
	        display: none
	    }
	    header>.content .bottom .mobile-bag.bag {
	        display: block;
	        position: absolute;
	        right: 15px
	    }
	    header>.content .compact-toggle {
	        position: absolute;
	        width: 50px;
	        height: 20px;
	        bottom: -9px;
	        left: 50%;
	        margin-left: -25px;
	        background-color: #fff;
	        z-index: 1;
	        border-radius: 50px/20px;
	        cursor: pointer;
	        display: none;
	        box-shadow: 0 4px 4px 0 #f0f0f0;
	        -webkit-transition: opacity .3s;
	        -moz-transition: opacity .3s;
	        transition: opacity .3s
	    }
	    header>.content .compact-toggle:before {
	        content: "";
	        display: inline-block;
	        position: relative;
	        left: 50%;
	        margin-left: -4px;
	        top: 0;
	        height: 0;
	        width: 0;
	        border-left: 4px solid transparent;
	        border-right: 4px solid transparent;
	        border-top: 4px solid #a9143c
	    }
	    header>.content .bottom {
	        -webkit-transition: max-height .3s;
	        -moz-transition: max-height .3s;
	        transition: max-height .3s
	    }
	    header>.content .bottom .search {
	        width: 100%;
	        float: left;
	        text-align: center
	    }
	    header>.content .bottom .marketplace {
	        text-align: center;
	        padding: 10px 25px;
	        vertical-align: middle;
	        font-size: 24px;
	        text-transform: uppercase;
	        height: 60px
	    }
	}
	
	@media (max-width:855px) {
	    header .content .right>ul>li {
	        padding: 0 10px
	    }
	    header .content .search form {
	        margin: 12px 16px 0 0
	    }
	}
	
	@media (min-width:856px) and (max-width:960px) {
	    header .content .right>ul>li {
	        padding: 0 15px
	    }
	}
	
	@media (max-width:1080px) {
	    header .content .marketplace.compact {
	        -webkit-transition: padding .15s;
	        -moz-transition: padding .15s;
	        transition: padding .15s
	    }
	}
	
	.select-list ul li {
	    font-size: 13px
	}
	
	.selected {
	    overflow: hidden;
	    text-overflow: ellipsis;
	    white-space: nowrap
	}
	
	@media (max-width:650px) {
	    .modal#createNewList .content {
	        height: auto
	    }
	}
	
	.create-new-wishlist-popup-container * {
	    float: left
	}
	
	.create-new-wishlist-popup-container {
	    display: block;
	    margin: 0 auto;
	    padding: 30px 20px 45px;
	    max-width: 700px;
	    text-align: center;
	    clear: both
	}
	
	.create-new-wishlist-popup-container label {
	    width: 100%;
	    text-align: left;
	    margin-bottom: 6px;
	    line-height: 12px;
	    font-weight: 500
	}
	
	.create-new-wishlist-popup-container input {
	    height: 30px;
	    width: 100%;
	    margin-bottom: 10px;
	    border: 1px solid #dfd1d5;
	    -webkit-transition: border .3s;
	    -moz-transition: border .3s;
	    transition: border .3s
	}
	
	.create-new-wishlist-popup-container input:focus {
	    border-color: #000;
	    -webkit-transition: border .15s;
	    -moz-transition: border .15s;
	    transition: border .15s
	}
	
	.create-new-wishlist-popup-container a.close {
	    margin-left: 20px;
	    text-decoration: underline;
	    display: inline-block;
	    float: none;
	    font-size: 12px;
	    font-weight: 400
	}
	
	.create-new-wishlist-popup-container .create_wishlist#CreateNewWishlist {
	    font-size: 12px;
	    font-weight: 500;
	    background-color: #a9143c;
	    border: 2px solid #a9143c;
	    height: 40px;
	    line-height: 36px;
	    padding: 0 20px;
	    color: #fff;
	    -webkit-transition: color .3s, background .3s;
	    -moz-transition: color .3s, background .3s;
	    transition: color .3s, background .3s;
	    display: inline-block;
	    float: none;
	    margin: 0;
	    border-color: #a9143c;
	    border-style: solid;
	    border-width: 2px
	}
	
	.create-new-wishlist-popup-container ul,
	.create-new-wishlist-popup-container li {
	    width: 100%;
	    padding: 0!important;
	    border: none!important;
	    font-size: 12px
	}
	
	.create-new-wishlist-popup-container {
	    padding-bottom: 10px
	}
	
	
	li.wishlist #myWishlistHeader {
	    display: block
	}
	
	.ui-front input {
	    overflow: hidden;
	    text-overflow: ellipsis;
	    white-space: nowrap
	}
	
	@media (max-width:790px) {
	    .sign-in-dropdown {
	        padding-left: 0!important
	    }
	    header>.content .bottom nav ul.words li.long .toggle::after {
	        content: '';
	        display: none
	    }
	}
	
	@media (min-width:791px) {
	    header>.content .bottom .marketplace {
	        display: table;
	        margin-top: 0px;
	    }
	    header>.content .bottom .marketplace .simple-banner-component {
	        display: table-cell;
	        vertical-align: middle;
	    }
	}
	
	@media (max-width:650px) {
	    .create-new-wishlist-popup-container .create_wishlist {
	        width: 100%
	    }
	    .create-new-wishlist-popup-container .close {
	        color: #a9143c;
	        margin-left: 0;
	        line-height: 30px;
	        margin-bottom: 20px
	    }
	    #createNewList button.close {
	        display: none
	    }
	}
	
	#mobile-menu-toggle {
	    display: none
	}
	
	@media (max-width:790px) {
	#mobile-menu-toggle {
    display: block;
    }
	}
	
	@media (max-width:790px) {
	    header .content .right>ul>li.logIn-hi {
	        border-bottom: 0;
	        padding: 0
	    }
	    header .content .right>ul>li.logIn-hi>div {
	        padding-left: 15px;
	        padding-right: 10px;
	        border-bottom: 1px solid #e6e6e6
	    }
	}
	
	@media (max-width:650px) {
	    header>.content .top .toggle span {
	        width: calc(100% - 30px);
	        height: 2px
	    }
	    header>.content .bottom .bag a {
	        font-size: 14px
	    }
	    header>.content .bottom .bag {
	        top: -5px
	    }
	}
	
	@media (min-width:426px) and (max-width:790px) {
	    .marketplace img {
	        height: 48px
	    }
	}
	li.download-app a:before {
	    margin-right: 5px;
	    top: 2px;
	    position: relative;
	    content: "";
	    background: url(${siteRootUrl}/theme-blue/images/Sprite-combined.png) no-repeat scroll -296px -175px;
	    display: inline-block;
	    width: 23px;
	    height: 23px;
	    opacity: .7
	}
	
	span.material-icons {
	    display: inline-block;
	    vertical-align: middle;
	    margin-right: -2px;
	    content: "";
	    background: url(${siteRootUrl}/theme-blue/images/Sprite-combined.png) no-repeat scroll -352px -176px;
	    width: 23px;
	    height: 23px;
	    opacity: .7
	}
	
	@media (min-width:1081px) {
	    header .content nav>ul>li>.toggle {
	        letter-spacing: 1.5px;
	        white-space: nowrap
	    }
	}
	
	header div.bottom .marketplace.linear-logo {
	    display: none
	}
	
	@media (max-width:790px) {
	    header div.bottom .marketplace {
	        display: none
	    }
	    header div.bottom .marketplace.linear-logo {
	        display: block
	    }
	}
	
	@media (min-width:791px) {
	    header .content nav>ul>li>.toggle>span:first-child {
	        font-size: 11px;
	        line-height: 15px;
	        height: 26px;
	        font-weight: 500;
	        padding-top: 9px;
	        display: block;
	        letter-spacing: .6px;
	        color: #666
	    }
	    header .content nav>ul>li>.toggle>span:last-child {
	        font-size: 13px;
	        line-height: 13px;
	        font-weight: 600;
	        letter-spacing: .6px
	    }
	}
	
	#errorCreate {
	    line-height: normal;
	    color: #ff1c47
	}
	
	#movedToWishlist_Cart {
	    position: absolute;
	    line-height: 15px;
	    bottom: -40px;
	    width: 190px;
	    left: 0;
	    z-index: 10;
	    background: #fafafa;
	    height: 40px;
	    padding: 5px
	}
	
	header .content .search input {
	    background: #fff;
	    cursor: text
	}
	
	@media (max-width:790px) {
	    li.download-app a:before {
	        margin-right: 9px
	    }
	    span.material-icons {
	        margin-left: -4px
	    }
	}
	
	@media (max-width:767px) {
	    select#enhancedSearchCategory {
	        background-position: 87% 51%
	    }
	    header>.content .bottom .mobile-bag.bag:before {
	        right: -12px!important
	    }
	    header>.content .bottom .mobile-bag.bag a {
	        right: -1px!important
	    }
	    header>.content .bottom .search form input {
	        font-size: 12px;
	        padding: 0 5px 0 8px
	    }
	}
	
	@media (max-width:790px) {
	    header>.content .top .toggle span:first-child {
	        top: 18px
	    }
	    header>.content .top .toggle span {
	        border-radius: 0;
	        height: 3px;
	        width: 20px
	    }
	    header>.content .top .toggle span:last-child {
	        top: 32px
	    }
	    header>.content {
	        padding: 0 23px 30px
	    }
	    header>.content .bottom .marketplace {
	        padding: 3px 25px 0
	    }
	    header>.content .bottom .mobile-bag.bag {
	        top: -6px;
	        right: 8px
	    }
	    header>.content .bottom .mobile-bag.bag:before {
	        content: "";
	        top: 18px;
	        width: 24px;
	        height: 25px;
	        display: inline-block;
	        position: absolute;
	        right: -3px
	    }
	    header>.content .bottom .marketplace span {
	        color: #fff;
	        font-size: 11px;
	        left: -2px;
	        line-height: 14px;
	        position: absolute;
	        text-align: center;
	        text-transform: none;
	        top: 0;
	        width: 26px
	    }
	    header>.content .bottom .mobile-bag.bag a {
	        width: 26px;
	        height: 19px;
	        background: #a8103d;
	        border: 2px solid #fff;
	        border-radius: 10px;
	        display: inline-block;
	        font-size: 16px;
	        position: absolute;
	        right: 8px;
	        text-transform: capitalize;
	        top: 27px
	    }
	    header>.content .bottom .search form button:before {
	        content: "";
	        display: inline-block;
	        height: 18px;
	        width: 18px
	    }
	    select#enhancedSearchCategory {
	        background-position: 92% 51%;
	        background-image: url(${siteRootUrl}/theme-blue/images/select-arrow-new.png)
	    }
	    .select-view select {
	        padding: 8px 37px 8px 5px !important;
	        line-height: 1
	    }
	    nav {
	        display: none
	    }
	    .words>li:first-child,
	    .top .container>.right>ul:first-child,
	    .top .container>.right>ul:first-child>li>div>span:first-child,
	    .top .container>.right>ul:first-child ul.a-z {
	        display: none
	    }
	    header>.content .top>div:not(.toggle) ul:first-child li,
	    header>.content .top ul:first-child li {
	        padding: 0;
	        position: relative
	    }
	    header>.content .top ul:first-child li>div,
	    header>.content .top ul:first-child li.short.images div {
	        padding-left: 15px
	    }
	    header>.content .top ul:first-child li.short.images div.view_brands {
	        display: none
	    }
	}
	
	.top .container>.right>ul:first-child,
	.top .container>.right>ul:first-child>li>div>span:first-child,
	.top .container>.right>ul:first-child ul.a-z {
	    display: none
	}
	
	@media (max-width:479px) {
	    select#enhancedSearchCategory {
	        display: none
	    }
	    header>.content .bottom .search form input {
	        width: calc(100% - 40px)
	    }
	}
	
	header .bottom-header-wrapper, header.brand-header .microsite_bottom_wrapper {
	    position: relative
	}
	
	@media (max-width:790px) and (min-width:768px) {
	    header>.content .bottom .mobile-bag.bag {
	        right: 0
	    }
	    header>.content .top .toggle {
	        right: -51px
	    }
	}		
	.form-control::-moz-placeholder {
	    color: #777;
	    opacity: 1
	}
	
	.form-control:-ms-input-placeholder {
	    color: #777
	}
	
	.form-control::-webkit-input-placeholder {
	    color: #777
	}
	
	.fade {
	    opacity: 0;
	    -webkit-transition: opacity .15s linear;
	    -o-transition: opacity .15s linear;
	    transition: opacity .15s linear
	}
	
	.dropdown {
	    position: relative;
	    font-family: 'Montserrat';
	    font-size: 12px;
	    font-weight: 500;
	    cursor: pointer
	}
	
	button.close {
	    padding: 0;
	    cursor: pointer;
	    background: transparent;
	    border: 0;
	    -webkit-appearance: none
	}	
	
	.container:after,
	.row:after {
	    clear: both
	}
	
	.pull-right {
	    float: right!important
	}
	
	.mini-cart-link {
	    float: none;
	    text-align: center;
	    display: block;
	    color: #fff;
	    white-space: nowrap;
	    position: relative
	}
	
	@media (max-width:1023px) {
	    .mini-cart-link {
	        height: 40px
	    }
	}
	
	.simple-banner-component a {
	    display: inline-block
	}
	
	.container {
	    padding: 0!important;
	    max-width: 1400px;
	    margin: 0 auto;
		width: 100%!important;
	}
	
	@media (min-width:601px) and (max-width:767px) {
	    .simple-banner-component {
	        text-align: center
	    }
	}
	@media (max-width:790px) {
	    header .content .bottom .bag .transient-mini-bag {
	        display: none
	    }
	}
	
	.simple-banner-component img {
	    max-width: 100%
	}
	
	.create-new-wishlist-popup-container h2 {
	    font-size: 24px;
	    margin-bottom: 15px;
	    line-height: 24px;
	    width: 100%;
	    float: right
	}
	
	@media (max-width:790px) {
	    .create-new-wishlist-popup-container h2 {
	        font-size: 22px
	    }
	}
	
	@media (max-width:790px) {
	    header>.content .top {
	        z-index: 10002
	    }
	    header>.content .bottom {
	        background-color: #fff
	    }
	}
	
	#flip-tabs {
	    width: 340px;
	    position: absolute;
	    z-index: 1
	}
	
	#flip-navigation li {
	    display: inline-block;
	    width: 170px;
	    margin: 0;
	    vertical-align: top;
	    float: left;
	    background: #000
	}
	
	#flip-navigation li a {
	    text-decoration: none;
	    padding: 11px 20px;
	    margin-right: 0;
	    font-family: montserrat;
	    outline: 0;
	    text-transform: uppercase;
	    width: 100%;
	    display: block;
	    text-align: center;
	    color: #fff;
	}
	
	@media (max-width:991px) {
	    #flip-navigation li a {
	        padding: 11px 10px;
	        font-size: 11px
	    }
	}
	
	@media (min-width:791px) {
	    li.download-app a:before,
	    span.material-icons,
	    header .content .right>ul>li.wishlist>a:before {
	        color: #fff
	    }
	    header .content .marketplace.compact a img {
	        margin-top: 24px
	    }
	}
	
	@media (max-width:790px) {
	    header>.content .top .toggle {
	        margin-top: 32px
	    }
	    #flip-tabs {
	        width: calc(100% - -46px);
	        overflow: hidden;
	        left: -23px;
	        position: relative
	    }
	    #flip-navigation li {
	        width: 50%;
	        float: left
	    }
	    #flip-navigation li a {
	        width: 100%;
	        line-height: 15px
	    }
	}
	
	@media (max-width:767px) {
	    #flip-tabs {
	        width: calc(100% - -24px);
	        left: -12px
	    }
	}
	
	@media (max-width:1140px) {
	    header .content .right {
	        clear: left
	    }
	}
	
	#flip-navigation li:first-child a {
	    background: #fff;
	    color: #a9143c
	}
	
	header>.content .bottom .marketplace .simple-banner-component img {
	    max-height: 45px
	}
	
	header.marketplace-header {
	    visibility: visible
	}
	
	@media (min-width:791px) {
	    header {
	        border-bottom: solid 1px #f2f2f2;
	    }
	}
	
	@media (max-width:790px) {
	    header .content #flip-tabs ~ .bottom .bottom-header-wrapper .search form#search_form[name='search_form'] {
	        display: none
	    }
	}
	
	@media (max-width:790px) {
	    #shopMicrositeSeller {
	        position: relative;
	        cursor: pointer
	    }
	}
	
	header>.content .bottom .marketplace .simple-banner-component img {
	    width: auto
	}
	
	#flip-navigation li a,
	#flip-navigation li.selected a {
	    font-size: 11px;
	    letter-spacing: .6px;
	    line-height: 14px;
	    font-weight: 400
	}
	
	li.download-app a:before {
	    font-size: 22px;
	    line-height: 22px;
	    top: 4px
	}
	
	.select-view .select-list {
	    padding-top: 39px
	}
	
	.select-view .select-list li {
	    line-height: 38px;
	    padding-left: 10px
	}
	
	.select-view .select-list>.selected,
	.select-list ul li {
	    font-size: 12px;
	    letter-spacing: .6px
	}
	
	header .content .right>ul>li>a#socialLogin {
	    position: relative;
	    top: 1px
	}
	
	header .content .right>ul>li.wishlist #myWishlistHeader {
	    position: relative;
	    top: 1px
	}
	
	@media (max-width:990px) and (min-width:791px) {
	    header .content .search .select-list {
	        width: 75px
	    }
	    .select-view .select-list>.selected:after {
	        right: 2px
	    }
	    header .content .search {
	        margin-left: 0
	    }
	    header .content .search input {
	        width: -webkit-calc(100% - 135px);
	        width: calc(100% - 135px)
	    }
	}
	
	@media (max-width:790px) {
	    #flip-navigation li.selected {
	        border-bottom: 1px solid #000
	    }
	    #flip-navigation li a {
	        padding: 4px 10px 4.5px
	    }
	    #flip-navigation li.selected a {
	        padding: 4px 10px 3.5px
	    }
	    header>.content .bottom .marketplace.linear-logo .simple-banner-component {
	        text-align: left
	    }
	    header>.content .bottom .marketplace .simple-banner-component img {
	        width: auto;
	        height: 24px
	    }
	    header>.content .bottom .marketplace {
	        padding-left: 43px;
	        padding-top: 22.5px;
	        height: 62px
	    }
	    header>.content .bottom .search form button {
	        width: 62px;
	        background: #f8f8f8
	    }
	    header>.content .bottom .search form input {
	        width: -webkit-calc(65% - 62px);
	        width: calc(65% - 62px)
	    }
	    header>.content .bottom .search form button,
	    header>.content .bottom .search form input,
	    header>.content .bottom .search form select {
	        height: 42px
	    }
	    header>.content .bottom .mobile-bag.bag {
	        top: 3px
	    }
	    header>.content .bottom .mobile-bag.bag:before {
	        background: url(${siteRootUrl}/theme-blue/images/Sprite-combined.png) no-repeat scroll -383px -145px
	    }
	    header>.content .bottom .mobile-bag.bag a {
	        width: 26px;
	        background: #000
	    }
	    header>.content .bottom .search form button:before {
	        background: url(${siteRootUrl}/theme-blue/images/Sprite-combined.png) no-repeat scroll -300px -207px
	    }
	    header>.content .top>div:not(.toggle) li.download-app {
	        padding-left: 8px
	    }
	    span.material-icons {
	        margin-right: 2px
	    }
	    header .content .right>ul>li.wishlist>a:before {
	        margin-right: 8px
	    }
	}
	
	@media (max-width:790px) and (min-width:768px) {
	    header>.content .top .toggle {
	        right: -64px
	    }
	}
	
	@media (max-width:767px) {
	    header>.content {
	        padding: 0 10px 10px!important
	    }
	    .select-view .select-list {
	        border: 1px solid #bbb
	    }
	    .select-view .select-list>.selected:after {
	        right: 16px
	    }
	    .select-view .select-list>.selected {
	        padding-left: 16px;
	        padding-right: 35px
	    }
	}
	
	@media (max-width:479px) {
	    header>.content .bottom .search form input {
	        width: -webkit-calc(100% - 62px);
	        width: calc(100% - 62px)
	    }
	}
	
	@media (min-width:1101px) {
	    header .content .search form {
	        margin: 12px 16px 0 0
	    }
	    header .content .search {
	        margin-left: 85px
	    }
	     header .content .right>ul>li:first-of-type {
	        margin-right: 17px
	    }
	    header .content .bottom .bag, .brand-sub header .content .bottom .bag {
	        float: right;
	        margin-right: 30px
	    }
		header .content nav>ul>li {
    		margin-right: 51px;
		}
	}
	
	header>.content .top ul a[href="/helpservices"] {
	    display: none
	}
	
	.error_text {
	    color: red
	}	
	
	#showTrackOrder {
	    display: none;
	    margin: 0 auto;
	    width: 100%;
	    position: fixed;
	    top: 100px;
	    left: 0
	}
	
	.error_text {
	    font-color: #a9143c;
	    display: none;
	    width: 100%;
	    text-align: center
	}
	
	#showTrackOrder .modal-dialog {
	    background: #fff;
	    border-radius: 5px;
	    padding: 10px 10px 50px;
	    width: 653px
	}
	
	#showTrackOrder .modal-dialog .trackPopupText {
	    color: #333;
	    font-size: 10px;
	    margin-top: 10px
	}
	
	#showTrackOrder .modal-dialog .viewOrderButton {
	    font-size: 12px;
	    background: #a9143c;
	    color: #fff;
	    margin-top: 10px;
	    float: right
	}
	
	#showTrackOrder .modal-dialog .col-md-6,
	#showTrackOrder .modal-dialog .col-md-12,
	#showTrackOrder .modal-dialog .col-md-5 {
	    margin: 0;
	    padding: 0 10px 0 0
	}
	
	#showTrackOrder .modal-dialog label {
	    font-weight: 700;
	    padding-bottom: 5px;
	    margin-top: 14px
	}
	
	#showTrackOrder .modal-dialog input[type="text"] {
	    width: 100%
	}
	
	#showTrackOrder .modal-dialog h4 {
	    width: 95%;
	    font-size: 24px
	}
	
	#showTrackOrder .modal-dialog .close {
	    float: right;
	    border-radius: 50%;
	    width: 30px;
	    height: 30px;
	    border: 1px solid #ccc;
	    margin-top: 18px!important;
	    margin-right: -10px!important
	}
	
	@media screen and (max-width:620px) {
	    #showTrackOrder .modal-dialog {
	        width: auto!important
	    }
	}
	
	h4 {
	    font-size: 18px!important;
	    font-weight: bolder!important;
	    margin-bottom: 15px!important
	}
	
	.create-new-wishlist-popup-container a.close {
	    border-radius: none;
	    border: 0!important;
	    width: auto;
	    height: auto;
	    margin-top: 0!important;
	    margin-right: 0!important;
	    position: relative;
	    right: 0;
	    top: 0
	}
	
	header>.content .bottom .marketplace .simple-banner-component img {
	    width: auto
	}
	
	li.download-app a:before {
	    top: 4px
	}
	
	.select-view .select-list {
	    padding-top: 39px
	}
	
	.select-view .select-list li {
	    line-height: 38px;
	    padding-left: 10px
	}
	
	.select-view .select-list>.selected,
	.select-list ul li {
	    font-size: 12px;
	    letter-spacing: .6px
	}
	
	header .content .right>ul>li>a#socialLogin {
	    position: relative;
	    top: 1px
	}
	
	header .content .right>ul>li.wishlist #myWishlistHeader {
	    position: relative;
	    top: 1px
	}
	
	@media (min-width:791px) {
	    header .content .top {
	        background: #a9133d;
	        min-height: 36px
	    }
	    #flip-navigation li {
	        width: 150px;
	        font-size: 11px;
	        letter-spacing: .6px
	    }
	    header .content nav {
	        margin-left: 26px;
	        line-height: 64px;
	        height: 64px
	    }
	    header .content .right>ul>li>a {
	        letter-spacing: .6px;
	        color: #eee
	    }
	    header .content nav>ul>li>.toggle>span:first-child {
	        height: auto;
	        padding-top: 0
	    }
	    header .content .search input,
	    header .content .search .select-list,
	    .select-view .select-list>.selected,
	    header .content .search button {
	        height: 42px;
	        line-height: 42px
	    }
	    header .content nav>ul>li>.toggle {
	        height: 50px;
	        padding-bottom: 0
	    }
	    header .content nav>ul>li>ul {
	        top: 64px
	    }
	    header .content nav>ul>li:first-child>ul,
	    header .content nav>ul>li:first-child+li>ul {
	        left: 0;
	        width: 217px
	    }
	    header .content .search {
	        float: left
	    }
	    header .content .search .select-list {
	        width: 176px
	    }
	    header .content .search button {
	        width: 62px;
	        background: #f8f8f8
	    }
	    header .content .search input {
	        width: -webkit-calc(100% - 236px);
	        width: calc(100% - 236px)
	    }
	    header .content .right>ul>li.sign-in .sign-in-info {
	        background-color: #fff;
	        box-shadow: 0 1px 1px 0 rgba(0, 0, 0, 0.35), 0 1px 2px 0 rgba(0, 0, 0, 0.25)
	    }
	    header .content nav>ul>li>.toggle {
	        position: relative
	    }
	    header .content nav>ul>li>.toggle:before {
	        content: "";
	        width: 20px;
	        height: 12px;
	        position: absolute;
	        bottom: 3px;
	        left: 16px;
	        z-index: 999999;
	        background-image: url(${siteRootUrl}/theme-blue/images/Sprite-combined.png);
	        background-position: -197px -376px;
	        background-repeat: no-repeat;
	        background-color: transparent;
	        opacity: 0
	    }
	    header .content nav>ul>li>.toggle.shop_dept:before {
	        left: 41px
	    }
	    header .content nav>ul>li:first-child>ul,
	    header .content nav>ul>li:first-child+li>ul {
	        border-top: 1px solid #eee
	    }
	    header .content .right>ul>li {
	        padding: 0 13px
	    }
	}
	
	@media (max-width:990px) and (min-width:791px) {
	    header .content .search .select-list {
	        width: 75px
	    }
	    .select-view .select-list>.selected:after {
	        right: 2px
	    }
	    header .content .search {
	        margin-left: 0
	    }
	    header .content .search input {
	        width: -webkit-calc(100% - 135px);
	        width: calc(100% - 135px)
	    }
	}
	
	@media (max-width:790px) and (min-width:768px) {
	    header>.content .top .toggle {
	        right: -64px
	    }
	}
	
	@media (max-width:479px) {
	    header>.content .bottom .search form input {
	        width: -webkit-calc(100% - 62px);
	        width: calc(100% - 62px)
	    }
	}
	
	header>.content .top ul a[href="/helpservices"] {
	    display: none
	}
	
	@media (max-width:1140px) and (min-width:791px) {
	    header .content .left,
	    header .content .right>ul>li.download-app,
	    header .content .right>ul>li.track_order_header {
	        display: none
	    }
	}
	
	@media (max-width:790px) {
	    header .content #flip-tabs ~ .bottom .bottom-header-wrapper .search form#search_form[name='search_form'] {
	        display: block
	    }
	    .searchButtonGlobal {
	        display: none
	    }
	}
	
	@media (min-width:791px) {
	    header .content .right>ul>li.track_order_header {
	        line-height: 37px
	    }
	}
	
	header.marketplace-header .content .top .container>.right>ul.headerUl {
	    display: block
	}
	
	.product-info>div.wishAddLogin,.product-info>div.wishAddSucess,.product-info>div.wishAddLogin,.product-info>div.wishRemoveSucess,.product-info>div.wishAlreadyAdded,div.wishAlreadyAddedPlp,div.wishAddLoginPlp,div.wishAddSucessPlp,div.wishRemoveSucessPlp,div.wishAddSucessQV,div.wishAddLoginQV,div.wishAlreadyAddedQV {
	    opacity: 0;
	    position: fixed;
	    top: -100px;
	}
	
	body {
	    margin: 0;
	    padding: 0;
	}
	
	.shop-promos {
	    clear: both;
	}
	
	.shop-promos .promos {
	    clear: both
	}
	
	.shop-promos .promos li {
	    padding: 20px 0;
	    position: relative
	}
	
	.shop-promos .promos li img,
	.shop-promos .promos li:before {
	    position: absolute;
	    left: 50px;
	    top: 35%;
	    max-width: 95px;
	    -webkit-transform: translateX(-50%) translateY(-50%);
	    -moz-transform: translateX(-50%) translateY(-50%);
	    -ms-transform: translateX(-50%) translateY(-50%);
	    -o-transform: translateX(-50%) translateY(-50%);
	    transform: translateX(-50%) translateY(-50%)
	}
	
	.shop-promos .promos li span,
	.shop-promos .promos li span:last-child {
	    padding-left: 100px;
	    display: block
	}
	@media (min-width:651px) {
	    .shop-promos .promos::after {
	        clear: both;
	        content: "";
	        display: table
	    }
	    .shop-promos .promos li {
	        float: left;
	        width: 20%;
	        padding: 80px 0 0
	    }
	    .shop-promos .promos li img,
	    .shop-promos .promos li:before {
	        position: absolute;
	        left: 50%;
	        top: 50px;
	        -webkit-transform: translateX(-50%) translateY(-50%);
	        -moz-transform: translateX(-50%) translateY(-50%);
	        -ms-transform: translateX(-50%) translateY(-50%);
	        -o-transform: translateX(-50%) translateY(-50%);
	        transform: translateX(-50%) translateY(-50%)
	    }
	    .shop-promos .promos li:before {
	        margin-left: 10px
	    }
	    .shop-promos .promos li span,
	    .shop-promos .promos li span:last-child {
	        padding: 18px 0;
	        text-align: center
	    }
	    .shop-promos .promos li span {
	        font-size: 12px;
	        margin-bottom: 0;
	        font-weight: 400
	    }
	    .shop-promos .promos {
	        border-top: 1px solid #ccc
	    }
	    .shop-promos .promos li {
	        border-bottom: 1px solid #ccc;
	        border-right: 1px solid #ccc;
	        padding: 34px 0 0 0;
	        display: table-cell;
	        float: none
	    }
	    .shop-promos .promos li img {
	        top: 30px
	    }
	    .shop-promos .promos {
	        display: table;
	        width: 100%
	    }
	    .shop-promos .promos li span {
	        text-align: center;
	        padding-bottom: 20px;
	        display: table;
	        width: 100%
	    }
	}
	
	.shop-promos {
	    padding: 0
	}
	.shop-promos .promos li span:after {
	    font-family: 'FontAwesome';
	    -webkit-font-smoothing: antialiased;
	    -moz-osx-font-smoothing: grayscale;
	    content: "\f105";
	    font-size: 14px;
	    padding-left: 5px;
	    font-weight: 100;
	    color: #000;
	    position: relative;
	    top: 1px
	}
	
	@media (max-width:767px) {
	    .shop-promos {
	        display: none
	    }
	}
	
	@media (max-width:790px) {
	    .shop-promos .promos {
	        -webkit-box-shadow: 0 1px 5px 0 rgba(214, 211, 214, 1);
	        -moz-box-shadow: 0 1px 5px 0 rgba(214, 211, 214, 1);
	        box-shadow: 0 1px 5px 0 rgba(214, 211, 214, 1);
	        border-top: 0
	    }
	    .shop-promos .promos li span {
	        font-weight: inherit
	    }
	}
	
	.shop-promos .promos li:last-child {
	    border-right: 0
	}
	
	.shop-promos .promos li {
	    border-bottom: 0
	}
	
	.shop-promos .promos {
	    border-right: 1px solid #ccc;
	    border-bottom: 1px solid #ccc;
	    border-left: 1px solid #ccc
	}
	
	@media (min-width:651px) and (max-width:790px) {
	    .shop-promos .promos {
	        border-top: 1px solid #ccc
	    }
	}
	
	/* TPR-628 */
	#rotatingImageMobile, #rotatingImageTimeoutMobile {
    display: none;
	}
	@media (max-width:791px){
	#rotatingImageTimeoutMobile,#rotatingImageMobile{
		display: block;
	}
	#rotatingImageTimeout,#rotatingImage{
		display: none;
	}
	}
	
	.listing.wrapper {
    padding: 0;
	overflow: hidden;
}
.listing.wrapper .search-result {
    width: 100%;
    text-align: center;
    font-size: 14px;
}
.listing.wrapper .search-result h2 {
    font-size: 14px;
    display: inline-block;
    width: 100%;
    text-align: center;
    margin-left: 10px;
    color: #000;
}
.listing.wrapper .search-result h2 span {
    color: #a9143c;
}
.searchString {
    text-transform: capitalize;
}
.listing.wrapper .search-result::after {
    clear: both;
    content: "";
    display: table;
}
.listing.wrapper .left-block {
    width: 19.83%;
    padding: 0 20px 0 18px;
    margin-top: 57px;
}
.listing.wrapper .left-block, .listing.wrapper .right-block {
    display: inline-block;
    float: left;
    width: 100%;
}
.listing.wrapper .right-block {
    float: right;
	width: 80.16%;
    padding: 0 0 0 20px;
}
.facet-list.filter-opt {
    text-align: center;
    margin: 0 auto;
    padding: 30px 0 0 0;
}
#scroll_to_top {
    position: fixed;
    right: 30px;
    bottom: 125px;
    cursor: pointer;
    width: 50px;
    height: 50px;
    border: 2px solid #bbb;
    display: none;
    z-index: 9998;
    background: #fff;
    text-align: center;
    padding-top: 26px;
    color: #999;
    text-transform: uppercase;
    font-size: 10px;
}
.listing.wrapper .right-block .listing-menu {
    padding: 24px 0 10px;
    border-bottom: 0 none;
    margin-bottom: 0;
    margin-top: 0;
	clear: both;
	overflow: hidden;
}
.listing.wrapper .right-block .listing-menu>div {
    display: inline-block;
    width: auto;
    float: right;
    padding: 2px 0;
}
.listing.wrapper .right-block .listing-menu>div.list_title {
    float: right;
    margin-right: 15px;
}
.list_title .UlContainer {
    float: right;
    margin-right: 15px;
    margin-top: 6px;
}
.list_title .UlContainer ul {
    float: left;
    font-size: 12px;
}
.list_title .UlContainer ul:first-of-type {
    margin-right: 10px;
}
.list_title .UlContainer ul>span {
    float: left;
    font-weight: bold;
}
.list_title .UlContainer ul li {
    float: left;
    border-right: 1px solid #666;
    padding: 0 5px;
    color: #666;
}
.sort {
    cursor: pointer;
}
.listing.wrapper .right-block .listing-menu>div.list_title_sort {
    display: none;
}
.list_title_sort .sortByMobile {
    display: none !important;
}
.listing.wrapper .right-block .listing-menu>div:last-child>* {
    display: inline-block;
    float: right;
}
.listing.wrapper .right-block .product-list>li {
    display: inline-block;
    width: 50%;
    margin-left: -3px;
    vertical-align: top;
}
.product-listing.product-grid li.product-item, .listing.wrapper .right-block .product-list>li {
    width: 24%;
    padding: 0;
    margin-left: -3px;
    margin-right: 1%;
}
.product-tile {
    display: block;
    width: 100%;
    padding: 10px;
}
.product-tile .image {
    position: relative;
}
.stockLevelStatus, .stockLevelStatusinStock {
    top: 40%;
    left: 0;
    right: 0;
    margin: auto;
    font-size: 12px;
    font-weight: 600;
    position: absolute;
    padding: 3px 5px;
    border: 1px solid #e4e4e4;
    background-color: #fff;
    color: #ff1c47;
    display: block;
    max-width: 105px;
    width: 100%;
}
.product-tile .image .quickview {
    height: auto !important;
}
.product-tile .image .quickview {
    position: absolute;
    cursor: pointer;
    bottom: 0;
    left: 0;
    z-index: 2;
    width: 100%;
    background-color: rgba(248,249,251,0.77);
    text-align: center;
    height: 70px;
    opacity: 0;
    letter-spacing: 1px;
    font-size: 12px;
    font-weight: 500;
    -webkit-transition: opacity .15s;
    -moz-transition: opacity .15s;
    transition: opacity .15s;
}
.product-tile .image .quickview a {
    position: relative;
    top: 0;
    display: inline-block;
    width: 48%;
    vertical-align: top;
}
.product-tile .image .quickview a, .product-tile .image .quickview span {
    color: #000;
}
.product-tile .image .quickview .js-reference-item {
    margin: 14px 0;
    text-align: center;
    display: block;
    width: 100%;
}
.productInfo {
    clear: both;
}
.productInfo li {
    margin-bottom: 5px;
}
div.productInfo ul li.product-size-list {
    min-height: 16px;
    line-height: 1.3;
}
.short-info, .latest-offer-info {
    position: relative;
    margin-top: 20px;
    width: 100%;
}
.product-tile .short-info .color-swatch {
    width: 100% !important;
    max-width: 100%;
    float: none;
    clear: both;
	display: block;
	line-height: 19px;
}
.short-info .color-swatch>li {
    display: inline-block;
    margin: 0 2px;
    margin-bottom: 5px;
    float: left;
}
.product-tile .short-info .color-swatch li {
    width: 16px;
    height: 15px;
    margin-right: 10px;
}
ul.color-swatch li span {
    padding: 0 !important;
}
.product-tile .short-info .color-swatch li span {
    display: block !important;
    cursor: pointer;
    height: 100%;
    border-radius: 0 !important;
    position: relative;
    width: 100%;
}
.short-info .color-swatch::after {
    clear: both;
    content: "";
    display: table;
}
.product-tile .details.short-info .brand, .product-tile .short-info .company {
    margin: 10px 0;
    font-size: 14px;
    min-height: 10px;
    text-align: left;
    float: left;
    width: 50%;
    word-wrap: break-word;
    color: #a9a9a9;
}
.product-tile .details.short-info .brand {
    width: 100%;
}
.product-tile .short-info .product-name {
    font-size: 14px;
	font-weight: normal;
    font-family: "Montserrat";
    line-height: 1.5;
    padding-bottom: 2px;
    float: left;
    width: 100%;
    word-wrap: break-word;
}
.short-info .price {
    color: #000;
}
.product-tile .short-info .company, .product-tile .short-info .price {
    font-size: 14px;
    font-family: "Montserrat";
    line-height: normal;
    float: left;
    font-weight: 500;
    width: 100%;
    text-align: left;
}
.product-tile .short-info .price {
    font-size: 14px;
}
.short-info .company .normal, .short-info .company .old, .short-info .company .sale, .short-info .price .normal, .short-info .price .old, .short-info .price .sale {
    display: inline-block;
}
.short-info .company .old, .short-info .price .old {
    color: #969696;
    font-weight: normal;
	text-decoration: line-through;
}
.short-info .company .sale, .short-info .price .sale {
    color: #a91f3d;
    font-weight: normal;
}
.product-item .product-tile p.savings {
    display: inline-block;
}
p.savings span {
    display: inline-block;
    padding: 0 5px;
    color: #ee5f73;
    margin-top: 5px;
    margin-bottom: 5px;
    line-height: 20px;
}
.product-item .product-tile p.savings span {
    background: 0;
    color: #a91f3d;
    font-size: 12px;
    padding: 0 2px;
    font-weight: normal;
}
.toggle-filterSerp, .no-results .mobile {
    display: none;
}
.product-facet {
    padding: 0 10px;
}
.left-block ul.product-facet.js-product-facet.listing-leftmenu {
    margin: 0;
}
.facet-name.js-facet-name {
    font-size: 14px;
    font-weight: bold;
    cursor: pointer;
    text-transform: capitalize;
}
.listing-leftmenu>div.facet_desktop, .facet-name.js-facet-name.facet_desktop {
    display: block !important;
}
.listing-leftmenu h3 {
    font-size: 14px;
    cursor: pointer;
    position: relative;
    padding: 10px 0;
    word-wrap: break-word;
    padding-right: 15px;
    letter-spacing: normal;
}
.listing.wrapper .facet-name.js-facet-name h3 {
    padding-left: 0;
    font-weight: normal;
    display: block;
}
.listing.wrapper .left-block .listing-leftmenu h3 {
    text-transform: uppercase !important;
}
.listing.wrapper .facet-name.js-facet-name h3.active, .listing-leftmenu h4.customPriceRange {
    font-weight: bold;
}

.listing-leftmenu h3:after {
    font-family: 'FontAwesome';
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    content: "\f105";
    position: absolute;
    font-size: 18px;
    margin-top: 1px;
    margin-left: 10px;
    right: 2px;
    top: 9px;
    font-weight: 100;
    padding: 1px;
    height: 14px;
    line-height: 11px;
}
.listing-leftmenu h3.true:after {
    -ms-transform: rotate(90deg);
    -webkit-transform: rotate(90deg);
    transform: rotate(90deg);
}
.listing-leftmenu h3.active:after {
    color: #a91f3d;
    -ms-transform: rotate(180deg);
    -webkit-transform: rotate(180deg);
    transform: rotate(180deg);
}
.listing-leftmenu h3.tree-dept:after {
    right: 2px;
}
.listing-leftmenu h3.active:after {
    font-family: 'FontAwesome';
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    content: "\f107";
    position: absolute;
    font-size: 18px;
    margin-top: 1px;
    margin-left: 10px;
    right: 2px;
    top: 9px;
    font-weight: 100;
    padding: 1px;
}
/* jqtree.css start */
ul.jqtree-tree {
	margin-left: 0;
}

ul.jqtree-tree, ul.jqtree-tree ul.jqtree_common {
	list-style: none outside;
	margin-bottom: 0;
	padding: 0;
	font-size: 13px;
}

ul.jqtree-tree ul.jqtree_common {
	display: block;
	margin-left: 12px;
	margin-right: 0;
}

ul.jqtree-tree li.jqtree-closed>ul.jqtree_common {
	display: none;
}

ul.jqtree-tree li.jqtree_common {
	clear: both;
	list-style-type: none;
}

ul.jqtree-tree .jqtree-toggler {
	border-bottom: none;
	color: #c6c6c6;
	text-decoration: none;
	margin-right: 0.5em;
	vertical-align: middle;
}

ul.jqtree-tree .jqtree-toggler:after {
	font-family: 'FontAwesome';
	-webkit-font-smoothing: antialiased;
	-moz-osx-font-smoothing: grayscale;
	content: "\f107";
	display: inline-block;
	text-align: center;
	vertical-align: middle;
	font-size: 20px;
	color: #000;
	font-weight: normal;
	margin-left: 5px;
}

ul.jqtree-tree .jqtree-toggler.jqtree-closed:after {
	font-family: 'FontAwesome';
	-webkit-font-smoothing: antialiased;
	-moz-osx-font-smoothing: grayscale;
	content: "\f105";
	display: inline-block;
	text-align: center;
	vertical-align: middle;
	font-size: 20px;
	color: #000;
	font-weight: normal;
	margin-left: 5px;
}

ul.jqtree-tree .jqtree-toggler:hover {
	/*  color: #000; */
	text-decoration: none;
}

ul.jqtree-tree .jqtree-element {
	cursor: pointer;
}

.jqtree-tree .jqtree-title {
	color: #000;
	opacity: 0.9;
	vertical-align: middle;
	/*  margin-left: 1.5em;
    display:inline-block; */
}

.jqtree-tree .jqtree-title:hover {
	color: #A9143C !important;
}

.jqtree-tree .jqtree-title.jqtree-title-folder {
	margin-left: 0;
	color: #000;
}

ul.jqtree-tree li.jqtree-folder {
	margin-bottom: 4px;
}

ul.jqtree-tree li.jqtree-folder.jqtree-closed {
	margin-bottom: 1px;
}

ul.jqtree-tree .jqtree-toggler.jqtree-closed {
	background-position: 0 0;
}

span.jqtree-dragging {
	color: #fff;
	background: #000;
	opacity: 0.6;
	cursor: pointer;
	padding: 2px 8px;
}

ul.jqtree-tree li.jqtree-ghost {
	position: relative;
	z-index: 10;
	margin-right: 10px;
}

ul.jqtree-tree li.jqtree-ghost span {
	display: block;
}

ul.jqtree-tree li.jqtree-ghost span.jqtree-circle {
	border: solid 2px #0000ff;
	-webkit-border-radius: 100px;
	-moz-border-radius: 100px;
	border-radius: 100px;
	height: 8px;
	width: 8px;
	position: absolute;
	top: -4px;
	left: -6px;
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	box-sizing: border-box;
}

ul.jqtree-tree li.jqtree-ghost span.jqtree-line {
	background-color: #0000ff;
	height: 2px;
	padding: 0;
	position: absolute;
	top: -1px;
	left: 2px;
	width: 100%;
}

ul.jqtree-tree li.jqtree-ghost.jqtree-inside {
	margin-left: 48px;
}

ul.jqtree-tree span.jqtree-border {
	position: absolute;
	display: block;
	left: -2px;
	top: 0;
	border: solid 2px #0000ff;
	border-radius: 6px;
	margin: 0;
	box-sizing: content-box;
}

ul.jqtree-tree .jqtree-element {
	position: relative;
	margin-bottom: 5px;
}

ul.jqtree-tree .jqtree-element {
	
}

ul.jqtree-tree .jqtree-moving>.jqtree-element .jqtree-title {
	outline: dashed 1px #0000ff;
}

.jqtree-element .jqtree_common:hover a, .jqtree-element .jqtree_common:hover span
	{
	color: #000;
}
/* jqtree.css end */

.facet-values.js-facet-values.js-facet-form.active, .brandSelectAllMain.active {
    display: block;
}
.product-facet .facet .facet-name .brandSelectAllMain {
    padding-top: 10px;
    position: relative;
    left: 0;
    width: 100%;
    margin: 0;
}
.brandSelectAllMain .brandSearchForm {
    margin-bottom: 15px;
}
.listing-leftmenu .search button, .listing-leftmenu .search input {
    float: left;
    position: relative;
}
.listing-leftmenu .search input.brandSearchTxt {
    width: 100%;
    font-size: 12px;
}
.listing-leftmenu .search input.brandSearchTxt {
    border: 1px #ccc solid !important;
    height: 30px;
    padding: 5px 5px 5px 10px !important;
    font-style: normal;
}
.facet-values.js-facet-values.js-facet-form {
    max-height: 255px;
    overflow-y: auto;
}
.facet-values.js-facet-values.js-facet-form.active, .brandSelectAllMain.active {
    display: block;
}
.left-block .facet-values.js-facet-values.js-facet-form {
    margin-top: 6px;
}
.product-facet .facet .facet-list {
    padding-left: 0;
    list-style: none;
    margin: 10px 5px;
    word-wrap: break-word;
}
.product-facet .facet .facet-list li {
    margin: 6px 0;
}
.facet .facet-values .facet-list li {
    font-size: 14px;
}
.product-facet .facet .facet-list label {
    cursor: pointer;
    font-weight: normal;
    display: block;
    margin: 0;
}
.product-facet .facet .facet-list .facet-mark {
    display: inline-block;
    float: left;
    width: 12px;
    height: 12px;
    padding: 0;
    margin: 2px 10px 0 -5px;
    border: 1px solid #c6c6c6;
    text-align: center;
    vertical-align: middle;
    background: #fff;
    position: relative;
}
.product-facet .facet .facet-list .facet-mark, .product-facet .facet .facet-list .facet-checkbox:checked+.facet-label .facet-mark:after {
    height: 15px;
    width: 15px;
}
.product-facet .facet .facet-list div.facet-text {
    display: block;
    margin-left: 17px;
    line-height: 18px;
}
.product-facet .facet .facet-list span.facet-text {
    line-height: 18px;
    width: auto;
    color: #000;
    opacity: .9;
    font-size: 13px;
    text-transform: capitalize !important;
    margin-bottom: 6px;
    margin-right: 5px;
    margin-left: -1px;
}
.product-facet .facet .facet-list .facet-count {
    font-size: 13px;
    color: #a9143c;
    line-height: normal;
}
.product-facet .facet .facet-list.facet-list-hidden {
    display: none;
}
.listing-leftmenu .checkbox-menu {
    padding: 10px;
    clear: both;
}
.product-facet .facet .more {
    display: block;
}
.listing-leftmenu .checkbox-menu>a, .listing-leftmenu .checkbox-menu input[type='submit'] {
    color: #4a4a4a;
    font-size: 11px;
    margin: 5px 0;
    font-weight: 400;
    letter-spacing: -1px;
    display: block;
    border: 0;
    -webkit-appearance: none;
}
.product-facet .facet .more {
    display: block;
}
.listing-leftmenu .checkbox-menu>a.more {
    margin-bottom: 0;
}
.product-facet .facet .less {
    display: none;
}
.listing-leftmenu>div.facet_mobile, .facet-name.js-facet-name.facet_mobile, .filter-name.facet_mobile {
    display: none !important;
}

@media (min-width: 651px){
.listing.wrapper .left-block {
    width: 30%;
    padding-top: 5px;
    margin-top: 55px;
    position: relative;
    z-index: 1;
}
.listing.wrapper .right-block .product-list>li {
    margin-bottom: 20px;
}
.listing.wrapper .right-block .product-list>li {
    -webkit-box-shadow: #fff 0 0 2px 2px;
    -moz-box-shadow: #fff 0 0 2px 2px;
    -o-box-shadow: #fff 0 0 2px 2px;
    box-shadow: #fff 0 0 2px 2px;
}
.facet-name.js-facet-name h3.tree-dept {
    padding-bottom: 10px;
}
}
@media (min-width: 791px){
.listing.wrapper .left-block:not(.fix) {
    width: 19.83% !important;
}
.listing.wrapper .left-block {
    width: 19.83%;
    margin-bottom: 20px;
}
.listing.wrapper .right-block {
    padding-top: 0 !important;
}
.facet-list.filter-opt {
    text-align: left;
    /* padding-left: calc(19.83% - -20px); */ /* TISPRDT-6115 */
}
.mob-filter-wrapper {
    display: block !important;
}
.filter-title {
    display: none;
}
.left-block ul.product-facet.js-product-facet.listing-leftmenu {
    display: block !important;
}
.facet-name.js-facet-name h3.tree-dept.active {
    border-bottom: 1px solid #ccc;
    margin-bottom: 10px;
}
}
@media (max-width: 790px){
.listing.wrapper .right-block .listing-menu {
    padding: 0;
}
.sort_by_wrapper {
    display: inline-block;
    float: right;
    top: 34px;
    position: absolute;
    right: 0;
}
.list_title_sort {
    background: #f5f5f5;
    width: 168px;
    height: 42px;
    text-transform: uppercase;
    font-size: 12px;
    padding: 0 0 0 8px !important;
    line-height: 41px;
    text-align: center;
}
.listing.wrapper .right-block .listing-menu>div.list_title_sort {
    display: block;
    margin-right: 23px;
}
.list_title_sort .sortByMobile {
    display: block !important;
    float: left !important;
    line-height: normal;
    padding-top: 5px;
    padding-right: 10px;
    padding-left: 35px;
}
.list_title_sort select {
    background: transparent;
    border: none;
    left: -76px;
    position: relative;
    margin-top: 14px;
    font-size: 9px;
    text-transform: uppercase;
}
.list_title_sort:after {
    content: "";
    position: absolute;
    background-image: url(../images/Sprite-combined.png);
    background-repeat: no-repeat;
    background-position: -46px 0;
    zoom: .7;
    -moz-transform: scale(0.7);
    -moz-transform-origin: 0 0;
    right: 42px;
    top: 17px;
    height: 26px;
    width: 25px;
}
.toggle-filterSerp {
    text-indent: 2.5px;
}
.sort-refine-bar.mobile, .toggle-filterSerp {
    position: relative;
    cursor: pointer;
    text-transform: uppercase;
    font-size: 12px;
    display: block;
    width: 168px;
    padding: 0 0 0 8px !important;
    background: #f5f5f5;
    height: 42px;
    margin-top: 20px;
    line-height: 41px;
    text-align: center;
}
.toggle-filterSerp:after {
    content: "";
    position: absolute;
    background-image: url(../images/Sprite-combined.png);
    background-repeat: no-repeat;
    background-position: -2px 0;
    zoom: 0.7;
    -moz-transform: scale(0.7);
    -moz-transform-origin: 0 0;
    right: 15px;
    top: 17px;
    height: 26px;
    width: 25px;
}
.product-listing.product-grid li.product-item, .listing.wrapper .right-block .product-list>li {
    width: 33%;
    padding: 0;
    margin-right: 0 !important;
    margin-left: 0;
}
.product-item .product-tile p.savings {
    margin-left: -3px;
    float: none;
    clear: both;
    display: block;
}
.list_title .UlContainer {
    display: none;
}
.listing.wrapper .right-block .listing-menu.sort_by_wrapper>div.list_title {
    padding: 0;
}
.listing.wrapper .left-block .mob-filter-wrapper {
    width: 94%;
    display: none;
    height: 556px;
    overflow: hidden;
    position: fixed;
    top: 50px;
    transition: width .3s ease-in;
    background-color: #fff;
    z-index: 99999;
    padding: 0;
    left: 3%;
    right: 0;
    min-height: 556px;
    -webkit-box-shadow: 0 0 5px 0 rgba(184,178,184,1);
    -moz-box-shadow: 0 0 5px 0 rgba(184,178,184,1);
    box-shadow: 0 0 5px 0 rgba(184,178,184,1);
}
.listing.wrapper .right-block {
    width: 100%;
    padding: 15px 0 20px;
}
}
@media (max-width: 767px){
.sort_by_wrapper {
    position: fixed;
    bottom: 0;
    top: auto !important;
    width: 49%;
    background: #000;
    color: #fff;
    padding-left: 23px !important;
    text-align: left;
    z-index: 99;
}
.list_title_sort {
    background: none;
    float: left !important;
    padding-left: 0px !important;
}
.list_title_sort .sortByMobile {
    padding-left: 0px;
}
.list_title_sort select {
    color: #fff;
}
.list_title_sort:after {
    content: "";
    position: absolute;
    background-image: url(../images/Sprite-combined.png);
    background-repeat: no-repeat;
    right: 15px;
    top: 14px;
    background-position: -332px -2px;
    width: 24px;
}
.toggle-filterSerp:after {
    background-position: -288px 0;
    right: 10px;
}
.toggle-filterSerp {
    left: 0;
}
.toggle-filterSerp, .listing.wrapper .right-block .listing-menu>div .wrapped-form.sort.mobile {
    position: fixed;
    bottom: 0;
    top: auto !important;
    width: 49%;
    background: #000;
    color: #fff;
    padding-left: 23px !important;
    text-align: left;
    z-index: 99;
}
.product-listing.product-grid li.product-item, .listing.wrapper .right-block .product-list>li {
    width: 50%;
    min-height: 430px;
    padding: 0;
    margin-left: -2px !important;
}
.product-tile .short-info .price .sale {
    float: right;
}
}
	</style>
