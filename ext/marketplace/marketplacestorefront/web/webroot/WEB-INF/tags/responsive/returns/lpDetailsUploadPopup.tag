<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ attribute name="entry" 
              required="true"
              type="de.hybris.platform.commercefacades.order.data.OrderEntryData" %>

<spring:url value="/my-account/returns/updateReturnInfo" var="returnInfoUrl" />

<!--   AWB codes PopUp  -->
       
        <div class="awsNumberModal" id="awbNumberPopup" >
           <form:form method="POST" action="${returnInfoUrl}"  commandName="mplReturnInfoForm"  enctype="multipart/form-data">
            <div class="modal-dialog changeAWS">
                <div class="modal-content">
	                    <div class="modal-body">
	                    	<button type="button" class="closeAWSNum" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	                        <div class="row clearfix">
	                        	<div class="col-md-12 col-sm-12 NOP">
                        			<div class="row clearfix">
                        				<h4 style="margin: 0px 10px;">Information Required For Return and Refund</h4>
                        				<div class="space">Fill in the below mentioned information to start the Return and Refund process for self shipped product and click on 'Submit' </div>
										<div class="col-md-6 form-group">
											<label for="awsNumber">AWB Number</label>
		    								<form:input type="text" path="awbNumber" class="form-control awsTextinput" id="awsNum" placeholder="AWS Number"/>
		    								<div class="errorText awsNumError"></div>
										</div>
										<div class="col-md-6 form-group">
											<label for="logisticPart">Logistics Partner</label>
		    								<form:input type="text"  path="lpname" class="form-control awsTextinput" id="logisticPartner" placeholder="Logistics Partner"/>
		    								<div class="errorText logisticPartnerError"></div>
										</div>
									</div>
									<div class="row clearfix">
										<div class="col-md-6">
											<label class="control-label" for="pod">Upload Proof of Delivery</label>
											
											<div  style="position: relative;">
											<input name="dispatchProof" type="file" id="uploadFile" class="file awsTextinput awsUpload" style="opacity: 0;z-index:999999; position: absolute;top: 0px;left: 0px;cursor: pointer;" data-show-preview="false">
											<%-- <form:input name="dispatchProof" path="dispatchProof" data-show-preview="false"/> --%>
											
											<%-- <form:input name="dispatchProof" path="dispatchProof" id="uploadFile" type="file" class="file awsTextinput awsUpload" style="opacity: 0;z-index:999999; position: absolute;top: 0px;left: 0px;cursor: pointer;" data-show-preview="false"/> --%>
											<div class="uploadDiv" style="position: absolute;top: 0px;left: 0px;z-index:999998;">
											<div class="col-md-7 textFile">No file Chosen</div>
											<div class="col-md-5 uploadButton">CHOOSE FILE</div>
											</div>
													</div>
											<div class="errorText uploadError"></div>
											
										</div>
										
										<div class="col-md-6">
											<div class="errorText mainError"></div>
											<form:hidden path="orderId" value="${subOrder.code}" />
											<form:hidden path="transactionId" value="${entry.transactionId}" />
											
										</div>
										<div class="col-md-6 form-group">
											<label for="awsNumber">Amount </label>
		    								<form:input type="text" path="amount" class="form-control awsTextinput" id="amount" placeholder="Amount"/>
		    								<div class="errorText amountError"></div>
										</div>
										<button type="submit" id="submitBlock" class="btn btn-primary submitButton">SUBMIT</button>
									</div>
								
								</div>
	                        	
	                        </div>
	                    </div>
	                   
                </div><!-- /.modal-content -->
                </div>
                </form:form>
            </div><!-- /.modal-dialog -->
       <style>
       .right-account .closeAWSNum{
	border-radius: 50%;
	border: 1px solid #ccc !important;
	width: 40px;
	height: 40px;
    position: absolute;
    right: 17px;
    top: -6px;
    font-size: 35px;
    font-weight: 100;
    color: #ccc;
    padding: 9px 9px;
}
       </style>       
      <!--   </div>/.modal -->
     
      
        <!-- End of  AWB codes PopUp  -->