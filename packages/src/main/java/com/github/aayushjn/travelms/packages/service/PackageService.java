package com.github.aayushjn.travelms.packages.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aayushjn.travelms.common.exceptions.AlreadyExistsException;
import com.github.aayushjn.travelms.common.exceptions.DoesNotExistException;
import com.github.aayushjn.travelms.common.exceptions.MalformedRequestException;
import com.github.aayushjn.travelms.packages.config.TravelMsConfig;
import com.github.aayushjn.travelms.packages.model.Package;
import com.github.aayushjn.travelms.packages.model.PackageType;
import com.github.aayushjn.travelms.packages.model.request.PackageUpdateRequest;
import com.github.aayushjn.travelms.packages.repository.PackageRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
@AllArgsConstructor
public class PackageService {
    private final PackageRepository packageRepo;
    private final ApplicationContext context;
    private final TravelMsConfig travelMsConfig;

    private final ObjectMapper mapper = new ObjectMapper();
    private final TypeReference<List<Map<String, Object>>> listTypeRef = new TypeReference<>() {};

    public int addPackage(Package pkg) throws Throwable {
        Optional<Package> optional = packageRepo.findByName(pkg.getName());
        if (optional.isPresent()) {
            throw new AlreadyExistsException("Package already exists");
        }

        if (pkg.getBusId() == null && (pkg.getPackageType() == PackageType.BUS_ONLY || pkg.getPackageType() == PackageType.HOTEL_AND_BUS)) {
            throw new MalformedRequestException("Bus should be specified");
        }
        if (pkg.getHotelId() == null && (pkg.getPackageType() == PackageType.HOTEL_ONLY || pkg.getPackageType() == PackageType.HOTEL_AND_BUS)) {
            throw new MalformedRequestException("Hotel should be specified");
        }

        HttpClient client = context.getBean(HttpClient.class);
        CompletableFuture<Void> cf;

        if (pkg.getHotelId() != null) {
            cf = client.sendAsync(HttpRequest.newBuilder()
                                    .GET()
                                    .uri(URI.create(travelMsConfig.getHotelsServiceUrl()))
                                    .build(),
                            responseInfo -> HttpResponse.BodySubscribers.ofByteArray())
                    .thenCompose(httpResponse -> {
                        CompletableFuture<Void> future;
                        if (httpResponse.statusCode() == 200) {
                            try {
                                List<Map<String, Object>> list = mapper.readValue(httpResponse.body(), listTypeRef);
                                if (list.stream().noneMatch(map -> ((int) map.get("hotelId")) == pkg.getHotelId())) {
                                    future = CompletableFuture.failedFuture(new DoesNotExistException("Hotel not found"));
                                } else {
                                    future = CompletableFuture.completedFuture(null);
                                }
                            } catch (IOException e) {
                                future = CompletableFuture.failedFuture(new JsonParseException("Failed to read data"));
                            }
                        } else {
                            future = CompletableFuture.failedFuture(new DoesNotExistException("Hotel not found"));
                        }
                        return future;
                    });
            cf.join();
            if (cf.isCompletedExceptionally()) {
                throw cf.exceptionNow();
            }
        }

        if (pkg.getBusId() != null) {
            cf = client.sendAsync(HttpRequest.newBuilder()
                                    .GET()
                                    .uri(URI.create(travelMsConfig.getBusesServiceUrl()))
                                    .build(),
                            responseInfo -> HttpResponse.BodySubscribers.ofByteArray())
                    .thenCompose(httpResponse -> {
                        CompletableFuture<Void> future;
                        if (httpResponse.statusCode() == 200) {
                            try {
                                List<Map<String, Object>> list = mapper.readValue(httpResponse.body(), listTypeRef);
                                if (list.stream().noneMatch(map -> ((int) map.get("busId")) == pkg.getBusId())) {
                                    future = CompletableFuture.failedFuture(new DoesNotExistException("Bus not found"));
                                } else {
                                    future = CompletableFuture.completedFuture(null);
                                }
                            } catch (IOException e) {
                                future = CompletableFuture.failedFuture(new JsonParseException("Failed to read data"));
                            }
                        } else {
                            future = CompletableFuture.failedFuture(new DoesNotExistException("Bus not found"));
                        }
                        return future;
                    });
            cf.join();
            if (cf.isCompletedExceptionally()) {
                throw cf.exceptionNow();
            }
        }

        return packageRepo.save(pkg).getPackageId();
    }

