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

def feedback_write(details : list):
    number_of_records_feedback_written = int(read_sheets(Feedback_SHEET_NAME)[0][0])
    # print(number_of_records_feedback_written)
    a = len(details)
    details = [details]

    write_sheet(Feedback_SHEET_NAME,details,"A",str(number_of_records_feedback_written+3),base26(a),str(number_of_records_feedback_written+3))
    write_sheet(Feedback_SHEET_NAME,[[number_of_records_feedback_written+1]]) 


### details = ['Session ID', 'name', 'email', 'state', 'district', 20*'A1']
# e.g.  ['Ayush',"ayush21031@iiitd.ac.in","Delhi","Ans_A","Ans_B","Ans_c"]
random_words = ["NA",'Ayush',"ayush21031@iiitd.ac.in","Delhi","NorthSouth",
    "Sunshine",
    "Elephant",
    "Whisper",
    "Bicycle",
    "Enigma",
    "Galaxy",
    "Velvet",
    "Mango",
    "Serenade",
    "Lighthouse",
    "Jubilant",
    "Quasar",
    "Cascade",
    "Kaleidoscope",
    "Serenity"
]
eval_random_words = ["NA",'Ayush',"ayush21031@iiitd.ac.in","Delhi","NorthSouth",
    "Sunshine",
    "Elephant",
    "Whisper",
    "Bicycle",
    "Enigma",
    "Galaxy",
    "Velvet",
    "Zingghur",
    "Mango",
    "Serenade",
    "Lighthouse",
    "Jubilant",
    "Quasar",
    "Cascade"
]

l = ["Ayush Sachan","3.5","User experience"]
## give the above function  a list of the deatils in above format(/order) and call them; (and get me a Arabian Horse)




def main():
    pretest_write(random_words)
    posttest_write(random_words)
    feval_write(eval_random_words)
    feedback_write(l)
    
if __name__ == "__main__":
    main()