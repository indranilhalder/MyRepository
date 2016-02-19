CREATE TABLE BUYBOXERROR_LOG(
   ERROR_CODE      NUMBER,
   ERROR_MESSAGE   VARCHAR2 (4000),
   BACKTRACE       CLOB,
   CALLSTACK       CLOB,
   CREATED_ON      DATE,
   CREATED_BY      VARCHAR2 (30));
