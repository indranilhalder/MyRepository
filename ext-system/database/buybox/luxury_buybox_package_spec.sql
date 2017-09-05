------------------------------------------------------------------
--  PACKAGE buybox_package
------------------------------------------------------------------

CREATE OR REPLACE PACKAGE "lux_buybox_package"
AS
   /**************************************************************************************
          /*  Procedure will calculate and update the buybox weightage for the price/inventory updated skus
          /*  It has to be invoked periodically
          /*  Delta will be identified by modifiedts column of price/stocklevels table
          /*      ***************************************************************************************/
   PROCEDURE lux_buybox_weightage;
   /***************************************************************************************
      /*  Procedure to update the special p_price when promotion start and end.
      /*  When promotion start:
      /*    It will take the special price and update the weightage
      /*  When promotion end:
      /*    it will take regular price and update the weightage
      ***************************************************************************************/
   PROCEDURE lux_buybox_price_update;
   /***************************************************************************************
      /*  Procedure to update the sellerstartdate and seller enddate when you change the mplsellerino table.
      /*  When promotion start:
      /*    It will take the seller startdate and enddate update the mplbuybox table
***************************************************************************************/
   PROCEDURE lux_buybox_dateupdate;
END;
/



