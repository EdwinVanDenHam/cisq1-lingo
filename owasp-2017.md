# Vulnerability Analysis

## A9:2017 Using Components with Known Vulnerabilities

### Description
Het niet updaten en in de gaten houden van components die gebruikt worden in het systeem zorgt ervoor dat de bekende exploits / kwetsbaarheden hiervan tegen het systeem gebruikt kan worden. Dit kan impact hebben op de applicatie en de omgeving waar het in draait.

### Risk
Het risico is niet overal hetzelfde maar vele components hebben dezelfde rechten als de applicatie en dat kan erg gevaarlijk zijn.

### Counter-measures
Alleen components van officiële bronnen gebruiken en de versie bijhouden van de dependencies en componeneten die gebruikt worden door middel van Dependency-check-maven en Dependabot.

## A1:2017 Injection

### Description
Door de input van de gebruiker op te halen kan er kwaadwillende data ingevoerd worden die naar een interpreter gestuurd worden.

### Risk
Door Injection kan data verloren gaan, corrupt raken en bekendgemaakt worden aan onbevoegde partijen. In sommige gevallen kan injection leiden tot een host takeover. Het risico kan dus erg groot zijn en ligt vaak vooral aan hoe gevoelig de data is.
### Counter-measures
Het gebruiken van prepared statements, hierdoor is de aanvaller niet in staat om de bedoeling van de zoekopdracht te veranderen. Ook is er een validatie op de input.
 

## A5:2017 Broken Access Control

### Description
Broken Access Control is het omzeilen van de access control. Op deze maner kunnen aanvallers zich voordoen als andere gebruikers en mogelijk extra privileges krijgen. Als er authenticatie en autorisatie zou zijn, zou je ervoor kunnen zorgen dat je geen permissies hebt, totdat je ingelogd bent.
### Risk
Het risico ligt aan hoe belangrijk de applicatie en data zijn, in dit geval kan je een spel van een ander spelen. Dit is niet de bedoeling maar het is niet gevaarlijk. 

### Counter-measures
Er zijn geen counter-measures genomen, deze zouden gemaakt zijn als authenticatie en autorisatie toegevoegd waren.
