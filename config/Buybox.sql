create or replace
PACKAGE BODY "buybox_package"
AS
   /**************************************************************************************
   /*  Procedure will calculate and update the buybox weightage for the price/inventory updated skus
   /*  It has to be invoked periodically 
   /*  Delta will be identified by modifiedts column of price/stocklevels table
   /****************************************************************************************/

   PROCEDURE buybox_weightage
   IS
      v_last_run_weightage         TIMESTAMP;
      v_prc_start_time_weightage   TIMESTAMP;
      v_typepk_buybox              NUMBER;
      v_itemtypecode_buybox        NUMBER;
      v_getpkfunction_buybox       NUMBER;
      -- v_typepk_delta               NUMBER;
      v_price_weightage            NUMBER;
      v_jprice_weightage            NUMBER;
      v_inv_weightage              NUMBER;
      v_catalogversion_buybox      NUMBER;
      v_mergecount                 NUMBER;
	    v_pricefallbackpk            NUMBER;
		--Added for jewellery
	    v_jewellerycount             NUMBER;
   -- TYPE t_buyboxussid IS TABLE OF MplBuyBoxProcTable.P_SELLERARTICLESKU%TYPE;


   -- v_t_buyboxussid                 t_buyboxussid;


   BEGIN
	  -- assign the last run time value into local variable
      SELECT MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME
        INTO v_last_run_weightage
        FROM MPLBUYBOXUPDTLOG
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'buybox_weightage';

      -- assign the buybox typepkstring value into local variable
      SELECT pk
        INTO v_typepk_buybox
        FROM composedtypes
       WHERE internalcode = 'BuyBoxProcTable';

      -- assign the buybox itemtypecode value into local variable
      SELECT itemtypecode
        INTO v_itemtypecode_buybox
        FROM composedtypes
       WHERE internalcode = 'BuyBoxProcTable';

      -- assign the deltabuybox typepkstring value into local variable
      --SELECT pk
      -- INTO v_typepk_delta
      -- FROM composedtypes
      -- WHERE internalcode = 'delta_buybox';

      -- assign the price weightage value into local variable
      SELECT p_weightagevalue
        INTO v_price_weightage
        FROM mplbbweightage
       WHERE p_weigtagetype = 'priceweightage';

      -- assign the inventory weightage value into local variable
      SELECT p_weightagevalue
        INTO v_inv_weightage
        FROM mplbbweightage
       WHERE p_weigtagetype = 'invweightage';
	   
	   ---- Jewellery weightage
	   SELECT p_weightagevalue
        INTO v_jprice_weightage
        FROM mplbbweightage
       WHERE p_weigtagetype = 'jpriceweightage';

      -- assign the current timestamp value into local variable
      SELECT TO_TIMESTAMP (
                TO_CHAR (SYSTIMESTAMP, 'rrrr-MM-DD hh24:mi:ss.FF3'),
                'rrrr-MM-DD hh24:mi:ss.FF3')
        INTO v_prc_start_time_weightage
        FROM DUAL;

      -- assign the product catalog version value into local variable
      SELECT CV.pk
        INTO v_catalogversion_buybox
        FROM catalogs c, catalogversions CV
       WHERE     c.p_id = 'mplProductCatalog'
             AND c.pk = CV.p_catalog
             AND CV.p_version = 'Online';
			 -- assign the pricefallbackpk value into local variable
	  SELECT pk
	     INTO v_pricefallbackpk
		 FROM PRODUCTS
		 WHERE PRODUCTS.P_CODE='PRICEFALLBACK' and PRODUCTS.p_catalogversion = v_catalogversion_buybox;

      --add sellerarticleskus to temp table for which priority rule becomes inactive
      --INSERT INTO TEMP_PRIORITY_LEVEL(P_USSID) SELECT a.P_USSID
      --from priorityleveldetails a
      --  where a.P_ISVALIDPRIORITY=0
      --  and a.modifiedts > v_last_run_weightage;

      -- assign the count value into local variable
      SELECT COUNT (*)
        INTO v_mergecount
        FROM pricerows P,
             stocklevels I,
             mplsellerinfo SI,
             products PR,
             mplpromotionalpricerow pp1,
             mplpromotionalpricerow pp2,
             mplpriorityleveldetails PLD
       WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
             AND P.p_sellerarticlesku = SI.p_sellerarticlesku
             AND PLD.p_ussid(+) = SI.p_sellerarticlesku
             AND P.PK = pp1.p_pricerow(+)
             AND P.PK = pp2.p_pricerow(+)
             AND P.p_product = PR.PK
             AND   SI.p_hasVariant is NULL
			 AND P.p_product<>v_pricefallbackpk
             AND P.p_catalogversion = v_catalogversion_buybox
             AND SI.p_catalogversion = v_catalogversion_buybox
             AND (   I.modifiedts > v_last_run_weightage
                  OR P.modifiedts > v_last_run_weightage
                  OR PLD.modifiedts > v_last_run_weightage
				  --Changes for Delisting 27_02_17
           OR pp1.modifiedts > v_last_run_weightage
           OR pp2.modifiedts > v_last_run_weightage
				   OR SI.P_DELISTDATE > v_last_run_weightage
				  );
				  
					  
		-- changes made for jewellery		  
				  SELECT COUNT (*)
			INTO v_jewellerycount
			FROM pricerows P,
				 stocklevels I,
				 mplsellerinfo SI,
				MPLJewelleryInfo JI,
				 products PR,
				 mplpromotionalpricerow pp1,
				mplpromotionalpricerow pp2,
				 mplpriorityleveldetails PLD
		 WHERE P.p_sellerarticlesku = I.p_sellerarticlesku
     AND   SI.p_ussid = JI.P_PCMUSSID
     AND   P.p_sellerarticlesku = JI.P_USSID
     AND   P.p_product = PR.PK
     AND   SI.p_hasVariant=1
	 AND P.PK = pp1.p_pricerow(+)
     AND P.PK = pp2.p_pricerow(+)
	  AND P.p_product<>v_pricefallbackpk
     AND   PLD.p_ussid(+) = SI.p_sellerarticlesku
     AND   P.p_catalogversion = v_catalogversion_buybox
     AND   SI.p_catalogversion = v_catalogversion_buybox
     AND   (I.modifiedts > v_last_run_weightage
                   OR P.modifiedts > v_last_run_weightage
                   OR PLD.modifiedts > v_last_run_weightage
				   --Changes for Delisting
				   OR pp1.modifiedts > v_last_run_weightage
				   OR pp2.modifiedts > v_last_run_weightage
                   OR SI.P_DELISTDATE > v_last_run_weightage
                   OR  JI.modifiedts > v_last_run_weightage);
				  
          
         ------ Start INC144314752 OOS issue 
		 ------Added attribute p_oosmodifiedval PRDI-50
		 ------Removed the current snippet and added it to CAR-302/CAR-303
   /* UPDATE MplBuyBoxProcTable SET MplBuyBoxProcTable.modifiedts=v_prc_start_time_weightage,MplBuyBoxProcTable.p_oosmodifiedval=v_prc_start_time_weightage where pk in (
   SELECT distinct
          bbox1.pk as pk
          from products p1, 
          products p2,
          MplBuyBoxProcTable bbox1,
          MplBuyBoxProcTable bbox2,
          stocklevels I
          WHERE
          p1.p_catalogversion = v_catalogversion_buybox
          and p2.p_catalogversion = v_catalogversion_buybox
          and p1.p_colour=p2.p_colour 
          and p1.p_baseproduct=p2.p_baseproduct 
          and p1.p_code=bbox1.p_product
          and p2.p_code=bbox2.p_product
          and I.p_sellerarticlesku=bbox2.p_sellerarticlesku
          and ((I.p_available>0 and bbox2.p_available<=0)
         or (I.p_available<=0 and bbox2.p_available>0))
          and I.modifiedts > v_last_run_weightage); */
    ------ INC144314752 OOS issue End           
	COMMIT;
  --Joins Price,Inventory,Delta tables and merge the result data into buybox table
	--CHANGES FOR JEWELLERY  
	  IF (v_jewellerycount > 0)
		THEN
		MERGE INTO MplBuyBoxProcTable M                                                          
		       USING ( SELECT jbb.*,
                  (CASE
                      WHEN (specialprice = 0) 
                   THEN p_price
                    ELSE specialprice
                   END) weightage_price,
			   (CASE
                      WHEN (mobilespecialprice = 0) THEN p_price
                      ELSE mobilespecialprice
                      END)
                      weightage_mobileprice
                   FROM(SELECT DISTINCT
                                     CASE 
                                     WHEN (SI.p_hasVariant=1)
                                     THEN
                                     JI.P_USSID								 
				                     END
                                      sellerarticlesku,
                                    NVL((SELECT p_specialprice FROM MPLPROMOTIONALPRICEROW WHERE (p_promotionchannel='Web' OR p_promotionchannel is null)
                                       AND v_prc_start_time_weightage BETWEEN p_promotionstartdate AND p_promotionenddate AND P.PK = p_pricerow AND ROWNUM=1),0)                                       
                                       specialprice,
                                      NVL((SELECT p_specialprice FROM MPLPROMOTIONALPRICEROW WHERE (p_promotionchannel='Mobile' OR p_promotionchannel is null)
                                       AND v_prc_start_time_weightage BETWEEN p_promotionstartdate AND p_promotionenddate AND P.PK = p_pricerow AND ROWNUM=1),0)                                       
                                       mobilespecialprice,

                                    PR.p_code p_code,
                                    P.p_price p_price,
                                    P.p_promotionstartdate p_promotionstartdate,
                                    P.p_promotionenddate p_promotionenddate,
                                    P.p_mrp p_mrp,
                                    I.p_available p_available,
                                    SI.p_sellerid p_sellerid,
                                    SI.p_sellername p_sellername,
									SI.p_sellertype p_sellertype,
									SI.p_startdate p_startdate,
                                    SI.P_enddate,
                                     NVL (GREATEST (PLD.P_L1PRIORITY,
                                                     PLD.P_L2PRIORITY,
                                                     PLD.P_L3PRIORITY,
                                                     PLD.P_L4PRIORITY,
                                                     PLD.P_PRODUCTPRIORITY),
                                           0)
                                    * NVL (P_ISVALIDPRIORITY, 0)

                                       AS maxVal,
									   EV.sequencenumber AS status, --Changes for Delisting 27_02_17
                                    JI.p_pcmussid as PCMUSSID, -- jewellery
									CASE 
									WHEN ( p.P_PRICE ! = 0 AND ((p.P_SPECIALPRICE = 0)OR(p.P_SPECIALPRICE is null) OR(v_prc_start_time_weightage NOT BETWEEN P.p_promotionstartdate AND P.p_promotionenddate)))
									THEN 
									(select max(p.P_PRICE) from pricerows p, MPLJewelleryInfo ji 
									Where P.p_sellerarticlesku = ji.p_ussid AND SI.P_USSID = ji.p_PCMUSSID)
									WHEN(((p.P_PRICE = 0 OR p.P_PRICE is null) AND (p.P_SPECIALPRICE = 0 OR p.P_SPECIALPRICE is null)) AND p.P_MRP != 0 )
									THEN
									(select max(p.P_MRP) from pricerows p, MPLJewelleryInfo ji 
									Where P.p_sellerarticlesku = ji.p_ussid AND SI.P_USSID = ji.p_PCMUSSID)
									END
									plpmax,
									CASE 
									WHEN ( p.P_PRICE ! = 0 AND ((p.P_SPECIALPRICE = 0)OR(p.P_SPECIALPRICE is null) OR(v_prc_start_time_weightage NOT BETWEEN P.p_promotionstartdate AND P.p_promotionenddate)))
									THEN 
									(select min(p.P_PRICE) from pricerows p, MPLJewelleryInfo ji 
									Where P.p_sellerarticlesku = ji.p_ussid AND SI.P_USSID = ji.p_PCMUSSID)
									WHEN(((p.P_PRICE = 0 OR p.P_PRICE is null) AND (p.P_SPECIALPRICE = 0 OR p.P_SPECIALPRICE is null)) AND p.P_MRP != 0 )
									THEN
									(select min(p.P_MRP) from pricerows p, MPLJewelleryInfo ji 
									Where P.p_sellerarticlesku = ji.p_ussid AND SI.P_USSID = ji.p_PCMUSSID)
									END
								    plpmin
                                FROM pricerows P,
                                     stocklevels I,
                                     mplsellerinfo SI,
                                     products PR,
                                     mplpriorityleveldetails PLD,
                                     enumerationvalues EV,--Changes For Delisting
                                     MPLJewelleryInfo JI --for jewellery
                                 WHERE P.p_sellerarticlesku = I.p_sellerarticlesku
								 AND P.p_product<>v_pricefallbackpk
                                    AND JI.P_PCMUSSID = SI.p_sellerarticlesku
                                    AND PLD.p_ussid(+) = SI.P_USSID
                                    AND P.p_product = PR.PK
								 	AND SI.P_SELLERASSOCIATIONSTATUS = EV.pk
									AND  JI.P_USSID=P.p_sellerarticlesku--for jewellery
                                    AND JI.P_USSID=I.p_sellerarticlesku--for jewellery
                                    AND JI.P_PCMUSSID = PLD.p_ussid(+)
									AND P.p_catalogversion = v_catalogversion_buybox
                                    AND SI.p_catalogversion = v_catalogversion_buybox
                                    AND (I.modifiedts > v_last_run_weightage
                                         OR P.modifiedts > v_last_run_weightage
                                         OR PLD.modifiedts > v_last_run_weightage
                                         OR JI.modifiedts > v_last_run_weightage) ) jbb) Q
          ON (M.p_sellerarticlesku = Q.sellerarticlesku)
				  WHEN MATCHED
          THEN
            
             UPDATE SET
			   M.p_product = Q.p_code,
               M.p_price = Q.p_price,
               M.p_specialprice = NVL (Q.specialprice, 0),
			   M.p_specialpricemobile = NVL(Q.mobilespecialprice,0),
               M.p_mrp = Q.p_mrp,
               M.p_available = Q.p_available,
               M.p_sellername = Q.p_sellername,
               M.p_sellerstartdate = Q.p_startdate,
               M.p_sellerenddate = Q.P_enddate,
               M.modifiedts = v_prc_start_time_weightage,
               M.p_weightage = v_jprice_weightage * Q.weightage_price + Q.maxVal,
			   M.p_weightagemobile = v_jprice_weightage * Q.weightage_mobileprice + Q.maxVal,
			   M.p_delisted = Q.status, --Changes for Delisting
               M.P_PUSSID = Q.PCMUSSID,--for jewellery
			   M.P_PLPMAXPRICE = Q.plpmax,--for jewellery
			   M.P_PLPMINPRICE = Q.plpmin--for jewellery
				  
	      WHEN NOT MATCHED
         THEN
            INSERT     (M.hjmpts,
                        M.pk,
						M.p_sellerarticlesku,
						M.p_product,
                        M.p_price,
                        M.p_specialprice,
                        M.p_mrp,
                        M.p_available,
                        M.p_weightage,
                        M.typepkstring,
                        M.p_sellerid,
                        M.p_sellername,
                        M.p_sellertype,
                        M.p_sellerstartdate,
                        M.p_sellerenddate,
                        M.modifiedts,
                        M.createdts,
                        M.p_delisted,                     /* D-list changes */
						M.aclts,
						M.propts,
						M.p_specialpricemobile,
                        M.p_weightagemobile,
                        M.p_PUSSID,--for jewellery
						M.P_PLPMAXPRICE,-- for jewellery
						M.P_PLPMINPRICE-- for jewellery
                        )
                VALUES ('0',
                        GETPK (v_itemtypecode_buybox, BUYBOX_SEQ.NEXTVAL),
						Q.sellerarticlesku,
						Q.p_code,
                        Q.p_price,
                        NVL (Q.specialprice, 0),
                        Q.p_mrp,
                        Q.p_available,
                        v_jprice_weightage * Q.weightage_price + Q.maxVal,
                        --      + v_inv_weightage * S.p_available,
                        v_typepk_buybox,
                        Q.p_sellerid,
                        Q.p_sellername,
                        Q.p_sellertype,
                        Q.p_startdate,
                        Q.p_enddate,
                        v_prc_start_time_weightage,
                        v_prc_start_time_weightage,
                        Q.status,--Changes for Delisting
						'0',
                        '0',
                        NVL(Q.mobilespecialprice,0),
                        v_jprice_weightage * Q.weightage_mobileprice + Q.maxVal,
                        Q.PCMUSSID,--for jewellery
						Q.plpmax,--for jewellery
						Q.plpmin--for jewellery
                        );
      
      
      END IF;
      COMMIT;
	--	 END OF CHANGES FOR JEWELLERY  
      IF (v_mergecount > 0)
      THEN
         --Joins Price,Inventory,Delta tables and merge the result data into buybox table
         MERGE INTO MplBuyBoxProcTable B
              USING (SELECT distinct bb.*,
                            (CASE
                                WHEN (specialprice = 0) THEN p_price
                                ELSE specialprice
                             END)
                               weightage_price,
                              (CASE
                                WHEN (mobilespecialprice = 0) THEN p_price
                                ELSE mobilespecialprice
                             END)
                               weightage_mobileprice
                       FROM (SELECT DISTINCT
                                    P.p_sellerarticlesku,
                                    PR.p_code,
                                    P.p_price,
                                    NVL((SELECT p_specialprice FROM MPLPROMOTIONALPRICEROW p1 WHERE (p1.p_promotionchannel='Web' OR p1.p_promotionchannel is null)
                                       AND v_prc_start_time_weightage BETWEEN p_promotionstartdate AND p_promotionenddate AND P.PK = p1.p_pricerow AND ROWNUM=1),0)                                       
                                       specialprice,
                                      NVL((SELECT p_specialprice FROM MPLPROMOTIONALPRICEROW p1 WHERE (p1.p_promotionchannel='Mobile' OR p1.p_promotionchannel is null)
                                       AND v_prc_start_time_weightage BETWEEN p_promotionstartdate AND p_promotionenddate AND P.PK = p1.p_pricerow AND ROWNUM=1),0)                                       
                                       mobilespecialprice,
                                    P.p_promotionstartdate,
                                    P.p_promotionenddate,
                                    P.p_mrp,
                                    I.p_available,
                                    SI.p_sellerid,
                                    SI.p_sellername,
                                    SI.p_sellertype,
                                    SI.p_startdate,
                                    SI.P_enddate,
                                      NVL (GREATEST (PLD.P_L1PRIORITY,
                                                     PLD.P_L2PRIORITY,
                                                     PLD.P_L3PRIORITY,
                                                     PLD.P_L4PRIORITY,
                                                     PLD.P_PRODUCTPRIORITY),
                                           0)
                                    * NVL (P_ISVALIDPRIORITY, 0)

                                       AS maxVal,
									   EV.sequencenumber AS status --Changes for Delisting 27_02_17
                               FROM pricerows P,
                                    stocklevels I,
                                    mplsellerinfo SI,
                                    products PR,
                                    mplpriorityleveldetails PLD,
									enumerationvalues EV--Changes for Delisting 27_02_17
                              
                              WHERE     P.p_sellerarticlesku =
                                           I.p_sellerarticlesku
									AND P.p_product<>v_pricefallbackpk
                                    AND P.p_sellerarticlesku =
                                           SI.p_sellerarticlesku
                                    AND PLD.p_ussid(+) =
                                           SI.p_sellerarticlesku
                                    AND P.p_product = PR.PK
                                    AND P.p_catalogversion =
                                           v_catalogversion_buybox
                                    AND SI.p_catalogversion =
                                           v_catalogversion_buybox
                                    AND PR.p_productcategorytype <> 'FineJewellery'
									AND SI.P_SELLERASSOCIATIONSTATUS = EV.pk --Changes for Delisting 27_02_17
                                    AND (   I.modifiedts >
                                               v_last_run_weightage
                                         OR P.modifiedts >
                                               v_last_run_weightage
                                         OR PLD.modifiedts >
                                               v_last_run_weightage
                                         )) bb) S
                 ON (B.p_sellerarticlesku = S.p_sellerarticlesku)
         WHEN MATCHED
         THEN
            UPDATE SET
			   B.p_product = S.p_code,
               B.p_price = S.p_price,
               B.p_specialprice = NVL (S.specialprice, 0),
               B.p_specialpricemobile = NVL(S.mobilespecialprice,0),
               B.p_mrp = S.p_mrp,
               B.p_available = S.p_available,
               B.p_sellername = S.p_sellername,
               B.p_sellerstartdate = S.p_startdate,
               B.p_sellerenddate = S.P_enddate,
               B.modifiedts = v_prc_start_time_weightage,
               B.p_weightage = v_price_weightage * S.weightage_price + S.maxVal,
               B.p_weightagemobile = v_price_weightage * S.weightage_mobileprice + S.maxVal,
			   B.p_delisted = S.status --Changes for Delisting 27_02_17
         --+ v_inv_weightage * S.p_available
         WHEN NOT MATCHED
         THEN
            INSERT     (B.hjmpts,
                        B.pk,
                        B.p_sellerarticlesku,
                        B.p_product,
                        B.p_price,
                        B.p_specialprice,
                        B.p_mrp,
                        B.p_available,
                        B.p_weightage,
                        B.typepkstring,
                        B.p_sellerid,
                        B.p_sellername,
                        B.p_sellertype,
                        B.p_sellerstartdate,
                        B.p_sellerenddate,
                        B.modifiedts,
                        B.createdts,
                        B.p_delisted,                     /* D-list changes */
                        B.aclts,
                        B.propts,
                        B.p_specialpricemobile,
                        B.p_weightagemobile
                        )
                VALUES ('0',
                        GETPK (v_itemtypecode_buybox, BUYBOX_SEQ.NEXTVAL),
                        S.p_sellerarticlesku,
                        S.p_code,
                        S.p_price,
                        NVL (S.specialprice, 0),
                        S.p_mrp,
                        S.p_available,
                        v_price_weightage * S.weightage_price + S.maxVal,
                        --      + v_inv_weightage * S.p_available,
                        v_typepk_buybox,
                        S.p_sellerid,
                        S.p_sellername,
                        S.p_sellertype,
                        S.p_startdate,
                        S.p_enddate,
                        v_prc_start_time_weightage,
                        v_prc_start_time_weightage,
                        S.status,--Changes for Delisting 27_02_17
                        '0',
                        '0',
                        NVL(S.mobilespecialprice,0),
                        v_price_weightage * S.weightage_mobileprice + S.maxVal
                        );
      END IF;


      --Update special price specific weightage which has active promotions and no seller hierarchy applied


      COMMIT;
	  -- CAR-302/CAR-303 size variant  update snippet
	  	update MplBuyBoxProcTable bbox2 set bbox2.modifiedts=v_prc_start_time_weightage, bbox2.p_oosmodifiedval=v_prc_start_time_weightage 
			where exists 
			(SELECT   
			null 
			from 
			MplBuyBoxProcTable bbox1, 
			MplBuyBox bbox, 
			products p1, 
			products p2 
			WHERE   
			bbox1.p_sellerarticlesku = bbox.p_sellerarticlesku 
			and ((bbox.P_MRP <> bbox1.P_MRP) 
			or (bbox.P_PRICE <> bbox1.P_PRICE) 
			or (bbox.p_specialpricemobile <> bbox1.p_specialpricemobile) 
			or (bbox.p_specialprice <> bbox1.p_specialprice) 
			or (bbox.p_delisted = 1 and bbox1.p_delisted = 0) 
			or (bbox.p_delisted = 0 and bbox1.p_delisted = 1) 
			or (bbox.p_available > 0 and bbox1.p_available <= 0) 
				or (bbox.p_available <= 0 and bbox1.p_available > 0) 
			) 
			and bbox1.p_product = p1.p_code 
			and p1.p_colour=p2.p_colour 
			and p1.p_baseproduct=p2.p_baseproduct 
			and bbox2.p_product = p2.p_code 
			and bbox1.modifiedts > v_last_run_weightage 
			and p1.p_catalogversion=v_catalogversion_buybox 
			and p2.p_catalogversion=v_catalogversion_buybox); 
			
			COMMIT;
	  -- CAR-302/CAR-303 size variant  update snippet ends
      --to Update the last run time
      UPDATE MPLBUYBOXUPDTLOG
         SET MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME = v_prc_start_time_weightage
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'buybox_weightage';
   EXCEPTION
      WHEN OTHERS
      THEN
         record_error ();

         COMMIT;
   END buybox_weightage;


   /****************************************************************************
***********
   /*  Procedure to update the special p_price when promotion start and end.
   /*  When promotion start:
   /*    It will take the special price and update the weightage
   /*  When promotion end:
   /*    it will take regular price and update the weightage

   *****************************************************************************
**********/

   PROCEDURE buybox_price_update
   IS
      v_last_run_price_update         TIMESTAMP;
      v_prc_start_time_price_update   TIMESTAMP;
      -- v_typepk_delta                  NUMBER;
      v_price_weightage               NUMBER;
      v_jprice_weightage            NUMBER;
      v_inv_weightage                 NUMBER;
      v_mergepromostdtcount           NUMBER;
      v_mergepromoenddtcount          NUMBER;
      v_catalogversion_buybox         NUMBER;
	  v_jmergepromostdtcount          NUMBER; ---Added for Jewellery
	  v_jmergepromoenddtcount         NUMBER; ---Added for Jewellery

      TYPE t_p_buyboxpk IS TABLE OF MplBuyBoxProcTable.pk%TYPE;

      v_t_p_buyboxpk                  t_p_buyboxpk;
   BEGIN
      -- assign the last run time value into local variable
      SELECT MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME
        INTO v_last_run_price_update
        FROM MPLBUYBOXUPDTLOG
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'buybox_price_update';

      -- assign the deltabuybox typepkstring value into local variable
      -- SELECT pk
      --  INTO v_typepk_delta
      --  FROM composedtypes
      -- WHERE internalcode = 'delta_buybox';

      -- assign the price weightage value into local variable
      SELECT p_weightagevalue
        INTO v_price_weightage
        FROM mplbbweightage
       WHERE p_weigtagetype = 'priceweightage';

      -- assign the inventory weightage value into local variable
      SELECT p_weightagevalue
        INTO v_inv_weightage
        FROM mplbbweightage
       WHERE p_weigtagetype = 'invweightage';
	   
	    ---- Jewellery weightage
	   SELECT p_weightagevalue
        INTO v_jprice_weightage
        FROM mplbbweightage
       WHERE p_weigtagetype = 'jpriceweightage';

      -- assign the current timestamp value into local variable
      SELECT TO_TIMESTAMP (
                TO_CHAR (SYSTIMESTAMP, 'rrrr-MM-DD hh24:mi:ss.FF3'),
                'rrrr-MM-DD hh24:mi:ss.FF3')
        INTO v_prc_start_time_price_update
        FROM DUAL;

      -- assign the product catalog version value into local variable
      SELECT CV.pk
        INTO v_catalogversion_buybox
        FROM catalogs c, catalogversions CV
       WHERE     c.p_id = 'mplProductCatalog'
             AND c.pk = CV.p_catalog
             AND CV.p_version = 'Online';

     -- assign the count value into v_mergepromostdtcount variable
      SELECT COUNT (*)
        INTO v_mergepromostdtcount
        FROM pricerows P, stocklevels I, mplpriorityleveldetails PLD,mplsellerinfo SI,
        mplpromotionalpricerow pp1,mplpromotionalpricerow pp2
       WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
             AND P.p_catalogversion = v_catalogversion_buybox
             AND P.PK = pp1.p_pricerow(+)
             AND P.PK = pp2.p_pricerow(+)
             AND PLD.p_ussid(+) = I.p_sellerarticlesku
             AND P.p_sellerarticlesku = SI.p_sellerarticlesku
             AND (SI.p_hasVariant=0 OR SI.p_hasVariant is null)
			 AND ((pp1.p_promotionstartdate BETWEEN v_last_run_price_update AND v_prc_start_time_price_update)
             OR (pp2.p_promotionstartdate BETWEEN v_last_run_price_update AND v_prc_start_time_price_update))
             ;


      -- assign the count value into v_mergepromoenddtcount variable
      SELECT COUNT (*)
        INTO v_mergepromoenddtcount
        FROM pricerows P, stocklevels I, mplpriorityleveldetails PLD,mplsellerinfo SI,
        mplpromotionalpricerow pp1,mplpromotionalpricerow pp2
       WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
             AND P.p_catalogversion = v_catalogversion_buybox
             AND P.PK = pp1.p_pricerow(+)
             AND P.PK = pp2.p_pricerow(+)
             AND PLD.p_ussid(+) = I.p_sellerarticlesku
             AND P.p_sellerarticlesku = SI.p_sellerarticlesku
             AND (SI.p_hasVariant=0 OR SI.p_hasVariant is null)
             AND ((pp1.p_promotionenddate BETWEEN v_last_run_price_update AND v_prc_start_time_price_update)
             OR (pp2.p_promotionenddate BETWEEN v_last_run_price_update AND v_prc_start_time_price_update))
             ;

     	  
	  -----Changes added for jewellery
	  
	 -- assign the count value into v_jmergepromostdtcount variable
 	    SELECT COUNT (*)
        INTO v_jmergepromostdtcount
        FROM pricerows P, stocklevels I, mplpriorityleveldetails PLD,
		MPLJewelleryInfo JI,mplsellerinfo SI,mplpromotionalpricerow pp1,mplpromotionalpricerow pp2
       WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
             AND P.p_catalogversion = v_catalogversion_buybox
             AND P.PK = pp1.p_pricerow(+)
             AND P.PK = pp2.p_pricerow(+)
             AND PLD.p_ussid(+) = JI.P_PCMUSSID
             AND (JI.P_PCMUSSID = SI.p_sellerarticlesku AND SI.p_hasVariant=1)
             AND ((pp1.p_promotionstartdate BETWEEN v_last_run_price_update AND v_prc_start_time_price_update)
             OR (pp2.p_promotionstartdate BETWEEN v_last_run_price_update AND v_prc_start_time_price_update))
             ;
											
      -- assign the count value into v_jmergepromoenddtcount variable
      SELECT COUNT (*)
        INTO v_jmergepromoenddtcount
       FROM pricerows P, stocklevels I, mplpriorityleveldetails PLD,
		MPLJewelleryInfo JI,mplsellerinfo SI,mplpromotionalpricerow pp1,mplpromotionalpricerow pp2
       WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
             AND P.p_catalogversion = v_catalogversion_buybox
			 AND P.PK = pp1.p_pricerow(+)
             AND P.PK = pp2.p_pricerow(+)
             AND PLD.p_ussid(+) = JI.P_PCMUSSID
			 AND (JI.P_PCMUSSID = SI.p_sellerarticlesku AND SI.p_hasVariant=1)
            AND ((pp1.p_promotionenddate BETWEEN v_last_run_price_update AND v_prc_start_time_price_update)
             OR (pp2.p_promotionenddate BETWEEN v_last_run_price_update AND v_prc_start_time_price_update))
             ; 
			 
		--Joins Price,p_available,Delta tables and merge the result data into buybox table based promo start time for jewellery
		--start promotion web jewellery
		IF (v_jmergepromostdtcount > 0)
      THEN
         MERGE INTO MplBuyBoxProcTable BJ
              USING (SELECT P.p_sellerarticlesku,
                            P.p_product,
                            NVL((SELECT p_specialprice FROM MPLPROMOTIONALPRICEROW WHERE (p_promotionchannel='Web' OR p_promotionchannel is null)
                                       AND P.PK = p_pricerow AND ROWNUM=1),0)                                       
                                       specialprice,
                            I.p_available,
							JI.P_PCMUSSID,
                              NVL (GREATEST (PLD.P_L1PRIORITY,
                                             PLD.P_L2PRIORITY,
                                             PLD.P_L3PRIORITY,
                                             PLD.P_L4PRIORITY,
                                             PLD.P_PRODUCTPRIORITY),
                                   0)
                            * NVL (P_ISVALIDPRIORITY, 0)
                               AS maxVal
                       FROM pricerows P,
                            stocklevels I,
                            mplpriorityleveldetails PLD,
                            MPLJewelleryInfo JI,
							mplpromotionalpricerow pp1,
							mplsellerinfo SI
                      WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
                            AND P.p_catalogversion = v_catalogversion_buybox
                            AND PLD.p_ussid(+) = JI.P_PCMUSSID
							AND (JI.P_PCMUSSID = SI.p_sellerarticlesku AND SI.p_hasVariant=1)--to be checked
							AND P.PK = pp1.p_pricerow
                            AND (pp1.p_promotionchannel='Web' OR pp1.p_promotionchannel is null)
                            AND (pp1.p_promotionstartdate BETWEEN v_last_run_price_update
                            AND v_prc_start_time_price_update))
                    SJ
                 ON (BJ.p_sellerarticlesku = SJ.p_sellerarticlesku)
         WHEN MATCHED
         THEN
            UPDATE SET
               BJ.p_specialprice = NVL (SJ.specialprice, 0),
               BJ.p_available = SJ.p_available,
               BJ.modifiedts = v_prc_start_time_price_update,
               BJ.p_weightage = v_jprice_weightage * SJ.specialprice + SJ.maxVal;
      END IF;	 

