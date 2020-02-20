# druid-hbase-indexing-test
Test hbase-indexer as druid extension

Druid uses hadoop for deep storage and the cluster is HA with an id of test1.

# HBase1.x

## 1. Native Batch Indexing

### Non-secure cluster
HBase1.x indexing was tested under HBase version 1.2.0-cdh5.11.2.  
The namenode of the cluster is HA and the id is nameservice1.

#### Table

##### Spec

```
{
  "type" : "index_parallel",
  "spec" : {
    "dataSchema" : {
      "dataSource" : "wikipedia-hbase-table-remote-nosecure",
      "timestampSpec": {
        "column": "time",
        "format": "iso"
      },
      "dimensionsSpec" : {
        "dimensions" : [
          "channel",
          "cityName",
          "comment",
          "countryIsoCode",
          "countryName",
          "isAnonymous",
          "isMinor",
          "isNew",
          "isRobot",
          "isUnpatrolled",
          "namespace",
          "page",
          "regionIsoCode",
          "regionName",
          "user"
        ]
      },
      "metricsSpec" : [
        {
          "type": "count",
          "name": "count"
        },
        {
          "type": "longSum",
          "name": "sum_added",
          "fieldName": "added",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_deleted",
          "fieldName": "deleted",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_delta",
          "fieldName": "delta",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_metroCode",
          "fieldName": "metroCode",
          "expression": null
        }
      ],
      "granularitySpec" : {
        "type" : "uniform",
        "segmentGranularity" : "day",
        "queryGranularity" : "hour",
        "intervals" : ["2015-09-12/2015-09-13"],
        "rollup" : true
      }
    },
    "ioConfig" : {
      "type" : "index_parallel",
      "inputSource" : {
        "type": "hbase",
        "connectionConfig": {
          "zookeeperQuorum": "<zookeeper-quorum>",
          "kerberosConfig": {
            "principal": null,
            "realm": null,
            "keytab": null
          }
        },
        "scanInfo": {
          "type": "table",
          "name": "default:wikipedia",
          "startKey": null,
          "endKey": null
        },
        "splitByRegion": true,
        "taskCount": 10,
        "hbaseClientConfig": {
          "hbase.client.scanner.timeout.period": 600000,
          "hbase.client.scanner.caching": 100,
          "hbase.client.scanner.max.result.size": 2097152
        }
      },
      "inputFormat": {
        "type": "hbase",
        "hbaseRowSpec": {
          "rowKeySpec": {
            "format": "delimiter",
            "delimiter": "|",
            "columns": [
              {
                "type": "string",
                "name": "salt"
              },
              {
                "type": "string",
                "name": "time"
              }
            ]
          },
          "columnSpec": [
            {
              "type": "string",
              "name": "A:channel",
              "mappingName": "channel"
            },
            {
              "type": "string",
              "name": "A:cityName",
              "mappingName": "cityName"
            },
            {
              "type": "string",
              "name": "A:comment",
              "mappingName": "comment"
            },
            {
              "type": "string",
              "name": "A:countryIsoCode",
              "mappingName": "countryIsoCode"
            },
            {
              "type": "string",
              "name": "A:countryName",
              "mappingName": "countryName"
            },
            {
              "type": "boolean",
              "name": "A:isAnonymous",
              "mappingName": "isAnonymous"
            },
            {
              "type": "boolean",
              "name": "A:isMinor",
              "mappingName": "isMinor"
            },
            {
              "type": "boolean",
              "name": "A:isNew",
              "mappingName": "isNew"
            },
            {
              "type": "boolean",
              "name": "A:isRobot",
              "mappingName": "isRobot"
            },
            {
              "type": "boolean",
              "name": "A:isUnpatrolled",
              "mappingName": "isUnpatrolled"
            },
            {
              "type": "string",
              "name": "A:namespace",
              "mappingName": "namespace"
            },
            {
              "type": "string",
              "name": "A:page",
              "mappingName": "page"
            },
            {
              "type": "string",
              "name": "A:regionIsoCode",
              "mappingName": "regionIsoCode"
            },
            {
              "type": "string",
              "name": "A:regionName",
              "mappingName": "regionName"
            },
            {
              "type": "string",
              "name": "A:user",
              "mappingName": "user"
            },
            {
              "type": "int",
              "name": "A:added",
              "mappingName": "added"
            },
            {
              "type": "int",
              "name": "A:deleted",
              "mappingName": "deleted"
            },
            {
              "type": "int",
              "name": "A:delta",
              "mappingName": "delta"
            },
            {
              "type": "int",
              "name": "A:metroCode",
              "mappingName": "metroCode"
            }
          ]
        }
      }
    },
    "tuningConfig" : {
      "type" : "index_parallel",
      "maxRowsPerSegment" : 5000000,
      "maxRowsInMemory" : 25000,
      "maxNumSubTasks": 20,
      "maxRetry": "1"
    }
  }
}
```

##### Requst

```
curl -X 'POST' -H 'Content-Type:application/json' -d @spec-files/hbase1/non-secure/wikipedia-hbase-table-remote-nosecure.json http://localhost:8888/druid/indexer/v1/task
```

#### Snapshot

##### Spec

```
{
  "type" : "index_parallel",
  "spec" : {
    "dataSchema" : {
      "dataSource" : "wikipedia-hbase-snapshot-remote-nosecure",
      "timestampSpec": {
        "column": "time",
        "format": "iso"
      },
      "dimensionsSpec" : {
        "dimensions" : [
          "channel",
          "cityName",
          "comment",
          "countryIsoCode",
          "countryName",
          "isAnonymous",
          "isMinor",
          "isNew",
          "isRobot",
          "isUnpatrolled",
          "namespace",
          "page",
          "regionIsoCode",
          "regionName",
          "user"
        ]
      },
      "metricsSpec" : [
        {
          "type": "count",
          "name": "count"
        },
        {
          "type": "longSum",
          "name": "sum_added",
          "fieldName": "added",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_deleted",
          "fieldName": "deleted",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_delta",
          "fieldName": "delta",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_metroCode",
          "fieldName": "metroCode",
          "expression": null
        }
      ],
      "granularitySpec" : {
        "type" : "uniform",
        "segmentGranularity" : "day",
        "queryGranularity" : "hour",
        "intervals" : ["2015-09-12/2015-09-13"],
        "rollup" : true
      }
    },
    "ioConfig" : {
      "type" : "index_parallel",
      "inputSource" : {
        "type": "hbase",
        "connectionConfig": {
          "kerberosConfig": {
            "principal": null,
            "realm": null,
            "keytab": null
          }
        },
        "scanInfo": {
          "type": "snapshot",
          "name": "wikipedia-snapshot",
          "startKey": null,
          "endKey": null,
          "filter": "A:metroCode > 0",
          "restoreDir": "hdfs://nameservice1/user/deploy"
        },
        "splitByRegion": true,
        "taskCount": 10,
        "hbaseClientConfig": {
          "hbase.rootdir": "hdfs://nameservice1/hbase",
          "ipc.client.fallback-to-simple-auth-allowed": "true",
          "dfs.nameservices": "test1, nameservice1",
          "dfs.internal.nameservices": "test1",
          "dfs.ha.namenodes.nameservice1": "namenode417,namenode444",
          "dfs.client.failover.proxy.provider.nameservice1": "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider",
          "dfs.namenode.rpc-address.nameservice1.namenode417": "<nn1>:8020",
          "dfs.namenode.rpc-address.nameservice1.namenode444": "<nn2>:8020",
          "hbase.client.scanner.timeout.period": 60000,
          "hbase.client.scanner.caching": 100,
          "hbase.client.scanner.max.result.size": 2097152
        }
      },
      "inputFormat": {
        "type": "hbase",
        "hbaseRowSpec": {
          "rowKeySpec": {
            "format": "delimiter",
            "delimiter": "|",
            "columns": [
              {
                "type": "string",
                "name": "salt"
              },
              {
                "type": "string",
                "name": "time"
              }
            ]
          },
          "columnSpec": [
            {
              "type": "string",
              "name": "A:channel",
              "mappingName": "channel"
            },
            {
              "type": "string",
              "name": "A:cityName",
              "mappingName": "cityName"
            },
            {
              "type": "string",
              "name": "A:comment",
              "mappingName": "comment"
            },
            {
              "type": "string",
              "name": "A:countryIsoCode",
              "mappingName": "countryIsoCode"
            },
            {
              "type": "string",
              "name": "A:countryName",
              "mappingName": "countryName"
            },
            {
              "type": "boolean",
              "name": "A:isAnonymous",
              "mappingName": "isAnonymous"
            },
            {
              "type": "boolean",
              "name": "A:isMinor",
              "mappingName": "isMinor"
            },
            {
              "type": "boolean",
              "name": "A:isNew",
              "mappingName": "isNew"
            },
            {
              "type": "boolean",
              "name": "A:isRobot",
              "mappingName": "isRobot"
            },
            {
              "type": "boolean",
              "name": "A:isUnpatrolled",
              "mappingName": "isUnpatrolled"
            },
            {
              "type": "string",
              "name": "A:namespace",
              "mappingName": "namespace"
            },
            {
              "type": "string",
              "name": "A:page",
              "mappingName": "page"
            },
            {
              "type": "string",
              "name": "A:regionIsoCode",
              "mappingName": "regionIsoCode"
            },
            {
              "type": "string",
              "name": "A:regionName",
              "mappingName": "regionName"
            },
            {
              "type": "string",
              "name": "A:user",
              "mappingName": "user"
            },
            {
              "type": "int",
              "name": "A:added",
              "mappingName": "added"
            },
            {
              "type": "int",
              "name": "A:deleted",
              "mappingName": "deleted"
            },
            {
              "type": "int",
              "name": "A:delta",
              "mappingName": "delta"
            },
            {
              "type": "int",
              "name": "A:metroCode",
              "mappingName": "metroCode"
            }
          ]
        }
      }
    },
    "tuningConfig" : {
      "type" : "index_parallel",
      "maxRowsPerSegment" : 5000000,
      "maxRowsInMemory" : 25000,
      "maxNumSubTasks": 20
    }
  }
}
```

##### Request

```
curl -X 'POST' -H 'Content-Type:application/json' -d @spec-files/hbase1/non-secure/wikipedia-hbase-snapshot-remote-nosecure.json http://localhost:8888/druid/indexer/v1/task
```

### Secure cluster
The security cluster was tested with HBase version 1.2.0-cdh5.9.0 and again the Namenode is HA and the id is dev-590-secure.

