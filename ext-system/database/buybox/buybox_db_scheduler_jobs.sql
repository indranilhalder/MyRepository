BEGIN
DBMS_SCHEDULER.CREATE_JOB (
   job_name           =>  'buybox_weightage_dbjob',
   job_type           =>  'PLSQL_BLOCK',
   job_action         =>  'BEGIN mplprdcom."buybox_package".BUYBOX_WEIGHTAGE();END;',
   start_date         =>  '10-NOV-2015 03.05.00 PM',
   repeat_interval    =>  'FREQ=MINUTELY;INTERVAL=15', /* every 15 mins */
   end_date           =>  '20-NOV-2050 07.00.00 PM',
   comments           =>  'Job to update weightage for price/inv update');
END;
/


BEGIN
DBMS_SCHEDULER.CREATE_JOB (
   job_name           =>  'buybox_price_update_dbjob',
   job_type           =>  'PLSQL_BLOCK',
   job_action         =>  'BEGIN mplprdcom."buybox_package".BUYBOX_PRICE_UPDATE();END;',
   start_date         =>  '10-NOV-2015 03.05.00 PM',
   repeat_interval    =>  'FREQ=MINUTELY;INTERVAL=15', /* every 15 mins */
   end_date           =>  '20-NOV-2050 7.30.00 PM',
   comments           =>  'Job to update weightage for promotional price update');
END;
/



BEGIN
DBMS_SCHEDULER.CREATE_JOB (
  job_name            =>  'buybox_dateupdate_dbjob',
   job_type           =>  'PLSQL_BLOCK',
   job_action         =>  'BEGIN mplprdcom."buybox_package".BUYBOX_DATEUPDATE();END;',
   start_date         =>  '10-NOV-2015 03.05.00 PM',
  repeat_interval    =>  'FREQ=DAILY; BYHOUR=0; BYMINUTE=0; BYSECOND=0', /* every day mid night */
   end_date           =>  '20-NOV-2050 7.30.00 PM',
   comments           =>  'Job to date update  to dateand time for sellerinformation');
END;
/