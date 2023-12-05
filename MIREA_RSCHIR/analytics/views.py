from django.core.files.base import ContentFile
from django.shortcuts import render, redirect
import requests
import matplotlib.pyplot as plt
from PIL import Image
from io import BytesIO

from django.views.generic import DetailView

from .models import ImageModel, Cookie
from .forms import StudentDataForm

url = 'http://go:8080/api/student/linear'


class ImageView(DetailView):
    model = ImageModel
    template_name = 'image.html'  # Создайте шаблон для вывода изображения
    context_object_name = 'image'


def create_view(request, name):
    cookie_value = Cookie.objects.get(id=1).cookie_value
    response = requests.get(url, cookies={"student_data": cookie_value})
    print(response)
    data = response.json()

    if ImageModel.objects.filter(name="A" + str(data['age']) + "AS" + str(data['averageScore'])).exists():
        return redirect('/volkov/image/' +
                        str(ImageModel.objects.get(name="A" + str(data['age']) + "AS" + str(data['averageScore'])).id))

    plt.bar(['Age', 'Average Score'], [data['age'], data['averageScore']])
    plt.xlabel('Category')
    plt.ylabel('Value')
    plt.title('Bar Chart of Age and Average Score')
    buffer = BytesIO()
    plt.savefig(buffer, format='png')
    buffer.seek(0)
    image = Image.open(buffer)
    image_model = ImageModel()
    buffer = BytesIO()
    image.save(buffer, format='PNG')
    image_model.image.save('scatter_plot.png',
                           ContentFile(buffer.getvalue()))  # Используем ContentFile для записи данных из буфера
    image_model.name = "A" + str(data['age']) + "AS" + str(data['averageScore'])
    buffer.close()
    image_model.save()
    plt.close()

    return redirect('/volkov/image/' +
                    str(ImageModel.objects.get(name="A" + str(data['age']) + "AS" + str(data['averageScore'])).id))


def write_server(request):
    if request.method == 'POST':
        form = StudentDataForm(request.POST)
        if form.is_valid():
            age = form.cleaned_data['age']
            average_score = form.cleaned_data['average_score']
            name = form.cleaned_data['name']

            data = {
                "age": age,
                "averageScore": average_score,
                "name": name
            }
            response = requests.post(url, json=data)

            if Cookie.objects.filter(id=1).exists():
                cookie = Cookie.objects.get(id=1)
                cookie.cookie_value = response.cookies.get('student_data')
                cookie.save()
            else:
                cookie = Cookie()
                cookie.cookie_value = response.cookies.get('student_data')
                cookie.save()
    else:
        form = StudentDataForm()

    return render(request, 'write_server.html', {'form': form})
