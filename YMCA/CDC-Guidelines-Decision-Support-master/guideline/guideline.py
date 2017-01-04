
import glob
import pandas as pd
from StringIO import StringIO

def get_csv(e):
  #df = pd.DataFrame(e)
  file2 = open(e, 'r')
  txt=StringIO(file2.read())
  #print(txt)  
  df=pd.read_csv(txt,skiprows=[0,1,2],delimiter="\t")
  file2.close()
  
  df['Source']=e
  print(len(df.index))
  return df  
  
  

def guideline_csvreader():
  RES_DIR='.'
  df_files=pd.DataFrame(glob.glob(RES_DIR+'/*.txt'))
  df_guidline=pd.DataFrame()
  
  for i, row in df_files.iterrows():
    print(row[0]) 
    df_guidline=df_guidline.append(get_csv(row[0]))  
  
  print(len(df_guidline.index))

  # Map code
  
    

  df_full=pd.DataFrame()  
  df_full['System']=df_guidline['Code System Name']
  df_full['Code']=df_guidline['Concept Code']
  df_full['Type']=df_guidline['Source']
  df_full['Concept Name']=df_guidline['Concept Name']
  df_full['Weight']=''
  df_full['Code Value']=''
  df_full.to_csv('df_full.csv',index=True)
    
  return df_guidline
  
###

if __name__ == '__main__':
  df=guideline_csvreader()
  df.to_csv('full_guideline.csv',index=False)