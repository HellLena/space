package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import com.space.validator.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static com.space.specification.ShipSpecification.*;

@Validated
@RestController
@RequestMapping(value = "/rest")
public class ShipController {

    @Autowired
    ShipService shipService;

    @GetMapping(value = "/ships")
    public List<Ship> getAll(
            @RequestParam(value="name", required=false, defaultValue = "_") String name,
            @RequestParam(value="planet", required=false) String planet,
            @RequestParam(value="shipType", required=false) ShipType shipType,
            @RequestParam(value="after", required=false) Long after,
            @RequestParam(value="before", required=false) Long before,
            @RequestParam(value="isUsed", required=false) Boolean isUsed,
            @RequestParam(value="minSpeed", required=false) Double minSpeed,
            @RequestParam(value="maxSpeed", required=false) Double maxSpeed,
            @RequestParam(value="minCrewSize", required=false) Integer minCrewSize,
            @RequestParam(value="maxCrewSize", required=false) Integer maxCrewSize,
            @RequestParam(value="minRating", required=false) Double minRating,
            @RequestParam(value="maxRating", required=false) Double maxRating,
            @RequestParam(value="order", required=false, defaultValue = "id") String order,
            @RequestParam(value="pageNumber", required=false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value="pageSize", required=false, defaultValue = "3") Integer pageSize
    ) {
        try {
            order = ShipOrder.valueOf(order.toUpperCase()).getFieldName();
        } catch (Exception e) {
            order = "id";
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order).ascending());
        Specification specification = getSpecification(name,planet,shipType,after,before,isUsed,minSpeed,maxSpeed,minCrewSize,maxCrewSize,minRating,maxRating);

        return shipService.getAll(specification,pageable).getContent();
    }

    @GetMapping(value = "/ships/count")
    public Long getCount(
            @RequestParam(value="name", required=false, defaultValue = "_") String name,
            @RequestParam(value="planet", required=false) String planet,
            @RequestParam(value="shipType", required=false) ShipType shipType,
            @RequestParam(value="after", required=false) Long after,
            @RequestParam(value="before", required=false) Long before,
            @RequestParam(value="isUsed", required=false) Boolean isUsed,
            @RequestParam(value="minSpeed", required=false) Double minSpeed,
            @RequestParam(value="maxSpeed", required=false) Double maxSpeed,
            @RequestParam(value="minCrewSize", required=false) Integer minCrewSize,
            @RequestParam(value="maxCrewSize", required=false) Integer maxCrewSize,
            @RequestParam(value="minRating", required=false) Double minRating,
            @RequestParam(value="maxRating", required=false) Double maxRating
    ) {

        Specification specification = getSpecification(name,planet,shipType,after,before,isUsed,minSpeed,maxSpeed,minCrewSize,maxCrewSize,minRating,maxRating);

        return shipService.getCount(specification);
    }

    @PostMapping(value = "/ships")
    public ResponseEntity<Ship> createShip(@Validated(Ship.Create.class) @RequestBody Ship ship, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        shipService.save(ship);
        return new ResponseEntity<>(ship,HttpStatus.OK);
    }

    @GetMapping(value = "/ships/{id}")
    public @ResponseBody Ship getShip(@PathVariable Long id) {
        if(id < 1)
            throw new BadRequestException();

        return shipService.getById(id).get();
    }

    @PostMapping(value = "/ships/{id}")
    public Ship updateShip(@PathVariable Long id, @Validated(Ship.Update.class) @RequestBody Ship newShip) {

        if(id < 1)
            throw new BadRequestException();

        if(!shipService.existsById(id))
            throw new NoSuchElementException();

        Ship existing = shipService.getById(id).get();

        if(newShip.getName() != null)
            existing.setName(newShip.getName());
        if(newShip.getPlanet() != null)
            existing.setPlanet(newShip.getPlanet());
        if(newShip.getShipType() != null)
            existing.setShipType(newShip.getShipType().name());
        if(newShip.getProdDate() != null)
            existing.setProdDate(newShip.getProdDate());
        if(newShip.getUsed() != null)
            existing.setUsed(newShip.getUsed());
        if(newShip.getSpeed() != null)
            existing.setSpeed(newShip.getSpeed());
        if(newShip.getCrewSize() != null)
            existing.setCrewSize(newShip.getCrewSize());

        shipService.save(existing);
        return existing;
    }

    @DeleteMapping(value = "/ships/{id}")
    public HttpStatus deleteShip(@PathVariable("id") Long id) {
        if(id < 1)
            throw new BadRequestException();

        if(!shipService.existsById(id))
            throw new NoSuchElementException();

        try {
            shipService.deleteById(id);
            return HttpStatus.OK;
        } catch (Exception e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    private Specification<Ship> getSpecification(String name,String planet,ShipType shipType,Long after,Long before,Boolean isUsed,Double minSpeed,Double maxSpeed,Integer minCrewSize,Integer maxCrewSize,Double minRating,Double maxRating) {
        Specification<Ship> specification = Specification
                .where(paramContains(name,"name"));

        if(planet != null)
            specification = specification.and(paramContains(planet,"planet"));

        if(shipType != null)
            specification = specification.and(paramEquals(shipType.toString(),"shipType"));

        if(after != null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(after));
            specification = specification.and(dateGE(cal.getTime(),"prodDate"));
        }

        if(before != null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(before));
            specification = specification.and(dateLE(cal.getTime(),"prodDate"));
        }

        if(isUsed != null)
            specification = specification.and(paramEquals(isUsed ? 1 : 0,"isUsed"));

        if(minSpeed != null)
            specification = specification.and(paramGE(minSpeed,"speed"));

        if(maxSpeed != null)
            specification = specification.and(paramLE(maxSpeed,"speed"));

        if(minCrewSize != null)
            specification = specification.and(paramGE(minCrewSize,"crewSize"));

        if(maxCrewSize != null)
            specification = specification.and(paramLE(maxCrewSize,"crewSize"));

        if(minRating != null)
            specification = specification.and(paramGE(minRating,"rating"));

        if(maxRating != null)
            specification = specification.and(paramLE(maxRating,"rating"));

        return specification;
    }

}
