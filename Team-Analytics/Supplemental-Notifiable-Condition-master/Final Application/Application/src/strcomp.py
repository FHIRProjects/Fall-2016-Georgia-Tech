# -*- coding: utf-8 -*-
"""
Class for comparing strings.  Includes a few different methods

@author: Kirk, Anu
"""

import string
from difflib import SequenceMatcher as SM

class StringCompare(object):
    """
    Different methods for comparing addresses.
    There are several ways to compute the ratio, like diff or set intersection.
    """
    
    @classmethod
    def compare_strings(cls, s1, s2, method='max_all'):
        """
        Performs a string comparison, and returns the similarity ratio (0->1).
        
        The method argument will get passed to getattr.  If the lookup fails,
        The ratio used is the max of all available comparison methods.
        """
        try:
            comparitor = getattr(cls, method)
        except:
            comparitor = cls.jaccard
            
        ratio = comparitor(s1, s2)
        return ratio
    
    @classmethod
    def diff(cls, s1, s2):
        """
        Uses the diff method (edit distance) to compare two strings.
        """
        return SM(None, str(s1).lower(), str(s2).lower()).ratio()
     
    @classmethod    
    def jaccard(cls, s1, s2):
        """
        Computes the jaccard index of two strings using tokenization.
        jaccard = |S1 \intersect S2| / |S1 \union S2|
        """
        S1, S2 = map(cls.get_tokens, [s1, s2])
        return float(len(S1.intersection(S2))) / len(S1.union(S2))
        
    @classmethod
    def max_all(cls, s1, s2):
        """
        Returns the maximum of all the comparison methods.
        Since max_membership is strictly >= jaccard, omits jaccard.
        
        Needs to be updated if/when new methods are added.
        """
        methods = [cls.jaccard, cls.diff]
        return max([f(s1, s2) for f in methods])
        
    @classmethod
    def get_tokens(cls, s):
        """
        Gets token set of a string (cleans it first).
        """
        return set(clean_string(s).split())
        
def clean_string(s):
    """
    Removes punctuation and case.
    """
    clean = remove_punctuation(str(s).lower().strip())
    return clean
    
def remove_punctuation(s):
    """
    Removes the punctuation from a string.  API is different for unicode.
    """
    if type(s)!=str and type(s)!=unicode:
        return s
        
    convert_ascii = type(s)==str
    if convert_ascii:
        s = unicode(s, 'utf-8')

    no_punctuation = s.translate({ord(c):ord(' ') for c in string.punctuation})
    if convert_ascii:
        no_punctuation = no_punctuation.encode('utf-8')
        
    return no_punctuation
    
if __name__=='__main__':
    SC = StringCompare