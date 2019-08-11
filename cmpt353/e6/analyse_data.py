import time
from implementations import all_implementations
import sys
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from datetime import datetime

from pandas import DataFrame
from scipy import stats
import numpy.distutils.system_info as sysinfo
from statsmodels.stats.multicomp import pairwise_tukeyhsd


#read Data

data = pd.read_csv('data.csv', sep=',', header=0 )
#print(data)

#do Anovoa to see if there is differences between groups
#qs1', 'qs2', 'qs3', 'qs4', 'qs5', 'merge1', 'partition_sort'
anova = stats.f_oneway(data['qs1'], data['qs2'], data['qs3'], data['qs4'], data['qs5'], data['merge1'], data['partition_sort'])
#print("anova")
#print(anova)
#print(anova.pvalue)

'''
p value is less than 0.05 therefore the means are different
'''

#Do Turkey's HSD test to find difference between groups
turkey_data = pd.melt(data)
#print(turkey_data)
posthoc = pairwise_tukeyhsd(
    turkey_data['value'], turkey_data['variable'],
    alpha=0.05)


print(posthoc)
fig = posthoc.plot_simultaneous()
plt.show()


'''
Give a ranking of the sorting implementations by speed, including which
ones could not be distinguished. (i.e. which pairs could our experiment not
conclude had different running times?)
'''
