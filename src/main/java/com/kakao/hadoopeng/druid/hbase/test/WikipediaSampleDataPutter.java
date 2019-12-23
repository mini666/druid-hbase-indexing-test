/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kakao.hadoopeng.druid.hbase.test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Hash;
import org.apache.hadoop.hbase.util.MurmurHash3;
import org.apache.hadoop.hbase.util.RegionSplitter;
import org.apache.hadoop.security.UserGroupInformation;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.collect.Lists;

// create 'wikipedia', {NAME=>'A', DATA_BLOCK_ENCODING => 'FAST_DIFF'}, {NUMREGIONS => 10, SPLITALGO => 'HexStringSplit'}
public class WikipediaSampleDataPutter {

  private static final String PRINCIPAL = "";
  private static final String KEYTAB_FILE_PATH = "";
  private static final String KRB5_CONF = "";
  private static final String REALM = "";
  
  private static final TableName WIKIPEDIA_TABLE = TableName.valueOf("wikipedia");
  private static final int REGION_SERVERS = 10;
  private static final byte[] DELIMITER = Bytes.toBytes("|");
  private static final byte[] CF_NAME_BYTES = Bytes.toBytes("A");
  
  private static final String DATA_FILE_PATH = "sampledata/wikiticker-2015-09-12-sampled.json";
  private static final List<String> NUMERIC_COLUMN_NAME_LIST = Lists.newArrayList("metroCode", "delta", "added", "deleted");
  private static final Hash HASH = MurmurHash3.getInstance();
  
  private byte[][] saltKeys = new byte[REGION_SERVERS][];
  
  public WikipediaSampleDataPutter() {
    byte[][] regionBorder = new RegionSplitter.HexStringSplit().split(REGION_SERVERS);
    saltKeys[0] = new byte[]{0};
    IntStream.range(0, regionBorder.length)
      .forEach(i -> {
        saltKeys[i + 1] = regionBorder[i];
      })
    ;
    saltKeys[REGION_SERVERS - 1] = new byte[] {Byte.MAX_VALUE};
  }
  
  public static void main(String[] args) throws Exception {
    WikipediaSampleDataPutter dataPutter = new WikipediaSampleDataPutter();

    dataPutter.start();
  }

  @SuppressWarnings("unchecked")
  private void start() throws Exception {
    Configuration conf = HBaseConfiguration.create();
    conf.set(HConstants.ZOOKEEPER_QUORUM, "hbase-ambari-test7.dakao.io");
    
    applyKerberosConfig(conf);
    
    ObjectMapper mapper = new ObjectMapper();
    try (Connection connection = ConnectionFactory.createConnection(conf);
         Table table = connection.getTable(WIKIPEDIA_TABLE)) {
      long rows = 
        Files.lines(Paths.get(DATA_FILE_PATH))
          .parallel()
          .mapToLong(line -> {
            try {
              Map<String, Object> jsonMap = mapper.readValue(line, Map.class);
              Put put = createPut(jsonMap);
              
              Result result = table.get(new Get(put.getRow()));
              if (result.isEmpty()) {
                table.put(put);
                return 1;
              } else {
                return 0;
              }
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          })
          .sum()
      ;
      
      System.out.println("rows: " + rows);
    }
  }
  
  private Put createPut(Map<String, Object> dataMap) {
    String time = (String)dataMap.remove("time");
    byte[] timeBytesValue = Bytes.toBytes(time);
    int targetRegion = Math.abs(HASH.hash(timeBytesValue) % REGION_SERVERS);
    byte[] saltBytes = saltKeys[targetRegion];
    
    Put put = new Put(Bytes.add(saltBytes, DELIMITER, timeBytesValue));
    dataMap.entrySet().stream()
      .forEach(e -> {
        String columnName = e.getKey();
        byte[] columnNameBytes = Bytes.toBytes(e.getKey());
        Object columnValue = e.getValue();
        
        if (columnValue == null) {
          return;
        }
        
        byte[] columnValueBytes = null;
        try {
          if (NUMERIC_COLUMN_NAME_LIST.contains(columnName)) {
            columnValueBytes = Bytes.toBytes((Integer)columnValue);
          } else if (columnName.startsWith("is")) {
            columnValueBytes = Bytes.toBytes((Boolean)columnValue);
          } else {
            columnValueBytes = Bytes.toBytes((String)columnValue);
          }
        } catch (Exception ex) {
          System.out.println(columnName + ": " + ex.getMessage());
          throw new RuntimeException(ex);
        }
        
        put.addColumn(CF_NAME_BYTES, columnNameBytes, columnValueBytes);
      })
    ;
    
    return put;
  }
  
  private void applyKerberosConfig(Configuration conf) throws IOException {
    conf.set("hadoop.security.authentication", "Kerberos");
    conf.set("hbase.security.authentication", "kerberos");
    conf.set("hbase.master.kerberos.principal", "hbase/_HOST@" + REALM);
    conf.set("hbase.regionserver.kerberos.principal", "hbase/_HOST@" + REALM);
    
    System.setProperty("java.security.krb5.conf", KRB5_CONF);
    
    UserGroupInformation.setConfiguration(conf);
    UserGroupInformation.loginUserFromKeytab(PRINCIPAL, KEYTAB_FILE_PATH);
    UserGroupInformation currentUser = UserGroupInformation.getCurrentUser();

    System.out.println("current user : " + currentUser + "\n");
  }
}