    public void deletePackage(int packageId) throws DoesNotExistException {
        if (packageRepo.existsById(packageId)) {
            packageRepo.deleteById(packageId);
        } else {
            throw new DoesNotExistException("Hotel does not exist");
        }
    }
    public void updatePackage(int packageId, PackageUpdateRequest packageUpdateRequest) throws Throwable {
        Package dbPackage = packageRepo.findById(packageId)
                .orElseThrow(() -> new DoesNotExistException("Hotel does not exist"));

        if (packageUpdateRequest.busId() == null && (packageUpdateRequest.packageType() == PackageType.BUS_ONLY || packageUpdateRequest.packageType() == PackageType.HOTEL_AND_BUS)) {
            throw new MalformedRequestException("Bus should be specified");
        }
        if (packageUpdateRequest.hotelId() == null && (packageUpdateRequest.packageType() == PackageType.HOTEL_ONLY || packageUpdateRequest.packageType() == PackageType.HOTEL_AND_BUS)) {
            throw new MalformedRequestException("Hotel should be specified");
        }

        HttpClient client = context.getBean(HttpClient.class);
        CompletableFuture<Void> cf;

        if (packageUpdateRequest.hotelId() != null) {
            cf = client.sendAsync(HttpRequest.newBuilder()
                                    .GET()
                                    .uri(URI.create(travelMsConfig.getHotelsServiceUrl()))
                                    .build(),
                            responseInfo -> HttpResponse.BodySubscribers.ofByteArray())
                    .thenCompose(httpResponse -> {
                        CompletableFuture<Void> future;
                        if (httpResponse.statusCode() == 200) {
                            try {
                                List<Map<String, Object>> list = mapper.readValue(httpResponse.body(), listTypeRef);
                                if (list.stream().noneMatch(map -> ((int) map.get("hotelId")) == packageUpdateRequest.hotelId())) {
                                    future = CompletableFuture.failedFuture(new DoesNotExistException("Hotel not found"));
                                } else {
                                    future = CompletableFuture.completedFuture(null);
                                }
                            } catch (IOException e) {
                                future = CompletableFuture.failedFuture(new JsonParseException("Failed to read data"));
                            }
                        } else {
                            future = CompletableFuture.failedFuture(new DoesNotExistException("Hotel not found"));
                        }
                        return future;
                    });
            cf.join();
            if (cf.isCompletedExceptionally()) {
                throw cf.exceptionNow();
            }
        }

        if (packageUpdateRequest.busId() != null) {
            cf = client.sendAsync(HttpRequest.newBuilder()
                                    .GET()
                                    .uri(URI.create(travelMsConfig.getBusesServiceUrl()))
                                    .build(),
                            responseInfo -> HttpResponse.BodySubscribers.ofByteArray())
                    .thenCompose(httpResponse -> {
                        CompletableFuture<Void> future;
                        if (httpResponse.statusCode() == 200) {
                            try {
                                List<Map<String, Object>> list = mapper.readValue(httpResponse.body(), listTypeRef);
                                if (list.stream().noneMatch(map -> ((int) map.get("busId")) == packageUpdateRequest.busId())) {
                                    future = CompletableFuture.failedFuture(new DoesNotExistException("Bus not found"));
                                } else {
                                    future = CompletableFuture.completedFuture(null);
                                }
                            } catch (IOException e) {
                                future = CompletableFuture.failedFuture(new JsonParseException("Failed to read data"));
                            }
                        } else {
                            future = CompletableFuture.failedFuture(new DoesNotExistException("Bus not found"));
                        }
                        return future;
                    });
            cf.join();
            if (cf.isCompletedExceptionally()) {
                throw cf.exceptionNow();
            }
        }

        dbPackage.setDescription(packageUpdateRequest.description());
        dbPackage.setCost(packageUpdateRequest.cost());
        dbPackage.setPackageType(packageUpdateRequest.packageType());
        dbPackage.setHotelId(packageUpdateRequest.hotelId());
        dbPackage.setBusId(packageUpdateRequest.busId());
        packageRepo.save(dbPackage);
    }

    public List<Package> getAllPackages() {
        return packageRepo.findAll();
    }
}