#### Table

##### Spec

```
{
  "type" : "index_parallel",
  "spec" : {
    "dataSchema" : {
      "dataSource" : "wikipedia-hbase-table-kerberos-remote-secure",
      "timestampSpec": {
        "column": "time",
        "format": "iso"
      },
      "dimensionsSpec" : {
        "dimensions" : [
          "channel",
          "cityName",
          "comment",
          "countryIsoCode",
          "countryName",
          "isAnonymous",
          "isMinor",
          "isNew",
          "isRobot",
          "isUnpatrolled",
          "namespace",
          "page",
          "regionIsoCode",
          "regionName",
          "user"
        ]
      },
      "metricsSpec" : [
        {
          "type": "count",
          "name": "count"
        },
        {
          "type": "longSum",
          "name": "sum_added",
          "fieldName": "added",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_deleted",
          "fieldName": "deleted",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_delta",
          "fieldName": "delta",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_metroCode",
          "fieldName": "metroCode",
          "expression": null
        }
      ],
      "granularitySpec" : {
        "type" : "uniform",
        "segmentGranularity" : "day",
        "queryGranularity" : "hour",
        "intervals" : ["2015-09-12/2015-09-13"],
        "rollup" : true
      }
    },
    "ioConfig" : {
      "type" : "index_parallel",
      "inputSource" : {
        "type": "hbase",
        "connectionConfig": {
          "zookeeperQuorum": "<zookeeper-quorum>",
          "kerberosConfig": {
            "principal": "<principal>",
            "keytab": "<keytab-filepath>" 
          }
        },
        "scanInfo": {
          "type": "table",
          "name": "default:wikipedia",
          "startKey": null,
          "endKey": null
        },
        "splitByRegion": true,
        "taskCount": 10,
        "hbaseClientConfig": {
          "hbase.client.scanner.timeout.period": 60000,
          "hbase.client.scanner.caching": 100,
          "hbase.client.scanner.max.result.size": 2097152
        }
      },
      "inputFormat": {
        "type": "hbase",
        "hbaseRowSpec": {
          "rowKeySpec": {
            "format": "delimiter",
            "delimiter": "|",
            "columns": [
              {
                "type": "string",
                "name": "salt"
              },
              {
                "type": "string",
                "name": "time"
              }
            ]
          },
          "columnSpec": [
            {
              "type": "string",
              "name": "A:channel",
              "mappingName": "channel"
            },
            {
              "type": "string",
              "name": "A:cityName",
              "mappingName": "cityName"
            },
            {
              "type": "string",
              "name": "A:comment",
              "mappingName": "comment"
            },
            {
              "type": "string",
              "name": "A:countryIsoCode",
              "mappingName": "countryIsoCode"
            },
            {
              "type": "string",
              "name": "A:countryName",
              "mappingName": "countryName"
            },
            {
              "type": "boolean",
              "name": "A:isAnonymous",
              "mappingName": "isAnonymous"
            },
            {
              "type": "boolean",
              "name": "A:isMinor",
              "mappingName": "isMinor"
            },
            {
              "type": "boolean",
              "name": "A:isNew",
              "mappingName": "isNew"
            },
            {
              "type": "boolean",
              "name": "A:isRobot",
              "mappingName": "isRobot"
            },
            {
              "type": "boolean",
              "name": "A:isUnpatrolled",
              "mappingName": "isUnpatrolled"
            },
            {
              "type": "string",
              "name": "A:namespace",
              "mappingName": "namespace"
            },
            {
              "type": "string",
              "name": "A:page",
              "mappingName": "page"
            },
            {
              "type": "string",
              "name": "A:regionIsoCode",
              "mappingName": "regionIsoCode"
            },
            {
              "type": "string",
              "name": "A:regionName",
              "mappingName": "regionName"
            },
            {
              "type": "string",
              "name": "A:user",
              "mappingName": "user"
            },
            {
              "type": "int",
              "name": "A:added",
              "mappingName": "added"
            },
            {
              "type": "int",
              "name": "A:deleted",
              "mappingName": "deleted"
            },
            {
              "type": "int",
              "name": "A:delta",
              "mappingName": "delta"
            },
            {
              "type": "int",
              "name": "A:metroCode",
              "mappingName": "metroCode"
            }
          ]
        }
      }
    },
    "tuningConfig" : {
      "type" : "index_parallel",
      "maxRowsPerSegment" : 5000000,
      "maxRowsInMemory" : 25000,
      "maxNumSubTasks": 20
    }
  }
}
```

##### Request

```
curl -X 'POST' -H 'Content-Type:application/json' -d @spec-files/hbase1/secure/wikipedia-hbase-table-kerberos-remote-secure.json http://localhost:8888/druid/indexer/v1/task
```

#### Snapshot

##### Spec

```
{
  "type" : "index_parallel",
  "spec" : {
    "dataSchema" : {
      "dataSource" : "wikipedia-hbase-snapshot-kerberos-remote-secure",
      "timestampSpec": {
        "column": "time",
        "format": "iso"
      },
      "dimensionsSpec" : {
        "dimensions" : [
          "channel",
          "cityName",
          "comment",
          "countryIsoCode",
          "countryName",
          "isAnonymous",
          "isMinor",
          "isNew",
          "isRobot",
          "isUnpatrolled",
          "namespace",
          "page",
          "regionIsoCode",
          "regionName",
          "user"
        ]
      },
      "metricsSpec" : [
        {
          "type": "count",
          "name": "count"
        },
        {
          "type": "longSum",
          "name": "sum_added",
          "fieldName": "added",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_deleted",
          "fieldName": "deleted",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_delta",
          "fieldName": "delta",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_metroCode",
          "fieldName": "metroCode",
          "expression": null
        }
      ],
      "granularitySpec" : {
        "type" : "uniform",
        "segmentGranularity" : "day",
        "queryGranularity" : "hour",
        "intervals" : ["2015-09-12/2015-09-13"],
        "rollup" : true
      }
    },
    "ioConfig" : {
      "type" : "index_parallel",
      "inputSource" : {
        "type": "hbase",
        "connectionConfig": {
          "kerberosConfig": {
            "principal": "<principal>",
            "keytab": "<keytab-filepath>" 
          }
        },
        "scanInfo": {
          "type": "snapshot",
          "name": "wikipedia-snapshot",
          "startKey": null,
          "endKey": null,
          "restoreDir": "hdfs://dev-590-secure/user/juke-mini666"
        },
        "splitByRegion": true,
        "taskCount": 10,
        "hbaseClientConfig": {
          "hbase.rootdir": "hdfs://dev-590-secure/hbase",
          "dfs.nameservices": "test1, dev-590-secure",
          "dfs.internal.nameservices": "test1",
          "dfs.ha.namenodes.dev-590-secure": "namenode124,namenode112",
          "dfs.client.failover.proxy.provider.dev-590-secure": "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider",
          "dfs.namenode.rpc-address.dev-590-secure.namenode124": "<nn1>:8020",
          "dfs.namenode.rpc-address.dev-590-secure.namenode112": "<nn2>:8020",
          "hbase.client.scanner.timeout.period": 60000,
          "hbase.client.scanner.caching": 100,
          "hbase.client.scanner.max.result.size": 2097152
        }
      },
      "inputFormat": {
        "type": "hbase",
        "hbaseRowSpec": {
          "rowKeySpec": {
            "format": "delimiter",
            "delimiter": "|",
            "columns": [
              {
                "type": "string",
                "name": "salt"
              },
              {
                "type": "string",
                "name": "time"
              }
            ]
          },
          "columnSpec": [
            {
              "type": "string",
              "name": "A:channel",
              "mappingName": "channel"
            },
            {
              "type": "string",
              "name": "A:cityName",
              "mappingName": "cityName"
            },
            {
              "type": "string",
              "name": "A:comment",
              "mappingName": "comment"
            },
            {
              "type": "string",
              "name": "A:countryIsoCode",
              "mappingName": "countryIsoCode"
            },
            {
              "type": "string",
              "name": "A:countryName",
              "mappingName": "countryName"
            },
            {
              "type": "boolean",
              "name": "A:isAnonymous",
              "mappingName": "isAnonymous"
            },
            {
              "type": "boolean",
              "name": "A:isMinor",
              "mappingName": "isMinor"
            },
            {
              "type": "boolean",
              "name": "A:isNew",
              "mappingName": "isNew"
            },
            {
              "type": "boolean",
              "name": "A:isRobot",
              "mappingName": "isRobot"
            },
            {
              "type": "boolean",
              "name": "A:isUnpatrolled",
              "mappingName": "isUnpatrolled"
            },
            {
              "type": "string",
              "name": "A:namespace",
              "mappingName": "namespace"
            },
            {
              "type": "string",
              "name": "A:page",
              "mappingName": "page"
            },
            {
              "type": "string",
              "name": "A:regionIsoCode",
              "mappingName": "regionIsoCode"
            },
            {
              "type": "string",
              "name": "A:regionName",
              "mappingName": "regionName"
            },
            {
              "type": "string",
              "name": "A:user",
              "mappingName": "user"
            },
            {
              "type": "int",
              "name": "A:added",
              "mappingName": "added"
            },
            {
              "type": "int",
              "name": "A:deleted",
              "mappingName": "deleted"
            },
            {
              "type": "int",
              "name": "A:delta",
              "mappingName": "delta"
            },
            {
              "type": "int",
              "name": "A:metroCode",
              "mappingName": "metroCode"
            }
          ]
        }
      }
    },
    "tuningConfig" : {
      "type" : "index_parallel",
      "maxRowsPerSegment" : 5000000,
      "maxRowsInMemory" : 25000,
      "maxNumSubTasks": 10
    }
  }
}
```

##### Request

```
curl -X 'POST' -H 'Content-Type:application/json' -d @spec-files/hbase1/secure/wikipedia-hbase-snapshot-kerberos-remote-secure.json http://localhost:8888/druid/indexer/v1/task
```

## 2. Hadoop Indexing

### Non-secure cluster

#### Table

##### Spec

