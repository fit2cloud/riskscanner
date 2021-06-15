package io.riskscanner.proxy.gcp;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.ComputeEngineCredentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;
import java.io.FileInputStream;
import java.io.IOException;

public class GcpClient {
    static void authImplicit() {

        Storage storage = StorageOptions.getDefaultInstance().getService();

        Page<Bucket> buckets = storage.list();
        for (Bucket bucket : buckets.iterateAll()) {
        }
    }

    static void authExplicit(String jsonPath) throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath))
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        Page<Bucket> buckets = storage.list();
        for (Bucket bucket : buckets.iterateAll()) {
            System.out.println(bucket.toString());
        }
    }

    static void authCompute() {
        GoogleCredentials credentials = ComputeEngineCredentials.create();
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        Page<Bucket> buckets = storage.list();
        for (Bucket bucket : buckets.iterateAll()) {
        }
    }

    static void authAppEngineStandard() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        Page<Bucket> buckets = storage.list();
        for (Bucket bucket : buckets.iterateAll()) {
        }
    }
}
