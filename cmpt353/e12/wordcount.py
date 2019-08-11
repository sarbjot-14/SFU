
from pyspark.sql import SparkSession, functions, types, Row
import re
import math
import numpy
import sys
from pyspark.sql import SparkSession, functions, types
import string, re

spark = SparkSession.builder.appName('reddit relative scores').getOrCreate()
spark.sparkContext.setLogLevel('WARN')

'''
how to run:
export PYSPARK_PYTHON=python3

export PATH=$PATH:/home/sarbjot/spark-2.4.3-bin-hadoop2.7/bin

export JAVA_HOME=/usr/lib/jvm/java-8-oracle/
local:
spark-submit wordcount.py wordcount-1 output

'''

assert sys.version_info >= (3, 5) # make sure we have Python 3.5+
assert spark.version >= '2.3' # make sure we have Spark 2.3+



def main(in_directory, out_directory):
    lines = spark.read.text(in_directory)
    lines.show()
    wordbreak = r'[%s\s]+' % (re.escape(string.punctuation),)  # regex that matches spaces and/or punctuation

    words = lines.select(functions.explode(functions.split(lines.value, wordbreak)).alias("word"))
    words.show()
    words_lowered= words.select(functions.lower(words.word).alias('word'));
    words_lowered.show();
    grouped = words_lowered.groupBy(words_lowered['word'])
    word_count = grouped.agg(functions.count(words_lowered['word']).alias('count'))
    word_count.show()

    word_count = word_count.orderBy(functions.desc('count'),'word');
    word_count.show()

    filtered = word_count.filter(word_count.word!='')
    filtered.show()

    filtered.write.json(out_directory, mode='overwrite')


if __name__=='__main__':
    in_directory = sys.argv[1]
    out_directory = sys.argv[2]
    main(in_directory, out_directory)
