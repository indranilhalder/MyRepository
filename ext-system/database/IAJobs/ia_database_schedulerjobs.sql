BEGIN
DBMS_SCHEDULER.CREATE_JOB (
   job_name           =>  'ia_category_dbjob',
   job_type           =>  'PLSQL_BLOCK',
   job_action         =>  'BEGIN mplprdcom."ia_datafeed_pkg".ia_category();END;',
   start_date         =>  '10-OCT-2015 01.00.00 PM',
   repeat_interval    =>  'FREQ=DAILY; BYHOUR=0; BYMINUTE=5; BYSECOND=0', /* every day at 12:05  night  */
   end_date           =>  '20-NOV-2050 07.00.00 PM',
   comments           =>  'Job to update category table');
END;
/


BEGIN
DBMS_SCHEDULER.CREATE_JOB (
   job_name           =>  'ia_catprdrel_dbjob',
   job_type           =>  'PLSQL_BLOCK',
   job_action         =>  'BEGIN mplprdcom."ia_datafeed_pkg".ia_catprdrel();END;',
   start_date         =>  '10-OCT-2015 01.00.00 PM',
   repeat_interval    =>  'FREQ=HOURLY;BYMINUTE=0; BYSECOND=0', /* every one hour  */
   end_date           =>  '20-NOV-2050 7.30.00 PM',
   comments           =>  'Job to update catprdrel table');
END;
/

BEGIN
DBMS_SCHEDULER.CREATE_JOB (
   job_name           =>  'ia_collection_dbjob',
   job_type           =>  'PLSQL_BLOCK',
   job_action         =>  'BEGIN mplprdcom."ia_datafeed_pkg".ia_collection();END;',
   start_date         =>  '10-OCT-2015 01.00.00 PM',
   repeat_interval    =>  'FREQ=DAILY; BYHOUR=0; BYMINUTE=25; BYSECOND=0', /* every day 12:25 mid night */
   end_date           =>  '20-NOV-2050 7.30.00 PM',
   comments           =>  'Job to update collection table');
END;
/

BEGIN
DBMS_SCHEDULER.CREATE_JOB (
   job_name           =>  'ia_brandprdrel_dbjob',
   job_type           =>  'PLSQL_BLOCK',
   job_action         =>  'BEGIN mplprdcom."ia_datafeed_pkg".ia_brandprdrel();END;',
   start_date         =>  '10-OCT-2015 01.00.00 PM',
    repeat_interval    =>  'FREQ=HOURLY;BYMINUTE=0; BYSECOND=0', /* every one hour  */
   end_date           =>  '20-NOV-2050 7.30.00 PM',
   comments           =>  'Job to update brandsprel table');
END;
/



BEGIN
DBMS_SCHEDULER.CREATE_JOB (
   job_name           =>  'ia_brands_dbjob',
   job_type           =>  'PLSQL_BLOCK',
   job_action         =>  'BEGIN mplprdcom."ia_datafeed_pkg".ia_brands();END;',
   start_date         =>  '10-OCT-2015 01.00.00 PM',
   repeat_interval    =>  'FREQ=DAILY; BYHOUR=0; BYMINUTE=15; BYSECOND=0', /* every  day  12:15 mid night */
   end_date           =>  '20-NOV-2050 7.30.00 PM',
   comments           =>  'Job to update brands table');
END;
/


BEGIN
DBMS_SCHEDULER.CREATE_JOB (
   job_name           =>  'ia_price_inventory_dbjob',
   job_type           =>  'PLSQL_BLOCK',
   job_action         =>  'BEGIN mplprdcom."ia_datafeed_priceinv_pkg".ia_price_inventory();END;',
   start_date         =>  '10-OCT-2015 01.00.00 PM',
   repeat_interval    =>  'FREQ=HOURLY;BYMINUTE=0; BYSECOND=0', /* every one hour  */
   end_date           =>  '20-NOV-2050 7.30.00 PM',
   comments           =>  'Job to update price_invetory table');
END;
/