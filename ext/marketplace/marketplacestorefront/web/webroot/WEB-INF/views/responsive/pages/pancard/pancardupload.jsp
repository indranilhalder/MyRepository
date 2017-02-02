<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>

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
	
});
</script>
<style>
	.pancard-msg, .pancard-img-msg{
		display: none;
		color: #f00;
	}
	
</style>
</head>

<body>
	<h2>PAN CARD DETAILS FOR ORDER ID:${orderreferancenumber} </h2>
	
	<form:form method="POST" action="/pancard/pancardupload" enctype="multipart/form-data" id="uploadPanDetails" >
       <table width="50%" border="0" cellspacing="2" cellpadding="2">
       
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
	</table>

	</form:form>

</body>
</html>