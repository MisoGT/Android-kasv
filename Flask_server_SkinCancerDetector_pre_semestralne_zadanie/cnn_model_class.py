import sys
import torch # general library that has all machine learning tools for creating neural network models
import torch.nn as nn
import torch.nn.functional as F

# image size 50x50 pixels
img_size = 50

# creating a neural network model by creating/defining a class
class Cnn(nn.Module): # I created a class named Cnn that inherits from parent class nn.Module

    # constructor
    def __init__(self): # init constructor method that will run after creating a new object of Cnn class and it initializes itself

        super().__init__() # calling the init constructor method for parent class nn.Module

        # defining convolutional layers
        self.conv1 = nn.Conv2d(1,32, kernel_size=5) # two dimensional convolutional layer, input size 1, output size 32, kernel_size=5
        self.conv2 = nn.Conv2d(32,64,kernel_size=5)
        self.conv3 = nn.Conv2d(64,128,kernel_size=5)

        # defining two fully connected linear layers
        self.fc1 = nn.Linear(128*2*2, 512)
        self.fc2 = nn.Linear(512, 2) # output 2 means that we are classifing between 2 classes

    # defining method called forward
    def forward(self,x): # x is our numpy array image representation that we pass throught the neural network
        x = F.max_pool2d(F.relu(self.conv1(x)), (2,2))
        # print(f"shape after conv1: {x.shape} ")

        x = F.max_pool2d(F.relu(self.conv2(x)), (2,2))
        #print(f"shape after conv2: {x.shape} ")

        x = F.max_pool2d(F.relu(self.conv3(x)), (2,2))
        #print(f"shape after conv3: {x.shape} ")

        #sys.exit("finding the shape for linear layer")

        x = x.view(-1,128*2*2)

        x = F.relu(self.fc1(x)) # pass x(image data) throught linear layer fc1 and then do transformation F.relu function
        x = self.fc2(x) # pass x throught the second linear layer fc2
        
        x = F.softmax(x) # at the final layer we pass x throught a softmax function that transforms numbers from output layer into a probability distribution

        return(x)
    

# define a Cnn object called cnn
#cnn = Cnn()

# make a fake test image only for passing it throught convolutional layers and get the size of input for linear layer
#test_img= torch.randn(img_size, img_size).view(-1,1,img_size,img_size)
#output = cnn(test_img)