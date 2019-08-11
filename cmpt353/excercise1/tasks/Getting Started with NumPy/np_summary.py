#!/usr/bin/env python
# coding: utf-8

# In[1]:


import numpy as np


# # Getting Started with NumPy

# In[2]:


data = np.load('monthdata.npz')
totals = data['totals']
counts = data['counts']


# In[3]:


#summing all rows (percip for each city) producing array
rowTotals = np.sum(totals, axis=1)


# In[4]:


#finding index of where the lowest percip is
minIndex = np.where(rowTotals == np.amin(rowTotals))


# ## City with lowest total precipitation:

# In[5
print("City with lowest total precipitation:")
print(minIndex[0][0])



# In[6]:


#summing along columns to find total percip per month
totPercipMonth = np.sum(totals, axis=0)


# In[7]:


#summing along columns to find total cities per month
totalCountMonth = np.sum(counts, axis=0)


# In[8]:


#finding averge percip per month
avgPercipMonth = np.divide(totPercipMonth, totalCountMonth)


# ##  Average precipitation in each month:

# In[9]:

print("Average precipitation in each month:")
print(avgPercipMonth)
print("\n")



# In[10]:


#finding total percip per city by summing along rows
totPercipCity = np.sum(totals, axis=1)


# In[11]:


#finding total opservations per city
totalCountCity = np.sum(counts, axis=1)


# In[12]:


#finding average percipitation per city
avgPercipCity = np.divide(totPercipCity, totalCountCity)


# ##  Average precipitation in each City:

# In[13]:

print(" Average precipitation in each City:")
print(avgPercipCity)
print("\n")


# In[14]:


#reshaping 12 months to where every inner array is one quarter
reshaped = totals.reshape(4*len(totals),3)


# In[15]:


#summing each quarter for each city. Every element now represent
#one quarter
summedQuarter = np.sum(reshaped, axis= 1)


# ## Quarterly precipitation totals:

# In[17]:


#Reshaping so that each inner array is one cities full year
finalReshape = summedQuarter.reshape(len(totals), 4)
print("Quarterly precipitation totals:")
print(finalReshape)


# In[ ]:
