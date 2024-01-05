from django.contrib import admin

# Register your models here.
# from .models import Employee, User
from .models import User, State, District,QuestionsHelp, QuestionNew,Trainer, Session
# class EmployeeAdmin(admin.ModelAdmin):
#     list_display = ('email', 'password')
# admin.site.register(Employee, EmployeeAdmin)

class UserAdmin(admin.ModelAdmin):
    list_display = ('name','age','phonenumber','gender','schoolname','schoolphone','schoolemail','schooladdress','emailid','state','district','password','status','pretest','posttest','session_attended')
admin.site.register(User, UserAdmin)

class TrainerAdmin(admin.ModelAdmin):
    list_display = ('name','age','phonenumber','gender','schoolname','schoolphone','schoolemail','schooladdress','emailid','state','district','password','status','pretest','posttest')
admin.site.register(Trainer, TrainerAdmin)

class StateAdmin(admin.ModelAdmin):
    list_display = ('name','district_list')
admin.site.register(State, StateAdmin)

class DistrictAdmin(admin.ModelAdmin):
    list_display = ('name','state','nearest_center')
admin.site.register(District, DistrictAdmin)

# class CenterAdmin(admin.ModelAdmin):
#     list_display = ('name','district','address','center_district_list')
# admin.site.register(Center, CenterAdmin)

class QuestionsHelpAdmin(admin.ModelAdmin):
    list_display = ['question','option1','option2','option3','option4','option5','option6','correct','language']
admin.site.register(QuestionsHelp,QuestionsHelpAdmin)


# class QuestionNewAdmin(admin.ModelAdmin):
#     list_display = ('question', 'mode')
#     list_filter = ('mode',)
#     search_fields = ('question',)

#     fieldsets = (
#         ('Question Details', {
#             'fields': ('question', 'mode')
#         }),
#         ('Options', {
#             'fields': ('option1', 'option2', 'option3', 'option4'),
#             'classes': ('collapse',),
#             'description': 'Only applicable for Multiple Choice Questions (MCQ)',
#         }),
#         ('Long Answer', {
#             'fields': ('long_answer',),
#             'classes': ('collapse',),
#             'description': 'Only applicable for Long Answer questions',
#         })
#     )

#     def get_readonly_fields(self, request, obj=None):
#         readonly_fields = super().get_readonly_fields(request, obj)
#         if obj and obj.mode == 'long_answer':
#             readonly_fields += ('option1', 'option2', 'option3', 'option4')
#         return readonly_fields

# admin.site.register(QuestionNew, QuestionNewAdmin)
class QuestionNewAdmin(admin.ModelAdmin):
    list_display = ('question', 'mode', 'language')  # Add 'language' to the list display
    list_filter = ('mode', 'language')  # Add 'language' to the list filter
    search_fields = ('question',)

    fieldsets = (
        ('Question Details', {
            'fields': ('question', 'mode', 'language')  # Add 'language' to the fieldset
        }),
        ('Options', {
            'fields': ('option1', 'option2', 'option3', 'option4'),
            'classes': ('collapse',),
            'description': 'Only applicable for Multiple Choice Questions (MCQ)',
        }),
        ('Long Answer', {
            'fields': ('long_answer',),
            'classes': ('collapse',),
            'description': 'Only applicable for Long Answer questions',
        })
    )

    def get_readonly_fields(self, request, obj=None):
        readonly_fields = super().get_readonly_fields(request, obj)
        if obj and obj.mode == 'long_answer':
            readonly_fields += ('option1', 'option2', 'option3', 'option4')
        return readonly_fields

admin.site.register(QuestionNew, QuestionNewAdmin)


class SessionAdmin(admin.ModelAdmin):
    list_display = ('session_id','emailid_T1','emailid_T2','state','district','address','datetime','capacity','status')
admin.site.register(Session, SessionAdmin)