--start promotion mobile jewellery
IF (v_jmergepromostdtcount > 0)
      THEN
         MERGE INTO MplBuyBoxProcTable BJ
              USING (SELECT P.p_sellerarticlesku,
                            P.p_product,
                            NVL((SELECT p_specialprice FROM MPLPROMOTIONALPRICEROW WHERE (p_promotionchannel='Mobile' OR p_promotionchannel is null)
                                       AND P.PK = p_pricerow AND ROWNUM=1),0)                                       
                                       mobilespecialprice,
                            I.p_available,
							              JI.P_PCMUSSID,
                              NVL (GREATEST (PLD.P_L1PRIORITY,
                                             PLD.P_L2PRIORITY,
                                             PLD.P_L3PRIORITY,
                                             PLD.P_L4PRIORITY,
                                             PLD.P_PRODUCTPRIORITY),
                                   0)
                            * NVL (P_ISVALIDPRIORITY, 0)
                               AS maxVal
                       FROM pricerows P,
                            stocklevels I,
                            mplpriorityleveldetails PLD,
                            MPLJewelleryInfo JI,
							mplpromotionalpricerow pp1,
							mplsellerinfo SI
                      WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
                            AND P.p_catalogversion = v_catalogversion_buybox
                            AND PLD.p_ussid(+) = JI.P_PCMUSSID
							AND (JI.P_PCMUSSID = SI.p_sellerarticlesku AND SI.p_hasVariant=1)--to be checked
							AND P.PK = pp1.p_pricerow
                            AND (pp1.p_promotionchannel='Mobile' OR pp1.p_promotionchannel is null)
                            AND (pp1.p_promotionstartdate BETWEEN v_last_run_price_update
                            AND v_prc_start_time_price_update))
                    SJ
                 ON (BJ.p_sellerarticlesku = SJ.p_sellerarticlesku)
         WHEN MATCHED
         THEN
            UPDATE SET
               BJ.p_specialpricemobile = NVL (SJ.mobilespecialprice, 0),
               BJ.p_available = SJ.p_available,
               BJ.modifiedts = v_prc_start_time_price_update,
               BJ.p_weightagemobile = v_jprice_weightage * SJ.mobilespecialprice + SJ.maxVal;
      END IF;


