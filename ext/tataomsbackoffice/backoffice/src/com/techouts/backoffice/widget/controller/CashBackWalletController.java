/**
 *
 */
package com.techouts.backoffice.widget.controller;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.annotation.Resource;

import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.tata.constants.TataomsbackofficeConstants;
import com.hybris.oms.tata.services.FilePathProviderService;
import com.hybris.oms.tata.tship.exceltocsv.pojo.CilqCashWalletPojo;
import com.techouts.dataonboarding.util.CSVFileReaderUtils;
import com.tisl.mpl.core.model.CustomerWalletDetailModel;
import com.tisl.mpl.facades.account.register.RegisterCustomerFacade;
import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.pojo.request.Customer;
import com.tisl.mpl.pojo.request.QCCustomerPromotionRequest;
import com.tisl.mpl.pojo.request.QCCustomerRegisterRequest;
import com.tisl.mpl.pojo.response.CustomerWalletDetailResponse;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;




/**
 * @author Tech
 *
 */
public class CashBackWalletController extends DefaultWidgetController
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@WireVariable("filePathProviderService")
	private FilePathProviderService filePathProviderService;

	@Resource(name = "mplWalletFacade")
	private MplWalletFacade mplWalletFacade;
	
	@Resource(name = "registerCustomerFacade")
	private RegisterCustomerFacade registerCustomerFacade;
	
	@Resource(name = "extendedUserService")
	private ExtendedUserService extendedUserService;
	
	@Resource(name = "modelService")
	private ModelService modelService;
	
	@Autowired
	private ConfigurationService configurationService;

	 
	private Label emptyFileError;
	private Textbox textbox;
	private Media media;
	private static final String CSV = ".csv";
	private static final String XLSX = ".xlsx";
	
	private static final String SUCCESS_MSG = "Success";
	private static final String FAILURE_MSG = "Failure";
	private static final String USER_DOES_NOT_MSG = "User does not have tatacliq account";
	private static final String USER_DOES_NOT_WALLET = "user does not have wallet Id";
	private static final String BUCKET_TYPE_PROMOTION = "PROMOTION";
	private static final String BUCKET_TYPE_CASHBACK = "CASHBACK";
	private static final String WALLET_UPLOAD_FILE_HEADER = "First name,Last Name,Email ID,Amount,Bucket name,Transaction ID,Wallet ID,Card Number,Card Expiry,Batch Number,Comments";
	private static final String WALLET_CANCEL_FILE_HEADER =  "Customer Email ID,Transaction ID,Wallet ID, BatchNumber , Comments";
   private static final String WALLET_UPLOAD = "WalletUplaod";
   private static final String CANCEL_UPLOAD = "CancelLoad";

	private static final Logger LOG = LoggerFactory.getLogger(CashBackWalletController.class);

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		textbox.setText("");
		emptyFileError.setVisible(false);
	}

	/**
	 * this method will call while uploading file
	 *
	 * @param uploadEvent
	 * @throws FileNotFoundException 
	 */
	@ViewEvent(componentID = "upload_button", eventName = Events.ON_UPLOAD)
	public void selectUploadWallet(final UploadEvent uploadEvent) throws FileNotFoundException
	{
		textbox.setText("");
		emptyFileError.setVisible(false);
		media = uploadEvent.getMedia();
		final String fileName = media.getName();
		boolean messageFlag=false;
		String fileUploadLocation =null;
      String fileUploadName =null;
        if (null != configurationService){
      		fileUploadLocation = configurationService.getConfiguration().getString("mpl.wallet.bulkuploadfileLocation");
      		fileUploadName =  configurationService.getConfiguration().getString("mpl.wallet.bulkuploadfilename");
      			if (fileName.endsWith(CSV) || fileName.endsWith(XLSX))
      			{
      				LOG.debug("**********************File Name :"+fileName);
      				List<CilqCashWalletPojo> cilqCashWalletPojoList = new ArrayList<CilqCashWalletPojo>();
      				  Scanner scanner = new Scanner(media.getStreamData());
      				  try{
         		        while (scanner.hasNext()) {
         		      	  CilqCashWalletPojo pojo = new CilqCashWalletPojo();
         		            List<String> line = CSVFileReaderUtils.parseLine(scanner.nextLine());
         		            try{
         		            	pojo.setCustomerFirstName(line.get(0));
            		            pojo.setCustomerLastName(line.get(1));
            		            pojo.setCustomerEmailId(line.get(2));
            		            pojo.setAmount(line.get(3));
            		            pojo.setBucketName(line.get(4));
               		            if(null != line.get(0) && !line.get(0).isEmpty() && null != line.get(1) && !line.get(1).isEmpty() 
               		            		&&  null != line.get(2) && !line.get(2).isEmpty() && line.get(2).contains("@") 
               		            		&&  null != line.get(3) && !line.get(3).isEmpty() &&  null != line.get(4) && !line.get(4).isEmpty()){
               		            cilqCashWalletPojoList.add(pojo);
               		            }
         		            }catch(Exception e){
         		            	Messagebox.show("Please upload file valid format like  First Name,Last Name ,Customer Email ,Amount , Bucket Type ", "Error Message", Messagebox.OK, Messagebox.ERROR);
         		            	messageFlag=true;
         		            }
         		        }
      				  }catch(Exception eObj){
      					  LOG.error("Exception Occer :"+eObj.getMessage());
      				  }finally{
      					  scanner.close();
      				  }
      		        PrintWriter pw = null;
      					   if(null != fileUploadLocation && !fileUploadLocation.isEmpty() && null!=fileUploadName ){
      					   	  try {
      					   		  Path path = Paths.get(fileUploadLocation);
      					           //if directory exists?
      					           if (!Files.exists(path)) {
      					               try {
      					                   Files.createDirectories(path);
      					               } catch (IOException e) {
      					                   //fail to create directory
      					               	 LOG.error("Exception Occer While Creating the File Location :"+e.getMessage());
      					               }
      					           }
      				      	      pw = new PrintWriter(new File(fileUploadLocation+fileUploadName));
      				      	      StringBuilder  builder=uploadWallettCashFile(cilqCashWalletPojoList,pw);
      				    	         pw.write(builder.toString());
      				          	   pw.close();
      				      	  } catch (FileNotFoundException e) {
      				      		  LOG.error("Exception Occer While Creating the File :"+e.getMessage());
      				      	  }finally{
      				      		  pw.close();
      				      	  }
      					}
      					   LOG.debug("successfully created File..........");
      	      	  if(!messageFlag){
      	      	  Messagebox.show("SuccessFully Uploaded wallet", "Success Message", Messagebox.OK, Messagebox.INFORMATION);
      	      	  }
      			}
        }
	}
	
