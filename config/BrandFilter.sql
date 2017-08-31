create or replace PACKAGE BODY "brandFilter_package"
AS
   /**************************************************************************************
   /*  Procedure will generate filter URL's for All Root Categories
   /*  It has to be invoked periodically 
   /*  No Delta Runs
   /****************************************************************************************/

   PROCEDURE buybox_createFilter
   IS
	  v_typepk_brandfilter         		NUMBER;
      v_itemtypecode_brandfilter        NUMBER;
      v_catalogversion_productOnline    NUMBER;
	  v_prc_start_time_createFilter   	TIMESTAMP;
      
 
BEGIN
	 
      -- assign the brandfilter typepkstring value into local variable
      SELECT pk
        INTO v_typepk_brandfilter
        FROM composedtypes
       WHERE internalcode = 'Mplbrandfilter';

      -- assign the buybox itemtypecode value into local variable
      SELECT itemtypecode
        INTO v_itemtypecode_brandfilter
        FROM composedtypes
       WHERE internalcode = 'Mplbrandfilter';
	   
	    -- assign the current timestamp value into local variable
      SELECT TO_TIMESTAMP (
                TO_CHAR (SYSTIMESTAMP, 'rrrr-MM-DD hh24:mi:ss.FF3'),
                'rrrr-MM-DD hh24:mi:ss.FF3')
        INTO v_prc_start_time_createFilter
        FROM DUAL;

      -- assign the product catalog version value into local variable
      SELECT CV.pk
        INTO v_catalogversion_productOnline
        FROM catalogs c, catalogversions CV
       WHERE     c.p_id = 'mplProductCatalog'
             AND c.pk = CV.p_catalog
             AND CV.p_version = 'Online';
	   
	  DELETE FROM Mplbrandfilter;
	   
	   COMMIT;
	   
	    INSERT INTO Mplbrandfilter B
		( B.hjmpts,
      B.pk,
			B.typepkstring,
			B.modifiedts,
      B.createdts,
		  B.p_L1,
			B.p_L2,
			B.p_L3,
			B.p_brandcode,
			B.p_url3,
			B.p_url2,
			--B.p_url1,
      B.ACLTS,
      B.PROPTS
      --B.OWNERPKSTRING
		)
                  SELECT 
									'0',
               		GETPK (v_itemtypecode_brandfilter, BRANDFILTER_SEQ.NEXTVAL),
									v_typepk_brandfilter,
									v_prc_start_time_createFilter,
									v_prc_start_time_createFilter,
                  sub.L1,
                  sub.L2,
                  sub.L3,
                  sub.BrandCode,
                  sub.URL3,
                  sub.URL2,
                  --sub.URL1,
                   '0',
                   '0'
                  --'0'
                  from
            ( SELECT distinct
            			c3.p_code L1,
									c2.p_code L2,
									c1.p_code L3,
									b1.p_code BrandCode, 
									REPLACE(REPLACE(REPLACE(LOWER(cname2.p_name||'-'||cname1.p_name||'-'||cname.p_name||'-'||bname.p_name||'/c-'||c1.p_code||'/b-'||b1.p_code),' ', '-'),'''',''),'&','&amp;') "URL3", 
									REPLACE(REPLACE(REPLACE(LOWER(cname2.p_name||'-'||cname1.p_name||'-'||bname.p_name||'/c-'||c2.p_code||'/b-'||b1.p_code),' ','-'),'''',''),'&','&amp;')"URL2"
									--REPLACE(REPLACE(REPLACE(LOWER(cname2.p_name||'-'||bname.p_name||'/c-'||c3.p_code||'/b-'||b1.p_code),' ','-'),'''',''),'&',';') "URL1"
                  --'0',
                  --'0'
                  --'0'
							FROM 	cat2prodrel c2p1,
									cat2prodrel b2p1,
									cat2catrel c2c1,
									cat2catrel c2c2,
									categories c3,
									categories c2, 
									categories c1,
									categories b1,
									products p1,
									mplbuybox bb,
									categorieslp cname,
									categorieslp cname1,
									categorieslp cname2,
									categorieslp bname
							WHERE
									p1.p_catalogversion=v_catalogversion_productOnline
								AND c2p1.targetpk=p1.pk
								AND bb.p_product=p1.p_code
								AND bb.p_delisted ='0'
								AND (SYSDATE BETWEEN  bb.p_sellerstartdate  AND  bb.p_sellerenddate )
								AND c2p1.sourcepk=c1.pk
								AND c1.p_code LIKE 'MSH%'
								AND b2p1.targetpk=p1.pk
								AND b2p1.sourcepk=b1.pk
								AND b1.p_code LIKE 'MBH%'
								AND cname.itempk=c1.pk
								AND bname.itempk=b1.pk
								AND c2c1.targetpk=c1.pk
								AND c2c1.sourcepk=c2.pk
								AND cname1.itempk=c2.pk
								AND c2c2.targetpk=c2.pk
								AND c2c2.sourcepk=c3.pk
								AND cname2.itempk=c3.pk
								) sub;
								 COMMIT;
							 

END buybox_createFilter;
END;