--Joins Price,p_available,Delta tables and merge the result data into buybox table based promo end time for jewellery
--Jewellery Web Promtion Reset
	   IF (v_jmergepromoenddtcount > 0)
      THEN
         MERGE INTO MplBuyBoxProcTable BJ
              USING (SELECT P.p_sellerarticlesku,
                            P.p_product,
                            P.p_price,
                            I.p_available,
							JI.P_PCMUSSID,
                            NVL (GREATEST (PLD.P_L1PRIORITY,
                                             PLD.P_L2PRIORITY,
                                             PLD.P_L3PRIORITY,
                                             PLD.P_L4PRIORITY,
                                             PLD.P_PRODUCTPRIORITY),
                                   0)
                            * NVL (P_ISVALIDPRIORITY, 0)
                               AS maxVal
                       FROM pricerows P,
                            stocklevels I,
                            mplpriorityleveldetails PLD,
							MPLJewelleryInfo JI,
							mplpromotionalpricerow pp1,
							mplsellerinfo SI
                      WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
                            AND P.p_catalogversion = v_catalogversion_buybox
                            AND PLD.p_ussid(+) = JI.P_PCMUSSID
							AND (JI.P_PCMUSSID = SI.p_sellerarticlesku AND SI.p_hasVariant=1)--to be checked
                            AND P.PK = pp1.p_pricerow
							AND (pp1.p_promotionchannel='Web' OR pp1.p_promotionchannel is null)
                            AND (pp1.p_promotionenddate BETWEEN v_last_run_price_update
                            AND v_prc_start_time_price_update))
                    SJ
                 ON (BJ.p_sellerarticlesku = SJ.p_sellerarticlesku)
         WHEN MATCHED
         THEN
            UPDATE SET
               BJ.p_specialprice = 0,                                                                        
               BJ.p_price = SJ.p_price,
               BJ.p_available = SJ.p_available,
               BJ.modifiedts = v_prc_start_time_price_update,
               BJ.p_weightage = v_jprice_weightage * SJ.p_price + SJ.maxVal;
			  		   
      END IF;	 

