import csv
import json

import pandas as pd
import numpy as np
from functools import reduce


def finder(df, row):
    for col in df:
        df = df.loc[(df[col] == row[col]) | (df[col].isnull() & pd.isnull(row[col]))]
    return df


def read_json2(sjson):

    x = json.loads(sjson)

    rows = []
    for y in x[0]['condition']:
        if y["resourceType"] == "Condition":
            rows.append([
                # f.writerow([
                y["code"]["coding"][0]["code"],
                np.nan,
                np.nan,
                np.nan
            ])

    for y in x[0]['observation']:
        if y["resourceType"] == "Observation":
            rows.append([
                np.nan,
                y["code"]["coding"][0]["code"],
                np.nan,
                np.nan
            ])

    df = pd.DataFrame(rows, columns=['Symptom', 'LabTest', 'LabResult', 'AffectedArea'])
    # print(df)
    return df

def compare_to_guideline(df_guideline, df_patient):
    # Find all the rows that match
    cols = ['Symptom', "LabTest", 'LabResult', 'AffectedArea']
    df_match = pd.merge(df_patient, df_guideline, on=cols, how='inner')

    df_match['match'] = df_match['Symptom'].map(lambda e: str(e)) + '_' + df_match['LabTest'].map(
        lambda e: str(e)) + '_' + df_match['LabResult'].map(lambda e: str(e)) + '_' + df_match[
                            'AffectedArea'].map(lambda e: str(e))

    df_guideline['match'] = df_guideline['Symptom'].map(lambda e: str(e)) + '_' + df_guideline['LabTest'].map(
        lambda e: str(e)) + '_' + df_guideline['LabResult'].map(lambda e: str(e)) + '_' + df_guideline[
                                'AffectedArea'].map(lambda e: str(e))


    # Setting found
    symptom = list(set(df_match['match']))
    df_results = df_guideline.copy()

    # print('symptoms',symptom)
    # print(df_results[df_results['Symptom'].isin(symptom)])
    df_results.ix[df_results['match'].isin(symptom), 'found'] = True
    df_results[~df_results['match'].isin(symptom)] = False

    # Weight based selection
    df_results2 = df_results[df_results['found'] == True]
    maxw = df_results2['Weight'].max()
    # print('found', df_results2, maxw)
    df_filtered = df_results2[df_results2['Weight'] == maxw]

    return df_filtered





class ZikaDetection():
    ""
    ZIKA_PRIOR = 0.2
    def __init__(self):
        ""

    def get_probability(self, df_results):
        ""
        df_results['Likelihood'] = df_results['Likelihood'].map(lambda e: float(e))
        df_results['NegLikelihood'] = df_results['NegLikelihood'].map(lambda e: float(e))

        # print('Results',df_results)
        prior = self.ZIKA_PRIOR
        neg_prior = 1.0 - prior

        pos = []
        neg = []
        for i, row in df_results.iterrows():
            if row['found'] == True:
                pos.append(row['Likelihood'])
                neg.append(row['NegLikelihood'])

            elif row['found'] == False:
                pos.append(1.0 - row['Likelihood'])
                neg.append(1.0 - row['NegLikelihood'])

        if len(pos) != 0:
            likelihood = reduce((lambda x, y: x * y), pos) * prior
            neg_likelihood = reduce((lambda x, y: x * y), neg) * neg_prior
        else:
            likelihood = prior
            neg_likelihood = neg_prior

        normalization = 1.0 / (likelihood + neg_likelihood)

        prob = round(normalization * likelihood, 6)

        return prob


def detect1(json):
    # json=''
    df_data=read_json2(json)
    df_guideline = pd.read_csv('sample_guideline.csv', header='infer', dtype=object)
    df_results = compare_to_guideline(df_guideline, df_data)
    det = ZikaDetection()
    prob = det.get_probability(df_results)

    return prob


