------------------------------------------------------------------
--  FUNCTION GETPK
------------------------------------------------------------------

CREATE OR REPLACE FUNCTION GETPK (TYPECODE IN NUMBER, COUNTER IN NUMBER)
   RETURN NUMBER
IS
   v_pk   NUMBER;
BEGIN
   v_pk := BITOR (Counter * 32768, Typecode);


   RETURN v_pk;
END;
/