```
{
  "type" : "index_hadoop_hbase",
  "spec" : {
    "dataSchema" : {
      "dataSource" : "wikipedia-hadoop-hbase-table-remote-nosecure",
      "parser" : {
        "type" : "hbase",
        "parseSpec" : {
          "format" : "hbase",
          "dimensionsSpec" : {
            "dimensions" : [
              "channel",
              "cityName",
              "comment",
              "countryIsoCode",
              "countryName",
              "isAnonymous",
              "isMinor",
              "isNew",
              "isRobot",
              "isUnpatrolled",
              "metroCode",
              "namespace",
              "page",
              "regionIsoCode",
              "regionName",
              "user"
            ]
          },
          "timestampSpec" : {
            "format" : "auto",
            "column" : "time"
          },
          "hbaseRowSpec": {
            "rowKeySpec": {
              "format": "delimiter",
              "delimiter": "|",
              "columns": [
                {
                  "type": "string",
                  "name": "salt"
                },
                {
                  "type": "string",
                  "name": "time"
                }
              ]
            },
            "columnSpec": [
              {
                "type": "string",
                "name": "A:channel",
                "mappingName": "channel"
              },
              {
                "type": "string",
                "name": "A:cityName",
                "mappingName": "cityName"
              },
              {
                "type": "string",
                "name": "A:comment",
                "mappingName": "comment"
              },
              {
                "type": "string",
                "name": "A:countryIsoCode",
                "mappingName": "countryIsoCode"
              },
              {
                "type": "string",
                "name": "A:countryName",
                "mappingName": "countryName"
              },
              {
                "type": "boolean",
                "name": "A:isAnonymous",
                "mappingName": "isAnonymous"
              },
              {
                "type": "boolean",
                "name": "A:isMinor",
                "mappingName": "isMinor"
              },
              {
                "type": "boolean",
                "name": "A:isNew",
                "mappingName": "isNew"
              },
              {
                "type": "boolean",
                "name": "A:isRobot",
                "mappingName": "isRobot"
              },
              {
                "type": "boolean",
                "name": "A:isUnpatrolled",
                "mappingName": "isUnpatrolled"
              },
              {
                "type": "string",
                "name": "A:namespace",
                "mappingName": "namespace"
              },
              {
                "type": "string",
                "name": "A:page",
                "mappingName": "page"
              },
              {
                "type": "string",
                "name": "A:regionIsoCode",
                "mappingName": "regionIsoCode"
              },
              {
                "type": "string",
                "name": "A:regionName",
                "mappingName": "regionName"
              },
              {
                "type": "string",
                "name": "A:user",
                "mappingName": "user"
              },
              {
                "type": "int",
                "name": "A:added",
                "mappingName": "added"
              },
              {
                "type": "int",
                "name": "A:deleted",
                "mappingName": "deleted"
              },
              {
                "type": "int",
                "name": "A:delta",
                "mappingName": "delta"
              },
              {
                "type": "int",
                "name": "A:metroCode",
                "mappingName": "metroCode"
              }
            ]
          }
        }
      },
      "metricsSpec" : [
        {
          "type": "count",
          "name": "count"
        },
        {
          "type": "longSum",
          "name": "sum_added",
          "fieldName": "added",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_deleted",
          "fieldName": "deleted",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_delta",
          "fieldName": "delta",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_metroCode",
          "fieldName": "metroCode",
          "expression": null
        }
      ],
      "granularitySpec" : {
        "type" : "uniform",
        "segmentGranularity" : "day",
        "queryGranularity" : "none",
        "intervals" : ["2015-09-12/2015-09-13"],
        "rollup" : true
      }
    },
    "ioConfig" : {
      "type" : "hadoop",
      "inputSpec" : {
        "type" : "hbase",
        "connectionConfig": {
          "zookeeperQuorum": "<zookeeper-quorum>",
          "kerberosConfig": {
            "principal": null,
            "realm": null,
            "keytab": null
          }
        },
        "scanInfo": {
          "type": "table",
          "name": "default:wikipedia",
          "startKey": null,
          "endKey": null
        },
        "hbaseClientConfig": {
          "hbase.rootdir": "/hbase",
          "hbase.client.scanner.timeout.period": 60000,
          "hbase.client.scanner.caching": 100,
          "hbase.client.scanner.max.result.size": 2097152
        }
      }
    },
    "tuningConfig" : {
      "type" : "hadoop",
      "partitionsSpec" : {
        "type" : "hashed",
        "targetPartitionSize" : 5000000
      },
      "forceExtendableShardSpecs" : true,
      "jobProperties" : {
        "mapreduce.job.classloader" : true
      }
    }
  },
  "hadoopDependencyCoordinates": ["org.apache.hadoop:hadoop-client:2.8.5"]
}
```

##### Request

```
curl -X 'POST' -H 'Content-Type:application/json' -d @spec-files/hbase1/non-secure/wikipedia-hadoop-hbase-table-remote-nosecure.json http://localhost:8888/druid/indexer/v1/task
```

#### Snapshot

##### Spec

```
{
  "type" : "index_hadoop_hbase",
  "spec" : {
    "dataSchema" : {
      "dataSource" : "wikipedia-hadoop-hbase-snapshot-remote-nosecure",
      "parser" : {
        "type" : "hbase",
        "parseSpec" : {
          "format" : "hbase",
          "dimensionsSpec" : {
            "dimensions" : [
              "channel",
              "cityName",
              "comment",
              "countryIsoCode",
              "countryName",
              "isAnonymous",
              "isMinor",
              "isNew",
              "isRobot",
              "isUnpatrolled",
              "metroCode",
              "namespace",
              "page",
              "regionIsoCode",
              "regionName",
              "user"
            ]
          },
          "timestampSpec" : {
            "format" : "auto",
            "column" : "time"
          },
          "hbaseRowSpec": {
            "rowKeySpec": {
              "format": "delimiter",
              "delimiter": "|",
              "columns": [
                {
                  "type": "string",
                  "name": "salt"
                },
                {
                  "type": "string",
                  "name": "time"
                }
              ]
            },
            "columnSpec": [
              {
                "type": "string",
                "name": "A:channel",
                "mappingName": "channel"
              },
              {
                "type": "string",
                "name": "A:cityName",
                "mappingName": "cityName"
              },
              {
                "type": "string",
                "name": "A:comment",
                "mappingName": "comment"
              },
              {
                "type": "string",
                "name": "A:countryIsoCode",
                "mappingName": "countryIsoCode"
              },
              {
                "type": "string",
                "name": "A:countryName",
                "mappingName": "countryName"
              },
              {
                "type": "boolean",
                "name": "A:isAnonymous",
                "mappingName": "isAnonymous"
              },
              {
                "type": "boolean",
                "name": "A:isMinor",
                "mappingName": "isMinor"
              },
              {
                "type": "boolean",
                "name": "A:isNew",
                "mappingName": "isNew"
              },
              {
                "type": "boolean",
                "name": "A:isRobot",
                "mappingName": "isRobot"
              },
              {
                "type": "boolean",
                "name": "A:isUnpatrolled",
                "mappingName": "isUnpatrolled"
              },
              {
                "type": "string",
                "name": "A:namespace",
                "mappingName": "namespace"
              },
              {
                "type": "string",
                "name": "A:page",
                "mappingName": "page"
              },
              {
                "type": "string",
                "name": "A:regionIsoCode",
                "mappingName": "regionIsoCode"
              },
              {
                "type": "string",
                "name": "A:regionName",
                "mappingName": "regionName"
              },
              {
                "type": "string",
                "name": "A:user",
                "mappingName": "user"
              },
              {
                "type": "int",
                "name": "A:added",
                "mappingName": "added"
              },
              {
                "type": "int",
                "name": "A:deleted",
                "mappingName": "deleted"
              },
              {
                "type": "int",
                "name": "A:delta",
                "mappingName": "delta"
              },
              {
                "type": "int",
                "name": "A:metroCode",
                "mappingName": "metroCode"
              }
            ]
          }
        }
      },
      "metricsSpec" : [
        {
          "type": "count",
          "name": "count"
        },
        {
          "type": "longSum",
          "name": "sum_added",
          "fieldName": "added",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_deleted",
          "fieldName": "deleted",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_delta",
          "fieldName": "delta",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_metroCode",
          "fieldName": "metroCode",
          "expression": null
        }
      ],
      "granularitySpec" : {
        "type" : "uniform",
        "segmentGranularity" : "day",
        "queryGranularity" : "none",
        "intervals" : ["2015-09-12/2015-09-13"],
        "rollup" : true
      }
    },
    "ioConfig" : {
      "type" : "hadoop",
      "inputSpec" : {
        "type" : "hbase",
        "connectionConfig": {
          "kerberosConfig": {
            "principal": null,
            "realm": null,
            "keytab": null
          }
        },
        "scanInfo": {
          "type": "snapshot",
          "name": "wikipedia-snapshot",
          "startKey": null,
          "endKey": null,
          "restoreDir": "hdfs://nameservice1/user/deploy"
        },
        "hbaseClientConfig": {
          "hbase.rootdir": "hdfs://nameservice1/hbase",
          "ipc.client.fallback-to-simple-auth-allowed": "true",
          "dfs.nameservices": "test1, nameservice1",
          "dfs.internal.nameservices": "test1",
          "dfs.ha.namenodes.nameservice1": "namenode417,namenode444",
          "dfs.client.failover.proxy.provider.nameservice1": "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider",
          "dfs.namenode.rpc-address.nameservice1.namenode417": "<nn1>:8020",
          "dfs.namenode.rpc-address.nameservice1.namenode444": "<nn2>:8020",
          "hbase.client.scanner.timeout.period": 60000,
          "hbase.client.scanner.caching": 100,
          "hbase.client.scanner.max.result.size": 2097152
        }
      }
    },
    "tuningConfig" : {
      "type" : "hadoop",
      "partitionsSpec" : {
        "type" : "hashed",
        "targetPartitionSize" : 5000000
      },
      "forceExtendableShardSpecs" : true,
      "jobProperties" : {
        "mapreduce.job.classloader" : true
      }
    }
  },
  "hadoopDependencyCoordinates": ["org.apache.hadoop:hadoop-client:2.8.5"]
}
```

##### Request

```
curl -X 'POST' -H 'Content-Type:application/json' -d @spec-files/hbase1/non-secure/wikipedia-hadoop-hbase-snapshot-remote-nosecure.json http://localhost:8888/druid/indexer/v1/task
```

### Secure cluster
#### Table

##### Spec

