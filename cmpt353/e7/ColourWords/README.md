Colour Words
With previous classes, I have collected data mapping colours (specifically RGB colours you saw on-screen) to colour words. When creating the experiment, I gave options for the English basic colour terms.

The result has been a nice data set: almost 4000 data points that we can try to learn with. It is included this week as colour-data.csv.

Let's actually use it for its intended purpose: training a classifier.

Create a program colour_bayes.py. You should take the name of the CSV file on the command line: the provided colour_bayes_hint.py does this and contains some code to nicely visualize the output.

Start by getting the data: read the CSV with Pandas. Extract the X values (the R, G, B columns) into a NumPy array and normalize them to the 0–1 range (by dividing by 255: the tools we use will be looking for RGB values 0–1). Also extract the colour words as y values.

Partition your data into training and validation sets using train_test_split.

Now we're ready to actually do something: create a naïve Bayes classifier and train it. Use the default priors for the model: they are set from the frequency in the input, which seems as sensible as anything else.

Have a look at the accuracy score on the validation data to see how you did. Print the accuracy score for this model.

The score doesn't tell much of a story: call plot_predictions from the hint to see a plot of colours (left) and predicted colour categories (right).

Colour Words and Colour Distances
The naïve Bayes approach implicitly assumes that distances in the input space make sense: distances between training X and new colour values are assumed to be comparable. That wasn't a great assumption: distances between colours in RGB colour space aren't especially useful.

Possibly our inputs are wrong: the LAB colour space is much more perceptually uniform. Let's convert the RGB colours we have been working with to LAB colours, and train on that. The skimage.color module has a function for the conversion we need. (You may have to install scikit-image, depending on your original setup).

We can create a pipeline model where the first step is a transformer that converts from RGB to LAB, and the second is a Gaussian classifier, exactly as before.

There is no built-in transformer that does the colour space conversion, but if you write a function that converts your X to LAB colours, you can create a FunctionTransformer to do the work.

The skimage.color functions assume you have a 2D image of pixel colors: you will have to a little NumPy reshaping in your function to make it all work. Reshape the array of colours to an image  (which is a .reshape(1,-1,3)), convert to LAB, and then reshape back to an array of colour values (.reshape(-1,3)).

Have a look at the accuracy value for this model as well. When finished, your colour_bayes.py should print two lines: the first and second accuracy score. Please do not have a plt.show() in your code when you submit: it makes marking a pain.
