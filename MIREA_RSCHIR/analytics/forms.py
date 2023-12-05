from django import forms


class StudentDataForm(forms.Form):
    age = forms.IntegerField(label='Age')
    average_score = forms.FloatField(label='Average Score')
    name = forms.CharField(label='Name', max_length=100)
