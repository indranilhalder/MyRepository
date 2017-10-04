/**
 *
 */
package com.techouts.backoffice.widget.controller;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.hybris.oms.tata.services.FilePathProviderService;
import com.hybris.oms.tata.tship.exceltocsv.pojo.CilqCashWalletPojo;
import com.tisl.mpl.core.model.CustomerWalletDetailModel;
import com.tisl.mpl.facades.account.register.RegisterCustomerFacade;
import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.pojo.request.Customer;
import com.tisl.mpl.pojo.request.QCCustomerPromotionRequest;
import com.tisl.mpl.pojo.request.QCCustomerRegisterRequest;
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
	private static final char DEFAULT_SEPARATOR = ',';
   private static final char DEFAULT_QUOTE = '"';
   
   
 

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
		if (fileName.endsWith(CSV) || fileName.endsWith(".xlsx"))
		{
			System.out.println("**********************File Name :"+fileName);
			List<CilqCashWalletPojo> cilqCashWalletPojoList = new ArrayList<CilqCashWalletPojo>();
			  Scanner scanner = new Scanner(media.getStreamData());
			  try{
	        while (scanner.hasNext()) {
	      	  CilqCashWalletPojo pojo = new CilqCashWalletPojo();
	            List<String> line = parseLine(scanner.nextLine());
	            try{
	            pojo.setCustomerName(line.get(0));
	            pojo.setCustomerEmailId(line.get(1));
	            pojo.setAmount(line.get(2));
	            pojo.setBucketName(line.get(3));
	            if(null != line.get(1) && !line.get(1).isEmpty() && line.get(1).contains("@")){
	            cilqCashWalletPojoList.add(pojo);
	            }
	            }catch(Exception e){
	            	Messagebox.show("Please upload file valid format like  Customer Name ,Customer Email ,Amount , Bucket Name ", "Error Message", Messagebox.OK, Messagebox.ERROR);
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
					fileUploadName =  configurationService.getConfiguration().getString("mpl.wallet.bulkuploadfilename");
				   if(null != fileUploadLocation && !fileUploadLocation.isEmpty() && null!=fileUploadName ){
				   	  try {
			      	      pw = new PrintWriter(new File(fileUploadLocation+fileUploadName));
			      	      StringBuilder  builder=uploadWallettCashFile(cilqCashWalletPojoList,pw);
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
private StringBuilder uploadWallettCashFile(List<CilqCashWalletPojo> cilqCashWalletPojoList, PrintWriter pw){
	StringBuilder  builder = new StringBuilder();
	  boolean  headerCheck = false ;
     boolean  responseCheck = false ;
     String commentMsg = "";
		QCRedeeptionResponse response = null;
		
	 for(CilqCashWalletPojo walletObj:cilqCashWalletPojoList){
  	  CustomerModel currentCustomer =  extendedUserService.getUserForOriginalUid(walletObj.getCustomerEmailId());
  	  if(null!= currentCustomer  ){
  		  LOG.debug("First Name :"+currentCustomer.getDisplayName());
     	  if (null != currentCustomer.getIsWalletActivated() && currentCustomer.getIsWalletActivated().booleanValue()){
     		  QCCustomerPromotionRequest request = new QCCustomerPromotionRequest();
     		  request.setAmount(Double.valueOf(walletObj.getAmount()));
     		  if(walletObj.getBucketName().equalsIgnoreCase("PROMOTION")){
     		  request.setNotes("Sample load for "+ walletObj.getAmount() +"  PROMOTION");
     		  response =  mplWalletFacade.createPromotion(currentCustomer.getCustomerWalletDetail().getWalletId() ,request);
     		  }else if (walletObj.getBucketName().equalsIgnoreCase("CASHBACK")){
     			  request.setNotes("Sample load for "+ walletObj.getAmount() +"  CASHBACK");
     			  response =  mplWalletFacade.addTULWalletCashBack(currentCustomer.getCustomerWalletDetail().getWalletId() ,request);
     		  }
     		  if(response.getResponseCode()== 0){
     			  commentMsg ="Success";
     		  }else{
     				 commentMsg ="Failure";
     				}
     	  }else{
     		  try{
    				final QCCustomerRegisterRequest customerRegisterReq = new QCCustomerRegisterRequest();
    				final Customer custInfo = new Customer();
    				custInfo.setEmail(currentCustomer.getOriginalUid());
    				custInfo.setEmployeeID(currentCustomer.getUid());
    				custInfo.setCorporateName("Tata Unistore Ltd");
    				
    				if (null != currentCustomer.getFirstName()){
    					custInfo.setFirstname(currentCustomer.getFirstName());
    				}if (null != currentCustomer.getLastName()){
    					custInfo.setLastName(currentCustomer.getLastName());
    				}

    				customerRegisterReq.setExternalwalletid(currentCustomer.getOriginalUid());
    				customerRegisterReq.setCustomer(custInfo);
    				customerRegisterReq.setNotes("Activating Customer " + currentCustomer.getOriginalUid());
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
    					currentCustomer.setIsWalletActivated(true);
    					modelService.save(currentCustomer);
    					
    					QCCustomerPromotionRequest request = new QCCustomerPromotionRequest();
  	      		  request.setAmount(Double.valueOf(walletObj.getAmount()));
  	      		  if(walletObj.getBucketName().equalsIgnoreCase("PROMOTION")){
  	      		  request.setNotes("Sample load for "+ walletObj.getAmount() +"  PROMOTION");
  	      		  response =  mplWalletFacade.createPromotion(currentCustomer.getCustomerWalletDetail().getWalletId() ,request);
  	      		  }else if (walletObj.getBucketName().equalsIgnoreCase("CASHBACK")){
  	      			  request.setNotes("Sample load for "+ walletObj.getAmount() +"  CASHBACK");
  	      			  response =  mplWalletFacade.addTULWalletCashBack(currentCustomer.getCustomerWalletDetail().getWalletId() ,request);
  	      		  } 
  	      		  commentMsg ="Success";
    				}else{
    				 commentMsg ="Failure";
    				}
    			}catch (final Exception ex){
    				ex.printStackTrace();
    			}
     	  }
     	 
     	  
  	  }else{
  		  commentMsg= "user does not exist";
  		  responseCheck=true;
  		  
  	  }
  	  CilqCashWalletPojo pojo= new CilqCashWalletPojo();
  	
  	  
  	  if(!responseCheck){
  	  pojo.setWalletId(response.getWalletNumber());
  	  pojo.setTransactionId(String.valueOf(response.getTransactionId()));
  	  pojo.setBatchNumber(String.valueOf(response.getBatchNumber()));
     	  if(null != response.getCards() && response.getCards().size()>0){
     		  pojo.setCardExpiry(response.getCards().get(0).getExpiry());
     		  pojo.setCardNumber(response.getCards().get(0).getCardNumber());
     	  }
  	  }else{
  		  pojo.setWalletId("");
  		  pojo.setTransactionId(String.valueOf(""));
     	  pojo.setBatchNumber(String.valueOf(""));
     	  pojo.setCardExpiry("");
     	  pojo.setCardNumber("");
  	  }
  	  pojo.setAmount(walletObj.getAmount());
  	  pojo.setBucketName(walletObj.getBucketName());
  	 
  	  pojo.setCustomerEmailId(walletObj.getCustomerEmailId());
  	  pojo.setCustomerName(walletObj.getCustomerName());
  	  pojo.setComments(commentMsg);

  	 
  	  String columnNamesList =null;
  	  if(!headerCheck){
  		  columnNamesList = "Customer Name,Email ID,Amount,Bucket name,Transaction ID,Wallet ID,Card Number,Card Expiry,Batch Number,Comments";
  		  builder.append(columnNamesList +"\n");
  		  headerCheck=true;
  	  }
  	 
  	  builder.append(pojo.getCustomerName()+",");
  	  builder.append(pojo.getCustomerEmailId()+",");
  	  builder.append(pojo.getAmount()+",");
  	  builder.append(pojo.getBucketName()+",");
  	  builder.append(pojo.getTransactionId()+",");
  	  builder.append(pojo.getWalletId()+",");
  	  builder.append(pojo.getCardNumber()+",");
  	  builder.append(pojo.getCardExpiry()+",");
  	  builder.append(pojo.getBatchNumber()+",");
  	  builder.append(pojo.getComments());
  	  builder.append('\n');
      	 
    }
	 return builder;
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
	//	QCRedeeptionResponse response = null;
		if (fileName.endsWith(CSV) || fileName.endsWith(".xlsx"))
		{
			List<CilqCashWalletPojo> cilqCashWalletPojoList = new ArrayList<CilqCashWalletPojo>();
			  Scanner scanner = new Scanner(media.getStreamData());
	        while (scanner.hasNext()) {
	      	  CilqCashWalletPojo pojo = new CilqCashWalletPojo();
	            List<String> line = parseLine(scanner.nextLine());
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
	        System.out.println("cilqCashWalletPojoList :"+cilqCashWalletPojoList);
	        System.out.println("cilqCashWalletPojoList size:"+cilqCashWalletPojoList.size());
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
	    	      		 if (responseObj.getResponseCode() == 0)
	   	     				{
	    	      			commentMsg= "Success";
	   	     				}else{
	   	     					commentMsg= "Failed";
	   	     				}
	    	      	  }else{
	    	      		 commentMsg= "user does not have wallet Id";
	    	      	  }
	      	   }else{
		      		  commentMsg= "user does not exist";
		      		  responseCheck=true;
		      		  
		      	  }
	      	   CilqCashWalletPojo pojo= new CilqCashWalletPojo();
		      	
		      	  
		      	  if(!responseCheck){
		      	  pojo.setWalletId(responseObj.getWalletNumber());
		      	  pojo.setTransactionId(String.valueOf(responseObj.getTransactionId()));
		      	  pojo.setBatchNumber(String.valueOf(responseObj.getBatchNumber()));
		      	  }else{
		      		  pojo.setWalletId("");
		      		  pojo.setTransactionId(String.valueOf(""));
			      	  pojo.setBatchNumber(String.valueOf(""));
		      	  }
		      	  pojo.setCustomerEmailId(walletObj.getCustomerEmailId());
		      	  pojo.setComments(commentMsg);
		      	  
		      	  String columnNamesList =null;
		      	  if(!headerCheck){
		      		  columnNamesList = "Customer Email ID,Transaction ID,Wallet ID, BatchNumber , Comments";
		      		  builder.append(columnNamesList +"\n");
		      		  headerCheck=true;
		      	  }
		      
		      	  builder.append(pojo.getCustomerEmailId()+","); 
		      	  builder.append(pojo.getTransactionId()+",");
		      	  builder.append(pojo.getWalletId()+",");
		      	  builder.append(pojo.getBatchNumber()+",");
		      	  builder.append(pojo.getComments());
		      	  builder.append('\n');
      	  }
      	  pw.write(builder.toString());
      	  pw.close();
      	  System.out.println("successfully created File..........");
      	  if(!messageFlag){
      	  Messagebox.show("SuccessFully Uploaded wallet", "Success Message", Messagebox.OK, Messagebox.INFORMATION);
      	  }
		}
		
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
	 public  List<String> parseLine(String cvsLine) {
       return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
   }
   
   @SuppressWarnings("null")
	public  List<String> parseLine(String cvsLine, char separators, char customQuote) {

      List<String> result = new ArrayList<>();
      if (cvsLine == null && cvsLine.isEmpty()) {
          return result;
      }if (customQuote == ' ') {
          customQuote = DEFAULT_QUOTE;
      } if (separators == ' ') {
          separators = DEFAULT_SEPARATOR;
      }

      StringBuffer curVal = new StringBuffer();
      boolean inQuotes = false;
      boolean startCollectChar = false;
      boolean doubleQuotesInColumn = false;

      char[] chars = cvsLine.toCharArray();
      for (char ch : chars) {

          if (inQuotes) {
              startCollectChar = true;
              if (ch == customQuote) {
                  inQuotes = false;
                  doubleQuotesInColumn = false;
              } else {

                  //Fixed : allow "" in custom quote enclosed
                  if (ch == '\"') {
                      if (!doubleQuotesInColumn) {
                          curVal.append(ch);
                          doubleQuotesInColumn = true;
                      }
                  } else {
                      curVal.append(ch);
                  }

              }
          } else {
              if (ch == customQuote) {

                  inQuotes = true;

                  //Fixed : allow "" in empty quote enclosed
                  if (chars[0] != '"' && customQuote == '\"') {
                      curVal.append('"');
                  }

                  //double quotes in column will hit this!
                  if (startCollectChar) {
                      curVal.append('"');
                  }

              } else if (ch == separators) {

                  result.add(curVal.toString());

                  curVal = new StringBuffer();
                  startCollectChar = false;

              } else if (ch == '\r') {
                  //ignore LF characters
                  continue;
              } else if (ch == '\n') {
                  //the end, break!
                  break;
              } else {
                  curVal.append(ch);
              }
          }

      }

      result.add(curVal.toString());

      return result;
  }

}
