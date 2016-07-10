------------------------------------------------------------------
--  PACKAGE ia_datafeed_priceinv_pkg
------------------------------------------------------------------

CREATE OR REPLACE PACKAGE "ia_datafeed_priceinv_pkg"
AS
   /**************************************************************************************
               /*  Procedure will calculate and update the datafeed price inventory table
              /*  for the price/inventory updated skus.
               /*  It has to be invoked periodically
               /*  Delta will be identified by modifiedts column of buybox table
               /*
               ***************************************************************************************/

   PROCEDURE ia_price_inventory;
END;
/