```
{
  "type" : "index_hadoop_hbase",
  "spec" : {
    "dataSchema" : {
      "dataSource" : "wikipedia-hadoop-hbase-table-kerberos-remote-secure",
      "parser" : {
        "type" : "hbase",
        "parseSpec" : {
          "format" : "hbase",
          "dimensionsSpec" : {
            "dimensions" : [
              "channel",
              "cityName",
              "comment",
              "countryIsoCode",
              "countryName",
              "isAnonymous",
              "isMinor",
              "isNew",
              "isRobot",
              "isUnpatrolled",
              "metroCode",
              "namespace",
              "page",
              "regionIsoCode",
              "regionName",
              "user"
            ]
          },
          "timestampSpec" : {
            "format" : "auto",
            "column" : "time"
          },
          "hbaseRowSpec": {
            "rowKeySpec": {
              "format": "delimiter",
              "delimiter": "|",
              "columns": [
                {
                  "type": "string",
                  "name": "salt"
                },
                {
                  "type": "string",
                  "name": "time"
                }
              ]
            },
            "columnSpec": [
              {
                "type": "string",
                "name": "A:channel",
                "mappingName": "channel"
              },
              {
                "type": "string",
                "name": "A:cityName",
                "mappingName": "cityName"
              },
              {
                "type": "string",
                "name": "A:comment",
                "mappingName": "comment"
              },
              {
                "type": "string",
                "name": "A:countryIsoCode",
                "mappingName": "countryIsoCode"
              },
              {
                "type": "string",
                "name": "A:countryName",
                "mappingName": "countryName"
              },
              {
                "type": "boolean",
                "name": "A:isAnonymous",
                "mappingName": "isAnonymous"
              },
              {
                "type": "boolean",
                "name": "A:isMinor",
                "mappingName": "isMinor"
              },
              {
                "type": "boolean",
                "name": "A:isNew",
                "mappingName": "isNew"
              },
              {
                "type": "boolean",
                "name": "A:isRobot",
                "mappingName": "isRobot"
              },
              {
                "type": "boolean",
                "name": "A:isUnpatrolled",
                "mappingName": "isUnpatrolled"
              },
              {
                "type": "string",
                "name": "A:namespace",
                "mappingName": "namespace"
              },
              {
                "type": "string",
                "name": "A:page",
                "mappingName": "page"
              },
              {
                "type": "string",
                "name": "A:regionIsoCode",
                "mappingName": "regionIsoCode"
              },
              {
                "type": "string",
                "name": "A:regionName",
                "mappingName": "regionName"
              },
              {
                "type": "string",
                "name": "A:user",
                "mappingName": "user"
              },
              {
                "type": "int",
                "name": "A:added",
                "mappingName": "added"
              },
              {
                "type": "int",
                "name": "A:deleted",
                "mappingName": "deleted"
              },
              {
                "type": "int",
                "name": "A:delta",
                "mappingName": "delta"
              },
              {
                "type": "int",
                "name": "A:metroCode",
                "mappingName": "metroCode"
              }
            ]
          }
        }
      },
      "metricsSpec" : [
        {
          "type": "count",
          "name": "count"
        },
        {
          "type": "longSum",
          "name": "sum_added",
          "fieldName": "added",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_deleted",
          "fieldName": "deleted",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_delta",
          "fieldName": "delta",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_metroCode",
          "fieldName": "metroCode",
          "expression": null
        }
      ],
      "granularitySpec" : {
        "type" : "uniform",
        "segmentGranularity" : "day",
        "queryGranularity" : "none",
        "intervals" : ["2015-09-12/2015-09-13"],
        "rollup" : true
      }
    },
    "ioConfig" : {
      "type" : "hadoop",
      "inputSpec" : {
        "type" : "hbase",
        "connectionConfig": {
          "kerberosConfig": {
            "principal": "<principal>",
            "keytab": "<keytab-filepath>"
          }
        },
        "scanInfo": {
          "type": "table",
          "name": "default:wikipedia",
          "startKey": null,
          "endKey": null
        }
      }
    },
    "tuningConfig" : {
      "type" : "hadoop",
      "partitionsSpec" : {
        "type" : "hashed",
        "targetPartitionSize" : 5000000
      },
      "forceExtendableShardSpecs" : true,
      "jobProperties" : {
        "mapreduce.job.classloader" : true,
        "yarn.app.mapreduce.am.log.level": "debug",
        "mapreduce.map.log.level": "debug",
        "hbase.zookeeper.quorum": "<zookeeper-quorum>",
        "hbase.security.authentication": "kerberos",
        "hbase.master.kerberos.principal": "hbase/_HOST@<realm>",
        "hbase.regionserver.kerberos.principal": "hbase/_HOST@<realm>"
      }
    }
  },
  "hadoopDependencyCoordinates": ["org.apache.hadoop:hadoop-client:2.8.5"]
}
```

##### Request

```
curl -X 'POST' -H 'Content-Type:application/json' -d @spec-files/hbase1/secure/wikipedia-hadoop-hbase-table-kerberos-remote-secure.json http://localhost:8888/druid/indexer/v1/task
```

#### Snapshot

##### Spec

```
{
  "type" : "index_hadoop_hbase",
  "spec" : {
    "dataSchema" : {
      "dataSource" : "wikipedia-hadoop-hbase-snapshot-kerberos-remote-secure",
      "parser" : {
        "type" : "hbase",
        "parseSpec" : {
          "format" : "hbase",
          "dimensionsSpec" : {
            "dimensions" : [
              "channel",
              "cityName",
              "comment",
              "countryIsoCode",
              "countryName",
              "isAnonymous",
              "isMinor",
              "isNew",
              "isRobot",
              "isUnpatrolled",
              "metroCode",
              "namespace",
              "page",
              "regionIsoCode",
              "regionName",
              "user"
            ]
          },
          "timestampSpec" : {
            "format" : "auto",
            "column" : "time"
          },
          "hbaseRowSpec": {
            "rowKeySpec": {
              "format": "delimiter",
              "delimiter": "|",
              "columns": [
                {
                  "type": "string",
                  "name": "salt"
                },
                {
                  "type": "string",
                  "name": "time"
                }
              ]
            },
            "columnSpec": [
              {
                "type": "string",
                "name": "A:channel",
                "mappingName": "channel"
              },
              {
                "type": "string",
                "name": "A:cityName",
                "mappingName": "cityName"
              },
              {
                "type": "string",
                "name": "A:comment",
                "mappingName": "comment"
              },
              {
                "type": "string",
                "name": "A:countryIsoCode",
                "mappingName": "countryIsoCode"
              },
              {
                "type": "string",
                "name": "A:countryName",
                "mappingName": "countryName"
              },
              {
                "type": "boolean",
                "name": "A:isAnonymous",
                "mappingName": "isAnonymous"
              },
              {
                "type": "boolean",
                "name": "A:isMinor",
                "mappingName": "isMinor"
              },
              {
                "type": "boolean",
                "name": "A:isNew",
                "mappingName": "isNew"
              },
              {
                "type": "boolean",
                "name": "A:isRobot",
                "mappingName": "isRobot"
              },
              {
                "type": "boolean",
                "name": "A:isUnpatrolled",
                "mappingName": "isUnpatrolled"
              },
              {
                "type": "string",
                "name": "A:namespace",
                "mappingName": "namespace"
              },
              {
                "type": "string",
                "name": "A:page",
                "mappingName": "page"
              },
              {
                "type": "string",
                "name": "A:regionIsoCode",
                "mappingName": "regionIsoCode"
              },
              {
                "type": "string",
                "name": "A:regionName",
                "mappingName": "regionName"
              },
              {
                "type": "string",
                "name": "A:user",
                "mappingName": "user"
              },
              {
                "type": "int",
                "name": "A:added",
                "mappingName": "added"
              },
              {
                "type": "int",
                "name": "A:deleted",
                "mappingName": "deleted"
              },
              {
                "type": "int",
                "name": "A:delta",
                "mappingName": "delta"
              },
              {
                "type": "int",
                "name": "A:metroCode",
                "mappingName": "metroCode"
              }
            ]
          }
        }
      },
      "metricsSpec" : [
        {
          "type": "count",
          "name": "count"
        },
        {
          "type": "longSum",
          "name": "sum_added",
          "fieldName": "added",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_deleted",
          "fieldName": "deleted",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_delta",
          "fieldName": "delta",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_metroCode",
          "fieldName": "metroCode",
          "expression": null
        }
      ],
      "granularitySpec" : {
        "type" : "uniform",
        "segmentGranularity" : "day",
        "queryGranularity" : "none",
        "intervals" : ["2015-09-12/2015-09-13"],
        "rollup" : true
      }
    },
    "ioConfig" : {
      "type" : "hadoop",
      "inputSpec" : {
        "type" : "hbase",
        "connectionConfig": {
          "zookeeperQuorum": "<zookeeper-quorum>",
          "kerberosConfig": {
            "principal": "<principal>",
            "keytab": "<keytab-filepath>"
          }
        },
        "scanInfo": {
          "type": "snapshot",
          "name": "wikipedia-snapshot",
          "startKey": null,
          "endKey": null,
          "restoreDir": "hdfs://dev-590-secure/user/juke-mini666"
        },
        "hbaseClientConfig": {
          "hbase.rootdir": "hdfs://dev-590-secure/hbase",
          "hbase.client.scanner.timeout.period": 60000,
          "hbase.client.scanner.caching": 100,
          "hbase.client.scanner.max.result.size": 2097152
        }
      }
    },
    "tuningConfig" : {
      "type" : "hadoop",
      "partitionsSpec" : {
        "type" : "hashed",
        "targetPartitionSize" : 5000000
      },
      "forceExtendableShardSpecs" : true,
      "jobProperties" : {
        "mapreduce.job.classloader" : true,
        "mapreduce.job.hdfs-servers": "hdfs://dev-590-secure,hdfs://test1",
        "mapreduce.job.hdfs-servers.token-renewal.exclude": "dev-590-secure",
        "dfs.nameservices": "test1, dev-590-secure",
        "dfs.internal.nameservices": "test1",
        "dfs.ha.namenodes.dev-590-secure": "namenode124,namenode112",
        "dfs.client.failover.proxy.provider.dev-590-secure": "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider",
        "dfs.namenode.rpc-address.dev-590-secure.namenode124": "<nn1>:8020",
        "dfs.namenode.rpc-address.dev-590-secure.namenode112": "<nn2>:8020"
      }
    }
  },
  "hadoopDependencyCoordinates": ["org.apache.hadoop:hadoop-client:2.8.5"]
}
```

##### Request

```
curl -X 'POST' -H 'Content-Type:application/json' -d @spec-files/hbase1/secure/wikipedia-hadoop-hbase-snapshot-kerberos-remote-secure.json http://localhost:8888/druid/indexer/v1/task
```

# HBase2.x


## 1. Native Batch Indexing

