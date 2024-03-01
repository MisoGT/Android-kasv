from datetime import datetime
from flask import Flask
from flask import flash
from flask import jsonify
from flask import request
from flask import session
from flask import render_template
from flask import redirect
from flask import url_for
import os
import torch
import cv2
import numpy as np

from cnn_model_class import Cnn # import Cnn object

# initialize flask web application
app = Flask(__name__)

# !!! CREATE A FOLDER "static" and in this subfolder "uploaded_images" !!!
# path to the folder for storing uploaded images
UPLOAD_FOLDER = "static/uploaded_images"

# Load the trained PyTorch model
    # make a new object of class Cnn
cnn = Cnn() # cnn equals new instantiation of this Cnn object
    # load in trained pytorch model
cnn.load_state_dict(torch.load("saved_model.pth"))
    # tell Pytorch that we are in evaluation state. Method eval() sets the module in evaluation mode
cnn.eval()

@app.route("/upload_predict_images", methods=['GET', 'POST'])
def upload_predict_images():
     
	image_file = request.files['image']

	if image_file:
            # Your existing code for saving the image and preprocessing
            timestamp = datetime.now().strftime('%Y%m%d%H%M%S')
            image_filename = f"IMG_{timestamp}.jpg"
            image_location = os.path.join(UPLOAD_FOLDER, image_filename)
            image_file.save(image_location)

            img_size = 50
            img = cv2.imread(image_location, cv2.IMREAD_GRAYSCALE)
            img = cv2.resize(img, (img_size, img_size))
            img_array = np.array(img)
            img_array = img_array / 255
            img_array = torch.Tensor(img_array)

            # Make a prediction about the uploaded skin mole image using my trained pytorch model
            with torch.no_grad():
                cnn_output = cnn(img_array.view(-1, 1, img_size, img_size))[0]

                if cnn_output[0] >= cnn_output[1]:
                    prediction = "BENIGN skin mole"
                    confidence = round(float(cnn_output[0]), 3)
                else:
                    prediction = "MALIGNANT skin mole"
                    confidence = round(float(cnn_output[1]), 3)

            # RETURN Prediction Result from Pytorch model as String
            #return str(confidence) + ', ' + prediction                    
            # Experimentalny RETURN, FUNGUJE
            #return 'confidence is: '+str(confidence) + ', prediction is: '+prediction        
            # Experimentalny RETURN 2, FUNGUJE 
            return 'Prediction is: '+prediction +', Confidence is: '+str(confidence)

            # RETURN Prediction Result as JSON response
            #response_data = {'prediction': prediction, 'confidence': confidence}
            #print(response_data)
            #return jsonify(response_data)


# run the application on a local development server
if __name__ == "__main__":
    app.run(host="0.0.0.0", port=12000, debug=True)