# import numpy as np
# import cv2
# import smtplib
# from email.mime.multipart import MIMEMultipart
# from email.mime.image import MIMEImage
# from email.mime.text import MIMEText
# import warnings

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
#     warnings.filterwarnings("ignore", message="NumPy")
#     template = cv2.imread('/home/testing/Navchetna/app/SPYM.jpg')
#     cv2.putText(template,name,(757,647),cv2.FONT_HERSHEY_SIMPLEX,3,(0,0,0),2,cv2.LINE_AA)
#     cv2.imwrite('/home/testing/Navchetna/app/SPYM_1.jpg', template)
#     sender_email = "ayushsachan02@gmail.com"
#     sender_password = "wajwqibbirzdvtsl"
#     receiver_email = receiver_email1
#     subject = "Certificate of participation"
#     body = "Here is your certificate"
#     image_path = "/home/testing/Navchetna/app/SPYM_1.jpg"
#     send_email(sender_email, sender_password, receiver_email, subject, body, image_path)
#     return True

# t = generate("lakshya21062@iiitd.ac.in","lakshya")
import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from email.mime.image import MIMEImage
from PIL import Image, ImageDraw, ImageFont
import os


def generate_certificate(name):
    base_path = os.path.dirname(os.path.abspath(__file__))  # Get the base directory of your Django app

    template_path = os.path.join(base_path, "SPYM.jpg")
    output_path = os.path.join(base_path, "SPYM_2.jpg")
    font_path = os.path.join(base_path, "arial.ttf")

    img = Image.open(template_path)
    fnt = ImageFont.truetype(font_path, 50)

    d1 = ImageDraw.Draw(img)
    d1.text((600,420), name, font=fnt, fill=(0, 0, 0))
    img.save(output_path)
    # img.show()

generate_certificate("Divyanshu Kumar Deepak")

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

# def generate(receiver_email1, name,path2):
#     sender_email = "ayushsachan02@gmail.com"
#     sender_password = "wajwqibbirzdvtsl"
#     receiver_email = receiver_email1
#     subject = "Certificate of participation"
#     body = "Here is your certificate"
#     image_path = path2
#     send_email(sender_email, sender_password, receiver_email, subject, body, image_path)
#     return True

