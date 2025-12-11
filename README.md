# Système de Réservation de Cinéma

## Structure du projet

```
cinema-project/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── cinema/
│                   ├── Main.java
│                   ├── models/
│                   │   ├── Cinema.java
│                   │   ├── Room.java
│                   │   ├── Seance.java
│                   │   ├── Reservation.java
│                   │   └── Person.java
│                   ├── interfaces/
│                   │   └── Reservable.java
│                   ├── enums/
│                   │   └── SeatType.java
│                   └── utils/
└── test/
```

## Organisation

### Models (`com.cinema.models`)
Contient toutes les entités métier du système.

### Interfaces (`com.cinema.interfaces`)
Contient les contrats d'interface pour découpler le code.

### Enums (`com.cinema.enums`)
Contient les énumérations (types de sièges, statuts, etc.).

### Utils (`com.cinema.utils`)
Pour les classes utilitaires futures (validation, formatage, etc.).

## Compilation

```bash
# Depuis la racine du projet
javac -d bin -sourcepath src/main/java src/main/java/com/cinema/Main.java

# Exécution
java -cp bin com.cinema.Main
```

## Conventions respectées

- Package naming: `com.cinema.*`
- Classes dans des packages dédiés par responsabilité
- Séparation models/interfaces/enums
- Un fichier = une classe publique
- Nommage en CamelCase pour classes
- Imports explicites