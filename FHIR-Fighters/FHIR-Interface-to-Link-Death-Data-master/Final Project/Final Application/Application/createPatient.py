#!/usr/bin/python

# To use:
# pip install fhirclient

# FHIRFighters - GTCS6440
#	Peter Graening
#	Tony Potter
#	Shashindra Pradhan
#	Ali Shaikh
#	Radha Venkataraman

import fhirclient

# Build a FHIR Practitioner object based on the record.
# The dohmpi field set needs to be sent as well to correlate the fields correctly.
def createPatient(dohmpifield, record):
    #extract fields from dohmpi from database
    field = []
    for i in range(len(dohmpifield)):
        f1, = dohmpifield[i]    # dohmpi loads from db as a list of onetuples.
        field.append(f1)        # we make a traditional list to refer to.
    
    fnamei = field.index('provider_first')
    lnamei = field.index('provider_last')
    genderi = field.index('provider_gender_cd')
    deathyi = field.index('deathccyy')
    deathmi = field.index('deathmm')
    deathdi = field.index('deathdd')
    birthi = field.index('provider_dob_dob')
    provi = field.index('provider_license_no')
    
    # record is one record from the postgres db
    import fhirclient.models.patient as p
    
    # set patient id as provider licence numberS
    pid = record[provi]
    p = p.Patient({})
    import fhirclient.models.identifier as idf
    identifier = idf.Identifier()
    identifier.value = pid
    p.identifier = [identifier]
    
    # resolve name
    import fhirclient.models.humanname as hn
    name = hn.HumanName()
    name.given = [record[fnamei]]
    name.family = [record[lnamei]]
    p.name = [name]
    #[name.family, name.given]
    
    # resolve gender
    if (record[genderi] == "1"):
        p.gender = 'male'
    elif (record[genderi] == "2"):
        p.gender = 'female'
    else:
        p.gender = 'unknown'
        
    # build and resolve birthdate
    birthdate = record[birthi]
    bsep1 = birthdate.index("/")
    bsep2 = birthdate.index("/",bsep1+1)
    year = birthdate[-4:]
    month = birthdate[0:bsep1]
    if (len(month) < 2):
        month = "0%s" % month
    date = birthdate[bsep1+1:bsep2]
    if (len(date) < 2):
        date = "0%s" % date
    birthdate = year+"-"+month+"-"+date
    p.birthDate = fhirclient.models.fhirdate.FHIRDate(birthdate)
        
    # build and resolve death date
    tt = "12:00:00" #Setting time to noon
    yyyy = record[deathyi]
    mm = record[deathmi]
    if (len(mm) < 2): 
        mm = "0%s" % mm
    dd = record[deathdi]
    if (len(dd) < 2):
        dd = "0%s" % dd
    deathdate = "%s-%s-%sT%s" % (yyyy,mm,dd,tt)
    deathdate = fhirclient.models.fhirdate.FHIRDate(deathdate)
    
    # Create period
    import fhirclient.models.period as per 
    per = per.Period()
    per.start = deathdate
    
    p.deceasedBoolean = True
    p.deceasedDateTime = deathdate

    return p
