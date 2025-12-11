# Guide d'Exécution des Tests - Cinema Manager

## 📋 Vue d'ensemble

Ce projet contient maintenant une suite complète de tests unitaires avec **JUnit 5**. Un total de **5 classes de tests** ont été créées, couvrant toutes les classes principales du projet.

## 📊 Statistiques des Tests

| Classe Testée | Fichier de Test | Nombre de Tests |
|---------------|-----------------|-----------------|
| Person.java | PersonTest.java | 11 tests |
| Reservation.java | ReservationTest.java | 10 tests |
| Cinema.java | CinemaTest.java | 13 tests |
| Room.java | RoomTest.java | 24 tests |
| Seance.java | SeanceTest.java | 27 tests |
| **TOTAL** | **5 fichiers** | **~85 tests** |

## 🏗️ Structure des Tests

```
src/
└── main/java/com/cinema/
    ├── Main.java
    ├── models/
    │   ├── Cinema.java
    │   ├── Person.java
    │   ├── Reservation.java
    │   ├── Room.java
    │   └── Seance.java
    ├── interfaces/
    │   └── Reservable.java
    └── enums/
        └── SeatType.java

test/
└── com/cinema/models/
    ├── CinemaTest.java
    ├── PersonTest.java
    ├── ReservationTest.java
    ├── RoomTest.java
    └── SeanceTest.java
```

## 🚀 Comment Exécuter les Tests

### Option 1: Avec Maven (Recommandé)

```bash
# Télécharger les dépendances et exécuter les tests
mvn clean test

# Exécuter les tests avec rapport détaillé
mvn test -Dsurefire.printSummary=true

# Exécuter une classe de test spécifique
mvn test -Dtest=PersonTest
mvn test -Dtest=SeanceTest
```

### Option 2: Avec un IDE (IntelliJ IDEA, Eclipse, VSCode)

1. Ouvrir le projet dans votre IDE
2. Naviguer vers `src/test/java/com/cinema/models/`
3. Clic droit sur un fichier de test → "Run Tests"
4. Ou utiliser le raccourci clavier (Ctrl+Shift+F10 dans IntelliJ)

### Option 3: En Ligne de Commande (Sans Maven)

```bash
# 1. Télécharger JUnit Platform Console Standalone
curl -L -o lib/junit-platform-console-standalone-1.10.1.jar \
  https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.1/junit-platform-console-standalone-1.10.1.jar

# 2. Compiler les classes principales
javac -d bin src/main/java/com/cinema/*.java \
  src/main/java/com/cinema/models/*.java \
  src/main/java/com/cinema/interfaces/*.java \
  src/main/java/com/cinema/enums/*.java

# 3. Compiler les tests
javac -cp "bin:lib/junit-platform-console-standalone-1.10.1.jar" \
  -d bin test/com/cinema/models/*.java

# 4. Exécuter les tests
java -jar lib/junit-platform-console-standalone-1.10.1.jar \
  --class-path bin \
  --scan-class-path
```

## 📝 Couverture des Tests

### PersonTest.java
✅ Constructeurs (par défaut et avec paramètres)
✅ Validation des champs vides (firstName, lastName)
✅ Méthode saveSeat()
✅ Méthodes getPerson() et getPlace()

### ReservationTest.java
✅ Constructeurs (simple et avec liste)
✅ Ajout de personnes à une réservation
✅ Enregistrement automatique des sièges
✅ Gestion de liste de personnes
✅ idReservation (test de la valeur par défaut)

### CinemaTest.java
✅ Création et renommage de cinéma
✅ Ajout de salles (unique et multiple)
✅ Remplacement de liste de salles
✅ Comptage de salles
✅ Affichage des salles (avec capture System.out)

### RoomTest.java
✅ Constructeurs (défaut et custom layout)
✅ Initialisation du plan de salle 5×10
✅ Modification du layout (setRoomLayout, setSeatType)
✅ Validation du layout (places doubles)
✅ Gestion des séances (add/remove avec ArrayList)
✅ Copie défensive du seat map
✅ Gestion d'erreurs (positions/types invalides)

### SeanceTest.java
✅ Création de séance avec clonage du plan
✅ Réservation de places (NORMAL, PMR, DOUBLE)
✅ Annulation de réservations
✅ Validation des positions (hors limites, négatives)
✅ Gestion des conflits (place déjà occupée)
✅ Indépendance Room/Seance seat maps
✅ Affichage du plan de salle
✅ Gestion des réservations multiples

## 🎯 Points Clés des Tests

### Techniques Utilisées
- **@BeforeEach / @AfterEach** : Configuration et nettoyage
- **@DisplayName** : Descriptions en français
- **Assertions JUnit 5** : assertTrue, assertEquals, assertThrows, etc.
- **Capture System.out** : Tests des méthodes d'affichage
- **Tests paramétrés** : Validation de multiples cas
- **Tests de copie défensive** : Vérification d'immutabilité

### Cas de Tests Critiques
- ✅ Validation des entrées (null, vide, hors limites)
- ✅ Gestion des erreurs (exceptions attendues)
- ✅ Comportements limites (array boundaries)
- ✅ Isolation des tests (indépendance)
- ✅ Tests positifs ET négatifs

## 📈 Prochaines Étapes Suggérées

1. **Augmenter la couverture** : Viser 90%+ de code coverage
2. **Ajouter des tests d'intégration** : Tester les workflows complets
3. **Tests de performance** : Vérifier les temps d'exécution
4. **Tests de concurrence** : Validation thread-safety
5. **Mock des dépendances** : Utiliser Mockito si nécessaire

## 🐛 Exécution et Debugging

### Voir les Résultats Détaillés
```bash
mvn test -Dsurefire.reportFormat=plain
```

### Exécuter en Mode Verbose
```bash
mvn test -X
```

### Générer un Rapport HTML
```bash
mvn surefire-report:report
# Rapport disponible dans: target/site/surefire-report.html
```

## ✅ Validation

Pour vérifier que tout fonctionne :
```bash
# Doit afficher: Tests run: X, Failures: 0, Errors: 0, Skipped: 0
mvn clean test
```

## 📚 Dépendances

Les dépendances JUnit sont définies dans `pom.xml` :
- JUnit Jupiter API 5.10.1
- JUnit Jupiter Engine 5.10.1
- JUnit Jupiter Params 5.10.1

## 💡 Conseils

1. **Exécutez les tests régulièrement** pendant le développement
2. **Maintenez une couverture élevée** (>80%)
3. **Ajoutez des tests pour chaque bug** trouvé
4. **Documentez les tests complexes** avec des commentaires
5. **Utilisez TDD** (Test-Driven Development) pour les nouvelles features

---

**Note** : Tous les tests ont été écrits selon les meilleures pratiques JUnit 5 et sont prêts à être exécutés. Si vous rencontrez des problèmes, vérifiez que Java 11+ et Maven 3.6+ sont installés.
