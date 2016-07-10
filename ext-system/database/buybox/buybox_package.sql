CREATE OR REPLACE PACKAGE BODY "buybox_package"
AS
  /**************************************************************************************
         /*  Procedure will calculate and update the buybox weightage for the price/inventory updated skus
         /*  It has to be invoked periodically
         /*  Delta will be identified by modifiedts column of price/stocklevels table
         /*       ***************************************************************************************/
   PROCEDURE buybox_weightage
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
      TYPE t_buyboxpk IS TABLE OF mplbuybox.pk%TYPE;
      v_t_buyboxpk                 t_buyboxpk;
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
       WHERE internalcode = 'BuyBox';
      -- assign the buybox itemtypecode value into local variable
      SELECT itemtypecode
        INTO v_itemtypecode_buybox
        FROM composedtypes
       WHERE internalcode = 'BuyBox';
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
       WHERE     c.p_id = 'mplProductCatalog'
             AND c.pk = CV.p_catalog
             AND CV.p_version = 'Online';
      -- assign the count value into local variable
      SELECT COUNT (*)
        INTO v_mergecount
        FROM pricerows P,
             stocklevels I,
             mplsellerinfo SI,
             products PR
       WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
             AND P.p_sellerarticlesku = SI.p_sellerarticlesku
             AND P.p_product = PR.PK
             AND P.p_catalogversion = v_catalogversion_buybox
             AND SI.p_catalogversion = v_catalogversion_buybox
             AND (   I.modifiedts > v_last_run_weightage
                  OR P.modifiedts > v_last_run_weightage);
      --Joins Price,Inventory,Delta tables and merge the result data into buybox table
      IF (v_mergecount > 0)
      THEN
         --Joins Price,Inventory,Delta tables and merge the result data into buybox table
         MERGE INTO mplbuybox B
              USING (SELECT DISTINCT P.p_sellerarticlesku,
                                     PR.p_code,
                                     P.p_price,
                                     p.p_specialprice,
                                     P.p_mrp,
                                     I.p_available,
                                     SI.p_sellerid,
                                     SI.p_sellername,
                                     SI.p_sellertype,
                                     SI.p_startdate,
                                     SI.P_enddate
                       FROM pricerows P,
                            stocklevels I,
                            mplsellerinfo SI,
                            products PR
                      WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
                            AND P.p_sellerarticlesku = SI.p_sellerarticlesku
                            AND P.p_product = PR.PK
                            AND P.p_catalogversion = v_catalogversion_buybox
                            AND SI.p_catalogversion = v_catalogversion_buybox
                            AND (   I.modifiedts > v_last_run_weightage
                                 OR P.modifiedts > v_last_run_weightage)) S
                 ON (B.p_sellerarticlesku = S.p_sellerarticlesku)
         WHEN MATCHED
         THEN
            UPDATE SET
               B.p_price = S.p_price,
               B.p_specialprice = S.p_specialprice,
               B.p_mrp = S.p_mrp,
               B.p_available = S.p_available,
               B.p_sellername = S.p_sellername,
               B.p_sellerstartdate = S.p_startdate,
               B.p_sellerenddate = S.P_enddate,
               B.modifiedts = v_prc_start_time_weightage,
               B.p_weightage =
                    v_price_weightage * S.p_price
                  + v_inv_weightage * S.p_available
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
                        B.propts)
                VALUES (
                          '0',
                          GETPK (v_itemtypecode_buybox, BUYBOX_SEQ.NEXTVAL),
                          S.p_sellerarticlesku,
                          S.p_code,
                          S.p_price,
                          S.p_specialprice,
                          S.p_mrp,
                          S.p_available,
                            v_price_weightage * S.p_price
                          + v_inv_weightage * S.p_available,
                          v_typepk_buybox,
                          S.p_sellerid,
                          S.p_sellername,
                          S.p_sellertype,
                          S.p_startdate,
                          S.p_enddate,
                          v_prc_start_time_weightage,
                          v_prc_start_time_weightage,
                          '0',
                          '0',
                          '0');
      END IF;
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
   /***************************************************************************************
   /*  Procedure to update the special p_price when promotion start and end.
   /*  When promotion start:
   /*    It will take the special price and update the weightage
   /*  When promotion end:
   /*    it will take regular price and update the weightage
   ***************************************************************************************/
   PROCEDURE buybox_price_update
   IS
      v_last_run_price_update         TIMESTAMP;
      v_prc_start_time_price_update   TIMESTAMP;
      -- v_typepk_delta                  NUMBER;
      v_price_weightage               NUMBER;
      v_inv_weightage                 NUMBER;
      v_mergepromostdtcount           NUMBER;
      v_mergepromoenddtcount          NUMBER;
      v_catalogversion_buybox         NUMBER;
      TYPE t_p_buyboxpk IS TABLE OF mplbuybox.pk%TYPE;
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
        FROM pricerows P, stocklevels I
       WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
             AND P.p_catalogversion = v_catalogversion_buybox
             AND P.p_promotionstartdate BETWEEN v_last_run_price_update
                                            AND v_prc_start_time_price_update;
      -- assign the count value into v_mergepromoenddtcount variable
      SELECT COUNT (*)
        INTO v_mergepromoenddtcount
        FROM pricerows P, stocklevels I
       WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
             AND P.p_catalogversion = v_catalogversion_buybox
             AND P.p_promotionenddate BETWEEN v_last_run_price_update
                                          AND v_prc_start_time_price_update;
      --Joins Price,p_available,Delta tables and merge the result data into buybox table based promo start time
      IF (v_mergepromostdtcount > 0)
      THEN
         MERGE INTO mplbuybox B
              USING (SELECT P.p_sellerarticlesku,
                            P.p_product,
                            P.p_specialprice,
                            I.p_available
                       FROM pricerows P, stocklevels I
                      WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
                            AND P.p_catalogversion = v_catalogversion_buybox
                            AND P.p_promotionstartdate BETWEEN v_last_run_price_update
                                                           AND v_prc_start_time_price_update)
                    S
                 ON (B.p_sellerarticlesku = S.p_sellerarticlesku)
         WHEN MATCHED
         THEN
            UPDATE SET
               B.p_specialprice = S.p_specialprice,
               B.p_available = S.p_available,
               B.modifiedts = v_prc_start_time_price_update,
               B.p_weightage =
                    v_price_weightage * S.p_specialprice
                  + v_inv_weightage * S.p_available;
      END IF;
      --Joins Price,p_available,Delta tables and merge the result data into buybox table based promo end time
      IF (v_mergepromoenddtcount > 0)
      THEN
         MERGE INTO mplbuybox B
              USING (SELECT P.p_sellerarticlesku,
                            P.p_product,
                            P.p_price,
                            I.p_available
                       FROM pricerows P, stocklevels I
                      WHERE     P.p_sellerarticlesku = I.p_sellerarticlesku
                            AND P.p_catalogversion = v_catalogversion_buybox
                            AND P.p_promotionenddate BETWEEN v_last_run_price_update
                                                         AND v_prc_start_time_price_update)
                    S
                 ON (B.p_sellerarticlesku = S.p_sellerarticlesku)
         WHEN MATCHED
         THEN
            UPDATE SET
               B.p_specialprice = NULL,
               B.p_price = S.p_price,
               B.p_available = S.p_available,
               B.modifiedts = v_prc_start_time_price_update,
               B.p_weightage =
                    v_price_weightage * S.p_price
                  + v_inv_weightage * S.p_available;
      END IF;
      MERGE INTO mplbuybox B
           USING (SELECT SI.P_SELLERARTICLESKU, EV.sequencenumber AS status
                    FROM mplsellerinfo SI, enumerationvalues EV
                   WHERE     SI.P_SELLERARTICLESKU IS NOT NULL
                         AND SI.P_SELLERASSOCIATIONSTATUS = EV.pk
                         AND SI.p_catalogversion = v_catalogversion_buybox
                         AND SI.P_DELISTDATE BETWEEN v_last_run_price_update
                                                 AND v_prc_start_time_price_update)
                 S
              ON (B.p_sellerarticlesku = S.P_SELLERARTICLESKU)
      WHEN MATCHED
      THEN
         UPDATE SET
            B.p_delisted = S.status,
            B.modifiedts = v_prc_start_time_price_update;
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
     /*  Procedure to update the sellers tartdate and seller enddate when you change the mplsellerino table.
     /*  It will take the seller startdate and enddate update in the mplbuybox table
***************************************************************************************/
   PROCEDURE buybox_dateupdate
   IS
      v_last_run_dateupdate         TIMESTAMP;
      v_prc_start_time_dateupdate   TIMESTAMP;
      v_typepk_buybox               NUMBER;
      v_catalogversion_buybox       NUMBER;
      TYPE t_buyboxpk IS TABLE OF mplbuybox.pk%TYPE;
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
      MERGE INTO mplbuybox B
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
/
