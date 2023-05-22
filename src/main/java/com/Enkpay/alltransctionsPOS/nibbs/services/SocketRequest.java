package com.Enkpay.alltransctionsPOS.nibbs.services;

import com.Enkpay.alltransctionsPOS.nibbs.model.NibsUtilityData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SocketRequest {
    java.util.logging.Logger logger =  java.util.logging.Logger.getLogger(this.getClass().getName());

    private final NibsUtilityData nibsUtilityData;

    public SocketRequest(NibsUtilityData nibsUtilityData) {
        this.nibsUtilityData = nibsUtilityData;
    }

    public String send( byte[] isoStream) throws Exception {
        if (nibsUtilityData.getIsSSL()) {
            SSLSocket sslsocket = getConnection(
                    nibsUtilityData.getIpAddress(),
                    nibsUtilityData.getIpPort()

            );
            sslsocket.setSoTimeout(60 * 1000);
            return send(isoStream, sslsocket);
        } else {
            Socket socket = new Socket();
            InetSocketAddress sockAddr = new InetSocketAddress(nibsUtilityData.getIpAddress(), nibsUtilityData.getIpPort());
            socket.connect(sockAddr, 60 * 1000);
            socket.setSoTimeout(60 * 1000);

            return send(isoStream, socket);
        }
    }

    private String send(byte[] isoStream, Socket socket) throws Exception {
        byte[] responseArray = new byte[0];
        try {
            DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataIn = new DataInputStream(socket.getInputStream());

            logger.info("OutData"+ new String(isoStream) + " Length: " + isoStream.length);

            dataOut.write(isoStream);

            responseArray = dataIn.readAllBytes();
        } catch (EOFException ignored) {
        } catch (SocketTimeoutException e) {
            throw new SocketTimeoutException("Connection timed out, failed to receive response from remote server");
        } catch (ConnectException e) {
            throw new RuntimeException("Could not connect to the internet, check your connection settings and try again");
        } catch (NoRouteToHostException e) {
            throw new RuntimeException("Could not connect with remote server, check your connection settings and try again");
        } catch (PortUnreachableException e) {
            throw new RuntimeException("Could not connect with remote server, port is unreachable, check your connection settings and try again");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed url, check your connection settings and try again");
        } catch (BindException e) {
            throw new RuntimeException("Could not bind socket to local address or port, check your connection settings and try again");
        } catch (SocketException e) {
            throw new RuntimeException("Could not create socket, check your connection settings and try again");
        } catch (UnknownHostException e) {
            throw new RuntimeException("Host address could not be recognized, check your connection settings and try again");
        } catch (UnknownServiceException e) {
            throw new RuntimeException("Unknown service, check your connection settings and try again");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new String(responseArray);
    }

    public SSLSocket getConnection(String ip, Integer port){
        SSLSocketFactory sslFactory =
                null;
        try {
            sslFactory = SSLManager.getTrustySSLSocketFactory();
            return SSLManager.createSocket(sslFactory, ip, port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


}
