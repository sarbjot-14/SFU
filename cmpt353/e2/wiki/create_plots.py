#!/usr/bin/python
import sys
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

'''
Terminal command : python3 create_plots.py pagecounts-20190509-120000.txt pagecounts-20190509-130000.txt
'''
filename12 = sys.argv[1]
filename13 = sys.argv[2]


wiki12 = pd.read_csv(filename12, sep=' ', header=None, index_col=1,
        names=['lang', 'page', 'views', 'bytes'])
wiki13 = pd.read_csv(filename13, sep=' ', header=None, index_col=1,
        names=['lang', 'page', 'views', 'bytes'])

wiki12asc = wiki12.sort_values('views', ascending=False);
print(wiki12)

wiki12['views13'] = wiki13['views'] #add the views from 1am

plt.figure(figsize=(10, 5)) # change the size to something sensible
plt.subplot(1, 2, 1) # subplots in 1 row, 2 columns, select the first
plt.plot(wiki12asc['views'].values) # build plot 1
plt.title('Popularity Distribution')
plt.xlabel('Rank')
plt.ylabel('Views')


plt.subplot(1, 2, 2) # ... and then select the second
plt.scatter(wiki12['views'].values, wiki12['views13'].values) # build plot 2
plt.yscale('log')
plt.xscale('log')
plt.title('Daily Correlation')
plt.xlabel('Daily 1 views')
plt.ylabel('Daily 2 views')
#plt.show()

plt.savefig('wikipedia.png')
