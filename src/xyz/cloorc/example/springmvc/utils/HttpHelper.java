package xyz.cloorc.example.springmvc.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhangh on 2016/03/01.
 *
 * @Description: com.dtdream.vanyar.message.autoconfiguration
 * @Author: zhangh
 * @Date: 2016/03/01
 * @Time: 15:09
 * @Email: zhangh@dtdream.com
 */
public class HttpHelper {
    private static SSLSocketFactory factory = null;
    static {
        KeyStore ksCACert = null;
        try {
            ksCACert = KeyStore.getInstance(KeyStore.getDefaultType());
            //Initialise a TrustManagerFactory with the CA keyStore, default: lib/security/jssecacerts
            ksCACert.load(new ClassPathResource("cacerts").getInputStream(), "123456".toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            //Create new SSLContext using our new TrustManagerFactory
            tmf.init(ksCACert);
            SSLContext context = SSLContext.getInstance("TLS");
            //Get a SSLSocketFactory from our SSLContext
            context.init(null, tmf.getTrustManagers(), null);
            //Set our custom SSLSocketFactory to be used by our HttpsURLConnection instance
            factory = context.getSocketFactory();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public static String url(String url, Map<String,Object> params) {
        if (null == url)
            return null;

        StringBuilder sb = new StringBuilder(url);

        if (! url.endsWith("?"))
            sb.append("?");

        if (null != params) {
            Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
            Map.Entry<String,Object> item;
            while (it.hasNext()) {
                item = it.next();
                sb.append(item.getKey()).append("=").append(item.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    public static void send (URLConnection connection, byte[] data) {
        if (null == connection) {
            return ;
        }
        OutputStream out = null;
        try {
            out = connection.getOutputStream();
            out.write(data);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void send (URLConnection connection, File file) {
        if (null == connection) {
            return;
        }
        OutputStream ostream = null;
        FileInputStream istream = null;
        try {
            ostream = connection.getOutputStream();
            istream = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = istream.read(buf)) != -1) {
                ostream.write(buf, 0, len);
            }
            ostream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != ostream)
                    ostream.close();
                if (null != istream)
                    istream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String receive (URLConnection connection) {
        BufferedReader br = null;
        StringBuilder objStrBuilder = new StringBuilder();
        String objStr;

        if (null == connection) {
            return null;
        }

        try {
            br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            while ((objStr = br.readLine()) != null) {
                objStrBuilder.append(objStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != br)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return objStrBuilder.toString();
    }

    public static URLConnection connect (String target, RequestMethod method, Map<String,String> headers) {
        if (null == target) {
            return null;
        }

        URL url = null;
        try {
            url = new URL(target);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        URLConnection connection = null;
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (connection instanceof HttpsURLConnection)
            ((HttpsURLConnection) connection).setSSLSocketFactory(factory);

        connection.setDoInput(true);
        if (method.equals(RequestMethod.POST)) {
            connection.setDoOutput(true);
        }

        connection.setRequestProperty("Pragma", "no-cache");
        connection.setRequestProperty("Cache-Control", "no-cache");

        if (null != headers) {
            Iterator<Map.Entry<String, String>> it = headers.entrySet().iterator();
            Map.Entry<String,String> item;
            while (it.hasNext()) {
                item = it.next();
                connection.setRequestProperty(item.getKey(), item.getValue());
            }
        }

        return connection;
    }

    public static String post (String url, Map<String,String> headers, byte[] data) {
        URLConnection connection = connect(url, RequestMethod.POST, headers);
        if (null != connection) {
            send(connection, data);
            return receive(connection);
        }
        return null;
    }

    public static String post (String url, Map<String,String> headers, String fmt, Object... parameters) {
        URLConnection connection = connect(url, RequestMethod.POST, headers);
        if (null != connection) {
            try {
                send(connection, String.format(fmt, parameters).getBytes("UTF-8"));
                return receive(connection);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String get (String url, Map<String,String> headers, Map<String,Object> params) {
        String target = url(url, params);
        URLConnection connection = connect(target, RequestMethod.GET, headers);
        if (null != connection) {
            return receive(connection);
        }
        return null;
    }

    public static InputStream getInputStream (String url, Map<String,String> headers, Map<String,Object> params) throws IOException {
        String target = url(url, params);
        URLConnection connection = connect(target, RequestMethod.GET, headers);
        if (null != connection) {
            return connection.getInputStream();
        }
        return null;
    }
}
