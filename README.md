CustomProject
=============

#Kishan Nirghin
#10823778

##Intro
Mijn app voorstel is een zelf gemaakte platform game.
Deze game zal gebruik maken van de canvas API, de SQLite API en de motion sensor API.
Het concept is een 2d fighter game waarbij je een character kan aansturen door middel van “knoppen” op het touch screen. 
Het doel van het spel is om een zo hoog mogelijke score te halen door zoveel mogelijk tegenstanders uit te schakelen. 
Het moet een zeer dynamische fighting game worden, waardoor het ook leuk zal zijn om het spel gewoon te spelen. 

##Menu
Dit is een mockup van het hoofdmenu die te zien zal zijn in mijn game.
Er is hier een knop die het spel laat beginnen, een knop om de highscores te bekijken, en een knop om eventueel instellingen te veranderen.
De highscores zullen worden opgeslagen in een SQL database en zullen dus gebruik maken van een API.
![Figuur 1: Main menu](/concept/mainmenu.png) 

##Game
Dit is een mockup van het spel scherm, hierbij is jou character de grijze rechthoek links onderin.
Deze zou je dus kunnen laten “lopen” door op de knoppen links onderin te drukken.
De knoppen aan de rechter onderkant van het scherm staan voor de verschillende soorten aanvallen(/verdedigingen) die jou character kan doen.
![Figuur 2: Spelscherm](/concept/basics.png)

##Aanvallen
Door op één van de aanval knoppen de drukken rechts onder in het scherm kan je je character aansturen om een aanval uit te voeren.
Het onderstaande scherm demonstreert hoe een aanval eruit zou kunnen komen te zien. 
![Figuur 3: Aanval 1 – Dit is een vooruit vliegende bol die een tegenstander zou kunnen raken](/concept/aanval1.png)  

##Verdedigen
Een goeie fighter game moet niet alleen manieren hebben om aan te vallen, maar ook manieren om je zelf te verdedigen. Verdedigen kan je doen door op een van de aanval knoppen te drukken.
In het onderstaande scherm is gedemonstreerd hoe zo een verdediging eruit kan komen te zien.  
![Figuur 4: Verdediging 1](/concept/verdediging1.png)

##Speciaal
Om het spel iets actiever te houden kan je character tijdelijk speciale krachten krijgen. Het sterretje in het onderstaande scherm betekent dat je bent opgeladen om je speciale krachten te activeren.
Het activeren van je speciale kracht doe je door je telefoon te schudden. Voor het detecteren van het geschud word gebruik gemaakt van een API.
![Figuur 4: Speciaal](/concept/speciaal.png)