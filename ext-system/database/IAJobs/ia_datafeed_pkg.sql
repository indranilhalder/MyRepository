CREATE OR REPLACE PACKAGE BODY "ia_datafeed_pkg"
AS
   /**************************************************************************************
       /*  Procedure will calculate and update the datafeed category table
      /*  for the  updated categories.
       /*  It has to be invoked periodically
       /*  Delta will be identified by modifiedts columns
       /*
       ***************************************************************************************/

   PROCEDURE ia_category
   IS
      v_last_run_category       TIMESTAMP;
      v_start_time_category     TIMESTAMP;
      v_catalogversion_online   NUMBER;
      v_logoimage               NUMBER;
      v_mediaformat             NUMBER;

      CURSOR CATMEDIMG
      IS
         SELECT C.p_code, p_internalurl AS IMAGE_URL
           FROM cat2medrel CM, medias M, categories C
          WHERE     C.PK = CM.SOURCEPK
                AND M.p_mediaformat = v_mediaformat
                AND M.PK = CM.TARGETPK
                AND C.P_CATALOGVERSION = v_catalogversion_online
                AND c.p_code LIKE 'MSH%';

      TYPE CATIMG IS RECORD
      (
         catid    CATEGORIES.p_code%TYPE,
         imgurl   MEDIAS.p_internalurl%TYPE
      );

      TYPE CATIMGARR IS TABLE OF CATIMG;

      v_catimg                  CATIMGARR;

      -- logo image url
      CURSOR CATLOGOIMG
      IS
         SELECT C.p_code, p_internalurl AS LOGO_URL
           FROM cat2medrel CM, medias M, categories C
          WHERE     C.PK = CM.SOURCEPK
                AND M.p_mediaformat = v_logoimage
                AND M.PK = CM.TARGETPK
                AND C.P_CATALOGVERSION = v_catalogversion_online
                AND c.p_code LIKE 'MSH%';

      v_attname                 VARCHAR2 (256);
      v_catid                   VARCHAR2 (256);
      v_catlogoimg              CATIMGARR;
   BEGIN
      -- assign the last run time value into local variable
      SELECT MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME
        INTO v_last_run_category
        FROM MPLBUYBOXUPDTLOG
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'ia_category';

      -- assign the current timestamp value into local variable
      SELECT TO_TIMESTAMP (
                TO_CHAR (SYSTIMESTAMP, 'rrrr-MM-DD hh24:mi:ss.FF3'),
                'rrrr-MM-DD hh24:mi:ss.FF3')
        INTO v_start_time_category
        FROM DUAL;

      -- assign the product catalog version value into local variable
      SELECT CV.pk
        INTO v_catalogversion_online
        FROM catalogs c, catalogversions CV
       WHERE     c.p_id = 'mplProductCatalog'
             AND c.pk = CV.p_catalog
             AND CV.p_version = 'Online';

      -- assign the mediafromatvalue pk  value into mediaformat variable
      SELECT M.Pk
        INTO v_mediaformat
        FROM mediaformat M
       WHERE M.p_QUALIFIER = '324Wx324H';

      -- assign the mediafromatvalue pk  value into mediaformat variable
      SELECT M.Pk
        INTO v_logoimage
        FROM mediaformat M
       WHERE M.p_QUALIFIER = '157Wx157H';


      --This is full data send to data feed,First
      -- delete the data from datafeed then pull the latest categoies data from commerce db

      DELETE FROM datafeed.category;

      --Joins Price,Inventory,Delta tables and merge the result data into category table
      MERGE INTO datafeed.category C
           USING (SELECT D.p_code, D.p_name, CS.p_code AS PARENT
                    FROM (SELECT C.p_code, CLP.p_name, CC.SOURCEPK
                            FROM categorieslp CLP,
                                 cat2catrel CC,
                                 categories C
                           WHERE     C.PK = CLP.ITEMPK
                                 AND C.PK = CC.TARGETPK
                                 AND C.P_CATALOGVERSION =
                                        v_catalogversion_online
                                 AND c.p_code LIKE 'MSH%') D,
                         categories CS
                   WHERE CS.pk = D.SOURCEPK) S
              ON (C.category_id = S.p_code AND C.parent_category = S.parent)
      WHEN MATCHED
      THEN
         UPDATE SET C.category_name = S.p_name, C.status = 0
      WHEN NOT MATCHED
      THEN
         INSERT     (C.category_id,
                     C.category_name,
                     C.parent_category,
                     C.status)
             VALUES (S.p_code,
                     S.p_name,
                     S.parent,
                     0);

      OPEN CATMEDIMG;

      FETCH CATMEDIMG BULK COLLECT INTO v_catimg;

      FORALL i IN 1 .. v_catimg.COUNT
         UPDATE DATAFEED.category
            SET IMAGE_URL = v_catimg (i).imgurl
          WHERE CATEGORY_ID = v_catimg (i).catid;

      CLOSE CATMEDIMG;



      OPEN CATLOGOIMG;

      FETCH CATLOGOIMG BULK COLLECT INTO v_catlogoimg;

      FORALL i IN 1 .. v_catlogoimg.COUNT
         UPDATE DATAFEED.category
            SET LOGO_URL = v_catlogoimg (i).imgurl
          WHERE CATEGORY_ID = v_catlogoimg (i).catid;

      CLOSE CATLOGOIMG;

      --to Update the last run time
      UPDATE MPLBUYBOXUPDTLOG
         SET MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME = v_start_time_category
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'ia_category';

      COMMIT;
   END ia_category;

   /**************************************************************************************
    /*  Procedure will calculate and update the datafeed collections table
   /*  for the  updated collections.
    /*  It has to be invoked periodically
    /*  Delta will be identified by modifiedts columns of collection table
    /*
    ***************************************************************************************/

   PROCEDURE ia_collection
   IS
      v_last_run_collection     TIMESTAMP;
      v_start_time_collection   TIMESTAMP;
      v_catalogversion_online   NUMBER;
   BEGIN
      -- assign the last run time value into local variable
      SELECT MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME
        INTO v_last_run_collection
        FROM MPLBUYBOXUPDTLOG
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'ia_collection';

      -- assign the current timestamp value into local variable
      SELECT TO_TIMESTAMP (
                TO_CHAR (SYSTIMESTAMP, 'rrrr-MM-DD hh24:mi:ss.FF3'),
                'rrrr-MM-DD hh24:mi:ss.FF3')
        INTO v_start_time_collection
        FROM DUAL;

      -- assign the product catalog version value into local variable
      SELECT CV.pk
        INTO v_catalogversion_online
        FROM catalogs c, catalogversions CV
       WHERE     c.p_id = 'mplProductCatalog'
             AND c.pk = CV.p_catalog
             AND CV.p_version = 'Online';

      /** This job is  full feed, first remove the data from collection then pull the whole collection
       data from commerce Db
      **/

      DELETE FROM datafeed.COLLECTION;

      --Joins collectionname collection category startdate end date and remaing values stores into collection table.
      MERGE INTO datafeed.COLLECTION C
           USING (SELECT MSL.P_COLLECTIONID,
                         MSL.P_COLLECTIONNAME,
                         MSL.P_COLLECTIONCATEGORY,
                         MSL.P_OCCASION,
                         MSL.P_TARGETAGEGROUP,
                         MSL.P_GENDER,
                         MSL.P_STARTDATE,
                         MSL.P_ENDDATE,
                         CONCAT ('/medias/sys_master/', m.p_location)
                            AS topright,
                         CONCAT ('/medias/sys_master/', m1.p_location)
                            AS topleft,
                         CONCAT ('/medias/sys_master/', m2.p_location)
                            AS bottomright,
                         CONCAT ('/medias/sys_master/', m3.p_location)
                            AS bottomleft,
                         J.PRODUCT_COLLECTION
                    FROM (  SELECT ML.SOURCEPK,
                                   LISTAGG (P.P_CODE, ',')
                                      WITHIN GROUP (ORDER BY P_CODE DESC)
                                      PRODUCT_COLLECTION
                              FROM MPLSHOPRELATIONTABLE ML, PRODUCTS P
                             WHERE ML.TARGETPK = P.PK
                          GROUP BY ML.SOURCEPK) J,
                         MPLSHOPBYLOOK MSL,
                         medias M,
                         medias M1,
                         medias M2,
                         medias M3
                   WHERE     MSL.PK = J.SOURCEPK
                         AND M.pk = MSL.P_TOPRIGHT
                         AND M1.pk = MSL.P_TOPLEFT
                         AND M2.pk = MSL.P_BOTTOMRIGHT
                         AND M3.pk = MSL.P_BOTTOMLEFT -- AND M.P_CATALOGVERSION = v_catalogversion_online
                                                     ) S
              ON (C.COLLECTION_ID = S.P_COLLECTIONID)
      WHEN MATCHED
      THEN
         UPDATE SET C.COLLECTION_NAME = S.P_COLLECTIONNAME,
                    C.COLLECTION_CATEGORY = S.P_COLLECTIONCATEGORY,
                    C.PRODUCT_COLLECTION = S.PRODUCT_COLLECTION,
                    C.OCCASION = S.P_OCCASION,
                    C.TARGET_GROUP = S.P_TARGETAGEGROUP,
                    C.GENDER = S.P_GENDER,
                    C.TOP_RIGHT_IMAGE_URL = S.topright,
                    C.TOP_LEFT_IMAGE_URL = S.topleft,
                    C.BOTTOM_RIGHT_IMAGE_URL = S.bottomright,
                    C.BOTTOM_LEFT_IMAGE_URL = S.bottomleft,
                    C.START_DATE = S.P_STARTDATE,
                    C.END_DATE = S.P_ENDDATE,
                    C.STATUS = 0
      WHEN NOT MATCHED
      THEN
         INSERT     (COLLECTION_ID,
                     COLLECTION_NAME,
                     COLLECTION_CATEGORY,
                     PRODUCT_COLLECTION,
                     OCCASION,
                     TARGET_GROUP,
                     GENDER,
                     TOP_RIGHT_IMAGE_URL,
                     TOP_LEFT_IMAGE_URL,
                     BOTTOM_RIGHT_IMAGE_URL,
                     BOTTOM_LEFT_IMAGE_URL,
                     START_DATE,
                     END_DATE,
                     STATUS)
             VALUES (S.P_COLLECTIONID,
                     S.P_COLLECTIONNAME,
                     S.P_COLLECTIONCATEGORY,
                     S.PRODUCT_COLLECTION,
                     S.P_OCCASION,
                     S.P_TARGETAGEGROUP,
                     S.P_GENDER,
                     S.topright,
                     S.topleft,
                     S.bottomright,
                     S.bottomleft,
                     S.P_STARTDATE,
                     S.P_ENDDATE,
                     0);

      --to Update the last run time
      UPDATE MPLBUYBOXUPDTLOG
         SET MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME = v_start_time_collection
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'ia_collection';

      COMMIT;
   END ia_collection;

   /**************************************************************************************
             /*  we need to implement the ia_userpreference

             ***************************************************************************************/



   PROCEDURE ia_catprdrel
   IS
      v_last_run_catprdrel      TIMESTAMP;
      v_start_time_catprdrel    TIMESTAMP;
      v_catalogversion_online   NUMBER;

      CURSOR PRDRELCUR
      IS
         SELECT A.p_code, A.listing_id
           FROM (SELECT C.p_code, P.p_code AS listing_id
                   FROM categories C, products P, cat2prodrel CP
                  WHERE     C.PK = CP.SOURCEPK
                        AND P.PK = CP.TARGETPK
                        AND C.P_CATALOGVERSION = v_catalogversion_online
                        AND C.p_code LIKE 'MSH%') A,
                (SELECT DISTINCT CA.p_code
                   FROM cat2prodrel CPL, categories CA
                  WHERE     CA.p_code LIKE 'MSH%'
                        AND CPL.SOURCEPK = CA.PK
                        AND CPL.MODIFIEDTS > v_last_run_catprdrel) B
          WHERE A.p_code = B.p_code;

      TYPE prdrel IS RECORD
      (
         catid    CATEGORIES.p_code%TYPE,
         listid   PRODUCTS.p_code%TYPE
      );

      TYPE PRDRELARR IS TABLE OF prdrel;

      v_catprd                  prdrelarr;
   BEGIN
      -- assign the last run time value into local variable
      SELECT MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME
        INTO v_last_run_catprdrel
        FROM MPLBUYBOXUPDTLOG
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'ia_catprdrel';

      -- assign the product catalog version value into local variable
      SELECT CV.pk
        INTO v_catalogversion_online
        FROM catalogs c, catalogversions CV
       WHERE     c.p_id = 'mplProductCatalog'
             AND c.pk = CV.p_catalog
             AND CV.p_version = 'Online';


      /** It is incremental feed it will delete the records status=1
       then pull the data incrementally from commerce db
      **/

      DELETE FROM datafeed.catprdrel
            WHERE status = 1;

      OPEN PRDRELCUR;

      LOOP
         FETCH PRDRELCUR BULK COLLECT INTO v_catprd LIMIT 100;

         FORALL i IN 1 .. v_catprd.COUNT
            INSERT INTO datafeed.catprdrel (category_id, listing_id, status)
                 VALUES (v_catprd (i).catid, v_catprd (i).listid, 0);

         EXIT WHEN PRDRELCUR%NOTFOUND;
      END LOOP;

      CLOSE PRDRELCUR;

      -- assign the current timestamp value into local variaRRBle
      SELECT TO_TIMESTAMP (
                TO_CHAR (SYSTIMESTAMP, 'rrrr-MM-DD hh24:mi:ss.FF3'),
                'rrrr-MM-DD hh24:mi:ss.FF3')
        INTO v_start_time_catprdrel
        FROM DUAL;

      --to Update the last run time
      UPDATE MPLBUYBOXUPDTLOG
         SET MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME = v_start_time_catprdrel
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'ia_catprdrel';

      COMMIT;
   END ia_catprdrel;

   /**************************************************************************************
               /*  we need to implement the ia_userpreference

              ***************************************************************************************/



   PROCEDURE ia_brandprdrel
   IS
      v_last_run_brandprdrel     TIMESTAMP;
      v_start_time_brandprdrel   TIMESTAMP;
      v_catalogversion_online    NUMBER;

      CURSOR PRDRELCUR
      IS
         SELECT A.p_code, A.listing_id
           FROM (SELECT C.p_code, P.p_code AS listing_id
                   FROM categories C, products P, cat2prodrel CP
                  WHERE     C.PK = CP.SOURCEPK
                        AND P.PK = CP.TARGETPK
                        AND C.P_CATALOGVERSION = v_catalogversion_online
                        AND C.p_code LIKE 'MBH%') A,
                (SELECT DISTINCT CA.p_code
                   FROM cat2prodrel CPL, categories CA
                  WHERE     CA.p_code LIKE 'MBH%'
                        AND CPL.SOURCEPK = CA.PK
                        AND CPL.MODIFIEDTS > v_last_run_brandprdrel) B
          WHERE A.p_code = B.p_code;


      TYPE prdrel IS RECORD
      (
         brandid   CATEGORIES.p_code%TYPE,
         listid    PRODUCTS.p_code%TYPE
      );

      TYPE PRDRELARR IS TABLE OF prdrel;

      v_catprd                   prdrelarr;
   BEGIN
      -- assign the last run time value into local variable
      SELECT MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME
        INTO v_last_run_brandprdrel
        FROM MPLBUYBOXUPDTLOG
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'ia_brandprdrel';

      -- assign the product catalog version value into local variable
      SELECT CV.pk
        INTO v_catalogversion_online
        FROM catalogs c, catalogversions CV
       WHERE     c.p_id = 'mplProductCatalog'
             AND c.pk = CV.p_catalog
             AND CV.p_version = 'Online';


      /** It is incremental feed it will delete the records where status=1
        then pull the data incrementally from commerce db
       **/
      DELETE FROM datafeed.brandprdrel
            WHERE status = 1;

      OPEN PRDRELCUR;

      LOOP
         FETCH PRDRELCUR BULK COLLECT INTO v_catprd LIMIT 100;

         FORALL i IN 1 .. v_catprd.COUNT
            INSERT INTO datafeed.brandprdrel (brand_id, listing_id, status)
                 VALUES (v_catprd (i).brandid, v_catprd (i).listid, 0);

         EXIT WHEN PRDRELCUR%NOTFOUND;
      END LOOP;

      CLOSE PRDRELCUR;

      -- assign the current timestamp value into local variaRRBle
      SELECT TO_TIMESTAMP (
                TO_CHAR (SYSTIMESTAMP, 'rrrr-MM-DD hh24:mi:ss.FF3'),
                'rrrr-MM-DD hh24:mi:ss.FF3')
        INTO v_start_time_brandprdrel
        FROM DUAL;

      --to Update the last run time
      UPDATE MPLBUYBOXUPDTLOG
         SET MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME = v_start_time_brandprdrel
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'ia_brandprdrel';

      COMMIT;
   END ia_brandprdrel;

   /**************************************************************************************
               /*  we need to implement the ia_userpreference

               ***************************************************************************************/



   PROCEDURE ia_brands
   IS
      v_last_run_brands         TIMESTAMP;
      v_start_time_brands       TIMESTAMP;
      v_catalogversion_online   NUMBER;
      v_overlay_image_url       NUMBER;
      v_imageurl                NUMBER;
      v_logo_image_url          NUMBER;

      --logo image url cursor
      CURSOR BRDLOGOIMG
      IS
         SELECT C.p_code, p_internalurl AS logourl
           FROM cat2medrel CM, medias M, categories C
          WHERE     C.PK = CM.SOURCEPK
                AND M.p_mediaformat = v_logo_image_url
                AND M.PK = CM.TARGETPK
                AND C.P_CATALOGVERSION = v_catalogversion_online
                AND c.p_code LIKE 'MBH%';

      TYPE BRDLOGO IS RECORD
      (
         brandid   CATEGORIES.p_code%TYPE,
         logourl   MEDIAS.p_internalurl%TYPE
      );

      TYPE BRDLOGORR IS TABLE OF BRDLOGO;

      v_brdlogoimg              BRDLOGORR;

      -- image url cursor
      CURSOR BRDMEDIMG
      IS
         SELECT C.p_code, p_internalurl AS imgurl
           FROM cat2medrel CM, medias M, categories C
          WHERE     C.PK = CM.SOURCEPK
                AND M.p_mediaformat = v_imageurl
                AND M.PK = CM.TARGETPK
                AND C.P_CATALOGVERSION = v_catalogversion_online
                AND c.p_code LIKE 'MBH%';

      TYPE BRDIMG IS RECORD
      (
         brandid   CATEGORIES.p_code%TYPE,
         imgurl    MEDIAS.p_internalurl%TYPE
      );

      TYPE BRDIMGARR IS TABLE OF BRDIMG;

      v_brdimg                  BRDIMGARR;

      -- overlay image
      CURSOR BRDOVERLAYIMG
      IS
         SELECT C.p_code, p_internalurl AS overlayurl
           FROM cat2medrel CM, medias M, categories C
          WHERE     C.PK = CM.SOURCEPK
                AND M.p_mediaformat = v_overlay_image_url
                AND M.PK = CM.TARGETPK
                AND C.P_CATALOGVERSION = v_catalogversion_online
                AND c.p_code LIKE 'MBH%';

      TYPE BRDOLIMG IS RECORD
      (
         brandid      CATEGORIES.p_code%TYPE,
         overlayurl   MEDIAS.p_internalurl%TYPE
      );

      TYPE BRDOLRR IS TABLE OF BRDOLIMG;

      v_brdolimg                BRDOLRR;
   BEGIN
      -- assign the last run time value into local variable
      SELECT MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME
        INTO v_last_run_brands
        FROM MPLBUYBOXUPDTLOG
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'ia_brands';

      -- assign the current timestamp value into local variaRRBle
      SELECT TO_TIMESTAMP (
                TO_CHAR (SYSTIMESTAMP, 'rrrr-MM-DD hh24:mi:ss.FF3'),
                'rrrr-MM-DD hh24:mi:ss.FF3')
        INTO v_start_time_brands
        FROM DUAL;

      -- assign the product catalog version value into local variable
      SELECT CV.pk
        INTO v_catalogversion_online
        FROM catalogs c, catalogversions CV
       WHERE     c.p_id = 'mplProductCatalog'
             AND c.pk = CV.p_catalog
             AND CV.p_version = 'Online';

      -- assign the mediafromatvalue pk  value into mediaformat variable
      SELECT M.Pk
        INTO v_logo_image_url
        FROM mediaformat M
       WHERE M.p_QUALIFIER = '135Wx60H';

      -- assign the mediafromatvalue pk  value into mediaformat variable
      SELECT M.Pk
        INTO v_overlay_image_url
        FROM mediaformat M
       WHERE M.p_QUALIFIER = '206Wx206H';

      -- assign the mediafromatvalue pk  value into mediaformat variable
      SELECT M.Pk
        INTO v_imageurl
        FROM mediaformat M
       WHERE M.p_QUALIFIER = '432Wx439H';


      /** This job is  full feed, first remove the data from data feed brands then pull the whole brand
      data from commerce Db
     **/

      DELETE FROM datafeed.brands;

      --Joins Pricecount,avgcount,onestar to fivestar merge the result data into rating_review table
      MERGE INTO datafeed.brands B
           USING (SELECT D.p_code, D.p_name, CS.p_code AS PARENT
                    FROM (SELECT C.p_code, CLP.p_name, CC.SOURCEPK
                            FROM categorieslp CLP,
                                 cat2catrel CC,
                                 categories C
                           WHERE     C.PK = CLP.ITEMPK
                                 AND C.PK = CC.TARGETPK
                                 AND C.P_CATALOGVERSION =
                                        v_catalogversion_online
                                 AND c.p_code LIKE 'MBH%') D,
                         categories CS
                   WHERE CS.pk = D.SOURCEPK) S
              ON (B.brand_id = S.p_code AND B.brandparent_category = S.parent)
      WHEN MATCHED
      THEN
         UPDATE SET B.brand_name = S.p_name, B.status = 0
      WHEN NOT MATCHED
      THEN
         INSERT     (brand_id,
                     brand_name,
                     brandparent_category,
                     status)
             VALUES (S.p_code,
                     S.p_name,
                     S.parent,
                     0);

      OPEN BRDLOGOIMG;

      FETCH BRDLOGOIMG BULK COLLECT INTO v_brdlogoimg;

      DBMS_OUTPUT.PUT_LINE ('COUNT++' || v_brdlogoimg.COUNT);

      FORALL i IN 1 .. v_brdlogoimg.COUNT
         UPDATE DATAFEED.brands
            SET LOGO_IMAGE_URL = v_brdlogoimg (i).logourl
          WHERE brand_id = v_brdlogoimg (i).brandid;

      CLOSE BRDLOGOIMG;

      OPEN BRDMEDIMG;

      FETCH BRDMEDIMG BULK COLLECT INTO v_brdimg;

      FORALL i IN 1 .. v_brdimg.COUNT
         UPDATE DATAFEED.brands
            SET IMAGE_URL = v_brdimg (i).imgurl
          WHERE brand_id = v_brdimg (i).brandid;

      CLOSE BRDMEDIMG;

      OPEN BRDOVERLAYIMG;

      FETCH BRDOVERLAYIMG BULK COLLECT INTO v_brdolimg;

      FORALL i IN 1 .. v_brdolimg.COUNT
         UPDATE DATAFEED.brands
            SET OVERLAY_IMAGE_URL = v_brdolimg (i).overlayurl
          WHERE brand_id = v_brdolimg (i).brandid;

      CLOSE BRDOVERLAYIMG;

      --to Update the last run time
      UPDATE MPLBUYBOXUPDTLOG
         SET MPLBUYBOXUPDTLOG.P_LAST_RUN_TIME = v_start_time_brands
       WHERE MPLBUYBOXUPDTLOG.P_PROC_NAME = 'ia_brands';

      COMMIT;
   END ia_brands;
END;
/
