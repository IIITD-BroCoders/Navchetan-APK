from django.shortcuts import render
from .serializers import UserSerializer,QuestionHelpSerializer,TrainerSerializer, SessionSerializer, QuestionNewSerializer
from .models import  User, QuestionsHelp , Trainer, Session, QuestionNew
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from django.contrib.sessions.backends.db import SessionStore
import random
from django.db.models import Q
import os
import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from email.mime.image import MIMEImage
from PIL import Image, ImageDraw, ImageFont
# from sheet_marker import *
from googleapiclient.discovery import build
from googleapiclient.errors import HttpError
from google.oauth2 import service_account
from googleapiclient import discovery
from typing import List

#JUST : Update this to the place where key is placed in yor system

# SERVICE_ACCOUNT_FILE =
SERVICE_ACCOUNT_FILE = r"/home/testing/Navchetna/app/navchetna_combined-portf-1cbce670537a.json"





SCOPES = ['https://www.googleapis.com/auth/spreadsheets']
creds = None
creds = service_account.Credentials.from_service_account_file(SERVICE_ACCOUNT_FILE, scopes=SCOPES)
service = build('sheets', 'v4', credentials=creds)
sheet = service.spreadsheets()

PRETEST_SHEET_NAME = "PreTest"  #sheet name in google sheets
POSTTEST_SHEET_NAME = "PostTest"
FEVAL_SHEET_NAME = "FinalEval"
Feedback_SHEET_NAME = "Feedback"

# Sheet URL  = https://docs.google.com/spreadsheets/d/17P5aM9TNtWEku-iwM9B7S4pxcGEmPrIdaNVGq911Vms/edit#gid=0
SAMPLE_SPREADSHEET_ID = '17P5aM9TNtWEku-iwM9B7S4pxcGEmPrIdaNVGq911Vms'

### metafunctions
def read_sheets(SHEET_NAME:str,start_col = "A",start_row = "1",end_col = "A",end_row = "1",SPREADSHEET_ID = SAMPLE_SPREADSHEET_ID):
    read_at = SHEET_NAME+"!"+start_col+start_row+":"+end_col+end_row
    try: 
        result = sheet.values().get(spreadsheetId=SPREADSHEET_ID,range=read_at).execute()
        values = result.get('values', [])
    except HttpError as err:
        print(err)

    return values
def write_sheet(SHEET_NAME:str,values : list[list], start_col = "A",start_row = "1",end_col = "A",end_row = "1",SPREADSHEET_ID = SAMPLE_SPREADSHEET_ID):
    current_range= SHEET_NAME+"!"+start_col+start_row+":"+end_col+end_row
    ValueInputOption = "USER_ENTERED"
    value_current_rangebody = {"values" : values}

    request = service.spreadsheets().values().update(spreadsheetId=SPREADSHEET_ID, 
                                                    range=current_range, 
                                                    valueInputOption=ValueInputOption, 
                                                    body=value_current_rangebody)
    response = request.execute()
