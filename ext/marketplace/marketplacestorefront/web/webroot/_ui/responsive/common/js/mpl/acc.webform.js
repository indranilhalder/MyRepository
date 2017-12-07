ACC.WebForm = {

	_autoload: [
		"bindClick"
	],

	bindClick: function(){
		
	},
	fileUpload : function(){
		var url="fileUpload";
		
	    $('#attachments').fileupload({
	        url: url,
	        dataType: 'json',
	        done: function (e, data) {
	            $.each(data.result.files, function (index, file) {
	                $('<p/>').text(file.name).appendTo('#files');
	            });
	        },
	        progressall: function (e, data) {
	            var progress = parseInt(data.loaded / data.total * 100, 10);
	            $('#progress .progress-bar').css(
	                'width',
	                progress + '%'
	            );
	        }
	    }).prop('disabled', !$.support.fileInput)
	    .parent().addClass($.support.fileInput ? undefined : 'disabled');
	}
};

$(function() {
	
	$(".node").on("change",function(e){
		var nodeParent=$(this).is(":selected").val();
		var nodeType=$(this).attr("name");
		var htmlOption="";
		$.ajax({
			url: ACC.config.encodedContextPath + "/crmChildrenNodes",
			data: {"nodeParent": nodeParent },
			type: 'GET',
			success: function (data)
			{
				if (nodeType.startsWith("nodeL1")) {
					$.each(data.nodes.nodes, function (index, data) {
				        htmlOption+="<option value='"+data.nodeCode+"'>"+data.nodeDesc + "</option>";
				    });
					
				    $("nodeL2").html(htmlOption);
				}
			},
			 error : function(resp) { 
				 console.log("Error in crmChildNodes"+resp);	
			 } 
		});
	});
});


