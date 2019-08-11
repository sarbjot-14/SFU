import sys
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from statsmodels.nonparametric.smoothers_lowess import lowess
import datetime as dt
from datetime import datetime

from pandas import DataFrame
import difflib
import gzip
import json
from scipy import stats
from pylab import *

'''
command line:
python3 ab_analysis.py searches.json
'''


OUTPUT_TEMPLATE = (
    '"Did more/less users use the search feature?" p-value: {more_users_p:.3g}\n'
    '"Did users search more/less?" p-value: {more_searches_p:.3g}\n'
    '"Did more/less instructors use the search feature?" p-value: {more_instr_p:.3g}\n'
    '"Did instructors search more/less?" p-value: {more_instr_searches_p:.3g}'
)


def main():
    searchdata_file = sys.argv[1]
    searches = pd.read_json(searchdata_file,orient = 'records', lines=True)
    #seperate control and treatment
    searches_odd_treatment = searches.loc[searches['uid'] % 2 == 1]
    #print(searches_odd_treatment)
    searches_even_control = searches.loc[searches['uid'] % 2 == 0]
    #print(searches_even_control)
    #sepeate searched and unsearched
    control_never_searched = searches_even_control.loc[searches_even_control['search_count'] ==0]
    control_never_searched = len(control_never_searched.index)
    control_searched  = searches_even_control.loc[searches_even_control['search_count'] >= 1]
    control_searched =  len(control_searched.index)
    treatment_never_search = searches_odd_treatment.loc[searches_odd_treatment['search_count'] == 0]
    treatment_never_search=  len(treatment_never_search.index)
    treatment_searched = searches_odd_treatment.loc[searches_odd_treatment['search_count'] >= 1]
    treatment_searched =  len(treatment_searched.index)

    #chi squared test : Did more/less users use the search feature?
    contingency = [[control_searched, control_never_searched], [treatment_searched, treatment_never_search]]
    chi2, p_chi, dof, expected = stats.chi2_contingency(contingency)
    #print(p_chi)

    #mann whitney : Did users search more/less?
    u_test_pvalue = stats.mannwhitneyu(searches_odd_treatment['search_count'], searches_even_control['search_count']).pvalue *2

    ############### REPEAT FOR INSTRUCTORS ######################
    searches = pd.read_json(searchdata_file,orient = 'records', lines=True)
    #print(searches)

    searches_instruct = searches.loc[searches['is_instructor'] == True]
    #print(searches_instruct)

    #seperate control and treatment
    searches_odd_treatment = searches_instruct.loc[searches_instruct['uid'] % 2 == 1]
    #print(searches_odd_treatment)
    searches_even_control = searches_instruct.loc[searches_instruct['uid'] % 2 == 0]
    #print(searches_even_control)
    #sepeate searched and unsearched
    control_never_searched = searches_even_control.loc[searches_even_control['search_count'] ==0]
    control_never_searched = len(control_never_searched.index)
    control_searched  = searches_even_control.loc[searches_even_control['search_count'] >= 1]
    control_searched =  len(control_searched.index)
    treatment_never_search = searches_odd_treatment.loc[searches_odd_treatment['search_count'] == 0]
    treatment_never_search=  len(treatment_never_search.index)
    treatment_searched = searches_odd_treatment.loc[searches_odd_treatment['search_count'] >= 1]
    treatment_searched =  len(treatment_searched.index)

    #chi squared test : Did more/less users use the search feature?
    contingency = [[control_searched, control_never_searched], [treatment_searched, treatment_never_search]]
    chi2, p_chi_instruct, dof, expected = stats.chi2_contingency(contingency)
    #print("\n for instructors:")
    #print(p_chi_instruct)

    #mann whitney : Did users search more/less?
    u_test_pvalue_instruct = stats.mannwhitneyu(searches_odd_treatment['search_count'], searches_even_control['search_count']).pvalue *2


    # Output
    print(OUTPUT_TEMPLATE.format(
        more_users_p=p_chi,
        more_searches_p=u_test_pvalue,
        more_instr_p=p_chi_instruct,
        more_instr_searches_p=u_test_pvalue_instruct,
    ))


if __name__ == '__main__':
    main()
