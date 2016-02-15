------------------------------------------------------------------
--  PROCEDURE RECORD_ERROR
------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE record_error
IS
   PRAGMA AUTONOMOUS_TRANSACTION;
   l_code   PLS_INTEGER := SQLCODE;
   l_mesg   VARCHAR2 (32767) := SQLERRM;
BEGIN
   INSERT INTO buyboxerror_log (ERROR_CODE,
                                error_message,
                                backtrace,
                                callstack,
                                created_on,
                                created_by)
        VALUES (l_code,
                l_mesg,
                sys.DBMS_UTILITY.format_error_backtrace,
                sys.DBMS_UTILITY.format_call_stack,
                SYSDATE,
                USER);

   COMMIT;
END;
/



