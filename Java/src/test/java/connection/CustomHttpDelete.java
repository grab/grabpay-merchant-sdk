package connection;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.net.URISyntaxException;

public class CustomHttpDelete extends HttpDelete {

    public CustomHttpDelete(final String uri) {
        super(uri);
    }

    private boolean checkUri(HttpDelete httpDelete) {
        try {
            return this.getUri().equals(httpDelete.getUri());
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private boolean checkHeader(HttpDelete httpDelete) {
        Header[] headers = httpDelete.getHeaders();
        Header[] headers1 = this.getHeaders();
        for (int i = 0; i < headers1.length; i++) {
            boolean ok = false;
            for (int j = 0; j < headers.length; j++) {
                if (headers1[i].getName().equals(headers[j].getName())
                        && headers1[i].getValue().equals(headers[j].getValue())) {
                    ok = true;
                    break;
                }
            }
            if (!ok)
                return false;
        }
        return true;
    }

    private boolean checkBody(HttpDelete httpDelete) throws Exception {
        HttpEntity entity1 = this.getEntity();
        HttpEntity entity2 = httpDelete.getEntity();
        if (entity1 == null && entity2 == null)
            return true;
        if (entity1 == null || entity2 == null)
            return false;

        String response1String = EntityUtils.toString(entity1, "UTF-8");
        String response2String = EntityUtils.toString(entity2, "UTF-8");

        return response1String.equals(response2String);
    }

    @Override
    public boolean equals(Object obj) {
        try {
            HttpDelete httpDelete = (HttpDelete) obj;
            return checkUri(httpDelete) && checkHeader(httpDelete) && checkBody(httpDelete);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
