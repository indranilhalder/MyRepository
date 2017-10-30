<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/responsive/store" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>

<template:page pageTitle="${pageTitle}">
	<div id="globalMessages">
		<common:globalMessages/>
	</div>
	<div class="custmCareHelp">
         
         <div class="custmCareHeadSec">
                <div class="secSide">
	               <h3 class="secLabel">Still Need Help?</h3>
	               <p>Tell us the exact issue. We would help you out.</p>
	             </div>
	             <div class="secSide btnRight">
	               <button class="needHelpBtn needHelpBtnLg contCustCareBtn dActive" id="needHelpBtn">Contact Customer Care</button>
	             </div>
	               <button class="closeBtn active" id="closeCustCareSec">X</button>
            </div>
            <div class="custmCareQrySec" style="display: block;">
                <div class="custmCareForms">
                  <div class="formGroup">
		             <h3 class="secLabel queryType">Type of Query</h3>
		             <div class="queryOptRadio">
		               <label>
		               <input type="radio" name="orderQuery">
		               <span></span>
		               Order Related Query</label>
		             </div>
		              <div class="queryOptRadio">
		               <label>
		                 <input type="radio" name="orderQuery">
		                 <span></span>
		                 Any Other Query
		                 </label>
		             </div>
		          </div>
		          <div class="loginSec">
			          <p class="loginTxt">Please login to see your order(s).</p>
			          <button class="needHelpBtn needHelpBtnSm">Login to Continue</button>
			      </div>
			       <div class="formGroup">
			       <div class="selectOrderSec">
			             <h3 class="secLabel">Select Your Order(s).</h3>
				          <div class="selectOrders">
				             <div class="selectedProduct filled">
				                	<div class="prodImg">
				                	  <img src="./Track Your Orders_files/product_1.jpg">
				                	</div>
				                	<div class="prodInfo">
					                	<div class="prodTxt">
					                	  <p class="orderDate">Order on: Sat, 28 May 2017</p>
					                	  <p class="prodName">Sparx Navy &amp; Yellow Running Shoes</p>
					                	  <p class="prodPrice">Price: ₹1299.00</p>
					                	  <span class="prodShiping">Delivered</span>
					                	</div>
					                </div>
				                </div>
				             <ul class="orderDrop" style="display: none;">
				                <li>
				                	<div class="prodImg">
				                	  <img src="./Track Your Orders_files/product_1.jpg">
				                	</div>
				                	<div class="prodInfo">
					                	<div class="prodTxt">
					                	  <p class="orderDate">Order on: Sat, 27 May 2017</p>
					                	  <p class="prodName">Sparx Navy &amp; Yellow Running Shoes</p>
					                	  <p class="prodPrice">Price: ₹1299.00</p>
					                	  <span class="prodShiping">Shipped</span>
					                	</div>
					                </div>
				                </li>
				                <li>
				                	<div class="prodImg">
				                	  <img src="./Track Your Orders_files/product_1.jpg">
				                	</div>
				                	<div class="prodInfo">
					                	<div class="prodTxt">
					                	  <p class="orderDate">Order on: Sat, 28 May 2017</p>
					                	  <p class="prodName">Sparx Navy &amp; Yellow Running Shoes</p>
					                	  <p class="prodPrice">Price: ₹1299.00</p>
					                	  <span class="prodShiping">Delivered</span>
					                	</div>
					                </div>
				                </li>
				                <li>
				                	<div class="prodImg">
				                	  <img src="./Track Your Orders_files/product_1.jpg">
				                	</div>
				                	<div class="prodInfo">
					                	<div class="prodTxt">
					                	  <p class="orderDate">Order on: Sat, 29 May 2017</p>
					                	  <p class="prodName">Sparx Navy &amp; Yellow Running Shoes</p>
					                	  <p class="prodPrice">Price: ₹1299.00</p>
					                	  <span class="prodShiping">Shipped</span>
					                	</div>
					                </div>
				                </li>
				                <li>
				                	<div class="prodImg">
				                	  <img src="./Track Your Orders_files/product_1.jpg">
				                	</div>
				                	<div class="prodInfo">
					                	<div class="prodTxt">
					                	  <p class="orderDate">Order on: Sat, 27 May 2017</p>
					                	  <p class="prodName">Sparx Navy &amp; Yellow Running Shoes</p>
					                	  <p class="prodPrice">Price: ₹1299.00</p>
					                	  <span class="prodShiping">Delivered</span>
					                	</div>
					                </div>
				                </li>
				             </ul>
				          </div>
				      </div>
				    </div>
				     <div class="formGroup">
				        <h3 class="secLabel">What is the issue?</h3>
				         <span class="customSelectWrap"><select class="customSelect">
				           <option>Select Your issue</option>
					    	<option>Where is my Order</option>
					    	<option>When I receive my Order</option>
					    	<option>What is tracking no. of my Order</option>           
				        </select><span class="holder active">When I receive my Order</span></span>
					  </div>
					  <div class="formGroup">
				        <h3 class="secLabel">Select a sub-issue.</h3>
				        <span class="customSelectWrap"><select class="customSelect">
				            <option>Select your exact problem</option>
					    	 <option>Wrong delivered message received</option>
					    	<option>Wrong delivered message received</option>
					    	<option>Wrong delivered message received</option>          
				        </select><span class="holder active">Wrong delivered message received</span></span>
					  </div>
					  <div class="formGroup">
				        <h3 class="secLabel">Sub-issue 2</h3>
				         <span class="customSelectWrap"><select class="customSelect">
				            <option>Select your exact problem</option>
					    	<option>Wrong delivered message received</option>
					    	<option>Wrong delivered message received</option>
					    	<option>Wrong delivered message received</option>         
				        </select><span class="holder active">Wrong delivered message received</span></span>
					  </div>
					  <div class="formGroup">
				        <h3 class="secLabel">Name</h3>
					    <input type="text" class="formControl feildError" placeholder="Enter Your Name">
					    <p class="errorTxt">Name Canot be empty.</p>
					  </div>
					  <div class="emalIdMobileFeild">
					  <div class="feildCols">
					      <div class="formGroup">
					        <h3 class="secLabel">Registered Email ID</h3>
						    <input type="text" class="formControl" placeholder="kanand@tataunistore.com">
						  </div>
					  </div>
					  <div class="feildCols">
					      <div class="formGroup">
					        <h3 class="secLabel">Mobile Number</h3>
						    <input type="text" class="formControl" placeholder="8876345123">
						  </div>
					  </div>
					  </div>
					  <div class="formGroup">
				        <h3 class="secLabel issueComment">Comment</h3><div class="commentLength">Remaining characters:1000</div>
					    <textarea class="formControl textArea" rows="3" placeholder="Describe your issue here."></textarea>
					  </div>
					  <div class="formGroup">
				        <h3 class="secLabel">Add attachment (Optional)</h3>
				        <p class="helpTxt">Upload JPEG, PNG, GIF, BMP, PDF (Maximum upload size 5MB)</p>
					    <div class="uploadFile">
					    <span>Upload File</span>
					       <input type="file">
					    </div>
					  </div>
					  <div class="formGroup">
				           <button class="needHelpBtn needHelpBtnMd">Submit</button>
					  </div>
				  </div>
	          </div>
         </div>




