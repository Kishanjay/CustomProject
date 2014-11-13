MageFighters - Kishan Nirghin 10823778
=============
#Ik heb nog te weinig research gedaan om nu al weten hoe ik alle methodes ga maken. En meestal kom ik er doende weg achter hoe ik het wil aanpakken.
Voor een echte lijst met alle classes en methodes, zie de code!

##a list of classes and public methods (and their return types and/or arguments) that you’ve decided to implement;
###Entity class
- methodes om X en Y coordinaten te 'getten' en 'setten'
- methode om health aan te passen
- methode om een entity te laten springen
- collision detection methodes

###Player class
- een player is een entity (dus extends of implements)

###Enemy class
- een enemy is een entity (dus extends of implements)

###Aanval class
- methodes om X en Y coordinaten te 'getten' en 'setten'
- collision detection methodes

###Map class
- collision detection methodes

###Game class
- methode om de controls te tekenen
- methode om input te herkennen
- methode om te kijken of er op de knoppen zijn gedrukt
- methode om alles te tekenen
- methode om alle posities te updaten
- methode om pauses te handelen
- methode om terug te gaan naar een ander scherm
- methode om het spel te starten
- methode om het spel te beeindigen (gameover)


##more advanced sketches of UIs that clearly explain which features are connected to which underlying classes;
- Komt nog

##a list of APIs and frameworks that you will be using to provide functionality in your app;
- API om highscores op te slaan in een SQLite database
- API om ervoor te zorgen dat 'geschud' herkent word, dus een soort van motionsensor

##a list of database tables and fields (and their types) that you’ve decided to implement (if needed).
- Datatable met de volgende colomns: id, timestamp, ip_addr, name, score, game_version 
