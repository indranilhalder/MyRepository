<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>

<script type="text/javascript">

function loadImageAjaxCall() {
	    
	    var formdata = $('#uploadImages')[0];
		var form = new FormData(formdata);
	 
	  $.ajax({
			url:ACC.config.encodedContextPath + "/my-account-imageUpload",
			type: "POST",
			 data:	form,
			 enctype: 'multipart/form-data',
		        processData: false,  // Important!
		        contentType: false,
		        cache: false,
			success : function(response) {
				alert(response[0].imageUrl);
				$("#upload_file").empty();
				$("#image_preview").empty();
			},
			error : function(resp) {
			console.log(resp);
			}
		});
}

function preview_image() 
{
	$("#upload_file").empty();
 var total_file=document.getElementById("upload_file").files.length;
 for(var i=0;i<total_file;i++)
 {
  $('#image_preview').append("<img src='"+URL.createObjectURL(event.target.files[i])+"'><br>");
 }
}
</script>

<template:page pageTitle="${pageTitle}">
<div id="wrapper">
 <form:form action="/my-account-imageUpload" method="POST" id="uploadImages" enctype="multipart/form-data">
	<select id="selectMediaFolder" >
	<c:forEach items="${mediaFolderList}" var="mediaFolder">
	<option value="multistep-pci">${mediaFolder.qualifier}</option>
	</c:forEach>
	</select>
  <input type="file" id="upload_file" name="file" onchange="preview_image();" multiple/>
  <input type="button" name='submit_image' value="Upload Image" onclick="loadImageAjaxCall();"/>
 </form:form>
 <div id="image_preview"></div>
  <div id="response_preview"></div>
</div>
</template:page>
	
