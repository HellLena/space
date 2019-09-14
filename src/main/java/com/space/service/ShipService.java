package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

@Service
public class ShipService {

    @Autowired
    ShipRepository<Ship> shipRepository;

    @Transactional
    public Page<Ship> getAll(Specification specification, Pageable pageable) {
        return shipRepository.findAll(specification, pageable);
    }

    @Transactional
    public Long getCount(Specification specification) {
        return shipRepository.count(specification);
    }

    @Transactional
    public Optional<Ship> getById(Long id) {
        return shipRepository.findById(id);
    }

    @Transactional
    public Ship save(Ship ship) {
        if(ship.getUsed() == null)
            ship.setUsed(false);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(ship.getProdDate());
        int year = calendar.get(Calendar.YEAR);

        Double rating = (80*ship.getSpeed()*(ship.getUsed() ? 0.5 : 1.0))/(3019 - year + 1);
        BigDecimal bd = new BigDecimal(rating).setScale(2,RoundingMode.HALF_UP);
        ship.setRating(bd.doubleValue());

        return shipRepository.save(ship);
    }

    @Transactional
    public void deleteById(Long id) {
        shipRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return shipRepository.existsById(id);
    }
}
