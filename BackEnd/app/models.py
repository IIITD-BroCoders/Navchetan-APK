from django.db import models

# class Employee(models.Model):
#     email = models.EmailField()
#     password = models.CharField(max_length=30)


class State(models.Model):
    name = models.CharField(max_length=50)
    district_list = models.CharField(max_length=1000)

class District(models.Model):
    name = models.CharField(max_length=50)
    state = models.CharField(max_length=50)
    nearest_center = models.CharField(max_length=100)

# class Center(models.Model):
#     name = models.CharField(max_length=50)
#     district = models.CharField(max_length=50)
#     address = models.CharField(max_length=100)
#     state = models.CharField(max_length=50,default ="lmao") # changed
#     center_district_list = models.CharField(max_length=1000)

# class Questions(models.Model):
#     question = models.CharField(max_length=1000)
#     option1 = models.CharField(max_length=100)
#     option2 = models.CharField(max_length=100)
#     option3 = models.CharField(max_length=100)
#     option4 = models.CharField(max_length=100)
#     option5 = models.CharField(max_length=100)
#     option6 = models.CharField(max_length=100)
#     correct = models.CharField(max_length=100)
#     language = models.CharField(max_length=100,default="English")

class QuestionsHelp(models.Model):
    question = models.CharField(max_length=1000)
    option1 = models.CharField(max_length=1000)
    option2 = models.CharField(max_length=1000)
    option3 = models.CharField(max_length=1000)
    option4 = models.CharField(max_length=1000)
    option5 = models.CharField(max_length=1000)
    option6 = models.CharField(max_length=1000)
    correct = models.CharField(max_length=1000)
    language = models.CharField(max_length=100,default="English")

class Session(models.Model):
    session_id = models.AutoField(primary_key=True) # session id added
    emailid_T1 = models.EmailField()
    emailid_T2 = models.EmailField(blank = True, null = True)
    state = models.CharField(max_length=50)
    district = models.CharField(max_length=50)
    address = models.CharField(max_length=200, default="address")
    datetime = models.DateTimeField()
    capacity = models.IntegerField()
    status = models.CharField(max_length=5)

class User(models.Model):
    name = models.CharField(max_length=50)
    age = models.CharField(max_length=5)
    phonenumber = models.CharField(max_length=15)
    gender = models.CharField(max_length=15)
    schoolname = models.CharField(max_length=50, default="DPS")
    schoolphone = models.CharField(max_length=50)
    schoolemail = models.EmailField()
    schooladdress = models.CharField(max_length=50)
    emailid=models.EmailField()
    state = models.CharField(max_length=50)
    district = models.CharField(max_length=50)
    password = models.CharField(max_length=50)
    status = models.CharField(max_length=50, default="-1")
    pretest = models.CharField(max_length=5,default="-1")
    posttest = models.CharField(max_length=5,default="-1")
    session_attended = models.ForeignKey(Session, on_delete=models.SET_NULL, null=True, blank=True) # session id added

class Trainer(models.Model):
    name = models.CharField(max_length=50)
    age = models.CharField(max_length=5)
    phonenumber = models.CharField(max_length=15)
    gender = models.CharField(max_length=15)
    schoolname = models.CharField(max_length=50, default="DPS")
    schoolphone = models.CharField(max_length=50)
    schoolemail = models.EmailField()
    schooladdress = models.CharField(max_length=50)
    emailid=models.EmailField()
    state = models.CharField(max_length=50)
    district = models.CharField(max_length=50)
    password = models.CharField(max_length=50)
    status = models.CharField(max_length=50, default="1")
    pretest = models.CharField(max_length=5,default="-1")
    posttest = models.CharField(max_length=5,default="-1")
    session_attended = models.ForeignKey(Session, on_delete=models.SET_NULL, null=True, blank=True) # session id added


class QuestionNew(models.Model):
    MODE_CHOICES = (
        ('mcq', 'Multiple Choice Question'),
        ('long_answer', 'Long Answer'),
    )

    question = models.CharField(max_length=255)
    mode = models.CharField(max_length=11, choices=MODE_CHOICES)
    option1 = models.CharField(max_length=100, blank=True, null=True)
    option2 = models.CharField(max_length=100, blank=True, null=True)
    option3 = models.CharField(max_length=100, blank=True, null=True)
    option4 = models.CharField(max_length=100, blank=True, null=True)
    long_answer = models.TextField(blank=True, null=True)
    language = models.CharField(max_length=100,default="English")

    def __str__(self):
        return self.question

    def get_options(self):
        return [self.option1, self.option2, self.option3, self.option4]

    class Meta:
        verbose_name_plural = 'Questions'
