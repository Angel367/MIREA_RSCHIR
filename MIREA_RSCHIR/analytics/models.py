from django.db import models


class ImageModel(models.Model):
    image = models.ImageField()
    name = models.CharField(max_length=30)


class Cookie(models.Model):
    cookie_value = models.TextField()