#converts base 10 number to base 26 (aphlabet)  
def base26(num):
    num = int(num)
    if num == 0:
        return 'A'
    string = ""
    while num > 0:
        rem = num%26
        if rem == 0:
            string += 'Z'
            num = (num//26) - 1
        else:
            string += chr((rem-1) + ord('A'))
            num = num//26
    return string[::-1]

def feedback_write(details : list):
    number_of_records_feedback_written = int(read_sheets(Feedback_SHEET_NAME)[0][0])
    # print(number_of_records_feedback_written)
    a = len(details)
    details = [details]

    write_sheet(Feedback_SHEET_NAME,details,"A",str(number_of_records_feedback_written+3),base26(a),str(number_of_records_feedback_written+3))
    write_sheet(Feedback_SHEET_NAME,[[number_of_records_feedback_written+1]]) 

def pretest_write(details : list):
    number_of_records_preTest_written = int(read_sheets(PRETEST_SHEET_NAME)[0][0])
    # print(number_of_records_preTest_written)
    a = len(details)
    details = [details]

    write_sheet(PRETEST_SHEET_NAME,details,"A",str(number_of_records_preTest_written+3),base26(a),str(number_of_records_preTest_written+3))
    write_sheet(PRETEST_SHEET_NAME,[[number_of_records_preTest_written+1]])
def posttest_write(details : list):
    number_of_records_postTest_written = int(read_sheets(POSTTEST_SHEET_NAME)[0][0])
    # print(number_of_records_postTest_written)
    a = len(details)
    details = [details]

    write_sheet(POSTTEST_SHEET_NAME,details,"A",str(number_of_records_postTest_written+3),base26(a),str(number_of_records_postTest_written+3))
    write_sheet(POSTTEST_SHEET_NAME,[[number_of_records_postTest_written+1]]) 
def feval_write(details : list):
    number_of_records_feval_written = int(read_sheets(FEVAL_SHEET_NAME)[0][0])
    # print(number_of_records_feval_written)
    a = len(details)
    details = [details]

    write_sheet(FEVAL_SHEET_NAME,details,"A",str(number_of_records_feval_written+3),base26(a),str(number_of_records_feval_written+3))
    write_sheet(FEVAL_SHEET_NAME,[[number_of_records_feval_written+1]])

def send_email(sender,recipients,subject,message,smtp_port,username,password):
    msg = MIMEText(message)
    msg['From'] = sender
    msg['To'] = ', '.join(recipients)
    msg['Subject'] = subject
    smtp = smtplib.SMTP('smtp.gmail.com', smtp_port)
    smtp.starttls()  
    smtp.login(username, password)
    smtp.sendmail(sender, recipients, msg.as_string())
    smtp.quit()

@api_view(['GET','POST'])
def login_api(request):
    if request.method == 'GET':
        user = User.objects.all()
        serializer = UserSerializer(user, many=True)
        return Response(serializer.data)
    elif request.method == 'POST':
        email = request.data.get('email')
        password = request.data.get('password')
        # serializer = UserSerializer(data=request.data)
        try:
            user =User.objects.get(emailid=email,password=password)
            session = SessionStore()
            session['email']=email
            session.create()
            session_id = "-1"
            if user.session_attended!=None:
                session_id = user.session_attended.session_id
            return Response({'message':'credentials already exists','session_token': session.session_key,'flag':'user','status':user.status,"pretest_score":user.pretest,"posttest_score":user.posttest,"session_id":session_id},status=status.HTTP_200_OK)
        except User.DoesNotExist:
            try:
                trainer = Trainer.objects.get(emailid=email,password=password)
                session = SessionStore()
                session['email']=email
                session.create()
                session_id = "-1"
                if trainer.session_attended!=None:
                    session_id = trainer.session_attended.session_id
                return Response({'message':'credentials already exists','session_token': session.session_key,'flag':'trainer','status':trainer.status,"pretest_score":trainer.pretest,"posttest_score":trainer.posttest,"session_id":session_id},status=status.HTTP_200_OK)
            except Trainer.DoesNotExist:
                return Response({'message': 'user does not exist'},status = status.HTTP_201_CREATED)
            

@api_view(['GET','POST'])
def sign_up(request):
    if request.method=='POST':
        emailid = request.data.get('emailid')
        password =request.data.get('password')
        serializer = UserSerializer(data =request.data)
        try:
            user = User.objects.get(emailid=emailid)
            return Response({'message':'Already exists'},status=status.HTTP_201_CREATED)
        except:
            # try:
            if serializer.is_valid():
                otp = random.randint(1000,9999)
                sender = 'madhur21063@iiitd.c.in'
                recipients = emailid
                subject = 'SPYM verification'
                message = f'Your verification otp is {otp}.'
                smtp_port = 587
                username = 'madhur21063@iiitd.ac.in'
                password = 'zmqgnpfmfdenqrhf'
                send_email(sender,recipients,subject,message,smtp_port,username,password)
                return Response({'otp':otp},status=status.HTTP_200_OK)
            else:
                return Response(serializer.errors,status=status.HTTP_400_BAD_REQUEST)
            # except:
                # return Response({'message':'error'},status=status.HTTP_404_NOT_FOUND)
        #         # respone = getotp(request)
        #         serializer.save()
        #         return Response(serializer.data,status = status.HTTP_200_OK)
        #     else:
        #         return Response(serializer.errors,status=status.HTTP_400_BAD_REQUEST)
        # emailid = request.data.get('emailid')
        # try:

@api_view(['POST'])
def verify(request):
    if(request.method =='POST'):
        emailid = request.data.get('emailid')
        password =request.data.get('password')
        serializer = UserSerializer(data =request.data)
        try:
            if serializer.is_valid():
                serializer.save()
                return Response(serializer.data,status = status.HTTP_200_OK)
            else:
                return Response(serializer.errors,status=status.HTTP_400_BAD_REQUEST)
        except:
            return Response({'message':'error'},status=status.HTTP_404_NOT_FOUND)

@api_view(['GET', 'PUT', 'DELETE'])
def employeeDetailView(request, pk):
    try:
        employee = Employee.objects.get(pk=pk)
        print(employee)
        # return JsonResponse({'message': 'Employee was retrieved successfully!'})
    except Employee.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    
    if request.method == 'GET':
        serializer = EmployeeSerializer(employee)
        return Response(serializer.data)
        # pass
    
    elif request.method == 'PUT':
        # jsondata = JSONParser().parse(request)
        serializer = EmployeeSerializer(employee, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        else:
            return Response(serializer.errors)
        
    elif request.method == 'DELETE':
        employee.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

@api_view(['GET', 'POST'])
def question_api(request):
    if request.method == 'POST':
        language = request.data.get('language')
        question = QuestionsHelp.objects.filter(language=language)
        serializer = QuestionHelpSerializer(question, many=True)
        questions = ""
        option1 = ""
        option2 = ""
        option3 = ""
        option4 = ""
        option5 = ""
        option6 = ""
        correct = ""
        for q in question:
            questions += q.question + ":::("
            option1 += q.option1 + ":::("
            option2 += q.option2 + ":::("
            option3 += q.option3 + ":::("
            option4 += q.option4 + ":::("
            option5 += q.option5 + ":::("
            option6 += q.option6 + ":::("
            correct += q.correct + ":::("
        try:
            questions = questions[:-4]
            option1 = option1[:-4]
            option2 = option2[:-4]
            option3 = option3[:-4]
            option4 = option4[:-4]
            option5 = option5[:-4]
            option6 = option6[:-4]
            correct = correct[:-4]
            return Response({'questions':questions,'option1':option1,'option2':option2,'option3':option3,'option4':option4,'option5':option5,'option6':option6,'correct':correct},status = status.HTTP_200_OK)
        except:
            return Response(serializer.errors,status = status.HTTP_404_NOT_FOUND)

@api_view(['POST'])
def score_pretest(request):
    if request.method == 'POST':
        email = request.data.get('email')
        score = request.data.get('score')
        answer = request.data.get('answer')
        try:
            user = User.objects.get(emailid=email)
            user.pretest = score
            user.save()
            answer = answer.split(":::(")[1:]
            pretest_write(["NA",user.name,user.district,user.state]+answer)
            return Response({'message':'score updated',"answer":answer},status=status.HTTP_200_OK)
        except User.DoesNotExist:
            return Response({'message':'User not found'},status=status.HTTP_404_NOT_FOUND)
        except Exception as e:
            return Response({'message':'Error: {}'.format(str(e))},status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    
            

@api_view(['POST'])
def score_posttest(request):
    if request.method == 'POST':
        email = request.data.get('email')
        score = request.data.get('score')
        answer = request.data.get('answer')
        # serializer = TrainerSerializer(data=request.data)
        try:
            user = User.objects.get(emailid=email)
            if(user.session_attended!=None):

                user.posttest = score
                user.status ="0"
                user.save()
                # trainer = Trainer()
                # trainer.name = user.name
                # trainer.age = user.age
                # trainer.phonenumber = user.phonenumber
                # trainer.gender = user.gender
                # trainer.schoolphone = user.schoolphone
                # trainer.schoolemail = user.schoolemail
                # trainer.schooladdress = user.schooladdress
                # trainer.emailid = user.emailid
                # trainer.state = user.state
                # trainer.district = user.district
                # trainer.password = user.password
                # trainer.status = user.status
                # trainer.pretest = user.pretest
                # trainer.posttest = user.posttest
                # user.delete()
                # trainer.save()
                # if user.session_attended!=None:
                    # session_id = user.session_attended.session_id
                answer = answer.split(":::(")[1:]
                posttest_write([user.session_attended.session_id,user.name,user.district,user.state]+answer)
                return Response({'message': 'Score updated'}, status=status.HTTP_200_OK)
            else:
                return Response({'message':'session not attended'},status=status.HTTP_400_BAD_REQUEST)
        except User.DoesNotExist:
            return Response({'message': 'User not found'}, status=status.HTTP_404_NOT_FOUND)
        except Exception as e:
            return Response({'message': 'Error: {}'.format(str(e))}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

@api_view(['POST'])
def session_create_api(request):
    if request.method == 'POST':
        # emailid_T1 = request.data.get('firstemail')
        # emailid_T2 = request.data.get('secondemail')
        # state = request.data.get('state')
        # district = request.data.get('district')
        # address = request.data.get('address')
        # capacity = request.data.get('batch')
        # hour = request.data.get('hour')
        # date = request.data.get('date')
        serializer = SessionSerializer(data=request.data)
        try:
            if serializer.is_valid():
                serializer.save()
                return Response({'message':'successful'},status=status.HTTP_200_OK)
            else:
                return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        except:
            return Response({'message': 'error'}, status=status.HTTP_404_NOT_FOUND)


@api_view(['POST'])
def session_get_api(request):
    if request.method == 'POST':
        try:
            email = request.data.get('email')
            state = User.objects.get(emailid=email).state
            sessions = Session.objects.filter(state=state, status="0", capacity__gt=-1)
            serializer = SessionSerializer(sessions, many=True)

            emailid_T1 = ""
            emailid_T2 = ""
            state = ""
            district = ""
            address = ""
            capacity = ""
            datetime = ""
            hour = ""
            date = ""
            session_id = ""
            for s in sessions:
                emailid_T1 += s.emailid_T1 + ":::("
                emailid_T2 += s.emailid_T2 + ":::("
                state += s.state + ":::("
                district += s.district + ":::("
                address += s.address + ":::("
                capacity += str(s.capacity) + ":::("
                datetime += str(s.datetime)[:-6] + ":::("
                date += str(s.datetime)[:10] + ":::("
                hour += str(s.datetime)[11:16] + ":::("
                session_id += str(s.session_id) + ":::("
            try:
                emailid_T1 = emailid_T1[:-4]
                emailid_T2 = emailid_T2[:-4]
                state = state[:-4]
                district = district[:-4]
                address = address[:-4]
                capacity = capacity[:-4]
                datetime = datetime[:-4]
                date = date[:-4]
                hour = hour[:-4]
                session_id = session_id[:-4]
                return Response({'emailid_T1':emailid_T1,'emailid_T2':emailid_T2,'state':state,'district':district,'address':address,'capacity':capacity,'datetime':datetime,'time':hour,'date':date,'session_id':session_id},status = status.HTTP_200_OK)
            # return Response(serializer.data, status=status.HTTP_200_OK)
            except:
                return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        except Session.DoesNotExist:
            return Response({'message': 'Sessions not found'}, status=status.HTTP_404_NOT_FOUND)


@api_view(['POST'])
def session_update_api(request):
    if request.method == 'POST':
        try:
            session_id = request.data.get('session_id')
            emailid = request.data.get('emailid')
            session = Session.objects.get(session_id=session_id)
            user = User.objects.get(emailid=emailid)
            user.session_attended = session
            session.capacity = session.capacity - 1
            session.save()
            user.save()
            return Response({'message': 'Session and user updated'}, status=status.HTTP_200_OK)
        except Session.DoesNotExist:
            return Response({'message': 'Session not found'}, status=status.HTTP_404_NOT_FOUND)

@api_view(['POST'])
def flag_receive(request):
    if request.method =='POST':
        email = request.data.get('email')
        try:
            user =User.objects.get(emailid=email)
            # session = SessionStore()
            # session['email']=email
            # session.create()
            sessionid = -1
            if user.session_attended!=None:
                sessionid = user.session_attended.session_id
            return Response({'message':'credentials already exists','flag':'user',"posttest_score":user.posttest,"pretest_score":user.pretest,"status":user.status,"name":user.name,"age":user.age,"phone":user.phonenumber,"email":user.emailid,"session_id":sessionid},status=status.HTTP_200_OK)
        except User.DoesNotExist:
            try:
                trainer = Trainer.objects.get(emailid=email)
                # session = SessionStore()
                # session['email']=email
                # session.create()
                sessionid = -1
                if trainer.session_attended!=None:
                    sessionid = trainer.session_attended.session_id
                return Response({'message':'credentials already exists','flag':'trainer',"posttest_score":trainer.posttest,"pretest_score":trainer.pretest,"status":trainer.status,"name":trainer.name,"age":trainer.age,"phone":trainer.phonenumber,"email":trainer.emailid,"session_id":sessionid},status=status.HTTP_200_OK)
            except Trainer.DoesNotExist:
                return Response({'message': 'credentials does not exist'},status = status.HTTP_201_CREATED)
@api_view(['POST'])
def session_delete(request):
    if request.method == 'POST':
        try:
            id = request.data.get('id')
            session = Session.objects.get(session_id=id)
            session.delete()
            return Response({'message': 'Session deleted'}, status=status.HTTP_200_OK)
        except Session.DoesNotExist:
            return Response({'message': 'Session not found'}, status=status.HTTP_404_NOT_FOUND)

@api_view(['POST'])
def status_receive(request):
    if request.method == 'POST':
        email = request.data.get('email')
        status1 = request.data.get('status')
        try:
            user = User.objects.get(emailid=email)
            user.status = status1
            user.save()
            return Response({'message':'status updated','status':user.status},status=status.HTTP_200_OK)
        except User.DoesNotExist:
            try:
                trainer = Trainer.objects.get(emailid=email)
                trainer.status = status1
                trainer.save()
                return Response({'message':'status updated','status':trainer.status},status=status.HTTP_200_OK)
            except Trainer.DoesNotExist:
                return Response({'message':'credentials does not exist'},status=status.HTTP_404_NOT_FOUND)
@api_view(['POST'])
def session_trainers(request):
    if request.method == 'POST':
        try:
            email = request.data.get('email')
            sessions = Session.objects.filter(Q(emailid_T1 = email)|Q(emailid_T2 = email), status="0", capacity__gt=-1)
            serializer = SessionSerializer(sessions, many=True)
            emailid_T1 = ""
            emailid_T2 = ""
            state = ""
            district = ""
            address = ""
            capacity = ""
            datetime = ""
            hour = ""
            date = ""
            session_id = ""
            for s in sessions:
                emailid_T1 += s.emailid_T1 + ":::("
                emailid_T2 += s.emailid_T2 + ":::("
                state += s.state + ":::("
                district += s.district + ":::("
                address += s.address + ":::("
                capacity += str(s.capacity) + ":::("
                datetime += str(s.datetime)[:-6] + ":::("
                date += str(s.datetime)[:10] + ":::("
                hour += str(s.datetime)[11:16] + ":::("
                session_id += str(s.session_id) + ":::("
            try:
                emailid_T1 = emailid_T1[:-4]
                emailid_T2 = emailid_T2[:-4]
                state = state[:-4]
                district = district[:-4]
                address = address[:-4]
                capacity = capacity[:-4]
                datetime = datetime[:-4]
                date = date[:-4]
                hour = hour[:-4]
                session_id = session_id[:-4]
                return Response({'emailid_T1':emailid_T1,'emailid_T2':emailid_T2,'state':state,'district':district,'address':address,'capacity':capacity,'datetime':datetime,'time':hour,'date':date,'session_id':session_id},status = status.HTTP_200_OK)
            # return Response(serializer.data, status=status.HTTP_200_OK)
            except:
                return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        except Session.DoesNotExist:
            return Response({'message': 'Sessions not found'}, status=status.HTTP_404_NOT_FOUND)

@api_view(['GET','POST'])
def final_evaluation(request):
    if request.method=='POST':
        language = request.data.get('language')
        try:
            form_questions = QuestionNew.objects.filter(language=language)
            serializer = QuestionNewSerializer(form_questions, many=True)
            questions = ""
            mode = ""
            option1 = ""
            option2 = ""
            option3 = ""
            option4 = ""
            long_answer = ""
            for q in form_questions:
                questions += q.question + ":::("
                mode += q.mode + ":::("
                if q.option1 == None:
                    q.option1 = "lol"
                if q.option2 == None:
                    q.option2 = "lol"
                if q.option3 == None:
                    q.option3 = "lol"
                if q.option4 == None:
                    q.option4 = "lol"
                if q.long_answer == None or q.long_answer == "":
                    q.long_answer = "lol"
                option1 += q.option1 + ":::("
                option2 += q.option2 + ":::("
                option3 += q.option3 + ":::("
                option4 += q.option4 + ":::("
                long_answer += q.long_answer + ":::("
            try:
                questions = questions[:-4]
                mode = mode[:-4]
                option1 = option1[:-4]
                option2 = option2[:-4]
                option3 = option3[:-4]
                option4 = option4[:-4]
                long_answer = long_answer[:-4]
                return Response({'questions':questions,'mode':mode,'option1':option1,'option2':option2,'option3':option3,'option4':option4,'long_answer':long_answer},status = status.HTTP_200_OK)
            # return Response(serializer.data, status=status.HTTP_200_OK)
            except:
                return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

            # return Response(serializer.data, status=status.HTTP_200_OK)
        except QuestionNew.DoesNotExist:
            return Response({'message': 'Questions not found'}, status=status.HTTP_404_NOT_FOUND)

@api_view(['POST'])
def addUser(request):
    if request.method == 'POST':
        email = request.data.get('email')
        password = request.data.get('password')
        serializer = SessionSerializer(data=request.data)
        try:
            if serializer.is_valid():
                serializer.save()
                return Response(serializer.data,status = status.HTTP_200_OK)
            else:
                return Response(serializer.errors,status = status.HTTP_400_BAD_REQUEST)
        except:
            return Response(serializer.errors,status = status.HTTP_400_BAD_REQUEST)

@api_view(['POST'])
def generate_certificate_api(request):
    if request.method == 'POST':
        try:
            receiver_email = request.data.get('email')
            user = User.objects.get(emailid=receiver_email)
            # user = request.data.get('name')
            # receiver_email = "lakshya21062@iiitd.ac.in"
            # user = "bukuku"
            name = user.name
            if receiver_email and name and generate_certificate(receiver_email, name):
                trainer = Trainer()
                trainer.name = user.name
                trainer.age = user.age
                trainer.phonenumber = user.phonenumber
                trainer.gender = user.gender
                trainer.schoolphone = user.schoolphone
                trainer.schoolemail = user.schoolemail
                trainer.schooladdress = user.schooladdress
                trainer.emailid = user.emailid
                trainer.state = user.state
                trainer.district = user.district
                trainer.password = user.password
                trainer.status = "5"
                trainer.pretest = user.pretest
                trainer.posttest = user.posttest
                trainer.session_attended = user.session_attended
                trainer.save()
                user.delete()
                # send_certificate_email(receiver_email, user)
                return Response({'message': 'Certificate sent successfully'}, status=200)
            else:
                return Response({'error': 'Invalid request data'}, status=400)
        except User.DoesNotExist:
            try:
                trainer = Trainer.objects.get(emailid=receiver_email)
                return Response({'message': 'Certificate already sent'}, status=200)
            except Trainer.DoesNotExist:
                return Response({'message': 'User not found'}, status=404)
        except Exception as e:
            return Response({'error': str(e)}, status=500)


def generate_certificate(emailid, name):
    base_path = os.path.dirname(os.path.abspath(__file__))  # Get the base directory of your Django app

    template_path = "/home/testing/Navchetna/app/SPYM.jpg"
    output_path = "/home/testing/Navchetna/app/SPYM_1.jpg"
    font_path = "/home/testing/Navchetna/app/arial.ttf"

    img = Image.open(template_path)
    fnt = ImageFont.truetype(font_path, 50)

    d1 = ImageDraw.Draw(img)
    d1.text((600,420), name, font=fnt, fill=(0, 0, 0))
    img.save(output_path)
    # img.show()

    return generate(emailid, name, output_path)

def send_email_1(sender_email, sender_password, receiver_email, subject, body, image_path):
    message = MIMEMultipart()
    message["From"] = sender_email
    message["To"] = receiver_email
    message["Subject"] = subject

    message.attach(MIMEText(body, "plain"))

    with open(image_path, "rb") as f:
        image = MIMEImage(f.read())
        image.add_header("Content-Disposition", "attachment", filename=image_path)
        message.attach(image)

    server = smtplib.SMTP("smtp.gmail.com", 587)
    server.starttls()
    server.login(sender_email, sender_password)
    server.sendmail(sender_email, receiver_email, message.as_string())
    server.quit()

def generate(receiver_email1, name,path2):
    sender_email = "ayushsachan02@gmail.com"
    sender_password = "wajwqibbirzdvtsl"
    receiver_email = receiver_email1
    subject = "Certificate of participation"
    body = "Here is your certificate"
    image_path = path2
    send_email_1(sender_email, sender_password, receiver_email, subject, body, image_path)
    return True


@api_view(['POST'])
def feedback_answers(request):
    if request.method == 'POST':
        email = request.data.get('email')
        user = User.objects.get(emailid=email)
        answer = request.data.get('answer')

        try:
            answer = answer.split(":::(")[1:]
            feval_write([user.session_attended.session_id,user.name,user.emailid,user.district,user.state]+answer)
            return Response({'message':'feedback updated'},status=status.HTTP_200_OK)
        except User.DoesNotExist:
            return Response({'message':'user not found'},status=status.HTTP_404_NOT_FOUND)
        except Exception as e:
            return Response({'message':'error: {}'.format(str(e))},status=status.HTTP_500_INTERNAL_SERVER_ERROR)

@api_view(['POST'])
def feedback_get_info(request):
    if request.method =='POST':
        email = request.data.get('email')
        rating = request.data.get('rating')
        answer = request.data.get('answer')
        try:
            user = User.objects.get(emailid=email)
            l = []
            l.append(user.name)
            l.append(user.emailid)
            l.append(rating)
            l.append(answer)
            feedback_write(l)
            return Response({'message':'feedback updated'},status=status.HTTP_200_OK)
        except User.DoesNotExist:
            return Response({'message':'user not found'},status=status.HTTP_404_NOT_FOUND)

# @api_view(['POST'])
# def generate_certificate_api(request):
#     if request.method =="POST":
#         email = request.data.get('email')
#         name = "Navchetna"
#         # user = User.objects.get(emailid=email)
#         try:
#             if generate(email, name):
#                 return Response({'message': 'Certificate generated'}, status=status.HTTP_200_OK)
#             else:
#                 return Response({'message': 'Certificate generation unsuccessful'}, status=status.HTTP_400_BAD_REQUEST)
#         except User.DoesNotExist:
#             return Response({'message': 'User not found'}, status=status.HTTP_404_NOT_FOUND)
#         except Exception as e:
#             return Response({'message': f'An error occurred: {str(e)}'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

# def send_email(sender_email, sender_password, receiver_email, subject, body, image_path):
#     message = MIMEMultipart()
#     message["From"] = sender_email
#     message["To"] = receiver_email
#     message["Subject"] = subject

#     message.attach(MIMEText(body, "plain"))

#     with open(image_path, "rb") as f:
#         image = MIMEImage(f.read())
#         image.add_header("Content-Disposition", "attachment", filename=image_path)
#         message.attach(image)

#     server = smtplib.SMTP("smtp.gmail.com", 587)
#     server.starttls()
#     server.login(sender_email, sender_password)
#     server.sendmail(sender_email, receiver_email, message.as_string())
#     server.quit()

# def generate(receiver_email1, name):
#     template = cv2.imread('/home/testing/Navchetna/app/SPYM.jpg')
#     cv2.putText(template,name,(757,647),cv2.FONT_HERSHEY_SIMPLEX,3,(0,0,0),2,cv2.LINE_AA)
#     cv2.imwrite('/home/testing/Navchetna/app/SPYM_1.jpg', template)
#     # Example usage
#     sender_email = "ayushsachan02@gmail.com"
#     sender_password = "wajwqibbirzdvtsl"
#     receiver_email = receiver_email1
#     subject = "Certificate of participation"
#     body = "Here is your certificate"
#     image_path = "/home/testing/Navchetna/app/SPYM_1.jpg"
#     send_email(sender_email, sender_password, receiver_email, subject, body, image_path)
#     return True

# @api_view(['POST'])
# def get_status(request):


