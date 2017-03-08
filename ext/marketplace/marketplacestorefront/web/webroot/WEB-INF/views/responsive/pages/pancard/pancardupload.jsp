<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/style.css"/>
<script type="text/javascript" src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#pancard_No").keyup(function(){
		if($(this).val().length < 10){
			$(".pancard-msg").show();
		}else{
			$(".pancard-msg").hide();
		}
	});	
	$("#uploadPanDetails").submit(function(e){
		
		if($("#pancard_No").val().length < 10){
			$(".pancard-msg").show();
		}else{
			$(".pancard-msg").hide();
		}
		
		if(!$('#pancard_Img').val()){
			$(".pancard-img-msg").show();
		}else{
			$(".pancard-img-msg").hide();
		} 
		
		if($("#pancard_No").val().length < 10 && $('#pancard_Img').val() != true){
			e.preventDefault();
			return false;
		}else{
			return true;
		}
	})
	$('#labelBrowseBtn').click(function () {
		$("input[type='file']").trigger('click');
	});

	$("input[type='file']").change(function () {
		$('#val').text(this.value.replace(/C:\\fakepath\\/i, ''))
	});
	$("#btn_PanDetails").on("click", function(){
		$("#uploadPanDetails").submit();
	})
	
});
</script>
</head>

<body>
	<%-- <h2>PAN CARD DETAILS FOR ORDER ID:${orderreferancenumber} </h2> --%>
	
	<%-- <form:form method="POST" action="/pancard/pancardupload" enctype="multipart/form-data" id="uploadPanDetails" > --%>
       <!--<table width="50%" border="0" cellspacing="2" cellpadding="2">
       
		  <input type="hidden" name="orderreferancenumber" value="${orderreferancenumber}" />
        <input type="hidden" name="Customer_name" value="${customername}">
        
        
         <tr>
          <td>OrderId : </td>
          <td><Strong>${orderreferancenumber}</Strong></td>
        </tr>

         <tr>
          <td>Name : </td>
          <td><Strong>${customername}</Strong></td>
        </tr>
 
         <tr>
          <td>Pancard Number: </td>
          <td>
          		<input type="text" name="Pancard_number" id="pancard_No"><br>
          		<span class="pancard-msg">Please enter correct pancard number.</span>          
          </td>
        </tr> 
              
         <tr>
          	<td>Please select a file to upload : </td>
            <td>
          		<input  type="file" name="file" id="pancard_Img"><br>
          		<span class="pancard-img-msg">Please upload Pan Card image.</span>
			</td>
        </tr>
       
		<tr>
	      <td>&nbsp;</td>
	      <td><input type="submit" value="Upload" id="btn_PanDetails"  /></td>
		</tr>
	</table>-->

	<%-- </form:form> --%>
	
	
	
	<div class="wrapper-block">
		<div class="sureheader">
			<div class="sure-head clearfix">
				<div class="logo-left">
					<img class="logo1" src="${commonResourcePath}/images/logo_camel.png" alt="SureThing">
					<img class="logo2" src="${commonResourcePath}/images/surething.png" alt="SureThing">
				</div>
				<div class="logo-right">
					<img src="${commonResourcePath}/images/tataclicq-logo.png" alt="TATA CLiQ">
				</div>				
			</div>
		</div>
		<section>
			<div class="pan-detail-sec">
				<h2 class="pan-heading">PAN Card Details</h2>
				<p class="lead-text">Your order is just one step away !</p>
				<div class="form-section">
					<form:form method="POST" action="/pancard/pancardupload" enctype="multipart/form-data" id="uploadPanDetails" >
						<input type="hidden" name="orderreferancenumber" value="${orderreferancenumber}" />
        				<input type="hidden" name="Customer_name" value="${customername}">
						<div class="panfield-wrapper clearfix">
							<div class="pan-label">Order ID</div>
							<div class="pan-label-value disable-color">${orderreferancenumber}</div>
						</div>
						<div class="panfield-wrapper clearfix">
							<div class="pan-label">Name</div>
							<div class="pan-label-value disable-color">${customername}</div>
						</div>
						<div class="panfield-wrapper clearfix">
							<div class="pan-label">PAN Card Number * <br>
								<span class="pancard-img-msg">Please provide the PAN Card Number of the person whose name appears on the invoice</span>
							</div>
							<div class="pan-label-value">
								<input type="text" name="Pancard_number" id="pancard_No">
							</div>
						</div>
						<div class="panfield-wrapper clearfix">
							<div class="pan-label">Upload a copy of the PAN Card * <br>
								<span class="pancard-img-msg">Please upload a copy of the PAN Card</span>
							</div>
							<div class="pan-label-value fileupload">
								<input  type="file" name="file" id="pancard_Img"> 
								<span id="val"></span>
								<span id="labelBrowseBtn" class="label-browse">Browse</span>
							</div>
						</div>
						<div class="panfield-wrapper btn-wrapper clearfix">
							<div class="pan-label-value">
								<a href="javascript:;" class="pan-submit-btn" title="SUBMIT" id="btn_PanDetails" >SUBMIT</a>
								<a href="javascript:;" class="pan-cancel-btn" title="CANCEL">CANCEL</a>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</section>
	</div>
</body>
</html>