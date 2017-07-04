package com.workmanagement.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class RedisUtil {

    //Redis服务器IP
    private static String ADDR = SettingUtils.getCommonSetting("redis.ip");

    //Redis的端口号
    private static int PORT = Integer.parseInt(SettingUtils.getCommonSetting("redis.port"));

    //访问密码
    private static String AUTH = SettingUtils.getCommonSetting("redis.auth");

    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = Integer.parseInt(SettingUtils.getCommonSetting("redis.maxActive"));
    ;

    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = Integer.parseInt(SettingUtils.getCommonSetting("redis.maxIdle"));
    ;

    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = Integer.parseInt(SettingUtils.getCommonSetting("redis.maxWait"));
    ;

    private static int TIMEOUT = Integer.parseInt(SettingUtils.getCommonSetting("redis.timeout"));
    ;

    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = Boolean.parseBoolean(SettingUtils.getCommonSetting("redis.testOnBorrow"));

    private static JedisPool jedisPool = null;

    /**
     * 初始化Redis连接池
     */
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxActive(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWait(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            //需要认证
            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
            //不需要认证
            //jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Jedis实例
     *
     * @return
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 释放jedis资源
     *
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 判断key是否存在或是否为null
     *
     * @param String key
     * @return boolean
     */
    public static boolean isEmpty(String key) {
        Jedis jedis = getJedis();
        boolean isEmpty = jedis.get(key) == null;
        returnResource(jedis);
        return isEmpty;
    }

    /**
     * 删除单个缓存
     *
     * @param key
     */
    public static void delSingleData(String key) {
        Jedis jedis = getJedis();
        jedis.del(key);
        returnResource(jedis);
    }

    /**
     * 批量删除缓存
     *
     * @param patternKeys
     */
    public static void delBatchData(String... patternKeys) {
        Jedis jedis = getJedis();
        for (String patternKey : patternKeys) {
            Set<String> set = jedis.keys(patternKey + "*");
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                String key = it.next();
                getJedis().del(key);
            }
        }
        returnResource(jedis);
    }

    /**
     * 保存缓存
     *
     * @param key
     * @param value
     */
    public static <T> void setData(String key, T value) {
        Jedis jedis = getJedis();
        String v = JsonUtil.toJson(value);
        jedis.set(key, v);
        returnResource(jedis);
    }

    /**
     * 获取对象缓存
     *
     * @param key
     */
    public static <T> T getObjData(String key, Class<T> clazz) {
        Jedis jedis = getJedis();
        String value = jedis.get(key);
        returnResource(jedis);
        return JsonUtil.fromJson(value, clazz);
    }

    /**
     * 获取集合缓存(未分页)
     *
     * @param key
     */
    public static <T> T getListData(String key, Type type) {
        Jedis jedis = getJedis();
        String value = jedis.get(key);
        returnResource(jedis);
        return JsonUtil.fromJson(value, type);
    }

    /**
     * (分页使用)删除值 
     * @param key
     * @param id 唯一标识
     * */  
    /*public static void delPagingData(String key, Integer id) {  
        Jedis jedis = getJedis();
    	jedis.del(key+id.toString());
    	jedis.zrem(key, id.toString());
    	returnResource(jedis);
    }  */

    /**
     * (分页使用)更新值 
     * @param key
     * @param value 更新的值 
     * @param id 唯一标识
     * */  
   /* public static <T> void updatePagingData(String key, T value, Integer id) {  
        Jedis jedis = getJedis();
    	jedis.set(key+id.toString(), JsonUtil.toJson(value));
    	returnResource(jedis);
    } */

    /**
     * (分页使用)获取总数 
     * @param String key 
     * @return 总数
     * */  
   /* public static long getPagingLength(String key) {  
        Jedis jedis = getJedis();
    	long length = jedis.zcard(key);
    	returnResource(jedis);
        return length;  
    } */

    /**
     * (分页使用)添加值
     *
     * @param key
     * @param value
     * @param score 第几页
     */
    public static <T> void setPagingData(String key, T value, double score) {
        Jedis jedis = getJedis();
        jedis.zadd(key, score, JsonUtil.toJson(value));
        returnResource(jedis);
    }  
    /*public static <T> void setPagingData(String key, T value, double score, Integer id) {  
    	Jedis jedis = getJedis();
    	jedis.set(key+id.toString(), JsonUtil.toJson(value));
    	jedis.zadd(key, score, id.toString());
    	returnResource(jedis);
    }  */

    /**
     * (分页使用)获取集合缓存
     *
     * @param key
     * @param page  第几页
     * @param clazz
     * @return
     */
    public static <T> List<T> getPagingData(String key, long page, Class<T> clazz) {
        Jedis jedis = getJedis();
        List<T> arr = new ArrayList<T>();

        //获取分页数据
        Set<String> list = jedis.zrangeByScore(key, page, page);
        Iterator<String> iterator = list.iterator();

        //转换为对象
        while (iterator.hasNext()) {
            arr.add(JsonUtil.fromJson(iterator.next(), clazz));
        }

        //释放redis
        returnResource(jedis);

        return arr;
    }  
   /* public static <T> List<T> getPagingData(String key, long start, long end, Class<T> clazz) {  
        Jedis jedis = getJedis();     
        List<T> arr = new ArrayList<T>();
        
        //设置分页起始位置
        start = start * end;
        end = (start + 1) * (end-1);
        
        //获取分页数据
        Set<String> list = jedis.zrevrange(key, start, end);
        Iterator<String> iterator = list.iterator();
        
        //转换为对象
        while(iterator.hasNext()){
        	arr.add(JsonUtil.fromJson(jedis.get(key+iterator.next()), clazz));
        }
        
        //释放redis
        returnResource(jedis);  
        
        return arr;  
    }  */
}