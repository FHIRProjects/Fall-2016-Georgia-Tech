import json
import urllib2

import pytz as pytz

from functools import wraps

#import requests
from django.utils import timezone
from django.utils.decorators import available_attrs
from django.core.urlresolvers import reverse

from django.shortcuts import render,redirect
from django.template import RequestContext

from fhirclient.models import patient, questionnaire, questionnaireresponse, communication, practitioner

from datetime import datetime

import utils


def require_valid_user(view_func, invalid_user_message=None):
    if invalid_user_message is None:
        invalid_user_message = "The user you've selected appears to be invalid. \
         Please return to <a href='/questionnaire/'>the index</a> and select a different user."
    @wraps(view_func, assigned=available_attrs(view_func))
    def _wrapped(request, *args, **kwargs):
        if request.COOKIES.get('userId') is not None:
            try:
                (patientId, serverId) = get_login_info(request)
                smart = utils.getFhirClient(serverId)
                patient.Patient.read(patientId, smart.server)
            except Exception:
                context = { 'error_text' : invalid_user_message }
                return render(request, 'error.html', context)
            return view_func(request, *args, **kwargs)
        else:
            return redirect("questionnaire.views.index")
    return _wrapped

def get_login_info(request):
    patientId = request.COOKIES.get('userId')
    serverId = utils.resolveServerId(patientId,request.COOKIES.get('serverId'))
    return (patientId, serverId)


def index(request):
    context = {'patientGroups': utils.getPatientMap()}
    return render(request, 'index.html', context)

@require_valid_user
def respond(request, form_id, goal = None):
    (patientId, serverId) = get_login_info(request)
    smart = utils.getFhirClient(serverId)

    childRecord = patient.Patient.read(patientId, smart.server)

    try:
        qMap = utils.getQuestionnaireMap()
        form = questionnaire.Questionnaire.read(qMap[serverId][form_id], smart.server)

        jsonResponse = []
        if request.method == 'POST':
            jsonResponse = {"group": {"linkId": "root"}, "resourceType": "QuestionnaireResponse",
                            "questionnaire": {"reference": "Questionnaire/"+form.id}, "status": "completed",
                            "subject": {"reference": "Patient/"+childRecord.id},
                            "author": {"reference": "Patient/"+childRecord.id},
                            "authored": datetime.today().isoformat()}
            if form.group.question is not None:
                jsonResponse["group"]["question"] = []
                for question in form.group.question:
                    type = {"text":"valueString", "boolean":"valueBoolean", "integer":"valueInteger"}[question.type]
                    questionJson = {"linkId": question.linkId, "answer": [{type: request.POST.get(question.linkId, default=0)}]}
                    jsonResponse["group"]["question"].append(questionJson)
            if form.group.group is not None:
                jsonResponse["group"]["group"] = []
                for group in form.group.group:
                    groupJson = {"linkId": group.linkId, "question":[]}
                    for question in group.question:
                        type = {"text":"valueString", "boolean":"valueBoolean", "integer":"valueInteger"}[question.type]
                        questionJson = {"linkId": question.linkId, "answer": [{type: request.POST.get(question.linkId, default=0)}]}
                        groupJson["question"].append(questionJson)
                    jsonResponse["group"]["group"].append(groupJson)

            print json.dumps(jsonResponse)
            smart.server.post_json('QuestionnaireResponse', jsonResponse)
            return redirect("/questionnaire/")

        context = {}
        context['patientName'] = smart.human_name(childRecord.name[0])
        context['questionnaire'] = form.group
        context['form_id'] = form_id
        context['json'] = jsonResponse
        if goal is not None:
            context['goalQuestionnaire'] = goal[0]
            context['goalResponse'] = goal[1]

        #return JsonResponse(jsonResponse)
        return render(request, 'questionnaire.html', context)

    except:
    #except requests.exceptions.HTTPError as error:
        # print(error)
        # print(error.response.text)
        context = {'error_text' : "Couldn't connect to the FHIR server or FHIR server has been reset. \
         Please contact <a href='mailto:asmiley3@gatech.edu'>the team</a> and ask them to investigate."}
        return render(request, 'error.html', context)

@require_valid_user
def respond_hh(request):
    (patientId, serverId) = get_login_info(request)
    smart = utils.getFhirClient(serverId)

    childRecord = patient.Patient.read(patientId, smart.server)
    age = (datetime.now().date() - childRecord.birthDate.date).days / 365.24

    if age < 13:
        return respond(request, utils.CHILD_FORM)
    else:
        return respond(request, utils.TEEN_FORM)

@require_valid_user
def respond_wic(request):
    return respond(request, utils.WIC_FORM)

@require_valid_user
def respond_food(request):
    return respond(request, utils.FOOD_FORM)

