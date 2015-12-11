package com.msf.metasploit.rpc;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.ArrayValue;
import org.msgpack.value.BooleanValue;
import org.msgpack.value.FloatValue;
import org.msgpack.value.IntegerValue;
import org.msgpack.value.MapValue;
import org.msgpack.value.NilValue;
import org.msgpack.value.RawValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Taken from BSD licensed msfgui by scriptjunkie.
 *
 * Implements an RPC backend using the MessagePack interface
 * @author scriptjunkie
 */
public class MsgRpcImpl extends RpcConnectionImpl {
	private URL u;
	private URLConnection huc; // new for each call
	protected int timeout = 0; /* scriptjunkie likes timeouts, I don't. :) */

	/**
	 * Creates a new URL to use as the basis of a connection.
	 */
	public MsgRpcImpl(String username, String password, String host, int port, boolean ssl, boolean debugf) throws MalformedURLException {
		if (ssl) { // Install the all-trusting trust manager & HostnameVerifier
			try {
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, new TrustManager[] {
					new X509TrustManager() {
						public java.security.cert.X509Certificate[] getAcceptedIssuers() {
							return null;
						}
						public void checkClientTrusted(
							java.security.cert.X509Certificate[] certs, String authType) {
						}
						public void checkServerTrusted(
							java.security.cert.X509Certificate[] certs, String authType) {
						}
					}
				}, new java.security.SecureRandom());

				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier() {
					public boolean verify(String string,SSLSession ssls) {
						return true;
					}
				});
			}
			catch (Exception e) {
			}
			u = new URL("https", host, port, "/api/1.1/");
		}
		else {
			u = new URL("http", host, port, "/api/1.1/");
		}

		/* login to msf server */
		Object[] params = new Object[]{ username, password };
		Map results = exec("auth.login",params);

		/* save the temp token (lasts for 5 minutes of inactivity) */
		rpcToken = results.get("token").toString();

		/* generate a non-expiring token and use that */
		params = new Object[]{ rpcToken };
		results = exec("auth.token_generate", params);
		rpcToken = results.get("token").toString();
	}


	/**
	 * Decodes a response recursively from MessagePackObject to a normal Java object
	 * @param src MessagePack response
	 * @return decoded object
	 */
	private static Object unMsg(Object src){
		Object out = src;
		if (src instanceof ArrayValue) {
			List l = ((ArrayValue)src).list();
			ArrayList outList = new ArrayList(l.size());
			out = outList;
			for(Object o : l)
				outList.add(unMsg(o));
		}
		else if (src instanceof BooleanValue) {
			out = ((BooleanValue)src).getBoolean();
		}
		else if (src instanceof FloatValue) {
			out = ((FloatValue)src).toFloat();
		}
		else if (src instanceof IntegerValue) {
			out = ((IntegerValue)src).asInt();
		}
		else if (src instanceof MapValue) {
//            MapValue mapValue;
//            out = mapValue.map();
			Set ents = ((MapValue)src).entrySet();
			out = new HashMap();
			for (Object ento : ents) {
				Map.Entry ent = (Map.Entry)ento;
				Object key = unMsg(ent.getKey());
				Object val = ent.getValue();
				// Hack - keep bytes of generated or encoded payload
				if(ents.size() == 1 && val instanceof RawValue && (key.equals("payload") || key.equals("encoded")))
					val = ((RawValue)val).asByteArray();
				else
					val = unMsg(val);
				((Map)out).put(key + "", val);
			}

			if (((Map)out).containsKey("error") && ((Map)out).containsKey("error_class")) {
				System.out.println(((Map)out).get("error_backtrace"));
				throw new RuntimeException(((Map)out).get("error_message").toString());
			}
		}
		else if (src instanceof NilValue) {
			out = null;
		}
		else if (src instanceof RawValue) {
			out = ((RawValue)src).asString();
		}
		return out;
	}

    private static void doMsg(MessagePacker pk, Object src) throws IOException {
        if (src instanceof String)
        {
            pk.packString(src.toString());
        }
        else if (src instanceof Integer)
        {
            pk.packInt((int) src);
        }
        else if (src instanceof Boolean)
        {
            pk.packBoolean((Boolean) src);
        }
		else if (src instanceof Object[]) {
			Object[] list = (Object[]) src;
			for (Object o : list) {
				doMsg(pk, o);
			}
		}
        else if (src instanceof Map) {
            Map map = (Map) src;
            Set<Map.Entry> entrySet = map.entrySet();
            pk.packMapHeader(entrySet.size());
			for (Map.Entry entry : entrySet) {
				pk.packString(entry.getKey().toString());
                pk.packString(entry.getValue().toString());
			}
        }
    }

	/** Creates an XMLRPC call from the given method name and parameters and sends it */
	protected void writeCall(String methodName, Object[] args) throws Exception {
		huc = u.openConnection();
		huc.setDoOutput(true);
		huc.setDoInput(true);
		huc.setUseCaches(false);
		huc.setRequestProperty("Content-Type", "binary/message-pack");
		huc.setReadTimeout(0);
		OutputStream os = huc.getOutputStream();
        MessagePacker pk = MessagePack.newDefaultPacker(os);
//        pk.packValue()
        pk.packArrayHeader(args.length + 1);
        pk.packString(methodName);
		for(Object o : args)
        {
            doMsg(pk, o);
        }
        pk.close();
		os.close();
	}

	/** Receives an RPC response and converts to an object */
	protected Object readResp() throws Exception {
		InputStream is = huc.getInputStream();
		MessageUnpacker mpo = MessagePack.newDefaultUnpacker(is);
//        mpo.
//        System.err.println("format " + mpo.getNextFormat());
//        ImmutableValue immutableValue = mpo.unpackValue();
//        immutableValue.
//        ValueType valueType = mpo.getNextFormat().getValueType();
		return unMsg(mpo.unpackValue());
	}
}