</template:page>

<style>
.custmCareHelp{color:#000;padding:25px 0 27px;border-top:1px solid #eeeeee;border-bottom:1px solid #eeeeee;font-size:14px;margin:30px 0;width:100%;float:left;}
.custmCareHeadSec{width:100%;float:left;margin:0 0 25px;}
.custmCareHeadSec .secSide{display:inline-block;min-width:235px;margin:0;}
.custmCareHeadSec .secSide.btnRight{float:right;}
.custmCareHelp .secHeading{font-weight:normal;font-size:18px;margin:0 0 5px;line-height:20px;}
.custmCareHelp .secHeading.queryType{margin:0 0 12px;}
.custmCareHelp p{margin:0 !important;line-height:1;}
.needHelpBtn{line-height: 12px;height: 42px;font-size: 12px;padding: 6px 30px;font-weight: 500;background-color: #a9143c;color: #fff;text-transform:uppercase;}
.needHelpBtn.active{display:none;}
.custmCareHelp .closeBtn{font-size:16px;font-weight:normal;color:#000;background:transparent;border:0;outline:0;display:none;height:auto;float:right;padding:0;}
.custmCareHelp .closeBtn.active{display:block;}
.custmCareQrySec{width:100%;float:left;display:none;}
.custmCareSecDiv{width:100%;float:left;margin:0 0 25px;}
.queryOptRadio{display:inline-block;width:320px;}
.queryOptRadio label{padding-left:30px;font-size:14px;position:relative;line-height:18px;cursor:pointer;}
.queryOptRadio label input[type="radio"]{opacity:0;position:absolute;left:0;top:0;}
.queryOptRadio label input[type="radio"] + span{display:inline-block;width:17px;height:17px;border:1px solid	#aaaaaa;border-radius:50%;background:#ffffff;position:absolute;left:0;top:0;}
.queryOptRadio label input[type="radio"] + span:after{content:"";width:9px;height:9px;border-radius:50%;background:#000;position:absolute;left:3px;top:3px;-webkit-transform:scale(0);-moz-transform:scale(0);-ms-transform:scale(0);transform:scale(0);transition:all 0.3s ease;z-index:99;}
.queryOptRadio label input[type="radio"]:checked + span:after{-webkit-transform:scale(1);-moz-transform:scale(1);-ms-transform:scale(1);transform:scale(1);}
.custmCareHelp .loginTxt{margin:0 0 18px !important;}

@media(max-width:767px){
	.custmCareHelp .secHeading{text-align:left !important;}
	.custmCareHeadSec .secSide{display:block;width:100%;}
	.custmCareHeadSec .secSide{margin:0 0 20px;}
	.custmCareHeadSec .secSide.btnRight{margin:0;}
	.queryOptRadio {display: block;width: 100%;}
}
</style>
