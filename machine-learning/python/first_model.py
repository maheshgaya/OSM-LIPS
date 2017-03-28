import tensorflow as tf
import pandas as pd
import numpy as np
from sklearn import preprocessing

# importing data and munging
constant_data = pd.read_csv('full_library_xt875.csv')
#normalizing data
#normalization = lambda df: (df - df.mean()) / (df.max() - df.min())
#constant_data = normalization(constant_data)

t_data = constant_data[:2787]
pred_data = t_data[['xPosition', 'yPosition']]
t_data = t_data.drop('Latitude', 1)
t_data = t_data.drop('Longitude', 1)
t_data = t_data.drop('xPosition', 1)
t_data = t_data.drop('yPosition', 1)


tp_data = constant_data[2789:]
pred_t_data = tp_data[['xPosition', 'yPosition']]
tp_data = tp_data.drop('Latitude', 1)
tp_data = tp_data.drop('Longitude', 1)
tp_data = tp_data.drop('xPosition', 1)
tp_data = tp_data.drop('yPosition', 1)


example = constant_data[2789:2791]
testing = example[['xPosition', 'yPosition']]
example = example.drop('Latitude',1)
example = example.drop('Longitude',1)
example = example.drop('xPosition',1)
example = example.drop('yPosition',1)

print(len(tp_data))

print(pred_data)




#paramters
learning_rate = 0.005
training_epochs = 100000
batch_size = 100
display_step = 1

#network paramters
n_input = 170
n_classes = 2
n_hidden_1 = 86
n_hidden_2 = 52
n_hidden_3 = 21
n_hidden_4 = 13

#tf Graph input
x = tf.placeholder('float', [None, n_input])
y = tf.placeholder('float', [None, n_classes])


#create model
def multilayer_perceptron(x, weights, biases):
    # Hidden layer with relu activation
    layer_1 = tf.add(tf.matmul(x, weights['h1']), biases['b1'])
    layer_1 = tf.nn.relu(layer_1)
    # Hidden layer with relu activation
    layer_2 = tf.add(tf.matmul(layer_1, weights['h2']), biases['b2'])
    layer_2 = tf.nn.relu(layer_2)
    # Hidden layer with relu activation
    layer_3 = tf.add(tf.matmul(layer_2, weights['h3']), biases['b3'])
    layer_3 = tf.nn.relu(layer_3)
    # Hidden layer with relu activation
    '''layer_4 = tf.add(tf.matmul(layer_3, weights['h4']), biases['b4'])
    layer_4 = tf.nn.relu(layer_4)'''
    #output layer with linear activation
    out_layer = tf.matmul(layer_3, weights['out']) + biases['out']
    return out_layer


# Store layers weight & bias
weights = {
    'h1': tf.Variable(tf.random_normal([n_input, n_hidden_1])),
    'h2': tf.Variable(tf.random_normal([n_hidden_1, n_hidden_2])),
    'h3': tf.Variable(tf.random_normal([n_hidden_2, n_hidden_3])),
    #'h4': tf.Variable(tf.random_normal([n_hidden_3, n_hidden_4])),
    'out': tf.Variable(tf.random_normal([n_hidden_3, n_classes]))
}
biases = {
    'b1': tf.Variable(tf.random_normal([n_hidden_1])),
    'b2': tf.Variable(tf.random_normal([n_hidden_2])),
    'b3': tf.Variable(tf.random_normal([n_hidden_3])),
    #'b4': tf.Variable(tf.random_normal([n_hidden_4])),
    'out': tf.Variable(tf.random_normal([n_classes]))
}

# Construct model
'''Convergence occurs at a loss at 779.46
May want to normalize the data to see if a reduction in
the error may occur (look up online)
'''
pred = multilayer_perceptron(x, weights, biases)

# Define loss and optimizer
cost = tf.reduce_mean(tf.square(pred-y))
optimizer = tf.train.AdamOptimizer(learning_rate=learning_rate).minimize(cost)

saver = tf.train.Saver()

# Initializing the variables
init = tf.initialize_all_variables()

with tf.Session() as sess:
    sess.run(init)
    total_batch_test = int(len(t_data)/batch_size)
    #training cycle
    for epoch in range(training_epochs):
        ptr = 0
        avg_cost = 0.0
        total_batch = int(len(t_data)/batch_size)

        #loop over all batches
        for i in range(total_batch):
            inp, out = t_data[ptr:ptr+total_batch], pred_data[ptr:ptr+total_batch]
            ptr+=batch_size
            _, c = sess.run([optimizer, cost], feed_dict={x: inp, y: out})



        # Compute average loss
            avg_cost += c / total_batch

            #print(avg_cost)
        # Display logs per epoch step
        if epoch % display_step == 0:
            print("Epoch:", '%06d' % (epoch+1), "cost=", \
                "{:.9f}".format(avg_cost))
        if avg_cost <= 0.11 and epoch > 100:
            break
    print("Optimization Finished!")

    # Test model
    correct_prediction = tf.equal(tf.argmax(pred, 1), tf.argmax(y, 1))
    # Calculate accuracy
    accuracy = tf.reduce_mean(tf.cast(correct_prediction, "float"))

    #print("Accuracy:", accuracy.eval({x: tp_data[0:length],
    #                                  y: pred_t_data[0:length]}))
    feed_dict = {x: example, y: testing}
    #classi = pred.eval(feed_dict)
    print(sess.run(pred, feed_dict))
    saver.save(sess, '/Users/Joel/Desktop/Research/lgps.ckpt')

# Ignore Latitude and Longitude
# Predict formula for converstion of x and y position infer Latitude and Longitude
