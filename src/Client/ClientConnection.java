
package Client;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import shared.RequestResponse.Request;

public class ClientConnection {
    //quản lý tập trung cho in và out để toàn bộ quá tình chỉ sử dụng đúng thứ này
    private static ObjectInputStream in;
    private static ObjectOutputStream out;

    // Sử dụng synchronized để đảm bảo chỉ 1 luồng được gửi/nhận tại một thời điểm
    public static synchronized Object sendRequest(Request request) throws Exception {
        out.writeObject(request);
        out.flush();
        return in.readObject(); // Chờ và nhận về Response
    }


    public static ObjectInputStream getIn() {
        return in;
    }

    public static void setIn(ObjectInputStream in) {
        ClientConnection.in = in;
    }

    public static ObjectOutputStream getOut() {
        return out;
    }

    public static void setOut(ObjectOutputStream out) {
        ClientConnection.out = out;
    }
    
}