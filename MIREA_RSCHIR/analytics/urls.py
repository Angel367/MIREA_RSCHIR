from . import views
from .views import ImageView
from django.urls import path

urlpatterns = [
    path('volkov/create/<name>', views.create_view, name='create_view_page'),
    path('volkov/write_server/', views.write_server, name='write_server_page'),
    path('volkov/image/<int:pk>/', ImageView.as_view(), name='image_view'),
]
