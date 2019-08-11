import sys
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
from scipy import stats
from pylab import *

'''
command line:
python3 reddit_weekends.py reddit-counts.json.gz
'''
'''
references:
https://www.interviewqs.com/ddi_code_snippets/select_pandas_dataframe_rows_between_two_dates
'''

OUTPUT_TEMPLATE = (
    "Initial (invalid) T-test p-value: {initial_ttest_p:.3g}\n"
    "Original data normality p-values: {initial_weekday_normality_p:.3g} {initial_weekend_normality_p:.3g}\n"
    "Original data equal-variance p-value: {initial_levene_p:.3g}\n"
    "Transformed data normality p-values: {transformed_weekday_normality_p:.3g} {transformed_weekend_normality_p:.3g}\n"
    "Transformed data equal-variance p-value: {transformed_levene_p:.3g}\n"
    "Weekly data normality p-values: {weekly_weekday_normality_p:.3g} {weekly_weekend_normality_p:.3g}\n"
    "Weekly data equal-variance p-value: {weekly_levene_p:.3g}\n"
    "Weekly T-test p-value: {weekly_ttest_p:.3g}\n"
    "Mann–Whitney U-test p-value: {utest_p:.3g}"
)


def main():
    reddit_counts = sys.argv[1]
    counts = pd.read_json(reddit_counts, lines=True)
    #print("all days:")
    #print(counts)
    #filter 2012 2013

    start_date = dt.datetime(2012,1,1)
    end_date =  dt.datetime(2013,12,31)
    #print("only 2012 and 2013")
    counts = counts[(counts['date'] >= start_date) & (counts['date'] <= end_date)]
    #print(counts)
    #filter canada
    bool_inCanada = counts['subreddit'] == 'canada'
    counts = counts[bool_inCanada]
    #print("in canada: ")
    #print(counts)

    counts['day'] = counts['date'].dt.dayofweek
    counts_weekend = counts[counts['day']>=5]

    print("weekends: ")
    print(counts_weekend)

    #weekdays only

    counts_weekday = counts[counts['day']<5]
    print("weedays: ")
    print(counts_weekday)


    '''
    are there a different number of Reddit comments
    posted on weekdays than on weekends?
    '''
    print("number of comments on weekends :")
    print(counts_weekend['comment_count'].sum())
    print("number of comments on weekdays :")
    print(counts_weekday['comment_count'].sum())
    print("Weekends has more\n\n")

    print("t test:")
    ttest = stats.ttest_ind(counts_weekend['comment_count'],counts_weekday['comment_count'] )
    print(ttest)
    print("statistic: ")
    print(ttest.statistic)
    print("t test p value:")
    print(ttest.pvalue)
    initial_ttest_p= ttest.pvalue
    print("\t\t -p value is not less than 5. Cannot reject null hypothesis ")
    #normally distributed?
    print("normal test:")
    print(stats.normaltest(counts_weekend['comment_count']).pvalue)
    print(stats.normaltest(counts_weekday['comment_count']).pvalue)
    initial_weekday_normality_p= stats.normaltest(counts_weekday['comment_count']).pvalue
    initial_weekend_normality_p= stats.normaltest(counts_weekend['comment_count']).pvalue
    '''
    if p>0.05 , so on that data, we proceed as if the distribution is normal
    '''
    #normal test with levene
    '''
     Levene's test has  that the two samples have equal variance.
    '''
    print("p value with Levene's test:")
    print(stats.levene(counts_weekend['comment_count'], counts_weekday['comment_count']).pvalue)
    initial_levene_p= stats.levene(counts_weekend['comment_count'], counts_weekday['comment_count']).pvalue
    print("\n\n\n\n\n\n\n\n\n")
    print("Transforming")


    #histograms
    num_bins = 20
    n, bins, patches = plt.hist(counts_weekend['comment_count'], num_bins, facecolor='blue', alpha=0.5)
    #plt.show()
    plt.savefig('weekend.png')

    n, bins, patches = plt.hist(counts_weekday['comment_count'], num_bins, facecolor='blue', alpha=0.5)
    #plt.show()
    plt.savefig('weekday.png')


    #######   transforming ######


    trans_counts_weekday = counts_weekday
    trans_counts_weekend = counts_weekend


    print("tansforming with sqrt")
    trans_counts_weekday = np.sqrt(counts_weekday['comment_count'])
    trans_counts_weekend = np.sqrt(counts_weekend['comment_count'])
    #cheking transformation
    n, bins, patches = plt.hist(counts_weekend['comment_count'], num_bins, facecolor='blue', alpha=0.5)
    #plt.show()
    plt.savefig('trans_weekend.png')

    n, bins, patches = plt.hist(counts_weekday['comment_count'], num_bins, facecolor='blue', alpha=0.5)
    #plt.show()
    plt.savefig('trans_weekday.png')


    #preform tests on transformed data
    print("normal test (transformed) if p>0.05 then distribution is normal:")
    print("p for weekend")
    print(stats.normaltest(trans_counts_weekend).pvalue)
    print("p for weekday")
    print(stats.normaltest(trans_counts_weekday).pvalue)
    transformed_weekday_normality_p = stats.normaltest(trans_counts_weekday).pvalue
    transformed_weekend_normality_p = stats.normaltest(trans_counts_weekend).pvalue
    '''
    if p>0.05 , so on that data, we proceed as if the distribution is normal
    '''
    #normal test with levene
    '''
     Levene's test has  that the two samples have equal variance.
    '''
    print("p value with Levene's test: (transformed) if p < 0.05 then do not have equal variance")
    print(stats.levene(trans_counts_weekend, trans_counts_weekday).pvalue)
    transformed_levene_p=stats.levene(trans_counts_weekend, trans_counts_weekday).pvalue

    # Fix 2: the Central Limit Theorem might save us.
    #do the number of comments on weekends and weekdays for each week differ?
    #weekend
    print("Fix 2")
    counts_weekend['year'] = counts_weekend['date'].apply(lambda x: x.isocalendar()[0])
    counts_weekend['week'] = counts_weekend['date'].apply(lambda x: x.isocalendar()[1])

    print("groupingh")
    counts_weekend_week = counts_weekend.groupby(['year','week']).comment_count.mean()
    print("\n average counts per week for weekends")
    print(counts_weekend_week)

    #weekday
    counts_weekday['year'] = counts_weekday['date'].apply(lambda x: x.isocalendar()[0])
    counts_weekday['week'] = counts_weekday['date'].apply(lambda x: x.isocalendar()[1])


    counts_weekday_week = counts_weekday.groupby(['year','week']).comment_count.mean()
    print("\n average counts per week for weekdays")
    print(counts_weekday_week)

    print("normal test (weeks): if p < 0.05 reject normality")
    print(stats.normaltest(counts_weekend_week).pvalue)
    print(stats.normaltest(counts_weekday_week).pvalue)
    weekly_weekday_normality_p= stats.normaltest(counts_weekday_week).pvalue
    weekly_weekend_normality_p= stats.normaltest(counts_weekend_week).pvalue

    '''
    if p>0.05 , so on that data, we proceed as if the distribution is normal
    '''
    #normal test with levene
    '''
     Levene's test has  that the two samples have equal variance.
    '''
    print("p value with Levene's test (weeks): if p<0.05 reject that they are equal, more likely they are not equal")
    print(stats.levene(counts_weekend_week, counts_weekday_week).pvalue)
    weekly_levene_p= stats.levene(counts_weekend_week, counts_weekday_week).pvalue


    print("t test (weeks):")
    ttest = stats.ttest_ind(counts_weekend_week,counts_weekday_week )

    print("t test p value: if p < 0.05 reject null, conclude alternative that they are different")
    print(ttest.pvalue)
    weekly_ttest_p= ttest.pvalue
    print("\n\n\n")

    print("Fix 3 a non-parametric test might save use:")
    print("Mann-Whitney U-test: ")
    #counts_weekend = counts_weekend.sort_values('comment_count')
    #counts_weekdayy = counts_weekday.sort_values('comment_count')
    print(stats.mannwhitneyu(counts_weekend['comment_count'], counts_weekday['comment_count']).pvalue *2)
    utest_p=stats.mannwhitneyu(counts_weekend['comment_count'], counts_weekday['comment_count']).pvalue*2
    print("\n\n")


    total_comments_weekday=  counts_weekday['comment_count'].sum()
    total_weekday= counts_weekday['comment_count'].count()
    total_comment_weekend= counts_weekend['comment_count'].sum()
    total_weekend_days= counts_weekday['comment_count'].count()

    mean_comments_weekday = total_comments_weekday/total_weekday
    mean_comments_weekend = total_comment_weekend/total_weekend_days
    print("mean weekday")
    print(mean_comments_weekday)
    print("mean weekend")
    print(mean_comments_weekend)

    print(OUTPUT_TEMPLATE.format(
        initial_ttest_p=initial_ttest_p,
        initial_weekday_normality_p=initial_weekday_normality_p,
        initial_weekend_normality_p=initial_weekend_normality_p,
        initial_levene_p=initial_levene_p,
        transformed_weekday_normality_p=transformed_weekday_normality_p,
        transformed_weekend_normality_p=transformed_weekend_normality_p,
        transformed_levene_p=transformed_levene_p,
        weekly_weekday_normality_p=weekly_weekday_normality_p,
        weekly_weekend_normality_p=weekly_weekend_normality_p,
        weekly_levene_p=weekly_levene_p,
        weekly_ttest_p=weekly_ttest_p,
        utest_p= utest_p,
    ))


if __name__ == '__main__':
    main()
