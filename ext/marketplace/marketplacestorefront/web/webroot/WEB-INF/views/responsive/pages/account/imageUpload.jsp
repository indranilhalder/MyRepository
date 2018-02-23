<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- <spring:url value="/my-account/profile" var="profileUrl"/> --%>


<script src="https://rawgit.com/enyo/dropzone/master/dist/dropzone.js"></script>
<link rel="stylesheet" href="https://rawgit.com/enyo/dropzone/master/dist/dropzone.css">

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">

<style type="text/css">
	.top-buffer { margin-top:100px; }
</style>

 <template:page pageTitle="${pageTitle}">
 
 <div class="container">
	<div class="row top-buffer">
	<div class="col-md-12">
	<p >
  	Drag & Drop Img SAP HYBRIS POC
	</p></div>

<div class="row"><form id="my-awesome-dropzone"  action="../my-account/saveImage" class="dropzone" method="POST" >
	 <ul>
	 <li>
	 <select id="selectMediaFolder" >
	<c:forEach items="${mediaFolderList}" var="mediaFolder">
	<option value="multistep-pci">${mediaFolder.qualifier}</option>
	</c:forEach>
	</select>
	</li>
	<li style="padding-top:50px">
    <input type="text" placeholder="Enter Username" value="H" name="Name" class="dropzone"/>
	</li></ul>
<!--     <input type="text" placeholder="Enter Username" value="H1" name="Name1" class="dropzone"/> -->
<!--     <input type="text" placeholder="Enter Username" value="H2" name="Name2" class="dropzone"/> -->
	<!-- Change /upload-target to your upload address -->
	</form></div>
	
	<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script> -->
<!-- <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script> -->


</template:page>
	
