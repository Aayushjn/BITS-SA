package com.github.aayushjn.travelms.hotels.service;

import com.github.aayushjn.travelms.common.exceptions.AlreadyExistsException;
import com.github.aayushjn.travelms.common.exceptions.DoesNotExistException;
import com.github.aayushjn.travelms.hotels.model.Hotel;
import com.github.aayushjn.travelms.hotels.model.request.HotelUpdateRequest;
import com.github.aayushjn.travelms.hotels.repository.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepo;

    public int addHotel(Hotel hotel) throws AlreadyExistsException {
        Optional<Hotel> optional = hotelRepo.findByName(hotel.getName());
        if (optional.isPresent()) {
            throw new AlreadyExistsException("Hotel already exists");
        }

        return hotelRepo.save(hotel).getHotelId();
    }

    public void deleteHotel(int hotelId) throws DoesNotExistException {
        if (hotelRepo.existsById(hotelId)) {
            hotelRepo.deleteById(hotelId);
        } else {
            throw new DoesNotExistException("Hotel does not exist");
        }
    }
    public void updateHotel(int hotelId, HotelUpdateRequest hotelUpdateRequest) throws DoesNotExistException {
        Hotel dbHotel = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new DoesNotExistException("Hotel does not exist"));
        dbHotel.setDescription(hotelUpdateRequest.description());
        dbHotel.setAddress(hotelUpdateRequest.address());
        dbHotel.setRent(hotelUpdateRequest.rent());
        hotelRepo.save(dbHotel);
    }

    public List<Hotel> getAllHotels() {
        return hotelRepo.findAll();
    }
}
