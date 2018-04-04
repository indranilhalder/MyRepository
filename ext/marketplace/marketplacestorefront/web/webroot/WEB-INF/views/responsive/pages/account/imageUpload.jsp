<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>

<style>
.imageUpload{
      max-width: 250px;
	  height: 50px;
	  padding-left: 0 15px;
	  }
table {
    border-collapse: collapse;
    width: 100%;
    padding-bottom: 5%;
}
th, td {
    text-align: left;
    padding: 8px;
}
tr:nth-child(even){background-color: #f2f2f2}
th {
    background-color: #514848;
    color: white;
    height : 30px !important;
    text-align: center;
}
.upload-img-btn,.download-img-btn{
	background: #a9133d;
    letter-spacing: .6px;
    color: #fff;
    padding: 10px 20px !important;
    font-size:12px !important;
    width: 120px;
}
.upload-img-btn{
	margin-right: 10px;
}
.upload-img-btn:hover,.download-img-btn:hover{
	color: #fff;
}
#upload_file{
	border:none !important;
}
#uploadImages{
	padding: 20px 30px;
}
#uploaded-files tbody tr th{
	padding: 10px !important;
	background: linear-gradient(to bottom, rgba(0,0,0,0.6), rgba(0,0,0,1));
}
#uploaded-files tr td{
	text-align:center;
}
@media (min-width:768px){
	#uploaded-files{
		width: 95.5%;
    	margin: 0 auto;
    	margin-bottom:30px;
	}
}
</style>
<script type="text/javascript">

	   // Function to add a row in the table
	   function addRow(filename, size, date,code) {
	     var markup = "<tr>";
	         markup += "<td class='img-preview' ><img class='imageUpload' src='" +filename + "'></td>";
	         markup += "<td class='img-url'>" + code + "</td>";
	         markup += "<td class='img-url'><a target='_blank' href='" +filename + "'>https:" + filename + "</a></td>";
	         markup += "<td>" + (size/1000).toFixed(2) + "kB </td>";
	         markup += "<td>" + date + "</td>";
	         markup += "</tr>";
	     $("#uploaded-files tbody:first-child").after(markup);
	   }

	   
	   function getDateTime(date) {
	     var dateTime = "";
	     dateTime = date.getDate() + "/" + (date.getMonth()+1) + "/" + date.getFullYear() + " " +
	     (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ":" +
	     (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ":" +
	     (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());

	     return dateTime;
	   }

function loadImageAjaxCall() {
	    
	    $("#uploaded-files tr").html("");
	    
	    var staticHost = $('#staticHost').val();
		$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
		$("body").append('<div class="loaderDiv" style="position: fixed; left: 45%;top:45%;z-index: 10000"><img src="'+staticHost+'/_ui/responsive/common/images/red_loader.gif" class="spinner"></div>');

	    
	    var formdata = $('#uploadImages')[0];
		var form = new FormData(formdata);
	 
	  $.ajax({
			url:ACC.config.encodedContextPath + "/my-account-upload",
			type: "POST",
			 data:	form,
			 enctype: 'multipart/form-data',
		        processData: false,  // Important!
		        contentType: false,
		        cache: false,
			success : function(response) {				
				$("#no-click,.loaderDiv").remove();
				$("#upload_file").empty();
				$("#image_preview").empty();
				$('#downloadBtn').removeClass('hidden');
				console.log(response);
				$("#uploaded-files").html("<tr><th>Image Preview</th><th>Image Name</th><th>URL</th><th>Size</th><th> Time </th></tr>");
				response.forEach(function(item) {
					//alert(item.imageUrl);
					var date = getDateTime(new Date());
					addRow(item.imageUrl, item.size,date,item.imageName);
				})
			},
			error : function(resp) {
		    $("#no-click,.loaderDiv").remove();	
		    alert("some error accured");
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
 // $('#image_preview').append("<img src='"+URL.createObjectURL(event.target.files[i])+"'><br>");
 }
}
</script>

<template:page pageTitle="${pageTitle}">
<div id="wrapper">
 <form:form action="/my-account-imageUpload" method="POST" id="uploadImages" enctype="multipart/form-data">
	<select id="selectMediaFolder" name="folderName">
	<c:forEach items="${mediaFolderList}" var="mediaFolder">
	<option value="${mediaFolder.qualifier}">${mediaFolder.qualifier}</option>
	</c:forEach>
	</select>
	
  <input type="file" id="upload_file" name="file" onchange="preview_image();" multiple/>
  <a class="btn upload-img-btn"  name='submit_image' value="Upload Image" onclick="loadImageAjaxCall();">Upload</a>
   <a class="btn download-img-btn" id="downloadBtn" href="#">Download</a>
 </form:form>
 <div id="image_preview"></div>
  <div id="response_preview"></div>
 
  <br><br>
 <table id="uploaded-files">
   <tr>
     <th>Image Preview</th>
     <th>Image Name</th>
     <th>URL</th>
     <th>Size</th>
     <th> Time </th>
   </tr>
 </table>
</div>
</template:page>
<script type="text/javascript">
function exportTableToCSV($table, filename) {

    var $rows = $table.find('tr:has(td),tr:has(th)'),

        // Temporary delimiter characters unlikely to be typed by keyboard
        // This is to avoid accidentally splitting the actual contents
        tmpColDelim = String.fromCharCode(11), // vertical tab character
        tmpRowDelim = String.fromCharCode(0), // null character

        // actual delimiter characters for CSV format
        colDelim = '","',
        rowDelim = '"\r\n"',

        // Grab text from table into CSV formatted string
        csv = '"' + $rows.map(function (i, row) {
            var $row = $(row), $cols = $row.find('td,th');

            return $cols.map(function (j, col) {
                var $col = $(col), text = $col.text();

                return text.replace(/"/g, '""'); // escape double quotes

            }).get().join(tmpColDelim);

        }).get().join(tmpRowDelim)
            .split(tmpRowDelim).join(rowDelim)
            .split(tmpColDelim).join(colDelim) + '"',



        // Data URI
        csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(csv);

        if (window.navigator.msSaveBlob) { // IE 10+
            //alert('IE' + csv);
            window.navigator.msSaveOrOpenBlob(new Blob([csv], {type: "text/plain;charset=utf-8;"}), "csvname.csv")
        }
        else {
            $(this).attr({ 'download': filename, 'href': csvData, 'target': '_blank' });
        }
}

$('#downloadBtn').click(function() {
	   exportTableToCSV.apply(this, [$('#uploaded-files'), 'image_upload.csv']);
});

</script>