### Non-secure cluster
HBase2.x indexing was tested with Apache HBase version 2.1.4 and the id of the HA namenode is juke-ns-test.

#### Table

##### spec

```
{
  "type" : "index_parallel",
  "spec" : {
    "dataSchema" : {
      "dataSource" : "wikipedia-hbase2-table-remote-nosecure",
      "parser" : {
        "type" : "hbase2",
        "parseSpec" : {
          "format" : "hbase2",
          "timestampSpec": {
            "column": "time",
            "format": "iso"
          },
          "dimensionsSpec" : {
            "dimensions" : [
              "channel",
              "cityName",
              "comment",
              "countryIsoCode",
              "countryName",
              "isAnonymous",
              "isMinor",
              "isNew",
              "isRobot",
              "isUnpatrolled",
              "namespace",
              "page",
              "regionIsoCode",
              "regionName",
              "user"
            ]
          },
          "hbaseRowSpec": {
            "rowKeySpec": {
              "format": "delimiter",
              "delimiter": "|",
              "columns": [
                {
                  "type": "string",
                  "name": "salt"
                },
                {
                  "type": "string",
                  "name": "time"
                }
              ]
            },
            "columnSpec": [
              {
                "type": "string",
                "name": "A:channel",
                "mappingName": "channel"
              },
              {
                "type": "string",
                "name": "A:cityName",
                "mappingName": "cityName"
              },
              {
                "type": "string",
                "name": "A:comment",
                "mappingName": "comment"
              },
              {
                "type": "string",
                "name": "A:countryIsoCode",
                "mappingName": "countryIsoCode"
              },
              {
                "type": "string",
                "name": "A:countryName",
                "mappingName": "countryName"
              },
              {
                "type": "boolean",
                "name": "A:isAnonymous",
                "mappingName": "isAnonymous"
              },
              {
                "type": "boolean",
                "name": "A:isMinor",
                "mappingName": "isMinor"
              },
              {
                "type": "boolean",
                "name": "A:isNew",
                "mappingName": "isNew"
              },
              {
                "type": "boolean",
                "name": "A:isRobot",
                "mappingName": "isRobot"
              },
              {
                "type": "boolean",
                "name": "A:isUnpatrolled",
                "mappingName": "isUnpatrolled"
              },
              {
                "type": "string",
                "name": "A:namespace",
                "mappingName": "namespace"
              },
              {
                "type": "string",
                "name": "A:page",
                "mappingName": "page"
              },
              {
                "type": "string",
                "name": "A:regionIsoCode",
                "mappingName": "regionIsoCode"
              },
              {
                "type": "string",
                "name": "A:regionName",
                "mappingName": "regionName"
              },
              {
                "type": "string",
                "name": "A:user",
                "mappingName": "user"
              },
              {
                "type": "int",
                "name": "A:added",
                "mappingName": "added"
              },
              {
                "type": "int",
                "name": "A:deleted",
                "mappingName": "deleted"
              },
              {
                "type": "int",
                "name": "A:delta",
                "mappingName": "delta"
              },
              {
                "type": "int",
                "name": "A:metroCode",
                "mappingName": "metroCode"
              }
            ]
          }
        }
      },
      "metricsSpec" : [
        {
          "type": "count",
          "name": "count"
        },
        {
          "type": "longSum",
          "name": "sum_added",
          "fieldName": "added",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_deleted",
          "fieldName": "deleted",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_delta",
          "fieldName": "delta",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_metroCode",
          "fieldName": "metroCode",
          "expression": null
        }
      ],
      "granularitySpec" : {
        "type" : "uniform",
        "segmentGranularity" : "day",
        "queryGranularity" : "hour",
        "intervals" : ["2015-09-12/2015-09-13"],
        "rollup" : true
      }
    },
    "ioConfig" : {
      "type" : "index_parallel",
      "firehose" : {
        "type": "hbase2",
        "connectionConfig": {
          "zookeeperQuorum": "<zookeeper-quorum>",
          "kerberosConfig": {
            "principal": null,
            "realm": null,
            "keytab": null
          }
        },
        "scanInfo": {
          "type": "table",
          "name": "default:wikipedia",
          "startKey": null,
          "endKey": null
        },
        "splitByRegion": true,
        "taskCount": 10,
        "hbaseClientConfig": {
          "hbase.client.scanner.timeout.period": 600000,
          "hbase.client.scanner.caching": 100,
          "hbase.client.scanner.max.result.size": 2097152
        }
      },
      "appendToExisting" : false
    },
    "tuningConfig" : {
      "type" : "index_parallel",
      "maxRowsPerSegment" : 5000000,
      "maxRowsInMemory" : 25000,
      "maxNumSubTasks": 20,
      "maxRetry": "1"
    }
  }
}
```

##### Request

```
curl -X 'POST' -H 'Content-Type:application/json' -d @spec-files/hbase2/non-secure/wikipedia-hbase2-table-remote-nosecure.json http://localhost:8888/druid/indexer/v1/task
```

#### Snapshot

##### Spec

```
{
  "type" : "index_parallel",
  "spec" : {
    "dataSchema" : {
      "dataSource" : "wikipedia-hbase2-snapshot-remote-nosecure",
      "parser" : {
        "type" : "hbase2",
        "parseSpec" : {
          "format" : "hbase2",
          "timestampSpec": {
            "column": "time",
            "format": "iso"
          },
          "dimensionsSpec" : {
            "dimensions" : [
              "channel",
              "cityName",
              "comment",
              "countryIsoCode",
              "countryName",
              "isAnonymous",
              "isMinor",
              "isNew",
              "isRobot",
              "isUnpatrolled",
              "namespace",
              "page",
              "regionIsoCode",
              "regionName",
              "user"
            ]
          },
          "hbaseRowSpec": {
            "rowKeySpec": {
              "format": "delimiter",
              "delimiter": "|",
              "columns": [
                {
                  "type": "string",
                  "name": "salt"
                },
                {
                  "type": "string",
                  "name": "time"
                }
              ]
            },
            "columnSpec": [
              {
                "type": "string",
                "name": "A:channel",
                "mappingName": "channel"
              },
              {
                "type": "string",
                "name": "A:cityName",
                "mappingName": "cityName"
              },
              {
                "type": "string",
                "name": "A:comment",
                "mappingName": "comment"
              },
              {
                "type": "string",
                "name": "A:countryIsoCode",
                "mappingName": "countryIsoCode"
              },
              {
                "type": "string",
                "name": "A:countryName",
                "mappingName": "countryName"
              },
              {
                "type": "boolean",
                "name": "A:isAnonymous",
                "mappingName": "isAnonymous"
              },
              {
                "type": "boolean",
                "name": "A:isMinor",
                "mappingName": "isMinor"
              },
              {
                "type": "boolean",
                "name": "A:isNew",
                "mappingName": "isNew"
              },
              {
                "type": "boolean",
                "name": "A:isRobot",
                "mappingName": "isRobot"
              },
              {
                "type": "boolean",
                "name": "A:isUnpatrolled",
                "mappingName": "isUnpatrolled"
              },
              {
                "type": "string",
                "name": "A:namespace",
                "mappingName": "namespace"
              },
              {
                "type": "string",
                "name": "A:page",
                "mappingName": "page"
              },
              {
                "type": "string",
                "name": "A:regionIsoCode",
                "mappingName": "regionIsoCode"
              },
              {
                "type": "string",
                "name": "A:regionName",
                "mappingName": "regionName"
              },
              {
                "type": "string",
                "name": "A:user",
                "mappingName": "user"
              },
              {
                "type": "int",
                "name": "A:added",
                "mappingName": "added"
              },
              {
                "type": "int",
                "name": "A:deleted",
                "mappingName": "deleted"
              },
              {
                "type": "int",
                "name": "A:delta",
                "mappingName": "delta"
              },
              {
                "type": "int",
                "name": "A:metroCode",
                "mappingName": "metroCode"
              }
            ]
          }
        }
      },
      "metricsSpec" : [
        {
          "type": "count",
          "name": "count"
        },
        {
          "type": "longSum",
          "name": "sum_added",
          "fieldName": "added",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_deleted",
          "fieldName": "deleted",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_delta",
          "fieldName": "delta",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_metroCode",
          "fieldName": "metroCode",
          "expression": null
        }
      ],
      "granularitySpec" : {
        "type" : "uniform",
        "segmentGranularity" : "day",
        "queryGranularity" : "hour",
        "intervals" : ["2015-09-12/2015-09-13"],
        "rollup" : true
      }
    },
    "ioConfig" : {
      "type" : "index_parallel",
      "firehose" : {
        "type": "hbase2",
        "connectionConfig": {
          "kerberosConfig": {
            "principal": null,
            "realm": null,
            "keytab": null
          }
        },
        "scanInfo": {
          "type": "snapshot",
          "name": "wikipedia-snapshot",
          "startKey": null,
          "endKey": null,
          "restoreDir": "hdfs://juke-ns-test/user/deploy"
        },
        "splitByRegion": true,
        "taskCount": 10,
        "hbaseClientConfig": {
          "hbase.rootdir": "hdfs://juke-ns-test/hbase",
          "ipc.client.fallback-to-simple-auth-allowed": "true",
          "dfs.nameservices": "test1, juke-ns-test",
          "dfs.internal.nameservices": "test1",
          "dfs.ha.namenodes.juke-ns-test": "nn1,nn2",
          "dfs.client.failover.proxy.provider.juke-ns-test": "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider",
          "dfs.namenode.rpc-address.juke-ns-test.nn1": "<nn1>:8020",
          "dfs.namenode.rpc-address.juke-ns-test.nn2": "<nn2>:8020",
          "hbase.client.scanner.timeout.period": 60000,
          "hbase.client.scanner.caching": 100,
          "hbase.client.scanner.max.result.size": 2097152
        }
      },
      "appendToExisting" : false
    },
    "tuningConfig" : {
      "type" : "index_parallel",
      "maxRowsPerSegment" : 5000000,
      "maxRowsInMemory" : 25000,
      "maxNumSubTasks": 20
    }
  }
}
```

##### Request

```
curl -X 'POST' -H 'Content-Type:application/json' -d @spec-files/hbase2/non-secure/wikipedia-hbase2-snapshot-remote-nosecure.json http://localhost:8888/druid/indexer/v1/task
```

### Secure cluster
In a secure cluster, HBase2.x indexing was tested identically with Apache HBase2.1.4, not namenode HA.

#### Table

##### Spec