def run_main():
    df_guideline = pd.read_csv('sample_guideline.csv', header='infer', dtype=object)

    # Test many symptoms and area
    # df_patient_data = pd.read_csv('sample_symptoms_data.csv', header='infer', dtype=object)

    # Test many symptoms and area
    df_patient_data = pd.read_csv('sample_symptoms2_data.csv', header='infer', dtype=object)

    # Test many symptoms and area
    df_patient_data = pd.read_csv('sample_symptoms3_data.csv', header='infer', dtype=object)

    # Test Labs
    # df_patient_data = pd.read_csv('sample_lab_data.csv', header='infer',dtype=object)

    # Test neg Labs
    # df_patient_data = pd.read_csv('sample_lab2_data.csv', header='infer',dtype=object)

    df_results = compare_to_guideline(df_guideline, df_patient_data)

    # print('New results', df_results)
    det = ZikaDetection()
    prob = det.get_probability(df_results)
    # print('Probability',prob)

    json = """
    [{"observation": [{"resourceType": "Observation", "id": "-3", "status": "final",
                       "code": {"coding": [{"system": "http://loinc.org", "code": "30745-4", "display": "Chest X-ray"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28222950"}, "effectiveDateTime": "2011-04-23T11:00:00-04:00",
                       "valueString": "Hyperinflation, depressed diaphragms"},
                      {"resourceType": "Observation", "id": "-4", "status": "final", "code": {
                          "coding": [{"system": "http://loinc.org", "code": "29757-2", "display": "Colposcopy study"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28222950"}, "effectiveDateTime": "2011-04-23T11:00:00-04:00",
                       "valueString": "Atypical cells with wrinkled nuclei, perinuclear halos"},
                      {"resourceType": "Observation", "id": "4440975", "status": "final", "code": {
                          "coding": [{"system": "http://loinc.org", "code": "9304-7", "display": "Respiration rhythm"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28222950"}, "effectiveDateTime": "2011-04-23T11:00:00-04:00",
                       "valueQuantity": {"value": 32, "unit": "/min", "system": "http://unitsofmeasure.org",
                                         "code": "/min"}},
                      {"resourceType": "Observation", "id": "4440974", "status": "final",
                       "code": {"coding": [{"system": "http://loinc.org", "code": "8867-4", "display": "Heart rate"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28222950"}, "effectiveDateTime": "2011-04-23T11:00:00-04:00",
                       "valueQuantity": {"value": 78, "unit": "/min", "system": "http://unitsofmeasure.org",
                                         "code": "/min"}},
                      {"resourceType": "Observation", "id": "4440973", "status": "final",
                       "code": {"coding": [{"system": "http://loinc.org", "code": "8302-2", "display": "Body height"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28222950"}, "effectiveDateTime": "2011-04-23T11:00:00-04:00",
                       "valueQuantity": {"value": 68, "unit": "in", "system": "http://unitsofmeasure.org", "code": "in"}},
                      {"resourceType": "Observation", "id": "4440979", "status": "final", "code": {"coding": [
                          {"system": "http://loinc.org", "code": "4544-3",
                           "display": "Hematocrit [Volume Fraction] of Blood by Automated count"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28222950"}, "effectiveDateTime": "2011-04-23T11:00:00-04:00",
                       "valueQuantity": {"value": 40, "unit": "%", "system": "http://unitsofmeasure.org", "code": "%"}},
                      {"resourceType": "Observation", "id": "4440978", "status": "final",
                       "code": {"coding": [{"system": "http://loinc.org", "code": "718-7", "display": "Hemoglobin"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28222950"}, "effectiveDateTime": "2011-04-23T11:00:00-04:00",
                       "valueQuantity": {"value": 12.5, "unit": "g/dL", "system": "http://unitsofmeasure.org",
                                         "code": "g/dL"}},
                      {"resourceType": "Observation", "id": "4440977", "status": "final",
                       "code": {"coding": [{"system": "http://loinc.org", "code": "8462-4", "display": "BP diastolic"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28222950"}, "effectiveDateTime": "2011-04-23T11:00:00-04:00",
                       "valueQuantity": {"value": 95, "unit": "mm[Hg]", "system": "http://unitsofmeasure.org",
                                         "code": "mm[Hg]"}},
                      {"resourceType": "Observation", "id": "4440976", "status": "final", "code": {"coding": [
                          {"system": "http://loinc.org", "code": "55284-4",
                           "display": "Blood pressure systolic & diastolic"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28222950"}, "effectiveDateTime": "2011-04-23T11:00:00-04:00",
                       "component": [{"code": {
                           "coding": [{"system": "http://loinc.org", "code": "8480-6", "display": "BP systolic"}]},
                                      "valueQuantity": {"value": 160, "unit": "mm[Hg]",
                                                        "system": "http://unitsofmeasure.org", "code": "mm[Hg]"}}, {
                                         "code": {"coding": [
                                             {"system": "http://loinc.org", "code": "8462-4", "display": "BP diastolic"}]},
                                         "valueQuantity": {"value": 95, "unit": "mm[Hg]",
                                                           "system": "http://unitsofmeasure.org", "code": "mm[Hg]"}}]},
                      {"resourceType": "Observation", "id": "4440983", "status": "final", "code": {
                          "coding": [{"system": "http://loinc.org", "code": "2075-0", "display": "Chloride serum/plasma"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28222950"}, "effectiveDateTime": "2011-04-23T11:00:00-04:00",
                       "valueQuantity": {"value": 98, "unit": "mmol/L", "system": "http://unitsofmeasure.org",
                                         "code": "mmol/L"}},
                      {"resourceType": "Observation", "id": "4440982", "status": "final", "code": {"coding": [
                          {"system": "http://loinc.org", "code": "2823-3", "display": "Potassium serum/plasma"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28222950"}, "effectiveDateTime": "2011-04-23T11:00:00-04:00",
                       "valueQuantity": {"value": 3.4, "unit": "mmol/L", "system": "http://unitsofmeasure.org",
                                         "code": "mmol/L"}},
                      {"resourceType": "Observation", "id": "4440981", "status": "final", "code": {
                          "coding": [{"system": "http://loinc.org", "code": "2951-2", "display": "Sodium serum/plasma"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28222950"}, "effectiveDateTime": "2011-04-23T11:00:00-04:00",
                       "valueQuantity": {"value": 137, "unit": "mmol/L", "system": "http://unitsofmeasure.org",
                                         "code": "mmol/L"}},
                      {"resourceType": "Observation", "id": "4440980", "status": "final", "code": {"coding": [
                          {"system": "http://loinc.org", "code": "789-8",
                           "display": "Erythrocytes [#/volume] in Blood by Automated count"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28222950"}, "effectiveDateTime": "2011-04-23T11:00:00-04:00",
                       "valueQuantity": {"value": 4.2, "unit": "No matching concept", "system": "OMOP generated",
                                         "code": "No matching concept"}},
                      {"resourceType": "Observation", "id": "4440987", "status": "final", "code": {
                          "coding": [{"system": "http://loinc.org", "code": "9304-7", "display": "Respiration rhythm"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28223000"}, "effectiveDateTime": "2011-08-24T13:15:00-04:00",
                       "valueQuantity": {"value": 32, "unit": "/min", "system": "http://unitsofmeasure.org",
                                         "code": "/min"}},
                      {"resourceType": "Observation", "id": "4440986", "status": "final",
                       "code": {"coding": [{"system": "http://loinc.org", "code": "8867-4", "display": "Heart rate"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28223000"}, "effectiveDateTime": "2011-08-24T13:15:00-04:00",
                       "valueQuantity": {"value": 68, "unit": "/min", "system": "http://unitsofmeasure.org",
                                         "code": "/min"}},
                      {"resourceType": "Observation", "id": "4440985", "status": "final", "code": {
                          "coding": [{"system": "http://loinc.org", "code": "3141-9", "display": "Body weight Measured"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28223000"}, "effectiveDateTime": "2011-08-24T13:15:00-04:00",
                       "valueQuantity": {"value": 185, "unit": "[lb_us]", "system": "http://unitsofmeasure.org",
                                         "code": "[lb_us]"}},
                      {"resourceType": "Observation", "id": "4440984", "status": "final", "code": {"coding": [
                          {"system": "http://loinc.org", "code": "17856-6",
                           "display": "Hemoglobin A1c/Hemoglobin.total in Blood by HPLC"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28222950"}, "effectiveDateTime": "2011-04-23T11:00:00-04:00",
                       "valueQuantity": {"value": 9.3, "unit": "%", "system": "http://unitsofmeasure.org", "code": "%"}},
                      {"resourceType": "Observation", "id": "4440991", "status": "final",
                       "code": {"coding": [{"system": "http://loinc.org", "code": "8302-2", "display": "Body height"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28223050"}, "effectiveDateTime": "2012-06-03T09:00:00-04:00",
                       "valueQuantity": {"value": 68, "unit": "in", "system": "http://unitsofmeasure.org", "code": "in"}},
                      {"resourceType": "Observation", "id": "4440990", "status": "final", "code": {"coding": [
                          {"system": "http://loinc.org", "code": "17856-6",
                           "display": "Hemoglobin A1c/Hemoglobin.total in Blood by HPLC"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28223000"}, "effectiveDateTime": "2011-08-24T13:15:00-04:00",
                       "valueQuantity": {"value": 8.9, "unit": "%", "system": "http://unitsofmeasure.org", "code": "%"}},
                      {"resourceType": "Observation", "id": "4440989", "status": "final",
                       "code": {"coding": [{"system": "http://loinc.org", "code": "8462-4", "display": "BP diastolic"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28223000"}, "effectiveDateTime": "2011-08-24T13:15:00-04:00",
                       "valueQuantity": {"value": 96, "unit": "mm[Hg]", "system": "http://unitsofmeasure.org",
                                         "code": "mm[Hg]"}},
                      {"resourceType": "Observation", "id": "4440988", "status": "final", "code": {"coding": [
                          {"system": "http://loinc.org", "code": "55284-4",
                           "display": "Blood pressure systolic & diastolic"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28223000"}, "effectiveDateTime": "2011-08-24T13:15:00-04:00",
                       "component": [{"code": {
                           "coding": [{"system": "http://loinc.org", "code": "8480-6", "display": "BP systolic"}]},
                                      "valueQuantity": {"value": 162, "unit": "mm[Hg]",
                                                        "system": "http://unitsofmeasure.org", "code": "mm[Hg]"}}, {
                                         "code": {"coding": [
                                             {"system": "http://loinc.org", "code": "8462-4", "display": "BP diastolic"}]},
                                         "valueQuantity": {"value": 96, "unit": "mm[Hg]",
                                                           "system": "http://unitsofmeasure.org", "code": "mm[Hg]"}}]},
                      {"resourceType": "Observation", "id": "4440995", "status": "final", "code": {"coding": [
                          {"system": "http://loinc.org", "code": "55284-4",
                           "display": "Blood pressure systolic & diastolic"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28223050"}, "effectiveDateTime": "2012-06-03T09:00:00-04:00",
                       "component": [{"code": {
                           "coding": [{"system": "http://loinc.org", "code": "8480-6", "display": "BP systolic"}]},
                                      "valueQuantity": {"value": 155, "unit": "mm[Hg]",
                                                        "system": "http://unitsofmeasure.org", "code": "mm[Hg]"}}, {
                                         "code": {"coding": [
                                             {"system": "http://loinc.org", "code": "8462-4", "display": "BP diastolic"}]},
                                         "valueQuantity": {"value": 88, "unit": "mm[Hg]",
                                                           "system": "http://unitsofmeasure.org", "code": "mm[Hg]"}}]},
                      {"resourceType": "Observation", "id": "4440994", "status": "final", "code": {
                          "coding": [{"system": "http://loinc.org", "code": "9304-7", "display": "Respiration rhythm"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28223050"}, "effectiveDateTime": "2012-06-03T09:00:00-04:00",
                       "valueQuantity": {"value": 34, "unit": "/min", "system": "http://unitsofmeasure.org",
                                         "code": "/min"}},
                      {"resourceType": "Observation", "id": "4440993", "status": "final",
                       "code": {"coding": [{"system": "http://loinc.org", "code": "8867-4", "display": "Heart rate"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28223050"}, "effectiveDateTime": "2012-06-03T09:00:00-04:00",
                       "valueQuantity": {"value": 76, "unit": "/min", "system": "http://unitsofmeasure.org",
                                         "code": "/min"}},
                      {"resourceType": "Observation", "id": "4440992", "status": "final", "code": {
                          "coding": [{"system": "http://loinc.org", "code": "3141-9", "display": "Body weight Measured"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28223050"}, "effectiveDateTime": "2012-06-03T09:00:00-04:00",
                       "valueQuantity": {"value": 185, "unit": "[lb_us]", "system": "http://unitsofmeasure.org",
                                         "code": "[lb_us]"}},
                      {"resourceType": "Observation", "id": "4440997", "status": "final", "code": {"coding": [
                          {"system": "http://loinc.org", "code": "17856-6",
                           "display": "Hemoglobin A1c/Hemoglobin.total in Blood by HPLC"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28223050"}, "effectiveDateTime": "2012-06-03T09:00:00-04:00",
                       "valueQuantity": {"value": 7.9, "unit": "%", "system": "http://unitsofmeasure.org", "code": "%"}},
                      {"resourceType": "Observation", "id": "4440996", "status": "final",
                       "code": {"coding": [{"system": "http://loinc.org", "code": "8462-4", "display": "BP diastolic"}]},
                       "subject": {"reference": "Patient/523050", "display": "Marla M Dixon"},
                       "encounter": {"reference": "Encounter/28223050"}, "effectiveDateTime": "2012-06-03T09:00:00-04:00",
                       "valueQuantity": {"value": 88, "unit": "mm[Hg]", "system": "http://unitsofmeasure.org",
                                         "code": "mm[Hg]"}}], "condition": [{"resourceType": "Condition", "id": "412510",
                                                                             "patient": {"reference": "Patient/523050",
                                                                                         "display": "Marla M Dixon"},
                                                                             "asserter": {
                                                                                 "reference": "Practitioner/232850",
                                                                                 "display": "Mark L Braunstein, MD"},
                                                                             "code": {"coding": [
                                                                                 {"system": "http://snomed.info/sct",
                                                                                  "code": "266394006",
                                                                                  "display": "Simple chronic bronchitis"}],
                                                                                      "text": "Simple chronic bronchitis, Systematic Nomenclature of Medicine - Clinical Terms (IHTSDO), 266394006"},
                                                                             "category": {"coding": [{
                                                                                                         "system": "http://hl7.org/fhir/condition-category",
                                                                                                         "code": "finding"}]},
                                                                             "verificationStatus": "confirmed",
                                                                             "onsetDateTime": "1999-05-01T00:00:00-04:00"},
                                                                            {"resourceType": "Condition", "id": "412511",
                                                                             "patient": {"reference": "Patient/523050",
                                                                                         "display": "Marla M Dixon"},
                                                                             "asserter": {
                                                                                 "reference": "Practitioner/232850",
                                                                                 "display": "Mark L Braunstein, MD"},
                                                                             "code": {"coding": [
                                                                                 {"system": "http://snomed.info/sct",
                                                                                  "code": "53741008",
                                                                                  "display": "Coronary arteriosclerosis"}],
                                                                                      "text": "Coronary arteriosclerosis, Systematic Nomenclature of Medicine - Clinical Terms (IHTSDO), 53741008"},
                                                                             "category": {"coding": [{
                                                                                                         "system": "http://hl7.org/fhir/condition-category",
                                                                                                         "code": "finding"}]},
                                                                             "verificationStatus": "confirmed",
                                                                             "onsetDateTime": "1999-05-01T00:00:00-04:00"},
                                                                            {"resourceType": "Condition", "id": "412508",
                                                                             "patient": {"reference": "Patient/523050",
                                                                                         "display": "Marla M Dixon"},
                                                                             "asserter": {
                                                                                 "reference": "Practitioner/232850",
                                                                                 "display": "Mark L Braunstein, MD"},
                                                                             "code": {"coding": [
                                                                                 {"system": "http://snomed.info/sct",
                                                                                  "code": "44054006",
                                                                                  "display": "Type 2 diabetes mellitus"}],
                                                                                      "text": "Type 2 diabetes mellitus, Systematic Nomenclature of Medicine - Clinical Terms (IHTSDO), 44054006"},
                                                                             "category": {"coding": [{
                                                                                                         "system": "http://hl7.org/fhir/condition-category",
                                                                                                         "code": "finding"}]},
                                                                             "verificationStatus": "confirmed",
                                                                             "onsetDateTime": "1999-05-01T00:00:00-04:00"},
                                                                            {"resourceType": "Condition", "id": "412509",
                                                                             "patient": {"reference": "Patient/523050",
                                                                                         "display": "Marla M Dixon"},
                                                                             "asserter": {
                                                                                 "reference": "Practitioner/232850",
                                                                                 "display": "Mark L Braunstein, MD"},
                                                                             "code": {"coding": [
                                                                                 {"system": "http://snomed.info/sct",
                                                                                  "code": "59621000",
                                                                                  "display": "Essential hypertension"}],
                                                                                      "text": "Essential hypertension, Systematic Nomenclature of Medicine - Clinical Terms (IHTSDO), 59621000"},
                                                                             "category": {"coding": [{
                                                                                                         "system": "http://hl7.org/fhir/condition-category",
                                                                                                         "code": "finding"}]},
                                                                             "verificationStatus": "confirmed",
                                                                             "onsetDateTime": "1999-05-01T00:00:00-04:00"}]}]
    """

    prob = detect1(json)

    print('Probability', prob)

def df_to_json(df):
    d = [
        dict([
                 (colname, row[i])
                 for i, colname in enumerate(df.columns)
                 ])
        for row in df.values
        ]

    return json.dumps(d)


# if __name__ == '__main__':
#     run_main()


### Web app ###

from flask import Flask, render_template, redirect, url_for, request
from flask import make_response
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

@app.route("/")
def home():
    return "hi"


@app.route('/detect', methods=['POST'])
def detect():
    input_json = request.form['mydata'];

    result = detect1(input_json)
    # resp = make_response('{"response": ' + result + '}')
    # resp.headers['Content-Type'] = "application/json"
    # return resp
    # print()
    return json.dumps({'prob':result})


if __name__ == "__main__":
    app.run(host="192.168.1.17")
