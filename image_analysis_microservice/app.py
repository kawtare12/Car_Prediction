from flask import Flask, request, jsonify
import torch
from torchvision import models, transforms
from PIL import Image
import io
import os
import py_eureka_client.eureka_client as eureka_client
from flask_cors import CORS

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "http://localhost:8090"}})  # Autorise uniquement la Gateway

# Charger le modèle
model_path = 'model/detector_all.pth'
data_dir = r'C:\Users\hp\Documents\Projet_IA_Baidad\kawtar3\kawtar\alldata'
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

# Chargement des classes
class_names = sorted([d.name for d in os.scandir(data_dir) if d.is_dir()])
print(f"DEBUG: Classes chargées -> {class_names}")

# Charger le modèle
num_classes = len(class_names)
model = models.resnet50(pretrained=False)
model.fc = torch.nn.Sequential(
    torch.nn.Linear(model.fc.in_features, 1024),
    torch.nn.ReLU(),
    torch.nn.Dropout(0.5),
    torch.nn.Linear(1024, num_classes)
)
model.load_state_dict(torch.load(model_path, map_location=device))
model.eval()
model.to(device)
print("DEBUG: Modèle chargé avec succès.")

image_transform = transforms.Compose([
    transforms.Resize((224, 224)),
    transforms.ToTensor(),
    transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
])

# Enregistrement Eureka
eureka_client.init(eureka_server="http://localhost:8761/",
                   app_name="flask-predictor",
                   instance_port=5000)

@app.route('/predict', methods=['POST'])
def predict():
    print("DEBUG: Requête reçue au point /predict")
    print(f"DEBUG: Headers -> {request.headers}")
    print(f"DEBUG: Files -> {request.files}")

    if 'image' not in request.files or not request.files['image']:
        print("DEBUG: Aucun fichier nommé 'image' trouvé ou fichier vide dans la requête.")
        return jsonify({'error': 'Aucune image fournie'}), 400

    file = request.files['image']
    print(f"DEBUG: Fichier reçu -> Nom : {file.filename}, Type : {file.content_type}")

    try:
        # Traitement de l'image
        image = Image.open(io.BytesIO(file.read())).convert('RGB')
        input_tensor = image_transform(image).unsqueeze(0).to(device)

        with torch.no_grad():
            output = model(input_tensor)
            _, predicted = torch.max(output, 1)
            class_id = predicted.item()
            class_name = class_names[class_id]

        print(f"DEBUG: Prédiction réussie -> Classe : {class_name}, ID : {class_id}")
        return jsonify({'class_id': class_id, 'class_name': class_name})

    except Exception as e:
        print(f"DEBUG: Erreur lors de la prédiction -> {e}")
        return jsonify({'error': str(e)}), 500


if __name__ == '__main__':
    app.run(debug=True, host="0.0.0.0", port=5000)
