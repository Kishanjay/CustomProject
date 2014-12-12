MageFighters - Kishan Nirghin 10823778
=============
###Ik heb nog te weinig research gedaan om nu al weten hoe ik alle methodes ga maken. En meestal kom ik er doende weg achter hoe ik het wil aanpakken.
###Voor een echte lijst met alle classes en methodes, zie de code!

##a list of classes and public methods (and their return types and/or arguments) that you’ve decided to implement
###MainMenu class
- Dit is het scherm die elke nieuwe gebruiker op het begin te zien krijgt
```java
//zet de contentView
public void onCreate()

//verandert de activity naar gameActivity
public void startGame()

//verandert de activity naar highscoreActivity
public void viewHighScore()

//verandert de activity naar settingsActivity
public void settings()
```

###highscoreActivity class
```java
public void getHighscores()
```

###settingsActivity class
```java
public void setVolume(int volume)
public void resetGame()
public void goBack()
```

###GameActivity class
- Is de activity die de gameView maakt
```java
//set de contentView op new GameView, of een opgeslagen gameView
public void onCreate()

//moet de huidige gameView state opslaan en de activity sluiten
public void onBackPressed()

```

###GameView class
```java
//Moet alle objecten updaten
void Update()

//Moet alle objecten tekenen
protected void onDraw(Canvas canvas)

//Houd bij op welke knoppen er gedrukt zijn
public boolean onTouchEvent(MotionEvent ev)
	return true;
```

###GameLoopThread class
```java
//is de thread die de framerate constant probeert te houden
public void run()

//zorgt er voor dat er geen updates vanuit run() naar de canvas worden gestuurt
public void setRunning(boolean run)
```

###Player
```java
//update de spelers positie elke frame
public void update()

//functies spreken voor zich
public void moveLeft()
public void moveRight()
public void stop()
public void jump()
public void attack()

//geeft een rectangle terug voor collision detection
public void getRect()
```

###Button
```java
public boolean isTouched()
public boolean isPressed()
public boolean isReleased()
public void getRect()
```

###Attack
```java
public void update()
public void isVisible()
```

###Enemy
```java
public void handleCollision(Attack attack)
public void update()
```

##more advanced sketches of UIs that clearly explain which features are connected to which underlying classes;
![Figuur 1: Main menu](../concept/mainmenu.png) 
![Figuur 2: Spelscherm](..-/concept/basics.png)
![Figuur 3: Aanval 1 – Dit is een vooruit vliegende bol die een tegenstander zou kunnen raken](/concept/attack1.png)
![Figuur 4: Verdediging 1](/concept/defence1.png)
![Figuur 5: Speciaal](/concept/special.png)

##a list of APIs and frameworks that you will be using to provide functionality in your app;
- API om highscores op te slaan, ik denk dat ik hiervoor de google play game API ga gebruiken
- API om ervoor te zorgen dat 'geschud' herkent word, dus de van motionsensor API

##a list of database tables and fields (and their types) that you’ve decided to implement (if needed).
- Datatable met de volgende colomns: id, timestamp, ip_addr, name, score, game_version 

##Final thoughts on this project (finished)
###Thoughts
- After creating this game I do realise that it is a lot more work than I expected it to be.
It really does make me wonder how much effort and organizing goes paired with creating some of the 'big' (mobile) games.
###Difficulties
- Writing a lot of code without duplicating too much (since that is a bad coding practise) turned out to be quite difficult to get a grasp on, but extremely handy in the end
- Also having this much code made it quite difficult to decide what functions should be handled by which classes.
- For example in my Enemy and Player class I now load all of the sprites used by giving them a spritesheet, I wonder if there is a better way to deal with sprites that belong to an object.
###Results
- I really learned the power of object oriented programming by extending and implementing classes.
I do believe that they make game development, or really any kind of development, less of a struggle.
- Also I now think that static functions and variables are also quite handy since they do not need an object to be referenced too,
but I wonder if coding like this goes paired with the best coding practises.

