# Best Reddit Comment

A program program that finds the best reddit comment in each sub reddit.


# Inputs and Outputs

## Inputs:
	- reddit-1 directory (spark takes directories filled with files as input)

## Ouputs:
	- output directory which contains the best comments in each subreddit (spark ouputs into a directory.

## Installation and Setup Instructions

Clone down this repository.

Installation:

Most of the libraries needed can be found in Anaconda which can be downloaded from: https://www.anaconda.com/distribution/

Need to install spark

Run the program:

`export PYSPARK_PYTHON=python3'

'export PATH=$PATH:/<path to hadoop bin file>'

'export JAVA_HOME=/<path to java module>/java-8-oracle/'

'spark-submit reddit_relative.py reddit-1 output`


## Reflection

  - This was an assignment for Data Science class
  - Had to do a lot of prelimary steps:
			1.Calculate the average score for each subreddit, as before.
			2.Exclude any subreddits with average score ≤0.
			3.Join the average score to the collection of all comments. Divide to get the relative score.
			4.Determine the max relative score for each subreddit.
			5.Join again to get the best comment on each subreddit: we need this step to get the author.
	- Learned how to use spark with big data
	- Learned how small extra steps can help optimize the computation time such as caching dataframes so that spark can it later rather than re-computing those values and using broadcast join when one dataframe is small enough

## Technologies, Frameworks, Tools used
	-Pandas and Numpy
	-Spark
