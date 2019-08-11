#!/usr/bin/python

import numpy as np
import pandas as pd


def get_precip_data():
    return pd.read_csv('precipitation.csv', parse_dates=[2])


def date_to_month(d):
    # You may need to modify this function, depending on your data types.
    #print("this is d")
    #print(d)
    return '%04i-%02i' % (d.year, d.month)


def pivot_months_pandas(data):
    """
    Create monthly precipitation totals for each station in the data set.

    This should use Pandas methods to manipulate the data.
    """
    #print("this is the original data")
    #print(data)
    data['month'] = data['date'].apply(date_to_month) #converting data
    #print("this is after date to month func")
    #print(data)









    
    grouped_data = data.groupby(['name','month'])
    #print("after grouping")
    '''
    for key, item in grouped_data:
            print(item)
    '''

    agg_data_totals = grouped_data.aggregate('sum').reset_index()  #sums each column of each group
    agg_data_totals =agg_data_totals.drop(['longitude','elevation','latitude'],axis=1)
    #print("after aggregate sum and drop")
    #print(agg_data_totals)
    totals = agg_data_totals.pivot(index ="name", columns = "month")
    #print("after pivoting for totals")
    #print(totals)
    totals.columns = totals.columns.droplevel()
    #print("after dropping for totals")
    #print(totals)


    agg_data_counts = grouped_data.aggregate('count').reset_index()
    agg_data_counts =agg_data_counts.drop(['longitude','elevation','latitude','station','date'],axis=1)
    #print("after aggregate count")
    #print(agg_data_counts)
    counts = agg_data_counts.pivot(index ="name", columns = "month")
    counts.columns = counts.columns.droplevel()
    #print("after pivoting and dropping for counts")
    #print(counts)

    return totals, counts


def pivot_months_loops(data):
    """
    Create monthly precipitation totals for each station in the data set.

    This does it the hard way: using Pandas as a dumb data store, and iterating in Python.
    """
    # Find all stations and months in the data set.
    stations = set()
    months = set()
    for i,r in data.iterrows():
        stations.add(r['name'])
        m = date_to_month(r['date'])
        months.add(m)

    # Aggregate into dictionaries so we can look up later.
    stations = sorted(list(stations))
    row_to_station = dict(enumerate(stations))
    station_to_row = {s: i for i,s in row_to_station.items()}

    months = sorted(list(months))
    col_to_month = dict(enumerate(months))
    month_to_col = {m: i for i,m in col_to_month.items()}

    # Create arrays for the data, and fill them.
    precip_total = np.zeros((len(row_to_station), 12), dtype=np.uint)
    obs_count = np.zeros((len(row_to_station), 12), dtype=np.uint)

    for _, row in data.iterrows():
        m = date_to_month(row['date'])
        r = station_to_row[row['name']]
        c = month_to_col[m]

        precip_total[r, c] += row['precipitation']
        obs_count[r, c] += 1

    # Build the DataFrames we needed all along (tidying up the index names while we're at it).
    totals = pd.DataFrame(
        data=precip_total,
        index=stations,
        columns=months,
    )
    totals.index.name = 'name'
    totals.columns.name = 'month'

    counts = pd.DataFrame(
        data=obs_count,
        index=stations,
        columns=months,
    )
    counts.index.name = 'name'
    counts.columns.name = 'month'

    return totals, counts


def main():
    data = get_precip_data()
    totals, counts = pivot_months_pandas(data)
    totals.to_csv('totals.csv')
    counts.to_csv('counts.csv')
    np.savez('monthdata.npz', totals=totals.values, counts=counts.values)


if __name__ == '__main__':
    main()