--Jewellery Mobile Promtion Reset
IF (v_jmergepromoenddtcount > 0)
      THEN
         MERGE INTO MplBuyBoxProcTable BJ
              USING (SELECT P.p_sellerarticlesku,
                            P.p_product,
                            P.p_price,
                            I.p_available,
							JI.P_PCMUSSID,
                            NVL (GREATEST (PLD.P_L1PRIORITY,
                                             PLD.P_L2PRIORITY,
                                             PLD.P_L3PRIORITY,
                                             PLD.P_L4PRIORITY,
                                             PLD.P_PRODUCTPRIORITY),
                                   0)
                            * NVL (P_ISVALIDPRIORITY, 0)
                               AS maxVal
                       FROM pricerows P,
                            stocklevels I,
                            mplpriorityleveldetails PLD,
							MPLJewelleryInfo JI,
							mplpromotionalpricerow pp1,
							mplsellerinfo SI
                      WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
                            AND P.p_catalogversion = v_catalogversion_buybox
                            AND PLD.p_ussid(+) = JI.P_PCMUSSID
							AND (JI.P_PCMUSSID = SI.p_sellerarticlesku AND SI.p_hasVariant=1)--to be checked
                            AND P.PK = pp1.p_pricerow
							AND (pp1.p_promotionchannel='Mobile' OR pp1.p_promotionchannel is null)
                            AND (pp1.p_promotionenddate BETWEEN v_last_run_price_update
                            AND v_prc_start_time_price_update))
                    SJ
                 ON (BJ.p_sellerarticlesku = SJ.p_sellerarticlesku)
         WHEN MATCHED
         THEN
            UPDATE SET
               BJ.p_specialprice = 0,                                                                        
               BJ.p_price = SJ.p_price,
               BJ.p_available = SJ.p_available,
               BJ.modifiedts = v_prc_start_time_price_update,
               BJ.p_weightagemobile = v_jprice_weightage * SJ.p_price + SJ.maxVal;
			  		   
      END IF;
	  
	--End of jewellery changes  
			 
 --Joins Price,p_available,Delta tables and merge the result data into buybox table based promo start time	  
