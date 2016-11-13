package hackatum.user.logic;

import org.restlet.data.MediaType;
import org.restlet.engine.Engine;
import org.restlet.ext.html.FormData;
import org.restlet.ext.html.FormDataSet;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.ext.json.JsonConverter;
import org.restlet.representation.StreamRepresentation;
import org.restlet.resource.ClientResource;

import java.io.*;

/**
 * //TODO 12.11.2016: add description
 *
 * @author Matthias Kammueller
 */
public class Main {
    public static void main(String[] args) {
        /*try {

            FormData fd = new FormData("username", "test1234567");
            new ClientResource("http://192.168.137.166:9000/getToken/abc").get().getText();
            new ClientResource("http://192.168.137.166:9000/register").post(fd);
        } catch (Exception ex) {
        }*/
        sophisticated();
        System.out.println("yo digga");
    }

    public static void sophisticated() {
        try {
            final InputStream stream = new FileInputStream("src/lotg-gaming.jpg");

            // send them to server
            StreamRepresentation entity = new StreamRepresentation(MediaType.APPLICATION_ALL) {
                @Override
                public InputStream getStream() throws IOException {
                    return stream;
                }

                @Override
                public void write(OutputStream outputStream) throws IOException {
                    throw new RuntimeException("OutputStream is not implemented.");
                }
            };
            FormDataSet fds = new FormDataSet();
            System.out.println("fds defined");
            FormData fd = new FormData("hint", entity);
            System.out.println("FD defined");
            fds.getEntries().add(fd);
            fds.setMultipart(true);
            System.out.println("multipart defined");

//            new ClientResource("http://192.168.137.166:9000/uploadNewHint").post(fds);
            // TODO: 12.11.2016 fails
            new ClientResource("http:///192.168.137.166:9000/uploadNewHint").post(fds);
            System.out.println("uploaded");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
