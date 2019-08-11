import sys
from pyspark.sql import SparkSession, functions, types

'''
how to run:
export PYSPARK_PYTHON=python3

export PATH=$PATH:/home/sarbjot/spark-2.4.3-bin-hadoop2.7/bin

export JAVA_HOME=/usr/lib/jvm/java-8-oracle/

spark-submit weather_etl.py weather-1 output
'''
spark = SparkSession.builder.appName('weather ETL').getOrCreate()
spark.sparkContext.setLogLevel('WARN')

assert sys.version_info >= (3, 5) # make sure we have Python 3.5+
assert spark.version >= '2.3' # make sure we have Spark 2.3+

observation_schema = types.StructType([
    types.StructField('station', types.StringType()),
    types.StructField('date', types.StringType()),
    types.StructField('observation', types.StringType()),
    types.StructField('value', types.IntegerType()),
    types.StructField('mflag', types.StringType()),
    types.StructField('qflag', types.StringType()),
    types.StructField('sflag', types.StringType()),
    types.StructField('obstime', types.StringType()),
])


def main():
    in_directory = sys.argv[1]
    out_directory = sys.argv[2]
    # 1. read the input direcotry of .csv.gz files
    weather = spark.read.csv(in_directory, schema=observation_schema)
    # 2. keep only the records we care about:
        # a. field qflag (quality flag) is null;
    weather1 = weather.filter(weather.qflag.isNull())
    #print(weather)
    #weather = weather.where(col("qflag").isNull())
        # b. the station starts with 'CA'
    weather = weather1.filter(weather1.station.startswith('CA'))
        # c. the observations is 'TMAX'
    weather = weather.filter(weather['observation'] == 'TMAX')
    # 3. Divide the temperature by 10 so it's acutally in C, and call thresulting column tmax. and     # 4. keep only the columns stations, data, and tmax

    cleaned_data = weather.select(weather['station'], weather['date'],(weather['value']/10).alias('tmax'))
    # 5. Write the result as a directory of JSON files GZIP compressed (in the Spark one-JSON- object-per-line way)
    #groups.write.csv(out_directory, compression=None, mode='overwrite')



    # TODO: finish here.

    cleaned_data.write.json(out_directory, compression='gzip', mode='overwrite')


if __name__=='__main__':
    main()
