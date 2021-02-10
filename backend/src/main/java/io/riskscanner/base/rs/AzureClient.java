package io.riskscanner.base.rs;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.PagedList;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.resources.Location;
import com.microsoft.azure.management.resources.Subscription;
import com.microsoft.azure.management.resources.fluentcore.arm.Region;
import org.apache.ibatis.plugin.PluginException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


public class AzureClient {
    private Azure azure;

    private static ConcurrentHashMap<String, Azure> azureConcurrentHashMap = new ConcurrentHashMap<String, Azure>();

    public static List<Map<String, String>> regions = new ArrayList<>();

    public static Map<String, String> map = new HashMap<>();

    public AzureClient() {

    }

    public AzureClient(final AzureCredential azureCredential) throws PluginException {
        if (azureConcurrentHashMap.get(azureCredential.getSubscription()) != null) {
            this.azure = azureConcurrentHashMap.get(azureCredential.getSubscription());
            return;
        }

        try {
            ExecutorService executorService = Executors.newFixedThreadPool(4);
            List<Callable<Azure>> callableList = new ArrayList<Callable<Azure>>();
            callableList.add(new Callable<Azure>() {
                public Azure call() throws Exception {
                    ApplicationTokenCredentials credentials = new ApplicationTokenCredentials(azureCredential.getClient(), azureCredential.getTenant(), azureCredential.getKey(), AzureEnvironment.AZURE_CHINA);
                    Azure azureChina = Azure.authenticate(credentials).withSubscription(azureCredential.getSubscription());
                    azureChina.resourceGroups().list();
                    map.put("regionId", "AzureChinaCloud");
                    map.put("regionName", "中国区");
                    regions.add(map);
                    return azureChina;
                }
            });
            callableList.add(new Callable<Azure>() {
                public Azure call() throws Exception {
                    ApplicationTokenCredentials credentials = new ApplicationTokenCredentials(azureCredential.getClient(), azureCredential.getTenant(), azureCredential.getKey(), AzureEnvironment.AZURE);
                    Azure azureGlobal = Azure.authenticate(credentials).withSubscription(azureCredential.getSubscription());
                    azureGlobal.resourceGroups().list();
                    map.put("regionId", "AzureCloud");
                    map.put("regionName", "国际区");
                    regions.add(map);
                    return azureGlobal;

                }
            });
            callableList.add(new Callable<Azure>() {
                public Azure call() throws Exception {
                    ApplicationTokenCredentials credentials = new ApplicationTokenCredentials(azureCredential.getClient(), azureCredential.getTenant(), azureCredential.getKey(), AzureEnvironment.AZURE_US_GOVERNMENT);
                    Azure azureUS = Azure.authenticate(credentials).withSubscription(azureCredential.getSubscription());
                    azureUS.resourceGroups().list();
                    map.put("regionId", "AzureUSGov");
                    map.put("regionName", "美国区");
                    regions.add(map);
                    return azureUS;

                }
            });
            callableList.add(new Callable<Azure>() {
                public Azure call() throws Exception {
                    ApplicationTokenCredentials credentials = new ApplicationTokenCredentials(azureCredential.getClient(), azureCredential.getTenant(), azureCredential.getKey(), AzureEnvironment.AZURE_GERMANY);
                    Azure azureGermany = Azure.authenticate(credentials).withSubscription(azureCredential.getSubscription());
                    azureGermany.resourceGroups().list();
                    map.put("regionId", "AzureGermanyCloud");
                    map.put("regionName", "德国区");
                    regions.add(map);
                    return azureGermany;

                }
            });
            Exception exception = new Exception("Authentication Failed");
            try {
                List<Future<Azure>> futureList = executorService.invokeAll(callableList, 2, TimeUnit.MINUTES);
                for (int i = 0; i < futureList.size(); i++) {
                    try {
                        Future<Azure> future = futureList.get(i);
                        Azure authenticatedAzure = future.get();
                        if (authenticatedAzure != null) {
                            this.azure = authenticatedAzure;
                        }
                    } catch (Exception e) {
                        exception = e;
                    }
                }
            } finally {
                executorService.shutdown();
            }
            if (this.azure == null) {
                throw exception;
            }
            azureConcurrentHashMap.put(azureCredential.getSubscription(), this.azure);
        } catch (Exception e) {
            throw new PluginException(e);
        }
    }

    //此region 不是chinanorth这种我们诠释的region，而是azure本身的中国区、国际区等概念
    public List<Map<String, String>> getCloudRegions () {
        return regions;
    }

    public List<AzureResourceObject> getRegions() {
        List<AzureResourceObject> result = new ArrayList<AzureResourceObject>();
        PagedList<Location> locationPagedList = azure.getCurrentSubscription().listLocations();
        locationPagedList.loadAll();
        for (Location location : locationPagedList) {
            AzureResourceObject azureRegion = new AzureResourceObject();
            azureRegion.setId(location.inner().name());
            azureRegion.setName(location.inner().displayName());
            result.add(azureRegion);
        }
        return result;
    }

    public Region getRegion(String regionId) {
        PagedList<Location> locationPagedList = azure.getCurrentSubscription().listLocations();
        locationPagedList.loadAll();
        for (Location location : locationPagedList) {
            if (location.region().name().equalsIgnoreCase(regionId)) {
                return location.region();
            }
        }
        return null;
    }

    public Subscription getCurrentSubscription() {
        return azure.subscriptions().getById(azure.subscriptionId());
    }

}
