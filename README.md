# EcoLush - Lushi Market Price 🛒

**EcoLush** est une application Android moderne conçue pour aider les consommateurs de Lubumbashi à comparer les prix des produits de première nécessité dans différents magasins en temps réel. Grâce à une base de données centralisée, les utilisateurs peuvent suivre l'évolution des prix et trouver les meilleures offres instantanément.

---

## 📸 Captures d'Écran

| Liste des Prix | Ajout de Produit | Comparateur |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/a5b7e811-2c90-41fa-a718-93a72263c06a" width="220"> | <img src="https://github.com/user-attachments/assets/e9c74d6e-b34f-4638-9af0-4d6645cb0fa3" width="220"> | <img src="https://github.com/user-attachments/assets/4d67f506-d0d0-435b-8883-fc98a250f586" width="220"> |

---

## 🚀 Fonctionnalités Principales

- **Suivi des Prix en Temps Réel** : Ajoutez et consultez les prix des produits (Alimentation, Électronique, Divers) avec photos, noms des magasins et localisations.
- **Comparateur Intelligent** : L'application regroupe automatiquement les produits identiques et affiche un classement par prix croissant pour identifier l'option la moins chère.
- **Gestion Cloud avec Supabase** : Synchronisation instantanée des données et stockage sécurisé des images via Supabase DB et Storage.
- **Interface Moderne (Jetpack Compose)** : Une UI fluide et responsive avec support complet du **Thème Clair / Sombre / Système**.
- **Recherche & Filtrage** : Filtrez les produits par catégorie ou recherchez par nom pour trouver rapidement ce que vous cherchez.

---

## 🛡️ Résilience et Expérience Utilisateur (Nouveau)

Pour répondre aux contraintes de connectivité, l'application intègre des fonctionnalités avancées de gestion des erreurs :
- **Messages d'Erreur Persistants** : Les alertes (Snackbars) restent affichées indéfiniment jusqu'à ce que l'utilisateur les valide manuellement, garantissant qu'aucune information de connexion n'est manquée.
- **Stabilité de l'Interface au "Réessayer"** : En cas de perte de réseau, l'affichage de l'erreur ne disparaît pas pendant la tentative de reconnexion, offrant une navigation stable et moins frustrante.
- **Splash Screen Immersif** : Durée de démarrage ajustée (4 secondes) pour une meilleure immersion visuelle et une transition fluide.

---

## 🛠 Technologies Utilisées

- **Langage** : Kotlin
- **UI Framework** : Jetpack Compose (Material 3)
- **Architecture** : MVVM (Model-View-ViewModel)
- **Backend-as-a-Service** : [Supabase](https://supabase.com/) (Postgrest, Storage)
- **Réseau** : Ktor Client
- **Chargement d'Images** : Coil
- **Navigation** : Jetpack Navigation Compose

---

## ⚙️ Instructions de Compilation

Pour compiler et tester l'application localement, suivez ces étapes :

### 1. Prérequis
- Android Studio Ladybug (ou version plus récente)
- JDK 17
- Un compte Supabase (pour vos propres clés API)

### 2. Configuration de Supabase
1. Créez un projet sur Supabase.
2. Créez une table `product_prices` avec les colonnes nécessaires (name, price, storeName, category, etc.).
3. Créez un bucket public nommé `product-images` dans le Storage.

### 3. Installation
1. Clonez le dépôt :
   ```bash
   git clone https://github.com/gmasmas1-cloud/Lushi-Market-Price.git
   ```
2. Ouvrez le projet dans Android Studio.
3. Localisez le fichier `SupabaseClient.kt` et remplacez les valeurs `SUPABASE_URL` et `SUPABASE_KEY` par vos propres identifiants.

### 4. Lancement
- Connectez un appareil physique ou lancez un émulateur.
- Cliquez sur le bouton **Run** (Flèche verte).

---

## 👨‍💻 Auteur
Développé par **G'mas** dans le cadre du projet EcoLush.
