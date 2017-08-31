<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/style.css"/>
<script type="text/javascript" src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	var regpan = /^([a-zA-Z]){5}([0-9]){4}([a-zA-Z]){1}?$/;
	var panv = true;
	
	 $("#pancard_No").keyup(function(){
		
		if(regpan.test($(this).val()) == false)
		{
			panv = false;
			$(".pancard-img-msg").show();
		}else{
			panv = true;
			$(".pancard-img-msg").hide();
		}
	}); 	
	
	$("#btn_PanDetails").on("click", function(e){
		
		if(regpan.test($("#pancard_No").val())==false)
		{
			panv = false;
			$(".pancard-img-msg").show();
		}else{
			panv = true;
			$(".pancard-img-msg").hide();
		}
	
		var has_selected_file = $('input[type=file]').filter(function(){
	        return $.trim(this.value) != ''
	    }).length  > 0 ;

	    if ((has_selected_file) && (panv)) {
	    	$(".pancard-img-msg").hide();
	    } else {
	    	$(".pancard-img-msg").show();
	    }

	    var status = '<c:out value="${status}"/>';
	    var orderId = '<c:out value="${orderreferancenumber}"/>';
	    $('#pancardMsg').hide();
		
		 if((regpan.test($("#pancard_No").val())==false))
			{
				e.preventDefault();
				return false;
			}
		 else if(status=="PENDING_FOR_VERIFICATION"){
			 e.preventDefault();
			 $('.pan-card-fields').html("<div class = 'pan-details-pending' id = 'pancardMsg'>PanDetails is already uploaded and in "+status+" state</div>");
			 return false;
		 }
		 else if(status=="APPROVED"){
			 e.preventDefault();
			 $('.pan-card-fields').html("<div class = 'pan-details-approved' id = 'pancardMsg'>PanDetails is already "+status+"</div>");
			 return false;
		 }
		 else{
			if (has_selected_file) {
					$("#uploadPanDetails").submit();
			    }else{
					e.preventDefault();
					return false;
			    }
			}
	})
	
	$('#labelBrowseBtn').click(function () {
		$("input[type='file']").trigger('click');
	});

	$("input[type='file']").change(function () {
		$('#val').text(this.value.replace(/C:\\fakepath\\/i, ''))
	});
});
</script>
</head>

<body>
	<div class="wrapper-block pan-card-block">
		<section>
			<div class="pan-detail-sec">
				<h2 class="pan-heading">PAN Card Details</h2>
				<p class="lead-text">Your order is just one step away!</p>
				
				<div class="form-section">
					<form:form method="POST" action="/pancard/pancardupload" enctype="multipart/form-data" id="uploadPanDetails">
						<input type="hidden" name="orderreferancenumber" value="${orderreferancenumber}" />
        				<input type="hidden" name="Customer_name" value="${customername}">
        				 <input type="hidden" name="transactionid" value="${transactionid}">
						<div class="panfield-wrapper clearfix orderId-left">
							<div class="pan-label">Order ID:</div>
							<div class="pan-label-value disable-color">${orderreferancenumber}</div>
						</div>
						<div class="panfield-wrapper clearfix orderName-right">
							<div class="pan-label">Name:</div>
							<div class="pan-label-value disable-color">${customername}</div>
						</div>
						<div class="pan-card-fields">
						
						<c:if test="${not empty failure}">
							<div class = "pan-failure-msg"><span class = "pan-failure-span">${failure}</span></div>
						</c:if>
						
						<div class="panfield-wrapper clearfix pan-num">
							<div class="pan-label">PAN Card Number * <br>
								<span class="pancard-img-msg">Please provide the PAN Card Number of the person whose name appears on the invoice</span>
							</div>
							<div class="pan-label-value">
								<input type="text" name="Pancard_number" id="pancard_No" maxlength = "10">
								<!-- <span class="pancard-msg">Please enter correct pancard number.</span>  -->
							</div>
						</div>
						<div class="panfield-wrapper clearfix pan-img">
							<div class="pan-label">Upload a copy of the PAN Card * <br>
								<span class="pancard-img-msg" style = "display:none">Please upload a copy of the PAN Card</span>
							</div>
							<div class="pan-label-value fileupload">
								<input  type="file" name="file" id="pancard_Img"> 
								<span id="val"></span>
								<span id="labelBrowseBtn" class="label-browse">Choose File</span>
							</div>
						</div>
						<div class="panfield-wrapper btn-wrapper clearfix">
							<div class="pan-label-value">
								<a href="javascript:;" class="pan-submit-btn" title="SUBMIT" id="btn_PanDetails">SUBMIT</a>
								<!-- <a href="javascript:;" class="pan-cancel-btn" title="CANCEL" id="btncncl_PanDetails">CANCEL</a> -->
							</div>
						</div>
						</div>
					</form:form>
				</div>
			</div>
		</section>
	</div>
</body>
</html>