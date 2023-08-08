package org.dromara.streamquery.stream.plugin.solon.integration;


import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.solon.integration.MybatisAdapterManager;
import org.dromara.streamquery.stream.plugin.solon.scanner.StreamScanConfig;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.BeanWrap;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**


/**
 * @author Cason
 */
public class StreamAdapterManager {
    private static StreamAdapterFactory adapterFactory = new StreamAdapterFactoryDefault();

    /**
     * 设置适配器工厂
     */
    public static void setAdapterFactory(StreamAdapterFactory adapterFactory) {
        StreamAdapterManager.adapterFactory = adapterFactory;
    }

    /**
     * 缓存适配器
     */
    private static Map<SqlSessionFactory, StreamAdapter> dbMap = new ConcurrentHashMap<>();



    public static Map<SqlSessionFactory, StreamAdapter> getAll(){
        return Collections.unmodifiableMap(dbMap);
    }

    /**
     * 获取适配器
     */
    public static StreamAdapter get(BeanWrap bw) {
        System.out.println("streamManager");
        SqlSessionFactory factory = MybatisAdapterManager.get(bw).getFactory();
        // 用 SqlSessionFactory 作为键来获取 StreamAdapter
        StreamAdapter db = dbMap.get(factory);

        if (db == null) {
            // 同步锁对象可以用 factory，因为每个 factory 都应该对应唯一的 StreamAdapter
            synchronized (factory) {
                // 再次检查，防止多线程情况下重复创建 StreamAdapter
                db = dbMap.get(factory);
                if (db == null) {
                    db = buildAdapter(bw);
                    // 用 SqlSessionFactory 作为键来存储 StreamAdapter
                    dbMap.put(factory, db);
                }
            }
        }

        return db;
    }


    /**
     * 注册数据源，并生成适配器
     *
     * @param bw 数据源的BW
     */
    public static void register(BeanWrap bw) {
//        System.out.println("订阅后触发注册");
//        System.out.println(Thread.currentThread().getId());
        get(bw);
    }

    /**
     * 构建适配器
     */
    private static StreamAdapter buildAdapter(BeanWrap bw) {
        StreamAdapter adapter;

        if (Utils.isEmpty(bw.name())) {
            adapter = adapterFactory.create(bw);
        } else {
            adapter = adapterFactory.create(bw, Solon.cfg().getProp("stream-query." + bw.name()));
        }


        return adapter;
    }

    public static StreamAdapter enableStreamQuery(SqlSessionFactory factory, StreamScanConfig scanConfig) {


        StreamAdapter adapter = dbMap.get(factory);

        adapter.setScanConfig(scanConfig);
        adapter.setSqlSessionFactory(factory);
        adapter.enhance();

        return adapter;
    }

}
