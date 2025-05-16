# sports-classification

Dieses Projekt ist eine Spring Boot Webanwendung zur Bildklassifikation von Sportarten auf Basis eines selbst trainierten Modells mit der Deep Java Library (DJL).

## ✨ Features

* Klassifikation von Sportbildern (Subset von 20 Sportarten)
* Eigener Trainingsprozess mit ResNet50 (DJL, Version 0.32.0)
* Webinterface mit Upload + REST API Endpoint
* Deployment als Docker-Container via GitHub Container Registry (GHCR)
* Azure Web App Integration für Public Inference

## 📅 Dataset

* Quelle: [Kaggle Sports Classification Dataset](https://www.kaggle.com/datasets/gpiosenka/sports-classification/data)
* Es wurde ein **Subset von 20 Sportarten** verwendet (Zufallsauswahl)
* Siehe `models/synset.txt` für die exakte Liste der verwendeten Labels

## 🏋️‍♂️ Training

* Architektur: ResNet50 (via DJL ResNetV1 Builder)
* Bildgröße: 224x224
* Optimizer: Adam (Lernrate 0.001)
* Epochen: 15
* Output: `sportsclassifier-0015.params`

## 🌐 Deployment

* Docker-Image gebaut mit Maven Wrapper + embedded Modell
* Image gehostet auf GHCR:

  ```
  ghcr.io/<username>/sports-classification
  ```
* Azure App Service zieht Image direkt aus GHCR
* Public erreichbar unter:

  ```
  https://sports-classification-dhfge5ahf5cjc3bm.switzerlandnorth-01.azurewebsites.net/
  ```

## 📂 Struktur

```
├── models/
│   ├── sportsclassifier-0015.params
│   └── synset.txt
├── src/
│   ├── main/
│   │   ├── java/ch/zhaw/deeplearningjava/
│   │   │   ├── Training.java
│   │   │   ├── Inference.java
|   |   |   ├── Models.java
│   │   │   └── ClassificationController.java
│   │   └── resources/
│   │       └── static/
│   │           ├── index.html
│   │           └── script.js
├── pom.xml
├── Dockerfile
├── .dockerignore
├── .gitignore
```

## 📊 Beispiel

Ein erfolgreicher Aufruf gibt folgendes zurück:

```json
[
  {
    "className": "Basketball",
    "probability": 0.83451
  },
  ...
]
```

## 🚀 Starten (lokal)

```bash
./mvnw spring-boot:run
```

Oder als Docker-Container:

```bash
docker build -t sports-classifier .
docker run -p 8080:8080 sports-classifier
```