--start promotion web
      IF (v_mergepromostdtcount > 0)
      THEN
         MERGE INTO MplBuyBoxProcTable B
              USING (SELECT P.p_sellerarticlesku,
                            P.p_product,
                                 NVL((SELECT p_specialprice FROM MPLPROMOTIONALPRICEROW WHERE (p_promotionchannel='Web' OR p_promotionchannel is null)
                                       AND P.PK = p_pricerow AND ROWNUM=1),0)                                       
                                       specialprice,
                                       I.p_available,
                              NVL (GREATEST (PLD.P_L1PRIORITY,
                                             PLD.P_L2PRIORITY,
                                             PLD.P_L3PRIORITY,
                                             PLD.P_L4PRIORITY,
                                             PLD.P_PRODUCTPRIORITY),
                                   0)
                            * NVL (P_ISVALIDPRIORITY, 0)
                               AS maxVal
                       FROM pricerows P,
                            stocklevels I,
                            mplpriorityleveldetails PLD,
							mplpromotionalpricerow pp1							
                      WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
                            AND P.p_catalogversion = v_catalogversion_buybox
                            AND PLD.p_ussid(+) = I.p_sellerarticlesku
							AND P.PK = pp1.p_pricerow
							AND (pp1.p_promotionchannel='Web' OR pp1.p_promotionchannel is null)
                            AND (pp1.p_promotionstartdate BETWEEN v_last_run_price_update
                            AND v_prc_start_time_price_update))
                    S
                 ON (B.p_sellerarticlesku = S.p_sellerarticlesku)
         WHEN MATCHED
         THEN
            UPDATE SET
               B.p_specialprice = NVL (S.specialprice, 0),
               --B.p_specialpricemobile = NVL(S.mobilespecialprice,0),
               B.p_available = S.p_available,
               B.modifiedts = v_prc_start_time_price_update,
               --B.p_weightagemobile = v_price_weightage * S.mobilespecialprice + S.maxVal,
               B.p_weightage = v_price_weightage * S.specialprice + S.maxVal;
      END IF;