private StringBuilder uploadWallettCashFile(List<CilqCashWalletPojo> cilqCashWalletPojoList, PrintWriter pw){
	StringBuilder  builder = new StringBuilder();
	  boolean  headerCheck = false ;
     boolean  responseCheck = false ;
     String commentMsg = "";
	  QCRedeeptionResponse response = null;
		
	 for(CilqCashWalletPojo walletObj:cilqCashWalletPojoList){
  	  CustomerModel currentCustomer =  extendedUserService.getUserForOriginalUid(walletObj.getCustomerEmailId());
  	  if(null!= currentCustomer  ){
     	  if (null != currentCustomer.getIsWalletActivated() && currentCustomer.getIsWalletActivated().booleanValue()){
     		      response = addAmountToQCWallet(currentCustomer,walletObj);
     		  if(null != response && response.getResponseCode()== 0){
     			  commentMsg =SUCCESS_MSG;
     		  }else{
     			  commentMsg = qcErrorMasseges(response.getResponseCode());
     			}
     	  }else if(null != currentCustomer.getCustomerWalletDetail() && null != currentCustomer.getIsWalletActivated() && currentCustomer.getIsWalletActivated().booleanValue()==false){
     		  try{
     		     response = addAmountToQCWallet(currentCustomer,walletObj);
      		  if(null != response && response.getResponseCode()== 0){
      			  commentMsg =SUCCESS_MSG;
      		  }else{
      			  commentMsg = qcErrorMasseges(response.getResponseCode());
      			}
    			}catch (final Exception ex){
    				ex.printStackTrace();
    			}
     	  }else if(currentCustomer.getCustomerWalletDetail() == null && !currentCustomer.getIsWalletActivated()){
     		try{
     			final String customerRegisterResponse = createQCWalletForCustomer(currentCustomer,walletObj);
    				if (customerRegisterResponse.equalsIgnoreCase(SUCCESS_MSG)){
    					response = addAmountToQCWallet(currentCustomer,walletObj);
    					 if(null != response && response.getResponseCode()== 0){
    		     			  commentMsg =SUCCESS_MSG;
    		     		  }else{
    		     			  commentMsg = qcErrorMasseges(response.getResponseCode());
    		     			}
    				}else{
    				 commentMsg =FAILURE_MSG;
    				}
    			}catch (final Exception ex){
    				ex.printStackTrace();
    			}
     	  }
  	  }else{
  		  commentMsg= USER_DOES_NOT_MSG;
  		  responseCheck=true;
  		  
  	  }
  	if(!headerCheck){
		  builder.append(WALLET_UPLOAD_FILE_HEADER +"\n");
		  headerCheck=true;
	  }
    	builder = genarateCsvFile(response,walletObj,responseCheck,commentMsg,builder,WALLET_UPLOAD);
    }
	 return builder;
}

