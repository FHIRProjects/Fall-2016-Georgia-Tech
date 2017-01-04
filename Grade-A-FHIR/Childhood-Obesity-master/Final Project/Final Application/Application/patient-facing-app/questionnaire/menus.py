from django.core.urlresolvers import reverse

from menu import Menu, MenuItem

Menu.add_item("main", MenuItem("Home",
                               reverse('questionnaire:index'),
                               icon="home"))

Menu.add_item("main", MenuItem("Healthy Habits Questionnaire",
                               reverse('questionnaire:respond_hh'),
                               icon="assessment"))

Menu.add_item("main", MenuItem("Food Questionnaire",
                               reverse("questionnaire:respond_food"),
                               icon="shopping_cart"))

Menu.add_item("main", MenuItem("Goal Status Questionnaire",
                               reverse("questionnaire:respond_status"),
                               icon="face"))

Menu.add_item("main", MenuItem("WIC Questionnaire",
                               reverse("questionnaire:respond_wic"),
                               icon="assignment"))

Menu.add_item("main", MenuItem("Messages",
                               reverse("questionnaire:messages"),
                               icon="mail"))

Menu.add_item("main", MenuItem("Questionnaire History",
                               reverse("questionnaire:history"),
                               icon="history"))
                               
Menu.add_item("main", MenuItem("Recommendations",
                               reverse("questionnaire:recommendations"),
                               icon="star"))                              

Menu.add_item("main", MenuItem("About",
                               reverse("questionnaire:about"),
                               icon="info"))

# TODO - This still needs to be updated to the new reverse structure
# this item will be shown to users who are not logged in
# Menu.add_item("main", MenuItem("Login",
#                                reverse('django.contrib.auth.views.login'),
#                                check=lambda request: not request.user.is_authenticated()))

# this will only be shown to logged in users and also demonstrates how to use
# a callable for the title to return a customized title for each request
# Menu.add_item("main", MenuItem(profile_title,
#                                reverse('accounts.views.profile'),
#                                check=lambda request: request.user.is_authenticated()))
