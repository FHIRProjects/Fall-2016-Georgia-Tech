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
def createPractitioner(dohmpifield, record):
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
    import fhirclient.models.practitioner as p
    
    # set practitioner id as provider licence numberS
    p = p.Practitioner({'id': record[provi]})
    
    # resolve name
    import fhirclient.models.humanname as hn
    name = hn.HumanName()
    name.given = [record[fnamei]]
    name.family = [record[lnamei]]
    p.name = name
    
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
    birthdate = birthdate[-4:]+"-"+birthdate[0:bsep1]+"-"+birthdate[bsep1+1:bsep2]
    p.birthDate = fhirclient.models.fhirdate.FHIRDate(birthdate)
        
    # build and resolve death date
    tz = "00:00:00-0600" # Utah is in the Mountain Time Zone, setting time as midnight.
    yyyy = record[deathyi]
    mm = record[deathmi]
    if (len(mm) < 2): 
        mm = "0%s" % mm
    dd = record[deathdi]
    if (len(dd) < 2):
        dd = "0%s" % dd
    deathdate = "%s-%s-%sT%s" % (yyyy,mm,dd,tz)
    deathdate = fhirclient.models.fhirdate.FHIRDate(deathdate)
    
    # Create period
    import fhirclient.models.period as per 
    per = per.Period()
    per.start = deathdate
    
    import fhirclient.models.fhirreference as ref
    
    location = ref.FHIRReference()
    location.reference = 'http://fhir.ehoth.net/deceased'
    location.display = 'deceased'
    
    # Create role
    import fhirclient.models.practitioner as r
    role = r.PractitionerPractitionerRole()
    role.healthcareservice = 'deceased'
    role.location = [location]
    role.period = per

    role.managingOrganization = location

    #print role.as_json()
    
    p.practitionerRole = [role]

    # return practitioner.
    return p