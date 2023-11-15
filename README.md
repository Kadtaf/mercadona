Déploiement en local:

# Application Mercadona - Guide de Déploiement en Local

Bienvenue dans le guide de déploiement de l'application Mercadona. Ce guide vous aidera à installer et à exécuter l'application sur votre propre ordinateur, même si vous n'avez pas beaucoup d'expérience en informatique. Suivez simplement les étapes ci-dessous.

## 1. Installer Java

Avant de commencer, assurez-vous que Java est installé sur votre ordinateur. Si ce n'est pas le cas, suivez ces étapes :

- Allez sur le site [Java Download](www.https://www.oracle.com/java/technologies/downloads/).
- Téléchargez et installez la dernière version de Java(JDK17 ou JDK21) qui sera compatible avec IOS de votre machine.

## 2. Installer Maven

Maven est un outil de gestion de projet pour Java. Suivez ces étapes pour l'installer :

- Allez sur le site [Maven Download](https://maven.apache.org/download.cgi).
- Téléchargez et installez la dernière version de Maven.

## 3. Télécharger le Code Source

Vous aurez besoin du code source de l'application, suivez ces étapes :

- Cliquez sur le bouton "Code" en haut de cette page.
- Sélectionnez "Download ZIP".
- Extrayez le contenu du fichier ZIP sur votre ordinateur.

## 4. Configurer la Base de Données PostgreSQL

Avant de lancer l'application, vous devez configurer la base de données. Suivez ces étapes :

- Téléchargez et installez PostgreSQL à partir du site officiel : [PostgreSQL Download](https://www.postgresql.org/download/).

- Lors de l'installation, notez votre nom d'utilisateur PostgreSQL et votre mot de passe.

## 5. Ouvrir une Invite de Commande

Ouvrez l'invite de commande sur votre ordinateur. Si vous ne savez pas comment faire, demandez de l'aide à quelqu'un de votre entourage ou faites une recherche rapide en ligne.

## 6. Naviguer vers le Dossier de l'Application

Utilisez la commande `cd` pour naviguer vers le dossier où vous avez extrait le code source. Par exemple :

***bash
cd chemin/vers/mercadona-app

## 8. Configurer les Paramètres de la Base de Données

Ouvrez le fichier `src/main/resources/application.properties` avec un éditeur de texte (comme Bloc-notes ou Visual Studio Code). Trouvez les lignes suivantes et remplacez les valeurs entre les guillemets par vos propres informations PostgreSQL :

**properties
spring.datasource.url=jdbc:postgresql://localhost:5432/votre_base_de_donnees
spring.datasource.username=votre_nom_utilisateur
spring.datasource.password=votre_mot_de_passe

## 9. Compiler le code
Entrez la commande suivante pour compiler le code avec Maven:
mvn clean install

## 10. Lancer l'application
Maintenant, vous êtes prêt à lancer l'application. Entrez la commande suivante:
bash
java -jar target/mercadona-app.jar

L'application devrait maintenant être accessible à l'adresse http://localhost/8080 depuis votre navigateur web.

## 11. Configuration de l'Authentification

L'application Mercadona utilise Spring Security pour gérer l'authentification. Après le déploiement, vous devrez vous connecter avec un compte utilisateur existant ou créer un nouveau compte.

### Comptes Utilisateur par Défaut

Lors du déploiement local à des fins de test, vous pouvez utiliser les comptes utilisateur par défaut :

- **Username :** admin
- **Mot de Passe :** admin

### Création d'un Nouveau Compte

1. Connectez-vous à l'application en utilisant les identifiants par défaut.
2. Accédez à la page de gestion du compte ou du profil(Volet: Utilisateurs).
3. Cliquez sur le bouton "Nouveau utilisateur".
4. Entrez votre nouveau nom d'utilisateur et mot de passe.
5. Validez et utilisez vos nouveaux identifiants pour vous connecter.

**Note :** Il est fortement recommandé de créer votre propre compte utilisateur pour des raisons de sécurité.

## Utilisation de l'Application

Félicitations pour avoir réussi la connexion! Vous pouvez maintenant explorer les fonctionnalités de l'application Mercadona.

### Gestion des Produits

1. **Création d'un Nouveau Produit :**
   - Accédez à la section "Produits" de l'application.
   - Sélectionnez l'option "Nouveau Produit".
   - Remplissez les détails du produit, tels que le label, la description, l'image la catégorie, le prix, etc.
   - Enregistrez le produit.

2. **Modification d'un Produit Existants :**
   - Parcourez la liste des produits existants.
   - Sélectionnez le produit que vous souhaitez modifier.
   - Mettez à jour les informations nécessaires et enregistrez les modifications.

### Gestion des Catégories

1. **Création d'une Nouvelle Catégorie :**
   - Accédez à la section "Catégories".
   - Choisissez l'option "Nouvelle Catégorie".
   - Entrez le nom de la catégorie et sauvegardez.

2. **Modification d'une Catégorie Existante :**
   - Consultez la liste des catégories existantes.
   - Sélectionnez la catégorie à modifier.
   - Modifiez les détails au besoin et enregistrez.

### Gestion des Promotions

1. **Ajout d'une Promotion :**
   - Dirigez-vous vers la section "catalogue".
   - Optez pour l'ajout d'une nouvelle promotion.
   - Cliquez sue le bouton "AddPromo"
   - Spécifiez les détails de la promotion (réduction, Date de début, Date de fin.).
   - Enregistrez la promotion.

### Gestion des Utilisateurs

1. **Ajout d'un Nouvel Utilisateur :**
   - Accédez à la section "Utilisateurs".
   - Sélectionnez "Ajouter un Utilisateur".
   - Entrez les informations requises pour le nouvel utilisateur.
   - Enregistrez le nouvel utilisateur.

2. **Modification des Informations Utilisateur :**
   - Consultez la liste des utilisateurs existants.
   - Choisissez l'utilisateur à modifier.
   - Mettez à jour les informations nécessaires et enregistrez.

Explorez ces fonctionnalités et n'hésitez pas à contribuer en signalant des bugs ou en suggérant des améliorations. Amusez-vous bien avec l'application Mercadona!

