# -*- coding: utf-8 -*-
"""
Created on Sun Oct 16 15:57:05 2016

Class for converting the spreadsheet to csv files, 
and some troubleshooting tools.

@author: Kirk
"""
import os
import xlwings as xw
import pandas as pd
import numpy as np

class Converter(object):
    @classmethod
    def convert_xlsx_to_csv(cls, data_path = '../data/',
                            xlsx_name = 'Att 7 - Disease Specific Data.xlsx'):
        """
        Converts the spreadsheet at xlsx_path to many csv files in the data
        folder.  This is done separately from the rest of the code, so that
        the data can be used on a server that doesn't have Excel.
        """
        ignore = ['General']
        xlsx_path = os.path.join(data_path, xlsx_name)
        book = xw.Book(xlsx_path)
        # Create an iterable for sheets we want to parse
        valid_sheets = [sht for sht in book.sheets if sht.name not in ignore]
        for sht in valid_sheets:
            csv_path = os.path.join(data_path, sht.name + '.csv')
            df = cls.get_df(sht)
            cls.replace_bad_chars(df)
            try:
                df.to_csv(open(csv_path, 'w'),index=None, encoding='utf-8')
            except UnicodeEncodeError as e:
                print(e)
                print(sht.name)

        #book.app.kill()
        
    @classmethod
    def get_df(cls, sht):
        return sht.range(1,1).expand().options(pd.DataFrame, index=None).value
    
    @classmethod
    def replace_bad_chars(cls, df):
        """
        Some spreadsheets have weird characters in them that ascii encoder has
        issues with, so this function modifies the input to replace them.
        
        Switched to using a utf-8 encoder, but these characters are obnoxious,
        so they get replaced anyway. 
        They are asymmetric quote/apostrophe, elipsis, and non-hyphen dash
        """
        bad_chars = {u'\u2018':"'",   u'\u2019':"'", 
                     u'\u201c':'"',   u'\u201d':'"',
                     u'\u2026':'...', u'\u2014':'-'}
                     #u'\u2265':'>=',u'\xb0':'degree symbol'}
        # Only try string comparisons on columns that support it
        valid_columns = [c for c in df.columns if df[c].dtype==np.object_]
        for c in valid_columns:
            for bad_char, good_char in bad_chars.items():
                # Get a series of True/False/None that contains the bad_char
                srs = df[c].str.contains(bad_char)
                # replace None with False (can't index with None)
                srs[srs.isnull()] = False
                # Get a new df that contains only the bad records
                df2 = df[srs.astype(bool)]
                # Replace with good_char and put back into original df
                for i in df2.index:
                    df.loc[i, c] = df.loc[i, c].replace(bad_char, good_char)
    @classmethod
    def find_bad_chars(cls, sht_name, char, data_path = '../data/',
                            xlsx_name = 'Att 7 - Disease Specific Data.xlsx'):
        """
        Given a df and a bad char (likely form a UnicodeEncodeError),
        Find where in the df it exists and print the strings containing it.
        """
        xlsx_path = os.path.join(data_path, xlsx_name)
        df = cls.get_df(xw.Book(xlsx_path).sheets[sht_name])
        # Only try string comparisons on columns that support it
        valid_columns = [c for c in df.columns if df[c].dtype==np.object_]
        output = []
        for c in valid_columns:
            srs = df[c].str.contains(char)
            srs[srs.isnull()] = False
            df2 = df[srs.astype(bool)]
            for i in df2.index:
                s = df.loc[i,c]
                output.append(s)
                print(s + '\n') # This prints how the string looks in the sheet
                print(repr(s)) # This prints the exact characters used
        return output

if __name__ == '__main__':
    data_path = '../data/'
    xlsx_path = data_path + 'Att 7 - Disease Specific Data.xlsx'
    Converter.convert_xlsx_to_csv()