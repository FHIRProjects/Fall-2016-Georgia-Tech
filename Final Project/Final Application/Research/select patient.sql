select 
	dbo.csd_persons2transactions2.trans_id as patient_id, 
	dbo.trans_hist.trans_key, 
	dbo.trans_hist.lname, 
	dbo.trans_hist.fname, 
	dbo.trans_hist.street_address, 
	dbo.trans_hist.state, 
	dbo.trans_hist.zip_code, 
	dbo.trans_hist.sex_code, 
	dbo.trans_hist.birth_date 
from dbo.trans_hist, dbo.csd_persons2transactions2
where dbo.trans_hist.trans_key = dbo.csd_persons2transactions2.csdid::int;