--start promotion mobile
      IF (v_mergepromostdtcount > 0)
      THEN
         MERGE INTO MplBuyBoxProcTable B
              USING (SELECT P.p_sellerarticlesku,
                            P.p_product,
                                 NVL((SELECT p_specialprice FROM MPLPROMOTIONALPRICEROW WHERE (p_promotionchannel='Mobile' OR p_promotionchannel is null)
                                       AND P.PK = p_pricerow AND ROWNUM=1),0)                                       
                                       mobilespecialprice,
                                       I.p_available,
                              NVL (GREATEST (PLD.P_L1PRIORITY,
                                             PLD.P_L2PRIORITY,
                                             PLD.P_L3PRIORITY,
                                             PLD.P_L4PRIORITY,
                                             PLD.P_PRODUCTPRIORITY),
                                   0)
                            * NVL (P_ISVALIDPRIORITY, 0)
                               AS maxVal
                       FROM pricerows P,
                            stocklevels I,
                            mplsellerinfo SI,
                            mplpriorityleveldetails PLD,
							mplpromotionalpricerow pp1							
                      WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
                            AND P.p_catalogversion = v_catalogversion_buybox
                            AND PLD.p_ussid(+) = I.p_sellerarticlesku
                            AND P.PK = pp1.p_pricerow
                            AND P.p_sellerarticlesku = SI.p_sellerarticlesku
                            AND (SI.p_hasVariant=0 OR SI.p_hasVariant is null)
                            AND (pp1.p_promotionchannel='Mobile' OR pp1.p_promotionchannel is null)
                            AND (pp1.p_promotionstartdate BETWEEN v_last_run_price_update
                            AND v_prc_start_time_price_update))
                    S
                 ON (B.p_sellerarticlesku = S.p_sellerarticlesku)
         WHEN MATCHED
         THEN
            UPDATE SET
              -- B.p_specialprice = NVL (S.specialprice, 0),
               B.p_specialpricemobile = NVL(S.mobilespecialprice,0),
               B.p_available = S.p_available,
               B.modifiedts = v_prc_start_time_price_update,
               B.p_weightagemobile = v_price_weightage * S.mobilespecialprice + S.maxVal;
               --B.p_weightage = v_price_weightage * S.specialprice + S.maxVal;
      END IF;
      --Joins Price,p_available,Delta tables and merge the result data into buybox table based promo end time
	  ---Web Promtion Reset
      IF (v_mergepromoenddtcount > 0)
      THEN
         MERGE INTO MplBuyBoxProcTable B
              USING (SELECT P.p_sellerarticlesku,
                            P.p_product,
                            P.p_price,
                            I.p_available,
                              NVL (GREATEST (PLD.P_L1PRIORITY,
                                             PLD.P_L2PRIORITY,
                                             PLD.P_L3PRIORITY,
                                             PLD.P_L4PRIORITY,
                                             PLD.P_PRODUCTPRIORITY),
                                   0)
                            * NVL (P_ISVALIDPRIORITY, 0)
                               AS maxVal
                       FROM pricerows P,
                            stocklevels I,
                            mplpriorityleveldetails PLD,
                            mplsellerinfo SI,
							mplpromotionalpricerow pp1
                      WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
                            AND P.p_catalogversion = v_catalogversion_buybox
                            AND PLD.p_ussid(+) = I.p_sellerarticlesku
                            AND P.PK = pp1.p_pricerow
                            AND P.p_sellerarticlesku = SI.p_sellerarticlesku
                            AND (SI.p_hasVariant=0 OR SI.p_hasVariant is null)
                            AND (pp1.p_promotionchannel='Web' OR pp1.p_promotionchannel is null)
                            AND (pp1.p_promotionenddate BETWEEN v_last_run_price_update
                            AND v_prc_start_time_price_update))
                    S
                 ON (B.p_sellerarticlesku = S.p_sellerarticlesku)
         WHEN MATCHED
         THEN
            UPDATE SET
               B.p_specialprice = 0,
			   B.p_price = S.p_price,
               B.p_available = S.p_available,
               B.modifiedts = v_prc_start_time_price_update,
			   B.p_weightage = v_price_weightage * S.p_price + S.maxVal;
      END IF;
