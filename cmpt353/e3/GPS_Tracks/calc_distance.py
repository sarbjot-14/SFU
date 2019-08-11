'''
references used : https://medium.com/@robertopreste/from-xml-to-pandas-dataframes-9292980b1c1c ,
https://stackoverflow.com/questions/21074361/how-to-recursively-iterate-over-xml-tags-in-python-using-elementtree
https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula/21623206
https://stackoverflow.com/questions/4913349/haversine-formula-in-python-bearing-and-distance-between-two-gps-points

'''

'''
terminal command : python3 calc_distance.py walk1.gpx
'''
from math import radians, cos, sin, asin, sqrt
import sys
from xml.dom.minidom import parseString
import xml.etree.ElementTree as et
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

#import gpxpy
'''
Create a DataFrame with columns 'lat' and 'lon'
'''
def get_data(elem):
    print(elem)
    tree = et.parse(elem)
    #create dataframe
    df_cols = ["lat", "lon"]
    points_df = pd.DataFrame(columns = df_cols)
    for elem in tree.iter('{http://www.topografix.com/GPX/1/0}trkpt'):
        #print(elem)
        #print(elem.attrib['lon'])
        lat = elem.attrib.get("lat")
        lon = elem.attrib.get("lon")
        points_df = points_df.append(pd.Series([lat,lon], index = df_cols),ignore_index=True)
    return points_df

def distance(points):
    points_df = points
    def haversine(lon1, lat1, lon2, lat2):
        """
        Calculate the great circle distance between two points
        on the earth (specified in decimal degrees)
        """
        # convert decimal degrees to radians
        lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2])

        # haversine formula
        dlon = lon2 - lon1
        dlat = lat2 - lat1
        a = sin(dlat/2)**2 + cos(lat1) * cos(lat2) * sin(dlon/2)**2
        c = 2 * asin(sqrt(a))
        r = 6371 # Radius of earth in kilometers. Use 3956 for miles
        return c * r * 1000
    '''
    #demo run:
    points = pd.DataFrame({
    'lat': [49.28, 49.26, 49.26],
    'lon': [123.00, 123.10, 123.05]})

    #print(points_df)
    #points['nextlat'] = points['lat'].shift(-1)
    #points['nextlon'] = points['lon'].shift(-1)
    '''
    points_df['nextlat'] = points_df['lat'].shift(-1)
    points_df['nextlon'] = points_df['lon'].shift(-1)
    #print(points_df)
    #print(points)
    #print(points)
    #points['difference'] = points.apply(lambda x: haversine(x['lon'],x['lat'] ,x['nextlon'],x['nextlat']), axis=1)

    points_df['difference'] = points_df.apply(lambda x: haversine(x['lon'],x['lat'] ,x['nextlon'],x['nextlat']), axis=1)

    #print(points_df)
    sumCols = points_df.sum(axis = 0, skipna = True)
    #print(sumCols)
    totalDistance = sumCols['difference']
    #print(totalDistance)

    return totalDistance

def smooth(points_df):
    #print("here")
    #print(points_df)
    #check : initial state???? use this????
    initial_state = points_df.iloc[0]
    #sensors: what kind of error do you usually expect to see (perhaps based on looking at the scatter plot, or by estimating the accuracy of the observed values
    stddev= 15/10**5
    print(stddev)
    #check; off by one degree or 15**5 meters??????
    observation_covariance = np.diag([stddev, stddev]) ** 2 # TODO: shouldn't be zero
    #transition_covariance expresses how accurate your prediction is
    stddev_transition = 10/10**5
    transition_covariance = np.diag([stddev_transition, stddev_transition]) ** 2 # TODO: shouldn't be zero
    #predict that the “next” values of the variables we're observing will be:
    transition = [[1,0], [0,1]] # TODO: shouldn't (all) be zero

    kf = KalmanFilter(
        initial_state_mean= initial_state,
        #okay
        initial_state_covariance=observation_covariance,
        #same as above??
        observation_covariance=observation_covariance,
        #
        transition_covariance=transition_covariance,
        #
        transition_matrices=transition
    )
    #check : why the underscore??
    kalman_smoothed, _ = kf.smooth(points_df)
    #print(kalman_smoothed)
    k_smoothed = DataFrame.from_records(kalman_smoothed)
    k_smoothed.columns = ['lat', 'lon']
    #print(k_smoothed)
    #k_smoothed = pd.DataFrame(np.vstack(kalman_smoothed))
    return k_smoothed

def output_gpx(points, output_filename):
    """
    Output a GPX file with latitude and longitude from the points DataFrame.
    """
    from xml.dom.minidom import getDOMImplementation
    def append_trkpt(pt, trkseg, doc):
        trkpt = doc.createElement('trkpt')
        trkpt.setAttribute('lat', '%.8f' % (pt['lat']))
        trkpt.setAttribute('lon', '%.8f' % (pt['lon']))
        trkseg.appendChild(trkpt)

    doc = getDOMImplementation().createDocument(None, 'gpx', None)
    trk = doc.createElement('trk')
    doc.documentElement.appendChild(trk)
    trkseg = doc.createElement('trkseg')
    trk.appendChild(trkseg)

    points.apply(append_trkpt, axis=1, trkseg=trkseg, doc=doc)

    with open(output_filename, 'w') as fh:
        doc.writexml(fh, indent=' ')


def main():
    points = get_data(sys.argv[1])
    points['lon'] =  pd.to_numeric(points['lon'])
    points['lat'] =  pd.to_numeric(points['lat'])

    somePoints = {'lat': points['lat'],
        'lon': points['lon']
        }

    points_dup = pd.DataFrame(somePoints,columns= ['lat', 'lon'])
    points_dup['lon'] =  pd.to_numeric(points_dup['lon'])
    points_dup['lat'] =  pd.to_numeric(points_dup['lat'])

    #points = points.convert_objects(convert_numeric=True)
    #print("regular points")
    #print(points)
    print('Unfiltered distance: %0.2f' % (distance(points),))
    smoothed_points = smooth(points_dup)
    #print("smoothed points: ")
    #print(smoothed_points)
    print('Filtered distance: %0.2f' % (distance(smoothed_points),))

    output_gpx(points, 'reg_out.gpx')
    output_gpx(smoothed_points, 'out.gpx')


#check: what does this mean
if __name__ == '__main__':
    main()
