# Generated by Django 4.2.2 on 2023-10-06 09:41

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('app', '0010_alter_questionshelp_option5'),
    ]

    operations = [
        migrations.AlterField(
            model_name='questionshelp',
            name='correct',
            field=models.CharField(max_length=1000),
        ),
        migrations.AlterField(
            model_name='questionshelp',
            name='option1',
            field=models.CharField(max_length=1000),
        ),
        migrations.AlterField(
            model_name='questionshelp',
            name='option2',
            field=models.CharField(max_length=1000),
        ),
        migrations.AlterField(
            model_name='questionshelp',
            name='option3',
            field=models.CharField(max_length=1000),
        ),
        migrations.AlterField(
            model_name='questionshelp',
            name='option4',
            field=models.CharField(max_length=1000),
        ),
        migrations.AlterField(
            model_name='questionshelp',
            name='option6',
            field=models.CharField(max_length=1000),
        ),
    ]