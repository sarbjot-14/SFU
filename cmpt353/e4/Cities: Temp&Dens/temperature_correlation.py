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
import gzip
import json

'''
references:
https://medium.com/@robertopreste/from-xml-to-pandas-dataframes-9292980b1c1c ,
https://stackoverflow.com/questions/21074361/how-to-recursively-iterate-over-xml-tags-in-python-using-elementtree ,
https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula/21623206 ,
https://stackoverflow.com/questions/4913349/haversine-formula-in-python-bearing-and-distance-between-two-gps-points


terminal input :
python3 temperature_correlation.py stations.json.gz city_data.csv output.svg
'''
def find_temp(city_lon, city_lat, temp_stations_dist ):
    temp_station = temp_stations_dist;
    def haversine(lon_city, lat_city, lon2_arr, lat2_arr):
        """
        Calculate the great circle distance between two points
        on the earth (specified in decimal degrees)
        """
        # convert decimal degrees to radians
        lon_city, lat_city, lon2_arr, lat2_arr = map(np.radians, [lon_city, lat_city, lon2_arr, lat2_arr])
        # haversine formula
        dlon = lon2_arr - lon_city
        print(dlon)
        dlat = lat2_arr - lat_city
        a = np.sin(dlat/2)**2 + np.cos(lat_city) * np.cos(lat2_arr) * np.sin(dlon/2)**2
        a = np.sqrt(a)
        c = 2 * np.arcsin(a)
        r = 6371 # Radius of earth in kilometers. Use 3956 for miles

        return c * r * 1000

    #find distnaces
    temp_station['temp_distance'] = haversine(city_lon, city_lat, temp_station['longitude'], temp_station['latitude'])
    #print("what")

    #find temp of closest station
    #print(temp_station)
    temp_dist = temp_station['temp_distance'].min()
    #print(temp_dist)

    row_with_temp = temp_station.loc[temp_station['temp_distance']==temp_dist, 'avg_tmax']
    #row_with_temp =  temp_station[which.min(temp_station$avg_tmax),]
    #print(row_with_temp)
    #print(type(row_with_temp))
    #print(row_with_temp.size)
    print(row_with_temp.iloc[0])


    return row_with_temp.iloc[0]


stations = pd.read_json('stations.json.gz', lines=True)
#print(stations)
stations['avg_tmax'] = stations['avg_tmax']/10
city_data = pd.read_csv('city_data.csv', sep=',', header=0 )
city_data = city_data.dropna(axis=0, how='any', thresh=None, subset=None, inplace=False)
city_data['area'] = city_data['area']/1000000
indexNames = city_data[ city_data['area'] > 10000 ].index
city_data.drop(indexNames , inplace=True)
#print(city_data)
temp_stations_dist = stations[['avg_tmax','latitude','longitude']]
#print(temp_stations_dist)

city_data['avg_tmax'] = city_data.apply(lambda x: find_temp(x['longitude'],x['latitude'], temp_stations_dist), axis=1)
city_data['pop_dens'] = city_data['population']/city_data['area']
#print("here")
#print(city_data)
print(city_data)

fig, ax = plt.subplots()
ax.set_title('Temperature vs Population Density')
ax.set_xlabel('Avg Max Temperature (\u00b0C)')
ax.set_ylabel('Population Density (people/km\u00b2)')
plt.plot(city_data['avg_tmax'], city_data['pop_dens'], 'o')

plt.savefig('output.svg')
