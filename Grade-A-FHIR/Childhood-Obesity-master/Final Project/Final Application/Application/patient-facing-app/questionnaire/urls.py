from django.conf.urls import url
from . import views

app_name = "questionnaire"

urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^respond/(?P<form_id>[-\w]+)/$', views.respond, name='respond'),
    url(r'^respond_hh/', views.respond_hh, name='respond_hh'),
    url(r'^respond_wic/', views.respond_wic, name='respond_wic'),
    url(r'^respond_food/', views.respond_food, name='respond_food'),
    url(r'^respond_status/', views.respond_status, name='respond_status'),
    url(r'^about/', views.about, name='about'),
    url(r'^messages/', views.messages, name='messages'),
    url(r'^recommendations/', views.recommendations, name='recommendations'),
    url(r'^history/', views.history, name='history'),
    url(r'^response-details/(?P<responseid>[0-9a-f]*)/$', views.details, name='details')
]
