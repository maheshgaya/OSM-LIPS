import pandas as pd
import numpy as np
import pickle
from sklearn.linear_model import LinearRegression

# importing data and munging
constant_data = pd.read_csv('full_library_xt875.csv')
t_data = constant_data[:2787]

tp_data = constant_data[2789:]
example = constant_data[2788:2789]
testing = example[['xPosition', 'yPosition']]
'''example = example.drop('Latitude',1)
example = example.drop('Longitude',1)'''
example = example.drop('xPosition',1)
example = example.drop('yPosition',1)

print(len(tp_data))
pred_data = t_data[['xPosition', 'yPosition']]
print(pred_data)
'''t_data = t_data.drop('Latitude', 1)
t_data = t_data.drop('Longitude', 1)'''
t_data = t_data.drop('xPosition', 1)
t_data = t_data.drop('yPosition', 1)

pred_t_data = tp_data[['xPosition', 'yPosition']]
'''tp_data = tp_data.drop('Latitude', 1)
tp_data = tp_data.drop('Longitude', 1)'''
tp_data = tp_data.drop('xPosition', 1)
tp_data = tp_data.drop('yPosition', 1)

# Linear Regression Model
model = LinearRegression()
model.fit(t_data, pred_data)
print(example)
print(model.predict(example))
score = model.score(tp_data, pred_t_data)
print(score)

with open('lin_reg_model.pkl', 'wb') as obj:
    pickle.dump(model, obj)
