# -*- coding: utf-8 -*-
"""
Created on Fri Dec 02 01:08:43 2016

@author: Kirk
"""
sample = \
{
  "resourceType": "Questionnaire",
  "id": "3141",
  "text": {
    "status": "generated",
    "div": "<div>\n      \n      \n      <pre>\n            Comorbidity? YES\n              Cardial Comorbidity? YES\n                Angina? YES\n                MI? NO\n              Vascular Comorbidity?\n                (no answers)\n              ...\n            Histopathology\n              Abdominal\n                pT category: 1a\n              ...\n          </pre>\n    \n    \n    </div>"
  },
  "contained": [
    {
      "resourceType": "ValueSet",
      "id": "yesno",
      "name": "Example YesNo",
      "_name": {
        "fhir_comments": [
          "    In practice you could (should?) use boolean for this sort of circumstance.  However, the reality is\n        that many systems treat booleans as coded data    "
        ]
      },
      "status": "active",
      "description": "Captures simple yes-no",
      "codeSystem": {
        "system": "http://example.org/system/code/yesno",
        "caseSensitive": True,
        "concept": [
          {
            "code": "1",
            "display": "Yes"
          },
          {
            "code": "2",
            "display": "No"
          }
        ]
      }
    }
  ],
  "status": "draft",
  "date": "2012-01",
  "subjectType": [
    "Patient"
  ],
  "group": {
    "linkId": "1",
    "title": "Cancer Quality Forum Questionnaire 2012",
    "required": True,
    "group": [
      {
        "linkId": "1.1",
        "_linkId": {
          "fhir_comments": [
            "    COMORBIDITY    ",
            "    First main section of the form, questions about comorbidity    "
          ]
        },
        "concept": [
          {
            "system": "http://example.org/system/code/sections",
            "code": "COMORBIDITY"
          }
        ],
        "question": [
          {
            "fhir_comments": [
              "    section contains one question: whether there is comorbidity    "
            ],
            "linkId": "1.1.1",
            "concept": [
              {
                "system": "http://example.org/system/code/questions",
                "code": "COMORB"
              }
            ],
            "type": "choice",
            "options": {
              "reference": "#yesno"
            },
            "group": [
              {
                "linkId": "1.1.1.1",
                "_linkId": {
                  "fhir_comments": [
                    "    COMORBIDITY/CARDIAL    ",
                    "    Subsection about specific comorbidity: cardial    "
                  ]
                },
                "concept": [
                  {
                    "system": "http://example.org/system/code/sections",
                    "code": "CARDIAL"
                  }
                ],
                "question": [
                  {
                    "linkId": "1.1.1.1.1",
                    "concept": [
                      {
                        "system": "http://example.org/system/code/questions",
                        "code": "COMORBCAR"
                      }
                    ],
                    "type": "choice",
                    "options": {
                      "reference": "#yesno"
                    }
                  },
                  {
                    "linkId": "1.1.1.1.2",
                    "_linkId": {
                      "fhir_comments": [
                        "    This answer carries both the questionnaire-specific name and an equivalent SNOMED CT code    "
                      ]
                    },
                    "concept": [
                      {
                        "system": "http://example.org/system/code/questions",
                        "code": "COMCAR00",
                        "display": "Angina Pectoris"
                      },
                      {
                        "system": "http://snomed.info/sct",
                        "code": "194828000",
                        "display": "Angina (disorder)"
                      }
                    ],
                    "type": "choice",
                    "options": {
                      "reference": "#yesno"
                    }
                  },
                  {
                    "linkId": "1.1.1.1.3",
                    "concept": [
                      {
                        "system": "http://snomed.info/sct",
                        "code": "22298006",
                        "display": "Myocardial infarction (disorder)"
                      }
                    ],
                    "type": "choice",
                    "options": {
                      "reference": "#yesno"
                    }
                  }
                ]
              },
              {
                "linkId": "1.1.1.2",
                "_linkId": {
                  "fhir_comments": [
                    "    COMORBIDITY/VASCULAR    "
                  ]
                },
                "concept": [
                  {
                    "system": "http://example.org/system/code/sections",
                    "code": "VASCULAR"
                  }
                ]
              }
            ]
          }
        ]
      },
      {
        "fhir_comments": [
          "    Todo: What's supposed to go here?    "
        ],
        "linkId": "1.2",
        "_linkId": {
          "fhir_comments": [
            "    HISTOPATHOLOGY    "
          ]
        },
        "concept": [
          {
            "system": "http://example.org/system/code/sections",
            "code": "HISTOPATHOLOGY"
          }
        ],
        "group": [
          {
            "linkId": "1.2.1",
            "concept": [
              {
                "system": "http://example.org/system/code/sections",
                "code": "ABDOMINAL"
              }
            ],
            "question": [
              {
                "linkId": "1.2.1.2",
                "concept": [
                  {
                    "system": "http://example.org/system/code/questions",
                    "code": "STADPT",
                    "display": "pT category"
                  }
                ]
              }
            ]
          }
        ]
      }
    ]
  }
}