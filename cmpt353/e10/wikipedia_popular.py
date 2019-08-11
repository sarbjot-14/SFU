import sys
from pyspark.sql import SparkSession, functions, types
import re
from pyspark.sql import GroupedData


spark = SparkSession.builder.appName('reddit averages').getOrCreate()
spark.sparkContext.setLogLevel('WARN')

assert sys.version_info >= (3, 5) # make sure we have Python 3.5+
assert spark.version >= '2.3' # make sure we have Spark 2.3+

'''
how to run:
export PYSPARK_PYTHON=python3

export PATH=$PATH:/home/sarbjot/spark-2.4.3-bin-hadoop2.7/bin

export JAVA_HOME=/usr/lib/jvm/java-8-oracle/

spark-submit wikipedia_popular.py pagecounts-1 output-wiki
'''

wiki_schema = types.StructType([
    types.StructField('language', types.StringType()),
    types.StructField('title', types.StringType()),
    types.StructField('visits', types.IntegerType()),
    types.StructField('bytes', types.IntegerType()),

])


def main(in_directory, out_directory):
    #comments = spark.read.json(in_directory,schema=comments_schema ) # schema=comments_schema

    df = spark.read.csv(in_directory,header= False,schema=wiki_schema , sep= ' ').withColumn('filename', functions.input_file_name())
    ### get the hour column
    def convert_filename(path_name):
        #print(path_name)
        p = re.compile("[0-9]{8}-\w+(?:\.\w+)*$") #referenced : https://stackoverflow.com/questions/15340582/python-extract-pattern-matches
        result = p.search(path_name)
        return result.group(0)[0:11]  # pretend this is complex work.

    convert_filename_udf = functions.udf(convert_filename,returnType=types.StringType())

    df = df.select(
        convert_filename_udf(df['filename']).alias('hour'),
        df['language'],
        df['title'],
        df['visits'],
        df['bytes']
    )

    #df.show()
    # 1 filter en
    df= df.filter(df['language'] == 'en')
    # 2 exclude main page
    df= df.filter(df['title'] != 'Main_Page')

    # 3 exclude Special:
    df= df.filter( df['title'][0:8] != 'Special:')
    # re.match('$Special:', df['title'])
    print('first')
    df.show()
    df = df.cache()

    groups = df.groupBy('hour').agg(functions.max('visits'))
    groups = groups.select((groups['hour']).alias('h'),groups['max(visits)'])
    groups.show()
    joined = df.join(groups, (groups.h==df.hour) & (groups['max(visits)']==df.visits))
    joined.show()
    joined = joined.select(joined['h'],joined['title'],joined['max(visits)'] )
    joined = joined.sort(['h','title'])

    joined.show(30)
    joined.write.csv(out_directory , mode='overwrite')


if __name__=='__main__':
    in_directory = sys.argv[1]
    out_directory = sys.argv[2]
    main(in_directory, out_directory)