private StringBuilder genarateCsvFile(final QCRedeeptionResponse response,final CilqCashWalletPojo walletObj,
		final boolean responseCheck,final String commentMsg,final StringBuilder  builder,final String fileCheck){
	  if(fileCheck.equalsIgnoreCase(WALLET_UPLOAD)){
	  builder.append(walletObj.getCustomerFirstName() +",");
	  builder.append(walletObj.getCustomerLastName() +",");
	  builder.append(walletObj.getCustomerEmailId() +",");
	  builder.append(walletObj.getAmount() +",");
	  builder.append(walletObj.getBucketName() +",");
	 if(!responseCheck){
		 builder.append(response.getTransactionId()+",");
		 builder.append(response.getWalletNumber()+",");
		 builder.append(response.getBatchNumber()+",");
	     	  if(null != response.getCards() && response.getCards().size()>0){
	     		builder.append(response.getCards().get(0).getExpiry()+",");
	     	   builder.append(response.getCards().get(0).getCardNumber()+",");
	     	  }
	  	  }else{
	  		builder.append(""+",");
	  	   builder.append(String.valueOf(""+","));
	  	   builder.append(String.valueOf(""+","));
	  	   builder.append(""+",");
	  	   builder.append(""+",");
	  	  }
	  builder.append(commentMsg);
	  builder.append('\n'); 
	  }else if(fileCheck.equalsIgnoreCase(CANCEL_UPLOAD)){
		  
		  builder.append(walletObj.getCustomerEmailId() +",");
		  if(!responseCheck){
			  builder.append(response.getTransactionId()+",");
			  builder.append(response.getWalletNumber()+",");
			  builder.append(response.getBatchNumber()+",");
      	  }else{
      			builder.append(""+",");
      	  	   builder.append(String.valueOf(""+","));
      	  	   builder.append(String.valueOf(""+","));
      	  }
		  builder.append(commentMsg);
		  builder.append('\n');
	  }
	return builder;
}
private QCRedeeptionResponse  addAmountToQCWallet(final  CustomerModel currentCustomer, final CilqCashWalletPojo walletObj){
	QCRedeeptionResponse response = null;
	try{
		  QCCustomerPromotionRequest request = new QCCustomerPromotionRequest();
  		  request.setAmount(Double.valueOf(walletObj.getAmount()));
  		  if(walletObj.getBucketName().equalsIgnoreCase(BUCKET_TYPE_PROMOTION)){
  		      request.setNotes("Sample load for "+ walletObj.getAmount() + " "+BUCKET_TYPE_PROMOTION);
  		  }else if (walletObj.getBucketName().equalsIgnoreCase(BUCKET_TYPE_CASHBACK)){
  			  request.setNotes("Sample load for "+ walletObj.getAmount() +  " "+BUCKET_TYPE_CASHBACK);
  		  }
  		 response =  mplWalletFacade.addTULWalletCashBack(currentCustomer.getCustomerWalletDetail().getWalletId() ,request);
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return response;
}
private String createQCWalletForCustomer(final  CustomerModel currentCustomer,final CilqCashWalletPojo walletObj ){
	String commentMsg =null;
	try{
			final QCCustomerRegisterRequest customerRegisterReq = new QCCustomerRegisterRequest();
			final Customer custInfo = new Customer();
			custInfo.setFirstname(walletObj.getCustomerFirstName());
			custInfo.setLastName(walletObj.getCustomerLastName());
			custInfo.setEmail(currentCustomer.getOriginalUid());
			custInfo.setEmployeeID(currentCustomer.getUid());
			custInfo.setCorporateName("Tata Unistore Ltd");
		   if(null!= walletObj.getCustomerFirstName()){
				custInfo.setFirstname(walletObj.getCustomerFirstName());
			}
			if(null!= walletObj.getCustomerLastName()){
				custInfo.setLastName(walletObj.getCustomerLastName());
			}
			customerRegisterReq.setExternalwalletid(currentCustomer.getUid());
			customerRegisterReq.setCustomer(custInfo);
			customerRegisterReq.setNotes("Activating Customer " + currentCustomer.getUid());
			final QCCustomerRegisterResponse customerRegisterResponse = mplWalletFacade.createWalletContainer(customerRegisterReq);
			if (customerRegisterResponse.getResponseCode() == 0)
			{
				final CustomerWalletDetailModel custWalletDetail = modelService.create(CustomerWalletDetailModel.class);
				custWalletDetail.setWalletId(customerRegisterResponse.getWallet().getWalletNumber());
				custWalletDetail.setWalletState(customerRegisterResponse.getWallet().getStatus());
				custWalletDetail.setCustomer(currentCustomer);
				custWalletDetail.setServiceProvider("Tata Unistore Ltd");
				modelService.save(custWalletDetail);
				currentCustomer.setCustomerWalletDetail(custWalletDetail);
				currentCustomer.setIsWalletActivated(Boolean.TRUE);
				modelService.save(currentCustomer);
  		    commentMsg =SUCCESS_MSG;
			}else{
			 commentMsg =qcErrorMasseges(customerRegisterResponse.getResponseCode());
			}
		}catch (final Exception ex){
			ex.printStackTrace();
		}
	return commentMsg;
}
	private String qcErrorMasseges(Integer qcErrorCode){
		String commentMsg = null;
		   if(qcErrorCode == Integer.valueOf(TataomsbackofficeConstants.ERROR_CODE_10004)){
				commentMsg =  TataomsbackofficeConstants.ERROR_CODE_10004_DESC;
			  }else if(qcErrorCode == Integer.valueOf(TataomsbackofficeConstants.ERROR_CODE_10027)){
				commentMsg =  TataomsbackofficeConstants.ERROR_CODE_10027_DESC;
			  }else if(qcErrorCode == Integer.valueOf(TataomsbackofficeConstants.ERROR_CODE_10528)){
				commentMsg =  TataomsbackofficeConstants.ERROR_CODE_10528_DESC;
			  }else if(qcErrorCode == Integer.valueOf(TataomsbackofficeConstants.ERROR_CODE_10086)){
				commentMsg =  TataomsbackofficeConstants.ERROR_CODE_10086_DESC;
			  }else if(qcErrorCode == Integer.valueOf(TataomsbackofficeConstants.ERROR_CODE_10096)){
				commentMsg =  TataomsbackofficeConstants.ERROR_CODE_10096_DESC;
			  }else if(qcErrorCode == Integer.valueOf(TataomsbackofficeConstants.ERROR_CODE_10550)){
				commentMsg =  TataomsbackofficeConstants.ERROR_CODE_10550_DESC;
			  }else{
				 commentMsg =FAILURE_MSG;
			  }
		
		return commentMsg;
	}
	
	@ViewEvent(componentID = "cancle_upload_button", eventName = Events.ON_UPLOAD)
	public void cancelUploadWallet(final UploadEvent uploadEvent) throws FileNotFoundException
	{
		System.out.println("**********************************click cancel upload button is ............");
		textbox.setText("");
		emptyFileError.setVisible(false);
		media = uploadEvent.getMedia();
		final String fileName = media.getName();
		String commentMsg = "";
		boolean messageFlag=false;
		if (fileName.endsWith(CSV) || fileName.endsWith(XLSX))
		{
			List<CilqCashWalletPojo> cilqCashWalletPojoList = new ArrayList<CilqCashWalletPojo>();
			  Scanner scanner = new Scanner(media.getStreamData());
	        while (scanner.hasNext()) {
	      	  CilqCashWalletPojo pojo = new CilqCashWalletPojo();
	            List<String> line = CSVFileReaderUtils.parseLine(scanner.nextLine());
	            try{
	            pojo.setCustomerEmailId(line.get(0));
	            pojo.setTransactionId(line.get(1));
	            if(null != line.get(0) && !line.get(0).isEmpty() && line.get(0).contains("@")){
	            cilqCashWalletPojoList.add(pojo);
	            }
	            }catch(Exception e){
	            	Messagebox.show("Please upload file valid format like  Customer Email ,Transaction Id ", "Error Message", Messagebox.OK, Messagebox.ERROR);
	            	messageFlag=true;
	            }
	        }
	        scanner.close();
	        boolean  headerCheck = false ;
	        boolean  responseCheck = false ;
	        PrintWriter pw = null;
	        StringBuilder  builder = new StringBuilder();
	        String fileUploadLocation =null;
	        String fileDownloadName =null;
	        if (null != configurationService)
				{
					fileUploadLocation = configurationService.getConfiguration().getString("mpl.wallet.bulkuploadfileLocation");
					fileDownloadName =  configurationService.getConfiguration().getString("mpl.wallet.bulkdownloadfilename");
				   if(null != fileUploadLocation && !fileUploadLocation.isEmpty() && null!=fileDownloadName){
				   	  try {
				   		  Path path = Paths.get(fileUploadLocation);
				           //if directory exists?
				           if (!Files.exists(path)) {
				               try {
				                   Files.createDirectories(path);
				               } catch (IOException e) {
				                   //fail to create directory
				               	 LOG.error("Exception Occer While Creating the File Location :"+e.getMessage());
				               }
				           }
			      	      pw = new PrintWriter(new File(fileUploadLocation+fileDownloadName));
			      	  } catch (FileNotFoundException e) {
			      	      e.printStackTrace();
			      	  }
				   }
				}
     	
      	  QCRedeeptionResponse responseObj = null;
      	  for(CilqCashWalletPojo walletObj:cilqCashWalletPojoList){
      		  	CustomerModel currentCustomer =  extendedUserService.getUserForOriginalUid(walletObj.getCustomerEmailId());
	      	   System.out.println("model Name :"+currentCustomer);
	      	   if(null!= currentCustomer  ){
	 	      	  System.out.println("First Name :"+currentCustomer.getDisplayName());
	    	      	  if (null != currentCustomer.getIsWalletActivated()){
	    	      		  responseObj = mplWalletFacade.refundTULPromotionalCash(currentCustomer.getCustomerWalletDetail().getWalletId() , walletObj.getTransactionId());
	    	      		 if(null != responseObj && responseObj.getResponseCode()== 0){
	    		     			  commentMsg =SUCCESS_MSG;
	    		     		  }else{
	    		     			  commentMsg = qcErrorMasseges(responseObj.getResponseCode());
	    		     			}
	    	      	  }else{
	    	      		 commentMsg= USER_DOES_NOT_WALLET;
	    	      	  }
	      	   }else{
		      		  commentMsg= USER_DOES_NOT_MSG;
		      		  responseCheck=true;
		      		  
		      	  }
		      		if(!headerCheck){
		      			  builder.append(WALLET_CANCEL_FILE_HEADER +"\n");
		      			  headerCheck=true;
		      		  }
		      	    	builder = genarateCsvFile(responseObj,walletObj,responseCheck,commentMsg,builder,CANCEL_UPLOAD);
      	  }
      	  pw.write(builder.toString());
      	  pw.close();
      	  System.out.println("successfully created File..........");
      	  if(!messageFlag){
      	  Messagebox.show("SuccessFully Uploaded wallet", "Success Message", Messagebox.OK, Messagebox.INFORMATION);
      	  }
		}
		
	}
	
	
	/**
	 * this method will call while uploading file
	 *
	 * @param uploadEvent
	 * @throws FileNotFoundException 
	 */
	@ViewEvent(componentID = "upload_button_for_activate_and_deactivate", eventName = Events.ON_UPLOAD)
	public void uploadWalletAccountsActivateAndDeactivate(final UploadEvent uploadEvent) throws FileNotFoundException
	{
		textbox.setText("");
		emptyFileError.setVisible(false);
		media = uploadEvent.getMedia();
		final String fileName = media.getName();
		
		boolean messageFlag=false;
		if (fileName.endsWith(CSV) || fileName.endsWith(XLSX))
		{
			System.out.println("**********************File Name :"+fileName);
			List<CilqCashWalletPojo> cilqCashWalletPojoList = new ArrayList<CilqCashWalletPojo>();
			  Scanner scanner = new Scanner(media.getStreamData());
			  try{
	        while (scanner.hasNext()) {
	      	  CilqCashWalletPojo pojo = new CilqCashWalletPojo();
	            List<String> line = CSVFileReaderUtils.parseLine(scanner.nextLine());
	            try{
	            pojo.setWalletId(line.get(0));
	            pojo.setCustomerEmailId(line.get(1));
	            pojo.setRemarks(line.get(2));
	            if(null != line.get(1) && !line.get(1).isEmpty() && line.get(1).contains("@")){
	            cilqCashWalletPojoList.add(pojo);
	            }
	            }catch(Exception e){
	            	Messagebox.show("Please upload file valid format like  Customer Wallet Id ,Customer Email ,Remarks ", "Error Message", Messagebox.OK, Messagebox.ERROR);
	            	messageFlag=true;
	            }
	        }
			  }catch(Exception eObj){
				  eObj.printStackTrace();
			  }finally{
				  scanner.close();
			  }
	        PrintWriter pw = null;
	      
	        String fileUploadLocation =null;
	        String fileUploadName =null;
	        if (null != configurationService)
				{
					fileUploadLocation = configurationService.getConfiguration().getString("mpl.wallet.bulkuploadfileLocation");
					fileUploadName =  configurationService.getConfiguration().getString("mpl.wallet.bulkupload.activateanddeactivate.filename");
				   if(null != fileUploadLocation && !fileUploadLocation.isEmpty() && null!=fileUploadName ){
				   	  try {
				   		  Path path = Paths.get(fileUploadLocation);
				           //if directory exists?
				           if (!Files.exists(path)) {
				               try {
				                   Files.createDirectories(path);
				               } catch (IOException e) {
				                   //fail to create directory
				               	 LOG.error("Exception Occer While Creating the File Location :"+e.getMessage());
				               }
				           }
			      	      pw = new PrintWriter(new File(fileUploadLocation+fileUploadName));
			      	      StringBuilder  builder=activateAndDeactivateQCAccounts(cilqCashWalletPojoList,pw);
			    	         pw.write(builder.toString());
			          	   pw.close();
			      	  } catch (FileNotFoundException e) {
			      	      e.printStackTrace();
			      	  }finally{
			      		  pw.close();
			      	  }
				   }
				}
      	  System.out.println("successfully created File..........");
      	  if(!messageFlag){
      	  Messagebox.show("SuccessFully Uploaded wallet", "Success Message", Messagebox.OK, Messagebox.INFORMATION);
      	  }
		}
	}
   
   
   private StringBuilder activateAndDeactivateQCAccounts(List<CilqCashWalletPojo> cilqCashWalletPojoList, PrintWriter pw){
   	StringBuilder  builder = new StringBuilder();
   	  boolean  headerCheck = false ;
        boolean  responseCheck = false ;
        String commentMsg = "";
   		CustomerWalletDetailResponse response = null;
   		
   	 for(CilqCashWalletPojo walletObj:cilqCashWalletPojoList){
     	  CustomerModel currentCustomer =  extendedUserService.getUserForOriginalUid(walletObj.getCustomerEmailId());
     	  if(null!= currentCustomer  ){
     		  LOG.debug("First Name :"+currentCustomer.getDisplayName());
        	  if (null != currentCustomer.getIsWalletActivated() && currentCustomer.getIsWalletActivated().booleanValue()){
        		  if(walletObj.getRemarks().equalsIgnoreCase("Deactivate")){
        		  LOG.debug("Customer Have Active wallet account Id :"+currentCustomer.getCustomerWalletDetail().getWalletId());
        		  response =  mplWalletFacade.deactivateQCUserAccount(currentCustomer.getCustomerWalletDetail().getWalletId());
        		  if(response.getResponseCode()== 0){
        			 commentMsg =SUCCESS_MSG;
        			 currentCustomer.setIsWalletActivated(Boolean.FALSE);
        			modelService.save(currentCustomer);
        		  }else{
        			 commentMsg = qcErrorMasseges(response.getResponseCode());
        		  }
        		  }
     	  }else if(null != currentCustomer.getIsWalletActivated() && currentCustomer.getIsWalletActivated().equals(Boolean.FALSE)){
     		LOG.debug("Customer Have Deactive wallet account Id :"+currentCustomer.getCustomerWalletDetail().getWalletId());
     	 if(walletObj.getRemarks().equalsIgnoreCase("Activate")){
   		  response =  mplWalletFacade.activateQCUserAccount(currentCustomer.getCustomerWalletDetail().getWalletId());
   		  if(response.getResponseCode()== 0){
   			  commentMsg =SUCCESS_MSG;
   			  currentCustomer.setIsWalletActivated(Boolean.TRUE);
       			modelService.save(currentCustomer);
   			  
   		  }else{
   			  commentMsg = qcErrorMasseges(response.getResponseCode());
   		  }  
     	   }
     	  }else{
     		  commentMsg= USER_DOES_NOT_MSG;
     		  responseCheck=true;
     		  
     	  }
        	  
        	if(!responseCheck){
      		  if(null != response.getWallet() && null!= response.getWallet().getWalletNumber()){
 			  builder.append(response.getWallet().getWalletNumber()+",");
      		  }else{
      			 builder.append(",");
      		  }
 			  builder.append(walletObj.getCustomerEmailId() +",");
 			  builder.append(walletObj.getRemarks() +",");
       	  }else{
       		  builder.append(""+",");
      	  	     builder.append(String.valueOf(""+","));
      	  	     builder.append(String.valueOf(""+","));
       	  }
 		  builder.append(commentMsg);
 		  builder.append('\n');
       }
   	}
   	 return builder;
   }
   
	@ViewEvent(componentID = "savecsv", eventName = Events.ON_CLICK)
	public void getUploadedCsv() throws InterruptedException, FileNotFoundException
	{
     System.out.println("**********getUploadedCsv***********");
     String fileUploadLocation =null;
     String fileDownloadName =null;
     if (null != configurationService)
		{
			fileUploadLocation = configurationService.getConfiguration().getString("mpl.wallet.bulkuploadfileLocation");
			fileDownloadName =  configurationService.getConfiguration().getString("mpl.wallet.bulkuploadfilename");
		   if(null != fileUploadLocation && !fileUploadLocation.isEmpty() && null!=fileDownloadName){
		   	  final File file = new File(fileUploadLocation.trim()+fileDownloadName);
					if (file.exists()){
						Filedownload.save(file, null);
					}
		   }
		}
     System.out.println("Successfully.....Downloading file...");
	}
	
	
	@ViewEvent(componentID = "cancelcsv", eventName = Events.ON_CLICK)
	public void getCancelCsv() throws InterruptedException, FileNotFoundException
	{
     System.out.println("*********getCancelCsv************");
     String fileUploadLocation =null;
     String fileDownloadName =null;
     if (null != configurationService)
		{
			fileUploadLocation = configurationService.getConfiguration().getString("mpl.wallet.bulkuploadfileLocation");
			fileDownloadName =  configurationService.getConfiguration().getString("mpl.wallet.bulkdownloadfilename");
		   if(null != fileUploadLocation && !fileUploadLocation.isEmpty() && null!=fileDownloadName){
		   	  final File file = new File(fileUploadLocation.trim()+fileDownloadName);
					if (file.exists()){
						Filedownload.save(file, null);
					}
		   }
		}
     System.out.println("Successfully.....Downloading file...");
	}

   @ViewEvent(componentID = "activeanddeactiveaccountscsv", eventName = Events.ON_CLICK)
	public void qcAccountActiveAndDeactiveUploadedCsv() throws InterruptedException, FileNotFoundException
	{
     System.out.println("**********getUploadedCsv***********");
     String fileUploadLocation =null;
     String fileDownloadName =null;
     if (null != configurationService)
		{
			fileUploadLocation = configurationService.getConfiguration().getString("mpl.wallet.bulkuploadfileLocation");
			fileDownloadName =  configurationService.getConfiguration().getString("mpl.wallet.bulkupload.activateanddeactivate.filename");
		   if(null != fileUploadLocation && !fileUploadLocation.isEmpty() && null!=fileDownloadName){
		   	  final File file = new File(fileUploadLocation.trim()+fileDownloadName);
					if (file.exists()){
						Filedownload.save(file, null);
					}
		   }
		}
     System.out.println("Successfully.....Downloading file...");
	}
}
