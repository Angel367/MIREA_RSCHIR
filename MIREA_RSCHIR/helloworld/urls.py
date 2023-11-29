from . import views
from django.urls import path

urlpatterns = [
    path('volkov/<username>', views.hello_world_page, name='hello_world_page'),
    path('volkov/', views.hello_world_page_anonymous, name='hello_world_page_anonymous')
]
