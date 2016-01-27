------------------------------------------------------------------
--  PACKAGE ia_datafeed_pkg
------------------------------------------------------------------

CREATE OR REPLACE PACKAGE "ia_datafeed_pkg"
AS
   /**************************************************************************************
          /*  Procedure will calculate and update the datafeed category table
         /*  for the  updated categories.
          /*  It has to be invoked periodically
          /*  Delta will be identified by modifiedts column of price/stocklevels table
          /*
          ***************************************************************************************/

   PROCEDURE ia_category;

   /**************************************************************************************
    /*  Procedure will calculate and update the datafeed collection table
   /*  for the  updated categories.
    /*  It has to be invoked periodically
    /*  Delta will be identified by modifiedts column of price/stocklevels table
    /*
    ***************************************************************************************/

   PROCEDURE ia_collection;

   /**************************************************************************************
    /*  Procedure will calculate and update the datafeed collection table
   /*  for the  updated collection.
    /*  It has to be invoked periodically
    /*  Delta will be identified by modifiedts  collection columns
    /*
    ***************************************************************************************/



   /**************************************************************************************
    /*  Procedure will calculate and update the datafeed user_preference table
   /*  for the  updated user_preference.
    /*  It has to be invoked periodically
    /*  Delta will be identified by modifiedts columns user_preference table
    /*
    ***************************************************************************************/


   PROCEDURE ia_brands;

   /**************************************************************************************
    /*  Procedure will calculate and update the datafeed brands table
   /*  for the  updated brands.
    /*  It has to be invoked periodically
     /*
    ***************************************************************************************/

   PROCEDURE ia_catprdrel;

   /**************************************************************************************
    /*  Procedure will calculate and update the datafeed category product association
   /*  for the  updated categoriess.
    /*  It has to be invoked periodically
     /*
    ***************************************************************************************/

   PROCEDURE ia_brandprdrel;
/**************************************************************************************
 /*  Procedure will calculate and update the datafeed category product association
/*  for the  updated categoriess.
 /*  It has to be invoked periodically
  /*
 ***************************************************************************************/
END;
/



