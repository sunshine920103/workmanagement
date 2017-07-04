package com.workmanagement.util;

import java.io.Closeable;

import org.apache.log4j.Logger;

public abstract class RedisSerializeTranscoder {

	protected static Logger logger = Logger.getLogger(RedisSerializeTranscoder.class);
	
	public abstract byte[] serialize(Object value);
	
	public abstract Object deserialize(byte[] in);
	
	public void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				 logger.info("Unable to close " + closeable, e); 
			}
		}
	}
}