--Mobile Promotion Reset

      IF (v_mergepromoenddtcount > 0)
      THEN
         MERGE INTO MplBuyBoxProcTable B
              USING (SELECT P.p_sellerarticlesku,
                            P.p_product,
                            P.p_price,
                            I.p_available,
                              NVL (GREATEST (PLD.P_L1PRIORITY,
                                             PLD.P_L2PRIORITY,
                                             PLD.P_L3PRIORITY,
                                             PLD.P_L4PRIORITY,
                                             PLD.P_PRODUCTPRIORITY),
                                   0)
                            * NVL (P_ISVALIDPRIORITY, 0)
                               AS maxVal
                       FROM pricerows P,
                            stocklevels I,
                            mplpriorityleveldetails PLD,
                            mplsellerinfo SI,
							mplpromotionalpricerow pp1
                      WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
                            AND P.p_catalogversion = v_catalogversion_buybox
                            AND PLD.p_ussid(+) = I.p_sellerarticlesku
                            AND P.PK = pp1.p_pricerow
                            AND P.p_sellerarticlesku = SI.p_sellerarticlesku
                            AND (SI.p_hasVariant=0 OR SI.p_hasVariant is null)
                            AND (pp1.p_promotionchannel='Mobile' OR pp1.p_promotionchannel is null)
                            AND (pp1.p_promotionenddate BETWEEN v_last_run_price_update
                            AND v_prc_start_time_price_update))
                    S
                 ON (B.p_sellerarticlesku = S.p_sellerarticlesku)
         WHEN MATCHED
         THEN
            UPDATE SET
               B.p_specialpricemobile = 0,
			   B.p_price = S.p_price,
               B.p_available = S.p_available,
               B.modifiedts = v_prc_start_time_price_update,
			  B.p_weightagemobile = v_price_weightage * S.p_price + S.maxVal;
      END IF;
	  
      MERGE INTO MplBuyBoxProcTable B
           USING (SELECT SI.P_SELLERARTICLESKU, EV.sequencenumber AS status
                    FROM mplsellerinfo SI, enumerationvalues EV
                   WHERE     SI.P_SELLERARTICLESKU IS NOT NULL
                         AND SI.P_SELLERASSOCIATIONSTATUS = EV.pk
                         AND SI.p_catalogversion = v_catalogversion_buybox
					--Changes for Delisting 27_02_17                      
					  AND SI.P_DELISTDATE > v_last_run_price_update)
                 	--Commented as there is no need to check wether the time frame lies in between .Any delist timestamp greater the v_last_run_price_update should work
						-- AND SI.P_DELISTDATE BETWEEN v_last_run_price_update
                         --AND v_prc_start_time_price_update)
                 S
              ON (B.p_sellerarticlesku = S.P_SELLERARTICLESKU)
      WHEN MATCHED
      THEN
         UPDATE SET
            B.p_delisted = S.status,
            B.modifiedts = v_prc_start_time_price_update;
		
		-- CAR-302/CAR-303 size variant  update snippet
		
		update MplBuyBoxProcTable bbox2 set bbox2.modifiedts=v_prc_start_time_price_update, bbox2.p_oosmodifiedval=v_prc_start_time_price_update 
			where exists 
			(SELECT   
			null 
			from 
			MplBuyBoxProcTable bbox1, 
			MplBuyBox bbox, 
			products p1, 
			products p2 
			WHERE   
			bbox1.p_sellerarticlesku = bbox.p_sellerarticlesku 
			and ((bbox.P_MRP <> bbox1.P_MRP) 
			or (bbox.P_PRICE <> bbox1.P_PRICE) 
			or (bbox.p_specialpricemobile <> bbox1.p_specialpricemobile) 
			or (bbox.p_specialprice <> bbox1.p_specialprice) 
			or (bbox.p_delisted = 1 and bbox1.p_delisted = 0) 
			or (bbox.p_delisted = 0 and bbox1.p_delisted = 1) 
			or (bbox.p_available > 0 and bbox1.p_available <= 0) 
				or (bbox.p_available <= 0 and bbox1.p_available > 0) 
			) 
			and bbox1.p_product = p1.p_code 
			and p1.p_colour=p2.p_colour 
			and p1.p_baseproduct=p2.p_baseproduct 
			and bbox2.p_product = p2.p_code 
			and bbox1.modifiedts > v_last_run_price_update 
			and p1.p_catalogversion=v_catalogversion_buybox 
			and p2.p_catalogversion=v_catalogversion_buybox);
			
			COMMIT;
			-- CAR-302/CAR-303 size variant  update snippet ends
		
      --to Update the last run time
      UPDATE MPLBUYBOXUPDTLOG
         SET MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME = v_prc_start_time_price_update
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'buybox_price_update';
   EXCEPTION
      WHEN OTHERS
      THEN
         record_error ();



         COMMIT;
   END buybox_price_update;


   /***************************************************************************************
     /*  Procedure to update the sellers startdate and seller enddate when you change the mplsellerinfo table.

     /*  It will take the seller startdate and enddate update in the MplBuyBoxProcTable table
	 
	 /*  Delisting and Relisting if Has not taken place over the day.It is taken up with this job


     ***************************************************************************************/



   PROCEDURE buybox_dateupdate
   IS
      v_last_run_dateupdate         TIMESTAMP;
      v_prc_start_time_dateupdate   TIMESTAMP;
      v_typepk_buybox               NUMBER;
      v_catalogversion_buybox       NUMBER;


      TYPE t_buyboxpk IS TABLE OF MplBuyBoxProcTable.pk%TYPE;

      v_t_buyboxpk                  t_buyboxpk;
   BEGIN
      -- assign the last run time value into local variable
      SELECT MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME
        INTO v_last_run_dateupdate
        FROM MPLBUYBOXUPDTLOG
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'buybox_dateupdate';



      -- assign the current timestamp value into local variable
      SELECT TO_TIMESTAMP (
                TO_CHAR (SYSTIMESTAMP, 'rrrr-MM-DD hh24:mi:ss.FF3'),
                'rrrr-MM-DD hh24:mi:ss.FF3')
        INTO v_prc_start_time_dateupdate
        FROM DUAL;

      -- assign the product catalog version value into local variable
      SELECT CV.pk
        INTO v_catalogversion_buybox
        FROM catalogs c, catalogversions CV
       WHERE     c.p_id = 'mplProductCatalog'
             AND c.pk = CV.p_catalog
             AND CV.p_version = 'Online';



      --Joins Price,Inventory,Delta tables and merge the result data into buybox table
      MERGE INTO MplBuyBoxProcTable B
           USING (SELECT SI.p_sellerarticlesku,
                         SI.p_sellerid,
                         SI.p_sellername,
                         SI.p_sellertype,
                         SI.p_startdate,
                         SI.P_enddate
                    FROM mplsellerinfo SI
                   WHERE     SI.p_catalogversion = v_catalogversion_buybox
                         AND (SI.modifiedts > v_last_run_dateupdate)) S
              ON (B.p_sellerarticlesku = S.p_sellerarticlesku)
      WHEN MATCHED
      THEN
         UPDATE SET B.p_sellerid = S.p_sellerid,
                    B.p_sellername = S.p_sellername,
                    B.p_sellertype = S.p_sellertype,
                    B.p_sellerstartdate = S.p_startdate,
                    B.p_sellerenddate = S.P_enddate,
                    B.modifiedts = v_prc_start_time_dateupdate;
					
					
