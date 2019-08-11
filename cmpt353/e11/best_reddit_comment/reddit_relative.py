import sys
from pyspark.sql import SparkSession, functions, types

spark = SparkSession.builder.appName('reddit relative scores').getOrCreate()
spark.sparkContext.setLogLevel('WARN')

'''
how to run:
export PYSPARK_PYTHON=python3

export PATH=$PATH:/home/sarbjot/spark-2.4.3-bin-hadoop2.7/bin

export JAVA_HOME=/usr/lib/jvm/java-8-oracle/
local:
spark-submit reddit_relative.py reddit-1 output
cluster:
spark-submit --conf spark.dynamicAllocation.enabled=false --num-executors=8 reddit_relative.py /courses/353/reddit-3 output

'''

assert sys.version_info >= (3, 5) # make sure we have Python 3.5+
assert spark.version >= '2.3' # make sure we have Spark 2.3+

schema = types.StructType([ # commented-out fields won't be read
    #types.StructField('archived', types.BooleanType(), False),
    types.StructField('author', types.StringType(), False),
    #types.StructField('author_flair_css_class', types.StringType(), False),
    #types.StructField('author_flair_text', types.StringType(), False),
    #types.StructField('body', types.StringType(), False),
    #types.StructField('controversiality', types.LongType(), False),
    #types.StructField('created_utc', types.StringType(), False),
    #types.StructField('distinguished', types.StringType(), False),
    #types.StructField('downs', types.LongType(), False),
    #types.StructField('edited', types.StringType(), False),
    #types.StructField('gilded', types.LongType(), False),
    #types.StructField('id', types.StringType(), False),
    #types.StructField('link_id', types.StringType(), False),
    #types.StructField('name', types.StringType(), False),
    #types.StructField('parent_id', types.StringType(), True),
    #types.StructField('retrieved_on', types.LongType(), False),
    types.StructField('score', types.LongType(), False),
    #types.StructField('score_hidden', types.BooleanType(), False),
    types.StructField('subreddit', types.StringType(), False),
    #types.StructField('subreddit_id', types.StringType(), False),
    #types.StructField('ups', types.LongType(), False),
])


def main(in_directory, out_directory):
    comments = spark.read.json(in_directory, schema=schema)
    comments = comments.cache()
    # TODO
    #Calculate the average score for each subreddit, as before.
    groups = comments.groupBy(comments['subreddit'])
    #groups.show()
    averaged = groups.agg(functions.avg(comments['score']))
    averaged = averaged.cache()
    #averaged.show()

    #Exclude any subreddits with average score ≤0.
    averaged = averaged.where("avg(score)>=0")

    #Join the average score to the collection of all comments. Divide to get the relative score.
    averaged_b = functions.broadcast(averaged)
    #averaged_b = averaged
    joined = comments.join(averaged_b, on= 'subreddit')
    relative = joined.select(joined['subreddit'], joined['author'], (joined['score']/joined['avg(score)']).alias('relative'))
    relative = relative.cache()
    #relative.show()

    #Determine the max relative score for each subreddit.
    groups_r = relative.groupBy(relative['subreddit'])
    max_relative = groups_r.agg(functions.max(relative['relative']))
    max_relative = max_relative.select((max_relative['subreddit']).alias('max_sub'), (max_relative['max(relative)']).alias('max_relative') )
    #max_relative.show()
    #Join again to get the best comment on each subreddit: we need this step to get the author.
    max_relative = functions.broadcast(max_relative)
    max_relative_author = relative.join(max_relative,(max_relative.max_sub==relative.subreddit) & (max_relative.max_relative==relative.relative))
    best_author = max_relative_author.select(max_relative_author.subreddit,max_relative_author.author,(max_relative_author.relative).alias('rel_score'))
    best_author.show()
    best_author.write.json(out_directory, mode='overwrite')


if __name__=='__main__':
    in_directory = sys.argv[1]
    out_directory = sys.argv[2]
    main(in_directory, out_directory)
