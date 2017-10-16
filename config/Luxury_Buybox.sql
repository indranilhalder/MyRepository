create or replace PACKAGE BODY "lux_buybox_package"
AS
   /**************************************************************************************
   /*  Procedure will calculate and update the buybox weightage for the price/inventory updated skus
   /*  It has to be invoked periodically 
   /*  Delta will be identified by modifiedts column of price/stocklevels table
   /****************************************************************************************/

   PROCEDURE lux_buybox_weightage
   IS
      v_last_run_weightage         TIMESTAMP;
      v_prc_start_time_weightage   TIMESTAMP;
      v_typepk_buybox              NUMBER;
      v_itemtypecode_buybox        NUMBER;
      v_getpkfunction_buybox       NUMBER;
      -- v_typepk_delta               NUMBER;
      v_price_weightage            NUMBER;
      v_inv_weightage              NUMBER;
      v_catalogversion_buybox      NUMBER;
      v_mergecount                 NUMBER;
	    v_pricefallbackpk            NUMBER;
   -- TYPE t_buyboxussid IS TABLE OF MplBuyBoxProcTable.P_SELLERARTICLESKU%TYPE;


   -- v_t_buyboxussid                 t_buyboxussid;


   BEGIN
	  -- assign the last run time value into local variable
      SELECT MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME
        INTO v_last_run_weightage
        FROM MPLBUYBOXUPDTLOG
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'lux_buybox_weightage';

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
       WHERE     c.p_id = 'luxProductCatalog'
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
      IF (v_mergecount > 0)
      THEN
         --Joins Price,Inventory,Delta tables and merge the result data into buybox table
         MERGE INTO MplBuyBoxProcTable B
              USING (SELECT bb.*,
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
                                    NVL((SELECT p_specialprice FROM MPLPROMOTIONALPRICEROW WHERE (p_promotionchannel='Web' OR p_promotionchannel is null)
                                       AND v_prc_start_time_weightage BETWEEN p_promotionstartdate AND p_promotionenddate AND P.PK = p_pricerow AND ROWNUM=1),0)                                       
                                       specialprice,
                                      NVL((SELECT p_specialprice FROM MPLPROMOTIONALPRICEROW WHERE (p_promotionchannel='Mobile' OR p_promotionchannel is null)
                                       AND v_prc_start_time_weightage BETWEEN p_promotionstartdate AND p_promotionenddate AND P.PK = p_pricerow AND ROWNUM=1),0)                                       
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
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'lux_buybox_weightage';
   EXCEPTION
      WHEN OTHERS
      THEN
         record_error ();

         COMMIT;
   END lux_buybox_weightage;


   /****************************************************************************
***********
   /*  Procedure to update the special p_price when promotion start and end.
   /*  When promotion start:
   /*    It will take the special price and update the weightage
   /*  When promotion end:
   /*    it will take regular price and update the weightage

   *****************************************************************************
**********/

   PROCEDURE lux_buybox_price_update
   IS
      v_last_run_price_update         TIMESTAMP;
      v_prc_start_time_price_update   TIMESTAMP;
      -- v_typepk_delta                  NUMBER;
      v_price_weightage               NUMBER;
      v_inv_weightage                 NUMBER;
      v_mergepromostdtcount           NUMBER;
      v_mergepromoenddtcount          NUMBER;
      v_catalogversion_buybox         NUMBER;

      TYPE t_p_buyboxpk IS TABLE OF MplBuyBoxProcTable.pk%TYPE;

      v_t_p_buyboxpk                  t_p_buyboxpk;
   BEGIN
      -- assign the last run time value into local variable
      SELECT MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME
        INTO v_last_run_price_update
        FROM MPLBUYBOXUPDTLOG
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'lux_buybox_price_update';

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
       WHERE     c.p_id = 'luxProductCatalog'
             AND c.pk = CV.p_catalog
             AND CV.p_version = 'Online';

     -- assign the count value into v_mergepromostdtcount variable
      SELECT COUNT (*)
        INTO v_mergepromostdtcount
        FROM pricerows P, stocklevels I, mplpriorityleveldetails PLD,
        mplpromotionalpricerow pp1,mplpromotionalpricerow pp2
       WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
             AND P.p_catalogversion = v_catalogversion_buybox
             AND P.PK = pp1.p_pricerow(+)
             AND P.PK = pp2.p_pricerow(+)
             AND PLD.p_ussid(+) = I.p_sellerarticlesku
			 AND ((pp1.p_promotionstartdate BETWEEN v_last_run_price_update AND v_prc_start_time_price_update)
             OR (pp2.p_promotionstartdate BETWEEN v_last_run_price_update AND v_prc_start_time_price_update))
             ;


      -- assign the count value into v_mergepromoenddtcount variable
      SELECT COUNT (*)
        INTO v_mergepromoenddtcount
        FROM pricerows P, stocklevels I, mplpriorityleveldetails PLD,
        mplpromotionalpricerow pp1,mplpromotionalpricerow pp2
       WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
             AND P.p_catalogversion = v_catalogversion_buybox
             AND P.PK = pp1.p_pricerow(+)
             AND P.PK = pp2.p_pricerow(+)
             AND PLD.p_ussid(+) = I.p_sellerarticlesku
             AND ((pp1.p_promotionenddate BETWEEN v_last_run_price_update AND v_prc_start_time_price_update)
             OR (pp2.p_promotionenddate BETWEEN v_last_run_price_update AND v_prc_start_time_price_update))
             ;

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
                            mplpriorityleveldetails PLD,
							mplpromotionalpricerow pp1							
                      WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
                            AND P.p_catalogversion = v_catalogversion_buybox
                            AND PLD.p_ussid(+) = I.p_sellerarticlesku
							AND P.PK = pp1.p_pricerow
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
							mplpromotionalpricerow pp1
                      WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
                            AND P.p_catalogversion = v_catalogversion_buybox
                            AND PLD.p_ussid(+) = I.p_sellerarticlesku
							AND P.PK = pp1.p_pricerow
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
							mplpromotionalpricerow pp1
                      WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
                            AND P.p_catalogversion = v_catalogversion_buybox
                            AND PLD.p_ussid(+) = I.p_sellerarticlesku
							AND P.PK = pp1.p_pricerow
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
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'lux_buybox_price_update';
   EXCEPTION
      WHEN OTHERS
      THEN
         record_error ();



         COMMIT;
   END lux_buybox_price_update;


   /***************************************************************************************
     /*  Procedure to update the sellers startdate and seller enddate when you change the mplsellerinfo table.

     /*  It will take the seller startdate and enddate update in the MplBuyBoxProcTable table
	 
	 /*  Delisting and Relisting if Has not taken place over the day.It is taken up with this job


     ***************************************************************************************/



   PROCEDURE lux_buybox_dateupdate
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
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'lux_buybox_dateupdate';



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
       WHERE     c.p_id = 'luxProductCatalog'
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
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'lux_buybox_dateupdate';
   EXCEPTION
      WHEN OTHERS
      THEN
         record_error ();
         COMMIT;
   END lux_buybox_dateupdate;
END;
