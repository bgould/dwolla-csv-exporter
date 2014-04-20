package org.bgould.dwolla.exporter;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TransactionList {
    
    public static final String DWOLLA_ENDPOINT      = "https://www.dwolla.com/oauth/rest";
    
    public static final String SANDBOX_ENDPOINT     = "https://uat.dwolla.com/oauth/rest";
    
    public static final String DATE_FORMAT_STRING   = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    
    private final String apiEndpoint;
    
    private final String oauthToken;
    
    private Date sinceDate;
    
    private Date endDate;
    
    private int skip = 0;
    
    private int limit = 10;
    
    private String groupId;
    
    private final Set<TransactionType> types = new HashSet<TransactionType>();
    
    public Date getSinceDate() {
        return sinceDate;
    }

    public void setSinceDate(Date sinceDate) {
        this.sinceDate = sinceDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Set<TransactionType> getTypes() {
        return types;
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public TransactionList(final String _oauthToken) {
        this(DWOLLA_ENDPOINT, _oauthToken);
    }
    
    public TransactionList(final String _endpoint, String _oauthToken) {
        if ( _oauthToken == null || "".equals(_oauthToken.trim()) ) {
            throw new IllegalArgumentException("OAuth token cannot be blank.");
        }
        if ( _endpoint == null || "".equals(_endpoint.trim()) ) {
            throw new IllegalArgumentException("Endpoint URL cannot be blank.");
        }
        this.oauthToken = _oauthToken;
        this.apiEndpoint = _endpoint;
    }
    
    public List<Transaction> request() throws IOException, JSONException {
        
        final String urlStr = buildUrlString();
        
        final String responseBody = httpGet(urlStr);
        
        final JSONObject jsonObj = new JSONObject(responseBody);
        boolean success = jsonObj.getBoolean("Success");
        if (!success) {
            throw new RuntimeException(responseBody);
        }
        
        final DateFormat df = new SimpleDateFormat(DATE_FORMAT_STRING);
        final JSONArray transactions = jsonObj.getJSONArray("Response");
        List<Transaction> results = new ArrayList<Transaction>(transactions.length());
        for (int i = 0, c = transactions.length(); i < c; i++) {
            final JSONObject jsonTxn = transactions.getJSONObject(i);
            final Transaction transaction = new Transaction();
            transaction.setId(              parseJsonString(    jsonTxn, "Id")                  );
            transaction.setUserType(        parseJsonString(    jsonTxn, "UserType")            );
            transaction.setDestinationId(   parseJsonString(    jsonTxn, "DestinationId")       );
            transaction.setDestinationName( parseJsonString(    jsonTxn, "DestinationName")     );
            transaction.setSourceId(        parseJsonString(    jsonTxn, "SourceId")            );
            transaction.setSourceName(      parseJsonString(    jsonTxn, "SourceName")          );
            transaction.setNotes(           parseJsonString(    jsonTxn, "Notes")               );
            transaction.setDate(              parseJsonDate(    jsonTxn, "Date", df)            );
            transaction.setClearingDate(      parseJsonDate(    jsonTxn, "ClearingDate", df)    );
            transaction.setAmount(          parseBigDecimal(    jsonTxn, "Amount", 2)           );
            transaction.setType(TransactionType.valueOf(jsonTxn.getString("Type").toUpperCase())    );
            transaction.setStatus(Status.valueOf(jsonTxn.getString("Status").toUpperCase()));
            transaction.setOriginalTransactionId(parseJsonString(jsonTxn, "originalTransactionId"));
            results.add(transaction);
        }
        return results;
        
    }
    
    public static BigDecimal parseBigDecimal(JSONObject jsonObj, String key, int precision) {
        String strVal = parseJsonString(jsonObj, key);
        if (strVal != null) {
            return new BigDecimal(strVal).setScale(precision, BigDecimal.ROUND_HALF_UP);
        } else {
            return null;
        }
    }
    
    public static String parseJsonString(JSONObject jsonObj, String key) {
        if (!jsonObj.has(key)) {
            return null;
        }
        if (jsonObj.isNull(key)) {
            return null;
        }
        Object val = jsonObj.get(key);
        if (val != null) {
            return val.toString();
        } else {
            return null;
        }
    }
    
    public static Date parseJsonDate(JSONObject jsonObj, String key, DateFormat df) {
        if (!jsonObj.has(key)) {
            return null;
        }
        if (jsonObj.isNull(key)) {
            return null;
        }
        final String strVal = jsonObj.getString(key);
        if (strVal.trim().equals("")) {
            return null;
        }
        try {
            return df.parse(strVal);
        } catch (ParseException e) {
            throw new JSONException(e);
        }
    }
    
    protected String buildUrlString() {
        StringBuilder sb = new StringBuilder();
        sb.append(apiEndpoint)
            .append("/transactions/?oauth_token=")
            .append(urlencode(oauthToken));
        if (sinceDate != null) {
            sb.append("&sinceDate=").append(urlencode(
                new SimpleDateFormat(DATE_FORMAT_STRING).format(sinceDate)));
        }
        if (endDate != null) {
            sb.append("&endDate=").append(urlencode(
                new SimpleDateFormat(DATE_FORMAT_STRING).format(endDate)));
        }
        if (skip > 0) {
            sb.append("&skip=").append(skip);
        }
        if (limit > 0) {
            sb.append("&limit=").append(limit);
        }
        if (groupId != null) {
            sb.append("&groupId=").append(urlencode(groupId));
        }
        if (!types.isEmpty()) {
            StringBuilder typeStr = new StringBuilder();
            for (Iterator<TransactionType> i = types.iterator(); i.hasNext(); ) {
                TransactionType type = i.next();
                typeStr.append(type.name().toLowerCase());
                if (i.hasNext()) {
                    typeStr.append(",");
                }
            }
            sb.append("&types=").append(urlencode(typeStr.toString()));
        }
        return sb.toString();
    }
    
    public static String httpGet(String urlStr) throws IOException {
        final URL url = new URL(urlStr);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            connection.connect();
            final int response = connection.getResponseCode();
            if (response == 200) {
                final InputStream in = connection.getInputStream();
                final StringBuilder sb = new StringBuilder();
                final byte[] buffer = new byte[4096];
                for (int n; (n = in.read(buffer)) > -1; ) {
                    sb.append(new String(buffer, 0, n));
                }
                return sb.toString();
            } else {
                throw new IOException(
                    "HTTP request failed with response => " + response + " " + 
                    connection.getResponseMessage()
                );
            }
        } finally {
            try { connection.disconnect(); } catch (Exception e) {}
        }
    }

    public static String urlencode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
