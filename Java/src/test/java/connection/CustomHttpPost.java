package connection;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.net.URISyntaxException;

public class CustomHttpPost extends HttpPost {

    public CustomHttpPost(final String uri) {
        super(uri);
    }

    private boolean checkUri(HttpPost httpPost) {
        try {
            return this.getUri().equals(httpPost.getUri());
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private boolean checkHeader(HttpPost httpPost) {
        Header[] headers = httpPost.getHeaders();
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

    private boolean checkBody(HttpPost httpPost) throws Exception {
        HttpEntity entity1 = this.getEntity();
        HttpEntity entity2 = httpPost.getEntity();
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
            HttpPost tmpPost = (HttpPost) obj;
            return checkUri(tmpPost) && checkHeader(tmpPost) && checkBody(tmpPost);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
