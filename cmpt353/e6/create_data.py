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

#print(sysinfo.platform_bits)




n = 20000 #size of array
data = pd.DataFrame(columns = ['qs1', 'qs2', 'qs3', 'qs4', 'qs5', 'merge1', 'partition_sort'])
random_array = np.random.randint(10000, 100 * n + 10000, size=(n,))

for i in range(40):
    one_run = []
    for sort in all_implementations:

        st = time.time()
        res = sort(random_array)
        en = time.time()
        #print(en)
        one_run.append(en-st)
    new_series = pd.Series(one_run, index=['qs1', 'qs2', 'qs3', 'qs4', 'qs5', 'merge1', 'partition_sort'])
    #print(new_series)
    #new_series = pd.Series(one_run)
    data = data.append(new_series,ignore_index = True)
    #data = data.concat(new_series)


#print(data)
data.to_csv('data.csv', index=False)
#checking if data is normal with histogram
#plt.hist(data['qs1'], normed=True, bins=30)
#plt.show()
print("done")
