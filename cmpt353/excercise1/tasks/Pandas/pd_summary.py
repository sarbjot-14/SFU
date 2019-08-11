#!/usr/bin/env python
# coding: utf-8

# In[1]:


import numpy as np
import pandas as pd


# # Getting Started with Pandas

# In[2]:


totals = pd.read_csv('totals.csv').set_index(keys=['name'])
counts = pd.read_csv('counts.csv').set_index(keys=['name'])


# In[3]:


#summing all rows (percip for each city) producing array
citySums = totals.sum(axis=1)


# ## City with lowest total precipitation:

# In[4]:


smallestIndex = citySums.idxmin()
print("City with lowest total precipitation:")
print(smallestIndex)
print("\n")


# In[5]:


#summing along columns to find total percip per month
sumsOfMonths = totals.sum(axis = 0)


# In[6]:


#summing along columns to find total cities per month
sumCountsMonth = counts.sum(axis=0)


# ## Average precipitation in each month:

# In[7]:


avgPercipMonth = sumsOfMonths.divide(sumCountsMonth)
print("Average precipitation in each month:")
print(avgPercipMonth)
print("\n")

# In[8]:


sumsOfCities = totals.sum(axis = 1)


# In[9]:


sumCountsCities = counts.sum(axis=1)


# ##  Average precipitation in each city:

# In[10]:


avgPercipCities = sumsOfCities.divide(sumCountsCities)
print("Average precipitation in each city:")
print(avgPercipCities)
