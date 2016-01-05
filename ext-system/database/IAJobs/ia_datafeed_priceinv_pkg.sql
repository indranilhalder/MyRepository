CREATE OR REPLACE PACKAGE BODY "ia_datafeed_priceinv_pkg"
AS
   /**************************************************************************************
          /*  Procedure will calculate and update the datafeed price inventory table
         /*  for the price/inventory updated skus.
          /*  It has to be invoked periodically
          /*  Delta will be identified by modifiedts column of price/stocklevels table
          /*
          ***************************************************************************************/

   PROCEDURE ia_price_inventory
   IS
      v_last_run_priceinventory     TIMESTAMP;
      v_start_time_priceinventory   TIMESTAMP;
      v_catalogversion_online       NUMBER;
   BEGIN
      -- assign the last run time value into local variable
      SELECT MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME
        INTO v_last_run_priceinventory
        FROM MPLBUYBOXUPDTLOG
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'ia_price_inventory';



      -- assign the current timestamp value into local variable
      SELECT TO_TIMESTAMP (
                TO_CHAR (SYSTIMESTAMP, 'rrrr-MM-DD hh24:mi:ss.FF3'),
                'rrrr-MM-DD hh24:mi:ss.FF3')
        INTO v_start_time_priceinventory
        FROM DUAL;

      -- assign the product catalog version value into local variable
      SELECT CV.pk
        INTO v_catalogversion_online
        FROM catalogs c, catalogversions CV
       WHERE     c.p_id = 'mplProductCatalog'
             AND c.pk = CV.p_catalog
             AND CV.p_version = 'Online';


      --Joins Price,Inventory,specialprice, inventory, mrp,weightage and merge the result data into price_inventory table
      MERGE INTO datafeed.PRICE_INVENTORY PI
           USING (SELECT B.p_sellerarticlesku,
                         B.p_product,
                         B.p_price,
                         DECODE (B.p_specialprice, 0, NULL, p_specialprice)
                            AS DISCOUNTED_PRICE,
                         B.p_mrp,
                         B.p_available,
                         B.p_weightage,
                         B.p_sellerid,
                         B.p_sellername,
                         B.p_delisted,
                         NVL (EV.sequencenumber, 0) AS ONLINEEXCLUSIVE
                    FROM mplbuybox B,
                         mplsellerinfo SI,
                         enumerationvalues EV,
                         products P
                   WHERE     SI.p_sellerarticlesku = B.p_sellerarticlesku
                         AND SI.p_catalogversion = v_catalogversion_online
                         AND P.p_catalogversion = v_catalogversion_online
                         AND B.p_product = P.p_code
                         AND (   SYSTIMESTAMP > P.p_onlinedate
                              OR P.p_onlinedate IS NULL)
                         AND (   SYSTIMESTAMP < P.p_offlinedate
                              OR P.p_offlinedate IS NULL)
                         AND (   SYSTIMESTAMP > SI.p_startdate
                              OR SI.p_startdate IS NULL)
                         AND (   SYSTIMESTAMP < SI.p_enddate
                              OR SI.p_enddate IS NULL)
                         AND SI.p_onlineexclusive = EV.PK(+)
                         AND B.modifiedts > v_last_run_priceinventory) S
              ON (PI.SITE_USS_ID = S.p_sellerarticlesku)
      WHEN MATCHED
      THEN
         UPDATE SET PI.price = S.p_price,
                    PI.ORIGINAL_PRICE = S.p_mrp,
                    PI.DISCOUNTED_PRICE = S.discounted_price,
                    PI.inventory = S.p_available,
                    PI.weightage = S.p_weightage,
                    PI.sellerid = S.p_sellerid,
                    PI.sellername = S.p_sellername,
                    PI.delisted = S.p_delisted,
                    PI.onlineexclusive = S.onlineexclusive,
                    PI.status = 0,
                    PI.ISCREATED = 0
      WHEN NOT MATCHED
      THEN
         INSERT     (PI.SITE_USS_ID,
                     PI.listing_id,
                     PI.price,
                     PI.ORIGINAL_PRICE,
                     PI.DISCOUNTED_PRICE,
                     PI.inventory,
                     PI.weightage,
                     pI.sellerid,
                     PI.sellername,
                     PI.delisted,
                     PI.onlineexclusive,
                     PI.status,
                     PI.ISCREATED)
             VALUES (S.p_sellerarticlesku,
                     S.p_product,
                     S.p_price,
                     S.p_mrp,
                     S.discounted_price,
                     S.p_available,
                     S.p_weightage,
                     S.p_sellerid,
                     S.p_sellername,
                     S.P_delisted,
                     S.onlineexclusive,
                     0,
                     1);

      --to Update the last run time
      UPDATE MPLBUYBOXUPDTLOG
         SET MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME = v_start_time_priceinventory
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'ia_price_inventory';

      COMMIT;
   END ia_price_inventory;
END;
/
