package com.scolin22.cpen431.A2;

import com.scolin22.cpen431.utils.StringUtils;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by colin on 2016-01-16.
 */
public class Request {
    final static int UNIQUE_ID_LENGTH = 16;
    final static int KEY_LENGTH = 32;
    final static int MAX_VAL_LENGTH = 10000;
    private static Logger log = Logger.getLogger(Request.class.getName());
    RequestType reqType;
    ReplyType repType;
    byte[] UID = new byte[UNIQUE_ID_LENGTH];
    short length = 0;
    byte[] value;
    InetAddress remoteIP;
    int remotePort;
    private byte[] key = new byte[KEY_LENGTH];

    public Request(ByteBuffer inBuf, InetAddress remoteIP, int remotePort) {
        log.setLevel(Level.OFF);

        this.remoteIP = remoteIP;
        this.remotePort = remotePort;

        inBuf.order(ByteOrder.LITTLE_ENDIAN);
        setUID(inBuf);
        setReqType(inBuf);
        setKey(inBuf);

        if (reqType == RequestType.PUT) {
            setPutParams(inBuf);
        }
        log.info("CREATED request, UID: " + StringUtils.byteArrayToHexString(UID) + " command: " + reqType.getByteCode());
    }

    public void setReply(ByteBuffer outBuf) {
        outBuf.order(ByteOrder.LITTLE_ENDIAN);
        outBuf.put(UID);
        outBuf.put(repType.getByteCode());

        if (reqType == RequestType.GET && repType == ReplyType.OP_SUCCESS) {
            outBuf.putShort(length);
            outBuf.put(value);
        }
    }

    public ByteBuffer getKey() {
        return ByteBuffer.wrap(key);
    }

    private void setKey(ByteBuffer inBuf) {
        inBuf.get(key);
    }

    private void setUID(ByteBuffer inBuf) {
        inBuf.get(UID);
    }

    private void setReqType(ByteBuffer inBuf) {
        reqType = RequestType.getType(inBuf.get());
    }

    private void setPutParams(ByteBuffer inBuf) {
        length = inBuf.getShort();
        if (length < 0 || length > MAX_VAL_LENGTH) return;
        value = new byte[length];
        inBuf.get(value);
    }

    enum RequestType {
        PUT((byte) 0x01),
        GET((byte) 0x02),
        REMOVE((byte) 0x03),
        SHUTDOWN((byte) 0x04),
        DELETE_ALL((byte) 0x05),
        INVALID_REQ_CODE((byte) 0x06);

        private byte index;

        RequestType(byte index) {
            this.index = index;
        }

        public static RequestType getType(byte index) {
            for (RequestType rc : RequestType.values()) {
                if (rc.index == index) {
                    return rc;
                }
            }
            return INVALID_REQ_CODE;
        }

        public byte getByteCode() {
            return this.index;
        }
    }

    //TODO: clarify what these codes mean
    enum ReplyType {
        OP_SUCCESS((byte) 0x00),
        INVALID_KEY((byte) 0x01),
        NO_SPACE((byte) 0x02),
        OVERLOADED((byte) 0x03),
        STORE_FAIL((byte) 0x04),
        UNKNOWN_COM((byte) 0x05),
        BAD_VAL_LEN((byte) 0x06),
        INVALID_REP_CODE((byte) 0x07);

        private byte index;

        ReplyType(byte index) {
            this.index = index;
        }

        public static ReplyType getType(byte index) {
            for (ReplyType rc : ReplyType.values()) {
                if (rc.index == index) {
                    return rc;
                }
            }
            return INVALID_REP_CODE;
        }

        public byte getByteCode() {
            return this.index;
        }
    }
}