@require_valid_user
def respond_status(request):
    (patientId, serverId) = get_login_info(request)
    smart = utils.getFhirClient(serverId)
    qMap = utils.getQuestionnaireMap()
    timezone.activate(pytz.timezone("US/Eastern"))
    (patientId, serverId) = get_login_info(request)
    smart = utils.getFhirClient(serverId)

    try:
        search = questionnaireresponse.QuestionnaireResponse.where(struct={"patient": patientId,
            "questionnaire":qMap[serverId][utils.GOAL_FORM],"_sort:desc": "authored", "_count": "30"})
        responses = search.perform_resources(smart.server)
        if len(responses)>0:
            goal_response = responses[0]
            goal_questionnaire = questionnaire.Questionnaire.read(qMap[serverId][utils.GOAL_FORM], smart.server)
            return respond(request, utils.STATUS_FORM, goal=(goal_questionnaire,goal_response))
        else:
            context = {'error_text' : "No goals have been set yet.  Try again soon!"}
            return render(request, 'error.html', context)
    except Exception:
        context = RequestContext(request)
        context['error_text'] = "There was an error retrieving the text goal information."
        return render(request, 'error.html', context)


@require_valid_user
def messages(request):
    timezone.activate(pytz.timezone("US/Eastern"))
    (patientId, serverId) = get_login_info(request)
    smart = utils.getFhirClient(serverId)
    childRecord = patient.Patient.read(patientId, smart.server)

    search = communication.Communication.where(struct={"recipient": "Patient/"+patientId})
    # This is a hack to work around a bug in the SMART on FHIR server.
    # The bug should go away when they finish their switch to a HAPI FHIR-based implementation.
    if serverId == utils.SMART:
        search = communication.Communication.where(struct={"recipient": patientId})
    messages = search.perform_resources(smart.server)
    if len(messages) > 0:
        context = RequestContext(request)

        def timestamp_key(entry):
            return entry.sent.date
        context['patientName'] = smart.human_name(childRecord.name[0])
        context['messages'] = sorted(messages, key=timestamp_key, reverse=True)
        return render(request, 'messages.html',
                                  context_instance=context)
    else:
        context = {}
        context['patientName'] = smart.human_name(childRecord.name[0])
        context['error_text'] = "No messages yet.  Check back soon!"
        return render(request, 'error.html', context)


@require_valid_user
def recommendations(request):
    timezone.activate(pytz.timezone("US/Eastern"))
    (patientId, serverId) = get_login_info(request)
    smart = utils.getFhirClient(serverId)
    childRecord = patient.Patient.read(patientId, smart.server)
    url = 'https://engine.gradeafhir.pw/api/recs?patient='+ patientId
    print(url)
    req = urllib2.Request(url, headers={'User-Agent' : "Magic Browser"})     
    serialized_data = urllib2.urlopen(req).read()
 
    recs = json.loads(serialized_data)
    recommendations = recs['entry']
    
    
    if len(recommendations) > 0:
        context = {}
            
        context['patientName'] = smart.human_name(childRecord.name[0])
        context['recommendations'] = recommendations
        return render(request, 'recommendations.html', context)
    else:
        context = {}
        context['patientName'] = smart.human_name(childRecord.name[0])
        context['error_text'] = "No Recommendations yet.  Check back soon!"
        return render(request, 'error.html', context)

@require_valid_user
def history(request):
    timezone.activate(pytz.timezone("US/Eastern"))
    (patientId, serverId) = get_login_info(request)
    smart = utils.getFhirClient(serverId)
    childRecord = patient.Patient.read(patientId, smart.server)

    search = questionnaireresponse.QuestionnaireResponse.where(struct={"patient": patientId, "_sort:desc": "authored", "_count": "30"})
    responses = search.perform_resources(smart.server)
    if len(responses) > 0:
        response_groups = {}
        for response in responses:
            ref = response.questionnaire.reference.split('/')[1]
            if ref in response_groups:
                response_groups[ref]['responses'].append(response)
            else:
                response_groups[ref] = {'questionnaire':utils.resolveFhirReference(response.questionnaire, server=smart.server), 'responses':[response]}

        context = {}
        context['pastResponses'] = response_groups
        context['patientName'] = smart.human_name(childRecord.name[0])
        return render(request, 'history.html', context)
    else:
        context = {}
        context['patientName'] = smart.human_name(childRecord.name[0])
        context['error_text'] = "You don't have any history yet. \
         <a href='"+reverse("questionnaire:respond_hh")+"'>Fill out the questionnaire</a> to get started!"
        return render(request, 'error.html', context)


@require_valid_user
def details(request, responseid):
    timezone.activate(pytz.timezone("US/Eastern"))
    (patientId, serverId) = get_login_info(request)
    smart = utils.getFhirClient(serverId)
    childRecord = patient.Patient.read(patientId, smart.server)

    try:
        response = questionnaireresponse.QuestionnaireResponse.read(responseid, smart.server)
        questionnaire = utils.resolveFhirReference(response.questionnaire, server=smart.server)
        context = {}
        context['response'] = response
        context['questionnaire'] = questionnaire
        context['patientName'] = smart.human_name(childRecord.name[0])
        return render(request, 'response-details.html', context)
    except Exception:
        context = RequestContext(request)
        context['error_text'] = "The response you've selected appears to be invalid. \
         Please return to <a href='/questionnaire/history'>the index</a> and select another."
        return render(request, 'error.html', context)


def about(request):
    return render(request, 'about.html')
