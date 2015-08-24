package maming.jdo;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.datastore.DataStoreCache;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.model.MTable;
import org.junit.Test;

public class TestRun {

	Properties prop = new Properties();
	
	public void setup(){
		HiveConf conf = new HiveConf(TestRun.class);
		System.out.println(conf.size());
		System.out.println(conf.get("javax.jdo.option.ConnectionURL"));
	
	    Iterator<Map.Entry<String, String>> iter = conf.iterator();
	    while (iter.hasNext()) {
	      Map.Entry<String, String> e = iter.next();
	      if (e.getKey().contains("datanucleus") || e.getKey().contains("jdo")) {
	    	  prop.setProperty(e.getKey(), conf.get(e.getKey()));
	      }
	    }
	}
	
	PersistenceManagerFactory pmf = null; 
	PersistenceManager pm = null;
	
	public void createTable(){
		MmOrder morder = new MmOrder("aa",1);
		pm.makePersistent(morder);
	}
	@Test
	public void run(){
		
		setup();
		
		//重新设置数据库属性信息
		prop.setProperty("javax.jdo.option.ConnectionDriverName","com.mysql.jdbc.Driver");
		prop.setProperty("javax.jdo.option.ConnectionURL","jdbc:mysql://192.168.10.50:3306/passport?rewriteBatchedStatements=true&amp;useUnicode=true&amp;characterEncoding=utf8");
		prop.setProperty("javax.jdo.option.ConnectionUserName","coohua");
		prop.setProperty("javax.jdo.option.ConnectionPassword","coohua#007");
		//prop.list(System.out);
		System.out.println(prop.getProperty("javax.jdo.option.ConnectionURL"));
		pmf = JDOHelper.getPersistenceManagerFactory(prop);
		DataStoreCache dsc = pmf.getDataStoreCache();
		System.out.println(dsc);
		dsc.pinAll(true, MmOrder.class);//对应的序列化和反序列化class
		dsc.pinAll(true, MTable.class);//对应的序列化和反序列化class
		
		pm = pmf.getPersistenceManager();
		createTable();
	}
	
}