```
{
  "type" : "index_parallel",
  "spec" : {
    "dataSchema" : {
      "dataSource" : "wikipedia-hbase2-table-kerberos-remote-secure",
      "parser" : {
        "type" : "hbase2",
        "parseSpec" : {
          "format" : "hbase2",
          "timestampSpec": {
            "column": "time",
            "format": "iso"
          },
          "dimensionsSpec" : {
            "dimensions" : [
              "channel",
              "cityName",
              "comment",
              "countryIsoCode",
              "countryName",
              "isAnonymous",
              "isMinor",
              "isNew",
              "isRobot",
              "isUnpatrolled",
              "namespace",
              "page",
              "regionIsoCode",
              "regionName",
              "user"
            ]
          },
          "hbaseRowSpec": {
            "rowKeySpec": {
              "format": "delimiter",
              "delimiter": "|",
              "columns": [
                {
                  "type": "string",
                  "name": "salt"
                },
                {
                  "type": "string",
                  "name": "time"
                }
              ]
            },
            "columnSpec": [
              {
                "type": "string",
                "name": "A:channel",
                "mappingName": "channel"
              },
              {
                "type": "string",
                "name": "A:cityName",
                "mappingName": "cityName"
              },
              {
                "type": "string",
                "name": "A:comment",
                "mappingName": "comment"
              },
              {
                "type": "string",
                "name": "A:countryIsoCode",
                "mappingName": "countryIsoCode"
              },
              {
                "type": "string",
                "name": "A:countryName",
                "mappingName": "countryName"
              },
              {
                "type": "boolean",
                "name": "A:isAnonymous",
                "mappingName": "isAnonymous"
              },
              {
                "type": "boolean",
                "name": "A:isMinor",
                "mappingName": "isMinor"
              },
              {
                "type": "boolean",
                "name": "A:isNew",
                "mappingName": "isNew"
              },
              {
                "type": "boolean",
                "name": "A:isRobot",
                "mappingName": "isRobot"
              },
              {
                "type": "boolean",
                "name": "A:isUnpatrolled",
                "mappingName": "isUnpatrolled"
              },
              {
                "type": "string",
                "name": "A:namespace",
                "mappingName": "namespace"
              },
              {
                "type": "string",
                "name": "A:page",
                "mappingName": "page"
              },
              {
                "type": "string",
                "name": "A:regionIsoCode",
                "mappingName": "regionIsoCode"
              },
              {
                "type": "string",
                "name": "A:regionName",
                "mappingName": "regionName"
              },
              {
                "type": "string",
                "name": "A:user",
                "mappingName": "user"
              },
              {
                "type": "int",
                "name": "A:added",
                "mappingName": "added"
              },
              {
                "type": "int",
                "name": "A:deleted",
                "mappingName": "deleted"
              },
              {
                "type": "int",
                "name": "A:delta",
                "mappingName": "delta"
              },
              {
                "type": "int",
                "name": "A:metroCode",
                "mappingName": "metroCode"
              }
            ]
          }
        }
      },
      "metricsSpec" : [
        {
          "type": "count",
          "name": "count"
        },
        {
          "type": "longSum",
          "name": "sum_added",
          "fieldName": "added",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_deleted",
          "fieldName": "deleted",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_delta",
          "fieldName": "delta",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_metroCode",
          "fieldName": "metroCode",
          "expression": null
        }
      ],
      "granularitySpec" : {
        "type" : "uniform",
        "segmentGranularity" : "day",
        "queryGranularity" : "hour",
        "intervals" : ["2015-09-12/2015-09-13"],
        "rollup" : true
      }
    },
    "ioConfig" : {
      "type" : "index_parallel",
      "firehose" : {
        "type": "hbase2",
        "connectionConfig": {
          "zookeeperQuorum": "hbase-ambari-test7.dakao.io",
          "kerberosConfig": {
            "principal": "<principal>",
            "keytab": "<keytab-filepath>"
          }
        },
        "scanInfo": {
          "type": "table",
          "name": "default:wikipedia",
          "startKey": null,
          "endKey": null
        },
        "splitByRegion": true,
        "taskCount": 10,
        "hbaseClientConfig": {
          "hbase.client.scanner.timeout.period": 60000,
          "hbase.client.scanner.caching": 100,
          "hbase.client.scanner.max.result.size": 2097152
        }
      },
      "appendToExisting" : false
    },
    "tuningConfig" : {
      "type" : "index_parallel",
      "maxRowsPerSegment" : 5000000,
      "maxRowsInMemory" : 25000,
      "maxNumSubTasks": 20
    }
  }
}
```

##### Request

```
curl -X 'POST' -H 'Content-Type:application/json' -d @spec-files/hbase2/secure/wikipedia-hbase2-table-kerberos-remote-secure.json http://localhost:8888/druid/indexer/v1/task
```

#### Snapshot

##### Spec

```
{
  "type" : "index_parallel",
  "spec" : {
    "dataSchema" : {
      "dataSource" : "wikipedia-hbase2-snapshot-kerberos-remote-secure",
      "parser" : {
        "type" : "hbase2",
        "parseSpec" : {
          "format" : "hbase2",
          "timestampSpec": {
            "column": "time",
            "format": "iso"
          },
          "dimensionsSpec" : {
            "dimensions" : [
              "channel",
              "cityName",
              "comment",
              "countryIsoCode",
              "countryName",
              "isAnonymous",
              "isMinor",
              "isNew",
              "isRobot",
              "isUnpatrolled",
              "namespace",
              "page",
              "regionIsoCode",
              "regionName",
              "user"
            ]
          },
          "hbaseRowSpec": {
            "rowKeySpec": {
              "format": "delimiter",
              "delimiter": "|",
              "columns": [
                {
                  "type": "string",
                  "name": "salt"
                },
                {
                  "type": "string",
                  "name": "time"
                }
              ]
            },
            "columnSpec": [
              {
                "type": "string",
                "name": "A:channel",
                "mappingName": "channel"
              },
              {
                "type": "string",
                "name": "A:cityName",
                "mappingName": "cityName"
              },
              {
                "type": "string",
                "name": "A:comment",
                "mappingName": "comment"
              },
              {
                "type": "string",
                "name": "A:countryIsoCode",
                "mappingName": "countryIsoCode"
              },
              {
                "type": "string",
                "name": "A:countryName",
                "mappingName": "countryName"
              },
              {
                "type": "boolean",
                "name": "A:isAnonymous",
                "mappingName": "isAnonymous"
              },
              {
                "type": "boolean",
                "name": "A:isMinor",
                "mappingName": "isMinor"
              },
              {
                "type": "boolean",
                "name": "A:isNew",
                "mappingName": "isNew"
              },
              {
                "type": "boolean",
                "name": "A:isRobot",
                "mappingName": "isRobot"
              },
              {
                "type": "boolean",
                "name": "A:isUnpatrolled",
                "mappingName": "isUnpatrolled"
              },
              {
                "type": "string",
                "name": "A:namespace",
                "mappingName": "namespace"
              },
              {
                "type": "string",
                "name": "A:page",
                "mappingName": "page"
              },
              {
                "type": "string",
                "name": "A:regionIsoCode",
                "mappingName": "regionIsoCode"
              },
              {
                "type": "string",
                "name": "A:regionName",
                "mappingName": "regionName"
              },
              {
                "type": "string",
                "name": "A:user",
                "mappingName": "user"
              },
              {
                "type": "int",
                "name": "A:added",
                "mappingName": "added"
              },
              {
                "type": "int",
                "name": "A:deleted",
                "mappingName": "deleted"
              },
              {
                "type": "int",
                "name": "A:delta",
                "mappingName": "delta"
              },
              {
                "type": "int",
                "name": "A:metroCode",
                "mappingName": "metroCode"
              }
            ]
          }
        }
      },
      "metricsSpec" : [
        {
          "type": "count",
          "name": "count"
        },
        {
          "type": "longSum",
          "name": "sum_added",
          "fieldName": "added",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_deleted",
          "fieldName": "deleted",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_delta",
          "fieldName": "delta",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_metroCode",
          "fieldName": "metroCode",
          "expression": null
        }
      ],
      "granularitySpec" : {
        "type" : "uniform",
        "segmentGranularity" : "day",
        "queryGranularity" : "hour",
        "intervals" : ["2015-09-12/2015-09-13"],
        "rollup" : true
      }
    },
    "ioConfig" : {
      "type" : "index_parallel",
      "firehose" : {
        "type": "hbase2",
        "connectionConfig": {
          "kerberosConfig": {
            "principal": "<principal>",
            "keytab": "<keytab-filepath>"
          }
        },
        "scanInfo": {
          "type": "snapshot",
          "name": "wikipedia-snapshot",
          "startKey": null,
          "endKey": null,
          "restoreDir": "hdfs://hbase-ambari-test7.dakao.io/user/juke-mini666"
        },
        "splitByRegion": true,
        "taskCount": 10,
        "hbaseClientConfig": {
          "dfs.namenode.kerberos.principal": "nn/_HOST@<realm>",
          "hbase.rootdir": "hdfs://hbase-ambari-test7.dakao.io/hbase",
          "hbase.client.scanner.timeout.period": 6000,
          "hbase.client.scanner.caching": 100,
          "hbase.client.scanner.max.result.size": 2097152
        }
      },
      "appendToExisting" : false
    },
    "tuningConfig" : {
      "type" : "index_parallel",
      "maxRowsPerSegment" : 5000000,
      "maxRowsInMemory" : 25000,
      "maxNumSubTasks": 10
    }
  }
}
```

##### Request

```
curl -X 'POST' -H 'Content-Type:application/json' -d @spec-files/hbase2/secure/wikipedia-hbase2-snapshot-kerberos-remote-secure.json http://localhost:8888/druid/indexer/v1/task
```

## 2. Hadoop Indexing

### Non-secure cluster

#### Table

##### Spec

