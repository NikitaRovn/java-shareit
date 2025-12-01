package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    public BookingClient(@Value("${shareit-server.url}") String serverUrl,
                         RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public ResponseEntity<Object> addBooking(long userId, BookingRegisterDto dto) {
        return post("", userId, dto);
    }

    public ResponseEntity<Object> changeStatus(long ownerId, long bookingId, boolean approved) {
        return patch("/{id}?approved={approved}", ownerId,
                Map.of("id", bookingId, "approved", approved), null);
    }

    public ResponseEntity<Object> getBooking(long userId, long bookingId) {
        return get("/{id}", userId, Map.of("id", bookingId));
    }

    public ResponseEntity<Object> getUserBookings(long userId, String state) {
        return get("?state={state}", userId, Map.of("state", state));
    }

    public ResponseEntity<Object> getOwnerBookings(long ownerId, String state) {
        return get("/owner?state={state}", ownerId, Map.of("state", state));
    }
}