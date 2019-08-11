import sys
from pyspark.sql import SparkSession, functions, types, Row
import re
import math
import numpy
spark = SparkSession.builder.appName('correlate logs').getOrCreate()
spark.sparkContext.setLogLevel('WARN')

assert sys.version_info >= (3, 5) # make sure we have Python 3.5+
assert spark.version >= '2.3' # make sure we have Spark 2.3+
'''
how to run:
export PYSPARK_PYTHON=python3

export PATH=$PATH:/home/sarbjot/spark-2.4.3-bin-hadoop2.7/bin

export JAVA_HOME=/usr/lib/jvm/java-8-oracle/

spark-submit correlate_logs.py nasa-logs-1/nasa-logs-1

'''
line_re = re.compile(r"^(\S+) - - \[\S+ [+-]\d+\] \"[A-Z]+ \S+ HTTP/\d\.\d\" \d+ (\d+)$")


def line_to_row(line):
    """
    Take a logfile line and return a Row object with hostname and bytes transferred. Return None if regex doesn't match.
    """
    m = line_re.match(line)
    if m:
        return Row(host_name=m.group(1), bytes=m.group(2))
    else:
        #print("nothing")
        return None

def not_none(row):
    """
    Is this None? Hint: .filter() with it.
    """
    return row is not None


def create_row_rdd(in_directory):
    log_lines = spark.sparkContext.textFile(in_directory)

    rows = log_lines.map(line_to_row).filter(not_none)
    # TODO: return an RDD of Row() objects
    return rows


def main(in_directory):
    logs = spark.createDataFrame(create_row_rdd(in_directory))
    grouped_hostname =  logs.groupBy(logs['host_name'])

    count_sum = grouped_hostname.agg((functions.count(logs['host_name'])).alias('requests'), (functions.sum(logs['bytes'])).alias('bytes_transfered'))
    count_sum = count_sum.select(count_sum['requests'], count_sum['bytes_transfered'])
    #count_sum.show()
    # TODO: calculate r.

    n = count_sum.count()
    sum_x =  count_sum.groupBy().agg(functions.sum(count_sum['bytes_transfered'])).first()[0]
    sum_y = count_sum.groupBy().agg(functions.sum(count_sum['requests'])).first()[0]
    xy_squared = count_sum.select((count_sum['requests']*count_sum['requests']).alias('req2'), (count_sum['bytes_transfered']*count_sum['bytes_transfered']).alias('bytes2'))
    xy_squared.show()
    sum_x2 = xy_squared.groupBy().agg(functions.sum(xy_squared['bytes2'])).first()[0]
    #print(sum_x2)
    sum_y2 = xy_squared.groupBy().agg(functions.sum(xy_squared['req2'])).first()[0]
    #print(sum_y2)
    xy = count_sum.select((count_sum['requests']*count_sum['bytes_transfered']).alias('x_time_y'))
    sum_xy = xy.groupBy().agg(functions.sum(xy['x_time_y'])).first()[0]
    #print(sum_xy)
    r = 0
    r = (n*sum_xy-sum_x*sum_y)/ ( math.sqrt(n*sum_x2-(math.pow(sum_x,2))) * math.sqrt((n*sum_y2)-(math.pow(sum_y,2))) )
    print("r = %g\nr^2 = %g" % (r, r**2))
    #print("corr coef by python:")

    pandas_df = count_sum.toPandas()
    #print(pandas_df)
    print(pandas_df['requests'].corr(pandas_df['bytes_transfered']))


if __name__=='__main__':
    in_directory = sys.argv[1]
    main(in_directory)
