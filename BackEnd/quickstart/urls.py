"""quickstart URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/4.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from app.views import employeeDetailView, sign_up, verify , login_api, question_api, score_pretest, score_posttest, session_create_api, session_get_api,session_update_api,flag_receive,status_receive,session_delete,session_trainers,final_evaluation, addUser, generate_certificate_api,feedback_answers,feedback_get_info

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/login/', login_api), # new
    path('api/employee/<int:pk>/', employeeDetailView), # new
    path('api/sign_up/',sign_up),
    path('api/verify/',verify),
    path('api/questions/',question_api),
    path('api/score_pretest/',score_pretest),
    path('api/score_posttest/',score_posttest),
    path('api/session_create/',session_create_api),
    path('api/session_trainers/',session_trainers),
    path('api/session_get/',session_get_api),
    path('api/session_update/',session_update_api),
    path('api/session_delete_api/',session_delete),
    path('api/flag_receive/',flag_receive),
    path('api/status_receive/',status_receive),
    path('api/final_evaluation/',final_evaluation),
    path('api/addUser/',addUser),
    path('api/generate_certificate/',generate_certificate_api),
    path('api/feedback_answer/',feedback_answers),
    path('api/feedback_get_info/',feedback_get_info)
]