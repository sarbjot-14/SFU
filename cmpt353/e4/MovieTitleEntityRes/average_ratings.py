
from math import radians, cos, sin, asin, sqrt
import sys
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from statsmodels.nonparametric.smoothers_lowess import lowess
import datetime as dt
from datetime import datetime
from pandas.plotting import register_matplotlib_converters
register_matplotlib_converters()
from pykalman import KalmanFilter
from xml.dom.minidom import parse, parseString
from pandas import DataFrame
import difflib
'''
command line:
python3 average_ratings.py movie_list.txt movie_ratings.csv output.csv
'''

def findMatches(aTitle):
    def sumRatings(matchesList):

        tmpDataFrame = ratings_df[ratings_df['title'].isin(matchesList)]
        print("this is tmpDataFramen")
        print(tmpDataFrame)
        total = tmpDataFrame['rating'].sum()
        return total

    #cutoff= 0.47
    matches = difflib.get_close_matches(aTitle, ratings_df['title'], n= 200,cutoff=0.47  )
    print(aTitle)
    print(matches)
    rating_total = sumRatings(matches)
    print("rating total and number of ratings:\n")
    print(rating_total)
    print(len(matches))
    avg_rating = round(rating_total/len(matches),2)
    print(avg_rating)
    #output_df = output_df.append({'title':aTitle, 'rating': avg_rating},ignore_index = True)
    #print(output_df)
    return avg_rating


#read files
ratings_df = pd.read_csv('movie_ratings.csv', sep=',', header=0 )
#drop rows without ratings
ratings_df = ratings_df.dropna(axis=0, how='any', thresh=None, subset=None, inplace=False)
#print(ratings_df)
movie_list_lines = open("movie_list.txt").readlines()
movie_df = pd.DataFrame(movie_list_lines, columns = ['movies'])
#remove \n character
movie_df['movies'] = movie_df['movies'].map(lambda x: str(x)[:-1])

#print(movie_df['movies'])

#create data frame for output
output_df = pd.DataFrame(columns = ['title','rating'])

output_df['rating'] = movie_df['movies'].apply(findMatches)
output_df['title'] = movie_df['movies']
output_df = output_df.dropna(axis=0, how='any', thresh=None, subset=None, inplace=False)
#sort by title
output_df.sort_values('title')
#output_df = output_df.drop(output_df.columns[0], axis=1)
print(output_df)
output_df.to_csv(r'output.csv', index= False)
