import sys
from pyspark.sql import SparkSession, functions, types
'''
how to run:
export PYSPARK_PYTHON=python3

export PATH=$PATH:/home/sarbjot/spark-2.4.3-bin-hadoop2.7/bin

export JAVA_HOME=/usr/lib/jvm/java-8-oracle/

spark-submit first_spark.py xyz-1 output
'''
spark = SparkSession.builder.appName('first Spark app').getOrCreate()
spark.sparkContext.setLogLevel('WARN')

assert sys.version_info >= (3, 5) # make sure we have Python 3.5+
assert spark.version >= '2.3' # make sure we have Spark 2.3+


schema = types.StructType([
    types.StructField('id', types.IntegerType()),
    types.StructField('x', types.FloatType()),
    types.StructField('y', types.FloatType()),
    types.StructField('z', types.FloatType()),
])


def main(in_directory, out_directory):
    # Read the data from the JSON files
    xyz = spark.read.json(in_directory, schema=schema)
    #xyz.show(); return

    # Create a DF with what we need: x, (soon y,) and id%10 which we'll aggregate by.
    with_bins = xyz.select(
        xyz['x'],
        # TODO: also the y values
        xyz['y'],
        (xyz['id'] % 10).alias('bin'),
    )
    #with_bins.show(); return

    # Aggregate by the bin number.
    grouped = with_bins.groupBy(with_bins['bin'])
    groups = grouped.agg(
        functions.sum(with_bins['x']),
        # TODO: output the average y value. Hint: avg
        functions.avg(with_bins['y']),
        functions.count('*'))

    # We know groups has <=10 rows, so it can safely be moved into two partitions.
    groups = groups.sort(groups['bin']).coalesce(2)
    groups.write.csv(out_directory, compression=None, mode='overwrite')


if __name__=='__main__':
    in_directory = sys.argv[1]
    out_directory = sys.argv[2]
    main(in_directory, out_directory)