--Changes for Delisting 27_02_17
-------------------Addition for Fixing the Delisting and Relisting-------------------------
	--Any Delisting or Relisting Feed Missed would be fixed with this part of the Job
      MERGE INTO MplBuyBoxProcTable B
           USING (SELECT SI.P_SELLERARTICLESKU, EV.sequencenumber AS status
                    FROM mplsellerinfo SI, enumerationvalues EV
                   WHERE     SI.P_SELLERARTICLESKU IS NOT NULL
                         AND SI.P_SELLERASSOCIATIONSTATUS = EV.pk
                         AND SI.p_catalogversion = v_catalogversion_buybox
                         AND SI.P_DELISTDATE  > v_last_run_dateupdate)
                 S
              ON (B.p_sellerarticlesku = S.P_SELLERARTICLESKU)
      WHEN MATCHED
      THEN
         UPDATE SET
            B.p_delisted = S.status,
            B.modifiedts = v_prc_start_time_dateupdate;
------------------------------------------------------------------------------------------

-- CAR-302/CAR-303 size variant  update snippet

	update MplBuyBoxProcTable bbox2 set bbox2.modifiedts=v_prc_start_time_dateupdate, bbox2.p_oosmodifiedval=v_prc_start_time_dateupdate 
	where exists 
	(SELECT   
	null 
	from 
	MplBuyBoxProcTable bbox1, 
	MplBuyBox bbox, 
	products p1, 
	products p2 
	WHERE   
	bbox1.p_sellerarticlesku = bbox.p_sellerarticlesku 
	and ((bbox.p_delisted = 1 and bbox1.p_delisted = 0) 
	or (bbox.p_delisted = 0 and bbox1.p_delisted = 1) 
	) 
	and bbox1.p_product = p1.p_code 
	and p1.p_colour=p2.p_colour 
	and p1.p_baseproduct=p2.p_baseproduct 
	and bbox2.p_product = p2.p_code 
	and bbox1.modifiedts > v_last_run_dateupdate 
	and p1.p_catalogversion=v_catalogversion_buybox 
	and p2.p_catalogversion=v_catalogversion_buybox); 

	COMMIT;
-- CAR-302/CAR-303 size variant  update snippet ends

      --to Update the last run time
      UPDATE MPLBUYBOXUPDTLOG
         SET MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME = v_prc_start_time_dateupdate
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'buybox_dateupdate';
   EXCEPTION
      WHEN OTHERS
      THEN
         record_error ();
         COMMIT;
   END buybox_dateupdate;
END;
