BEGIN
DBMS_SCHEDULER.ENABLE('BUYBOX_WEIGHTAGE_DBJOB, BUYBOX_PRICE_UPDATE_DBJOB');
END;
/

BEGIN
DBMS_SCHEDULER.ENABLE('BUYBOX_DATEUPDATE_DBJOB');
END;
/