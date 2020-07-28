#!/usr/local/bin/groovy
import javax.net.ssl.HttpsURLConnection

// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the ODPi Egeria project.

// Function to convert array to String

// Will configure a server chassis - which should already be running - for FVT testing

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

// Retrieve configuration - with defaults to aid in local testing (using default ports)
user=properties["user"] ?: "garygeeke";
baseURL=properties["baseURL"] ?: "https://localhost:9443";
server=properties["server"] ?: "server1";
retries=properties["retries"] ?: 12;
delay=properties["delay"] ?: 10;

// SSL setup to avoid self-signed errors for testing
TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }
            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
            {
                //No need to implement.
            }
            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
            {
                //No need to implement.
            }
        }
};
try
{
    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
}
catch (Exception e)
{
    System.out.println(e);
    System.exit(-1);
}

// Wait until the platform is ready
connected=false;
i=retries;
while (!connected && i>0)
{
    try {

        System.out.println("=== Checking platform at " + baseURL + " is available (" + i + " attempts remaining) ===");
        post0 = new java.net.URL(baseURL + "/open-metadata/platform-services/users/" + user + "/server-platform/origin").openConnection();
        post0RC = post0.getResponseCode();
        println(post0RC);
        if (post0RC.equals(200)) {
            connected = true;
            println(post0.getInputStream().getText());
        } else {
            i--;
            Thread.sleep(1000 * delay);
        }
    } catch (Throwable t)
    {
        // TODO: look at whether some exceptions should be deemed irrecoverable rather than retry
        i--;
        Thread.sleep(1000 * delay);
    }
}

// --- Configure the platform - any errors here and we exit
System.out.println("=== Configuring server: " + server + " ===");
post1 = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + server + "/local-repository/mode/in-memory-repository" ).openConnection()
post1.setRequestMethod("POST")
post1.setRequestProperty("Content-Type", "application/json")
postRC1 = post1.getResponseCode();
println(postRC1);
if(postRC1.equals(200)) {
    println(post1.getInputStream().getText());
}

// --- Enable Subject Area OMAS - any errors here and we exit
System.out.println("=== Enabling Subject Area OMAS: " + server + " ===");
post2 = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + server + "/access-services/subject-area" ).openConnection()
post2.setRequestMethod("POST")
post2.setRequestProperty("Content-Type", "application/json")
postRC2 = post2.getResponseCode();
println(postRC2);
if(postRC2.equals(200)) {
    println(post2.getInputStream().getText());
}


// --- Launch the server - any errors here and we exit
System.out.println("=== Starting server: " + server + " ===");
post3 = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + server + "/instance" ).openConnection()
post3.setRequestMethod("POST")
post3.setRequestProperty("Content-Type", "application/json")
postRC3 = post3.getResponseCode();
println(postRC3);
if(postRC3.equals(200)) {
    println(post3.getInputStream().getText());
}

// --- We're done
System.out.println("=== Configuration complete ===")
