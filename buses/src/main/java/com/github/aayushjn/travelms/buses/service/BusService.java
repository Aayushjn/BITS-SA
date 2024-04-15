package com.github.aayushjn.travelms.buses.service;

import com.github.aayushjn.travelms.buses.model.Bus;
import com.github.aayushjn.travelms.buses.model.request.BusUpdateRequest;
import com.github.aayushjn.travelms.buses.repository.BusRepository;
import com.github.aayushjn.travelms.common.exceptions.AlreadyExistsException;
import com.github.aayushjn.travelms.common.exceptions.DoesNotExistException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class BusService {
    private final BusRepository busRepo;

    public int addBus(Bus bus) throws AlreadyExistsException {
        Optional<Bus> optional = busRepo.findByName(bus.getName());
        if (optional.isPresent()) {
            throw new AlreadyExistsException("Hotel already exists");
        }

        return busRepo.save(bus).getBusId();
    }

    public void deleteBus(int busId) throws DoesNotExistException {
        if (busRepo.existsById(busId)) {
            busRepo.deleteById(busId);
        } else {
            throw new DoesNotExistException("Bus does not exist");
        }
    }
    public void updateBus(int busId, BusUpdateRequest busUpdateRequest) throws DoesNotExistException {
        Bus dbBus = busRepo.findById(busId)
                .orElseThrow(() -> new DoesNotExistException("Hotel does not exist"));
        dbBus.setCompany(busUpdateRequest.company());
        dbBus.setCost(busUpdateRequest.cost());
        dbBus.setBusType(busUpdateRequest.busType());
        busRepo.save(dbBus);
    }

    public List<Bus> getAllBuses() {
        return busRepo.findAll();
    }
}
