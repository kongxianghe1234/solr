/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.client.solrj.embedded;

import org.apache.solr.client.solrj.LargeVolumeTestBase;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.core.SolrCore;
import org.apache.solr.schema.SchemaField;

/**
 * @version $Id$
 * @since solr 1.3
 */
public class LargeVolumeJettyTest extends LargeVolumeTestBase {

  SolrServer server;
  JettySolrRunner jetty;

  static final int port = 8984; // not 8983
  static final String context = "/example";

  
  @Override public void setUp() throws Exception 
  {
    super.setUp();
    
    SolrCore c = SolrCore.getSolrCore();
    System.out.println( c.getConfigFile() );
    System.out.println( c.getSolrConfig().configFile );
    System.out.println( c.getSchema().getFields() );

    try {
      SchemaField f = c.getSchema().getField( "cat" );
      System.out.println( f );
    }
    catch( Exception ex ) {
      ex.printStackTrace();
    }
    System.out.println( "---" );
    
    
    jetty = new JettySolrRunner( context, port );
    jetty.start();
    
    server = this.createNewSolrServer();
  }

  @Override public void tearDown() throws Exception 
  {
    super.tearDown();
    jetty.stop();  // stop the server
  }
  

  @Override
  protected SolrServer getSolrServer()
  {
    return server;
  }

  @Override
  protected SolrServer createNewSolrServer()
  {
    try {
      // setup the server...
      String url = "http://localhost:"+port+context;
      CommonsHttpSolrServer s = new CommonsHttpSolrServer( url );
      s.setConnectionTimeout(100); // 1/10th sec
      s.setDefaultMaxConnectionsPerHost(100);
      s.setMaxTotalConnections(100);
      return s;
    }
    catch( Exception ex ) {
      throw new RuntimeException( ex );
    }
  }
}