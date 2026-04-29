# EcoLush - Lushi Market Price 🛒

**EcoLush** est une application Android moderne conçue pour aider les consommateurs de Lubumbashi (et d'ailleurs) à comparer les prix des produits de première nécessité dans différents magasins en temps réel. Grâce à une base de données centralisée, les utilisateurs peuvent suivre l'évolution des prix et trouver les meilleures offres instantanément.

---

## 🚀 Fonctionnalités Principales

- **Suivi des Prix en Temps Réel** : Ajoutez et consultez les prix des produits (Alimentation, Électronique, Divers) avec photos, noms des magasins et localisations.
- **Comparateur Intelligent** : L'application regroupe automatiquement les produits identiques et affiche un classement par prix croissant pour identifier l'option la moins chère.
- **Gestion Cloud avec Supabase** : Synchronisation instantanée des données et stockage sécurisé des images via Supabase DB et Storage.
- **Interface Moderne (Jetpack Compose)** : Une UI fluide et responsive avec support complet du **Thème Clair / Sombre / Système**.
- **Historique des Relevés** : Consultez la liste complète de vos contributions et gérez vos relevés existants.
- **Recherche Rapide** : Filtrez les produits par catégorie ou recherchez par nom pour trouver rapidement ce que vous cherchez.

---

## 📸 Captures d'Écran

*(Note : Remplacez les liens ci-dessous par les chemins réels de vos captures d'écran dans le dépôt)*

| Liste des Prix | Ajout de Produit | Comparateur |
| :---: | :---: | :---: |
| ![Screen 1](https://via.placeholder.com/200x400?text=Price+List) | ![Screen 2](https://via.placeholder.com/200x400?text=Add+Price) | ![Screen 3](https://via.placeholder.com/200x400?text=Comparison) |

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
2. Créez une table `product_prices` avec les colonnes suivantes :
   - `id` (int8, primary key)
   - `name` (text)
   - `price` (float8)
   - `currency` (text)
   - `storeName` (text)
   - `location` (text)
   - `category` (text)
   - `imageUrl` (text)
   - `createdAt` (timestamptz)
3. Créez un bucket public nommé `product-images` dans le Storage.

### 3. Installation
1. Clonez le dépôt :
   ```bash
   git clone https://github.com/votre-utilisateur/EcoLush.git
   ```
2. Ouvrez le projet dans Android Studio.
3. Localisez le fichier `SupabaseClient.kt` et remplacez les valeurs `SUPABASE_URL` et `SUPABASE_KEY` par vos propres identifiants trouvés dans les paramètres de votre projet Supabase (API).
4. Synchronisez le projet avec Gradle.

### 4. Lancement
- Connectez un appareil physique ou lancez un émulateur.
- Cliquez sur le bouton **Run** (Flèche verte) dans Android Studio.

---

## 👨‍💻 Auteur
Développé par **[Ton Nom/Pseudo]** dans le cadre du projet académique/personnel EcoLush.