```
{
  "type" : "index_hadoop_hbase2",
  "spec" : {
    "dataSchema" : {
      "dataSource" : "wikipedia-hadoop-hbase2-table-remote-nosecure",
      "parser" : {
        "type" : "hbase2",
        "parseSpec" : {
          "format" : "hbase2",
          "dimensionsSpec" : {
            "dimensions" : [
              "channel",
              "cityName",
              "comment",
              "countryIsoCode",
              "countryName",
              "isAnonymous",
              "isMinor",
              "isNew",
              "isRobot",
              "isUnpatrolled",
              "metroCode",
              "namespace",
              "page",
              "regionIsoCode",
              "regionName",
              "user"
            ]
          },
          "timestampSpec" : {
            "format" : "auto",
            "column" : "time"
          },
          "hbaseRowSpec": {
            "rowKeySpec": {
              "format": "delimiter",
              "delimiter": "|",
              "columns": [
                {
                  "type": "string",
                  "name": "salt"
                },
                {
                  "type": "string",
                  "name": "time"
                }
              ]
            },
            "columnSpec": [
              {
                "type": "string",
                "name": "A:channel",
                "mappingName": "channel"
              },
              {
                "type": "string",
                "name": "A:cityName",
                "mappingName": "cityName"
              },
              {
                "type": "string",
                "name": "A:comment",
                "mappingName": "comment"
              },
              {
                "type": "string",
                "name": "A:countryIsoCode",
                "mappingName": "countryIsoCode"
              },
              {
                "type": "string",
                "name": "A:countryName",
                "mappingName": "countryName"
              },
              {
                "type": "boolean",
                "name": "A:isAnonymous",
                "mappingName": "isAnonymous"
              },
              {
                "type": "boolean",
                "name": "A:isMinor",
                "mappingName": "isMinor"
              },
              {
                "type": "boolean",
                "name": "A:isNew",
                "mappingName": "isNew"
              },
              {
                "type": "boolean",
                "name": "A:isRobot",
                "mappingName": "isRobot"
              },
              {
                "type": "boolean",
                "name": "A:isUnpatrolled",
                "mappingName": "isUnpatrolled"
              },
              {
                "type": "string",
                "name": "A:namespace",
                "mappingName": "namespace"
              },
              {
                "type": "string",
                "name": "A:page",
                "mappingName": "page"
              },
              {
                "type": "string",
                "name": "A:regionIsoCode",
                "mappingName": "regionIsoCode"
              },
              {
                "type": "string",
                "name": "A:regionName",
                "mappingName": "regionName"
              },
              {
                "type": "string",
                "name": "A:user",
                "mappingName": "user"
              },
              {
                "type": "int",
                "name": "A:added",
                "mappingName": "added"
              },
              {
                "type": "int",
                "name": "A:deleted",
                "mappingName": "deleted"
              },
              {
                "type": "int",
                "name": "A:delta",
                "mappingName": "delta"
              },
              {
                "type": "int",
                "name": "A:metroCode",
                "mappingName": "metroCode"
              }
            ]
          }
        }
      },
      "metricsSpec" : [
        {
          "type": "count",
          "name": "count"
        },
        {
          "type": "longSum",
          "name": "sum_added",
          "fieldName": "added",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_deleted",
          "fieldName": "deleted",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_delta",
          "fieldName": "delta",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_metroCode",
          "fieldName": "metroCode",
          "expression": null
        }
      ],
      "granularitySpec" : {
        "type" : "uniform",
        "segmentGranularity" : "day",
        "queryGranularity" : "none",
        "intervals" : ["2015-09-12/2015-09-13"],
        "rollup" : true
      }
    },
    "ioConfig" : {
      "type" : "hadoop",
      "inputSpec" : {
        "type" : "hbase2",
        "connectionConfig": {
          "zookeeperQuorum": "<zookeeper-quorum>",
          "kerberosConfig": {
            "principal": null,
            "realm": null,
            "keytab": null
          }
        },
        "scanInfo": {
          "type": "table",
          "name": "default:wikipedia",
          "startKey": null,
          "endKey": null
        },
        "hbaseClientConfig": {
          "hbase.rootdir": "/hbase",
          "hbase.client.scanner.timeout.period": 60000,
          "hbase.client.scanner.caching": 100,
          "hbase.client.scanner.max.result.size": 2097152
        }
      }
    },
    "tuningConfig" : {
      "type" : "hadoop",
      "partitionsSpec" : {
        "type" : "hashed",
        "targetPartitionSize" : 5000000
      },
      "forceExtendableShardSpecs" : true,
      "jobProperties" : {
        "mapreduce.job.classloader" : true,
        "mapreduce.map.log.level": "debug",
        "mapreduce.framework.name": "yarn"
      }
    }
  },
  "hadoopDependencyCoordinates": ["org.apache.hadoop:hadoop-client:2.8.5"]
}
```

##### Request

```
curl -X 'POST' -H 'Content-Type:application/json' -d @spec-files/hbase2/non-secure/wikipedia-hadoop-hbase2-table-remote-nosecure.json http://localhost:8888/druid/indexer/v1/task
```

#### Snapshot

##### Spec

```
{
  "type" : "index_hadoop_hbase2",
  "spec" : {
    "dataSchema" : {
      "dataSource" : "wikipedia-hadoop-hbase2-snapshot-remote-nosecure",
      "parser" : {
        "type" : "hbase2",
        "parseSpec" : {
          "format" : "hbase2",
          "dimensionsSpec" : {
            "dimensions" : [
              "channel",
              "cityName",
              "comment",
              "countryIsoCode",
              "countryName",
              "isAnonymous",
              "isMinor",
              "isNew",
              "isRobot",
              "isUnpatrolled",
              "metroCode",
              "namespace",
              "page",
              "regionIsoCode",
              "regionName",
              "user"
            ]
          },
          "timestampSpec" : {
            "format" : "auto",
            "column" : "time"
          },
          "hbaseRowSpec": {
            "rowKeySpec": {
              "format": "delimiter",
              "delimiter": "|",
              "columns": [
                {
                  "type": "string",
                  "name": "salt"
                },
                {
                  "type": "string",
                  "name": "time"
                }
              ]
            },
            "columnSpec": [
              {
                "type": "string",
                "name": "A:channel",
                "mappingName": "channel"
              },
              {
                "type": "string",
                "name": "A:cityName",
                "mappingName": "cityName"
              },
              {
                "type": "string",
                "name": "A:comment",
                "mappingName": "comment"
              },
              {
                "type": "string",
                "name": "A:countryIsoCode",
                "mappingName": "countryIsoCode"
              },
              {
                "type": "string",
                "name": "A:countryName",
                "mappingName": "countryName"
              },
              {
                "type": "boolean",
                "name": "A:isAnonymous",
                "mappingName": "isAnonymous"
              },
              {
                "type": "boolean",
                "name": "A:isMinor",
                "mappingName": "isMinor"
              },
              {
                "type": "boolean",
                "name": "A:isNew",
                "mappingName": "isNew"
              },
              {
                "type": "boolean",
                "name": "A:isRobot",
                "mappingName": "isRobot"
              },
              {
                "type": "boolean",
                "name": "A:isUnpatrolled",
                "mappingName": "isUnpatrolled"
              },
              {
                "type": "string",
                "name": "A:namespace",
                "mappingName": "namespace"
              },
              {
                "type": "string",
                "name": "A:page",
                "mappingName": "page"
              },
              {
                "type": "string",
                "name": "A:regionIsoCode",
                "mappingName": "regionIsoCode"
              },
              {
                "type": "string",
                "name": "A:regionName",
                "mappingName": "regionName"
              },
              {
                "type": "string",
                "name": "A:user",
                "mappingName": "user"
              },
              {
                "type": "int",
                "name": "A:added",
                "mappingName": "added"
              },
              {
                "type": "int",
                "name": "A:deleted",
                "mappingName": "deleted"
              },
              {
                "type": "int",
                "name": "A:delta",
                "mappingName": "delta"
              },
              {
                "type": "int",
                "name": "A:metroCode",
                "mappingName": "metroCode"
              }
            ]
          }
        }
      },
      "metricsSpec" : [
        {
          "type": "count",
          "name": "count"
        },
        {
          "type": "longSum",
          "name": "sum_added",
          "fieldName": "added",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_deleted",
          "fieldName": "deleted",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_delta",
          "fieldName": "delta",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_metroCode",
          "fieldName": "metroCode",
          "expression": null
        }
      ],
      "granularitySpec" : {
        "type" : "uniform",
        "segmentGranularity" : "day",
        "queryGranularity" : "none",
        "intervals" : ["2015-09-12/2015-09-13"],
        "rollup" : true
      }
    },
    "ioConfig" : {
      "type" : "hadoop",
      "inputSpec" : {
        "type" : "hbase2",
        "connectionConfig": {
          "kerberosConfig": {
            "principal": null,
            "realm": null,
            "keytab": null
          }
        },
        "scanInfo": {
          "type": "snapshot",
          "name": "wikipedia-snapshot",
          "startKey": null,
          "endKey": null,
          "restoreDir": "hdfs://juke-ns-test/user/deploy"
        },
        "hbaseClientConfig": {
          "hbase.rootdir": "hdfs://juke-ns-test/hbase",
          "hbase.client.scanner.timeout.period": 6000,
          "hbase.client.scanner.caching": 100,
          "hbase.client.scanner.max.result.size": 2097152
        }
      }
    },
    "tuningConfig" : {
      "type" : "hadoop",
      "partitionsSpec" : {
        "type" : "hashed",
        "targetPartitionSize" : 5000000
      },
      "forceExtendableShardSpecs" : true,
      "jobProperties" : {
        "mapreduce.job.classloader" : true,
        "mapreduce.map.log.level": "info",
        "yarn.app.mapreduce.am.log.level": "info",
        "mapreduce.job.classloader.system.classes": "-org.apache.hadoop.hbase.,-org.apache.hadoop.metrics2.MetricHistogram,-org.apache.hadoop.metrics2.MetricsExecutor,-org.apache.hadoop.metrics2.lib.,org.apache.hadoop.",
        "dfs.nameservices": "test1, juke-ns-test",
        "dfs.internal.nameservices": "test1",
        "dfs.ha.namenodes.juke-ns-test": "nn1,nn2",
        "dfs.client.failover.proxy.provider.juke-ns-test": "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider",
        "dfs.namenode.rpc-address.juke-ns-test.nn1": "<nn1>:8020",
        "dfs.namenode.rpc-address.juke-ns-test.nn2": "<nn2>:8020"
      }
    }
  },
  "hadoopDependencyCoordinates": ["org.apache.hadoop:hadoop-client:2.8.5"]
}
```

##### Request

```
curl -X 'POST' -H 'Content-Type:application/json' -d @spec-files/hbase2/non-secure/wikipedia-hadoop-hbase2-snapshot-remote-nosecure.json http://localhost:8888/druid/indexer/v1/task
```

### Secure cluster
#### Table

##### Spec

