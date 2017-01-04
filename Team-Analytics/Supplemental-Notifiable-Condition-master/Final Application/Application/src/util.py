# -*- coding: utf-8 -*-
"""
@author: Kirk Jensen
"""
import pickle
from strcomp import StringCompare as SC
import pandas as pd

def load_object(path, default_type=None):
    """
    Un-pickles an object
    """
    try:
        return pickle.load(open(path, 'rb'))
    except:
        if default_type is None:
            return None
        else:
            return default_type()

def save_object(path, obj):
    """
    Pickles an object
    """
    pickle.dump(obj, open(path, 'wb'))
    
def add_dicts(x, y):
    """
    Merges two dictionaries together into a new dict.
    """
    z = x.copy()
    z.update(y)
    return z
    
def sort_tuples(tuples, description, desc_col):
    def comparitor(v):
        desc2 = v[desc_col]
        if pd.isnull(desc2):
            desc2 = v[desc_col-1] if desc_col>0 else pd.np.nan # Try the label column
        if pd.isnull(desc2):
            return 0.0
        return SC.compare_strings(description, desc2)
    tuples.sort(key=comparitor, reverse=True)