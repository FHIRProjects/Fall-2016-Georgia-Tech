from django.shortcuts import render

# Create your views here.

def low_range_image(request):
    return render(request, 'low_range_image.html')

def high_range_image(request):
    return render(request, 'high_range_image.html')