package HUtils;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.util.Base64;

import java.io.IOException;

/**
 * Created by root on 15-5-31.
 */
public class Common {

    public static Scan convertStringToScan(String base64) throws IOException {
        byte[] decoded = Base64.decode(base64);

        org.apache.hadoop.hbase.protobuf.generated.ClientProtos.Scan scan;
        try {
            scan = org.apache.hadoop.hbase.protobuf.generated.ClientProtos.Scan.parseFrom(decoded);
        } catch (InvalidProtocolBufferException var4) {
            throw new IOException(var4);
        }

        return ProtobufUtil.toScan(scan);
    }

    public static String convertScanToString(Scan scan) throws IOException {
        org.apache.hadoop.hbase.protobuf.generated.ClientProtos.Scan proto = ProtobufUtil.toScan(scan);
        return Base64.encodeBytes(proto.toByteArray());
    }
}