```
{
  "type" : "index_hadoop_hbase2",
  "spec" : {
    "dataSchema" : {
      "dataSource" : "wikipedia-hadoop-hbase2-table-kerberos-remote-secure",
      "parser" : {
        "type" : "hbase2",
        "parseSpec" : {
          "format" : "hbase2",
          "dimensionsSpec" : {
            "dimensions" : [
              "channel",
              "cityName",
              "comment",
              "countryIsoCode",
              "countryName",
              "isAnonymous",
              "isMinor",
              "isNew",
              "isRobot",
              "isUnpatrolled",
              "metroCode",
              "namespace",
              "page",
              "regionIsoCode",
              "regionName",
              "user"
            ]
          },
          "timestampSpec" : {
            "format" : "auto",
            "column" : "time"
          },
          "hbaseRowSpec": {
            "rowKeySpec": {
              "format": "delimiter",
              "delimiter": "|",
              "columns": [
                {
                  "type": "string",
                  "name": "salt"
                },
                {
                  "type": "string",
                  "name": "time"
                }
              ]
            },
            "columnSpec": [
              {
                "type": "string",
                "name": "A:channel",
                "mappingName": "channel"
              },
              {
                "type": "string",
                "name": "A:cityName",
                "mappingName": "cityName"
              },
              {
                "type": "string",
                "name": "A:comment",
                "mappingName": "comment"
              },
              {
                "type": "string",
                "name": "A:countryIsoCode",
                "mappingName": "countryIsoCode"
              },
              {
                "type": "string",
                "name": "A:countryName",
                "mappingName": "countryName"
              },
              {
                "type": "boolean",
                "name": "A:isAnonymous",
                "mappingName": "isAnonymous"
              },
              {
                "type": "boolean",
                "name": "A:isMinor",
                "mappingName": "isMinor"
              },
              {
                "type": "boolean",
                "name": "A:isNew",
                "mappingName": "isNew"
              },
              {
                "type": "boolean",
                "name": "A:isRobot",
                "mappingName": "isRobot"
              },
              {
                "type": "boolean",
                "name": "A:isUnpatrolled",
                "mappingName": "isUnpatrolled"
              },
              {
                "type": "string",
                "name": "A:namespace",
                "mappingName": "namespace"
              },
              {
                "type": "string",
                "name": "A:page",
                "mappingName": "page"
              },
              {
                "type": "string",
                "name": "A:regionIsoCode",
                "mappingName": "regionIsoCode"
              },
              {
                "type": "string",
                "name": "A:regionName",
                "mappingName": "regionName"
              },
              {
                "type": "string",
                "name": "A:user",
                "mappingName": "user"
              },
              {
                "type": "int",
                "name": "A:added",
                "mappingName": "added"
              },
              {
                "type": "int",
                "name": "A:deleted",
                "mappingName": "deleted"
              },
              {
                "type": "int",
                "name": "A:delta",
                "mappingName": "delta"
              },
              {
                "type": "int",
                "name": "A:metroCode",
                "mappingName": "metroCode"
              }
            ]
          }
        }
      },
      "metricsSpec" : [
        {
          "type": "count",
          "name": "count"
        },
        {
          "type": "longSum",
          "name": "sum_added",
          "fieldName": "added",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_deleted",
          "fieldName": "deleted",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_delta",
          "fieldName": "delta",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_metroCode",
          "fieldName": "metroCode",
          "expression": null
        }
      ],
      "granularitySpec" : {
        "type" : "uniform",
        "segmentGranularity" : "day",
        "queryGranularity" : "none",
        "intervals" : ["2015-09-12/2015-09-13"],
        "rollup" : true
      }
    },
    "ioConfig" : {
      "type" : "hadoop",
      "inputSpec" : {
        "type" : "hbase2",
        "connectionConfig": {
          "zookeeperQuorum": "hbase-ambari-test7.dakao.io",
          "kerberosConfig": {
            "principal": "<principal>",
            "keytab": "<keytab-filepath>"
          }
        },
        "scanInfo": {
          "type": "table",
          "name": "default:wikipedia",
          "startKey": null,
          "endKey": null
        }
      }
    },
    "tuningConfig" : {
      "type" : "hadoop",
      "partitionsSpec" : {
        "type" : "hashed",
        "targetPartitionSize" : 5000000
      },
      "forceExtendableShardSpecs" : true,
      "jobProperties" : {
        "mapreduce.job.classloader" : true
      }
    }
  },
  "hadoopDependencyCoordinates": ["org.apache.hadoop:hadoop-client:2.8.5"]
}
```

##### Request

```
curl -X 'POST' -H 'Content-Type:application/json' -d @spec-files/hbase2/secure/wikipedia-hadoop-hbase2-table-kerberos-remote-secure.json http://localhost:8888/druid/indexer/v1/task
```

#### Snapshot

##### Spec

```
{
  "type" : "index_hadoop_hbase2",
  "spec" : {
    "dataSchema" : {
      "dataSource" : "wikipedia-hadoop-hbase2-snapshot-kerberos-remote-secure",
      "parser" : {
        "type" : "hbase2",
        "parseSpec" : {
          "format" : "hbase2",
          "dimensionsSpec" : {
            "dimensions" : [
              "channel",
              "cityName",
              "comment",
              "countryIsoCode",
              "countryName",
              "isAnonymous",
              "isMinor",
              "isNew",
              "isRobot",
              "isUnpatrolled",
              "metroCode",
              "namespace",
              "page",
              "regionIsoCode",
              "regionName",
              "user"
            ]
          },
          "timestampSpec" : {
            "format" : "auto",
            "column" : "time"
          },
          "hbaseRowSpec": {
            "rowKeySpec": {
              "format": "delimiter",
              "delimiter": "|",
              "columns": [
                {
                  "type": "string",
                  "name": "salt"
                },
                {
                  "type": "string",
                  "name": "time"
                }
              ]
            },
            "columnSpec": [
              {
                "type": "string",
                "name": "A:channel",
                "mappingName": "channel"
              },
              {
                "type": "string",
                "name": "A:cityName",
                "mappingName": "cityName"
              },
              {
                "type": "string",
                "name": "A:comment",
                "mappingName": "comment"
              },
              {
                "type": "string",
                "name": "A:countryIsoCode",
                "mappingName": "countryIsoCode"
              },
              {
                "type": "string",
                "name": "A:countryName",
                "mappingName": "countryName"
              },
              {
                "type": "boolean",
                "name": "A:isAnonymous",
                "mappingName": "isAnonymous"
              },
              {
                "type": "boolean",
                "name": "A:isMinor",
                "mappingName": "isMinor"
              },
              {
                "type": "boolean",
                "name": "A:isNew",
                "mappingName": "isNew"
              },
              {
                "type": "boolean",
                "name": "A:isRobot",
                "mappingName": "isRobot"
              },
              {
                "type": "boolean",
                "name": "A:isUnpatrolled",
                "mappingName": "isUnpatrolled"
              },
              {
                "type": "string",
                "name": "A:namespace",
                "mappingName": "namespace"
              },
              {
                "type": "string",
                "name": "A:page",
                "mappingName": "page"
              },
              {
                "type": "string",
                "name": "A:regionIsoCode",
                "mappingName": "regionIsoCode"
              },
              {
                "type": "string",
                "name": "A:regionName",
                "mappingName": "regionName"
              },
              {
                "type": "string",
                "name": "A:user",
                "mappingName": "user"
              },
              {
                "type": "int",
                "name": "A:added",
                "mappingName": "added"
              },
              {
                "type": "int",
                "name": "A:deleted",
                "mappingName": "deleted"
              },
              {
                "type": "int",
                "name": "A:delta",
                "mappingName": "delta"
              },
              {
                "type": "int",
                "name": "A:metroCode",
                "mappingName": "metroCode"
              }
            ]
          }
        }
      },
      "metricsSpec" : [
        {
          "type": "count",
          "name": "count"
        },
        {
          "type": "longSum",
          "name": "sum_added",
          "fieldName": "added",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_deleted",
          "fieldName": "deleted",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_delta",
          "fieldName": "delta",
          "expression": null
        },
        {
          "type": "longSum",
          "name": "sum_metroCode",
          "fieldName": "metroCode",
          "expression": null
        }
      ],
      "granularitySpec" : {
        "type" : "uniform",
        "segmentGranularity" : "day",
        "queryGranularity" : "none",
        "intervals" : ["2015-09-12/2015-09-13"],
        "rollup" : true
      }
    },
    "ioConfig" : {
      "type" : "hadoop",
      "inputSpec" : {
        "type" : "hbase2",
        "connectionConfig": {
          "zookeeperQuorum": "hbase-ambari-test7.dakao.io",
          "kerberosConfig": {
            "principal": "<principal>",
            "keytab": "<keytab-filepath>"
          }
        },
        "scanInfo": {
          "type": "snapshot",
          "name": "wikipedia-snapshot",
          "startKey": null,
          "endKey": null,
          "restoreDir": "hdfs://hbase-ambari-test7.dakao.io/user/juke-mini666"
        },
        "hbaseClientConfig": {
          "hbase.rootdir": "hdfs://hbase-ambari-test7.dakao.io/hbase",
          "hbase.client.scanner.timeout.period": 60000,
          "hbase.client.scanner.caching": 100,
          "hbase.client.scanner.max.result.size": 2097152
        }
      }
    },
    "tuningConfig" : {
      "type" : "hadoop",
      "partitionsSpec" : {
        "type" : "hashed",
        "targetPartitionSize" : 5000000
      },
      "forceExtendableShardSpecs" : true,
      "jobProperties" : {
        "mapreduce.job.classloader" : true,
        "dfs.namenode.kerberos.principal.pattern": "*",
        "mapreduce.job.classloader.system.classes": "-org.apache.hadoop.hbase.,-org.apache.hadoop.metrics2.MetricHistogram,-org.apache.hadoop.metrics2.MetricsExecutor,-org.apache.hadoop.metrics2.lib.,org.apache.hadoop.",
        "hbase.rootdir": "hdfs://hbase-ambari-test7.dakao.io/hbase",
        "mapreduce.job.hdfs-servers": "hdfs://hbase-ambari-test7.dakao.io,hdfs://test1",
        "mapreduce.job.hdfs-servers.token-renewal.exclude": "hbase-ambari-test7.dakao.io"
      }
    }
  },
  "hadoopDependencyCoordinates": ["org.apache.hadoop:hadoop-client:2.8.5"]
}
```

##### Request

```
curl -X 'POST' -H 'Content-Type:application/json' -d @spec-files/hbase2/secure/wikipedia-hadoop-hbase2-snapshot-kerberos-remote-secure.json http://localhost:8888/druid/indexer/v1/task
```
