# Generated by Django 4.2.2 on 2023-08-15 13:56

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('app', '0006_alter_user_status'),
    ]

    operations = [
        migrations.AddField(
            model_name='trainer',
            name='session_attended',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.SET_NULL, to='app.session'),
        ),
    ]
