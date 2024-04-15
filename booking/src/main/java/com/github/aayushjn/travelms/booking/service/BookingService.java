package com.github.aayushjn.travelms.booking.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aayushjn.travelms.booking.config.TravelMsConfig;
import com.github.aayushjn.travelms.booking.model.Booking;
import com.github.aayushjn.travelms.booking.model.request.UpdateBookingRequest;
import com.github.aayushjn.travelms.booking.repository.BookingRepository;
import com.github.aayushjn.travelms.common.exceptions.DoesNotExistException;
import com.github.aayushjn.travelms.common.exceptions.UnauthorizedException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.github.aayushjn.travelms.common.Constants.HEADER_AUTH_KEY;

@Service
@Transactional
@AllArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepo;
    private final ApplicationContext context;
    private final TravelMsConfig travelMsConfig;

    private final ObjectMapper mapper = new ObjectMapper();
    private final TypeReference<HashMap<String, Object>> mapTypeRef = new TypeReference<>() {};
    private final TypeReference<List<HashMap<String, Object>>> listTypeRef = new TypeReference<>() {};

    public int makeBooking(String authKey, @RequestBody @Valid Booking booking) throws Throwable {
        HttpClient client = context.getBean(HttpClient.class);

        CompletableFuture<Integer> cf = client.sendAsync(HttpRequest.newBuilder()
                        .GET()
                        .header(HEADER_AUTH_KEY, authKey)
                        .uri(URI.create(travelMsConfig.getUsersServiceUrl()))
                        .build(),
                responseInfo -> HttpResponse.BodySubscribers.ofByteArray())
                .thenCompose(httpResponse -> {
                    CompletableFuture<Integer> future;
                    if (httpResponse.statusCode() == 200) {
                        try {
                            future = CompletableFuture.completedFuture(
                                    Integer.parseInt(
                                            mapper.readValue(httpResponse.body(), mapTypeRef)
                                                    .get("userId").toString()
                                    )
                            );
                        } catch (IOException e) {
                            future = CompletableFuture.failedFuture(new JsonParseException("Failed to parse data"));
                        }
                    } else {
                        future = CompletableFuture.failedFuture(new DoesNotExistException("User not found"));
                    }
                    return future;
                });
        int userId = cf.join();
        if (cf.isCompletedExceptionally()) {
            throw cf.exceptionNow();
        }

        cf = client.sendAsync(HttpRequest.newBuilder()
                                .GET()
                                .uri(URI.create(travelMsConfig.getPackagesServiceUrl()))
                                .build(),
                        responseInfo -> HttpResponse.BodySubscribers.ofByteArray())
                .thenCompose(httpResponse -> {
                    CompletableFuture<Integer> future;
                    if (httpResponse.statusCode() == 200) {
                        try {
                            future = CompletableFuture.completedFuture(
                                    Integer.parseInt(
                                            mapper.readValue(httpResponse.body(), listTypeRef)
                                                    .stream()
                                                    .filter(map -> map.get("packageId") == booking.getPackageId())
                                                    .findFirst()
                                                    .orElseThrow(() -> new DoesNotExistException("Package not found"))
                                                    .get("packageId")
                                                    .toString()
                                    )
                            );
                        } catch (IOException e) {
                            future = CompletableFuture.failedFuture(new JsonParseException("Failed to parse data"));
                        } catch (DoesNotExistException e) {
                            future = CompletableFuture.failedFuture(e);
                        }
                    } else {
                        future = CompletableFuture.failedFuture(new DoesNotExistException("Package not found"));
                    }
                    return future;
                });
        int packageId = cf.join();
        if (cf.isCompletedExceptionally()) {
            throw cf.exceptionNow();
        }

        booking.setUserId(userId);
        booking.setPackageId(packageId);
        return bookingRepo.save(booking).getBookingId();
    }

    public void cancelBooking(String authKey, int bookingId) throws Throwable {
        HttpClient client = context.getBean(HttpClient.class);

        CompletableFuture<Integer> cf = client.sendAsync(HttpRequest.newBuilder()
                                .GET()
                                .header(HEADER_AUTH_KEY, authKey)
                                .uri(URI.create(travelMsConfig.getUsersServiceUrl()))
                                .build(),
                        responseInfo -> HttpResponse.BodySubscribers.ofByteArray())
                .thenCompose(httpResponse -> {
                    CompletableFuture<Integer> future;
                    if (httpResponse.statusCode() == 200) {
                        try {
                            future = CompletableFuture.completedFuture(
                                    Integer.parseInt(
                                            mapper.readValue(httpResponse.body(), mapTypeRef)
                                                    .get("userId").toString()
                                    )
                            );
                        } catch (IOException e) {
                            future = CompletableFuture.failedFuture(new JsonParseException("Failed to parse data"));
                        }
                    } else {
                        future = CompletableFuture.failedFuture(new DoesNotExistException("User not found"));
                    }
                    return future;
                });
        int userId = cf.join();
        if (cf.isCompletedExceptionally()) {
            throw cf.exceptionNow();
        }

        Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new DoesNotExistException("Booking not found"));
        if (booking.getUserId() != userId) {
            throw new UnauthorizedException();
        }
        bookingRepo.deleteById(bookingId);
    }

    public Booking getBooking(String authKey, int bookingId) throws Throwable {
        HttpClient client = context.getBean(HttpClient.class);

        CompletableFuture<Integer> cf = client.sendAsync(HttpRequest.newBuilder()
                                .GET()
                                .header(HEADER_AUTH_KEY, authKey)
                                .uri(URI.create(travelMsConfig.getUsersServiceUrl()))
                                .build(),
                        responseInfo -> HttpResponse.BodySubscribers.ofByteArray())
                .thenCompose(httpResponse -> {
                    CompletableFuture<Integer> future;
                    if (httpResponse.statusCode() == 200) {
                        try {
                            future = CompletableFuture.completedFuture(
                                    Integer.parseInt(
                                            mapper.readValue(httpResponse.body(), mapTypeRef)
                                                    .get("userId").toString()
                                    )
                            );
                        } catch (IOException e) {
                            future = CompletableFuture.failedFuture(new JsonParseException("Failed to parse data"));
                        }
                    } else {
                        future = CompletableFuture.failedFuture(new DoesNotExistException("User not found"));
                    }
                    return future;
                });
        int userId = cf.join();
        if (cf.isCompletedExceptionally()) {
            throw cf.exceptionNow();
        }

        Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new DoesNotExistException("Booking not found"));
        if (booking.getUserId() != userId) {
            throw new UnauthorizedException();
        }
        return booking;
    }

    public List<Booking> getAllBookings(String authKey) throws Throwable {
        HttpClient client = context.getBean(HttpClient.class);

        CompletableFuture<Integer> cf = client.sendAsync(HttpRequest.newBuilder()
                                .GET()
                                .header(HEADER_AUTH_KEY, authKey)
                                .uri(URI.create(travelMsConfig.getUsersServiceUrl()))
                                .build(),
                        responseInfo -> HttpResponse.BodySubscribers.ofByteArray())
                .thenCompose(httpResponse -> {
                    CompletableFuture<Integer> future;
                    if (httpResponse.statusCode() == 200) {
                        try {
                            future = CompletableFuture.completedFuture(
                                    Integer.parseInt(
                                            mapper.readValue(httpResponse.body(), mapTypeRef)
                                                    .get("userId").toString()
                                    )
                            );
                        } catch (IOException e) {
                            future = CompletableFuture.failedFuture(new JsonParseException("Failed to parse data"));
                        }
                    } else {
                        future = CompletableFuture.failedFuture(new DoesNotExistException("User not found"));
                    }
                    return future;
                });
        int userId = cf.join();
        if (cf.isCompletedExceptionally()) {
            throw cf.exceptionNow();
        }

        return bookingRepo.getAllBookingsForUserId(userId);
    }

    public void updateBooking(String authKey, int bookingId, UpdateBookingRequest updateBookingRequest) throws Throwable {
        HttpClient client = context.getBean(HttpClient.class);

        CompletableFuture<Integer> cf = client.sendAsync(HttpRequest.newBuilder()
                                .GET()
                                .header(HEADER_AUTH_KEY, authKey)
                                .uri(URI.create(travelMsConfig.getUsersServiceUrl()))
                                .build(),
                        responseInfo -> HttpResponse.BodySubscribers.ofByteArray())
                .thenCompose(httpResponse -> {
                    CompletableFuture<Integer> future;
                    if (httpResponse.statusCode() == 200) {
                        try {
                            future = CompletableFuture.completedFuture(
                                    Integer.parseInt(
                                            mapper.readValue(httpResponse.body(), mapTypeRef)
                                                    .get("userId").toString()
                                    )
                            );
                        } catch (IOException e) {
                            future = CompletableFuture.failedFuture(new JsonParseException("Failed to parse data"));
                        }
                    } else {
                        future = CompletableFuture.failedFuture(new DoesNotExistException("User not found"));
                    }
                    return future;
                });
        int userId = cf.join();
        if (cf.isCompletedExceptionally()) {
            throw cf.exceptionNow();
        }

        Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new DoesNotExistException("Booking not found"));
        if (booking.getUserId() != userId) {
            throw new UnauthorizedException();
        }

        booking.setBookingStatus(updateBookingRequest.bookingStatus());
        bookingRepo.save(booking);
    }
}
