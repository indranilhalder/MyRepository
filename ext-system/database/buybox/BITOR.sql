------------------------------------------------------------------
--  FUNCTION BITOR
------------------------------------------------------------------

CREATE OR REPLACE FUNCTION BITOR (X IN NUMERIC, Y IN NUMERIC)
   RETURN NUMERIC
IS
BEGIN
   RETURN (X + Y) - BITAND (X, Y);
END;
/



