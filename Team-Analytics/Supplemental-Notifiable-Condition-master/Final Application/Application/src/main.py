# -*- coding: utf-8 -*-
"""
Created on Sun Oct 16 19:06:36 2016

Main scripts will go in here. Doesn't do anything yet.

@author: Kirk
"""
from manager import DataManager
from disease import Disease
from util import load_object, save_object

if __name__=='__main__':
    dm = DataManager()
    
    hepatitis = dm.diseases['Hepatitis']


    