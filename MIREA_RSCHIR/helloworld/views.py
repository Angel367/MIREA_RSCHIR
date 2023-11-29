from django.shortcuts import render


def hello_world_page(request, username):
    return render(request, "helloworld.html", context={
        'username': username
    })


def hello_world_page_anonymous(request):
    return hello_world_page(request, "Anon")
